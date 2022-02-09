package main;

import main.tiles.TileBar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class Main {

    static Font rockwell = new Font("Rockwell", Font.BOLD, 20);
    static JScrollPane mapScroll;
    public static JScrollPane barScroll;
    static MapPanel mapPanel;

    static StringBuilder emptyLayer = new StringBuilder();

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame();
        frame.setLayout(new GridLayout());
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                if (JOptionPane.showOptionDialog(frame, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, 0) == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        });

        mapPanel = new MapPanel();

        TileBar tileBar = new TileBar(mapPanel.tileScale);
        barScroll = new JScrollPane(tileBar);
        barScroll.setWheelScrollingEnabled(true);
        barScroll.getVerticalScrollBar().setUnitIncrement(12);
        barScroll.getHorizontalScrollBar().setEnabled(true);
        tileBar.add_EVERYTHING();

        mapScroll = new JScrollPane(mapPanel);
        mapScroll.setPreferredSize(new Dimension(0,  Toolkit.getDefaultToolkit().getScreenSize().height/5*4));  // 973

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, mapScroll, new Layers()), new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JSplitPane(JSplitPane.VERTICAL_SPLIT, new TileBarSettings(tileBar), new Tools()), barScroll));
        split.setDividerLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 4 * 3);
        split.getRightComponent().setEnabled(false);
        frame.add(split);

        frame.addKeyListener(new KeyInput());
        frame.pack();
        frame.setFocusable(true);
        frame.requestFocus();
        frame.setVisible(true);
        frame.setIconImage(ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("Tile ID = (60).png"))));

    }
}