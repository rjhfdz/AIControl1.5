package com.boray.Data;

public class DengJuData {
	private byte[] data;
	public DengJuData(){
		data = new byte[32];
		for (int i = 0; i < data.length; i++) {
			data[i] = 0;
		}
	}
	public byte[] getbytes(){
		data[0] = (byte)0xFD;//抬头
		data[1] = (byte)0x20;//字节数
		data[2] = (byte)0x60;//指令类型
		data[3] = (byte)0xA9;//机器型号
		data[4] = (byte)0x01;//一级功能
		data[5] = (byte)0x00;//二级关键字
		data[6] = (byte)0x00;//灯具编号
		data[7] = (byte)0x01;//灯库状态
		//校验和
		int temp = 0;
		for (int i = 0; i < 32; i++) {
			temp = temp + Byte.toUnsignedInt(data[i]);
		}
		data[31] = (byte)temp;
		return data;
	}
	//设置起始地址
	public void setAddress(int address){
		data[8] = (byte)(address / 256);
		data[9] = (byte)(address % 256);
	}
	
	//设置通道数
	public void setChannelCount(int count){
		data[10] = (byte)count;
	}
	//设置灯具名称
	public void setName(String name){
		byte[] temp = name.getBytes();
		data[11] = (byte)temp.length;
		for (int i = 0; i < temp.length; i++) {
			data[i+12] = temp[i];
		}
	}
	
	//设置关联灯库编号
	public void setChannelNo(int number){
		data[28] = (byte)number;
	}
}
