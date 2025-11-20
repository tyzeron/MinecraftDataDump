package com.tyzeron.datadump.abstraction.registry;

import java.util.Map;


/**
 * Represents a single entry in a registry
 */
public interface RegistryEntryInfo {

    /**
     * Gets the identifier/name of this registry entry (e.g., "minecraft:stone")
     */
    String getIdentifier();

    /**
     * Gets the raw/protocol ID for this entry
     */
    int getRawId();

    /**
     * Gets the encoded NBT data for this entry, if available
     * Returns null if encoding failed or is not supported
     */
    Map<String, Object> getEncodedData();

    /**
     * Checks if encoded data is available for this entry
     */
    boolean hasEncodedData();

}
