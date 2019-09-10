package com.boray.dengKu.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.usb.UsbPipe;

import com.boray.Data.Data;
import com.boray.Data.RdmData;
import com.boray.mainUi.MainUi;
import com.boray.usb.UsbUtil;

public class RdmSetUI implements ItemListener{
	private String uid,devType;
	private JComboBox box4,box5,box6,box7;
	public void show(String uid,String devType) {
		this.uid = uid;
		this.devType = devType;
		JFrame f = (JFrame)MainUi.map.get("frame");
		JDialog dialog = new JDialog(f,true);
		dialog.setResizable(false);
		dialog.setTitle("RDM配置");
		int w = 760,h = 460;
		dialog.setLocation(f.getLocation().x+f.getSize().width/2-w/2,f.getLocation().y+f.getSize().height/2-h/2);
		dialog.setSize(w,h);
		dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				RdmPaneUI.openSet = false;
				new Thread(new Runnable() {
					public void run() {
						try {
							//UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
							//UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType((byte[])RdmPaneUI.currentByte, 1));
							OutputStream os = Data.serialPort.getOutputStream();
							os.write(RdmData.serchType((byte[])RdmPaneUI.currentByte, 1));
							os.flush();
							Thread.sleep(200);
							byte[] b = new byte[2];
							b[0] = 1;
							b[1] = 0;
							//UsbUtil.sendMassge(sendUsbPipe, RdmData.setType(RdmPaneUI.currentByte, 6,b));
							os.write(RdmData.setType(RdmPaneUI.currentByte, 6,b));
							os.flush();
						} catch (Exception e2) {
							e2.printStackTrace();
						}
					}
				}).start();
			}
		});
		init(dialog);
		dialog.setVisible(true);
	}

	private void init(JDialog dialog) {
		JPanel p1 = new JPanel();
		p1.setPreferredSize(new Dimension(400,420));
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "终端设备参数表", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p1.setBorder(tb);
		setP1(p1);
		
		JPanel p2 = new JPanel();
		p2.setPreferredSize(new Dimension(330,420));
		TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "灯具参数设置", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p2.setBorder(tb2);
		setP2(p2);
		
		dialog.add(p1);
		dialog.add(p2);
	}

	private void setP2(JPanel p2) {
		JPanel pane = new JPanel();
		pane.setPreferredSize(new Dimension(250,380));
		//pane.setBorder(new LineBorder(Color.gray));
		FlowLayout flowLayout3 = new FlowLayout(FlowLayout.RIGHT);
		flowLayout3.setVgap(8);
		pane.setLayout(flowLayout3);
		pane.add(new JLabel("工作模式:"));
		String[] s = {"","快速自走模式","声控模式","单机模式","从机模式","慢速自走模式"};
		final JComboBox box = new JComboBox(s);
		box.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange() && box.getSelectedIndex() != 0) {
					try {
						//UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
						OutputStream os = Data.serialPort.getOutputStream();
						
						byte[] b = new byte[2];
						b[0] = 1;
						b[1] = (byte)(box.getSelectedIndex() + 80);
						
						//UsbUtil.sendMassge(sendUsbPipe, RdmData.setType(RdmPaneUI.currentByte, 7,b));
						os.write(RdmData.setType(RdmPaneUI.currentByte, 7,b));
						os.flush();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		box.setPreferredSize(new Dimension(150,32));
		pane.add(box);
		
		pane.add(new JLabel("正反显示:"));
		String[] s1 = {"","正向显示","反向显示"};
		final JComboBox box2 = new JComboBox(s1);
		box2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					try {
						//UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
						OutputStream os = Data.serialPort.getOutputStream();
						byte[] b = new byte[2];
						b[0] = 1;
						b[1] = (byte)(box2.getSelectedIndex()-1);
						
						//UsbUtil.sendMassge(sendUsbPipe, RdmData.setType(RdmPaneUI.currentByte, 8,b));
						os.write(RdmData.setType(RdmPaneUI.currentByte, 8,b));
						os.flush();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		MainUi.map.put("RdmSet_zfShow_box", box2);
		box2.setPreferredSize(new Dimension(150,32));
		pane.add(box2);
		
		pane.add(new JLabel("复位形式:"));
		String[] s2 = {"冷复位","复位"};
		final JComboBox box3 = new JComboBox(s2);
		box3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					try {
						//UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
						OutputStream os = Data.serialPort.getOutputStream();
						byte[] b = new byte[2];
						b[0] = 1;
						if (box3.getSelectedIndex() == 0) {
							b[1] = (byte)0xFF;
						} else {
							b[1] = (byte)0x01;
						}
						
						//UsbUtil.sendMassge(sendUsbPipe, RdmData.setType(RdmPaneUI.currentByte, 9,b));
						os.write(RdmData.setType(RdmPaneUI.currentByte, 9,b));
						os.flush();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		box3.setPreferredSize(new Dimension(150,32));
		pane.add(box3);
		
		pane.add(new JLabel("工厂默认值:"));
		JButton button = new JButton("恢复出厂设置");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
					OutputStream os = Data.serialPort.getOutputStream();
					byte[] b = new byte[2];
					b[0] = 1;b[1] = 1;
					//UsbUtil.sendMassge(sendUsbPipe, RdmData.setType(RdmPaneUI.currentByte, 10, b));
					os.write(RdmData.setType(RdmPaneUI.currentByte, 10, b));
					os.flush();
					new Thread(new Runnable() {
						public void run() {
							try {
								//UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
								OutputStream os = Data.serialPort.getOutputStream();
								//查信息
								Thread.sleep(200);
								//UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 1));
								os.write(RdmData.serchType(RdmPaneUI.currentByte, 1));
								os.flush();
								Thread.sleep(200);
								//查工作模式
								//UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 7));
								//Thread.sleep(200);
								
								//正反显示
								//UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 8));
								os.write(RdmData.serchType(RdmPaneUI.currentByte, 8));
								os.flush();
								Thread.sleep(200);
								
								//复位
								//UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 9));
								//Thread.sleep(200);
								
								//电动模式
								//UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 11));
								os.write(RdmData.serchType(RdmPaneUI.currentByte, 11));
								os.flush();
								Thread.sleep(200);
								
								//通道模式
								//UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 12));
								os.write(RdmData.serchType(RdmPaneUI.currentByte, 12));
								os.flush();
								Thread.sleep(200);
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		button.setPreferredSize(new Dimension(150,32));
		pane.add(button);
		
		JPanel centerPane = new JPanel();
		FlowLayout flowLayout = new FlowLayout(FlowLayout.RIGHT);
		flowLayout.setVgap(-2);
		flowLayout.setHgap(-2);
		centerPane.setLayout(flowLayout);
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "电机模式", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		centerPane.setBorder(tb);
		centerPane.setPreferredSize(new Dimension(224,90));
		centerPane.add(new JLabel("X轴:"));
		box4 = new JComboBox(new String[]{"","正转","反转"});
		box4.setPreferredSize(new Dimension(76,32));
		box5 = new JComboBox(new String[]{"","540°","360°","180°"});
		box5.setPreferredSize(new Dimension(76,32));
		centerPane.add(box4);centerPane.add(box5);
		centerPane.add(new JLabel("     Y轴:"));
		box6 = new JComboBox(new String[]{"","正转","反转"});
		box6.setPreferredSize(new Dimension(76,32));
		box7 = new JComboBox(new String[]{"","270°","180°","90°"});
		box4.addItemListener(this);box5.addItemListener(this);
		box6.addItemListener(this);box7.addItemListener(this);
		List list = new ArrayList();
		list.add(box4);list.add(box5);
		list.add(box6);list.add(box7);
		MainUi.map.put("DianJiComponet_list", list);
		box7.setPreferredSize(new Dimension(76,32));
		centerPane.add(box6);centerPane.add(box7);
		pane.add(centerPane);
		
		pane.add(new JLabel("通道模式:"));
		final JComboBox box8 = new JComboBox();
		MainUi.map.put("RdmSet_channelModel_box8", box8);
		box8.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					try {
						//UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
						OutputStream os = Data.serialPort.getOutputStream();
						byte[] b = new byte[2];
						b[0] = 1;
						b[1] = (byte)(box8.getSelectedIndex());
						
						//UsbUtil.sendMassge(sendUsbPipe, RdmData.setType(RdmPaneUI.currentByte, 12,b));
						os.write(RdmData.setType(RdmPaneUI.currentByte, 12,b));
						os.flush();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		box8.setPreferredSize(new Dimension(150,32));
		box8.addItem("");
		for (int i = 1; i < 101; i++) {
			box8.addItem(""+i);
		}
		pane.add(box8);
		
		JButton button2 = new JButton("刷新");
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						try {
							//UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
							OutputStream os = Data.serialPort.getOutputStream();
							//查信息
							//UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 1));
							os.write(RdmData.serchType(RdmPaneUI.currentByte, 1));
							os.flush();
							Thread.sleep(200);
							//查工作模式
							//UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 7));
							//Thread.sleep(200);
							
							//正反显示
							//UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 8));
							os.write(RdmData.serchType(RdmPaneUI.currentByte, 8));
							os.flush();
							Thread.sleep(200);
							
							//复位
							//UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 9));
							//Thread.sleep(200);
							
							//电动模式
							//UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 11));
							os.write(RdmData.serchType(RdmPaneUI.currentByte, 11));
							os.flush();
							Thread.sleep(200);
							
							//通道模式
							//UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 12));
							os.write(RdmData.serchType(RdmPaneUI.currentByte, 12));
							os.flush();
							Thread.sleep(200);
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		});
		button2.setPreferredSize(new Dimension(150,32));
		pane.add(button2);
		
		p2.add(pane);
	}

	private void setP1(JPanel p1) {
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
		flowLayout.setVgap(-8);
		p1.setLayout(flowLayout);
		JPanel leftPane = new JPanel();
		//leftPane.setBorder(new LineBorder(Color.gray));
		leftPane.setPreferredSize(new Dimension(280,350));
		FlowLayout flowLayout2 = new FlowLayout(FlowLayout.RIGHT);
		flowLayout2.setVgap(-2);
		leftPane.setLayout(flowLayout2);
		
		JPanel headPane = new JPanel();
		headPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		headPane.setPreferredSize(new Dimension(350,34));
		//headPane.setBorder(new LineBorder(Color.gray));
		
		headPane.add(new JLabel("             UID:"));
		if (uid.equals("")) {
			uid = "00-00-00-00-00-00";
		}
		JLabel uidLabe = new JLabel(uid);
		//uidLabe.setPreferredSize(new Dimension(180,34));
		headPane.add(new JLabel("        "));
		headPane.add(uidLabe);
		p1.add(headPane);
		
		final JTextField[] fields = new JTextField[10];
		MainUi.map.put("RdmSet_fields", fields);
		for (int i = 0; i < fields.length; i++) {
			fields[i] = new JTextField();
			fields[i].setEnabled(false);
			fields[i].setPreferredSize(new Dimension(150,32));
		}
		leftPane.add(new JLabel("设备型号:"));
		fields[0].setText(devType);
		leftPane.add(fields[0]);
		leftPane.add(new JLabel("RDM协议版本号:"));
		leftPane.add(fields[1]);
		leftPane.add(new JLabel("设备模型ID:"));
		leftPane.add(fields[2]);
		leftPane.add(new JLabel("产品分类:"));
		leftPane.add(fields[3]);
		leftPane.add(new JLabel("软件版本ID:"));
		leftPane.add(fields[4]);
		leftPane.add(new JLabel("设备通道数:"));
		leftPane.add(fields[5]);
		leftPane.add(new JLabel("DMX512特性:"));
		leftPane.add(fields[6]);
		leftPane.add(new JLabel("DMX512起始地址:"));
		leftPane.add(fields[7]);
		leftPane.add(new JLabel("从设备数量:"));
		leftPane.add(fields[8]);
		leftPane.add(new JLabel("传感器数量:"));
		leftPane.add(fields[9]);
		leftPane.add(new JLabel("设备状态:"));
		String[] s = {"开","关"};
		final JComboBox box = new JComboBox(s);
		box.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					try {
						//UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
						OutputStream os = Data.serialPort.getOutputStream();
						byte[] b = new byte[2];
						b[0] = 1;
						if (box.getSelectedIndex() == 0) {
							b[1] = 1;
						} else {
							b[1] = 0;
						}
						//UsbUtil.sendMassge(sendUsbPipe, RdmData.setType(RdmPaneUI.currentByte, 6,b));
						os.write(RdmData.setType(RdmPaneUI.currentByte, 6,b));
						os.flush();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		//MainUi.map.put("RdmSet_box", box);
		box.setFocusable(false);
		box.setPreferredSize(new Dimension(150,32));
		leftPane.add(box);
		
		JPanel rightPane = new JPanel();
		FlowLayout flowLayout3 = new FlowLayout(FlowLayout.LEFT);
		flowLayout3.setHgap(-2);
		rightPane.setLayout(flowLayout3);
		//rightPane.setBorder(new LineBorder(Color.gray));
		rightPane.setPreferredSize(new Dimension(80,370));
		JPanel N1 = new JPanel();
		N1.setPreferredSize(new Dimension(52,210));
		rightPane.add(N1);
		final JButton button = new JButton("编辑");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("编辑")) {
					button.setText("写入");
					fields[7].setEnabled(true);
				} else {
					try {
						int a = Integer.valueOf(fields[7].getText()).intValue();
						if (a > 512) {
							JFrame frame = (JFrame)MainUi.map.get("frame");
							JOptionPane.showMessageDialog(frame, "起始地址范围1 - 512！", "提示", JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						//UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
						OutputStream os = Data.serialPort.getOutputStream();
						byte[] b = new byte[3];
						b[0] = 2;
						b[1] = (byte)(a / 256);
						b[2] = (byte)(a % 256);
						//UsbUtil.sendMassge(sendUsbPipe, RdmData.setType(RdmPaneUI.currentByte, 3,b));
						os.write(RdmData.setType(RdmPaneUI.currentByte, 3,b));
						os.flush();
						
						button.setText("编辑");
						fields[7].setEnabled(false);
					} catch(java.lang.NumberFormatException e3){
						JFrame frame = (JFrame)MainUi.map.get("frame");
						JOptionPane.showMessageDialog(frame, "起始地址范围1 - 512！", "提示", JOptionPane.ERROR_MESSAGE);
						return;
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		});
		button.setFocusable(false);
		button.setPreferredSize(new Dimension(70,32));
		rightPane.add(button);
		
		p1.add(leftPane);
		p1.add(rightPane);
	}
	
	public void itemStateChanged(ItemEvent e) {
		if (ItemEvent.SELECTED == e.getStateChange() && box4.getSelectedIndex() != 0
				&& box5.getSelectedIndex() != 0 && box6.getSelectedIndex() != 0
				&& box7.getSelectedIndex() != 0) {
			int cc = 0;
			//X轴
			if (box4.getSelectedIndex() == 1) {
				cc = 0;
			} else {
				cc = 1;
			}
			
			//Y轴
			if (box6.getSelectedIndex() == 2) {
				cc = cc + 2;
			}
			
			//X轴0 表示最大转向为540度，1表示最大转向为360度，2表示最大转向为180度。
			if (box5.getSelectedIndex() == 2) {
				cc = cc + 4;
			} else if (box5.getSelectedIndex() == 3) {
				cc = cc + 8;
			}
			
			//位4-位5：表示Y轴电机最大的转向，0表示最大转向为270度，1表示最大转向为180度，2表示最大转向为90度。
			if (box7.getSelectedIndex() == 2) {
				cc = cc + 16;
			} else if (box7.getSelectedIndex() == 3) {
				cc = cc + 32;
			}
			try {
				//UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
				OutputStream os = Data.serialPort.getOutputStream();
				byte[] b = new byte[2];
				b[0] = 1;
				b[1] = (byte)cc;
				//UsbUtil.sendMassge(sendUsbPipe, RdmData.setType(RdmPaneUI.currentByte, 11,b));
				os.write(RdmData.setType(RdmPaneUI.currentByte, 11,b));
				os.flush();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
