package com.jlogical.vision.compiler.script.elements;

import com.jlogical.vision.api.elements.CustomElement;
import com.jlogical.vision.runtime.values.Value;
import com.jlogical.vision.project.CodeRange;

import java.util.ArrayList;

/**
 * A piece of code that was compiled.
 */
public abstract class CompiledElement<T extends CustomElement> {

    /**
     * The core of the compiled element.
     */
    private String core;

    /**
     * The CustomElement this CompiledElement is based on.
     */
    private T template;

    /**
     * List of Values that are used for inputs in this CompiledElement.
     */
    private ArrayList<Value> values;

    /**
     * Creates a new CompiledElement with a core and values.
     */
    public CompiledElement(String core, ArrayList<Value> values){
        this.core = core != null ? core : "";
        this.values = values != null ? values : new ArrayList<>();
    }

    /**
     * @return the CodeRange that this CompiledElement is in.
     */
    public abstract CodeRange getRange();

    public String getCore() {
        return core;
    }

    public ArrayList<Value> getValues() {
        return values;
    }
}
