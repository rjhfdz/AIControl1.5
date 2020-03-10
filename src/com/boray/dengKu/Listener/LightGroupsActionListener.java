package com.boray.dengKu.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.boray.Data.Data;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

public class LightGroupsActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        NewJTable table = (NewJTable) MainUi.map.get("InGroupsTable");//���ڵƾ�
        NewJTable table2 = (NewJTable) MainUi.map.get("GroupTable");//�������
        NewJTable table3 = (NewJTable) MainUi.map.get("allLightTable");//���еƾ�
        NewJTable table_dengJu = (NewJTable) MainUi.map.get("table_dengJu");
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int selectNum = table2.getSelectedRow();
        if (selectNum == -1) {
            return;
        }
        if (">>".equals(e.getActionCommand())) {
            TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(table2.getSelectedRow());
            int[] selects = table.getSelectedRows();
            if (selects.length > 0) {
                if (selects.length == table.getRowCount()) {
					Object[] options = {"��", "��"};
					int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "�ò�������ն�Ӧ��Ч���Ʊ�����ݣ��Ƿ�ִ�У�", "����",
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
							null, options, options[1]);
					if (yes == 1) {
						for (int i = 1;i<=24;i++){
							JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + i);
							timeBlockPanels[table2.getSelectedRow()+1].removeAll();
							timeBlockPanels[table2.getSelectedRow()+1].repaint();
						}
                        for (int i = selects.length - 1; i >= 0; i--) {
                            treeSet.remove(Integer.valueOf(table.getValueAt(selects[i], 0).toString().split("#")[0].substring(2)).intValue() - 1);
                            model.removeRow(selects[i]);
                        }
					}
                }

            }
        } else if ("<<".equals(e.getActionCommand())) {
            TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(table2.getSelectedRow());
            int[] selects = table3.getSelectedRows();
            TreeSet temp = new TreeSet<>();
            if (treeSet.size() >= 1) {
                int cc = (int) treeSet.first();
                temp.add(table_dengJu.getValueAt(cc, 3).toString());
            }
            for (int i = 0; i < selects.length; i++) {
                temp.add(table_dengJu.getValueAt(selects[i], 3).toString());
            }
            if (temp.size() > 1) {
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "��ͬ���͵ƿⲻ�ܷ�Ϊͬһ�飡", "��ʾ", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (int i = 0; i < selects.length; i++) {
                treeSet.add(selects[i]);
            }
            if (treeSet.size() > 8) {
                for (int i = 0; i < selects.length; i++) {
                    treeSet.remove(selects[i]);
                }
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "ͬһ��ƾ��������ܳ���8����", "��ʾ", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (int i = table.getRowCount() - 1; i >= 0; i--) {
                model.removeRow(i);
            }
            Iterator iterator = treeSet.iterator();
            while (iterator.hasNext()) {
                int a = (int) iterator.next();
                String[] s = {table3.getValueAt(a, 0).toString()};
                model.addRow(s);
            }
        } else if ("all>>".equals(e.getActionCommand())) {
			Object[] options = {"��", "��"};
			int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "�ò�������ն�Ӧ��Ч���Ʊ�����ݣ��Ƿ�ִ�У�", "����",
					JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
					null, options, options[1]);
			if (yes == 1) {
				for (int i = 1;i<=24;i++){
					JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + i);
					timeBlockPanels[table2.getSelectedRow()+1].removeAll();
					timeBlockPanels[table2.getSelectedRow()+1].repaint();
				}
				TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(table2.getSelectedRow());
				if (table.getRowCount() > 0) {
					for (int i = table.getRowCount() - 1; i >= 0; i--) {
						treeSet.remove(Integer.valueOf(table.getValueAt(i, 0).toString().split("#")[0].substring(2)).intValue() - 1);
						model.removeRow(i);
					}
				}
			}

        } else if ("all<<".equals(e.getActionCommand())) {
            TreeSet temp = new TreeSet<>();
            for (int i = 0; i < table_dengJu.getRowCount(); i++) {
                temp.add(table_dengJu.getValueAt(i, 3).toString());
            }
            if (temp.size() > 1) {
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "��ͬ���͵ƿⲻ�ܷ�Ϊͬһ�飡", "��ʾ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(table2.getSelectedRow());

            ///////////////����
            TreeSet tpTreeSet = new TreeSet();
            Iterator iterator2 = treeSet.iterator();
            while (iterator2.hasNext()) {
                int a = (int) iterator2.next();
                tpTreeSet.add(a);
            }
            /////////////////

            for (int i = 0; i < table3.getRowCount(); i++) {
                treeSet.add(i);
            }

            //////////////////////////����
            if (treeSet.size() > 8) {
                treeSet.clear();
                Iterator iterator = tpTreeSet.iterator();
                while (iterator.hasNext()) {
                    int a = (int) iterator.next();
                    treeSet.add(a);
                }
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "ͬһ��ƾ��������ܳ���8����", "��ʾ", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //////////////////////////

            for (int i = table.getRowCount() - 1; i >= 0; i--) {
                model.removeRow(i);
            }
            Iterator iterator = treeSet.iterator();
            while (iterator.hasNext()) {
                int a = (int) iterator.next();
                String[] s = {table3.getValueAt(a, 0).toString()};
                model.addRow(s);
            }
        }
    }
}
