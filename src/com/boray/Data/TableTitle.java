package com.boray.Data;

public class TableTitle {
	public static String[] getTitle(){
		String[] title = new String[515];
		title[0] = "����";
		title[1] = "ͣ��ʱ��";
		title[2] = "ִ��ʱ��";
		for (int i = 3; i < 515; i++) {
			title[i] = String.valueOf(i-2);
		}
		return title;
	}
}
