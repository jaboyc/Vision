package com.jlogical.vision.compiler.values;

import com.jlogical.vision.project.CodeRange;

/**
 * Value that holds a String.
 */
public class TextValue implements Value {

    /**
     * The String holding the value of the text.
     */
    private String value;

    /**
     * The range the TextValue is in.
     */
    private CodeRange range;

    public TextValue(String value, CodeRange range){
        if(range == null){
            throw new NullPointerException("Range for a TextValue cannot be null.");
        }
        this.value = value != null ? value : "";
        this.range = range;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public CodeRange getRange() {
        return range;
    }
}
