package com.boray.save;

import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

import com.boray.Data.Data;
import com.boray.Data.DengKuData;
import com.boray.Data.GetChannelNumber;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

public class DengKuDataExportAndImport {
	public void importData(File file){
		try {
			//File file = new File(path);
			Data.importPath = file.getParent();
			InputStream is = new FileInputStream(file);
			int size = (int)file.length();
			if (size != 0) {
				byte[] temp = new byte[size];
				is.read(temp);
				if (size%128 == 0 && Byte.toUnsignedInt(temp[0])==253 && Byte.toUnsignedInt(temp[1])==80) {
					handle(temp,128);
				} else if (size%80 == 0 && Byte.toUnsignedInt(temp[0])==253 && Byte.toUnsignedInt(temp[1])==80) {
					handle(temp,80);
				}
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	private void handle(byte[] file,int len){
		int count = file.length / len;
		for (int i = 0; i < count; i++) {
			int channelCount = Byte.toUnsignedInt(file[42+i*len]);
			if (channelCount != 0) {
				int nameLenght = Byte.toUnsignedInt(file[9+i*len]);
				byte[] b = new byte[nameLenght];
				for (int j = 0; j < nameLenght; j++) {
					b[j] = file[10+i*len+j];
				}
				String name = new String(b,0,nameLenght);//灯库名称
				
				String[] channels = new String[channelCount];//通道定义
				for (int j = 0; j < channelCount; j++) {
					channels[j] = GetChannelNumber.getChannelName(Byte.toUnsignedInt(file[43+i*len+j]));
				}
				
				//////////////////////////////////////////////////////
				NewJTable table = (NewJTable)MainUi.map.get("table_DkGl");
				Data.dengKuName.add(String.valueOf(channelCount));
				DefaultTableModel model = (DefaultTableModel)table.getModel();
				int index = table.getRowCount()+1;
				String[] s = {index+"",name};
				model.addRow(s);
				JComboBox[] boxs = (JComboBox[])MainUi.map.get("lamp_1_To_16");
				JComboBox[] boxs2 = (JComboBox[])MainUi.map.get("lamp_17_To_32");
				JLabel[] labels = (JLabel[])MainUi.map.get("lamp_1_To_16_label");
				JLabel[] labels2 = (JLabel[])MainUi.map.get("lamp_17_To_32_label");
				HashMap hashMap = new HashMap();
				ItemListener listener = (ItemListener)MainUi.map.get("ChannelItemListener");
				for (int j = 0; j < 16; j++) {
					boxs[j].removeItemListener(listener);
					boxs2[j].removeItemListener(listener);
				}
				if (channelCount > 16) {
					for (int n = 0; n < 16; n++) {
						//boxs[i].setEnabled(true);
						boxs[n].setVisible(true);
						labels[n].setVisible(true);
						boxs[n].setSelectedItem(channels[n]);
						hashMap.put(boxs[n].getName(), channels[n]);
					}
					for (int n = 0; n < channelCount-16; n++) {
						hashMap.put(boxs2[n].getName(), channels[n+16]);
						//boxs2[n].setEnabled(true);
						boxs2[n].setVisible(true);
						labels2[n].setVisible(true);
						boxs2[n].setSelectedItem(channels[n+16]);
					}
					for (int n = channelCount-16; n < boxs2.length; n++) {
						//boxs2[n].setEnabled(false);
						boxs2[n].setVisible(false);
						labels2[n].setVisible(false);
					}
				} else {
					for (int n = 0; n < boxs2.length; n++) {
						//boxs2[n].setEnabled(false);
						boxs2[n].setVisible(false);
						labels2[n].setVisible(false);
					}
					for (int n = 0; n < channelCount; n++) {
						//boxs[n].setEnabled(true);
						boxs[n].setVisible(true);
						labels[n].setVisible(true);
						boxs[n].setSelectedItem(channels[n]);
						hashMap.put(boxs[n].getName(), channels[n]);
					}
					for (int n = channelCount; n < boxs.length; n++) {
						//boxs[n].setEnabled(false);
						boxs[n].setVisible(false);
						labels[n].setVisible(false);
					}
				}
				for (int j = 0; j < 16; j++) {
					boxs[j].addItemListener(listener);
					boxs2[j].addItemListener(listener);
				}
				Data.DengKuList.add(hashMap);
				table.setRowSelectionInterval(table.getRowCount()-1, table.getRowCount()-1);
			}
		}
	}
	public void exportData(File file){
		NewJTable table = (NewJTable)MainUi.map.get("table_DkGl");
		//File file = new File(path);
		try {
			OutputStream os = new FileOutputStream(file);
			DengKuData dengKuData = new DengKuData();
			for (int n = 0; n < table.getRowCount(); n++) {
				HashMap map = (HashMap)Data.DengKuList.get(n);
				int channelCount = Integer.valueOf(Data.dengKuName.get(n).toString());
				JComboBox[] channelBoxs_L = (JComboBox[])MainUi.map.get("lamp_1_To_16");
				JComboBox[] channelBoxs_R = (JComboBox[])MainUi.map.get("lamp_17_To_32");
				int[] c = new int[channelCount];
				if (channelCount > 16) {
					for (int i = 0; i < 16; i++) {
						/*System.out.println(map.get(channelBoxs_L[i].getName()).toString());
						c[i] = GetChannelNumber.get(channelBoxs_L[i].getSelectedItem().toString());*/
						c[i] = GetChannelNumber.get(map.get(channelBoxs_L[i].getName()).toString());
					}
					for (int i = 16; i < channelCount; i++) {
						/*System.out.println(map.get(channelBoxs_L[i].getName()).toString());
						c[i] = GetChannelNumber.get(channelBoxs_R[i-16].getSelectedItem().toString());*/
						c[i] = GetChannelNumber.get(map.get(channelBoxs_R[i-16].getName()).toString());
					}
				} else {
					for (int i = 0; i < channelCount; i++) {
						/*System.out.println(map.get(channelBoxs_L[i].getName()).toString());
						c[i] = GetChannelNumber.get(channelBoxs_L[i].getSelectedItem().toString());*/
						c[i] = GetChannelNumber.get(map.get(channelBoxs_L[i].getName()).toString());
					}
				}
				dengKuData.setChannel(c);
				dengKuData.setName(table.getValueAt(n, 1).toString());
				dengKuData.setNo(n);
				dengKuData.setVersion(0);
				byte[] temp = dengKuData.getbytes();
				os.write(temp);
				os.flush();
			}
			os.close();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
}
