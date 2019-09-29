package com.jlogical.vision.compiler;

import com.jlogical.vision.api.elements.CustomCBlock;
import com.jlogical.vision.api.elements.CustomCommand;
import com.jlogical.vision.api.elements.CustomHat;
import com.jlogical.vision.api.elements.CustomReporter;
import com.jlogical.vision.compiler.exceptions.CompilerException;
import com.jlogical.vision.compiler.script.Script;
import com.jlogical.vision.compiler.script.elements.*;
import com.jlogical.vision.compiler.values.*;
import com.jlogical.vision.project.CodeLocation;
import com.jlogical.vision.project.CodeRange;
import com.jlogical.vision.project.Project;
import com.jlogical.vision.project.VisionFile;
import com.jlogical.vision.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Compiles a Project into a Script.
 * <p>
 * //TODO During precompile, check for vertical line imbalance. We do not want an error saying 'print []' is not a valid Hat! Should instead say 'There are more 'end's than hats and CBlocks'!
 */
public class Compiler {

    private static final String[] KEYWORDS = {"end", "define", "with", "as", "for", "new"};

    /**
     * Compiles a Project.
     *
     * @return the Script when fully compiled. Returns a failed script if there was an error.
     */
    public static Script compile(Project project) {
        if (project == null) {
            return Script.failedScript("The Project provided did not have any code.");
        }
        if (project.getFiles().isEmpty()) {
            return Script.failedScript("The Project provided did not have any code.");
        }
        try {
            ArrayList<Line> lines = precompile(project);
            Script script = compileLines(lines, project);
            postCompile();
            return script;
        } catch (CompilerException e) {
            e.printStackTrace();
            return Script.failedScript(e.getMessage());
        }
    }

    /**
     * Does everything necessary for the actual compilation.
     * Converts all the VisionFiles into Lines, and merges them together.
     *
     * @param project the Project it is currently compiling.
     * @throws CompilerException if there was an imbalance in one of the lines.
     */
    private static ArrayList<Line> precompile(Project project) throws CompilerException {
        ArrayList<Line> lines = new ArrayList<>();
        for (VisionFile vfile : project.getFiles()) {
            lines.addAll(toLines(vfile, project));
        }
        return lines;
    }

    /**
     * Converts all the Lines into a Script.
     *
     * @param lines the ArrayList of Lines for where to compile from.
     * @return the Script.
     * @throws CompilerException if an exception has occurred.
     */
    private static Script compileLines(ArrayList<Line> lines, Project project) throws CompilerException {
        Script script = Script.blank();

        ArrayList<Hat> hats = new ArrayList<>();
        Hat currHat = null;
        Stack<CBlock> cblocks = new Stack<>();
        CBlock currCBlock = null;
        int index = 0;
        for (Line line : lines) {
            if (index == 0) {
                currHat = toHat(line, project, script);
                hats.add(currHat);
                index++;
            } else if (index > 0) {
                Command command = toCommand(line, currHat, currCBlock, project);
                if (currHat == null) {
                    throw new CompilerException("Cannot compile a Command without a Hat!", "error", line.getRange());
                }
                if (currCBlock != null) {
                    currCBlock.getCommands().add(command);
                } else {
                    currHat.addCommand(command);
                }
                if (command instanceof End) {
                    index--;
                    if (!cblocks.isEmpty()) {
                        cblocks.pop();
                        currCBlock = cblocks.isEmpty() ? null : cblocks.peek();
                    }
                }
                if (command instanceof CBlock) {
                    CBlock cblock = (CBlock) command;
                    if (currCBlock != null && currCBlock.getTemplate().getChains().contains(command.getCore())) { //If the new CBlock is a chain...
                        currCBlock.setChain(cblock);
                        cblocks.pop();
                        if (!currCBlock.getCommands().isEmpty()) {
                            currCBlock.getCommands().remove(currCBlock.getCommands().size() - 1); //Remove the last Command because the new CBlock was added as a command, not as a chain...
                        }
                    } else {
                        index++;
                    }
                    cblocks.push(cblock);
                    currCBlock = cblock;
                }
            } else {
                throw new CompilerException("There are more 'end's than Hats or CBlocks!", "end imbalance", line.getRange());
            }
        }
        if (index > 0) {
            throw new CompilerException("There are not enough 'end's!", "end imbalance", lines.get(lines.size() - 1).getRange());
        }
        script.setHats(hats);
        script.setCompileLog("");
        return script;
    }

