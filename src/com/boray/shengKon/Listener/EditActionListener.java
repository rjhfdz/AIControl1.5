package com.boray.shengKon.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.boray.Data.MyData;
import com.boray.shengKon.UI.EditEffect_shengKonUI;

import javax.swing.*;

public class EditActionListener implements ActionListener{
	private int blockNum;
	private String groupNum;
	private String number;
	private JPanel pane;
	public EditActionListener(int blockNum, JPanel pane, String number){
		this.blockNum = blockNum;
		this.groupNum = pane.getName();
		this.number = number;
		this.pane = pane;
	}
	public void actionPerformed(ActionEvent e) {

		new EditEffect_shengKonUI().show(blockNum,groupNum,number,pane);
	}
}
