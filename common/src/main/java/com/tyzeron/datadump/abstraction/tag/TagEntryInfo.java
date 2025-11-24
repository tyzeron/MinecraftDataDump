package com.tyzeron.datadump.abstraction.tag;


/**
 * Represents a single entry in a tag.
 * An entry can be either a direct reference to a registry entry
 * or a reference to another tag (prefixed with #).
 */
public class TagEntryInfo {

    private final String id;
    private final boolean isTag;
    private final boolean required;

    public TagEntryInfo(String id, boolean isTag, boolean required) {
        this.id = id;
        this.isTag = isTag;
        this.required = required;
    }

    /**
     * Gets the entry identifier (e.g., "minecraft:stone" or "#minecraft:logs")
     */
    public String getId() {
        return id;
    }

    /**
     * Returns true if this entry references another tag (starts with #)
     */
    public boolean isTag() {
        return isTag;
    }

    /**
     * Returns true if this entry is required (loading fails if not found)
     */
    public boolean isRequired() {
        return required;
    }

}
