package com.tyzeron.datadump.builder;


/**
 * Abstract builder interface for creating hierarchical data structures.
 * Implementations handle specific formats (JSON, NBT, etc.)
 */
public interface DataStructureBuilder {

    /**
     * Creates a new object/compound structure
     */
    Object createObject();

    /**
     * Creates a new array/list structure
     */
    Object createArray();

    /**
     * Adds a child object to a parent object with a key
     */
    void addToObject(Object parent, String key, Object value);

    /**
     * Adds an element to an array
     */
    void addToArray(Object array, Object value);

    /**
     * Adds a byte property to an object
     */
    void addByteProperty(Object parent, String key, byte value);

    /**
     * Adds a short property to an object
     */
    void addShortProperty(Object parent, String key, short value);

    /**
     * Adds an integer property to an object
     */
    void addIntProperty(Object parent, String key, int value);

    /**
     * Adds a long property to an object
     */
    void addLongProperty(Object parent, String key, long value);

    /**
     * Adds a float property to an object
     */
    void addFloatProperty(Object parent, String key, float value);

    /**
     * Adds a double property to an object
     */
    void addDoubleProperty(Object parent, String key, double value);

    /**
     * Adds a string property to an object
     */
    void addStringProperty(Object parent, String key, String value);

    /**
     * Adds a byte array property to an object
     */
    void addByteArrayProperty(Object parent, String key, byte[] value);

    /**
     * Adds an integer array property to an object
     */
    void addIntArrayProperty(Object parent, String key, int[] value);

    /**
     * Adds a long array property to an object
     */
    void addLongArrayProperty(Object parent, String key, long[] value);

    /**
     * Adds a boolean property to an object
     */
    void addBooleanProperty(Object parent, String key, boolean value);

    /**
     * Adds a byte value to an array
     */
    void addByteToArray(Object array, byte value);

    /**
     * Adds a short value to an array
     */
    void addShortToArray(Object array, short value);

    /**
     * Adds an integer value to an array
     */
    void addIntToArray(Object array, int value);

    /**
     * Adds a long value to an array
     */
    void addLongToArray(Object array, long value);

    /**
     * Adds a float value to an array
     */
    void addFloatToArray(Object array, float value);

    /**
     * Adds a double value to an array
     */
    void addDoubleToArray(Object array, double value);

    /**
     * Adds a string value to an array
     */
    void addStringToArray(Object array, String value);

    /**
     * Adds a byte array to an array
     */
    void addByteArrayToArray(Object array, byte[] value);

    /**
     * Adds an integer array to an array
     */
    void addIntArrayToArray(Object array, int[] value);

    /**
     * Adds a long array to an array
     */
    void addLongArrayToArray(Object array, long[] value);

}
