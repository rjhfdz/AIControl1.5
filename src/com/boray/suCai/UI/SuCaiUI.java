package com.boray.suCai.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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

import com.boray.Data.Data;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.suCai.Listener.CreateOrDelSuCaiListener;
import com.boray.suCai.Listener.EditListener;
import com.boray.suCai.Listener.SuCaiTypeListener;

public class SuCaiUI {
	public void show(JPanel pane) {
		pane.setBorder(new LineBorder(Color.black));
		pane.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JScrollPane p1 = new JScrollPane();
		p1.setPreferredSize(new Dimension(200,594));
		setP1(p1);
		
		JPanel p2 = new JPanel();
		TitledBorder titledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "�ز�����", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p2.setBorder(titledBorder);
		p2.setPreferredSize(new Dimension(150,586));
		setP2(p2);
		
		JPanel p3 = new JPanel();
		TitledBorder titledBorder1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "�ز��б�", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p3.setBorder(titledBorder1);
		p3.setPreferredSize(new Dimension(280,594));
		setP3(p3);
		
		JPanel p4 = new JPanel();
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "�ƶ��ز�", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p4.setBorder(tb);
		p4.setPreferredSize(new Dimension(280,592));
		setP4(p4);
		
		pane.add(p1);
		pane.add(p2);
		pane.add(p3);
		pane.add(p4);
	}
	private void setP4(JPanel pane){
		
	}
	private void setP3(JPanel p3){
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setPreferredSize(new Dimension(270,520));
		
		//�ز��б�
		final JList list = new JList();
		MainUi.map.put("suCai_list", list);
		list.setFixedCellHeight(32);
		DefaultListCellRenderer renderer = new DefaultListCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		list.setCellRenderer(renderer);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//String[] s = {};
		//list.setListData(s);
		DefaultListModel  model = new DefaultListModel();
		list.setModel(model);
		list.setSelectionBackground(new Color(85,160,255));
		list.setSelectionForeground(Color.WHITE);
		//list.setOpaque(false);
		scrollPane.setViewportView(list);
		
		p3.add(scrollPane);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setPreferredSize(new Dimension(280,40));
		//bottomPanel.setBorder(new LineBorder(Color.black));
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setVgap(-2);
		flowLayout.setHgap(-2);
		bottomPanel.setLayout(flowLayout);
		JButton newBtn = new JButton("�½�");
		JButton delBtn = new JButton("ɾ��");
		JButton editBtn = new JButton("�༭");
		JButton reNameBtn = new JButton("������");
		JButton upLoadBtn = new JButton("�ϴ�");
		CreateOrDelSuCaiListener listener = new CreateOrDelSuCaiListener();
		EditListener listener2 = new EditListener();
		editBtn.addActionListener(listener2);
		newBtn.addActionListener(listener);
		delBtn.addActionListener(listener);
		Dimension dimension = new Dimension(55,34);
		newBtn.setPreferredSize(dimension);
		delBtn.setPreferredSize(dimension);
		editBtn.setPreferredSize(dimension);
		reNameBtn.setPreferredSize(new Dimension(68,34));
		upLoadBtn.setPreferredSize(dimension);
		bottomPanel.add(newBtn);bottomPanel.add(delBtn);
		bottomPanel.add(editBtn);bottomPanel.add(reNameBtn);
		bottomPanel.add(upLoadBtn);
		p3.add(bottomPanel);
	}
	private void setP1(JScrollPane scrollPane){
		final JList list = new JList();
		MainUi.map.put("suCaiLightType", list);
		list.setFixedCellHeight(32);
		DefaultListCellRenderer renderer = new DefaultListCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		list.setCellRenderer(renderer);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent listSelectionEvent) {
				if (list.getSelectedValue()!=null) {
					Map map = (Map)Data.suCaiMap.get(list.getSelectedValue().toString());
					JToggleButton[] btns = (JToggleButton[])MainUi.map.get("suCaiTypeBtns");
					String[] name = {"����","��ҡ","����","���","����","��ܰ","����","�λ�","����"};
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
		String[] s = {};
		list.setListData(s);
		list.setSelectionBackground(new Color(85,160,255));
		list.setSelectionForeground(Color.WHITE);
		//list.setOpaque(false);
		scrollPane.setViewportView(list);
	}
	private void setP2(JPanel pane){
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
		flowLayout.setVgap(-2);
		pane.setLayout(flowLayout);
		String[] name = {"����","��ҡ","����","���","����","��ܰ","����","�λ�","����"};
		JToggleButton[] btns = new JToggleButton[name.length];
		SuCaiTypeListener listener = new SuCaiTypeListener();
		MainUi.map.put("suCaiTypeBtns", btns);
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
}
