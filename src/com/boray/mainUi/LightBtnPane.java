package com.boray.mainUi;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Listener.IpConnectAndOffListener;
import com.boray.Listener.SwitchListener;
import com.boray.Utils.JPressButton;
import com.boray.returnListener.ComReturnListener;
import com.boray.usb.Link;

public class LightBtnPane implements ActionListener {
    private JComboBox comCheckBox;
    private String hex = null;
    private JToggleButton button = null;
    private JToggleButton button2 = null;
    private JTextField field;

    public void show(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(0);
        flowLayout.setHgap(-2);
        pane.setLayout(flowLayout);
        JPressButton btn10 = new JPressButton();//"主窗口"
        JPressButton btn1 = new JPressButton();//"场景配置"
        JPressButton btn2 = new JPressButton();//"灯库配置"
        JPressButton btn2_2 = new JPressButton();//"效果素材"
        JPressButton btn3 = new JPressButton();//"效果编程"
        JPressButton btn11 = new JPressButton();//"声控素材"
        JPressButton btn4 = new JPressButton();//"声控编程"
        MainUi.map.put("xiaoGuoBtn", btn3);
        MainUi.map.put("shengKonBtn", btn4);
        JPressButton btn5 = new JPressButton();//"空调设置"
        JPressButton btn6 = new JPressButton();//"中控设置"
        JPressButton btn7 = new JPressButton();//"全局设置"
        JPressButton btn8 = new JPressButton();//"备份还原"
        MainUi.map.put("QuanJuToggleBtn", btn7);
        btn1.setName("1");
        btn2.setName("2");
        btn2_2.setName("9");
        btn3.setName("3");
        btn4.setName("4");
        btn5.setName("5");
        btn6.setName("6");
        btn7.setName("7");
        btn8.setName("8");
        btn10.setName("10");
        btn11.setName("11");
        btn1.setContentAreaFilled(false);
        btn2.setContentAreaFilled(false);
        btn2_2.setContentAreaFilled(false);
        btn3.setContentAreaFilled(false);
        btn4.setContentAreaFilled(false);
        btn5.setContentAreaFilled(false);
        btn6.setContentAreaFilled(false);
        btn7.setContentAreaFilled(false);
        btn8.setContentAreaFilled(false);
        btn10.setContentAreaFilled(false);
        btn11.setContentAreaFilled(false);
        SwitchListener listener = new SwitchListener();
        btn1.addActionListener(listener);

        btn2.addActionListener(listener);
        btn2_2.addActionListener(listener);
        btn3.addActionListener(listener);
        btn4.addActionListener(listener);
        btn5.addActionListener(listener);
        btn6.addActionListener(listener);
        btn7.addActionListener(listener);
        btn8.addActionListener(listener);
        btn10.addActionListener(listener);
        btn11.addActionListener(listener);
        btn1.setSelected(true);

        ImageIcon main = new ImageIcon(getClass().getResource("/icon/界面-5-主页图标.png"));
        ImageIcon mainSel = new ImageIcon(getClass().getResource("/icon/界面-5-主页图标-1.png"));
        ImageIcon changJin = new ImageIcon(getClass().getResource("/icon/界面-5-场景配置.png"));
        ImageIcon changJinSel = new ImageIcon(getClass().getResource("/icon/界面-5-场景配置-1.png"));
        ImageIcon dengKu = new ImageIcon(getClass().getResource("/icon/界面-5-灯库配置.png"));
        ImageIcon dengKuSel = new ImageIcon(getClass().getResource("/icon/界面-5-灯库配置-1.png"));
        ImageIcon xiaoGuoSuCai = new ImageIcon(getClass().getResource("/icon/界面-5-效果灯素材.png"));
        ImageIcon xiaoGuoSuCaiSel = new ImageIcon(getClass().getResource("/icon/界面-5-效果灯素材-1.png"));
        ImageIcon xiaoGuoDeng = new ImageIcon(getClass().getResource("/icon/界面-5-效果灯编程.png"));
        ImageIcon xiaoGuoDengSel = new ImageIcon(getClass().getResource("/icon/界面-5-效果灯编程-1.png"));
        ImageIcon shengKonSuCai = new ImageIcon(getClass().getResource("/icon/界面-5-声控素材.png"));
        ImageIcon shengKonSuCaiSel = new ImageIcon(getClass().getResource("/icon/界面-5-声控素材-1.png"));
        ImageIcon shengKon = new ImageIcon(getClass().getResource("/icon/界面-5-声控编程.png"));
        ImageIcon shengKonSel = new ImageIcon(getClass().getResource("/icon/界面-5-声控编程-1.png"));
        ImageIcon konTiao = new ImageIcon(getClass().getResource("/icon/界面-5-空调设置.png"));
        ImageIcon konTiaoSel = new ImageIcon(getClass().getResource("/icon/界面-5-空调设置-1.png"));
        ImageIcon zhongKon = new ImageIcon(getClass().getResource("/icon/界面-5-中控设置.png"));
        ImageIcon zhongKonSel = new ImageIcon(getClass().getResource("/icon/界面-5-中控设置-1.png"));
        ImageIcon quanJu = new ImageIcon(getClass().getResource("/icon/界面-5-全局设置.png"));
        ImageIcon quanJuSel = new ImageIcon(getClass().getResource("/icon/界面-5-全局设置-1.png"));
        ImageIcon beifen = new ImageIcon(getClass().getResource("/icon/界面-5-备份还原.png"));
        ImageIcon beifenSel = new ImageIcon(getClass().getResource("/icon/界面-5-备份还原-1.png"));

        btn1.setIcon(changJin);
        btn1.setSelectedIcon(changJinSel);
        btn2.setIcon(dengKu);
        btn2.setSelectedIcon(dengKuSel);
        btn2_2.setIcon(xiaoGuoSuCai);
        btn2_2.setSelectedIcon(xiaoGuoSuCaiSel);
        btn3.setIcon(xiaoGuoDeng);
        btn3.setSelectedIcon(xiaoGuoDengSel);
        btn4.setIcon(shengKon);
        btn4.setSelectedIcon(shengKonSel);
        btn5.setIcon(konTiao);
        btn5.setSelectedIcon(konTiaoSel);
        btn6.setIcon(zhongKon);
        btn6.setSelectedIcon(zhongKonSel);
        btn7.setIcon(quanJu);
        btn7.setSelectedIcon(quanJuSel);
        btn8.setIcon(beifen);
        btn8.setSelectedIcon(beifenSel);
        btn10.setIcon(main);
        btn10.setSelectedIcon(mainSel);
        btn11.setIcon(shengKonSuCai);
        btn11.setSelectedIcon(shengKonSuCaiSel);


        btn1.setForeground(Color.BLACK);
        btn2.setForeground(Color.WHITE);
        btn2_2.setForeground(Color.WHITE);
        btn3.setForeground(Color.WHITE);
        btn4.setForeground(Color.WHITE);
        btn5.setForeground(Color.WHITE);
        btn6.setForeground(Color.WHITE);
        btn7.setForeground(Color.WHITE);
        btn8.setForeground(Color.WHITE);
        btn10.setForeground(Color.WHITE);
        btn11.setForeground(Color.WHITE);


//        btn1.setBackground(new Color(161, 66, 218));
//        btn2.setBackground(new Color(155, 65, 215));
//        btn2_2.setBackground(new Color(152, 63, 217));
//        btn3.setBackground(new Color(147, 60, 216));
//        btn4.setBackground(new Color(138, 56, 216));
//        btn5.setBackground(new Color(120, 48, 210));
//        btn6.setBackground(new Color(128, 52, 212));
//        btn7.setBackground(new Color(126, 50, 211));
//        btn8.setBackground(new Color(118, 48, 209));
//        btn10.setBackground(new Color(163, 69, 219));
//        btn11.setBackground(new Color(143, 58, 214));

        int width = 70, height = 70;
        btn1.setPreferredSize(new Dimension(width, height));
        btn2.setPreferredSize(new Dimension(width, height));
        btn3.setPreferredSize(new Dimension(width, height));
        btn4.setPreferredSize(new Dimension(width, height));
        btn5.setPreferredSize(new Dimension(width, height));
        btn6.setPreferredSize(new Dimension(width, height));
        btn7.setPreferredSize(new Dimension(width, height));
        btn8.setPreferredSize(new Dimension(width, height));
        btn2_2.setPreferredSize(new Dimension(width, height));
        btn10.setPreferredSize(new Dimension(width, height));
        btn11.setPreferredSize(new Dimension(width, height));
        btn1.setMargin(new Insets(0, -10, 0, -10));
        btn2.setMargin(new Insets(-10, -10, -10, -10));
        btn2_2.setMargin(new Insets(-10, -10, -10, -10));
        btn3.setMargin(new Insets(0, -10, 0, -10));
        btn4.setMargin(new Insets(0, -10, 0, -10));
        btn5.setMargin(new Insets(0, -10, 0, -10));
        btn6.setMargin(new Insets(0, -10, 0, -10));
        btn7.setMargin(new Insets(0, -10, 0, -10));
        btn8.setMargin(new Insets(0, -10, 0, -10));
        btn10.setMargin(new Insets(0, -10, 0, -10));
        btn11.setMargin(new Insets(0, -10, 0, -10));
        btn1.setFocusable(false);
        btn2.setFocusable(false);
        btn3.setFocusable(false);
        btn4.setFocusable(false);
        btn5.setFocusable(false);
        btn6.setFocusable(false);
        btn7.setFocusable(false);
        btn8.setFocusable(false);
        btn2_2.setFocusable(false);
        btn10.setFocusPainted(false);
        btn11.setFocusPainted(false);

        btn1.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn1.setHorizontalTextPosition(SwingConstants.CENTER);
        btn2.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn2.setHorizontalTextPosition(SwingConstants.CENTER);
        btn3.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn3.setHorizontalTextPosition(SwingConstants.CENTER);
        btn4.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn4.setHorizontalTextPosition(SwingConstants.CENTER);
        btn5.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn5.setHorizontalTextPosition(SwingConstants.CENTER);
        btn6.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn6.setHorizontalTextPosition(SwingConstants.CENTER);
        btn7.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn7.setHorizontalTextPosition(SwingConstants.CENTER);
        btn8.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn8.setHorizontalTextPosition(SwingConstants.CENTER);

        btn10.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn10.setHorizontalTextPosition(SwingConstants.CENTER);

        btn11.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn11.setHorizontalTextPosition(SwingConstants.CENTER);

        btn2_2.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn2_2.setHorizontalTextPosition(SwingConstants.CENTER);

        ButtonGroup group = new ButtonGroup();
        group.add(btn10);
        group.add(btn1);
        group.add(btn2);
        group.add(btn2_2);
        group.add(btn3);
        group.add(btn11);
        group.add(btn4);
        group.add(btn5);
        group.add(btn6);
        group.add(btn7);
        group.add(btn8);

        JPanel jPanel = new JPanel();
        MainUi.map.put("MenuButtonPanel",jPanel);
//        jPanel.setBorder(new LineBorder(Color.gray));
        jPanel.setPreferredSize(new Dimension(1200, 80));

        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(30,20));
        p1.setOpaque(false);
        JPanel p2 = new JPanel();
        p2.setPreferredSize(new Dimension(30,20));
        p2.setOpaque(false);
        JPanel p3 = new JPanel();
        p3.setPreferredSize(new Dimension(30,20));
        p3.setOpaque(false);
        JPanel p4 = new JPanel();
        p4.setPreferredSize(new Dimension(30,20));
        p4.setOpaque(false);
        JPanel p5 = new JPanel();
        p5.setPreferredSize(new Dimension(30,20));
        p5.setOpaque(false);
        JPanel p6 = new JPanel();
        p6.setPreferredSize(new Dimension(30,20));
        p6.setOpaque(false);
        JPanel p7 = new JPanel();
        p7.setPreferredSize(new Dimension(30,20));
        p7.setOpaque(false);
        JPanel p8 = new JPanel();
        p8.setPreferredSize(new Dimension(30,20));
        p8.setOpaque(false);
        JPanel p9 = new JPanel();
        p9.setPreferredSize(new Dimension(30,20));
        p9.setOpaque(false);
        JPanel p10 = new JPanel();
        p10.setPreferredSize(new Dimension(30,20));
        p10.setOpaque(false);

//        jPanel.setBorder(new LineBorder(Color.gray));

