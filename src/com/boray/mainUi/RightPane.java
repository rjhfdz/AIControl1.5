package com.boray.mainUi;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Listener.IpConnectAndOffListener;
import com.boray.Utils.Socket;
import com.boray.main.Listener.DataWriteListener;
import com.boray.returnListener.ComReturnListener;
import com.boray.usb.Link;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

public class RightPane implements ActionListener {

    private JComboBox comCheckBox;
    private String hex = null;
    private JToggleButton button = null;
    private JToggleButton button2 = null;
    private JButton dataWrite = null;
    private JTextField field;

    public void show(JPanel pane) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(150, 140));
        TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "��������", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        panel.setBorder(tb2);
        final JTextField IpAddress = new JTextField(10);
        IpAddress.setHorizontalAlignment(JTextField.CENTER);
        IpAddress.setText("192.168.4.1");
        MainUi.map.put("IpAddress", IpAddress);
        panel.add(new Label("IP��ַ:"));
        panel.add(IpAddress);
        final JToggleButton IpConnect = new JToggleButton("����");
        final JToggleButton IpOff = new JToggleButton("�Ͽ�");
        IpConnect.setPreferredSize(new Dimension(56, 32));
        IpOff.setPreferredSize(new Dimension(56, 32));
        IpOff.setSelected(true);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(IpConnect);
        buttonGroup.add(IpOff);
        IpConnect.setFocusable(false);
        IpOff.setFocusable(false);
        panel.add(IpConnect);
        panel.add(IpOff);
        IpConnectAndOffListener Iplistener = new IpConnectAndOffListener(IpConnect, IpOff);
        IpConnect.addActionListener(Iplistener);
        IpOff.addActionListener(Iplistener);

        JPanel N1 = new JPanel();
        FlowLayout flowLayout3 = new FlowLayout(FlowLayout.CENTER);
        flowLayout3.setVgap(-2);
        N1.setLayout(flowLayout3);
        JButton button3 = new JButton("����");
        MainUi.map.put("USBLink_head", button3);
        button3.addActionListener(new Link());
        button3.setFocusable(false);
        N1.add(new JLabel("                   "));
        dataWrite = new JButton("д�������");
        DataWriteListener dataWriteListener = new DataWriteListener(dataWrite);
        dataWrite.addActionListener(dataWriteListener);
        N1.add(dataWrite);

        N1.add(new JLabel("�豸�ͺ�:"));
        field = new JTextField(8);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setEnabled(false);
        MainUi.map.put("sheBeiField", field);
        N1.add(field);

        N1.setPreferredSize(new Dimension(150, 100));
        pane.add(N1);

        JPanel pane2 = new JPanel();
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.CENTER);
        flowLayout2.setVgap(-4);
        flowLayout2.setHgap(-2);
//        pane2.setLayout(flowLayout2);
        TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "�豸����", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        pane2.setBorder(tb1);
        pane2.setPreferredSize(new Dimension(150, 100));
        JLabel COMLabel = new JLabel("�˿�:");
        pane2.add(COMLabel);
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
                CommPortIdentifier cpid;
                Enumeration enumeration = CommPortIdentifier.getPortIdentifiers();
                comCheckBox.removeAllItems();
                while (enumeration.hasMoreElements()) {
                    cpid = (CommPortIdentifier) enumeration.nextElement();
                    if (cpid.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                        comCheckBox.addItem(cpid.getName());
                    }
                }
            }
        });
        pane2.add(comCheckBox);
        button = new JToggleButton("����");
        button2 = new JToggleButton("�Ͽ�");
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
        pane2.add(button);
        pane2.add(button2);
        JButton restartBtn = new JButton("�����豸");
        restartBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //fa 08 d9 +�����ͣ� 00 00 00 +У��
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
        pane.add(restartBtn);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final long time = 300;
        if ("����".equals(e.getActionCommand())) {
            if (Data.serialPort != null) {
                button2.setSelected(true);
                return;
            }
            if (Data.socket != null) {
                button2.setSelected(true);
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "���ȶϿ��������Ӻ��ٳ���!", "��ʾ", JOptionPane.ERROR_MESSAGE);
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
                                            JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "��������ʧ��!", "��ʾ", JOptionPane.ERROR_MESSAGE);
                                        } else {
                                            String s = hex;
                                            s = setType(hex);
                                            field.setText(s);
                                            //�л�������
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
                            //�л�������
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
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "�����ѱ�ռ��!", "��ʾ", JOptionPane.ERROR_MESSAGE);
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
        //8�����ɵ�
        JComboBox[] boxs8 = (JComboBox[]) MainUi.map.get("kaiGuangBox_BuKeTiao");
        //4���ƵĿ�������
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
		/*�豸����	�ؼ���	����·��	�ɵ�·��
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
		// #define __L9100A__     0xBC//8+1�ŷ�          X10
        // #define __L9200A__     0xBD//8+1�ŷ�+��ܿյ�1· X12
        // #define __L9500A__     0xBE//2+8+1�ŷ�+��ܿ�  ��׼��       X15
        // #define __L9600A__     0xBF//4+8+1�ŷ�+��ܿյ�1·==��׼��  X16
		 */
        String s = "";
        //8�����ɵ�
        JComboBox[] boxs8 = (JComboBox[]) MainUi.map.get("kaiGuangBox_BuKeTiao");
        //4���ƵĿ�������
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