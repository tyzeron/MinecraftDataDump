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
        } else if (value instanceof NbtList) {
            list.addList((NbtList) value);
        }
    }

    @Override
    public void addByteProperty(Object parent, String key, byte value) {
        NbtCompound compound = (NbtCompound) parent;
        compound.putByte(key, value);
    }

    @Override
    public void addShortProperty(Object parent, String key, short value) {
        NbtCompound compound = (NbtCompound) parent;
        compound.putShort(key, value);
    }

    @Override
    public void addIntProperty(Object parent, String key, int value) {
        NbtCompound compound = (NbtCompound) parent;
        compound.putInt(key, value);
    }

    @Override
    public void addLongProperty(Object parent, String key, long value) {
        NbtCompound compound = (NbtCompound) parent;
        compound.putLong(key, value);
    }

    @Override
    public void addFloatProperty(Object parent, String key, float value) {
        NbtCompound compound = (NbtCompound) parent;
        compound.putFloat(key, value);
    }

    @Override
    public void addDoubleProperty(Object parent, String key, double value) {
        NbtCompound compound = (NbtCompound) parent;
        compound.putDouble(key, value);
    }

    @Override
    public void addStringProperty(Object parent, String key, String value) {
        NbtCompound compound = (NbtCompound) parent;
        compound.putString(key, value);
    }

    @Override
    public void addByteArrayProperty(Object parent, String key, byte[] value) {
        NbtCompound compound = (NbtCompound) parent;
        compound.putByteArray(key, value);
    }

    @Override
    public void addIntArrayProperty(Object parent, String key, int[] value) {
        NbtCompound compound = (NbtCompound) parent;
        compound.putIntArray(key, value);
    }

    @Override
    public void addLongArrayProperty(Object parent, String key, long[] value) {
        NbtCompound compound = (NbtCompound) parent;
        compound.putLongArray(key, value);
    }

    @Override
    public void addBooleanProperty(Object parent, String key, boolean value) {
        NbtCompound compound = (NbtCompound) parent;
        compound.putBoolean(key, value);
    }

    @Override
    public void addByteToArray(Object array, byte value) {
        NbtList list = (NbtList) array;
        list.addByte(value);
    }

    @Override
    public void addShortToArray(Object array, short value) {
        NbtList list = (NbtList) array;
        list.addShort(value);
    }

    @Override
    public void addIntToArray(Object array, int value) {
        NbtList list = (NbtList) array;
        list.addInt(value);
    }

    @Override
    public void addLongToArray(Object array, long value) {
        NbtList list = (NbtList) array;
        list.addLong(value);
    }

    @Override
    public void addFloatToArray(Object array, float value) {
        NbtList list = (NbtList) array;
        list.addFloat(value);
    }

    @Override
    public void addDoubleToArray(Object array, double value) {
        NbtList list = (NbtList) array;
        list.addDouble(value);
    }

    @Override
    public void addStringToArray(Object array, String value) {
        NbtList list = (NbtList) array;
        list.addString(value);
    }

    @Override
    public void addByteArrayToArray(Object array, byte[] value) {
        NbtList list = (NbtList) array;
        list.addByteArray(value);
    }

    @Override
    public void addIntArrayToArray(Object array, int[] value) {
        NbtList list = (NbtList) array;
        list.addIntArray(value);
    }

    @Override
    public void addLongArrayToArray(Object array, long[] value) {
        NbtList list = (NbtList) array;
        list.addLongArray(value);
    }

}
