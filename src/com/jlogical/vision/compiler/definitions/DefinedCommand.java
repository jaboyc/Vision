package com.jlogical.vision.compiler.definitions;

import com.jlogical.vision.compiler.Input;
import com.jlogical.vision.compiler.Line;
import com.jlogical.vision.compiler.script.elements.Hat;

import java.util.ArrayList;

/**
 * Holds template information for a defined command. Defined by "define command action".
 */
public class DefinedCommand extends DefineTemplate {

    /**
     * The core represented by this definition.
     */
    private String core;

    /**
     * The hat that is holding the definition of this template.
     */
    private Hat hat;

    /**
     * Creates a DefinedCommand with the given core.
     */
    public DefinedCommand(Line line, String core) {
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
