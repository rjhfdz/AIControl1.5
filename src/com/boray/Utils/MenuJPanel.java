package com.boray.Utils;

import javax.swing.*;
import java.awt.*;

public class MenuJPanel extends JPanel {

    private String path;

    public MenuJPanel(String path){
        super();
        this.path = path;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon img = new ImageIcon(getClass().getResource(path));
        img.paintIcon(this, g, 0, 0);
    }
}
