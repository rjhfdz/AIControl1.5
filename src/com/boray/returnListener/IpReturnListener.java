package com.boray.returnListener;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.Socket;
import com.boray.Utils.Util;
import com.boray.beiFen.Listener.LoadDMXactionListener;
import com.boray.beiFen.Listener.LoadToDeviceActionListener;
import com.boray.beiFen.UI.JingDuUI;
import com.boray.mainUi.MainUi;
import com.boray.xiaoGuoDeng.Listener.DMXModelListener;
import com.boray.xiaoGuoDeng.UI.DefineJLable2;
import com.boray.zhongKon.Listener.WriteActionListener;
import sun.plugin2.main.server.Plugin;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.util.Timer;
import java.util.TimerTask;

public class IpReturnListener implements Runnable {
    @Override
    public void run() {
        byte[] temp = new byte[1024];
        int len = -1;
        DatagramPacket packet = new DatagramPacket(temp, temp.length);
        String hex0, hex1, hex2, hex3, hex4, hex5;
        while (true) {
            try {
                Data.socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            len = packet.getLength();
            if (len > 5) {
                hex0 = Integer.toHexString(temp[0] & 0xFF);
                hex1 = Integer.toHexString(temp[1] & 0xFF);
                hex2 = Integer.toHexString(temp[2] & 0xFF);
                hex3 = Integer.toHexString(temp[3] & 0xFF);
                hex4 = Integer.toHexString(temp[4] & 0xFF);
                hex5 = Integer.toHexString(temp[5] & 0xFF);
                if (hex0.equals("fd") && hex2.equals("d4")) {
                    int size = 20;
                    int len1 = 0;
                    byte[] b1 = new byte[size];
                    if (len <= size) {
                        for (int i = 0; i < len; i++) {
                            b1[i] = temp[i];
                        }
                        len1 = getAllData(size, len, b1);
                    } else {
                        for (int i = 0; i < size; i++) {
                            b1[i] = temp[i];
                        }
                        len1 = size;
                    }
                    if (len1 == size) {
                        final byte[] cc = b1;
                        new Thread(new Runnable() {
                            public void run() {
                                setStudyZhongKong(cc);
                            }
                        }).start();
                    }
                } else if (hex0.equals("fd") && hex2.equals("80")) {
                    int size = 10, packetN = 0;
                    byte[] b1 = new byte[size];
                    if (len <= size) {
                        for (int i = 0; i < len; i++) {
                            b1[i] = temp[i];
                        }
                    }
                    int len1 = getAllData(size, len, b1);
                    JProgressBar progressBar = (JProgressBar) MainUi.map.get("progressBar");
                    JLabel label = (JLabel) MainUi.map.get("JingDuLabel");
                    boolean notNull = false;
                    if (progressBar != null && label != null) {
                        notNull = true;
                    }
                    if (len1 == size && notNull) {
                        boolean retn = false;
                        int lx = Byte.toUnsignedInt(b1[4]);
                        packetN = Byte.toUnsignedInt(b1[5]) * 256 + Byte.toUnsignedInt(b1[6]);
                        if (lx == 1) {
                            progressBar.setMinimum(0);
                            progressBar.setMaximum(40);
                            label.setText(packetN + "/40");
                        } else if (lx == 6) {
                            progressBar.setMinimum(0);
                            progressBar.setMaximum(40);
                            label.setText(packetN + "/40");
                            if (packetN == 40) {
                                LoadToDeviceActionListener.timer.cancel();
                                LoadToDeviceActionListener.timer = null;
                                LoadToDeviceActionListener.temp = null;
                                JDialog dialog = (JDialog) MainUi.map.get("JingDuDialog");
                                dialog.dispose();
                                JFrame frame = (JFrame) MainUi.map.get("frame");
                                JOptionPane.showMessageDialog(frame, "加载完成！", "提示", JOptionPane.PLAIN_MESSAGE);
                                retn = true;
                            }
                        } else if (lx == 2) {
                            progressBar.setMinimum(0);
                            progressBar.setMaximum(16);
                            label.setText(packetN + "/16");
                            if (packetN == 16) {
                                LoadToDeviceActionListener.timer.cancel();
                                LoadToDeviceActionListener.timer = null;
                                LoadToDeviceActionListener.temp = null;
                                JDialog dialog = (JDialog) MainUi.map.get("JingDuDialog");
                                dialog.dispose();
                                JFrame frame = (JFrame) MainUi.map.get("frame");
                                JOptionPane.showMessageDialog(frame, "加载完成！", "提示", JOptionPane.PLAIN_MESSAGE);
                                retn = true;
                            }
                        } else if (lx == 3) {
                            progressBar.setMinimum(0);
                            progressBar.setMaximum(16);
                            label.setText(packetN + "/16");
								/*if (packetN == 16) {
									LoadToDeviceActionListener.timer.cancel();
									LoadToDeviceActionListener.timer = null;
									JDialog dialog = (JDialog)MainUi.map.get("JingDuDialog");
									dialog.dispose();
									JFrame frame = (JFrame)MainUi.map.get("frame");
									JOptionPane.showMessageDialog(frame, "加载完成！", "提示", JOptionPane.PLAIN_MESSAGE);
									retn = true;
								}*/
                        } else if (lx == 5) {
                            progressBar.setMinimum(0);
                            progressBar.setMaximum(18);
                            label.setText(packetN + "/18");
                            if (packetN == 18) {
                                LoadToDeviceActionListener.timer.cancel();
                                LoadToDeviceActionListener.timer = null;
                                LoadToDeviceActionListener.temp = null;
                                JDialog dialog = (JDialog) MainUi.map.get("JingDuDialog");
                                dialog.dispose();
                                JFrame frame = (JFrame) MainUi.map.get("frame");
                                JOptionPane.showMessageDialog(frame, "加载完成！", "提示", JOptionPane.PLAIN_MESSAGE);
                                retn = true;
                            }
                        } else if (lx == 16) {
                            progressBar.setMinimum(0);
                            progressBar.setMaximum(112);
                            label.setText(packetN + "/112");
                            if (packetN == 112) {
                                LoadToDeviceActionListener.timer.cancel();
                                LoadToDeviceActionListener.timer = null;
                                LoadToDeviceActionListener.temp = null;
                                JDialog dialog = (JDialog) MainUi.map.get("JingDuDialog");
                                dialog.dispose();
                                JFrame frame = (JFrame) MainUi.map.get("frame");
                                JOptionPane.showMessageDialog(frame, "加载完成！", "提示", JOptionPane.PLAIN_MESSAGE);
                                retn = true;
                            }
                        } else if (lx == 32) {
                            progressBar.setMinimum(0);
                            progressBar.setMaximum(LoadDMXactionListener.pktCnt);
                            label.setText(packetN + "/" + LoadDMXactionListener.pktCnt);
                            if (packetN == LoadDMXactionListener.pktCnt) {
                                LoadDMXactionListener.timer.cancel();
                                LoadDMXactionListener.temp = null;
                                LoadDMXactionListener.timer = null;
                                JDialog dialog = (JDialog) MainUi.map.get("JingDuDialog");
                                dialog.dispose();
                                JFrame frame = (JFrame) MainUi.map.get("frame");
                                JOptionPane.showMessageDialog(frame, "加载完成！", "提示", JOptionPane.PLAIN_MESSAGE);
                                retn = true;
                            } else {
                                if (JingDuUI.closeDialog) {
                                    LoadDMXactionListener.timer.cancel();
                                    LoadDMXactionListener.timer = null;
                                    if (packetN % 300 == 0) {
                                        System.gc();
                                    }
                                    LoadDMXactionListener.reSend(packetN + 1);
                                } else {
                                    if (LoadDMXactionListener.timer != null) {
                                        LoadDMXactionListener.timer.cancel();
                                        LoadDMXactionListener.timer = null;
                                    }
                                }
                                retn = true;
                            }
                        }
                        progressBar.setValue(packetN);
                        if (LoadToDeviceActionListener.timer != null) {
                            LoadToDeviceActionListener.timer.cancel();
                            LoadToDeviceActionListener.timer = null;
                        }
                        if (JingDuUI.closeDialog && !retn) {
                            LoadToDeviceActionListener.reSend(packetN + 1);
                        }
                    }
                } else if (hex0.equals("fd") && hex2.equals("61") && hex4.equals("91")) {
                    byte[] bytes = new byte[80];
                    for (int i = 0; i < 80; i++) {
                        bytes[i] = temp[i];
                    }
                    setLuZhiData(bytes);
                } else if (hex0.equals("fd") && hex1.equals("14") && hex2.equals("61")) {
                    byte[] buff = new byte[20];
                    for (int i = 0; i < 20; i++) {
                        buff[i] = temp[i];
                    }
                    String code = Integer.toHexString(buff[4] & 0XFF);
                    String str = Integer.toHexString(buff[5] & 0XFF);
                    String model = Integer.toHexString(buff[6] & 0XFF);
                    Integer modelInt = Math.toIntExact(Long.parseLong(model.toUpperCase(), 16));
                    if (code.equals("a4")) {
                        try {
                            Object[] options = {"否", "是"};
                            int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "数据擦除完成，开始录制数据？", "提示",
                                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                                    null, options, options[1]);
                            if (yes == 1) {
                                transcribeStart(modelInt, buff[3]);
                            }
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }

                    }
                    if (code.equals("a5")) {
                        JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "数据录制成功！", "提示", JOptionPane.PLAIN_MESSAGE);
                        JProgressBar bar = (JProgressBar) MainUi.map.get("DMXBarTwo" + modelInt);
                        JLabel DMXsec = (JLabel) MainUi.map.get("DMXsec" + modelInt);
                        DMXsec.setText("0秒");
                        bar.setValue(0);
                        DMXModelListener listener = new DMXModelListener();
                        Socket.UDPSendData(listener.queryLuZhi());
                    }
                    if (str.equals("9d")) {
                        String model2 = Integer.toHexString(buff[7] & 0XFF);
                        Integer integer = Math.toIntExact(Long.parseLong(model2.toUpperCase(), 16));
                        String hexString = Integer.toHexString(buff[11] & 0XFF);
                        Integer value = Math.toIntExact(Long.parseLong(hexString.toUpperCase(), 16));
                        JProgressBar bar = (JProgressBar) MainUi.map.get("DMXBarTwo" + integer);
                        JLabel DMXsec = (JLabel) MainUi.map.get("DMXsec" + integer);
                        DMXsec.setText(value + "秒");
                        bar.setValue(value);
                    }
                } else if (hex0.equals("fd") && hex1.equals("14") && hex2.equals("64") && hex4.equals("f")) {
                    byte[] buff = new byte[20];
                    for (int i = 0; i < 20; i++) {
                        buff[i] = temp[i];
                    }
                    String state = Integer.toHexString(buff[6] & 0XFF);
                    String ImportAndExport = Integer.toHexString(buff[5] & 0XFF);
                    JLabel stateLabel = (JLabel) MainUi.map.get("state");
                    if (state.equals("81") && (!ImportAndExport.equals("1") && !ImportAndExport.equals("2"))) {
                        stateLabel.setText("已插入");
                    } else if (state.equals("82") && (!ImportAndExport.equals("1") && !ImportAndExport.equals("2"))) {
                        stateLabel.setText("未插入");
                    }
                    if (ImportAndExport.equals("1") || ImportAndExport.equals("2")) {
                        final JProgressBar bar = (JProgressBar) MainUi.map.get("USBProgressBar");
                        bar.setVisible(true);
                        Integer value = Math.toIntExact(Long.parseLong(state.toUpperCase(), 16));
                        bar.setValue(value);
                        if (value.equals(255)) {
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    bar.setVisible(false);
                                }
                            }, 2000);
                        }
                    }
                } else if (hex0.equals("aa") && hex1.equals("57") && hex4.equals("85") && hex5.equals("17")) {
                    int size = 10, packetN = 0;
                    byte[] b1 = new byte[size];
                    if (len <= size) {
                        for (int i = 0; i < len; i++) {
                            b1[i] = temp[i];
                        }
                    }
                    int len1 = getAllData(size, len, b1);
                    if (len1 == size) {//收到反馈 停止定时器 发出数据后再重新开启 同时记录发包
                        Data.againSendDataTimer.cancel();
                        Data.againSendDataTimer = null;
                        packetN = Byte.toUnsignedInt(b1[6]) * 256 + Byte.toUnsignedInt(b1[7]);
                        JButton dataWrite = (JButton) MainUi.map.get("comAndWifiDataWrite");
                        if (packetN == Data.dataWrite.length) {
                            //清除重发记录数 防止下次重发出错
                            Data.sendDataCount = 0;
                            dataWrite.setText("写入控制器");
                            dataWrite.setEnabled(true);
                        } else {
                            Socket.UDPSendData((byte[]) Data.dataWrite[packetN]);
                            Data.sendDataSum = packetN;
                            dataWrite.setText((Data.sendDataSum + 1) + "/" + Data.dataWrite.length);
                            Util.againSendData();
                        }
                    }
                }
            }
        }
    }

    /**
     * 展示录制数据
     *
     * @param data
     */
    public void setLuZhiData(byte[] data) {
        int sc = Byte.toUnsignedInt(data[7]);
        JPanel timeBlockPanels = ((JPanel[]) MainUi.map.get("timeBlockPanels_group" + sc))[0];
        if (Byte.toUnsignedInt(data[9]) == 1) {
            timeBlockPanels.removeAll();
            int start = Byte.toUnsignedInt(data[11]) * 256 + Byte.toUnsignedInt(data[10]);
            int end = Byte.toUnsignedInt(data[13]) * 256 + Byte.toUnsignedInt(data[12]);
            int sumTime = Byte.toUnsignedInt(data[51]) * 256 + Byte.toUnsignedInt(data[50]);
            DefineJLable2 lable2 = new DefineJLable2(start + "", end + "", (timeBlockPanels.getComponentCount() + 1), sumTime, timeBlockPanels);
            lable2.setSize((end - start) * 5, 29);
            if (Integer.toHexString(data[52] & 0xFF).equals("80"))
                lable2.setBackground(Color.green);
            else
                lable2.setBackground(Color.red);
            timeBlockPanels.add(lable2);
        }
    }

    /**
     * 录制数据发送开始录制指令
     *
     * @param model
     */
    private void transcribeStart(int model, byte type) {
        byte[] buff = new byte[20];
        buff[0] = (byte) 0xFA;
        buff[1] = (byte) 0x14;
        buff[2] = (byte) 0x61;
        buff[3] = type;
        buff[5] = (byte) 0x9E;
        buff[7] = (byte) model;
        buff[10] = (byte) 0x04;
        buff[19] = ZhiLingJi.getJiaoYan(buff);
        Socket.UDPSendData(buff);
    }

    private int getAllData(int size, int len, byte[] bb) {
        int len1 = len, len2 = 0;
        try {
            byte[] b = new byte[size];
            DatagramPacket packet = new DatagramPacket(b, b.length);
            while (len1 < size) {
                Data.socket.receive(packet);
                len2 = packet.getLength();
                if (len2 != -1) {
                    len1 = len1 + len2;
                    if (len1 <= size) {
                        for (int i = len1 - len2; i < len1; i++) {
                            bb[i] = b[i - (len1 - len2)];
                        }
                    } else {
                        for (int i = len1 - len2; i < size; i++) {
                            bb[i] = b[i - (len1 - len2)];
                        }
                        len1 = size;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return len1;
    }

    private void setStudyZhongKong(byte[] b) {
        String s1 = Integer.toHexString(b[5] & 0xFF);
        int len = Byte.toUnsignedInt(b[6]);
        String tp = null;
        if ("5".equals(s1)) {
            //红外码
            JTextField[] fields = (JTextField[]) MainUi.map.get("redCodeFields");
            for (int j = 0; j < len; j++) {
                tp = Integer.toHexString(b[7 + j] & 0xFF);
                if (tp.length() < 2) {
                    tp = "0" + tp;
                }
                fields[j].setText(tp.toUpperCase());
            }
        } else if ("1".equals(s1)) {
            //串口墙板
            JTextField[] fields2 = null;
            if ("4".equals(WriteActionListener.select)) {
                fields2 = (JTextField[]) MainUi.map.get("comQianBanFields1");
            } else if ("8".equals(WriteActionListener.select)) {
                fields2 = (JTextField[]) MainUi.map.get("comQianBanFields2");
            } else if ("12".equals(WriteActionListener.select)) {
                fields2 = (JTextField[]) MainUi.map.get("comQianBanFields3");
            } else if ("16".equals(WriteActionListener.select)) {
                fields2 = (JTextField[]) MainUi.map.get("comQianBanFields4");
            }
            for (int j = 0; j < len; j++) {
                tp = Integer.toHexString(b[7 + j] & 0xFF);
                if (tp.length() < 2) {
                    tp = "0" + tp;
                }
                fields2[j].setText(tp.toUpperCase());
            }
            for (int i = len; i < fields2.length; i++) {
                fields2[i].setText("");
            }
        }
    }
}
