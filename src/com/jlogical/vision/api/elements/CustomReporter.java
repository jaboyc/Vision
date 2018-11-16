package com.jlogical.vision.api.elements;

import com.jlogical.vision.api.API;
import com.jlogical.vision.api.runnables.ReporterRunnable;
import com.jlogical.vision.compiler.values.Value;

public class CustomReporter extends CustomElement implements Value {

    /**
     * The lambda that gets the value of the Reporter.
     */
    ReporterRunnable runnable;

    /**
     * Creates a new CustomReporter with a given core and api.
     */
    public CustomReporter(String core, ReporterRunnable runnable, API api) {
        super(core, api);
        this.runnable = runnable;
    }

    @Override
    public Object getValue() {
        //TODO
        return null;
    }
}
