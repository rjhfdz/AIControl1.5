package com.boray.shengKon.UI;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.boray.Data.Data;
import com.boray.Data.MyData;
import com.boray.mainUi.MainUi;
import com.boray.shengKon.Listener.OverDmxListener;

public class ShengKonUI implements ActionListener{
	private CardLayout cardLayout;
	private JPanel rightParentPane;
	private JSlider slider;
	private JRadioButton radioButton,radioButton2,radioButton3;//执行方式
	private JComboBox[] boxs1 ;//灯开关
	private JSlider[] sliders ;//灯亮度值
	private JComboBox box2;//步骤数
	private JComboBox box;//步骤号
	private JLabel loopLabel ;//循环label
	private JComboBox box3_loop;//循环box
	public void show(JPanel pane) {
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setVgap(-5);
		pane.setLayout(flowLayout);
		JPanel leftPane = new JPanel();
		JPanel[] rightPanes = new JPanel[16];
		JPanel rightPane2 = new JPanel();
		//JPanel rightPane3 = new JPanel();
		setRightPane2(rightPane2);
		//setRightPane3(rightPane3);
		rightParentPane = new JPanel();
		cardLayout = new CardLayout();
		rightParentPane.setLayout(cardLayout);
		for (int i = 0; i < rightPanes.length; i++) {
			rightPanes[i] = new JPanel();
			new NewPaneOfShengKonUI().show(rightPanes[i],String.valueOf(i+1));
			rightParentPane.add("XiaoGuoDeng"+String.valueOf(i+1), rightPanes[i]);
		}
		
		rightParentPane.add("2", rightPane2);
		//rightParentPane.add("3", rightPane3);
		setLeftPane(leftPane);
		
		pane.add(leftPane);
		pane.add(rightParentPane);
	}
	
