package com.jlogical.vision.compiler.script.elements;

import com.jlogical.vision.api.elements.CustomCBlock;
import com.jlogical.vision.api.elements.CustomCommand;
import com.jlogical.vision.api.runnables.CBlockParameters;
import com.jlogical.vision.compiler.Line;
import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.compiler.script.Variable;
import com.jlogical.vision.compiler.values.Value;
import com.jlogical.vision.project.CodeRange;

import java.util.ArrayList;

public class CBlock extends Command<CustomCBlock> {

    /**
     * List of Commands that this CBlock can run.
     */
    private ArrayList<Command> commands;

    /**
     * CBlock that is chained at the end of this CBlock.
     */
    private CBlock chain;

    /**
     * List of Variables in this CBlock.
     */
    private ArrayList<Variable> variables;

    /**
     * Whether the CBlock is running.
     */
    private boolean running = false;

    /**
     * Creates a new CompiledElement with a core, template, values, and line.
     */
    public CBlock(CustomCBlock template, ArrayList<Value> values, Line line, Hat hatHolder, ArrayList<Command> commands, CBlock cblockHolder, CBlock chain) {
        super(template, values, line, hatHolder, cblockHolder);
        this.commands = commands != null ? commands : new ArrayList<>();
        this.chain = chain;
        this.variables = new ArrayList<>();
    }

    /**
     * Runs the commands inside this CBlock.
     */
    public void run() throws VisionException {
        running = true;
        getTemplate().getRunnable().run(new CBlockParameters(this, getValues(), getHatHolder(), getRange(), getCBlockHolder()));
        running = false;
    }

    /**
     * Runs all the Commands in this CBlock.
     *
     * @throws VisionException if any of the Commands have an exception when running.
     */
    public void runLoop() throws VisionException {
        running = true;
        for (Command command : getCommands()) {
            command.run();
            if (!running) return;
        }
    }

    /**
     * Stops the loop.
     */
    public void stop() {
        running = false;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<Command> commands) {
        this.commands = commands;
    }

    public CBlock getChain() {
        return chain;
    }

    public void setChain(CBlock chain) {
        this.chain = chain;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public CodeRange getRange() {
        return null;
    }
}
