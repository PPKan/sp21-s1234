package flik;

import org.junit.*;
import static flik.Flik.isSameNumber;


public class FilkTest {

    @Test
    public void testNumber() {
        Assert.assertTrue(isSameNumber(300,300));
    }

}
