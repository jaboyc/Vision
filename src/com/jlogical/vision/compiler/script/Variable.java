package com.jlogical.vision.compiler.script;

import com.jlogical.vision.compiler.script.elements.CBlock;
import com.jlogical.vision.compiler.script.elements.Hat;

/**
 * Container for a name and value.
 */
public class Variable {
    /**
     * The name of the variable.
     */
    private String name;

    /**
     * The value of the variable.
     */
    private Object value;

    /**
     * Creates a Variable with the given name and value.
     */
    public Variable(String name, Object value){
        this.name = name != null ? name : "";
        this.value = value;
    }

    /**
     * Creates a Variable with the given name and a default value of 0.
     */
    public Variable(String name){
        this(name, 0);
    }

    /**
     * Finds a Variable with the given name by searching through the Hat's local variables first then looking at the Scripts global variables.
     * @param name the name of the Variable.
     * @param cblock the CBlock to look at first. Looks at all CBlocks holding this one as well. Null if none.
     * @param hat the Hat to look at next. Uses the Hat to find the Script for global variables as well.
     * @return the Variable if it is found. Null if not found.
     */
    public static Variable findVariable(String name, CBlock cblock, Hat hat){
        if(cblock != null){
            CBlock look = cblock;
            do{
                for(Variable variable: look.getVariables()){
                    if(name.equals(variable.getName())){
                        return variable;
                    }
                }
            }while((look = look.getCBlockHolder()) != null);
        }
        for(Variable variable: hat.getVariables()){
            if(name.equals(variable.getName())){
                return variable;
            }
        }
        for(Variable variable: hat.getScript().getVariables()){
            if(name.equals(variable.getName())){
                return variable;
            }
        }
        return null;
    }

    /**
     * Finds a global Variable with the given name and returns it.
     * @param name the name of the Variable.
     * @param script the Script to look at.
     * @return the Variable if it is found. Null if not found.
     */
    public static Variable findGlobalVariable(String name, Script script) {
        for(Variable variable: script.getVariables()){
            if(name.equals(variable.getName())){
                return variable;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
