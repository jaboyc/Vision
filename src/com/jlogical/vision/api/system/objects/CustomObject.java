package com.jlogical.vision.api.system.objects;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a custom object.
 */
public class CustomObject {

    /**
     * The type of the object.
     */
    private String type;

    /**
     * The properties of the object.
     */
    private Map<String, Object> properties;

    /**
     * Creates a new custom object with the given type.
     *
     * @param type the type of the object.
     */
    public CustomObject(String type) {
        this.type = type;
        properties = new HashMap<>();
    }

    /**
     * @param propName the name of the property to get the value of.
     * @return the value of the property with the give name.
     * @throws IllegalArgumentException if the property doesn't exist.
     */
    public Object getProperty(String propName) throws IllegalArgumentException {
        if (properties.containsKey(propName)) return properties.get(propName);
        throw new IllegalArgumentException("Property " + propName + " not found!");
    }

    /**
     * Sets the value of the property to the given value. If the value doesn't exist, creates a new property.
     *
     * @param propName the name of the property to set.
     * @param value    the new value of the property.
     */
    public void setProperty(String propName, Object value) {
        properties.put(propName, value);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(type).append(" -> {\n");
        for (Map.Entry<String, Object> entry : properties.entrySet())
            stringBuilder.append("\t\"").append(entry.getKey()).append("\" : \"").append(entry.getValue()).append("\"\n");

        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public String getType() {
        return type;
    }
}
