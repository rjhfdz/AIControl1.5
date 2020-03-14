package com.boray.dengKu.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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
import com.boray.Utils.Util;
import com.boray.mainUi.MainUi;

public class DengJuSetDialog implements ActionListener {
    private JDialog dialog;
    private JTextField field;
    private JTextField field2;
    private JComboBox box;
    private JLabel label = new JLabel("0");//灯库型号
    private JLabel label2 = new JLabel("1.0");//库版本

    public void show() {
        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new JDialog(f, true);
        dialog.setResizable(false);
        dialog.setTitle("添加灯具");
        int w = 380, h = 280;
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
        dialog.setSize(w, h);
        dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        init();
        dialog.setVisible(true);
    }

    private void init() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        //panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(270, 40));
        panel.add(new JLabel("灯具名称"));
        field = new JTextField(14);
        panel.add(field);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel2.setPreferredSize(new Dimension(270, 40));
        panel2.add(new JLabel("DMX起始地址"));
        field2 = new JTextField(14);
        panel2.add(field2);

        JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel3.setPreferredSize(new Dimension(270, 40));
        panel3.add(new JLabel("灯库型号"));
        box = new JComboBox();
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        box.removeAllItems();
        if (table.getRowCount() > 0) {
            for (int i = 0; i < table.getRowCount(); i++) {
                box.addItem(table.getValueAt(i, 1).toString());
            }
            label.setText(Data.DengKuChannelCountList.get(0).toString());
            label2.setText(Data.DengKuVersionList.get(0).toString());
        }
        box.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    label.setText(Data.DengKuChannelCountList.get(box.getSelectedIndex()).toString());
                    label2.setText(Data.DengKuVersionList.get(box.getSelectedIndex()).toString());
                    field.setText(box.getSelectedItem().toString());
                }
            }
        });
        box.setPreferredSize(new Dimension(170, 30));
        box.setSelectedIndex(box.getItemCount() - 1);
        panel3.add(box);

        NewJTable table_dengJu = (NewJTable) MainUi.map.get("table_dengJu");
        if (box.getItemCount() > 0) {
            field.setText(box.getItemAt(box.getItemCount() - 1).toString());
        }
        if (table_dengJu.getRowCount() > 0) {
            int last = table_dengJu.getRowCount() - 1;
            int star = Integer.valueOf(table_dengJu.getValueAt(last, 5).toString()).intValue();
            int end = Integer.valueOf(table_dengJu.getValueAt(last, 6).toString()).intValue();
            field2.setText(String.valueOf(star + end));
        } else {
            field2.setText("1");
        }

        JPanel panel4 = new JPanel();
        panel4.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel4.setPreferredSize(new Dimension(266, 40));
        panel4.add(new JLabel("占用通道数"));

        label.setPreferredSize(new Dimension(56, 26));
        label.setOpaque(true);
        label.setBackground(new Color(243, 243, 243));
        panel4.add(label);

        panel4.add(new JLabel("库版本"));
        label2.setPreferredSize(new Dimension(56, 26));
        label2.setOpaque(true);
        label2.setBackground(new Color(243, 243, 243));
        panel4.add(label2);


        JPanel panel5 = new JPanel();
        JButton btn = new JButton("取消");
        JButton btn1 = new JButton("确定");
        btn.addActionListener(this);
        btn1.addActionListener(this);
        panel5.add(btn);
        panel5.add(new JLabel("     "));
        panel5.add(btn1);

        JPanel nullPane = new JPanel();
        nullPane.setPreferredSize(new Dimension(280, 12));
        dialog.add(nullPane);
        dialog.add(panel);
        dialog.add(panel2);
        dialog.add(panel3);
        dialog.add(panel4);
        dialog.add(panel5);
    }

    public void actionPerformed(ActionEvent e) {
        if ("确定".equals(e.getActionCommand())) {
            NewJTable table = (NewJTable) MainUi.map.get("table_dengJu");
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            int index = table.getRowCount();
            if (box.getSelectedItem() != null) {
                if (index == 50) {
                    JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "添加灯具数量不能超过50个！", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    int cnt = Integer.valueOf(field2.getText()).intValue() + Integer.valueOf(label.getText()).intValue();
                    if (cnt > 513) {
                        JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "通道已超出正常512范围！", "提示", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        int srartA = Integer.valueOf(field2.getText()).intValue();
                        int endA = cnt;
                        String str = "";
                        for (int i = 0; i < table.getRowCount(); i++) {
                            int startB = Integer.valueOf(table.getValueAt(i, 5).toString());
                            int endB = startB + Integer.valueOf(table.getValueAt(i, 6).toString());
                            if (Util.checkRepetition(srartA, startB, endA, endB)) {//判断是否重复
                                str += table.getValueAt(i, 2) + " , ";
                            }
                        }
                        if (!str.equals("")) {
                            JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "通道与灯具 " + str + "重复！", "提示", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        //Data.dengJu_change = true;
						/*if (table.getRowCount() == 0) {
							NewJTable table_DMX = (NewJTable)mainUI.map.get("table_DMX_All");
							DefaultTableModel modelTemp = (DefaultTableModel)table_DMX.getModel();
							for (int i = table_DMX.getRowCount() - 1; i >= 0 ; i--) {
								modelTemp.removeRow(i);
							}
							modelTemp.addRow(Data.initValue());
							table_DMX.setRowSelectionInterval(0, 0);
						}*/
                        Object[] s = {new Boolean(true), String.valueOf(index + 1), field.getText(), "ID" + (box.getSelectedIndex() + 1) + "#" + box.getSelectedItem().toString(), label2.getText(), field2.getText(), label.getText()};
                        model.addRow(s);

                        //JScrollPane mScrollPane = (JScrollPane)MainUi.map.get("DengJuScrollPane");
                        //JScrollBar jScrollBar = mScrollPane.getVerticalScrollBar();//获得垂直滚动条
                        //jScrollBar.setValue(table.getY()+table.getHeight());//设置垂直滚动条位置
                    }
                }
            } else {
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "请先创建灯库，再添加灯具", "提示", JOptionPane.ERROR_MESSAGE);
            }
            dialog.dispose();
        } else {
            dialog.dispose();
        }
    }
}
