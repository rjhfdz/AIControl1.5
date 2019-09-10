package com.boray.mainUi;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class AboutPane {
	public void show(JPanel pane) {
		pane.setLayout(new FlowLayout(FlowLayout.LEFT));
		JButton btn = new JButton("¹ØÓÚ");
		btn.setPreferredSize(new Dimension(104,60));
		pane.add(btn);
	}
}
