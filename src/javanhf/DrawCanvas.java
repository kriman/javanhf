package javanhf;

import org.opencv.core.Mat;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.*;
import java.io.IOException;

public class DrawCanvas extends Canvas{
    private Mat image = new Mat();
    private String image_path;

    public void paint(Graphics g) {
        int imgWidth = image.cols();
        int imgHeight = image.rows();

        double imgAspect = (double) imgHeight / imgWidth;

        int canvasWidth = this.getWidth();
        int canvasHeight = this.getHeight();

        double canvasAspect = (double) canvasHeight / canvasWidth;

        int x1 = 0; // top left X position
        int y1 = 0; // top left Y position
        int x2; // bottom right X position
        int y2; // bottom right Y position

        if (imgWidth < canvasWidth && imgHeight < canvasHeight) {
            // the image is smaller than the canvas
            x1 = (canvasWidth - imgWidth)  / 2;
            y1 = (canvasHeight - imgHeight) / 2;
            x2 = imgWidth + x1;
            y2 = imgHeight + y1;

        } else {
            if (canvasAspect > imgAspect) {
                y1 = canvasHeight;
                // keep image aspect ratio
                canvasHeight = (int) (canvasWidth * imgAspect);
                y1 = (y1 - canvasHeight) / 2;
            } else {
                x1 = canvasWidth;
                // keep image aspect ratio
                canvasWidth = (int) (canvasHeight / imgAspect);
                x1 = (x1 - canvasWidth) / 2;
            }
            x2 = canvasWidth + x1;
            y2 = canvasHeight + y1;
        }

        try {
            g.drawImage(ConverterTools.toBufferedImage(image),  x1, y1, x2, y2, 0, 0, imgWidth, imgHeight, null);
        } catch (IOException e) {
            //e.printStackTrace();
        }
        catch (CvException e) {
            e.printStackTrace();
        }

    }

    public void setImage(Mat image) {
        this.image = image;
    }

    public void setImage(String image_path, int type) {
        this.image_path = image_path;
        this.image = Imgcodecs.imread(image_path, type);
    }
    public void setImage(String image_path) {
        setImage(image_path, Imgcodecs.IMREAD_UNCHANGED);
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public Mat getImage() {
        return image;
    }
    public Size getImageSize() {
        return image.size();
    }
    public int getImageType() {
        return image.type();
    }
}
