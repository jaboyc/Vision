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
public class Command<T extends CustomCommand> extends CompiledElement<T> {

    /**
     * The line that this Command was found in.
     */
    private Line line;

    /**
     * The Hat that is holding this Command.
     */
    private Hat hatHolder;

    /**
     * CBlock that is holding this Command. Null if none.
     */
    private CBlock cblockHolder;

    /**
     * Creates a new CompiledElement with a core, template, values, and line.
     */
    public Command(T template, ArrayList<Value> values, Line line, Hat hatHolder, CBlock cblockHolder) {
        super(template, values);
        this.line = line;
        this.hatHolder = hatHolder;
        this.cblockHolder = cblockHolder;
    }

    /**
     * Runs the Command.
     *
     * @throws VisionException if there was an error running the Command.
     */
    public void run() throws VisionException {
        CommandRunnable runnable = getTemplate().getRunnable();
        if(runnable != null){
            runnable.run(new CommandParameters(this, getValues(),  hatHolder, getRange(), cblockHolder));
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
