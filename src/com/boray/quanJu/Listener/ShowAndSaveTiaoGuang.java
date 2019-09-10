package com.boray.quanJu.Listener;

import java.util.ArrayList;
import java.util.List;

import javax.comm.SerialPort;
import javax.swing.JComboBox;
import javax.swing.JSlider;

import com.boray.Data.Data;
import com.boray.changJing.Data.DataOfChangJing;
import com.boray.mainUi.MainUi;
import com.boray.quanJu.Data.DataOfTiaoGuang;

public class ShowAndSaveTiaoGuang {
	public void show() {
		ArrayList list = (ArrayList)MainUi.map.get("setTiaoGuangCpns");
		//灯1
		JComboBox box = (JComboBox)list.get(0);
		JSlider slider1 = (JSlider)list.get(1);
		JSlider slider2 = (JSlider)list.get(2);
		
		//灯2
		JComboBox box2 = (JComboBox)list.get(3);
		JSlider slider3 = (JSlider)list.get(4);
		JSlider slider4 = (JSlider)list.get(5);
		
		//灯3
		JComboBox box3 = (JComboBox)list.get(6);
		JSlider slider5 = (JSlider)list.get(7);
		JSlider slider6 = (JSlider)list.get(8);
		
		//灯4
		JComboBox box4 = (JComboBox)list.get(9);
		JSlider slider7 = (JSlider)list.get(10);
		JSlider slider8 = (JSlider)list.get(11);
		
		
		SerialPort serialPort = Data.serialPort;
		Data.serialPort = null;
		
		//25 可调、下限
		List list66 = (List)DataOfChangJing.map.get(""+25);
		
		box.setSelectedIndex(Integer.valueOf(list66.get(8).toString()).intValue());
		slider2.setValue(Integer.valueOf(list66.get(9).toString()).intValue());
		
		box2.setSelectedIndex(Integer.valueOf(list66.get(10).toString()).intValue());
		slider4.setValue(Integer.valueOf(list66.get(11).toString()).intValue());
		
		box3.setSelectedIndex(Integer.valueOf(list66.get(12).toString()).intValue());
		slider6.setValue(Integer.valueOf(list66.get(13).toString()).intValue());
		
		box4.setSelectedIndex(Integer.valueOf(list66.get(14).toString()).intValue());
		slider8.setValue(Integer.valueOf(list66.get(15).toString()).intValue());
		
		//26 上限
		list66 = (List)DataOfChangJing.map.get(""+26);
		slider1.setValue(Integer.valueOf(list66.get(9).toString()).intValue());
		slider3.setValue(Integer.valueOf(list66.get(11).toString()).intValue());
		slider5.setValue(Integer.valueOf(list66.get(13).toString()).intValue());
		slider7.setValue(Integer.valueOf(list66.get(15).toString()).intValue());
		
		Data.serialPort = serialPort;
		/*if (DataOfTiaoGuang.list.size() > 0) {
			SerialPort serialPort = Data.serialPort;
			Data.serialPort = null;
			box.setSelectedIndex(Integer.valueOf(DataOfTiaoGuang.list.get(0).toString()));
			slider1.setValue(Integer.valueOf(DataOfTiaoGuang.list.get(1).toString()));
			slider2.setValue(Integer.valueOf(DataOfTiaoGuang.list.get(2).toString()));
			box2.setSelectedIndex(Integer.valueOf(DataOfTiaoGuang.list.get(3).toString()));
			slider3.setValue(Integer.valueOf(DataOfTiaoGuang.list.get(4).toString()));
			slider4.setValue(Integer.valueOf(DataOfTiaoGuang.list.get(5).toString()));
			
			box3.setSelectedIndex(Integer.valueOf(DataOfTiaoGuang.list.get(6).toString()));
			slider5.setValue(Integer.valueOf(DataOfTiaoGuang.list.get(7).toString()));
			slider6.setValue(Integer.valueOf(DataOfTiaoGuang.list.get(8).toString()));
			
			box4.setSelectedIndex(Integer.valueOf(DataOfTiaoGuang.list.get(9).toString()));
			slider7.setValue(Integer.valueOf(DataOfTiaoGuang.list.get(10).toString()));
			slider8.setValue(Integer.valueOf(DataOfTiaoGuang.list.get(11).toString()));
			
			Data.serialPort = serialPort;
		}*/
	}
	public void save(){
		//DataOfTiaoGuang.list.clear();
		ArrayList list = (ArrayList)MainUi.map.get("setTiaoGuangCpns");
		//灯1
		JComboBox box = (JComboBox)list.get(0);
		JSlider slider1 = (JSlider)list.get(1);
		JSlider slider2 = (JSlider)list.get(2);
		
		//灯2
		JComboBox box2 = (JComboBox)list.get(3);
		JSlider slider3 = (JSlider)list.get(4);
		JSlider slider4 = (JSlider)list.get(5);
		
		//灯3
		JComboBox box3 = (JComboBox)list.get(6);
		JSlider slider5 = (JSlider)list.get(7);
		JSlider slider6 = (JSlider)list.get(8);
		
		//灯4
		JComboBox box4 = (JComboBox)list.get(9);
		JSlider slider7 = (JSlider)list.get(10);
		JSlider slider8 = (JSlider)list.get(11);
		
		/*DataOfTiaoGuang.list.add(String.valueOf(box.getSelectedIndex()));
		DataOfTiaoGuang.list.add(String.valueOf(slider1.getValue()));
		DataOfTiaoGuang.list.add(String.valueOf(slider2.getValue()));
		DataOfTiaoGuang.list.add(String.valueOf(box2.getSelectedIndex()));
		DataOfTiaoGuang.list.add(String.valueOf(slider3.getValue()));
		DataOfTiaoGuang.list.add(String.valueOf(slider4.getValue()));
		
		DataOfTiaoGuang.list.add(String.valueOf(box3.getSelectedIndex()));
		DataOfTiaoGuang.list.add(String.valueOf(slider5.getValue()));
		DataOfTiaoGuang.list.add(String.valueOf(slider6.getValue()));
		
		DataOfTiaoGuang.list.add(String.valueOf(box4.getSelectedIndex()));
		DataOfTiaoGuang.list.add(String.valueOf(slider7.getValue()));
		DataOfTiaoGuang.list.add(String.valueOf(slider8.getValue()));*/
		
		
		//25 可调、下限
		List list66 = (List)DataOfChangJing.map.get(""+25);
		
		list66.set(8, String.valueOf(box.getSelectedIndex()));
		list66.set(9,String.valueOf(slider2.getValue()));
		
		list66.set(10, String.valueOf(box2.getSelectedIndex()));
		list66.set(11,String.valueOf(slider4.getValue()));
		
		list66.set(12, String.valueOf(box3.getSelectedIndex()));
		list66.set(13,String.valueOf(slider6.getValue()));
		
		list66.set(14, String.valueOf(box4.getSelectedIndex()));
		list66.set(15,String.valueOf(slider8.getValue()));
		
		DataOfChangJing.map.put(""+25,list66);
		//26 上限
		list66 = (List)DataOfChangJing.map.get(""+26);
		list66.set(9,String.valueOf(slider1.getValue()));
		list66.set(11,String.valueOf(slider3.getValue()));
		list66.set(13,String.valueOf(slider5.getValue()));
		list66.set(15,String.valueOf(slider7.getValue()));
		
		DataOfChangJing.map.put(""+26,list66);
	}
}
