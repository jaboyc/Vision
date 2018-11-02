package com.jlogical.vision;

import com.jlogical.vision.project.Project;

public class Main {
    public static void main(String[] args) {
        Project project = Project.fromFile("C:\\Users\\jakeb\\OneDrive\\Documents\\Java Projects\\Project Data\\test.vproj");
        System.out.println(project);
    }
}
