package javanhf;

import nu.pattern.OpenCV;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        OpenCV.loadLocally();

        JFrame myframe = new MainFrame();
        myframe.setVisible(true);
        myframe.setSize(1920, 1080);

    }
}
