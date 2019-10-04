package com.jlogical.vision.util;

import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.compiler.script.Variable;
import com.jlogical.vision.compiler.values.ExpressionValue;
import com.jlogical.vision.project.CodeRange;

import java.util.ArrayList;

/**
 * Util class for solving expressions.
 */
public class Calc {

    /**
     * Calculates the value of the expression and returns it.
     *
     * @param value the ExpressionValue that is calling this function. Used for getting text, determining range, and finding variables.
     * @return the value. Null if none.
     * @throws VisionException if there was an error calculating the value.
     */
    public static Object calc(ExpressionValue value) throws VisionException {
        ArrayList<Object> elements = parse(value);
        adjust(elements);
        return solve(elements, value);
    }

    /**
     * Splits the given text and parses it to a List of elements.
     *
     * @param value the ExpressionValue that is calling this function.
     * @return the List of objects.
     */
    private static ArrayList<Object> parse(ExpressionValue value)  {

        String text = value.getText(); // The text of the equation.

        ArrayList<Object> elements = new ArrayList<>();
        String currInput = "";

        // Loop through and parse the expression.
        for (int i = 0; i < text.length(); i++) {

            char c = text.charAt(i);

            // if we found an operator, add the previous element and the current operator.
            if (isOperator(c)) {
                currInput = currInput.trim(); // Trim the input.
                if (!currInput.isBlank()) {

                    // Add the element. If it's a number, add it. Otherwise, try to convert it to a variable.
                    try {
                        elements.add(Double.parseDouble(currInput));
                    } catch (Exception e) {
                        Variable variable = Variable.findVariable(currInput, value.getCommandHolder().getCBlockHolder(), value.getCommandHolder().getHatHolder());
                        if (variable != null) {
                            elements.add(variable.getValue());
                        } else {
                            elements.add(currInput);
                        }
                    }
                    currInput = "";
                }
                elements.add(c);
            } else {
                currInput += c;
            }
        }

        // Add the last element to the list.
        currInput = currInput.trim();
        if (!currInput.isBlank()) {
            try {
                elements.add(Double.parseDouble(currInput));
            } catch (Exception e) {
                Variable variable = Variable.findVariable(currInput, value.getCommandHolder().getCBlockHolder(), value.getCommandHolder().getHatHolder());
                if (variable != null) {
                    elements.add(variable.getValue());
                } else {
                    elements.add(currInput);
                }
            }
        }
        return elements;
    }

    /**
     * Does some adjustments to the given elements before being solved.
     * Some of these changes include fixing positive and negative signs.
     *
     * @param elements the elements to adjust.
     */
    private static void adjust(ArrayList<Object> elements) {

        if(elements.size() <= 1) return; // Don't do anything if the expression is less than two elements.

        Object curr;
        Object next;
        Object moreNext;

        // Fix the case where (+) or (-) are the first elements of the expression.
        curr = elements.get(0);
        next = elements.get(1);
        if(curr.toString().equals("+") && isNumber(next)){
            elements.set(1, +Double.parseDouble(next.toString()));
            elements.remove(0);
        }else if (curr.toString().equals("-") && isNumber(next)){
            elements.set(1, -Double.parseDouble(next.toString()));
            elements.remove(0);
        }

        // Loop through and find stray (+) and (-) signs after operations, and cast the next number to either positive or negative.
        for (int i = 0; i < elements.size() - 2; i++) {
            curr = elements.get(i);
            next = elements.get(i+1);
            moreNext = elements.get(i+2);

            if(isOperator(curr) && next.toString().equals("+") && isNumber(moreNext)){
                elements.set(i+2, +Double.parseDouble(moreNext.toString()));
                elements.remove(i+1);
            }else if (isOperator(curr) && next.toString().equals("-") && isNumber(moreNext)){
                elements.set(i+2, -Double.parseDouble(moreNext.toString()));
                elements.remove(i+1);
            }
        }
    }

