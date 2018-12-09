package com.jlogical.vision.compiler.values;

import com.jlogical.vision.compiler.Compiler;
import com.jlogical.vision.compiler.Input;
import com.jlogical.vision.compiler.exceptions.CompilerException;
import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.compiler.script.elements.Command;
import com.jlogical.vision.project.CodeRange;
import com.jlogical.vision.project.Project;

import java.util.ArrayList;

/**
 * Value that holds either a math expression or logic expression.
 */
public class ExpressionValue implements Value {

    /**
     * Range the value is in.
     */
    private CodeRange range;

    /**
     * The text of this value.
     */
    private String text;

    /**
     * The Commmand that is holding this value.
     */
    private Command commandHolder;

    /**
     * The Project this value is in.
     */
    private Project project;

    /**
     * Creates a new ExpressionValue with the given text, range, commandHolder, and project.
     */
    public ExpressionValue(String text, CodeRange range, Command commandHolder, Project project){
        this.text = text;
        this.range = range;
        this.commandHolder = commandHolder;
        this.project = project;
    }

    /**
     * Calculates the value of the expression and returns it.
     * @param text the text to solve.
     * @return the value. Null if none.
     * @throws VisionException if there was an error calculating the value.
     */
    public Object calc(String text) throws VisionException {
        ArrayList<Object> elements = parse(text);
        preCalculate(elements);
        return solve(elements);
    }

    /**
     * Splits the given text and parses it to a List of elements.
     * @param text the text to parse.
     * @return the List of objects.
     * @throws VisionException if there was an error parsing the text.
     */
    private ArrayList<Object> parse(String text) throws VisionException{
        ArrayList<Object> elements = new ArrayList<>();
        String currInput = "";
        int pIndex = 0;
        for(int i=0;i<text.length();i++){
            char c = text.charAt(i);
            if(c == '('){
                pIndex++;
                currInput += c;
            } else if (c == ')'){
                pIndex --;
                currInput += c;
            }else if(pIndex == 0 && "+-/*^=<>|&".contains(""+c)){
                if(!currInput.isBlank()){
                    try{
                        elements.add(Double.parseDouble(currInput.trim()));
                    }catch(Exception e){
                        try {
                            if(currInput.trim().startsWith("(") && currInput.trim().endsWith(")")){
                                Value value = Compiler.toValue(new Input(currInput.trim().substring(1, currInput.trim().length()-1), new CodeRange(project, range.getFile(), range.getLineStart(), range.getCharStart() + i, range.getLineStart(), range.getCharStart() + i + currInput.length()), '('), commandHolder, project);
                                elements.add(value.getValue());
                            }else{
                                Value value = Compiler.toValue(new Input(currInput.trim(), new CodeRange(project, range.getFile(), range.getLineStart(), range.getCharStart() + i, range.getLineStart(), range.getCharStart() + i + currInput.length()), '('), commandHolder, project);
                                elements.add(value.getValue());
                            }
                        } catch (CompilerException e1) {
                            throw new VisionException("Could not find a value for " + currInput.trim(), range);
                        }
                    }
                    currInput = "";
                }
                elements.add(c);
            }else{
                currInput += c;
            }
        }
        if(pIndex == 0 && !currInput.isBlank()){
            try{
                elements.add(Double.parseDouble(currInput.trim()));
            }catch(Exception e){
                try {
                    if(currInput.trim().startsWith("(") && currInput.trim().endsWith(")")){
                        Value value = Compiler.toValue(new Input(currInput.trim().substring(1, currInput.trim().length()-1), new CodeRange(project, range.getFile(), range.getLineStart(), range.getCharEnd() - currInput.length(), range.getLineStart(), range.getCharEnd()), '('), commandHolder, project);
                        elements.add(value.getValue());
                    }else{
                        Value value = Compiler.toValue(new Input(currInput.trim(), new CodeRange(project, range.getFile(), range.getLineStart(), range.getCharEnd() - currInput.length(), range.getLineStart(), range.getCharEnd()), '('), commandHolder, project);
                        elements.add(value.getValue());
                    }
                } catch (CompilerException e1) {
                    throw new VisionException("Could not find a value for " + currInput.trim(), range);
                }
            }
        }
        if(pIndex != 0){
            throw new VisionException("Parentheses imbalance! Either too many '(' or ')' in this expression", range);
        }
        System.out.println(elements);
        return elements;
    }

