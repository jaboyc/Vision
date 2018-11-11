package com.jlogical.vision.compiler;

import com.jlogical.vision.compiler.exceptions.CompilerException;
import com.jlogical.vision.project.CodeLocation;
import com.jlogical.vision.project.Project;
import com.jlogical.vision.project.VisionFile;
import com.jlogical.vision.util.Pair;

import java.util.ArrayList;

/**
 * Compiles a Project into a Script.
 */
public class Compiler {

    /**
     * Contains the different levels of detail to be produced in the log.
     */
    public enum Detail {
        NONE, //No info
        BASIC, //Basic info including startup, finish, time, warnings, and errors.
        DEBUG //All of basic and additional debug info.
    }

    /**
     * The project the current Compiler is working with.
     */
    private Project project;

    /**
     * The detail of the compilation messages.
     */
    private Detail outputDetail;

    /**
     * The log of the compilation process.
     */
    private String log;

    /**
     * Private constructor.
     */
    private Compiler(Project project, Detail outputDetail) {
        this.project = project;
        this.outputDetail = outputDetail;
        this.log = "";
    }

    /**
     * Compiles a Project.
     *
     * @return the Script when fully compiled. Will never be null. If it cannot be compiled, an exception is thrown.
     */
    public static Script compile(Project project, Detail outputDetail) {
        return new Compiler(project, outputDetail).compile();
    }

    /**
     * Compiles a Project.
     *
     * @return the Script when fully compiled. Returns a failed script (script.succeeded() == false) if there was an error.
     */
    public static Script compile(Project project) {
        return new Compiler(project, Detail.BASIC).compile();
    }

    /**
     * Compiles a Project.
     *
     * @return the Script when fully compiled. Returns a failed script (script.succeeded() == false) if there was an error.
     */
    private Script compile() {
        if (project == null) {
            return Script.failedScript(logAppendCompilerException("The Project provided was null.", "null project", new CodeLocation(null)));
        }
        if (project.getFiles().isEmpty()) {
            return Script.failedScript(logAppendCompilerException("The Project provided did not have any code.", "empty project", new CodeLocation(project)));
        }
        try {
            ArrayList<Line> lines = precompile();
            compileFiles(lines);
            postCompile();
        } catch (CompilerException e) {
            e.printStackTrace();
            return Script.failedScript(logAppendCompilerException(e));
        }
        return null;
    }

    /**
     * Does everything necessary for the actual compilation.
     * Converts all the VisionFiles into Lines, and merges them together.
     *
     * @throws CompilerException if there was an imbalance in one of the lines.
     */
    private ArrayList<Line> precompile() throws CompilerException {
        ArrayList<Line> lines = new ArrayList<>();
        for (VisionFile vfile : project.getFiles()) {
            lines.addAll(toLines(vfile));
        }
        return lines;
    }

    /**
     * Converts all the Lines into a Script.
     *
     * @param lines the ArrayList of Lines for where to compile from.
     */
    private void compileFiles(ArrayList<Line> lines) {

    }

    /**
     * Does everything post compilation related.
     */
    private void postCompile() {

    }

    /**
     * Converts the given Project into an ArrayList of Lines.
     *
     * @param vfile the VisionFile to convert to lines.
     * @return the converted ArrayList. Null if text is null.
     * @throws CompilerException if a Line is off balance.
     */
    private ArrayList<Line> toLines(VisionFile vfile) throws CompilerException {
        if (vfile == null) {
            return null;
        }
        ArrayList<Line> output = new ArrayList<>();
        ArrayList<String> split = splitFile(vfile);
        for (int i = 0; i < split.size(); i++) {
            String line = split.get(i);
            output.add(toLine(line, i, vfile));
        }
        return output;
    }

    /**
     * Splits the vfile's code by new lines and returns it as an ArrayList of Strings.
     *
     * @param vfile the VisionFile whose code to split. Null if vfile is null.
     */
    private ArrayList<String> splitFile(VisionFile vfile) {
        if (vfile == null) {
            return null;
        }
        ArrayList<String> output = new ArrayList<>();
        String currLine = "";
        for (int i = 0; i < vfile.getCode().length(); i++) {
            char c = vfile.getCode().charAt(i);
            if (c == '\n' || c == '\r') {
                output.add(currLine);
                currLine = "";
            } else {
                currLine += vfile.getCode().charAt(i);
            }
        }
        output.add(currLine);
        return output;
    }

