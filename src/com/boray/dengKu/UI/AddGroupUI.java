package com.boray.dengKu.UI;

import java.awt.*;
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
import com.boray.Utils.IconJDialog;
import com.boray.mainUi.MainUi;
import com.boray.shengKon.UI.DefineJLable_shengKon;
import com.boray.shengKon.UI.DefineJLable_shengKon2;

public class AddGroupUI implements ActionListener {
    private IconJDialog dialog;
    private JTextField field;

    public void show() {
        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new IconJDialog(f, true);
        dialog.setResizable(false);
        dialog.setTitle("新建分组");
        int w = 380, h = 280;
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
        dialog.setSize(w, h);
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
        p2.setPreferredSize(new Dimension(300, 50));
        JButton btn = new JButton("取消");
        JButton btn2 = new JButton("确定");
        btn.addActionListener(this);
        btn2.addActionListener(this);
        p2.add(btn);
        p2.add(new JLabel("      "));
        p2.add(btn2);

        JPanel N1 = new JPanel();
        N1.setPreferredSize(new Dimension(300, 60));
        dialog.add(N1);
        dialog.add(p1);
        dialog.add(p2);
    }

    public void actionPerformed(ActionEvent e) {
        if ("确定".equals(e.getActionCommand())) {
            String s = field.getText().trim();
            if (!s.equals("")) {
                NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
                if (table.getRowCount() < 30) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    Object[] objects = {new Boolean(true), table.getRowCount() + 1, s};
                    model.addRow(objects);
                    //List list = new ArrayList();
                    TreeSet treeSet = new TreeSet<>();
                    Data.GroupOfLightList.add(treeSet);
                    table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
                    dialog.dispose();
                    for (int i = 1; i <= 16; i++) {
                        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels" + i);
                        timeBlockPanels[table.getRowCount()].removeAll();
                        for (int j = 0; j < timeBlockPanels[0].getComponentCount(); j++) {
                            int c = timeBlockPanels[table.getRowCount()].getComponentCount();
                            DefineJLable_shengKon2 label2 = new DefineJLable_shengKon2((c + 1) + "", timeBlockPanels[table.getRowCount()]);
                            DefineJLable_shengKon defineJLable = (DefineJLable_shengKon) timeBlockPanels[0].getComponent(j);
                            label2.setPreferredSize(defineJLable.getPreferredSize());
                            label2.setLocation(new Point(defineJLable.getLocation().x, defineJLable.getLocation().y));
                            label2.setSize(defineJLable.getWidth(), defineJLable.getHeight());
                            label2.setBackground(Color.red);
                            timeBlockPanels[table.getRowCount()].add(label2);
                        }
                        timeBlockPanels[table.getRowCount()].updateUI();
                    }
                } else {
                    JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "分组数量不能超过30组！", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } else {
            dialog.dispose();
        }
    }

}
