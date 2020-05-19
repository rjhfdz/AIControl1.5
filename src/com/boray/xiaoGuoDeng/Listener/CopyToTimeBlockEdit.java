package com.boray.xiaoGuoDeng.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.boray.Data.Data;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

public class CopyToTimeBlockEdit implements ActionListener {
    private NewJTable table_DMX;
    private int[][] data;
    private JLabel stepLabel;

    public CopyToTimeBlockEdit(NewJTable table) {
        this.table_DMX = table;
    }

    public CopyToTimeBlockEdit(NewJTable table, JLabel stepLabel) {
        this.table_DMX = table;
        this.stepLabel = stepLabel;
    }

    public void actionPerformed(ActionEvent e) {
        if ("�����ơ���������".equals(e.getActionCommand())) {
            int[] selects = table_DMX.getSelectedRows();
            if (selects.length > 0) {
                data = new int[selects.length][table_DMX.getColumnCount()];
                for (int i = 0; i < selects.length; i++) {
                    for (int j = 0; j < data[0].length; j++) {
                        data[i][j] = Integer.valueOf(table_DMX.getValueAt(selects[i], j).toString()).intValue();
                    }
                }
            }
        } else {
            if (data != null) {
                JFrame frame = (JFrame) MainUi.map.get("frame");
                if ((table_DMX.getName().equals("ҡ��") || table_DMX.getName().equals("���Ȳ�")) && data.length + table_DMX.getRowCount() > 16) {
                    JOptionPane.showMessageDialog(frame, "ճ��ʧ�ܣ��ܲ��������ܳ���16����", "��ʾ", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (table_DMX.getName().equals("���") && data.length + table_DMX.getRowCount() > 5) {
                    JOptionPane.showMessageDialog(frame, "ճ��ʧ�ܣ��ܲ��������ܳ���5����", "��ʾ", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if ((table_DMX.getName().equals("ʱ���") || table_DMX.getName().equals("����") ||
                        table_DMX.getName().equals("���")) && data.length + table_DMX.getRowCount() > 32) {
                    JOptionPane.showMessageDialog(frame, "ճ��ʧ�ܣ��ܲ��������ܳ���32����", "��ʾ", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if ((table_DMX.getName().equals("Ч�����")) && data.length + table_DMX.getRowCount() > 128) {
                    JOptionPane.showMessageDialog(frame, "ճ��ʧ�ܣ��ܲ��������ܳ���128����", "��ʾ", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                DefaultTableModel modelTemp = (DefaultTableModel) table_DMX.getModel();
                int lastSelect = table_DMX.getRowCount() - 1;
                int[] selects = table_DMX.getSelectedRows();
                if (selects.length > 0) {
                    lastSelect = selects[selects.length - 1];
                }

                if (lastSelect == -1) {
                    for (int i = 0; i < data.length; i++) {
                        String[] s = new String[515];
                        for (int j = 0; j < data[0].length; j++) {
                            s[j] = String.valueOf(data[i][j]);
                        }
                        modelTemp.addRow(s);
                    }
                } else {
                    for (int i = 0; i < data.length; i++) {
                        String[] s = new String[515];
                        for (int j = 0; j < data[0].length; j++) {
                            s[j] = String.valueOf(data[i][j]);
                        }
                        modelTemp.insertRow(lastSelect + 1 + i, s);
                    }
                }
                for (int i = 0; i < table_DMX.getRowCount(); i++) {
                    table_DMX.setValueAt("" + (i + 1), i, 0);
                }
                if (stepLabel != null) {
                    int size = table_DMX.getRowCount();
                    stepLabel.setText("�ܲ���:" + size);
                }
            }
        }
    }
}
