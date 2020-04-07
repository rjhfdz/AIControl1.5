package com.boray.mainUi;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Listener.ArtNet1ConnectListener;
import com.boray.Listener.ArtNet2ConnectListener;
import com.boray.Listener.IpConnectAndOffListener;
import com.boray.Utils.JIpAddressField;
import com.boray.Utils.Socket;
import com.boray.Utils.WinRegistry;
import com.boray.main.Listener.DataWriteListener;
import com.boray.returnListener.ComReturnListener;
import com.boray.usb.Link;

import javax.comm.CommDriver;
import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Timer;

public class RightPane implements ActionListener {

    private JComboBox comCheckBox;
    private String hex = null;
    private JToggleButton button = null;
    private JToggleButton button2 = null;
    private JButton dataWrite = null;
    private JToggleButton IpOff = null;
    private JToggleButton IpConnect = null;
    private JTextField field;

    public void show(JPanel pane) {
        JPanel panel = new JPanel();
        setWIFIUI(panel);//加载WiFi界面

        JPanel N1 = new JPanel();
        FlowLayout flowLayout3 = new FlowLayout(FlowLayout.CENTER);
        flowLayout3.setVgap(-2);
        N1.setLayout(flowLayout3);
        JButton button3 = new JButton("连接");
        MainUi.map.put("USBLink_head", button3);
        button3.addActionListener(new Link());
        button3.setFocusable(false);
//        N1.add(new JLabel("                   "));
        dataWrite = new JButton("写入控制器");
        MainUi.map.put("comAndWifiDataWrite", dataWrite);
        DataWriteListener dataWriteListener = new DataWriteListener(dataWrite);
        dataWrite.addActionListener(dataWriteListener);
        N1.add(dataWrite);
        JPanel panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(160, 10));
        N1.add(panel2);
        JPanel panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(160, 1));
        panel1.setBorder(BorderFactory.createLineBorder(Color.gray));
        N1.add(panel1);
        JPanel panel4 = new JPanel();
        panel4.setPreferredSize(new Dimension(160, 10));
        N1.add(panel4);
        N1.add(new JLabel("设备型号:"));
        field = new JTextField(5);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setEnabled(false);
        MainUi.map.put("sheBeiField", field);
        N1.add(field);
        N1.setPreferredSize(new Dimension(150, 80));
        pane.add(N1);

        JPanel pane2 = new JPanel();
        setComUI(pane2);//加载串口界面

        JButton restartBtn = new JButton("重启设备");
        restartBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //fa 08 d9 +（机型） 00 00 00 +校验
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "设备将重启，管理软件会断开连接，请重新点连接。", "提示", JOptionPane.ERROR_MESSAGE);
                if (Data.serialPort != null) {
                    try {
                        OutputStream os = Data.serialPort.getOutputStream();
                        byte[] b = new byte[8];
                        b[0] = (byte) 0xFA;
                        b[1] = (byte) 0x08;
                        b[2] = (byte) 0xD9;
                        b[3] = (byte) ZhiLingJi.TYPE;
                        b[7] = (byte) ZhiLingJi.getJiaoYan(b);
                        os.write(b);
                        os.flush();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    Data.thread.interrupt();
                    Data.thread = null;
                    Data.serialPort.close();
                    Data.serialPort = null;
                    button2.setSelected(true);
                } else if (Data.socket != null) {
                    byte[] b = new byte[8];
                    b[0] = (byte) 0xFA;
                    b[1] = (byte) 0x08;
                    b[2] = (byte) 0xD9;
                    b[3] = (byte) ZhiLingJi.TYPE;
                    b[7] = (byte) ZhiLingJi.getJiaoYan(b);
                    Socket.UDPSendData(b);
                    Data.thread.stop();
                    Data.thread = null;
                    Data.socket.close();
                    Data.socket = null;
                    IpOff.setSelected(true);
                }
                field.setText("");
            }
        });
        restartBtn.setFocusable(false);
