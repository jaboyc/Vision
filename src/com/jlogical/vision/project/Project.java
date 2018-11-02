package com.jlogical.vision.project;

import java.io.File;
import java.util.ArrayList;

/**
 * Stores information regarding the uncompiled version of a project. Stored as a .vproj file.
 */
public class Project {

    /**
     * The name of the Project.
     */
    String name;

    /**
     * A list of code files for this project.
     */
    ArrayList<VisionFile> files;

    /**
     * Can only be instantiated from the factory static methods. Creates a Project with a given name and files.
     */
    private Project(String name, ArrayList<VisionFile> files) {
        this.name = name != null ? name : "empty";
        this.files = files != null ? files : new ArrayList<>();
    }

    /**
     * Returns a Project that is stored in a file.
     *
     * @param path the path of the file the Project is stored in.
     * @return the Project inside of the file. Null if the file is not found, in the wrong format, or was unsuccessful in retrieving a Project.
     */
    public static Project fromFile(String path) {
        if(path == null || path.isEmpty()){
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
        if(file == null || !file.exists() || !file.getPath().endsWith(".vproj")){
            return null;
        }
        return new Project(null,null);
    }

    public String toString(){
        return "Project["+name+"]=file size:"+files.size();
    }


}
