package com.boray.Data;

public class ZhiLingJi {
	
	public static byte TYPE;//机型
	
	/*
	 * 查询机型
	 */
	public static byte[] link(){
		byte[] b = new byte[10];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;
		b[1] = (byte)0x0A;
		b[2] = (byte)0xC1;
		b[9] = (byte)0xC5;//校验
		return b;
	}
	
	/*
	 * 切换波特率
	 * FA 14 64 B9 03 01 A5 FF FF FF FF FF FF 00 00 00 00 00 00 CE
	 */
	public static byte[] setBaut(){
		byte[] bty = new byte[20];
		for (int i = 0; i < bty.length; i++) {
			bty[i] = 0;
		}
		bty[0] = (byte)0xFA;bty[1] = (byte)0x14;
		bty[2] = (byte)0x64;bty[3] = ZhiLingJi.TYPE;
		bty[4] = (byte)3;bty[5] = (byte)1;
		bty[6] = (byte)0xA6;
		for (int i = 7; i < 13; i++) {
			bty[i] = (byte)0xFF;
		}
		bty[19] = getJiaoYan(bty);
		return bty;
	}
	
	/*
	 * 切回波特率
	 * FA 14 64 BA 0C 00 12 ED 34 CB 00 00 00 00 00 00 00 00 00 36
	 */
	public static byte[] setBackBaut(){
		byte[] bty = new byte[20];
		for (int i = 0; i < bty.length; i++) {
			bty[i] = 0;
		}
		bty[0] = (byte)0xFA;bty[1] = (byte)0x14;
		bty[2] = (byte)0x64;bty[3] = ZhiLingJi.TYPE;
		bty[4] = (byte)0x0C;//bty[5] = (byte)0;
		bty[6] = (byte)0x12;bty[7] = (byte)0xED;
		bty[8] = (byte)0x34;bty[9] = (byte)0xCB;
		bty[19] = getJiaoYan(bty);
		return bty;
	}
	
	/*
	 * 查询设备信息
	 * FA 08 DB B9 00 00 00 96
	 */
	public static byte[] queryDevice(){
		byte[] bty = new byte[8];
		for (int i = 0; i < bty.length; i++) {
			bty[i] = 0;
		}
		bty[0] = (byte)0xFA;bty[1] = (byte)8;
		bty[2] = (byte)0xDB;bty[3] = ZhiLingJi.TYPE;
		bty[7] = getJiaoYan(bty);
		return bty;
	}
	/*
	 * 查询包数
	 * FA 0A 82 B9 10 00 00 00 00 4f
	 */
	public static byte[] queryPketCount(int type) {
		byte[] bty = new byte[10];
		for (int i = 0; i < bty.length; i++) {
			bty[i] = 0;
		}
		bty[0] = (byte)0xFA;bty[1] = (byte)0x0A;
		bty[2] = (byte)0x82;bty[3] = ZhiLingJi.TYPE;
		bty[4] = (byte)type;//0x10配置 0x20效果灯
		bty[9] = getJiaoYan(bty);
		return bty;
	}
	
	/*
	 * 根据包号查询数据
	 * FA 0A 81 A7 10 00 01 FF FF xx
	 */
	public static byte[] getDataByPacketN(int n,int type){
		byte[] bty = new byte[10];
		bty[0] = (byte)0xFA;bty[1] = (byte)0x0A;
		bty[2] = (byte)0x81;bty[3] = ZhiLingJi.TYPE;
		bty[4] = (byte)type;//0x10配置 0x20效果灯
		bty[5] = (byte)(n/256);
		bty[6] = (byte)(n%256);
		bty[7] = (byte)0xFF;bty[8] = (byte)0xFF;
		bty[9] = getJiaoYan(bty);
		return bty;
	}
	
