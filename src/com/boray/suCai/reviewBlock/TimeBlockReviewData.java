package com.boray.suCai.reviewBlock;

import com.boray.Data.Data;
import com.boray.Data.GetChannelNumber;
import com.boray.Data.ZhiLingJi;
import com.boray.dengKu.Entity.BlackOutEntity;
import com.boray.dengKu.Entity.SpeedEntity;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.xiaoGuoDeng.UI.DefineJLable;

import javax.swing.*;
import java.util.*;

public class TimeBlockReviewData {

    /*
     * 灯具
     */
    public static byte[] getInfOfLight() {
        NewJTable table;
        //灯具
        //byte[][] t3 = new byte[50][23];
        table = (NewJTable) MainUi.map.get("table_dengJu");
        //{"启用","ID","灯具名称","型号","库版本","DMX起始地址","占用通道数"};
        int add = 0;
        int lights = table.getRowCount();
        int length = lights * 4 + 9;
        byte[] lightsByte = new byte[length];
        lightsByte[0] = (byte) 0xBB;
        lightsByte[1] = (byte) 0x55;
        lightsByte[2] = (byte) (length / 256);
        lightsByte[3] = (byte) (length % 256);
        lightsByte[4] = (byte) 0x81;
        lightsByte[5] = (byte) 0x01;
        lightsByte[7] = (byte) lights;
        for (int i = 0; i < lights; i++) {
            boolean b = (boolean) table.getValueAt(i, 0);
            if (b) {
                //地址
                add = Integer.valueOf((String) (table.getValueAt(i, 5))).intValue();
                //t3[i][0] = (byte)(add/256);
                //t3[i][1] = (byte)(add%256);
                lightsByte[8 + i * 4] = (byte) (add / 256);
                lightsByte[9 + i * 4] = (byte) (add % 256);
                //占用通道数
                add = Integer.valueOf((String) (table.getValueAt(i, 6))).intValue();
                //t3[i][2] = (byte)add;
                lightsByte[10 + i * 4] = (byte) add;
                //灯具名称
                String s = (String) (table.getValueAt(i, 2));
                //关联灯库编号
                s = (String) (table.getValueAt(i, 3));
                s = s.split("#")[0].substring(2);
                //t3[i][3] = (byte)Integer.valueOf(s).intValue();
                lightsByte[11 + i * 4] = (byte) Integer.valueOf(s).intValue();
            }
        }
        lightsByte[length - 1] = ZhiLingJi.getJiaoYan(lightsByte);
        return lightsByte;
    }

    /*
     * 灯具分组
     */
    public static byte[] getGroupOfLights() {
        //灯具分组
        byte[][] t4 = new byte[30][8];
        NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
        int size = table.getRowCount() * 8;
        byte[] temp = new byte[size + 9];
        temp[0] = (byte) 0xBB;
        temp[1] = (byte) 0x55;
        temp[2] = (byte) ((size + 9) / 256);
        temp[3] = (byte) ((size + 9) % 256);
        temp[4] = (byte) 0x82;
        temp[5] = (byte) 0x01;
        temp[7] = (byte) table.getRowCount();
        int q, p;
        //{"启用","ID","组别名称"};
        for (int i = 0; i < table.getRowCount(); i++) {
            boolean b = (boolean) table.getValueAt(i, 0);
            if (b) {
                //t4[i][0] = 1;
                temp[8 + i * 8] = 1;
                //关联灯具编号
                TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(i);
                if (treeSet.size() > 0) {
                    Iterator iterator = treeSet.iterator();
                    while (iterator.hasNext()) {
                        q = (int) iterator.next();
                        p = q / 8;
                        q = 7 - (q % 8);
                        //t4[i][1+p] = (byte)(Byte.toUnsignedInt(t4[i][1+p])+ (1 << q));
                        temp[9 + i * 8 + p] = (byte) (Byte.toUnsignedInt(temp[9 + i * 8 + p]) + (1 << q));
                    }
                }
            }
        }
        temp[size + 8] = ZhiLingJi.getJiaoYan(temp);
        return temp;
    }

    /*
     * 灯库
     */
    public static byte[][] getlibOfLights() {
        //灯库
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        //byte[][] t1 = new byte[20][68];
        //byte[][] t2 = new byte[20][37];
        byte[][] temp = new byte[2][340];
        for (int i = 0; i < 2; i++) {
            temp[i][0] = (byte) 0xBB;
            temp[i][1] = (byte) 0x55;
            temp[i][2] = (byte) (340 / 256);
            temp[i][3] = (byte) (340 % 256);
            temp[i][4] = (byte) 0x84;
            temp[i][5] = (byte) 0x01;
            temp[i][6] = (byte) (i + 1);
        }

        //int size = table.getRowCount();
        int channelNum = 0;
        for (int selected = 0; selected < table.getRowCount(); selected++) {
            int channelCount = Integer.valueOf(Data.DengKuChannelCountList.get(selected).toString());
            HashMap map = (HashMap) Data.DengKuList.get(selected);

            JComboBox[] channelBoxs_L = (JComboBox[]) MainUi.map.get("lamp_1_To_16");
            JComboBox[] channelBoxs_R = (JComboBox[]) MainUi.map.get("lamp_17_To_32");
            if (selected <= 9) {
                temp[0][9 + (33 * selected)] = (byte) channelCount;
            } else {
                temp[1][9 + (33 * (selected - 10))] = (byte) channelCount;
            }
            if (channelCount > 16) {
                for (int i = 0; i < 16; i++) {
                    channelNum = GetChannelNumber.get(map.get(channelBoxs_L[i].getName()).toString());
                    if (selected <= 9) {
                        temp[0][10 + (33 * selected) + i] = (byte) channelNum;
                    } else {
                        temp[1][10 + (33 * (selected - 10)) + i] = (byte) channelNum;
                    }
                }
                for (int i = 16; i < channelCount; i++) {
                    channelNum = GetChannelNumber.get(map.get(channelBoxs_R[i - 16].getName()).toString());
                    if (selected <= 9) {
                        temp[0][10 + (33 * selected) + i] = (byte) channelNum;
                    } else {
                        temp[1][10 + (33 * (selected - 10)) + i] = (byte) channelNum;
                    }
                }
            } else {
                for (int i = 0; i < channelCount; i++) {
                    channelNum = GetChannelNumber.get(map.get(channelBoxs_L[i].getName()).toString());
                    if (selected <= 9) {
                        temp[0][10 + (33 * selected) + i] = (byte) channelNum;
                    } else {
                        temp[1][10 + (33 * (selected - 10)) + i] = (byte) channelNum;
                    }
                }
            }
        }
        for (int i = 0; i < 2; i++) {
            temp[i][339] = ZhiLingJi.getJiaoYan(temp[i]);
        }
        return temp;
    }

