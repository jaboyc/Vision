package com.jlogical.vision.api.runnables;

import com.jlogical.vision.compiler.script.elements.Hat;
import com.jlogical.vision.compiler.values.Value;

import java.util.ArrayList;

/**
 * Parameters for a Command.
 */
public class CommandParameters extends Parameters{

    //TODO Add CBlock Holder.


    /**
     * Creates a Parameters with a given List of Values.
     */
    public CommandParameters(ArrayList<Value> values, Hat hatHolder) {
        super(values, hatHolder);
    }
}
