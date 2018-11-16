package com.jlogical.vision.project;

import com.jlogical.vision.compiler.exceptions.FileFormatException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

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
     *
     * @param name the name of the project.
     * @return the blank project.
     */
    public static Project blank(String name) {
        return new Project(name, new ArrayList<>());
    }

    /**
     * Returns a Project that is stored in a file.
     *
     * @param path the path of the file the Project is stored in.
     * @return the Project inside of the file. Null if the file is not found, in the wrong format, or was unsuccessful in retrieving a Project.
     */
    public static Project fromFile(String path) throws IOException {
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
     * @throws IOException if there was an error opening a ZIP.
     */
    public static Project fromFile(File file) throws IOException {
        if (file == null || !file.exists() || !file.getPath().endsWith(".vproj")) {
            return null;
        }
        JSONObject json = unzipJSon(new ZipFile(file));
        return fromJSon(json);
    }

    /**
     * Converts a JSon to a Project.
     *
     * @param json the JSON to convert.
     * @return the Project. Null if not able to convert.
     */
    private static Project fromJSon(JSONObject json) {
        try {
            Object oName = json.get("name");
            if (oName == null) {
                return null;
            }
            String name = (String) oName;
            ArrayList<VisionFile> vFiles = new ArrayList<>();
            JSONArray files = (JSONArray) json.get("code");
            for (int i = 0; i < files.size(); i++) {
                JSONObject file = (JSONObject) files.get(i);
                VisionFile vfile = VisionFile.fromJSon(file);
                vFiles.add(vfile);
            }
            return new Project(name, vFiles);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Unzips a ZipFile and unpacks the project.json file inside of it.
     *
     * @param zip the ZipFile to unpack.
     * @return the json of project.json if found. Null if not found.
     * @throws IOException           if there was an error opening up the ZIP or parsing.
     * @throws FileNotFoundException if the file given was null.
     */
    private static JSONObject unzipJSon(ZipFile zip) throws IOException {
        if (zip == null) {
            throw new FileNotFoundException();
        }
        try {
            ZipEntry entry = zip.getEntry("project.json");
            if (entry == null) { //If project.json not found
                return null;
            }
            InputStream stream = zip.getInputStream(entry);
            byte[] bytes = stream.readAllBytes();
            String s = new String(bytes);
            JSONParser parse = new JSONParser();
            JSONObject obj = (JSONObject) parse.parse(s);
            return obj;
        } catch (ParseException e) {
            throw new IOException("Error parsing JSon");
        }
    }

    /**
     * Saves the Project in a File as a ZIP with an extension of .vproj.
     *
     * @param file the File to save the Project in.
     * @throws FileFormatException   if the File does not end with .vproj
     * @throws FileNotFoundException if the File is null.
     * @throws IOException           if there is an error writing to the file.
     */
    public void save(File file) throws FileFormatException, IOException {
        if (file == null) {
            throw new FileNotFoundException();
        }
        if (!file.getPath().endsWith(".vproj")) {
            throw new FileFormatException("Needs to be .vproj format.");
        }
        zip(file);
    }

    /**
     * Compresses the Project into a ZIP and stores it in the given file.
     *
     * @param file the File to store the Project in.
     * @throws IOException if there is an error writing to the file.
     */
    private void zip(File file) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
        out.putNextEntry(new ZipEntry("project.json"));
        out.write(toJSon().toJSONString().getBytes());
        out.closeEntry();
        out.close();
    }

    /**
     * @return the JSON version of the Project.
     */
    private JSONObject toJSon() {
        JSONObject root = new JSONObject();
        JSONArray files = new JSONArray();
        for (VisionFile file : this.files) {
            files.add(file.toJSon());
        }
        root.put("code", files);
        root.put("name", name);
        return root;
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

    @Override
    public String toString() {
        return "Project[" + name + "]=file size:" + files.size();
    }
}
