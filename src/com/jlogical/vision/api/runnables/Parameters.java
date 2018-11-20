package com.jlogical.vision.api.runnables;


import com.jlogical.vision.compiler.script.Script;
import com.jlogical.vision.compiler.script.elements.Hat;
import com.jlogical.vision.compiler.values.Value;

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
     * Creates a Parameters with a given List of Values.
     */
    public Parameters(ArrayList<Value> values, Hat hat){
        this.values = values != null ? values : new ArrayList<Value>();
        this.hatHolder = hat;
    }

    /**
     * Returns the value of the Value at the given index in the values List.
     * @param index the index of which Value to get the value from.
     * @return the value of the Value.
     */
    public Object get(int index){
        if(index < 0){
            throw new IllegalArgumentException("Index cannot be null!");
        }
        if(index >= values.size()){
            throw new IllegalArgumentException("Index cannot be out of range of values!");
        }
        return values.get(index).getValue();
    }

    /**
     * Returns the String value of the Value at the given index.
     * @param index the index of the Value.
     * @return the String representation of the value.
     */
    public String str(int index){
        return get(index).toString();
    }

    /**
     * Returns the int value of the Value at the given index.
     * @param index the index of the Value.
     * @return the int representatino of the Value.
     */
    public int numInt(int index) throws NumberFormatException {
        Object val = get(index);
        if(val instanceof Integer){
            return (int) val;
        }else if (val instanceof Double){
            return (int) val;
        }else if (val instanceof Boolean){
            return ((boolean) val) ? 1 : 0;
        }
        return (int) Double.parseDouble(get(index).toString());
    }

    /**
     * Returns the double value of the Value at the given index.
     * @param index the index of the Value.
     * @return the double representatino of the Value.
     */
    public double num(int index) throws NumberFormatException{
        Object val = get(index);
        if(val instanceof Integer){
            return (double) val;
        }else if (val instanceof Double){
            return (double) val;
        }else if (val instanceof Boolean){
            return ((boolean) val) ? 1: 0;
        }
        return Double.parseDouble(val.toString());
    }

    /**
     * Returns the boolean value of the Value at the given index.
     * @param index the index of the Value.
     * @return the boolean representatino of the Value.
     */
    public boolean bool(int index){
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
        throw new ClassCastException("Could not convert " + val + " to boolean!");
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
