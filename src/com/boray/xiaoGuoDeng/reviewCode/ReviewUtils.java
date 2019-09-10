package com.boray.xiaoGuoDeng.reviewCode;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;

public class ReviewUtils {
	
	/*
	 * 声控预览模式指令
	 */
	public static void modeReviewOrderByVoice(int model){
		//FA 20 61 B6 10 02 
		//01 //声控
		//01 //模式
		//00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 44
		byte[] buff = new byte[32];
		buff[0] = (byte)0xFA;buff[1] = (byte)0x20;
		buff[2] = (byte)0x61;
		buff[3] = ZhiLingJi.TYPE;
		buff[4] = (byte)0x10;
		buff[5] = (byte)0x02;
		buff[6] = (byte)0x01;
		buff[7] = (byte)model;
		buff[31] = ZhiLingJi.getJiaoYan(buff);
		if (Data.serialPort != null) {
			try {
				OutputStream os = Data.serialPort.getOutputStream();
				os.write(buff);
				os.flush();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	/*
	 * 场景预览模式指令
	 */
	public static void modeReviewOrder(int model){
		//FA 20 61 B6 10 02 
		//00 //场景
		//01 //模式
		//00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 44
		byte[] buff = new byte[32];
		buff[0] = (byte)0xFA;buff[1] = (byte)0x20;
		buff[2] = (byte)0x61;
		buff[3] = ZhiLingJi.TYPE;
		buff[4] = (byte)0x10;
		buff[5] = (byte)0x02;
		buff[7] = (byte)model;
		buff[31] = ZhiLingJi.getJiaoYan(buff);
		if (Data.serialPort != null) {
			try {
				OutputStream os = Data.serialPort.getOutputStream();
				os.write(buff);
				os.flush();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	/*
	 * 组装数据
	 */
	public static byte[] reviewData(byte[] data,int packetNum){
		byte[] tp = new byte[518];
		tp[0] = 0x55;tp[1] = (byte)0xAA;
		tp[2] = (byte)0x88;
		tp[3] = (byte)((packetNum-1)/256);
		tp[4] = (byte)((packetNum-1)%256);
		if (data!=null) {
			for (int i = 0; i < 512; i++) {
				tp[5+i] = data[i];
			}
		}
		tp[517] = ZhiLingJi.getJiaoYan(tp);
		return tp;
	}
	
	public static void sendReviewCode(){
		if (Data.serialPort != null) {
			for (int i = 0; i < Data.packetNumList.size(); i++) {
				//System.out.println("包号："+(((int)Data.packetNumList.get(i))-1));
				byte[] buff = reviewData((byte[])Data.valueList.get(i), (int)Data.packetNumList.get(i));
				try {
					OutputStream os = Data.serialPort.getOutputStream();
					os.write(buff);
					os.flush();
					Thread.sleep(180);
				} catch(Exception e){
					e.printStackTrace();
				}
			}
			int count = 5;
			while (count >= 0) {
				count--;
				if (Data.reviewMap.size() != 0) {
					Set set = Data.reviewMap.keySet();
					Iterator iterator = set.iterator();
					while (iterator.hasNext()) {
						int pk = -1;
						try {
							pk = (int)iterator.next();
							//System.out.println(pk);
							if (Data.reviewMap.get(pk)!=null) {
								byte[] buff = reviewData((byte[])Data.reviewMap.get(pk), pk+1);
								try {
									OutputStream os = Data.serialPort.getOutputStream();
									os.write(buff);
									os.flush();
									Thread.sleep(200);
								} catch(Exception e){
									e.printStackTrace();
								}
							}
						} catch (Exception e) {
							set = Data.reviewMap.keySet();
							iterator = set.iterator();
							//System.err.println("error");
						}
					}
				}
			}
		}
	}
	
	public static void reviewOrder(int type,int[] values){
		byte[] buff = new byte[32];
		buff[0] = (byte)0xFA;buff[1] = (byte)0x20;
		buff[2] = (byte)0x61;
		buff[3] = ZhiLingJi.TYPE;
		switch (type) {
			case 1://rgb1
				buff[4] = (byte)0x31;
				break;
			case 2://rgb2
				buff[4] = (byte)0x32;
				break;
			case 3://rgb3
				buff[4] = (byte)0x33;
				break;
			case 4://动作效果
				buff[4] = (byte)0x01;
				break;
			default:
				break;
		}
		buff[5] = (byte)0x02;
		buff[6] = (byte)0x00;//场景
		buff[7] = (byte)values[0];//模式
		buff[8] = (byte)values[1];//组号
		buff[9] = (byte)values[2];//时间块号
		buff[31] = ZhiLingJi.getJiaoYan(buff);
		if (Data.serialPort != null) {
			try {
				OutputStream os = Data.serialPort.getOutputStream();
				os.write(buff);
				os.flush();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	//FA 20 61 B6 31 02 00 01 01 01 00 00 FF FF FF 01 01 E0 32 01 01 00 00 00 00 00 00 00 00 00 00 7A
}
