package com.boray.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.table.DefaultTableModel;

import com.boray.Data.Data;
import com.boray.Data.DengJuData;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

public class DengJuDataExportAndImport {
	public void importData(File file){
		try {
			//File file = new File(path);
			Data.importPath = file.getParent();
			InputStream is = new FileInputStream(file);
			int size = (int)file.length();
			if (size != 0) {
				byte[] temp = new byte[size];
				is.read(temp);
				if (size%32 == 0 && Byte.toUnsignedInt(temp[0])==253 && Byte.toUnsignedInt(temp[1])==32) {
					handle(temp);
				}
			}
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void handle(byte[] file){
		NewJTable table_dengJu = (NewJTable)MainUi.map.get("table_dengJu");
		NewJTable table_DkGl = (NewJTable)MainUi.map.get("table_DkGl");
		DefaultTableModel model = (DefaultTableModel)table_dengJu.getModel();
		int count = file.length / 32;
		String[] temp = new String[5];
		int number = 1;
		for (int i = 0; i < count; i++) {
			int valid = Byte.toUnsignedInt(file[7+i*32]);
			if (valid == 1) {
				temp[0] = ""+number;
				number = number + 1;
				int nameLenght = Byte.toUnsignedInt(file[11+i*32]);
				byte[] b = new byte[nameLenght];
				for (int j = 0; j < nameLenght; j++) {
					b[j] = file[12+i*32+j];
				}
				String name = new String(b,0,nameLenght);//灯具名称
				int channelCount = Byte.toUnsignedInt(file[10+i*32]);//占用通道数
				int starChannel = Byte.toUnsignedInt(file[8+i*32]) * 256 + Byte.toUnsignedInt(file[9+i*32]);//起始地址
				int dengKuNo = Byte.toUnsignedInt(file[28+i*32]);
				if (dengKuNo != 0) {
					temp[1] = name;
					temp[2] = table_DkGl.getValueAt(dengKuNo-1, 1).toString();
					temp[3] = String.valueOf(starChannel);
					temp[4] = String.valueOf(channelCount);
					model.addRow(temp);
				}
			}
		}
	}
	public void exportData(File file){
		NewJTable table_dengJu = (NewJTable)MainUi.map.get("table_dengJu");
		NewJTable table_DkGl = (NewJTable)MainUi.map.get("table_DkGl");
		//File file = new File(path);
		try {
			OutputStream os = new FileOutputStream(file);
			for (int i = 0; i < table_dengJu.getRowCount(); i++) {
				DengJuData dengJuData = new DengJuData();
				dengJuData.setAddress(Integer.valueOf(table_dengJu.getValueAt(i, 3).toString()).intValue());
				dengJuData.setChannelCount(Integer.valueOf(table_dengJu.getValueAt(i, 4).toString()).intValue());
				for (int j = 0; j < table_DkGl.getRowCount(); j++) {
					if (table_DkGl.getValueAt(j, 1).toString().equals(table_dengJu.getValueAt(i, 2).toString())) {
						dengJuData.setChannelNo(j+1);
						break;
					}
				}
				dengJuData.setName(table_dengJu.getValueAt(i, 1).toString());
				byte[] temp = dengJuData.getbytes();
				os.write(temp);
				os.flush();
			}
			os.close();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
}
