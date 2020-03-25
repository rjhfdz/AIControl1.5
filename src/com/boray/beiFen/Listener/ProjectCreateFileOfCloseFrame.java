package com.boray.beiFen.Listener;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.table.DefaultTableModel;

import com.boray.Data.Data;
import com.boray.changJing.Data.DataOfChangJing;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.shengKon.UI.DefineJLable_shengKon;
import com.boray.shengKon.UI.DefineJLable_shengKon2;
import com.boray.xiaoGuoDeng.UI.DefineJLable;
import com.boray.xiaoGuoDeng.UI.DefineJLable3;
import com.boray.zhongKon.Data.DataOfZhongKon;

public class ProjectCreateFileOfCloseFrame {
    public void save() {
        JFileChooser fileChooser = new JFileChooser();
        if (!"".equals(Data.projectFilePath)) {
            fileChooser.setCurrentDirectory(new File(Data.projectFilePath));
        }

        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setSelectedFile(new File("project.xml"));
        int returnVal = fileChooser.showSaveDialog((JFrame) MainUi.map.get("frame"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            Data.projectFilePath = file.getParent();
            tt(file);
        }
    }

    public void tt(File file) {
        new DataActionListener().saveChangJingData().saveZhongKongData();
        try {
            OutputStream os = new FileOutputStream(file);
            XMLEncoder xmlEncoder = new XMLEncoder(os);

            //场景配置区
            xmlEncoder.writeObject(DataOfChangJing.map);

            //中控学习区
            //串口墙板
            xmlEncoder.writeObject(DataOfZhongKon.map2);

            //串口上行
            xmlEncoder.writeObject(DataOfZhongKon.map3);

            //串口下行
            xmlEncoder.writeObject(DataOfZhongKon.map4);

            //红外码输出
            xmlEncoder.writeObject(DataOfZhongKon.map1);

            //倒喝彩
            xmlEncoder.writeObject(DataOfZhongKon.map5);

            //全局设置数据区
            List list = new ArrayList();
            List list9 = (List) MainUi.map.get("quanJuComponeList");
            //波特率
            JComboBox box = (JComboBox) list9.get(0);
            list.add(box.getSelectedIndex());

            //灯光协议
            box = (JComboBox) list9.get(1);
            list.add(box.getSelectedIndex());

            //DMX字节间隔
            box = (JComboBox) list9.get(2);
            list.add(box.getSelectedIndex());

            //DMX帧间隔
            box = (JComboBox) list9.get(3);
            list.add(box.getSelectedIndex());
            //摇麦1状态地址
            JRadioButton radioButton2 = (JRadioButton) list9.get(6);
            if (radioButton2.isSelected()) {
                list.add(0);
            } else {
                list.add(1);
            }
            //摇麦2状态地址
            radioButton2 = (JRadioButton) list9.get(8);
            if (radioButton2.isSelected()) {
                list.add(0);
            } else {
                list.add(1);
            }

            //摇麦间隔
            JComboBox box2 = (JComboBox) MainUi.map.get("yaoMaiJianGeBox");
            list.add(box2.getSelectedIndex());
            //摇麦开关
            radioButton2 = (JRadioButton) MainUi.map.get("yaoMaiKaiGuangBtn1");
            if (radioButton2.isSelected()) {
                list.add(0);
            } else {
                list.add(1);
            }
            xmlEncoder.writeObject(list);


            /////////////////////////////中控离散数据区
            List list2 = new ArrayList();
            list9 = (ArrayList) MainUi.map.get("settingZhongKongListCompone");
            //服务灯
            JRadioButton radioButton = (JRadioButton) list9.get(0);
            if (radioButton.isSelected()) {
                list2.add(0);
            } else {
                list2.add(1);
            }
            //红外码频率
            JSlider slider = (JSlider) list9.get(8);
            list2.add(slider.getValue());
            //灯光上行
            radioButton = (JRadioButton) list9.get(2);
            if (radioButton.isSelected()) {
                list2.add(0);
            } else {
                list2.add(1);
            }
            //空调上行
            radioButton = (JRadioButton) list9.get(4);
            if (radioButton.isSelected()) {
                list2.add(0);
            } else {
                list2.add(1);
            }
            //红外单多码
            radioButton = (JRadioButton) list9.get(6);
            if (radioButton.isSelected()) {
                list2.add(0);
            } else {
                list2.add(1);
            }
            xmlEncoder.writeObject(list2);
            //////////////////////////////////////////////////
            ///////////////////////////空调数据区/////////////
            List list3 = new ArrayList();
            setKtData(list3);
            xmlEncoder.writeObject(list3);
            ///////////////灯库////////////////////////
            xmlEncoder.writeObject(Data.DengKuChannelCountList);
            xmlEncoder.writeObject(Data.DengKuVersionList);
            xmlEncoder.writeObject(Data.DengKuList);
            xmlEncoder.writeObject(Data.dengKuBlackOutAndSpeedList);
            NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            xmlEncoder.writeObject(model.getDataVector());
            ////////////////灯具///////////////////
            table = (NewJTable) MainUi.map.get("table_dengJu");
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }
            model = (DefaultTableModel) table.getModel();
            xmlEncoder.writeObject(model.getDataVector());
            /////////////////灯具分组//////////////////////
            table = (NewJTable) MainUi.map.get("GroupTable");
            if (table.isEditing()) {
                table.getCellEditor().stopCellEditing();
            }
            model = (DefaultTableModel) table.getModel();
            xmlEncoder.writeObject(model.getDataVector());
            xmlEncoder.writeObject(Data.GroupOfLightList);
            /////////////素材管理//////////////////////
            xmlEncoder.writeObject(Data.suCaiMap);
            xmlEncoder.writeObject(Data.SuCaiObjects);
            xmlEncoder.writeObject(Data.suCaiNameMap);
//			xmlEncoder.writeObject(Data.AddSuCaiOrder);
            ///////////////////////////////////////////
            /////////////效果灯编程-雾机//////////////////
            xmlEncoder.writeObject(Data.wuJiMap);
            //////效果灯编程-摇麦
            xmlEncoder.writeObject(Data.YaoMaiMap);
            ////////////////////效果灯编程-DMX模式
            xmlEncoder.writeObject(Data.XiaoGuoDengObjects);
            Object[][][] objects = new Object[24][30][20];
            for (int j = 1; j <= 24; j++) {
                JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + j);
                for (int k = 1; k < 31; k++) {
                    for (int i = 0; i < timeBlockPanels[k].getComponentCount(); i++) {
                        DefineJLable lable = (DefineJLable) timeBlockPanels[k].getComponent(i);
                        String[] strings = new String[5];
                        strings[0] = lable.getLocation().x + "";
                        strings[1] = lable.getLocation().y + "";
                        strings[2] = lable.getWidth() + "";
                        strings[3] = lable.getHeight() + "";
                        strings[4] = lable.getText();
                        objects[j - 1][k - 1][i] = strings;
                    }
                }
            }
            xmlEncoder.writeObject(objects);

            //声控素材
            xmlEncoder.writeObject(Data.shengKonSuCaiMap);
            xmlEncoder.writeObject(Data.shengKonSuCaiNameMap);
            xmlEncoder.writeObject(Data.ShengKonSuCai);

            //////////声控--环境灯模式数据
            xmlEncoder.writeObject(Data.ShengKonHuanJingModelMap);

            //////////声控--倒喝彩效果灯模式数据
            xmlEncoder.writeObject(Data.DaoHeCaiMap);

            /////////声控--效果灯模式叠加DMX
            xmlEncoder.writeObject(Data.ShengKonModelDmxMap);
            /////////声控--效果灯模式设置
            xmlEncoder.writeObject(Data.ShengKonModelSet);

            /////声控--效果灯模式顺序设置数据
            xmlEncoder.writeObject(Data.ShengKonShiXuSetObjects);
            /////声控--效果灯模式时间块数据
            Object[][][] objects2 = new Object[16][30][20];//时间长度
            int[][][] abc = new int[16][30][20];
            for (int j = 1; j <= 16; j++) {
                JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels" + j);
                for (int k = 1; k <= 30; k++) {
                    for (int i = 0; i < timeBlockPanels[k - 1].getComponentCount(); i++) {
                        if (timeBlockPanels[k - 1].isVisible()) {
                            if ((k - 1) == 0) {
                                DefineJLable_shengKon lable = (DefineJLable_shengKon) timeBlockPanels[k - 1].getComponent(i);
                                String[] strings = new String[5];
                                strings[0] = lable.getLocation().x + "";
                                strings[1] = lable.getLocation().y + "";
                                strings[2] = lable.getWidth() + "";
                                strings[3] = lable.getHeight() + "";
                                strings[4] = lable.getText();
                                objects2[j - 1][k - 1][i] = strings;
                            } else {
                                DefineJLable_shengKon2 lable = (DefineJLable_shengKon2) timeBlockPanels[k - 1].getComponent(i);
                                String[] strings = new String[6];
                                strings[0] = lable.getLocation().x + "";
                                strings[1] = lable.getLocation().y + "";
                                strings[2] = lable.getWidth() + "";
                                strings[3] = lable.getHeight() + "";
                                strings[4] = lable.getText();
                                if (lable.getBackground().getGreen() == 255)
                                    strings[5] = "255";
                                else
                                    strings[5] = "0";
                                objects2[j - 1][k - 1][i] = strings;
                            }
                        }
                    }
                }
//				for (int i = 0; i < timeBlockPanels[0].getComponentCount(); i++) {
//					DefineJLable_shengKon lable = (DefineJLable_shengKon)timeBlockPanels[0].getComponent(i);
//					String[] strings = new String[5];
//					strings[0] = lable.getLocation().x+"";
//					strings[1] = lable.getLocation().y+"";
//					strings[2] = lable.getWidth()+"";
//					strings[3] = lable.getHeight()+"";
//					strings[4] = lable.getText();
//					objects2[j-1][i] = strings;
//				}
                for (int i = 1; i < timeBlockPanels.length; i++) {
                    for (int k = 0; k < timeBlockPanels[i].getComponentCount(); k++) {
                        DefineJLable_shengKon2 lable2 = (DefineJLable_shengKon2) timeBlockPanels[i].getComponent(k);
                        if (!lable2.getBackground().equals(Color.red)) {
                            abc[j - 1][i - 1][k] = 1;
                        }
                    }

                }
            }
            xmlEncoder.writeObject(objects2);
            xmlEncoder.writeObject(abc);
            xmlEncoder.writeObject(Data.ShengKonEditObjects);

            //声控模式：调用灯光场景
            List list88 = null;
            int[][] sl = new int[16][2];
            for (int i = 1; i < 17; i++) {
                list88 = (List) MainUi.map.get("callLightScen" + i);
                JRadioButton radioButton3 = (JRadioButton) list88.get(0);
                if (radioButton3.isSelected()) {
                    sl[i - 1][0] = 1;
                }
                JComboBox box3 = (JComboBox) list88.get(2);
                sl[i - 1][1] = box3.getSelectedIndex();
            }

            xmlEncoder.writeObject(sl);

            //图形动作参数
            xmlEncoder.writeObject(bezier.Data.itemMap);
            xmlEncoder.writeObject(bezier.Data.map);

            //效果灯编程界面多灯
            xmlEncoder.writeObject(Data.XiaoGuoDengModelDmxMap);
            Object[][] objects1 = new Object[24][5];
            for (int i = 0; i < 24; i++) {
                DefineJLable3 lable3 = (DefineJLable3) MainUi.map.get("SuoYouDengZuLable" + (i + 1));
                String[] str = new String[5];
                str[0] = lable3.getLocation().x + "";
                str[1] = lable3.getLocation().y + "";
                str[2] = lable3.getWidth() + "";
                str[3] = lable3.getHeight() + "";
                str[4] = lable3.isEnabled() + "";
                objects1[i] = str;
            }
            xmlEncoder.writeObject(objects1);

            xmlEncoder.flush();
            xmlEncoder.close();
            //JFrame frame = (JFrame)MainUi.map.get("frame");
            //JOptionPane.showMessageDialog(frame, "工程保存成功！", "提示", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    void setKtData(List list) {
        //冷热模式
        JComboBox box1 = (JComboBox) MainUi.map.get("kongTiaoModelBox");
        list.add(box1.getSelectedIndex());

        //风速档位
        box1 = (JComboBox) MainUi.map.get("kongTiaoDangWeiBox");
        list.add(box1.getSelectedIndex());
        //当前温度
        box1 = (JComboBox) MainUi.map.get("kongTiaoNowWenDuBox");
        list.add(box1.getSelectedIndex());
        //设定温度
        box1 = (JComboBox) MainUi.map.get("kongTiaoSetWenDuBox");
        list.add(box1.getSelectedIndex());
        //排风开关
        box1 = (JComboBox) MainUi.map.get("kongTiaoPaiFengBox");
        list.add(box1.getSelectedIndex());
        //空调默认、预设选择
        JRadioButton radioButton1 = (JRadioButton) MainUi.map.get("kongTiaoKaiJiBtn1");
        if (radioButton1.isSelected()) {
            list.add(0);
        } else {
            list.add(1);
        }

        //空调阀模式
        box1 = (JComboBox) MainUi.map.get("kongTiaoFengJiTypeBox");
        list.add(box1.getSelectedIndex());
        //显温度
        radioButton1 = (JRadioButton) MainUi.map.get("kongTiaoShowWenDuBtn1");
        if (radioButton1.isSelected()) {
            list.add(0);
        } else {
            list.add(1);
        }

        //中央空调模式
        box1 = (JComboBox) MainUi.map.get("kongTiaoCenterModelBox");
        list.add(box1.getSelectedIndex());
        //485空调地址
        box1 = (JComboBox) MainUi.map.get("kongTiaoRS485AddBox");
        list.add(box1.getSelectedIndex());
    }
}
