package com.jlogical.vision.api.runnables;


import com.jlogical.vision.compiler.script.values.Value;

import java.util.ArrayList;

/**
 * Stores information regarding parameters of running a command. Needs to be concrete based on the type of Element running it.
 */
public abstract class Parameters {

    /**
     * The List of Values in the Parameters. Cannot be null.
     */
    ArrayList<Value> values;

    /**
     * Creates a Parameters with a given List of Values.
     */
    public Parameters(ArrayList<Value> values){
        this.values = values != null ? values : new ArrayList<Value>();
    }

    public ArrayList<Value> getValues() {
        return values;
    }
}
