package com.jlogical.vision.compiler.script.elements;

import com.jlogical.vision.api.elements.CustomReporter;
import com.jlogical.vision.api.runnables.ReporterParameters;
import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.compiler.values.Value;
import com.jlogical.vision.project.CodeRange;

import java.util.ArrayList;

/**
 * Piece of code that returns a Value.
 */
public class Reporter extends CompiledElement<CustomReporter> implements Value {

    /**
     * The Command that is holding the Reporter. Null if it is standalone.
     */
    private Command commandHolder;

    /**
     * The range this Reporter occupies.
     */
    private CodeRange range;

    /**
     * Creates a new Reporter based on a template, line, and values.
     */
    public Reporter(CustomReporter template, ArrayList<Value> values, Command commandHolder, CodeRange range) {
        super(template, values);
        this.commandHolder = commandHolder;
        this.range = range;
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
