package com.jlogical.vision.api.runnables;

/**
 * A lambda for running Commands.
 */
public interface CommandRunnable {
    /**
     * Action to run the Command with.
     * @param p the Parameters of the Command.
     */
    void run(Parameters p);
}
