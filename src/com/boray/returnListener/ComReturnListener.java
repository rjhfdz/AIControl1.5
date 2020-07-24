package com.boray.returnListener;

import java.awt.*;
import java.awt.event.ItemListener;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.comm.CommPortIdentifier;
import javax.comm.SerialPort;
import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.usb.UsbPipe;

import com.boray.Data.Data;
import com.boray.Data.RdmData;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.IconJDialog;
import com.boray.Utils.Socket;
import com.boray.Utils.Util;
import com.boray.beiFen.Listener.BackupActionListener;
import com.boray.beiFen.Listener.LoadDMXactionListener;
import com.boray.beiFen.Listener.LoadToDeviceActionListener;
import com.boray.beiFen.UI.JingDuUI;
import com.boray.dengKu.UI.NewJTable;
import com.boray.dengKu.UI.RdmPaneUI;
import com.boray.mainUi.MainUi;
import com.boray.usb.UsbUtil;
import com.boray.xiaoGuoDeng.Listener.DMXModelListener;
import com.boray.xiaoGuoDeng.Listener.TranscribeListener;
import com.boray.xiaoGuoDeng.UI.DefineJLable2;
import com.boray.zhongKon.Listener.WriteActionListener;
import sun.applet.Main;

public class ComReturnListener implements Runnable {
    public void run() {
        try {
            //Data.serialPort.sendBreak(0);
            Data.serialPort.setInputBufferSize(1024);
            Data.serialPort.setOutputBufferSize(1024);
			/*Data.serialPort.notifyOnDataAvailable(true);
			Data.serialPort.notifyOnOutputEmpty(true);
			Data.serialPort.notifyOnBreakInterrupt(true);*/
            InputStream is = Data.serialPort.getInputStream();
            byte[] temp = new byte[1024];
            int len = -1;
            String hex0, hex1, hex2, secHex, hex3, hex4;
            while (true) {
                len = is.read(temp);
                if (len > 5) {
                    hex0 = Integer.toHexString(temp[0] & 0xFF);
                    secHex = Integer.toHexString(temp[1] & 0xFF);
                    hex1 = Integer.toHexString(temp[2] & 0xFF);
                    hex2 = Integer.toHexString(temp[3] & 0xFF);
                    hex3 = Integer.toHexString(temp[4] & 0xFF);
                    hex4 = Integer.toHexString(temp[5] & 0xFF);
                    if (hex0.equals("fd") && hex1.equals("88")) {//预览
                        int pk = Byte.toUnsignedInt(temp[4]) * 256 + Byte.toUnsignedInt(temp[5]);
                        Data.reviewMap.remove(pk);
                    } else if (hex0.equals("fd") && hex1.equals("80")) {
                        int size = 10, packetN = 0;
                        byte[] b1 = new byte[size];
                        if (len <= size) {
                            for (int i = 0; i < len; i++) {
                                b1[i] = temp[i];
                            }
                        }
                        int len1 = getAllData(size, len, b1, is);
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
                                    IconJDialog dialog = (IconJDialog) MainUi.map.get("JingDuDialog");
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
                    } else if (hex0.equals("aa") && secHex.equals("ff") && hex1.equals("55") && hex2.equals("aa")) {
                        int size = 527, packetN = 0;
                        byte[] b1 = new byte[size];
                        if (len <= size) {
                            for (int i = 0; i < len; i++) {
                                b1[i] = temp[i];
                            }
                        }
                        int len1 = getAllData(size, len, b1, is);
                        if (len1 == size) {
                            JProgressBar progressBar = (JProgressBar) MainUi.map.get("progressBar");
                            JLabel label = (JLabel) MainUi.map.get("JingDuLabel");
                            if (Byte.toUnsignedInt(b1[8]) == 16) {
                                BackupActionListener.timer.cancel();
                                BackupActionListener.timer = null;
                                packetN = Byte.toUnsignedInt(b1[9]) * 256 + Byte.toUnsignedInt(b1[10]);
                                System.arraycopy(b1, 11, BackupActionListener.temp[packetN - 1], 0, 512);
                                progressBar.setValue(packetN);
                                label.setText(packetN + "/" + BackupActionListener.packetCount);
                                if (BackupActionListener.packetCount > packetN) {
                                    if (JingDuUI.closeDialog) {
                                        OutputStream os = Data.serialPort.getOutputStream();
                                        os.write(ZhiLingJi.getDataByPacketN(packetN + 1, 16));
                                        os.flush();
                                        BackupActionListener.reSend(packetN + 1, 16);
                                    } else {
                                        if (BackupActionListener.timer != null) {
                                            BackupActionListener.timer.cancel();
                                            BackupActionListener.timer = null;
                                        }
                                    }
                                } else if (BackupActionListener.packetCount == packetN) {
                                    BackupActionListener.fileWrite();
                                }
                            } else if (Byte.toUnsignedInt(b1[8]) == 32) {
                                BackupActionListener.timer.cancel();
                                BackupActionListener.timer = null;
                                packetN = Byte.toUnsignedInt(b1[9]) * 256 + Byte.toUnsignedInt(b1[10]);
                                System.arraycopy(b1, 11, BackupActionListener.temp[packetN - 1], 0, 512);
                                progressBar.setValue(packetN);
                                label.setText(packetN + "/" + BackupActionListener.packetCount);
                                if (BackupActionListener.packetCount > packetN) {
                                    if (JingDuUI.closeDialog) {
                                        OutputStream os = Data.serialPort.getOutputStream();
                                        os.write(ZhiLingJi.getDataByPacketN(packetN + 1, 32));
                                        os.flush();
                                        BackupActionListener.reSend(packetN + 1, 32);
                                    } else {
                                        if (BackupActionListener.timer != null) {
                                            BackupActionListener.timer.cancel();
                                            BackupActionListener.timer = null;
                                        }
                                    }
                                } else if (BackupActionListener.packetCount == packetN) {
                                    BackupActionListener.fileWrite();
                                }
                            }
                        }

                    } else if (hex0.equals("fd") && hex1.equals("82")) {
                        int size = 10;
                        byte[] b1 = new byte[size];
                        if (len <= size) {
                            for (int i = 0; i < len; i++) {
                                b1[i] = temp[i];
                            }
                        }
                        int len1 = getAllData(size, len, b1, is);
                        if (len1 == size) {
                            int h = Byte.toUnsignedInt(b1[5]);
                            int l = Byte.toUnsignedInt(b1[6]);
                            BackupActionListener.packetCount = 256 * h + l;
                            BackupActionListener.temp = new byte[BackupActionListener.packetCount][512];
                        }
                    } else if ((hex0.equals("fd") && hex1.equals("31")) || (hex0.equals("fd") && hex1.equals("32"))) {
                        int size = 95;
                        byte[] b1 = new byte[size];
                        if (len <= size) {
                            for (int i = 0; i < len; i++) {
                                b1[i] = temp[i];
                            }
                        }
                        if (getAllData(size, len, b1, is) == size) {
                            final byte[] cc = b1;
                            final String ss = hex1;
                            int cj = Byte.toUnsignedInt(temp[4]);
                            JToggleButton btn = (JToggleButton) MainUi.map.get("QuanJuToggleBtn");
                            if (cj == 25 || (cj == 26 && btn.isSelected())) {
                                setTiaoGuang(cc, cj, hex1);
                            } else {
                                //场景配置
                                new Thread(new Runnable() {
                                    public void run() {
                                        try {
                                            setChangJing1(cc);
                                            if (ss.equals("31")) {
                                                OutputStream os = Data.serialPort.getOutputStream();
                                                os.write(ZhiLingJi.changJingChaXun2(Byte.toUnsignedInt(cc[4])));
                                                os.flush();
                                                os.close();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        }
                    } /*else if (hex0.equals("fd") && hex1.equals("32")) {
						
					} */ else if (hex0.equals("fd") && hex1.equals("64") && !hex3.equals("f")) {
                        if (Byte.toUnsignedInt(temp[4]) == 1) {
                            int size = 30;
                            byte[] b1 = new byte[size];
                            if (len <= size) {
                                for (int i = 0; i < len; i++) {
                                    b1[i] = temp[i];
                                }
                            }
                            int len1 = getAllData(size, len, b1, is);
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
                            int len1 = getAllData(size, len, b1, is);
                            if (len1 == size) {
                                final byte[] cc = b1;
                                new Thread(new Runnable() {
                                    public void run() {
                                        setQuanJu(cc);
                                        try {
                                            OutputStream os = Data.serialPort.getOutputStream();
                                            os.write(ZhiLingJi.queryDevice());
                                            os.flush();
                                            os.close();
                                        } catch (Exception e) {
                                            e.printStackTrace();
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
                                len1 = getAllData(size, len, b1, is);
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
                    } else if (hex0.equals("fd") && hex1.equals("63")) {
                        if (Byte.toUnsignedInt(temp[5]) == 0 || Byte.toUnsignedInt(temp[5]) == 1) {
                            int size = 20;
                            byte[] b1 = new byte[size];
                            if (len <= size) {
                                for (int i = 0; i < len; i++) {
                                    b1[i] = temp[i];
                                }
                            }
                            int len1 = getAllData(size, len, b1, is);
                            if (len1 == 20) {
                                final byte[] cc = b1;
                                new Thread(new Runnable() {
                                    public void run() {
                                        try {
                                            setKongTiao(cc);
                                            OutputStream os = Data.serialPort.getOutputStream();
                                            os.write(ZhiLingJi.queryKTCtrl());
                                            os.flush();
                                            os.close();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }).start();
                            }
                        }
                    } else if (hex0.equals("ca") && secHex.equals("1")) {
                        int size = 9;
                        int len1 = 0;
                        byte[] b1 = new byte[size];
                        if (len <= size) {
                            for (int i = 0; i < len; i++) {
                                b1[i] = temp[i];
                            }
                            len1 = getAllData(size, len, b1, is);
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
                                    showKongTiaoCtrl(cc);
                                }
                            }).start();
                        }
                    } else if (hex0.equals("fd") && hex1.equals("d3")) {
                        int size = 64;
                        int len1 = 0;
                        byte[] b1 = new byte[size];
                        if (len <= size) {
                            for (int i = 0; i < len; i++) {
                                b1[i] = temp[i];
                            }
                            len1 = getAllData(size, len, b1, is);
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
                                    try {
                                        setZhongKong1(cc);
                                        OutputStream os = Data.serialPort.getOutputStream();
                                        os.write(ZhiLingJi.queryZhongKon2());
                                        os.flush();
                                        os.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    } else if (hex0.equals("fd") && hex1.equals("d4")) {
                        int size = 20;
                        int len1 = 0;
                        byte[] b1 = new byte[size];
                        if (len <= size) {
                            for (int i = 0; i < len; i++) {
                                b1[i] = temp[i];
                            }
                            len1 = getAllData(size, len, b1, is);
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
                    } else if (hex0.equals("fc")) {
                        hex1 = Integer.toHexString(temp[4] & 0xFF);
                        if (hex1.equals("b9")) {
                            JLabel label = (JLabel) MainUi.map.get("LiangDu");
                            label.setText(Byte.toUnsignedInt(temp[5]) + "");
                        }
                    } else if (hex0.equals("fd") && hex1.equals("db")) {
                        int size = 90;
                        int len1 = 0;
                        byte[] b1 = new byte[size];
                        if (len <= size) {
                            for (int i = 0; i < len; i++) {
                                b1[i] = temp[i];
                            }
                            len1 = getAllData(size, len, b1, is);
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
                    } else if (hex0.equals("fd") && hex1.equals("cc")) {
                        byte[] b1 = new byte[len];
                        for (int i = 0; i < len; i++) {
                            b1[i] = temp[i];
                        }
                        Rdmset(b1);
                    } else if (hex0.equals("fd") && secHex.equals("14") && hex1.equals("64") && hex3.equals("f")) {
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
                    } else if (hex0.equals("fd") && secHex.equals("14") && hex1.equals("61")) {
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
                            Socket.SerialPortSendData(listener.queryLuZhi());
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
                    } else if (hex0.equals("fd") && hex1.equals("61") && hex3.equals("91")) {
                        byte[] bytes = new byte[80];
                        for (int i = 0; i < 80; i++) {
                            bytes[i] = temp[i];
                        }
                        setLuZhiData(bytes);
                    } else if (hex0.equals("aa") && secHex.equals("58") && hex3.equals("85") && hex4.equals("17")) {
                        int size = 10, packetN = 0;
                        byte[] b1 = new byte[size];
                        if (len <= size) {
                            for (int i = 0; i < len; i++) {
                                b1[i] = temp[i];
                            }
                        }
                        int len1 = getAllData(size, len, b1, is);
                        if (len1 == size) {//收到反馈 停止定时器 发出数据后再重新开启 同时记录发包
                            Data.againSendDataTimer.cancel();
                            Data.againSendDataTimer = null;
                            Data.sendDataCount = 0;//清除重发记录数 防止定时器计数错误
                            packetN = Byte.toUnsignedInt(b1[6]) * 256 + Byte.toUnsignedInt(b1[7]);
                            JButton dataWrite = (JButton) MainUi.map.get(Data.DataWriteBtnName);
                            if (packetN == Data.dataWrite.length) {
                                dataWrite.setText("写入控制器");
                                dataWrite.setEnabled(true);
                                Thread.sleep(300);
                                Util.RAMReset();
                            } else {
                                Socket.SerialPortSendData((byte[]) Data.dataWrite[packetN]);
                                Data.sendDataSum = packetN;
                                dataWrite.setText((Data.sendDataSum + 1) + "/" + Data.dataWrite.length);
                                Util.againSendData();
                            }
                        }
                    }
					/*else if (hex0.equals("fd") && hex1.equals("db")) {
						int size = 180;
						int len1 = 0;
						byte[] b1 = new byte[size];
						if (len <= size) {
							for (int i = 0; i < len; i++) {
								b1[i] = temp[i];最近更新
							}
							len1 = getAllData(size, len, b1, is);
						} else {
							for (int i = 0; i < size; i++) {
								b1[i] = temp[i];
							}
							len1 = size;
						}
						if (len1 == size) {
							JLabel label = (JLabel)MainUi.map.get("DeviceLabel");
							int lg = Byte.toUnsignedInt(b1[5]);
							int lg2 = Byte.toUnsignedInt(b1[95]);
							String s = new String(b1, 6, lg);
							String s2 = new String(b1, 96, lg2);
							label.setText("<html>"+s+"<br>"+s2+"</html>");
						}
					}*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            timeBlockPanels.updateUI();
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
        try {
            OutputStream os = Data.serialPort.getOutputStream();
            os.write(buff);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
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
            if(!btn.isEnabled()){
                if(!RdmPaneUI.uidList.contains(uidTemp)) {
                    String[] s = {String.valueOf(table.getRowCount() + 1), uidTemp,
                            String.valueOf(table.getRowCount() + 1), "", "", "", "", "进入高级设置"};
                    model.addRow(s);
                    RdmPaneUI.tempUid_Byte.add(uidByte);
                    RdmPaneUI.tempUidList.add(uidTemp);
                }
            }else {
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
                                OutputStream os = Data.serialPort.getOutputStream();
                                //查工作模式
                                //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 7));
                                //Thread.sleep(200);

                                //正反显示
                                //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 8));
                                os.write(RdmData.serchType(RdmPaneUI.currentByte, 8));
                                os.flush();
                                Thread.sleep(300);

                                //复位
                                //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 9));
                                //Thread.sleep(200);

                                //电动模式
                                //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 11));
                                os.write(RdmData.serchType(RdmPaneUI.currentByte, 11));
                                os.flush();
                                Thread.sleep(300);

                                //通道模式
                                //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType(RdmPaneUI.currentByte, 12));
                                os.write(RdmData.serchType(RdmPaneUI.currentByte, 12));
                                os.flush();
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
                List list = (List) MainUi.map.get("DianJiComponet_list");
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

    private int getAllData2(int size, int len, byte[] bb, InputStream is) throws Exception {
        //byte[] tp = new byte[size];
        byte[] b = new byte[size];
        int len1 = len, len2 = 0;
        while (len1 < size) {
            len2 = is.read(b);
            if (len2 != -1) {
                len1 = len1 + len2;
                if (len1 <= size) {
                    for (int i = len1 - len2; i < len1; i++) {
                        bb[i] = b[i - (len1 - len2)];
                        //tp[i] = b[i-(len1-len2)];
                    }
                } else {
                    //size = size + 90;
                    for (int i = len1 - len2; i < size; i++) {
                        bb[i] = b[i - (len1 - len2)];
                    }
                }
            }
        }
        return len1;
    }

    private int getAllData(int size, int len, byte[] bb, InputStream is) {
        int len1 = len, len2 = 0;
        try {
            byte[] b = new byte[size];
            while (len1 < size) {
                len2 = is.read(b);
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

    private void setChangJing1(byte[] b) {
    }

    private void setChangJing1_1(byte[] b) {
        //8个不可调
        JComboBox[] boxs8 = (JComboBox[]) MainUi.map.get("kaiGuangBox_BuKeTiao");
        int[] aa = new int[8];
        ItemListener listener = null;
        ChangeListener changeListener = null;
        for (int i = 0; i < 8; i++) {
            aa[i] = Byte.toUnsignedInt(b[12 + i]);
            listener = boxs8[i].getItemListeners()[0];
            boxs8[i].removeItemListener(listener);
            if (aa[i] == 10) {
                boxs8[i].setSelectedIndex(0);
            } else if (aa[i] == 11) {
                boxs8[i].setSelectedIndex(1);
            } else if (aa[i] == 12) {
                boxs8[i].setSelectedIndex(2);
            }
            boxs8[i].addItemListener(listener);
        }

        //4个灯的开关亮度
        JComboBox[] boxs = (JComboBox[]) MainUi.map.get("kaiGuangBox");
        JSlider[] sliders = (JSlider[]) MainUi.map.get("liangDuSliders");
        JComboBox[] boxs2 = (JComboBox[]) MainUi.map.get("liangDuBoxs");

        for (int i = 0; i < 4; i++) {
            changeListener = sliders[i].getChangeListeners()[0];
            listener = boxs2[i].getItemListeners()[0];
            sliders[i].removeChangeListener(changeListener);
            boxs2[i].removeItemListener(listener);
            sliders[i].setValue(Byte.toUnsignedInt(b[20 + i]));
            boxs2[i].setSelectedItem(String.valueOf(Byte.toUnsignedInt(b[20 + i])));
            sliders[i].addChangeListener(changeListener);
            boxs2[i].addItemListener(listener);
        }

        int c = 0;
        for (int i = 0; i < 4; i++) {
            c = Byte.toUnsignedInt(b[8 + i]);
            listener = boxs[i].getItemListeners()[0];
            boxs[i].removeItemListener(listener);
            if (c == 10) {
                boxs[i].setSelectedIndex(0);
            } else if (c == 11) {
                boxs[i].setSelectedIndex(1);
            } else if (c == 12) {
                boxs[i].setSelectedIndex(2);
            }
            boxs[i].addItemListener(listener);
        }

        //JComboBox[] boxs2 = (JComboBox[])MainUi.map.get("liangDuBoxs");
        //全局亮度
        JSlider slider = (JSlider) MainUi.map.get("quanJuLiangDuSlider");
        JComboBox box = (JComboBox) MainUi.map.get("quanJuLiangDuBox");
        changeListener = slider.getChangeListeners()[0];
        listener = box.getItemListeners()[0];
        slider.removeChangeListener(changeListener);
        box.removeItemListener(listener);
        slider.setValue(Byte.toUnsignedInt(b[7]));
        box.setSelectedItem(String.valueOf(Byte.toUnsignedInt(b[7])));
        slider.addChangeListener(changeListener);
        box.addItemListener(listener);
        //开关模式
        int a = Byte.toUnsignedInt(b[5]);
        JRadioButton radioButton = (JRadioButton) MainUi.map.get("kaiGuangModelBtn1");
        JRadioButton radioButton2 = (JRadioButton) MainUi.map.get("kaiGuangModelBtn2");
        if (a == 1) {
            radioButton.setSelected(true);
        } else {
            radioButton2.setSelected(true);
        }
        //亮度模式
        a = Byte.toUnsignedInt(b[6]);
        JRadioButton radioButton3 = (JRadioButton) MainUi.map.get("liangDuModelBtn1");
        JRadioButton radioButton4 = (JRadioButton) MainUi.map.get("liangDuModelBtn2");
        if (a == 0) {
            radioButton4.setSelected(true);
        } else {
            radioButton3.setSelected(true);
        }
    }

    private void setChangJing2(byte[] b) {
    }

    private void setChangJing2_1(byte[] b) {
        ItemListener listener = null;
        //运行模式
        JRadioButton radioButton1 = (JRadioButton) MainUi.map.get("yunXingModelBtn1");
        JRadioButton radioButton2 = (JRadioButton) MainUi.map.get("yunXingModelBtn2");
        int temp = Byte.toUnsignedInt(b[20]);
        if (temp == 1) {
            radioButton1.setSelected(true);
        } else if (temp == 2) {
            radioButton2.setSelected(true);
        }
        //声控模式
        JComboBox[] boxs1 = (JComboBox[]) MainUi.map.get("shengKonModelBoxs");
        for (int i = 0; i < 3; i++) {
            temp = Byte.toUnsignedInt(b[17 + i]);
            listener = boxs1[i].getItemListeners()[0];
            boxs1[i].removeItemListener(listener);
            if (temp > 16 || temp < 1) {
                boxs1[i].setSelectedIndex(0);
            } else {
                boxs1[i].setSelectedItem(String.valueOf(temp));
            }
            boxs1[i].addItemListener(listener);
        }
        //场景模式
        JComboBox[] boxs2 = (JComboBox[]) MainUi.map.get("changJingModelBoxs");
        for (int i = 0; i < 3; i++) {
            temp = Byte.toUnsignedInt(b[13 + i]);
            listener = boxs2[i].getItemListeners()[0];
            boxs2[i].removeItemListener(listener);
            if (temp > 24 || temp < 1) {
                boxs2[i].setSelectedIndex(0);
            } else {
                boxs2[i].setSelectedItem(String.valueOf(temp));
            }
            boxs2[i].addItemListener(listener);
        }
        //效果灯开关
        JComboBox box = (JComboBox) MainUi.map.get("xiaoGuoDengKaiGuangBox");
        listener = box.getItemListeners()[0];
        box.removeItemListener(listener);
        temp = Byte.toUnsignedInt(b[12]);
        if (temp == 0) {
            box.setSelectedIndex(1);//关
        } else if (temp == 1) {
            box.setSelectedIndex(0);//开
        } else if (temp == 172) {
            box.setSelectedIndex(2);//保持
        }
        box.addItemListener(listener);
        //摇麦延续开关
        JRadioButton radioButton3 = (JRadioButton) MainUi.map.get("yaoMaiKaiGuangBtn1");
        JRadioButton radioButton4 = (JRadioButton) MainUi.map.get("yaoMaiKaiGuangBtn2");
        temp = Byte.toUnsignedInt(b[11]);
        if (temp == 0) {
            radioButton4.setSelected(true);
        } else if (temp == 1) {
            radioButton3.setSelected(true);
        }
        //摇麦触发间隔
        JComboBox box2 = (JComboBox) MainUi.map.get("yaoMaiJianGeBox");
        temp = Byte.toUnsignedInt(b[10]);
        listener = box2.getItemListeners()[0];
        box2.removeItemListener(listener);
        box2.setSelectedItem(String.valueOf(temp));
        box2.addItemListener(listener);
        //摇麦模式
        JComboBox box3 = (JComboBox) MainUi.map.get("yaoMaiModelBox");
        temp = Byte.toUnsignedInt(b[9]);
        listener = box3.getItemListeners()[0];
        box3.removeItemListener(listener);
        if (temp == 0) {
            box3.setSelectedIndex(0);
        } else if (temp == 1) {
            box3.setSelectedIndex(1);
        } else if (temp == 2) {
            box3.setSelectedIndex(2);
        } else if (temp == 172) {
            box3.setSelectedIndex(3);
        }
        box3.addItemListener(listener);
        //雾机模式
        JComboBox box4 = (JComboBox) MainUi.map.get("wuJiModelBox");
        temp = Byte.toUnsignedInt(b[21]);
        listener = box4.getItemListeners()[0];
        box4.removeItemListener(listener);
        if (temp >= 0 && temp <= 4) {
            box4.setSelectedIndex(temp);
        }
        box4.addItemListener(listener);
    }

    private void setKongTiao(byte[] b) {
    }

    private void setKongTiao_1(byte[] b) {
        ItemListener listener;
        //默认开机
        JRadioButton radioButton1 = (JRadioButton) MainUi.map.get("kongTiaoKaiJiBtn1");
        JRadioButton radioButton2 = (JRadioButton) MainUi.map.get("kongTiaoKaiJiBtn2");
        if (Byte.toUnsignedInt(b[9]) == 1) {
            radioButton1.setSelected(true);
        } else if (Byte.toUnsignedInt(b[9]) == 2) {
            radioButton2.setSelected(true);
        }
        //空调模式
        JComboBox box1 = (JComboBox) MainUi.map.get("kongTiaoModelBox");
        int tt = Byte.toUnsignedInt(b[13]);
        int selected = 0;
        if (tt == 106) {
            selected = 0;
        } else if (tt == 107) {
            selected = 1;
        } else if (tt == 108) {
            selected = 2;
        }
        listener = box1.getItemListeners()[0];
        box1.removeItemListener(listener);
        box1.setSelectedIndex(selected);
        box1.addItemListener(listener);

        //空调档位
        JComboBox box2 = (JComboBox) MainUi.map.get("kongTiaoDangWeiBox");
        tt = Byte.toUnsignedInt(b[14]);
        selected = 0;
        if (tt == 138) {
            selected = 0;
        } else if (tt == 139) {
            selected = 1;
        } else if (tt == 140) {
            selected = 2;
        } else if (tt == 141) {
            selected = 3;
        } else if (tt == 142) {
            selected = 4;
        }
        listener = box2.getItemListeners()[0];
        box2.removeItemListener(listener);
        box2.setSelectedIndex(selected);
        box2.addItemListener(listener);

        //设定温度
        JComboBox box3 = (JComboBox) MainUi.map.get("kongTiaoSetWenDuBox");
        listener = box3.getItemListeners()[0];
        box3.removeItemListener(listener);
        tt = Byte.toUnsignedInt(b[15]);
        box3.setSelectedItem(String.valueOf(tt));
        box3.addItemListener(listener);

        //当前温度
        JComboBox box4 = (JComboBox) MainUi.map.get("kongTiaoNowWenDuBox");
        listener = box4.getItemListeners()[0];
        box4.removeItemListener(listener);
        tt = Byte.toUnsignedInt(b[16]);
        box4.setSelectedItem(String.valueOf(tt));
        box4.addItemListener(listener);

        //排风开关
        JComboBox box5 = (JComboBox) MainUi.map.get("kongTiaoPaiFengBox");
        listener = box5.getItemListeners()[0];
        box5.removeItemListener(listener);
        tt = Byte.toUnsignedInt(b[17]);
        if (tt == 80) {
            box5.setSelectedIndex(0);
        } else if (tt == 81) {
            box5.setSelectedIndex(1);
        }
        box5.addItemListener(listener);

        //风机类型
        JComboBox box6 = (JComboBox) MainUi.map.get("kongTiaoFengJiTypeBox");
        listener = box6.getItemListeners()[0];
        box6.removeItemListener(listener);
        tt = Byte.toUnsignedInt(b[8]);
        box6.setSelectedIndex(tt - 1);
        box6.addItemListener(listener);

        //墙板温度显示
        JRadioButton radioButton3 = (JRadioButton) MainUi.map.get("kongTiaoShowWenDuBtn1");
        JRadioButton radioButton4 = (JRadioButton) MainUi.map.get("kongTiaoShowWenDuBtn2");
        tt = Byte.toUnsignedInt(b[11]);
        if (tt == 1) {
            radioButton3.setSelected(true);
        } else if (tt == 2) {
            radioButton4.setSelected(true);
        }

        //中央空调模式
        JComboBox box7 = (JComboBox) MainUi.map.get("kongTiaoCenterModelBox");
        listener = box7.getItemListeners()[0];
        box7.removeItemListener(listener);
        tt = Byte.toUnsignedInt(b[10]);
        box7.setSelectedIndex(tt - 1);
        box7.addItemListener(listener);

        //空调RS485地址
        JComboBox box8 = (JComboBox) MainUi.map.get("kongTiaoRS485AddBox");
        listener = box8.getItemListeners()[0];
        box8.removeItemListener(listener);
        tt = Byte.toUnsignedInt(b[12]);
        box8.setSelectedIndex(tt - 1);
        box8.addItemListener(listener);
    }

    //private void showKongTiaoCtrl(byte[] b){}
    private void showKongTiaoCtrl(byte[] b) {
        List list = (List) MainUi.map.get("ctrlAirCompone");
        JRadioButton radioButton = (JRadioButton) list.get(0);
        JRadioButton radioButton2 = (JRadioButton) list.get(1);
        JRadioButton radioButton3 = (JRadioButton) list.get(2);
        int tt = Byte.toUnsignedInt(b[3]);
        if (tt == 106) {
            radioButton.setSelected(true);
        } else if (tt == 107) {
            radioButton2.setSelected(true);
        } else if (tt == 108) {
            radioButton3.setSelected(true);
        }

        JRadioButton radioButton4 = (JRadioButton) list.get(3);
        JRadioButton radioButton5 = (JRadioButton) list.get(4);
        JRadioButton radioButton6 = (JRadioButton) list.get(5);
        JRadioButton radioButton7 = (JRadioButton) list.get(6);
        JRadioButton radioButton8 = (JRadioButton) list.get(7);
        tt = Byte.toUnsignedInt(b[4]);
        if (tt == 138) {
            radioButton4.setSelected(true);
        } else if (tt == 139) {
            radioButton5.setSelected(true);
        } else if (tt == 140) {
            radioButton6.setSelected(true);
        } else if (tt == 141) {
            radioButton7.setSelected(true);
        } else if (tt == 142) {
            radioButton8.setSelected(true);
        }

        JRadioButton radioButton9 = (JRadioButton) list.get(8);
        JRadioButton radioButton10 = (JRadioButton) list.get(9);
        tt = Byte.toUnsignedInt(b[7]);
        if (tt == 80) {
            radioButton9.setSelected(true);
        } else if (tt == 81) {
            radioButton10.setSelected(true);
        }

        JComboBox box = (JComboBox) list.get(10);
        ItemListener listener = box.getItemListeners()[0];
        box.removeItemListener(listener);
        tt = Byte.toUnsignedInt(b[6]);
        box.setSelectedItem("" + tt);
        box.addItemListener(listener);

        JLabel label = (JLabel) list.get(11);
        tt = Byte.toUnsignedInt(b[5]);
        if (tt > 36) {
            tt = 36;
        }
        label.setText(tt + "");
    }

    private void setQuanJu(byte[] b) {
    }

    private void setQuanJu_1(byte[] b) {
        List list = (List) MainUi.map.get("quanJuComponeList");
        int cc = 0;
        ItemListener listener = null;

        JComboBox box = (JComboBox) list.get(0);
        listener = box.getItemListeners()[0];
        box.removeItemListener(listener);
        cc = Byte.toUnsignedInt(b[6]);
        if (cc == 72) {
            box.setSelectedIndex(0);
        } else if (cc == 150) {
            box.setSelectedIndex(1);
        }
        box.addItemListener(listener);

        JComboBox box2 = (JComboBox) list.get(1);
        listener = box2.getItemListeners()[0];
        box2.removeItemListener(listener);
        cc = Byte.toUnsignedInt(b[7]) - 1;
        box2.setSelectedIndex(cc);
        box2.addItemListener(listener);

        JComboBox box3 = (JComboBox) list.get(2);
        listener = box3.getItemListeners()[0];
        box3.removeItemListener(listener);
        cc = Byte.toUnsignedInt(b[8]);
        box3.setSelectedIndex(cc);
        box3.addItemListener(listener);

        JComboBox box4 = (JComboBox) list.get(3);
        listener = box4.getItemListeners()[0];
        box4.removeItemListener(listener);
        cc = Byte.toUnsignedInt(b[9]);
        box4.setSelectedIndex(cc);
        box4.addItemListener(listener);

        JRadioButton radioButton = (JRadioButton) list.get(4);
        JRadioButton radioButton2 = (JRadioButton) list.get(5);
        cc = Byte.toUnsignedInt(b[18]);
        if (cc == 0) {
            radioButton.setSelected(true);
        } else if (cc == 1) {
            radioButton2.setSelected(true);
        }

        JRadioButton radioButton3 = (JRadioButton) list.get(6);
        JRadioButton radioButton4 = (JRadioButton) list.get(7);
        cc = Byte.toUnsignedInt(b[10]);
        if (cc == 0) {
            radioButton3.setSelected(true);
        } else if (cc == 1) {
            radioButton4.setSelected(true);
        }

        JRadioButton radioButton5 = (JRadioButton) list.get(8);
        JRadioButton radioButton6 = (JRadioButton) list.get(9);
        cc = Byte.toUnsignedInt(b[11]);
        if (cc == 0) {
            radioButton5.setSelected(true);
        } else if (cc == 1) {
            radioButton6.setSelected(true);
        }
    }

    private void setZhongKong1(byte[] b) {
    }

    private void setZhongKong1_1(byte[] b) {

        //红外码
        JTextField[] fields = (JTextField[]) MainUi.map.get("redCodeFields");
        String[] t = new String[4];
        int cc = 0;
        for (int i = 0; i < t.length; i++) {
            t[i] = Integer.toHexString(b[6 + i] & 0xFF);
            if ("ff".equals(t[i])) {
                cc++;
            }
        }
        if (cc != 4) {
            for (int i = 0; i < t.length; i++) {
                if (t[i].length() < 2) {
                    fields[i].setText("0" + t[i].toUpperCase());
                } else {
                    fields[i].setText(t[i].toUpperCase());
                }
            }
        } else {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setText("");
            }
        }


        //串口墙板
        JTextField[] fields2 = (JTextField[]) MainUi.map.get("comQianBanFields1");
        JTextField[] fields3 = (JTextField[]) MainUi.map.get("comQianBanFields2");
        JTextField[] fields4 = (JTextField[]) MainUi.map.get("comQianBanFields3");
        JTextField[] fields5 = (JTextField[]) MainUi.map.get("comQianBanFields4");
        int a1 = Byte.toUnsignedInt(b[10]);
        int a2 = Byte.toUnsignedInt(b[16]);
        int a3 = Byte.toUnsignedInt(b[22]);
        int a4 = Byte.toUnsignedInt(b[28]);
        int tp;
        if (a1 != 0) {
            for (int i = 0; i < a1; i++) {
                tp = Byte.toUnsignedInt(b[11 + i]);
                if (tp < 16) {
                    fields2[i].setText("0" + Integer.toHexString(tp).toUpperCase());
                } else {
                    fields2[i].setText(Integer.toHexString(tp).toUpperCase());
                }
            }
            for (int i = a1; i < fields2.length; i++) {
                fields2[i].setText("");
            }
        } else {
            for (int i = 0; i < fields2.length; i++) {
                fields2[i].setText("");
            }
        }
        if (a2 != 0) {
            for (int i = 0; i < a2; i++) {
                tp = Byte.toUnsignedInt(b[17 + i]);
                if (tp < 16) {
                    fields3[i].setText("0" + Integer.toHexString(tp).toUpperCase());
                } else {
                    fields3[i].setText(Integer.toHexString(tp).toUpperCase());
                }
            }
            for (int i = a2; i < fields3.length; i++) {
                fields3[i].setText("");
            }
        } else {
            for (int i = 0; i < fields3.length; i++) {
                fields3[i].setText("");
            }
        }
        if (a3 != 0) {
            for (int i = 0; i < a3; i++) {
                tp = Byte.toUnsignedInt(b[23 + i]);
                if (tp < 16) {
                    fields4[i].setText("0" + Integer.toHexString(tp).toUpperCase());
                } else {
                    fields4[i].setText(Integer.toHexString(tp).toUpperCase());
                }
            }
            for (int i = a3; i < fields4.length; i++) {
                fields4[i].setText("");
            }
        } else {
            for (int i = 0; i < fields4.length; i++) {
                fields4[i].setText("");
            }
        }
        if (a4 != 0) {
            for (int i = 0; i < a4; i++) {
                tp = Byte.toUnsignedInt(b[29 + i]);
                if (tp < 16) {
                    fields5[i].setText("0" + Integer.toHexString(tp).toUpperCase());
                } else {
                    fields5[i].setText(Integer.toHexString(tp).toUpperCase());
                }
            }
            for (int i = a4; i < fields5.length; i++) {
                fields5[i].setText("");
            }
        } else {
            for (int i = 0; i < fields5.length; i++) {
                fields5[i].setText("");
            }
        }

        //串口上行
        JTextField[] fields6 = (JTextField[]) MainUi.map.get("comShangXingFields");
        //串口下行
        JTextField[] fields7 = (JTextField[]) MainUi.map.get("comXiaXingFields");
        a1 = Byte.toUnsignedInt(b[34]);
        a2 = Byte.toUnsignedInt(b[47]);
        if (a1 != 0) {
            for (int i = 0; i < a1; i++) {
                tp = Byte.toUnsignedInt(b[35 + i]);
                if (tp < 16) {
                    fields6[i].setText("0" + Integer.toHexString(tp).toUpperCase());
                } else {
                    fields6[i].setText(Integer.toHexString(tp).toUpperCase());
                }
            }
            for (int i = a1; i < fields6.length; i++) {
                fields6[i].setText("");
            }
        } else {
            for (int i = 0; i < fields6.length; i++) {
                fields6[i].setText("");
            }
        }
        if (a2 != 0) {
            for (int i = 0; i < a2; i++) {
                tp = Byte.toUnsignedInt(b[48 + i]);
                if (tp < 16) {
                    fields7[i].setText("0" + Integer.toHexString(tp).toUpperCase());
                } else {
                    fields7[i].setText(Integer.toHexString(tp).toUpperCase());
                }
            }
            for (int i = a2; i < fields7.length; i++) {
                fields7[i].setText("");
            }
        } else {
            for (int i = 0; i < fields7.length; i++) {
                fields7[i].setText("");
            }
        }

        //喝彩
        JComboBox box2 = (JComboBox) MainUi.map.get("heCaiBox");
        JComboBox box3 = (JComboBox) MainUi.map.get("daoCaiBox");
        ItemListener listener = box2.getItemListeners()[0];
        ItemListener listener2 = box3.getItemListeners()[0];
        box2.removeItemListener(listener);
        box3.removeItemListener(listener2);
        a1 = Byte.toUnsignedInt(b[60]);
        a2 = Byte.toUnsignedInt(b[61]);
        box2.setSelectedIndex(a1);
        box3.setSelectedIndex(a2);
        box2.addItemListener(listener);
        box3.addItemListener(listener2);
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

    private void setZhongKong2(byte[] b) {
    }

    private void setZhongKong2_1(byte[] b) {
        List list = (List) MainUi.map.get("settingZhongKongListCompone");
        JRadioButton radioButton = (JRadioButton) list.get(0);
        JRadioButton radioButton2 = (JRadioButton) list.get(1);
        int tp = Byte.toUnsignedInt(b[6]);
        if (tp == 0) {
            radioButton.setSelected(true);
        } else {
            radioButton2.setSelected(true);
        }

        JRadioButton radioButton3 = (JRadioButton) list.get(2);
        JRadioButton radioButton4 = (JRadioButton) list.get(3);
        tp = Byte.toUnsignedInt(b[7]);
        if (tp == 1) {
            radioButton3.setSelected(true);
        } else {
            radioButton4.setSelected(true);
        }

        JRadioButton radioButton5 = (JRadioButton) list.get(4);
        JRadioButton radioButton6 = (JRadioButton) list.get(5);
        tp = Byte.toUnsignedInt(b[8]);
        if (tp == 1) {
            radioButton5.setSelected(true);
        } else {
            radioButton6.setSelected(true);
        }

        JRadioButton radioButton7 = (JRadioButton) list.get(6);
        JRadioButton radioButton8 = (JRadioButton) list.get(7);
        tp = Byte.toUnsignedInt(b[9]);
        if (tp == 0) {
            radioButton7.setSelected(true);
        } else {
            radioButton8.setSelected(true);
        }

        JSlider slider = (JSlider) list.get(8);
        JComboBox box = (JComboBox) list.get(9);
        slider.setValue(Byte.toUnsignedInt(b[10]));
    }

    private void setTiaoGuang(byte[] b, int cj, String s) {
    }

    private void setTiaoGuang_1(byte[] b, int cj, String s) {
        //调光设置
        ArrayList list = (ArrayList) MainUi.map.get("setTiaoGuangCpns");
        if (list == null) {
            return;
        }
        if (cj == 25) {//可调、下限
            JComboBox box = (JComboBox) list.get(0);
            JSlider slider = (JSlider) list.get(2);

            JComboBox box2 = (JComboBox) list.get(3);
            JSlider slider2 = (JSlider) list.get(5);

            JComboBox box3 = (JComboBox) list.get(6);
            JSlider slider3 = (JSlider) list.get(8);

            JComboBox box4 = (JComboBox) list.get(9);
            JSlider slider4 = (JSlider) list.get(11);

            if (box == null || slider == null || box2 == null || slider2 == null
                    || box3 == null || slider3 == null || box4 == null || slider4 == null) {
                return;
            }

            ItemListener listener = box.getItemListeners()[0];
            ItemListener listener2 = box2.getItemListeners()[0];
            ItemListener listener3 = box3.getItemListeners()[0];
            ItemListener listener4 = box4.getItemListeners()[0];
            box.removeItemListener(listener);
            box2.removeItemListener(listener2);
            box3.removeItemListener(listener3);
            box4.removeItemListener(listener4);
            if (Byte.toUnsignedInt(b[8]) == 10) {
                box.setSelectedIndex(0);
            } else if (Byte.toUnsignedInt(b[8]) == 11) {
                box.setSelectedIndex(1);
            }
            if (Byte.toUnsignedInt(b[9]) == 10) {
                box2.setSelectedIndex(0);
            } else if (Byte.toUnsignedInt(b[9]) == 11) {
                box2.setSelectedIndex(1);
            }
            if (Byte.toUnsignedInt(b[10]) == 10) {
                box3.setSelectedIndex(0);
            } else if (Byte.toUnsignedInt(b[10]) == 11) {
                box3.setSelectedIndex(1);
            }
            if (Byte.toUnsignedInt(b[11]) == 10) {
                box4.setSelectedIndex(0);
            } else if (Byte.toUnsignedInt(b[11]) == 11) {
                box4.setSelectedIndex(1);
            }
            box.addItemListener(listener);
            box2.addItemListener(listener2);
            box3.addItemListener(listener3);
            box4.addItemListener(listener4);
            slider.setValue(Byte.toUnsignedInt(b[20]));
            slider2.setValue(Byte.toUnsignedInt(b[21]));
            slider3.setValue(Byte.toUnsignedInt(b[22]));
            slider4.setValue(Byte.toUnsignedInt(b[23]));
            if ("31".equals(s)) {
                if (Data.serialPort != null) {
                    try {
                        OutputStream os = Data.serialPort.getOutputStream();
                        os.write(ZhiLingJi.changJingChaXun(26));
                        os.flush();
                        os.close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        } else if (cj == 26) {//上限
            JSlider slider3 = (JSlider) list.get(1);
            JSlider slider4 = (JSlider) list.get(4);
            JSlider slider5 = (JSlider) list.get(7);
            JSlider slider6 = (JSlider) list.get(10);
            if (slider3 == null || slider4 == null || slider5 == null || slider6 == null) {
                return;
            }
            slider3.setValue(Byte.toUnsignedInt(b[20]));
            slider4.setValue(Byte.toUnsignedInt(b[21]));
            slider5.setValue(Byte.toUnsignedInt(b[22]));
            slider6.setValue(Byte.toUnsignedInt(b[23]));
        }
    }
}
