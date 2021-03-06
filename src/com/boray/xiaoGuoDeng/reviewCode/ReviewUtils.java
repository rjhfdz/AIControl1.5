package com.boray.xiaoGuoDeng.reviewCode;

import java.io.OutputStream;
import java.util.*;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.xiaoGuoDeng.UI.DefineJLable;
import com.boray.xiaoGuoDeng.UI.DefineJLable3;

import javax.swing.*;

public class ReviewUtils {

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

    //场景预览 素材
    public static byte[] sceneSuCaiReview(int model) {
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
        int size = 0;//素材总数
        for (int i = 0; i < table.getRowCount(); i++) {
            Map map = (Map) Data.suCaiMap.get(table.getValueAt(i, 1));
            if (map != null) {
                for (int j = 0; j < btns.length; j++) {
                    List abc = (List) map.get("" + j);
                    if (abc != null) {
                        size += abc.size();
                    }
                }
            }
        }
        List<String> str = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            Map map = (Map) Data.suCaiMap.get(table.getValueAt(i, 1));
            if (map != null) {
                int count = 0;
                for (int j = 0; j < btns.length; j++) {
                    List abc = (List) map.get("" + j);
                    if (abc != null) {
                        count += abc.size();
                    }
                }
                for (int j = 0; j < count; j++) {
                    str.add(i + "#" + j);
                }
            }
        }

        byte[] dengKuTonDao = dengKuTonDao();//灯库通道定义

        byte[] dengKuMieZu = new byte[30];//灯库每组数量区
        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + model);//时间轴
        NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//所有灯具
        Map<Integer, TreeSet<Integer>> map = new HashMap<>();
        List<Integer> listDengKu = new ArrayList<>();
        List<String> suCai = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            //获得灯库id
            int number = Integer.valueOf(timeBlockPanels[i + 1].getName()).intValue();
            int tt = 0, a = 0;
            if (number - 1 < Data.GroupOfLightList.size()) {
                TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(number - 1);
                if (!treeSet.isEmpty()) {
                    a = (int) treeSet.first();
                    String typeString = table3.getValueAt(a, 3).toString();
                    tt = Integer.valueOf(typeString.split("#")[0].substring(2)).intValue();
                }
            }
            if (timeBlockPanels[i + 1].isVisible()) {
                if (!map.containsKey(tt)) {
                    TreeSet<Integer> set = new TreeSet<>();
                    map.put(tt, set);
                    listDengKu.add(tt);
                }
            }
            int count = 0;
            for (int j = 0; j < 20; j++) {
                if (timeBlockPanels[i + 1].isVisible()) {
                    if (timeBlockPanels[i + 1].getComponentCount() > j) {
                        DefineJLable lable = (DefineJLable) timeBlockPanels[i + 1].getComponent(j);
                        if (lable.getText().contains("√")) {
                            String s = lable.getText().substring(lable.getText().indexOf("(") + 1, lable.getText().indexOf(")"));
                            int integer = Integer.parseInt(s);
                            suCai.add((tt - 1) + "#" + (integer - 1));
                            TreeSet<Integer> set = map.get(tt);
                            set.add(integer);
                            count++;
                        }
                    }
                }
            }
            dengKuMieZu[i] = (byte) count;
        }

