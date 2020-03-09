package com.boray.Listener;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.mainUi.MainUi;
import com.boray.returnListener.IpReturnListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

public class IpConnectAndOffListener implements ActionListener {

    private String hex = null;
    private JToggleButton IpConnect;
    private JToggleButton IpOff;

    private JTextField field;

    public IpConnectAndOffListener(JToggleButton IpConnect, JToggleButton IpOff) {
        this.IpConnect = IpConnect;
        this.IpOff = IpOff;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        field = (JTextField) MainUi.map.get("sheBeiField");
        final long time = 300;
        if ("连接".equals(e.getActionCommand())) {
            if (Data.socket != null) {
                IpOff.setSelected(true);
                return;
            }
            if (Data.serialPort != null) {
                IpOff.setSelected(true);
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "请先断开串口连接后再尝试!", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JTextField IpAddress = (JTextField) MainUi.map.get("IpAddress");
            IpAddress.setEnabled(false);
            try {
                Data.address = new InetSocketAddress(IpAddress.getText(), Data.port);
                Data.socket = new DatagramSocket(8090);//创建客户端+端口
                final Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try {
                            byte[] b = new byte[16];
                            DatagramPacket packet = new DatagramPacket(b, b.length);
                            Data.socket.receive(packet);
                            int len = packet.getLength();
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
                DatagramPacket packet = new DatagramPacket(ZhiLingJi.link(), ZhiLingJi.link().length);
                packet.setSocketAddress(Data.address);
                Data.socket.send(packet);

                new java.util.Timer().schedule(new TimerTask() {

                    @Override
                    public void run() {
                        thread.stop();
                        if (hex == null) {
                            final Thread thread2 = new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        byte[] b = new byte[16];
                                        DatagramPacket packet = new DatagramPacket(b, b.length);
                                        Data.socket.receive(packet);
                                        int len = packet.getLength();
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
                            DatagramPacket packet = new DatagramPacket(ZhiLingJi.link(), ZhiLingJi.link().length);
                            packet.setSocketAddress(Data.address);
                            try {
                                Data.socket.send(packet);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    thread2.stop();
                                    if (hex == null) {
                                        field.setText("");
                                        Data.socket.close();
                                        Data.socket = null;
                                        Data.address = null;
                                        IpOff.setSelected(true);
                                        IpAddress.setEnabled(true);
                                        JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "网络连接失败!", "提示", JOptionPane.ERROR_MESSAGE);
                                    } else {
                                        String s = hex;
                                        s = setType(hex);
                                        field.setText(s);
                                        //调用监听
                                        (Data.thread = new Thread(new IpReturnListener())).start();
                                    }
                                }
                            }, time);
                        } else {
                            String s = hex;
                            s = setType(hex);
                            field.setText(s);
                            //调用监听
                            (Data.thread = new Thread(new IpReturnListener())).start();
                        }
                    }
                }, time);
            } catch (BindException ex) {
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "8090端口或8089端口被占用!", "提示", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (SocketException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            hex = null;
            if (Data.socket == null) {
                return;
            }
            DatagramPacket packet = new DatagramPacket(ZhiLingJi.setBackBaut(), ZhiLingJi.setBackBaut().length);
            packet.setSocketAddress(Data.address);
            try {
                Data.socket.send(packet);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Data.thread.stop();
            Data.thread = null;
            Data.socket.close();
            Data.socket = null;
            field.setText("");
            IpOff.setSelected(true);
            JTextField IpAddress = (JTextField) MainUi.map.get("IpAddress");
            IpAddress.setEnabled(false);
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
