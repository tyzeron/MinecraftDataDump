package com.tyzeron.datadump.abstraction.block;

import java.util.Map;


/**
 * Platform-agnostic representation of a block state.
 */
public class BlockStateInfo {

    private final int stateId;
    private final Map<String, String> properties;
    private final boolean isDefault;

    public BlockStateInfo(int stateId, Map<String, String> properties, boolean isDefault) {
        this.stateId = stateId;
        this.properties = properties;
        this.isDefault = isDefault;
    }

    public int getStateId() {
        return stateId;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public boolean isDefault() {
        return isDefault;
    }

}