	private void setRightPane3(JPanel rightPane3) {
		rightPane3.setPreferredSize(new Dimension(830,590));
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setVgap(13);
		rightPane3.setLayout(flowLayout);
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.setPreferredSize(new Dimension(822,574));
		panel.setBorder(new LineBorder(Color.gray));
		
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT));
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "声控", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p1.setBorder(tb);
		p1.setPreferredSize(new Dimension(600,130));
		p1.add(new JLabel("步骤号:"));
		JComboBox box_hdc = new JComboBox();
		box_hdc.setPreferredSize(new Dimension(88,32));
		p1.add(box_hdc);
		p1.add(new JLabel("总步骤:"));
		JComboBox box2_hdc = new JComboBox();
		box2_hdc.setPreferredSize(new Dimension(88,32));
		for (int i = 1; i < 21; i++) {
			box2_hdc.addItem(String.valueOf(i));
		}
		p1.add(box2_hdc);
		p1.add(new JLabel("执行方式:"));
		JRadioButton radioButton_hdc = new JRadioButton("单步");
		JRadioButton radioButton2_hdc = new JRadioButton("双步");
		JRadioButton radioButton3_hdc = new JRadioButton("全部");
		ButtonGroup group = new ButtonGroup();
		group.add(radioButton_hdc);group.add(radioButton2_hdc);group.add(radioButton3_hdc);
		p1.add(radioButton_hdc);p1.add(radioButton2_hdc);p1.add(radioButton3_hdc);
		p1.add(new JLabel("执行时长(毫秒):"));
		JSlider slider_hdc = new JSlider(0,5000);
		JTextField field_hdc = new JTextField(4);
		p1.add(slider_hdc);p1.add(field_hdc);
		p1.add(new JLabel("   循环轮数:"));
		JComboBox box3_hdc = new JComboBox();
		for (int i = 1; i < 13; i++) {
			box3_hdc.addItem(String.valueOf(i));
		}
		p1.add(box3_hdc);
		
		JPanel p2 = new JPanel();
		p2.setPreferredSize(new Dimension(600,180));
		JPanel[] panels = new JPanel[8];
		JLabel[] labels = new JLabel[8];
		JComboBox[] boxs1_hdc = new JComboBox[8];
		JSlider[] sliders_hdc = new JSlider[8];
		JComboBox[] boxs2_hdc = new JComboBox[8];
		for (int i = 0; i < boxs2_hdc.length; i++) {
			labels[i] = new JLabel("灯"+(i+1));
			labels[i].setBorder(BorderFactory.createEmptyBorder(-2,0,-2,0));
			//labels[i].setBorder(new LineBorder(Color.black));
			boxs1_hdc[i] = new JComboBox();
			boxs1_hdc[i].setBorder(BorderFactory.createEmptyBorder(-2,0,-2,0));
			boxs1_hdc[i].addItem("开");
			//boxs1[i].addItem("关");
			boxs1_hdc[i].addItem("保持");
			boxs1_hdc[i].setPreferredSize(new Dimension(70,24));
			sliders_hdc[i] = new JSlider(0,100,0);
			sliders_hdc[i].setMinorTickSpacing(2);
			sliders_hdc[i].setOrientation(SwingConstants.VERTICAL);
			sliders_hdc[i].setPreferredSize(new Dimension(24,60));
			boxs2_hdc[i] = new JComboBox();
			boxs2_hdc[i].setPreferredSize(new Dimension(70,24));
			boxs2_hdc[i].setBorder(BorderFactory.createEmptyBorder(-2,0,-2,0));
			for (int j = 0; j <= 100; j++) {
				if (j%2 == 0) {
					boxs2_hdc[i].addItem(String.valueOf(j));
				}
			}
		}
		for (int i = 0; i < panels.length; i++) {
			panels[i] = new JPanel();
			//panels[i].setBorder(new LineBorder(Color.black));
			panels[i].setPreferredSize(new Dimension(68,160));
			panels[i].setLayout(new FlowLayout(FlowLayout.CENTER));
			panels[i].add(labels[i]);panels[i].add(boxs1_hdc[i]);
			panels[i].add(sliders_hdc[i]);panels[i].add(boxs2_hdc[i]);
			p2.add(panels[i]);
		}
		
		panel.add(p1);
		panel.add(p2);
		rightPane3.add(panel);
	}

	private void setRightPane2(JPanel rightPane2) {
		rightPane2.setPreferredSize(new Dimension(830,590));
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setVgap(13);
		rightPane2.setLayout(flowLayout);
		
		//final JRadioButton radioButton,radioButton2,radioButton3;//执行方式
		boxs1 = new JComboBox[8];//灯开关
		sliders = new JSlider[8];//灯亮度值
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.setPreferredSize(new Dimension(822,574));
		panel.setBorder(new LineBorder(Color.gray));
		
		JPanel p1 = new JPanel();
		p1.setLayout(new FlowLayout(FlowLayout.LEFT));
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "声控", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p1.setBorder(tb);
		p1.setPreferredSize(new Dimension(600,130));
		p1.add(new JLabel("步骤号:"));
		box = new JComboBox();
		box.addItem("1");
		box.setPreferredSize(new Dimension(88,32));
		box.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					Map map = (Map)Data.ShengKonHuanJingModelMap.get(MyData.ShengKonHuanJingModel);
					if (map != null) {
						List list = (List)map.get("0");
						int[] tp = (int[])list.get(box.getSelectedIndex());
						slider.setValue(tp[0]);
						for (int i = 0; i < 8; i++) {
							boxs1[i].setSelectedIndex(tp[i+1]);
							sliders[i].setValue(tp[i+9]);
						}
					}
				}
			}
		});
		p1.add(box);
		p1.add(new JLabel("总步骤:"));
		box2 = new JComboBox();
		box2.setPreferredSize(new Dimension(88,32));
		for (int i = 1; i < 21; i++) {
			box2.addItem(String.valueOf(i));
		}
		box2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					if (box2.getSelectedIndex() == 0) {
						box2.setSelectedIndex(1);
						return;
					}
					ItemListener listener = box.getItemListeners()[0];
					box.removeItemListener(listener);
					box.removeAllItems();
					int step = box2.getSelectedIndex()+1;
					for (int i = 1; i <= step; i++) {
						box.addItem(i);
					}
					Map map = (Map)Data.ShengKonHuanJingModelMap.get(MyData.ShengKonHuanJingModel);
					if (map == null) {
						map = new HashMap();
						map.put("3", "0");
						Data.ShengKonHuanJingModelMap.put(MyData.ShengKonHuanJingModel, map);
					}
					List list = (List)map.get("0");
					if (list == null) {
						list = new ArrayList();
						map.put("0",list);
					}
					if (list.size() > step) {
						for (int i = list.size()-1; i > step-1; i--) {
							list.remove(i);
						}
					} else {
						for (int i = list.size(); i < step; i++) {
							int[] tp = new int[17];
							list.add(tp);
						}
					}
					
					if (map != null) {
						List list5 = (List)map.get("0");
						int[] tp = (int[])list5.get(0);
						slider.setValue(tp[0]);
						for (int i = 0; i < 8; i++) {
							boxs1[i].setSelectedIndex(tp[i+1]);
							sliders[i].setValue(tp[i+9]);
						}
					}
					box.setSelectedIndex(0);
					
					box.addItemListener(listener);
					map.put("2",box2.getSelectedItem().toString());
				}
			}
		});
		p1.add(box2);
		p1.add(new JLabel("执行方式:"));
		radioButton = new JRadioButton("单步");
		radioButton2 = new JRadioButton("双步");
		radioButton3 = new JRadioButton("全部");
		ButtonGroup group = new ButtonGroup();
		group.add(radioButton);group.add(radioButton2);group.add(radioButton3);
		radioButton.setSelected(true);
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map map = (Map)Data.ShengKonHuanJingModelMap.get(MyData.ShengKonHuanJingModel);
				if (map != null) {
					if (radioButton.isSelected()) {
						map.put("3", "0");
					} else if (radioButton2.isSelected()) {
						map.put("3", "1");
					} else if (radioButton3.isSelected()) {
						map.put("3", "2");
					}
				}
			}
		};
		radioButton.addActionListener(listener);
		radioButton2.addActionListener(listener);
		radioButton3.addActionListener(listener);
		p1.add(radioButton);p1.add(radioButton2);p1.add(radioButton3);
		p1.add(new JLabel("执行时长(毫秒):"));
		slider = new JSlider(0,5000);
		final JTextField field = new JTextField(4);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				field.setText(String.valueOf(slider.getValue()));
			}
		});
		slider.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				Map map = (Map)Data.ShengKonHuanJingModelMap.get(MyData.ShengKonHuanJingModel);
				if (map != null) {
					List list = (List)map.get("0");
					int[] tp = (int[])list.get(box.getSelectedIndex());
					tp[0] = slider.getValue();
				}
			}
		});
		field.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					int tb = Integer.valueOf(field.getText()).intValue();
					slider.setValue(tb);
					Map map = (Map)Data.ShengKonHuanJingModelMap.get(MyData.ShengKonHuanJingModel);
					if (map != null) {
						List list = (List)map.get("0");
						int[] tp = (int[])list.get(box.getSelectedIndex());
						tp[0] = tb;
					}
				}
			}
		});
		slider.setValue(0);
		p1.add(slider);p1.add(field);
		
		loopLabel = new JLabel("   循环轮数:");
		box3_loop = new JComboBox();
		for (int i = 1; i < 13; i++) {
			box3_loop.addItem(String.valueOf(i));
		}
		box3_loop.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					Map map = (Map)Data.ShengKonHuanJingModelMap.get(MyData.ShengKonHuanJingModel);
					if (map != null) {
						map.put("4", String.valueOf(box3_loop.getSelectedIndex()));
					}
				}
			}
		});
		box3_loop.setVisible(false);
		loopLabel.setVisible(false);
		p1.add(loopLabel);
		p1.add(box3_loop);
		//p1.setBorder(new LineBorder(Color.gray));
		
		JPanel p2 = new JPanel();
		//p2.setBorder(new LineBorder(Color.gray));
		p2.setPreferredSize(new Dimension(600,180));
		JPanel[] panels = new JPanel[8];
		JLabel[] labels = new JLabel[8];
		final JComboBox[] boxs2 = new JComboBox[8];
		for (int i = 0; i < boxs2.length; i++) {
			final int a = i;
			labels[i] = new JLabel("灯"+(i+1));
			labels[i].setBorder(BorderFactory.createEmptyBorder(-2,0,-2,0));
			//labels[i].setBorder(new LineBorder(Color.black));
			boxs1[i] = new JComboBox();
			boxs1[i].setBorder(BorderFactory.createEmptyBorder(-2,0,-2,0));
			boxs1[i].addItem("开");
			//boxs1[i].addItem("关");
			boxs1[i].addItem("保持");
			boxs1[i].setPreferredSize(new Dimension(70,24));
			boxs1[i].addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (ItemEvent.SELECTED == e.getStateChange()) {
						Map map = (Map)Data.ShengKonHuanJingModelMap.get(MyData.ShengKonHuanJingModel);
						if (map != null) {
							List list = (List)map.get("0");
							int[] tp = (int[])list.get(box.getSelectedIndex());
							tp[a+1] = boxs1[a].getSelectedIndex();
						}
					}
				}
			});
			sliders[i] = new JSlider(0,100,0);
			sliders[i].setMinorTickSpacing(2);
			sliders[i].setOrientation(SwingConstants.VERTICAL);
			sliders[i].setPreferredSize(new Dimension(24,60));
			
			boxs2[i] = new JComboBox();
			boxs2[i].setPreferredSize(new Dimension(70,24));
			boxs2[i].setBorder(BorderFactory.createEmptyBorder(-2,0,-2,0));
			for (int j = 0; j <= 100; j++) {
				if (j%2 == 0) {
					boxs2[i].addItem(String.valueOf(j));
				}
			}
			
			sliders[i].addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					int vu = sliders[a].getValue();
					if (vu % 2 != 0) {
						vu = vu + 1;
					}
					sliders[a].setValue(vu);
					ItemListener listener = boxs2[a].getItemListeners()[0];
					boxs2[a].removeItemListener(listener);
					boxs2[a].setSelectedItem(String.valueOf(sliders[a].getValue()));
					boxs2[a].addItemListener(listener);
					
					Map map = (Map)Data.ShengKonHuanJingModelMap.get(MyData.ShengKonHuanJingModel);
					if (map != null) {
						List list = (List)map.get("0");
						int[] tp = (int[])list.get(box.getSelectedIndex());
						tp[9+a] = vu;
					}
				}
			});
			boxs2[i].addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (ItemEvent.SELECTED == e.getStateChange()) {
						sliders[a].setValue(Integer.valueOf(boxs2[a].getSelectedItem().toString()).intValue());
					}
				}
			});
		}
		for (int i = 0; i < panels.length; i++) {
			panels[i] = new JPanel();
			//panels[i].setBorder(new LineBorder(Color.black));
			panels[i].setPreferredSize(new Dimension(68,160));
			panels[i].setLayout(new FlowLayout(FlowLayout.CENTER));
			panels[i].add(labels[i]);panels[i].add(boxs1[i]);
			panels[i].add(sliders[i]);panels[i].add(boxs2[i]);
			p2.add(panels[i]);
		}
		
		panel.add(p1);
		panel.add(p2);
		rightPane2.add(panel);
	}

	private void setLeftPane(JPanel leftPane) {
		leftPane.setPreferredSize(new Dimension(180,588));
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "声控模式", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		leftPane.setBorder(tb);
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
		flowLayout.setVgap(-4);
		flowLayout.setHgap(2);
		leftPane.setLayout(flowLayout);
		
		JToggleButton[] buttons = new JToggleButton[16];
		JToggleButton[] buttons2 = new JToggleButton[18];
		ButtonGroup group = new ButtonGroup();
		for (int i = 0; i < 16; i++) {
			buttons[i] = new JToggleButton("效果灯"+(i+1));
			buttons2[i] = new JToggleButton("环境灯"+(i+1));
			buttons[i].setName("xgd");
			buttons2[i].setName("hjd");
			buttons[i].setFocusable(false);
			buttons2[i].setFocusable(false);
			buttons[i].addActionListener(this);
			buttons2[i].addActionListener(this);
			
			buttons[i].setPreferredSize(new Dimension(80,33));
			buttons[i].setMargin(new Insets(0,-10,0,-10));
			buttons2[i].setPreferredSize(new Dimension(80,33));
			buttons2[i].setMargin(new Insets(0,-10,0,-10));
			
			group.add(buttons[i]);group.add(buttons2[i]);
			leftPane.add(buttons[i]);
			leftPane.add(buttons2[i]);
		}
		buttons[0].setSelected(true);
		
		FlowLayout flowLayout2 = new FlowLayout(FlowLayout.CENTER);
		flowLayout2.setVgap(-10);
		flowLayout2.setHgap(-2);
		
		JPanel panel = new JPanel();
		panel.setLayout(flowLayout2);
		TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "喝彩", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		panel.setBorder(tb2);
		panel.setPreferredSize(new Dimension(170,52));
		JButton button = new JButton("效果灯");
		button.addActionListener(this);
		button.setName("xgd-1");
		buttons2[16] = new JToggleButton("环境灯");
		buttons2[16].addActionListener(this);
		buttons2[16].setName("hjd-1");
		group.add(buttons2[16]);
		button.setPreferredSize(new Dimension(80,32));
		button.setMargin(new Insets(0,-10,0,-10));
		buttons2[16].setPreferredSize(new Dimension(80,32));
		buttons2[16].setMargin(new Insets(0,-10,0,-10));
		panel.add(button);panel.add(buttons2[16]);
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(flowLayout2);
		TitledBorder tb3 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "倒彩", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		panel2.setBorder(tb3);
		panel2.setPreferredSize(new Dimension(170,52));
		JButton button2 = new JButton("效果灯");
		button2.addActionListener(this);
		button2.setName("xgd-2");
		buttons2[17] = new JToggleButton("环境灯");
		buttons2[17].addActionListener(this);
		buttons2[17].setName("hjd-2");
		group.add(buttons2[17]);
		button2.setPreferredSize(new Dimension(80,32));
		button2.setMargin(new Insets(0,-10,0,-10));
		buttons2[17].setPreferredSize(new Dimension(80,32));
		buttons2[17].setMargin(new Insets(0,-10,0,-10));
		panel2.add(button2);panel2.add(buttons2[17]);
		
		/////////////////////////////////////////////////
		/*JPanel p3 = new JPanel();
		TitledBorder tb8 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "叠加DMX", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p3.setBorder(tb8);
		p3.setPreferredSize(new Dimension(170,52));
		
		p3.setLayout(flowLayout2);
		JButton btn = new JButton("DMX1");
		JButton btn2 = new JButton("DMX2");
		btn.setPreferredSize(new Dimension(80,30));
		btn2.setPreferredSize(new Dimension(80,30));
		OverDmxListener listener = new OverDmxListener();
		btn.addActionListener(listener);btn2.addActionListener(listener);
		p3.add(btn);p3.add(btn2);*/
		/////////////////////////////////////////////////
		
		leftPane.add(panel);
		leftPane.add(panel2);
		//leftPane.add(p3);
	}
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			JButton button = (JButton)e.getSource();
			XiaoGuoDengModelUI xiaoGuoDengModelUI = new XiaoGuoDengModelUI();
			if ("xgd-1".equals(button.getName())) {
				xiaoGuoDengModelUI.show("效果灯喝彩模式");
			} else {
				xiaoGuoDengModelUI.show("效果灯倒彩模式");
			}
		} else {
			JToggleButton toggleButton = (JToggleButton)e.getSource();
			if (toggleButton.getName().contains("hjd")) {
				if (!"hjd".equals(toggleButton.getName())) {
					box3_loop.setVisible(true);
					loopLabel.setVisible(true);
					if (toggleButton.getName().equals("hjd-1")) {
						MyData.ShengKonHuanJingModel = "17";
					} else if (toggleButton.getName().equals("hjd-2")) {
						MyData.ShengKonHuanJingModel = "18";
					}
				} else {
					MyData.ShengKonHuanJingModel = toggleButton.getActionCommand().substring(3);
					box3_loop.setVisible(false);
					loopLabel.setVisible(false);
				}
				cardLayout.show(rightParentPane, "2");
				
				Map map = (Map)Data.ShengKonHuanJingModelMap.get(MyData.ShengKonHuanJingModel);
				if (map == null) {
					
					map = new HashMap();
					map.put("3", "0");
					Data.ShengKonHuanJingModelMap.put(MyData.ShengKonHuanJingModel, map);
					List list = new ArrayList();
					map.put("0",list);
					
					int[] tp = new int[17];
					list.add(tp);
					
					slider.setValue(tp[0]);
					for (int i = 0; i < 8; i++) {
						boxs1[i].setSelectedIndex(tp[i+1]);
						sliders[i].setValue(tp[i+9]);
					}
					
					map.put("2","2");
					if (box2.getSelectedIndex() == 1) {
						box2.setSelectedIndex(0);
						try {
							Thread.sleep(10);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						box2.setSelectedIndex(1);
					} else {
						box2.setSelectedIndex(1);
					}
					
					box.setSelectedIndex(0);
					
					radioButton.setSelected(true);
					if ("17".equals(MyData.ShengKonHuanJingModel) || "18".equals(MyData.ShengKonHuanJingModel)) {
						map.put("4","0");
						box3_loop.setSelectedIndex(0);
					}
					
				} else {
					String s = (String)map.get("3");
					if (s.equals("0")) {
						radioButton.setSelected(true);
					} else if (s.equals("1")) {
						radioButton2.setSelected(true);
					} else if (s.equals("2")) {
						radioButton3.setSelected(true);
					}
					
					s = (String)map.get("2");
					if (Integer.valueOf(s).intValue()-1 == box2.getSelectedIndex()) {
						if (box.getSelectedIndex() == 0) {
							if (map != null) {
								List list5 = (List)map.get("0");
								int[] tp = (int[])list5.get(0);
								slider.setValue(tp[0]);
								for (int i = 0; i < 8; i++) {
									boxs1[i].setSelectedIndex(tp[i+1]);
									sliders[i].setValue(tp[i+9]);
								}
							}
						} else {
							box.setSelectedIndex(0);
						}
					} else {
						box2.setSelectedIndex(Integer.valueOf(s).intValue()-1);
					}
					
					if ("17".equals(MyData.ShengKonHuanJingModel) || "18".equals(MyData.ShengKonHuanJingModel)) {
						s = (String)map.get("4");
						box3_loop.setSelectedIndex(Integer.valueOf(s).intValue());
					}
				}
				
				
			} else if ("xgd".equals(toggleButton.getName())) {
				MyData.ShengKonModel = toggleButton.getActionCommand().substring(3);
				cardLayout.show(rightParentPane, "XiaoGuoDeng"+MyData.ShengKonModel);
			} else {
				/*box3_loop.setVisible(true);
				loopLabel.setVisible(true);
				if (toggleButton.getName().equals("hjd-1")) {
					MyData.ShengKonHuanJingModel = "17";
				} else if (toggleButton.getName().equals("hjd-2")) {
					MyData.ShengKonHuanJingModel = "18";
				}
				cardLayout.show(rightParentPane, "2");*/
			}
		}
	}

	private void setRightPane1(JPanel rightPane1) {
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
		new Right3().show(rightPane,"1");
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
