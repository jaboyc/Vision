package com.jlogical.vision;

import com.jlogical.vision.compiler.Script;
import com.jlogical.vision.compiler.exceptions.CompilerException;
import com.jlogical.vision.project.Project;
import com.jlogical.vision.compiler.Compiler;
import com.jlogical.vision.project.VisionFile;

public class Main {
    public static void main(String[] args) {
        Project project = Project.blank("test");
        project.getFiles().add(new VisionFile("main", "when started\n\tprint [Hello World]\nend"));
        Script script = Compiler.compile(project, Compiler.Detail.BASIC);
        System.out.println(script.compileSuccess());
    }
}
