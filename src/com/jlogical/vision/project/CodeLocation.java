package com.jlogical.vision.project;

/**
 * Contains the absolute location of a piece of code.
 */
public class CodeLocation {

    /**
     * The Project the code is found in. Null if not in any project.
     */
    private Project project;

    /**
     * The file the code is found in. Null if not in a VisionFile.
     */
    private VisionFile file;

    /**
     * The line of code the code is found in. -1 if not a line of code.
     */
    private int lineNum;

    /**
     * The character in the line of code the code is found in. -1 if not in a line of code.
     */
    private int charNum;

    /**
     * Creates a reference to a character in code.
     */
    public CodeLocation(Project project, VisionFile file, int lineNum, int charNum) {
        this.project = project;
        this.file = file;
        this.lineNum = lineNum >= -1 ? lineNum : -1;
        this.charNum = charNum;
    }

    /**
     * Creates a reference to a line of code.
     */
    public CodeLocation(Project project, VisionFile file, int lineNum) {
        this(project, file, lineNum, -1);
    }

    /**
     * Creates a reference to a file in code.
     */
    public CodeLocation(Project project, VisionFile file) {
        this(project, file, -1);
    }

    /**
     * Creates a reference to a project.
     */
    public CodeLocation(Project project) {
        this(project, null);
    }

    /**
     * Returns a copy of the given CodeLocation.
     * @param origin the CodeLocation to copy.
     * @return the CodeLocation. Null if the origin is null.
     */
    public static CodeLocation copy(CodeLocation origin){
        if(origin == null){
            return null;
        }
        return new CodeLocation(origin.project, origin.file, origin.lineNum, origin.charNum);
    }

    /**
     * Returns a copy of the given CodeLocation, but the charNum can be changed.
     * @param origin the CodeLocation to copy.
     * @param charNum the character index of this CodeLocation.
     * @return the CodeLocation. Null if the origin is null or charNum < 0.
     */
    public static CodeLocation copyLine(CodeLocation origin, int charNum){
        if(origin == null){
            return null;
        }
        if(charNum < 0){
            return null;
        }
        return new CodeLocation(origin.project, origin.file, origin.lineNum, charNum);
    }

    /**
     * @return whether the CodeLocation references an exact location in code with no ambiguouty.
     */
    public boolean isAbsolute() {
        return project != null && file != null && lineNum != -1 && charNum != -1;
    }

    /**
     * Returns whether it is equal to another CodeLocation.
     * @param o the Object to test it with.
     * @return false if o is not a CodeLocation. True if both reference the exact same position.
     */
    public boolean equals(Object o){
        if(o instanceof CodeLocation){
            CodeLocation cl = (CodeLocation) o;
            return project == cl.project && file == cl.file && lineNum == cl.lineNum && charNum == cl.charNum;
        }
        return false;
    }

    /**
     * Returns the comparison of the code location between this CodeLocation and the given CodeLocation. Both must be absolute and referencing the same Project and VisionFile.
     * @param loc the CodeLocation to compare this to.
     * @return 0 if they are equal, 1 if this one is greater than the given one, or -1 if this one is less than the given one.
     * @throws IllegalArgumentException if either is not absolute or do not reference the same Project and VisionFile.
     */
    public int compareTo(CodeLocation loc) throws IllegalArgumentException{
        if(!isAbsolute() || !loc.isAbsolute()){
            throw new IllegalArgumentException("Both CodeLocations must be absolute in order to compare them.");
        }
        if(project != loc.project){
            throw new IllegalArgumentException("Both CodeLocations must be referencing the same Project to compare them.");
        }
        if(file != loc.file){
            throw new IllegalArgumentException("Both CodeLocations must be referencing the same File to compare them.");
        }
        if(equals(loc)){
            return 0;
        }else if(lineNum > loc.lineNum){
            return 1;
        }else if(lineNum < loc.lineNum){
            return -1;
        }else if(charNum > loc.charNum){
            return 1;
        }else if (charNum < loc.charNum){
            return -1;
        }
        throw new UnsupportedOperationException("Error with compareTo method.");
    }

    /**
     * Returns a String representation of the CodeLocation.
     */
    public String toString() {
        if (project == null) {
            return "nowhere";
        }
        if (file == null) {
            return project.getName();
        }
        if (lineNum == -1) {
            return project.getName() + " => " + file.getName();
        }
        if (charNum == -1) {
            return project.getName() + " => " + file.getName() + ":" + lineNum;
        }
        return project.getName() + "=>" + file.getName() + ":" + lineNum + ":" + charNum;
    }



    public Project getProject() {
        return project;
    }

    public VisionFile getFile() {
        return file;
    }

    public int getLineNum() {
        return lineNum;
    }

    public int getCharNum() {
        return charNum;
    }
}
