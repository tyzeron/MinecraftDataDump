package com.tyzeron.datadump.abstraction.nbt;


/**
 * Platform-agnostic wrapper for NBT compound tags.
 * This allows the common module to work with NBT without depending on Minecraft classes.
 */
public interface NbtCompound {

    /**
     * Put a byte value.
     */
    void putByte(String key, byte value);

    /**
     * Put a short value.
     */
    void putShort(String key, short value);

    /**
     * Put an integer value.
     */
    void putInt(String key, int value);

    /**
     * Put a long value.
     */
    void putLong(String key, long value);

    /**
     * Put a float value.
     */
    void putFloat(String key, float value);

    /**
     * Put a double value.
     */
    void putDouble(String key, double value);

    /**
     * Put a string value.
     */
    void putString(String key, String value);

    /**
     * Put a byte array.
     */
    void putByteArray(String key, byte[] value);

    /**
     * Put an integer array.
     */
    void putIntArray(String key, int[] value);

    /**
     * Put a long array.
     */
    void putLongArray(String key, long[] value);

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
