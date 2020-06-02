package com.boray.changJing.Listener;

import com.boray.Data.Data;
import com.boray.Utils.Socket;
import com.boray.changJing.Data.DataOfChangJing;
import com.boray.mainUi.MainUi;

import javax.comm.SerialPort;
import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

public class ChangJingSelectItemListener implements ItemListener {

    @Override
    public void itemStateChanged(ItemEvent e) {
        JComboBox box = (JComboBox) e.getSource();
        if (e.getStateChange() == ItemEvent.SELECTED) {
            int n = -1;
            if (box.getItemCount() == 32) {
                int i = box.getSelectedIndex();
                if (i < 22) {
                    n = i;
                } else {
                    if (i == 22) {
                        n = 23;
                    }
                    if (i == 23) {
                        n = 24;
                    }
                    if (i >= 24) {
                        n = i + 5;
                    }
                }
            } else {
                int i = box.getSelectedIndex();
                if (i == 0) {
                    n = 22;
                } else if (i == 1) {
                    n = 26;
                } else if (i == 2) {
                    n = 27;
                } else if (i == 3) {
                    n = 28;
                }
            }
            JLabel label = (JLabel) MainUi.map.get("changJingBianChengLabel");
            label.setText("正在编辑：【" + n + "】场景");
            //保存前一个场景数据
            if (Data.changJingModel != -1) {
                List list = (List) DataOfChangJing.map.get("" + Data.changJingModel);
                if (list == null) {
                    list = new ArrayList();
                }
                saveData(list);
                DataOfChangJing.map.put("" + Data.changJingModel, list);
            }
            //加载当前场景数据
            loadData(n);
            //加载数据后  发送数据
            if (Data.serialPort != null || Data.socket != null)
                Socket.SendData(new DengJuKaiGuangItemListener().code());

            Data.changJingModel = n;
            JComboBox boxs = (JComboBox) MainUi.map.get("copyBox");
            boxs.removeAllItems();
            for (int i = 0; i < 37; i++) {
                if (i != 25 && i != Data.changJingModel) {
                    boxs.addItem(String.valueOf(i));
                }
            }
        }
//        new Thread(new Runnable() {
//            public void run() {
//                JComboBox box = (JComboBox) MainUi.map.get("copyBox");
//                box.removeAllItems();
//                for (int i = 0; i < 37; i++) {
//                    if (i != 25 && i != Data.changJingModel) {
//                        box.addItem(String.valueOf(i));
//                    }
//                }
//            }
//        }).start();
    }

