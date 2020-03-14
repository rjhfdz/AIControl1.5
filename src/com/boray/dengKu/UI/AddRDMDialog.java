package com.boray.dengKu.UI;

import com.boray.Data.Data;
import com.boray.Utils.Util;
import com.boray.addJCheckBox.CWCheckBoxRenderer;
import com.boray.addJCheckBox.CheckBoxCellEditor;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddRDMDialog implements ActionListener {
    private JDialog dialog;
    private NewJTable table;
    private String[] str;
    private JFrame frame;
    private int x;

    /**
     * 展示页面
     */
    public void show() {
        frame = (JFrame) MainUi.map.get("frame");
        dialog = new JDialog(frame, true);
        dialog.setResizable(false);
        dialog.setTitle("添加RDM灯具信息");
        int w = 910, h = 570;
        dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
        dialog.setSize(w, h);
        dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        init();
        dialog.setVisible(true);
    }

    private void init() {
        JPanel panel = new JPanel();
        JButton btn = new JButton("添加");
        JButton btn1 = new JButton("取消");
        JButton btn2 = new JButton("新建灯库");
        btn.addActionListener(this);
        btn1.addActionListener(this);
        btn2.addActionListener(this);
        panel.add(btn);
        panel.add(btn1);
        panel.add(btn2);

        JScrollPane bodyPane = new JScrollPane();
        bodyPane.setPreferredSize(new Dimension(890, 552));
        Object[][] data = {};
        String[] title = {"启用", "ID", "灯具名称", "型号", "库版本", "DMX起始地址", "占用通道数"};
        DefaultTableModel model = new DefaultTableModel(data, title);
        table = new NewJTable(model, 9);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        cell.setBackground(new Color(237, 243, 254));
                        cell.setForeground(Color.black);
                    } else {
                        cell.setBackground(Color.white); //设置偶数行底色
                        cell.setForeground(Color.black);
                    }
                } else {
                    cell.setBackground(new Color(85, 160, 255));
                    cell.setForeground(Color.white);
                }
                return cell;
            }
        };
        for (int i = 0; i < title.length; i++) {
            table.getColumn(table.getColumnName(i)).setCellRenderer(cellRenderer);
        }
        table.setSelectionBackground(new Color(56, 117, 215));
        table.getTableHeader().setUI(new BasicTableHeaderUI());
        table.getTableHeader().setReorderingAllowed(false);
        table.setOpaque(false);
        table.setFont(new Font(Font.SERIF, Font.BOLD, 14));
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(194);
        table.getColumnModel().getColumn(3).setPreferredWidth(320);
        table.getColumnModel().getColumn(4).setPreferredWidth(88);
        table.setRowHeight(28);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn Column0 = table.getColumnModel().getColumn(0);
        Column0.setCellEditor(new CheckBoxCellEditor());
        Column0.setCellRenderer(new CWCheckBoxRenderer());
        str = dengKuItems();
        bodyPane.setViewportView(table);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getX() == x) {
                    if (mouseEvent.getButton() == 1 && mouseEvent.getClickCount() == 2) {
                        str = dengKuItems();
                        new RDMChangeDialog().show(table, str);
                    }
                }
                x = mouseEvent.getX();
            }
        });
        initData();

        dialog.add(panel);
        dialog.add(bodyPane);
    }

    /**
     * 加载数据
     */
    private void initData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        NewJTable rdmTable = (NewJTable) MainUi.map.get("RDM_table");
        for (int i = 0; i < rdmTable.getRowCount(); i++) {
            Object[] s = {new Boolean(true), String.valueOf(i + 1), rdmTable.getValueAt(i, 3).toString(), str.length > 0 ? str[0] : "", rdmTable.getValueAt(i, 3).toString().split(" ")[1], rdmTable.getValueAt(i, 4), rdmTable.getValueAt(i, 5)};
            model.addRow(s);
        }
    }

    private String[] dengKuItems() {
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        String[] str = new String[table.getRowCount()];
        for (int i = 0; i < str.length; i++) {
            str[i] = "ID" + (i + 1) + "#" + table.getValueAt(i, 1).toString() + "--" + Data.DengKuChannelCountList.get(i);
        }
        return str;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("添加".equals(e.getActionCommand())) {
            NewJTable dengJutable = (NewJTable) MainUi.map.get("table_dengJu");
            String s = "";
            int count = 0;
            for (int i = 0; i < table.getRowCount(); i++) {
                if ((boolean) table.getValueAt(i, 0)) {
                    int a = Integer.valueOf(table.getValueAt(i, 3).toString().split("--")[1]);
                    int b = Integer.valueOf(table.getValueAt(i, 6).toString());
                    count++;
                    if (a != b) {
                        s += table.getValueAt(i, 2) + " , ";
                    }
                }
            }
            if (dengJutable.getRowCount() + count > 50) {
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "添加灯具数量不能超过50个！", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!s.equals("")) {
                JOptionPane.showMessageDialog(frame, "灯具 " + s + "所选型号通道与占用通道不一致！", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (int i = 0; i < table.getRowCount(); i++) {
                if ((boolean) table.getValueAt(i, 0)) {
                    int startA = Integer.valueOf(table.getValueAt(i, 5).toString());
                    int endA = startA + Integer.valueOf(table.getValueAt(i, 6).toString());
                    for (int j = 0; j < dengJutable.getRowCount(); j++) {
                        int startB = Integer.valueOf(dengJutable.getValueAt(j, 5).toString());
                        int endB = startB + Integer.valueOf(dengJutable.getValueAt(j, 6).toString());
                        if (Util.checkRepetition(startA, startB, endA, endB)) {
                            s += table.getValueAt(i, 2) + "与" + dengJutable.getValueAt(j, 2) + " , ";
                        }
                    }
                }
            }
            if (!s.equals("")) {
                JOptionPane.showMessageDialog(frame, "灯具 " + s + "DMX地址重复！", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
            DefaultTableModel model = (DefaultTableModel) dengJutable.getModel();
            for (int i = 0; i < table.getRowCount(); i++) {
                Object[] objects = {new Boolean(true), String.valueOf(dengJutable.getRowCount() + 1), table.getValueAt(i, 2).toString(), table.getValueAt(i, 3).toString().split("--")[0], table.getValueAt(i, 4).toString(), table.getValueAt(i, 5).toString(), table.getValueAt(i, 6).toString()};
                model.addRow(objects);
            }
            dialog.dispose();
        } else if ("取消".equals(e.getActionCommand())) {
            dialog.dispose();
        } else {
            new dkCreateDialog().show((NewJTable) MainUi.map.get("table_DkGl"));
        }
    }
}
