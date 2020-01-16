package com.boray.kongTiao.Listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.OutputStream;

import javax.swing.JComboBox;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.Socket;

public class AllComBoxItemListener implements ItemListener {
    public void itemStateChanged(ItemEvent e) {
        if (ItemEvent.SELECTED == e.getStateChange()) {
            JComboBox box = (JComboBox) e.getSource();
            if (Data.serialPort != null) {
                try {
                    OutputStream os = Data.serialPort.getOutputStream();
                    os.write(code(box.getName(), box.getSelectedIndex()));
                    os.flush();
                    os.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else if (Data.socket != null) {
                Socket.UDPSendData(code(box.getName(), box.getSelectedIndex()));
            }
        }
    }

    private byte[] code(String a, int selected) {
        byte[] b = null;
        switch (a) {
            case "空调冷热模式":
                b = ZhiLingJi.setKongTiao(6, 106 + selected);
                break;
            case "空调档位":
                b = ZhiLingJi.setKongTiao(7, 138 + selected);
                break;
            case "设定温度":
                b = ZhiLingJi.setKongTiao(8, 12 + selected);
                break;
            case "当前温度":
                b = ZhiLingJi.setKongTiao(9, 12 + selected);
                break;
            case "排风状态":
                b = ZhiLingJi.setKongTiao(10, 80 + selected);
                break;
            case "线阀模式":
                b = ZhiLingJi.setKongTiao(1, 1 + selected);
                break;
            case "空调模式":
                b = ZhiLingJi.setKongTiao(3, 1 + selected);
                break;
            case "空调RS485地址":
                b = ZhiLingJi.setKongTiao(5, 1 + selected);
                break;
            default:
                break;
        }
        return b;
    }
}
