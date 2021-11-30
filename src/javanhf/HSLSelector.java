package javanhf;

import java.awt.*;

import static java.lang.Math.round;

/**
 * Választás HSL színkomponensek alapján
 */
public class HSLSelector implements Selector {
    /** A HSL színkomponensek lehetséges értékei */
    public enum HSLChannels {
        /** Hue */
        Hue,
        /** Saturation */
        Saturation,
        /** Lightness */
        Lightness
    }
    /** A kiválasztás alapja */
    private final HSLChannels ch;

    /**
     * Létrehoz egy HSLSelectort a kiválaszott csatornával.
     * @param ch kiválasztott csatorna
     * @see HSLChannels
     */
    public HSLSelector(HSLChannels ch) { this.ch = ch; }

    /**
     * Visszaadja a kiválasztott csatorna értékét. A BGR színeket a Color.RGBtoHSB segítségével alakítja át
     * @param b Blue
     * @param g Green
     * @param r Red
     * @see java.awt.Color
     * @return kiválasztott csatorna értéke Integerként
     */
    @Override
    public int selector(byte b, byte g, byte r) {
        float[] hsbvals = new float[3];
        Color.RGBtoHSB(r & 0xff, g & 0xff, b & 0xff, hsbvals);
        return switch (ch) {
            case Hue -> round((hsbvals[0] * 360));
            case Saturation -> round(hsbvals[1] * 100);
            case Lightness -> round(hsbvals[2] * 100);
        };
    }
}
