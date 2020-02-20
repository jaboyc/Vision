package com.jlogical.vision.compiler.definitions;

import com.jlogical.vision.compiler.Line;
import com.jlogical.vision.compiler.script.elements.Hat;

/**
 * Holds template information for a defined reporter. Defined by "define reporter name".
 */
public class DefinedReporter extends DefineTemplate{

    /**
     * The core represented by this definition.
     */
    private String core;

    /**
     * The hat that is holding the definition of this template.
     */
    private Hat hat;

    /**
     * Creates a DefinedReporter with the given line and core.
     */
    public DefinedReporter(Line line, String core) {
        super(line);
        this.core = core;
        hat = null;
    }

    public Hat getHat() {
        return hat;
    }

    public void setHat(Hat hat) {
        this.hat = hat;
    }

    public String getCore() {
        return core;
    }
}
