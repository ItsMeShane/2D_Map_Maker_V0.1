package main;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static main.Main.rockwell;

public class Tools extends JPanel {

    Border border = BorderFactory.createLineBorder(Color.YELLOW.darker().darker(), 5);
    Color red = new Color(182, 66, 66);
    Color green = new Color(91, 182, 66);

    static String selectedTool = "Pen";

    public Tools() {
        this.setLayout(new GridLayout(1, 4));
        this.setMinimumSize(new Dimension(0, 50));
        doStuff();
    }


    void doStuff() {

        JButton penButt = new JButton("Pen");
        JButton fillButt = new JButton("Fill");
        JButton moveButt = new JButton("Move");
        JButton changeLayersButt = new JButton("Change Layers");

        penButt.setPreferredSize(new Dimension(250, 50));
        penButt.setBackground(green);
        penButt.setFocusable(false);
        penButt.setFocusPainted(false);
        penButt.setVisible(true);
        penButt.setFont(rockwell);
        penButt.setForeground(Color.black);
        penButt.setBorder(border);
        penButt.setOpaque(true);
        penButt.addActionListener(e -> {
            selectedTool = "Pen";

            penButt.setBackground(green);
            fillButt.setBackground(red);
            moveButt.setBackground(red);
            changeLayersButt.setBackground(red);
        });


        fillButt.setPreferredSize(new Dimension(250, 50));
        fillButt.setBackground(red);
        fillButt.setFocusable(false);
        fillButt.setFocusPainted(false);
        fillButt.setVisible(true);
        fillButt.setFont(rockwell);
        fillButt.setForeground(Color.black);
        fillButt.setBorder(border);
        fillButt.setOpaque(true);
        fillButt.addActionListener(e -> {
            selectedTool = "Fill";

            penButt.setBackground(red);
            fillButt.setBackground(green);
            moveButt.setBackground(red);
            changeLayersButt.setBackground(red);
        });

        moveButt.setPreferredSize(new Dimension(250, 50));
        moveButt.setBackground(red);
        moveButt.setFocusable(false);
        moveButt.setFocusPainted(false);
        moveButt.setVisible(true);
        moveButt.setFont(rockwell);
        moveButt.setForeground(Color.black);
        moveButt.setBorder(border);
        moveButt.setOpaque(true);
        moveButt.addActionListener(e -> {
            selectedTool = "Move";

            penButt.setBackground(red);
            fillButt.setBackground(red);
            moveButt.setBackground(green);
            changeLayersButt.setBackground(red);
        });

        changeLayersButt.setPreferredSize(new Dimension(250, 50));
        changeLayersButt.setBackground(red);
        changeLayersButt.setFocusable(false);
        changeLayersButt.setFocusPainted(false);
        changeLayersButt.setVisible(true);
        changeLayersButt.setFont(rockwell);
        changeLayersButt.setForeground(Color.black);
        changeLayersButt.setBorder(border);
        changeLayersButt.setOpaque(true);
        changeLayersButt.addActionListener(e -> {
            selectedTool = "Change Layers";

            penButt.setBackground(red);
            fillButt.setBackground(red);
            moveButt.setBackground(red);
            changeLayersButt.setBackground(green);
        });


        this.add(penButt);
        this.add(fillButt);
//        this.add(moveButt);
//        this.add(changeLayersButt);
    }
}