package com.jlogical.vision.compiler.script.elements;

import com.jlogical.vision.api.elements.CustomCommand;
import com.jlogical.vision.compiler.Line;
import com.jlogical.vision.compiler.script.values.Value;
import com.jlogical.vision.project.CodeRange;

import java.util.ArrayList;

/**
 * Contains information for a compiled command which can be executed later.
 */
public class Command extends CompiledElement {

    /**
     * The line that this Command was found in.
     */
    private Line line;

    /**
     * The CustomCommand this Command is based off of.
     */
    private CustomCommand template;

    /**
     * Creates a new CompiledElement with a core, template, values, and line.
     */
    public Command(String core, CustomCommand template, ArrayList<Value> values, Line line) {
        super(core, values);
        this.template = template;
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

    public CustomCommand getTemplate() {
        return template;
    }
}
