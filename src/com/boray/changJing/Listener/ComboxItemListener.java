package com.boray.changJing.Listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.OutputStream;

import javax.swing.JComboBox;
import javax.swing.JRadioButton;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.Socket;
import com.boray.mainUi.MainUi;

public class ComboxItemListener implements ItemListener{
	public void itemStateChanged(ItemEvent e) {
		if (ItemEvent.SELECTED == e.getStateChange()) {
			if (Data.serialPort != null|| Data.socket!=null) {
				Socket.SendData(code());
//				try {
//					OutputStream os = Data.serialPort.getOutputStream();
//					os.write(code());
//					os.flush();
//					os.close();
//				} catch (Exception e2) {
//					e2.printStackTrace();
//				}
			}
		}
	}

	public byte[] code() {
		int[] t = new int[13];
		//运行模式
		JRadioButton radioButton1 = (JRadioButton)MainUi.map.get("yunXingModelBtn1");
		JRadioButton radioButton2 = (JRadioButton)MainUi.map.get("yunXingModelBtn2");
		if (radioButton1.isSelected()) {
			t[11] = 1;
		} else if (radioButton2.isSelected()) {
			t[11] = 2;
		}
		//声控模式
		JComboBox[] boxs1 = (JComboBox[])MainUi.map.get("shengKonModelBoxs");
		for (int i = 0; i < 3; i++) {
			t[8+i] = boxs1[i].getSelectedIndex();
		}
		//场景模式
		JComboBox[] boxs2 = (JComboBox[])MainUi.map.get("changJingModelBoxs");
		for (int i = 0; i < 3; i++) {
			t[4+i] = boxs2[i].getSelectedIndex();
		}
		t[7] = 0;
		//效果灯开关
		JComboBox box = (JComboBox)MainUi.map.get("xiaoGuoDengKaiGuangBox");
		if (box.getSelectedIndex() == 0) {
			t[3] = 1;
		} else if (box.getSelectedIndex() == 1) {
			t[3] = 0;
		} else if (box.getSelectedIndex() == 2) {
			t[3] = 172;
		}
		//摇麦延续开关
		JRadioButton radioButton3 = (JRadioButton)MainUi.map.get("yaoMaiKaiGuangBtn1");
		JRadioButton radioButton4 = (JRadioButton)MainUi.map.get("yaoMaiKaiGuangBtn2");
		if (radioButton3.isSelected()) {
			t[2] = 1;
		} else if (radioButton4.isSelected()) {
			t[2] = 0;
		}
		//摇麦触发间隔
		JComboBox box2 = (JComboBox)MainUi.map.get("yaoMaiJianGeBox");
		t[1] = box2.getSelectedIndex();
		//摇麦模式
		JComboBox box3 = (JComboBox)MainUi.map.get("yaoMaiModelBox");
		if (box3.getSelectedIndex() == 3) {
			t[0] = 172;
		} else {
			t[0] = box3.getSelectedIndex();
		}
		//雾机模式
		JComboBox box4 = (JComboBox)MainUi.map.get("wuJiModelBox");
		t[12] = box4.getSelectedIndex();
		return ZhiLingJi.setChangJing2(Data.changJingModel, t);
	}
}
