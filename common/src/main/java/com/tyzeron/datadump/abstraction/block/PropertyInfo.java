package com.tyzeron.datadump.abstraction.block;

import java.util.List;


/**
 * Platform-agnostic representation of a block property.
 */
public class PropertyInfo {

    private final String name;
    private final List<String> possibleValues;

    public PropertyInfo(String name, List<String> possibleValues) {
        this.name = name;
        this.possibleValues = possibleValues;
    }

    public String getName() {
        return name;
    }

    public List<String> getPossibleValues() {
        return possibleValues;
    }

}
