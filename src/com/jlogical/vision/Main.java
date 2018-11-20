package com.jlogical.vision;

import com.jlogical.vision.compiler.Compiler;
import com.jlogical.vision.compiler.script.Script;
import com.jlogical.vision.project.Project;
import com.jlogical.vision.project.VisionFile;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        try {
            Project project = Project.fromTextFile("res/helloworld.txt", "test");
            Script script = Compiler.compile(project);
            script.start();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
