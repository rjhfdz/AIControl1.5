package com.boray.zhongKon.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.boray.Data.Data;
import com.boray.Data.MyData;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.Socket;
import com.boray.mainUi.MainUi;
import com.boray.zhongKon.Data.DataOfZhongKon;
import com.boray.zhongKon.Listener.SettingActionListener;
import com.boray.zhongKon.Listener.ShowAndSaveCode;
import com.boray.zhongKon.Listener.WriteActionListener;

public class ZhongKonUI {
    public void show(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        pane.setLayout(flowLayout);
        JPanel p = new JPanel();
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "编码", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p.setBorder(tb);
        p.setPreferredSize(new Dimension(968, 366));

        JPanel p1 = new JPanel();
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
        flowLayout2.setHgap(0);
        flowLayout2.setVgap(5);
        p1.setLayout(flowLayout2);
        //p1.setBorder(new LineBorder(Color.gray));
        p1.setPreferredSize(new Dimension(520, 330));
        p1.add(new JLabel("组号:           "));
        final JComboBox box = new JComboBox(MyData.getGroup());
        box.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    ShowAndSaveCode showAndSaveCode = new ShowAndSaveCode();
                    showAndSaveCode.save(DataOfZhongKon.selectedNum);
                    DataOfZhongKon.selectedNum = box.getSelectedIndex();
                    showAndSaveCode.show(DataOfZhongKon.selectedNum);
                    if (Data.serialPort != null) {
                        try {
                            OutputStream os = Data.serialPort.getOutputStream();
                            os.write(ZhiLingJi.queryZhongKon(box.getSelectedIndex()));
                            os.flush();
                            os.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    } else if (Data.socket != null) {
                        Socket.UDPSendData(ZhiLingJi.queryZhongKon(box.getSelectedIndex()));
                    }
                }
            }
        });
        MainUi.map.put("zhongKonGroupBox", box);
        p1.add(box);
        JButton button = new JButton("测试");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Data.serialPort != null) {
                    try {
                        OutputStream os = Data.serialPort.getOutputStream();
                        os.write(ZhiLingJi.testCode(box.getSelectedIndex()));
                        os.flush();
                        os.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } else if (Data.socket != null) {
                    Socket.UDPSendData(ZhiLingJi.testCode(box.getSelectedIndex()));
                }
            }
        });
        p1.add(button);
        p1.add(new JLabel("                            "));
        //p1.setBorder(new LineBorder(Color.gray));
        p1.add(new JLabel("红外码:        "));
        JTextField[] fields = new JTextField[12];
        MainUi.map.put("redCodeFields", fields);
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new JTextField(2);
            if (i > 3) {
                fields[i].setEnabled(false);
            }
            p1.add(fields[i]);
        }

        p1.add(new JLabel("串口墙板1:   "));
        JTextField[] fields2 = new JTextField[12];
        MainUi.map.put("comQianBanFields1", fields2);
        for (int i = 0; i < fields2.length; i++) {
            fields2[i] = new JTextField(2);
            if (i > 4) {
                fields2[i].setEnabled(false);
            }
            p1.add(fields2[i]);
        }

        p1.add(new JLabel("串口墙板2:   "));
        JTextField[] fields3 = new JTextField[12];
        MainUi.map.put("comQianBanFields2", fields3);
        for (int i = 0; i < fields3.length; i++) {
            fields3[i] = new JTextField(2);
            if (i > 4) {
                fields3[i].setEnabled(false);
            }
            p1.add(fields3[i]);
        }

        p1.add(new JLabel("串口墙板3:   "));
        JTextField[] fields4 = new JTextField[12];
        MainUi.map.put("comQianBanFields3", fields4);
        for (int i = 0; i < fields4.length; i++) {
            fields4[i] = new JTextField(2);
            if (i > 4) {
                fields4[i].setEnabled(false);
            }
            p1.add(fields4[i]);
        }

        p1.add(new JLabel("串口墙板4:   "));
        JTextField[] fields5 = new JTextField[12];
        MainUi.map.put("comQianBanFields4", fields5);
        for (int i = 0; i < fields5.length; i++) {
            fields5[i] = new JTextField(2);
            if (i > 4) {
                fields5[i].setEnabled(false);
            }
            p1.add(fields5[i]);
        }

        p1.add(new JLabel("串口上行:     "));
        JTextField[] fields6 = new JTextField[12];
        MainUi.map.put("comShangXingFields", fields6);
        for (int i = 0; i < fields6.length; i++) {
            fields6[i] = new JTextField(2);
            p1.add(fields6[i]);
        }

        p1.add(new JLabel("串口下行:     "));
        JTextField[] fields7 = new JTextField[12];
        MainUi.map.put("comXiaXingFields", fields7);
        for (int i = 0; i < fields7.length; i++) {
            fields7[i] = new JTextField(2);
            p1.add(fields7[i]);
        }

        p1.add(new JLabel("                           喝彩:"));
        final JComboBox box2 = new JComboBox(new String[]{"关闭", "打开"});
        MainUi.map.put("heCaiBox", box2);
        box2.setName("hc");
        p1.add(box2);
        p1.add(new JLabel("         倒彩"));
        final JComboBox box3 = new JComboBox(new String[]{"关闭", "打开"});
        MainUi.map.put("daoCaiBox", box3);
        box3.setName("dc");
        p1.add(box3);

        ItemListener listener = new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    JComboBox b = (JComboBox) e.getSource();
                    ItemListener listener6 = null;
                    if (e.getItem().toString().equals("打开")) {
                        if (b.getName().equals("hc")) {
                            listener6 = box3.getItemListeners()[0];
                            box3.removeItemListener(listener6);
                            box3.setSelectedItem("关闭");
                            box3.addItemListener(listener6);
                        } else {
                            listener6 = box2.getItemListeners()[0];
                            box2.removeItemListener(listener6);
                            box2.setSelectedItem("关闭");
                            box2.addItemListener(listener6);
                        }
                    }
                    if (Data.serialPort != null) {
                        try {
                            OutputStream os = Data.serialPort.getOutputStream();
                            os.write(ZhiLingJi.setHcOrDc(box.getSelectedIndex(),
                                    box2.getSelectedIndex(), box3.getSelectedIndex()));
                            os.flush();
                            os.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    } else if (Data.socket != null) {
                        Socket.UDPSendData(ZhiLingJi.setHcOrDc(box.getSelectedIndex(),
                                box2.getSelectedIndex(), box3.getSelectedIndex()));
                    }
                }
            }
        };
        box2.addItemListener(listener);
        box3.addItemListener(listener);

        JPanel p2 = new JPanel();
        FlowLayout flowLayout3 = new FlowLayout(FlowLayout.CENTER);
        flowLayout3.setVgap(7);
        p2.setLayout(flowLayout3);
        p2.setPreferredSize(new Dimension(300, 330));
        //p2.setBorder(new LineBorder(Color.gray));
        JPanel N1 = new JPanel();
        N1.setPreferredSize(new Dimension(280, 24));
        p2.add(N1);
        JButton[] buttons = new JButton[20];
        WriteActionListener listener2 = new WriteActionListener();
        for (int i = 0; i < buttons.length; i++) {
            String s = "";
            boolean cc = true;
            if (i % 4 == 0) {
                s = "学习";
            } else if (i % 4 == 1) {
                s = "退出学习";
            } else if (i % 4 == 2) {
                s = "添加";
                cc = true;
            } else if (i % 4 == 3) {
                s = "清除";
                cc = false;
            }
            buttons[i] = new JButton(s);
            buttons[i].setName("" + i);
            buttons[i].setMargin(new Insets(0, -10, 0, -10));
            buttons[i].setPreferredSize(new Dimension(68, 30));
            buttons[i].addActionListener(listener2);
			/*if (i > 7) {
				buttons[i].setEnabled(false);
			}*/
            if (i == 2) {
                buttons[i].setVisible(false);
            }
            if (i == 6 || i == 10 || i == 14 || i == 18) {
                buttons[i].setText("清除");
                buttons[i].setName("" + (i + 1));
            }
            if (i == 7 || i == 11 || i == 15 || i == 19) {
                buttons[i].setText("添加");
                buttons[i].setName("" + (i - 1));
                buttons[i].setVisible(false);
            }
            p2.add(buttons[i]);
            if (i == 3 || i == 6 || i == 10 || i == 14 || i == 18) {
                p2.add(new JLabel("               "));
            }
        }
        JPanel n1 = new JPanel();
        n1.setPreferredSize(new Dimension(270, 108));
        //n1.setBorder(new LineBorder(Color.gray));
        //p2.add(n1);
        //p2.setBorder(new LineBorder(Color.gray));
        JButton[] buttons2 = new JButton[4];
        for (int i = 0; i < buttons2.length; i++) {
            String s = "", s1 = "";
            s1 = "               " +
                    "              " +
                    "              " +
                    "        ";
            if (i % 2 == 0) {
                s = "写入";
                //s1 = "               ";
            } else if (i % 2 == 1) {
                s = "清除";
                //s1 = "";
            }
            buttons2[i] = new JButton(s);
            buttons2[i].setName("sx" + i);
            buttons2[i].addActionListener(listener2);
            buttons2[i].setMargin(new Insets(0, -10, 0, -10));
            buttons2[i].setPreferredSize(new Dimension(68, 30));
            if (i % 2 == 0) {
                buttons2[i].setVisible(false);
            }
            p2.add(buttons2[i]);
            if (i == 1 || i == 3) {
                p2.add(new JLabel(s1));
            }
        }


        p.add(p1);
        p.add(p2);

        JPanel p3 = new JPanel();
        //p3.setBorder(new LineBorder(Color.gray));
        p3.setPreferredSize(new Dimension(68, 330));
        JButton btn = new JButton("加载");
        btn.setPreferredSize(new Dimension(68, 30));
        //btn.addActionListener(new LoadActionListener());
        p3.add(btn);
        //p.add(p3);

        List list = new ArrayList();
        SettingActionListener listener3 = new SettingActionListener();
        JPanel p_p = new JPanel();
        p_p.setLayout(new FlowLayout(FlowLayout.LEFT));
        TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "设置", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p_p.setBorder(tb1);
        p_p.setPreferredSize(new Dimension(968, 220));
        JPanel p_p_p1 = new JPanel();
        p_p_p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        p_p_p1.setPreferredSize(new Dimension(400, 130));
        //p_p_p1.setBorder(new LineBorder(Color.gray));
        p_p_p1.add(new JLabel("       服务灯发起呼叫:"));
        JRadioButton radioButton = new JRadioButton("仅复位");
        JRadioButton radioButton2 = new JRadioButton("复位/呼叫");
        radioButton.setSelected(true);
        radioButton.addActionListener(listener3);
        radioButton2.addActionListener(listener3);
        list.add(radioButton);
        list.add(radioButton2);
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton);
        group.add(radioButton2);
        p_p_p1.add(radioButton);
        p_p_p1.add(new JLabel("   "));
        p_p_p1.add(radioButton2);
        p_p_p1.add(new JLabel("            "));
        p_p_p1.add(new JLabel("       屏蔽灯光上行码:"));
        JRadioButton radioButton3 = new JRadioButton("屏蔽");
        JRadioButton radioButton4 = new JRadioButton("开启");
        radioButton3.setSelected(true);
        radioButton3.addActionListener(listener3);
        radioButton4.addActionListener(listener3);
        list.add(radioButton3);
        list.add(radioButton4);
        ButtonGroup group2 = new ButtonGroup();
        group2.add(radioButton3);
        group2.add(radioButton4);
        p_p_p1.add(radioButton3);
        p_p_p1.add(new JLabel("      "));
        p_p_p1.add(radioButton4);
        p_p_p1.add(new JLabel("            "));
        p_p_p1.add(new JLabel("       屏蔽空调上行码:"));
        JRadioButton radioButton5 = new JRadioButton("屏蔽");
        JRadioButton radioButton6 = new JRadioButton("开启");
        radioButton5.setSelected(true);
        radioButton5.addActionListener(listener3);
        radioButton6.addActionListener(listener3);
        list.add(radioButton5);
        list.add(radioButton6);
        ButtonGroup group3 = new ButtonGroup();
        group3.add(radioButton5);
        group3.add(radioButton6);
        p_p_p1.add(radioButton5);
        p_p_p1.add(new JLabel("      "));
        p_p_p1.add(radioButton6);
        p_p_p1.add(new JLabel("            "));
        p_p_p1.add(new JLabel("       升/降调红外码:  "));
        JRadioButton radioButton7 = new JRadioButton("单码");
        JRadioButton radioButton8 = new JRadioButton("多码");
        radioButton7.setSelected(true);
        radioButton7.addActionListener(listener3);
        radioButton8.addActionListener(listener3);
        list.add(radioButton7);
        list.add(radioButton8);
        ButtonGroup group4 = new ButtonGroup();
        group4.add(radioButton7);
        group4.add(radioButton8);
        JButton button2 = new JButton("多码设置");
        p_p_p1.add(radioButton7);
        p_p_p1.add(new JLabel("      "));
        p_p_p1.add(radioButton8);
        //p_p_p1.add(button2);

        JPanel p_p_p2 = new JPanel();
        //p_p_p2.setBorder(new LineBorder(Color.gray));
        p_p_p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        p_p_p2.setPreferredSize(new Dimension(480, 36));
        p_p_p2.add(new JLabel("       功放红外码频率:"));
        final JSlider slider = new JSlider(1, 16);
        list.add(slider);
        slider.setPreferredSize(new Dimension(140, 28));
        p_p_p2.add(slider);
        final JComboBox box4 = new JComboBox();
        list.add(box4);
        box4.setPreferredSize(new Dimension(78, 30));
        for (int i = 1; i < 17; i++) {
            box4.addItem(String.valueOf(i));
        }
        slider.setValue(1);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                ItemListener listener = box4.getItemListeners()[0];
                box4.removeItemListener(listener);
                box4.setSelectedItem(String.valueOf(slider.getValue()));
                box4.addItemListener(listener);
            }
        });
        slider.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                new SettingActionListener().sendCode();
            }
        });
        box4.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    slider.setValue(Integer.valueOf(box4.getSelectedItem().toString()));
                    new SettingActionListener().sendCode();
                }
            }
        });
        MainUi.map.put("settingZhongKongListCompone", list);

        p_p_p2.add(box4);
        p_p_p2.add(new JLabel("(出厂默认为01)"));

        JPanel N2 = new JPanel();
        //N2.setBorder(new LineBorder(Color.gray));
        N2.setPreferredSize(new Dimension(480, 80));
        p_p.add(p_p_p1);
        p_p.add(N2);
        p_p.add(p_p_p2);

        pane.add(p);
        pane.add(p_p);
    }
}
