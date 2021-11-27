package javanhf;

import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Statikus függvények átalakításokhoz
 */
public class ConverterTools {
    /**
     * Mat képet BufferedImage típusra alakítja
     * @param src Mat képbemenet
     * @return az eredeti kép BufferedImage formában
     * @throws IOException ha nem sikeres a beolvasás
     * @throws CvException sikertelen konvertálás
     */
    public static BufferedImage toBufferedImage(Mat src) throws IOException, CvException {

        MatOfByte matOfByte = new MatOfByte();

        Imgcodecs.imencode(".png", src, matOfByte);

        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage;
        InputStream in = new ByteArrayInputStream(byteArray);
        bufImage = ImageIO.read(in);
        return bufImage;
    }
}
