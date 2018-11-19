package com.jlogical.vision;

import com.jlogical.vision.compiler.Compiler;
import com.jlogical.vision.compiler.script.Script;
import com.jlogical.vision.project.Project;
import com.jlogical.vision.project.VisionFile;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        String code = "when started\nprint [Hey!]\nend";

        Project project = Project.blank("test");
        project.getFiles().add(new VisionFile("main", code));
        Script script = Compiler.compile(project);
        script.start();
    }
}