    /**
     * Patches some calculations, like making a number negative or multiplyling two parentheses near each other.
     * @param elements the elements to precalculate.
     */
    private void preCalculate(ArrayList<Object> elements){
        if(elements.size() > 2 && elements.get(0).toString().trim().equals("-")){
            elements.set(0, "-"+elements.get(1).toString().trim());
            elements.remove(1);
        }
        for(int i=0;i<elements.size()-2;i++){
            String s =elements.get(i).toString().trim();
            if("+/*-^=<>".contains(s) && elements.get(i+1).toString().trim().equals("-")){
                elements.set(i+1, "-"+elements.get(i+2).toString().trim());
                elements.remove(i+2);
            }
            if("<>".contains(s) && elements.get(i+1).toString().trim().equals("=")){
                elements.set(i, s+"=");
                elements.remove(i+1);
            }
        }
    }

    /**
     * Solves the given expression and returns its value.
     * @param elements List of elements to solve. Found using parse.
     * @return the value. Null if none.
     * @throws VisionException if there was an error solving the text.
     */
    private Object solve(ArrayList<Object> elements) throws VisionException{
        int index = -1;
        while(elements.size() > 1){
            if((index = indexOf(elements, "^")) != -1){
                ArrayList<Double> nums = checkNum(elements, index, -1, 1);
                elements.set(index-1, Math.pow(nums.get(0), nums.get(1)));
                elements.remove(index);
                elements.remove(index);
            }else if((index = indexOf(elements, "*", "/")) != -1){
                String s = elements.get(index).toString();
                if(s.equals("*")){ //Multiply
                    ArrayList<Double> nums = checkNum(elements, index, -1, 1);
                    elements.set(index-1, nums.get(0) * nums.get(1));
                    elements.remove(index);
                    elements.remove(index);
                } else if (s.equals("/")) { //Divide
                    ArrayList<Double> nums = checkNum(elements, index, -1, 1);
                    elements.set(index-1, nums.get(0) / nums.get(1));
                    elements.remove(index);
                    elements.remove(index);
                }
            }else if((index = indexOf(elements, "+", "-")) != -1){
                String s = elements.get(index).toString();
                if(s.equals("+")){ //Add
                    ArrayList<Double> nums = checkNum(elements, index, -1, 1);
                    elements.set(index-1, nums.get(0) + nums.get(1));
                    elements.remove(index);
                    elements.remove(index);
                } else if (s.equals("-")) { //Subtract
                    ArrayList<Double> nums = checkNum(elements, index, -1, 1);
                    elements.set(index-1, nums.get(0) - nums.get(1));
                    elements.remove(index);
                    elements.remove(index);
                }
            }else if ((index = indexOf(elements, "<", ">", "<=", ">=", "=")) != -1){
                String s = elements.get(index).toString();
                checkNum(elements, index, -1, 1);
                if(s.equals("<")){
                    ArrayList<Double> nums = checkNum(elements, index, -1, 1);
                    elements.set(index-1, nums.get(0) < nums.get(1));
                    elements.remove(index);
                    elements.remove(index);
                }else if(s.equals(">")){
                    ArrayList<Double> nums = checkNum(elements, index, -1, 1);
                    elements.set(index-1, nums.get(0) > nums.get(1));
                    elements.remove(index);
                    elements.remove(index);
                }else if(s.equals("<=")){
                    ArrayList<Double> nums = checkNum(elements, index, -1, 1);
                    elements.set(index-1, nums.get(0) <= nums.get(1));
                    elements.remove(index);
                    elements.remove(index);
                }else if(s.equals(">=")){
                    ArrayList<Double> nums = checkNum(elements, index, -1, 1);
                    elements.set(index-1, nums.get(0) >= nums.get(1));
                    elements.remove(index);
                    elements.remove(index);
                }else if(s.equals("=")){
                    ArrayList<Double> nums = checkNum(elements, index, -1, 1);
                    elements.set(index-1, nums.get(0).equals(nums.get(1)));
                    elements.remove(index);
                    elements.remove(index);
                }
            }else if((index = indexOf(elements, "&", "|")) != -1){
                String s = elements.get(index).toString();
                if(s.equals("&")){ //And
                    ArrayList<Boolean> bools = checkBools(elements, index, -1, 1);
                    elements.set(index-1, bools.get(0) && bools.get(1));
                    elements.remove(index);
                    elements.remove(index);
                } else if (s.equals("|")) { //Or
                    ArrayList<Boolean> bools = checkBools(elements, index, -1, 1);
                    elements.set(index-1, bools.get(0) || bools.get(1));
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
    private int indexOf(ArrayList<Object> elements, String... tests){
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
     * @param index the initial index to check.
     * @param relative all the relative indices to check.
     * @return List of doubles of the numbers it just checked.
     * @throws VisionException if the relative elements are not numbers.
     */
    private ArrayList<Double> checkNum(ArrayList<Object> elements, int index, int... relative) throws VisionException {
        ArrayList<Double> nums = new ArrayList<>();
        for(int i=0;i<relative.length;i++){
            int ri = index + relative[i];
            if(ri< 0){
                throw new VisionException(elements.get(index) + " needs to be after a number.", range);
            }
            if(ri > elements.size()){
                throw new VisionException(elements.get(index) + " needs to be before a number.", range);
            }
            try{
                nums.add(Double.parseDouble(elements.get(ri)+""));
            }catch(Exception e){
                try {
                    if(elements.get(ri).toString().trim().startsWith("(") && elements.get(ri).toString().trim().endsWith(")")){
                        Value value = Compiler.toValue(new Input(elements.get(ri).toString().trim().substring(1, elements.get(ri).toString().trim().length()-1), new CodeRange(project, range.getFile(), range.getLineStart(), range.getCharStart() + i, range.getLineStart(), range.getCharStart() + i + elements.get(ri).toString().length()), '('), commandHolder, project);
                        nums.add(Double.parseDouble(value.getValue().toString()));
                    }else{
                        Value value = Compiler.toValue(new Input(elements.get(ri).toString().trim(), new CodeRange(project, range.getFile(), range.getLineStart(), range.getCharStart() + i, range.getLineStart(), range.getCharStart() + i + elements.get(ri).toString().length()), '('), commandHolder, project);
                        nums.add(Double.parseDouble(value.getValue().toString()));
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    throw new VisionException(elements.get(ri) + " must be a number or valid numerical value next to " + elements.get(index), range);
                }
            }
        }
        return nums;
    }

    /**
     * Checks if any of the elements relative to the index are booleans. If not, throws an exception.
     * @param elements the elements of the current calculation.
     * @param index the initial index to check.
     * @param relative all the relative indices to check.
     * @return List of booleans of the values it just checked.
     * @throws VisionException if the relative elements are not booleans.
     */
    private ArrayList<Boolean> checkBools(ArrayList<Object> elements, int index, int... relative) throws VisionException {
        ArrayList<Boolean> bools = new ArrayList<>();
        for(int i=0;i<relative.length;i++){
            int ri = index + relative[i];
            if(ri< 0){
                throw new VisionException(elements.get(index) + " needs to be after a boolean.", range);
            }
            if(ri > elements.size()){
                throw new VisionException(elements.get(index) + " needs to be before a boolean.", range);
            }
            try{
                bools.add(toBool(elements.get(ri)));
            }catch(Exception e){
                try {
                    if(elements.get(ri).toString().trim().startsWith("(") && elements.get(ri).toString().trim().endsWith(")")){
                        Value value = Compiler.toValue(new Input(elements.get(ri).toString().trim().substring(1, elements.get(ri).toString().trim().length()-1), new CodeRange(project, range.getFile(), range.getLineStart(), range.getCharStart() + i, range.getLineStart(), range.getCharStart() + i + elements.get(ri).toString().length()), '('), commandHolder, project);
                        bools.add(toBool(value.getValue().toString()));
                    }else{
                        Value value = Compiler.toValue(new Input(elements.get(ri).toString().trim(), new CodeRange(project, range.getFile(), range.getLineStart(), range.getCharStart() + i, range.getLineStart(), range.getCharStart() + i + elements.get(ri).toString().length()), '('), commandHolder, project);
                        bools.add(toBool(value.getValue().toString()));
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                    throw new VisionException(elements.get(ri) + " must be a boolean or valid boolean value next to " + elements.get(index), range);
                }
            }
        }
        return bools;
    }

    /**
     * Returns the boolean version of the object. Throws an exception if not a boolean.
     * @param o the object to convert.
     * @return the boolean version of the object.
     * @throws VisionException if it is not a boolean.
     */
    private static boolean toBool(Object o) throws VisionException{
        if(o.toString().equals("true") || o.toString().equals("false")){
            return o.toString().equals("true");
        }
        throw new IllegalArgumentException("Cannot convert " + o + " to a boolean!");
    }

    @Override
    public Object getValue() throws VisionException {
        return calc(text);
    }

    @Override
    public CodeRange getRange() {
        return range;
    }
}
