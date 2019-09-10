package com.boray.xiaoGuoDeng.UI;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.boray.mainUi.MainUi;
import com.boray.xiaoGuoDeng.Listener.DMXModelListener;

public class XiaoGuoDengUI implements ActionListener{
	public void show(JPanel pane) {
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setVgap(-5);
		pane.setLayout(flowLayout);
		JPanel leftPane = new JPanel();
		JPanel rightPane = new JPanel();
		setLeftPane(leftPane);
		setRightPane(rightPane);
		pane.add(leftPane);
		pane.add(rightPane);
	}
	
	private void setLeftPane(JPanel leftPane) {
		leftPane.setPreferredSize(new Dimension(180,588));
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "效果灯模式", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		leftPane.setBorder(tb);
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
		flowLayout.setVgap(3);
		flowLayout.setHgap(0);
		leftPane.setLayout(flowLayout);
		
		JToggleButton[] buttons = new JToggleButton[24];
		JButton[] btns = new JButton[6];
		ButtonGroup group = new ButtonGroup();
		String temp = "";
		DMXModelListener listener = new DMXModelListener();
		for (int i = 0; i < 30; i++) {
			if (i <= 23) {
				temp = String.valueOf(i+1);
				if (i+1 < 10) {
					temp = "0" + String.valueOf(i+1);
				}
				buttons[i] = new JToggleButton("DMX"+temp);
				buttons[i].setFocusable(false);
				buttons[i].setPreferredSize(new Dimension(84,34));
				buttons[i].addActionListener(listener);
				group.add(buttons[i]);
				leftPane.add(buttons[i]);
			} else {
				if (i <= 27) {
					btns[i-24] = new JButton("雾机"+String.valueOf(i-23));
				} else {
					btns[i-24] = new JButton("摇麦"+String.valueOf(i-27));
				}
				btns[i-24].setFocusable(false);
				btns[i-24].setPreferredSize(new Dimension(84,34));
				btns[i-24].addActionListener(this);
				leftPane.add(btns[i-24]);
			}
		}
		buttons[0].setSelected(true);
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().contains("雾机")) {
			WuJiModelUI wuJiModelUI = new WuJiModelUI();
			if ("雾机1".equals(e.getActionCommand())) {
				wuJiModelUI.show(1);
			} else if ("雾机2".equals(e.getActionCommand())) {
				wuJiModelUI.show(2);
			} else if ("雾机3".equals(e.getActionCommand())) {
				wuJiModelUI.show(3);
			} else if ("雾机4".equals(e.getActionCommand())) {
				wuJiModelUI.show(4);
			}
		} else {
			YaoMaiModelUI yaoMaiModelUI = new YaoMaiModelUI();
			if ("摇麦1".equals(e.getActionCommand())) {
				yaoMaiModelUI.show(1);
			} else if ("摇麦2".equals(e.getActionCommand())) {
				yaoMaiModelUI.show(2);
			}
		}
	}

	private void setRightPane(JPanel parentPane) {
		parentPane.setPreferredSize(new Dimension(830,590));
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setVgap(13);
		parentPane.setLayout(flowLayout);
		//parentPane.setBorder(new LineBorder(Color.gray));
		
		JPanel rightPane = new JPanel();
		CardLayout cardLayout = new CardLayout();
		MainUi.map.put("XiaoGuoRightPane_8", rightPane);
		MainUi.map.put("XiaoGuoCardLayout_8", cardLayout);
		rightPane.setLayout(cardLayout);
		JPanel[] rightPanes = new JPanel[24];
		for (int i = 0; i < rightPanes.length; i++) {
			rightPanes[i] = new JPanel();
			new RightMainUI().show(rightPanes[i],(i+1));
			rightPane.add(""+(i+1), rightPanes[i]);
		}
		//rightPane.setPreferredSize(new Dimension(822,574));
		//rightPane.setBorder(new LineBorder(Color.red));
		//new RightMainUI().show(rightPane,1);
		parentPane.add(rightPane);
		
	}
}
