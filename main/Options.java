package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

import static main.Main.*;
import static main.MapPanel.*;
import static java.awt.Color.lightGray;
import static java.awt.Color.orange;

public class Options extends JPanel {

    static JSlider scaleSlider;
    boolean up = false;

    public Options() {
        this.setPreferredSize(new Dimension(350, 500));
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setBackground(Color.YELLOW);
        this.setDoubleBuffered(true);
        this.setBorder(BorderFactory.createLineBorder(Color.YELLOW.darker().darker(), 5));
        this.setMinimumSize(new Dimension(0, 0));
        initializeOptionPanel();
    }

    void initializeOptionPanel() {
        JButton exit = new JButton("Exit");
        exit.setPreferredSize(new Dimension(300, 50));
        exit.setBackground(Color.ORANGE);
        exit.setFocusable(false);
        exit.setFocusPainted(false);
        exit.setVisible(true);
        exit.setFont(MV_Boli);
        exit.setForeground(Color.black);
        exit.setBorder(BorderFactory.createLineBorder(Color.YELLOW.darker().darker(), 5));
        exit.setOpaque(true);
        exit.addActionListener(e -> {
            int confirmExit = JOptionPane.showOptionDialog(
                    frame, "Are you sure you want to exit?", "Exit", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, 0);
            if (confirmExit == JOptionPane.YES_OPTION) System.exit(0);
        });

        JButton save = new JButton("Save Map");
        save.setPreferredSize(new Dimension(300, 50));
        save.setBackground(Color.ORANGE);
        save.setFocusable(false);
        save.setFocusPainted(false);
        save.setVisible(true);
        save.setFont(MV_Boli);
        save.setForeground(Color.black);
        save.setBorder(BorderFactory.createLineBorder(Color.YELLOW.darker().darker(), 5));
        save.setOpaque(true);
        save.addActionListener(actionEvent -> save(JOptionPane.showInputDialog(null, "What do you want your map to be named?", "SAVE MAP", JOptionPane.QUESTION_MESSAGE)));

        JButton load = new JButton("Load Map");
        load.setPreferredSize(new Dimension(300, 50));
        load.setBackground(Color.ORANGE);
        load.setFocusable(false);
        load.setFocusPainted(false);
        load.setVisible(true);
        load.setFont(MV_Boli);
        load.setForeground(Color.black);
        load.setBorder(BorderFactory.createLineBorder(Color.YELLOW.darker().darker(), 5));
        load.setOpaque(true);
        load.addActionListener(e -> {
            loadMap(JOptionPane.showInputDialog(null, "What is the name of the map you want to load?", "LOAD MAP", JOptionPane.QUESTION_MESSAGE));
        });

        JTextPane genMapTxt = new JTextPane();
        genMapTxt.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width/10*9, Toolkit.getDefaultToolkit().getScreenSize().height/10*9));
        genMapTxt.setBackground(lightGray);
        JScrollPane genMapScroll = new JScrollPane(genMapTxt);
        genMapScroll.getHorizontalScrollBar().setEnabled(true);
        genMapScroll.getHorizontalScrollBar().setVisible(true);
        genMapScroll.getVerticalScrollBar().setEnabled(true);

        JButton genMap = new JButton("Generate Map");
        genMap.setPreferredSize(new Dimension(300, 50));
        genMap.setBackground(orange);
        genMap.setFocusable(false);
        genMap.setFocusPainted(false);
        genMap.setVisible(true);
        genMap.setFont(MV_Boli);
        genMap.setForeground(Color.black);
        genMap.setBorder(BorderFactory.createLineBorder(Color.YELLOW.darker().darker(), 5));
        genMap.setOpaque(true);
        genMap.addActionListener(e -> {
            genMapTxt.setText(""+L1_mapStringLine+L2_mapStringLine+L3_mapStringLine+L4_mapStringLine);
            genMapTxt.selectAll();
            JOptionPane.showOptionDialog(null, genMapScroll,"Number Map", JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
        });

        JButton upload = new JButton("Upload Map");
        upload.setPreferredSize(new Dimension(300, 50));
        upload.setBackground(orange);
        upload.setFocusable(false);
        upload.setFocusPainted(false);
        upload.setVisible(true);
        upload.setFont(MV_Boli);
        upload.setForeground(Color.black);
        upload.setBorder(BorderFactory.createLineBorder(Color.YELLOW.darker().darker(), 5));
        upload.setOpaque(true);
        upload.addActionListener(e -> {
            up = true;
            loadMap(JOptionPane.showInputDialog(frame, "Enter your saved number map (obtained from Generate Map)", "Enter Number Map", JOptionPane.PLAIN_MESSAGE));
        });

        scaleSlider = new JSlider(0, 150);
        scaleSlider.setValue(scaleSlider.getMaximum()/2);
        scaleSlider.setPreferredSize(new Dimension(300, 50));
        scaleSlider.setPaintTrack(true);
        scaleSlider.setPaintTicks(true);
        scaleSlider.setPaintLabels(true);
        scaleSlider.setMajorTickSpacing(50);
        scaleSlider.setMinorTickSpacing(25);
        scaleSlider.setBackground(Color.ORANGE);
        scaleSlider.setBorder(BorderFactory.createLineBorder(Color.YELLOW.darker().darker(), 5));
        scaleSlider.setForeground(Color.black);
        scaleSlider.addChangeListener(changeEvent -> {
            tileScale = scaleSlider.getValue();
            if (scaleSlider.getValue() < 1) tileScale = 1;
            frame.repaint();
            mapScroll.getVerticalScrollBar().setUnitIncrement(tileScale/3);
            mapScroll.getHorizontalScrollBar().setUnitIncrement(tileScale/3);
        });

        JButton controls = new JButton("Show Controls");
        controls.setPreferredSize(new Dimension(300, 50));
        controls.setBackground(orange);
        controls.setFocusable(false);
        controls.setFocusPainted(false);
        controls.setVisible(true);
        controls.setFont(MV_Boli);
        controls.setForeground(Color.black);
        controls.setBorder(BorderFactory.createLineBorder(Color.YELLOW.darker().darker(), 5));
        controls.setOpaque(true);
        controls.addActionListener(e -> {
            try {
                JOptionPane.showOptionDialog(null, new ImageIcon(ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("Controls.png"))).getScaledInstance(frameSize.width/10*7, frameSize.height/10*7, BufferedImage.SCALE_SMOOTH)),"Controls", JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE, null, new Object[]{}, null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        save.setEnabled(false);
        load.setEnabled(false);

        this.add(exit);
        this.add(save);
        this.add(load);
        this.add(genMap);
        this.add(upload);
        this.add(controls);
        this.add(scaleSlider);
    }

    void save(String mapName){
        if (mapName != null) {
            final Path path = Paths.get("src\\res\\maps\\"+mapName);
            try (final BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
                writer.write(""+L1_mapStringLine+L2_mapStringLine+L3_mapStringLine+L4_mapStringLine);
                writer.flush();
            } catch (IOException ignored) {}
        }
    }

    void loadMap(String mapName) {
        File path = new File("src\\res\\maps\\"+mapName);
        if (mapName != null && path.exists() || mapName != null && up) {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(""))));
            String line;
            String[] lines = null;

            if (!up) bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(mapName))));
            else lines = mapName.split(" {2}"); // two spaces

            try {
                // reads loaded map and transfers contents into a temporary string
                for (int positionY = 0; positionY < maxMapTilesY*4; positionY++) {  // max Y * total layers
                    line = up ? lines[positionY] : bufferedReader.readLine(); // assigns the Y value of the map to load
                    // layer one
                    if (positionY < maxMapTilesY) for (int positionX = 0; positionX < maxMapTilesX; positionX++)
                        L1_mapTileCoordinate[positionX][positionY] = Integer.parseInt(line.split(" ")[positionX]);  // mapTileCoordinate[x][y] = number that line read
                    // layer two
                    if (positionY > maxMapTilesY - 1 && positionY < maxMapTilesY * 2) for (int positionX = 0; positionX < maxMapTilesX; positionX++)
                        L2_mapTileCoordinate[positionX][positionY - maxMapTilesY] = Integer.parseInt(line.split(" ")[positionX]);
                    // layer three
                    if (positionY > maxMapTilesY * 2 - 1 && positionY < maxMapTilesY * 3) for (int positionX = 0; positionX < maxMapTilesX; positionX++)
                        L3_mapTileCoordinate[positionX][positionY - maxMapTilesY * 2] = Integer.parseInt(line.split(" ")[positionX]);
                    // layer four
                    if (positionY > maxMapTilesY * 3 - 1 && positionY < maxMapTilesY * 4) for (int positionX = 0; positionX < maxMapTilesX; positionX++)
                        L4_mapTileCoordinate[positionX][positionY - maxMapTilesY * 3] = Integer.parseInt(line.split(" ")[positionX]);
                }
                bufferedReader.close();
                // takes string from previous step and moves it to a new string
                L1_mapStringLine = new StringBuilder();
                for (int i = 0; i < maxMapTilesY; i++) {
                    for (int ii = 0; ii < maxMapTilesX; ii++)
                        L1_mapStringLine.append(L1_mapTileCoordinate[ii][i]).append(" ");
                    L1_mapStringLine.append("\n");
                }
                L2_mapStringLine = new StringBuilder();
                for (int i = 0; i < maxMapTilesY; i++) {
                    for (int ii = 0; ii < maxMapTilesX; ii++)
                        L2_mapStringLine.append(L2_mapTileCoordinate[ii][i]).append(" ");
                    L2_mapStringLine.append("\n");
                }
                L3_mapStringLine = new StringBuilder();
                for (int i = 0; i < maxMapTilesY; i++) {
                    for (int ii = 0; ii < maxMapTilesX; ii++)
                        L3_mapStringLine.append(L3_mapTileCoordinate[ii][i]).append(" ");
                    L3_mapStringLine.append("\n");
                }
                L4_mapStringLine = new StringBuilder();
                for (int i = 0; i < maxMapTilesY; i++) {
                    for (int ii = 0; ii < maxMapTilesX; ii++)
                        L4_mapStringLine.append(L4_mapTileCoordinate[ii][i]).append(" ");
                    L4_mapStringLine.append("\n");
                }
                up = false;
            } catch (IOException ignored) {}
            if (up) up = false;
            frame.repaint();
        }
    }
    //  an un concise version of creating LX_mapTileCoordinate
//     if (positionY < maxMapTilesY) for (int positionX = 0; positionX < maxMapTilesX; positionX++) {
//         numbers = line.split(" ");  // divides line up between spaces
//         num = Integer.parseInt(numbers[positionX]); // turns character into an integer
//         L1_mapTileCoordinate[positionX][positionY] = num;  // mapTileCoordinate[x][y] = number that line read
//     }
}