package com.boray.xiaoGuoDeng.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JPanel;

import com.boray.Data.Data;
import com.boray.Data.XiaoGuoDengModel;
import com.boray.Utils.CloneUtils;
import com.boray.mainUi.MainUi;
import com.boray.xiaoGuoDeng.UI.DefineJLable;
import com.boray.xiaoGuoDeng.UI.TimeBlockEditUI;

public class PopMenuListener implements ActionListener{
	private JPanel pane;
	private int blockNum;
	private String ss;
	public PopMenuListener(int blockNum, JPanel pane,String ss) {
		this.pane = pane;
		this.blockNum = blockNum;
		this.ss = ss;
	}
	public void actionPerformed(ActionEvent e) {
		if ("���༭".equals(e.getActionCommand())) {
			new TimeBlockEditUI().show(Integer.parseInt(ss.substring(2,3)),pane.getName());
		} else if ("��ɾ��".equals(e.getActionCommand())) {
			if (blockNum == pane.getComponentCount()) {
				int model = Integer.valueOf(XiaoGuoDengModel.model)-1;
				int grpN = Integer.valueOf(pane.getName()).intValue()-1;
				int blkN = Integer.valueOf(blockNum).intValue()-1;
//				Data.XiaoGuoDengObjects[model][grpN][blkN] = null;
				pane.remove(blockNum-1);
				pane.repaint();
			} else {
				
			}
		} else if ("��ɾ��ȫ��".equals(e.getActionCommand())) {
			pane.removeAll();
			pane.repaint();
		} else if ("������".equals(e.getActionCommand())) {
			int grpN = Integer.valueOf(pane.getName()).intValue();
			int blkN = Integer.valueOf(blockNum).intValue()-1;
			JPanel[] timeBlockPanels = (JPanel[])MainUi.map.get("timeBlockPanels_group"+XiaoGuoDengModel.model);
			DefineJLable lable = (DefineJLable)timeBlockPanels[grpN].getComponent(blkN);
			String[] strings = new String[2];
			strings[0] = lable.getWidth()+"";
			strings[1] = lable.getHeight()+"";
			
			HashMap map = (HashMap)Data.XiaoGuoDengObjects[XiaoGuoDengModel.model-1][grpN-1][blkN];
			HashMap copyMap = CloneUtils.clone(map);
			Object[] obj = new Object[]{strings,copyMap};
			Data.CopyObj = obj;
		}/* else if ("��ճ��".equals(e.getActionCommand())) {
			
		}*/
	}
}
