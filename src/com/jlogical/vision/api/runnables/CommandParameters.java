package com.jlogical.vision.api.runnables;

import com.jlogical.vision.compiler.script.elements.CBlock;
import com.jlogical.vision.compiler.script.elements.Command;
import com.jlogical.vision.compiler.script.elements.Hat;
import com.jlogical.vision.compiler.values.Value;
import com.jlogical.vision.project.CodeRange;

import java.util.ArrayList;

/**
 * Parameters for a Command.
 */
public class CommandParameters<T extends Command> extends Parameters<T>{

    /**
     * The CBlock that is holding this Command. Null if none.
     */
    CBlock cblockHolder;

    /**
     * Creates a Parameters with a given List of Values.
     */
    public CommandParameters(T command, ArrayList<Value> values, Hat hatHolder, CodeRange range, CBlock cblockHolder) {
        super(command, values, hatHolder, range);
        this.cblockHolder = cblockHolder;
    }

    public CBlock getCBblockHolder() {
        return cblockHolder;
    }


}
