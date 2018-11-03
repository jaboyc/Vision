package com.jlogical.vision;

import com.jlogical.vision.compiler.Script;
import com.jlogical.vision.compiler.exceptions.CompilerException;
import com.jlogical.vision.compiler.exceptions.FileFormatException;
import com.jlogical.vision.project.Project;
import com.jlogical.vision.compiler.Compiler;
import com.jlogical.vision.project.VisionFile;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Project project = Project.blank("test");
        project.getFiles().add(new VisionFile("main", "when started\n\tprint [Hello World]\nend"));
        try {
            project.save(new File("res/test.vproj"));
        } catch (FileFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Script script = Compiler.compile(project, Compiler.Detail.BASIC);
        System.out.println(script.compileSuccess());
    }
}
