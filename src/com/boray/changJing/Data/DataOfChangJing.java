package com.boray.changJing.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataOfChangJing {
	public static Map map = new HashMap();
	static{
		for (int i = 0; i <= 40; i++) {
			List list = new ArrayList<>();
			for (int j = 0; j < 31; j++) {
				list.add(String.valueOf(0));
			}
			list.set(16,"50");//ȫ������
			list.set(17,"1");//����ģʽ
			list.set(18,"1");//����ģʽ
			list.set(26,"1");//����ģʽ
			map.put(""+i, list);
		}
	}
}
