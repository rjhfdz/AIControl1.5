package com.boray.zhongKon.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.util.List;

import javax.swing.JRadioButton;
import javax.swing.JSlider;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.mainUi.MainUi;

public class SettingActionListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		sendCode();
	}
	public void sendCode(){
		byte[] b = new byte[5];
		List list = (List)MainUi.map.get("settingZhongKongListCompone");
		JRadioButton radioButton = (JRadioButton)list.get(0);
		//JRadioButton radioButton2 = (JRadioButton)list.get(1);
		if (radioButton.isSelected()) {
			b[0] = 0;
		} else {
			b[0] = 1;
		}
		
		JRadioButton radioButton3 = (JRadioButton)list.get(2);
		//JRadioButton radioButton4 = (JRadioButton)list.get(3);
		if (radioButton3.isSelected()) {
			b[1] = 1;
		} else {
			b[1] = 0;
		}
		
		
		JRadioButton radioButton5 = (JRadioButton)list.get(4);
		//JRadioButton radioButton6 = (JRadioButton)list.get(5);
		if (radioButton5.isSelected()) {
			b[2] = 1;
		} else {
			b[2] = 0;
		}
		
		
		JRadioButton radioButton7 = (JRadioButton)list.get(6);
		//JRadioButton radioButton8 = (JRadioButton)list.get(7);
		if (radioButton7.isSelected()) {
			b[3] = 0;
		} else {
			b[3] = 1;
		}
		
		JSlider slider = (JSlider)list.get(8);
		//JComboBox box = (JComboBox)list.get(9);
		b[4] = (byte)slider.getValue();
		
		if (Data.serialPort != null) {
			try {
				OutputStream os = Data.serialPort.getOutputStream();
				os.write(ZhiLingJi.setZhongKon2(b));
				os.flush();
				os.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
