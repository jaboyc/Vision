package com.jlogical.vision.compiler.values;

import com.jlogical.vision.compiler.Compiler;
import com.jlogical.vision.compiler.Input;
import com.jlogical.vision.compiler.exceptions.CompilerException;
import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.compiler.script.elements.Command;
import com.jlogical.vision.project.CodeRange;
import com.jlogical.vision.project.Project;

/**
 * Value that holds a String.
 */
public class TextValue implements Value {

    /**
     * List of characters that break a string interpolation variable.
     */
    private static String varBreaks = " +-*/=#.!?~\"'[]{}()";

    /**
     * The String holding the value of the text. Hashtags indicate interpolation.
     */
    private String value;

    /**
     * The range the TextValue is in.
     */
    private CodeRange range;

    /**
     * Command the TextValue is in. Used to get values in text interpolation.
     */
    private Command commandHolder;

    /**
     * The Project the TextValue is in.
     */
    private Project project;

    /**
     * Creates a new TextValue with a given value, range, command.
     */
    public TextValue(String value, CodeRange range, Command commandHolder, Project project) {
        this.value = value != null ? value : "";
        this.range = range;
        this.commandHolder = commandHolder;
        this.project = project;
    }

    @Override
    public Object getValue() throws VisionException {
        String output = ""; //The current output text.
        String currVar = null; //The current variable name. Null if not inside a variable now.

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '#') { //Text interpolation has started.
                if (currVar != null) { //Double hashtag, escape sequence for one hashtag.
                    output += "#";
                    currVar = null;
                } else {
                    currVar = "";
                }
            } else if (currVar != null) { //Inside of interpolation.
                if (varBreaks.contains(c + "")) { //Spaces indicate end of interpolation.
                    if(currVar.isBlank()){
                        throw new VisionException("Value in text interpolation cannot be blank!", getRange());
                    }
                    try {
                        Value v = Compiler.toValue(new Input(currVar, getRange(), '('), commandHolder, project);
                        output += v.getValue().toString() + c;
                    } catch (CompilerException e) {
                        throw new VisionException(e.getMessage(), getRange());
                    }
                    currVar = null;
                } else {
                    currVar += c;
                }
            } else {
                output += c;
            }
        }

        if (currVar != null) { //If it ends with a Variable name.
            if(currVar.isBlank()){
                throw new VisionException("Value in text interpolation cannot be blank!", getRange());
            }
            try {
                Value v = Compiler.toValue(new Input(currVar, getRange(), '('), commandHolder, project);
                output += v.getValue().toString();
            } catch (CompilerException e) {
                throw new VisionException(e.getMessage(), getRange());
            }
        }

        return output;
    }

    @Override
    public CodeRange getRange() {
        return range;
    }
}
