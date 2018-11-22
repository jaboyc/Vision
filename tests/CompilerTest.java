import com.jlogical.vision.compiler.Compiler;
import com.jlogical.vision.compiler.exceptions.VisionException;
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
    private String compileAndRun(String path) throws VisionException {
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
    public void testSuccess(){
        assertTrue(compile("res/helloworld.txt"));
    }

    @Test
    public void testProjects() throws VisionException{
        assertEquals(compileAndRun("res/helloworld.txt"), "Hello World");
    }
}
