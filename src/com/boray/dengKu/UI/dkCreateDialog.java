package com.boray.dengKu.UI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.boray.Data.Data;
import com.boray.mainUi.MainUi;


public class dkCreateDialog implements ActionListener{
	private JDialog dialog;
	private NewJTable table;
	private JTextField field;
	private JComboBox channelBox;
	private JTextField versionField;
	public void show(NewJTable table){
		this.table = table;
		JFrame f = (JFrame)MainUi.map.get("frame");
		dialog = new JDialog(f,true);
		dialog.setResizable(false);
		dialog.setTitle("新建灯库");
		int w = 380,h = 280;
		dialog.setLocation(f.getLocation().x+f.getSize().width/2-w/2,f.getLocation().y+f.getSize().height/2-h/2);
		dialog.setSize(w,h);
		dialog.setLayout(new FlowLayout(FlowLayout.LEFT));
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		init();
		dialog.setVisible(true);
	}
	private void init(){
		dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
		///////////////
		JPanel panel = new JPanel();
		panel.add(new JLabel("通道数量"));
		channelBox = new JComboBox();
		channelBox.setFocusable(false);
		channelBox.setPreferredSize(new Dimension(180,32));
		for (int i = 0; i < 32; i++) {
			channelBox.addItem(""+(i+1));
		}
		channelBox.setSelectedItem("10");
		panel.add(channelBox);
		////////////////
		JPanel panel2 = new JPanel();
		panel2.add(new JLabel("灯库名称"));
		field = new JTextField(15);
		field.setText("灯库"+String.valueOf(table.getRowCount()+1));
		panel2.add(field);
		
		///////////////////
		JPanel p3_3 = new JPanel();
		p3_3.add(new JLabel("   库版本"));
		versionField = new JTextField(15);
		versionField.setText("1.0");
		p3_3.add(versionField);
		///////////////////
		JPanel panel3 = new JPanel();
		JButton btn = new JButton("确定");
		JButton btn1 = new JButton("取消");
		btn.addActionListener(this);
		btn1.addActionListener(this);
		panel3.add(btn1);
		panel3.add(new JLabel("    "));
		panel3.add(btn);
		
		
		
		JPanel nullPane = new JPanel();
		nullPane.setPreferredSize(new Dimension(320,36));
		JPanel nullPane2 = new JPanel();
		nullPane2.setPreferredSize(new Dimension(320,16));
		dialog.add(nullPane);
		dialog.add(panel2);
		dialog.add(panel);
		dialog.add(p3_3);
		dialog.add(nullPane2);
		dialog.add(panel3);
	}
	public void actionPerformed(ActionEvent e) {
		if ("确定".equals(e.getActionCommand())) {
			if (table.getRowCount() >= 20) {
				JFrame frame = (JFrame)MainUi.map.get("frame");
				JOptionPane.showMessageDialog(frame, "最多只能创建20个灯库！", "提示", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int c = Integer.valueOf(channelBox.getSelectedItem().toString()).intValue();
			//Data.dengKuName.add(String.valueOf(c));
			Data.DengKuChannelCountList.add(String.valueOf(c));
			Data.DengKuVersionList.add(versionField.getText());
			DefaultTableModel model = (DefaultTableModel)table.getModel();
			String name = field.getText();
			boolean same = false;
			for (int i = 0; i < table.getRowCount(); i++) {
				if (name.equals(table.getValueAt(i, 1).toString())) {
					same = true;
					break;
				}
			}
			if (same) {
				JFrame frame = (JFrame)MainUi.map.get("frame");
				JOptionPane.showMessageDialog(frame, "该灯库名称已存在！", "提示", JOptionPane.ERROR_MESSAGE);
				return;
			}
			int index = table.getRowCount()+1;
			String[] s = {index+"",name};
			model.addRow(s);
			JComboBox[] boxs = (JComboBox[])MainUi.map.get("lamp_1_To_16");
			JComboBox[] boxs2 = (JComboBox[])MainUi.map.get("lamp_17_To_32");
			//JLabel[] labels = (JLabel[])mainUI.map.get("lamp_1_To_16_label");
			//JLabel[] labels2 = (JLabel[])mainUI.map.get("lamp_17_To_32_label");
			HashMap hashMap = new HashMap();
			if (c > 16) {
				for (int i = 0; i < 16; i++) {
					boxs[i].setEnabled(true);
					//boxs[i].setVisible(true);
					//labels[i].setVisible(true);
					hashMap.put(boxs[i].getName(), "X轴");
				}
				for (int i = 0; i < c-16; i++) {
					hashMap.put(boxs2[i].getName(), "X轴");
					boxs2[i].setEnabled(true);
					//boxs2[i].setVisible(true);
					//labels2[i].setVisible(true);
				}
				for (int i = c-16; i < boxs2.length; i++) {
					boxs2[i].setEnabled(false);
					//boxs2[i].setVisible(false);
					//labels2[i].setVisible(false);
				}
			} else {
				for (int i = 0; i < boxs2.length; i++) {
					boxs2[i].setEnabled(false);
					//boxs2[i].setVisible(false);
					//labels2[i].setVisible(false);
				}
				for (int i = 0; i < c; i++) {
					boxs[i].setEnabled(true);
					//boxs[i].setVisible(true);
					//labels[i].setVisible(true);
					hashMap.put(boxs[i].getName(), "X轴");
				}
				for (int i = c; i < boxs.length; i++) {
					boxs[i].setEnabled(false);
					//boxs[i].setVisible(false);
					//labels[i].setVisible(false);
				}
			}
			Data.DengKuList.add(hashMap);
			Data.dengKuBlackOutAndSpeedList.add(null);
			new AddCustomTonDaoDialog().addDengKuTonDaoList();
			table.setRowSelectionInterval(table.getRowCount()-1, table.getRowCount()-1);
			dialog.dispose();
		} else {
			dialog.dispose();
		}
	}
}