//        List<String> list = getdengKuSuCai(map, listDengKu);

        byte[] xiaoGuoDengSuCaiLianBiao = xiaoGuoDengSuCaiLianBiao(suCai);//效果灯素材联表区
        byte[] suCaiData = suCaiQuData(suCai);//素材区数据
        byte[] bytes = new byte[dengKuTonDao.length + dengKuMieZu.length + xiaoGuoDengSuCaiLianBiao.length + suCaiData.length];
        for (int i = 0; i < dengKuTonDao.length; i++) {
            bytes[i] = dengKuTonDao[i];
        }
        for (int i = 0; i < dengKuMieZu.length; i++) {
            bytes[dengKuTonDao.length + i] = dengKuMieZu[i];
        }
        for (int i = 0; i < xiaoGuoDengSuCaiLianBiao.length; i++) {
            bytes[dengKuTonDao.length + dengKuMieZu.length + i] = xiaoGuoDengSuCaiLianBiao[i];
        }
        for (int i = 0; i < suCaiData.length; i++) {
            bytes[dengKuTonDao.length + dengKuMieZu.length + xiaoGuoDengSuCaiLianBiao.length + i] = suCaiData[i];
        }

        return bytes;
    }

    //场景预览
    public static byte[] sceneChangJingReview(int model) {
        byte[] xiaoGuoDengChangJing = xiaoGuoDengChangJing(model);//效果灯场景
        return xiaoGuoDengChangJing;
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
    public static byte[] xiaoGuoDengSuCaiLianBiao(List<String> str) {
        byte[] buff = new byte[8 + str.size() * 3];
        buff[0] = 0x55;
        buff[1] = (byte) 0xAA;
        buff[6] = (byte) (str.size() % 256);
        buff[7] = (byte) (str.size() / 256);//引导区
        //素材关联、素材步数
        for (int i = 0; i < str.size(); i++) {
            int denKuNum = Integer.parseInt(str.get(i).split("#")[0]);
            int suCaiNum = Integer.parseInt(str.get(i).split("#")[1]);
            buff[8 + i * 2] = (byte) (denKuNum + 1);
            buff[9 + i * 2] = (byte) (suCaiNum + 1);
            HashMap hashMap = (HashMap) Data.SuCaiObjects[denKuNum][suCaiNum];
            buff[8 + i + (str.size() * 2)] = 1;//默认一条通道
            if (hashMap != null) {
                List list66 = (List) hashMap.get("channelData");
                Vector vector88 = null;
                if (list66 != null) {
                    vector88 = (Vector) list66.get(0);
                    if (vector88 != null)
                        buff[8 + i + (str.size() * 2)] = (byte) vector88.size();
                }
            }
        }
        return buff;
    }

    //素材区数据
    public static byte[] suCaiQuData(List<String> str) {
        List<Byte> list = new ArrayList<>();
        for (int i = 0; i < str.size(); i++) {
            int denKuNum = Integer.parseInt(str.get(i).split("#")[0]);
            int suCaiNum = Integer.parseInt(str.get(i).split("#")[1]);
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
                byte[] beiSaiEr = new byte[25];
                if (map != null) {
                    int selected = Integer.valueOf((String) map.get("2"));
                    if (selected == 1) {
                        selected = 255;
                    } else if (selected > 1) {
                        selected = selected - 1;
                    }
                    dongZuoTuXing[0] = (byte) selected;
                    ////动作12点数据
                    int action = selected + 1;
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
                                p1[j] = Double.valueOf(s[j]).intValue();
                            }
                        } else {
                            p1 = bezier.Data.ZB[0];
                        }
                    }
                    beiSaiEr[0] = (byte) selected;
                    for (int j = 0; j < 24; j++) {
                        beiSaiEr[j + 1] = (byte) p1[j];
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
                    boolean b = map.containsKey("5") ? (boolean) map.get("5") : false;
                    //X轴反向    是1/否0
                    if ("true".equals(tp1[1])) {
                        a = 1;
                    } else {
                        a = 0;
                    }
                    dongZuoTuXing[5] = (byte) a;
                    if (b) {
                        if (a == 1) {
                            dongZuoTuXing[5] = (byte) 0x81;
                        } else {
                            dongZuoTuXing[5] = (byte) 0x80;
                        }
                    }
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
                    a = Integer.valueOf(tp1[5]).intValue();
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
                    a = Integer.valueOf((String) list2.get(9)).intValue();
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
                    a = Integer.valueOf((String) list2.get(9)).intValue();
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
                    a = Integer.valueOf((String) list2.get(9)).intValue();
                    rgb[34] = (byte) (a % 256);
                    rgb[35] = (byte) (a / 256);
                }
                List list66 = (List) hashMap.get("channelData");
                Vector vector88 = null;
                //通道控制配置数据
                byte[] tonDaoKonZhi = new byte[6];
                byte[] gouXuan = new byte[4];
                byte[] gouXuan2 = new byte[4];
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
                    //第二排勾选
                    bp1 = new int[4];
                    bn = (boolean[]) list66.get(3);
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
                        gouXuan2[l] = (byte) bp1[l];
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
                integrate(list, gouXuan2);//渐变勾选 4
                integrate(list, new byte[2]);//备用 2
            } else {
                for (int j = 0; j < 95 + suCaiTonDaoShu; j++) {//没有数据默认补零，一个通道
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

    //效果灯场景
    public static byte[] xiaoGuoDengChangJing(int sc) {
        //引导区
        byte[] temp = new byte[2560];
        temp[0] = 0x55;
        temp[1] = (byte) 0xAA;
        short[][] a = new short[20][4];//场景启用
        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + sc);
        int j = 0, yu = 0;
        int maxTime = 0, tp6 = 0;
        for (int i = 0; i < 20; i++) {
            for (int k = 1; k < 31; k++) {
                j = (k - 1) / 8;
                yu = 7 - ((k - 1) % 8);
                if (timeBlockPanels[k].isVisible()) {
                    if (timeBlockPanels[k].getComponentCount() > i) {
                        DefineJLable lable = (DefineJLable) timeBlockPanels[k].getComponent(i);
                        if (lable.getText().contains("√")) {
                            tp6 = (lable.getX() + lable.getWidth()) / 5;
                            if (tp6 > maxTime) {
                                maxTime = tp6;
                            }
                            a[i][j] = (short) (a[i][j] + (1 << yu));
                        }
                    }
                }
            }
            for (int k = 0; k < 4; k++) {
                temp[20 + (i * 4) + k] = (byte) a[i][k];
            }
        }
        temp[3] = (byte) (maxTime % 256);
        temp[4] = (byte) (maxTime / 256);
        for (int k = 1; k < 31; k++) {
            temp[100 + (k - 1) * 81] = (byte) timeBlockPanels[k].getComponentCount();
            for (int n = 0; n < timeBlockPanels[k].getComponentCount(); n++) {
                DefineJLable lable = (DefineJLable) timeBlockPanels[k].getComponent(n);
                int start = lable.getX() / 5;
                int end = (lable.getX() + lable.getWidth()) / 5;
                temp[100 + (k - 1) * 81 + 1 + n * 4] = (byte) (start % 256);
                temp[100 + (k - 1) * 81 + 2 + n * 4] = (byte) (start / 256);

                temp[100 + (k - 1) * 81 + 3 + n * 4] = (byte) (end % 256);
                temp[100 + (k - 1) * 81 + 4 + n * 4] = (byte) (end / 256);
            }
        }
        //数据
        byte[][][] t2 = new byte[30][20][4];
        NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//所有灯具
        for (int i = 0; i < 30; i++) {
            //获得灯库id
            int number = Integer.valueOf(timeBlockPanels[i + 1].getName()).intValue();
            int tt = 0, b = 0;
            if (number - 1 < Data.GroupOfLightList.size()) {
                TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(number - 1);
                if (!treeSet.isEmpty()) {
                    b = (int) treeSet.first();
                    String typeString = table3.getValueAt(b, 3).toString();
                    tt = Integer.valueOf(typeString.split("#")[0].substring(2)).intValue();
                }
            }
            for (int k = 0; k < 20; k++) {
                if (timeBlockPanels[i + 1].isVisible()) {
                    if (timeBlockPanels[i + 1].getComponentCount() > k) {
                        DefineJLable lable = (DefineJLable) timeBlockPanels[i + 1].getComponent(k);
                        if (lable.getText().contains("√")) {
                            String s = lable.getText().substring(lable.getText().indexOf("(") + 1, lable.getText().indexOf(")"));
                            int integer = Integer.parseInt(s);
                            t2[i][k][0] = (byte) tt;
                            t2[i][k][1] = (byte) integer;
                        }
                    }
                }
            }
        }
        List<Byte> list = new ArrayList<>();
        for (int i = 0; i < temp.length; i++) {
            list.add(temp[i]);
        }
        for (int i = 0; i < 30; i++) {
            for (int k = 0; k < 20; k++) {
                list.add(t2[i][k][0]);
                list.add(t2[i][k][1]);
                list.add(t2[i][k][2]);
                list.add(t2[i][k][3]);
            }
        }
        byte[] buff = new byte[list.size()];
        for (int i = 0; i < buff.length; i++) {
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

    public static List<String> getdengKuSuCai(Map<Integer, TreeSet<Integer>> map, List<Integer> dengKu) {
        List<String> list = new ArrayList<>();
        for (Integer key : dengKu) {
            TreeSet<Integer> set = map.get(key);
            for (Integer value : set) {
                list.add((key - 1) + "#" + (value - 1));
            }
        }
        return list;
    }

    /**
     * 场景多灯预览 或 数据打包
     *
     * @param model
     * @param flag
     * @return
     */
    public static byte[] changJingDuoDengReview(int model, boolean flag) {
        //场景步骤信息表和场景配置
        byte[] t1 = new byte[126];
        t1[0] = (byte) 0xFE;
        t1[1] = (byte) 0xA3;
        Vector tp = null, vector99 = null;
        for (int i = 0; i < 24; i++) {//24模式总步数
            vector99 = (Vector) Data.XiaoGuoDengModelDmxMap.get("TableData" + (i + 1));
            if (vector99 == null || vector99.size() == 0)//默认一步
                t1[i + 6] = (byte) 1;
            else
                t1[i + 6] = (byte) vector99.size();
            //24模式全局设置
            DefineJLable3 lable3 = (DefineJLable3) MainUi.map.get("SuoYouDengZuLable" + (i + 1));
            int maxTime = lable3.getWidth() / 5;
            t1[(i * 4) + 30] = (byte) (lable3.getText().contains("√") ? 1 : 0);
            t1[(i * 4) + 32] = (byte) (maxTime % 256);
            t1[(i * 4) + 33] = (byte) (maxTime / 256);
        }
        List<Byte> bytes = new ArrayList<>();
        integrate(bytes, t1);
        boolean[] tbs = null;
        int r = 0, yu = 0, a = 0;
        if (flag) {//如果为true 展示24模式的数据 否则展示单个模式
            for (int i = 0; i < 24; i++) {
                DefineJLable3 lable3 = (DefineJLable3) MainUi.map.get("SuoYouDengZuLable" + (i + 1));
                if (lable3.getText().contains("√")) {
                    //通道勾选
                    byte[][] channalGouXuan = new byte[2][64];//通道勾选 渐变勾选
                    tbs = (boolean[]) Data.XiaoGuoDengModelDmxMap.get("GouXuanValue" + (i + 1));
                    if (tbs != null) {
                        for (int k = 0; k < tbs.length; k++) {
                            r = k / 8;
                            yu = 7 - (k % 8);
                            if (tbs[k]) {
                                channalGouXuan[0][r] = (byte) (Byte.toUnsignedInt(channalGouXuan[0][r]) + (1 << yu));
                            }
                        }
                    }
                    //渐变勾选
                    tbs = (boolean[]) Data.XiaoGuoDengModelDmxMap.get("JianBianGouXuanValue" + (i + 1));
                    if (tbs != null) {
                        for (int k = 0; k < tbs.length; k++) {
                            r = k / 8;
                            yu = 7 - (k % 8);
                            if (tbs[k]) {
                                channalGouXuan[1][r] = (byte) (Byte.toUnsignedInt(channalGouXuan[1][r]) + (1 << yu));
                            }
                        }
                    }
                    integrate(bytes, channalGouXuan[0]);
                    integrate(bytes, channalGouXuan[1]);
                    //步编辑数据
                    vector99 = (Vector) Data.XiaoGuoDengModelDmxMap.get("TableData" + (i + 1));
                    if (vector99 == null || vector99.size() == 0) {//默认一步
                        integrate(bytes, new byte[512]);
                    } else {
                        for (int j = 0; j < vector99.size(); j++) {
                            tp = (Vector) vector99.get(j);
                            a = Integer.valueOf(tp.get(1).toString());
                            bytes.add((byte) (a % 256));
                            bytes.add((byte) (a / 256));
                            for (int k = 2; k < 512; k++) {
                                a = Integer.valueOf(tp.get(k).toString());
                                bytes.add((byte) a);
                            }
                        }
                    }
                }
            }
        } else {
            //通道勾选
            byte[][] channalGouXuan = new byte[2][64];//通道勾选 渐变勾选
            tbs = (boolean[]) Data.XiaoGuoDengModelDmxMap.get("GouXuanValue" + model);
            if (tbs != null) {
                for (int k = 0; k < tbs.length; k++) {
                    r = k / 8;
                    yu = 7 - (k % 8);
                    if (tbs[k]) {
                        channalGouXuan[0][r] = (byte) (Byte.toUnsignedInt(channalGouXuan[0][r]) + (1 << yu));
                    }
                }
            }
            //渐变勾选
            tbs = (boolean[]) Data.XiaoGuoDengModelDmxMap.get("JianBianGouXuanValue" + model);
            if (tbs != null) {
                for (int k = 0; k < tbs.length; k++) {
                    r = k / 8;
                    yu = 7 - (k % 8);
                    if (tbs[k]) {
                        channalGouXuan[1][r] = (byte) (Byte.toUnsignedInt(channalGouXuan[1][r]) + (1 << yu));
                    }
                }
            }
            integrate(bytes, channalGouXuan[0]);
            integrate(bytes, channalGouXuan[1]);
            //步编辑数据
            vector99 = (Vector) Data.XiaoGuoDengModelDmxMap.get("TableData" + model);
            if (vector99 == null || vector99.size() == 0) {//默认一步
                integrate(bytes, new byte[512]);
            } else {
                for (int j = 0; j < vector99.size(); j++) {
                    tp = (Vector) vector99.get(j);
                    a = Integer.valueOf(tp.get(1).toString());
                    bytes.add((byte) (a % 256));
                    bytes.add((byte) (a / 256));
                    for (int k = 2; k < 512; k++) {
                        a = Integer.valueOf(tp.get(k).toString());
                        bytes.add((byte) a);
                    }
                }
            }
        }
        byte[] data = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            data[i] = bytes.get(i);
        }
        return data;
    }
}
