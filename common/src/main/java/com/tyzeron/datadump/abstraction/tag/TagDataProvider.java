package com.tyzeron.datadump.abstraction.tag;

import java.util.Collection;


/**
 * Platform-specific provider for tag data.
 * Each mod loader (Fabric, Forge, NeoForge) implements this to extract
 * tag information from the Minecraft registry system.
 */
public interface TagDataProvider {

    /**
     * Gets all loaded tags across all registry types
     * @return Collection of all tags
     */
    Collection<TagInfo> getAllTags();

    /**
     * Gets all tags for a specific registry type
     * @param registryType The registry type (e.g., "block", "item")
     * @return Collection of tags for that registry
     */
    Collection<TagInfo> getTagsForRegistry(String registryType);

}
