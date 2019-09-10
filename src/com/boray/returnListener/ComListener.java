package com.boray.returnListener;

import java.io.InputStream;

import com.boray.Data.Data;

public class ComListener /*implements Runnable*/{
	/*private int size2;
	public ComListener(int size){
		size2 = size;
	}
	public void run() {
		try {
			getData(size2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	public byte[] getData(int size) throws Exception{
		InputStream is = Data.serialPort.getInputStream();
		byte[] b = new byte[size];
		byte[] b1 = new byte[size];
		int len1 = 0,len2 = 0;
		while (len1 < size) {
			len2 = is.read(b);
			if (len2 != -1) {
				len1 = len1 + len2;
				if (len1 <= 95) {
					System.out.println(len1+"//"+Thread.currentThread().getName());
					for (int i = len1-len2; i < len1; i++) {
						b1[i] = b[i-(len1-len2)];
					}
				}
			}
		}
		return b1;
	}
}
