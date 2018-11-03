import com.jlogical.vision.project.Project;
import org.junit.*;

import java.io.File;

import static org.junit.Assert.*;

public class ProjectTest {

    @Test
    public void testInvalidProject(){
        assertNull(Project.fromFile(""));
        assertNull(Project.fromFile("res/invalid"));
        assertNull(Project.fromFile("res/invalid.vproj"));
        assertNull(Project.fromFile(new File("")));
        assertNull(Project.fromFile(new File("res/invalid")));
        assertNull(Project.fromFile(new File("res/invalid.vproj")));
    }

    @Test
    public void testValidProject(){
        assertNotNull(Project.fromFile("res/test.vproj"));
        assertNotNull(Project.fromFile(new File("res/test.vproj")));
    }

}
