package com.boray.Data;

public class DengKuData {
	private byte[] data;
	public DengKuData(){
		data = new byte[80];
		for (int i = 0; i < data.length; i++) {
			data[i] = 0;
		}
	}
	public byte[] getbytes(){
		data[0] = (byte)0xFD;//̧ͷ
		data[1] = (byte)0x50;//�ֽ���
		data[2] = (byte)0x60;//ָ������
		data[3] = (byte)0xB9;//�����ͺ�
		data[4] = (byte)0x20;//һ������
		data[5] = (byte)0x01;//�����ؼ���
		//У���
		int temp = 0;
		for (int i = 0; i < 80; i++) {
			temp = temp + Byte.toUnsignedInt(data[i]);
		}
		data[79] = (byte)temp;
		return data;
	}
	//���õƿ���
	public void setNo(int number){
		data[6] = (byte)number;
	}
	//�汾��
	public void setVersion(int version){
		data[7] = (byte)(version/100);
		data[8] = (byte)(version%100);
	}
	//���õƿ�����
	public void setName(String name){
		byte[] temp = name.getBytes();
		data[9] = (byte)temp.length;
		for (int i = 0; i < temp.length; i++) {
			data[i+10] = temp[i];
		}
	}
	//����ͨ������
	public void setChannel(int[] c){
		data[42] = (byte)c.length;
		for (int i = 0; i < c.length; i++) {
			data[43+i] = (byte)c[i];
		}
	}
}
