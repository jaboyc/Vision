package com.jlogical.vision.compiler.script.elements;

import com.jlogical.vision.api.elements.CustomCommand;
import com.jlogical.vision.compiler.Line;
import com.jlogical.vision.compiler.values.Value;

import java.util.ArrayList;

/**
 * Command that signifies a reduction in the index during compilation.
 */
public class End extends Command{

    private static CustomCommand endTemplate = new CustomCommand("end", null, null);

    /**
     * Creates a new End in the given line.
     */
    public End(Line line) {
        super(endTemplate, null, line);
    }
}
