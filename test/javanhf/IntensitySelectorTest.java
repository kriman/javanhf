package javanhf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntensitySelectorTest {
    Selector iselector;

    @BeforeEach
    void setUp() {
        iselector = new IntensitySelector();
    }

    @Test
    void selector() {
        assertEquals(333, iselector.selector((byte)100, (byte)111, (byte)122));
    }
}