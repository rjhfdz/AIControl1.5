package com.boray.changJing.Listener;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.OutputStream;

import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.Socket;
import com.boray.mainUi.MainUi;

public class DengJuKaiGuangItemListener implements ItemListener {
    public void itemStateChanged(ItemEvent e) {
        if (ItemEvent.SELECTED == e.getStateChange()) {
            if (Data.serialPort != null||Data.socket!=null) {
                Socket.SendData(code());
//                try {
//                    OutputStream os = Data.serialPort.getOutputStream();
//                    os.write(code());
//                    os.flush();
//                    os.close();
//                } catch (Exception e2) {
//                    e2.printStackTrace();
//                }
            }
        }
    }

    public void sendData(){
        if (Data.serialPort != null||Data.socket!=null) {
            Socket.SendData(code());
//            try {
//                OutputStream os = Data.serialPort.getOutputStream();
//                os.write(code());
//                os.flush();
//                os.close();
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
        } /*else if (Data.socket != null) {
//            Socket.UDPSendData(code());
//        }*/
    }

    public byte[] code() {
        int[] t = new int[19];

        //4个灯的开关亮度
        JComboBox[] boxs = (JComboBox[]) MainUi.map.get("kaiGuangBox");
        JSlider[] sliders = (JSlider[]) MainUi.map.get("liangDuSliders");

        t[3] = boxs[0].getSelectedIndex() + 10;
        t[4] = boxs[1].getSelectedIndex() + 10;
        t[5] = boxs[2].getSelectedIndex() + 10;
        t[6] = boxs[3].getSelectedIndex() + 10;

        t[15] = sliders[0].getValue();
        t[16] = sliders[1].getValue();
        t[17] = sliders[2].getValue();
        t[18] = sliders[3].getValue();

        //8个不可调
        JComboBox[] boxs8 = (JComboBox[]) MainUi.map.get("kaiGuangBox_BuKeTiao");
        for (int i = 0; i < 8; i++) {
            t[7 + i] = boxs8[i].getSelectedIndex() + 10;
        }

        //全局亮度
        JSlider slider = (JSlider) MainUi.map.get("quanJuLiangDuSlider");
        t[2] = slider.getValue();
        //开关模式
        JRadioButton radioButton = (JRadioButton) MainUi.map.get("kaiGuangModelBtn1");
        JRadioButton radioButton2 = (JRadioButton) MainUi.map.get("kaiGuangModelBtn2");
        if (radioButton.isSelected()) {
            t[0] = 1;
        } else if (radioButton2.isSelected()) {
            t[0] = 0;
        }
        //亮度模式
        JRadioButton radioButton3 = (JRadioButton) MainUi.map.get("liangDuModelBtn1");
        JRadioButton radioButton4 = (JRadioButton) MainUi.map.get("liangDuModelBtn2");
        if (radioButton3.isSelected()) {
            t[1] = 1;
        } else if (radioButton4.isSelected()) {
            t[1] = 0;
        }
        return ZhiLingJi.setChangJing1(Data.changJingModel, t);
    }
}