//        N1.add(restartBtn);
        pane.add(pane2);
        pane.add(panel);
        JPanel pane3 = new JPanel();
        setArtNetPanel(pane3);
        pane.add(pane3);
        pane.add(restartBtn);

    }

    public void setWIFIUI(JPanel panel) {
        panel.setPreferredSize(new Dimension(180, 100));
        TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "网络连接", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        panel.setBorder(tb2);
        JPanel pane4 = new JPanel();
        panel.add(pane4);
        pane4.setPreferredSize(new Dimension(100, 60));
//        pane4.setBorder(BorderFactory.createLineBorder(Color.gray));
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
        flowLayout2.setVgap(2);
        flowLayout2.setHgap(2);
        pane4.setLayout(flowLayout2);

        JIpAddressField IpAddress = new JIpAddressField(20);
        IpAddress.setIpAddress("192.168.4.1");
        MainUi.map.put("IpAddress", IpAddress);
//        panel.add(new Label("IP地址:"));
//        panel.add(IpAddress);
        pane4.add(new Label("IP地址:"));
        pane4.add(IpAddress);
        IpConnect = new JToggleButton("连接");
        IpOff = new JToggleButton("断开");
        IpConnect.setPreferredSize(new Dimension(56, 32));
        IpOff.setPreferredSize(new Dimension(56, 32));
        IpOff.setSelected(true);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(IpConnect);
        buttonGroup.add(IpOff);
        IpConnect.setFocusable(false);
        IpOff.setFocusable(false);
        JPanel pane5 = new JPanel();
//        pane5.setBorder(BorderFactory.createLineBorder(Color.gray));
        pane5.setPreferredSize(new Dimension(60, 60));
        FlowLayout flowLayout4 = new FlowLayout(FlowLayout.RIGHT);
        flowLayout4.setVgap(-2);
        flowLayout4.setHgap(-2);
        pane5.setLayout(flowLayout4);
        pane5.add(IpConnect);
        pane5.add(IpOff);
//        panel.add(IpConnect);
//        panel.add(IpOff);
        panel.add(pane5);
        IpConnectAndOffListener Iplistener = new IpConnectAndOffListener(IpConnect, IpOff);
        IpConnect.addActionListener(Iplistener);
        IpOff.addActionListener(Iplistener);
    }

    public void setComUI(JPanel pane2) {
        TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "串口连接", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        pane2.setBorder(tb1);
        pane2.setPreferredSize(new Dimension(180, 100));
        JPanel pane4 = new JPanel();
        pane2.add(pane4);
        pane4.setPreferredSize(new Dimension(90, 60));
//        pane4.setBorder(BorderFactory.createLineBorder(Color.gray));
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.CENTER);
        flowLayout2.setVgap(-2);
        flowLayout2.setHgap(-2);
        pane4.setLayout(flowLayout2);
        JLabel COMLabel = new JLabel("端口:");
        pane4.add(COMLabel);
        comCheckBox = new JComboBox();
        comCheckBox.setFocusable(false);
        comCheckBox.setPreferredSize(new Dimension(80, 30));
        CommPortIdentifier cpid;
        Enumeration enumeration = CommPortIdentifier.getPortIdentifiers();
        while (enumeration.hasMoreElements()) {
            cpid = (CommPortIdentifier) enumeration.nextElement();
            if (cpid.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                comCheckBox.addItem(cpid.getName());
            }
        }
        COMLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    Map<String, String> map = WinRegistry.valuesForPath(WinRegistry.HKEY_LOCAL_MACHINE, "HARDWARE\\DEVICEMAP\\SERIALCOMM");
                    if (map.size() > 0) {
                        comCheckBox.removeAllItems();
                        for (String str : map.values()) {
                            comCheckBox.addItem(str);
                        }
                    }
                    System.out.println();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                } catch (InvocationTargetException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        pane4.add(comCheckBox);
        button = new JToggleButton("连接");
        button2 = new JToggleButton("断开");
        button2.setSelected(true);
        button.setPreferredSize(new Dimension(56, 32));
        button2.setPreferredSize(new Dimension(56, 32));
        button.addActionListener(this);
        button2.addActionListener(this);
        ButtonGroup group2 = new ButtonGroup();
        group2.add(button);
        group2.add(button2);
        button.setFocusable(false);
        button2.setFocusable(false);
        JPanel pane5 = new JPanel();
//        pane5.setBorder(BorderFactory.createLineBorder(Color.gray));
        pane5.setPreferredSize(new Dimension(60, 60));
        FlowLayout flowLayout4 = new FlowLayout(FlowLayout.RIGHT);
        flowLayout4.setVgap(-2);
        flowLayout4.setHgap(-2);
        pane5.setLayout(flowLayout4);
        pane5.add(button);
        pane5.add(button2);
        pane2.add(pane5);
    }

    public void setArtNetPanel(JPanel pane3) {
//        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.CENTER);
//        flowLayout2.setVgap(-4);
//        flowLayout2.setHgap(-2);
//        pane3.setLayout(flowLayout2);
        TitledBorder tb3 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Art--Net", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        pane3.setBorder(tb3);
        pane3.setPreferredSize(new Dimension(180, 250));
        JPanel panel = new JPanel();
        FlowLayout flowLayout3 = new FlowLayout(FlowLayout.LEFT);
        flowLayout3.setVgap(0);
        flowLayout3.setHgap(0);
        panel.setLayout(flowLayout3);
//        panel.setBorder(BorderFactory.createLineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(170, 60));

//        pane3.add(new JLabel("IP1:"));
        panel.add(new Label("IP:  "));
        JIpAddressField artNet1 = new JIpAddressField(25);
        panel.add(artNet1);
        panel.add(new JLabel("port: "));
        JTextField port1 = new JTextField(4);
        panel.add(port1);
//        pane3.add(artNet1);
//        pane3.add(new JLabel("IP2:"));
        JPanel pane2 = new JPanel();
        pane2.setLayout(flowLayout3);
        pane2.setPreferredSize(new Dimension(170, 60));
        pane2.add(new Label("IP:  "));
        JIpAddressField artNet2 = new JIpAddressField(25);
        pane2.add(artNet2);
        pane2.add(new JLabel("port: "));
        JTextField port2 = new JTextField(4);
        pane2.add(port2);
//        pane3.add(artNet2);
//        pane3.add(new JLabel("Port1:"));
//        JTextField port1 = new JTextField(3);
//        port1.setHorizontalAlignment(JTextField.CENTER);
//        pane3.add(port1);
//        pane3.add(new JLabel("Port2:"));
//        JTextField port2 = new JTextField(3);
//        port2.setHorizontalAlignment(JTextField.CENTER);
//        pane3.add(port2);
        JButton Connect = new JButton("连接");
        JButton Disconnect = new JButton("断开");
        Disconnect.setSelected(true);
        ButtonGroup group3 = new ButtonGroup();
        group3.add(Connect);
        group3.add(Disconnect);
        Connect.setFocusable(false);
        Disconnect.setFocusable(false);
        ArtNet1ConnectListener artNet1ConnectListener = new ArtNet1ConnectListener(artNet1, port1, Connect, Disconnect);
        Connect.addActionListener(artNet1ConnectListener);
        Disconnect.addActionListener(artNet1ConnectListener);
        Connect.setPreferredSize(new Dimension(56, 32));
        Disconnect.setPreferredSize(new Dimension(56, 32));
        JPanel pane5 = new JPanel();
//        pane5.setBorder(BorderFactory.createLineBorder(Color.gray));
        pane5.setPreferredSize(new Dimension(170, 40));
        FlowLayout flowLayout4 = new FlowLayout(FlowLayout.CENTER);
        flowLayout4.setVgap(0);
        flowLayout4.setHgap(0);
        pane5.setLayout(flowLayout4);
        pane5.add(Connect);
        pane5.add(Disconnect);
        JPanel pane6 = new JPanel();
        pane6.setPreferredSize(new Dimension(170, 40));
        pane6.setLayout(flowLayout4);
        JButton Connect2 = new JButton("连接");
        JButton Disconnect2 = new JButton("断开");
        ArtNet2ConnectListener artNet2ConnectListener = new ArtNet2ConnectListener(artNet2, port2, Connect2, Disconnect2);
        Connect2.addActionListener(artNet2ConnectListener);
        Disconnect2.addActionListener(artNet2ConnectListener);
        ButtonGroup group4 = new ButtonGroup();
        group4.add(Connect2);
        group4.add(Disconnect2);
        Disconnect2.setSelected(true);
        Connect2.setFocusable(false);
        Disconnect2.setFocusable(false);
        Connect2.setPreferredSize(new Dimension(56, 32));
        Disconnect2.setPreferredSize(new Dimension(56, 32));
        pane6.add(Connect2);
        pane6.add(Disconnect2);
        pane3.add(panel);
        pane3.add(pane5);
        JPanel panel7 = new JPanel();
        panel7.setPreferredSize(new Dimension(175, 1));
        panel7.setBorder(BorderFactory.createLineBorder(Color.gray));
        pane3.add(panel7);
        pane3.add(pane2);
        pane3.add(pane6);
//        pane3.add(Connect);
//        pane3.add(Disconnect);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final long time = 300;
        if ("连接".equals(e.getActionCommand())) {
            if (Data.serialPort != null) {
                button2.setSelected(true);
                return;
            }
            if (Data.socket != null) {
                button2.setSelected(true);
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "请先断开网络连接后再尝试!", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                hex = null;
                final String comsString = comCheckBox.getSelectedItem().toString();
                Data.comKou = comsString;
                CommPortIdentifier cpid;
                try {
                    cpid = CommPortIdentifier.getPortIdentifier(comsString);
                } catch (Exception e1) {
                    CommPortIdentifier.addPortName(comsString, 1, (CommDriver) Class.forName("com.sun.comm.Win32Driver").newInstance());
                    cpid = CommPortIdentifier.getPortIdentifier(comsString);
                }
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

                new java.util.Timer().schedule(new TimerTask() {
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
