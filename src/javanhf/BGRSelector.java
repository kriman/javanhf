package javanhf;

/**
 * Választás BGR színkomponensek alapján.
 */
@SuppressWarnings("ALL")
public class BGRSelector implements Selector{
    /** A BGR színkomponensek lehetséges értékei */
    public enum BGRvals {
        /** Blue */
        Blue,
        /** Green */
        Green,
        /** Red */
        Red
    }
    /** A kiválasztás alapja */
    private final BGRvals channel;

    /**
     * Létrehoz egy BGRSelectort a kiválasztott csatornával.
     * @param ch kiválasztott csatorna
     * @see BGRvals
     */
    public BGRSelector(BGRvals ch) {channel = ch;}

    /**
     * Visszaadja a kiválasztott csatorna értékét
     * @param b Blue
     * @param g Green
     * @param r Red
     * @return kiválasztott csatorna értéke Integerként
     */
    @Override
    public int selector(byte b, byte g, byte r) {
        return switch (channel) {
            case Blue -> b & 0xff;
            case Green -> g & 0xff;
            case Red -> r & 0xff;
        };
    }
}
