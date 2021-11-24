package javanhf;

/**
 * Interface selectoroknak
 */
public interface Selector {
    /**
     * BGR színkódok alapján megadott algoritmussal kiválaszt egy rendezés alapjául szolgáló értéket.
     * @param b Blue
     * @param g Green
     * @param r Red
     * @return A kiválasztott érték
     */
    int selector(byte b, byte g, byte r);
}
