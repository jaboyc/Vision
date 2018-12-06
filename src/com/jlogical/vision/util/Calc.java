package com.jlogical.vision.util;

import com.jlogical.vision.compiler.exceptions.VisionException;

import java.util.ArrayList;

/**
 * Util class for solving expressions.
 */
public class Calc {

    /**
     * Calculates the value of the expression and returns it.
     * @param text the text to solve.
     * @return the value. Null if none.
     * @throws VisionException if there was an error calculating the value.
     */
    public static Object calc(String text) throws VisionException {
        ArrayList<Object> elements = parse(text);
        return solve(elements);
    }

    /**
     * Splits the given text and parses it to a List of elements.
     * @param text the text to parse.
     * @return the List of objects.
     * @throws VisionException if there was an error parsing the text.
     */
    private static ArrayList<Object> parse(String text) throws VisionException{
        ArrayList<Object> elements = new ArrayList<>();
        String currInput = "";
        for(int i=0;i<text.length();i++){
            char c = text.charAt(i);
            if("+-/*=<>()".contains(""+c)){
                if(!currInput.isBlank()){
                    try{
                        elements.add(Double.parseDouble(currInput));
                    }catch(Exception e){
                        elements.add(currInput);
                    }
                }
                elements.add(c);
            }
        }
        return elements;
    }

    /**
     * Solves the given expression and returns its value.
     * @param elements List of elements to solve. Found using parse.
     * @return the value. Null if none.
     * @throws VisionException if there was an error solving the text.
     */
    private static Object solve(ArrayList<Object> elements) throws VisionException{
        while(elements.size() > 1){

        }
        return elements.get(0);
    }
}
