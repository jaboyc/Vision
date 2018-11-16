package com.jlogical.vision.compiler.script.elements;

import com.jlogical.vision.project.CodeRange;

import java.util.ArrayList;

/**
 * Stores a list of commands that is run when an event occurs.
 */
public class Hat extends CompiledElement{

    /**
     * List of Commands in this Hat.
     */
    ArrayList<Command> commands;

    /**
     * Creates a new CompiledElement with a core and values.
     *
     */
    public Hat(String core, ArrayList<Command> commands) {
        super(core, null);
        this.commands = commands != null ? commands : new ArrayList<>();
    }

    @Override
    public CodeRange getRange() {
        //TODO
        return null;
    }
}
