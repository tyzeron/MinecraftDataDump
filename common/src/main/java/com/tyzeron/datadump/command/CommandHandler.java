package com.tyzeron.datadump.command;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import com.tyzeron.datadump.BlockDataDump;
import com.tyzeron.datadump.DataDump;
import com.tyzeron.datadump.PlatformHelper;
import com.tyzeron.datadump.RegistryDataDump;
import com.tyzeron.datadump.abstraction.block.BlockDataProvider;
import com.tyzeron.datadump.abstraction.block.BlockInfo;
import com.tyzeron.datadump.abstraction.nbt.NbtCompound;
import com.tyzeron.datadump.abstraction.nbt.NbtWriter;
import com.tyzeron.datadump.abstraction.registry.RegistryDataProvider;
import com.tyzeron.datadump.abstraction.registry.RegistryInfo;
import com.tyzeron.datadump.builder.DataStructureBuilder;
import com.tyzeron.datadump.builder.JsonDataBuilder;
import com.tyzeron.datadump.builder.NbtDataBuilder;
import com.tyzeron.datadump.config.ConfigManager;
import com.tyzeron.datadump.config.ProfileConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;


/**
 * Common command handler logic for all platforms
 */
public class CommandHandler {

    private static ConfigManager configManager;

    /**
     * Initializes the command handler with the config directory
     */
    public static void initialize(Path configDir) {
        configManager = new ConfigManager(configDir);
        // Initialize profiles on startup
        configManager.initializeProfiles();
    }

    /**
     * Handles the /datadump run <profile> command
     */
    public static CommandResult handleRun(String profileName) {
        if (configManager == null) {
            return CommandResult.error("Command handler not initialized!");
        }
        try {
            // Load the specified profile
            ProfileConfig profile = configManager.loadProfile(profileName);
            
            // Check if single file or multi-file mode
            if (profile.getExport().isSingleFile()) {
                return runSingleFileDump(profile);
            } else {
                return runMultiFileDump(profile);
            }
        } catch (Exception e) {
            DataDump.LOGGER.error("Failed to run data dump for profile: {}", profileName, e);
            return CommandResult.error("Failed to run data dump: " + e.getMessage());
        }
    }

    /**
     * Runs a single-file dump
     */
    private static CommandResult runSingleFileDump(ProfileConfig profile) {
        try {
            Path outputDir = PlatformHelper.getGameDirectory().resolve("datadump");
            String format = profile.getExport().getFormat();
            String filename = profile.getExport().getFilename();

            // Add appropriate extension if not present
            if (!filename.contains(".")) {
                if ("json".equalsIgnoreCase(format)) {
                    filename += ".json";
                } else if ("nbt".equalsIgnoreCase(format)) {
                    filename += ".nbt";
                }
            }
            File outputFile = new File(outputDir.toFile(), filename);

            // Create parent directory if needed
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            DataDump.LOGGER.info("Running combined data dump -> {}", outputFile.getAbsolutePath());

            // Generate combined dump with all enabled data sources
            generateCombinedDump(outputFile, profile);

            return CommandResult.success(String.format(
                "Data dump completed successfully! File saved to: %s",
                outputFile.getAbsolutePath()
            ));
        } catch (Exception e) {
            DataDump.LOGGER.error("Failed to run single-file dump", e);
            return CommandResult.error("Failed to run dump: " + e.getMessage());
        }
    }

