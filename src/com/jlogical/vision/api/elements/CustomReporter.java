package com.jlogical.vision.api.elements;

import com.jlogical.vision.api.API;
import com.jlogical.vision.api.runnables.ReporterRunnable;
import com.jlogical.vision.compiler.values.Value;
import com.jlogical.vision.project.CodeRange;

public class CustomReporter extends CustomElement {

    /**
     * The lambda that gets the value of the Reporter.
     */
    private ReporterRunnable runnable;

    /**
     * Creates a new CustomReporter with a given core and api.
     */
    public CustomReporter(String core, ReporterRunnable runnable, API api) {
        super(core, api);
        this.runnable = runnable;
    }
}
