package com.boray.shengKon.Listener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import com.boray.Data.Data;
import com.boray.Data.MyData;
import com.boray.Utils.CloneUtils;
import com.boray.mainUi.MainUi;
import com.boray.shengKon.UI.DefineJLable_shengKon;
import com.boray.shengKon.UI.DefineJLable_shengKon2;
import com.boray.shengKon.UI.ShiXuSetUI;

public class TimeAssignPopMenuListener implements ActionListener{
	private JPanel pane;
	private int blockNum;
	public TimeAssignPopMenuListener(int blockNum, JPanel pane) {
		this.pane = pane;
		this.blockNum = blockNum;
	}
	public void actionPerformed(ActionEvent e) {
		if ("É¾³ý".equals(e.getActionCommand())) {
			if (blockNum == pane.getComponentCount()) {
				JPanel[] timeBlockPanels = (JPanel[])MainUi.map.get("timeBlockPanels"+MyData.ShengKonModel);
				for (int i = 0; i < timeBlockPanels.length; i++) {
					timeBlockPanels[i].remove(blockNum-1);
					timeBlockPanels[i].updateUI();
				}
				int modelInt = Integer.valueOf(MyData.ShengKonModel)-1;
				Data.ShengKonShiXuSetObjects[modelInt][blockNum] = null;
			}
		} else if("Éù¿ØË³ÐòÉèÖÃ".equals(e.getActionCommand())){
			new ShiXuSetUI().show(blockNum);
		} else if ("¸´ÖÆ".equals(e.getActionCommand())) {
			Map map = new HashMap<>();
			JPanel[] timeBlockPanels = (JPanel[])MainUi.map.get("timeBlockPanels"+MyData.ShengKonModel);
			DefineJLable_shengKon lable = (DefineJLable_shengKon)timeBlockPanels[0].getComponent(blockNum-1);
			String[] strings = new String[1];
			strings[0] = lable.getWidth()+"";//¼ÇÂ¼¿í¶È
			map.put("0", strings);
			
			int abc[] = new int[30];//¼ÇÂ¼³¡¾°±à¼­
			for (int i = 1; i < timeBlockPanels.length; i++) {
				DefineJLable_shengKon2 lable2 = (DefineJLable_shengKon2)timeBlockPanels[i].getComponent(blockNum-1);
				if (!lable2.getBackground().equals(Color.red)) {
					abc[i-1] = 1;
				}
			}
			map.put("1", abc);
			
			//Éù¿ØË³ÐòÉèÖÃ²ÎÊý
			int modelInt = Integer.valueOf(MyData.ShengKonModel).intValue()-1;
			int TimeBlockInt = blockNum - 1;
			int[][] DataSetInts = (int[][])Data.ShengKonShiXuSetObjects[modelInt][TimeBlockInt];
			int[][] tempDataSet = CloneUtils.clone(DataSetInts);
			map.put("2", tempDataSet);
			
			//±à¼­Ð§¹ûÊý¾Ý
			Object[] objects = new Object[30];
			for (int i = 0; i < 30; i++) {
				HashMap temp = (HashMap)Data.ShengKonEditObjects[modelInt][i][TimeBlockInt];
				Map map2 = CloneUtils.clone(temp);
				objects[i] = map2;
			}
			map.put("3", objects);
			MainUi.map.put("shengkongCopyMap", map);
		}
	}
}
