package com.boray.beiFen.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import com.boray.beiFen.Listener.BackupActionListener;
import com.boray.beiFen.Listener.DataActionListener;
import com.boray.beiFen.Listener.LoadDMXactionListener;
import com.boray.beiFen.Listener.LoadProjectFileActionListener;
import com.boray.beiFen.Listener.LoadToDeviceActionListener;
import com.boray.beiFen.Listener.MergeAllListener;
import com.boray.beiFen.Listener.ProjectCreateFileActionListener;
import com.boray.mainUi.MainUi;

public class BeiFenUI {
	public void show(JPanel pane) {
		pane.setLayout(new FlowLayout(FlowLayout.LEFT));
		JPanel head = new JPanel();
		head.setLayout(new FlowLayout(FlowLayout.LEFT));
		head.setPreferredSize(new Dimension(430,80));
		TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "工程的生成与加载", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		head.setBorder(tb1);
		
		JPanel head2 = new JPanel();
		head2.setLayout(new FlowLayout(FlowLayout.LEFT));
		head2.setPreferredSize(new Dimension(484,80));
		TitledBorder tb8 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "控制器文件生成", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		head2.setBorder(tb8);
		
		JButton button = new JButton("生成配置数据");
		//button.addActionListener(new DataActionListener());
		JButton button2 = new JButton("生成灯控数据");
		//button2.addActionListener(new DataMergeListener());
		JButton button3 = new JButton("生成控制器文件");
		button3.addActionListener(new MergeAllListener());
		JButton button4 = new JButton("加载工程");
		button4.addActionListener(new LoadProjectFileActionListener());
		//button4.addActionListener(new LoadActionListener());
		JButton button8 = new JButton("保存工程");
		button8.addActionListener(new ProjectCreateFileActionListener());
		//button8.addActionListener(new DataAnalyseOfBeiFenListener());
		button.setPreferredSize(new Dimension(120,34));
		button2.setPreferredSize(new Dimension(120,34));
		button3.setPreferredSize(new Dimension(120,34));
		button4.setPreferredSize(new Dimension(120,34));
		button8.setPreferredSize(new Dimension(120,34));
		//head.add(button);
		//head.add(button2);
		head.add(button4);
		head.add(button8);
		JPanel n1 = new JPanel();
		n1.setPreferredSize(new Dimension(200,40));
		head.add(n1);
		head2.add(button3);
		pane.add(head);
		pane.add(head2);
		
		JPanel p1 = new JPanel();
		TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "数据加载到控制器", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p1.setBorder(tb2);
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setVgap(4);
		p1.setLayout(flowLayout);
		//p1.setPreferredSize(new Dimension(920,180));
		p1.setPreferredSize(new Dimension(920,120));
		
		JPanel p1_1 = new JPanel();
		p1_1.setPreferredSize(new Dimension(420,80));
		p1_1.setLayout(flowLayout);
		TitledBorder tb5 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "配置", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p1_1.setBorder(tb5);
		//p1.setBorder(new LineBorder(Color.gray));
		JButton btn = new JButton("加载配置");
		btn.addActionListener(new LoadToDeviceActionListener());
		btn.setPreferredSize(new Dimension(120,34));
		JRadioButton radioButton = new JRadioButton("灯光");
		JRadioButton radioButton2 = new JRadioButton("空调");
		JRadioButton radioButton3 = new JRadioButton("中控");
		JRadioButton radioButton4 = new JRadioButton("全部");
		List list = new ArrayList();
		list.add(radioButton);list.add(radioButton2);
		list.add(radioButton3);list.add(radioButton4);
		MainUi.map.put("LoadSetListComponent", list);
		radioButton.setSelected(true);
		ButtonGroup group = new ButtonGroup();
		group.add(radioButton);group.add(radioButton2);
		group.add(radioButton3);group.add(radioButton4);
		p1_1.add(btn);p1_1.add(radioButton);p1_1.add(radioButton2);
		p1_1.add(radioButton3);p1_1.add(radioButton4);
		JPanel np = new JPanel();
		np.setPreferredSize(new Dimension(500,20));
		//np.setBorder(new LineBorder(Color.gray));
		//p1.add(np);
		BackupActionListener backupActionListener = new BackupActionListener();
		JButton button5 = new JButton("备份配置");
		button5.addActionListener(backupActionListener);
		button5.setPreferredSize(new Dimension(120,34));
		//p1.add(button5);
		//p1.add(new JLabel("全部"));
		p1.add(p1_1);
		
		
		JPanel p2 = new JPanel();
		TitledBorder tb3 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "效果灯", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p2.setBorder(tb3);
		p2.setLayout(flowLayout);
		p2.setPreferredSize(new Dimension(420,80));
		JButton button6 = new JButton("加载DMX");
		JButton button7 = new JButton("备份DMX");
		button6.addActionListener(new LoadDMXactionListener());
		button7.addActionListener(backupActionListener);
		button6.setPreferredSize(new Dimension(120,34));
		button7.setPreferredSize(new Dimension(120,34));
		p2.add(button6);//p2.add(button7);
		//p1.add(p2);
		
		//pane.add(p2);
		pane.add(p1);
	}
}
