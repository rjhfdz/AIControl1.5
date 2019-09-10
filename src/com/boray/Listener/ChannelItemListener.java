package com.boray.Listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import javax.swing.JComboBox;

import com.boray.Data.Data;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

public class ChannelItemListener implements ItemListener{
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == 1) {
			JComboBox box = (JComboBox)e.getSource();
			NewJTable table = (NewJTable)MainUi.map.get("table_DkGl");
			String name = box.getName();
			Map map = (Map)Data.DengKuList.get(table.getSelectedRow());
			map.put(name, box.getSelectedItem().toString());
			Data.dengKu_change = true;
		}
	}
}
