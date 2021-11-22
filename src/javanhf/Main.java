package javanhf;

import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        OpenCV.loadLocally();
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        MyCanvas m=new MyCanvas();

        JFrame myframe = new MainFrame();
        myframe.setVisible(true);
        myframe.setSize(1920, 1080);

    }
}
