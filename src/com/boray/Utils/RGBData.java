package com.boray.Utils;

public class RGBData {

    public static int[][] LED_Model_1 = {{255, 255, 255}, {255, 255, 255}, {255, 255, 255}};//红绿蓝常亮
    public static int[][] LED_Model_2 = {//红绿蓝交替
            {255, 0, 0},//红色
            {0, 255, 0},//绿色
            {0, 0, 255}//蓝色
    };
    public static int[][] LED_Model_3 = {//七色交替
            {255, 255, 0},//黄色
            {0, 255, 0},//绿色
            {255, 250, 250},//雪白
            {255, 0, 0},//红色
            {139, 0, 255},//紫色
            {0, 255, 255},//青色
            {0, 0, 255}//蓝色
    };
    public static int[][] LED_Model_4 = {//渐变R-RB-B-BG-G-GR-R
            {255, 0, 0},//r
            {255, 0, 255},//rb
            {0, 0, 255},//b
            {0, 255, 255},//bg
            {0, 255, 0},//g
            {255, 255, 0}//gr
    };
    public static int[][] LED_Model_5 = {//渐变B-RGB-G-GR-R-RB-B
            {0, 0, 255},//B
            {255, 255, 255},//RGB
            {0, 255, 0},//G
            {255, 255, 0},//GR
            {255, 0, 0},//R
            {255, 0, 255},//RB
    };
    public static int[][] LED_Model_6 = {//渐变RB-GB-RG
            {255, 0, 255},//rb
            {0, 255, 255},//gb
            {255, 255, 0}//rg
    };
    public static int[][] LED_Model_7 = {//双色过渡切换
            {255, 0, 60},
            {255, 0, 255},
            {60, 0, 255},
            {0, 60, 255},
            {0, 255, 255},
            {0, 255, 60},
            {60, 255, 0},
            {255, 255, 0},
            {255, 60, 0}
    };
    public static int[][] LED_Model_8 = {//RG双色渐变1
            {100, 30, 0},
            {30, 100, 0},
            {100, 30, 0},
            {200, 100, 0},
            {10, 50, 0},
            {70, 100, 0},
            {200, 100, 0},
            {10, 100, 0},
            {100, 30, 0}
    };
    public static int[][] LED_Model_9 = {//RG双色渐变2
            {120, 50, 0},
            {50, 120, 0},
            {120, 50, 0},
            {220, 120, 0},
            {30, 40, 0},
            {20, 120, 0},
            {220, 120, 0},
            {30, 120, 0},
            {120, 50, 0}
    };
    public static int[][] LED_Model_10 = {//RG双色渐变3
            {150, 80, 0},
            {80, 150, 0},
            {150, 80, 0},
            {250, 150, 0},
            {60, 100, 0},
            {120, 150, 0},
            {250, 150, 0},
            {60, 150, 0},
            {150, 80, 0}
    };
    public static int[][] LED_Model_11 = {//RB双色渐变1
            {100, 0, 30},
            {30, 0, 100},
            {100, 0, 30},
            {200, 0, 100},
            {10, 0, 50},
            {70, 0, 100},
            {200, 0, 100},
            {10, 0, 100},
            {100, 0, 30}
    };
    public static int[][] LED_Model_12 = {//RB双色渐变2
            {120, 0, 50},
            {50, 0, 120},
            {120, 0, 50},
            {220, 0, 120},
            {30, 0, 70},
            {90, 0, 120},
            {220, 0, 120},
            {30, 0, 120},
            {120, 0, 50}
    };
    public static int[][] LED_Model_13 = {//RB双色渐变3
            {150, 0, 80},
            {80, 0, 150},
            {150, 0, 80},
            {250, 0, 150},
            {60, 0, 100},
            {120, 0, 150},
            {250, 0, 150},
            {60, 0, 150},
            {150, 0, 80}
    };
    public static int[][] LED_Model_14 = {//GB双色渐变1
            {0, 200, 200},
            {0, 30, 100},
            {0, 100, 30},
            {0, 200, 100},
            {0, 10, 50},
            {0, 70, 100},
            {0, 200, 100},
            {0, 10, 100},
            {0, 200, 200}
    };
    public static int[][] LED_Model_15 = {//GB双色渐变2
            {0, 220, 220},
            {0, 50, 120},
            {0, 120, 50},
            {0, 220, 120},
            {0, 30, 80},
            {0, 90, 120},
            {0, 220, 120},
            {0, 30, 120},
            {0, 220, 220}
    };
    public static int[][] LED_Model_16 = {//GB双色渐变3
            {0, 250, 250},
            {0, 80, 150},
            {0, 150, 80},
            {0, 250, 150},
            {0, 60, 100},
            {0, 120, 150},
            {0, 250, 150},
            {0, 60, 150},
            {0, 250, 250}
    };
    public static int[][] LED_Model_17 = {//白光频闪
            {255, 255, 255},
            {0, 0, 0},
            {255, 255, 255},
            {0, 0, 0}
    };
    public static int[][] LED_Model_18 = {//RG双色渐变1+B
            {100, 30, 30},
            {30, 100, 30},
            {100, 30, 30},
            {200, 100, 30},
            {10, 50, 30},
            {70, 100, 30},
            {200, 100, 30},
            {10, 100, 30},
            {100, 30, 30}
    };
    public static int[][] LED_Model_19 = {//RG双色渐变1+B
            {120, 50, 30},
            {50, 120, 30},
            {120, 50, 30},
            {220, 120, 30},
            {30, 40, 30},
            {20, 120, 30},
            {220, 120, 30},
            {30, 120, 30},
            {120, 50, 30}
    };
    public static int[][] LED_Model_20 = {//RG双色渐变1+B
            {150, 80, 30},
            {80, 150, 30},
            {150, 80, 30},
            {250, 150, 30},
            {60, 100, 30},
            {120, 150, 30},
            {250, 150, 30},
            {60, 150, 30},
            {150, 80, 30}
    };
    public static int[][] LED_Model_default = {
            {0,0,0},
            {0,0,0},
            {0,0,0}
    };

    public static int[][] getRGBModel(int index) {
        int[][] ints;
        switch (index) {
            case 1:
                ints = LED_Model_1;
                break;
            case 2:
                ints = LED_Model_2;
                break;
            case 3:
                ints = LED_Model_3;
                break;
            case 4:
                ints = LED_Model_4;
                break;
            case 5:
                ints = LED_Model_5;
                break;
            case 6:
                ints = LED_Model_6;
                break;
            case 7:
                ints = LED_Model_7;
                break;
            case 8:
                ints = LED_Model_8;
                break;
            case 9:
                ints = LED_Model_9;
                break;
            case 10:
                ints = LED_Model_10;
                break;
            case 11:
                ints = LED_Model_11;
                break;
            case 12:
                ints = LED_Model_12;
                break;
            case 13:
                ints = LED_Model_13;
                break;
            case 14:
                ints = LED_Model_14;
                break;
            case 15:
                ints = LED_Model_15;
                break;
            case 16:
                ints = LED_Model_16;
                break;
            case 17:
                ints = LED_Model_17;
                break;
            case 18:
                ints = LED_Model_18;
                break;
            case 19:
                ints = LED_Model_19;
                break;
            case 20:
                ints = LED_Model_20;
                break;
            default:
                ints = LED_Model_default;
        }
        return ints;
    }
}
