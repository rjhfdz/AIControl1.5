package com.boray.quanJu.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.mainUi.MainUi;
import com.boray.quanJu.Listener.ComBoxItemListener;
import com.boray.quanJu.Listener.TiaoGuangDialog;

public class QuanJuUI implements ActionListener{
	private JComboBox box,box2,box3,box4;//������,�ƹ�Э������,�ֽڼ����֡�����
	private JRadioButton radioButton4,radioButton5,radioButton6,radioButton7;
	public void show(JPanel pane) {
		ComBoxItemListener listener = new ComBoxItemListener();
		List list = new ArrayList();
		MainUi.map.put("quanJuComponeList", list);
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "��������", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		pane.setBorder(tb);
		pane.setLayout(new FlowLayout(FlowLayout.LEFT));
		TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "������", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		JPanel p1 = new JPanel();
		p1.setBorder(tb1);
		p1.setPreferredSize(new Dimension(180,80));
		p1.add(new JLabel("���ڲ�����:"));
		box = new JComboBox(new String[]{"4800","9600"});
		box.setName("������");
		box.addItemListener(listener);
		list.add(box);
		p1.add(box);
		
		JPanel p2 = new JPanel();
		TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Э������", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p2.setBorder(tb2);
		p2.setPreferredSize(new Dimension(200,80));
		p2.add(new JLabel("�ƹ�Э������:"));
		box2 = new JComboBox(new String[]{"����","SY","K-250"});
		box2.setName("Э������");
		box2.addItemListener(listener);
		list.add(box2);
		p2.add(box2);
		
		JPanel p3 = new JPanel();
		p3.setPreferredSize(new Dimension(400,80));
		TitledBorder tb3 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "DMX512Ƶ������", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p3.setBorder(tb3);
		p3.add(new JLabel("�ֽڼ��:"));
		box3 = new JComboBox();
		box4 = new JComboBox();
		box3.setName("�ֽڼ��");
		box4.setName("֡�����");
		list.add(box3);list.add(box4);
		for (int i = 0; i < 201; i++) {
			box3.addItem(String.valueOf(i));
			box4.addItem(String.valueOf(i));
		}
		box3.addItemListener(listener);box4.addItemListener(listener);
		p3.add(box3);p3.add(new JLabel("(΢��)    "));
		p3.add(new JLabel("֡�����:"));
		p3.add(box4);p3.add(new JLabel("(����)"));
		
		JPanel p4 = new JPanel();
		TitledBorder tb4 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "��������", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p4.setBorder(tb4);
		p4.setPreferredSize(new Dimension(200,80));
		JButton button = new JButton("��������");
		button.addActionListener(new TiaoGuangDialog());
		button.setPreferredSize(new Dimension(128,36));
		p4.add(button);
		
		JPanel p5 = new JPanel();
		TitledBorder tb5 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "������ӳ������", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p5.setBorder(tb5);
		p5.setPreferredSize(new Dimension(385,80));
		p5.add(new JLabel("�ܿ���:"));
		JRadioButton radioButton = new JRadioButton("��");
		JRadioButton radioButton1 = new JRadioButton("��");
		p5.add(radioButton);p5.add(radioButton1);
		p5.add(new JLabel("       ӳ����ʼ��ַ:"));
		JComboBox box5 = new JComboBox();
		for (int i = 1; i < 513; i++) {
			box5.addItem(String.valueOf(i));
		}
		p5.add(box5);
		
		JPanel p6 = new JPanel();
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
		p6.setLayout(flowLayout);
		TitledBorder tb6 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Ч�������ݱ������", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p6.setBorder(tb6);
		p6.setPreferredSize(new Dimension(340,80));
		p6.add(new JLabel("Ч��������ģʽ:"));
		JRadioButton radioButton2 = new JRadioButton("ʱ����");
		JRadioButton radioButton3 = new JRadioButton("��֡����");
		radioButton2.setEnabled(false);radioButton3.setEnabled(false);
		list.add(radioButton2);list.add(radioButton3);
		ButtonGroup group3 = new ButtonGroup();
		group3.add(radioButton2);group3.add(radioButton3);
		p6.add(radioButton2);p6.add(radioButton3);
		
		JPanel p7 = new JPanel();
		p7.setPreferredSize(new Dimension(495,80));
		p7.setLayout(new FlowLayout(FlowLayout.LEFT));
		TitledBorder tb7 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "ҡ��ģʽ1", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p7.setBorder(tb7);
		radioButton4 = new JRadioButton("����");
		radioButton5 = new JRadioButton("ѭ��");
		radioButton4.setSelected(true);
		radioButton4.setName("1");radioButton5.setName("2");
		radioButton4.addActionListener(this);
		radioButton5.addActionListener(this);
		list.add(radioButton4);list.add(radioButton5);
		ButtonGroup group = new ButtonGroup();
		group.add(radioButton4);group.add(radioButton5);
		p7.add(new JLabel("ҡ������ģʽ1��"));
		p7.add(radioButton4);p7.add(radioButton5);
		
		JPanel p8 = new JPanel();
		p8.setPreferredSize(new Dimension(495,80));
		p8.setLayout(new FlowLayout(FlowLayout.LEFT));
		TitledBorder tb8 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "ҡ��ģʽ2", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p8.setBorder(tb8);
		radioButton6 = new JRadioButton("����");
		radioButton7 = new JRadioButton("ѭ��");
		radioButton6.setSelected(true);
		radioButton6.setName("3");radioButton7.setName("4");
		radioButton6.addActionListener(this);
		radioButton7.addActionListener(this);
		list.add(radioButton6);list.add(radioButton7);
		ButtonGroup group2 = new ButtonGroup();
		group2.add(radioButton6);group2.add(radioButton7);
		p8.add(new JLabel("ҡ������ģʽ2��"));
		p8.add(radioButton6);p8.add(radioButton7);
		
		
		JPanel p9 = new JPanel();
		p9.setLayout(new FlowLayout(FlowLayout.LEFT));
		p9.setPreferredSize(new Dimension(994,200));
		TitledBorder tb9 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "�豸��Ϣ", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p9.setBorder(tb9);
		JLabel label = new JLabel();
		//JLabel label2 = new JLabel();
		p9.add(label);//p9.add(label2);
		MainUi.map.put("DeviceLabel", label);
		
		pane.add(p1);
		pane.add(p2);
		pane.add(p3);
		pane.add(p4);
		//pane.add(p5);
		//pane.add(p6);
		pane.add(p7);
		pane.add(p8);
		pane.add(p9);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (Data.serialPort != null) {
			byte[] b = null;
			int[] tt = new int[6];
			if (box.getSelectedIndex() == 0) {
				tt[0] = 72;
			} else {
				tt[0] = 150;
			}
			tt[1] = box2.getSelectedIndex()+1;
			tt[2] = box3.getSelectedIndex();
			tt[3] = box4.getSelectedIndex();
			if (radioButton4.isSelected()) {
				tt[4] = 0;
			} else {
				tt[4] = 1;
			}
			if (radioButton6.isSelected()) {
				tt[5] = 0;
			} else {
				tt[5] = 1;
			}
			b = ZhiLingJi.setQuanJu(tt);
			try {
				OutputStream os = Data.serialPort.getOutputStream();
				os.write(b);
				os.flush();
				os.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
