package com.boray.xiaoGuoDeng.Listener;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;

import javax.swing.JPanel;

import com.boray.Data.Data;
import com.boray.Data.XiaoGuoDengModel;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.Socket;
import com.boray.mainUi.MainUi;

public class DMXModelListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        XiaoGuoDengModel.model = Integer.valueOf(e.getActionCommand().substring(3)).intValue();
        JPanel p30 = (JPanel) MainUi.map.get("XiaoGuoRightPane_8");
        CardLayout cardLayout = (CardLayout) MainUi.map.get("XiaoGuoCardLayout_8");
        cardLayout.show(p30, XiaoGuoDengModel.model + "");
        if (Data.serialPort != null) {
            try {
                OutputStream os = Data.serialPort.getOutputStream();
                os.write(queryLuZhi());
                os.flush();
            }catch (Exception e1){
                e1.printStackTrace();
            }
        } else if (Data.socket != null) {
            Socket.UDPSendData(queryLuZhi());
        }
    }

    public byte[] queryLuZhi(){
        byte[] bytes = new byte[80];
        bytes[0] = (byte) 0xFA;
        bytes[1] = (byte) 0x50;
        bytes[2] = (byte) 0x61;
        bytes[3] = ZhiLingJi.TYPE;
        bytes[4] = (byte) 0x91;
        bytes[5] = (byte) 00;
        bytes[7] = (byte) XiaoGuoDengModel.model;
        bytes[9] = (byte) 01;
        bytes[79] = ZhiLingJi.getJiaoYan(bytes);
        return bytes;
    }
}
