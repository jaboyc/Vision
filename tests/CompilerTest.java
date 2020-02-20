import com.jlogical.vision.compiler.Compiler;
import com.jlogical.vision.compiler.exceptions.CompilerException;
import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.compiler.script.Script;
import com.jlogical.vision.project.Project;
import org.junit.jupiter.api.Test;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

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
    public void testCompileSuccess(){
        assertTrue(compile("res/helloworld.txt"));
        assertTrue(compile("res/variable1.txt"));
        assertTrue(compile("res/variable2.txt"));
        assertTrue(compile("res/variable3.txt"));
        assertTrue(compile("res/operators1.txt"));
        assertTrue(compile("res/operators2.txt"));
        assertTrue(compile("res/control1.txt"));
        assertTrue(compile("res/control2.txt"));
        assertTrue(compile("res/string_interpolation1.txt"));
        assertTrue(compile("res/string_interpolation2.txt"));
        assertTrue(compile("res/string_interpolation3.txt"));
        assertTrue(compile("res/compiler_sugar.txt"));
        assertTrue(compile("res/definitions.txt"));
        assertTrue(compile("res/list.txt"));
        assertTrue(compile("res/string.txt"));
        assertTrue(compile("res/custom_objects.txt"));
        assertTrue(compile("res/hangman.txt"));
        assertTrue(compile("res/tictactoe.txt"));
    }

    @Test
    public void testProjects() throws VisionException{
        assertEquals(compileAndRun("res/helloworld.txt"), "Hello World");
        assertEquals(compileAndRun("res/variable1.txt"),  "Hello World");
        assertEquals(compileAndRun("res/variable2.txt"),  "5");
        assertEquals(compileAndRun("res/variable3.txt"),  "5\n14");
        assertEquals(compileAndRun("res/operators1.txt"), "5\n5\n5");
        assertEquals(compileAndRun("res/operators2.txt"), "false\ntrue\n5\n5\n5");
        assertEquals(compileAndRun("res/control1.txt"),   "3");
        assertEquals(compileAndRun("res/control2.txt"), "1\n2\n3");
        assertEquals(compileAndRun("res/string_interpolation1.txt"), "Hello World");
        assertEquals(compileAndRun("res/string_interpolation2.txt"), "pi="+Math.PI+". e="+Math.E);
        assertEquals(compileAndRun("res/string_interpolation3.txt"), "#3+2=5#");
        assertEquals(compileAndRun("res/compiler_sugar.txt"), "3");
        assertEquals(compileAndRun("res/definitions.txt"), "1\nHello Jake\n0\n9");
        assertEquals(compileAndRun("res/list.txt"), "1\n2\n10\n6\n6\nfalse\ntrue\n2\n5\n0\ntrue\n1\n3\n6");
        assertEquals(compileAndRun("res/string.txt"), "11\nH\nHello World!\ns: Hello World");
        assertEquals(compileAndRun("res/custom_objects.txt"), "Robinson Crusoe");
    }

    @Test
    public void testExceptions(){
        assertFalse(compile("res/compiler_exception.txt"));
    }
}
