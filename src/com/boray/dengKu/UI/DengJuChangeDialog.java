package com.boray.dengKu.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Iterator;
import java.util.TreeSet;

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

public class DengJuChangeDialog implements ActionListener {
    private JDialog dialog;
    private JTextField field;
    private JTextField field2;
    private JComboBox box;
    private JLabel label = new JLabel("0");
    private JLabel label2 = new JLabel();
    private String str;
    private int select;

    public void show() {
        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new JDialog(f, true);
        dialog.setResizable(false);
        dialog.setTitle("修改灯具");
        int w = 380, h = 280;
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
        dialog.setSize(w, h);
        dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        init();
    }

    private void init() {
        NewJTable table_dengJu = (NewJTable) MainUi.map.get("table_dengJu");
        select = table_dengJu.getSelectedRow();
        if (select == -1) {
            return;
        }
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        //panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(270, 40));
        panel.add(new JLabel("灯具名称"));
        field = new JTextField(14);
        field.setText(table_dengJu.getValueAt(select, 2).toString());
        panel.add(field);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel2.setPreferredSize(new Dimension(270, 40));
        panel2.add(new JLabel("DMX起始地址"));
        field2 = new JTextField(14);
        field2.setText(table_dengJu.getValueAt(select, 5).toString());
        panel2.add(field2);

        JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panel3.setPreferredSize(new Dimension(270, 40));
        panel3.add(new JLabel("型号"));
        box = new JComboBox();
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        //box.removeAllItems();
        if (table.getRowCount() > 0) {
            for (int i = 0; i < table.getRowCount(); i++) {
                box.addItem(table.getValueAt(i, 1).toString());
            }
            box.setSelectedItem(table_dengJu.getValueAt(select, 3).toString().split("#")[1]);
            str = table_dengJu.getValueAt(select, 3).toString().split("#")[1];
            label.setText(table_dengJu.getValueAt(select, 6).toString());
            label2.setText(table_dengJu.getValueAt(select, 4).toString());
        }
        box.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    label.setText(Data.DengKuChannelCountList.get(box.getSelectedIndex()).toString());
                    label2.setText(Data.DengKuVersionList.get(box.getSelectedIndex()).toString());
                }
            }
        });
        box.setPreferredSize(new Dimension(170, 30));
        panel3.add(box);

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
        dialog.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if ("确定".equals(e.getActionCommand())) {
            NewJTable table = (NewJTable) MainUi.map.get("table_dengJu");
            int select = table.getSelectedRow();
            if (box.getSelectedItem() != null) {
                int cnt = Integer.valueOf(field2.getText()).intValue() + Integer.valueOf(label.getText()).intValue();
                if (cnt > 513) {
                    JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "通道已超出正常512范围！", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!box.getSelectedItem().toString().equals(str)) {
					String str = examine();
					if(!str.equals("")){
						JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "该灯具被"+str+"引用，请解除关系后再修改！", "提示", JOptionPane.ERROR_MESSAGE);
						return;
					}
                }
                //Data.dengJu_change = true;
                table.setValueAt(field.getText(), select, 2);
                table.setValueAt("ID" + (box.getSelectedIndex() + 1) + "#" + box.getSelectedItem().toString(), select, 3);
                table.setValueAt(label2.getText(), select, 4);
                table.setValueAt(field2.getText(), select, 5);
                table.setValueAt(label.getText(), select, 6);
            } else {
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "请先创建灯库，再添加灯具", "提示", JOptionPane.ERROR_MESSAGE);
            }
            dialog.dispose();
        } else {
            dialog.dispose();
        }
    }

    private String examine() {
        String str = "";
        NewJTable dengJuFengZutable = (NewJTable) MainUi.map.get("GroupTable");
        NewJTable table3 = (NewJTable) MainUi.map.get("allLightTable");//所有灯具
        for (int i = 0; i < dengJuFengZutable.getRowCount(); i++) {
            TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(i);
            Iterator iterator = treeSet.iterator();
            while (iterator.hasNext()) {
                int a = (int) iterator.next();
                if (table3.getRowCount() > 0) {
                    String s = table3.getValueAt(a, 0).toString();
                    Integer id = Integer.parseInt(s.split("#")[0].substring(2));//组内灯具的灯具id
                    if ((select + 1) == id) {
                        str += dengJuFengZutable.getValueAt(i, 2) + ",";
                    }
                }
            }
        }
        return str;
    }

}
