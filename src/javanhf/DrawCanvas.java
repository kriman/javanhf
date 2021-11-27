package javanhf;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.*;
import java.io.IOException;

/**
 * Canvas a képek kirajzolásához
 */
public class DrawCanvas extends MainFrame {
    /** A canvas alapjául szolgáló kép */
    private Mat image;
    /** A kép útvonala */
    private String image_path;

    /**
     * Canvas kirajzolása
     * @param g Graphics
     */
    public void paint(Graphics g) {
        int imgWidth = image.cols();
        int imgHeight = image.rows();

        double imgAspect = (double) imgHeight / imgWidth;

        int canvasWidth = this.getWidth();
        int canvasHeight = this.getHeight();

        double canvasAspect = (double) canvasHeight / canvasWidth;

        int x1 = 0;
        int y1 = 0;
        int x2;
        int y2;

        if (imgWidth < canvasWidth && imgHeight < canvasHeight) {
            x1 = (canvasWidth - imgWidth)  / 2;
            y1 = (canvasHeight - imgHeight) / 2;
            x2 = imgWidth + x1;
            y2 = imgHeight + y1;

        } else {
            if (canvasAspect > imgAspect) {
                y1 = canvasHeight;
                canvasHeight = (int) (canvasWidth * imgAspect);
                y1 = (y1 - canvasHeight) / 2;
            } else {
                x1 = canvasWidth;
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

    /**
     * Kép beállítása
     * @param image beállítandó kép
     */
    public void setImage(Mat image) {
        this.image = image;
    }

    /**
     * Kép beállítása útvonal és típus alapján
     * @param image_path kép útvonala
     * @param type kép típusa
     */
    public void setImage(String image_path, int type) {
        this.image_path = image_path;
        this.image = Imgcodecs.imread(image_path, type);
    }

    /**
     * Kép beállítása útvonal alapján
     * @param image_path kép útvonala
     */
    public void setImage(String image_path) {
        setImage(image_path, Imgcodecs.IMREAD_UNCHANGED);
    }

    /**
     * Kép útvonalát adja vissza
     * @return útvonal
     */
    public String getImage_path() {
        return image_path;
    }

    /**
     * Kép útvonalának beállítása
     * @param image_path útvonal
     */
    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    /**
     * Visszadja a képet Mat formában
     * @return kép
     */
    public Mat getImage() {
        return image;
    }

    /**
     * Visszaadja a kép méretét
     * @return kép mérete
     */
    public Size getImageSize() {
        return image.size();
    }

    /**
     * Visszaadja a kép típusát
     * @return típus
     */
    public int getImageType() {
        return image.type();
    }
}
