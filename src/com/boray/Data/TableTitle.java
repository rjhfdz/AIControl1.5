package com.boray.Data;

public class TableTitle {
	public static String[] getTitle(){
		String[] title = new String[515];
		title[0] = "步骤";
		title[1] = "停留时间";
		title[2] = "执行时间";
		for (int i = 3; i < 515; i++) {
			title[i] = String.valueOf(i-2);
		}
		return title;
	}
}
