package com.boray.suCai.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.mainUi.MainUi;
import com.boray.suCai.Listener.SuCaiTypeListener;
import com.boray.suCai.Listener.YunSuCaiTypeListener;
import com.boray.suCai.Listener.YuninsertListener;

public class DialogSuCaiUI {
	public void show(JPanel pane) {
		pane.setBorder(new LineBorder(Color.black));
		pane.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JScrollPane p1 = new JScrollPane();
		p1.setPreferredSize(new Dimension(200,594));
		setP1(p1);
		
		JPanel p2 = new JPanel();
		TitledBorder titledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "素材类型", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p2.setBorder(titledBorder);
		p2.setPreferredSize(new Dimension(150,586));
		setP2(p2);
		
		JPanel p3 = new JPanel();
		TitledBorder titledBorder1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "素材列表", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p3.setBorder(titledBorder1);
		p3.setPreferredSize(new Dimension(280,594));
		setP3(p3);
		
		
		
		pane.add(p1);
		pane.add(p2);
		pane.add(p3);
	}
	
  private void setP3(JPanel pane) {
		JScrollPane scro2llPane = new JScrollPane();
		scro2llPane.setPreferredSize(new Dimension(270,520));
		Map map = new HashMap<>();
		//素材列表
		final JList list = new JList();
		
		MainUi.map.put("yunsc", list);
		DefaultListModel  model=new DefaultListModel<>();
		list.setModel(model);
		list.setSelectionBackground(new Color(85,160,255));
		list.setSelectionForeground(Color.WHITE);
		//list.setOpaque(false);
		scro2llPane.setViewportView(list);
		pane.add(scro2llPane);
		Dimension dimension = new Dimension(100,34);
		
		JButton editBtn = new JButton("下载");
		
		editBtn.setPreferredSize(dimension);
		editBtn.addActionListener(new YuninsertListener());
		pane.add(editBtn);
	

	}
	
  private 	 void setP2(JPanel pane) {
	  FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
		flowLayout.setVgap(-2);
		pane.setLayout(flowLayout);
		String[] name = {"动感","慢摇","抒情","柔和","浪漫","温馨","炫丽","梦幻","其他"};
		JToggleButton[] btns = new JToggleButton[name.length];
		YunSuCaiTypeListener listener = new YunSuCaiTypeListener();
		MainUi.map.put("yunsuCaiTypeBtns", btns);
		ButtonGroup group = new ButtonGroup();
		for (int i = 0; i < btns.length; i++) {
			btns[i] = new JToggleButton(name[i]);
			btns[i].setName(""+i);
			btns[i].setPreferredSize(new Dimension(130,32));
			btns[i].addActionListener(listener);
			group.add(btns[i]);
			pane.add(btns[i]);
		}
		btns[0].setSelected(true);
	}
	
	private void setP1(JScrollPane scrollPane){
		 final JList list = new JList();
		DefaultListModel  model = new DefaultListModel();
		String str =HttpClientUtil.doGet("http://localhost:8778/getku?username=1");
		
		if(str!=null&&!str.equals("")) {
			JSONArray j = JSONArray.parseArray(str);
			System.out.println(str);
			for (int i = 0; i < j.size(); i++) {
				JSONObject jo = j.getJSONObject(i);
				model.addElement(jo.get("kuname"));
			}
			
		}
     
		list.setModel(model);
		
		
		MainUi.map.put("yunsuCaiLightType", list);
		list.setFixedCellHeight(32);
		DefaultListCellRenderer renderer = new DefaultListCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		list.setCellRenderer(renderer);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				if (list.getSelectedValue()!=null) {
					
					Map map = (Map)Data.suCaiMap.get(list.getSelectedValue().toString());
					JToggleButton[] btns = (JToggleButton[])MainUi.map.get("yunsuCaiTypeBtns");
					String[] name = {"动感","慢摇","抒情","柔和","浪漫","温馨","炫丽","梦幻","其他"};
					if (map!=null) {
						for (int i = 0; i < btns.length; i++) {
							List abc = (List)map.get(""+i);
							int size = 0;
							if (abc!=null) {
								size = abc.size();
							}
							btns[i].setText(name[i]+"("+size+")");
						}
					} else {
						for (int i = 0; i < btns.length; i++) {
							btns[i].setText(name[i]+"(0)");
						}
					}
					
					String str =HttpClientUtil.doGet("http://localhost:8778/getsctype?username=1&kuname="+list.getSelectedValue().toString()+"");
					
					if(str!=null&&!str.equals("")) {
						JSONArray j = JSONArray.parseArray(str);
						System.out.println(str);
						for (int i = 0; i < j.size(); i++) {
							JSONObject jo = j.getJSONObject(i);
							Data.yunSuCaiMap.put(jo.get("filename"), jo.get("filejson"));
							btns[jo.getIntValue("sctype")].setText(name[i]+jo.getString("countsctype"));
						}
						
					}
					
					//
					int selected = 0;
					for (int i = 0; i < btns.length; i++) {
						if (btns[i].isSelected()) {
							selected = i;
							break;
						}
					}
					
					Map nameMap = (Map)Data.suCaiNameMap.get(list.getSelectedValue().toString());
					JList list = (JList)MainUi.map.get("suCai_list");
					DefaultListModel  model = (DefaultListModel)list.getModel();
					model.removeAllElements();
					if (nameMap!=null) {
						List tmp = (List)nameMap.get(""+selected);
						if (tmp!=null) {
							for (int i = 0; i < tmp.size(); i++) {
								model.addElement(tmp.get(i).toString());
							}
							if (tmp.size()>0) {
								list.setSelectedIndex(0);
							}
						}
					}
				}
			}
		});
		
		list.setSelectionBackground(new Color(85,160,255));
	//	list.setSelectedIndex(model.getSize()-1);
		list.setSelectionForeground(Color.WHITE);
		//list.setOpaque(false);
		scrollPane.setViewportView(list);
	}
}
