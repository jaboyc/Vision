package com.jlogical.vision.api.runnables;

import com.jlogical.vision.compiler.script.elements.Command;
import com.jlogical.vision.compiler.values.Value;

import java.util.ArrayList;

public class ReporterParameters extends Parameters {

    /**
     * The Command that is running this Reporter. Null if standalone.
     */
    private Command commandHolder;

    /**
     * Creates a Parameters with a given List of Values.
     */
    public ReporterParameters(ArrayList<Value> values, Command commandHolder) {
        super(values, commandHolder.getHatHolder());
        this.commandHolder = commandHolder;
    }

    public Command getCommandHolder() {
        return commandHolder;
    }
}
