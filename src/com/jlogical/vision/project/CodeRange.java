package com.jlogical.vision.project;

/**
 * Stores a range in the code.
 */
public class CodeRange {
    /**
     * The Project this CodeRange is found in. Cannot be null.
     */
    private Project project;

    /**
     * The VisionFile this CodeRange is found in. Cannot be null.
     */
    private VisionFile file;

    /**
     * The starting line of the range. Cannot be null.
     */
    private int lineStart;

    /**
     * The starting character number of the range. Cannot be null.
     */
    private int charStart;

    /**
     * The ending line of the range. Cannot be null.
     */
    private int lineEnd;

    /**
     * The ending character number of the range. Cannot be null.
     */
    private int charEnd;

    /**
     * Creates a new CodeRange with a specified project, file, start position, and end position.
     * @throws IllegalArgumentException if any of the parameters are null or not valid.
     */
    public CodeRange(Project project, VisionFile file, int lineStart, int charStart, int lineEnd, int charEnd) throws IllegalArgumentException{
        if(project == null){
            throw new IllegalArgumentException("The Project cannot be null when creating a CodeRange.");
        }
        if(file == null){
            throw new IllegalArgumentException("The VisionFile cannot be null when creating a CodeRange.");
        }
        if(lineStart < 0){
            throw new IllegalArgumentException("The starting line must be positive or 0 when creating a CodeRange.");
        }
        if(charStart < 0){
            throw new IllegalArgumentException("The starting character number must be positive or 0 when creating a CodeRange.");
        }
        if(lineEnd < 0){
            throw new IllegalArgumentException("The ending line must be positive or 0 when creating a CodeRange.");
        }
        if(charEnd < 0){
            throw new IllegalArgumentException("The ending character number must be positive or 0 when creating a CodeRange.");
        }
        this.project = project;
        this.file = file;
        this.lineStart = lineStart;
        this.charStart = charStart;
        this.lineEnd = lineEnd;
        this.charEnd = charEnd;
    }

    /**
     * Creates a CodeRange between the given CodeLocations (inclusive). The order of the start/end locations does not matter.
     * @param loc1 the first location.
     * @param loc2 the second location.
     * @return the CodeRange between the two locations (inclusive).
     * @throws IllegalArgumentException if either of the CodeLocations are not absolute or don't reference the same project and file.
     */
    public static CodeRange between(CodeLocation loc1, CodeLocation loc2) throws IllegalArgumentException{
        if(loc1 == null || loc2 == null){
            throw new IllegalArgumentException("The CodeLocations cannot be null when creating a CodeRange.");
        }
        if(!loc1.isAbsolute() || !loc2.isAbsolute()){
            throw new IllegalArgumentException("The CodeLocations must be absolute in order to create a CodeRange from them!");
        }
        if(loc1.getProject() != loc2.getProject()){
            throw new IllegalArgumentException("The CodeLocations must reference the same project!");
        }
        if(loc1.getFile() != loc2.getFile()){
            throw new IllegalArgumentException("The CodeLocations must reference the same file!");
        }
        int compare = loc1.compareTo(loc2);
        if(compare == 0){
            return new CodeRange(loc1.getProject(), loc1.getFile(), loc1.getLineNum(), loc1.getCharNum(), loc1.getLineNum(), loc1.getCharNum());
        }else if(compare == -1){
            return new CodeRange(loc1.getProject(), loc1.getFile(), loc1.getLineNum(), loc1.getCharNum(), loc2.getLineNum(), loc2.getCharNum());
        }else if(compare == 1){
            return new CodeRange(loc1.getProject(), loc1.getFile(), loc2.getLineNum(), loc2.getCharNum(), loc1.getLineNum(), loc1.getCharNum());
        }
        throw new UnsupportedOperationException("Error with between method.");
    }

    /**
     * @return the CodeLocation for the start of the range.
     */
    public CodeLocation startLocation(){
        return new CodeLocation(project, file, lineStart, charStart);
    }

    /**
     * @return the CodeLocation for the end of the range.
     */
    public CodeLocation endLocation(){
        return new CodeLocation(project, file, lineEnd, charEnd);
    }

    @Override
    public String toString(){
        return "beg: ("+lineStart+","+charStart+") end: ("+lineEnd+","+charEnd+")";
    }

    public Project getProject() {
        return project;
    }

    public VisionFile getFile() {
        return file;
    }

    public int getLineStart() {
        return lineStart;
    }

    public int getCharStart() {
        return charStart;
    }

    public int getLineEnd() {
        return lineEnd;
    }

    public int getCharEnd() {
        return charEnd;
    }
}
