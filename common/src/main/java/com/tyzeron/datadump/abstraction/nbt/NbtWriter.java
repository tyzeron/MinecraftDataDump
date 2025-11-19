package com.tyzeron.datadump.abstraction.nbt;

import java.io.File;
import java.io.IOException;


/**
 * Platform-agnostic interface for writing NBT data.
 * Each mod loader (Fabric, Forge, NeoForge) implements this interface.
 */
public interface NbtWriter {

    /**
     * Write NBT compound data to a file.
     * @param compound The NBT compound to write
     * @param file The output file
     * @param compressed Whether to use compression
     * @throws IOException If writing fails
     */
    void writeNbt(NbtCompound compound, File file, boolean compressed) throws IOException;

    /**
     * Create a new NBT compound.
     * @return A new empty NBT compound
     */
    NbtCompound createCompound();

}
