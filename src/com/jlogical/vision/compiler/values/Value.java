package com.jlogical.vision.compiler.values;

/**
 * Holder for a value that is evaluated at runtime.
 */
public interface Value {

    /**
     * @return the value of this Value.
     */
    Object getValue();

}
