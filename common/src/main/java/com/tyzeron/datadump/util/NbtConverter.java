package com.tyzeron.datadump.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Base class for converting platform-specific NBT tags to common Map/Object structures.
 * Platform implementations should extend this and implement the platform-specific conversion.
 */
public abstract class NbtConverter<T> {

    /**
     * Converts an NBT tag to a Map structure
     */
    public Map<String, Object> convertToMap(T tag) {
        if (isCompound(tag)) {
            return convertCompoundToMap(tag);
        } else {
            // If it's not a compound, wrap it in a map
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("value", convertToObject(tag));
            return map;
        }
    }

    /**
     * Converts an NBT tag to an Object (recursive)
     */
    public Object convertToObject(T tag) {
        if (isCompound(tag)) {
            return convertCompoundToMap(tag);
        } else if (isList(tag)) {
            return convertListToList(tag);
        } else if (isByteArray(tag)) {
            return getAsByteArray(tag);
        } else if (isIntArray(tag)) {
            return getAsIntArray(tag);
        } else if (isLongArray(tag)) {
            return getAsLongArray(tag);
        } else if (isInt(tag)) {
            return getAsInt(tag);
        } else if (isLong(tag)) {
            return getAsLong(tag);
        } else if (isFloat(tag)) {
            return getAsFloat(tag);
        } else if (isDouble(tag)) {
            return getAsDouble(tag);
        } else if (isByte(tag)) {
            return getAsByte(tag);
        } else if (isShort(tag)) {
            return getAsShort(tag);
        } else {
            return getAsString(tag);
        }
    }

    private Map<String, Object> convertCompoundToMap(T compound) {
        Map<String, Object> map = new LinkedHashMap<>();
        for (String key : getCompoundKeys(compound)) {
            T value = getCompoundValue(compound, key);
            if (value != null) {
                map.put(key, convertToObject(value));
            }
        }
        return map;
    }

    private List<Object> convertListToList(T list) {
        List<Object> result = new ArrayList<>();
        for (T item : getListElements(list)) {
            result.add(convertToObject(item));
        }
        return result;
    }

    // Abstract methods to be implemented by platform-specific converters
    protected abstract boolean isCompound(T tag);
    protected abstract boolean isList(T tag);
    protected abstract boolean isByteArray(T tag);
    protected abstract boolean isIntArray(T tag);
    protected abstract boolean isLongArray(T tag);
    protected abstract boolean isInt(T tag);
    protected abstract boolean isLong(T tag);
    protected abstract boolean isFloat(T tag);
    protected abstract boolean isDouble(T tag);
    protected abstract boolean isByte(T tag);
    protected abstract boolean isShort(T tag);

    protected abstract Iterable<String> getCompoundKeys(T compound);
    protected abstract T getCompoundValue(T compound, String key);
    protected abstract Iterable<T> getListElements(T list);

    protected abstract int getAsInt(T tag);
    protected abstract long getAsLong(T tag);
    protected abstract float getAsFloat(T tag);
    protected abstract double getAsDouble(T tag);
    protected abstract byte getAsByte(T tag);
    protected abstract short getAsShort(T tag);
    protected abstract byte[] getAsByteArray(T tag);
    protected abstract int[] getAsIntArray(T tag);
    protected abstract long[] getAsLongArray(T tag);
    protected abstract String getAsString(T tag);

}
