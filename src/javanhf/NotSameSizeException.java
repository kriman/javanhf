package javanhf;

/**
 * Kivétel, akkor, ha két kép mérete nem egyezik.
 */
public class NotSameSizeException extends Exception {
    public NotSameSizeException(String msg) {
        super(msg);
    }
}
