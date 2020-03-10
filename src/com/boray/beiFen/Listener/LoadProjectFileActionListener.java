package com.boray.beiFen.Listener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.boray.Data.Data;
import com.boray.Data.MyColor;
import com.boray.Utils.Util;
import com.boray.changJing.Data.DataOfChangJing;
import com.boray.changJing.Listener.ChangJingSendCodeListener;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.shengKon.UI.DefineJLable_shengKon;
import com.boray.shengKon.UI.DefineJLable_shengKon2;
import com.boray.xiaoGuoDeng.UI.DefineJLable;
import com.boray.zhongKon.Data.DataOfZhongKon;
import com.boray.zhongKon.Listener.ShowAndSaveCode;

public class LoadProjectFileActionListener implements ActionListener {
    private JDialog dialog;
    private JRadioButton radioButton, radioButton2;

    private void loadPjtUI() {
        int a = 0;

        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new JDialog(f, true);
        dialog.setResizable(false);
        dialog.setTitle("���ع���");
        int w = 380, h = 200;
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
        dialog.setSize(w, h);
        dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        radioButton = new JRadioButton("ȫ��");
        radioButton2 = new JRadioButton("�п�");
        //JRadioButton radioButton3 = new JRadioButton("Ч����");
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton);
        group.add(radioButton2);//group.add(radioButton3);
        radioButton.setSelected(true);
        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(300, 200));
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setVgap(40);
        flowLayout.setHgap(26);
        p1.setLayout(flowLayout);
        p1.add(radioButton);
        p1.add(new JLabel("     "));
        p1.add(radioButton2);//p1.add(radioButton3);
        JButton sureBtn = new JButton("ȷ��");
        JButton canceBtn = new JButton("ȡ��");
        sureBtn.addActionListener(this);
        canceBtn.addActionListener(this);
        sureBtn.setName("sure");
        canceBtn.setName("cance");
        p1.add(sureBtn);
        p1.add(new JLabel(" "));
        p1.add(canceBtn);
        dialog.add(p1);
        dialog.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        if ("sure".equals(btn.getName())) {
            int type = 0;
            if (radioButton.isSelected()) {
                type = 1;//ȫ��
            } else if (radioButton2.isSelected()) {
                type = 2;//�п�
            }
            dialog.dispose();
            loadFile(type);
            Data.file = null;
        } else if ("cance".equals(btn.getName())) {
            dialog.dispose();
        } else {
            loadPjtUI();
        }
    }

    void loadFile(int type) {
        JFileChooser fileChooser = new JFileChooser();
        try {
			/*String path = getClass().getResource("/SD���ļ�/").getPath().substring(1);
			path = URLDecoder.decode(path,"utf-8"); 
			fileChooser.setCurrentDirectory(new File(path));*/

            if (!"".equals(Data.projectFilePath)) {
                fileChooser.setCurrentDirectory(new File(Data.projectFilePath));
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        String[] houZhui = {"xml"};
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.xml", houZhui);
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showOpenDialog((JFrame) MainUi.map.get("frame"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            Data.projectFilePath = file.getParent();
            try {
                Util.stopAutoSaveFile();
                tt(file, type);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void tt(File file, int type) {
        try {
            InputStream is = new FileInputStream(file);
            XMLDecoder xmlDecoder = new XMLDecoder(is);
            //����������
            if (type == 2) {
                xmlDecoder.readObject();
            } else {
                DataOfChangJing.map = (Map) xmlDecoder.readObject();
            }

            //�п�ѧϰ��
            //����ǽ��
            DataOfZhongKon.map2 = (Map) xmlDecoder.readObject();

            //��������
            DataOfZhongKon.map3 = (Map) xmlDecoder.readObject();

            //��������
            DataOfZhongKon.map4 = (Map) xmlDecoder.readObject();

            //���������
            DataOfZhongKon.map1 = (Map) xmlDecoder.readObject();

            //���Ȳ�
            DataOfZhongKon.map5 = (Map) xmlDecoder.readObject();

            /////////////////////////ȫ������������/////////////////////
            List list = (ArrayList) xmlDecoder.readObject();
            List list9 = (List) MainUi.map.get("quanJuComponeList");
            //������
            JComboBox box = (JComboBox) list9.get(0);
            box.setSelectedIndex((int) (list.get(0)));
            //�ƹ�Э��
            box = (JComboBox) list9.get(1);
            box.setSelectedIndex((int) (list.get(1)));
            //DMX�ֽڼ��
            box = (JComboBox) list9.get(2);
            box.setSelectedIndex((int) (list.get(2)));
            //DMX֡���
            box = (JComboBox) list9.get(3);
            box.setSelectedIndex((int) (list.get(3)));
            //ҡ��1״̬��ַ
            JRadioButton radioButton2 = (JRadioButton) list9.get(6);
            if ((int) (list.get(4)) == 0) {
                radioButton2.setSelected(true);
            } else {
                radioButton2 = (JRadioButton) list9.get(7);
                radioButton2.setSelected(true);
            }
            //ҡ��2״̬��ַ
            radioButton2 = (JRadioButton) list9.get(8);
            if ((int) (list.get(5)) == 0) {
                radioButton2.setSelected(true);
            } else {
                radioButton2 = (JRadioButton) list9.get(9);
                radioButton2.setSelected(true);
            }
            //ҡ����
            JComboBox box2 = (JComboBox) MainUi.map.get("yaoMaiJianGeBox");
            box2.setSelectedIndex((int) (list.get(6)));
            //ҡ�󿪹�
            radioButton2 = (JRadioButton) MainUi.map.get("yaoMaiKaiGuangBtn1");
            if ((int) (list.get(7)) == 0) {
                radioButton2.setSelected(true);
            } else {
                radioButton2 = (JRadioButton) MainUi.map.get("yaoMaiKaiGuangBtn2");
                radioButton2.setSelected(true);
            }
            ////////////////////////////////////////////////////////////
            ////////////////////////////////�п���ɢ������/////////
            List list2 = (ArrayList) xmlDecoder.readObject();
            list9 = (ArrayList) MainUi.map.get("settingZhongKongListCompone");
            //�����
            JRadioButton radioButton = (JRadioButton) list9.get(0);
            if ((int) (list2.get(0)) == 0) {
                radioButton.setSelected(true);
            } else {
                radioButton = (JRadioButton) list9.get(1);
                radioButton.setSelected(true);
            }
            //������Ƶ��
            JSlider slider = (JSlider) list9.get(8);
            slider.setValue((int) (list2.get(1)));

            //�ƹ�����
            radioButton = (JRadioButton) list9.get(2);
            if ((int) (list2.get(2)) == 0) {
                radioButton.setSelected(true);
            } else {
                radioButton = (JRadioButton) list9.get(3);
                radioButton.setSelected(true);
            }
            //�յ�����
            radioButton = (JRadioButton) list9.get(4);
            if ((int) (list2.get(3)) == 0) {
                radioButton.setSelected(true);
            } else {
                radioButton = (JRadioButton) list9.get(5);
                radioButton.setSelected(true);
            }

            //���ⵥ����
            radioButton = (JRadioButton) list9.get(6);
            if ((int) (list2.get(4)) == 0) {
                radioButton.setSelected(true);
            } else {
                radioButton = (JRadioButton) list9.get(7);
                radioButton.setSelected(true);
            }
            /////ˢ���пؽ���
            box = (JComboBox) MainUi.map.get("zhongKonGroupBox");
            new ShowAndSaveCode().show(box.getSelectedIndex());
            /////ˢ�³�������/////////////////////////////////////////////
            if (type != 2) {
                int n = Data.changJingModel;
                if (n != -1) {
                    ChangJingSendCodeListener changJingSendCodeListener = new ChangJingSendCodeListener();
                    changJingSendCodeListener.loadData(n);
                }
                /////////////////�յ�������///////////////////////
                List list3 = (ArrayList) xmlDecoder.readObject();
                setKtData(list3);
                ////////////////////////////////////////////

                /////////////////�ƿ�///////////////////
                Data.DengKuChannelCountList = (List) xmlDecoder.readObject();
                Data.DengKuVersionList = (List) xmlDecoder.readObject();
                Data.DengKuList = (List) xmlDecoder.readObject();
                Data.dengKuBlackOutAndSpeedList = (List) xmlDecoder.readObject();
                Vector vector = (Vector) xmlDecoder.readObject();
                NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                for (int i = table.getRowCount() - 1; i >= 0; i--) {
                    model.removeRow(i);
                }
                if (vector != null) {
                    Vector tp = null;
                    int a = 0;
                    for (int i = 0; i < vector.size(); i++) {
                        tp = (Vector) vector.get(i);
                        model.addRow(tp);
                    }
                    if (table.getRowCount() > 0) {
                        table.setRowSelectionInterval(0, 0);
                    }
                }
                /////////////////////�ƾ�///////////////////////////
                vector = (Vector) xmlDecoder.readObject();
                table = (NewJTable) MainUi.map.get("table_dengJu");
                model = (DefaultTableModel) table.getModel();
                for (int i = table.getRowCount() - 1; i >= 0; i--) {
                    model.removeRow(i);
                }
                if (vector != null) {
                    Vector tp = null;
                    int a = 0;
                    for (int i = 0; i < vector.size(); i++) {
                        tp = (Vector) vector.get(i);
                        model.addRow(tp);
                    }
                    if (table.getRowCount() > 0) {
                        table.setRowSelectionInterval(0, 0);
                    }
                }
                ///////////////////////�ƾ߷���/////////////////////
                vector = (Vector) xmlDecoder.readObject();
                Data.GroupOfLightList = (List) xmlDecoder.readObject();
                table = (NewJTable) MainUi.map.get("GroupTable");
                model = (DefaultTableModel) table.getModel();
                for (int i = table.getRowCount() - 1; i >= 0; i--) {
                    model.removeRow(i);
                }
                if (vector != null) {
                    Vector tp = null;
                    int a = 0;
                    for (int i = 0; i < vector.size(); i++) {
                        tp = (Vector) vector.get(i);
                        model.addRow(tp);
                    }
                    if (table.getRowCount() > 0) {
                        table.setRowSelectionInterval(0, 0);
                    }
                }
                ///////////////////���еƾ�///////////////////////
                NewJTable t2 = (NewJTable) MainUi.map.get("table_dengJu");
                table = (NewJTable) MainUi.map.get("allLightTable");
                model = (DefaultTableModel) table.getModel();
                for (int i = table.getRowCount() - 1; i >= 0; i--) {
                    model.removeRow(i);
                }
                String[][] s = new String[t2.getRowCount()][1];
                for (int i = 0; i < s.length; i++) {
                    s[i][0] = "ID" + (i + 1) + "#" + t2.getValueAt(i, 2).toString()
                            + "--" + t2.getValueAt(i, 5).toString() + "--" + t2.getValueAt(i, 6).toString();
                    model.addRow(s[i]);
                }
                ////////////////////////////////////////////////
                ///////////////�زĹ���///////////////////////
                Data.suCaiMap = (Map) xmlDecoder.readObject();
                Data.SuCaiObjects = (Object[][]) xmlDecoder.readObject();
                Data.suCaiNameMap = (Map) xmlDecoder.readObject();
//				Data.AddSuCaiOrder = (List<String>) xmlDecoder.readObject();
                JList suCaiList = (JList) MainUi.map.get("suCaiLightType");
                if (suCaiList.getSelectedValue() != null) {
                    {
                        Map map = (Map) Data.suCaiMap.get(suCaiList.getSelectedValue().toString());
                        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
                        String[] name = {"����", "��ҡ", "����", "���", "����", "��ܰ", "����", "�λ�", "����"};
                        if (map != null) {
                            for (int i = 0; i < btns.length; i++) {
                                List abc = (List) map.get("" + i);
                                int size = 0;
                                if (abc != null) {
                                    size = abc.size();
                                }
                                btns[i].setText(name[i] + "(" + size + ")");
                            }
                        } else {
                            for (int i = 0; i < btns.length; i++) {
                                btns[i].setText(name[i] + "(0)");
                            }
                        }
                        //
                        int selected = 0;
                        for (int i = 0; i < btns.length; i++) {
                            if (btns[i].isSelected()) {
                                selected = i;
                                break;
                            }
                        }

                        Map nameMap = (Map) Data.suCaiNameMap.get(suCaiList.getSelectedValue().toString());
                        JList suCai_list = (JList) MainUi.map.get("suCai_list");
                        DefaultListModel suCai_list_model = (DefaultListModel) suCai_list.getModel();
                        suCai_list_model.removeAllElements();
                        if (nameMap != null) {
                            List tmp = (List) nameMap.get("" + selected);
                            if (tmp != null) {
                                for (int i = 0; i < tmp.size(); i++) {
                                    suCai_list_model.addElement(tmp.get(i).toString());
                                }
                                if (tmp.size() > 0) {
                                    suCai_list.setSelectedIndex(0);
                                }
                            }
                        }
                    }
                }
                /////////////Ч���Ʊ��-���//////////////////
                Data.wuJiMap = (Map) xmlDecoder.readObject();
                //////Ч���Ʊ��-ҡ��
                Data.YaoMaiMap = (Map) xmlDecoder.readObject();
                ////////////////////Ч���Ʊ��-DMXģʽ
                Data.XiaoGuoDengObjects = (Object[][][]) xmlDecoder.readObject();
                Object[][][] objects = (Object[][][]) xmlDecoder.readObject();
                for (int j = 1; j <= 24; j++) {
                    JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + j);
                    for (int k = 1; k < 31; k++) {
                        timeBlockPanels[k].removeAll();
                        for (int i = 0; i < 20; i++) {
                            if (objects[j - 1][k - 1][i] != null) {
                                String[] strings = (String[]) objects[j - 1][k - 1][i];
                                DefineJLable lable = new DefineJLable(strings[4].substring(0, strings[4].length() - 1), timeBlockPanels[k]);
                                lable.setText(strings[4]);
                                lable.setLocation(new Point(Integer.valueOf(strings[0]).intValue(), Integer.valueOf(strings[1]).intValue()));
                                lable.setSize(Integer.valueOf(strings[2]).intValue(), Integer.valueOf(strings[3]).intValue());
                                int c = Integer.valueOf(strings[4].substring(0, strings[4].indexOf("("))) - 1;
                                if (c >= 10) {
                                    c = c - 10;
                                }
                                lable.setBackground(MyColor.colors[c]);
                                timeBlockPanels[k].add(lable);
                            }
                        }
                    }
                }

                //�����ز�
                Data.shengKonSuCaiMap = (Map) xmlDecoder.readObject();
                Data.shengKonSuCaiNameMap = (Map) xmlDecoder.readObject();
                Data.ShengKonSuCai = (Object[][]) xmlDecoder.readObject();

                //////////����--������ģʽ����
                Data.ShengKonHuanJingModelMap = (Map) xmlDecoder.readObject();
                //////////����--���Ȳ�Ч����ģʽ����
                Data.DaoHeCaiMap = (Map) xmlDecoder.readObject();
                /////////����--Ч����ģʽ����DMX
                Data.ShengKonModelDmxMap = (Map) xmlDecoder.readObject();
                /////////����--Ч����ģʽ����
                Data.ShengKonModelSet = (Map) xmlDecoder.readObject();
                /////����--Ч����ģʽ˳����������
                Data.ShengKonShiXuSetObjects = (Object[][]) xmlDecoder.readObject();
                //xmlDecoder.readObject();
                /////����--Ч����ģʽʱ�������
                Object[][][] objects2 = (Object[][][]) xmlDecoder.readObject();
                int[][][] abc = (int[][][]) xmlDecoder.readObject();
                for (int j = 1; j <= 16; j++) {
                    JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels" + j);
                    for (int e = 0; e < timeBlockPanels.length; e++) {
                        timeBlockPanels[e].removeAll();
                        //timeBlockPanels[k].repaint();
                    }
                    for (int k = 0; k < 30; k++) {
                        for (int i = 0; i < 20; i++) {
                            if (objects2[j - 1][k][i] != null) {
                                if (k == 0) {
                                    String[] strings = (String[]) objects2[j - 1][k][i];
                                    DefineJLable_shengKon lable = new DefineJLable_shengKon(strings[4], timeBlockPanels[k]);
                                    lable.setText(strings[4]);
                                    lable.setLocation(new Point(Integer.valueOf(strings[0]).intValue(), Integer.valueOf(strings[1]).intValue()));
                                    lable.setSize(Integer.valueOf(strings[2]).intValue(), Integer.valueOf(strings[3]).intValue());
                                    int c = 0;
                                    if (strings[4].indexOf("(") != -1)
                                        c = Integer.valueOf(strings[4].substring(0, strings[4].indexOf("("))) - 1;
                                    else
                                        c = Integer.valueOf(strings[4]) - 1;
                                    if (c >= 10) {
                                        c = c - 10;
                                    }
                                    lable.setBackground(MyColor.colors[c]);
                                    timeBlockPanels[k].add(lable);
                                } else {
                                    String[] strings = (String[]) objects2[j - 1][k][i];
                                    DefineJLable_shengKon2 lable = new DefineJLable_shengKon2(strings[4], timeBlockPanels[k]);
                                    lable.setText(strings[4]);
                                    lable.setLocation(new Point(Integer.valueOf(strings[0]).intValue(), Integer.valueOf(strings[1]).intValue()));
                                    lable.setSize(Integer.valueOf(strings[2]).intValue(), Integer.valueOf(strings[3]).intValue());
                                    if (strings[5].equals("255"))
                                        lable.setBackground(new Color(0, 255, 0));
                                    else
                                        lable.setBackground(Color.red);
                                    timeBlockPanels[k].add(lable);
                                }
                            }
                        }
                    }
//                    for (int i = 0; i < 20; i++) {
//                        if (objects2[j - 1][i] != null) {
//                            String[] strings = (String[]) objects2[j - 1][i];
//                            DefineJLable_shengKon lable = new DefineJLable_shengKon(strings[4], timeBlockPanels[0]);
//                            lable.setText(strings[4]);
//                            lable.setLocation(new Point(Integer.valueOf(strings[0]).intValue(), Integer.valueOf(strings[1]).intValue()));
//                            lable.setSize(Integer.valueOf(strings[2]).intValue(), Integer.valueOf(strings[3]).intValue());
//                            int c = Integer.valueOf(strings[4]) - 1;
//                            if (c >= 10) {
//                                c = c - 10;
//                            }
//                            lable.setBackground(MyColor.colors[c]);
//                            timeBlockPanels[0].add(lable);
//                            for (int k = 1; k < timeBlockPanels.length; k++) {
//                                c = timeBlockPanels[k].getComponentCount();
//                                DefineJLable_shengKon2 label2 = new DefineJLable_shengKon2((c + 1) + "", timeBlockPanels[k]);
//                                ////////////////////////////////////////////////////////��
//                                if (c > 0) {
//                                    DefineJLable_shengKon2 defineJLable = (DefineJLable_shengKon2) timeBlockPanels[k].getComponent(c - 1);
//                                    int x = defineJLable.getLocation().x + defineJLable.getWidth();
//                                    int y = defineJLable.getLocation().y;
//                                    label2.setLocation(new Point(x, y));
//                                }
//                                label2.setSize(Integer.valueOf(strings[2]).intValue(), Integer.valueOf(strings[3]).intValue());
//								/*if (c >= 10) {
//									c = c - 10;
//								}*/
//                                if (abc[j - 1][k - 1][c] == 1) {
//                                    label2.setBackground(new Color(0, 255, 0));
//                                } else {
//                                    label2.setBackground(Color.red);
//                                }
//                                timeBlockPanels[k].add(label2);
//                                timeBlockPanels[k].updateUI();
//                            }
//                        }
//                    }
                }
                Data.ShengKonEditObjects = (Object[][][]) xmlDecoder.readObject();

                //����ģʽ�����õƹⳡ��
                int[][] cc = (int[][]) xmlDecoder.readObject();
                List list88 = null;
                for (int i = 1; i < 17; i++) {
                    list88 = (List) MainUi.map.get("callLightScen" + i);
                    JRadioButton radioButton3 = (JRadioButton) list88.get(0);
                    if (cc[i - 1][0] == 1) {
                        radioButton3.setSelected(true);
                    } else {
                        radioButton3 = (JRadioButton) list88.get(1);
                        radioButton3.setSelected(true);
                    }
                    JComboBox box3 = (JComboBox) list88.get(2);
                    box3.setSelectedIndex(cc[i - 1][1]);
                }

                //����ͼ�β���
                bezier.Data.itemMap = (HashMap) xmlDecoder.readObject();
                bezier.Data.map = (HashMap) xmlDecoder.readObject();

            }

        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    void setKtData(List list) {
        //����ģʽ
        JComboBox box1 = (JComboBox) MainUi.map.get("kongTiaoModelBox");
        box1.setSelectedIndex((int) (list.get(0)));

        //���ٵ�λ
        box1 = (JComboBox) MainUi.map.get("kongTiaoDangWeiBox");
        box1.setSelectedIndex((int) (list.get(1)));
        //��ǰ�¶�
        box1 = (JComboBox) MainUi.map.get("kongTiaoNowWenDuBox");
        box1.setSelectedIndex((int) (list.get(2)));
        //�趨�¶�
        box1 = (JComboBox) MainUi.map.get("kongTiaoSetWenDuBox");
        box1.setSelectedIndex((int) (list.get(3)));
        //�ŷ翪��
        box1 = (JComboBox) MainUi.map.get("kongTiaoPaiFengBox");
        box1.setSelectedIndex((int) (list.get(4)));
        //�յ�Ĭ�ϡ�Ԥ��ѡ��
        JRadioButton radioButton1 = (JRadioButton) MainUi.map.get("kongTiaoKaiJiBtn1");
        if ((int) (list.get(5)) == 0) {
            radioButton1.setSelected(true);
        } else {
            radioButton1 = (JRadioButton) MainUi.map.get("kongTiaoKaiJiBtn2");
            radioButton1.setSelected(true);
        }
        //�յ���ģʽ
        box1 = (JComboBox) MainUi.map.get("kongTiaoFengJiTypeBox");
        box1.setSelectedIndex((int) (list.get(6)));
        //���¶�
        radioButton1 = (JRadioButton) MainUi.map.get("kongTiaoShowWenDuBtn1");
        if ((int) (list.get(7)) == 0) {
            radioButton1.setSelected(true);
        } else {
            radioButton1 = (JRadioButton) MainUi.map.get("kongTiaoShowWenDuBtn2");
            radioButton1.setSelected(true);
        }
        //����յ�ģʽ
        box1 = (JComboBox) MainUi.map.get("kongTiaoCenterModelBox");
        box1.setSelectedIndex((int) (list.get(8)));
        //485�յ���ַ
        box1 = (JComboBox) MainUi.map.get("kongTiaoRS485AddBox");
        box1.setSelectedIndex((int) (list.get(9)));
    }
}
