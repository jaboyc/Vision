package com.jlogical.vision.compiler;

/**
 * Stores the compiled information for a Project. Cannot be back-compiled. Stored as a .vis file.
 */
public class Script {
    /**
     * The compilation log associated with compiling this Script.
     */
    String compileLog;

    /**
     * Creates a new Script.
     */
    private Script(String compileLog){
        this.compileLog = compileLog != null ? compileLog : "";
    }

    /**
     * Returns a Script that only has a compileScript attached due to failure to compile.
     * @return the Script.
     */
    public static Script failedScript(String compileLog){
        return new Script(compileLog);
    }

    /**
     * @return the Script was successfully compiled.
     */
    public boolean compileSuccess(){
        return true;
    }
}
