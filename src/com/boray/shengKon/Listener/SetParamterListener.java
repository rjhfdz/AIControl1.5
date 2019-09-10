package com.boray.shengKon.Listener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.boray.Data.Data;
import com.boray.Data.MyData;
import com.boray.mainUi.MainUi;

public class SetParamterListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		show();
	}
	public void show(){
		JDialog dialog = new JDialog();
		final JSlider slider = new JSlider(0,20000);
		final JSlider slider2 = new JSlider(0,20000);
		final JSlider slider3 = new JSlider(0,100);
		final  JTextField field = new JTextField(4);
		final JTextField field2 = new JTextField(4);
		final JTextField field3 = new JTextField(4);
		
		JFrame f = (JFrame)MainUi.map.get("frame");
		dialog = new JDialog(f,true);
		dialog.setResizable(false);
		dialog.setTitle("设置-模式"+MyData.ShengKonModel);
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setVgap(0);
		dialog.setLayout(flowLayout);
		int width = 580,height = 450;
		dialog.setSize(width, height);
		dialog.setLocation(f.getLocation().x+f.getSize().width/2-width/2,f.getLocation().y+f.getSize().height/2-height/2);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				List list = new ArrayList();
				list.add(String.valueOf(slider.getValue()));
				list.add(String.valueOf(slider2.getValue()));
				list.add(String.valueOf(slider3.getValue()));
				Data.ShengKonModelSet.put(MyData.ShengKonModel, list);
			}
		});
		
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "时间轴数据", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		//TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "时间轴数据", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "录制数据", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p1.setBorder(tb);p2.setBorder(tb);p3.setBorder(tb2);
		
		p1.setPreferredSize(new Dimension(560,80));
		p2.setPreferredSize(new Dimension(560,80));
		p3.setPreferredSize(new Dimension(560,80));
		p1.add(new JLabel("低频返回场景延时"));
		p1.add(slider);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				field.setText(String.valueOf(slider.getValue()));
			}
		});
		field.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					slider.setValue(Integer.valueOf(field.getText()).intValue());
				}
			}
		});
		p1.add(field);p1.add(new JLabel("毫秒"));
		
		p2.add(new JLabel("高频返回场景延时"));
		p2.add(slider2);
		slider2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				field2.setText(String.valueOf(slider2.getValue()));
			}
		});
		field2.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					slider2.setValue(Integer.valueOf(field2.getText()).intValue());
				}
			}
		});
		p2.add(field2);p2.add(new JLabel("毫秒"));
		p3.add(new JLabel("             跳帧加速"));
		p3.add(slider3);
		slider3.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				field3.setText(String.valueOf(slider3.getValue()));
			}
		});
		field3.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					slider3.setValue(Integer.valueOf(field3.getText()).intValue());
				}
			}
		});
		p3.add(field3);p3.add(new JLabel("毫秒"));
		List list = (List)Data.ShengKonModelSet.get(MyData.ShengKonModel);
		slider.setValue(0);slider2.setValue(0);slider3.setValue(0);
		if (list != null) {
			slider.setValue(Integer.valueOf(list.get(0).toString()));
			slider2.setValue(Integer.valueOf(list.get(1).toString()));
			slider3.setValue(Integer.valueOf(list.get(2).toString()));
		} else {
			slider.setValue(0);slider2.setValue(0);slider3.setValue(0);
		}
		dialog.add(p1);dialog.add(p2);dialog.add(p3);
		dialog.setVisible(true);
	}
}
