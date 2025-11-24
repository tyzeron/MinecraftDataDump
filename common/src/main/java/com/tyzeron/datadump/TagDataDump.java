package com.tyzeron.datadump;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.tyzeron.datadump.abstraction.nbt.NbtCompound;
import com.tyzeron.datadump.abstraction.nbt.NbtWriter;
import com.tyzeron.datadump.abstraction.tag.TagDataProvider;
import com.tyzeron.datadump.abstraction.tag.TagEntryInfo;
import com.tyzeron.datadump.abstraction.tag.TagInfo;
import com.tyzeron.datadump.builder.DataStructureBuilder;
import com.tyzeron.datadump.builder.JsonDataBuilder;
import com.tyzeron.datadump.builder.NbtDataBuilder;
import com.tyzeron.datadump.config.ProfileConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class TagDataDump {

    private static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();
    private static final Gson GSON_MINIFIED = new Gson();

    /**
     * Generates a tag data dump according to the specified profile configuration
     */
    public static void generateDump(File outputFile, ProfileConfig profile) throws IOException {
        String format = profile.getExport().getFormat().toLowerCase();

        // Check if format is binary and reject it
        if ("binary".equals(format)) {
            throw new IOException("Binary format is not implemented yet");
        }

        // Get platform-specific tag data provider
        TagDataProvider provider = PlatformHelper.getTagDataProvider();
        Collection<TagInfo> tags = provider.getAllTags();

        if (tags.isEmpty()) {
            DataDump.LOGGER.warn("No tags found! Check if server is initialized.");
        }

        // Create parent directory if needed
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        if ("json".equals(format)) {
            generateJsonDump(outputFile, profile, tags);
        } else if ("nbt".equals(format)) {
            generateNbtDump(outputFile, profile, tags);
        } else {
            throw new IOException("Unknown format: " + format);
        }
    }

    /**
     * Common method to build tag data structure using any builder implementation
     */
    public static Object buildTagData(Collection<TagInfo> tags, ProfileConfig profile, DataStructureBuilder builder) {
        Object root = builder.createObject();

        // Group tags by registry type
        Map<String, List<TagInfo>> tagsByRegistry = new TreeMap<>();
        for (TagInfo tag : tags) {
            tagsByRegistry.computeIfAbsent(tag.getRegistryType(), k -> new ArrayList<>()).add(tag);
        }

        // Iterate through each registry type in sorted order
        for (Map.Entry<String, List<TagInfo>> registryEntry : tagsByRegistry.entrySet()) {
            String registryType = registryEntry.getKey();
            List<TagInfo> registryTags = registryEntry.getValue();

            // Sort tags by identifier for consistent output
            registryTags.sort(Comparator.comparing(TagInfo::getTagIdentifier));

            // Create object for this registry's tags
            Object registryObject = builder.createObject();

            for (TagInfo tag : registryTags) {
                Object tagObject = builder.createObject();

                // Add replace field if true
                if (tag.isReplace()) {
                    builder.addBooleanProperty(tagObject, "replace", true);
                }

                // Add values array
                Object valuesArray = builder.createArray();
                for (TagEntryInfo entry : tag.getEntries()) {
                    // If entry has non-default properties (not required), use object format
                    if (!entry.isRequired()) {
                        Object entryObject = builder.createObject();
                        builder.addStringProperty(entryObject, "id", entry.getId());
                        builder.addBooleanProperty(entryObject, "required", false);
                        builder.addToArray(valuesArray, entryObject);
                    } else {
                        // Otherwise use simple string format
                        builder.addStringToArray(valuesArray, entry.getId());
                    }
                }
                builder.addToObject(tagObject, "values", valuesArray);

                // Add this tag to the registry object
                builder.addToObject(registryObject, tag.getTagIdentifier(), tagObject);
            }

            // Add this registry's tags to root
            builder.addToObject(root, registryType, registryObject);
        }

        return root;
    }

    /**
     * Generates a JSON dump
     */
    private static void generateJsonDump(File outputFile, ProfileConfig profile, Collection<TagInfo> tags) throws IOException {
        JsonDataBuilder builder = new JsonDataBuilder();
        JsonObject root = (JsonObject) buildTagData(tags, profile, builder);

        // Choose GSON instance based on format
        boolean pretty = profile.getExport().getJson() != null && profile.getExport().getJson().isPretty();
        Gson gson = pretty ? GSON_PRETTY : GSON_MINIFIED;

        try (FileWriter writer = new FileWriter(outputFile)) {
            gson.toJson(root, writer);
        }
        DataDump.LOGGER.info("Successfully dumped tag data to: {}", outputFile.getAbsolutePath());
    }

    /**
     * Generates an NBT dump
     */
    private static void generateNbtDump(File outputFile, ProfileConfig profile, Collection<TagInfo> tags) throws IOException {
        // Get platform-specific NBT writer and create root compound
        NbtWriter nbtWriter = PlatformHelper.getNbtWriter();
        NbtCompound root = nbtWriter.createCompound();
        NbtDataBuilder builder = new NbtDataBuilder(root);
        buildTagData(tags, profile, builder);

        // Write NBT to file
        boolean compressed = profile.getExport().getNbt() != null && profile.getExport().getNbt().isCompressed();
        nbtWriter.writeNbt(root, outputFile, compressed);

        DataDump.LOGGER.info("Successfully dumped tag data to: {} (NBT, compressed: {})", 
            outputFile.getAbsolutePath(), compressed);
    }

}
