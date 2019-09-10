package com.boray.zhongKon.Listener;

import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.boray.mainUi.MainUi;
import com.boray.zhongKon.Data.DataOfZhongKon;

public class ShowAndSaveCode {
	public void show(int zh){
		//∫ÏÕ‚¬Î
		JTextField[] fields = (JTextField[])MainUi.map.get("redCodeFields");
		List list = (List)DataOfZhongKon.map1.get(""+zh);
		if (list != null) {
			for (int i = 0; i < 4; i++) {
				if (list.get(i).toString().length() == 1) {
					fields[i].setText("0"+list.get(i).toString());
				} else {
					fields[i].setText(list.get(i).toString());
				}
			}
		} else {
			for (int i = 0; i < 4; i++) {
				fields[i].setText("");
			}
		}
		//¥Æø⁄«Ω∞Â
		JTextField[] fields2 = (JTextField[])MainUi.map.get("comQianBanFields1");
		JTextField[] fields3 = (JTextField[])MainUi.map.get("comQianBanFields2");
		JTextField[] fields4 = (JTextField[])MainUi.map.get("comQianBanFields3");
		JTextField[] fields5 = (JTextField[])MainUi.map.get("comQianBanFields4");
		List list2 = (List)DataOfZhongKon.map2.get("1«Ω∞Â"+zh);
		List list3 = (List)DataOfZhongKon.map2.get("2«Ω∞Â"+zh);
		List list4 = (List)DataOfZhongKon.map2.get("3«Ω∞Â"+zh);
		List list5 = (List)DataOfZhongKon.map2.get("4«Ω∞Â"+zh);
		if (list2 != null) {
			for (int i = 0; i < list2.size(); i++) {
				if (list2.get(i).toString().length() == 1) {
					fields2[i].setText("0"+list2.get(i).toString());
				} else {
					fields2[i].setText(list2.get(i).toString());
				}
			}
			for (int i = list2.size(); i < 5; i++) {
				fields2[i].setText("");
			}
		} else {
			for (int i = 0; i < 5; i++) {
				fields2[i].setText("");
			}
		}
		if (list3 != null) {
			for (int i = 0; i < list3.size(); i++) {
				if (list3.get(i).toString().length() == 1) {
					fields3[i].setText("0"+list3.get(i).toString());
				} else {
					fields3[i].setText(list3.get(i).toString());
				}
			}
			for (int i = list3.size(); i < 5; i++) {
				fields3[i].setText("");
			}
		} else {
			for (int i = 0; i < 5; i++) {
				fields3[i].setText("");
			}
		}
		if (list4 != null) {
			for (int i = 0; i < list4.size(); i++) {
				if (list4.get(i).toString().length() == 1) {
					fields4[i].setText("0"+list4.get(i).toString());
				} else {
					fields4[i].setText(list4.get(i).toString());
				}
			}
			for (int i = list4.size(); i < 5; i++) {
				fields4[i].setText("");
			}
		} else {
			for (int i = 0; i < 5; i++) {
				fields4[i].setText("");
			}
		}
		if (list5 != null) {
			for (int i = 0; i < list5.size(); i++) {
				if (list5.get(i).toString().length() == 1) {
					fields5[i].setText("0"+list5.get(i).toString());
				} else {
					fields5[i].setText(list5.get(i).toString());
				}
			}
			for (int i = list5.size(); i < 5; i++) {
				fields5[i].setText("");
			}
		} else {
			for (int i = 0; i < 5; i++) {
				fields5[i].setText("");
			}
		}
		
		//¥Æø⁄…œ––
		JTextField[] fields6 = (JTextField[])MainUi.map.get("comShangXingFields");
		List list6 = (List)DataOfZhongKon.map3.get(""+zh);
		if (list6 != null) {
			for (int i = 0; i < fields6.length; i++) {
				if (list6.get(i).toString().length() == 1) {
					fields6[i].setText("0"+list6.get(i).toString());
				} else {
					fields6[i].setText(list6.get(i).toString());
				}
			}
		} else {
			for (int i = 0; i < fields6.length; i++) {
				fields6[i].setText("");
			}
		}
		
		//¥Æø⁄œ¬––
		JTextField[] fields7 = (JTextField[])MainUi.map.get("comXiaXingFields");
		List list7 = (List)DataOfZhongKon.map4.get(""+zh);
		if (list7 != null) {
			for (int i = 0; i < fields7.length; i++) {
				if (list7.get(i).toString().length() == 1) {
					fields7[i].setText("0"+list7.get(i).toString());
				} else {
					fields7[i].setText(list7.get(i).toString());
				}
			}
		} else {
			for (int i = 0; i < fields7.length; i++) {
				fields7[i].setText("");
			}
		}
		
		//∫»≤ 
		JComboBox box2 = (JComboBox)MainUi.map.get("heCaiBox");
		JComboBox box3 = (JComboBox)MainUi.map.get("daoCaiBox");
		List list8 = (List)DataOfZhongKon.map5.get(""+zh);
		ItemListener listener,listener2;
		listener = box2.getItemListeners()[0];
		listener2 = box3.getItemListeners()[0];
		box2.removeItemListener(listener);
		box3.removeItemListener(listener2);
		if (list8 != null) {
			box2.setSelectedIndex(Integer.valueOf(list8.get(0).toString()));
			box3.setSelectedIndex(Integer.valueOf(list8.get(1).toString()));
		} else {
			box2.setSelectedIndex(0);
			box3.setSelectedIndex(0);
		}
		box2.addItemListener(listener);
		box3.addItemListener(listener2);
	}
	public void save(int zh){
		//∫ÏÕ‚¬Î
		JTextField[] fields = (JTextField[])MainUi.map.get("redCodeFields");
		List list = (List)DataOfZhongKon.map1.get(""+zh);
		if (list == null) {
			list = new ArrayList();
		}
		list.clear();
		for (int i = 0; i < 4; i++) {
			list.add(fields[i].getText());
		}
		DataOfZhongKon.map1.put(""+zh, list);
		
		
		//¥Æø⁄«Ω∞Â
		JTextField[] fields2 = (JTextField[])MainUi.map.get("comQianBanFields1");
		JTextField[] fields3 = (JTextField[])MainUi.map.get("comQianBanFields2");
		JTextField[] fields4 = (JTextField[])MainUi.map.get("comQianBanFields3");
		JTextField[] fields5 = (JTextField[])MainUi.map.get("comQianBanFields4");
		List list2 = (List)DataOfZhongKon.map2.get("1«Ω∞Â"+zh);
		List list3 = (List)DataOfZhongKon.map2.get("2«Ω∞Â"+zh);
		List list4 = (List)DataOfZhongKon.map2.get("3«Ω∞Â"+zh);
		List list5 = (List)DataOfZhongKon.map2.get("4«Ω∞Â"+zh);
		if (list2 == null) {
			list2 = new ArrayList<>();
		}
		if (list3 == null) {
			list3 = new ArrayList<>();
		}
		if (list4 == null) {
			list4 = new ArrayList<>();
		}
		if (list5 == null) {
			list5 = new ArrayList<>();
		}
		list2.clear();list3.clear();
		list4.clear();list5.clear();
		for (int i = 0; i < 5; i++) {
			list2.add(fields2[i].getText());
			list3.add(fields3[i].getText());
			list4.add(fields4[i].getText());
			list5.add(fields5[i].getText());
		}
		DataOfZhongKon.map2.put("1«Ω∞Â"+zh, list2);
		DataOfZhongKon.map2.put("2«Ω∞Â"+zh, list3);
		DataOfZhongKon.map2.put("3«Ω∞Â"+zh, list4);
		DataOfZhongKon.map2.put("4«Ω∞Â"+zh, list5);
		
		//¥Æø⁄…œ––
		JTextField[] fields6 = (JTextField[])MainUi.map.get("comShangXingFields");
		List list6 = (List)DataOfZhongKon.map3.get(""+zh);
		if (list6 == null) {
			list6 = new ArrayList<>();
		}
		list6.clear();
		for (int i = 0; i < fields6.length; i++) {
			list6.add(fields6[i].getText());
		}
		DataOfZhongKon.map3.put(""+zh, list6);
		
		//¥Æø⁄œ¬––
		JTextField[] fields7 = (JTextField[])MainUi.map.get("comXiaXingFields");
		List list7 = (List)DataOfZhongKon.map4.get(""+zh);
		if (list7 == null) {
			list7 = new ArrayList<>();
		}
		list7.clear();
		for (int i = 0; i < fields7.length; i++) {
			list7.add(fields7[i].getText());
		}
		DataOfZhongKon.map4.put(""+zh, list7);
		
		//∫»≤ 
		JComboBox box2 = (JComboBox)MainUi.map.get("heCaiBox");
		JComboBox box3 = (JComboBox)MainUi.map.get("daoCaiBox");
		List list8 = (List)DataOfZhongKon.map5.get(""+zh);
		if (list8 == null) {
			list8 = new ArrayList<>();
		}
		list8.clear();
		list8.add(String.valueOf(box2.getSelectedIndex()));
		list8.add(String.valueOf(box3.getSelectedIndex()));
		DataOfZhongKon.map5.put(""+zh, list8);
	}
}
