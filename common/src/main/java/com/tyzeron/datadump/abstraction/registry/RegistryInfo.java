package com.tyzeron.datadump.abstraction.registry;

import java.util.List;


/**
 * Represents a single registry with all its entries
 */
public interface RegistryInfo {

    /**
     * Gets the identifier/name of this registry (e.g., "minecraft:block")
     */
    String getRegistryIdentifier();

    /**
     * Gets all entries in this registry
     */
    List<RegistryEntryInfo> getEntries();

    /**
     * Gets the total number of entries in this registry
     */
    int getEntryCount();

}
