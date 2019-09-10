package com.boray.usb;

public class LastPacketData {
	public static byte[] getL(byte[] data512,int[] sl){
		byte[] temp = new byte[64];
		temp[0] = 0x55;
		temp[1] = (byte)0xAA;
		temp[2] = 0x01;
		temp[3] = 0x02;
		temp[4] = 0x00;
		temp[5] = 0x09;
		int check = 0;
		for (int i = 0; i < 512; i++) {
			check = Byte.toUnsignedInt(data512[i]) + check;
		}
		temp[6] = (byte)check;
		
		temp[7] = data512[0];
		temp[8] = 0;
		temp[9] = 1;
		
		temp[10] = data512[64];
		temp[11] = 0;
		temp[12] = 2;
		
		temp[13] = data512[128];
		temp[14] = 0;
		temp[15] = 3;
		
		temp[16] = data512[192];
		temp[17] = 0;
		temp[18] = 4;
		
		temp[19] = data512[256];
		temp[20] = 0;
		temp[21] = 5;
		
		temp[22] = data512[320];
		temp[23] = 0;
		temp[24] = 6;
		
		temp[25] = data512[384];
		temp[26] = 0;
		temp[27] = 7;
		
		temp[28] = data512[448];
		temp[29] = 0;
		temp[30] = 8;
		
		temp[31] = (byte)(sl[0]/256);
		temp[32] = (byte)(sl[0]%256);
		
		temp[33] = (byte)(sl[1]/256);
		temp[34] = (byte)(sl[1]%256);
		
		temp[35] = (byte)(sl[2]/256);
		temp[36] = (byte)(sl[2]%256);
		
		/*60	0XA1	固定1
		61	0X5E	固定2
		62	0X89	固定3
		63	0X76	固定4*/
		
		temp[59] = (byte)0xA1;
		temp[60] = (byte)0x5E;
		temp[61] = (byte)0x89;
		temp[62] = (byte)0x76;
		
		int cnt = 0;
		for (int i = 0; i < 63; i++) {
			cnt = Byte.toUnsignedInt(temp[i]) + cnt;
		}
		temp[63] = (byte)cnt;
		return temp;
	}
}
