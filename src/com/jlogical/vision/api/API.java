package com.jlogical.vision.api;

import com.jlogical.vision.api.elements.CustomCBlock;
import com.jlogical.vision.api.elements.CustomCommand;
import com.jlogical.vision.api.elements.CustomHat;
import com.jlogical.vision.api.elements.CustomReporter;
import com.jlogical.vision.api.runnables.CBlockRunnable;
import com.jlogical.vision.api.runnables.CommandRunnable;
import com.jlogical.vision.api.runnables.ReporterRunnable;
import com.jlogical.vision.project.Project;

import java.util.ArrayList;
import java.util.Arrays;

/**
The abstract class for an API. Defines the commands, objects, and functions for an API.
 */
public abstract class API {

    /**
     * Project the API is in.
     */
    private Project project;

    /**
     * List of Commands in this API.
     */
    private ArrayList<CustomCommand> commands;

    /**
     * A List of Hats in this API.
     */
    private ArrayList<CustomHat> hats;

    /**
     * List of Reporters in this API.
     */
    private ArrayList<CustomReporter> reporters;

    /**
     * List of CBlocks in this API.
     */
    private ArrayList<CustomCBlock> cblocks;

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
        this.cblocks = new ArrayList<>();
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

    /**
     * Adds a CustomCBlock to this API.
     * @param core the core of the CBlock.
     * @param runnable the action that runs when the cblock is run.
     * @param chains the cores of the CBlocks that chain onto this one.
     * @return the CustomCBlock that was created.
     */
    protected CustomCBlock addCBlock(String core, CBlockRunnable runnable, String... chains){
        CustomCBlock cblock = new CustomCBlock(core != null ? core : "", runnable, new ArrayList<>(Arrays.asList(chains)), this);
        cblocks.add(cblock);
        return cblock;
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
