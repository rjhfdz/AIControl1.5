package com.boray.kongTiao.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

import com.boray.Data.Data;
import com.boray.Data.MyData;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.Socket;
import com.boray.kongTiao.Listener.AllComBoxItemListener;
import com.boray.kongTiao.Listener.RadioActionListener;
import com.boray.mainUi.MainUi;

public class KongTiaoUI implements ActionListener {
    private ItemListener listener;
    private ActionListener actionListener;

    public void show(JPanel pane) {
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();
        p1.setPreferredSize(new Dimension(220, 350));
        p2.setPreferredSize(new Dimension(440, 350));
        p3.setPreferredSize(new Dimension(330, 350));
        p4.setPreferredSize(new Dimension(1000, 80));
		/*p1.setBorder(new LineBorder(Color.black));
		p2.setBorder(new LineBorder(Color.black));
		p3.setBorder(new LineBorder(Color.black));*/
        listener = new AllComBoxItemListener();
        actionListener = new RadioActionListener();
        setP1(p1);
        setP2(p2);
        setP3(p3);
        setP4(p4);
        pane.add(p1);
        pane.add(p2);
        pane.add(p3);//pane.add(p4);
    }

    void setP1(JPanel allPane) {
        JPanel pane = new JPanel();
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "空调默认开机参数", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        pane.setBorder(tb);
        pane.setPreferredSize(new Dimension(220, 340));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(10);
        pane.setLayout(flowLayout);
        pane.add(new JLabel("默认开机"));
        JRadioButton radioButton = new JRadioButton("启用");
        JRadioButton radioButton2 = new JRadioButton("不启用");
        radioButton.setSelected(true);
        radioButton.addActionListener(actionListener);
        radioButton2.addActionListener(actionListener);
        MainUi.map.put("kongTiaoKaiJiBtn1", radioButton);
        MainUi.map.put("kongTiaoKaiJiBtn2", radioButton2);
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton);
        group.add(radioButton2);
        pane.add(radioButton);
        pane.add(radioButton2);
        pane.add(new JLabel("空调模式"));
        JComboBox box = new JComboBox(new String[]{"制冷", "制热", "送风"});
        box.addItemListener(listener);
        box.setName("空调冷热模式");
        MainUi.map.put("kongTiaoModelBox", box);
        box.setPreferredSize(new Dimension(120, 32));
        pane.add(box);
        pane.add(new JLabel("空调档位"));
        JComboBox box1 = new JComboBox(new String[]{"1档", "2档", "3档", "自动", "关机"});
        box1.addItemListener(listener);
        box1.setName("空调档位");
        MainUi.map.put("kongTiaoDangWeiBox", box1);
        box1.setPreferredSize(new Dimension(120, 32));
        pane.add(box1);
        pane.add(new JLabel("设定温度"));
        JComboBox box2 = new JComboBox();
        box2.setName("设定温度");
        MainUi.map.put("kongTiaoSetWenDuBox", box2);
        box2.setPreferredSize(new Dimension(120, 32));
        JComboBox box3 = new JComboBox();
        box3.setName("当前温度");
        MainUi.map.put("kongTiaoNowWenDuBox", box3);
        box3.setPreferredSize(new Dimension(120, 32));
        for (int i = 18; i < 37; i++) {
            box2.addItem(String.valueOf(i));
            box3.addItem(String.valueOf(i));
        }
        box2.addItemListener(listener);
        box3.addItemListener(listener);
        pane.add(box2);
        pane.add(new JLabel("当前温度"));
        pane.add(box3);
        pane.add(new JLabel("排风开关"));
        JComboBox box4 = new JComboBox(new String[]{"开", "关"});
        box4.addItemListener(listener);
        box4.setName("排风状态");
        MainUi.map.put("kongTiaoPaiFengBox", box4);
        box4.setPreferredSize(new Dimension(120, 32));
        pane.add(box4);
        allPane.add(pane);
    }

    void setP2(JPanel allPane) {
        JPanel p1 = new JPanel();
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "空调类型", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p1.setBorder(tb);
        p1.setPreferredSize(new Dimension(440, 254));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(18);
        p1.setLayout(flowLayout);
        p1.add(new JLabel("               风机类型:"));
        JComboBox box = new JComboBox(new String[]{"单线阀", "双线阀", "双水管"});
        box.addItemListener(listener);
        box.setName("线阀模式");
        MainUi.map.put("kongTiaoFengJiTypeBox", box);
        box.setPreferredSize(new Dimension(180, 32));
        p1.add(box);
        p1.add(new JLabel("           "));

        p1.add(new JLabel("         墙板温度显示:"));
        JRadioButton radioButton = new JRadioButton("取墙板");
        JRadioButton radioButton2 = new JRadioButton("取空调");
        radioButton.setSelected(true);
        radioButton.addActionListener(actionListener);
        radioButton2.addActionListener(actionListener);
        MainUi.map.put("kongTiaoShowWenDuBtn1", radioButton);
        MainUi.map.put("kongTiaoShowWenDuBtn2", radioButton2);
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton);
        group.add(radioButton2);
        p1.add(radioButton);
        p1.add(radioButton2);
        p1.add(new JLabel("                            "));

        p1.add(new JLabel("         中央空调模式:"));
        JComboBox box2 = new JComboBox(MyData.getAirModel());
        box2.addItemListener(listener);
        box2.setName("空调模式");
        MainUi.map.put("kongTiaoCenterModelBox", box2);
        box2.setPreferredSize(new Dimension(180, 32));
        p1.add(box2);
        p1.add(new JLabel("                 "));

        p1.add(new JLabel("      空调RS485地址:"));
        JComboBox box3 = new JComboBox(MyData.getAdd());
        box3.setName("空调RS485地址");
        box3.addItemListener(listener);
        MainUi.map.put("kongTiaoRS485AddBox", box3);
        box3.setPreferredSize(new Dimension(180, 32));
        p1.add(box3);

        JPanel p2 = new JPanel();
        TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "出厂设置", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p2.setBorder(tb2);
        p2.setPreferredSize(new Dimension(440, 80));
        JButton button = new JButton("恢复出厂设置");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Data.serialPort != null) {
                    try {
                        OutputStream os = Data.serialPort.getOutputStream();
                        os.write(ZhiLingJi.setInitToBebin());
                        os.flush();
                        os.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } else if (Data.socket != null) {
                    Socket.UDPSendData(ZhiLingJi.setInitToBebin());
                }
            }
        });
        button.setPreferredSize(new Dimension(180, 38));
        p2.add(button);

        allPane.add(p1);
        allPane.add(p2);
    }

    void setP3(JPanel allPane) {
        List list = new ArrayList();
        JPanel pane = new JPanel();
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "空调控制", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        pane.setBorder(tb);
        pane.setPreferredSize(new Dimension(330, 254));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(-8);
        pane.setLayout(flowLayout);

        JPanel p1 = new JPanel();
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
        flowLayout2.setVgap(-2);
        flowLayout2.setHgap(24);
        p1.setLayout(flowLayout2);
        p1.setPreferredSize(new Dimension(310, 60));
        TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "运行模式", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p1.setBorder(tb1);
        JRadioButton radioButton = new JRadioButton("制冷");
        JRadioButton radioButton2 = new JRadioButton("制热");
        JRadioButton radioButton3 = new JRadioButton("送风");
        radioButton.addActionListener(this);
        radioButton2.addActionListener(this);
        radioButton3.addActionListener(this);
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton);
        group.add(radioButton2);
        group.add(radioButton3);
        p1.add(radioButton);
        p1.add(radioButton2);
        p1.add(radioButton3);
        JPanel p2 = new JPanel();
        FlowLayout flowLayout3 = new FlowLayout(FlowLayout.CENTER);
        flowLayout3.setVgap(26);
        p2.setLayout(flowLayout3);
        p2.setPreferredSize(new Dimension(310, 170));
        TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "空调1", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p2.setBorder(tb2);
        JRadioButton radioButton4 = new JRadioButton("1档");
        JRadioButton radioButton5 = new JRadioButton("2档");
        JRadioButton radioButton6 = new JRadioButton("3档");
        JRadioButton radioButton7 = new JRadioButton("自动");
        JRadioButton radioButton8 = new JRadioButton("关机");
        radioButton4.addActionListener(this);
        radioButton5.addActionListener(this);
        radioButton6.addActionListener(this);
        radioButton7.addActionListener(this);
        radioButton8.addActionListener(this);
        ButtonGroup group2 = new ButtonGroup();
        group2.add(radioButton4);
        group2.add(radioButton5);
        group2.add(radioButton6);
        group2.add(radioButton7);
        group2.add(radioButton8);
        p2.add(radioButton4);
        p2.add(radioButton5);
        p2.add(radioButton6);
        p2.add(radioButton7);
        p2.add(radioButton8);
        p2.add(new JLabel("设定温度"));
        final JComboBox box = new JComboBox();
        box.setPreferredSize(new Dimension(100, 32));
        for (int i = 12; i < 37; i++) {
            box.addItem("" + i);
        }
        box.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    if (Data.serialPort != null) {
                        try {
                            OutputStream os = Data.serialPort.getOutputStream();
                            os.write(ZhiLingJi.setKongTiaoCtrl(5, Integer.valueOf(box.getSelectedItem().toString()).intValue()));
                            os.flush();
                            os.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    } else if (Data.socket != null) {
                        Socket.UDPSendData(ZhiLingJi.setKongTiaoCtrl(5, Integer.valueOf(box.getSelectedItem().toString()).intValue()));
                    } else {
                        new Thread(new Runnable() {
                            public void run() {
                                JFrame frame = (JFrame) MainUi.map.get("frame");
                                JOptionPane.showMessageDialog(frame, "还未连接设备！", "提示", JOptionPane.ERROR_MESSAGE);
                            }
                        }).start();
                    }
                }
            }
        });
        p2.add(box);
        p2.add(new JLabel("当前温度"));
        JLabel label = new JLabel("12");
        p2.add(label);


        pane.add(p1);
        JPanel N1 = new JPanel();
        N1.setPreferredSize(new Dimension(300, 14));
        pane.add(N1);
        pane.add(p2);


        JPanel pane2 = new JPanel();
        TitledBorder tb3 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "排风控制", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        pane2.setBorder(tb3);
        pane2.setPreferredSize(new Dimension(330, 80));
        JRadioButton radioButton9 = new JRadioButton("开");
        JRadioButton radioButton10 = new JRadioButton("关");
        radioButton9.addActionListener(this);
        radioButton10.addActionListener(this);
        ButtonGroup group3 = new ButtonGroup();
        group3.add(radioButton9);
        group3.add(radioButton10);
        pane2.add(radioButton9);
        pane2.add(radioButton10);

        list.add(radioButton);
        list.add(radioButton2);
        list.add(radioButton3);
        list.add(radioButton4);
        list.add(radioButton5);
        list.add(radioButton6);
        list.add(radioButton7);
        list.add(radioButton8);
        list.add(radioButton9);
        list.add(radioButton10);
        list.add(box);
        list.add(label);
        MainUi.map.put("ctrlAirCompone", list);

        allPane.add(pane);
        allPane.add(pane2);
    }

    void setP4(JPanel pane) {
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "刷新", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        pane.setBorder(tb);
        JButton button = new JButton("读取参数");
        button.setPreferredSize(new Dimension(180, 38));
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Data.serialPort != null) {
                    try {
                        OutputStream os = Data.serialPort.getOutputStream();
                        os.write(ZhiLingJi.queryKongTiao());
                        os.flush();
                        os.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } else if (Data.socket != null) {
                    Socket.UDPSendData(ZhiLingJi.queryKongTiao());
                }
            }
        });
        pane.add(button);
    }

    public void actionPerformed(ActionEvent e) {
        JRadioButton btn = (JRadioButton) e.getSource();
        String s = btn.getText();
        byte[] b = null;
        switch (s) {
            case "制冷":
                b = ZhiLingJi.setKongTiaoCtrl(2, 106);
                break;
            case "制热":
                b = ZhiLingJi.setKongTiaoCtrl(2, 107);
                break;
            case "送风":
                b = ZhiLingJi.setKongTiaoCtrl(2, 108);
                break;
            case "1档":
                b = ZhiLingJi.setKongTiaoCtrl(3, 138);
                break;
            case "2档":
                b = ZhiLingJi.setKongTiaoCtrl(3, 139);
                break;
            case "3档":
                b = ZhiLingJi.setKongTiaoCtrl(3, 140);
                break;
            case "自动":
                b = ZhiLingJi.setKongTiaoCtrl(3, 141);
                break;
            case "关机":
                b = ZhiLingJi.setKongTiaoCtrl(3, 142);
                break;
            case "开":
                b = ZhiLingJi.setKongTiaoCtrl(6, 80);
                break;
            case "关":
                b = ZhiLingJi.setKongTiaoCtrl(6, 81);
                break;
            default:
                break;
        }
        if (Data.serialPort != null) {
            try {
                OutputStream os = Data.serialPort.getOutputStream();
                os.write(b);
                os.flush();
                os.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else if (Data.socket != null) {
            Socket.UDPSendData(b);
        } else {
            JFrame frame = (JFrame) MainUi.map.get("frame");
            JOptionPane.showMessageDialog(frame, "还未连接设备！", "提示", JOptionPane.ERROR_MESSAGE);
        }
    }
}
