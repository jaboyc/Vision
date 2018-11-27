package com.jlogical.vision.compiler;

import com.jlogical.vision.project.CodeRange;

/**
 * Stores information for one input which will be transformed into a Value.
 */
public class Input {
    /**
     * The text of the input.
     */
    private String text;

    /**
     * The range of the input.
     */
    private CodeRange range;

    /**
     * The type of the input. Either parentheses, brackets, or curly braces.
     */
    private char type;

    /**
     * Creates a new Input with a given text, range, and type.
     */
    public Input(String text, CodeRange range, char type){
        this.text = text != null ? text : "";
        this.range = range;
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public CodeRange getRange() {
        return range;
    }

    public char getType() {
        return type;
    }
}
