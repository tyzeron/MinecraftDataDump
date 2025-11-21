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
        } else if (value instanceof JsonArray) {
            jsonArray.add((JsonArray) value);
        }
    }

    @Override
    public void addByteProperty(Object parent, String key, byte value) {
        JsonObject jsonParent = (JsonObject) parent;
        jsonParent.addProperty(key, value);
    }

    @Override
    public void addShortProperty(Object parent, String key, short value) {
        JsonObject jsonParent = (JsonObject) parent;
        jsonParent.addProperty(key, value);
    }

    @Override
    public void addIntProperty(Object parent, String key, int value) {
        JsonObject jsonParent = (JsonObject) parent;
        jsonParent.addProperty(key, value);
    }

    @Override
    public void addLongProperty(Object parent, String key, long value) {
        JsonObject jsonParent = (JsonObject) parent;
        jsonParent.addProperty(key, value);
    }

    @Override
    public void addFloatProperty(Object parent, String key, float value) {
        JsonObject jsonParent = (JsonObject) parent;
        jsonParent.addProperty(key, value);
    }

    @Override
    public void addDoubleProperty(Object parent, String key, double value) {
        JsonObject jsonParent = (JsonObject) parent;
        jsonParent.addProperty(key, value);
    }

    @Override
    public void addStringProperty(Object parent, String key, String value) {
        JsonObject jsonParent = (JsonObject) parent;
        jsonParent.addProperty(key, value);
    }

    @Override
    public void addByteArrayProperty(Object parent, String key, byte[] value) {
        JsonObject jsonParent = (JsonObject) parent;
        JsonArray array = new JsonArray();
        for (byte b : value) {
            array.add(b);
        }
        jsonParent.add(key, array);
    }

    @Override
    public void addIntArrayProperty(Object parent, String key, int[] value) {
        JsonObject jsonParent = (JsonObject) parent;
        JsonArray array = new JsonArray();
        for (int i : value) {
            array.add(i);
        }
        jsonParent.add(key, array);
    }

    @Override
    public void addLongArrayProperty(Object parent, String key, long[] value) {
        JsonObject jsonParent = (JsonObject) parent;
        JsonArray array = new JsonArray();
        for (long l : value) {
            array.add(l);
        }
        jsonParent.add(key, array);
    }
 
    @Override
    public void addBooleanProperty(Object parent, String key, boolean value) {
        JsonObject jsonParent = (JsonObject) parent;
        jsonParent.addProperty(key, value);
    }

    @Override
    public void addByteToArray(Object array, byte value) {
        JsonArray jsonArray = (JsonArray) array;
        jsonArray.add(value);
    }

    @Override
    public void addShortToArray(Object array, short value) {
        JsonArray jsonArray = (JsonArray) array;
        jsonArray.add(value);
    }

    @Override
    public void addIntToArray(Object array, int value) {
        JsonArray jsonArray = (JsonArray) array;
        jsonArray.add(value);
    }

    @Override
    public void addLongToArray(Object array, long value) {
        JsonArray jsonArray = (JsonArray) array;
        jsonArray.add(value);
    }

    @Override
    public void addFloatToArray(Object array, float value) {
        JsonArray jsonArray = (JsonArray) array;
        jsonArray.add(value);
    }

    @Override
    public void addDoubleToArray(Object array, double value) {
        JsonArray jsonArray = (JsonArray) array;
        jsonArray.add(value);
    }

    @Override
    public void addStringToArray(Object array, String value) {
        JsonArray jsonArray = (JsonArray) array;
        jsonArray.add(value);
    }

    @Override
    public void addByteArrayToArray(Object array, byte[] value) {
        JsonArray jsonArray = (JsonArray) array;
        JsonArray byteArray = new JsonArray();
        for (byte b : value) {
            byteArray.add(b);
        }
        jsonArray.add(byteArray);
    }

    @Override
    public void addIntArrayToArray(Object array, int[] value) {
        JsonArray jsonArray = (JsonArray) array;
        JsonArray intArray = new JsonArray();
        for (int i : value) {
            intArray.add(i);
        }
        jsonArray.add(intArray);
    }

    @Override
    public void addLongArrayToArray(Object array, long[] value) {
        JsonArray jsonArray = (JsonArray) array;
        JsonArray longArray = new JsonArray();
        for (long l : value) {
            longArray.add(l);
        }
        jsonArray.add(longArray);
    }

}
