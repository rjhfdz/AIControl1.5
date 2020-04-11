package com.boray.quanJu.Listener;

import java.awt.event.ItemEvent;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.JSlider;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.mainUi.MainUi;

public class TiaoGuangSendCode {
	public void sendCode(int cj){
		if (Data.serialPort != null) {
			ArrayList list = (ArrayList)MainUi.map.get("setTiaoGuangCpns");
			int[] b = new int[19];
			for (int i = 0; i < b.length; i++) {
				b[i] = 0;
			}
			//可调、下限
			JComboBox box = (JComboBox)list.get(0);
			JSlider slider = (JSlider)list.get(2);
			
			JComboBox box2 = (JComboBox)list.get(3);
			JSlider slider2 = (JSlider)list.get(5);
			
			JComboBox box3 = (JComboBox)list.get(6);
			JSlider slider3_3 = (JSlider)list.get(8);
			
			JComboBox box4 = (JComboBox)list.get(9);
			JSlider slider4_4 = (JSlider)list.get(11);
			
			
			//上限
			JSlider slider3 = (JSlider)list.get(1);
			JSlider slider4 = (JSlider)list.get(4);
			JSlider slider5 = (JSlider)list.get(7);
			JSlider slider6 = (JSlider)list.get(10);
			
			if (cj == 25) {
				b[3] = box.getSelectedIndex()+10;
				b[4] = box2.getSelectedIndex()+10;
				b[5] = box3.getSelectedIndex()+10;
				b[6] = box4.getSelectedIndex()+10;
				b[15] = slider.getValue();
				b[16] = slider2.getValue();
				b[17] = slider3_3.getValue();
				b[18] = slider4_4.getValue();
			} else if (cj == 26) {
				b[15] = slider3.getValue();
				b[16] = slider4.getValue();
				b[17] = slider5.getValue();
				b[18] = slider6.getValue();
			}
			
			try {
				OutputStream os = Data.serialPort.getOutputStream();
				os.write(ZhiLingJi.setChangJing1(cj, b));
				os.flush();
				os.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public void setData(){
		ArrayList list = (ArrayList)MainUi.map.get("setTiaoGuangCpns");
		//可调、下限
		JComboBox box = (JComboBox)list.get(0);
		JSlider slider = (JSlider)list.get(2);
		slider.setValue(8);
		JComboBox box2 = (JComboBox)list.get(3);
		JSlider slider2 = (JSlider)list.get(5);
		slider2.setValue(8);
		JComboBox box3 = (JComboBox)list.get(6);
		JSlider slider3_3 = (JSlider)list.get(8);
		slider3_3.setValue(8);
		JComboBox box4 = (JComboBox)list.get(9);
		JSlider slider4_4 = (JSlider)list.get(11);
		slider4_4.setValue(8);
		//上限
		JSlider slider3 = (JSlider)list.get(1);
		slider3.setValue(100);
		JSlider slider4 = (JSlider)list.get(4);
		slider4.setValue(100);
		JSlider slider5 = (JSlider)list.get(7);
		slider5.setValue(100);
		JSlider slider6 = (JSlider)list.get(10);
		slider6.setValue(100);

	}
}
