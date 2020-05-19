package com.boray.Data;

public class ChannelName {
    //	public static String[] names = {"X��","X��΢��","Y��","Y��΢��","�ٶ�","����",
//									"RGBR-1","RGBG-1","RGBB-1","RGBR-2","RGBG-2",
//									"RGBB-2","RGBR-3","RGBG-3","RGBB-3","�Զ�����ɫ",
//									"ͼ��","��ɫ��","��Ȧ","����","����","��","�⾵",
//									"�ƾ߸�λ","�Զ���ͨ��","δ����ͨ��","����1","����2","δ֪"};
    public static String[] names = {"X��", "X��΢��", "Y��", "Y��΢��", "�ٶ�", "����",
            "RGBR-1", "RGBG-1", "RGBB-1", "�Զ�����ɫ", "ͼ��", "��ɫ��", "��Ȧ", "����", "����", "��", "�⾵",
            "�ƾ߸�λ", "RGBR-2", "RGBG-2", "RGBB-2", "RGBR-3", "RGBG-3", "RGBB-3", "Ч��ģʽ", "�Զ�����",
            "����ģʽ", "ȫ��-��ɫ", "ȫ��-��ɫ", "ȫ��-��ɫ", "ȫ��-��ɫ", "������", "����", "���С", "�����С",
            "Ч���ٶ�", "������", "δ֪"};

    public static String getChangeName(String s) {
        String temp = "";
        switch (s) {
            case "X��":
                temp = "X��<br><br>";
                break;
            case "X��΢��":
                temp = "X��΢��<br><br>";
                break;
            case "Y��":
                temp = "Y��<br><br>";
                break;
            case "Y��΢��":
                temp = "Y��΢��<br><br>";
                break;
            case "�ٶ�":
                temp = "�ٶ�<br><br>";
                break;
            case "����":
                temp = "����<br><br>";
                break;
            case "RGBR-1":
                temp = "RGBR-1<br><br>";
                break;
            case "RGBG-1":
                temp = "RGBG-1<br><br>";
                break;
            case "RGBB-1":
                temp = "RGBB-1<br><br>";
                break;
            case "�Զ�����ɫ":
                temp = "�Զ�����ɫ";
                break;
            case "ͼ��":
                temp = "ͼ��<br><br>";
                break;
            case "��ɫ��":
                temp = "��ɫ��<br><br>";
                break;
            case "��Ȧ":
                temp = "��Ȧ<br><br>";
                break;
            case "����":
                temp = "����<br><br>";
                break;
            case "����":
                temp = "����<br><br>";
                break;
            case "��":
                temp = "��<br><br>";
                break;
            case "�⾵":
                temp = "�⾵<br><br>";
                break;
            case "�ƾ߸�λ":
                temp = "�ƾ߸�λ";
                break;
            case "�Զ���ͨ��":
                temp = "�Զ���ͨ��";
                break;
            case "δ����ͨ��":
                temp = "δ����ͨ��";
                break;
            case "����1":
                temp = "����1<br><br>";
                break;
            case "����2":
                temp = "����2<br><br>";
                break;
            case "δ֪":
                temp = "δ֪<br><br>";
                break;
            case "RGBR-2":
                temp = "RGBR-2<br><br>";
                break;
            case "RGBG-2":
                temp = "RGBG-2<br><br>";
                break;
            case "RGBB-2":
                temp = "RGBB-2<br><br>";
                break;
            case "RGBR-3":
                temp = "RGBR-3<br><br>";
                break;
            case "RGBG-3":
                temp = "RGBG-3<br><br>";
                break;
            case "RGBB-3":
                temp = "RGBB-3<br><br>";
                break;
            default:
                temp = s + "<br><br>";
                break;
        }
        StringBuilder builder = new StringBuilder(temp);
        builder.insert(0, "<html>");
        builder.append("</html>");
        return builder.toString();
    }
}