    /**
     * Converts the Line to a Hat.
     *
     * @param line    the Line to convert.
     * @param project the Project it is currently compiling.
     * @param script  the Script that it is currently making.
     * @return the Hat.
     * @throws CompilerException if the Hat is not valid.
     */
    private static Hat toHat(Line line, Project project, Script script) throws CompilerException {
        if (containsKeyword(line.getCore())) {
            throw new IllegalArgumentException("Hat cannot contain a keyword!");
        } else {
            for (CustomHat hat : project.getHats()) {
                if (coreEquals(hat.getCore(), line.getCore())) {
                    return new Hat(hat, null, script);
                }
            }
        }
        throw new CompilerException(line.getCode() + " is not a valid Hat!", "invalid code", line.getRange());
    }

    /**
     * Compiles the Line and returns a Command version of it.
     *
     * @param line         the Line to compile.
     * @param hatHolder    the Hat that will hold the Command.
     * @param cblockHolder the CBlock that will hold the Command. Null if none.
     * @param project      the Project it is currently compiling.
     * @return the Command.
     * @throws CompilerException if the Command is not valid.
     */
    private static Command toCommand(Line line, Hat hatHolder, CBlock cblockHolder, Project project) throws CompilerException {
        if (containsKeyword(line.getCore())) {
            if (line.getCode().equals("end")) {
                return new End(line);
            }
        }
        for (CustomCommand command : project.getCommands()) {
            if (coreEquals(command.getCore(), line.getCore())) {
                Command c = new Command(command, null, line, hatHolder, cblockHolder);
                c.setValues(toValues(line.getInputs(), c, project));
                return c;
            }
        }
        for (CustomCBlock cblock : project.getCBlocks()) {
            if (coreEquals(cblock.getCore(), line.getCore())) {
                CBlock c = new CBlock(cblock, null, line, hatHolder, null, cblockHolder, null);
                c.setValues(toValues(line.getInputs(), c, project));
                return c;
            }
        }

        throw new CompilerException(line.getCode() + " is not a valid Command!", "invalid code", line.getRange());
    }

    /**
     * Converts the given inputs into Values.
     *
     * @param inputs        the inputs to convert.
     * @param commandHolder the Command that is holding the Values.
     * @param project       the Project it is currently compiling.
     * @return a List of Values. Never will be null.
     * @throws CompilerException if any of the Values produce an error when compiling.
     */
    private static ArrayList<Value> toValues(ArrayList<Input> inputs, Command commandHolder, Project project) throws CompilerException {
        ArrayList<Value> values = new ArrayList<>();
        for (Input input : inputs) {
            values.add(toValue(input, commandHolder, project));
        }
        return values;
    }

