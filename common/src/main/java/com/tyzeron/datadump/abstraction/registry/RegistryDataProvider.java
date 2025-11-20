package com.tyzeron.datadump.abstraction.registry;

import java.util.Collection;


/**
 * Platform-specific provider for accessing registry data
 */
public interface RegistryDataProvider {

    /**
     * Gets all available registries from the dynamic registry manager
     */
    Collection<RegistryInfo> getAllRegistries();

}
