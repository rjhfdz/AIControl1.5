package com.boray.Utils;

import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class UtilJDialog {

    private IconJDialog dialog;

    public UtilJDialog(IconJDialog dialog, String title, JTextField field, String fieldName, ActionListener listener) {
        JFrame f = (JFrame) MainUi.map.get("frame");
        this.dialog = dialog;
//        dialog = new IconJDialog(f, true);
        dialog.setResizable(false);
        dialog.setTitle(title);
        int w = 380, h = 280;
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
        dialog.setSize(w, h);
        dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(300, 50));
        p1.add(new JLabel(fieldName));
        p1.add(field);

        JPanel p2 = new JPanel();
        p2.setPreferredSize(new Dimension(300, 50));
        JButton btn = new JButton("取消");
        JButton btn2 = new JButton("确定");
        btn.addActionListener(listener);
        btn2.addActionListener(listener);
        p2.add(btn);
        p2.add(new JLabel("      "));
        p2.add(btn2);

        JPanel N1 = new JPanel();
        N1.setPreferredSize(new Dimension(300, 60));
        dialog.add(N1);
        dialog.add(p1);
        dialog.add(p2);

    }

    public void isVisible(boolean flag){
        dialog.setVisible(flag);
    }
}