    /**
     * Converts the given input to a Value and returns it.
     *
     * @param input         the input to convert.
     * @param commandHolder the Command that is holding the Value.
     * @param project       the Project it is currently compiling.
     * @return the Value.
     * @throws CompilerException if the given input cannot be converted to any value.
     */
    public static Value toValue(Input input, Command commandHolder, Project project) throws CompilerException {
        String val = input.getText();
        switch (input.getType()) {
            case '[':
                return new TextValue(val, input.getRange(), commandHolder, project);
            case '(':
                val = val.trim();
                if (looksNumeric(val)) {
                    try {
                        double d = Double.parseDouble(val);
                        return new NumValue(d, input.getRange());
                    } catch (Exception e) {
                    }
                }
                try {
                    Pair<String, ArrayList<Input>> split = splitElement(val, input.getRange().startLocation());
                    Reporter reporter = toReporter(split.getFirst(), split.getSecond(), commandHolder, input.getRange(), project);
                    if (reporter != null) {
                        return reporter;
                    }
                    if (!split.getSecond().isEmpty() || split.getFirst().matches(".*[+\\-*/^<>=].*")) {
                        return new ExpressionValue(input.getText(), toValues(split.getSecond(), commandHolder, project), input.getRange(), commandHolder);
                    }
                    return new VariableValue(val, input.getRange(), commandHolder);
                } catch (CompilerException e) {
                } catch (Exception e) {
                    e.printStackTrace();
                }
                throw new CompilerException("Value '" + input.getText() + "' cannot be found!", "invalid value", input.getRange());
            case '{':
                val = val.trim();
            default:
                return null;
        }
    }

    /**
     * Finds a CustomReporter and returns the Reporter version of the given core and inputs.
     *
     * @param core          the core of the reporter to find.
     * @param inputs        the inputs of the reporter.
     * @param commandHolder the Command that is holding the Reporter.
     * @param range         the Range of the Reporter.
     * @param project       the Project it is currently compiling.
     * @return the Reporter if successfully able to find the CustomReporter. Null otherwise.
     * @throws CompilerException if any of the inputs contain produce an error when compiling.
     */
    private static Reporter toReporter(String core, ArrayList<Input> inputs, Command commandHolder, CodeRange range, Project project) throws CompilerException {
        if (containsKeyword(core)) {

        }
        for (CustomReporter reporter : project.getReporters()) {
            if (coreEquals(reporter.getCore(), core)) {
                return new Reporter(reporter, toValues(inputs, commandHolder, project), commandHolder, range);
            }
        }
        return null;
    }

    /**
     * Checks if the given value looks like a number based on a regular expression.
     *
     * @param val the value to check.
     * @return whether the value looks like a number.
     */
    private static boolean looksNumeric(String val) {
        return val.matches("-?\\d+(\\.\\d+)?");
    }

    /**
     * Returns whether the line has a keyword.
     *
     * @param core the core of the command or reporter to check.
     * @return true if the Line has a keyword.
     */
    private static boolean containsKeyword(String core) {
        String[] split = core.split(" "); //Splits up the line's code into separate words.
        List<String> splitList = new ArrayList<>(Arrays.asList(split)); //Converts the array into a list.
        splitList.retainAll(Arrays.asList(KEYWORDS)); //Intersects the first list with the keywords list.
        return !splitList.isEmpty(); //Returns if the splitList has anything inside it, meaning there was a match.
    }

    /**
     * Compares the cores of two elements and returns whether they are equal to each other.
     *
     * @param perfect the core you are using to test against.
     * @param test    the test core you are using to test with.
     * @return whether the cores are equal.
     */
    private static boolean coreEquals(String perfect, String test) {
        int pIndex = 0; //Current index of perfect.
        int tIndex = 0; //Current index of test.
        while (pIndex < perfect.length() && tIndex < test.length()) {
            char pChar = perfect.charAt(pIndex);
            char tChar = test.charAt(tIndex);

            switch (pChar) {
                case '[':
                    if (!(tChar == '[' || tChar == '(' || tChar == '{')) { //Can be any parameter.
                        return false;
                    }
                    break;
                case '(':
                    if (tChar != '(') { //Can only be objects.
                        return false;
                    }
                case '{':
                    if (!(tChar == '{' || tChar == '(')) { //Can be statements or a reference to a statement.
                        return false;
                    }
                case ']':
                case ')':
                case '}':
                    break;
                default:
                    if (tChar != pChar) {
                        return false;
                    }
            }

            pIndex++;
            tIndex++;
        }
        return pIndex == perfect.length() && tIndex == test.length();
    }

    /**
     * Does everything post compilation related.
     */
    private static void postCompile() {

    }

