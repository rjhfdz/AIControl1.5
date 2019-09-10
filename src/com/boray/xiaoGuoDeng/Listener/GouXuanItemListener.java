package com.boray.xiaoGuoDeng.Listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JToggleButton;

import com.boray.Data.Data;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

public class GouXuanItemListener implements ItemListener{
	public void itemStateChanged(ItemEvent e) {
		JCheckBox box = (JCheckBox)e.getSource();
		if (box.isEnabled()) {
			JToggleButton[] toggleButtons = (JToggleButton[])MainUi.map.get("toggleButtons_changjin");
			//NewJTable table_DMX_All = (NewJTable)MainUi.map.get("table_DMX_All");
			NewJTable table_dengJu = (NewJTable)MainUi.map.get("table_dengJu");
			int a = Integer.valueOf(box.getName()).intValue();
			for (int j = 0; j < toggleButtons.length; j++) {
				if (toggleButtons[j].isSelected() && toggleButtons[j].isEnabled()) {
					//int slt = table_DMX_All.getSelectedRow();
					//if (slt != -1) {
						table_dengJu = (NewJTable)MainUi.map.get("table_dengJu");
						int start = Integer.valueOf(table_dengJu.getValueAt(j, 3).toString()).intValue();
						//System.out.println(start+a);
						Data.checkList[start+a-1] = box.isSelected();
					//} else {
						
					//}
				}
			}
		}
	}
}
