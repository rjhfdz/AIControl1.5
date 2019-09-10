package com.boray.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLDecoder;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.boray.Data.Data;
import com.boray.Data.DengJuData;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.save.DengJuDataExportAndImport;
import com.boray.save.DengKuDataExportAndImport;

public class DengJuDataListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		if ("导出".equals(e.getActionCommand())) {
			export();
		} else if ("导入".equals(e.getActionCommand())){
			importData();
		} else if ("保存".equals(e.getActionCommand())) {
			saveAllData();
		}
	}
	private void saveAllData(){
		try {
			URL url = getClass().getResource("/lib/Library_Gather.hxl");
			URL url2 = getClass().getResource("/lib/Lights_Gather.hxl");
			String path = getClass().getResource("/lib/").getPath().substring(1);
			path = URLDecoder.decode(path,"utf-8"); 
			DengKuDataExportAndImport dengKuDataExportAndImport = new DengKuDataExportAndImport();
			DengJuDataExportAndImport dengJuDataExportAndImport = new DengJuDataExportAndImport();
			if (url != null && url2 != null) {
				//String path = "C:/Documents and Settings/Administrator/桌面/8系列智能控制器管理系统V1.1/";
				File file = new File(path+"Library_Gather.hxl");
				File file2 = new File(path+"Lights_Gather.hxl");
				dengKuDataExportAndImport.exportData(file);
				dengJuDataExportAndImport.exportData(file2);
			} else {
				File file = new File(path+"Library_Gather.hxl");
				File file2 = new File(path+"Lights_Gather.hxl");
				if (!file.exists()) {
					file.createNewFile();
				}
				if (!file2.exists()) {
					file2.createNewFile();
				}
				dengKuDataExportAndImport.exportData(file);
				dengJuDataExportAndImport.exportData(file2);
				
			}
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	private void importData(){
		JFileChooser fileChooser = new JFileChooser();
		if (!"".equals(Data.importPath)) {
			fileChooser.setCurrentDirectory(new File(Data.importPath));
		} else if (!"".equals(Data.exportPath)) {
			fileChooser.setCurrentDirectory(new File(Data.exportPath));
		}
		String[] houZhui = {"hxl"};
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.hxl", houZhui);
		fileChooser.setFileFilter(filter);
		int returnVal = fileChooser.showOpenDialog((JFrame)MainUi.map.get("frame"));
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			try {
				File file = fileChooser.getSelectedFile();
				Data.importPath = file.getParent();
				InputStream is = new FileInputStream(file);
				int size = (int)file.length();
				byte[] temp = new byte[size];
				is.read(temp);
				is.close();
				if (size%32 == 0 && Byte.toUnsignedInt(temp[0])==253 && Byte.toUnsignedInt(temp[1])==32) {
					handle(temp);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			int nameLenght = Byte.toUnsignedInt(file[11+i*32]);
			int valid = Byte.toUnsignedInt(file[7+i*32]);
			if (valid == 1) {
				temp[0] = ""+number;
				number = number + 1;
				byte[] b = new byte[nameLenght];
				for (int j = 0; j < nameLenght; j++) {
					b[j] = file[12+i*32+j];
				}
				String name = new String(b,0,nameLenght);//灯具名称
				int channelCount = Byte.toUnsignedInt(file[10+i*32]);//占用通道数
				int starChannel = Byte.toUnsignedInt(file[8+i*32]) * 256 + Byte.toUnsignedInt(file[9+i*32]);//起始地址
				int dengKuNo = Byte.toUnsignedInt(file[28+i*32]);
				temp[1] = name;
				temp[2] = table_DkGl.getValueAt(dengKuNo-1, 1).toString();
				temp[3] = String.valueOf(starChannel);
				temp[4] = String.valueOf(channelCount);
				model.addRow(temp);
			}
		}
	}
	private void export(){
		NewJTable table_dengJu = (NewJTable)MainUi.map.get("table_dengJu");
		NewJTable table_DkGl = (NewJTable)MainUi.map.get("table_DkGl");
		String dkName = "灯具";
		JFileChooser fileChooser = new JFileChooser();
		/*if (!"".equals(Data.exportPath)) {
			fileChooser.setCurrentDirectory(new File(Data.exportPath));
		} else if (!"".equals(Data.importPath)) {
			fileChooser.setCurrentDirectory(new File(Data.importPath));
		}*/
		try {
			String path = getClass().getResource("/lib/").getPath().substring(1);
			path = URLDecoder.decode(path,"utf-8"); 
			fileChooser.setCurrentDirectory(new File(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setSelectedFile(new File(dkName + ".hxl"));
		int returnVal = fileChooser.showSaveDialog((JFrame)MainUi.map.get("frame"));
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			try {
				Data.exportPath = file.getParent();
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
}