    /**
     * Converts the given Project into an ArrayList of Lines.
     *
     * @param vfile   the VisionFile to convert to lines.
     * @param project the Project it is currently compiling.
     * @return the converted ArrayList. Null if text is null.
     * @throws CompilerException if a Line is off balance.
     */
    private static ArrayList<Line> toLines(VisionFile vfile, Project project) throws CompilerException {
        if (vfile == null) {
            return null;
        }
        ArrayList<Line> output = new ArrayList<>();
        ArrayList<String> split = splitFile(vfile);
        for (int i = 0; i < split.size(); i++) {
            String line = split.get(i);
            if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                continue;
            }
            output.add(toLine(line, i + 1, vfile, project));
        }
        return output;
    }

    /**
     * Splits the vfile's code by new lines and returns it as an ArrayList of Strings. Automatically trims the lines when split.
     *
     * @param vfile the VisionFile whose code to split. Null if vfile is null.
     */
    private static ArrayList<String> splitFile(VisionFile vfile) {
        if (vfile == null) {
            return null;
        }
        ArrayList<String> output = new ArrayList<>();
        String currLine = "";
        for (int i = 0; i < vfile.getCode().length(); i++) {
            char curr = vfile.getCode().charAt(i);
            char next = i + 1 == vfile.getCode().length() ? '_' : vfile.getCode().charAt(i + 1);

            if (curr == '\\' && (next == '\n' || next == '\r')) { // Multi-lined command.
                i += 2;
            } else if ((curr == '\n' || curr == '\r')) {
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
     * @param project the Project it is currently compiling.
     * @return the Line. Null if line is null.
     * @throws CompilerException if a Line is off balance.
     */
    private static Line toLine(String line, int lineNum, VisionFile vfile, Project project) throws CompilerException {
        if (line == null) {
            return null;
        }
        CodeLocation location = new CodeLocation(project, vfile, lineNum, 0);
        Pair<String, ArrayList<Input>> split = splitElement(line, location);
        return new Line(line, split.getFirst(), split.getSecond(), location);
    }

    /**
     * Splits an element into its imperfect core and inputs.
     *
     * @param element  the element whose text to split.
     * @param location the beginning location of this element.
     * @return a Pair of its core (first) and an ArrayList of its inputs in Pairs (the first is the input itself, second is the CodeRange for that input). (second).
     * @throws CompilerException if it is off balance.
     */
    private static Pair<String, ArrayList<Input>> splitElement(String element, CodeLocation location) throws CompilerException {
        String core = "";
        ArrayList<Input> inputs = new ArrayList<>();
        String currInput = null; //The current input.
        Stack<Character> inputTypes = new Stack<>(); //List of the types of parameters used.

        int index = 0;
        int pIndex = 0; //Index for ()
        int cIndex = 0; //Index for {}

        boolean inString = false;
        boolean hadInterpolation = false; //Whether this element had interpolation. If so, make the input type '#' instead of '['.
        for (int i = 0; i < element.length(); i++) {
            char c = element.charAt(i);
            if (inString) {
                if (c == ']') { //If ending the text.
                    inString = false;
                } else {
                    currInput += c;
                }
            }
            if (!inString) {
                if (c == ']' || c == '}' || c == ')') {
                    if (inputTypes.isEmpty()) { //Error. Too many closing parameters.
                        switch (c) {
                            case ']':
                                throw new CompilerException("There are more ']' than '[' in this line.", "line imbalance", new CodeRange(location.getProject(), location.getFile(), location.getLineNum(), location.getCharNum(), location.getLineNum(), location.getCharNum() + element.length()));
                            case ')':
                                pIndex--;
                            case '}':
                                cIndex--;
                        }
                        if (pIndex < 0) {
                            throw new CompilerException("There are more ')' than '(' in this line.", "line imbalance", new CodeRange(location.getProject(), location.getFile(), location.getLineNum(), location.getCharNum(), location.getLineNum(), location.getCharNum() + element.length()));
                        }
                        if (cIndex < 0) {
                            throw new CompilerException("There are more '}' than '{' in this line.", "line imbalance", new CodeRange(location.getProject(), location.getFile(), location.getLineNum(), location.getCharNum(), location.getLineNum(), location.getCharNum() + element.length()));
                        }
                    }
                    char lastInputType = inputTypes.pop();
                    switch (c) {
                        case ']':
                            if (lastInputType != '[') {
                                throw new CompilerException("Parameters must match one another. Cannot have " + lastInputType + " matched with ]", "parameter mismatch", new CodeRange(location.getProject(), location.getFile(), location.getLineNum(), location.getCharNum(), location.getLineNum(), location.getCharNum() + element.length()));
                            }
                            break;
                        case '}':
                            cIndex--;
                            if (lastInputType != '{') {
                                throw new CompilerException("Parameters must match one another. Cannot have " + lastInputType + " matched with }", "parameter mismatch", new CodeRange(location.getProject(), location.getFile(), location.getLineNum(), location.getCharNum(), location.getLineNum(), location.getCharNum() + element.length()));
                            }
                            break;
                        case ')':
                            pIndex--;
                            if (lastInputType != '(') {
                                throw new CompilerException("Parameters must match one another. Cannot have " + lastInputType + " matched with )", "parameter mismatch", new CodeRange(location.getProject(), location.getFile(), location.getLineNum(), location.getCharNum(), location.getLineNum(), location.getCharNum() + element.length()));
                            }
                            break;
                    }
                    index--;
                    if (index == 0) {
                        inputs.add(new Input(currInput, new CodeRange(location.getProject(), location.getFile(), location.getLineNum(), i - currInput.length(), location.getLineNum(), i - 1), hadInterpolation ? '#' : lastInputType));
                        currInput = null;
                    } else if (index < 0) {
                        throw new CompilerException("Cannot have a closing parameter (']', ')', or '}') without an opening parameter ('[', '(', or '{') first.", "line imbalance", new CodeRange(location.getProject(), location.getFile(), location.getLineNum(), location.getCharNum(), location.getLineNum(), location.getCharNum() + element.length()));
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
                            inString = true;
                            break;
                        case '{':
                            cIndex++;
                            break;
                        case '(':
                            pIndex++;
                            break;
                    }
                    inputTypes.push(c);
                    index++;
                    if (currInput == null) {
                        currInput = "";
                    }
                }
            }

        }
        if (index == 0 && !(pIndex == 0 && cIndex == 0)) {
            throw new CompilerException("Something strange happened with the index of this line.", "line imbalance", new CodeRange(location.getProject(), location.getFile(), location.getLineNum(), location.getCharNum(), location.getLineNum(), location.getCharNum() + element.length()));
        }
        if (pIndex < 0) {
            throw new CompilerException("There are more ')' than '(' in this line.", "line imbalance", new CodeRange(location.getProject(), location.getFile(), location.getLineNum(), location.getCharNum(), location.getLineNum(), location.getCharNum() + element.length()));
        }
        if (cIndex < 0) {
            throw new CompilerException("There are more '}' than '{' in this line.", "line imbalance", new CodeRange(location.getProject(), location.getFile(), location.getLineNum(), location.getCharNum(), location.getLineNum(), location.getCharNum() + element.length()));
        }
        if (pIndex > 0) {
            throw new CompilerException("There are more '(' than ')' in this line.", "line imbalance", new CodeRange(location.getProject(), location.getFile(), location.getLineNum(), location.getCharNum(), location.getLineNum(), location.getCharNum() + element.length()));
        }
        if (cIndex > 0) {
            throw new CompilerException("There are more '{' than '}' in this line.", "line imbalance", new CodeRange(location.getProject(), location.getFile(), location.getLineNum(), location.getCharNum(), location.getLineNum(), location.getCharNum() + element.length()));
        }

        core = core.trim().replaceAll(" +", " ");
        return new Pair<>(core, inputs);
    }
}