    /**
     * Runs a multi-file dump
     */
    private static CommandResult runMultiFileDump(ProfileConfig profile) {
        try {
            Path outputDir = PlatformHelper.getGameDirectory().resolve("datadump");
            int successCount = 0;
            int failCount = 0;

            // Process each multi-output configuration
            if (profile.getMultiOutput() != null) {
                for (var entry : profile.getMultiOutput().entrySet()) {
                    String category = entry.getKey();
                    ProfileConfig.MultiOutputConfig outputConfig = entry.getValue();

                    try {
                        String format = outputConfig.getFormat();
                        String filename = outputConfig.getFile();
                        if (!filename.contains(".")) {
                            if ("json".equalsIgnoreCase(format)) {
                                filename += ".json";
                            } else if ("nbt".equalsIgnoreCase(format)) {
                                filename += ".nbt";
                            }
                        }
                        File outputFile = new File(outputDir.toFile(), filename);

                        // Create a temporary profile config with the specific format
                        ProfileConfig tempProfile = new ProfileConfig();
                        ProfileConfig.ExportConfig exportConfig = new ProfileConfig.ExportConfig();
                        exportConfig.setFormat(format);
                        exportConfig.setSingleFile(true);
                        exportConfig.setFilename(filename);

                        // Copy JSON/NBT settings from original profile
                        exportConfig.setJson(profile.getExport().getJson());
                        exportConfig.setNbt(profile.getExport().getNbt());

                        tempProfile.setExport(exportConfig);

                        DataDump.LOGGER.info("Running {} dump -> {}", category, outputFile.getAbsolutePath());

                        // Route to the appropriate dump handler based on category
                        if ("blocks".equals(category)) {
                            tempProfile.setBlocks(profile.getBlocks());
                            BlockDataDump.generateDump(outputFile, tempProfile);
                            successCount++;
                        } else if ("registries".equals(category)) {
                            tempProfile.setRegistries(profile.getRegistries());
                            RegistryDataDump.generateDump(outputFile, tempProfile);
                            successCount++;
                        } else {
                            DataDump.LOGGER.warn("Unknown category '{}', skipping", category);
                        }
                    } catch (Exception e) {
                        DataDump.LOGGER.error("Failed to dump category: {}", category, e);
                        failCount++;
                    }
                }
            }

            if (successCount == 0 && failCount == 0) {
                return CommandResult.error("No valid output configurations found in profile");
            }
            if (failCount > 0) {
                return CommandResult.success(String.format(
                    "Data dump completed with errors. Success: %d, Failed: %d. Check logs for details.",
                    successCount, failCount
                ));
            } else {
                return CommandResult.success(String.format(
                    "Data dump completed successfully! Generated %d file(s) in: %s",
                    successCount, outputDir
                ));
            }
        } catch (Exception e) {
            DataDump.LOGGER.error("Failed to run multi-file dump", e);
            return CommandResult.error("Failed to run dump: " + e.getMessage());
        }
    }

    /**
     * Generates a combined dump with all enabled data sources
     */
    private static void generateCombinedDump(File outputFile, ProfileConfig profile) throws IOException {
        String format = profile.getExport().getFormat().toLowerCase();

        // Check if format is binary and reject it
        if ("binary".equals(format)) {
            throw new IOException("Binary format is not implemented yet");
        }

        if ("json".equals(format)) {
            generateCombinedJsonDump(outputFile, profile);
        } else if ("nbt".equals(format)) {
            generateCombinedNbtDump(outputFile, profile);
        } else {
            throw new IOException("Unknown format: " + format);
        }
    }

    /**
     * Generates a combined JSON dump
     */
    private static void generateCombinedJsonDump(File outputFile, ProfileConfig profile) throws IOException {
        JsonDataBuilder builder = new JsonDataBuilder();
        JsonObject root = (JsonObject) builder.createObject();

        // Add blocks data if enabled
        if (profile.getBlocks() != null) {
            BlockDataProvider blockProvider = PlatformHelper.getBlockDataProvider();
            Collection<BlockInfo> blocks = blockProvider.getAllBlocks();
            
            DataDump.LOGGER.info("Building blocks data for combined dump...");
            Object blocksData = BlockDataDump.buildBlockData(blocks, profile, builder);
            builder.addToObject(root, "blocks", blocksData);
        }

        // Add registries data if enabled
        if (profile.getRegistries() != null) {
            RegistryDataProvider registryProvider = PlatformHelper.getRegistryDataProvider();
            Collection<RegistryInfo> registries = registryProvider.getAllRegistries();
            
            DataDump.LOGGER.info("Building registries data for combined dump...");
            Object registriesData = RegistryDataDump.buildRegistryData(registries, profile, builder);
            builder.addToObject(root, "registries", registriesData);
        }

        // Choose GSON instance based on format
        boolean pretty = profile.getExport().getJson() != null && profile.getExport().getJson().isPretty();
        Gson gson = pretty ? new GsonBuilder().setPrettyPrinting().create() : new Gson();

        try (FileWriter writer = new FileWriter(outputFile)) {
            gson.toJson(root, writer);
        }
        DataDump.LOGGER.info("Successfully dumped combined data to: {}", outputFile.getAbsolutePath());
    }

