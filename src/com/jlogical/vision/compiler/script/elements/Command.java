package com.jlogical.vision.compiler.script.elements;

import com.jlogical.vision.api.elements.CustomCommand;
import com.jlogical.vision.compiler.Line;
import com.jlogical.vision.compiler.values.Value;
import com.jlogical.vision.project.CodeRange;

import java.util.ArrayList;

/**
 * Contains information for a compiled command which can be executed later.
 */
public class Command extends CompiledElement<CustomCommand> {

    /**
     * The line that this Command was found in.
     */
    private Line line;

    /**
     * Creates a new CompiledElement with a core, template, values, and line.
     */
    public Command(CustomCommand template, ArrayList<Value> values, Line line) {
        super(template, values);
        this.line = line;
    }

    /**
     * Runs the Command.
     */
    public void run() {

    }

    @Override
    public CodeRange getRange() {
        return line.getRange();
    }

    public Line getLine() {
        return line;
    }
}
