package javanhf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


import nu.pattern.OpenCV;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MainFrame extends JFrame {
    MyCanvas canvas = new MyCanvas();
    MyCanvas mask = new MyCanvas();
    MyCanvas placeholder = new MyCanvas();
    MyCanvas result = new MyCanvas();
    JPanel panelCanvas;
    JPanel panelMask;
    JPanel panelPlaceholder;
    JPanel panelResult;

    Mat src = new Mat();
    JFileChooser fc = new JFileChooser();
    JCheckBox checkHoriz;
    JCheckBox checkVert;
    JCheckBox invert;
    String[] sels = {"Intensity", "Hue", "Saturation", "Lightness", "Blue", "Green", "Red"};
    JComboBox selectorBox;
    JButton sortbutton;
    boolean mousepressed = false;
    Pixelsort ps;

    public MainFrame() {
        super("JavaNHF");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.black);
        fc.setBackground(Color.black);
        fc.setForeground(Color.white);


        ActionListener al = new MyActionListener();
        //b.addActionListener(al);
        ArrayList<JMenuItem> menuitems = new ArrayList<>();

        JMenuItem mi1 = new JMenuItem("Add Noise");
        mi1.setActionCommand("addnoise"); // action command beállítása
        menuitems.add(mi1);

        JMenuItem mi2 = new JMenuItem("Load Image");
        mi2.setActionCommand("loadimage"); // action command beállítása
        menuitems.add(mi2);

        JMenuItem mi3 = new JMenuItem("Load Mask");
        mi3.setActionCommand("loadmask"); // action command beállítása
        menuitems.add(mi3);

        JMenuItem mi4 = new JMenuItem("Load Empty Mask");
        mi4.setActionCommand("loadempty"); // action command beállítása
        menuitems.add(mi4);

        JMenuItem mi9 = new JMenuItem("Load Full Mask");
        mi9.setActionCommand("loadfull"); // action command beállítása
        menuitems.add(mi9);

        JMenuItem mi5 = new JMenuItem("Set Placeholder to Source");
        mi5.setActionCommand("tosrc"); // action command beállítása
        menuitems.add(mi5);

        JMenuItem mi8 = new JMenuItem("Set Result to Placeholder");
        mi8.setActionCommand("toplaceholder"); // action command beállítása
        menuitems.add(mi8);

        JMenuItem mi6 = new JMenuItem("Sort");
        mi6.setActionCommand("sort"); // action command beállítása
        menuitems.add(mi6);

        JMenuItem mi7 = new JMenuItem("Save Result");
        mi7.setActionCommand("save"); // action command beállítása
        menuitems.add(mi7);

        JMenu m1 = new JMenu("Test");
        m1.setForeground(Color.white);
        m1.setBackground(Color.black);

        for (JMenuItem i : menuitems) {
            i.addActionListener(al);    // listener hozzáadása
            i.setForeground(Color.white);
            i.setBackground(Color.black);
            m1.add(i);
        }

        JMenuBar bar = new JMenuBar();
        bar.setBackground(Color.black);
        bar.add(m1);

        checkHoriz = new JCheckBox("Horizontal");
        checkVert = new JCheckBox("Vertical");
        checkHoriz.setBackground(Color.black);
        checkHoriz.setForeground(Color.white);
        checkVert.setBackground(Color.black);
        checkVert.setForeground(Color.white);
        bar.add(checkHoriz);
        bar.add(checkVert);

        invert = new JCheckBox("Invert");
        invert.setBackground(Color.black);
        invert.setForeground(Color.white);
        bar.add(invert);

        selectorBox = new JComboBox(sels);
        selectorBox.setBackground(Color.black);
        selectorBox.setForeground(Color.white);
        selectorBox.setBounds(50, 50, 90, 20);
        bar.add(selectorBox);

        sortbutton = new JButton("Sort");
        sortbutton.setActionCommand("sort");
        sortbutton.addActionListener(al);
        sortbutton.setBackground(Color.black);
        sortbutton.setForeground(Color.white);
        bar.add(sortbutton);

        setJMenuBar(bar);


        GridLayout lm = new GridLayout();
        lm.setRows(2);
        lm.setColumns(2);
        this.setLayout(lm);

        ArrayList<JPanel> panels = new ArrayList<>();

        panelCanvas = new JPanel(new GridLayout());
        panelCanvas.add(canvas);
        panelCanvas.addMouseListener(new ButtonClickListener());
        panels.add(panelCanvas);

        panelMask = new JPanel(new GridLayout());
        panelMask.add(mask);
        panelMask.addMouseListener(new ButtonClickListener());
        panels.add(panelMask);

        panelPlaceholder = new JPanel(new GridLayout());
        panelPlaceholder.add(placeholder);
        panelPlaceholder.addMouseListener(new ButtonClickListener());
        panels.add(panelPlaceholder);

        panelResult = new JPanel(new GridLayout());
        panelResult.add(result);
        panelResult.addMouseListener(new ButtonClickListener());
        panels.add(panelResult);

        int color = 60;
        for (JPanel p : panels) {
            p.setBackground(new Color(color, color, color));
            color -= 15;
            add(p);
        }

        mask.addMouseMotionListener(new MouseMove());
        mask.addMouseListener(new ButtonClickListener());

        try {
            Filter.loadFilter(this, "frame.dat");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setResizable(true);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Filter.saveFilter((MainFrame) e.getSource(), "frame.dat");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


    }

    private class MouseMove implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (mousepressed) {
                int mousex = (MouseInfo.getPointerInfo().getLocation().x - mask.getLocationOnScreen().x);
                int mousey = (MouseInfo.getPointerInfo().getLocation().y - mask.getLocationOnScreen().y);
                //System.out.println(canvas.getWidth() + " " + canvas.getHeight());
                //System.out.println((MouseInfo.getPointerInfo().getLocation().x-canvas.getLocationOnScreen().x) + " " + (MouseInfo.getPointerInfo().getLocation().y-canvas.getLocationOnScreen().y));
                //System.out.println(((double) MouseInfo.getPointerInfo().getLocation().y-canvas.getLocationOnScreen().y)/canvas.getHeight());
                int x = (int) (((double) mousex) / mask.getWidth() * mask.getImage().cols());
                int y = (int) (((double) mousey) / mask.getHeight() * mask.getImage().rows());
                Mat img = mask.getImage();
                //img.put(min(min(0,x), img.cols()-1),min(min(0,y), img.rows()-1), new byte[] {0,0,0});
                System.out.println(img.rows() + " " + img.cols() + " " + img.channels());
                System.out.println(x + " " + y);
                for (int i = max(0, x); i < min(x + 30, img.cols() - 1); ++i) {
                    for (int j = max(0, y); j < min(y + 30*(mask.getWidth()/mask.getHeight()), img.rows() - 1); ++j) {
                        img.put(j, i, new byte[]{(byte) 255, (byte) 255, (byte) 255});
                    }
                }
                mask.setImage(img);
                mask.repaint(mousex, mousey, 30 * mask.getWidth() / mask.getImage().cols(), 30 * mask.getHeight() / mask.getImage().rows());
            }
        }
    }

    private class MyActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent ae) {

            String cmd = ae.getActionCommand();
            System.out.println(cmd);
            if (cmd.equals("addnoise")) {
                Mat dst = ImageTools.addNoise(canvas.getImage());
                canvas.setImage(dst);
                canvas.repaint();
            } else if (cmd.equals("loadimage")) {

                int returnVal = fc.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    canvas.setImage(file.getAbsolutePath());
                    canvas.repaint();
                }
            } else if (cmd.equals("loadmask")) {
                int returnVal = fc.showOpenDialog(null);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    //mask.setImage(file.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
                    src = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_COLOR);
                    mask.setImage(src);
                    mask.repaint();
                }
            } else if (cmd.equals("loadempty")) {
                Mat src = Mat.zeros(canvas.getImageSize(), canvas.getImageType());
                src.setTo(new Scalar(0, 0, 0));
                mask.setImage(src);
                mask.repaint();

            } else if (cmd.equals("loadfull")) {
            Mat src = Mat.zeros(canvas.getImageSize(), canvas.getImageType());
            src.setTo(new Scalar(255, 255, 255));
            mask.setImage(src);
            mask.repaint();

        }
            else if (cmd.equals("tosrc")) {
                Mat src = placeholder.getImage().clone();
                canvas.setImage(src);
                canvas.setImage_path(placeholder.getImage_path());
                canvas.repaint();

            } else if (cmd.equals("toplaceholder")) {
                Mat src = result.getImage().clone();
                placeholder.setImage(src);
                placeholder.setImage_path(result.getImage_path());
                placeholder.repaint();

            } else if (cmd.equals("sort")) {
                Mat src = canvas.getImage().clone();
                ps = new Pixelsort();
                if (invert.isSelected())
                    ps.setInvert(true);
                switch (selectorBox.getSelectedIndex()) {
                    case 0:
                        ps.setSelector(new IntensitySelector());
                        break;
                    case 1:
                        ps.setSelector(new HSLSelector(HSLSelector.HSLChannels.Hue));
                        break;
                    case 2:
                        ps.setSelector(new HSLSelector(HSLSelector.HSLChannels.Saturation));
                        break;
                    case 3:
                        ps.setSelector(new HSLSelector(HSLSelector.HSLChannels.Lightness));
                        break;
                    case 4:
                        ps.setSelector(new BGRSelector(BGRSelector.BGRvals.Blue));
                        break;
                    case 5:
                        ps.setSelector(new BGRSelector(BGRSelector.BGRvals.Green));
                        break;
                    case 6:
                        ps.setSelector(new BGRSelector(BGRSelector.BGRvals.Red));
                        break;
                    default:
                        ps.setSelector(new IntensitySelector());
                        break;

                }
                Pixelsort.direction dir;
                if (checkHoriz.isSelected())
                    src = ps.sort(src, mask.getImage(), Pixelsort.direction.horizontal);
                if (checkVert.isSelected())
                    src = ps.sort(src, mask.getImage(), Pixelsort.direction.vertical);
                //src = ps.sort(src, mask.getImage(), dir);
                result.setImage(src);
                result.repaint();

            } else if (cmd.equals("save")) {
                int returnVal = fc.showSaveDialog(null);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    Imgcodecs.imwrite(file.getAbsolutePath(), result.getImage());
                    result.setImage_path(file.getAbsolutePath());
                }
                placeholder.setImage(result.getImage().clone());
                placeholder.setImage_path(result.getImage_path());
                placeholder.repaint();

            }
            System.out.println("actionperformed");
        }
    }

    private class ButtonClickListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            mousepressed = !mousepressed;
            if (!mousepressed) {
                Mat img = result.getImage();
                if (checkHoriz.isSelected())
                    src = ps.sort(img, mask.getImage(), Pixelsort.direction.horizontal);
                if (checkVert.isSelected())
                    src = ps.sort(img, mask.getImage(), Pixelsort.direction.vertical);

                result.setImage(img);
                result.repaint();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
