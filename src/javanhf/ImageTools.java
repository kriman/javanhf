package javanhf;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;

/**
 * Osztály különböző képmanipulációs eszközökhöz
 */
public class ImageTools {
    /**
     * Random zajt ad a képhez
     * @param src a kép amihez a zajt kell adni
     * @return végeredmény
     */
    public static Mat addNoise(Mat src) {
        Mat dst = new Mat(src.rows(), src.cols(), src.type());
        Mat noise = new Mat(src.rows(), src.cols(), src.type());
        MatOfDouble mean = new MatOfDouble();
        MatOfDouble dev = new MatOfDouble();
        Core.meanStdDev(src, mean, dev);
        Core.randn(noise, mean.get(0,0)[0], dev.get(0,0)[0]);
        Core.add(src, noise, dst);
        return dst;
    }
}
