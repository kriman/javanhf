package javanhf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static java.lang.Math.round;
import static org.junit.jupiter.api.Assertions.*;

class HSLSelectorTest {
    HSLSelector selectorhue;
    HSLSelector selectorsat;
    HSLSelector selectorlig;

    @BeforeEach
    void setUp() {
        selectorhue = new HSLSelector(HSLSelector.HSLChannels.Hue);
        selectorsat = new HSLSelector(HSLSelector.HSLChannels.Saturation);
        selectorlig = new HSLSelector(HSLSelector.HSLChannels.Lightness);
    }

    @Test
    void selector() {
        byte b = (byte) 41;
        byte g = (byte) 209;
        byte r = (byte) 170;

        assertEquals(74, selectorhue.selector(b,g,r));
        assertEquals(80, selectorsat.selector(b,g,r));
        assertEquals(82, selectorlig.selector(b,g,r));

        float[] hsbvals = new float[3];
        Color.RGBtoHSB(170,209,41, hsbvals);

        assertEquals(round(hsbvals[0]*360), selectorhue.selector(b,g,r));
        assertEquals(round(hsbvals[1]*100), selectorsat.selector(b,g,r));
        assertEquals(round(hsbvals[2]*100), selectorlig.selector(b,g,r));
    }
}