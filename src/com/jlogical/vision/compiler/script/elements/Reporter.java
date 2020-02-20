package com.jlogical.vision.compiler.script.elements;

import com.jlogical.vision.api.elements.CustomCommand;
import com.jlogical.vision.api.elements.CustomReporter;
import com.jlogical.vision.api.runnables.CommandParameters;
import com.jlogical.vision.api.runnables.ReporterParameters;
import com.jlogical.vision.compiler.Line;
import com.jlogical.vision.compiler.definitions.DefinedCommand;
import com.jlogical.vision.compiler.definitions.DefinedReporter;
import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.compiler.script.Variable;
import com.jlogical.vision.compiler.values.Value;
import com.jlogical.vision.project.CodeRange;

import java.util.ArrayList;

/**
 * Piece of code that returns a Value.
 */
public class Reporter extends CompiledElement<CustomReporter> implements Value {

    /**
     * Custom command that represents a command running a defined template command.
     */
    private static CustomReporter definedCustomReporter = new CustomReporter("", e -> Reporter.runDefinedReporter(e), null);

    /**
     * The Command that is holding the Reporter. Null if it is standalone.
     */
    private Command commandHolder;

    /**
     * The range this Reporter occupies.
     */
    private CodeRange range;

    /**
     * If this reporter will run a defined reporter, it is stored here.
     */
    private DefinedReporter definedReporter;

    /**
     * Creates a new Reporter based on a template, line, and values.
     */
    public Reporter(CustomReporter template, ArrayList<Value> values, Command commandHolder, CodeRange range) {
        super(template, values);
        this.commandHolder = commandHolder;
        this.range = range;
    }

    /**
     * Returns a special reporter for running defined reporters.
     */
    public static Reporter definedReporter(DefinedReporter definedReporter, ArrayList<Value> values, Command commandHolder, CodeRange range) {
        Reporter reporter = new Reporter(definedCustomReporter, values, commandHolder, range);
        reporter.definedReporter = definedReporter;

        return reporter;
    }

    /**
     * Runs a defined command based on the parameters given by e.
     */
    private static Object runDefinedReporter(ReporterParameters e) throws VisionException{
        Reporter reporter = e.getElement();
        Hat hat = reporter.definedReporter.getHat();

//        hat.getVariables().clear();
//        for(int i=0;i<reporter.getValues().size();i++){
//            hat.getVariables().add(new Variable(reporter.definedReporter.getVariableNames().get(i), e.getValues().get(i).getValue()));
//        }


        ArrayList<Object> inputs = new ArrayList<>();
        for(int i = 0;i<e.getValues().size();i++)
            inputs.add(e.get(i));
        hat.run(inputs.toArray());
        return hat.getOutput();
    }

    @Override
    public Object getValue() throws VisionException {
        return getTemplate().getRunnable().getValue(new ReporterParameters(this, getValues(), commandHolder, getRange()));
    }

    @Override
    public CodeRange getRange() {
        return range;
    }
}
