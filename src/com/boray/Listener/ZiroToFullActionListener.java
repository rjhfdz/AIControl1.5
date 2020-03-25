package com.boray.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.usb.UsbPipe;

import com.boray.Data.Data;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.usb.LastPacketData;
import com.boray.usb.UsbUtil;
import com.boray.xiaoGuoDeng.UI.YaoMaiModelUI;

public class ZiroToFullActionListener implements ActionListener {
    private NewJTable table_DMX_All;
    private JSlider[] ChannelValueSliders;
    private JTextField[] textFields;

    public ZiroToFullActionListener(NewJTable table) {
        table_DMX_All = table;
    }

    public void setDt(JSlider[] sliders, JTextField[] fields) {
        ChannelValueSliders = sliders;
        textFields = fields;
    }

    public void actionPerformed(ActionEvent e) {
        //JSlider[] ChannelValueSliders = (JSlider[])MainUi.map.get("ChannelValueSliders");
        //JTextField[] textFields = (JTextField[])MainUi.map.get("ChannelValueFields");
        //JToggleButton[] toggleButtons = (JToggleButton[])MainUi.map.get("toggleButtons_changjin");
        //NewJTable table_DMX_All = (NewJTable)MainUi.map.get("table_DMX_All");
        /*NewJTable table2 = (NewJTable) MainUi.map.get("table_dengJu");

        boolean yes = false;
        int[] slt2 = table_DMX_All.getSelectedRows();
        if (slt2.length > 0) {
            yes = true;
            //Data.changJin_change = true;
        }*/
		
		/*if (yes) {
			Vector v = (Vector)MainUi.map.get("cancelTable");
			if (v == null) {
				v = new Vector();
				MainUi.map.put("cancelTable", v);
			}
			DefaultTableModel modelTemp = (DefaultTableModel)table_DMX_All.getModel();
			Vector tt = (Vector)modelTemp.getDataVector();
			Vector temp = new Vector<>();
			for (int i = 0; i < tt.size(); i++) {
				temp.add(((Vector)tt.get(i)).clone());
			}
			v.add(temp);
		}*/

        ChangeListener listener = null;
        if ("清零".equals(e.getActionCommand())) {
            for (int i = 0; i < ChannelValueSliders.length; i++) {
                if (ChannelValueSliders[i].isEnabled()) {
                    //listener = ChannelValueSliders[i].getChangeListeners()[0];
                    //ChannelValueSliders[i].removeChangeListener(listener);
                    ChannelValueSliders[i].setValue(0);
                    //textFields[i].setText("0");
                    //ChannelValueSliders[i].addChangeListener(listener);
                }
            }
			
			/*int[] slt = table_DMX_All.getSelectedRows();
			int start = Integer.valueOf(table2.getValueAt(YaoMaiModelUI.selected, 5).toString()).intValue();
			int count = Integer.valueOf(table2.getValueAt(YaoMaiModelUI.selected, 6).toString()).intValue();
			if (slt.length > 0) {
				for (int n = 0; n < slt.length; n++) {
					for (int i = 0; i < count; i++) {
						table_DMX_All.setValueAt("0", slt[n], start+i+2);
					}
				}
			} else {
				
			}*/

        } else if ("满值".equals(e.getActionCommand())) {
            for (int i = 0; i < ChannelValueSliders.length; i++) {
                if (ChannelValueSliders[i].isEnabled()) {
                    //listener = ChannelValueSliders[i].getChangeListeners()[0];
                    //ChannelValueSliders[i].removeChangeListener(listener);
                    ChannelValueSliders[i].setValue(255);
                    //textFields[i].setText("255");
                    //ChannelValueSliders[i].addChangeListener(listener);
                }
            }
			/*int[] slt = table_DMX_All.getSelectedRows();
			int start = Integer.valueOf(table2.getValueAt(YaoMaiModelUI.selected, 5).toString()).intValue();
			int count = Integer.valueOf(table2.getValueAt(YaoMaiModelUI.selected, 6).toString()).intValue();
			if (slt.length > 0) {
				for (int n = 0; n < slt.length; n++) {
					for (int i = 0; i < count; i++) {
						table_DMX_All.setValueAt("255", slt[n], start+i+2);
					}
				}
			} else {
				
			}*/
        } else if ("全部清零".equals(e.getActionCommand())) {
            for (int i = 0; i < ChannelValueSliders.length; i++) {
                if (ChannelValueSliders[i].isEnabled()) {
                    ChannelValueSliders[i].setValue(0);
                }
            }
            for (int i = 2; i < table_DMX_All.getColumnCount(); i++) {
                table_DMX_All.setValueAt("0", table_DMX_All.getSelectedRow(), i);
            }
        } else if ("全部满值".equals(e.getActionCommand())) {
            for (int i = 0; i < ChannelValueSliders.length; i++) {
                if (ChannelValueSliders[i].isEnabled()) {
                    ChannelValueSliders[i].setValue(255);
                }
            }
            for (int i = 2; i < table_DMX_All.getColumnCount(); i++) {
                table_DMX_All.setValueAt("255", table_DMX_All.getSelectedRow(), i);
            }
        }
        outDevice();
    }

    private void outDevice() {
        int[] slt = table_DMX_All.getSelectedRows();
        int value = 0;
        //int startAddress = box88.getSelectedIndex()+1;
        UsbPipe sendUsbPipe = (UsbPipe) MainUi.map.get("sendUsbPipe");
        if (sendUsbPipe != null && slt.length != 0) {
            byte[] buff = new byte[512];
            byte[] temp = new byte[64];
            int[] tl = new int[3];
            for (int j = 0; j < 2; j++) {
                tl[j] = Integer.valueOf(table_DMX_All.getValueAt(slt[0], j).toString()).intValue();
            }
            for (int j = 2; j < table_DMX_All.getColumnCount(); j++) {
                value = Integer.valueOf(table_DMX_All.getValueAt(slt[0], j).toString()).intValue();
                buff[j - 2] = (byte) value;
            }
            try {
                for (int k = 0; k < 8; k++) {
                    System.arraycopy(buff, k * 64, temp, 0, 64);
                    UsbUtil.sendMassge(sendUsbPipe, temp);
                }
                UsbUtil.sendMassge(sendUsbPipe, LastPacketData.getL(buff, tl));
            } catch (javax.usb.UsbPlatformException e2) {
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "数据写出发生错误！", "提示", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
