package com.jlogical.vision.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Stores information regarding the uncompiled version of a project. Stored as a ZIP file disguised as a .vproj file. A project.json file can be found in the ZIP containing the information and code for the project.
 */
public class Project {

    /**
     * The name of the Project.
     */
    private String name;

    /**
     * A list of code files for this project.
     */
    private ArrayList<VisionFile> files;

    /**
     * Can only be instantiated from the factory static methods. Creates a Project with a given name and files.
     */
    private Project(String name, ArrayList<VisionFile> files) {
        this.name = name != null ? name : "empty";
        this.files = files != null ? files : new ArrayList<>();
    }

    /**
     * Returns a blank project.
     * @param name the name of the project.
     * @return the blank project.
     */
    public static Project blank(String name){
        return new Project(name, new ArrayList<>());
    }

    /**
     * Returns a Project that is stored in a file.
     *
     * @param path the path of the file the Project is stored in.
     * @return the Project inside of the file. Null if the file is not found, in the wrong format, or was unsuccessful in retrieving a Project.
     */
    public static Project fromFile(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        return fromFile(new File(path));
    }

    /**
     * Returns a Project that is stored in a file.
     *
     * @param file the file the Project is stored in.
     * @return the Project inside of the file. Null if the file is not found, in the wrong format, or was unsuccessful in retrieving a Project.
     */
    public static Project fromFile(File file) {
        if (file == null || !file.exists() || !file.getPath().endsWith(".vproj")) {
            return null;
        }
        return new Project(null, null);
    }


    /**
     * Saves the Project in a File.
     *
     * @param file the File to save the Project in.
     * @throws FileNotFoundException if the File is not found.
     */
    public void save(File file) throws FileNotFoundException {
        if(file == null || !file.exists()){
            throw new FileNotFoundException();
        }
        //TODO save the Project into a File.
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name : "empty";
    }

    public ArrayList<VisionFile> getFiles() {
        return files;
    }

    public String toString() {
        return "Project[" + name + "]=file size:" + files.size();
    }
}
