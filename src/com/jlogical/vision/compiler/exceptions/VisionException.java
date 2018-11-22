package com.jlogical.vision.compiler.exceptions;

import com.jlogical.vision.project.CodeRange;

/**
 * Exception that is run during runtime. Stores the CodeRange of where the error occurred.
 */
public class VisionException extends Exception{

    /**
     * The range of where the error occurred.
     */
    private CodeRange range;

    /**
     * Creates a new VisionException with a given message and range.
     */
    public VisionException(String message, CodeRange range){
        super(message);
        this.range = range;
    }

    public CodeRange getRange() {
        return range;
    }
}
