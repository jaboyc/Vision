import com.jlogical.vision.compiler.Compiler;
import com.jlogical.vision.compiler.script.Script;
import com.jlogical.vision.project.Project;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CompilerTest {

    /**
     * Compiles a Project based on the given file location and returns whether it was a success.
     * @param path the path to the file.
     * @return whether the compilation was a success.
     */
    private boolean compile(String path) {
        try {
            Project project = Project.fromTextFile(path, "test");
            Script script = Compiler.compile(project);
            return script.succeeded();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Compiles a Project from the given file and returns the output log after it has run.
     * @param path the path to the file.
     * @return the log of the Script.
     */
    private String compileAndRun(String path){
        try {
            Project project = Project.fromTextFile(path, "test");
            Script script = Compiler.compile(project);
            script.start();
            return script.getOutputLog();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Test
    public void testCompileSuccess(){
        assertTrue(compile("res/helloworld.txt"));
        assertTrue(compile("res/variable1.txt"));
        assertTrue(compile("res/variable2.txt"));
        assertTrue(compile("res/variable3.txt"));
    }

    @Test
    public void testProjects(){
        assertEquals(compileAndRun("res/helloworld.txt"), "Hello World");
        assertEquals(compileAndRun("res/variable1.txt"), "Hello World");
        assertEquals(compileAndRun("res/variable2.txt"), "5.0");
        assertEquals(compileAndRun("res/variable3.txt"), "5.0");
    }
}
