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
		data[0] = (byte)0xFD;//̧ͷ
		data[1] = (byte)0x20;//�ֽ���
		data[2] = (byte)0x60;//ָ������
		data[3] = (byte)0xA9;//�����ͺ�
		data[4] = (byte)0x01;//һ������
		data[5] = (byte)0x00;//�����ؼ���
		data[6] = (byte)0x00;//�ƾ߱��
		data[7] = (byte)0x01;//�ƿ�״̬
		//У���
		int temp = 0;
		for (int i = 0; i < 32; i++) {
			temp = temp + Byte.toUnsignedInt(data[i]);
		}
		data[31] = (byte)temp;
		return data;
	}
	//������ʼ��ַ
	public void setAddress(int address){
		data[8] = (byte)(address / 256);
		data[9] = (byte)(address % 256);
	}
	
	//����ͨ����
	public void setChannelCount(int count){
		data[10] = (byte)count;
	}
	//���õƾ�����
	public void setName(String name){
		byte[] temp = name.getBytes();
		data[11] = (byte)temp.length;
		for (int i = 0; i < temp.length; i++) {
			data[i+12] = temp[i];
		}
	}
	
	//���ù����ƿ���
	public void setChannelNo(int number){
		data[28] = (byte)number;
	}
}
