package com.boray.xiaoGuoDeng.Listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class CreateTimeBlockListener implements MouseListener {
    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 3) {
            JPanel pane = (JPanel) e.getSource();
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem menuItem = new JMenuItem("���½�");
            JMenuItem menuItem1 = new JMenuItem("��ɾ��ȫ��");
            TimeBlockActionListener listener = new TimeBlockActionListener(pane);
            menuItem.addActionListener(listener);
            menuItem1.addActionListener(listener);
            popupMenu.add(menuItem);
            popupMenu.add(menuItem1);
            if (pane.isEnabled())
                popupMenu.show(pane, e.getX(), e.getY());
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
