package com.boray.xiaoGuoDeng;

import com.boray.Data.ChannelName;
import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.RGBData;
import com.boray.Utils.Socket;

import java.nio.Buffer;
import java.util.*;

public class ArtNetReview {

    private static int group, index;
    private static int[] startAddress;//ÿ���Ƶ���ʼ��ַ
    private static HashMap map;
    private static HashMap nameMap;//ͨ������
    private static int suCaiTonDaoShu;
    public static Timer ArtNetReviewTimer;


    public ArtNetReview(int group, int index, int[] startAddress) {
        this.group = group;
        this.index = index;
        this.startAddress = startAddress;
        this.map = (HashMap) Data.SuCaiObjects[this.group][this.index];
        this.suCaiTonDaoShu = Integer.parseInt(Data.DengKuChannelCountList.get(group).toString());
        this.nameMap = (HashMap) Data.DengKuList.get(this.group);
    }


    public void getData() {
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
        //������еĲ�����
        int[][] buData = new int[vector88.size()][suCaiTonDaoShu];
        int[] number = new int[vector88.size()];
        for (int i = 0; i < vector88.size(); i++) {
            Vector tpe = (Vector) vector88.get(i);
            for (int j = 1; j < tpe.size(); j++) {
                if (j == 1) {//��ִ��ʱ��
                    int time = Integer.valueOf(tpe.get(j).toString());
                    int num = time / 100;
                    num = num == 0 ? 1 : num;//С��100�����ʱ��ֱ������100����
                    number[i] = num;
                } else {
                    buData[i][j - 2] = Integer.valueOf(tpe.get(j).toString());
                }
            }
        }
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < vector88.size(); i++) {
            int[] oldData = new int[buData[i].length];//������
            int[] newData = buData[i];//������
            int[] tempData = new int[buData[i].length];//ÿ����������ֵ
            if (!(i == 0)) {
                oldData = buData[i - 1];
            }
            for (int j = 0; j < buData[i].length; j++) {
                if (bn[j]) {
                    if (bn2[j]) {
                        tempData[j] = (newData[j] - oldData[j]) / number[i];
                    } else {
                        oldData[j] = newData[j];
                    }
                } else {
                    oldData[j] = 0;
                }
            }
            for (int k = 0; k < number[i]; k++) {
                if (k == (number[i] - 1)) {
                    list.add(newData);
                } else {
                    int[] temp = new int[tempData.length];
                    for (int h = 0; h < tempData.length; h++) {
                        temp[h] = oldData[h] + (k * tempData[h]);
                    }
                    list.add(temp);
                }
            }
        }
        //�ж��Ƿ�����RGB ͬʱ�滻�������е�RGB
        //RGB1
        List list2 = (List) map.get("rgb1Data");
        neatenRGBData(list, list2, "RGB1");
        //RGB2
        list2 = (List) map.get("rgb2Data");
        neatenRGBData(list, list2, "RGB2");
        //RGB3
        list2 = (List) map.get("rgb3Data");
        neatenRGBData(list, list2, "RGB3");

        List<byte[]> bytes = new ArrayList<>();
        //�������ݺ󣬷�������
        for (int i = 0; i < list.size(); i++) {
//            serialPortTest(list.get(i), 100);
//            neatenData(list.get(i),100);
            int[] a = list.get(i);
            byte[] buff = new byte[512];
            for (int j = 0; j < a.length; j++) {
                for (int k = 0; k < startAddress.length; k++) {
                    buff[startAddress[k] + j - 1] = (byte) a[j];
                }
            }
            bytes.add(buff);
        }

