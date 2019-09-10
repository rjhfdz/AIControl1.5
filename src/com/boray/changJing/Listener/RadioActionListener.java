package com.boray.changJing.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;

import com.boray.Data.Data;

public class RadioActionListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		if (Data.serialPort != null) {
			try {
				OutputStream os = Data.serialPort.getOutputStream();
				os.write(new DengJuKaiGuangItemListener().code());
				os.flush();
				os.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
