package com.jlogical.vision.api.runnables;


import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.compiler.script.Script;
import com.jlogical.vision.compiler.script.elements.CBlock;
import com.jlogical.vision.compiler.script.elements.CompiledElement;
import com.jlogical.vision.compiler.script.elements.Hat;
import com.jlogical.vision.compiler.values.Value;
import com.jlogical.vision.project.CodeRange;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores information regarding parameters of running a command. Needs to be concrete based on the type of Element running it.
 */
public abstract class Parameters<T extends CompiledElement> {

    /**
     * The CompiledElement holding this Parameter.
     */
    private T element;

    /**
     * The List of Values in the Parameters. Cannot be null.
     */
    private ArrayList<Value> values;

    /**
     * The Hat that the Parameters are in.
     */
    private Hat hatHolder;

    /**
     * The CBlock that the Parameters are in. Null if not present.
     */
    private CBlock cblockHolder;

    /**
     * The range that this Parameter is covering. Used for exception throwing.
     */
    private CodeRange range;

    /**
     * Creates a Parameters with a given List of Values.
     */
    public Parameters(T element, ArrayList<Value> values, Hat hat, CBlock cBlock, CodeRange range) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null!");
        }
        this.element = element;
        this.values = values != null ? values : new ArrayList<>();
        this.hatHolder = hat;
        this.cblockHolder = cBlock;
        this.range = range;
    }

    /**
     * Throws a VisionException with its range.
     *
     * @param message the error message.
     * @throws VisionException always.
     */
    public void err(String message) throws VisionException {
        throw new VisionException(message, range);
    }


    /**
     * Returns out of the hat with the given return value.
     * @param returnValue the value to return from the hat.
     */
    public void hatReturn(Object returnValue){
        CBlock cBlock = cblockHolder;
        while(cBlock != null){
            cBlock.stop();
            cBlock = cBlock.getCBlockHolder();
        }

        hatHolder.setOutput(returnValue);
        hatHolder.stop();
    }

    /**
     * Stops the most enclosed loop.
     */
    public void stopLoop(){
        cblockHolder.stop();
    }

    /**
     * Returns the value of the Value at the given index in the values List.
     *
     * @param index the index of which Value to get the value from.
     * @return the value of the Value.
     * @throws VisionException if the index is negative or too big.
     */
    public Object get(int index) throws VisionException {
        if (index < 0) {
            throw new VisionException("Index cannot be null!", range);
        }
        if (index >= values.size()) {
            throw new VisionException("Index cannot be out of range of values!", range);
        }
        Value value = values.get(index);
        if (value == null) {
            throw new VisionException("A Value cannot be null!", range);
        }
        Object o = value.getValue();
        if (o instanceof Double) {
            Double d = (Double) o;
            if (d == (int) d.doubleValue())
                return (int) d.doubleValue();
        }
        return o;
    }

    /**
     * Returns the String value of the Value at the given index.
     *
     * @param index the index of the Value.
     * @return the String representation of the value.
     * @throws VisionException if the index is negative or too big.
     */
    public String str(int index) throws VisionException {
        return toString(get(index));
    }

    /**
     * Returns the given value as a String. Used as a util command.
     *
     * @param value the value to convert.
     * @return the String value of the given input.
     */
    public String toString(Object value) {
        return value.toString();
    }

    /**
     * Returns the int value of the Value at the given index.
     *
     * @param index the index of the Value.
     * @return the int representation of the Value.
     * @throws VisionException if the index is negative, too big, or cannot be converted to an integer.
     */
    public int numInt(int index) throws VisionException {
        return toInt(get(index));
    }

    /**
     * Returns the given value as a int. Used as a util command.
     *
     * @param val the value to convert.
     * @return the int value of the given input.
     * @throws VisionException if the value cannot be converted to an int.
     */
    public int toInt(Object val) throws VisionException {
        if (val instanceof Integer) {
            return (int) val;
        } else if (val instanceof Double) {
            return (int) ((double) val);
        } else if (val instanceof Boolean) {
            return ((boolean) val) ? 1 : 0;
        }
        try {
            return (int) Double.parseDouble(val.toString());
        } catch (NumberFormatException e) {
            throw new VisionException("Cannot convert '" + val + "' to an integer!", range);
        }
    }

    /**
     * Returns the double value of the Value at the given index.
     *
     * @param index the index of the Value.
     * @return the double representation of the Value.
     * @throws VisionException if the index is negative, too big, or cannot be converted to a number.
     */
    public double num(int index) throws VisionException {
        return toNum(get(index));
    }

    /**
     * Returns the given value as a double. Used as a util command.
     *
     * @param val the value to convert.
     * @return the double value of the given input.
     * @throws VisionException if the value cannot be converted to an double.
     */
    public double toNum(Object val) throws VisionException {
        if (val instanceof Integer) {
            return (double) ((int) val);
        } else if (val instanceof Double) {
            return (double) val;
        } else if (val instanceof Boolean) {
            return ((boolean) val) ? 1 : 0;
        }
        try {
            return Double.parseDouble(val.toString());
        } catch (NumberFormatException e) {
            throw new VisionException("Cannot convert '" + val + "' to a number!", range);
        }
    }

    /**
     * Returns the boolean value of the Value at the given index.
     *
     * @param index the index of the Value.
     * @return the boolean representation of the Value.
     * @throws VisionException if the index is negative, too big, or cannot be converted to a boolean.
     */
    public boolean bool(int index) throws VisionException {
        return toBoolean(get(index));
    }

    /**
     * Returns the given value as a boolean. Used as a util command.
     *
     * @param val the value to convert.
     * @return the boolean value of the given input.
     * @throws VisionException if the value cannot be converted to an boolean.
     */
    public boolean toBoolean(Object val) throws VisionException {
        if (val instanceof Boolean) {
            return (boolean) val;
        } else if (val instanceof Integer || val instanceof Double) {
            return ((int) val) != 0;
        } else if (val instanceof String) {
            String sVal = (String) val;
            if (sVal.equalsIgnoreCase("true")) {
                return true;
            } else if (sVal.equalsIgnoreCase(("false"))) {
                return false;
            }
        }
        throw new VisionException("Cannot convert '" + val + "' to a boolean!", range);
    }

    /**
     * Returns the list of the value at the given index.
     *
     * @param index the index of the value.
     * @return the List representation of the value.
     * @throws VisionException if the index is negative, too big, or cannot be converted to a List.
     */
    public List list(int index) throws VisionException {
        return toList(get(index));
    }

    /**
     * Returns the given value as a List. Used as a util command.
     *
     * @param val the value to convert.
     * @return the List representation of the value.
     * @throws VisionException if the value cannot be converted to a List.
     */
    public List toList(Object val) throws VisionException {
        if (val instanceof List) return (List) val;
        throw new VisionException("Cannot convert '" + val + "' to a list!", range);
    }

    public Script getScript() {
        return hatHolder.getScript();
    }

    public ArrayList<Value> getValues() {
        return values;
    }

    public Hat getHatHolder() {
        return hatHolder;
    }

    public CodeRange getRange() {
        return range;
    }

    public T getElement() {
        return element;
    }
}
