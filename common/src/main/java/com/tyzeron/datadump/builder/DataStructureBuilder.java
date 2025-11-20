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
     * Adds a string property to an object
     */
    void addStringProperty(Object parent, String key, String value);

    /**
     * Adds an integer property to an object
     */
    void addIntProperty(Object parent, String key, int value);

    /**
     * Adds a boolean property to an object
     */
    void addBooleanProperty(Object parent, String key, boolean value);

    /**
     * Adds a string value to an array
     */
    void addStringToArray(Object array, String value);

}
