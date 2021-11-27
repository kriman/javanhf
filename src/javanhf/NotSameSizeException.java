package javanhf;

/**
 * Kivétel, akkor, ha két kép mérete nem egyezik.
 */
public class NotSameSizeException extends Exception {
    /**
     * Létrehoz egy NotSameSizeException objektumot
     * @param msg kivétel szövege
     */
    public NotSameSizeException(String msg) {
        super(msg);
    }
}
