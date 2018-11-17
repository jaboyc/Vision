package com.jlogical.vision.compiler.script;

/**
 * Container for a name and value.
 */
public class Variable {
    /**
     * The name of the variable.
     */
    private String name;

    /**
     * The value of the variable.
     */
    private Object value;

    /**
     * Creates a Variable with the given name and value.
     */
    public Variable(String name, Object value){
        this.name = name != null ? name : "";
        this.value = value;
    }

    /**
     * Creates a Variable with the given name and a default value of 0.
     */
    public Variable(String name){
        this(name, 0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
