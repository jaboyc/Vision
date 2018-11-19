package com.jlogical.vision.compiler.script;

import com.jlogical.vision.compiler.script.elements.Hat;

import java.util.ArrayList;

/**
 * Stores the compiled information for a Project. Cannot be back-compiled. Stored as a .vis file.
 */
public class Script {
    /**
     * The compilation log associated with compiling this Script.
     */
    private String compileLog;

    /**
     * List of Hats in the Script.
     */
    private ArrayList<Hat> hats;

    /**
     * Whether this Script was successfully compiled.
     */
    private boolean succeeded;

    /**
     * Creates a new Script.
     */
    public Script(String compileLog, ArrayList<Hat> hats) {
        this.compileLog = compileLog != null ? compileLog : "";
        this.hats = hats != null ? hats : new ArrayList<>();
        succeeded = true;
    }

    /**
     * Returns a Script that only has a compileScript attached due to failure to compile.
     *
     * @return the Script.
     */
    public static Script failedScript(String compileLog) {
        Script script = new Script(compileLog, null);
        script.succeeded = false;
        return script;
    }

    /**
     * Starts a Hat with the given core.
     * @param core the core of the Hat to run.
     */
    public void start(String core){
        for(Hat hat: hats){
            if(hat.getCore().equals(core)){
                hat.run();
            }
        }
    }

    /**
     * Starts the Script with the 'when started' Hat.
     */
    public void start(){
        start("when started");
    }

    public String getCompileLog() {
        return compileLog;
    }

    public boolean succeeded() {
        return succeeded;
    }

    public ArrayList<Hat> getHats() {
        return hats;
    }
}
