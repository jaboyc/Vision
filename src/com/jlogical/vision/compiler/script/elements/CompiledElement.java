package com.jlogical.vision.compiler.script.elements;

import com.jlogical.vision.api.elements.CustomElement;
import com.jlogical.vision.compiler.Line;
import com.jlogical.vision.compiler.values.Value;
import com.jlogical.vision.project.CodeRange;

import java.util.ArrayList;

/**
 * A piece of code that was compiled.
 */
public abstract class CompiledElement<T extends CustomElement> {

    /**
     * The CustomElement this CompiledElement is based on.
     */
    private T template;

    /**
     * List of Values that are used for inputs in this CompiledElement.
     */
    private ArrayList<Value> values;

    /**
     * Creates a new CompiledElement based on a template, line, and values.
     */
    public CompiledElement(T template, ArrayList<Value> values){
        this.template = template;
        this.values = values != null ? values : new ArrayList<>();
    }

    /**
     * @return the CodeRange that this CompiledElement is in.
     */
    public abstract CodeRange getRange();

    /**
     * @return the core of the template.
     */
    public String getCore() {
        return template.getCore();
    }

    public T getTemplate() {
        return template;
    }

    public ArrayList<Value> getValues() {
        return values;
    }

    public void setValues(ArrayList<Value> values) {
        this.values = values;
    }
}
