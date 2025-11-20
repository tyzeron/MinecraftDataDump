package com.tyzeron.datadump;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.tyzeron.datadump.abstraction.block.BlockDataProvider;
import com.tyzeron.datadump.abstraction.block.BlockInfo;
import com.tyzeron.datadump.abstraction.block.BlockStateInfo;
import com.tyzeron.datadump.abstraction.block.PropertyInfo;
import com.tyzeron.datadump.abstraction.nbt.NbtCompound;
import com.tyzeron.datadump.abstraction.nbt.NbtWriter;
import com.tyzeron.datadump.builder.DataStructureBuilder;
import com.tyzeron.datadump.builder.JsonDataBuilder;
import com.tyzeron.datadump.builder.NbtDataBuilder;
import com.tyzeron.datadump.config.ProfileConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;


public class BlockDataDump {

    private static final Gson GSON_PRETTY = new GsonBuilder().setPrettyPrinting().create();
    private static final Gson GSON_MINIFIED = new Gson();

    /**
     * Generates a block data dump according to the specified profile configuration
     */
    public static void generateDump(File outputFile, ProfileConfig profile) throws IOException {
        String format = profile.getExport().getFormat().toLowerCase();

        // Check if format is binary and reject it
        if ("binary".equals(format)) {
            throw new IOException("Binary format is not implemented yet");
        }

        // Get platform-specific block data provider
        BlockDataProvider provider = PlatformHelper.getBlockDataProvider();
        Collection<BlockInfo> blocks = provider.getAllBlocks();

        // Create parent directory if needed
        File parentDir = outputFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        if ("json".equals(format)) {
            generateJsonDump(outputFile, profile, blocks);
        } else if ("nbt".equals(format)) {
            generateNbtDump(outputFile, profile, blocks);
        } else {
            throw new IOException("Unknown format: " + format);
        }
    }

    /**
     * Common method to build block data structure using any builder implementation
     */
    public static Object buildBlockData(Collection<BlockInfo> blocks, ProfileConfig profile, DataStructureBuilder builder) {
        Object root = builder.createObject();
        Map<String, Object> sortedBlocks = new TreeMap<>();

        boolean includeProperties = profile.getBlocks().isProperties();
        boolean includeStates = profile.getBlocks().isStates();

        // Iterate through all registered blocks
        for (BlockInfo blockInfo : blocks) {
            Object blockData = builder.createObject();

            // Add properties if the block has any and profile includes them
            if (includeProperties && !blockInfo.getProperties().isEmpty()) {
                Object propertiesObject = builder.createObject();

                for (PropertyInfo property : blockInfo.getProperties()) {
                    Object valuesArray = builder.createArray();
                    for (String value : property.getPossibleValues()) {
                        builder.addStringToArray(valuesArray, value);
                    }
                    builder.addToObject(propertiesObject, property.getName(), valuesArray);
                }

                builder.addToObject(blockData, "properties", propertiesObject);
            }

            // Add all possible block states if profile includes them
            if (includeStates) {
                Object statesArray = builder.createArray();
                for (BlockStateInfo state : blockInfo.getStates()) {
                    Object stateObject = builder.createObject();

                    // Add state ID
                    builder.addIntProperty(stateObject, "id", state.getStateId());

                    // Add properties for this state
                    if (!state.getProperties().isEmpty()) {
                        Object stateProperties = builder.createObject();
                        for (Map.Entry<String, String> entry : state.getProperties().entrySet()) {
                            builder.addStringProperty(stateProperties, entry.getKey(), entry.getValue());
                        }
                        builder.addToObject(stateObject, "properties", stateProperties);
                    }

                    // Mark default state
                    if (state.isDefault()) {
                        builder.addBooleanProperty(stateObject, "default", true);
                    }

                    builder.addToArray(statesArray, stateObject);
                }

                builder.addToObject(blockData, "states", statesArray);
            }

            sortedBlocks.put(blockInfo.getIdentifier(), blockData);
        }

        // Add all blocks to root object
        for (Map.Entry<String, Object> entry : sortedBlocks.entrySet()) {
            builder.addToObject(root, entry.getKey(), entry.getValue());
        }

        return root;
    }

    /**
     * Generates a JSON dump
     */
    private static void generateJsonDump(File outputFile, ProfileConfig profile, Collection<BlockInfo> blocks) throws IOException {
        JsonDataBuilder builder = new JsonDataBuilder();
        JsonObject root = (JsonObject) buildBlockData(blocks, profile, builder);

        // Choose GSON instance based on format
        boolean pretty = profile.getExport().getJson() != null && profile.getExport().getJson().isPretty();
        Gson gson = pretty ? GSON_PRETTY : GSON_MINIFIED;

        try (FileWriter writer = new FileWriter(outputFile)) {
            gson.toJson(root, writer);
        }
        DataDump.LOGGER.info("Successfully dumped block data to: {}", outputFile.getAbsolutePath());
    }

    /**
     * Generates an NBT dump
     */
    private static void generateNbtDump(File outputFile, ProfileConfig profile, Collection<BlockInfo> blocks) throws IOException {
        // Get platform-specific NBT writer and create root compound
        NbtWriter nbtWriter = PlatformHelper.getNbtWriter();
        NbtCompound root = nbtWriter.createCompound();
        NbtDataBuilder builder = new NbtDataBuilder(root);
        buildBlockData(blocks, profile, builder);

        // Write NBT to file
        boolean compressed = profile.getExport().getNbt() != null && profile.getExport().getNbt().isCompressed();
        nbtWriter.writeNbt(root, outputFile, compressed);

        DataDump.LOGGER.info("Successfully dumped block data to: {} (NBT, compressed: {})", 
            outputFile.getAbsolutePath(), compressed);
    }

}
