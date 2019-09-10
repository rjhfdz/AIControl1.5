package com.boray.changJing.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.changJing.Data.DataOfChangJing;
import com.boray.mainUi.MainUi;
import com.boray.returnListener.ComListener;

public class ChangJingSendCodeListener implements ActionListener{
	private byte[] b = new byte[1];
	private int a = 0;
	public void actionPerformed(ActionEvent e) {
		JToggleButton btn = (JToggleButton)e.getSource();
		final int n = Integer.valueOf(btn.getName());
		if (Data.isTest) {
			if (Data.serialPort != null) {
				try {
					byte[] b = new byte[3];
					b[0] = (byte)0xFB;b[1] = (byte)n;b[2] = (byte)0xC0;
					OutputStream os = Data.serialPort.getOutputStream();
					os.write(b);
					os.flush();
					os.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		} else {
			//����ǰһ����������
			if (Data.changJingModel != -1) {
				List list = (List)DataOfChangJing.map.get(""+Data.changJingModel);
				if (list == null) {
					list = new ArrayList();
				}
				saveData(list);
				DataOfChangJing.map.put(""+Data.changJingModel, list);
			}
			//���ص�ǰ��������
			loadData(n);
			
			Data.changJingModel = n;
			new Thread(new Runnable() {
				public void run() {
					JComboBox box = (JComboBox)MainUi.map.get("copyBox");
					box.removeAllItems();
					for (int i = 0; i < 37; i++) {
						if (i != 25 && i != n) {
							box.addItem(String.valueOf(i));
						}
					}
				}
			}).start();
			if (Data.serialPort != null) {
				try {
					OutputStream os = Data.serialPort.getOutputStream();
					os.write(ZhiLingJi.changJingChaXun(n));
					os.flush();
					os.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	public void loadData(int cj){
		List list = (List)DataOfChangJing.map.get(""+cj);
		if (list != null) {
			SerialPort serialPort = Data.serialPort;
			Data.serialPort = null;
			
			//8�����ɵ�
			JComboBox[] boxs8 = (JComboBox[])MainUi.map.get("kaiGuangBox_BuKeTiao");
			for (int i = 0; i < boxs8.length; i++) {
				boxs8[i].setSelectedIndex(Integer.valueOf(list.get(i).toString()));
			}
			
			//4���ƵĿ��غ�����
			JComboBox[] boxs = (JComboBox[])MainUi.map.get("kaiGuangBox");
			JSlider[] sliders = (JSlider[])MainUi.map.get("liangDuSliders");
			for (int i = 0; i < sliders.length; i++) {
				boxs[i].setSelectedIndex(Integer.valueOf(list.get(8+i*2).toString()));
				sliders[i].setValue(Integer.valueOf(list.get(i*2+9).toString()));
			}
			
			//ȫ������
			JSlider slider = (JSlider)MainUi.map.get("quanJuLiangDuSlider");
			slider.setValue(Integer.valueOf(list.get(16).toString()));
			
			//����ģʽ
			JRadioButton radioButton = (JRadioButton)MainUi.map.get("kaiGuangModelBtn1");
			JRadioButton radioButton2 = (JRadioButton)MainUi.map.get("kaiGuangModelBtn2");
			if (list.get(17).toString().equals("1")) {
				radioButton.setSelected(true);
			} else {
				radioButton2.setSelected(true);
			}
			
			//����ģʽ
			JRadioButton radioButton3 = (JRadioButton)MainUi.map.get("liangDuModelBtn1");
			JRadioButton radioButton4 = (JRadioButton)MainUi.map.get("liangDuModelBtn2");
			if (list.get(18).toString().equals("1")) {
				radioButton3.setSelected(true);
			} else {
				radioButton4.setSelected(true);
			}
			
			//Ч���ƿ���
			JComboBox box = (JComboBox)MainUi.map.get("xiaoGuoDengKaiGuangBox");
			box.setSelectedIndex(Integer.valueOf(list.get(19).toString()));
			
			//����ģʽ
			JComboBox[] boxs2 = (JComboBox[])MainUi.map.get("changJingModelBoxs");
			for (int i = 0; i < boxs2.length; i++) {
				boxs2[i].setSelectedIndex(Integer.valueOf(list.get(20+i).toString()));
			}
			
			//����ģʽ
			JComboBox[] boxs1 = (JComboBox[])MainUi.map.get("shengKonModelBoxs");
			for (int i = 0; i < boxs1.length; i++) {
				boxs1[i].setSelectedIndex(Integer.valueOf(list.get(23+i).toString()));
			}
			
			//����ģʽ
			JRadioButton radioButton1 = (JRadioButton)MainUi.map.get("yunXingModelBtn1");
			JRadioButton radioButton6 = (JRadioButton)MainUi.map.get("yunXingModelBtn2");
			if (list.get(26).toString().equals("1")) {
				radioButton1.setSelected(true);
			} else {
				radioButton6.setSelected(true);
			}
			
			//ҡ��ģʽ
			JComboBox box3 = (JComboBox)MainUi.map.get("yaoMaiModelBox");
			box3.setSelectedIndex(Integer.valueOf(list.get(27).toString()));
			
			//ҡ�󴥷����
			/*JComboBox box2 = (JComboBox)MainUi.map.get("yaoMaiJianGeBox");
			box2.setSelectedIndex(Integer.valueOf(list.get(28).toString()));
			
			//ҡ����������
			JRadioButton radioButton5 = (JRadioButton)MainUi.map.get("yaoMaiKaiGuangBtn1");
			JRadioButton radioButton8 = (JRadioButton)MainUi.map.get("yaoMaiKaiGuangBtn2");
			if (list.get(29).toString().equals("1")) {
				radioButton5.setSelected(true);
			} else {
				radioButton8.setSelected(true);
			}*/
			
			Data.serialPort = serialPort;
		}
	}
	private void saveData(List list){
		list.clear();
		
		//8�����ɵ�
		JComboBox[] boxs8 = (JComboBox[])MainUi.map.get("kaiGuangBox_BuKeTiao");
		for (int i = 0; i < boxs8.length; i++) {
			list.add(String.valueOf(boxs8[i].getSelectedIndex()));
		}
		
		//4���ƵĿ��غ�����
		JComboBox[] boxs = (JComboBox[])MainUi.map.get("kaiGuangBox");
		JSlider[] sliders = (JSlider[])MainUi.map.get("liangDuSliders");
		for (int i = 0; i < sliders.length; i++) {
			list.add(String.valueOf(boxs[i].getSelectedIndex()));
			list.add(String.valueOf(sliders[i].getValue()));
		}
		//ȫ������
		JSlider slider = (JSlider)MainUi.map.get("quanJuLiangDuSlider");
		list.add(String.valueOf(slider.getValue()));
		//����ģʽ
		JRadioButton radioButton = (JRadioButton)MainUi.map.get("kaiGuangModelBtn1");
		if (radioButton.isSelected()) {
			list.add(String.valueOf(1));
		} else {
			list.add(String.valueOf(0));
		}
		
		//����ģʽ
		JRadioButton radioButton3 = (JRadioButton)MainUi.map.get("liangDuModelBtn1");
		if (radioButton3.isSelected()) {
			list.add(String.valueOf(1));
		} else {
			list.add(String.valueOf(0));
		}
		
		//Ч���ƿ���
		JComboBox box = (JComboBox)MainUi.map.get("xiaoGuoDengKaiGuangBox");
		list.add(String.valueOf(box.getSelectedIndex()));
		
		//����ģʽ
		JComboBox[] boxs2 = (JComboBox[])MainUi.map.get("changJingModelBoxs");
		for (int i = 0; i < boxs2.length; i++) {
			list.add(String.valueOf(boxs2[i].getSelectedIndex()));
		}
		
		//����ģʽ
		JComboBox[] boxs1 = (JComboBox[])MainUi.map.get("shengKonModelBoxs");
		for (int i = 0; i < boxs1.length; i++) {
			list.add(String.valueOf(boxs1[i].getSelectedIndex()));
		}
		
		//����ģʽ
		JRadioButton radioButton1 = (JRadioButton)MainUi.map.get("yunXingModelBtn1");
		if (radioButton1.isSelected()) {
			list.add(String.valueOf(1));
		} else {
			list.add(String.valueOf(0));
		}
		
		//ҡ��ģʽ
		JComboBox box3 = (JComboBox)MainUi.map.get("yaoMaiModelBox");
		list.add(String.valueOf(box3.getSelectedIndex()));
		
		//ҡ�󴥷����
		/*JComboBox box2 = (JComboBox)MainUi.map.get("yaoMaiJianGeBox");
		list.add(String.valueOf(box2.getSelectedIndex()));
		
		//ҡ����������
		JRadioButton radioButton4 = (JRadioButton)MainUi.map.get("yaoMaiKaiGuangBtn1");
		if (radioButton4.isSelected()) {
			list.add(String.valueOf(1));
		} else {
			list.add(String.valueOf(0));
		}*/
	}
}
