package com.boray.xiaoGuoDeng.reviewBlock;

import java.io.OutputStream;

import com.boray.Data.Data;
import com.boray.Utils.Socket;

import javax.swing.*;

public class TimeBlockReviewActionListener {
    private int sc, group, block, index, denKuNum, suCaiNum;

    private JComboBox comboBox;

    public TimeBlockReviewActionListener(int sc, int group, int block, int index) {
        this.sc = sc;
        this.block = block;
        this.group = group;
        this.index = index;
    }

    public TimeBlockReviewActionListener(JComboBox comboBox,int denKuNum, int suCaiNum) {
        this.denKuNum = denKuNum;
        this.suCaiNum = suCaiNum;
        this.comboBox = comboBox;
    }

    public void actionPerformed() {
        if (Data.serialPort != null) {
            try {

                //�ƾ�����
                byte[] b = TimeBlockReviewData.getInfOfLight();
                OutputStream os = Data.serialPort.getOutputStream();
                os.write(b);
                os.flush();

                Thread.sleep(200);

                //����
                b = TimeBlockReviewData.getGroupOfLights();
                os.write(b);
                os.flush();

                Thread.sleep(200);


                //Ϩ��+���ٶ�
                b = TimeBlockReviewData.getOffLights()[0];
                os.write(b);
                os.flush();

                Thread.sleep(200);
                b = TimeBlockReviewData.getOffLights()[1];
                os.write(b);
                os.flush();

                //�ƿ�
                Thread.sleep(200);
                b = TimeBlockReviewData.getlibOfLights()[0];
                os.write(b);
                os.flush();
                Thread.sleep(200);
                b = TimeBlockReviewData.getlibOfLights()[1];
                os.write(b);
                os.flush();

                //Ч����ʱ�������
                Thread.sleep(200);
                Object[] objects = TimeBlockReviewData.getEffectLight(sc - 1, group, block, index);
                for (int i = 0; i < objects.length; i++) {
                    b = (byte[]) objects[i];
                    os.write(b);
                    os.flush();
                    Thread.sleep(200);
                }

                //����Ԥ��
                Thread.sleep(200);
//                b = TimeBlockReviewData.getStarReview(sc, group, index);
//                os.write(b);
                os.flush();


                //os.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else if (Data.socket != null) {
            try {

                //�ƾ�����
                byte[] b = TimeBlockReviewData.getInfOfLight();
                Socket.UDPSendData(b);
                Thread.sleep(300);

                //����
                b = TimeBlockReviewData.getGroupOfLights();
                Socket.UDPSendData(b);
                Thread.sleep(300);


                //Ϩ��+���ٶ�
                b = TimeBlockReviewData.getOffLights()[0];
                Socket.UDPSendData(b);

                Thread.sleep(300);
                b = TimeBlockReviewData.getOffLights()[1];
                Socket.UDPSendData(b);
                //�ƿ�
                Thread.sleep(200);
                b = TimeBlockReviewData.getlibOfLights()[0];
                Socket.UDPSendData(b);
                Thread.sleep(300);
                b = TimeBlockReviewData.getlibOfLights()[1];
                Socket.UDPSendData(b);

                //Ч����ʱ�������
                Thread.sleep(300);
                Object[] objects = TimeBlockReviewData.getEffectLight(sc - 1, group, block, index);
                for (int i = 0; i < objects.length; i++) {
                    b = (byte[]) objects[i];
                    Socket.UDPSendData(b);
                    Thread.sleep(300);
                }

                //����Ԥ��
                Thread.sleep(300);
                b = TimeBlockReviewData.getStarReview(sc, group, index);
                Socket.UDPSendData(b);


            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }


    public void actionPerformed2() {
        if (Data.serialPort != null) {
            try {

                //�ƾ�����
                byte[] b = TimeBlockReviewData.getInfOfLight();
                OutputStream os = Data.serialPort.getOutputStream();
                os.write(b);
                os.flush();

                Thread.sleep(200);

                //����
                b = TimeBlockReviewData.getGroupOfLights();
                os.write(b);
                os.flush();

                Thread.sleep(200);


                //Ϩ��+���ٶ�
                b = TimeBlockReviewData.getOffLights()[0];
                os.write(b);
                os.flush();

                Thread.sleep(200);
                b = TimeBlockReviewData.getOffLights()[1];
                os.write(b);
                os.flush();

                //�ƿ�
                Thread.sleep(200);
                b = TimeBlockReviewData.getlibOfLights()[0];
                os.write(b);
                os.flush();
                Thread.sleep(200);
                b = TimeBlockReviewData.getlibOfLights()[1];
                os.write(b);
                os.flush();

                //Ч����ʱ�������
                Thread.sleep(200);
                Object[] objects = TimeBlockReviewData.getEffectLightObjects(denKuNum, suCaiNum);
                for (int i = 0; i < objects.length; i++) {
                    b = (byte[]) objects[i];
                    os.write(b);
                    os.flush();
                    Thread.sleep(200);
                }

                //����Ԥ��
                Thread.sleep(200);
                b = TimeBlockReviewData.getStarReview(1, Integer.parseInt(comboBox.getSelectedItem().toString().split("#")[0]), 1);
                os.write(b);
                os.flush();


                //os.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else if (Data.socket != null) {
            try {

                //�ƾ�����
                byte[] b = TimeBlockReviewData.getInfOfLight();
                Socket.UDPSendData(b);
                Thread.sleep(300);

                //����
                b = TimeBlockReviewData.getGroupOfLights();
                Socket.UDPSendData(b);
                Thread.sleep(300);


                //Ϩ��+���ٶ�
                b = TimeBlockReviewData.getOffLights()[0];
                Socket.UDPSendData(b);

                Thread.sleep(300);
                b = TimeBlockReviewData.getOffLights()[1];
                Socket.UDPSendData(b);
                //�ƿ�
                Thread.sleep(300);
                b = TimeBlockReviewData.getlibOfLights()[0];
                Socket.UDPSendData(b);
                Thread.sleep(300);
                b = TimeBlockReviewData.getlibOfLights()[1];
                Socket.UDPSendData(b);

                //Ч����ʱ�������
                Thread.sleep(300);
                Object[] objects = TimeBlockReviewData.getEffectLightObjects(denKuNum, suCaiNum);
                for (int i = 0; i < objects.length; i++) {
                    b = (byte[]) objects[i];
                    Socket.UDPSendData(b);
                    Thread.sleep(300);
                }

                //����Ԥ��
                Thread.sleep(300);
                b = TimeBlockReviewData.getStarReview(1, Integer.parseInt(comboBox.getSelectedItem().toString().split("#")[0]), 1);
                Socket.UDPSendData(b);


            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
