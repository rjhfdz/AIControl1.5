package com.boray.Data;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;

public class DataMerge {
	
	public byte[] getData(){
		byte[] data = new byte[512];
		byte[] t = "BORAY".getBytes();
		for (int i = 0; i < 5; i++) {
			data[i] = t[i];
		}
		String path = getClass().getResource("/Data/").getPath().substring(1);
		try {
			path = URLDecoder.decode(path,"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		File file = null;
		//³¡¾°
		String nn = "";
		for (int i = 1; i < 25; i++) {
			if (i > 9) {
				nn = path+"CJ0"+(i)+".sc";
			} else {
				nn = path+"CJ00"+(i)+".sc";
			}
			file = new File(nn);
			if (file.exists()) {
				data[i*4+0+1] = (byte)(file.length()/256/256/256);
				data[i*4+1+1] = (byte)(file.length()/256/256%256);
				data[i*4+2+1] = (byte)(file.length()/256%256);
				data[i*4+3+1] = (byte)(file.length()%256);
			} else {
				data[i*4+0+1] = 0;
				data[i*4+1+1] = 0;
				data[i*4+2+1] = 8;
				data[i*4+3+1] = 0;
			}
		}
		
		//Éù¿Ø
		for (int i = 0; i < 16; i++) {
			if (i > 8) {
				nn = path+"SK0"+(i+1)+".sc";
			} else {
				nn = path+"SK00"+(i+1)+".sc";
			}
			file = new File(nn);
			if (file.exists()) {
				data[i*4+0+101] = (byte)(file.length()/256/256/256);
				data[i*4+1+101] = (byte)(file.length()/256/256%256);
				data[i*4+2+101] = (byte)(file.length()/256%256);
				data[i*4+3+101] = (byte)(file.length()%256);
			} else {
				data[i*4+0+101] = 0;
				data[i*4+1+101] = 0;
				data[i*4+2+101] = 8;
				data[i*4+3+101] = 0;
			}
		}
		
		return data;
	}
}
