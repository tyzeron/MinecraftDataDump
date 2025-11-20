package com.tyzeron.datadump.abstraction.registry;

import java.util.Map;


/**
 * Simple implementation of RegistryEntryInfo
 */
public class RegistryEntryData implements RegistryEntryInfo {

    private final String identifier;
    private final int rawId;
    private final Map<String, Object> encodedData;

    public RegistryEntryData(String identifier, int rawId, Map<String, Object> encodedData) {
        this.identifier = identifier;
        this.rawId = rawId;
        this.encodedData = encodedData;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public int getRawId() {
        return rawId;
    }

    @Override
    public Map<String, Object> getEncodedData() {
        return encodedData;
    }

    @Override
    public boolean hasEncodedData() {
        return encodedData != null && !encodedData.isEmpty();
    }

}
