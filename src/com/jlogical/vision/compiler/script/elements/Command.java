package com.jlogical.vision.compiler.script.elements;

import com.jlogical.vision.api.elements.CustomCommand;
import com.jlogical.vision.api.runnables.CommandParameters;
import com.jlogical.vision.api.runnables.CommandRunnable;
import com.jlogical.vision.compiler.Input;
import com.jlogical.vision.compiler.Line;
import com.jlogical.vision.compiler.definitions.DefinedCommand;
import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.compiler.script.Variable;
import com.jlogical.vision.compiler.values.Value;
import com.jlogical.vision.project.CodeRange;

import java.util.ArrayList;

/**
 * Contains information for a compiled command which can be executed later.
 */
public class Command<T extends CustomCommand> extends CompiledElement<T> {

    /**
     * Custom command that represents a command running a defined template command.
     */
    private static CustomCommand definedCustomCommand = new CustomCommand("", e -> Command.runDefinedCommand(e), null);

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
     * If this command will run a defined command, it is stored here.
     */
    private DefinedCommand definedCommand;

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
     * Returns a special command for running defined commands.
     */
    public static Command definedCommand(DefinedCommand definedCommand, ArrayList<Value> values, Line line, Hat hatHolder, CBlock cblockHolder) {
        Command command = new Command(definedCustomCommand, values, line, hatHolder, cblockHolder);
        command.definedCommand = definedCommand;

        return command;
    }

    /**
     * Runs the Command.
     *
     * @throws VisionException if there was an error running the Command.
     */
    public void run() throws VisionException {
        CommandRunnable runnable = getTemplate().getRunnable();
        if (runnable != null) {
            runnable.run(new CommandParameters(this, getValues(), hatHolder, getRange(), cblockHolder));
        }
    }

    /**
     * Runs a defined command based on the parameters given by e.
     */
    private static void runDefinedCommand(CommandParameters e) throws VisionException{
        Command command = (Command) e.getElement();
        Hat hat = command.definedCommand.getHat();

        hat.getVariables().clear();
        for(int i=0;i<command.getValues().size();i++){
            hat.getVariables().add(new Variable(command.definedCommand.getVariableNames().get(i), ((Value) e.getValues().get(i)).getValue()));
        }

        hat.run(null);
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

    public CBlock getCBlockHolder() {
        return cblockHolder;
    }
}
