package javanhf;

import nu.pattern.OpenCV;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opencv.core.CvException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ConverterToolsTest {
    @BeforeEach
    void setUp() {
        OpenCV.loadLocally();
    }

    @Test
    void toBufferedImage() throws IOException {
        Mat img;
        img = Mat.zeros(new Size(10,10), CvType.CV_8UC3);
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                img.put(i, j, new byte[] {(byte)j, (byte)j, (byte)j});
            }
        }
        BufferedImage image = ConverterTools.toBufferedImage(img);
        assertEquals(10, image.getHeight());
        assertEquals(10, image.getWidth());
        Color c = new Color(image.getRGB(1,9));
        assertEquals(1, c.getRed());
        Color c2 = new Color(image.getRGB(9,1));
        assertEquals(9, c2.getRed());
    }

    @Test
    void toBufferedImageException() {
        Mat img = new Mat();
        assertThrows(CvException.class, () -> ConverterTools.toBufferedImage(img));
    }
}