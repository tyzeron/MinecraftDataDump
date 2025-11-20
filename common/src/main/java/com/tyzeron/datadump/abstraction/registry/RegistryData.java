package com.tyzeron.datadump.abstraction.registry;

import java.util.List;


/**
 * Simple implementation of RegistryInfo
 */
public class RegistryData implements RegistryInfo {

    private final String registryId;
    private final List<RegistryEntryInfo> entries;

    public RegistryData(String registryId, List<RegistryEntryInfo> entries) {
        this.registryId = registryId;
        this.entries = entries;
    }

    @Override
    public String getRegistryIdentifier() {
        return registryId;
    }

    @Override
    public List<RegistryEntryInfo> getEntries() {
        return entries;
    }

    @Override
    public int getEntryCount() {
        return entries.size();
    }

}
