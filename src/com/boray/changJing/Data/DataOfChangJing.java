package com.boray.changJing.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataOfChangJing {
    public static Map map = new HashMap();
    public static Map timeMap = new HashMap();

    static {
        for (int i = 0; i <= 40; i++) {
            List list = new ArrayList<>();
            for (int j = 0; j < 31; j++) {
                list.add(String.valueOf(0));
            }
            if (i == 25) {
                list.set(8, "0");
                list.set(9, "8");
                list.set(10, "0");
                list.set(11, "8");
                list.set(12, "0");
                list.set(13, "8");
                list.set(14, "0");
                list.set(15, "8");
            }
            if (i == 26) {
                list.set(9, "100");
                list.set(11, "100");
                list.set(13, "100");
                list.set(15, "100");
            }
            list.set(16, "50");//ȫ������
            list.set(17, "1");//����ģʽ
            list.set(18, "1");//����ģʽ
            list.set(26, "1");//����ģʽ
            map.put("" + i, list);
            List timeList = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                timeList.add("100");
            }
            timeMap.put("" + i, timeList);
        }
    }
}
