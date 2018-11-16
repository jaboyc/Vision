package com.jlogical.vision.api.elements;

import com.jlogical.vision.api.API;

/**
 * Model for a template for a CompiledElement.
 */
public abstract class CustomElement {

    /**
     * The API this CustomCommand is part of.
     */
    private API api;

    /**
     * The core of the CustomCommand.
     * <p>
     * Parameter types are [], (), or {}.
     * [] = Anything
     * () = Object
     * {} = Statement
     * <p>
     * Examples: "print []", "for each [] of () do {}", "set [] to []"
     */
    private String core;

    /**
     * Creates a new CustomElement with a given core and api.
     */
    public CustomElement(String core, API api){
        this.core = core != null ? core : "";
        this.api = api;
    }


    public API getApi() {
        return api;
    }

    public String getCore() {
        return core;
    }
}
