package com.tyzeron.datadump.command;

import com.tyzeron.datadump.BlockDataDump;
import com.tyzeron.datadump.DataDump;
import com.tyzeron.datadump.PlatformHelper;
import com.tyzeron.datadump.config.ConfigManager;
import com.tyzeron.datadump.config.ProfileConfig;

import java.io.File;
import java.nio.file.Path;
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

            DataDump.LOGGER.info("Running data dump -> {}", outputFile.getAbsolutePath());
            BlockDataDump.generateDump(outputFile, profile);

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
                        // Skip non-blocks categories for now
                        if (!"blocks".equals(category)) {
                            DataDump.LOGGER.info("Skipping '{}' dump (not implemented yet)", category);
                            continue;
                        }

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
                        tempProfile.setBlocks(profile.getBlocks());

                        DataDump.LOGGER.info("Running {} dump -> {}", category, outputFile.getAbsolutePath());
                        BlockDataDump.generateDump(outputFile, tempProfile);
                        successCount++;
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
