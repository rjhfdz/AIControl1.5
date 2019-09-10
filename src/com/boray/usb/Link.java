package com.boray.usb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.usb.UsbInterface;
import javax.usb.UsbPipe;

import com.boray.mainUi.MainUi;

public class Link implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		//final JButton btn = (JButton)MainUi.map.get("LinkBtn_ChanJin");
		//final JButton skButton = (JButton)MainUi.map.get("LinkBtn_ShengKon");
		final JButton button3 = (JButton)MainUi.map.get("USBLink_head");
		if ("����".equals(e.getActionCommand())) {
			try {
				//UsbUtil.LINK = true;
				UsbPipe sendUsbPipe = new UsbUtil().useUsb();
				if (sendUsbPipe != null) {
					//btn.setText("�Ͽ�");
					//skButton.setText("�Ͽ�");
					button3.setText("�Ͽ�");
					MainUi.map.put("sendUsbPipe", sendUsbPipe);
				} else {
					JFrame frame = (JFrame)MainUi.map.get("frame");
		        	JOptionPane.showMessageDialog(frame, "δ�����豸��", "��ʾ", JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		} else {
			//btn.setEnabled(false);
			//skButton.setEnabled(false);
			button3.setEnabled(false);
			new Thread(new Runnable() {
				public void run() {
					UsbInterface iface = (UsbInterface)MainUi.map.get("iface");
					UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
					UsbPipe receivedUsbPipe = (UsbPipe)MainUi.map.get("receivedUsbPipe");
					//UsbUtil.LINK = false;
					try {
						sendUsbPipe.close();
						MainUi.map.put("sendUsbPipe", null);
						UsbUtil.thread.stop();
						if (receivedUsbPipe.isActive()) {
							try {
								receivedUsbPipe.abortAllSubmissions();
								receivedUsbPipe.close();
							} catch (Exception e2) {
								e2.printStackTrace();
							}
						}
						iface.release();
						
						//btn.setText("����");
						//skButton.setText("����");
						button3.setText("����");
						//btn.setEnabled(true);
						//skButton.setEnabled(true);
						button3.setEnabled(true);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}).start();
		}
	}
}