        jPanel.add(btn10);
        jPanel.add(p1);
        jPanel.add(btn1);
        jPanel.add(p2);
        jPanel.add(btn2);
        jPanel.add(p3);
        jPanel.add(btn2_2);
        jPanel.add(p4);
        jPanel.add(btn3);
        jPanel.add(p5);
        jPanel.add(btn11);
        jPanel.add(p6);
        jPanel.add(btn4);
        jPanel.add(p7);
        jPanel.add(btn5);
        jPanel.add(p8);
        jPanel.add(btn6);
        jPanel.add(p9);
        jPanel.add(btn7);
        jPanel.add(p10);
        jPanel.add(btn8);

//		JScrollPane scrollPane = new JScrollPane();
//		scrollPane.setPreferredSize(new Dimension(550,80));
//		scrollPane.setBorder(BorderFactory.createMatteBorder(0,0,0,0,Color.gray));
//		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//		scrollPane.setViewportView(jPanel);

//		pane.add(scrollPane);
//		pane.add(btn10);
//		pane.add(btn1);
//		pane.add(btn2);pane.add(btn2_2);
//		pane.add(btn3);
//        pane.add(btn11);
//		pane.add(btn4);
//		pane.add(btn5);
//		pane.add(btn6);
//		pane.add(btn7);
//		pane.add(btn8);

