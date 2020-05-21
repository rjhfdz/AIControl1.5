package com.boray.beiFen.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.IconJDialog;
import com.boray.beiFen.UI.JingDuUI;
import com.boray.mainUi.MainUi;

public class BackupActionListener implements ActionListener{
	public static int packetCount = 0;
	public static byte[][] temp;
	public static File file;
	public static Timer timer;
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
			//e2.printStackTrace();
		}
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		if ("备份配置".equals(e.getActionCommand())) {
			fileChooser.setSelectedFile(new File("config.bry"));
		} else if ("备份DMX".equals(e.getActionCommand())) {
			fileChooser.setSelectedFile(new File("DMX.dat"));
		}
		int returnVal = fileChooser.showSaveDialog((JFrame)MainUi.map.get("frame"));
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			file = fileChooser.getSelectedFile();
		}
		if (file == null) {
			return;
		}
		if ("备份配置".equals(e.getActionCommand())) {
			getPacketCnt(16);
		} else if ("备份DMX".equals(e.getActionCommand())) {
			getPacketCnt(32);
		}
	}
	
	public static void fileWrite(){
		try {
			FileOutputStream os = new FileOutputStream(file);
			for (int i = 0; i < temp.length; i++) {
				os.write(temp[i]);
			}
			os.flush();
			os.close();
			IconJDialog dialog = (IconJDialog)MainUi.map.get("JingDuDialog");
			dialog.dispose();
			BackupActionListener.temp = null;
			JFrame frame = (JFrame)MainUi.map.get("frame");
			JOptionPane.showMessageDialog(frame, "备份完成！", "提示", JOptionPane.PLAIN_MESSAGE);
			return;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void reSend(final int PcktNum,final int type8){
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				try {
					OutputStream os = Data.serialPort.getOutputStream();
					os.write(ZhiLingJi.getDataByPacketN(PcktNum,type8));
					os.flush();
					timer = new Timer();
					timer.schedule(new TimerTask() {
						public void run() {
							try {
								OutputStream os = Data.serialPort.getOutputStream();
								os.write(ZhiLingJi.getDataByPacketN(PcktNum,type8));
								os.flush();
								timer = new Timer();
								timer.schedule(new TimerTask() {
									public void run() {
										try {
											OutputStream os = Data.serialPort.getOutputStream();
											os.write(ZhiLingJi.getDataByPacketN(PcktNum,type8));
											os.flush();
											timer = new Timer();
											timer.schedule(new TimerTask() {
												public void run() {
													try {
														OutputStream os = Data.serialPort.getOutputStream();
														os.write(ZhiLingJi.getDataByPacketN(PcktNum,type8));
														os.flush();
														new Timer().schedule(new TimerTask() {
															public void run() {
																JDialog dialog = (JDialog)MainUi.map.get("JingDuDialog");
																dialog.dispose();
																JFrame frame = (JFrame)MainUi.map.get("frame");
																JOptionPane.showMessageDialog(frame, "备份数据失败！", "提示", JOptionPane.ERROR_MESSAGE);
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
		}, 2000);
	}

	private void getPacketCnt(final int type8) {
		try {
			packetCount = 0;
			OutputStream os = Data.serialPort.getOutputStream();
			os.write(ZhiLingJi.queryPketCount(type8));
			os.flush();
			new Timer().schedule(new TimerTask() {
				public void run() {
					if (packetCount == 0) {
						try {
							OutputStream os = Data.serialPort.getOutputStream();
							os.write(ZhiLingJi.queryPketCount(type8));
							os.flush();
							new Timer().schedule(new TimerTask() {
								public void run() {
									if (packetCount == 0) {
										try {
											OutputStream os = Data.serialPort.getOutputStream();
											os.write(ZhiLingJi.queryPketCount(type8));
											os.flush();
											new Timer().schedule(new TimerTask() {
												public void run() {
													if (packetCount == 0) {
														JFrame frame = (JFrame)MainUi.map.get("frame");
														JOptionPane.showMessageDialog(frame, "备份数据失败！", "提示", JOptionPane.ERROR_MESSAGE);
													} else {
														new Thread(new Runnable() {
															public void run() {
																new JingDuUI().show();
															}
														}).start();
														try {
															OutputStream os = Data.serialPort.getOutputStream();
															os.write(ZhiLingJi.getDataByPacketN(1,type8));
															os.flush();
															reSend(1,type8);
														} catch (Exception e) {
															e.printStackTrace();
														}
													}
												}
											}, 2000);
										} catch (Exception e) {
											e.printStackTrace();
										}
									} else {
										new Thread(new Runnable() {
											public void run() {
												new JingDuUI().show();
											}
										}).start();
										try {
											OutputStream os = Data.serialPort.getOutputStream();
											os.write(ZhiLingJi.getDataByPacketN(1,type8));
											os.flush();
											reSend(1,type8);
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
							}, 2000);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						new Thread(new Runnable() {
							public void run() {
								new JingDuUI().show();
							}
						}).start();
						try {
							OutputStream os = Data.serialPort.getOutputStream();
							os.write(ZhiLingJi.getDataByPacketN(1,type8));
							os.flush();
							reSend(1,type8);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}, 800);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
}
