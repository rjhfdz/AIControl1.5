package com.boray.changJing.UI;

import com.boray.Data.ChangJinData;
import com.boray.Data.Data;
import com.boray.changJing.Listener.*;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.OutputStream;

public class ProgrammePaneUI {

    private ComboxItemListener itemListener8;
    private RadioActionListener2 radioActionListener2;

    /**
     * ���س�����̽���
     *
     * @param pane
     */
    public void show(JPanel pane) {

        itemListener8 = new ComboxItemListener();
        radioActionListener2 = new RadioActionListener2();

        FlowLayout flowLayout5 = new FlowLayout(FlowLayout.LEFT);
        flowLayout5.setVgap(0);
        flowLayout5.setHgap(0);
        pane.setPreferredSize(new Dimension(1010, 700));
        pane.setLayout(flowLayout5);
        JPanel changJing = new JPanel();
        changJing.setPreferredSize(new Dimension(1010, 40));
        JLabel dengGuangLabel = new JLabel("�ƹⳡ��:");
        JLabel gonNengLabel = new JLabel("���ܳ���:");
        JComboBox dengGuangBoxs = new JComboBox();
        for (int i = 0; i < 32; i++) {
            dengGuangBoxs.addItem(ChangJinData.T1()[i]);
        }
        JComboBox gonNengBoxs = new JComboBox();
        for (int i = 0; i < 4; i++) {
            gonNengBoxs.addItem(ChangJinData.T1()[i + 32]);
        }
        changJing.setBorder(new LineBorder(Color.gray));
        JLabel label = new JLabel();
        MainUi.map.put("changJingBianChengLabel", label);
        changJing.add(label);
        changJing.add(dengGuangLabel);
        changJing.add(dengGuangBoxs);
        changJing.add(gonNengLabel);
        changJing.add(gonNengBoxs);

        ChangJingSelectItemListener changJingSelectItemListener = new ChangJingSelectItemListener();

        dengGuangBoxs.addItemListener(changJingSelectItemListener);
        gonNengBoxs.addItemListener(changJingSelectItemListener);
        label.setText("���ڱ༭����"+dengGuangBoxs.getSelectedIndex()+"������");

        DengJuKaiGuangItemListener itemListener = new DengJuKaiGuangItemListener();

        JPanel p9 = new JPanel();
        TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "��������", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p9.setBorder(tb1);
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.CENTER);
        flowLayout2.setVgap(-2);
        flowLayout2.setHgap(4);
        FlowLayout flowLayout7 = new FlowLayout(FlowLayout.CENTER);
        flowLayout7.setVgap(-8);
        flowLayout7.setHgap(4);
        p9.setLayout(flowLayout7);
        p9.setPreferredSize(new Dimension(1010, 75));
//        panel.add(p9,BorderLayout.NORTH);

        JPanel[] panels8 = new JPanel[8];
        JLabel[] labels8 = new JLabel[8];
        JComboBox[] boxs8 = new JComboBox[8];
        MainUi.map.put("kaiGuangBox_BuKeTiao", boxs8);
        for (int i = 0; i < boxs8.length; i++) {
            labels8[i] = new JLabel("��" + (i + 1));
            boxs8[i] = new JComboBox();
            boxs8[i].setBorder(BorderFactory.createEmptyBorder(-2, 0, -2, 0));
            boxs8[i].addItem("��");
            boxs8[i].addItem("��");
            boxs8[i].addItem("����");
            boxs8[i].addItemListener(itemListener);
            boxs8[i].setPreferredSize(new Dimension(90, 30));
            panels8[i] = new JPanel();
            panels8[i].setPreferredSize(new Dimension(100, 160));
            panels8[i].setLayout(flowLayout2);
            panels8[i].add(labels8[i]);
            panels8[i].add(boxs8[i]);
            p9.add(panels8[i]);
        }
        JPanel p10 = new JPanel();
        TitledBorder tb8 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "�ɵ�����", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p10.setBorder(tb8);
        p10.setPreferredSize(new Dimension(440, 160));
        p10.setLayout(flowLayout5);
//        panel.add(p10);

