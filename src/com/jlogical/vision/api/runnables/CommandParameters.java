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
     * The Hat that is holding this Command.
     */
    private Hat hatHolder;

    /**
     * Creates a Parameters with a given List of Values.
     */
    public CommandParameters(ArrayList<Value> values, Hat hatHolder) {
        super(values);
        if(hatHolder == null){
            throw new IllegalArgumentException("hatHolder cannot be null!");
        }
        this.hatHolder = hatHolder;
    }

    public Hat getHatHolder() {
        return hatHolder;
    }
}
