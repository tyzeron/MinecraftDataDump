package com.tyzeron.datadump.abstraction.nbt;


/**
 * Platform-agnostic wrapper for NBT list tags.
 */
public interface NbtList {

    /**
     * Add a string value to the list.
     */
    void addString(String value);

    /**
     * Add a compound to the list.
     */
    void addCompound(NbtCompound compound);

}
