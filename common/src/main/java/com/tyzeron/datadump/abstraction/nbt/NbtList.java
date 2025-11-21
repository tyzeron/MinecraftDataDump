package com.tyzeron.datadump.abstraction.nbt;


/**
 * Platform-agnostic wrapper for NBT list tags.
 */
public interface NbtList {

    /**
     * Add a byte value to the list.
     */
    void addByte(byte value);

    /**
     * Add a short value to the list.
     */
    void addShort(short value);

    /**
     * Add an integer value to the list.
     */
    void addInt(int value);

    /**
     * Add a long value to the list.
     */
    void addLong(long value);

    /**
     * Add a float value to the list.
     */
    void addFloat(float value);

    /**
     * Add a double value to the list.
     */
    void addDouble(double value);

    /**
     * Add a string value to the list.
     */
    void addString(String value);

    /**
     * Add a byte array to the list.
     */
    void addByteArray(byte[] value);

    /**
     * Add an integer array to the list.
     */
    void addIntArray(int[] value);

    /**
     * Add a long array to the list.
     */
    void addLongArray(long[] value);

    /**
     * Add a compound to the list.
     */
    void addCompound(NbtCompound compound);

    /**
     * Add a list to the list.
     */
    void addList(NbtList list);

}
