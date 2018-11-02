import com.jlogical.vision.project.Project;
import org.junit.*;

import java.io.File;

public class ProjectTest {

    @Test
    public void testInvalidProject(){
        Assert.assertNull(Project.fromFile(""));
        Assert.assertNull(Project.fromFile("invalid"));
        Assert.assertNull(Project.fromFile("invalid.vproj"));
        Assert.assertNull(Project.fromFile(new File("")));
        Assert.assertNull(Project.fromFile(new File("invalid")));
        Assert.assertNull(Project.fromFile(new File("invalid.vproj")));
    }

}
