package com.jlogical.vision.api.runnables;

import com.jlogical.vision.compiler.script.elements.CBlock;
import com.jlogical.vision.compiler.script.elements.Command;
import com.jlogical.vision.compiler.script.elements.Hat;
import com.jlogical.vision.compiler.script.elements.Reporter;
import com.jlogical.vision.compiler.values.Value;
import com.jlogical.vision.project.CodeRange;

import java.util.ArrayList;

public class ReporterParameters extends Parameters<Reporter> {

    /**
     * The Command that is running this Reporter. Null if standalone.
     */
    private Command commandHolder;

    /**
     * Creates a Parameters with a given List of Values.
     */
    public ReporterParameters(Reporter reporter, ArrayList<Value> values, Command commandHolder, CodeRange range) {
        super(reporter, values, commandHolder.getHatHolder(), range);
        this.commandHolder = commandHolder;
    }

    public Command getCommandHolder() {
        return commandHolder;
    }

    public CBlock getCBlockHolder(){
        return commandHolder.getCBlockHolder();
    }

    public Hat getHatHolder(){
        return commandHolder.getHatHolder();
    }
}
