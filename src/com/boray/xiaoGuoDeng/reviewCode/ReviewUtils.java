package com.boray.xiaoGuoDeng.reviewCode;

import java.io.OutputStream;
import java.util.*;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.xiaoGuoDeng.UI.DefineJLable;

import javax.swing.*;

public class ReviewUtils {

    /*
     * ����Ԥ��ģʽָ��
     */
    public static void modeReviewOrderByVoice(int model) {
        //FA 20 61 B6 10 02
        //01 //����
        //01 //ģʽ
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
     * ����Ԥ��ģʽָ��
     */
    public static void modeReviewOrder(int model) {
        //FA 20 61 B6 10 02
        //00 //����
        //01 //ģʽ
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
     * ��װ����
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
                //System.out.println("���ţ�"+(((int)Data.packetNumList.get(i))-1));
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
            case 4://����Ч��
                buff[4] = (byte) 0x01;
                break;
            default:
                break;
        }
        buff[5] = (byte) 0x02;
        buff[6] = (byte) 0x00;//����
        buff[7] = (byte) values[0];//ģʽ
        buff[8] = (byte) values[1];//���
        buff[9] = (byte) values[2];//ʱ����
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

    //����Ԥ�� �ز�
    public static byte[] sceneSuCaiReview(int model) {
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
        int size = 0;//�ز�����
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

        byte[] dengKuTonDao = dengKuTonDao();//�ƿ�ͨ������
        byte[] xiaoGuoDengSuCaiLianBiao = xiaoGuoDengSuCaiLianBiao(size, str);//Ч�����ز�������
        byte[] suCaiData = suCaiQuData(str);//�ز�������
        byte[] bytes = new byte[dengKuTonDao.length + xiaoGuoDengSuCaiLianBiao.length + suCaiData.length];
        for (int i = 0; i < dengKuTonDao.length; i++) {
            bytes[i] = dengKuTonDao[i];
        }
        for (int i = 0; i < xiaoGuoDengSuCaiLianBiao.length; i++) {
            bytes[dengKuTonDao.length + i] = xiaoGuoDengSuCaiLianBiao[i];
        }
        for (int i = 0; i < suCaiData.length; i++) {
            bytes[dengKuTonDao.length + xiaoGuoDengSuCaiLianBiao.length + i] = suCaiData[i];
        }

        return bytes;
    }

    //����Ԥ��
    public static byte[] sceneChangJingReview(int model) {
        byte[] xiaoGuoDengChangJing = xiaoGuoDengChangJing(model);//Ч���Ƴ���
        return xiaoGuoDengChangJing;
    }

    //�ƿ�ͨ������
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

    //Ч�����ز�������
    public static byte[] xiaoGuoDengSuCaiLianBiao(int size, List<String> str) {
        byte[] buff = new byte[8 + size * 3];
        buff[0] = 0x55;
        buff[1] = (byte) 0xAA;
        buff[6] = (byte) (size % 256);
        buff[7] = (byte) (size / 256);//������
        //�زĹ������زĲ���
        for (int i = 0; i < str.size(); i++) {
            int denKuNum = Integer.parseInt(str.get(i).split("#")[0]);
            int suCaiNum = Integer.parseInt(str.get(i).split("#")[1]);
            buff[8 + i * 2] = (byte) (denKuNum + 1);
            buff[9 + i * 2] = (byte) (suCaiNum + 1);
            HashMap hashMap = (HashMap) Data.SuCaiObjects[denKuNum][suCaiNum];
            buff[8 + i + (size * 2)] = 1;//Ĭ��һ��ͨ��
            if (hashMap != null) {
                List list66 = (List) hashMap.get("channelData");
                Vector vector88 = null;
                if (list66 != null) {
                    vector88 = (Vector) list66.get(0);
                    if (vector88 != null)
                        buff[8 + i + (size * 2)] = (byte) vector88.size();
                }
            }
        }
        return buff;
    }

    //�ز�������
    public static byte[] suCaiQuData(List<String> str) {
        List<Byte> list = new ArrayList<>();
        for (int i = 0; i < str.size(); i++) {
            int denKuNum = Integer.parseInt(str.get(i).split("#")[0]);
            int suCaiNum = Integer.parseInt(str.get(i).split("#")[1]);
            HashMap hashMap = (HashMap) Data.SuCaiObjects[denKuNum][suCaiNum];
            //�ز�ͨ����
            int suCaiTonDaoShu = Integer.parseInt(Data.DengKuChannelCountList.get(denKuNum).toString());
            if (hashMap != null) {
                //����ͼ��
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
                    ////����12������
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
                    for (int j = 0; j < 24; j++) {
                        beiSaiEr[j + 1] = (byte) p1[j];
                    }
                    //�����ٶ�
                    int yunXinSpeed = Integer.valueOf((String) map.get("3"));
                    dongZuoTuXing[1] = (byte) yunXinSpeed;
                    //ʹ�ÿ��� 1����/0��
                    String ss = (String) map.get("0");
                    if (ss != null && "true".equals(ss)) {
                        dongZuoTuXing[2] = (byte) 1;
                    }
                    //���    �����01/�м���02/���˲��03
                    String[] tp1 = (String[]) map.get("4");
                    int cc = Integer.valueOf(tp1[0]) + 1;
                    dongZuoTuXing[3] = (byte) cc;
                    //��ַ���
                    //X�ᷴ��    ��1/��0
                    if ("true".equals(tp1[1])) {
                        a = 1;
                    } else {
                        a = 0;
                    }
                    dongZuoTuXing[5] = (byte) a;
                    //X��
                    if ("true".equals(tp1[2])) {
                        a = 1;
                    } else {
                        a = 0;
                    }
                    dongZuoTuXing[6] = (byte) a;
                    //Y�ᷴ��
                    if ("true".equals(tp1[3])) {
                        a = 1;
                    } else {
                        a = 0;
                    }
                    dongZuoTuXing[7] = (byte) a;
                    //Y��
                    if ("true".equals(tp1[4])) {
                        a = 1;
                    } else {
                        a = 0;
                    }
                    dongZuoTuXing[8] = (byte) a;
                    //ʱ��A_L	ʱ��B_H
                    a = Integer.valueOf(tp1[5]).intValue() * 10;
                    dongZuoTuXing[9] = (byte) (a % 256);
                    dongZuoTuXing[10] = (byte) (a / 256);
                    ///////////////�Զ��嶯������
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
                    //��ɫ
                    tp2 = (String[]) list2.get(1);
                    a = Integer.valueOf(tp2[0]).intValue();
                    rgb[0] = (byte) a;
                    //��ɫ
                    a = Integer.valueOf(tp2[1]).intValue();
                    rgb[1] = (byte) a;
                    //��ɫ
                    a = Integer.valueOf(tp2[2]).intValue();
                    rgb[2] = (byte) a;
                    //��������
                    b = (String) list2.get(4);
                    a = Integer.valueOf(b).intValue();
                    rgb[3] = (byte) a;
                    //����
                    bb = (boolean) list2.get(5);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[4] = (byte) a;
                    //���뽥�乴ѡ
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
                    //�����ٶ�
                    b = (String) list2.get(6);
                    a = Integer.valueOf(b).intValue();
                    rgb[6] = (byte) a;
                    //ʹ�ÿ���
                    bb = (boolean) list2.get(0);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[7] = (byte) a;
                    //���
                    b = (String) list2.get(7);
                    a = Integer.valueOf(b).intValue() + 1;
                    rgb[8] = (byte) a;
                    //��ַ���
                    bb = (boolean) list2.get(8);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[9] = (byte) a;
                    //ʱ��A_L	ʱ��B_H
                    a = Integer.valueOf((String) list2.get(9)).intValue() * 10;
                    rgb[10] = (byte) (a % 256);
                    rgb[11] = (byte) (a / 256);
                }
                //RGB2
                list2 = (List) hashMap.get("rgb2Data");
                if (list2 != null) {
                    //��ɫ
                    tp2 = (String[]) list2.get(1);
                    a = Integer.valueOf(tp2[0]).intValue();
                    rgb[12] = (byte) a;
                    //��ɫ
                    a = Integer.valueOf(tp2[1]).intValue();
                    rgb[13] = (byte) a;
                    //��ɫ
                    a = Integer.valueOf(tp2[2]).intValue();
                    rgb[14] = (byte) a;
                    //��������
                    b = (String) list2.get(4);
                    a = Integer.valueOf(b).intValue();
                    rgb[15] = (byte) a;
                    //����
                    bb = (boolean) list2.get(5);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[16] = (byte) a;
                    //���뽥�乴ѡ
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
                    //�����ٶ�
                    b = (String) list2.get(6);
                    a = Integer.valueOf(b).intValue();
                    rgb[18] = (byte) a;
                    //ʹ�ÿ���
                    bb = (boolean) list2.get(0);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[19] = (byte) a;
                    //���
                    b = (String) list2.get(7);
                    a = Integer.valueOf(b).intValue() + 1;
                    rgb[20] = (byte) a;
                    //��ַ���
                    bb = (boolean) list2.get(8);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[21] = (byte) a;
                    //ʱ��A_L	ʱ��B_H
                    a = Integer.valueOf((String) list2.get(9)).intValue() * 10;
                    rgb[22] = (byte) (a % 256);
                    rgb[23] = (byte) (a / 256);
                }
                //RGB3
                list2 = (List) hashMap.get("rgb3Data");
                if (list2 != null) {
                    //��ɫ
                    tp2 = (String[]) list2.get(1);
                    a = Integer.valueOf(tp2[0]).intValue();
                    rgb[24] = (byte) a;
                    //��ɫ
                    a = Integer.valueOf(tp2[1]).intValue();
                    rgb[25] = (byte) a;
                    //��ɫ
                    a = Integer.valueOf(tp2[2]).intValue();
                    rgb[26] = (byte) a;
                    //��������
                    b = (String) list2.get(4);
                    a = Integer.valueOf(b).intValue();
                    rgb[27] = (byte) a;
                    //����
                    bb = (boolean) list2.get(5);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[28] = (byte) a;
                    //���뽥�乴ѡ
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
                    //�����ٶ�
                    b = (String) list2.get(6);
                    a = Integer.valueOf(b).intValue();
                    rgb[30] = (byte) a;
                    //ʹ�ÿ���
                    bb = (boolean) list2.get(0);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[31] = (byte) a;
                    //���
                    b = (String) list2.get(7);
                    a = Integer.valueOf(b).intValue() + 1;
                    rgb[32] = (byte) a;

                    //��ַ���
                    bb = (boolean) list2.get(8);
                    a = 0;
                    if (bb) {
                        a = 1;
                    }
                    rgb[33] = (byte) a;
                    //ʱ��A_L	ʱ��B_H
                    a = Integer.valueOf((String) list2.get(9)).intValue() * 10;
                    rgb[34] = (byte) (a % 256);
                    rgb[35] = (byte) (a / 256);
                }
                List list66 = (List) hashMap.get("channelData");
                Vector vector88 = null;
                //ͨ��������������
                byte[] tonDaoKonZhi = new byte[6];
                byte[] gouXuan = new byte[4];
                List<Byte> tonDaoBu = new ArrayList<>();
                for (int l = 0; l < suCaiTonDaoShu + 2; l++) {
                    tonDaoBu.add((byte) 0);
                }
                if (list66 != null) {
                    //////////��ѡ
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
                    //��֡��
                    tonDaoKonZhi[0] = (byte) a;
                    //�ֶ��������
                    tonDaoKonZhi[1] = (byte) 1;

                    //ʱ��A_L	ʱ��B_H
                    a = Integer.valueOf(ddTemp[2]).intValue();
                    tonDaoKonZhi[2] = (byte) (a % 256);
                    tonDaoKonZhi[3] = (byte) (a / 256);
                    //���
                    a = Integer.valueOf(ddTemp[0]).intValue() + 1;
                    tonDaoKonZhi[4] = (byte) a;
                    //��ַ���
                    a = 0;
                    if (!ddTemp[1].equals("0")) {
                        a = 1;
                    }
                    tonDaoKonZhi[5] = (byte) a;
                    //ͨ����XX���
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
                integrate(list, dongZuoTuXing);//�Զ��嶯��
                integrate(list, rgb);//3RGB
                integrate(list, tonDaoKonZhi);//�ֶ�����
                integrate2(list, tonDaoBu);//ͨ������
                integrate(list, ziDingYi);//�Զ�������
                integrate(list, gouXuan);//��ѡ����
                integrate(list, beiSaiEr);//����������
            } else {
                for (int j = 0; j < 146 + suCaiTonDaoShu; j++) {//û������Ĭ�ϲ��㣬һ��ͨ��
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

    //Ч���Ƴ���
    public static byte[] xiaoGuoDengChangJing(int sc) {
        //������
        byte[] temp = new byte[2560];
        temp[0] = 0x55;
        temp[1] = (byte) 0xAA;
        short[][] a = new short[20][4];//��������
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
                        if (lable.getText().contains("��")) {
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
        //����
        byte[][][] t2 = new byte[30][20][4];
        NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//���еƾ�
        for (int i = 0; i < 30; i++) {
            //��õƿ�id
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
                        String s = lable.getText().substring(lable.getText().indexOf("(") + 1, lable.getText().indexOf(")"));
                        int integer = Integer.parseInt(s);
                        t2[i][k][0] = (byte) tt;
                        t2[i][k][1] = (byte) integer;
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

    //��������
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
