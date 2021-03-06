package com.boray.Listener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;

import javax.swing.*;

import com.boray.Data.Data;
import com.boray.Data.RdmData;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.JPressButton;
import com.boray.Utils.Socket;
import com.boray.beiFen.Listener.DataActionListener;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.xiaoGuoDeng.Listener.DMXModelListener;

public class SwitchListener implements ActionListener {
    private static String selected = "1";

    public void actionPerformed(ActionEvent e) {
        CardLayout cardLayout = (CardLayout) MainUi.map.get("titileCard");
        JPanel parentPane = (JPanel) MainUi.map.get("titilePane");
        JToggleButton btn = (JToggleButton) e.getSource();
        if (btn.getName().equals("3")) {
            NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }
            //List list = new ArrayList(30);
            for (int n = 0; n < table.getRowCount(); n++) {
                boolean b = (boolean) table.getValueAt(n, 0);
                for (int i = 1; i <= 24; i++) {
                    JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + i);
                    JLabel[] labels = (JLabel[]) MainUi.map.get("labels_group" + i);
                    if (b) {
                        timeBlockPanels[(n * 2)].setVisible(true);
                        timeBlockPanels[(n * 2) + 1].setVisible(true);
                        labels[n + 1].setVisible(true);
                        labels[n + 1].setText(table.getValueAt(n, 2).toString());
                    } else {
                        timeBlockPanels[(n * 2)].setVisible(false);
                        timeBlockPanels[(n * 2) + 1].setVisible(false);
                        labels[n + 1].setVisible(false);
                    }
                }
            }
            if (table.getRowCount() < 30) {
                for (int i = 1; i <= 24; i++) {
                    JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + i);
                    JLabel[] labels = (JLabel[]) MainUi.map.get("labels_group" + i);
                    for (int j = table.getRowCount(); j < labels.length - 1; j++) {
                        timeBlockPanels[(j * 2)].setVisible(false);
                        timeBlockPanels[(j * 2) + 1].setVisible(false);
                        labels[j + 1].setVisible(false);
                    }
                }
            }
            DMXModelListener listener = new DMXModelListener();
            Socket.SendData(listener.queryLuZhi());
        } else if (btn.getName().equals("4")) {

            NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }
            //List list = new ArrayList(30);
            for (int n = 0; n < table.getRowCount(); n++) {
                boolean b = (boolean) table.getValueAt(n, 0);
                for (int i = 1; i <= 16; i++) {
                    JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels" + i);
                    JLabel[] labels = (JLabel[]) MainUi.map.get("labels_shengKong" + i);
                    if (b) {
                        timeBlockPanels[n + 1].setVisible(true);
                        labels[n + 1].setVisible(true);
                        labels[n + 1].setText(table.getValueAt(n, 2).toString());
                    } else {
                        timeBlockPanels[n + 1].setVisible(false);
                        labels[n + 1].setVisible(false);
                    }
                }
            }
            if (table.getRowCount() <= 30) {
                for (int i = 1; i <= 16; i++) {
                    JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels" + i);
                    JLabel[] labels = (JLabel[]) MainUi.map.get("labels_shengKong" + i);
                    for (int j = table.getRowCount(); j < labels.length - 1; j++) {
                        timeBlockPanels[j + 1].setVisible(false);
                        labels[j + 1].setVisible(false);
                    }
                }
            }
        } else if (btn.getName().equals("7")) {
            if (Data.serialPort != null || Data.socket != null) {
                Data.deviceShow = true;
                Socket.SendData(ZhiLingJi.queryQuanJuSet());
            }
        }/* else if (btn.getName().equals("6") && !"6".equals(selected)) {
			JComboBox box = (JComboBox)MainUi.map.get("zhongKonGroupBox");
			new ShowAndSaveCode().show(box.getSelectedIndex());
		} else if (btn.getName().equals("1") && !"1".equals(selected)) {
			int n = Data.changJingModel;
			if (n != -1) {
				ChangJingSendCodeListener changJingSendCodeListener = new ChangJingSendCodeListener();
				changJingSendCodeListener.loadData(n);
			}
		}*/
        if (!btn.getName().equals("6") && "6".equals(selected)) {
            new DataActionListener().saveZhongKongData();
        }
        if (!btn.getName().equals("1") && "1".equals(selected)) {
//            JSlider slider = (JSlider) MainUi.map.get("quanJuLiangDuSlider");
//            slider.setValue(50);
            new DataActionListener().saveChangJingData();
        }
        if (btn.getName().equals("9")) {
            JList list = (JList) MainUi.map.get("suCaiLightType");
            NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
            String[] s = {};
            if (table.getRowCount() != 0) {
                s = new String[table.getRowCount()];
                for (int i = 0; i < s.length; i++) {
                    s[i] = table.getValueAt(i, 2).toString();
                }
            }
            list.setListData(s);
            if (s.length > 0) {
                list.setSelectedIndex(0);
            }
        }
        if (btn.getName().equals("11")) {
            JList list = (JList) MainUi.map.get("shengKonSuCaiLightType");
            NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
            String[] s = {};
            if (table.getRowCount() != 0) {
                s = new String[table.getRowCount()];
                for (int i = 0; i < s.length; i++) {
                    s[i] = table.getValueAt(i, 2).toString();
                }
            }
            list.setListData(s);
            if (s.length > 0) {
                list.setSelectedIndex(0);
            }
        }
        if (btn.getName().equals("10")) {

        }
        if (btn.getName().equals("8")) {
            try {
                Thread.sleep(500);
                //U��״̬��ѯ
                Socket.SendData(ZhiLingJi.queryUSBFlashDiskState());
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        JPanel MenuButtonPanel = (JPanel) MainUi.map.get("MenuButtonPanel");
        for (int i = 0; i < MenuButtonPanel.getComponentCount(); i++) {
            if (MenuButtonPanel.getComponent(i) instanceof JPressButton) {
                JPressButton button = (JPressButton) MenuButtonPanel.getComponent(i);
                if (button.isSelected()) {
                    button.setForeground(Color.black);
                } else {
                    button.setForeground(Color.WHITE);
                }
            }
        }
        try {//��300���������� dmx¼��״̬��ѯ
            Thread.sleep(500);
            if (!btn.getName().equals("2")) {
                Socket.SendData(RdmData.quit());
            } else {
                Socket.SendData(RdmData.access());
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        selected = btn.getName();
        cardLayout.show(parentPane, btn.getName());
    }
}
