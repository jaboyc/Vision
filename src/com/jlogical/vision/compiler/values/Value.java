package com.jlogical.vision.compiler.values;

import com.jlogical.vision.project.CodeRange;

/**
 * Holder for a value that is evaluated at runtime.
 */
public interface Value {

    /**
     * @return the value of this Value.
     */
    Object getValue();

    /**
     * @return the CodeRange this Value is in.
     */
    CodeRange getRange();

}
