package javanhf;

import org.opencv.core.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class pixelsorthoz
 */
public class Pixelsort {
    /** A rendezés lehetséges irányai */
    enum direction {
        /** Függőleges rendezés */
        vertical,
        /** Vízszintes rendezés */
        horizontal}

    /** Rendezési sorrend invertálása */
    private boolean invert = false;
    /** Rendezési szempont választása */
    private Selector selector;

    /**
     * Selector beállítása a rendezéshez
     * @param selector Selector típusú objektum
     */
    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    /**
     * Rendezés sorrendjének invertálása
     * @param invert Invertálás
     */
    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    /**
     * Kép rendezése megadott maszk alapján, a megadott irányban
     * @param img rendezendő kép
     * @param mask használandó maszk
     * @param dir kijelölt irány
     * @return Rendezett kép
     * @throws NotSameSizeException Ha a maszk és a kép mérete nem egyezik
     */
    public Mat sort(Mat img, Mat mask, direction dir) throws NotSameSizeException {

        if (img.width() != mask.width() || img.height() != mask.height()) {
            throw new NotSameSizeException("Cannot sort image (" + img.size() + ") with mask (" + mask.size() + ")");
        }

        int dir1 = dir == direction.vertical ? img.rows() : img.cols();
        int dir2 = dir == direction.vertical ? img.cols() : img.rows();

        byte[] buff = new byte[(int) (mask.total() * mask.channels())];
        mask.get(0, 0, buff);

        byte[] img_buff = new byte[(int) (img.total() * img.channels())];
        img.get(0, 0, img_buff);

        for (int col = 0; col < dir2; col++) {
            int stop;
            if (!mask.empty()) {
                int row = 0;
                while (row < dir1) {
                    byte maskpixel;
                    if (dir == direction.vertical)
                        maskpixel = buff[row * mask.cols() * mask.channels() + col * mask.channels()];
                    else
                        maskpixel = buff[col * mask.cols() * mask.channels() + row * mask.channels()];

                    if (maskpixel == (byte) 255) { // start to include
                        stop = row;
                        for (int x = row; x < dir1; x++) {
                            byte maskpixel2;
                            if (dir == direction.vertical)
                                maskpixel2 = buff[x * mask.cols() * mask.channels() + col * mask.channels()];
                            else
                                maskpixel2 = buff[col * mask.cols() * mask.channels() + x * mask.channels()];
                            if (maskpixel2 != (byte) 255 || x + 1 == dir1) {
                                stop = x + 1;
                                break;
                            }
                        }
                        sortcols(img, row, stop, col, dir, img_buff);
                        row = stop;
                    }
                    row++;
                }
            } else {
                sortcols(img, 0, dir1, col, dir, img_buff);
            }
        }
        img.put(0, 0, img_buff);
        return img;
    }

    /**
     * Kijelelölt oszlopot vagy sort rendezi egy megadott intervallumon
     * @param img Eredeti kép
     * @param start intervallum kezdete
     * @param stop intervallum vége
     * @param col kijelölt sor/oszlop
     * @param dir kijelölt irány
     * @param img_buff a sort által átadott tömb, ami rendezve lesz
     */
    private void sortcols(Mat img, int start, int stop, int col, direction dir, byte[] img_buff) {
        /* select values to sort by */
        int size = stop - start;
        int[] partline = new int[size];
        /* az értékek kigyűjtése */
        int i = 0;

        for (int rowi = start; rowi < stop; rowi++) {
            int index;
            if (dir == direction.vertical) {
                index = rowi * img.cols() * img.channels() + col * img.channels();
            } else { // horizontal
                index = col * img.cols() * img.channels() + rowi * img.channels();
            }

            byte b = img_buff[index];
            byte g = img_buff[index + 1];
            byte r = img_buff[index + 2];

            partline[i++] = (selector.selector(b, g, r));
        }

        /* az értékek rendezése */
        Integer[] indices = new Integer[size];
        for (int j = 0; j < size; ++j) {
            indices[j] = j;
        }

        Arrays.sort(indices, (o1, o2) -> {
            if (!invert)
                return Integer.compare(partline[o1], partline[o2]);
            else
                return Integer.compare(partline[o2], partline[o1]);
        });


        /* a rendezett pixelek átmásolása */
        ArrayList<Byte> newarray = new ArrayList<>();
        for (int rowi = 0; rowi < size; rowi++) {
            int index;
            if (dir == direction.vertical) {
                index = (indices[rowi] + start) * img.cols() * img.channels() + col * img.channels();
            } else {
                index = (col) * img.cols() * img.channels() + (indices[rowi] + start) * img.channels();
            }

            newarray.add(img_buff[index]);
            newarray.add(img_buff[index + 1]);
            newarray.add(img_buff[index + 2]);
        }

        /* a rendezett pixelek visszamásolása a képre */
        int start_i = start;
        for (int j = 0; j < size * 3; j += 3) {
            int index;
            if (dir == direction.vertical) {
                index = (start_i++) * img.cols() * img.channels() + col * img.channels();
            } else {
                index = (col) * img.cols() * img.channels() + start_i++ * img.channels();
            }

            img_buff[index] = newarray.get(j);
            img_buff[index + 1] = newarray.get(j + 1);
            img_buff[index + 2] = newarray.get(j + 2);
        }
    }
}
