package com.boray.dengKu.UI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.boray.Data.Data;
import com.boray.mainUi.MainUi;


public class dkChangeDialog implements ActionListener {
    private JDialog dialog;
    private NewJTable table;
    private JTextField field;
    private JComboBox channelBox;
    private JTextField versionField;

    public void show(NewJTable table) {
        this.table = table;
        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new JDialog(f, true);
        dialog.setResizable(false);
        dialog.setTitle("修改灯库");
        int w = 380, h = 280;
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
        dialog.setSize(w, h);
        dialog.setLayout(new FlowLayout(FlowLayout.LEFT));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        init();
    }

    private void init() {
        dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
        ///////////////
        JPanel panel = new JPanel();
        panel.add(new JLabel("通道数量"));
        channelBox = new JComboBox();
        channelBox.setFocusable(false);
        channelBox.setPreferredSize(new Dimension(180, 32));
        for (int i = 0; i < 32; i++) {
            channelBox.addItem("" + (i + 1));
        }
        if (table.getSelectedRow() != -1) {
            channelBox.setSelectedItem(Data.DengKuChannelCountList.get(table.getSelectedRow()).toString());
        } else {
            return;
        }
        panel.add(channelBox);
        ////////////////
        JPanel panel2 = new JPanel();
        panel2.add(new JLabel("灯库名称"));
        field = new JTextField(15);
        if (table.getSelectedRow() != -1) {
            field.setText(table.getValueAt(table.getSelectedRow(), 1).toString());
        }
        panel2.add(field);

        ///////////////////

        JPanel p3_3 = new JPanel();
        p3_3.add(new JLabel("   库版本"));
        versionField = new JTextField(15);
        versionField.setText(Data.DengKuVersionList.get(table.getSelectedRow()).toString());
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
        nullPane.setPreferredSize(new Dimension(320, 36));
        JPanel nullPane2 = new JPanel();
        nullPane2.setPreferredSize(new Dimension(320, 16));
        dialog.add(nullPane);
        dialog.add(panel2);
        dialog.add(panel);
        dialog.add(p3_3);
        dialog.add(nullPane2);
        dialog.add(panel3);
        dialog.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if ("确定".equals(e.getActionCommand())) {
            int c = Integer.valueOf(channelBox.getSelectedItem().toString()).intValue();
            //Data.dengKuName.set(table.getSelectedRow(),String.valueOf(c));
            String name = field.getText();

            boolean same = false;
            for (int i = 0; i < table.getRowCount(); i++) {
                if (table.getSelectedRow() != i && name.equals(table.getValueAt(i, 1).toString())) {
                    same = true;
                    break;
                }
            }
            if (same) {
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "该灯库名称已存在！", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
            updateTonDao(table.getSelectedRow(), c);
            Data.DengKuChannelCountList.set(table.getSelectedRow(), String.valueOf(c));
            //Data.dengKu_change = true;
            JTextField field8 = (JTextField) MainUi.map.get("versionField");
            field8.setText(versionField.getText());
            Data.DengKuVersionList.set(table.getSelectedRow(), versionField.getText());
            table.setValueAt(name, table.getSelectedRow(), 1);
            JComboBox[] boxs = (JComboBox[]) MainUi.map.get("lamp_1_To_16");
            JComboBox[] boxs2 = (JComboBox[]) MainUi.map.get("lamp_17_To_32");
            //JLabel[] labels = (JLabel[])mainUI.map.get("lamp_1_To_16_label");
            //JLabel[] labels2 = (JLabel[])mainUI.map.get("lamp_17_To_32_label");
            HashMap hashMap = (HashMap) Data.DengKuList.get(table.getSelectedRow());
            if (c > 16) {
                for (int i = 0; i < 16; i++) {
                    boxs[i].setEnabled(true);
                    //boxs[i].setVisible(true);
                    //labels[i].setVisible(true);
                    if (hashMap.get(boxs[i].getName()) != null) {
                        boxs[i].setSelectedItem(hashMap.get(boxs[i].getName()).toString());
                    } else {
                        boxs[i].setSelectedItem("X轴");
                        hashMap.put(boxs[i].getName(), "X轴");
                    }
                }
                for (int i = 0; i < c - 16; i++) {
                    boxs2[i].setEnabled(true);
					/*boxs2[i].setVisible(true);
					labels2[i].setVisible(true);*/
                    if (hashMap.get(boxs2[i].getName()) != null) {
                        boxs2[i].setSelectedItem(hashMap.get(boxs2[i].getName()).toString());
                    } else {
                        boxs2[i].setSelectedItem("X轴");
                        hashMap.put(boxs2[i].getName(), "X轴");
                    }
                }
                for (int i = c - 16; i < boxs2.length; i++) {
                    boxs2[i].setEnabled(false);
					/*boxs2[i].setVisible(false);
					labels2[i].setVisible(false);*/
                }
            } else {
                for (int i = 0; i < boxs2.length; i++) {
                    boxs2[i].setEnabled(false);
					/*boxs2[i].setVisible(false);
					labels2[i].setVisible(false);*/
                }
                for (int i = 0; i < c; i++) {
                    boxs[i].setEnabled(true);
					/*boxs[i].setVisible(true);
					labels[i].setVisible(true);*/
                    if (hashMap.get(boxs[i].getName()) != null) {
                        boxs[i].setSelectedItem(hashMap.get(boxs[i].getName()).toString());
                    } else {
                        boxs[i].setSelectedItem("X轴");
                        hashMap.put(boxs[i].getName(), "X轴");
                    }
                }
                for (int i = c; i < boxs.length; i++) {
                    boxs[i].setEnabled(false);
					/*boxs[i].setVisible(false);
					labels[i].setVisible(false);*/
                }
            }
            NewJTable table8 = (NewJTable) MainUi.map.get("table_dengJu");
            for (int i = 0; i < table8.getRowCount(); i++) {
                int a = Integer.valueOf(table8.getValueAt(i, 3).toString().split("#")[0].substring(2)).intValue();
                if ((a - 1) == table.getSelectedRow()) {
                    table8.setValueAt("ID" + a + "#" + name, i, 3);
                    table8.setValueAt(versionField.getText(), i, 4);
                    table8.setValueAt(String.valueOf(c), i, 6);
                }
            }
            new AddCustomTonDaoDialog().addDengKuTonDaoList();
            //table.setRowSelectionInterval(table.getRowCount()-1, table.getRowCount()-1);
            dialog.dispose();
        } else {
            dialog.dispose();
        }
    }

    /**
     * 修改灯库通道，同时修改素材通道以及步数
     *
     * @param s
     * @param c
     */
    public void updateTonDao(int s, int c) {
        int str = Integer.parseInt(Data.DengKuChannelCountList.get(s).toString());
        if (c != str) {
            for (int i = 0; i < 30; i++) {
                HashMap hashMap = (HashMap) Data.SuCaiObjects[s][i];
                if (hashMap != null) {
                    List list66 = (List) hashMap.get("channelData");
                    Vector vector88 = (Vector) list66.get(0);
                    for (int j = 0; j < vector88.size(); j++) {
                        Vector tpe = (Vector) vector88.get(j);
                        int b = 0;
                        if ((c + 2) > tpe.size()) {
                            b = (c + 2) - tpe.size();
                        }
                        for (int k = 0; k < b; k++) {
                            tpe.add("0");
                        }
                    }
                }
            }
        }
    }
}