    /**
     * Solves the given expression and returns its value.
     *
     * @param elements List of elements to solve. Found using parse.
     * @param value    the ExpressionValue this
     * @return the value. Null if none.
     * @throws VisionException if there was an error solving the text.
     */
    private static Object solve(ArrayList<Object> elements, ExpressionValue value) throws VisionException {
        int index;
        while (elements.size() > 1) {
            if ((index = indexOf(elements, "^")) != -1) {
                ArrayList<Double> nums = checkNum(elements, value.getRange(), index, -1, 1);
                elements.set(index - 1, Math.pow(nums.get(0), nums.get(1)));
                elements.remove(index);
                elements.remove(index);
            } else if ((index = indexOf(elements, "*", "/")) != -1) {
                String s = elements.get(index).toString();
                if (s.equals("*")) { //Multiply
                    ArrayList<Double> nums = checkNum(elements, value.getRange(), index, -1, 1);
                    elements.set(index - 1, nums.get(0) * nums.get(1));
                    elements.remove(index);
                    elements.remove(index);
                } else if (s.equals("/")) { //Divide
                    ArrayList<Double> nums = checkNum(elements, value.getRange(), index, -1, 1);
                    elements.set(index - 1, nums.get(0) / nums.get(1));
                    elements.remove(index);
                    elements.remove(index);
                }
            } else if ((index = indexOf(elements, "+", "-")) != -1) {
                String s = elements.get(index).toString();
                if (s.equals("+")) { //Add
                    ArrayList<Double> nums = checkNum(elements, value.getRange(), index, -1, 1);
                    elements.set(index - 1, nums.get(0) + nums.get(1));
                    elements.remove(index);
                    elements.remove(index);
                } else if (s.equals("-")) { //Subtract
                    ArrayList<Double> nums = checkNum(elements, value.getRange(), index, -1, 1);
                    elements.set(index - 1, nums.get(0) - nums.get(1));
                    elements.remove(index);
                    elements.remove(index);
                }
            }
        }
        return elements.get(0);
    }

    /**
     * Returns whether the given object is a mathematical operator.
     * @param o the object to test.
     * @return true if it is an operator.
     */
    private static boolean isOperator(Object o){
        return "+-*/^".contains(o.toString());
    }

    /**
     * Returns whether the given object is a number or not.
     * @param o the object to test.
     * @return true if the object is a number.
     */
    private static boolean isNumber(Object o){
        try{
            Double.parseDouble(o.toString());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * Returns the index of the smallest index where any of the tests are in the elements. -1 if none are found.
     *
     * @param elements the elements to look through.
     * @param tests    the Strings to test.
     * @return the index.
     */
    private static int indexOf(ArrayList<Object> elements, String... tests) {
        for (int i = 0; i < elements.size(); i++) {
            Object o = elements.get(i);
            for (String s : tests) {
                if (s.equals(o.toString())) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Checks if any of the elements relative to the index are numbers. If not, throws an exception.
     *
     * @param elements the elements of the current calculation.
     * @param range    the CodeRange this calculation occurs in.
     * @param index    the initial index to check.
     * @param relative all the relative indices to check.
     * @return List of doubles of the numbers it just checked.
     * @throws VisionException if the relative elements are not numbers.
     */
    private static ArrayList<Double> checkNum(ArrayList<Object> elements, CodeRange range, int index, int... relative) throws VisionException {
        ArrayList<Double> nums = new ArrayList<>();
        for (int i = 0; i < relative.length; i++) {
            int ri = index + relative[i];
            if (ri < 0) {
                throw new VisionException(elements.get(index) + " needs to be proceeded by a number.", range);
            }
            if (ri >= elements.size()) {
                throw new VisionException(elements.get(index) + " needs to be before a number.", range);
            }
            try {
                nums.add(Double.parseDouble(elements.get(ri) + ""));
            } catch (Exception e) {
                throw new VisionException(elements.get(ri) + " must be a number next to " + elements.get(index), range);
            }
        }
        return nums;
    }
}
