package com.jlogical.vision.compiler;

import com.jlogical.vision.project.CodeLocation;
import com.jlogical.vision.project.CodeRange;
import com.jlogical.vision.util.Pair;

import java.util.ArrayList;

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
     * The core of the command. The part of the command without inputs.
     */
    private String core;

    /**
     * The inputs of the command. First is the input itself, second is the absolute location of that input.
     */
    private ArrayList<Pair<String, CodeRange>> inputs;

    /**
     * Creates a new Line with the given code and location. Core and inputs must be given.
     */
    public Line(String code, String core, ArrayList<Pair<String, CodeRange>> inputs, CodeLocation location) {
        if (location == null) {
            throw new NullPointerException("Location of a line cannot be null.");
        }
        if (location.getFile() == null || location.getProject() == null || location.getLineNum() == -1) {
            throw new NullPointerException("Code location needs to be referencing a specific line in a specific file.");
        }
        this.code = code != null ? code : "";
        this.core = core != null ? core : "";
        this.inputs = inputs != null ? inputs : new ArrayList<>();
        this.location = location;
    }

    /**
     * @return the CodeRange this Line is in.
     */
    public CodeRange getRange(){
        return new CodeRange(location.getProject(), location.getFile(), location.getLineNum(), 0, location.getLineNum(), code.length());
    }

    @Override
    public String toString() {
        return "Line " + location.getLineNum() + ": " + code;
    }

    public String getCode() {
        return code;
    }

    public CodeLocation getLocation() {
        return location;
    }

}
