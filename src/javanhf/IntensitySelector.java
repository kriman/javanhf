package javanhf;

/**
 * Választás intenzitás alapján.
 */
public class IntensitySelector implements Selector {
    /**
     * Visszaadja a pixel intenzitását
     * @param b Blue
     * @param g Green
     * @param r Red
     * @return A három pixel értékeinek összeg Integerként
     */
    public int selector(byte b, byte g, byte r) {
        return (b & 0xff) + (g & 0xff) + (r & 0xff);
    }
}
