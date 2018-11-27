package com.jlogical.vision.compiler.values;

import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.compiler.script.Variable;
import com.jlogical.vision.compiler.script.elements.CBlock;
import com.jlogical.vision.compiler.script.elements.Hat;
import com.jlogical.vision.project.CodeRange;

/**
 * Value that holds a String.
 */
public class TextValue implements Value {

    /**
     * The String holding the value of the text. Hashtags indicate interpolation.
     */
    private String value;

    /**
     * The range the TextValue is in.
     */
    private CodeRange range;

    /**
     * CBlock the TextValue is in. Used to find local variables. Null if none.
     */
    private CBlock cblock;

    /**
     * Hat the TextValue is in. Used to find global variables.
     */
    private Hat hat;

    /**
     * Creates a new TextValue with a given value, range, cblock, and hat.
     */
    public TextValue(String value, CodeRange range, CBlock cblock, Hat hat) {
        this.value = value != null ? value : "";
        this.range = range;
        this.cblock = cblock;
        this.hat = hat;
    }

    @Override
    public Object getValue() throws VisionException {
        String output = ""; //The current output text.
        String currVar = null; //The current variable name. Null if not inside a variable now.
        boolean complex = false; //Whether the interpolation is inside of brackets or not.

        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '#') { //Text interpolation has started.
                if (currVar != null) { //Double hashtag, escape sequence for one hashtag.
                    output += "#";
                    currVar = null;
                } else {
                    currVar = "";
                }
            }else if (currVar != null) { //Inside of interpolation.
                if (c == ' ') { //Spaces indicate end of interpolation.
                    Variable v = Variable.findVariable(currVar, cblock, hat);
                    if (v == null) {
                        throw new VisionException("Cannot find variable '" + currVar + "' used for text interpolation!", getRange());
                    }
                    output += v.getValue().toString() + " ";
                    currVar = null;
                } else {
                    currVar += c;
                }
            }else{
                output += c;
            }
        }

        if(currVar != null){ //If it ends with a Variable name.
            Variable v = Variable.findVariable(currVar, cblock, hat);
            if (v == null) {
                throw new VisionException("Cannot find variable '" + currVar + "' used for text interpolation!", getRange());
            }
            output += v.getValue().toString();
        }

        return output;
    }

    @Override
    public CodeRange getRange() {
        return range;
    }
}
