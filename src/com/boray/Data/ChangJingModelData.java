package com.boray.Data;

import java.util.BitSet;

import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

public class ChangJingModelData {
	private byte[] head;
	private byte[] frame;
	private byte[] timeBlock;
	private int frameNum;
	/*public static void main(String[] args) {
		boolean[] b = {false,false,false,false,false,false,false,true};
		BitSet set = new BitSet(8);
		for (int i = 0; i < b.length; i++) {
			set.set(i, b[b.length-1 - i]);
		}
		byte[] t = set.toByteArray();
		System.out.println(Byte.toUnsignedInt(t[0]));
	}*/
	public ChangJingModelData(int frameNum){
		head = new byte[512];
		timeBlock = new byte[1024];
		this.frameNum = frameNum;
		frame = new byte[frameNum * 512];
		for (int i = 0; i < frame.length; i++) {
			frame[i] = 0;
		}
		for (int i = 0; i < 1024; i++) {
			timeBlock[i] = 0;
		}
	}
	
	public byte[] getModelData(){
		byte[] temp = new byte[head.length + frame.length + timeBlock.length];
		byte[] h = getHead();
		byte[] t = getTimeBlock();
		byte[] b = getFrame();
		System.arraycopy(h, 0, temp, 0, h.length);
		System.arraycopy(t, 0, temp, h.length, t.length);
		System.arraycopy(b, 0, temp, (h.length + t.length), b.length);
		return temp;
	}
	
	public byte[] getHead(){
		for (int i = 0; i < 512; i++) {
			head[i] = 0;
		}
		head[0] = (byte)0xAA;
		head[1] = (byte)0xBB;
		head[2] = (byte)0xCC;
		head[3] = (byte)0xDD;
		if (frameNum > 255) {
			head[4] = (byte)(frameNum / 256);
			head[5] = (byte)(frameNum % 256);
		} else {
			head[4] = (byte)0;
			head[5] = (byte)frameNum;
		}
		boolean[] temp = new boolean[8];
		for (int j = 0; j < 64; j++) {
			for (int i = 8*j; i < 8*(j+1); i++) {
				temp[i-8*j] = Data.checkList[i];
			}
			head[j+6] = bitTobyte(temp);
		}
		return head;
	}
	
	public byte[] getTimeBlock(){
		NewJTable table_DMX_All = (NewJTable)MainUi.map.get("table_DMX_All");
		for (int i = 0; i < frameNum; i++) {
			//执行时间
			timeBlock[i*4 + 0] = (byte)(Integer.valueOf(table_DMX_All.getValueAt(i, 2).toString()).intValue() / 256);
			timeBlock[i*4 + 1] = (byte)(Integer.valueOf(table_DMX_All.getValueAt(i, 2).toString()).intValue() % 256);
			
			//停留时间
			timeBlock[i*4 + 2] = (byte)(Integer.valueOf(table_DMX_All.getValueAt(i, 1).toString()).intValue() / 256);
			timeBlock[i*4 + 3] = (byte)(Integer.valueOf(table_DMX_All.getValueAt(i, 1).toString()).intValue() % 256);
		}
		return timeBlock;
	}
	
	public byte[] getFrame(){
		NewJTable table_DMX_All = (NewJTable)MainUi.map.get("table_DMX_All");
		for (int i = 0; i < frameNum; i++) {
			for (int j = 3; j < 515; j++) {
				frame[i*512 + (j-3)] = (byte)Integer.valueOf(table_DMX_All.getValueAt(i, j).toString()).intValue();
			}
		}
		return frame;
	}
	
	/*
	 * 通道：508个字节，时间：4个字节
	 */
	public byte[] getFrame_old(){
		NewJTable table_DMX_All = (NewJTable)MainUi.map.get("table_DMX_All");
		for (int i = 0; i < frameNum; i++) {
			for (int j = 1; j < 511; j++) {
				if (j == 1) {
					frame[i*512 + 510] = (byte)(Integer.valueOf(table_DMX_All.getValueAt(i, j).toString()).intValue() / 256);
					frame[i*512 + 511] = (byte)(Integer.valueOf(table_DMX_All.getValueAt(i, j).toString()).intValue() % 256);
				} else if (j == 2) {
					frame[i*512 + 508] = (byte)(Integer.valueOf(table_DMX_All.getValueAt(i, j).toString()).intValue() / 256);
					frame[i*512 + 509] = (byte)(Integer.valueOf(table_DMX_All.getValueAt(i, j).toString()).intValue() % 256);
				} else {
					frame[i*512 + (j-3)] = (byte)Integer.valueOf(table_DMX_All.getValueAt(i, j).toString()).intValue();
				}
			}
		}
		return frame;
	}
	
	/*public static void main(String[] args) {
		boolean[] b = {true,true,false,true,false,true,false,true,false,true,false,true,false,true,false,true};
		boolean[] temp = new boolean[8];
		for (int j = 0; j < 2; j++) {
			for (int i = 8*j; i < 8*(j+1); i++) {
				temp[i-8*j] = Data.checkList[i];
			}
			System.out.println(Byte.toUnsignedInt(bitTobyte(temp)));
		}
	}*/
	
	private static byte bitTobyte(boolean[] b){
		BitSet set = new BitSet(8);
		for (int i = 0; i < b.length; i++) {
			set.set(i, b[b.length-1 - i]);
		}
		byte[] t = set.toByteArray();
		if (t.length > 0) {
			return t[0];
		}
		return 0;
	}
}
