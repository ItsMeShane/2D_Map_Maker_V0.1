package main;

import static main.Layers.*;
import static main.Main.*;

import static main.TileBarSettings.*;
import static main.tiles.TileBar.tileValue;
import static main.tiles.TileList.tileID;
import static java.awt.Color.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import java.util.Stack;

public class MapPanel extends JPanel {

    int tileScale = 100;

    int maxMapTilesX = 100;
    int maxMapTilesY = 100;
    {
        for (int i = 0; i < maxMapTilesY; i++) {
            for (int ii = 0; ii < maxMapTilesX; ii++)
                emptyLayer.append("0 ");
            emptyLayer.append("\n");
        }
    }
    StringBuilder L1_mapStringLine = new StringBuilder(emptyLayer);
    StringBuilder L2_mapStringLine = new StringBuilder(emptyLayer);
    StringBuilder L3_mapStringLine = new StringBuilder(emptyLayer);
    StringBuilder L4_mapStringLine = new StringBuilder(emptyLayer);

    int[][] L1_mapTileCoordinate = new int[maxMapTilesX][maxMapTilesY];
    int[][] L2_mapTileCoordinate = new int[maxMapTilesX][maxMapTilesY];
    int[][] L3_mapTileCoordinate = new int[maxMapTilesX][maxMapTilesY];
    int[][] L4_mapTileCoordinate = new int[maxMapTilesX][maxMapTilesY];

    int tileLocationX = -1;
    int tileLocationY = -1;

    Point pointStart = new Point(0, 0);
    Point pointEnd = new Point(0, 0);