        System.out.println();
    }

    /**
     * artnetЭ�鷢������
     *
     * @param a
     * @param time
     */
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

    /**
     * ����ͨ��������RGB����
     *
     * @param list
     * @param list2
     * @param type
     */
    public void neatenRGBData(List<int[]> list, List list2, String type) {
        if (list2 != null) {
            if ((boolean) list2.get(0)) {//����
                List<Integer> ints = getRGBTonDaoIndex(type);//��ö�ӦRGB��ͨ���±�
                int[][] RGB = RGBData.getRGBModel(Integer.parseInt(list2.get(4).toString()));//����ѡ������ͻ�ö�Ӧ��RGBֵ
                boolean flag = (boolean) list2.get(5);//�������Ӳ��
                int speed = Integer.parseInt(list2.get(6).toString());//�����ٶ�
                int num = (101 - speed);//�ֶ�С��
                List<int[]> RGBData = new ArrayList<>();//RGB����
                if (flag) {//����
                    for (int i = 1; i < RGB.length; i++) {
                        int[] oldData = RGB[i - 1];
                        int[] newData = RGB[i];
                        int[] temp = new int[oldData.length];
                        for (int j = 0; j < temp.length; j++) {
                            temp[j] = (newData[j] - oldData[j]) / num;
                        }
                        for (int j = 0; j < num; j++) {
                            if (j == (num - 1)) {
                                RGBData.add(newData);
                            } else {
                                int[] temp2 = new int[temp.length];
                                for (int k = 0; k < temp.length; k++) {
                                    temp2[k] = oldData[k] + (temp[k] * j);
                                }
                                RGBData.add(temp2);
                            }
                        }
                    }
                } else {//Ӳ��
                    for (int i = 1; i < RGB.length; i++) {
                        int[] oldData = RGB[i - 1];
                        int[] newData = RGB[i];
                        for (int j = 0; j < num; j++) {
                            if (j == (num - 1)) {
                                RGBData.add(newData);
                            } else {
                                RGBData.add(oldData);
                            }
                        }
                    }
                }
                int b = 0;
                for (int i = 0; i < list.size(); i++) {
                    int[] temp = list.get(i);
                    if (b >= RGBData.size()) {
                        b = 0;
                    }
                    int[] rgbTemp = RGBData.get(b);
                    for (int j = 0; j < temp.length; j++) {
                        for (int k = 0; k < ints.size(); k++) {
                            Integer a = ints.get(k);
                            if (j == a) {
                                temp[j] = rgbTemp[k];
                            }
                        }
                    }
                    list.set(i, temp);
                    b++;
                }
            }
        }
    }

    /**
     * ����RGB�еĶ������
     *
     * @param bytes
     * @param list2
     * @param type
     */
    public void neatenRGBDDuoDengata(List<byte[]> bytes, List list2, String type) {
        if (list2 != null) {
            if ((boolean) list2.get(0)) {//����
                List<Integer> ints = getRGBTonDaoIndex(type);//��ö�ӦRGB��ͨ���±�
                int select = Integer.parseInt(list2.get(11).toString());//���ģʽѡ��
                boolean flag = (boolean) list2.get(12);//�Ƿ��ַ���
                int speed = Integer.parseInt(list2.get(13).toString()) / 100;//���ģʽʱ��
                switch (select) {
                    case 0://�����
                        break;
                    case 1://�м���
                        break;
                    case 2://���˲��
                        break;
                    case 3://�����
                        break;
                    case 4://�����(����)
                        break;
                    case 5://�����2
                        break;
                    case 6://�����(����)
                        break;
                    case 7://����
                        break;
                    case 8://����(����)
                        break;
                    case 9://ż��
                        break;
                    case 10://ż��(����)
                        break;
                    case 11://����+ż��
                        break;
                    case 12://����+ż��(����)
                        break;
                    case 13://ż��+����
                        break;
                    case 14://ż��+����(����)
                        break;
                    case 15://��ˮ��
                        break;
                }

            }
        }
    }

    public void RGBDuoDeng1(List<Integer> ints, boolean flag, int speed, List<byte[]> bytes) {
        List<Set<Integer>> setList = new ArrayList<>();
        if (startAddress.length % 2 == 0) {
            int a = (startAddress.length / 2);
            for (int i = 0; i < a; i++) {
                Set<Integer> sets = getstartAddressSets();
                Set<Integer> newSet = new HashSet<>();
                newSet.add(a - (i + 1));
                newSet.add(a + i);
                sets.removeAll(newSet);
                setList.add(sets);
            }
        } else {
            int a = (startAddress.length + 1) / 2;
            for (int i = 0; i < a; i++) {
                if (i == 0) {
                    Set<Integer> sets = getstartAddressSets();
                    Set<Integer> newSet = new HashSet<>();
                    newSet.add(a - 1);
                    sets.removeAll(newSet);
                    setList.add(sets);
                } else {
                    Set<Integer> sets = getstartAddressSets();
                    Set<Integer> newSet = new HashSet<>();
                    newSet.add(a - (i + 1));
                    newSet.add(a + (i - 1));
                    sets.removeAll(newSet);
                    setList.add(sets);
                }
            }
        }
        int a = 0;
        for (int i = 0; i < bytes.size(); i++) {
            if ((i + 1) % speed == 0) {

            } else {

            }
        }
    }

    public void RGBDuoDeng2() {

    }

    public void RGBDuoDeng3() {

    }

    public void RGBDuoDeng4() {

    }

    public void RGBDuoDeng5() {

    }

    public void RGBDuoDeng6() {

    }

    public void RGBDuoDeng7() {

    }

    public void RGBDuoDeng8() {

    }

    public void RGBDuoDeng9() {

    }

    public void RGBDuoDeng10() {

    }

    public void RGBDuoDeng11() {

    }

    public void RGBDuoDeng12() {

    }

    public void RGBDuoDeng13() {

    }

    public void RGBDuoDeng14() {

    }

    public void RGBDuoDeng15() {

    }

    public Set<Integer> getstartAddressSets() {
        Set<Integer> sets = new HashSet<>();
        for (int i = 0; i < startAddress.length; i++) {
            sets.add(i);
        }
        return sets;
    }


    /**
     * ��ö�ӦRGBͨ���±�
     *
     * @param type
     * @return
     */
    public List<Integer> getRGBTonDaoIndex(String type) {
        List<Integer> ints = new ArrayList<>();
        String str = "";
        for (int j = 0; j < suCaiTonDaoShu; j++) {
            if (j < 16) {
                str = ChannelName.getChangeName(nameMap.get("L" + j).toString());
                if (type.equals("RGB1")) {
                    if (str.contains("RGBR-1") || str.contains("RGBG-1") || str.contains("RGBB-1")) {
                        ints.add(j);
                    }
                } else if (type.equals("RGB2")) {
                    if (str.contains("RGBR-2") || str.contains("RGBG-2") || str.contains("RGBB-2")) {
                        ints.add(j);
                    }
                } else if (type.equals("RGB3")) {
                    if (str.contains("RGBR-3") || str.contains("RGBG-3") || str.contains("RGBB-3")) {
                        ints.add(j);
                    }
                }
            } else {
                str = ChannelName.getChangeName(nameMap.get("R" + (j - 16)).toString());
                if (type.equals("RGB1")) {
                    if (str.contains("RGBR-1") || str.contains("RGBG-1") || str.contains("RGBB-1")) {
                        ints.add(j);
                    }
                } else if (type.equals("RGB2")) {
                    if (str.contains("RGBR-2") || str.contains("RGBG-2") || str.contains("RGBB-2")) {
                        ints.add(j);
                    }
                } else if (type.equals("RGB3")) {
                    if (str.contains("RGBR-3") || str.contains("RGBG-3") || str.contains("RGBB-3")) {
                        ints.add(j);
                    }
                }

            }
        }
        return ints;
    }

    /**
     * �ò�Ԥ���ķ�ʽ���� artNet Э�����
     *
     * @param a
     * @param time
     */
    public void serialPortTest(int[] a, int time) {
        byte[] buff = new byte[512 + 8];
        buff[0] = (byte) 0xBB;
        buff[1] = (byte) 0x55;
        buff[2] = (byte) (520 / 256);
        buff[3] = (byte) (520 % 256);
        buff[4] = (byte) 0x80;
        buff[5] = (byte) 0x01;
        buff[6] = (byte) 0xFF;
        for (int j = 0; j < a.length; j++) {
            for (int i = 0; i < startAddress.length; i++) {
                buff[j - 1 + startAddress[i] + 7] = (byte) a[j];
            }
        }
        buff[519] = ZhiLingJi.getJiaoYan(buff);
        try {
            Socket.SerialPortSendData(buff);
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
