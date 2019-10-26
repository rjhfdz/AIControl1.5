package com.boray.suCai.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JToggleButton;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.mainUi.MainUi;

public class YunSuCaiTypeListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
		System.out.println("wlail");
		JToggleButton btn = (JToggleButton)e.getSource();
		JList suCaiLightType = (JList)MainUi.map.get("yunsuCaiLightType");
		if (suCaiLightType.getSelectedValue() != null) {
		
				
				JList list = (JList)MainUi.map.get("yunsc");
				DefaultListModel  model = (DefaultListModel)list.getModel();
				model.removeAllElements();
				
				String str =HttpClientUtil.doGet("http://localhost:8778/getsc?username=1&kuname="+suCaiLightType.getSelectedValue().toString()+"&sctype="+btn.getName()+"");
				System.out.println(suCaiLightType.getSelectedValue().toString()+btn.getName());
				if(str!=null&&!str.equals("")) {
					JSONArray j = JSONArray.parseArray(str);
					System.out.println(str);
					for (int i = 0; i < j.size(); i++) {
						JSONObject jo = j.getJSONObject(i);
						Data.yunSuCaiMap.put(jo.get("filename"), jo.get("filejson"));
						model.addElement(jo.get("filename"));
					}
					list.setSelectedIndex(0);
					
				}
				
				
			} else {
				JList list = (JList)MainUi.map.get("yunsc");
				list.removeAll();
			}
		}
	}

