package javanhf;

public class IntensitySelector implements Selector {
    public int selector(byte b, byte g, byte r) {
        return (b & 0xff) + (g & 0xff) + (r & 0xff);
    }
}
