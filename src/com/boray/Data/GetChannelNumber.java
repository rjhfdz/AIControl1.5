package com.boray.Data;

public class GetChannelNumber {
    public static int get(String s) {
        int number = 0;
        switch (s) {
            case "X轴":
                number = 1;
                break;
            case "X轴微调":
                number = 2;
                break;
            case "Y轴":
                number = 3;
                break;
            case "Y轴微调":
                number = 4;
                break;
            case "速度":
                number = 5;
                break;
            case "调光":
                number = 6;
                break;
            case "RGBR-1":
                number = 7;
                break;
            case "RGBG-1":
                number = 8;
                break;
            case "RGBB-1":
                number = 9;
                break;
            case "自定义颜色":
                number = 10;
                break;
            case "图案":
                number = 11;
                break;
            case "颜色盘":
                number = 12;
                break;
            case "光圈":
                number = 13;
                break;
            case "调焦":
                number = 14;
                break;
            case "缩放":
                number = 15;
                break;
            case "雾化":
                number = 16;
                break;
            case "棱镜":
                number = 17;
                break;
            case "灯具复位":
                number = 18;
                break;
            case "RGBR-2":
                number = 37;
                break;
            case "RGBG-2":
                number = 38;
                break;
            case "RGBB-2":
                number = 39;
                break;
            case "RGBR-3":
                number = 47;
                break;
            case "RGBG-3":
                number = 48;
                break;
            case "RGBB-3":
                number = 49;
                break;
            case "效果模式":
                number = 50;
                break;
            case "自动模式":
                number = 51;
                break;
            case "声控模式":
                number = 52;
                break;
            case "全彩-红色":
                number = 53;
                break;
            case "全彩-绿色":
                number = 54;
                break;
            case "全彩-蓝色":
                number = 55;
                break;
            case "全彩-白色":
                number = 56;
                break;
            case "正反向":
                number = 57;
                break;
            case "开关":
                number = 58;
                break;
            case "块大小":
                number = 59;
                break;
            case "间隔大小":
                number = 60;
                break;
            case "效果速度":
                number = 61;
                break;
            case "动作宏":
                number = 62;
                break;
            case "颜色宏":
                number = 63;
                break;
            case "未知":
                number = 255;
                break;
            default:
                number = 254;
                break;
        }
        return number;
    }

    public static String getChannelName(int number) {
        String temp = "";
        switch (number) {
            case 1:
                temp = "X轴";
                break;
            case 2:
                temp = "X轴微调";
                break;
            case 3:
                temp = "Y轴";
                break;
            case 4:
                temp = "Y轴微调";
                break;
            case 5:
                temp = "速度";
                break;
            case 6:
                temp = "调光";
                break;
            case 7:
                temp = "RGBR-1";
                break;
            case 8:
                temp = "RGBG-1";
                break;
            case 9:
                temp = "RGBB-1";
                break;
            case 10:
                temp = "自定义颜色";
                break;
            case 11:
                temp = "图案";
                break;
            case 12:
                temp = "颜色盘";
                break;
            case 13:
                temp = "光圈";
                break;
            case 14:
                temp = "调焦";
                break;
            case 15:
                temp = "缩放";
                break;
            case 16:
                temp = "雾化";
                break;
            case 17:
                temp = "棱镜";
                break;
            case 18:
                temp = "灯具复位";
                break;
            case 255:
                temp = "未知";
                break;
            case 37:
                temp = "RGBR-2";
                break;
            case 38:
                temp = "RGBG-2";
                break;
            case 39:
                temp = "RGBB-2";
                break;
            case 47:
                temp = "RGBR-3";
                break;
            case 48:
                temp = "RGBG-3";
                break;
            case 49:
                temp = "RGBB-3";
                break;
            case 50:
                temp = "效果模式";
                break;
            case 51:
                temp = "自动程序";
                break;
            case 52:
                temp = "声控模式";
                break;
            case 53:
                temp = "全彩-红色";
                break;
            case 54:
                temp = "全彩-绿色";
                break;
            case 55:
                temp = "全彩-蓝色";
                break;
            case 56:
                temp = "全彩-白色";
                break;
            case 57:
                temp = "正反向";
                break;
            case 58:
                temp = "开关";
                break;
            case 59:
                temp = "块大小";
                break;
            case 60:
                temp = "间隔大小";
                break;
            case 61:
                temp = "效果速度";
                break;
            case 62:
                temp = "动作宏";
                break;
            case 63:
                temp = "颜色宏";
                break;
            default:
                break;
        }
        return temp;
    }
}
