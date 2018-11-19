package com.jlogical.vision.api;

import com.jlogical.vision.api.elements.CustomCommand;
import com.jlogical.vision.api.elements.CustomHat;
import com.jlogical.vision.api.elements.CustomReporter;
import com.jlogical.vision.api.runnables.CommandRunnable;
import com.jlogical.vision.api.runnables.ReporterRunnable;
import com.jlogical.vision.project.Project;

import java.util.ArrayList;

/**
The abstract class for an API. Defines the commands, objects, and functions for an API.
 */
public abstract class API {

    /**
     * The Project the API is in.
     */
    private Project project;

    /**
     * A List of Commands in this API.
     */
    private ArrayList<CustomCommand> commands;

    /**
     * A List of Hats in this API.
     */
    private ArrayList<CustomHat> hats;

    /**
     * A List of Reporters in this API.
     */
    private ArrayList<CustomReporter> reporters;

    /**
     * Creates a blank API with a given Project root.
     *
     * Add CustomCommands, CustomHats, CustomReporters by calling .add() in the Constructor.
     */
    public API(Project project){
        this.project = project;
        this.commands = new ArrayList<>();
        this.hats = new ArrayList<>();
        this.reporters = new ArrayList<>();
    }

    /**
     * Adds a new CustomCommand to this API.
     *
     * @param core the core of the Command.
     * @param runnable the action that runs when the command is called.
     * @return the CustomCommand that was created.
     */
    protected CustomCommand addCommand(String core, CommandRunnable runnable){
        CustomCommand command = new CustomCommand(core != null ? core : "", runnable != null ? runnable : p->{}, this);
        commands.add(command);
        return command;
    }

    /**
     * Adds a new CustomHat to this API.
     * @param core the core of the Hat.
     * @return the CustomHat that was created.
     */
    protected CustomHat addHat(String core){
        CustomHat hat = new CustomHat(core != null ? core : "", this);
        hats.add(hat);
        return hat;
    }

    /**
     * Adds a new CustomReporter to this API.
     * @param core the core of the Reporter.
     * @param runnable the action that runs when the reporter is called.
     * @return the CustomReporter that was created.
     */
    protected CustomReporter addReporter(String core, ReporterRunnable runnable){
        CustomReporter reporter = new CustomReporter(core != null ? core : "", runnable, this);
        reporters.add(reporter);
        return reporter;
    }

    public Project getProject() {
        return project;
    }

    public ArrayList<CustomCommand> getCommands() {
        return commands;
    }

    public ArrayList<CustomHat> getHats() {
        return hats;
    }

    public ArrayList<CustomReporter> getReporters() {
        return reporters;
    }
}
