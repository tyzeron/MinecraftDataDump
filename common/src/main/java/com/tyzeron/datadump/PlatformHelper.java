package com.tyzeron.datadump;

import com.tyzeron.datadump.abstraction.block.BlockDataProvider;
import com.tyzeron.datadump.abstraction.nbt.NbtWriter;

import java.nio.file.Path;


/**
 * Helper class to get platform-specific implementations.
 * Each loader will set its own provider implementation.
 */
public class PlatformHelper {

    private static BlockDataProvider blockDataProvider;
    private static NbtWriter nbtWriter;
    private static Path gameDirectory;
    private static Path configDirectory;

    public static void setBlockDataProvider(BlockDataProvider provider) {
        blockDataProvider = provider;
    }

    public static BlockDataProvider getBlockDataProvider() {
        if (blockDataProvider == null) {
            throw new IllegalStateException("BlockDataProvider not initialized! Make sure the loader-specific code sets it.");
        }
        return blockDataProvider;
    }

    public static void setNbtWriter(NbtWriter writer) {
        nbtWriter = writer;
    }

    public static NbtWriter getNbtWriter() {
        if (nbtWriter == null) {
            throw new IllegalStateException("NbtWriter not initialized! Make sure the loader-specific code sets it.");
        }
        return nbtWriter;
    }

    public static void setGameDirectory(Path directory) {
        gameDirectory = directory;
    }

    public static Path getGameDirectory() {
        if (gameDirectory == null) {
            throw new IllegalStateException("Game directory not initialized! Make sure the loader-specific code sets it.");
        }
        return gameDirectory;
    }

    public static void setConfigDirectory(Path directory) {
        configDirectory = directory;
    }

    public static Path getConfigDirectory() {
        if (configDirectory == null) {
            throw new IllegalStateException("Config directory not initialized! Make sure the loader-specific code sets it.");
        }
        return configDirectory;
    }

}
