package com.jlogical.vision.api;

import com.jlogical.vision.api.elements.CustomCommand;
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
     * An Arraylist of Commands in this API.
     */
    private ArrayList<CustomCommand> commands;



}
