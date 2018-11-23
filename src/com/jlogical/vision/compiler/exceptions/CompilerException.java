package com.jlogical.vision.compiler.exceptions;

import com.jlogical.vision.project.CodeRange;

/**
 * An exception created during compilation. Caught by Vision and depending on the IDE, an error is displayed.
 */
public class CompilerException extends Exception {

    /**
     * The type of the exception. Used for differentiating between different types of CompilerExceptions.
     */
    String type;

    /**
     * The range of where the error occured.
     */
    CodeRange range;

    /**
     * Creates a new CompilerException.
     *
     * @param message the message of the exception.
     */
    public CompilerException(String message, String type, CodeRange range) {
        super(message + "(@ " + range + ")");
        this.type = type;
        this.range = range;
    }

    public String getType() {
        return type;
    }

    public CodeRange getRange() {
        return range;
    }
}
