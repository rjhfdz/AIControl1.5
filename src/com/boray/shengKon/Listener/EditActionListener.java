package com.boray.shengKon.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.boray.Data.MyData;
import com.boray.shengKon.UI.EditEffect_shengKonUI;

public class EditActionListener implements ActionListener{
	private int blockNum;
	private String groupNum;
	public EditActionListener(int blockNum,String groupNum){
		this.blockNum = blockNum;
		this.groupNum = groupNum;
	}
	public void actionPerformed(ActionEvent e) {
		new EditEffect_shengKonUI().show(blockNum,groupNum);
	}
}
