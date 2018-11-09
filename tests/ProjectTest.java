import com.jlogical.vision.project.Project;
import com.jlogical.vision.project.VisionFile;
import org.junit.*;

import java.io.File;

import static org.junit.Assert.*;

public class ProjectTest {

    @Test
    public void testInvalidProject() throws Exception{
        assertNull(Project.fromFile(""));
        assertNull(Project.fromFile("res/invalid"));
        assertNull(Project.fromFile("res/invalid.vproj"));
        assertNull(Project.fromFile(new File("")));
        assertNull(Project.fromFile(new File("res/invalid")));
        assertNull(Project.fromFile(new File("res/invalid.vproj")));
    }

    @Test
    public void testValidProject() throws Exception{
        assertNotNull(Project.fromFile("res/test.vproj"));
        assertNotNull(Project.fromFile(new File("res/test.vproj")));
    }

    @Test
    public void testOpenProject() throws Exception{
        Project project = Project.fromFile("res/test.vproj");
        assertEquals(project.getName(), "test1");
        assertEquals(project.getFiles().size(), 1);
        assertEquals(project.getFiles().get(0).getName(), "main");
        assertEquals(project.getFiles().get(0).getCode(), "when started\n\tprint [Hello World]\nend");
    }

    @Test
    public void testSaveAndOpenProject() throws Exception{

        File file = new File("res/mtest.vproj");

        Project project = Project.blank("testOpenSave");
        project.getFiles().add(new VisionFile("name", "code"));
        project.save(file);

        Project openProject = Project.fromFile(file);
        assertEquals(openProject.getName(), "testOpenSave");
        assertEquals(openProject.getFiles().size(), 1);
        assertEquals(openProject.getFiles().get(0).getName(), "name");
        assertEquals(openProject.getFiles().get(0).getCode(), "code");

        file.delete();
    }

}
