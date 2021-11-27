package javanhf;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.json.simple.JSONObject;

/**
 * Osztály az opciók mentéséhez és visszatöltéséhez
 */
public class Filter {
    /**
     * JSON formátumú szöveget generál egy HashMap alapján
     * @param options HashMap amiből a szöveget lehet generálni
     * @return generált JSON formátumú szöveg
     */
    private static String genJson(HashMap<String, String> options) {
        JSONObject jOb = new JSONObject();
        for (String key : options.keySet())
            jOb.put(key, options.get(key));
        return(jOb.toJSONString());
    }
    /**
     * Filter mentése
     * @param frame elmentendő JFrame
     * @param filename fájlnév amibe menteni kell
     * @throws IOException ha sikertelen a fájlmentés
     */
    public static void saveFilter(MainFrame frame, String filename) throws IOException {
        HashMap<String, String> options = new HashMap<>();
        PrintWriter pw = new PrintWriter(filename, StandardCharsets.ISO_8859_1);

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
        options.put("dir", dir);

        String sel = frame.sels[frame.selectorBox.getSelectedIndex()];
        options.put("selector", sel);

        options.put("invert", frame.invert.isSelected() ? "True" : "False");

        Mat canvasimg = frame.canvas.getImage();
        if (!canvasimg.empty())
            Imgcodecs.imwrite("src.png", canvasimg);
        else {
            File file = new File("src.png");
             if (file.delete()) {
                 System.out.println("Cannot delete src.png");
             }
        }
        Mat maskimg = frame.mask.getImage();
        if (!maskimg.empty())
            Imgcodecs.imwrite("mask.png", maskimg);
        else {
            File file = new File("mask.png");
            if (file.delete()) {
                System.out.println("Cannot delete mask.png");
            }
        }
        Mat resultimg = frame.result.getImage();
        if (!resultimg.empty())
            Imgcodecs.imwrite("result.png", resultimg);
        else {
            File file = new File("result.png");
            if (file.delete()) {
                System.out.println("Cannot delete result.png");
            }
        }
        Mat placeholderimg = frame.placeholder.getImage();
        if (!placeholderimg.empty())
            Imgcodecs.imwrite("placeholder.png", placeholderimg);
        else {
            File file = new File("placeholder.png");
            if (file.delete()) {
                System.out.println("Cannot delete placeholder.png");
            }
        }

        pw.printf(genJson(options));
        pw.close();
    }

    /**
     * Filter visszatöltése
     * @param frame visszatöltendő frame
     * @param filename fájlnév ahonnan az adatokat be kell tölteni
     * @throws IOException ha sikertelen a fájl beolvasás
     * @throws ParseException ha nem megfelelő formátumú a JSON file
     */
    public static void loadFilter(MainFrame frame, String filename) throws IOException, ParseException {

        Object objc = new JSONParser().parse(new FileReader(filename,StandardCharsets.ISO_8859_1));
        JSONObject jOb = (JSONObject) objc;

        switch ((String) jOb.get("dir")) {
            case "vertical" -> {
                frame.checkVert.setSelected(true);
                frame.checkHoriz.setSelected(false);
            }
            case "horizontal" -> {
                frame.checkHoriz.setSelected(true);
                frame.checkVert.setSelected(false);
            }
            case "diagonal" -> {
                frame.checkVert.setSelected(true);
                frame.checkHoriz.setSelected(true);
            }
        }
        for (int i = 0; i < frame.sels.length; ++i) {
            if (frame.sels[i].equals(jOb.get("selector")))
                frame.selectorBox.setSelectedIndex(i);
        }
        frame.invert.setSelected(jOb.get("invert").equals("True"));
        frame.genSorter();

        Mat src = Imgcodecs.imread("src.png");
        if (!src.empty()) {
            frame.canvas.setImage(src);
        }
        else {
            System.out.println("Cannot load src.png. Loading empty canvas.");
            frame.canvas.setImage(Mat.zeros(new Size(1920,1080), CvType.CV_8UC3));
        }

        Mat mask = Imgcodecs.imread("mask.png");
        if (!mask.empty()) {
            frame.mask.setImage(mask);
        }
        else {
            System.out.println("Cannot load mask.png. Loading empty canvas.");
            frame.mask.setImage(Mat.zeros(new Size(1920,1080), CvType.CV_8UC3));
        }

        Mat result = Imgcodecs.imread("result.png");
        if (!result.empty()) {
            frame.result.setImage(result);
        }
        else {
            System.out.println("Cannot load result.png. Loading empty canvas.");
            frame.result.setImage(Mat.zeros(new Size(1920,1080), CvType.CV_8UC3));
        }

        Mat placeholder = Imgcodecs.imread("placeholder.png");
        if (!placeholder.empty()) {
            frame.placeholder.setImage(placeholder);
        }
        else {
            System.out.println("Cannot load placeholder.png. Loading empty canvas.");
            frame.placeholder.setImage(Mat.zeros(new Size(1920,1080), CvType.CV_8UC3));
        }
    }
}
