package com.jlogical.vision.compiler.values;

import com.jlogical.vision.project.CodeRange;

/**
 * Value that holds a number.
 */
public class NumValue implements Value {

    /**
     * The double value of the Value.
     */
    private double value;

    /**
     * The range the Value is in.
     */
    private CodeRange range;

    /**
     * Creates a NumValue with a given value and range.
     */
    public NumValue(double value, CodeRange range){
        this.value = value;
        this.range = range;
    }

    @Override
    public Object getValue() {
        if (value == (int) value)
            return (int) value;
        return value;
    }

    @Override
    public CodeRange getRange() {
        return range;
    }
}
