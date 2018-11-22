package com.jlogical.vision.compiler.values;

import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.project.CodeRange;

/**
 * Holder for a value that is evaluated at runtime.
 */
public interface Value {

    /**
     * @return the value of this Value.
     */
    Object getValue() throws VisionException;

    /**
     * @return the CodeRange this Value is in.
     */
    CodeRange getRange();

}
