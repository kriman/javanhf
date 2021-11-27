package javanhf;

import nu.pattern.OpenCV;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

import static org.junit.jupiter.api.Assertions.*;

class PixelsortTest {
    Mat img;
    Mat mask;
    Pixelsort ps;

    @BeforeEach
    void setUp() {
        OpenCV.loadLocally();

        img = new Mat();
        img = Mat.zeros(new Size(10,10), CvType.CV_8UC3);
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                img.put(i, j, new byte[] {(byte)j, (byte)j, (byte)j});
            }
        }

        mask = new Mat();
        mask = Mat.zeros(new Size(10,10), CvType.CV_8UC1);
        mask.setTo(new Scalar(255,255,255));

        ps = new Pixelsort();
        ps.setInvert(true);
        ps.setSelector(new BGRSelector(BGRSelector.BGRvals.Red));

    }

    @Test
    void sort() throws NotSameSizeException {
        Mat result;
        result = ps.sort(img, mask, Pixelsort.direction.horizontal);
        assertEquals(9, result.get(0,0)[0]);
        assertEquals(0, result.get(0,9)[0]);

    }
    @Test
    void sortException(){
        assertThrows(NotSameSizeException.class, () -> ps.sort(img, new Mat(), Pixelsort.direction.horizontal));
    }
}