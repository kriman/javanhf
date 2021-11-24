package javanhf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;


import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class MainFrame extends JFrame {
    DrawCanvas canvas = new DrawCanvas();
    DrawCanvas mask = new DrawCanvas();
    DrawCanvas placeholder = new DrawCanvas();
    DrawCanvas result = new DrawCanvas();
    JPanel panelCanvas;
    JPanel panelMask;
    JPanel panelPlaceholder;
    JPanel panelResult;

    JFileChooser fc = new JFileChooser(); /** Fájlválasztás */
    JCheckBox checkHoriz; /** Vízszintes rendezés */
    JCheckBox checkVert; /** Függőleges rendezés */
    JCheckBox invert; /** Rendezés iránya (növekvő, csökkenő) */
    String[] sels = {"Intensity", "Hue", "Saturation", "Lightness", "Blue", "Green", "Red"}; /** Rendezési szempontok */
    JComboBox selectorBox; /** Rendezési szempontok kiválasztása */
    JButton sortbutton; /** Rendezés indítása */
    boolean mousepressed = false;
    Pixelsort ps; /** Pixelsorter */

    public MainFrame() {
        super("JavaNHF");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.black);
        fc.setBackground(Color.black);
        fc.setForeground(Color.white);


        ActionListener al = new MyActionListener();
        ArrayList<JMenuItem> menuitems = new ArrayList<>();

        JMenuItem mi1 = new JMenuItem("Add Noise");
        mi1.setActionCommand("addnoise");
        menuitems.add(mi1);

        JMenuItem mi2 = new JMenuItem("Load Image");
        mi2.setActionCommand("loadimage");
        menuitems.add(mi2);

        JMenuItem mi3 = new JMenuItem("Load Mask");
        mi3.setActionCommand("loadmask");
        menuitems.add(mi3);

        JMenuItem mi4 = new JMenuItem("Load Empty Mask");
        mi4.setActionCommand("loadempty");
        menuitems.add(mi4);

        JMenuItem mi9 = new JMenuItem("Load Full Mask");
        mi9.setActionCommand("loadfull");
        menuitems.add(mi9);

        JMenuItem mi5 = new JMenuItem("Set Placeholder to Source");
        mi5.setActionCommand("tosrc");
        menuitems.add(mi5);

        JMenuItem mi8 = new JMenuItem("Set Result to Placeholder");
        mi8.setActionCommand("toplaceholder");
        menuitems.add(mi8);

        JMenuItem mi6 = new JMenuItem("Sort");
        mi6.setActionCommand("sort");
        menuitems.add(mi6);

        JMenuItem mi7 = new JMenuItem("Save Result");
        mi7.setActionCommand("save");
        menuitems.add(mi7);

        JMenu m1 = new JMenu("Actions");
        m1.setForeground(Color.white);
        m1.setBackground(Color.black);

        for (JMenuItem i : menuitems) {
            i.addActionListener(al);
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

    private void fillCanvas(DrawCanvas ref, DrawCanvas dst, Scalar color) {
        Mat src = Mat.zeros(ref.getImageSize(), ref.getImageType());
        src.setTo(color);
        dst.setImage(src);
        dst.repaint();
    }

    private void moveCanvas(DrawCanvas src, DrawCanvas dst) {
        Mat img = src.getImage().clone();
        dst.setImage(img);
        dst.setImage_path(src.getImage_path());
        dst.repaint();
    }

    private void loadImage(DrawCanvas canvas) {
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            canvas.setImage(file.getAbsolutePath());
            canvas.repaint();
        }
    }

    public void genSorter() {
        ps = new Pixelsort();
        if (invert.isSelected())
            ps.setInvert(true);
        switch (selectorBox.getSelectedIndex()) {
            case 0 -> ps.setSelector(new IntensitySelector());
            case 1 -> ps.setSelector(new HSLSelector(HSLSelector.HSLChannels.Hue));
            case 2 -> ps.setSelector(new HSLSelector(HSLSelector.HSLChannels.Saturation));
            case 3 -> ps.setSelector(new HSLSelector(HSLSelector.HSLChannels.Lightness));
            case 4 -> ps.setSelector(new BGRSelector(BGRSelector.BGRvals.Blue));
            case 5 -> ps.setSelector(new BGRSelector(BGRSelector.BGRvals.Green));
            case 6 -> ps.setSelector(new BGRSelector(BGRSelector.BGRvals.Red));
            default -> ps.setSelector(new IntensitySelector());
        }
    }
    private Mat performSort(Mat img, Pixelsort.direction dir) {
        Mat maskimg = mask.getImage();
        try {
            img = ps.sort(img, maskimg, dir);
        }
        catch (NotSameSizeException em) {
            JOptionPane opt = new JOptionPane("Cannot sort image (" + img.size() + ") with mask (" + maskimg.size() + ")",
                    JOptionPane.ERROR_MESSAGE);
            JDialog jd = opt.createDialog(new MainFrame(), "Error: image and mask are not the same size");
            jd.setLocationRelativeTo(this);
            jd.setVisible(true);
        }
        return img;
    }

    private class MouseMove implements MouseMotionListener {

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {
            if (mousepressed) {
                int mousex = (MouseInfo.getPointerInfo().getLocation().x - mask.getLocationOnScreen().x);
                int mousey = (MouseInfo.getPointerInfo().getLocation().y - mask.getLocationOnScreen().y);
                int x = (int) (((double) mousex) / mask.getWidth() * mask.getImage().cols());
                int y = (int) (((double) mousey) / mask.getHeight() * mask.getImage().rows());

                Mat img = mask.getImage();
                System.out.println(img.rows() + " " + img.cols() + " " + img.channels());
                System.out.println(x + " " + y);

                for (int i = max(0, x); i < min(x + 30, img.cols() - 1); ++i) {
                    for (int j = max(0, y); j < min(y + 30 * (mask.getWidth() / mask.getHeight()), img.rows() - 1); ++j) {
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

            switch (cmd) {
                case "addnoise" -> {
                    Mat dst = ImageTools.addNoise(canvas.getImage());
                    canvas.setImage(dst);
                    canvas.repaint();
                }
                case "loadimage" -> loadImage(canvas);
                case "loadmask" -> loadImage(mask);
                case "loadempty" -> fillCanvas(canvas, mask, new Scalar(0, 0, 0));
                case "loadfull" -> fillCanvas(canvas, mask, new Scalar(255, 255, 255));
                case "tosrc" -> moveCanvas(placeholder, canvas);
                case "toplaceholder" -> moveCanvas(result, placeholder);
                case "sort" -> {
                    Mat img = canvas.getImage().clone();
                    genSorter();
                    if (checkHoriz.isSelected())
                        img = performSort(img, Pixelsort.direction.horizontal);
                    if (checkVert.isSelected())
                        img = performSort(img, Pixelsort.direction.vertical);
                    result.setImage(img);
                    result.repaint();
                }
                case "save" -> {
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
            }
        }
    }

    private class ButtonClickListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            mousepressed = !mousepressed;
            if (!mousepressed) {
                Mat img = result.getImage();
                if (checkHoriz.isSelected())
                    img = performSort(img, Pixelsort.direction.horizontal);
                if (checkVert.isSelected())
                    img = performSort(img, Pixelsort.direction.vertical);

                result.setImage(img);
                result.repaint();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }
}
