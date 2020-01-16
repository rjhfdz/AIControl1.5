package com.boray.shengKon.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.boray.Data.Data;
import com.boray.Data.MyData;
import com.boray.Data.XiaoGuoDengModel;
import com.boray.fileCompare.Compare;
import com.boray.mainUi.MainUi;
import com.boray.shengKon.Listener.CreateTimeBlock_shengKon_Listener;
import com.boray.shengKon.Listener.ModelCopyOfShengKonListener;
import com.boray.shengKon.Listener.SetParamterListener;
import com.boray.xiaoGuoDeng.UI.RightMainUI;
import com.boray.xiaoGuoDeng.reviewCode.ReviewUtils;

public class Right3 {
	private JScrollPane topPane,bottom_leftPane;
	private String Number = "";
	public void show(JPanel pane,String Number){
		this.Number = Number;
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		pane.setLayout(flowLayout);
		
		
		JPanel p1 = new JPanel();
		p1.setLayout(flowLayout);
		//p1.setBorder(new LineBorder(Color.gray));
		p1.setPreferredSize(new Dimension(810,32));
		final JButton button = new JButton("预览");
		JButton button8 = new JButton("设置");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
					public void run() {
						button.setEnabled(false);
						if (Data.serialPort != null) {
							if (Data.file!=null) {
								new Compare().saveTemp();
								Compare.compareFile();
								ReviewUtils.sendReviewCode();
								ReviewUtils.modeReviewOrderByVoice(Integer.valueOf(MyData.ShengKonModel).intValue()-1);
							} else {
								JFrame frame = (JFrame)MainUi.map.get("frame");
								JOptionPane.showMessageDialog(frame, "请先生成初始版本的控制器文件导入到控制器，再进行预览！", "提示", JOptionPane.ERROR_MESSAGE);
							}
						}
						button.setEnabled(true);
					}
				}).start();
			}
		});
		button8.addActionListener(new SetParamterListener());
		p1.add(button);
		p1.add(button8);
		JPanel N1 = new JPanel();
		//N1.setBorder(new LineBorder(Color.gray));
		N1.setPreferredSize(new Dimension(102,24));
		p1.add(N1);
		
		p1.add(new JLabel("调用灯光场景:"));
		JRadioButton radioButton = new JRadioButton("开");
		JRadioButton radioButton2 = new JRadioButton("关");
		ButtonGroup group = new ButtonGroup();
		group.add(radioButton);group.add(radioButton2);
		JComboBox box3 = new JComboBox();
		for (int i = 1; i < 25; i++) {
			box3.addItem(""+i);
		}
		radioButton2.setSelected(true);
		box3.setSelectedIndex(0);
		List list = new ArrayList();
		list.add(radioButton);
		list.add(radioButton2);
		list.add(box3);
		MainUi.map.put("callLightScen"+Number, list);
		p1.add(radioButton);
		p1.add(radioButton2);
		p1.add(box3);
		
		JPanel N2 = new JPanel();
		//N1.setBorder(new LineBorder(Color.gray));
		N2.setPreferredSize(new Dimension(128,24));
		p1.add(N2);
		
		p1.add(new JLabel("源场景"));
		JComboBox box = new JComboBox();
		for (int i = 1; i <= 16; i++) {
			box.addItem(String.valueOf(i));
		}
		JButton button2 = new JButton("复制到当前");
		button2.addActionListener(new ModelCopyOfShengKonListener(box));
		p1.add(box);p1.add(button2);
		
		////////////////////////////////
		JPanel topLeftPane = new JPanel();
		//topLeftPane.setBorder(new LineBorder(Color.gray));
		topLeftPane.setPreferredSize(new Dimension(100,32));
		topPane = new JScrollPane();
		setTopPane(topPane);
		bottom_leftPane = new JScrollPane();
		setBottom_leftPane(bottom_leftPane);
		JScrollPane bottom_rightPane = new JScrollPane();
		setBottom_rightPane(bottom_rightPane);
		////////////////////////////////
		
		pane.add(p1);
		pane.add(topLeftPane);
		pane.add(topPane);
		pane.add(bottom_leftPane);
		pane.add(bottom_rightPane);
	}
	private void setTopPane(JScrollPane scrollPane){
		scrollPane.setBorder(BorderFactory.createMatteBorder(0,0,0,0,Color.gray));
		//scrollPane.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.gray));
		scrollPane.setPreferredSize(new Dimension(720,32));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		JPanel rulePane = new JPanel();
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setVgap(0);
		flowLayout.setHgap(0);
		rulePane.setLayout(flowLayout);
		
		rulePane.setPreferredSize(new Dimension(12560,30));
		
		try {
			if (RightMainUI.image == null) {
				RightMainUI.image = ImageIO.read(getClass().getResource("/image/rule.png"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JPanel ruleLabe = new JPanel(true){
			protected void paintComponent(Graphics g) {
				try {
					if (RightMainUI.image != null) {
						g.drawImage(RightMainUI.image,0,0,null);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		ruleLabe.setPreferredSize(new Dimension(12450,30));
		MainUi.map.put("ruleLabe_shengKon"+Number, ruleLabe);
		rulePane.add(ruleLabe);
		
		scrollPane.setViewportView(rulePane);
	}
	private void setBottom_leftPane(JScrollPane scrollPane){
		//scrollPane.setPreferredSize(new Dimension(100,508));
		scrollPane.setPreferredSize(new Dimension(100,458));
		scrollPane.setBorder(BorderFactory.createMatteBorder(0,0,0,0,Color.gray));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		JPanel pane = new JPanel();
		//pane.setPreferredSize(new Dimension(94,1130));
		pane.setPreferredSize(new Dimension(92,1110));
		JLabel[] labels = new JLabel[31];
		MainUi.map.put("labels_shengKong"+Number, labels);
		for (int i = 0; i < labels.length; i++) {
			if (i == 0) {
				labels[i] = new JLabel("时间分配");
			} else {
				labels[i] = new JLabel("组"+i);
			}
			labels[i].setOpaque(true);
			labels[i].setBackground(new Color(243,243,243));
			labels[i].setPreferredSize(new Dimension(88,30));
			labels[i].setHorizontalAlignment(SwingConstants.CENTER); 
			labels[i].setBorder(new LineBorder(Color.gray));
			pane.add(labels[i]);
		}
		scrollPane.setViewportView(pane);
	}
	private void setBottom_rightPane(JScrollPane scrollPane){
		//scrollPane.setPreferredSize(new Dimension(720,508));
		scrollPane.setPreferredSize(new Dimension(720,458));
		scrollPane.setBorder(BorderFactory.createMatteBorder(0,0,0,0,Color.gray));
		scrollPane.getHorizontalScrollBar().setUnitIncrement(30);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		//scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		//scrollPane.setBorder(new LineBorder(Color.black));
		JPanel parentPane = new JPanel();
		MainUi.map.put("shengKonParentPane"+Number, parentPane);
		parentPane.setPreferredSize(new Dimension(10000,1096));
		JPanel[] timeBlockPanels = new JPanel[31];
		MainUi.map.put("timeBlockPanels"+Number, timeBlockPanels);
		//CreateTimeBlockListener[] listeners = new CreateTimeBlockListener[31];
		CreateTimeBlock_shengKon_Listener listener = new CreateTimeBlock_shengKon_Listener();
		//FlowLayout flowLayout3 = new FlowLayout(FlowLayout.LEFT);
		//flowLayout3.setHgap(0);flowLayout3.setVgap(0);
		for (int i = 0; i < timeBlockPanels.length; i++) {
			timeBlockPanels[i] = new JPanel();
			timeBlockPanels[i].setLayout(null);
			timeBlockPanels[i].setBorder(new LineBorder(Color.gray));
			timeBlockPanels[i].setPreferredSize(new Dimension(10000,30));
			timeBlockPanels[i].setBackground(new Color(192,192,192));
			timeBlockPanels[i].setOpaque(true);
			timeBlockPanels[i].setName(""+i);
			//listeners[i] = new CreateTimeBlockListener();
			if (i == 0) {
				timeBlockPanels[i].addMouseListener(listener);
			}
			parentPane.add(timeBlockPanels[i]);
		}
		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				bottom_leftPane.getVerticalScrollBar().setValue(e.getValue());
			}
		});
		scrollPane.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				topPane.getHorizontalScrollBar().setValue(e.getValue());
			}
		});
		scrollPane.setViewportView(parentPane);
	}
}