    private void loadData(int cj) {
        List list = (List) DataOfChangJing.map.get("" + cj);
        if (list != null) {
            SerialPort serialPort = Data.serialPort;
            Data.serialPort = null;

            //8个不可调
            JComboBox[] boxs8 = (JComboBox[]) MainUi.map.get("kaiGuangBox_BuKeTiao");
            for (int i = 0; i < boxs8.length; i++) {
                boxs8[i].setSelectedIndex(Integer.valueOf(list.get(i).toString()));
            }

            //4个灯的开关和亮度
            JComboBox[] boxs = (JComboBox[]) MainUi.map.get("kaiGuangBox");
            JSlider[] sliders = (JSlider[]) MainUi.map.get("liangDuSliders");
            for (int i = 0; i < sliders.length; i++) {
                boxs[i].setSelectedIndex(Integer.valueOf(list.get(8 + i * 2).toString()));
                sliders[i].setValue(Integer.valueOf(list.get(i * 2 + 9).toString()));
            }
            List timeList = (List) DataOfChangJing.timeMap.get("" + cj);
            if (timeList != null) {
                JTextField[] field8 = (JTextField[]) MainUi.map.get("kaiGuangField_BuKeTiao");
                for (int i = 0; i < field8.length; i++) {
                    field8[i].setText(timeList.get(i).toString());
                }
                JTextField[] fields2 = (JTextField[]) MainUi.map.get("liangDufields");
                for (int i = 0; i < fields2.length; i++) {
                    fields2[i].setText(timeList.get(6 + i).toString());
                }
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

            //摇麦触发间隔
			/*JComboBox box2 = (JComboBox)MainUi.map.get("yaoMaiJianGeBox");
			box2.setSelectedIndex(Integer.valueOf(list.get(28).toString()));

			//摇麦延续开关
			JRadioButton radioButton5 = (JRadioButton)MainUi.map.get("yaoMaiKaiGuangBtn1");
			JRadioButton radioButton8 = (JRadioButton)MainUi.map.get("yaoMaiKaiGuangBtn2");
			if (list.get(29).toString().equals("1")) {
				radioButton5.setSelected(true);
			} else {
				radioButton8.setSelected(true);
			}*/

            Data.serialPort = serialPort;
        }
    }

    private void saveData(List list) {
        List timeList = (List) DataOfChangJing.timeMap.get("" + Data.changJingModel);
        if (timeList == null) {
            timeList = new ArrayList();
        } else {
            timeList.clear();
        }
        list.clear();

        //8个不可调
        JComboBox[] boxs8 = (JComboBox[]) MainUi.map.get("kaiGuangBox_BuKeTiao");
        for (int i = 0; i < boxs8.length; i++) {
            list.add(String.valueOf(boxs8[i].getSelectedIndex()));
        }
        JTextField[] field8 = (JTextField[]) MainUi.map.get("kaiGuangField_BuKeTiao");
        for (int i = 0; i < field8.length; i++) {
            timeList.add(field8[i].getText());
        }

        //4个灯的开关和亮度
        JComboBox[] boxs = (JComboBox[]) MainUi.map.get("kaiGuangBox");
        JSlider[] sliders = (JSlider[]) MainUi.map.get("liangDuSliders");
        for (int i = 0; i < sliders.length; i++) {
            list.add(String.valueOf(boxs[i].getSelectedIndex()));
            list.add(String.valueOf(sliders[i].getValue()));
        }
        JTextField[] fields2 = (JTextField[]) MainUi.map.get("liangDufields");
        for (int i = 0; i < fields2.length; i++) {
            timeList.add(fields2[i].getText());
        }
        //全局亮度
        JSlider slider = (JSlider) MainUi.map.get("quanJuLiangDuSlider");
        list.add(String.valueOf(slider.getValue()));
        //开关模式
        JRadioButton radioButton = (JRadioButton) MainUi.map.get("kaiGuangModelBtn1");
        if (radioButton.isSelected()) {
            list.add(String.valueOf(1));
        } else {
            list.add(String.valueOf(0));
        }

        //亮度模式
        JRadioButton radioButton3 = (JRadioButton) MainUi.map.get("liangDuModelBtn1");
        if (radioButton3.isSelected()) {
            list.add(String.valueOf(1));
        } else {
            list.add(String.valueOf(0));
        }

        //效果灯开关
        JComboBox box = (JComboBox) MainUi.map.get("xiaoGuoDengKaiGuangBox");
        list.add(String.valueOf(box.getSelectedIndex()));

        //场景模式
        JComboBox[] boxs2 = (JComboBox[]) MainUi.map.get("changJingModelBoxs");
        for (int i = 0; i < boxs2.length; i++) {
            list.add(String.valueOf(boxs2[i].getSelectedIndex()));
        }

        //声控模式
        JComboBox[] boxs1 = (JComboBox[]) MainUi.map.get("shengKonModelBoxs");
        for (int i = 0; i < boxs1.length; i++) {
            list.add(String.valueOf(boxs1[i].getSelectedIndex()));
        }

        //运行模式
        JRadioButton radioButton1 = (JRadioButton) MainUi.map.get("yunXingModelBtn1");
        if (radioButton1.isSelected()) {
            list.add(String.valueOf(1));
        } else {
            list.add(String.valueOf(0));
        }

        //摇麦模式
        JComboBox box3 = (JComboBox) MainUi.map.get("yaoMaiModelBox");
        list.add(String.valueOf(box3.getSelectedIndex()));

        //摇麦触发间隔
        JComboBox box2 = (JComboBox) MainUi.map.get("yaoMaiJianGeBox");
        list.add(String.valueOf(box2.getSelectedIndex()));

        //摇麦延续开关
        JRadioButton radioButton4 = (JRadioButton) MainUi.map.get("yaoMaiKaiGuangBtn1");
        if (radioButton4.isSelected()) {
            list.add(String.valueOf(1));
        } else {
            list.add(String.valueOf(0));
        }

        //雾机模式
        JComboBox boxes = (JComboBox) MainUi.map.get("wuJiModelBox");
        list.add(String.valueOf(boxes.getSelectedIndex()));

        DataOfChangJing.timeMap.put("" + Data.changJingModel, timeList);
    }
}
