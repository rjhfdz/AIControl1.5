package com.boray.kongTiao.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;

import javax.swing.JRadioButton;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;

public class RadioActionListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		if (Data.serialPort != null) {
			try {
				byte[] b = null;
				JRadioButton btn = (JRadioButton)e.getSource();
				if ("启用".equals(btn.getText())) {
					b = ZhiLingJi.setKongTiao(2, 1);
				} else if ("不启用".equals(btn.getText())) {
					b = ZhiLingJi.setKongTiao(2, 2);
				} else if ("取墙板".equals(btn.getText())) {
					b = ZhiLingJi.setKongTiao(4, 1);
				} else if ("取空调".equals(btn.getText())) {
					b = ZhiLingJi.setKongTiao(4, 2);
				}
				OutputStream os = Data.serialPort.getOutputStream();
				os.write(b);
				os.flush();
				os.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
