package com.tyzeron.datadump.abstraction.nbt;


/**
 * Platform-agnostic wrapper for NBT compound tags.
 * This allows the common module to work with NBT without depending on Minecraft classes.
 */
public interface NbtCompound {

    /**
     * Put a string value.
     */
    void putString(String key, String value);

    /**
     * Put an integer value.
     */
    void putInt(String key, int value);

    /**
     * Put a boolean value.
     */
    void putBoolean(String key, boolean value);

    /**
     * Put another compound.
     */
    void putCompound(String key, NbtCompound compound);

    /**
     * Put a list.
     */
    void putList(String key, NbtList list);

    /**
     * Create a new empty compound.
     */
    NbtCompound createCompound();

    /**
     * Create a new empty list.
     */
    NbtList createList();

}
