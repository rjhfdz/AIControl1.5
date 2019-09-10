package com.boray.quanJu.Listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.OutputStream;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JRadioButton;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.mainUi.MainUi;

public class ComBoxItemListener implements ItemListener{
	public void itemStateChanged(ItemEvent e) {
		if (ItemEvent.SELECTED == e.getStateChange()) {
			if (Data.serialPort != null) {
				try {
					OutputStream os = Data.serialPort.getOutputStream();
					os.write(code());
					os.flush();
					os.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
	}
	private byte[] code(){
		byte[] b = null;
		List list = (List)MainUi.map.get("quanJuComponeList");
		JComboBox box = (JComboBox)list.get(0);
		JComboBox box2 = (JComboBox)list.get(1);
		JComboBox box3 = (JComboBox)list.get(2);
		JComboBox box4 = (JComboBox)list.get(3);
		JRadioButton radioButton4 = (JRadioButton)list.get(6);
		JRadioButton radioButton5 = (JRadioButton)list.get(8);
		int[] tt = new int[6];
		if (box.getSelectedIndex() == 0) {
			tt[0] = 72;
		} else {
			tt[0] = 150;
		}
		tt[1] = box2.getSelectedIndex()+1;
		tt[2] = box3.getSelectedIndex();
		tt[3] = box4.getSelectedIndex();
		if (radioButton4.isSelected()) {
			tt[4] = 0;
		} else {
			tt[4] = 1;
		}
		if (radioButton5.isSelected()) {
			tt[5] = 0;
		} else {
			tt[5] = 1;
		}
		b = ZhiLingJi.setQuanJu(tt);
		return b;
	}
}