    /**
     * Converts the given String and linenum into a Line for the given VisionFile.
     *
     * @param line    the line to convert.
     * @param lineNum the line number of the given line.
     * @param vfile   the VisionFile the line is found in.
     * @return the Line. Null if line is null.
     * @throws CompilerException if a Line is off balance.
     */
    private Line toLine(String line, int lineNum, VisionFile vfile) throws CompilerException {
        if (line == null) {
            return null;
        }
        CodeLocation location = new CodeLocation(project, vfile, lineNum);
        Pair<String, ArrayList<Pair<String, CodeLocation>>> split = splitElement(line, location);
        return new Line(line, split.getFirst(), split.getSecond(), location);
    }

    /**
     * Splits an element into its imperfect core and inputs.
     *
     * @param element  the element whose text to split.
     * @param location the beginning location of this element.
     * @return a Pair of its core (first) and an ArrayList of its inputs in Pairs (the first is the input itself, second is the CodeLocation for that input). (second).
     * @throws CompilerException if it is off balance.
     */
    private Pair<String, ArrayList<Pair<String, CodeLocation>>> splitElement(String element, CodeLocation location) throws CompilerException {
        String core = "";
        ArrayList<Pair<String, CodeLocation>> inputs = new ArrayList<>();
        String currInput = null;

        int index = 0;
        int pIndex = 0; //Index for ()
        int bIndex = 0; //Index for []
        int cIndex = 0; //Index for {}
        for (int i = 0; i < element.length(); i++) {
            char c = element.charAt(i);
            if (c == ']' || c == '}' || c == ')') {
                switch (c) {
                    case ']':
                        bIndex--;
                        break;
                    case '}':
                        cIndex--;
                        break;
                    case ')':
                        pIndex--;
                        break;
                }
                index--;
                if (index == 0) {
                    inputs.add(new Pair<>(currInput, CodeLocation.copyLine(location, i)));
                    currInput = null;
                }
            }
            if (index == 0) {
                core += c;
            } else {
                currInput += c;
            }
            if (c == '[' || c == '{' || c == '(') {
                switch (c) {
                    case '[':
                        bIndex++;
                        break;
                    case '{':
                        cIndex++;
                        break;
                    case '(':
                        pIndex++;
                        break;
                }
                index++;
                if (currInput == null) {
                    currInput = "";
                }
            }
        }
        if (index == 0 && !(pIndex == 0 && bIndex == 0 && cIndex == 0)) {
            throw new CompilerException("Something strange happened with the index of this line.", "line imbalance", location);
        }
        if (pIndex < 0) {
            throw new CompilerException("There are more ')' than '(' in this line.", "line imbalance", location);
        }
        if (bIndex < 0) {
            throw new CompilerException("There are more ']' than '[' in this line.", "line imbalance", location);
        }
        if (cIndex < 0) {
            throw new CompilerException("There are more '}' than '{' in this line.", "line imbalance", location);
        }
        if (pIndex > 0) {
            throw new CompilerException("There are more '(' than ')' in this line.", "line imbalance", location);
        }
        if (bIndex > 0) {
            throw new CompilerException("There are more '[' than ']' in this line.", "line imbalance", location);
        }
        if (cIndex > 0) {
            throw new CompilerException("There are more '{' than '}' in this line.", "line imbalance", location);
        }
        return new Pair<>(core, inputs);
    }

    /**
     * Returns a CodeLocation for a specific line of code. Used for CompilerException throwing.
     *
     * @param file the file where the error is found.
     * @param line the line where the error is found. -1 if not in a line.
     * @return the CodeLocation.
     */
    private CodeLocation getLineLocation(VisionFile file, int line) {
        return new CodeLocation(project, file, line);
    }

    /**
     * Adds a CompilerMessage to the log of the Compiler.
     *
     * @param message  the message of the error.
     * @param type     the type of the error.
     * @param location the CodeLocation of where the error is found.
     * @return the log.
     */
    private String logAppendCompilerException(String message, String type, CodeLocation location) {
        if (outputDetail == Detail.BASIC || outputDetail == Detail.DEBUG) {
            log += "Compile Exception (" + type + "): (" + location + ")";
            log += "\n\t" + message;
        }
        return log;
    }

    /**
     * Adds a CompilerMessage to the log of the Compiler.
     *
     * @param ce the exception to add.
     * @return the log.
     */
    private String logAppendCompilerException(CompilerException ce) {
        if (ce == null) {
            return log;
        }
        return logAppendCompilerException(ce.getMessage(), ce.getType(), ce.getLocation());
    }
}
