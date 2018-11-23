package com.jlogical.vision.project;

import com.jlogical.vision.api.API;
import com.jlogical.vision.api.elements.CustomCBlock;
import com.jlogical.vision.api.elements.CustomCommand;
import com.jlogical.vision.api.elements.CustomHat;
import com.jlogical.vision.api.elements.CustomReporter;
import com.jlogical.vision.api.system.CoreAPI;
import com.jlogical.vision.compiler.exceptions.FileFormatException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Stores information regarding the uncompiled version of a project. Stored as a ZIP file disguised as a .vproj file. A project.json file can be found in the ZIP containing the information and code for the project.
 */
public class Project {

    /**
     * Name of the Project.
     */
    private String name;

    /**
     * List of code files for this project.
     */
    private ArrayList<VisionFile> files;

    /**
     * List of APIs in the Project.
     */
    private ArrayList<API> apis;

    /**
     * List of the sorted CustomCommands of all the APIs combined.
     */
    private ArrayList<CustomCommand> commands;

    /**
     * List of the sorted CustomReporters of all the APIs combined.
     */
    private ArrayList<CustomReporter> reporters;

    /**
     * List of the sorted CustomHat of all the APIs combined.
     */
    private ArrayList<CustomHat> hats;

    /**
     * List of the sorted CBlocks of all the APIs combined.
     */
    private ArrayList<CustomCBlock> cblocks;

    /**
     * Can only be instantiated from the factory static methods. Creates a Project with a given name and files.
     */
    private Project(String name, ArrayList<VisionFile> files, ArrayList<API> apis) {
        this.name = name != null ? name : "empty";
        this.files = files != null ? files : new ArrayList<>();
        this.apis = apis != null ? apis : new ArrayList<>(Arrays.asList(new CoreAPI(this)));
        commands = new ArrayList<>();
        reporters = new ArrayList<>();
        hats = new ArrayList<>();
        cblocks = new ArrayList<>();
        for (API api : this.apis) {
            indexCustomElements(api);
        }
    }

    /**
     * Returns a blank project.
     *
     * @param name the name of the project.
     * @return the blank project.
     */
    public static Project blank(String name) {
        return new Project(name, null, null);
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
     * Returns a single filed Project whose main code is in the given text file.
     *
     * @param file the .txt file the code is in.
     * @param name the name of the Project.
     * @return the Project. Null if the file is null, not found, or not in a .txt file.
     * @throws IOException if there was an error opening the file.
     */
    public static Project fromTextFile(File file, String name) throws IOException{
        if(file == null || !file.exists() || !file.getPath().endsWith(".txt")){
            return null;
        }
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String code = "";
        String line;
        while((line = reader.readLine()) != null){
            code += line + "\n";
        }
        return new Project(name, new ArrayList<>(Arrays.asList(new VisionFile("main", code))), null);
    }

    /**
     * Returns a single-filed Project whose main code is in the given text file.
     *
     * @param path the path to the .txt file.
     * @param name the name of the Project.
     * @return the Project. Null if the path is null, not found, or not a .txt file.
     * @throws IOException if there was an error opening the file.
     */
    public static Project fromTextFile(String path, String name) throws IOException{
        if (path == null || path.isEmpty()) {
            return null;
        }
        return fromTextFile(new File(path), name);
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
            return new Project(name, vFiles, null); //TODO Implement APIs stored in files and imported into Projects.
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
     * @param file the File to store the Project in. Must be valid.
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

    /**
     * Adds an API to the Projects APIs and sorts it.
     * @param api the API to add.
     */
    public void addAPI(API api) {
        if (api == null) {
            return;
        }
        apis.add(api);
        indexCustomElements(api);
    }

    /**
     * Gets all the CustomElements of the API
     *
     * @param api
     */
    private void indexCustomElements(API api) {
        commands.addAll(api.getCommands());
        Collections.sort(commands, Comparator.comparing(CustomCommand::getCore));
        reporters.addAll(api.getReporters());
        Collections.sort(reporters, Comparator.comparing(CustomReporter::getCore));
        hats.addAll(api.getHats());
        Collections.sort(hats, Comparator.comparing(CustomHat::getCore));
        cblocks.addAll(api.getCBlocks());
        Collections.sort(cblocks, Comparator.comparing(CustomCBlock::getCore));
    }

    @Override
    public String toString() {
        return "Project[" + name + "]=file size:" + files.size();
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

    /**
     * Returns the APIs. Do not use this to add APIs. Use .addAPI() instead in order to sort the CustomElements into the Project.
     * @return the APIs.
     */
    public ArrayList<API> getApis() {
        return apis;
    }

    public ArrayList<CustomCommand> getCommands() {
        return commands;
    }

    public ArrayList<CustomReporter> getReporters() {
        return reporters;
    }

    public ArrayList<CustomHat> getHats() {
        return hats;
    }

    public ArrayList<CustomCBlock> getCBlocks() {
        return cblocks;
    }
}
