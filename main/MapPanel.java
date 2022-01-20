package main;

import static main.Layers.*;
import static main.Main.*;

import static main.Options.*;
import static main.TileBarSettings.*;
import static main.tiles.TileBar.tileValue;
import static main.tiles.TileList.tileID;
import static java.awt.Color.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MapPanel extends JPanel {

    public static int tileScale = 100;
    static StringBuilder L1_mapStringLine;
    static StringBuilder L2_mapStringLine;
    static StringBuilder L3_mapStringLine;
    static StringBuilder L4_mapStringLine;

    // map dimensions // can be adjusted
    static int maxMapTilesX = 100;
    static int maxMapTilesY = 100;

    static int[][] L1_mapTileCoordinate = new int[maxMapTilesX][maxMapTilesY];
    static int[][] L2_mapTileCoordinate = new int[maxMapTilesX][maxMapTilesY];
    static int[][] L3_mapTileCoordinate = new int[maxMapTilesX][maxMapTilesY];
    static int[][] L4_mapTileCoordinate = new int[maxMapTilesX][maxMapTilesY];

    static int tileLocationX = -1;
    static int tileLocationY = -1;

    boolean canBeDragged;
    boolean scrollPressed;

    BufferedImage COLLISION = ImageIO.read(ClassLoader.getSystemResourceAsStream("Collision.png"));

    public MapPanel() throws IOException {
        this.setPreferredSize(new Dimension(maxMapTilesX * tileScale, maxMapTilesY * tileScale));
        this.setBackground(DARK_GRAY);
        addListener();
    }

    int pressedX, pressedY;
    void addListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                    canBeDragged = true;
                    if (mouseEvent.isControlDown() || mouseEvent.isShiftDown()) {
                        tileLocationX = -1;
                        tileLocationY = -1;
                    } else {
                        tileLocationX = mouseEvent.getX() / tileScale;
                        tileLocationY = mouseEvent.getY() / tileScale;
                        repaint();
                    }
                    pressedX = mouseEvent.getX();
                    pressedY = mouseEvent.getY();
                } else if
                (mouseEvent.getButton() == MouseEvent.BUTTON2) {
                    scrollPressed = true;
                    pressedX = mouseEvent.getX();
                    pressedY = mouseEvent.getY();
                }
            }
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
                    canBeDragged = false;
                    tileLocationX = -1;
                    tileLocationY = -1;
                } else if
                (mouseEvent.getButton() == MouseEvent.BUTTON2) {
                    scrollPressed = false;
                    tileLocationX = -1;
                    tileLocationY = -1;
                }
            }
        });
        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                if (canBeDragged || scrollPressed) {
                    if (mouseEvent.isControlDown() && mouseEvent.isShiftDown() || scrollPressed) {
                        if (mouseEvent.getX() > pressedX)
                            mapScroll.getHorizontalScrollBar().setValue(mapScroll.getHorizontalScrollBar().getValue() - Math.abs(pressedX - mouseEvent.getX()));    // left
                        if (mouseEvent.getX() < pressedX)
                            mapScroll.getHorizontalScrollBar().setValue(mapScroll.getHorizontalScrollBar().getValue() + Math.abs(pressedX - mouseEvent.getX()));    // right
                        if (mouseEvent.getY() > pressedY)
                            mapScroll.getVerticalScrollBar().setValue(mapScroll.getVerticalScrollBar().getValue() - Math.abs(pressedY - mouseEvent.getY()));    // up
                        if (mouseEvent.getY() < pressedY)
                            mapScroll.getVerticalScrollBar().setValue(mapScroll.getVerticalScrollBar().getValue() + Math.abs(pressedY - mouseEvent.getY()));    // down
                    } else {
                        if (mouseEvent.isControlDown() || mouseEvent.isShiftDown()) {
                            tileLocationX = -1;
                            tileLocationY = -1;
                        } else {
                            tileLocationX = mouseEvent.getX() / tileScale;
                            tileLocationY = mouseEvent.getY() / tileScale;
                            repaint();
                        }
                    }
                }
            }
        });
        this.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
                int scrollingDirection = e.getWheelRotation();
                // if scrolling towards
                if (scrollingDirection == 1) {
                    if (e.isControlDown()) {
                        scaleSlider.setValue(scaleSlider.getValue() - 5);
                        repaint();
                    } else {
                        if (!e.isShiftDown())
                            mapScroll.getVerticalScrollBar().setValue(mapScroll.getVerticalScrollBar().getValue() + tileScale / 2);    // right
                        else
                            mapScroll.getHorizontalScrollBar().setValue(mapScroll.getHorizontalScrollBar().getValue() + tileScale / 2);    // down
                    }
                }
                // if scrolling away
                if (scrollingDirection == -1) {
                    if (e.isControlDown()) {
                        scaleSlider.setValue(scaleSlider.getValue() + 5);
                        repaint();
                    } else {
                        if (!e.isShiftDown())
                            mapScroll.getVerticalScrollBar().setValue(mapScroll.getVerticalScrollBar().getValue() - tileScale / 2);   // left
                        else
                            mapScroll.getHorizontalScrollBar().setValue(mapScroll.getHorizontalScrollBar().getValue() - tileScale / 2);    // up
                    }
                }
            }
        });
    }

    int repaintCount = 0;
    public void paint(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();   // casts g to graphics 2d

        // draw checkered bg (aesthetic only)
        for (int file = 0; file < maxMapTilesY; file++)
            for (int row = 0; row < maxMapTilesX; row++) {
                boolean swap = (file + row) % 2 != 0;
                g.setColor(swap ? lightGray.darker() : gray);
                g.fillRect(row * tileScale, file * tileScale, tileScale, tileScale);
            }

        // create number maps
        if (tileLocationX < maxMapTilesX && tileLocationY < maxMapTilesY && tileLocationX > -1 && tileLocationY > -1) { // checks if in bounds
            switch (Layers.selectedLayer) {
                case "Layer 1":
                    if (showLayerOne) {
                        L1_mapTileCoordinate[tileLocationX][tileLocationY] = tileValue;
                        if (collisionState.equals("On"))
                            L1_mapTileCoordinate[tileLocationX][tileLocationY] += tileID.length / 2;
                        L1_mapStringLine = new StringBuilder();
                        for (int i = 0; i < maxMapTilesY; i++) {
                            for (int ii = 0; ii < maxMapTilesX; ii++)
                                L1_mapStringLine.append(L1_mapTileCoordinate[ii][i]).append(" ");
                            L1_mapStringLine.append("\n");
                        }
                    }
                    break;
                case "Layer 2":
                    if (showLayerTwo) {
                        L2_mapTileCoordinate[tileLocationX][tileLocationY] = tileValue;
                        if (collisionState.equals("On"))
                            L2_mapTileCoordinate[tileLocationX][tileLocationY] += tileID.length / 2;
                        L2_mapStringLine = new StringBuilder();
                        for (int i = 0; i < maxMapTilesY; i++) {
                            for (int ii = 0; ii < maxMapTilesX; ii++)
                                L2_mapStringLine.append(L2_mapTileCoordinate[ii][i]).append(" ");
                            L2_mapStringLine.append("\n");
                        }
                    }
                    break;
                case "Layer 3":
                    if (showLayerThree) {
                        L3_mapTileCoordinate[tileLocationX][tileLocationY] = tileValue;
                        if (collisionState.equals("On"))
                            L3_mapTileCoordinate[tileLocationX][tileLocationY] += tileID.length / 2;
                        L3_mapStringLine = new StringBuilder();
                        for (int i = 0; i < maxMapTilesY; i++) {
                            for (int ii = 0; ii < maxMapTilesX; ii++)
                                L3_mapStringLine.append(L3_mapTileCoordinate[ii][i]).append(" ");
                            L3_mapStringLine.append("\n");
                        }
                    }
                    break;
                case "Layer 4":
                    if (showLayerFour) {
                        L4_mapTileCoordinate[tileLocationX][tileLocationY] = tileValue;
                        if (collisionState.equals("On"))
                            L4_mapTileCoordinate[tileLocationX][tileLocationY] += tileID.length / 2;
                        L4_mapStringLine = new StringBuilder();
                        for (int i = 0; i < maxMapTilesY; i++) {
                            for (int ii = 0; ii < maxMapTilesX; ii++)
                                L4_mapStringLine.append(L4_mapTileCoordinate[ii][i]).append(" ");
                            L4_mapStringLine.append("\n");
                        }
                    }
                    break;
            }
        }

        // draw tiles
        for (int row = 0; row < maxMapTilesY; row++)
            for (int file = 0; file < maxMapTilesX; file++) {
                int mapX = file * tileScale;
                int mapY = row * tileScale;
                // we don't paint tile if tile == 0 because 0 represents empty tile. this drastically improves performance
                // layer one
                if (showLayerOne && L1_mapTileCoordinate[file][row] != 0)
                    g2d.drawImage(tileID[L1_mapTileCoordinate[file][row]].image, mapX, mapY, tileScale, tileScale, null);
                // layer two
                if (showLayerTwo && L2_mapTileCoordinate[file][row] != 0)
                    g2d.drawImage(tileID[L2_mapTileCoordinate[file][row]].image, mapX, mapY, tileScale, tileScale, null);
                // layer three
                if (showLayerThree && L3_mapTileCoordinate[file][row] != 0)
                    g2d.drawImage(tileID[L3_mapTileCoordinate[file][row]].image, mapX, mapY, tileScale, tileScale, null);
                // layer four
                if (showLayerFour && L4_mapTileCoordinate[file][row] != 0)
                    g2d.drawImage(tileID[L4_mapTileCoordinate[file][row]].image, mapX, mapY, tileScale, tileScale, null);
                // paint collision
                if (tileID[L1_mapTileCoordinate[file][row]].collision && showLayerOne||
                        tileID[L2_mapTileCoordinate[file][row]].collision && showLayerTwo||
                        tileID[L3_mapTileCoordinate[file][row]].collision && showLayerThree||
                        tileID[L4_mapTileCoordinate[file][row]].collision && showLayerFour)
                    if (showCollisionState.equals("On"))
                        g2d.drawImage(COLLISION, mapX, mapY, tileScale, tileScale, null);
            }
        if (repaintCount/500*500==repaintCount) System.out.println("times repainted: "+repaintCount);   // count times repainted
        repaintCount++;
    }
}