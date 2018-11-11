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
     * @throws CompilerException if an exception with compiling has occurred.
     */
    public static Script compile(Project project, Detail outputDetail) throws CompilerException {
        return new Compiler(project, outputDetail).compile();
    }

    /**
     * Compiles a Project.
     *
     * @return the Script when fully compiled. Will never be null. If it cannot be compiled, an exception is thrown.
     * @throws CompilerException if an exception with compiling has occurred.
     */
    public static Script compile(Project project) throws CompilerException {
        return new Compiler(project, Detail.BASIC).compile();
    }

    /**
     * Compiles a Project.
     *
     * @return the Script when fully compiled. Will never be null. If it cannot be compiled, an exception is thrown.
     * @throws CompilerException if an exception with compiling has occurred.
     */
    private Script compile() throws CompilerException {
        if (project == null) {
            return Script.failedScript(logAppendCompilerException("The Project provided was null.", "null project", new CodeLocation(null)));
        }
        if (project.getFiles().isEmpty()) {
            return Script.failedScript(logAppendCompilerException("The Project provided did not have any code.", "empty project", new CodeLocation(project)));
        }
        ArrayList<Line> lines = new ArrayList<>();
        for (VisionFile vfile : project.getFiles()) {
            lines.addAll(toLines(vfile));
        }
        return null;
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
        Pair<String, ArrayList<String>> split = splitElement(line);
        return new Line(line, split.getFirst(), split.getSecond(), new CodeLocation(project, vfile, lineNum));
    }

    /**
     * Splits an element into its imperfect core and inputs.
     *
     * @param element the element whose text to split.
     * @return a Pair of its core (first) and an ArrayList of its inputs in String format (second).
     * @throws CompilerException if it is off balance.
     */
    private Pair<String, ArrayList<String>> splitElement(String element) throws CompilerException {
        String core = "";
        ArrayList<String> inputs = new ArrayList<>();
        String currInput = null;
        int index = 0;
        for (int i = 0; i < element.length(); i++) {
            char c = element.charAt(i);
            if(c==']' || c=='}' || c==')'){
                index--;
                if(index == 0){
                    inputs.add(currInput);
                    currInput = null;
                }
            }
            if(index == 0){
                core += c;
            }else{
                currInput += c;
            }
            if(c=='[' || c=='{' || c=='('){
                index++;
                if(currInput == null){
                    currInput = "";
                }
            }
        }
        if(index < 0){

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
}
