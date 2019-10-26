package com.boray.suCai.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JToggleButton;

import com.boray.Data.Data;
import com.boray.mainUi.MainUi;

public class SuCaiTypeListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		JToggleButton btn = (JToggleButton)e.getSource();
		JList suCaiLightType = (JList)MainUi.map.get("suCaiLightType");
		if (suCaiLightType.getSelectedValue() != null) {
			Map map = (Map)Data.suCaiNameMap.get(suCaiLightType.getSelectedValue().toString());
			if (map!=null) {
				List tmp = (List)map.get(btn.getName());
				JList list = (JList)MainUi.map.get("suCai_list");
				DefaultListModel  model = (DefaultListModel)list.getModel();
				model.removeAllElements();
				if (tmp!=null) {
					for (int i = 0; i < tmp.size(); i++) {
						model.addElement(tmp.get(i).toString());
					}
					if (tmp.size()>0) {
						list.setSelectedIndex(0);
					}
				}
			} else {
				JList list = (JList)MainUi.map.get("suCai_list");
				list.removeAll();
			}
		}
	}
}
