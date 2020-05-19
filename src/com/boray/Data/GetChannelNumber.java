package com.boray.Data;

public class GetChannelNumber {
    public static int get(String s) {
        int number = 0;
        switch (s) {
            case "X��":
                number = 1;
                break;
            case "X��΢��":
                number = 2;
                break;
            case "Y��":
                number = 3;
                break;
            case "Y��΢��":
                number = 4;
                break;
            case "�ٶ�":
                number = 5;
                break;
            case "����":
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
            case "�Զ�����ɫ":
                number = 10;
                break;
            case "ͼ��":
                number = 11;
                break;
            case "��ɫ��":
                number = 12;
                break;
            case "��Ȧ":
                number = 13;
                break;
            case "����":
                number = 14;
                break;
            case "����":
                number = 15;
                break;
            case "��":
                number = 16;
                break;
            case "�⾵":
                number = 17;
                break;
            case "�ƾ߸�λ":
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
            case "Ч��ģʽ":
                number = 50;
                break;
            case "�Զ�ģʽ":
                number = 51;
                break;
            case "����ģʽ":
                number = 52;
                break;
            case "ȫ��-��ɫ":
                number = 53;
                break;
            case "ȫ��-��ɫ":
                number = 54;
                break;
            case "ȫ��-��ɫ":
                number = 55;
                break;
            case "ȫ��-��ɫ":
                number = 56;
                break;
            case "������":
                number = 57;
                break;
            case "����":
                number = 58;
                break;
            case "���С":
                number = 59;
                break;
            case "�����С":
                number = 60;
                break;
            case "Ч���ٶ�":
                number = 61;
                break;
            case "������":
                number = 62;
                break;
            case "��ɫ��":
                number = 63;
                break;
            case "δ֪":
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
                temp = "X��";
                break;
            case 2:
                temp = "X��΢��";
                break;
            case 3:
                temp = "Y��";
                break;
            case 4:
                temp = "Y��΢��";
                break;
            case 5:
                temp = "�ٶ�";
                break;
            case 6:
                temp = "����";
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
                temp = "�Զ�����ɫ";
                break;
            case 11:
                temp = "ͼ��";
                break;
            case 12:
                temp = "��ɫ��";
                break;
            case 13:
                temp = "��Ȧ";
                break;
            case 14:
                temp = "����";
                break;
            case 15:
                temp = "����";
                break;
            case 16:
                temp = "��";
                break;
            case 17:
                temp = "�⾵";
                break;
            case 18:
                temp = "�ƾ߸�λ";
                break;
            case 255:
                temp = "δ֪";
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
                temp = "Ч��ģʽ";
                break;
            case 51:
                temp = "�Զ�����";
                break;
            case 52:
                temp = "����ģʽ";
                break;
            case 53:
                temp = "ȫ��-��ɫ";
                break;
            case 54:
                temp = "ȫ��-��ɫ";
                break;
            case 55:
                temp = "ȫ��-��ɫ";
                break;
            case 56:
                temp = "ȫ��-��ɫ";
                break;
            case 57:
                temp = "������";
                break;
            case 58:
                temp = "����";
                break;
            case 59:
                temp = "���С";
                break;
            case 60:
                temp = "�����С";
                break;
            case 61:
                temp = "Ч���ٶ�";
                break;
            case 62:
                temp = "������";
                break;
            case 63:
                temp = "��ɫ��";
                break;
            default:
                break;
        }
        return temp;
    }
}