    /*
     * 熄灯+加速度通道
     */
    public static byte[][] getOffLights() {
        byte[][] temp = new byte[2][338];
        for (int i = 0; i < 2; i++) {
            temp[i][0] = (byte) 0xBB;
            temp[i][1] = (byte) 0x55;
            temp[i][2] = (byte) (338 / 256);
            temp[i][3] = (byte) (338 % 256);
            temp[i][4] = (byte) 0x83;
            temp[i][5] = (byte) 0x01;
            temp[i][6] = (byte) (i + 1);
        }
        //灯库
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        byte offLightChannel = 0, speedChannel = 0, direction = 0;
        for (int select = 0; select < table.getRowCount(); select++) {
            Map map2 = (Map) Data.dengKuBlackOutAndSpeedList.get(select);
            if (map2 != null) {
                BlackOutEntity blackOutEntity = (BlackOutEntity) map2.get("blackOutEntity");
                SpeedEntity speedEntity = (SpeedEntity) map2.get("speedEntity");
                for (int i = 0; i < 4; i++) {
                    if (blackOutEntity.getC(i)[0].equals("所有")) {
                        offLightChannel = (byte) 0xFF;
                    } else if (blackOutEntity.getC(i)[0].equals("无")) {
                        offLightChannel = 0;
                    } else {
                        offLightChannel = (byte) Integer.valueOf(blackOutEntity.getC(i)[0]).intValue();
                    }

                    if (select <= 9) {
                        temp[0][8 + 33 * select + i * 4] = offLightChannel;
                        temp[0][9 + 33 * select + i * 4] = (byte) Integer.valueOf(blackOutEntity.getC(i)[1]).intValue();
                        temp[0][10 + 33 * select + i * 4] = (byte) Integer.valueOf(blackOutEntity.getC(i)[2]).intValue();
                    } else {
                        temp[1][8 + 33 * (select - 10) + i * 4] = offLightChannel;
                        temp[1][9 + 33 * (select - 10) + i * 4] = (byte) Integer.valueOf(blackOutEntity.getC(i)[1]).intValue();
                        temp[1][10 + 33 * (select - 10) + i * 4] = (byte) Integer.valueOf(blackOutEntity.getC(i)[2]).intValue();
                    }
                }
                for (int i = 0; i < 3; i++) {
                    if (speedEntity.getS(i)[0].equals("无")) {
                        speedChannel = (byte) 0;
                    } else {
                        speedChannel = (byte) Integer.valueOf(speedEntity.getS(i)[0]).intValue();
                    }
                    if (speedEntity.getS(i)[3].equals("正向")) {
                        direction = (byte) 0;
                    } else {
                        direction = (byte) 1;
                    }
                    if (select <= 9) {
                        temp[0][28 + 33 * select + i * 4] = speedChannel;
                        temp[0][29 + 33 * select + i * 4] = (byte) Integer.valueOf(speedEntity.getS(i)[1]).intValue();
                        temp[0][30 + 33 * select + i * 4] = (byte) Integer.valueOf(speedEntity.getS(i)[2]).intValue();
                        temp[0][31 + 33 * select + i * 4] = direction;
                    } else {
                        temp[1][28 + 33 * (select - 10) + i * 4] = speedChannel;
                        temp[1][29 + 33 * (select - 10) + i * 4] = (byte) Integer.valueOf(speedEntity.getS(i)[1]).intValue();
                        temp[1][30 + 33 * (select - 10) + i * 4] = (byte) Integer.valueOf(speedEntity.getS(i)[2]).intValue();
                        temp[1][31 + 33 * (select - 10) + i * 4] = direction;
                    }
                }
            }
        }
        for (int i = 0; i < 2; i++) {
            temp[i][337] = ZhiLingJi.getJiaoYan(temp[i]);
        }
        return temp;
    }

    //停止预览
    public static byte[] getStopReview(int sc, int group, int block) {
        byte[] temp = new byte[15];
        temp[0] = (byte) 0xBB;
        temp[1] = (byte) 0x55;
        temp[2] = (byte) ((15) / 256);
        temp[3] = (byte) ((15) % 256);
        temp[4] = (byte) 0x86;
        temp[5] = (byte) 0x01;
        temp[6] = (byte) 0xF0;
        temp[7] = (byte) sc;
        temp[8] = (byte) group;
        temp[9] = (byte) block;
        temp[14] = ZhiLingJi.getJiaoYan(temp);
        return temp;
    }

    //启动预览
    public static byte[] getStarReview(int sc, int group, int block) {
        byte[] temp = new byte[15];
        temp[0] = (byte) 0xBB;
        temp[1] = (byte) 0x55;
        temp[2] = (byte) ((15) / 256);
        temp[3] = (byte) ((15) % 256);
        temp[4] = (byte) 0x86;
        temp[5] = (byte) 0x01;
        temp[6] = (byte) 0xF1;
        temp[7] = (byte) sc;
        temp[8] = (byte) group;
        temp[9] = (byte) block;
        temp[14] = ZhiLingJi.getJiaoYan(temp);
        return temp;
    }

