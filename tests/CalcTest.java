import com.jlogical.vision.compiler.Compiler;
import com.jlogical.vision.compiler.exceptions.VisionException;
import com.jlogical.vision.compiler.script.Script;
import com.jlogical.vision.compiler.values.ExpressionValue;
import com.jlogical.vision.project.CodeLocation;
import com.jlogical.vision.project.CodeRange;
import com.jlogical.vision.project.Project;
import com.jlogical.vision.util.Calc;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CalcTest {

    /**
     * Returns the result of a calculation.
     * @param text the text to calculate.
     * @return the result.
     * @throws VisionException if parsing didn't work.
     */
    private Object calc(String text) throws VisionException{
        return Calc.calc(new ExpressionValue(text,  new ArrayList<>(), CodeRange.fromCodeLocation(CodeLocation.emptyReference()), null));
    }

    @Test
    public void testNumbers() throws Exception{
        assertEquals(calc("1"), 1.0);
        assertEquals(calc("-1"), -1.0);
        assertEquals(calc("0"), 0.0);
        assertEquals(calc("3.14"), 3.14);
    }

    @Test
    public void testSimple() throws Exception{
        assertEquals(calc("1+1"), 2.0);
        assertEquals(calc("4-1"), 3.0);
        assertEquals(calc("7*4"), 28.0);
        assertEquals(calc("8/2"), 4.0);
        assertEquals(calc("3^2"), 9.0);
    }

    @Test
    public void testComplex() throws Exception{
        assertEquals(calc("5+4*4"), 21.0);
        assertEquals(calc("-2.00000+1*2"), 0.0);
        assertEquals(calc("10^10/1000000000"), 10.0);
        assertEquals(calc("1+2*3/4+5-6"), 1.5);
    }
}
