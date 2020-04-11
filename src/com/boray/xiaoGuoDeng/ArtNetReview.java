package com.boray.xiaoGuoDeng;

import com.boray.Data.ChannelName;
import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.RGBData;
import com.boray.Utils.Socket;

import java.util.*;

public class ArtNetReview {

    private static int group, index;
    private static int[] startAddress;//每个灯的起始地址
    private static HashMap map;
    private static HashMap nameMap;//通道名称
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
        List list66 = (List) map.get("channelData");//通道控制
        boolean[] bn = (boolean[]) list66.get(1);//通道勾选
        boolean[] bn2 = (boolean[]) list66.get(3);//渐变勾选
        Vector vector88 = (Vector) list66.get(0);//通道步数
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
        //获得所有的步数据
        int[][] buData = new int[vector88.size()][suCaiTonDaoShu];
        int[] number = new int[vector88.size()];
        for (int i = 0; i < vector88.size(); i++) {
            Vector tpe = (Vector) vector88.get(i);
            for (int j = 1; j < tpe.size(); j++) {
                if (j == 1) {//步执行时长
                    int time = Integer.valueOf(tpe.get(j).toString());
                    int num = time / 100;
                    num = num == 0 ? 1 : num;//小于100毫秒的时候，直接设置100毫秒
                    number[i] = num;
                } else {
                    buData[i][j - 2] = Integer.valueOf(tpe.get(j).toString());
                }
            }
        }
        List<int[]> list = new ArrayList<>();
        for (int i = 0; i < vector88.size(); i++) {
            int[] oldData = new int[buData[i].length];//旧数据
            int[] newData = buData[i];//新数据
            int[] tempData = new int[buData[i].length];//每次累增的数值
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
        //判断是否启用RGB 同时替换步数据中的RGB
        //RGB1
        List list2 = (List) map.get("rgb1Data");
        neatenRGBData(list, list2, "RGB1");
        //RGB2
        list2 = (List) map.get("rgb2Data");
        neatenRGBData(list, list2, "RGB2");
        //RGB3
        list2 = (List) map.get("rgb3Data");
        neatenRGBData(list, list2, "RGB3");

        //整理数据后，发送数据
        for (int i = 0; i < list.size(); i++) {
//            serialPortTest(list.get(i), 100);
            neatenData(list.get(i),100);
        }
        System.out.println();
    }

    /**
     * artnet协议发送数据
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
            Socket.ArtNetSendData(bytes);//添加artNet数据协议发送
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 整理通道数据与RGB数据
     *
     * @param list
     * @param list2
     * @param type
     */
    public void neatenRGBData(List<int[]> list, List list2, String type) {
        if (list2 != null) {
            if ((boolean) list2.get(0)) {//启用
                List<Integer> ints = getRGBTonDaoIndex(type);//获得对应RGB的通道下标
                int[][] RGB = RGBData.getRGBModel(Integer.parseInt(list2.get(4).toString()));//根据选择的类型获得对应的RGB值
                boolean flag = (boolean) list2.get(5);//渐变或者硬切
                int speed = Integer.parseInt(list2.get(6).toString());//渐变速度
                int num = (101 - speed);//分段小步
                List<int[]> RGBData = new ArrayList<>();//RGB数据
                if (flag) {//渐变
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
                } else {//硬切
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
     * 获得对应RGB通道下标
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
     * 用步预览的方式代替 artNet 协议输出
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
