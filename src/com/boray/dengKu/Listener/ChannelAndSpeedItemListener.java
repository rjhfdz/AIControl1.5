package com.boray.dengKu.Listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;

import com.boray.Data.Data;
import com.boray.dengKu.Entity.BlackOutEntity;
import com.boray.dengKu.Entity.SpeedEntity;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

public class ChannelAndSpeedItemListener implements ItemListener{
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == 1) {
			JComboBox box = (JComboBox)e.getSource();
			NewJTable table = (NewJTable)MainUi.map.get("table_DkGl");
			if (table.getSelectedRow() != -1) {
				Map map = (Map)Data.dengKuBlackOutAndSpeedList.get(table.getSelectedRow());
				setValue(map, box);
			}
		}
	}
	private void setValue(Map map,JComboBox box){
		BlackOutEntity blackOutEntity;
		SpeedEntity speedEntity;
		String type = box.getName();
		if (map == null) {
			map = new HashMap();
			blackOutEntity = new BlackOutEntity();
			speedEntity = new SpeedEntity();
			map.put("blackOutEntity", blackOutEntity);
			map.put("speedEntity", speedEntity);
			NewJTable table = (NewJTable)MainUi.map.get("table_DkGl");
			Data.dengKuBlackOutAndSpeedList.set(table.getSelectedRow(), map);
		} else {
			blackOutEntity = (BlackOutEntity)map.get("blackOutEntity");
			speedEntity = (SpeedEntity)map.get("speedEntity");
		}
		
		switch (type) {
			case "通道0":
				blackOutEntity.setC1(box.getSelectedItem().toString());
				break;
			case "通道1":
				blackOutEntity.setC2(box.getSelectedItem().toString());
				break;
			case "通道2":
				blackOutEntity.setC3(box.getSelectedItem().toString());
				break;
			case "通道3":
				blackOutEntity.setC4(box.getSelectedItem().toString());
				break;
			case "灭灯0":
				blackOutEntity.setMin1(box.getSelectedItem().toString());
				break;
			case "灭灯1":
				blackOutEntity.setMin2(box.getSelectedItem().toString());
				break;
			case "灭灯2":
				blackOutEntity.setMin3(box.getSelectedItem().toString());
				break;
			case "灭灯3":
				blackOutEntity.setMin4(box.getSelectedItem().toString());
				break;
			case "最亮0":
				blackOutEntity.setMax1(box.getSelectedItem().toString());
				break;
			case "最亮1":
				blackOutEntity.setMax2(box.getSelectedItem().toString());
				break;
			case "最亮2":
				blackOutEntity.setMax3(box.getSelectedItem().toString());
				break;
			case "最亮3":
				blackOutEntity.setMax4(box.getSelectedItem().toString());
				break;
			case "速度0":
				speedEntity.setS1(box.getSelectedItem().toString());
				break;
			case "速度1":
				speedEntity.setS2(box.getSelectedItem().toString());
				break;
			case "速度2":
				speedEntity.setS3(box.getSelectedItem().toString());
				break;
			case "最小0":
				speedEntity.setMin1(box.getSelectedItem().toString());
				break;
			case "最小1":
				speedEntity.setMin2(box.getSelectedItem().toString());
				break;
			case "最小2":
				speedEntity.setMin3(box.getSelectedItem().toString());
				break;
			case "最大0":
				speedEntity.setMax1(box.getSelectedItem().toString());
				break;
			case "最大1":
				speedEntity.setMax2(box.getSelectedItem().toString());
				break;
			case "最大2":
				speedEntity.setMax3(box.getSelectedItem().toString());
				break;
			case "方向0":
				speedEntity.setDirect1(box.getSelectedItem().toString());
				break;
			case "方向1":
				speedEntity.setDirect2(box.getSelectedItem().toString());
				break;
			case "方向2":
				speedEntity.setDirect3(box.getSelectedItem().toString());
				break;
			default:
				break;
		}
	}
}
