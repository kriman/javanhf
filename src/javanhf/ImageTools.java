package javanhf;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;

public class ImageTools {
    public static Mat addNoise(Mat src) {
        //Creating destination matrix
        Mat dst = new Mat(src.rows(), src.cols(), src.type());
        //Creating a matrix for the noise
        Mat noise = new Mat(src.rows(), src.cols(), src.type());
        //Calculating the mean and standard deviation
        MatOfDouble mean = new MatOfDouble();
        MatOfDouble dev = new MatOfDouble();
        Core.meanStdDev(src, mean, dev);
        //Filling the noise matrix
        Core.randn(noise, mean.get(0,0)[0], dev.get(0,0)[0]);
        //Adding noise to the destination
        Core.add(src, noise, dst);
        return dst;
    }
}