    public static Object[] getEffectLight(int sc, int group, int block, int index) {
        byte[] b = getEffectLightToOne2(sc, group, block, index);
        int packet = (b.length - 9) / 512 + 1;
        int lastSize = (b.length - 9) % 512;
        Object[] temp = new Object[packet];
        for (int i = 0; i < packet; i++) {
            if (i != (packet - 1)) {
                byte[] tp1 = new byte[521];
                tp1[0] = (byte) 0xBB;
                tp1[1] = (byte) 0x55;
                tp1[2] = (byte) (521 / 256);
                tp1[3] = (byte) (521 % 256);
                tp1[4] = (byte) 0x85;
                tp1[5] = (byte) 0x11;
                tp1[6] = (byte) ((i + 1) / 256);
                tp1[7] = (byte) ((i + 1) % 256);
                System.arraycopy(b, 8 + i * 512, tp1, 8, 512);
                tp1[520] = ZhiLingJi.getJiaoYan(tp1);
                temp[i] = tp1;
            } else {
                byte[] tp2 = new byte[lastSize + 9];
                tp2[0] = (byte) 0xBB;
                tp2[1] = (byte) 0x55;
                tp2[2] = (byte) ((lastSize + 9) / 256);
                tp2[3] = (byte) ((lastSize + 9) % 256);
                tp2[4] = (byte) 0x85;
                tp2[5] = (byte) 0x11;
                tp2[6] = (byte) ((i + 1) / 256);
                tp2[7] = (byte) ((i + 1) % 256);
                System.arraycopy(b, 8 + i * 512, tp2, 8, lastSize);
                tp2[lastSize + 8] = ZhiLingJi.getJiaoYan(tp2);
                temp[i] = tp2;
            }
        }
        return temp;
    }

