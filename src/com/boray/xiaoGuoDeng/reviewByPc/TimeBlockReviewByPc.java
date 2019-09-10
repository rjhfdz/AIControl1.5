package com.boray.xiaoGuoDeng.reviewByPc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

public class TimeBlockReviewByPc {
	public static void review(int[] startAddress,NewJTable table,HashMap map){
		
		//动作
		Map actionXiaoGuoData = (Map)map.get("actionXiaoGuoData");
		boolean atn = true;
		if (actionXiaoGuoData!=null && actionXiaoGuoData.get("0").toString().equals("false")) {
			atn = false;
		}
		
		//rgb1
		List list = (List)map.get("rgb1Data");
		boolean rgbAct1 = true;
		if (list==null || (list!=null && !(boolean)list.get(0))) {
			rgbAct1 = false;
		}
		
		//rgb2
		List list2 = (List)map.get("rgb2Data");
		boolean rgbAct2 = true;
		if (list2==null || (list2!=null && !(boolean)list2.get(0))) {
			rgbAct2 = false;
		}
		
		//rgb3
		List list3 = (List)map.get("rgb3Data");
		boolean rgbAct3 = true;
		if (list3==null || (list3!=null && !(boolean)list3.get(0))) {
			rgbAct3 = false;
		}
		
		if (!atn && !rgbAct1 && !rgbAct2 && !rgbAct3) {
			System.out.println("aaaa");
			int rows = table.getRowCount();
			byte[][] buff = new byte[rows][512];
			int value = 0;
			for (int r = 0; r < rows; r++) {
				for (int j = 2; j < table.getColumnCount(); j++) {
					value = Integer.valueOf(table.getValueAt(r, j).toString()).intValue();
					for (int i = 0; i < startAddress.length; i++) {
						buff[r][startAddress[i]+j-3] = (byte)value;
					}
				}
			}
		}
		
		
		/*for (int i = 0; i < rows; i++) {
			for (int j = 0; j < 512; j++) {
				System.out.print(Byte.toUnsignedInt(buff[i][j])+" ");
			}
			System.out.println("");
		}*/
		//buff[511] = ZhiLingJi.getJiaoYan(buff);
		
		/*NewJTable table = (NewJTable)MainUi.map.get("GroupTable");
		NewJTable allLightTable = (NewJTable)MainUi.map.get("allLightTable");
		//for (int i = 0; i < table.getRowCount(); i++) {
			boolean b = (boolean)table.getValueAt(i, 0);
			int nb = 0;
			if (b) {
				//关联灯具编号
				TreeSet treeSet = (TreeSet)Data.GroupOfLightList.get(i);
				if (treeSet.size() > 0) {
					Iterator iterator = treeSet.iterator();
					while (iterator.hasNext()) {
						nb = (int)iterator.next();
						System.out.println(allLightTable.getValueAt(nb, 0).toString());
					}
				}
			}
		//}
*/	}
}
