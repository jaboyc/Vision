package com.jlogical.vision.compiler.exceptions;

import com.jlogical.vision.project.CodeLocation;
import com.jlogical.vision.project.VisionFile;

/**
 * An exception created during compilation. Caught by Vision and depending on the IDE, an error is displayed.
 */
public class CompilerException extends Exception {

    /**
     * The type of the exception. Used for differentiating between different types of CompilerExceptions.
     */
    String type;

    /**
     * The location of where the error occured.
     */
    CodeLocation location;

    /**
     * Creates a new CompilerException.
     * @param message the message of the exception.
     */
    public CompilerException(String message, String type, CodeLocation location){
        super(message);
        this.type = type;
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public CodeLocation getLocation() {
        return location;
    }
}
