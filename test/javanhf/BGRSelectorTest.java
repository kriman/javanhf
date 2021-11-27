package javanhf;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BGRSelectorTest {
    BGRSelector selectorRed;
    BGRSelector selectorBlue;
    BGRSelector selectorGreen;

    @Before
    public void setup() {
        selectorRed = new BGRSelector(BGRSelector.BGRvals.Red);
        selectorBlue = new BGRSelector(BGRSelector.BGRvals.Blue);
        selectorGreen = new BGRSelector(BGRSelector.BGRvals.Green);
    }
    @Test
    public void TestRed() {
        assertEquals( 122, selectorRed.selector((byte)255, (byte)232, (byte)122));
    }
    @Test
    public void TestBlue() {
        assertEquals( 255, selectorBlue.selector((byte)255, (byte)232, (byte)122));
    }
    @Test
    public void TestGreen() {
        assertEquals( 232, selectorGreen.selector((byte)255, (byte)232, (byte)122));
    }

}