package com.boray.xiaoGuoDeng.Listener;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;

public class TranscribeListener implements ActionListener {

    private String model;
    private boolean first = true;

    public TranscribeListener(String model) {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String code = ((JButton) e.getSource()).getText();
        boolean flag = Data.serialPort != null;
        if (code.equals("����")) {
            play(flag);
        } else if (code.equals("ֹͣ")) {
            stop(flag);
        } else if (code.equals("ɾ��")) {
            delete(flag);
        } else if (code.equals("��ʼ¼��")) {
            if (first) {
                Object[] options = {"��", "��"};
                int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "����ѧϰ������ԭ��¼�����ݣ��Ƿ������", "����",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, options, options[1]);
                if(yes==1){
                    transcribeStart(flag);
                    first = false;
                }
            } else {
                transcribeStart(flag);
            }
        } else if (code.equals("ֹͣ¼��")) {
            transcribeStop(flag);
        }
    }

    /**
     * ���Ͳ�������
     *
     * @param flag
     */
    private void play(boolean flag) {
        if (flag) {
            byte[] buff = new byte[20];
            buff[0] = (byte) 0xFA;
            buff[1] = (byte) 0x14;
            buff[2] = (byte) 0x61;
            buff[3] = ZhiLingJi.TYPE;
            buff[5] = (byte) 0x02;
            buff[7] = (byte) Integer.parseInt(model);
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
    }

    /**
     * ����ֹͣ����
     *
     * @param flag
     */
    private void stop(boolean flag) {
        if (flag) {
            byte[] buff = new byte[20];
            buff[0] = (byte) 0xFA;
            buff[1] = (byte) 0x14;
            buff[2] = (byte) 0x61;
            buff[3] = ZhiLingJi.TYPE;
            buff[5] = (byte) 0x03;
            buff[7] = (byte) Integer.parseInt(model);
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
    }

    /**
     * ����ɾ������
     *
     * @param flag
     */
    private void delete(boolean flag) {
        if (flag) {
            byte[] buff = new byte[20];
            buff[0] = (byte) 0xFA;
            buff[1] = (byte) 0x14;
            buff[2] = (byte) 0x61;
            buff[3] = ZhiLingJi.TYPE;
            buff[5] = (byte) 0x04;
            buff[7] = (byte) Integer.parseInt(model);
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
    }

    /**
     * ���Ϳ�ʼ¼������
     *
     * @param flag
     */
    private void transcribeStart(boolean flag) {
        if (flag) {
            byte[] buff = new byte[20];
            buff[0] = (byte) 0xFA;
            buff[1] = (byte) 0x14;
            buff[2] = (byte) 0x61;
            buff[3] = ZhiLingJi.TYPE;
            buff[5] = (byte) 0x9E;
            buff[7] = (byte) Integer.parseInt(model);
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
    }

    /**
     * ����ֹͣ¼������
     *
     * @param flag
     */
    private void transcribeStop(boolean flag) {
        if (flag) {
            byte[] buff = new byte[20];
            buff[0] = (byte) 0xFA;
            buff[1] = (byte) 0x14;
            buff[2] = (byte) 0x61;
            buff[3] = ZhiLingJi.TYPE;
            buff[5] = (byte) 0x9F;
            buff[7] = (byte) Integer.parseInt(model);
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
    }
}