package com.jlogical.vision.compiler.definitions;

import com.jlogical.vision.compiler.Line;

/**
 * An abstract class that holds a template for a custom definition. These are defined by "define action" in code.
 */
public abstract class DefineTemplate {

    /**
     * The line the definition started at.
     */
    private Line line;

    /**
     * Creates a DefineTemplate with the given line.
     */
    public DefineTemplate(Line line){
        this.line = line;
    }

    public Line getLine() {
        return line;
    }
}
