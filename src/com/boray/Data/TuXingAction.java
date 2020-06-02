package com.boray.Data;

public class TuXingAction {
    //	public static String[] values2 = {
//		"无","1(特定模式1)","2(特定模式2)","3(特定模式3)"
//		,"4(特定模式4)","5(特定模式5)","6(特定模式6)","7(特定模式7)"
//		,"8(特定模式8)","9(特定模式9)","10(特定模式10)","11(8字)","12(Z型(大))","13(Z型(中))","14(Z型(小))"
//		,"15(工字型(大))","16(工字型(中))","17(工字型(小))"
//		,"18(横8字双圆(大))","19(横8字双圆(中))","20(横8字双圆(小))","21(弧形)","22(花)","23(矩形)"
//		,"24(来回走圆型)","25(梅花型(大))","26(梅花型(小))","27(扭曲)","28(前后走动)","29(前后走动(大))"
//		,"30(前后走动(中))","31(前后走动(小))","32(双半圆型(大))","33(双半圆型(小))","34(水平横向移动(0度))","35(水平横向移动(30度))"
//		,"36(水平横向移动(45度))","37(旋涡)","38(漩涡(大))","39(漩涡(中))"
//		,"40(漩涡(小))","41(圆)","42(圆形(大))","43(圆形(中))","44(圆形(小))","45(走8字型)","46(走梅花型)","47(左右走动1)","48(左右走动2)"
//		,"49(左右走动(大))","50(左右走动(中))","51(左右走动(小))","52((null))","53((null))","54((null))","55((null))","56((null))"
//		,"57((null))","58((null))","59((null))","60((null))"
//	};
    public static String[] values = {
            "动作默认", "自定义", "1(8字)", "2(Z型(大))", "3(Z型(中))", "4(Z型(小))"
            , "5(工字型(大))", "6(工字型(中))", "7(工字型(小))"
            , "8(横8字双圆(大))", "9(横8字双圆(中))", "10(横8字双圆(小))", "11(弧形)", "12(花)", "13(画圆前)"
            , "14(矩形)", "15(矩形前)", "16(来回走圆型)", "17(梅花型(大))", "18(梅花型(小))", "19(扭曲)"
            , "20(前后走动)", "21(前后走动(大))", "22(前后走动(中))", "23(前后走动(小))", "24(双半圆型(大))", "25(双半圆型(小))"
            , "26(水平横向移动(0度))", "27(水平横向移动(30度))", "28(水平横向移动(45度))", "29(小八字)"
            , "30(旋涡)", "31(旋涡(大))", "32(旋涡(中))", "33(旋涡(小))", "34(雪人)", "35(圆)", "36(圆形(大))", "37(圆形(中))", "38(圆形(小))"
            , "39(走8字型)", "40(走梅花型)", "41(左右)", "42(左右走动1)", "43(左右走动2)", "44(左右走动(大))", "45(左右走动(中))", "46(左右走动(小))"
    };
    public static String[] values2 = {
            "无", "1(红绿蓝常亮)", "2(红绿蓝交替)", "3(七色交替)", "4(渐变R-RB-B-BG-G-GR-R)"
            , "5(渐变B-RGB-G-GR-R-GB-B)", "6(渐变RB-GB-RG)", "7(双色过渡切换)", "8(RG双色渐变1)", "9(RG双色渐变2)"
            , "10(RG双色渐变3)", "11(RB双色渐变1)", "12(RB双色渐变2)", "13(RB双色渐变3)", "14(GB双色渐变1)"
            , "15(GB双色渐变2)", "16(GB双色渐变3)", "17(白光频闪)", "18(RG双色渐变1+B)", "19(RG双色渐变2+B)"
            , "20(RG双色渐变3+B)"
    };
    public static String[] values3 = {
            "1(不拆分)","2(中间拆分)","3(两端拆分)","4(跑马灯1)","5(跑马灯1(往返))","6(跑马灯2)",
            "7(跑马灯2(往返))","8(奇数)","9(奇数(往返))","10(偶数)","11(偶数(往返))","12(奇数+偶数)",
            "13(奇数+偶数(往返))","14(偶数+奇数)","15(偶数+奇数(往返))","16(流水灯)"
    };

    public static String[] getValus() {
        String[] s = new String[256];
        for (int i = 0; i < 256; i++) {
            if (i < values.length) {
                s[i] = values[i];
            } else {
                s[i] = String.valueOf(i - 1);
            }
        }
        return s;
    }

    public static String[] getValus2() {
        String[] s = new String[256];
        for (int i = 0; i < 255; i++) {
            if (i < values2.length) {
                s[i] = values2[i];
            } else {
                s[i] = String.valueOf(i);
            }
        }
        s[255] = "颜色检测器模式";
        return s;
    }

    public static String[] getValues3(){
        String[] s = new String[10];
        for (int i = 0;i<s.length;i++){
            s[i] = values3[i];
        }
        return values3;
    }
}
