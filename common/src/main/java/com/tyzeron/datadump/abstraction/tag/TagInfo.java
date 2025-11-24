package com.tyzeron.datadump.abstraction.tag;

import java.util.List;


/**
 * Represents a complete registry tag with all its metadata and entries.
 * Tags group registry elements together (e.g., all log blocks, all stone types).
 */
public class TagInfo {

    private final String registryType;
    private final String tagIdentifier;
    private final boolean replace;
    private final List<TagEntryInfo> entries;

    public TagInfo(String registryType, String tagIdentifier, boolean replace, List<TagEntryInfo> entries) {
        this.registryType = registryType;
        this.tagIdentifier = tagIdentifier;
        this.replace = replace;
        this.entries = entries;
    }

    /**
     * Gets the registry type this tag belongs to (e.g., "block", "item", "entity_type")
     */
    public String getRegistryType() {
        return registryType;
    }

    /**
     * Gets the full identifier of this tag (e.g., "minecraft:logs")
     */
    public String getTagIdentifier() {
        return tagIdentifier;
    }

    /**
     * Returns true if this tag replaces lower-priority tags with the same name
     */
    public boolean isReplace() {
        return replace;
    }

    /**
     * Gets all entries in this tag
     */
    public List<TagEntryInfo> getEntries() {
        return entries;
    }

}