    public static byte[] getEffectLightToOne2(int sc, int group, int block, int index) {
        byte[] temp = null;
        int denKuNum = Integer.parseInt(getxiaoGuoDengDengKuName(group));
        int suCaiNum = block;
        int tt = Integer.valueOf((String) Data.DengKuChannelCountList.get(denKuNum - 1)).intValue();
        HashMap hashMap = (HashMap) Data.SuCaiObjects[denKuNum - 1][suCaiNum - 1];
        byte[] T1 = new byte[53];
        byte[] zdyObjects = new byte[5];
        byte[] gxObjects = new byte[4];
        byte[] bTp = new byte[(tt + 2) * 32];
        if (hashMap != null) {
            //动作图形
            Map map = (Map) hashMap.get("actionXiaoGuoData");
            List list = null;
            boolean bb = false;
            int a = 0;
            if (map != null) {
                int selected = Integer.valueOf((String) map.get("2"));
                if (selected == 1) {
                    selected = 255;
                } else if (selected > 1) {
                    selected = selected - 1;
                }
                T1[0] = (byte) selected;
                //运行速度
                int yunXinSpeed = Integer.valueOf((String) map.get("3"));
                T1[1] = (byte) yunXinSpeed;
                //使用开关 1启用/0关
                String ss = (String) map.get("0");
                if (ss != null && "true".equals(ss)) {
                    T1[2] = (byte) 1;
                }
                //拆分    不拆分01/中间拆分02/两端拆分03
                String[] tp1 = (String[]) map.get("4");
                int cc = Integer.valueOf(tp1[0]) + 1;
                T1[3] = (byte) cc;

                //拆分反向
                boolean b = map.containsKey("5") ? (boolean) map.get("5") : false;
                //X轴反向    是1/否0
                if ("true".equals(tp1[1])) {
                    a = 1;
                } else {
                    a = 0;
                }
                T1[5] = (byte) a;
                if (b) {
                    if (a == 1) {
                        T1[5] = (byte) 0x81;
                    } else {
                        T1[5] = (byte) 0x80;
                    }
                }
                //X半
                if ("true".equals(tp1[2])) {
                    a = 1;
                } else {
                    a = 0;
                }
                T1[6] = (byte) a;
                //Y轴反向
                if ("true".equals(tp1[3])) {
                    a = 1;
                } else {
                    a = 0;
                }
                T1[7] = (byte) a;
                //Y半
                if ("true".equals(tp1[4])) {
                    a = 1;
                } else {
                    a = 0;
                }
                T1[8] = (byte) a;


                //时间A_L	时间B_H
                a = Integer.valueOf(tp1[5]).intValue();
                T1[9] = (byte) (a % 256);
                T1[10] = (byte) (a / 256);

                ///////////////自定义动作数据
                String[] values = (String[]) map.get("1");
                byte[] bt1 = new byte[5];
                if (values != null) {
                    if (values[0].equals("true")) {
                        bt1[0] = 1;
                    }
                    for (int k = 1; k < bt1.length; k++) {
                        bt1[k] = (byte) Integer.valueOf(values[k]).intValue();
                    }
                }
                zdyObjects = bt1;
            }

            //RGB1
            list = (List) hashMap.get("rgb1Data");
            String[] tp2 = null;
            boolean[] bs = null;
            String b = "";
            if (list != null) {
                //红色
                tp2 = (String[]) list.get(1);
                a = Integer.valueOf(tp2[0]).intValue();
                T1[11] = (byte) a;
                //绿色
                a = Integer.valueOf(tp2[1]).intValue();
                T1[12] = (byte) a;
                //蓝色
                a = Integer.valueOf(tp2[2]).intValue();
                T1[13] = (byte) a;
                //渐变类型
                b = (String) list.get(4);
                a = Integer.valueOf(b).intValue();
                T1[14] = (byte) a;
                //渐变
                bb = (boolean) list.get(5);
                a = 0;
                if (bb) {
                    a = 1;
                }
                T1[15] = (byte) a;
                //参与渐变勾选
                bs = (boolean[]) list.get(2);
                a = 0;
                if (bs[0]) {
                    a = 128;
                }
                if (bs[1]) {
                    a = a + 64;
                }
                if (bs[2]) {
                    a = a + 32;
                }
                T1[16] = (byte) a;
                //渐变速度
                b = (String) list.get(6);
                a = Integer.valueOf(b).intValue();
                T1[17] = (byte) a;
                //使用开关
                bb = (boolean) list.get(0);
                a = 0;
                if (bb) {
                    a = 1;
                }
                T1[18] = (byte) a;
                //拆分
                b = (String) list.get(7);
                a = Integer.valueOf(b).intValue() + 1;
                T1[19] = (byte) a;

                //拆分反向
                bb = (boolean) list.get(8);
                a = 0;
                if (bb) {
                    a = 1;
                }
                T1[20] = (byte) a;
                //时间A_L	时间B_H
                a = Integer.valueOf((String) list.get(9)).intValue();
                T1[21] = (byte) (a % 256);
                T1[22] = (byte) (a / 256);
            }

            //RGB2
            list = (List) hashMap.get("rgb2Data");
            if (list != null) {
                //红色
                tp2 = (String[]) list.get(1);
                a = Integer.valueOf(tp2[0]).intValue();
                T1[23] = (byte) a;
                //绿色
                a = Integer.valueOf(tp2[1]).intValue();
                T1[24] = (byte) a;
                //蓝色
                a = Integer.valueOf(tp2[2]).intValue();
                T1[25] = (byte) a;
                //渐变类型
                b = (String) list.get(4);
                a = Integer.valueOf(b).intValue();
                T1[26] = (byte) a;
                //渐变
                bb = (boolean) list.get(5);
                a = 0;
                if (bb) {
                    a = 1;
                }
                T1[27] = (byte) a;
                //参与渐变勾选
                bs = (boolean[]) list.get(2);
                a = 0;
                if (bs[0]) {
                    a = 128;
                }
                if (bs[1]) {
                    a = a + 64;
                }
                if (bs[2]) {
                    a = a + 32;
                }
                T1[28] = (byte) a;
                //渐变速度
                b = (String) list.get(6);
                a = Integer.valueOf(b).intValue();
                T1[29] = (byte) a;
                //使用开关
                bb = (boolean) list.get(0);
                a = 0;
                if (bb) {
                    a = 1;
                }
                T1[30] = (byte) a;
                //拆分
                b = (String) list.get(7);
                a = Integer.valueOf(b).intValue() + 1;
                T1[31] = (byte) a;

                //拆分反向
                bb = (boolean) list.get(8);
                a = 0;
                if (bb) {
                    a = 1;
                }
                T1[32] = (byte) a;
                //时间A_L	时间B_H
                a = Integer.valueOf((String) list.get(9)).intValue();
                T1[33] = (byte) (a % 256);
                T1[34] = (byte) (a / 256);
            }

            //RGB3
            list = (List) hashMap.get("rgb3Data");
            if (list != null) {
                //红色
                tp2 = (String[]) list.get(1);
                a = Integer.valueOf(tp2[0]).intValue();
                T1[35] = (byte) a;
                //绿色
                a = Integer.valueOf(tp2[1]).intValue();
                T1[36] = (byte) a;
                //蓝色
                a = Integer.valueOf(tp2[2]).intValue();
                T1[37] = (byte) a;
                //渐变类型
                b = (String) list.get(4);
                a = Integer.valueOf(b).intValue();
                T1[38] = (byte) a;
                //渐变
                bb = (boolean) list.get(5);
                a = 0;
                if (bb) {
                    a = 1;
                }
                T1[39] = (byte) a;
                //参与渐变勾选
                bs = (boolean[]) list.get(2);
                a = 0;
                if (bs[0]) {
                    a = 128;
                }
                if (bs[1]) {
                    a = a + 64;
                }
                if (bs[2]) {
                    a = a + 32;
                }
                T1[40] = (byte) a;
                //渐变速度
                b = (String) list.get(6);
                a = Integer.valueOf(b).intValue();
                T1[41] = (byte) a;
                //使用开关
                bb = (boolean) list.get(0);
                a = 0;
                if (bb) {
                    a = 1;
                }
                T1[42] = (byte) a;
                //拆分
                b = (String) list.get(7);
                a = Integer.valueOf(b).intValue() + 1;
                T1[43] = (byte) a;

                //拆分反向
                bb = (boolean) list.get(8);
                a = 0;
                if (bb) {
                    a = 1;
                }
                T1[44] = (byte) a;
                //时间A_L	时间B_H
                a = Integer.valueOf((String) list.get(9)).intValue();
                T1[45] = (byte) (a % 256);
                T1[46] = (byte) (a / 256);
            }


            /////手动编程配置
            List list66 = (List) hashMap.get("channelData");
            Vector vector88 = null;
            if (list66 != null) {
                //////////勾选
                int r = 0, yu = 0;
                int[] bp1 = new int[4];
                boolean[] bn = (boolean[]) list66.get(1);
                for (int k = 0; k < bn.length; k++) {
                    r = k / 8;
                    //yu = 7 - (k % 8);
                    yu = k % 8;
                    if (bn[k]) {
                        bp1[r] = bp1[r] + (1 << yu);
                    }
                }
                for (int k = bn.length; k < 32; k++) {
                    r = k / 8;
                    yu = k % 8;
                    bp1[r] = bp1[r] + (1 << yu);
                }
                byte[] bp2 = new byte[4];
                for (int k = 0; k < bp1.length; k++) {
                    bp2[k] = (byte) bp1[k];
                }
                gxObjects = bp2;
                //////////////

                String[] ddTemp = (String[]) list66.get(2);
                a = 0;
                vector88 = (Vector) list66.get(0);
                if (vector88 != null) {
                    a = vector88.size();
                }
                //总帧数
                T1[47] = (byte) a;
                //手动编程启用
                T1[48] = (byte) 1;

                //时差A_L	时差B_H
                a = Integer.valueOf(ddTemp[2]).intValue();
                T1[49] = (byte) (a % 256);
                T1[50] = (byte) (a / 256);
                //拆分
                a = Integer.valueOf(ddTemp[0]).intValue() + 1;
                T1[51] = (byte) a;
                //拆分反向
                a = 0;
                if (!ddTemp[1].equals("0")) {
                    a = 1;
                }
                T1[52] = (byte) a;

                Vector tpe = null;
                int lenght = tt + 2;
                if (vector88 != null) {
                    for (int n = 0; n < vector88.size(); n++) {
                        tpe = (Vector) vector88.get(n);
                        int timeTp = Integer.valueOf(tpe.get(1).toString()).intValue();
                        bTp[(tt + 2) * n] = (byte) (timeTp % 256);
                        bTp[(tt + 2) * n + 1] = (byte) (timeTp / 256);
                        if (lenght > tpe.size()) {
                            lenght = tpe.size();
                        }
                        for (int k = 2; k < lenght; k++) {
                            bTp[(tt + 2) * n + k] = Integer.valueOf(tpe.get(k).toString()).byteValue();
                        }
                    }
                }
            }
        }

        int length = 62 + bTp.length + 118;
        temp = new byte[length];
        temp[0] = (byte) 0xBB;
        temp[1] = (byte) 0x55;
        temp[2] = (byte) (length / 256);
        temp[3] = (byte) (length % 256);
        temp[4] = (byte) 0x85;
        temp[5] = (byte) 0x11;
        temp[6] = (byte) 0x00;
        temp[7] = (byte) 0x01;

        //自定义动作
        for (int i = 0; i < 5; i++) {
            temp[i + 8] = zdyObjects[i];
        }
        //勾选数据
        for (int i = 0; i < 4; i++) {
            temp[i + 13] = gxObjects[i];
        }
        //时间块长度
        byte[] b3 = new byte[4];
        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + (sc + 1));
        DefineJLable lable = (DefineJLable) timeBlockPanels[group].getComponent(index - 1);
        int start = lable.getX() / 5;
        int end = (lable.getX() + lable.getWidth()) / 5;
        b3[0] = (byte) (start % 256);
        b3[1] = (byte) (start / 256);

