package com.jlogical.vision.compiler.definitions;

import com.jlogical.vision.compiler.Input;
import com.jlogical.vision.compiler.Line;
import com.jlogical.vision.compiler.script.elements.Hat;

import java.util.ArrayList;

/**
 * Holds template information for a defined reporter. Defined by "define reporter name".
 */
public class DefinedReporter extends DefineTemplate{

    /**
     * The core represented by this definition.
     */
    private String core;

    /**
     * The hat that is holding the definition of this template.
     */
    private Hat hat;

    /**
     * List of names for variables that could be inserted into this hat.
     */
    private ArrayList<String> variableNames;

    /**
     * Creates a DefinedReporter with the given line and core.
     */
    public DefinedReporter(Line line, String core) {
        super(line);
        this.core = core;
        hat = null;
        variableNames = new ArrayList<>();
    }

    public Hat getHat() {
        return hat;
    }

    /**
     * Gathers variable names from the given inputs.
     */
    public void parseVariableNames(ArrayList<Input> inputs){
        for(Input i : inputs){
            variableNames.add(i.getText());
        }
    }

    public void setHat(Hat hat) {
        this.hat = hat;
    }

    public String getCore() {
        return core;
    }

    public ArrayList<String> getVariableNames() {
        return variableNames;
    }
}
