package com.boray.beiFen.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.URLDecoder;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.beiFen.UI.JingDuUI;
import com.boray.mainUi.MainUi;

public class LoadToDeviceActionListener implements ActionListener {
    public static Timer timer;
    public static byte[][] temp;
    private static int type;
    private static int pktN = 1;
    private static int reSendCnt = 0;

    public void actionPerformed(ActionEvent e) {
        if (Data.serialPort == null && Data.socket == null) {
            JFrame frame = (JFrame) MainUi.map.get("frame");
            JOptionPane.showMessageDialog(frame, "请先连接设备！", "提示", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
//        try {
//            String path = getClass().getResource("/SD卡文件/").getPath().substring(1);
//            path = URLDecoder.decode(path, "utf-8");
//            fileChooser.setCurrentDirectory(new File(path));
//        } catch (Exception e2) {
//            e2.printStackTrace();
//        }
        if (!Data.saveCtrlFilePath.equals("")) {
            fileChooser.setCurrentDirectory(new File(Data.saveCtrlFilePath));
        }
        String[] houZhui = {"dat", "bry"};
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.dat,*.bry", houZhui);
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showOpenDialog((JFrame) MainUi.map.get("frame"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                sendCodeToDevice(file);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


    private void sendCodeToDevice(File file) {
        try {
            FileInputStream is = new FileInputStream(file);
            List list = (List) MainUi.map.get("LoadSetListComponent");
            if (file.length() >= 57344) {
                temp = new byte[14][4096];
                for (int i = 0; i < 14; i++) {
                    is.read(temp[i]);
                }
                is.close();
                new Thread(new Runnable() {
                    public void run() {
                        new JingDuUI().show();
                    }
                }).start();
                JRadioButton radioButton = (JRadioButton) list.get(0);
                JRadioButton radioButton2 = (JRadioButton) list.get(1);
                JRadioButton radioButton3 = (JRadioButton) list.get(2);
                JRadioButton radioButton4 = (JRadioButton) list.get(3);
                if (radioButton.isSelected()) {//灯光
                    type = 1;
                    //A部分  0-4扇区

                    //B部分 7-11扇区
                    reSend(1);
                }
                if (radioButton2.isSelected()) {//空调  12-13扇区
                    type = 2;
                    reSend(1);
                }
                if (radioButton3.isSelected()) {//中控  5-6扇区
                    type = 3;
                    reSend(1);
                }
                if (radioButton4.isSelected()) {//全部
                    type = 16;
                    reSend(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static byte[] getDataByPacket(int PcktNum) {
        byte[] tp = new byte[512];
        int start, sq;

        sq = (PcktNum - 1) / 8;//扇区
        start = ((PcktNum - 1) % 8) * 512;//起始地址
        if (type == 1) {//灯光
            //A部分  0-4扇区
            if (PcktNum <= 40) {
                System.arraycopy(temp[sq], start, tp, 0, 512);
            } else {
                //B部分 7-11扇区
                if (PcktNum == 41) {
                    System.out.println("B部分");
                    PcktNum = 1;
                    sq = 7;
                    start = 0;
                    System.arraycopy(temp[sq], start, tp, 0, 512);
                    type = 6;
                }
            }
        } else if (type == 2) {//空调  12-13扇区
            sq = sq + 12;
            System.arraycopy(temp[sq], start, tp, 0, 512);
        } else if (type == 3) {//中控  5-6扇区
            if (PcktNum <= 16) {
                sq = sq + 5;
                System.arraycopy(temp[sq], start, tp, 0, 512);
            } else {
                if (PcktNum == 17) {
                    PcktNum = 1;
                    //sq = 2;
                    start = 1536;
                    System.arraycopy(temp[2], start, tp, 0, 512);
                    type = 5;
                }
            }
        } else if (type == 5) {
            if (PcktNum == 2) {
                sq = 2;
                start = 2048;
            } else if (PcktNum == 3) {
                sq = 2;
                start = 2560;
            } else if (PcktNum == 4) {
                sq = 2;
                start = 3072;
            } else if (PcktNum == 5) {
                sq = 2;
                start = 3584;
            } else {
                sq = ((PcktNum - 6) / 8) + 3;//扇区
                start = ((PcktNum - 6) % 8) * 512;//起始地址
            }
            System.arraycopy(temp[sq], start, tp, 0, 512);
        } else if (type == 6) {//灯光  B部分 7-11扇区
            sq = sq + 7;
            System.arraycopy(temp[sq], start, tp, 0, 512);
        } else if (type == 16) {
            System.arraycopy(temp[sq], start, tp, 0, 512);
        }
        pktN = PcktNum;
        return tp;
    }

    public static void reSend(final int PcktNum) {
        reSendCnt = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    if (reSendCnt == 5) {
                        JDialog dialog = (JDialog) MainUi.map.get("JingDuDialog");
                        dialog.dispose();
                        JFrame frame = (JFrame) MainUi.map.get("frame");
                        JOptionPane.showMessageDialog(frame, "加载数据失败！", "提示", JOptionPane.ERROR_MESSAGE);
                        timer.cancel();
                    } else {
                        if (Data.serialPort != null) {
                            OutputStream os = Data.serialPort.getOutputStream();

						/*for (int i = 0; i < 527; i++) {
							os.write(ZhiLingJi.packetData(getDataByPacket(PcktNum), type, pktN)[i]);
						}*/
                            os.write(ZhiLingJi.packetData(getDataByPacket(PcktNum), type, pktN), 0, 527);
                            //os.close();
                            //os.flush();
                        } else if (Data.socket != null) {
                            DatagramPacket packet = new DatagramPacket(ZhiLingJi.packetData(getDataByPacket(PcktNum), type, pktN), 0, 527);
                            packet.setSocketAddress(Data.address);
                            Data.socket.send(packet);
                        }
                        reSendCnt = reSendCnt + 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        }, 1000);
    }

    public static void reSend2(final int PcktNum) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    OutputStream os = Data.serialPort.getOutputStream();
                    os.write(ZhiLingJi.packetData(getDataByPacket(PcktNum), type, pktN));
                    os.flush();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            try {
                                OutputStream os = Data.serialPort.getOutputStream();
                                os.write(ZhiLingJi.packetData(getDataByPacket(PcktNum), type, pktN));
                                os.flush();
                                timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    public void run() {
                                        try {
                                            OutputStream os = Data.serialPort.getOutputStream();
                                            os.write(ZhiLingJi.packetData(getDataByPacket(PcktNum), type, pktN));
                                            os.flush();
                                            timer = new Timer();
                                            timer.schedule(new TimerTask() {
                                                public void run() {
                                                    try {
                                                        OutputStream os = Data.serialPort.getOutputStream();
                                                        os.write(ZhiLingJi.packetData(getDataByPacket(PcktNum), type, pktN));
                                                        os.flush();
                                                        timer = new Timer();
                                                        timer.schedule(new TimerTask() {
                                                            public void run() {
                                                                JDialog dialog = (JDialog) MainUi.map.get("JingDuDialog");
                                                                dialog.dispose();
                                                                JFrame frame = (JFrame) MainUi.map.get("frame");
                                                                JOptionPane.showMessageDialog(frame, "加载数据失败！", "提示", JOptionPane.ERROR_MESSAGE);
                                                            }
                                                        }, 1000);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }, 1000);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0);
    }
}
