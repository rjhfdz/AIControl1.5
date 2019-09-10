package com.boray.dengKu.UI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.boray.Data.Data;
import com.boray.mainUi.MainUi;

public class AddGroupUI implements ActionListener{
	private JDialog dialog;
	private JTextField field;
	public void show() {
		JFrame f = (JFrame)MainUi.map.get("frame");
		dialog = new JDialog(f,true);
		dialog.setResizable(false);
		dialog.setTitle("添加分组");
		int w = 380,h = 280;
		dialog.setLocation(f.getLocation().x+f.getSize().width/2-w/2,f.getLocation().y+f.getSize().height/2-h/2);
		dialog.setSize(w,h);
		dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		init();
		dialog.setVisible(true);
	}
	private void init() {
		JPanel p1 = new JPanel();
		p1.setPreferredSize(new Dimension(300, 50));
		p1.add(new JLabel("组别名称"));
		field = new JTextField(12);
		p1.add(field);
		
		JPanel p2 = new JPanel();
		p2.setPreferredSize(new Dimension(300,50));
		JButton btn = new JButton("取消");
		JButton btn2 = new JButton("确定");
		btn.addActionListener(this);btn2.addActionListener(this);
		p2.add(btn);p2.add(new JLabel("      "));p2.add(btn2);
		
		JPanel N1 = new JPanel();
		N1.setPreferredSize(new Dimension(300,60));
		dialog.add(N1);
		dialog.add(p1);dialog.add(p2);
	}
	public void actionPerformed(ActionEvent e) {
		if ("确定".equals(e.getActionCommand())) {
			String s = field.getText().trim();
			if (!s.equals("")) {
				NewJTable table = (NewJTable)MainUi.map.get("GroupTable");
				if (table.getRowCount() < 30) {
					DefaultTableModel model = (DefaultTableModel)table.getModel();
					Object[] objects = {new Boolean(true),table.getRowCount()+1,s};
					model.addRow(objects);
					//List list = new ArrayList();
					TreeSet treeSet = new TreeSet<>();
					Data.GroupOfLightList.add(treeSet);
					table.setRowSelectionInterval(table.getRowCount()-1, table.getRowCount()-1);
					dialog.dispose();
				} else {
					JOptionPane.showMessageDialog((JFrame)MainUi.map.get("frame"), "分组数量不能超过30组！", "提示", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		} else {
			dialog.dispose();
		}
	}
}
