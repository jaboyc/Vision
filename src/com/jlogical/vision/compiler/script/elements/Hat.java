package com.jlogical.vision.compiler.script.elements;

import com.jlogical.vision.api.elements.CustomHat;
import com.jlogical.vision.compiler.Input;
import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.compiler.script.Script;
import com.jlogical.vision.compiler.script.Variable;
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
    private ArrayList<Command> commands;

    /**
     * The Script the Hat is in.
     */
    private Script script;

    /**
     * List of variable names for the predefined variables in the hat.
     */
    private ArrayList<String> variableNames;

    /**
     * List of local Variables in the Hat.
     */
    private ArrayList<Variable> variables;

    /**
     * The output of the hat. Used when defining reporters.
     */
    private Object output;

    /**
     * Whether this hat is running. If set to false during run(), stops the hat from running.
     */
    private boolean running;

    /**
     * Creates a new Hat with a core.
     *
     * @param hat the custom hat to model this hat on.
     * @param inputs the inputs of the hat. Used for getting variable names.
     * @param script the script this hat belongs to.
     */
    public Hat(CustomHat hat, ArrayList<Input> inputs, Script script) {
        super(hat, null);
        this.script = script;
        this.commands = new ArrayList<>();
        this.variables = new ArrayList<>();
        initVariableNames(inputs);
        running = false;
    }

    /**
     * Returns a special hat that is for custom definitions of commands and reporters.
     *
     * @return the hat.
     */
    public static Hat defineTemplateHat(Script script) {
        return new Hat(null, null, script);
    }

    /**
     * Runs the Hat.
     *
     * @param inputs inputs of the hat.
     * @throws VisionException if there is an error running any of the Commands in the Hat.
     */
    public void run(Object[] inputs) throws VisionException {
        setupInputs(inputs);

        running = true;
        for (Command command : commands) {
            if (!running) return;
            command.run();
        }
        running = false;
    }

    /**
     * Sets up the inputs of the hat with the given inputs.
     * @param inputs the inputs to initialize the hat variables to.
     */
    private void setupInputs(Object[] inputs){

        // Don't do anything if there are no inputs.
        if(inputs == null) return;

        // Check for invalid argument size.
        if(inputs.length != variableNames.size())
            throw new IllegalArgumentException("Cannot have different sizes of inputs and variable names");

        // Add new a new variable for each input.
        for(int i=0;i<variableNames.size();i++)
            variables.add(new Variable(variableNames.get(i), inputs[i]));
    }

    /**
     * Sets up the variable names of the hat with the given inputs in the line the hat was declared in.
     *
     * @param inputs the inputs to set up.
     */
    private void initVariableNames(ArrayList<Input> inputs) {
        variableNames = new ArrayList<>();
        if(inputs == null) return;
        for(Input input : inputs)
            variableNames.add(input.getText());
    }

    public Script getScript() {
        return script;
    }

    /**
     * Adds a Command to the Hat.
     *
     * @param command the Command to add.
     */
    public void addCommand(Command command) {
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

    /**
     * Stops the hat from continuing to run.
     */
    public void stop() {
        running = false;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }
}
