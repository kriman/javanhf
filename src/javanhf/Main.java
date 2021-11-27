package javanhf;

import nu.pattern.OpenCV;

import javax.swing.*;

/**
 * A program fő osztálya
 */
public class Main {
    /**
     * A program indítása
     * @param args command line arguments
     */
    public static void main(String[] args) {
        OpenCV.loadLocally();

        JFrame myframe = new MainFrame();
        myframe.setVisible(true);
        myframe.setSize(1920, 1080);

    }
}
