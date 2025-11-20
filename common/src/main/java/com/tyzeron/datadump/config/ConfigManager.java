package com.tyzeron.datadump.config;

import com.moandjiezana.toml.Toml;
import com.tyzeron.datadump.DataDump;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;


public class ConfigManager {

    private static final String PROFILE_DIR_NAME = "datadump";
    private static final String[] DEFAULT_PROFILES = {
        "all-json.toml",
        "multiple-files.toml",
        "picolimbo.toml"
    };

    private final Path profileDir;

    public ConfigManager(Path configDir) {
        this.profileDir = configDir.resolve(PROFILE_DIR_NAME);
    }

    /**
     * Loads a specific profile configuration from file
     */
    public ProfileConfig loadProfile(String profileName) throws IOException {
        // Ensure profile has .toml extension
        if (!profileName.endsWith(".toml")) {
            profileName = profileName + ".toml";
        }
        File profileFile = new File(profileDir.toFile(), profileName);
        if (!profileFile.exists()) {
            throw new IOException("Profile not found: " + profileName);
        }

        try {
            Toml toml = new Toml().read(profileFile);
            ProfileConfig config = toml.to(ProfileConfig.class);
            DataDump.LOGGER.info("Successfully loaded profile: {}", profileName);
            return config;
        } catch (Exception e) {
            DataDump.LOGGER.error("Failed to parse profile: {}", profileName, e);
            throw new IOException("Failed to parse profile: " + profileName, e);
        }
    }

    /**
     * Lists all available profile names
     */
    public List<String> listProfiles() {
        List<String> profiles = new ArrayList<>();
        if (!profileDir.toFile().exists()) {
            return profiles;
        }

        File[] files = profileDir.toFile().listFiles((dir, name) -> name.endsWith(".toml"));
        if (files != null) {
            for (File file : files) {
                profiles.add(file.getName());
            }
        }
        return profiles;
    }

    /**
     * Initializes the profile directory with default profiles if it doesn't exist
     */
    public void initializeProfiles() {
        if (!profileDir.toFile().exists()) {
            DataDump.LOGGER.info("Profile directory not found, creating and copying default profiles...");
            copyDefaultProfiles();
        } else {
            // Check if directory is empty
            File[] files = profileDir.toFile().listFiles((dir, name) -> name.endsWith(".toml"));
            if (files == null || files.length == 0) {
                DataDump.LOGGER.info("Profile directory is empty, copying default profiles...");
                copyDefaultProfiles();
            }
        }
    }

    /**
     * Resets the profiles by copying default ones
     */
    public void resetProfiles() {
        DataDump.LOGGER.info("Resetting profiles to defaults...");
        copyDefaultProfiles();
    }

    /**
     * Copies default profile files from resources to config directory
     */
    private void copyDefaultProfiles() {
        try {
            // Create directory if it doesn't exist
            if (!profileDir.toFile().exists() && !profileDir.toFile().mkdirs()) {
                DataDump.LOGGER.error("Failed to create profile directory: {}", profileDir.toFile().getAbsolutePath());
                return;
            }

            // Copy each default profile
            for (String profileName : DEFAULT_PROFILES) {
                try (InputStream is = getClass().getClassLoader().getResourceAsStream("profiles/" + profileName)) {
                    if (is == null) {
                        DataDump.LOGGER.warn("Default profile not found in resources: {}", profileName);
                        continue;
                    }
                    File targetFile = new File(profileDir.toFile(), profileName);
                    Files.copy(is, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    DataDump.LOGGER.info("Copied default profile: {}", profileName);
                } catch (IOException e) {
                    DataDump.LOGGER.error("Failed to copy profile: {}", profileName, e);
                }
            }
            DataDump.LOGGER.info("Default profiles copied to: {}", profileDir.toFile().getAbsolutePath());
        } catch (Exception e) {
            DataDump.LOGGER.error("Failed to copy default profiles", e);
        }
    }

    /**
     * Gets the profile directory path
     */
    public File getProfileDir() {
        return profileDir.toFile();
    }

}
