package com.boray.shengKon.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.boray.shengKon.UI.OverDmxUI;

public class OverDmxListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		int num = Integer.valueOf(e.getActionCommand().substring(4)).intValue();
		new OverDmxUI().show(num);
	}
}
