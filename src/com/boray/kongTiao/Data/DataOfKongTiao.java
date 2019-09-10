package com.boray.kongTiao.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataOfKongTiao {
	public static Map map = new HashMap();
	static{
		List list = new ArrayList<>();
		for (int j = 0; j < 10; j++) {
			list.add(String.valueOf(0));
		}
		map.put("KTData", list);
	}
}
