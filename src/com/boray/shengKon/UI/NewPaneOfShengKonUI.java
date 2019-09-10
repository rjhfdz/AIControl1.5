package com.boray.shengKon.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.boray.shengKon.Listener.OverDmxListener;

public class NewPaneOfShengKonUI {
	public void show(JPanel rightPane1,String Number) {
		rightPane1.setPreferredSize(new Dimension(830,520));
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setVgap(13);
		rightPane1.setLayout(flowLayout);
		//rightPane1.setBorder(new LineBorder(Color.gray));
		
		JPanel rightPane = new JPanel();
		rightPane.setPreferredSize(new Dimension(822,524));
		rightPane.setBorder(new LineBorder(Color.gray));
		//rightPane.setPreferredSize(new Dimension(822,574));
		//rightPane.setBorder(new LineBorder(Color.red));
		new Right3().show(rightPane,Number);
		rightPane1.add(rightPane);
		
		
		JPanel p2 = new JPanel();
		p2.setBorder(BorderFactory.createEmptyBorder(-12,-7,0,0));
		
		JPanel p1 = new JPanel();
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "声控多灯运行", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p1.setBorder(tb);
		p1.setPreferredSize(new Dimension(210,46));
		FlowLayout flowLayout2 = new FlowLayout(FlowLayout.CENTER);
		flowLayout2.setVgap(-12);
		p1.setLayout(flowLayout2);
		JButton btn = new JButton("多灯运行1");
		JButton btn2 = new JButton("多灯运行2");
		OverDmxListener listener = new OverDmxListener();
		btn.addActionListener(listener);btn2.addActionListener(listener);
		p1.add(btn);p1.add(btn2);
		
		p2.add(p1);
		rightPane1.add(p2);
	}
}
