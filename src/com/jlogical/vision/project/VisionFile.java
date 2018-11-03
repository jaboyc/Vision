package com.jlogical.vision.project;

import org.json.simple.JSONObject;

/**
 * Stores information for one file of code.
 */
public class VisionFile {
    /**
     * The name of the file.
     */
    private String name;

    /**
     * The code of the file.
     */
    private String code;

    /**
     * Creates a new VisionFile.
     * @param name the name of the file.
     * @param code the code of the file.
     */
    public VisionFile(String name, String code){
        this.name = name;
        this.code = code;
    }

    /**
     * @return the VisionFile in JSON format.
     */
    String toJSon(){
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("code", code);
        return obj.toJSONString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
