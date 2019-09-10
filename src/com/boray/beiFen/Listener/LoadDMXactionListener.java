package com.boray.beiFen.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.beiFen.UI.JingDuUI;
import com.boray.mainUi.MainUi;

public class LoadDMXactionListener implements ActionListener{
	public static Timer timer;
	public static byte[][] temp;
	private static int type;
	public static int pktCnt = 0;
	public void actionPerformed(ActionEvent e) {
		if (Data.serialPort == null) {
			JFrame frame = (JFrame)MainUi.map.get("frame");
			JOptionPane.showMessageDialog(frame, "请先连接设备！", "提示", JOptionPane.ERROR_MESSAGE);
			return;
		}
		JFileChooser fileChooser = new JFileChooser();
		try {
			String path = getClass().getResource("/SD卡文件/").getPath().substring(1);
			path = URLDecoder.decode(path,"utf-8"); 
			fileChooser.setCurrentDirectory(new File(path));
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		String[] houZhui = {"dat"};
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.dat", houZhui);
		fileChooser.setFileFilter(filter);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fileChooser.showOpenDialog((JFrame)MainUi.map.get("frame"));
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				sendCodeToDevice(file);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	

	private void sendCodeToDevice(File file) {
		if (file.length() <= 57344) {
			return;
		}
		try {
			//FileInputStream is = new FileInputStream(file);
			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
			randomAccessFile.seek(57344);
			int length = (int)file.length() - 57344;
			if (length%512 == 0) {
				pktCnt = (int)(length/512);
			} else {
				pktCnt = (int)(length/512);
				pktCnt = pktCnt + 1;
			}
			temp = new byte[pktCnt][512];
			for (int i = 0; i < pktCnt; i++) {
				randomAccessFile.read(temp[i]);
			}
			randomAccessFile.close();
			type = 32;
			reSend(1);
			new Thread(new Runnable() {
				public void run() {
					new JingDuUI().show();
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void reSend(final int PcktNum){
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					OutputStream os = Data.serialPort.getOutputStream();
					os.write(ZhiLingJi.packetData(temp[PcktNum-1], type, PcktNum));
					os.flush();
					timer = new Timer();
					timer.schedule(new TimerTask() {
						public void run() {
							try {
								OutputStream os = Data.serialPort.getOutputStream();
								os.write(ZhiLingJi.packetData(temp[PcktNum-1], type, PcktNum));
								os.flush();
								timer = new Timer();
								timer.schedule(new TimerTask() {
									public void run() {
										try {
											OutputStream os = Data.serialPort.getOutputStream();
											os.write(ZhiLingJi.packetData(temp[PcktNum-1], type, PcktNum));
											os.flush();
											timer = new Timer();
											timer.schedule(new TimerTask() {
												public void run() {
													try {
														OutputStream os = Data.serialPort.getOutputStream();
														os.write(ZhiLingJi.packetData(temp[PcktNum-1], type, PcktNum));
														os.flush();
														new Timer().schedule(new TimerTask() {
															public void run() {
																JDialog dialog = (JDialog)MainUi.map.get("JingDuDialog");
																dialog.dispose();
																JFrame frame = (JFrame)MainUi.map.get("frame");
																JOptionPane.showMessageDialog(frame, "加载数据失败！", "提示", JOptionPane.ERROR_MESSAGE);
															}
														}, 2000);
													} catch (Exception e) {
														e.printStackTrace();
													}
												}
											}, 2000);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}, 2000);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}, 2000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0);
	}
}
