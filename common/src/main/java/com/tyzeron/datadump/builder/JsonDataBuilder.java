package com.tyzeron.datadump.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 * JSON implementation of DataStructureBuilder using Gson
 */
public class JsonDataBuilder implements DataStructureBuilder {

    @Override
    public Object createObject() {
        return new JsonObject();
    }

    @Override
    public Object createArray() {
        return new JsonArray();
    }

    @Override
    public void addToObject(Object parent, String key, Object value) {
        JsonObject jsonParent = (JsonObject) parent;
        if (value instanceof JsonObject) {
            jsonParent.add(key, (JsonObject) value);
        } else if (value instanceof JsonArray) {
            jsonParent.add(key, (JsonArray) value);
        }
    }

    @Override
    public void addToArray(Object array, Object value) {
        JsonArray jsonArray = (JsonArray) array;
        if (value instanceof JsonObject) {
            jsonArray.add((JsonObject) value);
        }
    }

    @Override
    public void addStringProperty(Object parent, String key, String value) {
        JsonObject jsonParent = (JsonObject) parent;
        jsonParent.addProperty(key, value);
    }

    @Override
    public void addIntProperty(Object parent, String key, int value) {
        JsonObject jsonParent = (JsonObject) parent;
        jsonParent.addProperty(key, value);
    }
 
    @Override
    public void addBooleanProperty(Object parent, String key, boolean value) {
        JsonObject jsonParent = (JsonObject) parent;
        jsonParent.addProperty(key, value);
    }

    @Override
    public void addStringToArray(Object array, String value) {
        JsonArray jsonArray = (JsonArray) array;
        jsonArray.add(value);
    }

}