        b3[2] = (byte) (end % 256);
        b3[3] = (byte) (end / 256);
        for (int i = 0; i < 4; i++) {
            temp[i + 17] = b3[i];
        }
        //动作12点数据
        int[] p1 = null;
        int action = Byte.toUnsignedInt(T1[0]);
        if (action == 0) {
            p1 = bezier.Data.ZB[1];
        } else if (action == 1) {
            p1 = bezier.Data.ZB[0];
        } else if (action > 1 && action < 48) {
            p1 = bezier.Data.ZB[action];
        } else if (action >= 48) {
            String[] s = (String[]) bezier.Data.map.get("" + action);
            if (s != null) {
                p1 = new int[24];
                for (int i = 0; i < s.length; i++) {
                    p1[i] = Integer.valueOf(s[i]);
                }
            } else {
                p1 = bezier.Data.ZB[0];
            }
        }
        temp[26] = (byte) action;
        for (int i = 0; i < 24; i++) {
            temp[27 + i] = (byte) p1[i];
        }
        //rgb1 12点数据
        int[] p2 = null;
        int rgb1 = Byte.toUnsignedInt(T1[14]);
        if (rgb1 <= 10) {
            p2 = new int[24];
        } else if (rgb1 > 10 && rgb1 < 52) {
            p2 = bezier.Data.ZBcolor[rgb1 - 11];
        } else if (rgb1 > 51) {
            String[] s = (String[]) bezier.Data.map.get("color" + rgb1);
            if (s != null) {
                p2 = new int[24];
                for (int i = 0; i < s.length; i++) {
                    p2[i] = Integer.valueOf(s[i]);
                }
            } else {
                p2 = bezier.Data.ZB[0];
            }
        }
        temp[51] = (byte) rgb1;
        for (int i = 0; i < 24; i++) {
            temp[52 + i] = (byte) p2[i];
        }
        //rgb2 12点数据
        int[] p3 = null;
        int rgb2 = Byte.toUnsignedInt(T1[26]);
        if (rgb2 <= 10) {
            p3 = new int[24];
        } else if (rgb2 > 10 && rgb2 < 52) {
            p3 = bezier.Data.ZBcolor[rgb2 - 11];
        } else if (rgb2 > 51) {
            String[] s = (String[]) bezier.Data.map.get("color" + rgb2);
            if (s != null) {
                p3 = new int[24];
                for (int i = 0; i < s.length; i++) {
                    p3[i] = Integer.valueOf(s[i]);
                }
            } else {
                p3 = bezier.Data.ZB[0];
            }
        }
        temp[76] = (byte) rgb2;
        for (int i = 0; i < 24; i++) {
            temp[77 + i] = (byte) p3[i];
        }
        //rgb3 12点数据
        int[] p4 = null;
        int rgb3 = Byte.toUnsignedInt(T1[38]);
        if (rgb3 <= 10) {
            p4 = new int[24];
        } else if (rgb3 > 10 && rgb3 < 52) {
            p4 = bezier.Data.ZBcolor[rgb3 - 11];
        } else if (rgb3 > 51) {
            String[] s = (String[]) bezier.Data.map.get("color" + rgb3);
            if (s != null) {
                p4 = new int[24];
                for (int i = 0; i < s.length; i++) {
                    p4[i] = Integer.valueOf(s[i]);
                }
            } else {
                p4 = bezier.Data.ZB[0];
            }
        }
        temp[101] = (byte) rgb3;
        for (int i = 0; i < 24; i++) {
            temp[102 + i] = (byte) p4[i];
        }
        //其他数据
        for (int i = 0; i < 53; i++) {
            temp[i + 126] = T1[i];
        }
        for (int i = 179; i < length - 1; i++) {
            temp[i] = bTp[i - 179];
        }
        temp[length - 1] = ZhiLingJi.getJiaoYan(temp);
        return temp;
    }

    public static String getxiaoGuoDengDengKuName(int group) {
        NewJTable table3 = (NewJTable) MainUi.map.get("allLightTable");//所有灯具
        Integer name = group;
        TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(name - 1);
        Iterator iterator = treeSet.iterator();
        String s2 = "";
        while (iterator.hasNext()) {
            int a = (int) iterator.next();
            if (table3.getRowCount() > 0) {
                String s = table3.getValueAt(a, 0).toString();
                Integer s1 = Integer.parseInt(s.split("#")[0].substring(2));//组内灯具的灯具id
                NewJTable table_dengJu = (NewJTable) MainUi.map.get("table_dengJu");//灯具配置
                s2 = ((String) table_dengJu.getValueAt(s1 - 1, 3)).substring(2, 3);//灯库名称
            }
        }
        return s2;
    }

    /*
     * 效果灯
     * sc 场景号
     * group 组号
     * block 时间块号
     */
    public static byte[] getEffectLightToOne(int sc, int group, int block) {
        byte[] temp = null;

        Object[][] objects = new Object[30][20];
        Object[][] zdyObjects = new Object[30][20];
        Object[][] gxObjects = new Object[30][20];
        byte[][][] t2 = timeBlockData(sc + 1, objects, zdyObjects, gxObjects);
        byte[] t3 = null;

        if (objects[group][block] != null) {
            t3 = (byte[]) objects[group][block];
        } else {
            t3 = new byte[64];
        }
        int length = 62 + t3.length + 118;
        temp = new byte[length];
        temp[0] = (byte) 0xBB;
        temp[1] = (byte) 0x55;
        temp[2] = (byte) (length / 256);
        temp[3] = (byte) (length % 256);
        temp[4] = (byte) 0x85;
        temp[5] = (byte) 0x11;
        temp[6] = (byte) 0x00;
        temp[7] = (byte) 0x01;

        //自定义动作
        byte[] b1 = (byte[]) zdyObjects[group][block];
        if (b1 == null) {
            b1 = new byte[5];
        }
        for (int i = 0; i < 5; i++) {
            temp[i + 8] = b1[i];
        }

        //勾选数据
        byte[] b2 = (byte[]) gxObjects[group][block];
        if (b2 == null) {
            b2 = new byte[4];
            for (int j2 = 0; j2 < b2.length; j2++) {
                b2[j2] = (byte) 0xFF;
            }
        }
        for (int i = 0; i < 4; i++) {
            temp[i + 13] = b2[i];
        }
        //时间块长度
        byte[] b3 = new byte[4];
        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + (sc + 1));
        DefineJLable lable = (DefineJLable) timeBlockPanels[group + 1].getComponent(block - 1);
        int start = lable.getX() / 5;
        int end = (lable.getX() + lable.getWidth()) / 5;
        b3[0] = (byte) (start % 256);
        b3[1] = (byte) (start / 256);

        b3[2] = (byte) (end % 256);
        b3[3] = (byte) (end / 256);
        for (int i = 0; i < 4; i++) {
            temp[i + 17] = b3[i];
        }
        //动作12点数据
        int[] p1 = null;
        int action = Byte.toUnsignedInt(t2[group][block][0]);
        if (action == 0) {
            p1 = bezier.Data.ZB[1];
        } else if (action == 1) {
            p1 = bezier.Data.ZB[0];
        } else if (action > 1 && action < 48) {
            p1 = bezier.Data.ZB[action];
        } else if (action >= 48) {
            String[] s = (String[]) bezier.Data.map.get("" + action);
            if (s != null) {
                //System.out.println(s.length+"//"+s[0]+"//"+s[1]);
                p1 = new int[24];
                for (int i = 0; i < s.length; i++) {
                    p1[i] = Integer.valueOf(s[i]);
                }
            } else {
                p1 = bezier.Data.ZB[0];
            }
        }
        temp[26] = (byte) action;
        for (int i = 0; i < 24; i++) {
            temp[27 + i] = (byte) p1[i];
        }
        //rgb1 12点数据
        int[] p2 = null;
        int rgb1 = Byte.toUnsignedInt(t2[group][block][14]);
        if (rgb1 <= 10) {
            p2 = new int[24];
        } else if (rgb1 > 10 && rgb1 < 52) {
            p2 = bezier.Data.ZBcolor[rgb1 - 11];
        } else if (rgb1 > 51) {
            String[] s = (String[]) bezier.Data.map.get("color" + rgb1);
            if (s != null) {
                p2 = new int[24];
                for (int i = 0; i < s.length; i++) {
                    p2[i] = Integer.valueOf(s[i]);
                }
            } else {
                p2 = bezier.Data.ZB[0];
            }
        }
        temp[51] = (byte) rgb1;
        for (int i = 0; i < 24; i++) {
            temp[52 + i] = (byte) p2[i];
        }
        //rgb2 12点数据
        int[] p3 = null;
        int rgb2 = Byte.toUnsignedInt(t2[group][block][26]);
        if (rgb2 <= 10) {
            p3 = new int[24];
        } else if (rgb2 > 10 && rgb2 < 52) {
            p3 = bezier.Data.ZBcolor[rgb2 - 11];
        } else if (rgb2 > 51) {
            String[] s = (String[]) bezier.Data.map.get("color" + rgb2);
            if (s != null) {
                p3 = new int[24];
                for (int i = 0; i < s.length; i++) {
                    p3[i] = Integer.valueOf(s[i]);
                }
            } else {
                p3 = bezier.Data.ZB[0];
            }
        }
        temp[76] = (byte) rgb2;
        for (int i = 0; i < 24; i++) {
            temp[77 + i] = (byte) p3[i];
        }
        //rgb3 12点数据
        int[] p4 = null;
        int rgb3 = Byte.toUnsignedInt(t2[group][block][38]);
        if (rgb3 <= 10) {
            p4 = new int[24];
        } else if (rgb3 > 10 && rgb3 < 52) {
            p4 = bezier.Data.ZBcolor[rgb3 - 11];
        } else if (rgb3 > 51) {
            String[] s = (String[]) bezier.Data.map.get("color" + rgb3);
            if (s != null) {
                p4 = new int[24];
                for (int i = 0; i < s.length; i++) {
                    p4[i] = Integer.valueOf(s[i]);
                }
            } else {
                p4 = bezier.Data.ZB[0];
            }
        }
        temp[101] = (byte) rgb3;
        for (int i = 0; i < 24; i++) {
            temp[102 + i] = (byte) p4[i];
        }

        //其他数据
        for (int i = 0; i < 53; i++) {
            temp[i + 126] = t2[group][block][i];
        }
        for (int i = 179; i < length - 1; i++) {
            temp[i] = t3[i - 179];
        }
        temp[length - 1] = ZhiLingJi.getJiaoYan(temp);

        return temp;
    }

    /* 时间块数据
     * sc场景号
     * objects 编辑数据
     * zdyObjects  自定义动作数据
     * gxObjects 场景勾选数据
     */
    private static byte[][][] timeBlockData(int sc, Object[][] objects, Object[][] zdyObjects, Object[][] gxObjects) {
        HashMap hashMap = null;
        byte[][][] T1 = new byte[30][20][53];
        //Object[][] objects = new Object[30][20];
        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + sc);

        NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//所有灯具

        for (int i = 0; i < 30; i++) {
            int number = Integer.valueOf(timeBlockPanels[i + 1].getName()).intValue();
            int tt = 0, a = 0;
            if (number - 1 < Data.GroupOfLightList.size()) {
                TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(number - 1);
                if (!treeSet.isEmpty()) {
                    a = (int) treeSet.first();
                    String typeString = table3.getValueAt(a, 3).toString();
                    int dengKuNumber = Integer.valueOf(typeString.split("#")[0].substring(2)).intValue() - 1;
                    tt = Integer.valueOf((String) Data.DengKuChannelCountList.get(dengKuNumber)).intValue();
                }
            }

            //byte[] bTp = new byte[(tt+2)*32];
            for (int j = 0; j < 20; j++) {
                hashMap = (HashMap) Data.SuCaiObjects[i][j];
                byte[] bTp = new byte[(tt + 2) * 32];
                if (hashMap != null) {
                    //动作图形
                    Map map = (Map) hashMap.get("actionXiaoGuoData");
                    List list = null;
                    boolean bb = false;
                    a = 0;
                    if (map != null) {
                        int selected = Integer.valueOf((String) map.get("2"));
                        if (selected == 1) {
                            selected = 255;
                        } else if (selected > 1) {
                            selected = selected - 1;
                        }
                        T1[i][j][0] = (byte) selected;
                        //运行速度
                        int yunXinSpeed = Integer.valueOf((String) map.get("3"));
                        T1[i][j][1] = (byte) yunXinSpeed;
                        //使用开关 1启用/0关
                        String ss = (String) map.get("0");
                        if (ss != null && "true".equals(ss)) {
                            T1[i][j][2] = (byte) 1;
                        }
                        //list = (List)hashMap.get("rgb3Data");
						/*if (list!=null) {
							bb = (boolean)list.get(0);
							if (bb) {
								T1[i][j][2] = (byte)1;
							}
						}*/
                        //拆分    不拆分01/中间拆分02/两端拆分03
                        String[] tp1 = (String[]) map.get("4");
                        int cc = Integer.valueOf(tp1[0]) + 1;
                        T1[i][j][3] = (byte) cc;

                        //拆分反向
                        boolean b = map.containsKey("5") ? (boolean) map.get("5") : false;
                        //X轴反向    是1/否0
                        if ("true".equals(tp1[1])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][5] = (byte) a;
                        if (b) {
                            if (a == 1) {
                                T1[i][j][5] = (byte) 0x81;
                            } else {
                                T1[i][j][5] = (byte) 0x80;
                            }
                        }
                        //X半
                        if ("true".equals(tp1[2])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][6] = (byte) a;
                        //Y轴反向
                        if ("true".equals(tp1[3])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][7] = (byte) a;
                        //Y半
                        if ("true".equals(tp1[4])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][8] = (byte) a;


                        //时间A_L	时间B_H
                        a = Integer.valueOf(tp1[5]).intValue();
                        T1[i][j][9] = (byte) (a % 256);
                        T1[i][j][10] = (byte) (a / 256);

                        ///////////////自定义动作数据
                        String[] values = (String[]) map.get("1");
                        byte[] bt1 = new byte[5];
                        if (values != null) {
                            if (values[0].equals("true")) {
                                bt1[0] = 1;
                            }
                            for (int k = 1; k < bt1.length; k++) {
                                bt1[k] = (byte) Integer.valueOf(values[k]).intValue();
                            }
                        }
                        zdyObjects[i][j] = bt1;
                        /////////////////////////
                    } else {

                    }

                    //RGB1
                    list = (List) hashMap.get("rgb1Data");
                    String[] tp2 = null;
                    boolean[] bs = null;
                    String b = "";
                    if (list != null) {
                        //红色
                        tp2 = (String[]) list.get(1);
                        a = Integer.valueOf(tp2[0]).intValue();
                        T1[i][j][11] = (byte) a;
                        //绿色
                        a = Integer.valueOf(tp2[1]).intValue();
                        T1[i][j][12] = (byte) a;
                        //蓝色
                        a = Integer.valueOf(tp2[2]).intValue();
                        T1[i][j][13] = (byte) a;
                        //渐变类型
                        b = (String) list.get(4);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][14] = (byte) a;
                        //渐变
                        bb = (boolean) list.get(5);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][15] = (byte) a;
                        //参与渐变勾选
                        bs = (boolean[]) list.get(2);
                        a = 0;
                        if (bs[0]) {
                            a = 128;
                        }
                        if (bs[1]) {
                            a = a + 64;
                        }
                        if (bs[2]) {
                            a = a + 32;
                        }
                        T1[i][j][16] = (byte) a;
                        //渐变速度
                        b = (String) list.get(6);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][17] = (byte) a;
                        //使用开关
                        bb = (boolean) list.get(0);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][18] = (byte) a;
                        //拆分
                        b = (String) list.get(7);
                        a = Integer.valueOf(b).intValue() + 1;
                        T1[i][j][19] = (byte) a;

                        //拆分反向
                        bb = (boolean) list.get(8);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][20] = (byte) a;
                        //时间A_L	时间B_H
                        a = Integer.valueOf((String) list.get(9)).intValue();
                        T1[i][j][21] = (byte) (a % 256);
                        T1[i][j][22] = (byte) (a / 256);
                    }

                    //RGB2
                    list = (List) hashMap.get("rgb2Data");
                    if (list != null) {
                        //红色
                        tp2 = (String[]) list.get(1);
                        a = Integer.valueOf(tp2[0]).intValue();
                        T1[i][j][23] = (byte) a;
                        //绿色
                        a = Integer.valueOf(tp2[1]).intValue();
                        T1[i][j][24] = (byte) a;
                        //蓝色
                        a = Integer.valueOf(tp2[2]).intValue();
                        T1[i][j][25] = (byte) a;
                        //渐变类型
                        b = (String) list.get(4);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][26] = (byte) a;
                        //渐变
                        bb = (boolean) list.get(5);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][27] = (byte) a;
                        //参与渐变勾选
                        bs = (boolean[]) list.get(2);
                        a = 0;
                        if (bs[0]) {
                            a = 128;
                        }
                        if (bs[1]) {
                            a = a + 64;
                        }
                        if (bs[2]) {
                            a = a + 32;
                        }
                        T1[i][j][28] = (byte) a;
                        //渐变速度
                        b = (String) list.get(6);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][29] = (byte) a;
                        //使用开关
                        bb = (boolean) list.get(0);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][30] = (byte) a;
                        //拆分
                        b = (String) list.get(7);
                        a = Integer.valueOf(b).intValue() + 1;
                        T1[i][j][31] = (byte) a;

                        //拆分反向
                        bb = (boolean) list.get(8);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][32] = (byte) a;
                        //时间A_L	时间B_H
                        a = Integer.valueOf((String) list.get(9)).intValue();
                        T1[i][j][33] = (byte) (a % 256);
                        T1[i][j][34] = (byte) (a / 256);
                    }

                    //RGB3
                    list = (List) hashMap.get("rgb3Data");
                    if (list != null) {
                        //红色
                        tp2 = (String[]) list.get(1);
                        a = Integer.valueOf(tp2[0]).intValue();
                        T1[i][j][35] = (byte) a;
                        //绿色
                        a = Integer.valueOf(tp2[1]).intValue();
                        T1[i][j][36] = (byte) a;
                        //蓝色
                        a = Integer.valueOf(tp2[2]).intValue();
                        T1[i][j][37] = (byte) a;
                        //渐变类型
                        b = (String) list.get(4);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][38] = (byte) a;
                        //渐变
                        bb = (boolean) list.get(5);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][39] = (byte) a;
                        //参与渐变勾选
                        bs = (boolean[]) list.get(2);
                        a = 0;
                        if (bs[0]) {
                            a = 128;
                        }
                        if (bs[1]) {
                            a = a + 64;
                        }
                        if (bs[2]) {
                            a = a + 32;
                        }
                        T1[i][j][40] = (byte) a;
                        //渐变速度
                        b = (String) list.get(6);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][41] = (byte) a;
                        //使用开关
                        bb = (boolean) list.get(0);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][42] = (byte) a;
                        //拆分
                        b = (String) list.get(7);
                        a = Integer.valueOf(b).intValue() + 1;
                        T1[i][j][43] = (byte) a;

                        //拆分反向
                        bb = (boolean) list.get(8);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][44] = (byte) a;
                        //时间A_L	时间B_H
                        a = Integer.valueOf((String) list.get(9)).intValue();
                        T1[i][j][45] = (byte) (a % 256);
                        T1[i][j][46] = (byte) (a / 256);
                    }


                    /////手动编程配置
                    List list66 = (List) hashMap.get("channelData");
                    Vector vector88 = null;
                    if (list66 != null) {
                        //////////勾选
                        int r = 0, yu = 0;
                        int[] bp1 = new int[4];
                        boolean[] bn = (boolean[]) list66.get(1);
                        for (int k = 0; k < bn.length; k++) {
                            r = k / 8;
                            //yu = 7 - (k % 8);
                            yu = k % 8;
                            if (bn[k]) {
                                bp1[r] = bp1[r] + (1 << yu);
                            }
                        }
                        for (int k = bn.length; k < 32; k++) {
                            r = k / 8;
                            yu = k % 8;
                            bp1[r] = bp1[r] + (1 << yu);
                        }
                        byte[] bp2 = new byte[4];
                        for (int k = 0; k < bp1.length; k++) {
                            bp2[k] = (byte) bp1[k];
                        }
                        gxObjects[i][j] = bp2;
                        //////////////

                        String[] ddTemp = (String[]) list66.get(2);
                        a = 0;
                        vector88 = (Vector) list66.get(0);
                        if (vector88 != null) {
                            a = vector88.size();
                        }
                        //总帧数
                        T1[i][j][47] = (byte) a;
                        //手动编程启用
                        T1[i][j][48] = (byte) 1;

                        //时差A_L	时差B_H
                        a = Integer.valueOf(ddTemp[2]).intValue();
                        T1[i][j][49] = (byte) (a % 256);
                        T1[i][j][50] = (byte) (a / 256);
                        //拆分
                        a = Integer.valueOf(ddTemp[0]).intValue() + 1;
                        T1[i][j][51] = (byte) a;
                        //拆分反向
                        a = 0;
                        if (!ddTemp[1].equals("0")) {
                            a = 1;
                        }
                        T1[i][j][52] = (byte) a;

                        Vector tpe = null;
                        int lenght = tt + 2;
                        if (vector88 != null) {
                            for (int n = 0; n < vector88.size(); n++) {
                                tpe = (Vector) vector88.get(n);
                                int timeTp = Integer.valueOf(tpe.get(1).toString()).intValue();
                                bTp[(tt + 2) * n] = (byte) (timeTp % 256);
                                bTp[(tt + 2) * n + 1] = (byte) (timeTp / 256);
                                if (lenght > tpe.size()) {
                                    lenght = tpe.size();
                                }
                                for (int k = 2; k < lenght; k++) {
                                    bTp[(tt + 2) * n + k] = Integer.valueOf(tpe.get(k).toString()).byteValue();
                                }
                            }
                        }

                    }
                }
                objects[i][j] = bTp;
            }
        }
        return T1;
    }

    /**
     * 声控素材块预览
     * @param denKuNum 灯组序号
     * @param suCaiNum 素材序号
     * @param startAddress 每个灯的起始地址
     */
    public static void sendShengKonData(int denKuNum,int suCaiNum,int[] startAddress) {
        Map map88 = (Map) Data.ShengKonSuCai[denKuNum][suCaiNum];

    }
}