        JPanel[] panels = new JPanel[4];
        JLabel[] labels = new JLabel[4];
        JComboBox[] boxs1 = new JComboBox[4];
        final JSlider[] sliders = new JSlider[4];
        final JComboBox[] boxs2 = new JComboBox[4];
        MainUi.map.put("kaiGuangBox", boxs1);
        MainUi.map.put("liangDuSliders", sliders);
        MainUi.map.put("liangDuBoxs", boxs2);
        for (int i = 0; i < boxs2.length; i++) {
            final int a = i;
            labels[i] = new JLabel("��" + (i + 1));
            labels[i].setBorder(BorderFactory.createEmptyBorder(-2, 0, -2, 0));
            //labels[i].setBorder(new LineBorder(Color.black));
            boxs1[i] = new JComboBox();
            boxs1[i].setBorder(BorderFactory.createEmptyBorder(-2, 0, -2, 0));
            boxs1[i].addItem("��");
            boxs1[i].addItem("��");
            boxs1[i].addItem("����");
            boxs1[i].addItemListener(itemListener);
            boxs1[i].setPreferredSize(new Dimension(90, 24));
            sliders[i] = new JSlider(0, 100, 0);
            sliders[i].setMinorTickSpacing(2);
            sliders[i].setOrientation(SwingConstants.VERTICAL);
            sliders[i].setPreferredSize(new Dimension(30, 70));
            sliders[i].addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    if (sliders[a].getValue() % 2 != 0) {
                        sliders[a].setValue(sliders[a].getValue() + 1);
                    }
                    itemListener.sendData();
                    ItemListener listener = boxs2[a].getItemListeners()[0];
                    boxs2[a].removeItemListener(listener);
                    boxs2[a].setSelectedItem(String.valueOf(sliders[a].getValue()));
                    boxs2[a].addItemListener(listener);
                }
            });
            sliders[i].addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    int cc = sliders[a].getValue();
                    if (cc % 2 != 0) {
                        cc = cc + 1;
                        sliders[a].setValue(cc);
                    }
                    if (Data.serialPort != null) {
                        try {
                            OutputStream os = Data.serialPort.getOutputStream();
                            os.write(new DengJuKaiGuangItemListener().code());
                            os.flush();
                            os.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            });
            boxs2[i] = new JComboBox();
            boxs2[i].setPreferredSize(new Dimension(90, 24));
            boxs2[i].setBorder(BorderFactory.createEmptyBorder(-2, 0, -2, 0));
            boxs2[i].addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (ItemEvent.SELECTED == e.getStateChange()) {
                        sliders[a].setValue(Integer.valueOf(boxs2[a].getSelectedItem().toString()));
                        if (Data.serialPort != null) {
                            try {
                                OutputStream os = Data.serialPort.getOutputStream();
                                os.write(new DengJuKaiGuangItemListener().code());
                                os.flush();
                                os.close();
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }
            });
            for (int j = 0; j <= 100; j++) {
                if (j % 2 == 0) {
                    boxs2[i].addItem(String.valueOf(j));
                }
            }
        }
        for (int i = 0; i < panels.length; i++) {
            panels[i] = new JPanel();
            panels[i].setPreferredSize(new Dimension(95, 160));
            panels[i].setLayout(flowLayout2);
            panels[i].add(labels[i]);
            panels[i].add(boxs1[i]);
            panels[i].add(sliders[i]);
            panels[i].add(boxs2[i]);
            p10.add(panels[i]);
        }

        JPanel panel3 = new JPanel();
        T3(panel3);
        TitledBorder tb3 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "����", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        panel3.setBorder(tb3);
        panel3.setPreferredSize(new Dimension(560, 75));

        JPanel panel6 = new JPanel();
        T6(panel6);
        TitledBorder tb6 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "�������ģʽ", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        panel6.setBorder(tb6);
        panel6.setPreferredSize(new Dimension(560, 75));

        JPanel panel2 = new JPanel();
        T2(panel2);
        TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "ȫ�ֿ���", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        panel2.setBorder(tb2);
        panel2.setPreferredSize(new Dimension(1010, 70));

        JPanel panel4 = new JPanel();
        T4(panel4);
        TitledBorder tb4 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Ч����ģʽ����", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        panel4.setBorder(tb4);
        panel4.setPreferredSize(new Dimension(1010, 80));

        JPanel panel5 = new JPanel();
        T5(panel5);
        TitledBorder tb5 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "ҡ������", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        panel5.setBorder(tb5);
        panel5.setPreferredSize(new Dimension(1010, 80));

        JPanel jPanel = new JPanel();
        jPanel.setPreferredSize(new Dimension(1010, 165));
        jPanel.setLayout(flowLayout5);
        jPanel.add(p10);
        JPanel jPanel1 = new JPanel();
        jPanel1.setPreferredSize(new Dimension(570, 165));
        jPanel1.add(panel3);
        jPanel1.add(panel6);
        jPanel.add(jPanel1);

        pane.add(changJing);
        pane.add(p9);
        pane.add(jPanel);
        pane.add(panel2);
        pane.add(panel4);
        pane.add(panel5);
    }

    private void T2(JPanel pane) {
        pane.setLayout(new BorderLayout());
        JPanel chdPane = new JPanel();
        chdPane.setBorder(BorderFactory.createEmptyBorder(-5, 0, 0, 0));
        chdPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        pane.add(chdPane);

        JLabel label = new JLabel("ȫ������:");
        chdPane.add(label);
        final JSlider slider = new JSlider(0, 100, 0);
        MainUi.map.put("quanJuLiangDuSlider", slider);

        chdPane.add(slider);
        //slider.setOrientation(SwingConstants.HORIZONTAL);
        slider.setPreferredSize(new Dimension(99, 24));
        final JComboBox box = new JComboBox();
        MainUi.map.put("quanJuLiangDuBox", box);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (slider.getValue() % 2 != 0) {
                    slider.setValue(slider.getValue() + 1);
                }
                ItemListener listener = box.getItemListeners()[0];
                box.removeItemListener(listener);
                box.setSelectedItem(String.valueOf(slider.getValue()));
                box.addItemListener(listener);
            }
        });
        slider.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                int cc = slider.getValue();
                if (cc % 2 != 0) {
                    cc = cc + 1;
                    slider.setValue(cc);
                }
                if (Data.serialPort != null) {
                    try {
                        OutputStream os = Data.serialPort.getOutputStream();
                        os.write(new DengJuKaiGuangItemListener().code());
                        os.flush();
                        os.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
        box.setPreferredSize(new Dimension(60, 28));
        box.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    slider.setValue(Integer.valueOf(box.getSelectedItem().toString()));
                    if (Data.serialPort != null) {
                        try {
                            OutputStream os = Data.serialPort.getOutputStream();
                            os.write(new DengJuKaiGuangItemListener().code());
                            os.flush();
                            os.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        });
        for (int i = 0; i <= 100; i++) {
            if (i % 2 == 0) {
                box.addItem(String.valueOf(i));
            }
        }
        box.setSelectedIndex(25);
        chdPane.add(box);
        RadioActionListener radioActionListener = new RadioActionListener();
        JLabel label2 = new JLabel("����ģʽ:");
        chdPane.add(label2);
        JRadioButton radioButton = new JRadioButton("�̶�");
        JRadioButton radioButton2 = new JRadioButton("����");
        radioButton.setSelected(true);
        radioButton.addActionListener(radioActionListener);
        radioButton2.addActionListener(radioActionListener);
        MainUi.map.put("kaiGuangModelBtn1", radioButton);
        MainUi.map.put("kaiGuangModelBtn2", radioButton2);
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton);
        group.add(radioButton2);
        chdPane.add(radioButton);
        chdPane.add(radioButton2);
        JLabel label3 = new JLabel("����ģʽ:");
        chdPane.add(label3);
        JRadioButton radioButton3 = new JRadioButton("�̶�");
        JRadioButton radioButton4 = new JRadioButton("�̳�");
        radioButton3.setSelected(true);
        radioButton3.addActionListener(radioActionListener);
        radioButton4.addActionListener(radioActionListener);
        MainUi.map.put("liangDuModelBtn1", radioButton3);
        MainUi.map.put("liangDuModelBtn2", radioButton4);
        ButtonGroup group2 = new ButtonGroup();
        group2.add(radioButton3);
        group2.add(radioButton4);
        chdPane.add(radioButton3);
        chdPane.add(radioButton4);
    }

    /**
     * ҡ���������
     *
     * @param pane
     */
    private void T5(JPanel pane) {
        pane.setLayout(new BorderLayout());
        JPanel chdPane = new JPanel();
        chdPane.setBorder(BorderFactory.createEmptyBorder(-5, 0, 0, 0));
        chdPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        pane.add(chdPane);

        chdPane.add(new JLabel("ҡ��ģʽ"));
        JComboBox box = new JComboBox();
        box.addItemListener(itemListener8);
        MainUi.map.put("yaoMaiModelBox", box);
        box.addItem("��");
        box.addItem("ҡ��1");
        box.addItem("ҡ��2");
        box.addItem("����");
        chdPane.add(box);
        chdPane.add(new JLabel("         ҡ�󴥷����(��)"));
        JComboBox box2 = new JComboBox();
        box2.addItemListener(itemListener8);
        MainUi.map.put("yaoMaiJianGeBox", box2);
        for (int i = 0; i <= 60; i++) {
            box2.addItem(String.valueOf(i));
        }
        chdPane.add(box2);
        chdPane.add(new JLabel("         ҡ����������"));
        JRadioButton radioButton = new JRadioButton("��");
        JRadioButton radioButton2 = new JRadioButton("��");
        radioButton2.setSelected(true);
        radioButton.addActionListener(radioActionListener2);
        radioButton2.addActionListener(radioActionListener2);
        MainUi.map.put("yaoMaiKaiGuangBtn1", radioButton);
        MainUi.map.put("yaoMaiKaiGuangBtn2", radioButton2);
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton);
        group.add(radioButton2);
        chdPane.add(radioButton);
        chdPane.add(radioButton2);
    }

    /**
     * Ч�����������
     *
     * @param pane
     */
    private void T4(JPanel pane) {
        pane.setLayout(new BorderLayout());
        JPanel chdPane = new JPanel();
        chdPane.setBorder(BorderFactory.createEmptyBorder(-5, 0, 0, 0));
        chdPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        pane.add(chdPane);

        JLabel label = new JLabel("Ч���ƿ���");
        chdPane.add(label);
        JComboBox box = new JComboBox();
        box.addItemListener(itemListener8);
        MainUi.map.put("xiaoGuoDengKaiGuangBox", box);
        box.addItem("��");
        box.addItem("��");
        box.addItem("����");
        chdPane.add(box);
        JLabel label2 = new JLabel("����ģʽ");
        chdPane.add(label2);
        JComboBox[] boxs1 = new JComboBox[3];
        MainUi.map.put("changJingModelBoxs", boxs1);
        for (int i = 0; i < boxs1.length; i++) {
            boxs1[i] = new JComboBox();
            boxs1[i].addItem("��");
            for (int j = 1; j < 25; j++) {
                boxs1[i].addItem(String.valueOf(j));
            }
            boxs1[i].addItemListener(itemListener8);
            chdPane.add(boxs1[i]);
        }
//        JPanel nullPane = new JPanel();
//        nullPane.setPreferredSize(new Dimension(140,10));
//        nullPane.setBorder(BorderFactory.createEmptyBorder(-5,0,-5,0));
//        chdPane.add(nullPane);
//        JPanel nullPane2 = new JPanel();
//        nullPane2.setPreferredSize(new Dimension(148,20));
//        chdPane.add(nullPane2);
        JLabel label3 = new JLabel("����ģʽ");
        chdPane.add(label3);

        JComboBox[] boxs2 = new JComboBox[3];
        MainUi.map.put("shengKonModelBoxs", boxs2);
        for (int i = 0; i < boxs2.length; i++) {
            boxs2[i] = new JComboBox();
            boxs2[i].addItem("��");
            for (int j = 1; j < 17; j++) {
                boxs2[i].addItem(String.valueOf(j));
            }
            boxs2[i].addItemListener(itemListener8);
            chdPane.add(boxs2[i]);
        }
        JPanel endPane = new JPanel();
        endPane.setBorder(BorderFactory.createEmptyBorder(-9, -6, 0, -4));
        endPane.setPreferredSize(new Dimension(164, 44));
        endPane.add(new JLabel("����ģʽ"));
        JRadioButton radioButton = new JRadioButton("˳��");
        JRadioButton radioButton2 = new JRadioButton("���");
        radioButton.setSelected(true);
        radioButton.addActionListener(radioActionListener2);
        radioButton2.addActionListener(radioActionListener2);
        MainUi.map.put("yunXingModelBtn1", radioButton);
        MainUi.map.put("yunXingModelBtn2", radioButton2);
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton);
        group.add(radioButton2);
        endPane.add(radioButton);
        endPane.add(radioButton2);
        chdPane.add(endPane);
    }

    private void T6(JPanel pane) {
        pane.setLayout(new BorderLayout());
        JPanel chdPane = new JPanel();
        chdPane.setBorder(BorderFactory.createEmptyBorder(-5, 0, 0, 0));
        chdPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        pane.add(chdPane);

        chdPane.add(new JLabel("���ģʽ"));
        JComboBox box = new JComboBox();
        box.addItemListener(itemListener8);
        MainUi.map.put("wuJiModelBox", box);
        box.setPreferredSize(new Dimension(100, 40));
        box.addItem("��");
        box.addItem("����");
        for (int i = 1; i < 5; i++) {
            box.addItem(String.valueOf(i));
        }
        chdPane.add(box);
    }

    /**
     * �������
     *
     * @param pane
     */
    private void T3(JPanel pane) {
        pane.setLayout(new BorderLayout());
        JPanel chdPane = new JPanel();
        chdPane.setBorder(BorderFactory.createEmptyBorder(-5, 0, 0, 0));
        chdPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        pane.add(chdPane);

        JLabel label = new JLabel("Դ������");
        chdPane.add(label);
        JComboBox box = new JComboBox();
        MainUi.map.put("copyBox", box);
        box.setPreferredSize(new Dimension(100, 40));
        for (int i = 0; i < 37; i++) {
            box.addItem(String.valueOf(i));
        }
        chdPane.add(box);
        chdPane.add(new JLabel("  "));
        JButton button = new JButton("���Ƶ���ǰ");
        MainUi.map.put("copyBtn", button);
        button.addActionListener(new CopyToCurrent());
        chdPane.add(button);
    }
}
