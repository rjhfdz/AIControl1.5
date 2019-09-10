package com.boray.dengKu.UI;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.boray.Data.Data;
import com.boray.mainUi.MainUi;

public class DengKuUI implements ItemListener{
	private CardLayout card;
	private JPanel rightPane;
	public void show(JPanel pane) {
		pane.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel leftPane = new JPanel();
		leftPane.setBorder(new LineBorder(Color.gray));
		leftPane.setPreferredSize(new Dimension(108,588));
		JToggleButton btn = new JToggleButton("灯库管理");
		JToggleButton btn2 = new JToggleButton("灯具配置");
		JToggleButton btn3 = new JToggleButton("灯具分组");
		JToggleButton btn4 = new JToggleButton("RDM管理");
		btn.setPreferredSize(new Dimension(98,58));
		btn2.setPreferredSize(new Dimension(98,58));
		btn3.setPreferredSize(new Dimension(98,58));
		btn4.setPreferredSize(new Dimension(98,58));
		ButtonGroup group = new ButtonGroup();
		group.add(btn);
		group.add(btn2);
		group.add(btn3);
		group.add(btn4);
		btn.setSelected(true);
		/*btn.addActionListener(this);
		btn2.addActionListener(this);*/
		btn.addItemListener(this);
		btn2.addItemListener(this);
		btn3.addItemListener(this);
		btn4.addItemListener(this);
		btn.setFocusable(false);btn2.setFocusable(false);
		btn3.setFocusable(false);btn4.setFocusable(false);
		leftPane.add(btn);
		leftPane.add(btn2);
		leftPane.add(btn3);
		leftPane.add(btn4);
		
		rightPane = new JPanel();
		card = new CardLayout();
		rightPane.setLayout(card);
		//rightPane.setBorder(new LineBorder(Color.gray));
		//rightPane.setPreferredSize(new Dimension(588,608));
		
		JPanel guanLiPane = new JPanel();
		new GuangLiUI().show(guanLiPane);
		
		
		JPanel peiZhiPane = new JPanel();
		new PeiZhiUI().show(peiZhiPane);
		
		JPanel fenZhuPan = new JPanel();
		new FenZhuUI().show(fenZhuPan);
		
		JPanel RDMPane = new JPanel();
		new RdmPaneUI().show(RDMPane);
		
		rightPane.add(guanLiPane,"1");
		rightPane.add(peiZhiPane,"2");
		rightPane.add(fenZhuPan,"3");
		rightPane.add(RDMPane,"4");
		
		pane.add(leftPane);
		pane.add(rightPane);
	}
	public void itemStateChanged(ItemEvent e) {
		JToggleButton btn = (JToggleButton)e.getSource();
		if (e.getStateChange() == ItemEvent.SELECTED){
			if ("灯库管理".equals(btn.getText())) {
				
				card.show(rightPane, "1");
			} else if ("灯具配置".equals(btn.getText())) {
				/*NewJTable table = (NewJTable)mainUI.map.get("table_dengJu");
				NewJTable table2 = (NewJTable)mainUI.map.get("table_DkGl");
				for (int i = 0; i < table.getRowCount(); i++) {
					for (int j = 0; j < table2.getRowCount(); j++) {
						if (table2.getValueAt(j, 1).toString().equals(table.getValueAt(i, 2).toString())) {
							table.setValueAt(Data.dengKuName.get(j).toString(), i, 4);
						}
					}
				}*/
				card.show(rightPane, "2");
			} else if ("灯具分组".equals(btn.getText())) {
				NewJTable t2 = (NewJTable)MainUi.map.get("table_dengJu");
				NewJTable table = (NewJTable)MainUi.map.get("allLightTable");
				DefaultTableModel model = (DefaultTableModel)table.getModel();
				for (int i = table.getRowCount()-1; i >= 0; i--) {
					model.removeRow(i);
				}
				String[][] s = new String[t2.getRowCount()][1];
				for (int i = 0; i < s.length; i++) {
					s[i][0] = "ID"+(i+1)+"#"+t2.getValueAt(i, 2).toString()
							+"--"+t2.getValueAt(i, 5).toString()+"--"+t2.getValueAt(i, 6).toString();
					model.addRow(s[i]);
				}
				NewJTable table4 = (NewJTable)MainUi.map.get("GroupTable");
				int select = table4.getSelectedRow();
				if (select != -1) {
					TreeSet treeSet = (TreeSet)Data.GroupOfLightList.get(select);
					NewJTable table2 = (NewJTable)MainUi.map.get("InGroupsTable");//组内灯具
					//NewJTable table3 = (NewJTable)MainUi.map.get("allLightTable");//所有灯具
					DefaultTableModel model2 = (DefaultTableModel)table2.getModel();
					if (treeSet.size() > 0) {
						for (int i = table2.getRowCount()-1; i >= 0; i--) {
							model2.removeRow(i);
						}
						Iterator iterator = treeSet.iterator();
						while (iterator.hasNext()) {
							int i = (int)iterator.next();
							if (i < table.getRowCount()) {
								String[] ss = {table.getValueAt(i,0).toString()};
								model2.addRow(ss);
							} else {
								//treeSet.remove(i);
							}
						}
					}
				}
				
				card.show(rightPane, "3");
			} else if ("RDM管理".equals(btn.getText())) {
				card.show(rightPane, "4");
			}
		}
	}
}
