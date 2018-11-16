package com.jlogical.vision.api.elements;

import com.jlogical.vision.api.API;
import com.jlogical.vision.api.runnables.CommandRunnable;

import java.util.ArrayList;

/**
 * Template for a Command.
 */
public class CustomCommand extends CustomElement{

    /**
     * The CommandRunnable to run this CustomCommand with. Can be null.
     */
    private CommandRunnable runnable;

    /**
     * Creates a new CustomElement with a given core and api.
     */
    public CustomCommand(String core, CommandRunnable runnable, API api) {
        super(core, api);
        this.runnable = runnable;
    }

    public CommandRunnable getRunnable() {
        return runnable;
    }
}
