package com.jlogical.vision.compiler;

import com.jlogical.vision.project.CodeLocation;

/**
 * Holds information for one line of code.
 */
public class Line {

    /**
     * The code associated with this Line.
     */
    private String code;

    /**
     * The location of this line.
     */
    private CodeLocation location;

    /**
     * Creates a new Line with the given code and location.
     */
    public Line(String code, CodeLocation location){
        if(location == null){
            throw new NullPointerException("Location of a line cannot be null.");
        }
        if(location.getFile() == null || location.getProject() == null || location.getLineNum() == -1){
            throw new NullPointerException("Code location needs to be referencing a specific line in a specific file.");
        }
        this.code = code != null ? code : "";
        this.location = location;
    }

    public String toString(){
        return "Line " + location.getLineNum() + ": "+code;
    }

    public String getCode() {
        return code;
    }

    public CodeLocation getLocation() {
        return location;
    }

}
