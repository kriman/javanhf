package javanhf;

import java.awt.*;

public class HSLSelector implements Selector {
    public enum HSLChannels {Hue, Saturation, Lightness};
    private HSLChannels ch;
    public HSLSelector(HSLChannels ch) {
        this.ch = ch;
    }
    public HSL rgb2hsl(byte bi, byte gi, byte ri) {
        double H = 0;
        double S = 0;
        double L = 0;

        //printArray(pixel, 3);

        double r = (ri & 0xff)/255.0f;
        double g = (gi & 0xff)/255.0f;
        double b = (ri & 0xff)/255.0f;

        double Xmin = Double.min(r, g);
        Xmin = Double.min(Xmin, b);
        double Xmax = Double.max(r, g);
        Xmax = Double.max(Xmax, b);

        L = (Xmin + Xmax)/2.0;
        if (Xmin != Xmax) {
            if (L < 0.5)
                S = (Xmax - Xmin)/(Xmax + Xmin);
            else
                S = (Xmax - Xmin)/(2 - Xmax - Xmin);
            if (r == Xmax)
                H = (g-b)/(Xmax - Xmin) + ((g < b) ? 6 : 0);
            else if (g == Xmax)
                H = 2 + (b-r)/(Xmax - Xmin);
            else if (b == Xmax)
                H = 4 + (r-g)/(Xmax - Xmin);

            if (H < 0.0)
                H += 6;
            H /= 6.0;
        }

        HSL hsl = new HSL(H, S, L);
        return hsl;
    }
    @Override
    public int selector(byte b, byte g, byte r) {
        HSL hsl = rgb2hsl(b, g, r);
        float[] hsbvals = new float[3];
        Color.RGBtoHSB(r,g,b, hsbvals);
        switch (ch) {
            case Hue:
                //System.out.println(hsl.Hue + " " + hsbvals[0]);
                return (int)(hsbvals[0]*360);
            case Saturation:
                return (int)(hsl.Saturation*255);
            case Lightness:
                return (int)(hsl.Lightness*255);
        }
        return 0;
    }
}
