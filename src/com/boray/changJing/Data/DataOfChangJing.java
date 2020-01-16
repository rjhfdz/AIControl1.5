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
			map.put(""+i, list);
		}
	}
}
