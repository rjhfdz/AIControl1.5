package com.boray.dengKu.UI;

import com.boray.Data.Data;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class RDMChangeDialog implements ActionListener {
    private JDialog dialog;
    private JFrame frame;
    private NewJTable table;
    private String[] str;
    private JComboBox box;
    private int select;
    private JTextField field;
    private JLabel label2 = new JLabel();

    public void show(NewJTable table, String[] str) {
        this.table = table;
        this.str = str;
        frame = (JFrame) MainUi.map.get("frame");
        dialog = new JDialog(frame, true);
        dialog.setResizable(false);
        dialog.setTitle("修改");
        int w = 380, h = 250;
        dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
        dialog.setSize(w, h);
        dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        init();
        dialog.setVisible(true);
    }

    private void init() {
        select = table.getSelectedRow();
        if (select == -1) {
            return;
        }
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel.setPreferredSize(new Dimension(270, 40));
        panel.add(new JLabel("灯具名称"));
        field = new JTextField(14);
        field.setText(table.getValueAt(select, 2).toString());
        panel.add(field);

        JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel3.setPreferredSize(new Dimension(270, 40));
        panel3.add(new JLabel("型号"));
        box = new JComboBox(str);
        box.setSelectedItem(table.getValueAt(select, 3));
        panel3.add(box);
        box.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    label2.setText(Data.DengKuVersionList.get(box.getSelectedIndex()).toString());
                }
            }
        });
        box.setPreferredSize(new Dimension(170, 30));
        JPanel panel4 = new JPanel();
        panel4.add(new JLabel("库版本"));
        panel4.setPreferredSize(new Dimension(270, 40));
        label2.setPreferredSize(new Dimension(56, 26));
        label2.setOpaque(true);
        label2.setBackground(new Color(243, 243, 243));
        label2.setText(table.getValueAt(select, 4).toString());
        panel4.add(label2);

        JPanel panel5 = new JPanel();
        JButton btn = new JButton("取消");
        JButton btn1 = new JButton("确定");
        btn.addActionListener(this);
        btn1.addActionListener(this);
        panel5.add(btn1);
        panel5.add(new JLabel("     "));
        panel5.add(btn);

        dialog.add(panel);
        dialog.add(panel3);
        dialog.add(panel4);
        dialog.add(panel5);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("确定".equals(e.getActionCommand())) {
            table.setValueAt(field.getText(), select, 2);//灯具名称
            table.setValueAt(box.getSelectedItem().toString(), select, 3);//型号
            table.setValueAt(label2.getText(), select, 4);//版本
            dialog.dispose();
        } else {
            dialog.dispose();
        }
    }
}
