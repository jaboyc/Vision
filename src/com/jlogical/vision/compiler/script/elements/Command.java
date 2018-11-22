package com.jlogical.vision.compiler.script.elements;

import com.jlogical.vision.api.elements.CustomCommand;
import com.jlogical.vision.api.runnables.CommandParameters;
import com.jlogical.vision.api.runnables.CommandRunnable;
import com.jlogical.vision.compiler.Line;
import com.jlogical.vision.compiler.exceptions.VisionException;
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
     * The Hat that is holding this Command.
     */
    private Hat hatHolder;

    /**
     * Creates a new CompiledElement with a core, template, values, and line.
     */
    public Command(CustomCommand template, ArrayList<Value> values, Line line, Hat hatHolder) {
        super(template, values);
        this.line = line;
        this.hatHolder = hatHolder;
    }

    /**
     * Runs the Command.
     *
     * @throws VisionException if there was an error running the Command.
     */
    public void run() throws VisionException {
        CommandRunnable runnable = getTemplate().getRunnable();
        if(runnable != null){
            runnable.run(new CommandParameters(getValues(),  hatHolder, getRange()));
        }
    }

    @Override
    public CodeRange getRange() {
        return line.getRange();
    }

    public Line getLine() {
        return line;
    }

    public Hat getHatHolder() {
        return hatHolder;
    }
}
