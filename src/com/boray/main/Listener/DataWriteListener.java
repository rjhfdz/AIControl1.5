package com.boray.main.Listener;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.Socket;
import com.boray.Utils.Util;
import com.boray.beiFen.Listener.MergeAllListener;
import com.boray.beiFen.Listener.ProjectCreateFileActionListener;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DataWriteListener implements ActionListener {

    private JButton dataWrite;

    public DataWriteListener(JButton dataWrite) {
        this.dataWrite = dataWrite;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        this.DataWriteBtnName = dataWrite.getName();
        Data.DataWriteBtnName = dataWrite.getName();
        JFrame frame = (JFrame) MainUi.map.get("frame");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Data.serialPort != null) {
//                    if (Data.file != null) {
                    dataWrite.setEnabled(false);
                    try {
                        Data.dataWrite = DataPack(4096);
                        //发起第一包后 线程监听
                        Socket.SerialPortSendData((byte[]) Data.dataWrite[0]);
                        Data.sendDataSum = 0;//记录发包
                        dataWrite.setText((Data.sendDataSum + 1) + "/" + Data.dataWrite.length);
                        Util.againSendData();//开启定时器 未收到反馈时，重发数据
//                            Object[] objects = DataPack(4096);
//                            OutputStream os = Data.serialPort.getOutputStream();
//                            int j = 0;
//                            for (int i = 0; i < objects.length; i++) {
//                                if (j == 0)
//                                    dataWrite.setText("写入中");
//                                else if (j == 1)
//                                    dataWrite.setText("写入中.");
//                                else if (j == 2)
//                                    dataWrite.setText("写入中..");
//                                else if (j == 3)
//                                    dataWrite.setText("写入中...");
//                                j++;
//                                if (j > 3)
//                                    j = 0;
//                                byte[] b = (byte[]) objects[i];
//                                os.write(b);
//                                os.flush();
//                                Thread.sleep(200);
//                            }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
//                    } else {
//                        JOptionPane.showMessageDialog(frame, "请先生成初始版本的控制器文件导入到控制器，再进行写入！", "提示", JOptionPane.ERROR_MESSAGE);
//                    }
                } else if (Data.socket != null) {
//                    if (Data.file != null) {
                    dataWrite.setEnabled(false);
                    try {
                        Data.dataWrite = DataPack(1280);
                        //发起第一包后 线程监听
                        Socket.UDPSendData((byte[]) Data.dataWrite[0]);
                        Data.sendDataSum = 0;//记录发包
                        dataWrite.setText((Data.sendDataSum + 1) + "/" + Data.dataWrite.length);
                        Util.againSendData();//开启定时器 未收到反馈时，重发数据
//                            int j = 0;
//                            for (int i = 0; i < objects.length; i++) {
//                                if (j == 0)
//                                    dataWrite.setText("写入中");
//                                else if (j == 1)
//                                    dataWrite.setText("写入中.");
//                                else if (j == 2)
//                                    dataWrite.setText("写入中..");
//                                else if (j == 3)
//                                    dataWrite.setText("写入中...");
//                                j++;
//                                if (j > 3)
//                                    j = 0;
//                                byte[] b = (byte[]) objects[i];
//                                Socket.UDPSendData(b);
//                                Thread.sleep(300);
//                            }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
//                    } else {
//                        JOptionPane.showMessageDialog(frame, "请先生成初始版本的控制器文件导入到控制器，再进行写入！", "提示", JOptionPane.ERROR_MESSAGE);
//                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "串口或网络未连接，请连接串口后，再进行写入！", "提示", JOptionPane.ERROR_MESSAGE);
                }
            }
        }).start();
    }

    public Object[] DataPack(int length) throws IOException {
        byte[] buff = readFileData();
        int packet = (buff.length - 9) / length + 1;
        int lastSize = (buff.length - 9) % length;
        int dataLength = length + 9;
        Object[] temp = new Object[packet];
        for (int i = 0; i < packet; i++) {
            if (i != (packet - 1)) {
                byte[] tp1 = new byte[dataLength];
                tp1[0] = (byte) 0xBB;
                tp1[1] = (byte) 0x58;
                tp1[2] = (byte) (dataLength / 256);
                tp1[3] = (byte) (dataLength % 256);
                tp1[4] = (byte) 0x85;
                tp1[5] = (byte) 0x17;
                tp1[6] = (byte) ((i + 1) / 256);
                tp1[7] = (byte) ((i + 1) % 256);
                System.arraycopy(buff, i * length, tp1, 8, length);
                tp1[dataLength - 1] = ZhiLingJi.getJiaoYan(tp1);
                temp[i] = tp1;
            } else {
                byte[] tp2 = new byte[dataLength];
                tp2[0] = (byte) 0xBB;
                tp2[1] = (byte) 0x58;
                tp2[2] = (byte) ((lastSize + 9) / 256);
                tp2[3] = (byte) ((lastSize + 9) % 256);
                tp2[4] = (byte) 0x85;
                tp2[5] = (byte) 0x17;
                tp2[6] = (byte) ((i + 1) / 256);
                tp2[7] = (byte) ((i + 1) % 256);
                System.arraycopy(buff, i * length, tp2, 8, lastSize);
                tp2[dataLength - 1] = ZhiLingJi.getJiaoYan(tp2);
                temp[i] = tp2;
            }
        }
        return temp;
    }

    /**
     * 获得数据
     *
     * @return
     */
    public byte[] readFileData() throws IOException {
//        if (Data.file == null) {

        if (DataWriteBtnName.equals("BeiFenComAndWifiDataWrite")) {
            Data.file = new File(Data.saveCtrlFile);
        } else {
            String path = System.getProperty("user.dir");//获得程序当前文件夹目录
            File file = new File(path + "\\F0.DAT");
            File file1 = new File(path + "\\K0.DAT");
            Data.file = file;
            MergeAllListener listener = new MergeAllListener();
            listener.DataWrite(file, file1);
            ProjectCreateFileActionListener fileOfCloseFrame = new ProjectCreateFileActionListener();
            fileOfCloseFrame.tt(new File(path + "\\project.xml"));
        }
//        }
        long fileSize = Data.file.length();
        FileInputStream stream = new FileInputStream(Data.file);
        byte[] buff = new byte[(int) fileSize];
        stream.read(buff);
        stream.close();
        return buff;
    }
}
