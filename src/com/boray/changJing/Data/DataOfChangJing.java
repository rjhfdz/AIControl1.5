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
			list.set(16,"50");//全局亮度
			list.set(17,"1");//开关模式
			list.set(18,"1");//亮度模式
			list.set(26,"1");//运行模式
			map.put(""+i, list);
		}
	}
}