    /**
     * Generates a combined NBT dump
     */
    private static void generateCombinedNbtDump(File outputFile, ProfileConfig profile) throws IOException {
        NbtWriter nbtWriter = PlatformHelper.getNbtWriter();
        NbtCompound root = nbtWriter.createCompound();
        NbtDataBuilder builder = new NbtDataBuilder(root);

        // Add blocks data if enabled
        if (profile.getBlocks() != null) {
            BlockDataProvider blockProvider = PlatformHelper.getBlockDataProvider();
            Collection<BlockInfo> blocks = blockProvider.getAllBlocks();
            
            DataDump.LOGGER.info("Building blocks data for combined dump...");
            Object blocksData = BlockDataDump.buildBlockData(blocks, profile, builder);
            builder.addToObject(root, "blocks", blocksData);
        }

        // Add registries data if enabled
        if (profile.getRegistries() != null) {
            RegistryDataProvider registryProvider = PlatformHelper.getRegistryDataProvider();
            Collection<RegistryInfo> registries = registryProvider.getAllRegistries();
            
            DataDump.LOGGER.info("Building registries data for combined dump...");
            Object registriesData = RegistryDataDump.buildRegistryData(registries, profile, builder);
            builder.addToObject(root, "registries", registriesData);
        }

        // Write NBT to file
        boolean compressed = profile.getExport().getNbt() != null && profile.getExport().getNbt().isCompressed();
        nbtWriter.writeNbt(root, outputFile, compressed);

        DataDump.LOGGER.info("Successfully dumped combined data to: {} (NBT, compressed: {})", 
            outputFile.getAbsolutePath(), compressed);
    }

    /**
     * Handles the /datadump list command
     */
    public static CommandResult handleList() {
        if (configManager == null) {
            return CommandResult.error("Command handler not initialized!");
        }
        try {
            List<String> profiles = configManager.listProfiles();
            if (profiles.isEmpty()) {
                return CommandResult.info("No profiles found. Run '/datadump reset' to create default profiles.");
            }

            StringBuilder message = new StringBuilder();
            message.append("§6§lAvailable Profiles:§r\n");
            for (String profile : profiles) {
                message.append("  §e").append(profile).append("§r\n");
            }
            message.append("\n§7Use '/datadump run <profile>' to execute a profile");

            return CommandResult.info(message.toString());
        } catch (Exception e) {
            DataDump.LOGGER.error("Failed to list profiles", e);
            return CommandResult.error("Failed to list profiles: " + e.getMessage());
        }
    }

    /**
     * Handles the /datadump reset command
     */
    public static CommandResult handleReset() {
        if (configManager == null) {
            return CommandResult.error("Command handler not initialized!");
        }
        try {
            configManager.resetProfiles();
            return CommandResult.success("Profiles have been reset to defaults at: " + 
                configManager.getProfileDir().getAbsolutePath());
        } catch (Exception e) {
            DataDump.LOGGER.error("Failed to reset profiles", e);
            return CommandResult.error("Failed to reset profiles: " + e.getMessage());
        }
    }

    /**
     * Handles the /datadump help command
     */
    public static CommandResult handleHelp() {
        String help = "§6§l=== Data Dump Command Help ===§r\n" +
                "§e/datadump run <profile>§r - Executes data dump using specified profile\n" +
                "§e/datadump list§r - Lists all available profiles\n" +
                "§e/datadump reset§r - Resets preset profiles to default values\n" +
                "§e/datadump help§r - Shows this help message\n" +
                "§7For more information, check the profile files in the profile directory.";
        return CommandResult.info(help);
    }

    /**
     * Gets the config manager instance
     */
    public static ConfigManager getConfigManager() {
        return configManager;
    }

}
