package com.jlogical.vision.util;

import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.project.CodeRange;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Util class for solving expressions.
 */
public class Calc {

    /**
     * Calculates the value of the expression and returns it.
     * @param text the text to solve.
     * @param range the CodeRange this calculation occurs in.
     * @return the value. Null if none.
     * @throws VisionException if there was an error calculating the value.
     */
    public static Object calc(String text, CodeRange range) throws VisionException {
        ArrayList<Object> elements = parse(text);
        return solve(elements, range);
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
            if("+-/*=<>".contains(""+c)){
                if(!currInput.isBlank()){
                    try{
                        elements.add(Double.parseDouble(currInput));
                    }catch(Exception e){
                        elements.add(currInput);
                    }
                    currInput = "";
                }
                elements.add(c);
            }else{
                currInput += c;
            }
        }
        if(!currInput.isBlank()){
            try{
                elements.add(Double.parseDouble(currInput));
            }catch(Exception e){
                elements.add(currInput);
            }
        }
        System.out.println(elements);
        return elements;
    }

    /**
     * Solves the given expression and returns its value.
     * @param elements List of elements to solve. Found using parse.
     * @param range the CodeRange this calculation occurs in.
     * @return the value. Null if none.
     * @throws VisionException if there was an error solving the text.
     */
    private static Object solve(ArrayList<Object> elements, CodeRange range) throws VisionException{
        int index = -1;
        while(elements.size() > 1){
            if((index = indexOf(elements, "^")) != -1){
                ArrayList<Double> nums = checkNum(elements, range, index, -1, 1);
                elements.set(index-1, Math.pow(nums.get(0), nums.get(1)));
                elements.remove(index);
                elements.remove(index);
            }else if((index = indexOf(elements, "*", "/")) != -1){
                String s = elements.get(index).toString();
                if(s.equals("*")){ //Multiply
                    ArrayList<Double> nums = checkNum(elements, range, index, -1, 1);
                    elements.set(index-1, nums.get(0) * nums.get(1));
                    elements.remove(index);
                    elements.remove(index);
                } else if (s.equals("/")) { //Divide
                    ArrayList<Double> nums = checkNum(elements, range, index, -1, 1);
                    elements.set(index-1, nums.get(0) / nums.get(1));
                    elements.remove(index);
                    elements.remove(index);
                }
            }else if((index = indexOf(elements, "+", "-")) != -1){
                String s = elements.get(index).toString();
                if(s.equals("+")){ //Add
                    ArrayList<Double> nums = checkNum(elements, range, index, -1, 1);
                    elements.set(index-1, nums.get(0) + nums.get(1));
                    elements.remove(index);
                    elements.remove(index);
                } else if (s.equals("-")) { //Subtract
                    ArrayList<Double> nums = checkNum(elements, range, index, -1, 1);
                    elements.set(index-1, nums.get(0) - nums.get(1));
                    elements.remove(index);
                    elements.remove(index);
                }
            }
        }
        return elements.get(0);
    }

    /**
     * Returns the index of the smallest index where any of the tests are in the elements. -1 if none are found.
     * @param elements the elements to look through.
     * @param tests the Strings to test.
     * @return the index.
     */
    private static int indexOf(ArrayList<Object> elements, String... tests){
        for(int i=0;i<elements.size();i++){
            Object o = elements.get(i);
            for(String s : tests){
                if(s.equals(o.toString())){
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Checks if any of the elements relative to the index are numbers. If not, throws an exception.
     * @param elements the elements of the current calculation.
     * @param range the CodeRange this calculation occurs in.
     * @param index the initial index to check.
     * @param relative all the relative indices to check.
     * @return List of doubles of the numbers it just checked.
     * @throws VisionException if the relative elements are not numbers.
     */
    private static ArrayList<Double> checkNum(ArrayList<Object> elements, CodeRange range, int index, int... relative) throws VisionException {
        ArrayList<Double> nums = new ArrayList<>();
        for(int i=0;i<relative.length;i++){
            int ri = index + relative[i];
            if(ri< 0){
                throw new VisionException(elements.get(index) + " needs to be proceeded by a number.", range);
            }
            if(ri > elements.size()){
                throw new VisionException(elements.get(index) + " needs to be before a number.", range);
            }
            try{
                nums.add(Double.parseDouble(elements.get(ri)+""));
            }catch(Exception e){
                throw new VisionException(elements.get(ri)+" must be a number next to "+elements.get(index), range);
            }
        }
        return nums;
    }
}
