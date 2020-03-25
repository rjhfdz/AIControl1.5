package com.boray.xiaoGuoDeng;

import com.boray.Data.Data;
import com.boray.Utils.Socket;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class ArtNetReview {

    private int group;
    private int index;
    private int[] startAddress;//ÿ���Ƶ���ʼ��ַ
    private HashMap map;

    public ArtNetReview(int group, int index, int[] startAddress) {
        this.group = group;
        this.index = index;
        this.startAddress = startAddress;
        this.map = (HashMap) Data.SuCaiObjects[this.group][this.index];
    }

    public void getData() {
        int suCaiTonDaoShu = Integer.parseInt(Data.DengKuChannelCountList.get(group).toString());
        List list66 = (List) map.get("channelData");//ͨ������
        boolean[] bn = (boolean[]) list66.get(1);//ͨ����ѡ
        boolean[] bn2 = (boolean[]) list66.get(3);//���乴ѡ
        Vector vector88 = (Vector) list66.get(0);//ͨ������
        int[][] tonDao = new int[vector88.size()][suCaiTonDaoShu + 1];
        int lenght = suCaiTonDaoShu + 1;
        for (int n = 0; n < vector88.size(); n++) {
            Vector tpe = (Vector) vector88.get(n);
            int timeTp = Integer.valueOf(tpe.get(1).toString()).intValue();
            if (lenght > tpe.size()) {
                lenght = tpe.size();
            }
            tonDao[n][0] = timeTp;
            for (int l = 1; l < lenght; l++) {
                tonDao[n][l] = Integer.valueOf(tpe.get(l).toString());
            }
        }
        if (whether(bn2)) {//������
            for (int i = 0; i < vector88.size(); i++) {
                Vector tpe = (Vector) vector88.get(i);
                int[] ints = new int[tpe.size() - 2];
                for (int l = 2; l < tpe.size(); l++) {
                    ints[l] = Integer.valueOf(tpe.get(l).toString());
                }
                if (i == 0) {
                    neatenTonDao(ints, new int[ints.length], bn, bn2, true, Integer.valueOf(tpe.get(1).toString()).intValue());
                } else {
                    tpe = (Vector) vector88.get(i - 1);
                    int[] ints2 = new int[tpe.size() - 2];
                    for (int l = 2; l < tpe.size(); l++) {
                        ints2[l] = Integer.valueOf(tpe.get(l).toString());
                    }
                    neatenTonDao(ints2, ints, bn, bn2, true, Integer.valueOf(tpe.get(1).toString()).intValue());
                }
            }
        } else {//δ��ѡ����
            for (int i = 0; i < vector88.size(); i++) {
                Vector tpe = (Vector) vector88.get(i);
                int[] ints = new int[tpe.size() - 2];
                for (int l = 2; l < tpe.size(); l++) {
                    ints[l] = Integer.valueOf(tpe.get(l).toString());
                }
                if (i == 0) {
                    neatenTonDao(ints, new int[ints.length], bn, bn2, false, Integer.valueOf(tpe.get(1).toString()).intValue());
                } else {
                    tpe = (Vector) vector88.get(i - 1);
                    int[] ints2 = new int[tpe.size() - 2];
                    for (int l = 2; l < tpe.size(); l++) {
                        ints2[l] = Integer.valueOf(tpe.get(l).toString());
                    }
                    neatenTonDao(ints2, ints, bn, bn2, false, Integer.valueOf(tpe.get(1).toString()).intValue());
                }
            }
        }
    }

    public void neatenTonDao(int[] oldData, int[] newData, boolean[] bn, boolean[] bn2, boolean flag, int time) {
        if (flag) {
            int num = time / 100;
            num = num == 0 ? 1 : num;//С��100�����ʱ��ֱ������100����
            int[][] temp = new int[num][oldData.length];
            int[] everyAdd = new int[oldData.length];
            for (int i = 0; i < oldData.length; i++) {
                if (bn[i]) {
                    if (bn2[i]) {
                        everyAdd[i] = (newData[i] - oldData[i]) / num;
                    }
                }
            }
            for (int i = 0; i < num; i++) {
                for (int j = 0; j < oldData.length; j++) {
                    if (i == (num - 1))//��ֹ���� ����С��ƫ�� ��������ֱ���滻
                        temp[i][j] = newData[i];
                    else
                        temp[i][j] = oldData[j] + everyAdd[j];

                }
            }
            for (int i = 0; i < num; i++) {
                neatenData(temp[i], 100);
            }
        } else {
            int num = time / 500;
            num = num == 0 ? 1 : num;//ʱ��С��500��ʱ�� ֱ������500����
            for (int i = 0; i < num; i++) {
                neatenData(oldData, 500);
            }
        }
    }

    /**
     * �ж��Ƿ�ѡ����
     *
     * @param bn
     * @return
     */
    public boolean whether(boolean[] bn) {
        boolean flag = false;
        for (int i = 0; i < bn.length; i++) {
            if (bn[i]) {
                flag = true;
                break;
            }
        }
        return flag;
    }


    public void neatenData(int[] a, int time) {
        try {
            byte[] bytes = new byte[512];
            for (int j = 0; j < a.length; j++) {
                for (int i = 0; i < startAddress.length; i++) {
                    bytes[startAddress[i] + j - 1] = (byte) a[j];
                }
            }
            Socket.ArtNetSendData(bytes);//���artNet����Э�鷢��
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
