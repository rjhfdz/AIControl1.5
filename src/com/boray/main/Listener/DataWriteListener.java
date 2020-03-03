package com.boray.main.Listener;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.Socket;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DataWriteListener implements ActionListener {

    private JButton dataWrite;

    public DataWriteListener(JButton dataWrite) {
        this.dataWrite = dataWrite;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JFrame frame = (JFrame) MainUi.map.get("frame");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Data.serialPort != null) {
                    if (Data.file != null) {
                        dataWrite.setEnabled(false);
                        try {
                            Object[] objects = DataPack(4096);
                            OutputStream os = Data.serialPort.getOutputStream();
                            int j = 0;
                            for (int i = 0; i < objects.length; i++) {
                                if (j == 0)
                                    dataWrite.setText("д����");
                                else if (j == 1)
                                    dataWrite.setText("д����.");
                                else if (j == 2)
                                    dataWrite.setText("д����..");
                                else if (j == 3)
                                    dataWrite.setText("д����...");
                                j++;
                                if (j > 3)
                                    j = 0;
                                byte[] b = (byte[]) objects[i];
                                os.write(b);
                                os.flush();
                                Thread.sleep(200);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            dataWrite.setEnabled(true);
                            dataWrite.setText("д�������");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "�������ɳ�ʼ�汾�Ŀ������ļ����뵽���������ٽ���д�룡", "��ʾ", JOptionPane.ERROR_MESSAGE);
                    }
                } else if (Data.socket != null) {
                    if (Data.file != null) {
                        dataWrite.setEnabled(false);
                        try {
                            Object[] objects = DataPack(1280);
                            int j = 0;
                            for (int i = 0; i < objects.length; i++) {
                                if (j == 0)
                                    dataWrite.setText("д����");
                                else if (j == 1)
                                    dataWrite.setText("д����.");
                                else if (j == 2)
                                    dataWrite.setText("д����..");
                                else if (j == 3)
                                    dataWrite.setText("д����...");
                                j++;
                                if (j > 3)
                                    j = 0;
                                byte[] b = (byte[]) objects[i];
                                Socket.UDPSendData(b);
                                Thread.sleep(300);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        } finally {
                            dataWrite.setEnabled(true);
                            dataWrite.setText("д�������");
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "�������ɳ�ʼ�汾�Ŀ������ļ����뵽���������ٽ���д�룡", "��ʾ", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "���ڻ�����δ���ӣ������Ӵ��ں��ٽ���д�룡", "��ʾ", JOptionPane.ERROR_MESSAGE);
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
     * �������
     *
     * @return
     */
    public byte[] readFileData() throws IOException {
//        if(Data.file==null)
        long fileSize = Data.file.length();
        FileInputStream stream = new FileInputStream(Data.file);
        byte[] buff = new byte[(int) fileSize];
        stream.read(buff);
        stream.close();
        return buff;
    }
}