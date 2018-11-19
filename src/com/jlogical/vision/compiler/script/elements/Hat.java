package com.jlogical.vision.compiler.script.elements;

import com.jlogical.vision.api.elements.CustomHat;
import com.jlogical.vision.compiler.values.Value;
import com.jlogical.vision.project.CodeRange;

import java.util.ArrayList;

/**
 * Stores a list of commands that is run when an event occurs.
 */
public class Hat extends CompiledElement<CustomHat> {

    /**
     * List of Commands in this Hat.
     */
    ArrayList<Command> commands;

    /**
     * Creates a new CompiledElement with a core and values.
     */
    public Hat(CustomHat hat, ArrayList<Value> values, ArrayList<Command> commands) {
        super(hat, values);
        this.commands = commands != null ? commands : new ArrayList<>();
    }

    @Override
    public CodeRange getRange() {
        if (commands == null || commands.isEmpty()) {
            return null;
        }
        return CodeRange.between(commands.get(0).getRange().startLocation(), commands.get(commands.size() - 1).getRange().endLocation());
    }
}
