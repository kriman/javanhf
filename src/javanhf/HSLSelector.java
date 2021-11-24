package javanhf;

import java.awt.*;

/**
 * Választás HSL színkomponensek alapján
 */
public class HSLSelector implements Selector {
    /** A HSL színkomponensek lehetséges értékei */
    public enum HSLChannels {Hue, Saturation, Lightness}
    /** A kiválasztás alapja */
    private HSLChannels ch;
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
        Color.RGBtoHSB(r,g,b, hsbvals);
        return switch (ch) {
            case Hue -> (int) (hsbvals[0] * 360);
            case Saturation -> (int) (hsbvals[1] * 255);
            case Lightness -> (int) (hsbvals[2] * 255);
        };
    }
}
