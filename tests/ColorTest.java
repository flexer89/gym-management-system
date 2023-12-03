import org.junit.Assert;
import org.junit.Test;

import utils.Color;

public class ColorTest {
    @Test
    public void testColorBgString() {
        String expected = "\u001B[40m\u001B[31mHello\u001B[0m";
        String actual = Color.ColorBgString(Color.ANSI_BLACK_BACKGROUND, "Hello", Color.ANSI_RED);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testColorString() {
        String expected = "\u001B[31mHello\u001B[0m";
        String actual = Color.ColorString("Hello", Color.ANSI_RED);
        Assert.assertEquals(expected, actual);
    }
}