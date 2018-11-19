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
     * Creates a new Hat with a core and a List of Commands.
     */
    public Hat(CustomHat hat, ArrayList<Command> commands) {
        super(hat, null);
        this.commands = commands != null ? commands : new ArrayList<>();
    }

    /**
     * Runs the Hat.
     */
    public void run(){
        for(Command command:commands){
            command.run();
        }
    }

    /**
     * Adds a Command to the Hat.
     * @param command the Command to add.
     */
    public void addCommand(Command command){
        commands.add(command);
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    @Override
    public CodeRange getRange() {
        if (commands == null || commands.isEmpty()) {
            return null;
        }
        return CodeRange.between(commands.get(0).getRange().startLocation(), commands.get(commands.size() - 1).getRange().endLocation());
    }
}
