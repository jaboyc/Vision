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
