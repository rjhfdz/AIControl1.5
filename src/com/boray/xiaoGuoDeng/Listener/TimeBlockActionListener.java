package com.boray.xiaoGuoDeng.Listener;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JPanel;

import com.boray.Data.Data;
import com.boray.Data.MyColor;
import com.boray.Data.XiaoGuoDengModel;
import com.boray.Utils.CloneUtils;
import com.boray.xiaoGuoDeng.UI.DefineJLable;
import com.boray.xiaoGuoDeng.UI.SelectSuCaiUI;

public class TimeBlockActionListener implements ActionListener{
	private JPanel pane;
	public TimeBlockActionListener(JPanel pane){
		this.pane = pane;
	}
	public void actionPerformed(ActionEvent e) {
		if ("　新建".equals(e.getActionCommand())) {
			int c = pane.getComponentCount();
			if (c == 20) {
				return;
			}
			new SelectSuCaiUI().show(pane);
//			DefineJLable label = new DefineJLable((c+1)+"",pane);
//			////////////////////////////////////////////////////////×
//			if (c > 0) {
//				DefineJLable defineJLable = (DefineJLable)pane.getComponent(c-1);
//				int x = defineJLable.getLocation().x + defineJLable.getWidth();
//				int y = defineJLable.getLocation().y;
//				label.setLocation(new Point(x, y));
//			}
//
//			////////////////////////////////////////////////////////
//			int model = Integer.valueOf(XiaoGuoDengModel.model)-1;
//			int grpN = Integer.valueOf(pane.getName()).intValue()-1;
//			int blkN = c;
//			HashMap hashMap = new HashMap<>();
//			Data.XiaoGuoDengObjects[model][grpN][blkN] = hashMap;
//			////////////////
//
//			if (c >= 10) {
//				c = c - 10;
//			}
//			label.setBackground(MyColor.colors[c]);
//
//			pane.add(label);
//			pane.updateUI();
		} else if ("　删除全部".equals(e.getActionCommand())) {
			pane.removeAll();
			pane.repaint();
		} else if ("　粘贴".equals(e.getActionCommand())) {
			Object[] obj = CloneUtils.clone(Data.CopyObj);
			if (obj != null) {
				String[] strings = (String[])obj[0];
				HashMap hashMap = (HashMap)obj[1];
				int width = Integer.valueOf(strings[0]);
				int height = Integer.valueOf(strings[1]);
				int c = pane.getComponentCount();
				if (c == 20) {
					return;
				}
				DefineJLable label = new DefineJLable((c+1)+"",pane);
				////////////////////////////////////////////////////////×
				if (c > 0) {
					DefineJLable defineJLable = (DefineJLable)pane.getComponent(c-1);
					int x = defineJLable.getLocation().x + defineJLable.getWidth();
					int y = defineJLable.getLocation().y;
					label.setLocation(new Point(x, y));
					label.setSize(width, height);
				}
				//label.setSize(width, height);
				////////////////////////////////////////////////////////
				int model = Integer.valueOf(XiaoGuoDengModel.model)-1;
				int grpN = Integer.valueOf(pane.getName()).intValue()-1;
				int blkN = c;
				//HashMap hashMap = new HashMap<>();
				Data.XiaoGuoDengObjects[model][grpN][blkN] = hashMap;
				////////////////
				
				if (c >= 10) {
					c = c - 10;
				}
				label.setBackground(MyColor.colors[c]);
				
				pane.add(label);
				pane.updateUI();
			}
		}
	}
}
