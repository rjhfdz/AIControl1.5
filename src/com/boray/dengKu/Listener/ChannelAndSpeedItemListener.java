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
			case "ͨ��0":
				blackOutEntity.setC1(box.getSelectedItem().toString());
				break;
			case "ͨ��1":
				blackOutEntity.setC2(box.getSelectedItem().toString());
				break;
			case "ͨ��2":
				blackOutEntity.setC3(box.getSelectedItem().toString());
				break;
			case "ͨ��3":
				blackOutEntity.setC4(box.getSelectedItem().toString());
				break;
			case "���0":
				blackOutEntity.setMin1(box.getSelectedItem().toString());
				break;
			case "���1":
				blackOutEntity.setMin2(box.getSelectedItem().toString());
				break;
			case "���2":
				blackOutEntity.setMin3(box.getSelectedItem().toString());
				break;
			case "���3":
				blackOutEntity.setMin4(box.getSelectedItem().toString());
				break;
			case "����0":
				blackOutEntity.setMax1(box.getSelectedItem().toString());
				break;
			case "����1":
				blackOutEntity.setMax2(box.getSelectedItem().toString());
				break;
			case "����2":
				blackOutEntity.setMax3(box.getSelectedItem().toString());
				break;
			case "����3":
				blackOutEntity.setMax4(box.getSelectedItem().toString());
				break;
			case "�ٶ�0":
				speedEntity.setS1(box.getSelectedItem().toString());
				break;
			case "�ٶ�1":
				speedEntity.setS2(box.getSelectedItem().toString());
				break;
			case "�ٶ�2":
				speedEntity.setS3(box.getSelectedItem().toString());
				break;
			case "��С0":
				speedEntity.setMin1(box.getSelectedItem().toString());
				break;
			case "��С1":
				speedEntity.setMin2(box.getSelectedItem().toString());
				break;
			case "��С2":
				speedEntity.setMin3(box.getSelectedItem().toString());
				break;
			case "���0":
				speedEntity.setMax1(box.getSelectedItem().toString());
				break;
			case "���1":
				speedEntity.setMax2(box.getSelectedItem().toString());
				break;
			case "���2":
				speedEntity.setMax3(box.getSelectedItem().toString());
				break;
			case "����0":
				speedEntity.setDirect1(box.getSelectedItem().toString());
				break;
			case "����1":
				speedEntity.setDirect2(box.getSelectedItem().toString());
				break;
			case "����2":
				speedEntity.setDirect3(box.getSelectedItem().toString());
				break;
			default:
				break;
		}
	}
}
