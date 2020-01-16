package com.boray.changJing.Listener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.changJing.Data.DataOfChangJing;
import com.boray.mainUi.MainUi;

public class ProgramToTestListener implements ActionListener {

    private CardLayout card;
    private JPanel rightPane;

    public ProgramToTestListener(CardLayout card, JPanel rightPane) {
        this.card = card;
        this.rightPane = rightPane;
    }

    public void actionPerformed(ActionEvent e) {
        JRadioButton radioButton = (JRadioButton) e.getSource();
        JPanel p1 = (JPanel) MainUi.map.get("changJingLiangDuPane");
        if ("场景编程".equals(radioButton.getText())) {
            card.show(rightPane, "场景编程");
            setChangJing1(true);
            setChangJing2(true);
            p1.setVisible(false);
            Data.isTest = false;
        } else if ("场景测试".equals(radioButton.getText())) {
            card.show(rightPane, "场景测试");
            List list = (List) DataOfChangJing.map.get("" + Data.changJingModel);
            if (list == null) {
                list = new ArrayList();
            }
            saveData(list);
            DataOfChangJing.map.put("" + Data.changJingModel, list);
            setChangJing1(false);
            setChangJing2(false);
            p1.setVisible(true);
            Data.isTest = true;
        }
    }

    //保存场景数据
    private void saveData(List list) {
        list.clear();
        //8个不可调
        JComboBox[] boxs8 = (JComboBox[]) MainUi.map.get("kaiGuangBox_BuKeTiao");
        for (int i = 0; i < boxs8.length; i++) {
            list.add(String.valueOf(boxs8[i].getSelectedIndex()));
        }

        //4个灯的开关和亮度
        JComboBox[] boxs = (JComboBox[]) MainUi.map.get("kaiGuangBox");
        JSlider[] sliders = (JSlider[]) MainUi.map.get("liangDuSliders");
        for (int i = 0; i < sliders.length; i++) {
            list.add(String.valueOf(boxs[i].getSelectedIndex()));
            list.add(String.valueOf(sliders[i].getValue()));
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
    }

    private void setChangJing1(boolean b) {
        //8个不可调
        JComboBox[] boxs8 = (JComboBox[]) MainUi.map.get("kaiGuangBox_BuKeTiao");
        for (int i = 0; i < boxs8.length; i++) {
            boxs8[i].setEnabled(b);
        }

        //10个灯的开关亮度
        JComboBox[] boxs = (JComboBox[]) MainUi.map.get("kaiGuangBox");
        JSlider[] sliders = (JSlider[]) MainUi.map.get("liangDuSliders");
        JComboBox[] boxs2 = (JComboBox[]) MainUi.map.get("liangDuBoxs");

        for (int i = 0; i < boxs.length; i++) {
            sliders[i].setEnabled(b);
            boxs[i].setEnabled(b);
            boxs2[i].setEnabled(b);
        }
        if (b && Data.serialPort != null) {
            setType(Integer.toHexString(Byte.toUnsignedInt(ZhiLingJi.TYPE)));
			/*if (Byte.toUnsignedInt(ZhiLingJi.TYPE) != 185) {
				sliders[6].setEnabled(false);sliders[7].setEnabled(false);
				boxs[6].setEnabled(false);boxs[7].setEnabled(false);
				boxs2[6].setEnabled(false);boxs2[7].setEnabled(false);
			}*/
        }
        //JComboBox[] boxs2 = (JComboBox[])MainUi.map.get("liangDuBoxs");
        //全局亮度
        JSlider slider = (JSlider) MainUi.map.get("quanJuLiangDuSlider");
        JComboBox box = (JComboBox) MainUi.map.get("quanJuLiangDuBox");
        slider.setEnabled(b);
        box.setEnabled(b);
        //开关模式
        JRadioButton radioButton = (JRadioButton) MainUi.map.get("kaiGuangModelBtn1");
        JRadioButton radioButton2 = (JRadioButton) MainUi.map.get("kaiGuangModelBtn2");
        radioButton.setEnabled(b);
        radioButton2.setEnabled(b);
        //亮度模式
        JRadioButton radioButton3 = (JRadioButton) MainUi.map.get("liangDuModelBtn1");
        JRadioButton radioButton4 = (JRadioButton) MainUi.map.get("liangDuModelBtn2");
        radioButton3.setEnabled(b);
        radioButton4.setEnabled(b);
        //
        JComboBox box2 = (JComboBox) MainUi.map.get("copyBox");
        JButton button = (JButton) MainUi.map.get("copyBtn");
        box2.setEnabled(b);
        button.setEnabled(b);
    }

    private void setChangJing2(boolean b) {
        //运行模式
        JRadioButton radioButton1 = (JRadioButton) MainUi.map.get("yunXingModelBtn1");
        JRadioButton radioButton2 = (JRadioButton) MainUi.map.get("yunXingModelBtn2");
        radioButton1.setEnabled(b);
        radioButton2.setEnabled(b);
        //声控模式
        JComboBox[] boxs1 = (JComboBox[]) MainUi.map.get("shengKonModelBoxs");
        //场景模式
        JComboBox[] boxs2 = (JComboBox[]) MainUi.map.get("changJingModelBoxs");
        for (int i = 0; i < 3; i++) {
            boxs1[i].setEnabled(b);
            boxs2[i].setEnabled(b);
        }
        //效果灯开关
        JComboBox box = (JComboBox) MainUi.map.get("xiaoGuoDengKaiGuangBox");
        box.setEnabled(b);

        //摇麦延续开关
        JRadioButton radioButton3 = (JRadioButton) MainUi.map.get("yaoMaiKaiGuangBtn1");
        JRadioButton radioButton4 = (JRadioButton) MainUi.map.get("yaoMaiKaiGuangBtn2");
        radioButton3.setEnabled(b);
        radioButton4.setEnabled(b);
        //摇麦触发间隔
        JComboBox box2 = (JComboBox) MainUi.map.get("yaoMaiJianGeBox");
        box2.setEnabled(b);
        //摇麦模式
        JComboBox box3 = (JComboBox) MainUi.map.get("yaoMaiModelBox");
        box3.setEnabled(b);
        //雾机模式
        JComboBox box4 = (JComboBox) MainUi.map.get("wuJiModelBox");
        box4.setEnabled(b);
    }

    private String setType(String type) {
		/*设备机型	关键字	开关路数	可调路数
		CL-9600A	B9	6	2
		CL-9500A	BA	6	0
		CL-9200A	B6	6	0
		CL-9100A	B7	6	0
		CL-8600A	B9-B0	8	4
		CL-8500A	BA-B0	8	2
		CL-8200A	B6-B0	8	0
		CL-8100A	B7-B0	8	0
		CL-7500A	B3	8	2
		CL-7200A	B4	8	0
		CL-7100A	B5	8	0*/
		/*
		// #define __L9100A__     0xBC//8+1排风          X10
        // #define __L9200A__     0xBD//8+1排风+风管空调1路 X12
        // #define __L9500A__     0xBE//2+8+1排风+风管空  标准的       X15
        // #define __L9600A__     0xBF//4+8+1排风+风管空调1路==标准工  X16
		 */
        String s = "";
        //8个不可调
        JComboBox[] boxs8 = (JComboBox[]) MainUi.map.get("kaiGuangBox_BuKeTiao");
        //4个灯的开关亮度
        JComboBox[] boxs = (JComboBox[]) MainUi.map.get("kaiGuangBox");
        JSlider[] sliders = (JSlider[]) MainUi.map.get("liangDuSliders");
        JComboBox[] boxs2 = (JComboBox[]) MainUi.map.get("liangDuBoxs");
        switch (type) {
            case "bf":
                s = "X16";
                for (int i = 0; i < 8; i++) {
                    boxs8[i].setEnabled(true);
                    if (i > 5) {
                        boxs8[i].setEnabled(false);
                    }
                }
                for (int i = 0; i < 4; i++) {
                    boxs[i].setEnabled(true);
                    sliders[i].setEnabled(true);
                    boxs2[i].setEnabled(true);
                    if (i > 1) {
                        boxs[i].setEnabled(false);
                        sliders[i].setEnabled(false);
                        boxs2[i].setEnabled(false);
                    }
                }
                break;
            case "be":
                s = "X15";
            case "bd":
                if ("bd".equals(type)) {
                    s = "X12";
                }
            case "bc":
                for (int i = 0; i < 8; i++) {
                    boxs8[i].setEnabled(true);
                    if (i > 5) {
                        boxs8[i].setEnabled(false);
                    }
                }
                for (int i = 0; i < 4; i++) {
                    boxs[i].setEnabled(false);
                    sliders[i].setEnabled(false);
                    boxs2[i].setEnabled(false);
                }
                if ("bc".equals(type)) {
                    s = "X10";
                }
                break;
            case "9":
                for (int i = 0; i < 8; i++) {
                    boxs8[i].setEnabled(true);
                }
                for (int i = 0; i < 4; i++) {
                    boxs[i].setEnabled(true);
                    sliders[i].setEnabled(true);
                    boxs2[i].setEnabled(true);
                }
                s = "CL-8600A";
                break;
            case "a":
                for (int i = 0; i < 8; i++) {
                    boxs8[i].setEnabled(true);
                }
                for (int i = 0; i < 4; i++) {
                    boxs[i].setEnabled(true);
                    sliders[i].setEnabled(true);
                    boxs2[i].setEnabled(true);
                    if (i > 1) {
                        boxs[i].setEnabled(false);
                        sliders[i].setEnabled(false);
                        boxs2[i].setEnabled(false);
                    }
                }
                s = "CL-8500A";
                break;
            case "6":
                for (int i = 0; i < 8; i++) {
                    boxs8[i].setEnabled(true);
                }
                for (int i = 0; i < 4; i++) {
                    boxs[i].setEnabled(false);
                    sliders[i].setEnabled(false);
                    boxs2[i].setEnabled(false);
                }
                s = "CL-8200A";
                break;
            case "7":
                for (int i = 0; i < 8; i++) {
                    boxs8[i].setEnabled(true);
                }
                for (int i = 0; i < 4; i++) {
                    boxs[i].setEnabled(false);
                    sliders[i].setEnabled(false);
                    boxs2[i].setEnabled(false);
                }
                s = "CL-8100A";
                break;
            case "b3":
                for (int i = 0; i < 8; i++) {
                    boxs8[i].setEnabled(true);
                }
                for (int i = 0; i < 4; i++) {
                    boxs[i].setEnabled(true);
                    sliders[i].setEnabled(true);
                    boxs2[i].setEnabled(true);
                    if (i > 1) {
                        boxs[i].setEnabled(false);
                        sliders[i].setEnabled(false);
                        boxs2[i].setEnabled(false);
                    }
                }
                s = "CL-7500A";
                break;
            case "b4":
                for (int i = 0; i < 8; i++) {
                    boxs8[i].setEnabled(true);
                }
                for (int i = 0; i < 4; i++) {
                    boxs[i].setEnabled(false);
                    sliders[i].setEnabled(false);
                    boxs2[i].setEnabled(false);
                }
                s = "CL-7200A";
                break;
            case "b5":
                for (int i = 0; i < 8; i++) {
                    boxs8[i].setEnabled(true);
                }
                for (int i = 0; i < 4; i++) {
                    boxs[i].setEnabled(false);
                    sliders[i].setEnabled(false);
                    boxs2[i].setEnabled(false);
                }
                s = "CL-7100A";
                break;
            default:
                break;
        }
        return s;
    }
}