    BufferedImage COLLISION = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("Collision.png")));
    BufferedImage SELECTED = ImageIO.read(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("Selected.png")));

    Stack<String> undo = new Stack<>();

    MouseInput mouseInput = new MouseInput();


    public MapPanel() throws IOException {
        this.setPreferredSize(new Dimension(maxMapTilesX * 150, maxMapTilesY * 150));
        this.setBackground(DARK_GRAY);
        this.addMouseListener(mouseInput);
        this.addMouseMotionListener(mouseInput);
        this.addMouseWheelListener(mouseInput);
        storeState();
    }


    public void paint(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // draw checkered bg (aesthetic only)
        for (int file = 0; file < maxMapTilesY; file++)
            for (int row = 0; row < maxMapTilesX; row++) {
                boolean swap = (file + row) % 2 != 0;
                g.setColor(swap ? lightGray.darker() : gray);
                g.fillRect(row * tileScale, file * tileScale, tileScale, tileScale);
            }

        // draw tiles from number maps
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
                if (tileID[L1_mapTileCoordinate[file][row]].collision && showLayerOne ||
                        tileID[L2_mapTileCoordinate[file][row]].collision && showLayerTwo ||
                        tileID[L3_mapTileCoordinate[file][row]].collision && showLayerThree ||
                        tileID[L4_mapTileCoordinate[file][row]].collision && showLayerFour)
                    if (showCollisionState.equals("On"))
                        g2d.drawImage(COLLISION, mapX, mapY, tileScale, tileScale, null);
            }


        // DRAW SELECTION BOX
        g2d.setStroke(new BasicStroke(tileScale/10f));
        g2d.setColor(new Color(20, 181, 224));

        // up left
        if (up(pointStart, pointEnd) && left(pointStart, pointEnd)) {
            // cycles through every tile. if tile is selected then draw selected image
            for (int row = 0; row < maxMapTilesY; row++)
                for (int file = 0; file < maxMapTilesX; file++)
                    if (selected(pointEnd, pointStart, file + 1, row + 1)){
                        numMap_P1(file, row);
                        g2d.drawImage(SELECTED, file * tileScale, row * tileScale, tileScale, tileScale, null);
                    }
            // draws selection border
            g2d.drawRect(snapX(pointEnd.x), snapY(pointEnd.y), snapX(pointStart.x) - snapX(pointEnd.x) + tileScale, snapY(pointStart.y) - snapY(pointEnd.y) + tileScale);
        }
        // up right
        else if (up(pointStart, pointEnd) && right(pointStart, pointEnd)) {
            // cycles through every tile. if tile is selected then draw selected image
            for (int row = 0; row < maxMapTilesY; row++)
                for (int file = 0; file < maxMapTilesX; file++)
                    if (selected(new Point(pointStart.x, pointEnd.y), new Point(pointEnd.x, pointStart.y), file + 1, row + 1)){
                        numMap_P1(file, row);
                        g2d.drawImage(SELECTED, file * tileScale, row * tileScale, tileScale, tileScale, null);
                    }
            // draws selection border
            g2d.drawRect(snapX(pointStart.x), snapY(pointEnd.y), snapX(pointEnd.x) - snapX(pointStart.x) + tileScale, snapY(pointStart.y) - snapY(pointEnd.y) + tileScale);
        }
        // down right
        else if (down(pointStart, pointEnd) && right(pointStart, pointEnd)) {
            // cycles through every tile. if tile is selected then draw selected image
            for (int row = 0; row < maxMapTilesY; row++)
                for (int file = 0; file < maxMapTilesX; file++)
                    if (selected(pointStart, pointEnd, file + 1, row + 1)) {
                        numMap_P1(file, row);
                        g2d.drawImage(SELECTED, file * tileScale, row * tileScale, tileScale, tileScale, null);
                    }
            // draws selection border
            g2d.drawRect(snapX(pointStart.x), snapY(pointStart.y), snapX(pointEnd.x) - snapX(pointStart.x) + tileScale, snapY(pointEnd.y) - snapY(pointStart.y) + tileScale);
        }
        // down left
        else if (down(pointStart, pointEnd) && left(pointStart, pointEnd)) {
            // cycles through every tile. if tile is selected then draw selected image
            for (int row = 0; row < maxMapTilesY; row++)
                for (int file = 0; file < maxMapTilesX; file++)
                    if (selected(new Point(pointEnd.x, pointStart.y), new Point(pointStart.x, pointEnd.y), file + 1, row + 1)){
                        numMap_P1(file, row);
                        g2d.drawImage(SELECTED, file * tileScale, row * tileScale, tileScale, tileScale, null);
                    }
            // draws selection border
            g2d.drawRect(snapX(pointEnd.x), snapY(pointStart.y), snapX(pointStart.x) - snapX(pointEnd.x) + tileScale, snapY(pointEnd.y) - snapY(pointStart.y) + tileScale);
        }
    }


    boolean selected(Point pointA, Point pointB, int x, int y) {
        Rectangle rect = new Rectangle(pointA, new Dimension(pointB.x - pointA.x + tileScale, pointB.y - pointA.y + tileScale));
        // returns true if tile checked is contained within selection
        return rect.contains(new Point(x * tileScale, y * tileScale));
    }


    boolean left(Point pointA, Point pointB) {
        return pointA.x > pointB.x;
    }
    boolean right(Point pointA, Point pointB) {
        return pointA.x < pointB.x;
    }
    boolean up(Point pointA, Point pointB) {
        return pointA.y > pointB.y;
    }
    boolean down(Point pointA, Point pointB) {
        return pointA.y < pointB.y;
    }


    // rounds int to match tile scale grid.     e.g. turns 964 to 900
    int snapX(int num) {
        if (num > maxMapTilesX *tileScale) return maxMapTilesX *tileScale-tileScale;
        return num / tileScale * tileScale;
    }
    int snapY(int num) {
        if (num > maxMapTilesY *tileScale) return maxMapTilesY *tileScale-tileScale;
        return num / tileScale * tileScale;
    }


    void wipeSelection() {
        pointStart.x = 0;
        pointStart.y = 0;
        pointEnd.x = 0;
        pointEnd.y = 0;
    }


    void numMap_P1(int x, int y) {
        // checks if in bounds
        if (x < maxMapTilesX && y < maxMapTilesY && x > -1 && y > -1)
            switch (selectedLayer) {
                case "Layer 1":
                    if (showLayerOne) {
                        // tile at mouse action = selected tile type
                        L1_mapTileCoordinate[x][y] = tileValue;
                        if (collisionState.equals("On"))
                            L1_mapTileCoordinate[x][y] += tileID.length / 2;
                    }
                    break;
                case "Layer 2":
                    if (showLayerTwo) {
                        L2_mapTileCoordinate[x][y] = tileValue;
                        if (collisionState.equals("On"))
                            L2_mapTileCoordinate[x][y] += tileID.length / 2;
                    }
                    break;
                case "Layer 3":
                    if (showLayerThree) {
                        L3_mapTileCoordinate[x][y] = tileValue;
                        if (collisionState.equals("On"))
                            L3_mapTileCoordinate[x][y] += tileID.length / 2;
                    }
                    break;
                case "Layer 4":
                    if (showLayerFour) {
                        L4_mapTileCoordinate[x][y] = tileValue;
                        if (collisionState.equals("On"))
                            L4_mapTileCoordinate[x][y] += tileID.length / 2;
                    }
                    break;
            }
    }
    void numMap_P2() {
        switch (selectedLayer) {
            case "Layer 1":
                if (showLayerOne) {
                    L1_mapStringLine = new StringBuilder();
                    for (int row = 0; row < maxMapTilesY; row++) {
                        for (int file = 0; file < maxMapTilesX; file++)
                            L1_mapStringLine.append(L1_mapTileCoordinate[file][row]).append(" ");
                        L1_mapStringLine.append("\n");
                    }
                }
                break;
            case "Layer 2":
                if (showLayerTwo) {
                    L2_mapStringLine = new StringBuilder();
                    for (int row = 0; row < maxMapTilesY; row++) {
                        for (int file = 0; file < maxMapTilesX; file++)
                            L2_mapStringLine.append(L2_mapTileCoordinate[file][row]).append(" ");
                        L2_mapStringLine.append("\n");
                    }
                }
                break;
            case "Layer 3":
                if (showLayerThree) {
                    L3_mapStringLine = new StringBuilder();
                    for (int row = 0; row < maxMapTilesY; row++) {
                        for (int file = 0; file < maxMapTilesX; file++)
                            L3_mapStringLine.append(L3_mapTileCoordinate[file][row]).append(" ");
                        L3_mapStringLine.append("\n");
                    }
                }
                break;
            case "Layer 4":
                if (showLayerFour) {
                    L4_mapStringLine = new StringBuilder();
                    for (int row = 0; row < maxMapTilesY; row++) {
                        for (int file = 0; file < maxMapTilesX; file++)
                            L4_mapStringLine.append(L4_mapTileCoordinate[file][row]).append(" ");
                        L4_mapStringLine.append("\n");
                    }
                }
                break;
        }
    }

    void storeState() {
        undo.push("" + L1_mapStringLine + L2_mapStringLine + L3_mapStringLine + L4_mapStringLine);
        if (undo.size() > 21) undo.remove(0);
    }
}