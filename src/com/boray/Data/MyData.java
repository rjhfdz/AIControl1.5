package com.boray.Data;

public class MyData {
    public static String ShengKonModel = "1";
    public static String ShengKonHuanJingModel = "1";
    private static String[] airModel = {
            "1(格力Z4835)", "2(美的红外)", "3(奥克斯红外)", "4(大金转换器TYC-VRV)", "5(格力红外)", "6(海尔红外)"
            , "7(约克红外)", "8(格力XK27)", "9(格力Z4535)", "10(LG红外6711A20091M)", "11(美的红外RN02H-BG)", "12(志高红外ZH/JT-01)"
            , "13(海尔红外)", "14(海信红外HYC_Q01)", "15(海信红外Y_H1_02C)", "16(YAIR红外-TY_DQ_1004)", "17(CHEBLO红外-KK28A-1)"
            , "18(麦克维尔红外-MCGS01)", "19(春兰红外)", "20(大金红外-BRC4C158)", "21(格力红外-YAPOF)", "22(长虹红外-KK22A)", "23(日立红外-PC_LH6Q)"
            , "24(海信485-集控盒B541)", "25(志高红外-ZH/YT-01)", "26(海尔红外-Haier)", "27(美的红外-RM12A/BGF)", "28(海尔红外-品牌通KT-A01)", "29(格力红外-GREE)", "30(-)", "31(-)"
            , "32(伴菱红外-伴菱)", "33(-)", "34(-)", "35(-)", "36(-)", "37(-)", "38(-)", "39(台湾三洋红外-SANLUX)", "40(海信红外-HYC-W01)", "41(创维红外-Skeyworth)"
            , "42(Y_H1_02(C))", "43(志高红外)", "44(TCL红外)", "45(志高红外(ZH/JT-03))", "46(新迎燕 KKCQ_1X)", "47(美的 RMO05/BG(T)-A[RMO05/BGS])", "48(海信 Y_H1_02C)"
            , "49(Yiruite)", "50(科龙空调 KELON KY_R01)", "51(海尔Haier intelligent)", "52(欧科 EK135E)", "53(欧科 EKY-J1)", "54(TCL_ECO)", "55(群达 TM)"
            , "56(约克 YORK RD-03)", "57(海尔 UM-4\"AAAA\"R03)", "58(奥克斯AUX YKR-H/801)", "59(惠而浦 ZH/JT_01)", "60(美的RN02CA5(2))", "61(东芝WH-UA02NE-C)"
            , "62(三菱重工海尔MHN502A064)", "63(巴壁虎)", "64(志高空调带ECO)", "65(成都宇哲)", "66(索尔SUDER F-126A)", "67(大金DAIKIN BRC4L165)", "68(天加TICA_RC201)"
            , "69(大金DAIKIN_BRC4C158)", "70(格力YAP0F3(方式1))", "71(格力YAP0F3(方式2))", "72(奥克斯AUX_YKR_T011)", "73(美的RM05_BG_A)"
    };

    public static String[] getAirModel() {
        String[] temp = new String[255];
        for (int i = 0; i < airModel.length; i++) {
            temp[i] = airModel[i];
        }
        for (int i = airModel.length; i < temp.length; i++) {
            temp[i] = String.valueOf(i + 1);
        }
        return temp;
    }

    public static String[] getAdd() {
        String[] temp = new String[255];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = String.valueOf(i + 1);
        }
        return temp;
    }

    public static String[] getGroup() {
        String[] group = new String[37 * 3];
        for (int i = 0; i < group.length; i = i + 3) {
            if (i / 3 < 10) {
                group[i] = "灯光0" + (i / 3);
                group[i + 1] = "灯光0" + (i / 3) + "亮度加";
                group[i + 2] = "灯光0" + (i / 3) + "亮度减";
            } else {
                group[i] = "灯光" + (i / 3);
                group[i + 1] = "灯光" + (i / 3) + "亮度加";
                group[i + 2] = "灯光" + (i / 3) + "亮度减";
            }
        }
        String[] s = {
                "中控41(服务灯闪烁)", "中控42(取消服务灯闪烁)", "中控43(服务灯常亮)", "中控44(取消服务灯常亮)", "中控45(电脑音乐+)", "中控46(电脑音乐-)"
                , "中控47(功放音乐+)", "中控48(功放音乐-)", "中控49(麦克风+)", "中控50(麦克风-)", "中控51(升调)", "中控52(标准调)", "中控53(降调)", "中控54(混响+)"
                , "中控55(混响中)", "中控56(混响-)", "中控57(切歌)", "中控58(重唱)", "中控59(原/伴唱)", "中控60(静音)", "中控61(暂停)", "中控62(喝彩)", "中控63(献花)"
                , "中控64(倒彩)", "中控65(口哨)", "中控66(导唱)", "中控67(原唱)", "中控68(伴唱)", "中控69(幻影切屏)", "中控70(标准/业余 音效1)", "中控71(流行/剧场 音效2)"
                , "中控72(专业音效3)", "中控73(抒情音效4)", "中控74(唱将音效5)", "中控75(搞怪音效6)", "中控76(和声音效7)", "中控77(整蛊音效8)", "中控78(摇滚音效9)", "中控79(电视)"
                , "中控80(频道+)", "中控81(频道-)", "中控82(录音)", "中控83(停止录音)", "中控84(录音回放)", "中控85(快进)", "中控86(快退)", "中控87(欣赏)", "中控88", "中控89", "中控90"
                , "中控91", "中控92", "中控93", "中控94", "中控95", "中控96", "中控97", "中控98", "中控99", "中控A0", "中控A1", "中控A2", "中控A3", "中控A4", "中控A5", "中控A6", "中控A7"
                , "中控A8(测试1)", "中控A9(测试2)", "中控B0(预留)", "中控B1(门牌灯熄灭)", "中控B2(门牌灯红色常亮)", "中控B3(门牌灯红色闪烁)", "中控B4(门牌灯绿色常亮)", "中控B5(门牌灯绿色闪烁)"
                , "中控B6(门牌灯蓝色常亮)", "中控B7(门牌灯蓝色闪烁)", "中控B8(门牌灯黄色常亮)", "中控B9(门牌灯黄色闪烁)", "中控C0(门牌灯紫色常亮)", "中控C1(门牌灯紫色闪烁)"
                , "中控C2(门牌灯青色常亮)", "中控C3(门牌灯青色闪烁)", "中控C4(门牌灯白色常亮)", "中控C5(门牌灯白色闪烁)", "中控C6", "中控C7", "中控C8", "中控C9", "中控D0", "中控D1"
        };
        String[] temp = new String[group.length + s.length];
        for (int i = 0; i < temp.length; i++) {
            if (i < group.length) {
                temp[i] = group[i];
            } else {
                temp[i] = s[i - group.length];
            }
        }
        return temp;
    }
}
