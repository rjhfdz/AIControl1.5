package com.boray.changJing.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.comm.SerialPort;
import javax.swing.*;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.Socket;
import com.boray.changJing.Data.DataOfChangJing;
import com.boray.mainUi.MainUi;

public class CopyToCurrent implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        JComboBox box = (JComboBox) MainUi.map.get("copyBox");
        int src = Integer.valueOf(box.getSelectedItem().toString());
        int total = Data.changJingModel;

        //int a = JOptionPane.showConfirmDialog((JFrame)MainUi.map.get("frame"),"确定要复制源场景"+src+"到当前场景吗?", "提示", JOptionPane.YES_NO_OPTION);
        int a = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "确定要复制源场景" + src + "到当前场景吗?", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, new String[]{"否", "是"}, "是");
        if (a == 0) {
            return;
        }
        loadData(src);
        if (Data.serialPort != null || Data.socket != null) {
            try {
                Socket.SendData(ZhiLingJi.changJingCopy(src, total));
//				OutputStream os = Data.serialPort.getOutputStream();
//				os.write(ZhiLingJi.changJingCopy(src, total));
//				os.flush();
//				os.close();
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        try {
                            Socket.SendData(ZhiLingJi.changJingChaXun(Data.changJingModel));
//							OutputStream os = Data.serialPort.getOutputStream();
//							os.write(ZhiLingJi.changJingChaXun(Data.changJingModel));
//							os.flush();
//							os.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }, 500);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void loadData(int cj) {
        List list = (List) DataOfChangJing.map.get("" + cj);
        List timeList = (List) DataOfChangJing.timeMap.get("" + cj);
        if (list != null) {
            SerialPort serialPort = Data.serialPort;
            Data.serialPort = null;

            //8个不可调
            JComboBox[] boxs8 = (JComboBox[]) MainUi.map.get("kaiGuangBox_BuKeTiao");
            for (int i = 0; i < boxs8.length; i++) {
                boxs8[i].setSelectedIndex(Integer.valueOf(list.get(i).toString()));
            }
            JTextField[] field8 = (JTextField[]) MainUi.map.get("kaiGuangField_BuKeTiao");
            for (int i = 0; i < field8.length; i++) {
                field8[i].setText(timeList.get(i).toString());
            }

            //4个灯的开关和亮度
            JComboBox[] boxs = (JComboBox[]) MainUi.map.get("kaiGuangBox");
            JSlider[] sliders = (JSlider[]) MainUi.map.get("liangDuSliders");
            for (int i = 0; i < sliders.length; i++) {
                boxs[i].setSelectedIndex(Integer.valueOf(list.get(8 + i * 2).toString()));
                sliders[i].setValue(Integer.valueOf(list.get(i * 2 + 9).toString()));
            }
            JTextField[] fields2 = (JTextField[]) MainUi.map.get("liangDufields");
            for (int i = 0; i < fields2.length; i++) {
                fields2[i].setText(timeList.get(6 + i).toString());
            }

            //全局亮度
            JSlider slider = (JSlider) MainUi.map.get("quanJuLiangDuSlider");
            slider.setValue(Integer.valueOf(list.get(16).toString()));

            //开关模式
            JRadioButton radioButton = (JRadioButton) MainUi.map.get("kaiGuangModelBtn1");
            JRadioButton radioButton2 = (JRadioButton) MainUi.map.get("kaiGuangModelBtn2");
            if (list.get(17).toString().equals("1")) {
                radioButton.setSelected(true);
            } else {
                radioButton2.setSelected(true);
            }

            //亮度模式
            JRadioButton radioButton3 = (JRadioButton) MainUi.map.get("liangDuModelBtn1");
            JRadioButton radioButton4 = (JRadioButton) MainUi.map.get("liangDuModelBtn2");
            if (list.get(18).toString().equals("1")) {
                radioButton3.setSelected(true);
            } else {
                radioButton4.setSelected(true);
            }

            //效果灯开关
            JComboBox box = (JComboBox) MainUi.map.get("xiaoGuoDengKaiGuangBox");
            box.setSelectedIndex(Integer.valueOf(list.get(19).toString()));

            //场景模式
            JComboBox[] boxs2 = (JComboBox[]) MainUi.map.get("changJingModelBoxs");
            for (int i = 0; i < boxs2.length; i++) {
                boxs2[i].setSelectedIndex(Integer.valueOf(list.get(20 + i).toString()));
            }

            //声控模式
            JComboBox[] boxs1 = (JComboBox[]) MainUi.map.get("shengKonModelBoxs");
            for (int i = 0; i < boxs1.length; i++) {
                boxs1[i].setSelectedIndex(Integer.valueOf(list.get(23 + i).toString()));
            }

            //运行模式
            JRadioButton radioButton1 = (JRadioButton) MainUi.map.get("yunXingModelBtn1");
            JRadioButton radioButton6 = (JRadioButton) MainUi.map.get("yunXingModelBtn2");
            if (list.get(26).toString().equals("1")) {
                radioButton1.setSelected(true);
            } else {
                radioButton6.setSelected(true);
            }

            //摇麦模式
            JComboBox box3 = (JComboBox) MainUi.map.get("yaoMaiModelBox");
            box3.setSelectedIndex(Integer.valueOf(list.get(27).toString()));

            Data.serialPort = serialPort;
        }
    }
}
