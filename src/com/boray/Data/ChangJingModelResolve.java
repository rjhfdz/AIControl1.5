package com.boray.Data;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;

import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

public class ChangJingModelResolve {
	//private int threadNumber = 0;
	/*public static void main(String[] args) {
		String temp = "00110";
		StringBuilder tempBuilder;
		tempBuilder = new StringBuilder(temp);
		if (temp.length() != 8) {
			for (int j = 0; j < 8 - temp.length(); j++) {
				tempBuilder.insert(0,"0");
				System.out.println(0);
			}
		}
		for (int j = 0; j < tempBuilder.length(); j++) {
			if (tempBuilder.charAt(j) == '0') {
				System.out.print(0);
			} else if (tempBuilder.charAt(j) == '1') {
				System.out.print(1);
			}
		}
	}*/
	public void importData(final byte[] file){
		//512*3 = 1536
		final NewJTable table = (NewJTable)MainUi.map.get("table_DMX_All");
		int histryNo = table.getRowCount(); 
		String temp;
		StringBuilder tempBuilder;
		for (int i = 0; i < 64; i++) {
			temp = Integer.toBinaryString(Byte.toUnsignedInt(file[6+i]));
			tempBuilder = new StringBuilder(temp);
			if (temp.length() != 8) {
				for (int j = 0; j < 8 - temp.length(); j++) {
					tempBuilder.insert(0,"0");
				}
			}
			for (int j = 0; j < tempBuilder.length(); j++) {
				if (tempBuilder.charAt(j) == '0') {
					Data.checkList[(i*8)+j] = false;
				} else if (tempBuilder.charAt(j) == '1') {
					Data.checkList[(i*8)+j] = true;
				}
			}
		}
		/////////////////ÉèÖÃ¹´Ñ¡
		NewJTable table_dengJu = (NewJTable)MainUi.map.get("table_dengJu");
		if (table_dengJu != null) {
			JToggleButton[] btns = (JToggleButton[])MainUi.map.get("toggleButtons_changjin");
			int cnt = table_dengJu.getRowCount();
			for (int i = 0; i < cnt; i++) {
				if (btns[i].isSelected()) {
					//new ToggleBtnItemListener().fuckOneMoreTime(i);
					JCheckBox[] ChannelCheckBoxs = (JCheckBox[])MainUi.map.get("ChannelCheckBoxs");
					NewJTable table_DkGl = (NewJTable)MainUi.map.get("table_DkGl");
					int index = 0;
					for (int j = 0; j < table_DkGl.getRowCount(); j++) {
						if (table_dengJu.getValueAt(i, 2).equals(table_DkGl.getValueAt(j, 1))) {
							index = j;
							break;
						}
					}
					int channelCount = Integer.valueOf(Data.dengKuName.get(index).toString()).intValue();
					int start = Integer.valueOf(table_dengJu.getValueAt(i, 3).toString()).intValue();
					for (int n = 0; n < channelCount; n++) {
						ChannelCheckBoxs[n].setSelected(Data.checkList[start+n-1]);
					}
					break;
				} else {
					
				}
			}
		}
		//////////////////////////////
		
		int changJingCount = Byte.toUnsignedInt(file[4])*256+Byte.toUnsignedInt(file[5]);
		table.setVisible(false);
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		for (int i = histryNo - 1; i >= 0; i--) {
			model.removeRow(i);
		}
		histryNo = 0;
		/*for (int i = 0; i < changJingCount; i++) {
			model.addRow(Data.initValue2(histryNo+i+1));
		}
		int zhiX = 0;
		int tingL = 0;
		
		for (int i = 0; i < changJingCount * 4;i=i+4) {
			zhiX = Byte.toUnsignedInt(file[512+i])*256 + Byte.toUnsignedInt(file[513+i]);
			tingL = Byte.toUnsignedInt(file[514+i])*256 + Byte.toUnsignedInt(file[515+i]);
			table.setValueAt(String.valueOf(zhiX),i/4+histryNo,2);
			table.setValueAt(String.valueOf(tingL),i/4+histryNo,1);
		}
		for (int i = 0; i < changJingCount; i++) {
			for (int j = 0; j < 512; j++) {
				table.setValueAt(String.valueOf(Byte.toUnsignedInt(file[1536+512*i+j])), i, j+3);
			}
			if (i == 0) {
				table.setRowSelectionInterval(0, 0);
			}
		}*/
		String[][] temps = new String[changJingCount][515];
		for (int i = 0; i < changJingCount; i++) {
			temps[i] = Data.initValue2(histryNo+i+1);
		}
		int zhiX = 0;
		int tingL = 0;
		
		for (int i = 0; i < changJingCount * 4;i=i+4) {
			zhiX = Byte.toUnsignedInt(file[512+i])*256 + Byte.toUnsignedInt(file[513+i]);
			tingL = Byte.toUnsignedInt(file[514+i])*256 + Byte.toUnsignedInt(file[515+i]);
			temps[i/4+histryNo][2] = String.valueOf(zhiX);
			temps[i/4+histryNo][1] = String.valueOf(tingL);
		}
		for (int i = 0; i < changJingCount; i++) {
			for (int j = 0; j < 512; j++) {
				temps[i][j+3] = String.valueOf(Byte.toUnsignedInt(file[1536+512*i+j]));
			}
		}
		for (int i = 0; i < changJingCount; i++) {
			model.addRow(temps[i]);
		}
		table.setVisible(true);
		if (changJingCount >= 1) {
			table.setRowSelectionInterval(0, 0);
			//Rectangle rect = table.getCellRect(changJingCount-1,0,true);
			//table.scrollRectToVisible(rect);
		}
		JLabel stepCount = (JLabel)MainUi.map.get("stepCount");
		stepCount.setText(String.valueOf(changJingCount));
	}
}
