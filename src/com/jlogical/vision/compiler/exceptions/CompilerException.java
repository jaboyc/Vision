package com.jlogical.vision.compiler.exceptions;

import com.jlogical.vision.project.CodeRange;

/**
 * An exception created during compilation. Caught by Vision and depending on the IDE, an error is displayed.
 */
public class CompilerException extends Exception {


    /**
     * The range of where the error occured.
     */
    CodeRange range;

    /**
     * Creates a new CompilerException.
     *
     * @param message the message of the exception.
     */
    public CompilerException(String message, CodeRange range) {
        super(message + "(@ " + range + ")");
        this.range = range;
    }


    public CodeRange getRange() {
        return range;
    }
}
