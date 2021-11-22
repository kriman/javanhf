package javanhf;

import org.opencv.core.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Pixelsort {

    enum direction {vertical, horizontal}

    private boolean invert = false;
    private Selector selector;

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public void setInvert(boolean invert) {
        this.invert = invert;
    }

    public Mat sort(Mat img, Mat mask, direction dir) {

        int dir1 = dir == direction.vertical ? img.rows() : img.cols();
        int dir2 = dir == direction.vertical ? img.cols() : img.rows();

        byte buff[] = new byte[(int) (mask.total() * mask.channels())];
        mask.get(0, 0, buff);

        byte img_buff[] = new byte[(int) (img.total() * img.channels())];
        img.get(0, 0, img_buff);
        // working with buff
        // ...
        //mask.put(0, 0, buff);

        for (int col = 0; col < dir2; col++) {
            int stop;
            if (!mask.empty()) {
                int row = 0;
                while (row < dir1) {
                    byte maskpixel;
                    if (dir == direction.vertical)
                        maskpixel = buff[row * mask.cols() * mask.channels() + col * mask.channels()];//mask.ptr(row, col);
                    else
                        maskpixel = buff[col * mask.cols() * mask.channels() + row * mask.channels()];//mask.ptr(row, col);

                    if (maskpixel == (byte) 255) { // start to include
                        stop = row;
                        for (int x = row; x < dir1; x++) {
                            byte maskpixel2;
                            if (dir == direction.vertical)
                                //maskpixel2 = mask->ptr(x, col);
                                maskpixel2 = buff[x * mask.cols() * mask.channels() + col * mask.channels()];
                            else
                                //maskpixel2 = mask->ptr(col, x);
                                maskpixel2 = buff[col * mask.cols() * mask.channels() + x * mask.channels()];
                            if (maskpixel2 != (byte) 255 || x + 1 == dir1) {
                                stop = x + 1;
                                break;
                            }
                        }
                        //System.out.println(row + " " + stop + " " + col + " " + dir);
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

    public byte[] sortcols(Mat img, int start, int stop, int col, direction dir, byte[] img_buff) {
        /** select values to sort by */
        int size = stop - start;
        //byte[] partline = new byte[size];
        int[] partline = new int[size];
        //ArrayList<Integer> partline = new ArrayList<>();
        /** az értékek kigyűjtése */
        int i = 0;

        //byte buff[] = new byte[(int) (img.total() * img.channels())];
        //img.get(0, 0, buff);

        for (int rowi = start; rowi < stop; rowi++) {
            int index;
            if (dir == direction.vertical) {
                //pixel = img -> ptr(rowi, col);
                index = rowi * img.cols() * img.channels() + col * img.channels();
                //pixel = buff[rowi * img.cols() * img.channels() + col * img.channels()];
            } else { // horizontal
                //pixel = img -> ptr(col, rowi);
                index = col * img.cols() * img.channels() + rowi * img.channels();
                //pixel = buff[col * img.cols() * img.channels() + rowi * img.channels()];
            }

            byte b = img_buff[index];
            byte g = img_buff[index + 1];
            byte r = img_buff[index + 2];

            partline[i++] = (selector.selector(b, g, r));
            //partline[i++] = selector -> selector(b, g, r);
        }

        /** az értékek rendezése */
        //List<Integer> indices = IntStream.range(0, i).boxed().collect(Collectors.toList());
        Integer[] indices = new Integer[size];
        for (int j = 0; j < size; ++j) {
            indices[j] = j;
        }

        Arrays.sort(indices, new Comparator<Integer>() {
            public int compare(final Integer o1, final Integer o2) {
                if (!invert)
                    return Integer.compare(partline[o1], partline[o2]);
                else
                    return Integer.compare(partline[o2], partline[o1]);
            }
        });
        //indexes = sorter.sort(partline, 0, size);


        /** a rendezett pixelek átmásolása */
        /* copy values in a sorted way to a new array 3d */
        //auto * newarray = new uchar[size * 3];
        //Byte[] newarray = new Byte[size*3];
        ArrayList<Byte> newarray = new ArrayList<Byte>();
        int n_i = 0;
        for (int rowi = 0; rowi < size; rowi++) {
            int index;
            if (dir == direction.vertical) {
                index = (indices[rowi] + start) * img.cols() * img.channels() + col * img.channels();
                //pixel = img -> ptr(indexes[rowi] + start, col); // pointer a sor elejére-oszlop
            } else {
                index = (col) * img.cols() * img.channels() + (indices[rowi] + start) * img.channels();
                //pixel = img -> ptr(col, indexes[rowi] + start); // pointer a sor elejére
            }

            newarray.add(img_buff[index]);
            newarray.add(img_buff[index + 1]);
            newarray.add(img_buff[index + 2]);
            //newarray[n_i++] = *pixel++;
            //newarray[n_i++] = *pixel++;
            //newarray[n_i++] = *pixel;
        }

        /* copy back from new array to original image */
        /** a rendezett pixelek visszamásolása a képre */
        int start_i = start;
        for (int j = 0; j < size * 3; j += 3) {
            //uchar * rowptr;
            int index;
            if (dir == direction.vertical) {
                index = (start_i++) * img.cols() * img.channels() + col * img.channels();
                //rowptr = img -> ptr(start_i++, col);
            } else {
                index = (col) * img.cols() * img.channels() + start_i++ * img.channels();
                //rowptr = img -> ptr(col, start_i++);
            }

            img_buff[index] = newarray.get(j);
            img_buff[index + 1] = newarray.get(j + 1);
            img_buff[index + 2] = newarray.get(j + 2);
        }

        return img_buff;
    }
}
