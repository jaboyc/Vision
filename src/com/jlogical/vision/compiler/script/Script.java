package com.jlogical.vision.compiler.script;

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
     * Whether this Script was successfully compiled.
     */
    private boolean succeeded;

    /**
     * Creates a new Script.
     */
    private Script(String compileLog) {
        this.compileLog = compileLog != null ? compileLog : "";
    }

    /**
     * Returns a Script that only has a compileScript attached due to failure to compile.
     *
     * @return the Script.
     */
    public static Script failedScript(String compileLog) {
        Script script = new Script(compileLog);
        script.succeeded = false;
        return script;
    }

    public String getCompileLog() {
        return compileLog;
    }

    public boolean succeeded() {
        return succeeded;
    }
}
