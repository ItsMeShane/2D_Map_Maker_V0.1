package main;

import javax.swing.*;
import java.awt.event.*;

import static main.Main.*;
import static main.TileBarSettings.options;

public class MouseInput implements MouseListener, MouseMotionListener, MouseWheelListener {

    int pressedX, pressedY;

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        int X = e.getX();
        int Y = e.getY();
        if (e.getButton() == MouseEvent.BUTTON1) {
            pressedX = X;
            pressedY = Y;
            if (Tools.selectedTool.equals("Pen")) {
                if (e.isControlDown() || e.isShiftDown()) {
                    mapPanel.tileLocationX = -1;
                    mapPanel.tileLocationY = -1;
                } else {
                    mapPanel.tileLocationX = X / mapPanel.tileScale;
                    mapPanel.tileLocationY = Y / mapPanel.tileScale;
                    mapPanel.numMap_P1(mapPanel.tileLocationX, mapPanel.tileLocationY);
                    mapPanel.repaint();                }
            } else if (Tools.selectedTool.equals("Fill")) {
                if (!e.isControlDown() && !e.isShiftDown()) {
                    mapPanel.pointStart.x = X;
                    mapPanel.pointStart.y = Y;
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        mapPanel.tileLocationX = -1;
        mapPanel.tileLocationY = -1;

        mapPanel.numMap_P2();
        mapPanel.storeState();
        mapPanel.wipeSelection();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }


    @Override
    public void mouseDragged(MouseEvent e) {
        int X = e.getX();
        int Y = e.getY();
        if (e.isControlDown() && e.isShiftDown()) {
            if (X > pressedX)
                mapScroll.getHorizontalScrollBar().setValue(mapScroll.getHorizontalScrollBar().getValue() - Math.abs(pressedX - X));    // left
            if (X < pressedX)
                mapScroll.getHorizontalScrollBar().setValue(mapScroll.getHorizontalScrollBar().getValue() + Math.abs(pressedX - X));    // right
            if (Y > pressedY)
                mapScroll.getVerticalScrollBar().setValue(mapScroll.getVerticalScrollBar().getValue() - Math.abs(pressedY - Y));    // up
            if (Y < pressedY)
                mapScroll.getVerticalScrollBar().setValue(mapScroll.getVerticalScrollBar().getValue() + Math.abs(pressedY - Y));    // down
        } else if (e.isControlDown() || e.isShiftDown()) {
            mapPanel.tileLocationX = -1;
            mapPanel.tileLocationY = -1;
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            mapPanel.tileLocationX = X / mapPanel.tileScale;
            mapPanel.tileLocationY = Y / mapPanel.tileScale;
            mapPanel.numMap_P1(mapPanel.tileLocationX, mapPanel.tileLocationY);
            if (Tools.selectedTool.equals("Fill")) {
                mapPanel.pointEnd.x = X;
                mapPanel.pointEnd.y = Y;
            }
            mapPanel.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int scrollingDirection = e.getWheelRotation();
        // if scrolling towards
        if (scrollingDirection == 1) {
            if (e.isControlDown())
                options.scaleSlider.setValue(options.scaleSlider.getValue() - 5);
            else if (!e.isShiftDown())
                mapScroll.getVerticalScrollBar().setValue(mapScroll.getVerticalScrollBar().getValue() + mapPanel.tileScale / 2);    // right
            else
                mapScroll.getHorizontalScrollBar().setValue(mapScroll.getHorizontalScrollBar().getValue() + mapPanel.tileScale / 2);    // down
        }
        // if scrolling away
        else if (scrollingDirection == -1) {
            if (e.isControlDown())
                options.scaleSlider.setValue(options.scaleSlider.getValue() + 5);
            else if (!e.isShiftDown())
                mapScroll.getVerticalScrollBar().setValue(mapScroll.getVerticalScrollBar().getValue() - mapPanel.tileScale / 2);   // left
            else
                mapScroll.getHorizontalScrollBar().setValue(mapScroll.getHorizontalScrollBar().getValue() - mapPanel.tileScale / 2);    // up
        }
    }
}
