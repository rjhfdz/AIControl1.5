package com.boray.usb;

import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.usb.UsbConfiguration;
import javax.usb.UsbDevice;
import javax.usb.UsbDeviceDescriptor;
import javax.usb.UsbEndpoint;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.UsbInterface;
import javax.usb.UsbPipe;

import com.boray.Data.RdmData;
import com.boray.dengKu.UI.NewJTable;
import com.boray.dengKu.UI.RdmPaneUI;
import com.boray.mainUi.MainUi;

public class UsbUtil {
	private static short idVendor = (short)0x8888;
	private static short idProduct = (short)0x0006;
	public static boolean LINK = true;
	public static Thread thread = null;
	/*public static void main(String[] args) {
		try {
			UsbPipe sendUsbPipe = new UsbUtil().useUsb();
			
			byte[] buff = new byte[64];
			for (int i = 0; i < 9; i++) {
				buff[i] = (byte)i;
				sendMassge(sendUsbPipe, buff);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	public UsbPipe useUsb() throws Exception{
		UsbInterface iface = linkDevice();
		if (iface == null) {
			return null;
		}
		UsbEndpoint receivedUsbEndpoint,sendUsbEndpoint;
		
		sendUsbEndpoint = (UsbEndpoint)iface.getUsbEndpoints().get(0);
		if (!sendUsbEndpoint.getUsbEndpointDescriptor().toString().contains("OUT")) {
			receivedUsbEndpoint = sendUsbEndpoint;
			sendUsbEndpoint = (UsbEndpoint)iface.getUsbEndpoints().get(1);
		} else {
			receivedUsbEndpoint = (UsbEndpoint)iface.getUsbEndpoints().get(1);
		}
		
		//发送：
		final UsbPipe sendUsbPipe =  sendUsbEndpoint.getUsbPipe();
		sendUsbPipe.open();
		
		//接收
		final UsbPipe receivedUsbPipe =  receivedUsbEndpoint.getUsbPipe();
		MainUi.map.put("receivedUsbPipe", receivedUsbPipe);
		receivedUsbPipe.open();
		
		new Thread(new Runnable() {
			public void run() {
				try {
					receivedMassge(receivedUsbPipe);
				} catch(javax.usb.UsbPlatformException e){
					try {
						sendUsbPipe.close();
						receivedUsbPipe.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					JButton linkBtn = (JButton)MainUi.map.get("LinkBtn_ChanJin");
					linkBtn.setText("连接");
					JFrame frame = (JFrame)MainUi.map.get("frame");
					JOptionPane.showMessageDialog(frame, "设备已断开！", "提示", JOptionPane.ERROR_MESSAGE);
				} catch (Exception e) {
					//System.out.println("thread");
					//e.printStackTrace();
				}
			}
		}).start();
		return sendUsbPipe;
	}
	
	public UsbInterface linkDevice() throws Exception{

    	UsbDevice device = findDevice(UsbHostManager.getUsbServices().getRootUsbHub());
        if (device == null) {
            return null;
        }
    	UsbConfiguration configuration = device.getActiveUsbConfiguration();
    	UsbInterface iface = null;
		if (configuration != null && configuration.getUsbInterfaces().size() > 0) {
			iface = (UsbInterface)configuration.getUsbInterfaces().get(0);
		} else {
			return null;
		}
		iface.claim();
		MainUi.map.put("iface",iface);
		return iface;
	}
	
	public void receivedMassge(final UsbPipe usbPipe) throws Exception{
		thread = new Thread(new Runnable() {
			public void run() {
				try {
					byte[] b = new byte[64];
					int length = 0;
					String hex0 = "",uidTemp = "";
					String[] a = new String[6];
					while (LINK) {
						length = usbPipe.syncSubmit(b);//阻塞
						hex0 = Integer.toHexString(b[9] & 0xFF);
						if ("41".equals(hex0)) {//设备数量
							RdmPaneUI.deviceCount = Byte.toUnsignedInt(b[17])*256 + Byte.toUnsignedInt(b[18]);
						} else if ("11".equals(hex0)) {//查UID
							uidTemp = "";
							byte[] uidByte = new byte[6];
							for (int i = 0; i < a.length; i++) {
								uidByte[i] = b[10+i];
								a[i] = Integer.toHexString(b[10+i] & 0xFF).toUpperCase();
								if (a[i].length()==1) {
									a[i] = "0" + a[i];
								}
								if (i == 0) {
									uidTemp = uidTemp + a[i];
								} else {
									uidTemp = uidTemp + "-" + a[i];
								}
							}
							NewJTable table = (NewJTable)MainUi.map.get("RDM_table");
							DefaultTableModel model = (DefaultTableModel)table.getModel();
							String[] s = {String.valueOf(table.getRowCount()+1),uidTemp,
									"","","","","","进入高级设置"};
							model.addRow(s);
							RdmPaneUI.uid_Byte.add(uidByte);
							RdmPaneUI.uidList.add(uidTemp);
							if (RdmPaneUI.uidList.size() == RdmPaneUI.deviceCount) {
								new Thread(new Runnable() {
									public void run() {
										try {
											UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
											//查型号
											for (int j = 0; j < RdmPaneUI.deviceCount; j++) {
												UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType((byte[])RdmPaneUI.uid_Byte.get(j), 5));
												Thread.sleep(200);
											}
											//查起始地址、通道数
											for (int j = 0; j < RdmPaneUI.deviceCount; j++) {
												UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType((byte[])RdmPaneUI.uid_Byte.get(j), 1));
												Thread.sleep(200);
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
									}
								}).start();
							}
						} else if ("21".equals(hex0)) {
							hex0 = Integer.toHexString(b[8] & 0xFF);
							if ("5".equals(hex0)) {//查型号
								uidTemp = "";
								for (int i = 0; i < a.length; i++) {
									a[i] = Integer.toHexString(b[10+i] & 0xFF).toUpperCase();
									if (a[i].length()==1) {
										a[i] = "0" + a[i];
									}
									if (i == 0) {
										uidTemp = uidTemp + a[i];
									} else {
										uidTemp = uidTemp + "-" + a[i];
									}
								}
								NewJTable table = (NewJTable)MainUi.map.get("RDM_table");
								for (int i = 0; i < table.getRowCount(); i++) {
									if (table.getValueAt(i, 1).toString().equals(uidTemp)) {
										table.setValueAt(new String(b,23,12), i, 3);
										break;
									}
								}
							} else if ("1".equals(hex0)) {//查起始地址、通道数
								if (RdmPaneUI.openSet) {
									
									JTextField[] fields = (JTextField[])MainUi.map.get("RdmSet_fields");
									if (fields != null) {
										fields[1].setText(Byte.toUnsignedInt(b[17])+"."+Byte.toUnsignedInt(b[18]));//RDM协议版本号
										fields[2].setText(Byte.toUnsignedInt(b[19])+""+Byte.toUnsignedInt(b[20]));//设备模型ID
										fields[3].setText(Byte.toUnsignedInt(b[21])+""+Byte.toUnsignedInt(b[22]));//产品分类
										fields[4].setText(Byte.toUnsignedInt(b[23])+","+Byte.toUnsignedInt(b[24])
												+","+Byte.toUnsignedInt(b[25])+","+Byte.toUnsignedInt(b[26]));//软件版本ID
										fields[5].setText(""+Byte.toUnsignedInt(b[28]));//设备通道数
										fields[6].setText(Byte.toUnsignedInt(b[29])+""+Byte.toUnsignedInt(b[30]));//DMX512特性
										fields[7].setText((Byte.toUnsignedInt(b[31])*256+Byte.toUnsignedInt(b[32]))+"");//DMX512起始地址
										fields[8].setText((Byte.toUnsignedInt(b[33])*256+Byte.toUnsignedInt(b[34]))+"");//从设备数量
										fields[9].setText(""+Byte.toUnsignedInt(b[35]));//传感器数量
									}
									//设备状态查询
									//UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
									//UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 6));
									new Thread(new Runnable() {
										public void run() {
											try {
												UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
												//查工作模式
												//UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 7));
												//Thread.sleep(200);
												
												//正反显示
												UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 8));
												Thread.sleep(200);
												
												//复位
												//UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 9));
												//Thread.sleep(200);
												
												//电动模式
												UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 11));
												Thread.sleep(200);
												
												//通道模式
												UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 12));
												Thread.sleep(200);
												
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									}).start();
								} else {
									uidTemp = "";
									for (int i = 0; i < a.length; i++) {
										a[i] = Integer.toHexString(b[10+i] & 0xFF).toUpperCase();
										if (a[i].length()==1) {
											a[i] = "0" + a[i];
										}
										if (i == 0) {
											uidTemp = uidTemp + a[i];
										} else {
											uidTemp = uidTemp + "-" + a[i];
										}
									}
									String[] s = new String[2];
									int address = Byte.toUnsignedInt(b[28]);//起始地址
									int channels = Byte.toUnsignedInt(b[31])*256+Byte.toUnsignedInt(b[32]);//通道数
									s[0] = address+"";s[1] = channels+"";
									
									NewJTable table = (NewJTable)MainUi.map.get("RDM_table");
									for (int i = 0; i < table.getRowCount(); i++) {
										if (table.getValueAt(i, 1).toString().equals(uidTemp)) {
											table.setValueAt(s[1], i, 4);
											table.setValueAt(s[0], i, 5);
											break;
										}
									}
									//RdmPaneUI.addAndChannelMap.put(uidTemp, s);
								}
							} else if ("8".equals(hex0)) {//正反显示
								JComboBox box2 = (JComboBox)MainUi.map.get("RdmSet_zfShow_box");
								ItemListener listener = box2.getItemListeners()[0];
								box2.removeItemListener(listener);
								box2.setSelectedIndex(Byte.toUnsignedInt(b[17])+1);
								box2.addItemListener(listener);
							}else if ("c".equals(hex0)) {//通道模式
								JComboBox box8 = (JComboBox)MainUi.map.get("RdmSet_channelModel_box8");
								ItemListener listener = box8.getItemListeners()[0];
								box8.removeItemListener(listener);
								box8.setSelectedIndex(Byte.toUnsignedInt(b[17]));
								box8.addItemListener(listener);
								if (box8 != null) {
									
								}
							} else if ("b".equals(hex0)) {//电动模式
								int cc = Byte.toUnsignedInt(b[17]);
								int tp = cc;
								List list = (List)MainUi.map.get("DianJiComponet_list");
								JComboBox box = null;
								ItemListener listener = null;
								//X轴
								box = (JComboBox)list.get(0);
								listener = box.getItemListeners()[0];
								box.removeItemListener(listener);
								tp = tp % 2;
								box.setSelectedIndex(tp+1);
								box.addItemListener(listener);
								
								//Y轴
								box = (JComboBox)list.get(2);
								box.removeItemListener(listener);
								tp = cc / 2;
								tp = tp % 2;
								box.setSelectedIndex(tp+1);
								box.addItemListener(listener);
								
								//X轴角度
								box = (JComboBox)list.get(1);
								box.removeItemListener(listener);
								tp = cc / 4;
								tp = tp % 4;
								box.setSelectedIndex(tp+1);
								box.addItemListener(listener);
								
								//Y轴角度
								box = (JComboBox)list.get(3);
								box.removeItemListener(listener);
								tp = cc/16;
								box.setSelectedIndex(tp+1);
								box.addItemListener(listener);
								
							}
						}
					}
				} catch(javax.usb.UsbPlatformException e){
					thread = null;
					MainUi.map.put("sendUsbPipe", null);
					MainUi.map.put("receivedUsbPipe", null);
					//JButton linkBtn = (JButton)MainUi.map.get("LinkBtn_ChanJin");
					//JButton linkBtn2 = (JButton)MainUi.map.get("LinkBtn_ShengKon");
					JButton button3 = (JButton)MainUi.map.get("USBLink_head");
					//linkBtn.setText("连接");
					//linkBtn2.setText("连接");
					button3.setText("连接");
					JFrame frame = (JFrame)MainUi.map.get("frame");
					JOptionPane.showMessageDialog(frame, "设备已断开！", "提示", JOptionPane.ERROR_MESSAGE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
		/*usbPipe.addUsbPipeListener(new UsbPipeListener() {
			
			public void errorEventOccurred(UsbPipeErrorEvent e) {
				System.out.println(e);
			}
			
			public void dataEventOccurred(UsbPipeDataEvent e) {
				System.out.println(e);
			}
		});*/
	}
	
	public static void sendMassge(UsbPipe usbPipe,byte[] buff) throws Exception{
		usbPipe.syncSubmit(buff);//阻塞
		/*try {
			usbPipe.syncSubmit(buff);//阻塞
			//usbPipe.asyncSubmit(buff);//非阻塞
		}*/ /*catch(javax.usb.UsbDisconnectedException e){
			MainUi.map.put("sendUsbPipe", null);
			JButton linkBtn = (JButton)MainUi.map.get("LinkBtn_ChanJin");
			linkBtn.setText("连接");
			JFrame frame = (JFrame)MainUi.map.get("frame");
			JOptionPane.showMessageDialog(frame, "设备已断开！", "提示", JOptionPane.ERROR_MESSAGE);
		} *//*catch (Exception e) {
			//javax.usb.UsbPlatformException
			System.out.println("sssssssss");
			e.printStackTrace();
		}*/
	}
	
	@SuppressWarnings("unchecked")
	public UsbDevice findDevice(UsbHub hub)
	{
		UsbDevice device = null;
		List<UsbDevice> list = (List<UsbDevice>) hub.getAttachedUsbDevices();
	    for (int i = 0;i<list.size();i++)
	    {
	    	device = (UsbDevice)list.get(i);
	        UsbDeviceDescriptor desc = device.getUsbDeviceDescriptor();
	        if (desc.idVendor() == idVendor && desc.idProduct() == idProduct) {return device;}
	        if (device.isUsbHub())
	        {
	            device = findDevice((UsbHub) device);
	            if (device != null) return device;
	        }
	    }
	    return null;
	}
}
