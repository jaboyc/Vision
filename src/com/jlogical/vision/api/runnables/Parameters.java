package com.jlogical.vision.api.runnables;


import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.compiler.script.Script;
import com.jlogical.vision.compiler.script.elements.Hat;
import com.jlogical.vision.compiler.values.Value;
import com.jlogical.vision.project.CodeRange;

import java.util.ArrayList;

/**
 * Stores information regarding parameters of running a command. Needs to be concrete based on the type of Element running it.
 */
public abstract class Parameters {

    /**
     * The List of Values in the Parameters. Cannot be null.
     */
    ArrayList<Value> values;

    /**
     * The Hat that the Parameters are in.
     */
    Hat hatHolder;

    /**
     * The range that this Parameter is covering. Used for exception throwing.
     */
    CodeRange range;
    //TODO Use Range to throw exceptions!

    /**
     * Creates a Parameters with a given List of Values.
     */
    public Parameters(ArrayList<Value> values, Hat hat, CodeRange range){
        this.values = values != null ? values : new ArrayList<Value>();
        this.hatHolder = hat;
        this.range = range;
    }

    /**
     * Returns the value of the Value at the given index in the values List.
     * @param index the index of which Value to get the value from.
     * @return the value of the Value.
     * @throws VisionException if the index is negative or too big.
     */
    public Object get(int index) throws VisionException{
        if(index < 0){
            throw new VisionException("Index cannot be null!", range);
        }
        if(index >= values.size()){
            throw new VisionException("Index cannot be out of range of values!", range);
        }
        Value value = values.get(index);
        if(value == null){
            throw new VisionException("A Value cannot be null!", range);
        }
        return value.getValue();
    }

    /**
     * Returns the String value of the Value at the given index.
     * @param index the index of the Value.
     * @return the String representation of the value.
     * @throws VisionException if the index is negative or too big.
     */
    public String str(int index) throws VisionException{
        return get(index).toString();
    }

    /**
     * Returns the int value of the Value at the given index.
     * @param index the index of the Value.
     * @return the int representation of the Value.
     * @throws VisionException if the index is negative, too big, or cannot be converted to an integer.
     */
    public int numInt(int index) throws VisionException {
        Object val = get(index);
        if(val instanceof Integer){
            return (int) val;
        }else if (val instanceof Double){
            return (int) val;
        }else if (val instanceof Boolean){
            return ((boolean) val) ? 1 : 0;
        }
        try {
            return (int) Double.parseDouble(val.toString());
        } catch (NumberFormatException e) {
            throw new VisionException("Cannot convert '"+val+"' to an integer!", range);
        }
    }

    /**
     * Returns the double value of the Value at the given index.
     * @param index the index of the Value.
     * @return the double representation of the Value.
     * @throws VisionException if the index is negative, too big, or cannot be converted to a number.
     */
    public double num(int index) throws VisionException{
        Object val = get(index);
        if(val instanceof Integer){
            return (double) val;
        }else if (val instanceof Double){
            return (double) val;
        }else if (val instanceof Boolean){
            return ((boolean) val) ? 1: 0;
        }
        try {
            return Double.parseDouble(val.toString());
        } catch (NumberFormatException e) {
            throw new VisionException("Cannot convert '"+val+"' to a number!", range);
        }
    }

    /**
     * Returns the boolean value of the Value at the given index.
     * @param index the index of the Value.
     * @return the boolean representation of the Value.
     * @throws VisionException if the index is negative, too big, or cannot be converted to a boolean.
     */
    public boolean bool(int index) throws VisionException{
        Object val = get(index);
        if(val instanceof Boolean){
            return (boolean) val;
        } else if(val instanceof Integer || val instanceof Double){
            return ((int) val) != 0;
        }else if (val instanceof String){
            String sVal = (String) val;
            if(sVal.equalsIgnoreCase("true")){
                return true;
            }else if(sVal.equalsIgnoreCase(("false"))){
                return false;
            }
        }
        throw new VisionException("Cannot convert '"+val+"' to an boolean!", range);
    }

    public Script getScript(){
        return hatHolder.getScript();
    }

    public ArrayList<Value> getValues() {
        return values;
    }

    public Hat getHatHolder() {
        return hatHolder;
    }
}
