package com.boray.zhongKon.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.mainUi.MainUi;
import com.boray.zhongKon.Data.DataOfZhongKon;

public class WriteActionListener implements ActionListener{
	public static String select = "";
	public void actionPerformed(ActionEvent e) {

		try {
			JButton btn = (JButton)e.getSource();
			String s = btn.getName();
			//红外码
			JTextField[] fields = (JTextField[])MainUi.map.get("redCodeFields");
			//串口墙板
			JTextField[] fields2 = (JTextField[])MainUi.map.get("comQianBanFields1");
			
			//串口上行
			JTextField[] fields6 = (JTextField[])MainUi.map.get("comShangXingFields");
			//串口下行
			JTextField[] fields7 = (JTextField[])MainUi.map.get("comXiaXingFields");
			
			//组号
			JComboBox box = (JComboBox)MainUi.map.get("zhongKonGroupBox");
			int selected = box.getSelectedIndex();
			OutputStream os = null;
			if (Data.serialPort != null) {
				os = Data.serialPort.getOutputStream();
			}
			byte[] code = null;
			int size = 0;
			if ("2".equals(s)) {
				size = 0;
				code = new byte[4];
				for (int i = 0; i < 4; i++) {
					if ("".equals(fields[i].getText())) {
						code[i] = 0;
					} else {
						code[i] = Integer.valueOf(fields[i].getText(), 16).byteValue();
						++size;
					}
				}
				if (os != null) {
					os.write(ZhiLingJi.writeCode(selected, 5, code,size));
					os.flush();
					os.close();
				}
			} else if ("3".equals(s)) {
				if (os != null) {
					os.write(ZhiLingJi.caChuMaZhi(selected, 5));
					os.flush();
					os.close();
				}
				for (int i = 0; i < fields.length; i++) {
					fields[i].setText("");
				}
			} else if ("6".equals(s)) {
				size = 0;
				code = new byte[5];
				String tp = "";
				for (int i = 0; i < 5; i++) {
					if ("".equals(fields2[i].getText())) {
						code[i] = 0;
					} else {
						code[i] = Integer.valueOf(fields2[i].getText(), 16).byteValue();
						++size;
					}
				}
				
				
				/*if (os != null) {
					os.write(ZhiLingJi.writeCode(selected, 1, code,size));
					os.flush();
					os.close();
				} else {*/
					if (os != null) {
						os.write(ZhiLingJi.writeCode(selected, 1, code,size));
						os.flush();
						os.close();
					}
					if (size != 0) {
						JTextField[] fields3 = (JTextField[])MainUi.map.get("comQianBanFields2");
						JTextField[] fields4 = (JTextField[])MainUi.map.get("comQianBanFields3");
						JTextField[] fields5 = (JTextField[])MainUi.map.get("comQianBanFields4");
						int cnt1 = 0,cnt2 = 0,cnt3 = 0;
						for (int i = 0; i < 5; i++) {
							if (fields2[i].getText().equals(fields3[i].getText())) {
								cnt1++;
							}
							if (fields2[i].getText().equals(fields4[i].getText())) {
								cnt2++;
							}
							if (fields2[i].getText().equals(fields5[i].getText())) {
								cnt3++;
							}
						}
						for (int i = 0; i < 5; i++) {
							if (cnt1 != 5 && cnt2 != 5 && cnt3 != 5) {
								fields5[i].setText(fields4[i].getText());
								fields4[i].setText(fields3[i].getText());
								fields3[i].setText(fields2[i].getText());
							}
							fields2[i].setText("");
						}
					}
				//}
			} else if ("7".equals(s) || "11".equals(s) || "15".equals(s) || "19".equals(s)) {
				JTextField[] fields3 = fields2;
				if ("11".equals(s)) {
					fields3 = (JTextField[])MainUi.map.get("comQianBanFields2");
				} else if ("15".equals(s)) {
					fields3 = (JTextField[])MainUi.map.get("comQianBanFields3");
				} else if ("19".equals(s)) {
					fields3 = (JTextField[])MainUi.map.get("comQianBanFields4");
				}
				for (int i = 0; i < 5; i++) {
					fields3[i].setText("");
				}
				if (os != null) {
					os.write(ZhiLingJi.caChuMaZhi(selected, 1));
					os.flush();
					os.close();
				} /*else {
					for (int i = 0; i < 5; i++) {
						JTextField[] fields3 = (JTextField[])MainUi.map.get("comQianBanFields2");
						JTextField[] fields4 = (JTextField[])MainUi.map.get("comQianBanFields3");
						JTextField[] fields5 = (JTextField[])MainUi.map.get("comQianBanFields4");
						fields2[i].setText(fields3[i].getText());
						fields3[i].setText(fields4[i].getText());
						fields4[i].setText(fields5[i].getText());
						fields5[i].setText("");
					}
				}*/
			} else if ("sx0".equals(s)) {
				size = 0;
				code = new byte[fields6.length];
				for (int i = 0; i < fields6.length; i++) {
					if ("".equals(fields6[i].getText())) {
						code[i] = 0;
					} else {
						code[i] = Integer.valueOf(fields6[i].getText(), 16).byteValue();
						++size;
					}
				}
				if (os != null) {
					os.write(ZhiLingJi.writeCode(selected, 6, code,size));
					os.flush();
					os.close();
				}
			} else if ("sx1".equals(s)) {
				if (os != null) {
					os.write(ZhiLingJi.caChuMaZhi(selected, 6));
					os.flush();
					os.close();
				}
				for (int i = 0; i < fields6.length; i++) {
					fields6[i].setText("");
				}
			} else if ("sx2".equals(s)) {
				size = 0;
				code = new byte[fields6.length];
				for (int i = 0; i < fields7.length; i++) {
					if ("".equals(fields7[i].getText())) {
						code[i] = 0;
					} else {
						code[i] = Integer.valueOf(fields7[i].getText(), 16).byteValue();
						++size;
					}
				}
				if (os != null) {
					os.write(ZhiLingJi.writeCode(selected, 7, code,size));
					os.flush();
					os.close();
				}
			} else if ("sx3".equals(s)) {
				if (os != null) {
					os.write(ZhiLingJi.caChuMaZhi(selected, 7));
					os.flush();
					os.close();
				}
				for (int i = 0; i < fields7.length; i++) {
					fields7[i].setText("");
				}
			} else if ("0".equals(s)) {
				if (os != null) {
					os.write(ZhiLingJi.studyCode(selected, 5));
					os.flush();
					os.close();
				}
			} else if ("4".equals(s) || "8".equals(s) || "12".equals(s) || "16".equals(s)) {
				if (os != null) {
					select = s;
					os.write(ZhiLingJi.studyCode(selected, 1));
					os.flush();
					os.close();
				}
			}  else if ("1".equals(s) || "5".equals(s) || "9".equals(s) || "13".equals(s) || "17".equals(s)) {
				if (os != null) {
					os.write(ZhiLingJi.exiteStudy(selected));
					os.flush();
					os.close();
				}
			} 
		} catch(java.lang.NumberFormatException e1){
			JOptionPane.showMessageDialog((JFrame)MainUi.map.get("frame"), "请输入十六进制数！", "提示", JOptionPane.ERROR_MESSAGE);
		}
		catch (Exception e2) {
			e2.printStackTrace();
		}
	}
}
