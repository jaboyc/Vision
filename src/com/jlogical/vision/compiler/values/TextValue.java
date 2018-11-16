package com.jlogical.vision.compiler.values;

/**
 * Value that holds a String.
 */
public class TextValue implements Value {

    /**
     * The String holding the value of the text.
     */
    private String value;

    public TextValue(String value){
        this.value = value != null ? value : "";
    }

    @Override
    public Object getValue() {
        return value;
    }
}
