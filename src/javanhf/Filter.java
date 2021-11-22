package javanhf;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.io.*;
import java.nio.charset.Charset;

public class Filter {
    public static void saveFilter(MainFrame frame, String filename) throws IOException {
        PrintWriter pw = new PrintWriter(filename, "ISO-8859-1");

        String dir = "none";
        if (frame.checkVert.isSelected()) {
            dir = "vertical";
        }
        if (frame.checkHoriz.isSelected()) {
            if (dir.equals("none"))
                dir = "horizontal";
            else
                dir = "diagonal";
        }

        String sel = frame.sels[frame.selectorBox.getSelectedIndex()];
        Mat canvasimg = frame.canvas.getImage();
        if (!canvasimg.empty())
            Imgcodecs.imwrite("src.png", canvasimg);
        else {
            File file = new File("src.png");
            file.delete();
        }
        Mat maskimg = frame.mask.getImage();
        if (!maskimg.empty())
            Imgcodecs.imwrite("mask.png", maskimg);
        else {
            File file = new File("mask.png");
            file.delete();
        }
        Mat resultimg = frame.result.getImage();
        if (!resultimg.empty())
            Imgcodecs.imwrite("result.png", resultimg);
        else {
            File file = new File("result.png");
            file.delete();
        }
        Mat placeholderimg = frame.placeholder.getImage();
        if (!placeholderimg.empty())
            Imgcodecs.imwrite("placeholder.png", placeholderimg);
        else {
            File file = new File("placeholder.png");
            file.delete();
        }

        String src_path = frame.canvas.getImage_path();
        String mask_path = frame.mask.getImage_path();
        String result_path = frame.result.getImage_path();

        pw.printf("%s\n%s\n%s\n%s\n%s", dir, sel, src_path, mask_path, result_path);
        pw.close();
    }
    public static void loadFilter(MainFrame frame, String filename) throws IOException {

        FileReader fr = new FileReader(filename, Charset.forName("ISO-8859-1"));
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        if (line.equals("vertical")) {
            frame.checkVert.setSelected(true);
        }
        else if (line.equals("horizontal")) {
            frame.checkHoriz.setSelected(true);
        }
        else if (line.equals("diagonal")) {
            frame.checkVert.setSelected(true);
            frame.checkHoriz.setSelected(true);
        }
        line = br.readLine();
        for (int i = 0; i < frame.sels.length; ++i) {
            if (frame.sels[i].equals(line))
                frame.selectorBox.setSelectedIndex(i);
        }

        try {
            frame.canvas.setImage(Imgcodecs.imread("src.png"));
        }
        catch (Exception e) {e.printStackTrace();}
        try {
            frame.mask.setImage(Imgcodecs.imread("mask.png"));
        }
        catch (Exception e) {e.printStackTrace();}
        try {
            frame.result.setImage(Imgcodecs.imread("result.png"));
        }
        catch (Exception e) {e.printStackTrace();}
        try {
            frame.placeholder.setImage(Imgcodecs.imread("placeholder.png"));
        }
        catch (Exception e) {e.printStackTrace();}
    }
}