	/*
	 * PC写命令：AAFFFFAA-020F-80-A7-10-0005-D1D2D3，，，D512+校验和1BYTE+FF8899
	 */
	public static byte[] packetData(byte[] dt,int leiX,int packetN){
		byte[] tp = new byte[527];
		tp[0] = (byte)0xAA;tp[1] = (byte)0xFF;tp[2] = (byte)0xFF;tp[3] = (byte)0xAA;
		tp[4] = (byte)0x02;tp[5] = (byte)0x0F;
		tp[6] = (byte)0x80;tp[7] = ZhiLingJi.TYPE;
		tp[8] = (byte)leiX;
		tp[9] = (byte)(packetN/256);
		tp[10] = (byte)(packetN%256);
		for (int i = 0; i < dt.length; i++) {
			tp[i+11] = dt[i];
		}
		tp[523] = getJiaoYan(tp);
		tp[524] = (byte)0xFF;
		tp[525] = (byte)0x88;
		tp[526] = (byte)0x89;
		return tp;
	}
	
	/*
	 * 场景查询
	 */
	public static byte[] changJingChaXun(int n){
		byte[] b = new byte[10];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;
		b[1] = (byte)0x0A;
		b[2] = 0x31;
		b[3] = TYPE;
		b[4] = (byte)n;
		b[9] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 效果灯模式、摇麦、雾机控制设置
	 */
	public static byte[] changJingChaXun2(int n){
		byte[] b = new byte[30];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;
		b[1] = (byte)0x1E;
		b[2] = 0x64;
		b[3] = TYPE;
		b[4] = 1;
		b[5] = 0;
		b[6] = (byte)n;
		b[29] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 场景复制
	 * FA 14 C0 B9 xx FF 00 02 00 mb FF 00 00 05 FF 00 00 00 00 92
	 */
	public static byte[] changJingCopy(int src,int total){
		byte[] b = new byte[20];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;
		b[1] = (byte)0x14;
		b[2] = (byte)0xC0;
		b[3] = TYPE;
		b[4] = (byte)src;
		b[5] = (byte)0xFF;
		b[7] = (byte)2;
		b[9] = (byte)total;
		b[10] = (byte)0xFF;
		b[13] = (byte)total;
		b[14] = (byte)0xFF;
		b[19] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 设置场景前部分
	 */
	public static byte[] setChangJing1(int n,int[] t){
		byte[] b = new byte[95];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0x0C;
		}
		b[0] = (byte)0xFA;b[1] = (byte)0x5F;b[2] = (byte)0x32;
		b[3] = TYPE;b[4] = (byte)n;
		for (int i = 0; i < t.length; i++) {
			b[5+i] = (byte)t[i];
		}
		b[92] = (byte)255;b[93] = (byte)255;
		b[94] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 设置场景后部分
	 */
	public static byte[] setChangJing2(int n,int[] t){
		byte[] b = new byte[30];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;b[1] = (byte)30;b[2] = 0x64;
		b[3] = TYPE;b[4] = 1;b[5] = 1;b[6] = (byte)n;
		for (int i = 0; i < t.length; i++) {
			b[9+i] = (byte)t[i];
		}
		b[29] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 空调参数查询
	 * FA 14 63 B9 02 00 FF 02 00 00 00 00 00 00 00 00 00 00 00 2D
	 */
	public static byte[] queryKongTiao(){
		byte[] b = new byte[20];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;
		b[1] = 0x14;
		b[2] = 0x63;
		b[3] = TYPE;
		b[4] = 0x02;
		b[6] = (byte)0xFF;
		b[7] = 0x02;
		b[19] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 设置空调参数
	 */
	public static byte[] setKongTiao(int key,int value){
		byte[] b = new byte[20];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;
		b[1] = 0x14;
		b[2] = 0x63;
		b[3] = TYPE;
		b[4] = 0x02;
		b[5] = 0x01;
		b[6] = (byte)0xFF;
		b[7] = 0x02;
		switch (key) {
		case 1:
			b[8] = (byte)value;
			break;
		case 2:
			b[9] = (byte)value;
			break;
		case 3:
			b[10] = (byte)value;
			break;
		case 4:
			b[11] = (byte)value;
			break;
		case 5:
			b[12] = (byte)value;
			break;
		case 6:
			b[13] = (byte)value;
			break;
		case 7:
			b[14] = (byte)value;
			break;
		case 8:
			b[15] = (byte)value;
			break;
		case 9:
			b[16] = (byte)value;
			break;
		case 10:
			b[17] = (byte)value;
			break;
		default:
			break;
		}
		b[19] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 恢复出厂设置
	 * FA 14 63 B9 02 01 FF 02 00 03 00 00 00 00 00 00 00 00 00 31
	 */
	public static byte[] setInitToBebin(){
		byte[] b = new byte[20];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;
		b[1] = 0x14;
		b[2] = 0x63;
		b[3] = TYPE;
		b[4] = 0x02;
		b[5] = 0x01;
		b[6] = (byte)0xFF;
		b[7] = 0x02;
		b[9] = 0x03;
		b[19] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 查询空调控制码
	 * FA 14 63 B9 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 2B
	 */
	public static byte[] queryKTCtrl(){
		byte[] b = new byte[20];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;
		b[1] = 0x14;
		b[2] = 0x63;
		b[3] = TYPE;
		b[4] = 1;
		b[19] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 空调控制发码
	 */
	public static byte[] setKongTiaoCtrl(int key ,int value){
		byte[] b = new byte[20];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;
		b[1] = 0x14;
		b[2] = 0x63;
		b[3] = TYPE;
		b[4] = 1;
		b[5] = 1;
		b[6] = 1;
		switch (key) {
		case 1:
			b[7] = (byte)value;
			break;
		case 2:
			b[8] = (byte)value;
			break;
		case 3:
			b[9] = (byte)value;
			break;
		case 4:
			b[10] = (byte)value;
			break;
		case 5:
			b[11] = (byte)value;
			break;
		case 6:
			b[12] = (byte)value;
			break;
		default:
			break;
		}
		b[19] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 查询全局配置参数
	 * FA 14 64 B9 03 00 FF FF FF FF FF FF FF 00 00 00 00 00 00 27
	 */
	public static byte[] queryQuanJuSet(){
		byte[] b = new byte[20];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;
		b[1] = 0x14;
		b[2] = 0x64;
		b[3] = TYPE;
		b[4] = 0x03;
		for (int i = 0; i < 7; i++) {
			b[6+i] = (byte)0xFF;
		}
		b[19] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 设置全局配置参数
	 */
	public static byte[] setQuanJu(int[] values){
		byte[] b = new byte[20];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
			if (i >= 6 && i <= 12) {
				b[i] = (byte)0xFF;
			}
		}
		b[0] = (byte)0xFA;
		b[1] = 0x14;
		b[2] = 0x64;
		b[3] = TYPE;
		b[4] = 0x03;
		b[5] = 1;
		for (int i = 0; i < values.length; i++) {
			b[6+i] = (byte)values[i];
		}
		/*switch (type) {
		case 1:
			b[6] = (byte)value;
			break;
		case 2:
			b[7] = (byte)value;
			break;
		case 3:
			b[8] = (byte)value;
			break;
		case 4:
			b[9] = (byte)value;
			break;
		case 5:
			b[10] = (byte)value;
			break;
		case 6:
			b[11] = (byte)value;
			break;
		default:
			break;
		}*/
		b[18] = 1;
		b[19] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 中控查询
	 * FA 08 D3 B3 01 00 00 89
	 */
	public static byte[] queryZhongKon(int groupN){
		byte[] b = new byte[8];
		b[0] = (byte)0xFA;b[1] = 0x08;
		b[2] = (byte)0xD3;b[3] = (byte)TYPE;
		b[4] = (byte)groupN;b[5] = 0x00;
		b[6] = 0x00;b[7] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 中控写码
	 * FA 14 D2 B9 00 01 04 FC 66 08 02 00 00 00 00 00 00 00 00 0A
	 */
	public static byte[] writeCode(int groupN,int type,byte[] code,int size){
		byte[] b = new byte[20];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;b[1] = 0x14;
		b[2] = (byte)0xD2;b[3] = (byte)TYPE;
		b[4] = (byte)groupN;b[5] = (byte)type;
		if (code != null) {
			b[6] = (byte)size;
			for (int i = 0; i < size; i++) {
				b[7+i] = code[i];
			}
		}
		b[19] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 中控测试码
	 * EA 05 E1 06 FF
	 */
	public static byte[] testCode(int groupN){
		byte[] b = new byte[5];
		b[0] = (byte)0xEA;b[1] = 0x05;
		b[2] = (byte)0xE1;b[3] = (byte)groupN;
		b[4] = (byte)0xFF;
		return b;
	}
	
	/*
	 * 擦除码值
	 * FA 14 D2 B9 05 01 C1 00 00 00 00 00 00 00 00 00 00 00 00 60
	 */
	public static byte[] caChuMaZhi(int cj,int type){
		byte[] b = new byte[20];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;b[1] = 0x14;
		b[2] = (byte)0xD2;b[3] = (byte)TYPE;
		b[4] = (byte)cj;
		b[5] = (byte)type;
		b[6] = (byte)(type+192);
		b[19] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 中控学习
	 * FA 0A D4 B9 05 01 00 00 00 97
	 */
	public static byte[] studyCode(int cj,int type){
		byte[] b = new byte[10];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;b[1] = (byte)0x0A;
		b[2] = (byte)0xD4;b[3] = TYPE;
		b[4] = (byte)cj;b[5] = (byte)type;
		b[9] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 退出学习
	 * FA 0A D4 B9 05 00 00 00 00 96
	 */
	public static byte[] exiteStudy(int cj){
		byte[] b = new byte[10];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;b[1] = (byte)0x0A;
		b[2] = (byte)0xD4;b[3] = TYPE;
		b[4] = (byte)cj;b[9] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 设置中控界面设置
	 * FA 14 64 B9 02 01 （01 01 01 00 01） 00 00 00 00 00 00 00 00 32
	 */
	public static byte[] setZhongKon2(byte[] values){
		byte[] b = new byte[20];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;b[1] = 0x14;
		b[2] = (byte)0x64;b[3] = (byte)TYPE;
		b[4] = (byte)2;b[5] = (byte)1;
		for (int i = 0; i < values.length; i++) {
			b[6+i] = values[i];
		}
		b[19] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 查询中控界面设置
	 * FA 14 64 B9 02 00 FF FF FF FF FF 00 00 00 00 00 00 00 00 28
	 */
	public static byte[] queryZhongKon2(){
		byte[] b = new byte[20];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;b[1] = 0x14;
		b[2] = (byte)0x64;b[3] = (byte)TYPE;
		b[4] = (byte)2;b[5] = (byte)0;
		for (int i = 0; i < 5; i++) {
			b[6+i] = (byte)0xFF;
		}
		b[19] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 设置喝彩或倒彩开关
	 * FA 14 D2 B9 09 08 02 xx xx 00 00 00 00 00 00 00 00 00 00 AD 
	 */
	public static byte[] setHcOrDc(int cj,int hc,int dc){
		byte[] b = new byte[20];
		for (int i = 0; i < b.length; i++) {
			b[i] = 0;
		}
		b[0] = (byte)0xFA;b[1] = 0x14;
		b[2] = (byte)0xD2;b[3] = (byte)TYPE;
		b[4] = (byte)cj;
		b[5] = (byte)8;
		b[6] = (byte)2;
		b[7] = (byte)hc;
		b[8] = (byte)dc;
		b[19] = getJiaoYan(b);
		return b;
	}
	
	/*
	 * 校验和
	 */
	public static byte getJiaoYan(byte[] b){
		int all = 0;
		for (int i = 0; i < b.length-1; i++) {
			all = all + Byte.toUnsignedInt(b[i]);
		}
		return (byte)all;
	}
}
