package com.tyzeron.datadump.builder;

import com.tyzeron.datadump.abstraction.nbt.NbtCompound;
import com.tyzeron.datadump.abstraction.nbt.NbtList;


/**
 * NBT implementation of DataStructureBuilder
 */
public class NbtDataBuilder implements DataStructureBuilder {

    private final NbtCompound rootCompound;
    private boolean isFirstObjectCreation = true;

    public NbtDataBuilder(NbtCompound rootCompound) {
        this.rootCompound = rootCompound;
    }

    @Override
    public Object createObject() {
        // The first time createObject is called, return the root compound
        // Subsequent calls create new nested compounds
        if (isFirstObjectCreation) {
            isFirstObjectCreation = false;
            return rootCompound;
        }
        return rootCompound.createCompound();
    }

    @Override
    public Object createArray() {
        return rootCompound.createList();
    }

    @Override
    public void addToObject(Object parent, String key, Object value) {
        NbtCompound compound = (NbtCompound) parent;
        if (value instanceof NbtCompound) {
            compound.putCompound(key, (NbtCompound) value);
        } else if (value instanceof NbtList) {
            compound.putList(key, (NbtList) value);
        }
    }

    @Override
    public void addToArray(Object array, Object value) {
        NbtList list = (NbtList) array;
        if (value instanceof NbtCompound) {
            list.addCompound((NbtCompound) value);
        }
    }

    @Override
    public void addStringProperty(Object parent, String key, String value) {
        NbtCompound compound = (NbtCompound) parent;
        compound.putString(key, value);
    }

    @Override
    public void addIntProperty(Object parent, String key, int value) {
        NbtCompound compound = (NbtCompound) parent;
        compound.putInt(key, value);
    }

    @Override
    public void addBooleanProperty(Object parent, String key, boolean value) {
        NbtCompound compound = (NbtCompound) parent;
        compound.putBoolean(key, value);
    }

    @Override
    public void addStringToArray(Object array, String value) {
        NbtList list = (NbtList) array;
        list.addString(value);
    }

}
