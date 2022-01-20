package main;

import main.tiles.TileBar;
import main.tiles.TileList;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class Main {

    static Font MV_Boli = new Font("Rockwell", Font.BOLD, 20);
    static JFrame frame;
    static JScrollPane mapScroll;
    public static JScrollPane barScroll;
    static TileBar tileBar;
    static final Dimension frameSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static void main(String[] args) throws Exception {
        frame = new JFrame();
        frame.setLayout(new GridLayout());
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setPreferredSize(new Dimension((int) frameSize.getWidth(), (int) frameSize.getHeight()));
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                int remember = JOptionPane.showConfirmDialog(null, "HAVE YOU SAVED YET?!?!?!?!", "REMEMBER TO SAVE!!!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (remember == JOptionPane.YES_OPTION) System.exit(0);
            }
        });

        MapPanel mapPanel = new MapPanel();
        Options options = new Options();
        TileList tileList = new TileList();

        tileBar = new TileBar();
        barScroll = new JScrollPane(tileBar);
        barScroll.setWheelScrollingEnabled(true);
        barScroll.getVerticalScrollBar().setUnitIncrement(12);
        barScroll.getHorizontalScrollBar().setEnabled(true);
        tileBar.add_EVERYTHING();

        mapScroll = new JScrollPane(mapPanel);
        mapScroll.setPreferredSize(new Dimension(0,  frameSize.height/5*4));  // 973

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, mapScroll, new Layers()), new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new TileBarSettings(), barScroll));
        split.setDividerLocation((int) frameSize.getWidth() / 4 * 3);
        split.getRightComponent().setEnabled(false);
        frame.add(split);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == 27) {   // ESCAPE
                    int confirmExit = JOptionPane.showOptionDialog(frame, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, 0);
                    if (confirmExit == JOptionPane.YES_OPTION) System.exit(0);
                }
                if (e.isShiftDown() && e.isControlDown())
                    mapPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (!e.isShiftDown() || !e.isControlDown())
                    mapPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        frame.pack();
        frame.setFocusable(true);
        frame.requestFocus();
        frame.setVisible(true);
        frame.setIconImage(ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("Tile ID = (60).png"))));

    }
}