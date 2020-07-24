package com.boray.returnListener;

import com.boray.Data.Data;
import com.boray.Data.RdmData;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.Socket;
import com.boray.Utils.Util;
import com.boray.beiFen.Listener.LoadDMXactionListener;
import com.boray.beiFen.Listener.LoadToDeviceActionListener;
import com.boray.beiFen.UI.JingDuUI;
import com.boray.dengKu.UI.NewJTable;
import com.boray.dengKu.UI.RdmPaneUI;
import com.boray.mainUi.MainUi;
import com.boray.xiaoGuoDeng.Listener.DMXModelListener;
import com.boray.xiaoGuoDeng.UI.DefineJLable2;
import com.boray.zhongKon.Listener.WriteActionListener;
import sun.plugin2.main.server.Plugin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class IpReturnListener implements Runnable {
    @Override
    public void run() {
        try {


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
                        final JProgressBar bar = (JProgressBar) MainUi.map.get("USBProgressBar");
                        String state = Integer.toHexString(buff[6] & 0XFF);
                        String ImportAndExport = Integer.toHexString(buff[5] & 0XFF);
                        JLabel stateLabel = (JLabel) MainUi.map.get("state");
                        if (state.equals("81") && (!ImportAndExport.equals("1") && !ImportAndExport.equals("2"))) {
                            stateLabel.setText("已插入");
                        } else if (state.equals("82") && (!ImportAndExport.equals("1") && !ImportAndExport.equals("2"))) {
                            stateLabel.setText("未插入");
                            bar.setVisible(false);
                        }
                        if (ImportAndExport.equals("1") || ImportAndExport.equals("2")) {
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
                    } else if (hex0.equals("aa") && hex1.equals("58") && hex4.equals("85") && hex5.equals("17")) {
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
                            Data.sendDataCount = 0;//清除重发记录数 防止定时器计数错误
                            packetN = Byte.toUnsignedInt(b1[6]) * 256 + Byte.toUnsignedInt(b1[7]);
                            JButton dataWrite = (JButton) MainUi.map.get(Data.DataWriteBtnName);
                            if (packetN == Data.dataWrite.length) {
                                //清除重发记录数 防止下次重发出错
                                dataWrite.setText("写入控制器");
                                dataWrite.setEnabled(true);
                                Thread.sleep(300);
                                Util.RAMReset();
                            } else {
                                try {
                                    Thread.sleep(100);
                                    Socket.UDPSendData((byte[]) Data.dataWrite[packetN]);
                                    Data.sendDataSum = packetN;
                                    dataWrite.setText((Data.sendDataSum + 1) + "/" + Data.dataWrite.length);
                                    Util.againSendData();
                                } catch (Exception e) {

                                }
                            }
                        }
                    } else if (hex0.equals("fd") && hex2.equals("cc")) {
                        byte[] b1 = new byte[len];
                        for (int i = 0; i < len; i++) {
                            b1[i] = temp[i];
                        }
                        Rdmset(b1);
                    } else if (hex0.equals("fd") && hex2.equals("64") && !hex4.equals("f")) {
                        if (Byte.toUnsignedInt(temp[4]) == 1) {
                            int size = 30;
                            byte[] b1 = new byte[size];
                            if (len <= size) {
                                for (int i = 0; i < len; i++) {
                                    b1[i] = temp[i];
                                }
                            }
                            int len1 = getAllData(size, len, b1);
                            if (len1 == 30) {
                                final byte[] cc = b1;
                                new Thread(new Runnable() {
                                    public void run() {
                                        setChangJing2(cc);
                                    }
                                }).start();
                            }
                        } else if (Byte.toUnsignedInt(temp[4]) == 3) {
                            int size = 20;
                            byte[] b1 = new byte[size];
                            if (len <= size) {
                                for (int i = 0; i < len; i++) {
                                    b1[i] = temp[i];
                                }
                            }
                            int len1 = getAllData(size, len, b1);
                            if (len1 == size) {
                                final byte[] cc = b1;
                                new Thread(new Runnable() {
                                    public void run() {
                                        setQuanJu(cc);
                                        try {
                                            Thread.sleep(50);
                                            Socket.UDPSendData(ZhiLingJi.queryDevice());
                                        } catch (Exception e) {
                                        }
                                    }
                                }).start();
                            }
                        } else if (Byte.toUnsignedInt(temp[4]) == 2) {
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
                                        setZhongKong2(cc);
                                    }
                                }).start();
                            }
                        }
                    } else if (hex0.equals("fd") && hex2.equals("db")) {
                        int size = 90;
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
                            JLabel label = (JLabel) MainUi.map.get("DeviceLabel");
                            if (Data.deviceShow) {
                                label.setText("");
                                Data.deviceShow = false;
                            }
                            int lg = Byte.toUnsignedInt(b1[5]);
                            String s = new String(b1, 6, lg);
                            String ss = label.getText();
                            if (ss.length() > 0) {
                                label.setText(ss.substring(0, ss.length() - 7) + s + "<br>" + "</html>");
                            } else {
                                label.setText("<html>" + s + "<br>" + "</html>");
                            }
                        }
                    }
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    private void Rdmset(byte[] b) {
        //int length = 0;
        String hex0 = "", uidTemp = "";
        String[] a = new String[6];
        //length = b.length;//
        int ld = 4;
        hex0 = Integer.toHexString(b[9 - ld] & 0xFF);
        if ("41".equals(hex0)) {//设备数量
            RdmPaneUI.deviceCount = Byte.toUnsignedInt(b[17 - ld]) * 256 + Byte.toUnsignedInt(b[18 - ld]);
        } else if ("11".equals(hex0) && b.length >= 15) {//查UID
            uidTemp = "";
            byte[] uidByte = new byte[6];
            for (int i = 0; i < a.length; i++) {
                uidByte[i] = b[10 + i - ld];
                a[i] = Integer.toHexString(b[10 + i - ld] & 0xFF).toUpperCase();
                if (a[i].length() == 1) {
                    a[i] = "0" + a[i];
                }
                if (i == 0) {
                    uidTemp = uidTemp + a[i];
                } else {
                    uidTemp = uidTemp + "-" + a[i];
                }
            }
            NewJTable table = (NewJTable) MainUi.map.get("RDM_table");
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            JButton btn = (JButton) MainUi.map.get("RDMRefresh");
            if (!btn.isEnabled()) {
                if (!RdmPaneUI.uidList.contains(uidTemp)) {
                    String[] s = {String.valueOf(table.getRowCount() + 1), uidTemp,
                            String.valueOf(table.getRowCount() + 1), "", "", "", "", "进入高级设置"};
                    model.addRow(s);
                    RdmPaneUI.tempUid_Byte.add(uidByte);
                    RdmPaneUI.tempUidList.add(uidTemp);
                }
            } else {
                if (RdmPaneUI.uidList.size() == 0) {
                    for (int i = table.getRowCount() - 1; i >= 0; i--) {
                        model.removeRow(i);
                    }
                }
                String[] s = {String.valueOf(table.getRowCount() + 1), uidTemp,
                        String.valueOf(table.getRowCount() + 1), "", "", "", "", "进入高级设置"};
                model.addRow(s);
                RdmPaneUI.uid_Byte.add(uidByte);
                RdmPaneUI.uidList.add(uidTemp);
            }
        } else if ("21".equals(hex0)) {
            hex0 = Integer.toHexString(b[8 - ld] & 0xFF);
            if ("5".equals(hex0) && b.length >= 32) {//查型号
                uidTemp = "";
                for (int i = 0; i < a.length; i++) {
                    a[i] = Integer.toHexString(b[10 + i - ld] & 0xFF).toUpperCase();
                    if (a[i].length() == 1) {
                        a[i] = "0" + a[i];
                    }
                    if (i == 0) {
                        uidTemp = uidTemp + a[i];
                    } else {
                        uidTemp = uidTemp + "-" + a[i];
                    }
                }
                NewJTable table = (NewJTable) MainUi.map.get("RDM_table");
                for (int i = 0; i < table.getRowCount(); i++) {
                    if (table.getValueAt(i, 1).toString().equals(uidTemp)) {
                        table.setValueAt(new String(b, 23 - ld, 12), i, 3);
                        break;
                    }
                }
            } else if ("1".equals(hex0) && b.length >= 28) {//查起始地址、通道数
                if (RdmPaneUI.openSet) {

                    JTextField[] fields = (JTextField[]) MainUi.map.get("RdmSet_fields");
                    if (fields != null) {
                        fields[1].setText(Byte.toUnsignedInt(b[17 - ld]) + "." + Byte.toUnsignedInt(b[18 - ld]));//RDM协议版本号
                        fields[2].setText(Byte.toUnsignedInt(b[19 - ld]) + "" + Byte.toUnsignedInt(b[20 - ld]));//设备模型ID
                        fields[3].setText(Byte.toUnsignedInt(b[21 - ld]) + "" + Byte.toUnsignedInt(b[22 - ld]));//产品分类
                        fields[4].setText(Byte.toUnsignedInt(b[23 - ld]) + "," + Byte.toUnsignedInt(b[24 - ld])
                                + "," + Byte.toUnsignedInt(b[25 - ld]) + "," + Byte.toUnsignedInt(b[26 - ld]));//软件版本ID
                        fields[5].setText("" + Byte.toUnsignedInt(b[28 - ld]));//设备通道数
                        fields[6].setText(Byte.toUnsignedInt(b[29 - ld]) + "" + Byte.toUnsignedInt(b[30 - ld]));//DMX512特性
                        fields[7].setText((Byte.toUnsignedInt(b[31 - ld]) * 256 + Byte.toUnsignedInt(b[32 - ld])) + "");//DMX512起始地址
                        fields[8].setText((Byte.toUnsignedInt(b[33 - ld]) * 256 + Byte.toUnsignedInt(b[34 - ld])) + "");//从设备数量
                        fields[9].setText("" + Byte.toUnsignedInt(b[35 - ld]));//传感器数量
                    }
                    //设备状态查询
                    //UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
                    //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 6));
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                //UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
//                                OutputStream os = Data.serialPort.getOutputStream();
                                //查工作模式
                                //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 7));
                                //Thread.sleep(200);

                                //正反显示
                                //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 8));
//                                os.write(RdmData.serchType(RdmPaneUI.currentByte, 8));
//                                os.flush();
                                Socket.UDPSendData(RdmData.serchType(RdmPaneUI.currentByte, 8));
                                Thread.sleep(300);

                                //复位
                                //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 9));
                                //Thread.sleep(200);

                                //电动模式
                                //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 11));
//                                os.write(RdmData.serchType(RdmPaneUI.currentByte, 11));
//                                os.flush();
                                Socket.UDPSendData(RdmData.serchType(RdmPaneUI.currentByte, 11));
                                Thread.sleep(300);

                                //通道模式
                                //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 12));
//                                os.write(RdmData.serchType(RdmPaneUI.currentByte, 12));
//                                os.flush();
                                Socket.UDPSendData(RdmData.serchType(RdmPaneUI.currentByte, 12));
                                Thread.sleep(300);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } else {
                    uidTemp = "";
                    for (int i = 0; i < a.length; i++) {
                        a[i] = Integer.toHexString(b[10 + i - ld] & 0xFF).toUpperCase();
                        if (a[i].length() == 1) {
                            a[i] = "0" + a[i];
                        }
                        if (i == 0) {
                            uidTemp = uidTemp + a[i];
                        } else {
                            uidTemp = uidTemp + "-" + a[i];
                        }
                    }
                    String[] s = new String[2];
                    int address = Byte.toUnsignedInt(b[28 - ld]);//起始地址
                    int channels = Byte.toUnsignedInt(b[31 - ld]) * 256 + Byte.toUnsignedInt(b[32 - ld]);//通道数
                    s[0] = address + "";
                    s[1] = channels + "";

                    NewJTable table = (NewJTable) MainUi.map.get("RDM_table");
                    for (int i = 0; i < table.getRowCount(); i++) {
                        if (table.getValueAt(i, 1).toString().equals(uidTemp)) {
                            table.setValueAt(s[1], i, 4);
                            table.setValueAt(s[0], i, 5);
                            break;
                        }
                    }
                    //RdmPaneUI.addAndChannelMap.put(uidTemp, s);
                }
            } else if ("8".equals(hex0)) {//正反显示
                JComboBox box2 = (JComboBox) MainUi.map.get("RdmSet_zfShow_box");
                ItemListener listener = box2.getItemListeners()[0];
                box2.removeItemListener(listener);
                box2.setSelectedIndex(Byte.toUnsignedInt(b[17 - ld]) + 1);
                box2.addItemListener(listener);
            } else if ("c".equals(hex0)) {//通道模式
                JComboBox box8 = (JComboBox) MainUi.map.get("RdmSet_channelModel_box8");
                ItemListener listener = box8.getItemListeners()[0];
                box8.removeItemListener(listener);
                box8.setSelectedIndex(Byte.toUnsignedInt(b[17 - ld]));
                box8.addItemListener(listener);
                if (box8 != null) {

                }
            } else if ("b".equals(hex0)) {//电动模式
                int cc = Byte.toUnsignedInt(b[17 - ld]);
                int tp = cc;
                java.util.List list = (List) MainUi.map.get("DianJiComponet_list");
                JComboBox box = null;
                ItemListener listener = null;
                //X轴
                box = (JComboBox) list.get(0);
                listener = box.getItemListeners()[0];
                box.removeItemListener(listener);
                tp = tp % 2;
                box.setSelectedIndex(tp + 1);
                box.addItemListener(listener);

                //Y轴
                box = (JComboBox) list.get(2);
                box.removeItemListener(listener);
                tp = cc / 2;
                tp = tp % 2;
                box.setSelectedIndex(tp + 1);
                box.addItemListener(listener);

                //X轴角度
                box = (JComboBox) list.get(1);
                box.removeItemListener(listener);
                tp = cc / 4;
                tp = tp % 4;
                box.setSelectedIndex(tp + 1);
                box.addItemListener(listener);

                //Y轴角度
                box = (JComboBox) list.get(3);
                box.removeItemListener(listener);
                tp = cc / 16;
                box.setSelectedIndex(tp + 1);
                box.addItemListener(listener);

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

    private void setChangJing2(byte[] b) {
    }

    private void setQuanJu(byte[] b) {
    }

    private void setZhongKong2(byte[] b) {
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
