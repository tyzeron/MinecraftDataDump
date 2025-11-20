package com.tyzeron.datadump;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.tyzeron.datadump.abstraction.nbt.NbtCompound;
import com.tyzeron.datadump.abstraction.nbt.NbtWriter;
import com.tyzeron.datadump.abstraction.registry.RegistryDataProvider;
import com.tyzeron.datadump.abstraction.registry.RegistryEntryInfo;
import com.tyzeron.datadump.abstraction.registry.RegistryInfo;
import com.tyzeron.datadump.builder.DataStructureBuilder;
import com.tyzeron.datadump.builder.JsonDataBuilder;
import com.tyzeron.datadump.builder.NbtDataBuilder;
import com.tyzeron.datadump.config.ProfileConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class RegistryDataDump {

    private static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();
    private static final Gson GSON_MINIFIED = new Gson();

    /**
     * Generates a registry data dump according to the specified profile configuration
     */
    public static void generateDump(File outputFile, ProfileConfig profile) throws IOException {
        String format = profile.getExport().getFormat().toLowerCase();

        // Check if format is binary and reject it
        if ("binary".equals(format)) {
            throw new IOException("Binary format is not implemented yet");
        }

        // Get platform-specific registry data provider
        RegistryDataProvider provider = PlatformHelper.getRegistryDataProvider();
        DataDump.LOGGER.info("Fetching registries from provider...");
        Collection<RegistryInfo> registries = provider.getAllRegistries();
        DataDump.LOGGER.info("Retrieved {} registries", registries.size());

        if (registries.isEmpty()) {
            DataDump.LOGGER.warn("No registries found! Check if server is initialized.");
        }

        // Create parent directory if needed
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        if ("json".equals(format)) {
            generateJsonDump(outputFile, profile, registries);
        } else if ("nbt".equals(format)) {
            generateNbtDump(outputFile, profile, registries);
        } else {
            throw new IOException("Unknown format: " + format);
        }
    }

    /**
     * Common method to build registry data structure using any builder implementation
     */
    private static Object buildRegistryData(Collection<RegistryInfo> registries, ProfileConfig profile, DataStructureBuilder builder) {
        Object root = builder.createObject();

        boolean includeCodec = profile.getRegistries() != null && profile.getRegistries().isCodec();
        DataDump.LOGGER.info("Building registry data, codec enabled: {}", includeCodec);

        int totalEntries = 0;
        int entriesWithCodec = 0;
        int registryCount = 0;

        // Sort registries by identifier for consistent output
        List<RegistryInfo> sortedRegistries = new ArrayList<>(registries);
        sortedRegistries.sort((a, b) -> a.getRegistryIdentifier().compareTo(b.getRegistryIdentifier()));

        // Iterate through all registries in sorted order
        for (RegistryInfo registryInfo : sortedRegistries) {
            Object registryData = builder.createObject();

            // Add registry type/name
            builder.addStringProperty(registryData, "type", registryInfo.getRegistryIdentifier());

            // Create entries array
            Object entriesArray = builder.createArray();

            for (RegistryEntryInfo entry : registryInfo.getEntries()) {
                Object entryObject = builder.createObject();

                // Add entry name/identifier
                builder.addStringProperty(entryObject, "name", entry.getIdentifier());

                // Add raw/protocol ID
                builder.addIntProperty(entryObject, "id", entry.getRawId());

                // Add encoded data if available and profile includes codec
                if (includeCodec && entry.hasEncodedData()) {
                    Map<String, Object> encodedData = entry.getEncodedData();
                    if (encodedData != null && !encodedData.isEmpty()) {
                        Object elementObject = buildNestedData(encodedData, builder);
                        builder.addToObject(entryObject, "element", elementObject);
                        entriesWithCodec++;
                    }
                }

                builder.addToArray(entriesArray, entryObject);
                totalEntries++;
            }

            builder.addToObject(registryData, "value", entriesArray);

            // Add this registry directly to root (no intermediate storage)
            builder.addToObject(root, registryInfo.getRegistryIdentifier(), registryData);
            registryCount++;
        }

        DataDump.LOGGER.info("Built {} registries with {} total entries ({} with codec data)", 
            registryCount, totalEntries, entriesWithCodec);

        return root;
    }

    /**
     * Recursively builds nested data structures from a Map
     */
    @SuppressWarnings("unchecked")
    private static Object buildNestedData(Object data, DataStructureBuilder builder) {
        if (data instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) data;
            Object obj = builder.createObject();

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                addValueToObject(obj, entry.getKey(), entry.getValue(), builder);
            }

            return obj;
        } else if (data instanceof Collection<?> collection) {
            Object array = builder.createArray();

            for (Object item : collection) {
                addValueToArray(array, item, builder);
            }

            return array;
        } else {
            // For primitive types at the root level, we can't directly add them
            // They should always be part of an object or array
            // This shouldn't happen in normal usage
            return data;
        }
    }

    /**
     * Adds a value to an object, handling different types appropriately
     */
    private static void addValueToObject(Object parent, String key, Object value, DataStructureBuilder builder) {
        switch (value) {
            case null -> builder.addStringProperty(parent, key, "null");
            case String s -> builder.addStringProperty(parent, key, s);
            case Integer i -> builder.addIntProperty(parent, key, i);
            case Boolean b -> builder.addBooleanProperty(parent, key, b);
            case Map map -> {
                Object nestedObj = buildNestedData(value, builder);
                builder.addToObject(parent, key, nestedObj);
            }
            case Collection collection -> {
                Object nestedArray = buildNestedData(value, builder);
                builder.addToObject(parent, key, nestedArray);
            }
            case Long l ->
                // Handle long as int (may lose precision but keeps as number)
                    builder.addIntProperty(parent, key, l.intValue());
            case Byte b -> builder.addIntProperty(parent, key, b.intValue());
            case Short i -> builder.addIntProperty(parent, key, i.intValue());
            case Float v ->
                // For floating point, convert to string to preserve precision
                    builder.addStringProperty(parent, key, value.toString());
            case Double aDouble -> builder.addStringProperty(parent, key, value.toString());
            case Number number ->
                // Handle any other number types
                    builder.addIntProperty(parent, key, number.intValue());
            default ->
                // For any other type, convert to string
                    builder.addStringProperty(parent, key, value.toString());
        }
    }

    /**
     * Adds a value to an array, handling different types appropriately
     */
    private static void addValueToArray(Object array, Object value, DataStructureBuilder builder) {
        if (value == null) {
            builder.addStringToArray(array, "null");
        } else if (value instanceof String) {
            builder.addStringToArray(array, (String) value);
        } else if (value instanceof Map || value instanceof Collection) {
            Object nestedValue = buildNestedData(value, builder);
            builder.addToArray(array, nestedValue);
        } else {
            // For primitives in arrays, convert to string
            builder.addStringToArray(array, value.toString());
        }
    }

    /**
     * Generates a JSON dump
     */
    private static void generateJsonDump(File outputFile, ProfileConfig profile, Collection<RegistryInfo> registries) throws IOException {
        JsonDataBuilder builder = new JsonDataBuilder();
        JsonObject root = (JsonObject) buildRegistryData(registries, profile, builder);

        // Choose GSON instance based on format
        boolean pretty = profile.getExport().getJson() != null && profile.getExport().getJson().isPretty();
        Gson gson = pretty ? GSON_PRETTY : GSON_MINIFIED;

        try (FileWriter writer = new FileWriter(outputFile)) {
            gson.toJson(root, writer);
        }
        DataDump.LOGGER.info("Successfully dumped registry data to: {}", outputFile.getAbsolutePath());
    }

    /**
     * Generates an NBT dump
     */
    private static void generateNbtDump(File outputFile, ProfileConfig profile, Collection<RegistryInfo> registries) throws IOException {
        // Get platform-specific NBT writer and create root compound
        NbtWriter nbtWriter = PlatformHelper.getNbtWriter();
        NbtCompound root = nbtWriter.createCompound();
        NbtDataBuilder builder = new NbtDataBuilder(root);
        buildRegistryData(registries, profile, builder);

        // Write NBT to file
        boolean compressed = profile.getExport().getNbt() != null && profile.getExport().getNbt().isCompressed();
        nbtWriter.writeNbt(root, outputFile, compressed);

        DataDump.LOGGER.info("Successfully dumped registry data to: {} (NBT, compressed: {})", 
            outputFile.getAbsolutePath(), compressed);
    }

}
