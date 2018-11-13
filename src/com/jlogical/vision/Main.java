package com.jlogical.vision;

import com.jlogical.vision.compiler.Compiler;
import com.jlogical.vision.compiler.Script;
import com.jlogical.vision.compiler.exceptions.CompilerException;
import com.jlogical.vision.project.Project;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            Project project = Project.fromFile("res/test.vproj");
            Script script = Compiler.compile(project);
            //System.out.println(script.succeeded());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
