package com.tyzeron.datadump.abstraction.block;

import java.util.List;


/**
 * Platform-agnostic representation of a block.
 */
public class BlockInfo {

    private final String identifier;
    private final List<PropertyInfo> properties;
    private final List<BlockStateInfo> states;

    public BlockInfo(String identifier, List<PropertyInfo> properties, List<BlockStateInfo> states) {
        this.identifier = identifier;
        this.properties = properties;
        this.states = states;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<PropertyInfo> getProperties() {
        return properties;
    }

    public List<BlockStateInfo> getStates() {
        return states;
    }

}
