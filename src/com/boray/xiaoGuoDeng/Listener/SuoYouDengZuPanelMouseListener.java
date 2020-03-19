package com.boray.xiaoGuoDeng.Listener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SuoYouDengZuPanelMouseListener implements MouseListener, ActionListener {
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 3) {
            JPanel panel = (JPanel) e.getSource();
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem item = new JMenuItem("新建");
            JMenuItem item2 = new JMenuItem("粘贴");
            JMenuItem item3 = new JMenuItem("删除全部");
            item.addActionListener(this);
            item2.addActionListener(this);
            item3.addActionListener(this);
            popupMenu.add(item);
            popupMenu.add(item2);
            popupMenu.add(item3);
            if (panel.isEnabled())
                popupMenu.show(panel, e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("新建".equals(e.getActionCommand())) {

        } else if ("粘贴".equals(e.getActionCommand())) {

        } else if ("删除全部".equals(e.getActionCommand())) {

        }
    }
}
