package com.boray.xiaoGuoDeng.reviewCode;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

import java.io.OutputStream;
import java.util.*;

public class ReviewUtils2 {

    /*
     * 声控预览模式指令
     */
    public static void modeReviewOrderByVoice(int model) {
        //FA 20 61 B6 10 02
        //01 //声控
        //01 //模式
        //00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 44
        byte[] buff = new byte[32];
        buff[0] = (byte) 0xFA;
        buff[1] = (byte) 0x20;
        buff[2] = (byte) 0x61;
        buff[3] = ZhiLingJi.TYPE;
        buff[4] = (byte) 0x10;
        buff[5] = (byte) 0x02;
        buff[6] = (byte) 0x01;
        buff[7] = (byte) model;
        buff[31] = ZhiLingJi.getJiaoYan(buff);
        if (Data.serialPort != null) {
            try {
                OutputStream os = Data.serialPort.getOutputStream();
                os.write(buff);
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 场景预览模式指令
     */
    public static void modeReviewOrder(int model) {
        //FA 20 61 B6 10 02
        //00 //场景
        //01 //模式
        //00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 44
        byte[] buff = new byte[32];
        buff[0] = (byte) 0xFA;
        buff[1] = (byte) 0x20;
        buff[2] = (byte) 0x61;
        buff[3] = ZhiLingJi.TYPE;
        buff[4] = (byte) 0x10;
        buff[5] = (byte) 0x02;
        buff[7] = (byte) model;
        buff[31] = ZhiLingJi.getJiaoYan(buff);
        if (Data.serialPort != null) {
            try {
                OutputStream os = Data.serialPort.getOutputStream();
                os.write(buff);
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * 组装数据
     */
    public static byte[] reviewData(byte[] data, int packetNum) {
        byte[] tp = new byte[518];
        tp[0] = 0x55;
        tp[1] = (byte) 0xAA;
        tp[2] = (byte) 0x88;
        tp[3] = (byte) ((packetNum - 1) / 256);
        tp[4] = (byte) ((packetNum - 1) % 256);
        if (data != null) {
            for (int i = 0; i < 512; i++) {
                tp[5 + i] = data[i];
            }
        }
        tp[517] = ZhiLingJi.getJiaoYan(tp);
        return tp;
    }

    public static void sendReviewCode() {
        if (Data.serialPort != null) {
            for (int i = 0; i < Data.packetNumList.size(); i++) {
                //System.out.println("包号："+(((int)Data.packetNumList.get(i))-1));
                byte[] buff = reviewData((byte[]) Data.valueList.get(i), (int) Data.packetNumList.get(i));
                try {
                    OutputStream os = Data.serialPort.getOutputStream();
                    os.write(buff);
                    os.flush();
                    Thread.sleep(180);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            int count = 5;
            while (count >= 0) {
                count--;
                if (Data.reviewMap.size() != 0) {
                    Set set = Data.reviewMap.keySet();
                    Iterator iterator = set.iterator();
                    while (iterator.hasNext()) {
                        int pk = -1;
                        try {
                            pk = (int) iterator.next();
                            //System.out.println(pk);
                            if (Data.reviewMap.get(pk) != null) {
                                byte[] buff = reviewData((byte[]) Data.reviewMap.get(pk), pk + 1);
                                try {
                                    OutputStream os = Data.serialPort.getOutputStream();
                                    os.write(buff);
                                    os.flush();
                                    Thread.sleep(200);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            set = Data.reviewMap.keySet();
                            iterator = set.iterator();
                            //System.err.println("error");
                        }
                    }
                }
            }
        }
    }

    public static void reviewOrder(int type, int[] values) {
        byte[] buff = new byte[32];
        buff[0] = (byte) 0xFA;
        buff[1] = (byte) 0x20;
        buff[2] = (byte) 0x61;
        buff[3] = ZhiLingJi.TYPE;
        switch (type) {
            case 1://rgb1
                buff[4] = (byte) 0x31;
                break;
            case 2://rgb2
                buff[4] = (byte) 0x32;
                break;
            case 3://rgb3
                buff[4] = (byte) 0x33;
                break;
            case 4://动作效果
                buff[4] = (byte) 0x01;
                break;
            default:
                break;
        }
        buff[5] = (byte) 0x02;
        buff[6] = (byte) 0x00;//场景
        buff[7] = (byte) values[0];//模式
        buff[8] = (byte) values[1];//组号
        buff[9] = (byte) values[2];//时间块号
        buff[31] = ZhiLingJi.getJiaoYan(buff);
        if (Data.serialPort != null) {
            try {
                OutputStream os = Data.serialPort.getOutputStream();
                os.write(buff);
                os.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //FA 20 61 B6 31 02 00 01 01 01 00 00 FF FF FF 01 01 E0 32 01 01 00 00 00 00 00 00 00 00 00 00 7A

    //场景预览
    public static void sceneReview() {
        byte[] dengKuTonDao = dengKuTonDao();//灯库通道定义
        byte[] xiaoGuoDengSuCaiLianBiao = xiaoGuoDengSuCaiLianBiao();//效果灯素材联表区
        byte[] suCaiData = suCaiQuData();//素材区数据
    }

    //灯库通道定义
    public static byte[] dengKuTonDao() {
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        byte[] buff = new byte[3 + 20];
        buff[0] = 0x55;
        buff[1] = (byte) 0xAA;
        buff[2] = (byte) ((byte) table.getRowCount() == 0 ? 0x00 : table.getRowCount());
        for (int i = 0; i < table.getRowCount(); i++) {
            int number = Integer.parseInt(Data.DengKuChannelCountList.get(i).toString());
            buff[3 + i] = (byte) number;
        }
        return buff;
    }

    //效果灯素材联表区
    public static byte[] xiaoGuoDengSuCaiLianBiao() {
        byte[] buff = new byte[8 + Data.AddSuCaiOrder.size() * 3];
        buff[0] = 0x55;
        buff[1] = (byte) 0xAA;
        buff[6] = (byte) (Data.AddSuCaiOrder.size() % 256);
        buff[7] = (byte) (Data.AddSuCaiOrder.size() / 256);//引导区
        //素材关联、素材步数
        for (int i = 0; i < Data.AddSuCaiOrder.size(); i++) {
            int denKuNum = Integer.parseInt(Data.AddSuCaiOrder.get(i).split("#")[0]);
            int suCaiNum = Integer.parseInt(Data.AddSuCaiOrder.get(i).split("#")[1]);
            buff[8 + i * 2] = (byte) (denKuNum + 1);
            buff[9 + i * 2] = (byte) (suCaiNum + 1);
            HashMap hashMap = (HashMap) Data.SuCaiObjects[denKuNum][suCaiNum];
            buff[8 + i + (Data.AddSuCaiOrder.size() * 2)] = 1;//默认一条通道
            if (hashMap != null) {
                List list66 = (List) hashMap.get("channelData");
                Vector vector88 = null;
                if (list66 != null) {
                    vector88 = (Vector) list66.get(0);
                    if (vector88 != null)
                        buff[8 + i + (Data.AddSuCaiOrder.size() * 2)] = (byte) vector88.size();
                }
            }
        }
        return buff;
    }

    //素材区数据
    public static byte[] suCaiQuData() {
        List<Byte> list = new ArrayList<>();
        for (int i = 0; i < Data.AddSuCaiOrder.size(); i++) {
            int denKuNum = Integer.parseInt(Data.AddSuCaiOrder.get(i).split("#")[0]);
            int suCaiNum = Integer.parseInt(Data.AddSuCaiOrder.get(i).split("#")[1]);
            HashMap hashMap = (HashMap) Data.SuCaiObjects[denKuNum][suCaiNum];
            //素材通道数
            int suCaiTonDaoShu = Integer.parseInt(Data.DengKuChannelCountList.get(denKuNum).toString());
            if (hashMap != null) {
                //动作图形
                Map map = (Map) hashMap.get("actionXiaoGuoData");
                List list2 = null;
                boolean bb = false;
                int a = 0;
                byte[] dongZuoTuXing = new byte[11];
                byte[] ziDingYi = new byte[5];
                byte[] beiSaiEr = new byte[42];
                if (map != null) {
                    int selected = Integer.valueOf((String) map.get("2"));
                    if (selected == 1) {
                        selected = 255;
                    } else if (selected > 1) {
                        selected = selected - 1;
                    }
                    dongZuoTuXing[0] = (byte) selected;
                    ////动作12点数据
                    int action = selected;
                    int[] p1 = null;
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
                            for (int j = 0; j < s.length; j++) {
                                p1[j] = Integer.valueOf(s[j]);
                            }
                        } else {
                            p1 = bezier.Data.ZB[0];
                        }
                    }
                    beiSaiEr[0] = (byte) selected;
                    for (int j = 1; j < 24; j++) {
                        beiSaiEr[j] = (byte) p1[i];
                    }
                    //运行速度
                    int yunXinSpeed = Integer.valueOf((String) map.get("3"));
                    dongZuoTuXing[1] = (byte) yunXinSpeed;
                    //使用开关 1启用/0关
                    String ss = (String) map.get("0");
                    if (ss != null && "true".equals(ss)) {
                        dongZuoTuXing[2] = (byte) 1;
                    }
                    //拆分    不拆分01/中间拆分02/两端拆分03
                    String[] tp1 = (String[]) map.get("4");
                    int cc = Integer.valueOf(tp1[0]) + 1;
                    dongZuoTuXing[3] = (byte) cc;
                    //拆分反向
                    //X轴反向    是1/否0
                    if ("true".equals(tp1[1])) {
                        a = 1;
                    } else {
                        a = 0;
                    }
                    dongZuoTuXing[5] = (byte) a;
                    //X半
                    if ("true".equals(tp1[2])) {
                        a = 1;
                    } else {
                        a = 0;
                    }
                    dongZuoTuXing[6] = (byte) a;
                    //Y轴反向
                    if ("true".equals(tp1[3])) {
                        a = 1;
                    } else {
                        a = 0;
                    }
                    dongZuoTuXing[7] = (byte) a;
                    //Y半
                    if ("true".equals(tp1[4])) {
                        a = 1;
                    } else {
                        a = 0;
                    }
                    dongZuoTuXing[8] = (byte) a;
                    //时间A_L	时间B_H
                    a = Integer.valueOf(tp1[5]).intValue() * 10;
                    dongZuoTuXing[9] = (byte) (a % 256);
                    dongZuoTuXing[10] = (byte) (a / 256);
                    ///////////////自定义动作数据
                    String[] values = (String[]) map.get("1");
                    if (values != null) {
                        if (values[0].equals("true")) {
                            ziDingYi[0] = 1;
                        }
                        for (int l = 1; l < ziDingYi.length; l++) {
                            ziDingYi[l] = (byte) Integer.valueOf(values[l]).intValue();
                        }
                    }
                }
                byte[] rgb = new byte[36];
                //RGB1
                list2 = (List) hashMap.get("rgb1Data");
                String[] tp2 = null;
                boolean[] bs = null;
                String b = "";
                if (list2 != null) {
                    //红色
                    tp2 = (String[]) list2.get(1);
                    a = Integer.valueOf(tp2[0]).intValue();
                    rgb[0] = (byte) a;
                    //绿色
                    a = Integer.valueOf(tp2[1]).intValue();
                    rgb[1] = (byte) a;
                    //蓝色
                    a = Integer.valueOf(tp2[2]).intValue();
                    rgb[2] = (byte) a;
                    //渐变类型
                    b = (String) list2.get(4);
                    a = Integer.valueOf(b).intValue();
                    rgb[3] = (byte) a;
                    //渐变
                    bb = (boolean) list2.get(5);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[4] = (byte) a;
                    //参与渐变勾选
                    bs = (boolean[]) list2.get(2);
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
                    rgb[5] = (byte) a;
                    //渐变速度
                    b = (String) list2.get(6);
                    a = Integer.valueOf(b).intValue();
                    rgb[6] = (byte) a;
                    //使用开关
                    bb = (boolean) list2.get(0);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[7] = (byte) a;
                    //拆分
                    b = (String) list2.get(7);
                    a = Integer.valueOf(b).intValue() + 1;
                    rgb[8] = (byte) a;
                    //拆分反向
                    bb = (boolean) list2.get(8);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[9] = (byte) a;
                    //时间A_L	时间B_H
                    a = Integer.valueOf((String) list2.get(9)).intValue() * 10;
                    rgb[10] = (byte) (a % 256);
                    rgb[11] = (byte) (a / 256);
                }
                //RGB2
                list2 = (List) hashMap.get("rgb2Data");
                if (list2 != null) {
                    //红色
                    tp2 = (String[]) list2.get(1);
                    a = Integer.valueOf(tp2[0]).intValue();
                    rgb[12] = (byte) a;
                    //绿色
                    a = Integer.valueOf(tp2[1]).intValue();
                    rgb[13] = (byte) a;
                    //蓝色
                    a = Integer.valueOf(tp2[2]).intValue();
                    rgb[14] = (byte) a;
                    //渐变类型
                    b = (String) list2.get(4);
                    a = Integer.valueOf(b).intValue();
                    rgb[15] = (byte) a;
                    //渐变
                    bb = (boolean) list2.get(5);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[16] = (byte) a;
                    //参与渐变勾选
                    bs = (boolean[]) list2.get(2);
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
                    rgb[17] = (byte) a;
                    //渐变速度
                    b = (String) list2.get(6);
                    a = Integer.valueOf(b).intValue();
                    rgb[18] = (byte) a;
                    //使用开关
                    bb = (boolean) list2.get(0);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[19] = (byte) a;
                    //拆分
                    b = (String) list2.get(7);
                    a = Integer.valueOf(b).intValue() + 1;
                    rgb[20] = (byte) a;
                    //拆分反向
                    bb = (boolean) list2.get(8);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[21] = (byte) a;
                    //时间A_L	时间B_H
                    a = Integer.valueOf((String) list2.get(9)).intValue() * 10;
                    rgb[22] = (byte) (a % 256);
                    rgb[23] = (byte) (a / 256);
                }
                //RGB3
                list2 = (List) hashMap.get("rgb3Data");
                if (list2 != null) {
                    //红色
                    tp2 = (String[]) list2.get(1);
                    a = Integer.valueOf(tp2[0]).intValue();
                    rgb[24] = (byte) a;
                    //绿色
                    a = Integer.valueOf(tp2[1]).intValue();
                    rgb[25] = (byte) a;
                    //蓝色
                    a = Integer.valueOf(tp2[2]).intValue();
                    rgb[26] = (byte) a;
                    //渐变类型
                    b = (String) list2.get(4);
                    a = Integer.valueOf(b).intValue();
                    rgb[27] = (byte) a;
                    //渐变
                    bb = (boolean) list2.get(5);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[28] = (byte) a;
                    //参与渐变勾选
                    bs = (boolean[]) list2.get(2);
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
                    rgb[29] = (byte) a;
                    //渐变速度
                    b = (String) list2.get(6);
                    a = Integer.valueOf(b).intValue();
                    rgb[30] = (byte) a;
                    //使用开关
                    bb = (boolean) list2.get(0);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[31] = (byte) a;
                    //拆分
                    b = (String) list2.get(7);
                    a = Integer.valueOf(b).intValue() + 1;
                    rgb[32] = (byte) a;

                    //拆分反向
                    bb = (boolean) list2.get(8);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[33] = (byte) a;
                    //时间A_L	时间B_H
                    a = Integer.valueOf((String) list2.get(9)).intValue() * 10;
                    rgb[34] = (byte) (a % 256);
                    rgb[35] = (byte) (a / 256);
                }
                List list66 = (List) hashMap.get("channelData");
                Vector vector88 = null;
                //通道控制配置数据
                byte[] tonDaoKonZhi = new byte[6];
                byte[] gouXuan = new byte[4];
                List<Byte> tonDaoBu = new ArrayList<>();
                for (int l = 0; l < suCaiTonDaoShu + 2; l++) {
                    tonDaoBu.add((byte) 0);
                }
                if (list66 != null) {
                    //////////勾选
                    int r = 0, yu = 0;
                    int[] bp1 = new int[4];
                    boolean[] bn = (boolean[]) list66.get(1);
                    for (int l = 0; l < bn.length; l++) {
                        r = l / 8;
                        yu = l % 8;
                        if (bn[l]) {
                            bp1[r] = bp1[r] + (1 << yu);
                        }
                    }
                    for (int l = bn.length; l < 32; l++) {
                        r = l / 8;
                        yu = l % 8;
                        bp1[r] = bp1[r] + (1 << yu);
                    }
                    for (int l = 0; l < bp1.length; l++) {
                        gouXuan[l] = (byte) bp1[l];
                    }

                    String[] ddTemp = (String[]) list66.get(2);
                    a = 0;
                    vector88 = (Vector) list66.get(0);
                    if (vector88 != null) {
                        a = vector88.size();
                    }
                    //总帧数
                    tonDaoKonZhi[0] = (byte) a;
                    //手动编程启用
                    tonDaoKonZhi[1] = (byte) 1;

                    //时差A_L	时差B_H
                    a = Integer.valueOf(ddTemp[2]).intValue();
                    tonDaoKonZhi[2] = (byte) (a % 256);
                    tonDaoKonZhi[3] = (byte) (a / 256);
                    //拆分
                    a = Integer.valueOf(ddTemp[0]).intValue() + 1;
                    tonDaoKonZhi[4] = (byte) a;
                    //拆分反向
                    a = 0;
                    if (!ddTemp[1].equals("0")) {
                        a = 1;
                    }
                    tonDaoKonZhi[5] = (byte) a;
                    //通道步XX编程
                    Vector tpe = null;
                    int lenght = suCaiTonDaoShu + 2;
                    if (vector88 != null) {
                        tonDaoBu.clear();
                        for (int n = 0; n < vector88.size(); n++) {
                            tpe = (Vector) vector88.get(n);
                            int timeTp = Integer.valueOf(tpe.get(1).toString()).intValue();
                            if (lenght > tpe.size()) {
                                lenght = tpe.size();
                            }
                            tonDaoBu.add((byte) (timeTp % 256));
                            tonDaoBu.add((byte) (timeTp / 256));
                            for (int l = 2; l < lenght; l++) {
                                tonDaoBu.add(Integer.valueOf(tpe.get(l).toString()).byteValue());
                            }
                        }
                    }
                }
                integrate(list, dongZuoTuXing);//自定义动作
                integrate(list, rgb);//3RGB
                integrate(list, tonDaoKonZhi);//手动配置
                integrate2(list, tonDaoBu);//通道步数
                integrate(list, ziDingYi);//自定义数据
                integrate(list, gouXuan);//勾选数据
                integrate(list, beiSaiEr);//贝塞尔曲线
            } else {
                for (int j = 0; j < 146 + suCaiTonDaoShu; j++) {//没有数据默认补零，一个通道
                    list.add((byte) 00);
                }
            }
        }
        byte[] buff = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            buff[i] = list.get(i);
        }
        return buff;
    }

    //数据整合
    public static void integrate(List<Byte> list, byte[] buff) {
        for (int i = 0; i < buff.length; i++) {
            list.add(buff[i]);
        }
    }

    public static void integrate2(List<Byte> list, List<Byte> tonDaoBu) {
        for (int i = 0; i < tonDaoBu.size(); i++) {
            list.add(tonDaoBu.get(i));
        }
    }

}