        pane.add(jPanel);

//        JPanel panel = new JPanel();
//        TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "网络连接", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
//        panel.setBorder(tb2);
//        final JTextField IpAddress = new JTextField(15);
//        MainUi.map.put("IpAddress", IpAddress);
//        panel.add(new Label("IP地址:"));
//        panel.add(IpAddress);
//        final JToggleButton IpConnect = new JToggleButton("连接");
//        final JToggleButton IpOff = new JToggleButton("断开");
//        IpOff.setSelected(true);
//        ButtonGroup buttonGroup = new ButtonGroup();
//        buttonGroup.add(IpConnect);
//        buttonGroup.add(IpOff);
//        IpConnect.setFocusable(false);
//        IpOff.setFocusable(false);
//        panel.add(IpConnect);
//        panel.add(IpOff);
//        IpConnectAndOffListener Iplistener = new IpConnectAndOffListener();
//        IpConnect.addActionListener(Iplistener);
//        IpOff.addActionListener(Iplistener);
//
//        JPanel N1 = new JPanel();
//        FlowLayout flowLayout3 = new FlowLayout(FlowLayout.CENTER);
//        flowLayout3.setVgap(-2);
//        N1.setLayout(flowLayout3);
//        //N1.setBorder(new LineBorder(Color.gray));
//		/*N1.add(new JLabel("设备型号:"));
//		field = new JTextField(8);
//		field.setEnabled(false);
//		N1.add(field);*/
//
//        ////////////////注释usb
//        //N1.add(new JLabel("USB转DMX512:"));
//        JButton button3 = new JButton("连接");
//        MainUi.map.put("USBLink_head", button3);
//        button3.addActionListener(new Link());
//        button3.setFocusable(false);
//        //N1.add(button3);
//        ///////////////////////
//        N1.add(new JLabel("                   "));
//        JButton restartBtn = new JButton("重启设备");
//        restartBtn.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                //fa 08 d9 +（机型） 00 00 00 +校验
//                if (Data.serialPort != null) {
//                    try {
//                        OutputStream os = Data.serialPort.getOutputStream();
//                        byte[] b = new byte[8];
//                        b[0] = (byte) 0xFA;
//                        b[1] = (byte) 0x08;
//                        b[2] = (byte) 0xD9;
//                        b[3] = (byte) ZhiLingJi.TYPE;
//                        b[7] = (byte) ZhiLingJi.getJiaoYan(b);
//                        os.write(b);
//                        os.flush();
//                    } catch (Exception e2) {
//                        e2.printStackTrace();
//                    }
//                }
//            }
//        });
//        restartBtn.setFocusable(false);
//        N1.add(restartBtn);
//
//        N1.add(new JLabel("设备型号:"));
//        field = new JTextField(8);
//        field.setEnabled(false);
//        N1.add(field);
//
//        N1.setPreferredSize(new Dimension(194, 60));
//        pane.add(N1);
//
//        JPanel pane2 = new JPanel();
//        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.CENTER);
//        flowLayout2.setVgap(-4);
//        flowLayout2.setHgap(-2);
//        pane2.setLayout(flowLayout2);
//        TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "设备连接", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
//        pane2.setBorder(tb1);
//        pane2.setPreferredSize(new Dimension(240, 60));
//        pane2.add(new JLabel("端口:"));
//        comCheckBox = new JComboBox();
//        comCheckBox.setFocusable(false);
//        comCheckBox.setPreferredSize(new Dimension(88, 30));
//        CommPortIdentifier cpid;
//        Enumeration enumeration = CommPortIdentifier.getPortIdentifiers();
//        while (enumeration.hasMoreElements()) {
//            cpid = (CommPortIdentifier) enumeration.nextElement();
//            if (cpid.getPortType() == CommPortIdentifier.PORT_SERIAL) {
//                comCheckBox.addItem(cpid.getName());
//            }
//        }
//        pane2.add(comCheckBox);
//        button = new JToggleButton("连接");
//        button2 = new JToggleButton("断开");
//        button2.setSelected(true);
//        button.setPreferredSize(new Dimension(56, 32));
//        button2.setPreferredSize(new Dimension(56, 32));
//        button.addActionListener(this);
//        button2.addActionListener(this);
//        ButtonGroup group2 = new ButtonGroup();
//        group2.add(button);
//        group2.add(button2);
//        button.setFocusable(false);
//        button2.setFocusable(false);
//        pane2.add(button);
//        pane2.add(button2);
//        pane.add(panel);
//        pane.add(pane2);
    }

    public void actionPerformed(ActionEvent e) {
        final long time = 300;
        if ("连接".equals(e.getActionCommand())) {
            if (Data.serialPort != null) {
                return;
            }
            try {
                hex = null;
                final String comsString = comCheckBox.getSelectedItem().toString();
                Data.comKou = comsString;
                CommPortIdentifier cpid = CommPortIdentifier.getPortIdentifier(comsString);
                Data.serialPort = (SerialPort) cpid.open(comsString, 5);
                Data.serialPort.setSerialPortParams(4800, 8, 1, 0);
                Data.baud = 4800;
                final Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            InputStream is = Data.serialPort.getInputStream();
                            byte[] b = new byte[16];
                            int len = is.read(b);
                            if (len == 8) {
                                ZhiLingJi.TYPE = b[3];
                                hex = Integer.toHexString(b[3] & 0xFF);
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                });
                thread.start();
                OutputStream os = Data.serialPort.getOutputStream();
                os.write(ZhiLingJi.link());
                os.flush();

                new Timer().schedule(new TimerTask() {
                    public void run() {
                        thread.stop();
                        if (hex == null) {
                            field.setText("");
                            Data.serialPort.close();
                            Data.serialPort = null;
                            try {
                                CommPortIdentifier cpid = CommPortIdentifier.getPortIdentifier(comsString);
                                Data.serialPort = (SerialPort) cpid.open(comsString, 5);
                                Data.serialPort.setSerialPortParams(9600, 8, 1, 0);
                                Data.baud = 9600;
                                final Thread thread2 = new Thread(new Runnable() {
                                    public void run() {
                                        try {
                                            InputStream is = Data.serialPort.getInputStream();
                                            byte[] b = new byte[16];
                                            int len = is.read(b);
                                            if (len == 8) {
                                                ZhiLingJi.TYPE = b[3];
                                                hex = Integer.toHexString(b[3] & 0xFF);
                                            }
                                        } catch (Exception e2) {
                                            e2.printStackTrace();
                                        }
                                    }
                                });
                                thread2.start();
                                OutputStream os = Data.serialPort.getOutputStream();
                                os.write(ZhiLingJi.link());
                                os.flush();

                                new Timer().schedule(new TimerTask() {
                                    public void run() {
                                        thread2.stop();
                                        if (hex == null) {
                                            field.setText("");
                                            Data.serialPort.close();
                                            Data.serialPort = null;
                                            button2.setSelected(true);
                                            JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "串口连接失败!", "提示", JOptionPane.ERROR_MESSAGE);
                                        } else {
                                            String s = hex;
                                            s = setType(hex);
                                            field.setText(s);
                                            //切换波特率
                                            try {
                                                OutputStream os = Data.serialPort.getOutputStream();
                                                os.write(ZhiLingJi.setBaut());
                                                os.flush();
                                                Thread.sleep(100);
												/*Data.serialPort.close();
												Data.serialPort = null;
												CommPortIdentifier cpid = CommPortIdentifier.getPortIdentifier(comsString);
												Data.serialPort = (SerialPort)cpid.open(comsString, 5);*/
                                                Data.serialPort.setSerialPortParams(115200, 8, 1, 0);
                                                Data.baud = 115200;
                                            } catch (Exception e2) {
                                                e2.printStackTrace();
                                            }
                                            (Data.thread = new Thread(new ComReturnListener())).start();
                                        }
                                    }
                                }, time);
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        } else {
                            String s = hex;
                            s = setType(hex);
                            field.setText(s);
                            //切换波特率
                            try {
                                OutputStream os = Data.serialPort.getOutputStream();
                                os.write(ZhiLingJi.setBaut());
                                os.flush();
                                Thread.sleep(100);
								/*Data.serialPort.close();
								Data.serialPort = null;
								CommPortIdentifier cpid = CommPortIdentifier.getPortIdentifier(comsString);
								Data.serialPort = (SerialPort)cpid.open(comsString, 5);*/
                                //Data.serialPort.setSerialPortParams(57600, 8, 1, 0);
                                Data.serialPort.setSerialPortParams(115200, 8, 1, 0);
                                Data.baud = 115200;
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            (Data.thread = new Thread(new ComReturnListener())).start();
                        }
                    }
                }, time);
            } catch (javax.comm.PortInUseException ee) {
                button2.setSelected(true);
                field.setText("");
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "串口已被占用!", "提示", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else {
            hex = null;
            if (Data.serialPort == null) {
                return;
            }
            try {
                OutputStream os = Data.serialPort.getOutputStream();
                os.write(ZhiLingJi.setBackBaut());
                os.flush();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            Data.thread.interrupt();
            Data.thread = null;
            Data.serialPort.close();
            Data.serialPort = null;
            button2.setSelected(true);
            field.setText("");
            init();
        }
    }

    private void init() {
        //8个不可调
        JComboBox[] boxs8 = (JComboBox[]) MainUi.map.get("kaiGuangBox_BuKeTiao");
        //4个灯的开关亮度
        JComboBox[] boxs = (JComboBox[]) MainUi.map.get("kaiGuangBox");
        JSlider[] sliders = (JSlider[]) MainUi.map.get("liangDuSliders");
        JComboBox[] boxs2 = (JComboBox[]) MainUi.map.get("liangDuBoxs");
        for (int i = 0; i < 8; i++) {
            boxs8[i].setEnabled(true);
        }
        for (int i = 0; i < 4; i++) {
            boxs[i].setEnabled(true);
            sliders[i].setEnabled(true);
            boxs2[i].setEnabled(true);
        }
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
