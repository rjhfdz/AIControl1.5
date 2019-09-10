package com.boray.shengKon.Listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class CreateTimeBlock_shengKon_Listener implements MouseListener{
	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 3) {
			JPanel pane = (JPanel)e.getSource();
			JPopupMenu popupMenu = new JPopupMenu();
			JMenuItem menuItem = new JMenuItem("　新建");
			JMenuItem menuItem1 = new JMenuItem("　删除所有");
			JMenuItem menuItem2 = new JMenuItem("　粘贴");
			TimeBlockAction_shengKon_Listener listener = new TimeBlockAction_shengKon_Listener(pane);
			menuItem.addActionListener(listener);
			menuItem1.addActionListener(listener);
			menuItem2.addActionListener(listener);
			popupMenu.add(menuItem);
			popupMenu.add(menuItem1);
			popupMenu.add(menuItem2);
			popupMenu.show(pane,e.getX(),e.getY());
		}
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
}
