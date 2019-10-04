package com.jlogical.vision.compiler.definitions;

/**
 * Holds template information for a defined command. Defined by "define command action".
 */
public class DefinedCommand extends DefineTemplate{

    /**
     * The core represented by this definition.
     */
    private String core;

    /**
     * Creates a DefinedCommand with the given core.
     */
    public DefinedCommand(String core){
        this.core = core;
    }
}
