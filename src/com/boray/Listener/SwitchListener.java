package com.boray.Listener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import com.boray.Data.Data;
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
                        timeBlockPanels[n + 1].setVisible(true);
                        labels[n + 1].setVisible(true);
                        labels[n + 1].setText(table.getValueAt(n, 2).toString());
                    } else {
                        timeBlockPanels[n + 1].setVisible(false);
                        labels[n + 1].setVisible(false);
                    }
                }
            }
            if (table.getRowCount() < 30) {
                for (int i = 1; i <= 24; i++) {
                    JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + i);
                    JLabel[] labels = (JLabel[]) MainUi.map.get("labels_group" + i);
                    for (int j = table.getRowCount(); j < labels.length - 1; j++) {
                        timeBlockPanels[j + 1].setVisible(false);
                        labels[j + 1].setVisible(false);
                    }
                }
            }
            DMXModelListener listener = new DMXModelListener();
            if (Data.serialPort != null) {
                try {
                    OutputStream os = Data.serialPort.getOutputStream();
                    os.write(listener.queryLuZhi());
                    os.flush();
                }catch (Exception e1){
                    e1.printStackTrace();
                }
            } else if (Data.socket != null) {
                Socket.UDPSendData(listener.queryLuZhi());
            }
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
            if (Data.serialPort != null) {
                Data.deviceShow = true;
                try {
                    OutputStream os = Data.serialPort.getOutputStream();
                    os.write(ZhiLingJi.queryQuanJuSet());
                    os.flush();
                    os.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
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
            new DataActionListener().saveChangJingData();
        }
        if (btn.getName().equals("9")) {
            JList list = (JList) MainUi.map.get("suCaiLightType");
            NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
            String[] s = {};
            if (table.getRowCount() != 0) {
                s = new String[table.getRowCount()];
                for (int i = 0; i < s.length; i++) {
                    s[i] = table.getValueAt(i, 1).toString();
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
                if (Data.serialPort != null) {
                    //UÅÌ×´Ì¬²éÑ¯
                    OutputStream os = Data.serialPort.getOutputStream();
                    os.write(ZhiLingJi.queryUSBFlashDiskState());
                    os.flush();
                }else if(Data.socket !=null){
                    Socket.UDPSendData(ZhiLingJi.queryUSBFlashDiskState());
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        JPanel MenuButtonPanel = (JPanel) MainUi.map.get("MenuButtonPanel");
        for (int i = 0; i < MenuButtonPanel.getComponentCount(); i++) {
            if(MenuButtonPanel.getComponent(i) instanceof JPressButton) {
                JPressButton button = (JPressButton) MenuButtonPanel.getComponent(i);
                if (button.isSelected()) {
                    button.setForeground(Color.black);
                } else {
                    button.setForeground(Color.WHITE);
                }
            }
        }
        selected = btn.getName();
        cardLayout.show(parentPane, btn.getName());
    }
}
