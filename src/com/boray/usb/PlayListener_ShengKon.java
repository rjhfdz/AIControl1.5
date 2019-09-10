package com.boray.usb;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.usb.UsbPipe;

import com.boray.Data.Data;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

public class PlayListener_ShengKon implements ActionListener{
	private Thread thread;
	private int flag = 0;
	public void actionPerformed(ActionEvent e) {
		final JButton btn = (JButton)e.getSource();
		if ("停止".equals(e.getActionCommand())) {
			if (thread!=null) {
				flag = 0;
				try {
					Thread.sleep(500);
					thread.stop();
					thread = null;
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				NewJTable table_DMX = (NewJTable)MainUi.map.get("table_DMX_All_shengKon");
				table_DMX.setEnabled(true);
				JButton viewStep = (JButton)MainUi.map.get("viewStep_ShengKon");
				viewStep.setEnabled(true);
				JButton linkBtn = (JButton)MainUi.map.get("LinkBtn_ShengKon");
				linkBtn.setEnabled(true);
				/*JTabbedPane tabbedPane = (JTabbedPane)MainUi.map.get("tabbedPane");
				tabbedPane.setEnabledAt(0, true);*/
				JToggleButton xiaoGuoTgl = (JToggleButton)MainUi.map.get("xiaoGuoBtn");
				xiaoGuoTgl.setEnabled(true);
				JComboBox box = (JComboBox)MainUi.map.get("changJinBox_shengKon");
				box.setEnabled(true);
			}
		} else {
			btn.setEnabled(false);
			flag = 1;
			NewJTable table_DMX = (NewJTable)MainUi.map.get("table_DMX_All_shengKon");
			JButton linkBtn = (JButton)MainUi.map.get("LinkBtn_ShengKon");
			linkBtn.setEnabled(false);
			table_DMX.setEnabled(false);
			/*JTabbedPane tabbedPane = (JTabbedPane)MainUi.map.get("tabbedPane");
			tabbedPane.setEnabledAt(0, false);*/
			JToggleButton xiaoGuoTgl = (JToggleButton)MainUi.map.get("xiaoGuoBtn");
			xiaoGuoTgl.setEnabled(false);
			JComboBox box = (JComboBox)MainUi.map.get("changJinBox_shengKon");
			box.setEnabled(false);
			thread = new Thread(new Runnable() {
				public void run() {
					NewJTable table_DMX = (NewJTable)MainUi.map.get("table_DMX_All_shengKon");
					int check = -1;
					///////////////////20180608声控取消渐变发码/////////////////////////
					/*for (int i = 0; i < Data.checkList_shengKon.length; i++) {
						if (Data.checkList_shengKon[i]) {
							check = i;
							break;
						}
					}*/
					/////////////////////////////////////////////
					if (check == -1) {
						int slt = table_DMX.getRowCount();
						UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
						if (sendUsbPipe != null && slt != 0) {
							for (int row = 0; row < slt; row++) {
								table_DMX.setRowSelectionInterval(row, row);
							    Rectangle rect=table_DMX.getCellRect(row,0,true);
							    table_DMX.scrollRectToVisible(rect);
								byte[] buff = new byte[512];
								byte[] temp = new byte[64];
								int[] tl = new int[3];
								for (int j = 0; j < 3; j++) {
									tl[j] = Integer.valueOf(table_DMX.getValueAt(row, j).toString()).intValue();
								}
								for (int j = 0; j < 512; j++) {
									buff[j] = (byte)Integer.valueOf(table_DMX.getValueAt(row, j+3).toString()).intValue();
								}
								if (flag == 1) {
									try {
										for (int k = 0; k < 8; k++) {
											System.arraycopy(buff, k*64, temp, 0, 64);
											UsbUtil.sendMassge(sendUsbPipe, temp);
										}
										UsbUtil.sendMassge(sendUsbPipe, LastPacketData.getL(buff, tl));
									} catch(javax.usb.UsbPlatformException e2){
										/*JFrame frame = (JFrame)MainUi.map.get("frame");
										JOptionPane.showMessageDialog(frame, "数据写出发生错误！", "提示", JOptionPane.ERROR_MESSAGE);
										return;*/
									} catch (Exception e2) {
										e2.printStackTrace();
									}
								} else {
									table_DMX.setEnabled(true);
									JButton viewStep = (JButton)MainUi.map.get("viewStep_ShengKon");
									viewStep.setEnabled(true);
									JButton linkBtn = (JButton)MainUi.map.get("LinkBtn_ShengKon");
									linkBtn.setEnabled(true);
									return;
								}
								
								try {
									Thread.sleep(tl[1]+tl[2]);
								} catch (Exception e2) {
									e2.printStackTrace();
								}
							}
						}
					} else {
						int slt = table_DMX.getRowCount();
						UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
						if (sendUsbPipe != null && slt != 0) {
							List checks = new ArrayList();
							for (int i = check; i < Data.checkList_shengKon.length; i++) {
								if (Data.checkList_shengKon[i]) {
									checks.add(i);
								}
							}
							for (int row = 0; row < slt; row++) {
								table_DMX.setRowSelectionInterval(row, row);
								Rectangle rect=table_DMX.getCellRect(row,0,true);
							    table_DMX.scrollRectToVisible(rect);
								byte[] buff = new byte[512];
								byte[] temp = new byte[64];
								int[] tl = new int[3];
								for (int j = 0; j < 3; j++) {
									tl[j] = Integer.valueOf(table_DMX.getValueAt(row, j).toString()).intValue();
								}
								for (int j = 0; j < 512; j++) {
									buff[j] = (byte)Integer.valueOf(table_DMX.getValueAt(row, j+3).toString()).intValue();
								}
								
								/////////正常值
								/*for (int k = 0; k < 8; k++) {
									System.arraycopy(buff, k*64, temp, 0, 64);
									UsbUtil.sendMassge(sendUsbPipe, temp);
								}
								UsbUtil.sendMassge(sendUsbPipe, LastPacketData.getL(buff, tl));*/
								
								/////////渐变效果
								if (row < slt - 1) {
									
									if (flag == 1) {
										try {
											for (int k = 0; k < 8; k++) {
												System.arraycopy(buff, k*64, temp, 0, 64);
												UsbUtil.sendMassge(sendUsbPipe, temp);
											}
											UsbUtil.sendMassge(sendUsbPipe, LastPacketData.getL(buff, tl));
										} catch(javax.usb.UsbPlatformException e2){
											/*JFrame frame = (JFrame)MainUi.map.get("frame");
											JOptionPane.showMessageDialog(frame, "数据写出发生错误！", "提示", JOptionPane.ERROR_MESSAGE);
											return;*/
										} catch (Exception e2) {
											e2.printStackTrace();
										}
									} else {
										table_DMX.setEnabled(true);
										JButton viewStep = (JButton)MainUi.map.get("viewStep_ShengKon");
										viewStep.setEnabled(true);
										JButton linkBtn = (JButton)MainUi.map.get("LinkBtn_ShengKon");
										linkBtn.setEnabled(true);
										return;
									}
									////////////
									/*try {
										if (row == 0) {
											Thread.sleep(tl[1]);
										}
									} catch (Exception e2) {
										e2.printStackTrace();
									}*/
									//////////////////
									int JianGeTime = 50;
									if (tl[2] != 0) {
										int n = tl[2]/JianGeTime;
										int tp = 0;
										int chaZhi = 0,cnt = 0;
										Map tempMap = new HashMap();
										long start = 0,end = 0;
										double d1,d2,d3,d4;
										for (int i = 0; i < n+1; i++) {
											start = System.currentTimeMillis();
											for (int j = 0; j < checks.size(); j++) {
												tp = (int)checks.get(j);
												if (i == n) {
													buff[tp] = (byte)(Integer.valueOf(table_DMX.getValueAt(row+1, tp+3).toString()).intValue());
												} else {
													chaZhi = Integer.valueOf(table_DMX.getValueAt(row+1, tp+3).toString()).intValue() - 
															Integer.valueOf(table_DMX.getValueAt(row, tp+3).toString()).intValue();
													d1 = chaZhi;
													d2 = n;
													d3 = d1 / d2;
													buff[tp] = (byte)((int)((Integer.valueOf(table_DMX.getValueAt(row, tp+3).toString()).intValue() + Math.floor(d3 * i))));
													/*if (houCha >= 0) {
														if (n-houCha <= i) {
															if (tempMap.get(""+tp) != null) {
																cnt = (int)tempMap.get(""+tp);
															}
															++cnt;
															tempMap.put(""+tp, cnt);
															buff[tp] = (byte)(Integer.valueOf(table_DMX.getValueAt(row, tp+3).toString()).intValue() +cnt+ (chaZhi*(i)));
														} else {
															//System.out.println(Integer.valueOf(table_DMX.getValueAt(row, tp+3).toString()).intValue() + (chaZhi*(i)));
															buff[tp] = (byte)(Integer.valueOf(table_DMX.getValueAt(row, tp+3).toString()).intValue() + (chaZhi*(i)));
														}
													} else {
														if (n+houCha <= i) {
															if (tempMap.get(""+tp) != null) {
																cnt = (int)tempMap.get(""+tp);
															}
															++cnt;
															tempMap.put(""+tp, cnt);
															buff[tp] = (byte)(Integer.valueOf(table_DMX.getValueAt(row, tp+3).toString()).intValue() -cnt+ (chaZhi*(i)));
														} else {
															//System.out.println(Integer.valueOf(table_DMX.getValueAt(row, tp+3).toString()).intValue() + (chaZhi*(i)));
															buff[tp] = (byte)(Integer.valueOf(table_DMX.getValueAt(row, tp+3).toString()).intValue() + (chaZhi*(i)));
														}
													}*/
												}
											}
											if (flag == 1) {
												try {
													for (int k = 0; k < 8; k++) {
														System.arraycopy(buff, k*64, temp, 0, 64);
														UsbUtil.sendMassge(sendUsbPipe, temp);
													}
													UsbUtil.sendMassge(sendUsbPipe, LastPacketData.getL(buff, tl));
												} catch(javax.usb.UsbPlatformException e2){
													/*JFrame frame = (JFrame)MainUi.map.get("frame");
													JOptionPane.showMessageDialog(frame, "数据写出发生错误！", "提示", JOptionPane.ERROR_MESSAGE);
													return;*/
												} catch (Exception e2) {
													e2.printStackTrace();
												}
											} else {
												table_DMX.setEnabled(true);
												JButton viewStep = (JButton)MainUi.map.get("viewStep_ShengKon");
												viewStep.setEnabled(true);
												JButton linkBtn = (JButton)MainUi.map.get("LinkBtn_ShengKon");
												linkBtn.setEnabled(true);
												return;
											}
											end = System.currentTimeMillis();
											/*try {
												Thread.sleep(40);
											} catch (Exception e2) {
												e2.printStackTrace();
											}*/
											long tt = end - start;
											if (tt < JianGeTime) {
												try {
													Thread.sleep(JianGeTime - tt);
												} catch (Exception e2) {
													e2.printStackTrace();
												}
											}
										}
									}
								} else if (row == slt - 1) {
									if (flag == 1) {
										try {
											for (int k = 0; k < 8; k++) {
												System.arraycopy(buff, k*64, temp, 0, 64);
												UsbUtil.sendMassge(sendUsbPipe, temp);
											}
											UsbUtil.sendMassge(sendUsbPipe, LastPacketData.getL(buff, tl));
										} catch(javax.usb.UsbPlatformException e2){
											/*JFrame frame = (JFrame)MainUi.map.get("frame");
											JOptionPane.showMessageDialog(frame, "数据写出发生错误！", "提示", JOptionPane.ERROR_MESSAGE);
											return;*/
										} catch (Exception e2) {
											e2.printStackTrace();
										}
									} else {
										table_DMX.setEnabled(true);
										JButton viewStep = (JButton)MainUi.map.get("viewStep_ShengKon");
										viewStep.setEnabled(true);
										JButton linkBtn = (JButton)MainUi.map.get("LinkBtn_ShengKon");
										linkBtn.setEnabled(true);
										return;
									}
								}
								
								try {
									//if (row != 0) {
										if (row != slt-1) {
											Thread.sleep(tl[1]);
										}
									//}
								} catch (Exception e2) {
									e2.printStackTrace();
								}
							}
						}
					}
					btn.setEnabled(true);
					table_DMX.setEnabled(true);
					JButton linkBtn = (JButton)MainUi.map.get("LinkBtn_ShengKon");
					linkBtn.setEnabled(true);
					/*JTabbedPane tabbedPane = (JTabbedPane)MainUi.map.get("tabbedPane");
					tabbedPane.setEnabledAt(0, true);*/
					JToggleButton xiaoGuoTgl = (JToggleButton)MainUi.map.get("xiaoGuoBtn");
					xiaoGuoTgl.setEnabled(true);
					JComboBox box = (JComboBox)MainUi.map.get("changJinBox_shengKon");
					box.setEnabled(true);
				}
			});
			thread.start();
		}
	}
}
