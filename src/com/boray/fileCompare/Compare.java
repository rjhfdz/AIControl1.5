package com.boray.fileCompare;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.boray.Data.Data;
import com.boray.Data.DengKuData;
import com.boray.Data.GetChannelNumber;
import com.boray.beiFen.Listener.DataActionListener;
import com.boray.dengKu.Entity.BlackOutEntity;
import com.boray.dengKu.Entity.SpeedEntity;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.shengKon.UI.DefineJLable_shengKon;
import com.boray.shengKon.UI.DefineJLable_shengKon2;
import com.boray.xiaoGuoDeng.UI.DefineJLable;

public class Compare {
	public static void compareFile(){
		try {
			File file1 = Data.file;
			File file2 = Data.tempFile;
			InputStream is1 = new FileInputStream(file1);
			InputStream is2 = new FileInputStream(file2);
			byte[] b1 = new byte[512];
			byte[] b2 = new byte[512];
			int pckNum = 0;
			Data.packetNumList.clear();
			Data.valueList.clear();
			Data.reviewMap.clear();
			while ((is1.read(b1)) > 0) {
				is2.read(b2);
				pckNum++;
				for (int i = 0; i < b2.length; i++) {
					if (b1[i] != b2[i]) {
						//System.out.println(pckNum);
						Data.packetNumList.add(pckNum);
						Data.valueList.add(b2.clone());
						Data.reviewMap.put((pckNum-1), b2.clone());
						break;
					}
				}
			}
			is1.close();
			is2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void saveTemp(){
		try {
			String path = getClass().getResource("/").getPath();
			try {
				path = URLDecoder.decode(path, "UTF-8");
			} catch (UnsupportedEncodingException e2) {
				e2.printStackTrace();
			}
			File file10 = new File(path+"/borayTemp.dat");
			OutputStream os10 = new FileOutputStream(file10);
			//系统设置
			systemSet(os10);
			//红外空调
			byte[] b1 = new byte[4096];
			for (int i = 0; i < 64; i++) {
				os10.write(b1);
			}
			//灯库
			writeFile2(os10);
			byte[] b2 = new byte[4092];
			os10.write(b2);
			//12 点数据地址分配数据（80-85SEC）6
			actionTuXing(os10);
			os10.write(new byte[3146]);
			//录制抽样数据（86-109SEC）24
			for (int i = 0; i < 24; i++) {
				os10.write(b1);
			}
			//按步编程（倒彩&喝彩&摇麦-110-127SEC）
			writeHeCaiYaoMai(os10);
			writeWuJiModelData(os10);
			//4060-548
			byte[] b3 = new byte[3512];
			os10.write(b3);
			////录制数据（128-4127SE）
			byte[] b_FF = new byte[4096];
			for (int i = 0; i < b_FF.length; i++) {
				b_FF[i] = (byte)0xFF;
			}
			for (int i = 0; i < 4000; i++) {
				os10.write(b_FF);
			}
			////场景效果灯数据（动态空间,起始 4128SEC）
			for (int i = 1; i < 25; i++) {
				writeFile(os10,i);
			}
			////声控效果灯数据（动态空间）
			for (int i = 1; i < 17; i++) {
				writeShengKon(os10,i);
			}
			////多灯数据区-16 个声控模式
			shengKonMoreLigthData(os10);
			//////////////////////
			os10.flush();
			long length = file10.length();
			int sy = (int)(33554432-length);
			byte[] cc = new byte[sy];
			for (int i = 0; i < sy; i++) {
				cc[i] = (byte)0XFF;
			}
			os10.write(cc);
			os10.flush();
			os10.close();
			Data.tempFile = file10;
			//JFrame frame = (JFrame)MainUi.map.get("frame");
			//JOptionPane.showMessageDialog(frame, "生成灯控文件成功!", "提示", JOptionPane.PLAIN_MESSAGE);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	
	/*
	 * 灯库模块
	 */
	private void writeFile2(OutputStream os) throws Exception{
		//灯库
		NewJTable table = (NewJTable)MainUi.map.get("table_DkGl");
		byte[][] t1 = new byte[20][68];
		byte[][] t2 = new byte[20][37];
		for (int selected = 0; selected < table.getRowCount(); selected++) {
			String dkName = table.getValueAt(selected, 1).toString();
			int channelCount = Integer.valueOf(Data.DengKuChannelCountList.get(selected).toString());
			HashMap map = (HashMap)Data.DengKuList.get(selected);
			
			DengKuData dengKuData = new DengKuData();
			JComboBox[] channelBoxs_L = (JComboBox[])MainUi.map.get("lamp_1_To_16");
			JComboBox[] channelBoxs_R = (JComboBox[])MainUi.map.get("lamp_17_To_32");
			int[] c = new int[channelCount];
			
			if (channelCount > 16) {
				for (int i = 0; i < 16; i++) {
					c[i] = GetChannelNumber.get(map.get(channelBoxs_L[i].getName()).toString());
				}
				for (int i = 16; i < channelCount; i++) {
					c[i] = GetChannelNumber.get(map.get(channelBoxs_R[i-16].getName()).toString());
				}
			} else {
				for (int i = 0; i < channelCount; i++) {
					c[i] = GetChannelNumber.get(map.get(channelBoxs_L[i].getName()).toString());
				}
			}
			dengKuData.setChannel(c);
			dengKuData.setName(dkName);
			dengKuData.setNo(selected);
			dengKuData.setVersion((int)(Double.valueOf(Data.DengKuVersionList.get(selected).toString())*100));
			System.arraycopy(dengKuData.getbytes(), 7, t1[selected], 0,68);
			//熄灯速度通道
			System.arraycopy(getCC(selected), 7, t2[selected], 0,37);
		}
		
		//灯具
		byte[][] t3 = new byte[50][23];
		table = (NewJTable)MainUi.map.get("table_dengJu");
		//{"启用","ID","灯具名称","型号","库版本","DMX起始地址","占用通道数"};
		int add = 0;
		for (int i = 0; i < 50; i++) {
			if (i < table.getRowCount()) {
				t3[i][0] = (byte)(i+1);
				boolean b = (boolean)table.getValueAt(i, 0);
				if (b) {
					t3[i][1] = 1;
				}
				//地址
				add = Integer.valueOf((String)(table.getValueAt(i, 5))).intValue();
				t3[i][2] = (byte)(add/256);
				t3[i][3] = (byte)(add%256);
				//占用通道数
				add = Integer.valueOf((String)(table.getValueAt(i, 6))).intValue();
				t3[i][4] = (byte)add;
				//灯具名称
				String s = (String)(table.getValueAt(i, 2));
				t3[i][5] = (byte)s.getBytes().length;
				for (int j = 0; j < s.getBytes().length; j++) {
					t3[i][6+j] = s.getBytes()[j];
				}
				//关联灯库编号
				s = (String)(table.getValueAt(i, 3));
				s = s.split("#")[0].substring(2);
				t3[i][22] = (byte)Integer.valueOf(s).intValue();
			}
		}
		//灯具分组
		byte[][] t4 = new byte[30][28];
		table = (NewJTable)MainUi.map.get("GroupTable");
		int q,p;
		//{"启用","ID","组别名称"};
		for (int i = 0; i < 30; i++) {
			if (i < table.getRowCount()) {
				t4[i][1] = (byte)(i+1);
				boolean b = (boolean)table.getValueAt(i, 0);
				if (b) {
					t4[i][2] = 1;
				}
				//关联灯具编号
				TreeSet treeSet = (TreeSet)Data.GroupOfLightList.get(i);
				if (treeSet.size() > 0) {
					Iterator iterator = treeSet.iterator();
					while (iterator.hasNext()) {
						q = (int)iterator.next();
						p = q / 8;
						q = 7 - (q % 8);
						t4[i][3+p] = (byte)(Byte.toUnsignedInt(t4[i][3+p])+ (1 << q));
					}
				}
				//分组名称
				String s = (String)table.getValueAt(i, 2);
				t4[i][10] = (byte)s.getBytes().length;
				for (int j = 0; j < s.getBytes().length; j++) {
					t4[i][11+j] = s.getBytes()[j];
				}
			}
		}
		
		//写入文件
		for (int i = 0; i < 20; i++) {
			os.write(t1[i]);
		}
		for (int i = 0; i < 20; i++) {
			os.write(t2[i]);
		}
		os.write(new  byte[10]);
		for (int i = 0; i < 50; i++) {
			os.write(t3[i]);
		}
		for (int i = 0; i < 30; i++) {
			os.write(t4[i]);
		}
	}
	private byte[] getCC(int select){
		byte[] cc = new byte[48];
		cc[0] = (byte)0xFD;
		cc[1] = (byte)0x30;
		cc[2] = (byte)0x60;
		cc[3] = (byte)0xB9;
		cc[4] = (byte)0x21;
		cc[5] = (byte)0x00;
		cc[6] = (byte)0x01;
		cc[7] = (byte)0x04;
		
		Map map2 = (Map)Data.dengKuBlackOutAndSpeedList.get(select);
		if (map2 == null) {
			for (int i = 8; i < cc.length; i++) {
				cc[8] = 0;
			}
		} else {
			BlackOutEntity blackOutEntity = (BlackOutEntity)map2.get("blackOutEntity");
			SpeedEntity speedEntity = (SpeedEntity)map2.get("speedEntity");
			for (int i = 0; i < 4; i++) {
				if (blackOutEntity.getC(i)[0].equals("所有")) {
					cc[8+i*4] = (byte)0xFF;
				} else if (blackOutEntity.getC(i)[0].equals("无")) {
					cc[8+i*4] = 0;
				} else {
					cc[8+i*4] = (byte)Integer.valueOf(blackOutEntity.getC(i)[0]).intValue();
				}
				cc[9+i*4] = (byte)Integer.valueOf(blackOutEntity.getC(i)[1]).intValue();
				cc[10+i*4] = (byte)Integer.valueOf(blackOutEntity.getC(i)[2]).intValue();
			}
			for (int i = 0; i < 3; i++) {
				if (speedEntity.getS(i)[0].equals("无")) {
					cc[28+i*4] = (byte)0;
				} else {
					cc[28+i*4] = (byte)Integer.valueOf(speedEntity.getS(i)[0]).intValue();
				}
				cc[29+i*4] = (byte)Integer.valueOf(speedEntity.getS(i)[1]).intValue();
				cc[30+i*4] = (byte)Integer.valueOf(speedEntity.getS(i)[2]).intValue();
				if (speedEntity.getS(i)[3].equals("正向")) {
					cc[31+i*4] = (byte)0;
				} else {
					cc[31+i*4] = (byte)1;
				}
			}
		}
		return cc;
	}
	
	/*
	 * 场景模块
	 * sc 场景号
	 */
	private void writeFile(OutputStream os,int sc) throws Exception{
		byte[] t1 = new byte[2560];
		setT1(t1,sc);//引导数据
		os.write(t1);
		
		Object[][] objects = new Object[30][20];
		Object[][] zdyObjects = new Object[30][20];
		Object[][] gxObjects = new Object[30][20];
		byte[][][] t2 = timeBlockData(sc,objects,zdyObjects,gxObjects);
		byte[] t3 = null;
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 20; j++) {
				os.write(t2[i][j]);
				if (objects[i][j]!=null) {
					t3 = (byte[])objects[i][j];
				} else {
					t3 = new byte[64];
				}
				//System.out.println("组："+i+"块："+j+"###"+t2[i][j].length+"//"+t3.length);
				os.write(t3);
			}
		}
		byte[] b1 = null;
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 20; j++) {
				b1 = (byte[])zdyObjects[i][j];
				if (b1 == null) {
					b1 = new byte[5];
				}
				os.write(b1);
			}
		}
		byte[] b2 = null;
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 20; j++) {
				b2 = (byte[])gxObjects[i][j];
				if (b2 == null) {
					b2 = new byte[4];
					for (int j2 = 0; j2 < b2.length; j2++) {
						b2[j2] = (byte)0xFF;
					}
				}
				os.write(b2);
			}
		}
	}
	
	/* 引导数据
	 * sc场景号
	 */
	private void setT1(byte[] temp,int sc){
		temp[0] = 0x55;
		temp[1] = (byte)0xAA;
		short[][] a = new short[20][4];//场景启用

		JPanel[] timeBlockPanels = (JPanel[])MainUi.map.get("timeBlockPanels_group"+sc);
		int j = 0,yu = 0;
		int maxTime = 0,tp6 = 0;
		for (int i = 0; i < 20; i++) {
			for (int k = 1; k < 31; k++) {
				j = (k-1) / 8;
				yu = 7 - ((k-1) % 8);
				if (timeBlockPanels[k].isVisible()) {
					if (timeBlockPanels[k].getComponentCount() > i) {
						DefineJLable lable = (DefineJLable)timeBlockPanels[k].getComponent(i);
						if (lable.getText().contains("✔")) {
							tp6 = (lable.getX()+lable.getWidth())/5;
							if (tp6 > maxTime) {
								maxTime = tp6;
							}
							a[i][j] = (short) (a[i][j] + (1 << yu));
						}
					}
				}
			}
			for (int k = 0; k < 4; k++) {
				temp[20+(i*4)+k] = (byte)a[i][k];
			}
		}
		temp[3] = (byte)(maxTime % 256);
		temp[4] = (byte)(maxTime / 256);
		for (int k = 1; k < 31; k++) {
			temp[100+(k-1)*81] = (byte)timeBlockPanels[k].getComponentCount();
			for (int n = 0; n < timeBlockPanels[k].getComponentCount(); n++) {
				DefineJLable lable = (DefineJLable)timeBlockPanels[k].getComponent(n);
				int start = lable.getX()/5;
				int end = (lable.getX()+lable.getWidth())/5;
				temp[100+(k-1)*81+1+n*4] = (byte)(start % 256);
				temp[100+(k-1)*81+2+n*4] = (byte)(start / 256);
				
				temp[100+(k-1)*81+3+n*4] = (byte)(end % 256);
				temp[100+(k-1)*81+4+n*4] = (byte)(end / 256);
			}
		}
		
	}
	
	/* 时间块数据
	 * sc场景号
	 * objects 编辑数据
	 * zdyObjects  自定义动作数据
	 * gxObjects 场景勾选数据
	 */
	private byte[][][] timeBlockData(int sc,Object[][] objects,Object[][] zdyObjects,Object[][] gxObjects){
		HashMap hashMap = null;
		byte[][][] T1 = new byte[30][20][53];
		//Object[][] objects = new Object[30][20];
		JPanel[] timeBlockPanels = (JPanel[])MainUi.map.get("timeBlockPanels_group"+sc);

		NewJTable table3 = (NewJTable)MainUi.map.get("table_dengJu");//所有灯具
		
		for (int i = 0; i < 30; i++) {
			int number = Integer.valueOf(timeBlockPanels[i+1].getName()).intValue();
			int tt = 0,a = 0;
			if (number-1 < Data.GroupOfLightList.size()) {
				TreeSet treeSet = (TreeSet)Data.GroupOfLightList.get(number-1);
				if (!treeSet.isEmpty()) {
					a = (int) treeSet.first();
					String typeString = table3.getValueAt(a, 3).toString();
					int dengKuNumber = Integer.valueOf(typeString.split("#")[0].substring(2)).intValue()-1;
					tt = Integer.valueOf((String)Data.DengKuChannelCountList.get(dengKuNumber)).intValue();
				}
			}
			
			//byte[] bTp = new byte[(tt+2)*32];
			for (int j = 0; j < 20; j++) {
				hashMap = (HashMap)Data.XiaoGuoDengObjects[sc-1][i][j];
				byte[] bTp = new byte[(tt+2)*32];
				if (hashMap != null) {
					//动作图形
					Map map = (Map)hashMap.get("actionXiaoGuoData");
					List list = null;
					boolean bb = false;
					a = 0;
					if (map!=null) {
						int selected = Integer.valueOf((String)map.get("2"));
						if (selected == 1) {
							selected = 255;
						} else if (selected > 1) {
							selected = selected - 1;
						}
						T1[i][j][0] = (byte)selected;
						//运行速度
						int yunXinSpeed = Integer.valueOf((String)map.get("3"));
						T1[i][j][1] = (byte)yunXinSpeed;
						//使用开关 1启用/0关
						String ss = (String)map.get("0");
						if (ss!=null && "true".equals(ss)) {
							T1[i][j][2] = (byte)1;
						}
						//list = (List)hashMap.get("rgb3Data");
						/*if (list!=null) {
							bb = (boolean)list.get(0);
							if (bb) {
								T1[i][j][2] = (byte)1;
							}
						}*/
						//拆分    不拆分01/中间拆分02/两端拆分03
						String[] tp1 = (String[])map.get("4");
						int cc = Integer.valueOf(tp1[0])+1;
						T1[i][j][3] = (byte)cc;
						
						//拆分反向

						//X轴反向    是1/否0
						if ("true".equals(tp1[1])) {
							a = 1;
						} else {
							a = 0;
						}
						T1[i][j][5] = (byte)a;
						//X半
						if ("true".equals(tp1[2])) {
							a = 1;
						} else {
							a = 0;
						}
						T1[i][j][6] = (byte)a;
						//Y轴反向
						if ("true".equals(tp1[3])) {
							a = 1;
						} else {
							a = 0;
						}
						T1[i][j][7] = (byte)a;
						//Y半
						if ("true".equals(tp1[4])) {
							a = 1;
						} else {
							a = 0;
						}
						T1[i][j][8] = (byte)a;
						
						
						//时间A_L	时间B_H
						a = Integer.valueOf(tp1[5]).intValue()*10;
						T1[i][j][9] = (byte)(a%256);
						T1[i][j][10] = (byte)(a/256);
						
						///////////////自定义动作数据
						String[] values = (String[])map.get("1");
						byte[] bt1 = new byte[5];
						if (values != null) {
							if (values[0].equals("true")) {
								bt1[0] = 1;
							}
							for (int k = 1; k < bt1.length; k++) {
								bt1[k] = (byte)Integer.valueOf(values[k]).intValue();
							}
						}
						zdyObjects[i][j] = bt1;
						/////////////////////////
					} else {
						
					}
					
					//RGB1
					list = (List)hashMap.get("rgb1Data");
					String[] tp2 = null;
					boolean[] bs = null;
					String b = "";
					if (list!=null) {
						//红色
						tp2 = (String[])list.get(1);
						a = Integer.valueOf(tp2[0]).intValue();
						T1[i][j][11] = (byte)a;
						//绿色
						a = Integer.valueOf(tp2[1]).intValue();
						T1[i][j][12] = (byte)a;
						//蓝色
						a = Integer.valueOf(tp2[2]).intValue();
						T1[i][j][13] = (byte)a;
						//渐变类型
						b = (String)list.get(4);
						a = Integer.valueOf(b).intValue();
						T1[i][j][14] = (byte)a;
						//渐变
						bb = (boolean)list.get(5);
						a = 0;
						if (bb) {
							a = 1;
						}
						T1[i][j][15] = (byte)a;
						//参与渐变勾选
						bs = (boolean[])list.get(2);
						a = 0;
						if (bs[0]) {
							a = 128;
						}
						if (bs[1]) {
							a = a + 64;
						}
						if (bs[2]) {
							a = a + 32;
						}
						T1[i][j][16] = (byte)a;
						//渐变速度
						b = (String)list.get(6);
						a = Integer.valueOf(b).intValue();
						T1[i][j][17] = (byte)a;
						//使用开关
						bb = (boolean)list.get(0);
						a = 0;
						if (bb) {
							a = 1;
						}
						T1[i][j][18] = (byte)a;
						//拆分
						b = (String)list.get(7);
						a = Integer.valueOf(b).intValue()+1;
						T1[i][j][19] = (byte)a;
						
						//拆分反向
						bb = (boolean)list.get(8);
						a = 0;
						if (bb) {
							a = 1;
						}
						T1[i][j][20] = (byte)a;
						//时间A_L	时间B_H
						a = Integer.valueOf((String)list.get(9)).intValue()*10;
						T1[i][j][21] = (byte)(a%256);
						T1[i][j][22] = (byte)(a/256);
					}
					
					//RGB2
					list = (List)hashMap.get("rgb2Data");
					if (list != null) {
						//红色
						tp2 = (String[])list.get(1);
						a = Integer.valueOf(tp2[0]).intValue();
						T1[i][j][23] = (byte)a;
						//绿色
						a = Integer.valueOf(tp2[1]).intValue();
						T1[i][j][24] = (byte)a;
						//蓝色
						a = Integer.valueOf(tp2[2]).intValue();
						T1[i][j][25] = (byte)a;
						//渐变类型
						b = (String)list.get(4);
						a = Integer.valueOf(b).intValue();
						T1[i][j][26] = (byte)a;
						//渐变
						bb = (boolean)list.get(5);
						a = 0;
						if (bb) {
							a = 1;
						}
						T1[i][j][27] = (byte)a;
						//参与渐变勾选
						bs = (boolean[])list.get(2);
						a = 0;
						if (bs[0]) {
							a = 128;
						}
						if (bs[1]) {
							a = a + 64;
						}
						if (bs[2]) {
							a = a + 32;
						}
						T1[i][j][28] = (byte)a;
						//渐变速度
						b = (String)list.get(6);
						a = Integer.valueOf(b).intValue();
						T1[i][j][29] = (byte)a;
						//使用开关
						bb = (boolean)list.get(0);
						a = 0;
						if (bb) {
							a = 1;
						}
						T1[i][j][30] = (byte)a;
						//拆分
						b = (String)list.get(7);
						a = Integer.valueOf(b).intValue()+1;
						T1[i][j][31] = (byte)a;
						
						//拆分反向
						bb = (boolean)list.get(8);
						a = 0;
						if (bb) {
							a = 1;
						}
						T1[i][j][32] = (byte)a;
						//时间A_L	时间B_H
						a = Integer.valueOf((String)list.get(9)).intValue()*10;
						T1[i][j][33] = (byte)(a%256);
						T1[i][j][34] = (byte)(a/256);
					}
					
					//RGB3
					list = (List)hashMap.get("rgb3Data");
					if (list != null) {
						//红色
						tp2 = (String[])list.get(1);
						a = Integer.valueOf(tp2[0]).intValue();
						T1[i][j][35] = (byte)a;
						//绿色
						a = Integer.valueOf(tp2[1]).intValue();
						T1[i][j][36] = (byte)a;
						//蓝色
						a = Integer.valueOf(tp2[2]).intValue();
						T1[i][j][37] = (byte)a;
						//渐变类型
						b = (String)list.get(4);
						a = Integer.valueOf(b).intValue();
						T1[i][j][38] = (byte)a;
						//渐变
						bb = (boolean)list.get(5);
						a = 0;
						if (bb) {
							a = 1;
						}
						T1[i][j][39] = (byte)a;
						//参与渐变勾选
						bs = (boolean[])list.get(2);
						a = 0;
						if (bs[0]) {
							a = 128;
						}
						if (bs[1]) {
							a = a + 64;
						}
						if (bs[2]) {
							a = a + 32;
						}
						T1[i][j][40] = (byte)a;
						//渐变速度
						b = (String)list.get(6);
						a = Integer.valueOf(b).intValue();
						T1[i][j][41] = (byte)a;
						//使用开关
						bb = (boolean)list.get(0);
						a = 0;
						if (bb) {
							a = 1;
						}
						T1[i][j][42] = (byte)a;
						//拆分
						b = (String)list.get(7);
						a = Integer.valueOf(b).intValue()+1;
						T1[i][j][43] = (byte)a;
						
						//拆分反向
						bb = (boolean)list.get(8);
						a = 0;
						if (bb) {
							a = 1;
						}
						T1[i][j][44] = (byte)a;
						//时间A_L	时间B_H
						a = Integer.valueOf((String)list.get(9)).intValue()*10;
						T1[i][j][45] = (byte)(a%256);
						T1[i][j][46] = (byte)(a/256);
					}
					
					
					/////手动编程配置
					List list66 = (List)hashMap.get("channelData");
					Vector vector88 = null;
					if (list66!=null) {
						//////////勾选
						int r=0,yu=0;
						int[] bp1 = new int[4];
						boolean[] bn = (boolean[])list66.get(1);
						for (int k = 0; k < bn.length; k++) {
							r = k / 8;
							//yu = 7 - (k % 8);
							yu = k % 8;
							if (bn[k]) {
								bp1[r] = bp1[r] + (1 << yu);
							}
						}
						for (int k = bn.length; k < 32; k++) {
							r = k / 8;
							yu = k % 8;
							bp1[r] = bp1[r] + (1 << yu);
						}
						byte[] bp2 = new byte[4];
						for (int k = 0; k < bp1.length; k++) {
							bp2[k] = (byte)bp1[k];
						}
						gxObjects[i][j] = bp2;
						//////////////
						
						String[] ddTemp = (String[])list66.get(2);
						a = 0;
						vector88 = (Vector)list66.get(0);
						if (vector88!=null) {
							a = vector88.size();
						}
						//总帧数	
						T1[i][j][47] = (byte)a;
						//手动编程启用	
						T1[i][j][48] = (byte)1;
						
						//时差A_L	时差B_H	
						a = Integer.valueOf(ddTemp[2]).intValue();
						T1[i][j][49] = (byte)(a % 256);
						T1[i][j][50] = (byte)(a / 256);
						//拆分	
						a = Integer.valueOf(ddTemp[0]).intValue()+1;
						T1[i][j][51] = (byte)a;
						//拆分反向
						a = 0;
						if (!ddTemp[1].equals("0")) {
							a = 1;
						}
						T1[i][j][52] = (byte)a;
						
						Vector tpe = null;
						int lenght = tt+2;
						if (vector88!=null) {
							for (int n = 0; n < vector88.size(); n++) {
								tpe = (Vector)vector88.get(n);
								int timeTp = Integer.valueOf(tpe.get(1).toString()).intValue();
								bTp[(tt+2)*n] = (byte)(timeTp % 256);
								bTp[(tt+2)*n+1] = (byte)(timeTp / 256);
								if (lenght > tpe.size()) {
									lenght = tpe.size();
								}
								for (int k = 2; k < lenght; k++) {
									bTp[(tt+2)*n+k] = Integer.valueOf(tpe.get(k).toString()).byteValue();
								}
							}
						}
						
					}
				}
				objects[i][j] = bTp;
			}
		}
		return T1;
	}
	
	/*
	 * 场景模块 --雾机模式数据
	 */
	private void writeWuJiModelData(OutputStream os) throws Exception{
		List allList = null;
		Vector tp = null;
		int a = 0;
		String[] tkp2 = (String[])Data.wuJiMap.get("QJ_set");
		int x = 0,y=0;
		if (tkp2 != null) {
			x = Integer.valueOf(tkp2[0]);
			y = Integer.valueOf(tkp2[1]);
		}
		byte[][][] b1 = new byte[4][5][13];//启动
		byte[][][] b2 = new byte[4][5][13];//运行
		byte[][] b3 = new byte[4][7];//配置
		for (int i = 1; i < 5; i++) {
			allList = (List)Data.wuJiMap.get(""+i);
			if (allList != null) {
				Vector vector88 = (Vector)allList.get(0);//启动
				Vector vector99 = (Vector)allList.get(1);//运行
				String[] tkp = (String[])allList.get(2);
				if (tkp[0].equals("true")) {//雾机状态
					b3[i-1][0] = 1;
				}
				//地址
				b3[i-1][1] = (byte)(x/256);
				b3[i-1][2] = (byte)(x%256);
				//通道数
				b3[i-1][3] = (byte)y;
				
				if (tkp[2].equals("true")) {//运行模式
					b3[i-1][4] = 1;
				} else {
					b3[i-1][4] = 2;
				}
				if (vector88!=null) {
					for (int n = 0; n < vector88.size(); n++) {
						b1[i-1][n][0] = (byte)vector88.size();
						tp = (Vector)vector88.get(n);
						a = Integer.valueOf((String)(tp.get(1)));
						b1[i-1][n][1] = (byte)(a%256);
						b1[i-1][n][2] = (byte)(a/256);
						for (int j = 2; j < 12; j++) {
							a = Integer.valueOf((String)(tp.get(j)));
							b1[i-1][n][1+j] = (byte)a;
						}
					}
				}
				if (vector99!=null) {
					for (int n = 0; n < vector99.size(); n++) {
						b2[i-1][n][0] = (byte)vector99.size();
						tp = (Vector)vector99.get(n);
						a = Integer.valueOf((String)(tp.get(1)));
						b2[i-1][n][1] = (byte)(a%256);
						b2[i-1][n][2] = (byte)(a/256);
						for (int j = 2; j < 12; j++) {
							a = Integer.valueOf((String)(tp.get(j)));
							b2[i-1][n][1+j] = (byte)a;
						}
					}
				}
			}
		}
		//byte[] temp = new byte[548];
		for (int i = 0; i < 4; i++) {
			//System.arraycopy(b3[i], 0, temp, 520+i*7, 7);
			for (int j = 0; j < 5; j++) {
				//System.arraycopy(b1[i][j], 0, temp, i*130+j*26, 13);
				//System.arraycopy(b2[i][j], 0, temp, i*130+j*26+13, 13);
				os.write(b1[i][j]);
			}
			for (int j = 0; j < 5; j++) {
				os.write(b2[i][j]);
			}
		}
		for (int i = 0; i < 4; i++) {
			os.write(b3[i]);
		}
	}

	/*
	 * 场景模块--摇麦与倒喝彩模式数据
	 */
	private void writeHeCaiYaoMai(OutputStream os) throws Exception{
		///摇麦
		Vector[] vectors = null;
		boolean[][] tbs = null;
		String[] setValue = null;
		byte[][][] b1 = new byte[2][16][512];
		byte[][][] b2 = new byte[2][16][512];
		byte[][][] b3 = new byte[2][2][512];
		byte[][][] steps1 = new byte[2][2][1];//启用、运行 步数
		byte[][] set1 = new byte[2][7];//全局设置
		int a = 0,size8 = 0;
		Vector vector99 = null,tp = null;
		for (int n = 1; n < 3; n++) {
			size8 = 0;
			vectors = (Vector[])Data.YaoMaiMap.get("TableData"+n);
			tbs = (boolean[][])Data.YaoMaiMap.get("GouXuanValue"+n);
			if (vectors!=null) {
				vector99 = vectors[0];//启动阶段
				if (vector99!=null) {
					size8 = vector99.size();
					steps1[n-1][0][0] = (byte)size8;
					for (int i = 0; i < size8; i++) {
						tp = (Vector)vector99.get(i);
						a = Integer.valueOf((String)(tp.get(1)));
						b1[n-1][i][0] = (byte)(a%256);
						b1[n-1][i][1] = (byte)(a/256);
						for (int j = 2; j < 512; j++) {
							a = Integer.valueOf((String)(tp.get(j)));
							b1[n-1][i][j] = (byte)a;
						}
					}
				}
				
				vector99 = vectors[1];//运行阶段
				if (vector99!=null) {
					size8 = vector99.size();
					steps1[n-1][1][0] = (byte)size8;
					for (int i = 0; i < size8; i++) {
						tp = (Vector)vector99.get(i);
						a = Integer.valueOf((String)(tp.get(1)));
						b2[n-1][i][0] = (byte)(a%256);
						b2[n-1][i][1] = (byte)(a/256);
						for (int j = 2; j < 512; j++) {
							a = Integer.valueOf((String)(tp.get(j)));
							b2[n-1][i][j] = (byte)a;
						}
					}
				}
			}
			//勾选
			if (tbs != null) {
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < 510; j++) {
						if (tbs[i][j]) {
							b3[n-1][i][j+2] = 1;
						}
					}
				}
			}
			//全局设置
			setValue = (String[])Data.YaoMaiMap.get("YaoMaiSet"+n);
			set1[n-1][0] = 1;//启用
			if (setValue != null) {
				if (Boolean.valueOf(setValue[1])) {
					set1[n-1][1] = 1;
				} else {
					set1[n-1][1] = 2;
				}
				set1[n-1][2] = (byte)Integer.valueOf(setValue[2]).intValue();
			}
		}
		///////////////////////////
		///////////倒喝彩
		byte[][][] b1_2 = new byte[2][16][512];
		byte[][][] b2_2 = new byte[2][16][512];
		byte[][][] b3_2 = new byte[2][2][512];
		byte[][][] steps1_2 = new byte[2][2][1];//启用、运行 步数
		byte[][] set1_2 = new byte[2][7];//全局设置
		String[] s = {"效果灯倒彩模式","效果灯喝彩模式"};
		for (int n = 1; n < 3; n++) {
			size8 = 0;
			vectors = (Vector[])Data.DaoHeCaiMap.get("TableData"+s[n-1]);
			tbs = (boolean[][])Data.DaoHeCaiMap.get("GouXuanValue"+s[n-1]);
			if (vectors != null) {
				vector99 = vectors[0];//启动阶段
				if (vector99!=null) {
					size8 = vector99.size();
					steps1_2[n-1][0][0] = (byte)size8;
					for (int i = 0; i < size8; i++) {
						tp = (Vector)vector99.get(i);
						a = Integer.valueOf((String)(tp.get(1)));
						b1_2[n-1][i][0] = (byte)(a%256);
						b1_2[n-1][i][1] = (byte)(a/256);
						for (int j = 2; j < 512; j++) {
							a = Integer.valueOf((String)(tp.get(j)));
							b1_2[n-1][i][j] = (byte)a;
						}
					}
				}
				
				vector99 = vectors[1];//运行阶段
				if (vector99!=null) {
					size8 = vector99.size();
					steps1_2[n-1][1][0] = (byte)size8;
					for (int i = 0; i < size8; i++) {
						tp = (Vector)vector99.get(i);
						a = Integer.valueOf((String)(tp.get(1)));
						b2_2[n-1][i][0] = (byte)(a%256);
						b2_2[n-1][i][1] = (byte)(a/256);
						for (int j = 2; j < 512; j++) {
							a = Integer.valueOf((String)(tp.get(j)));
							b2_2[n-1][i][j] = (byte)a;
						}
					}
				}
				//勾选
				if (tbs != null) {
					for (int i = 0; i < 2; i++) {
						for (int j = 0; j < 510; j++) {
							if (tbs[i][j]) {
								b3_2[n-1][i][j+2] = 1;
							}
						}
					}
				}
				//全局设置
				setValue = (String[])Data.DaoHeCaiMap.get("DaoHeCaiSet"+s[n-1]);
				set1_2[n-1][0] = 1;//启用
				if (setValue != null) {
					if (Boolean.valueOf(setValue[1])) {
						set1_2[n-1][1] = 1;
					} else {
						set1_2[n-1][1] = 2;
					}
					set1_2[n-1][2] = (byte)Integer.valueOf(setValue[2]).intValue();
				}
			}
		}
		////////////////////////////
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 16; j++) {
				os.write(b1[i][j]);//摇麦启动
			}
			for (int j = 0; j < 16; j++) {
				os.write(b2[i][j]);//摇麦运行
			}
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 16; j++) {
				os.write(b1_2[i][j]);//倒喝彩启动
			}
			for (int j = 0; j < 16; j++) {
				os.write(b2_2[i][j]);//倒喝彩运行
			}
		}
		//勾选
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				os.write(b3[i][j]);
			}
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				os.write(b3_2[i][j]);
			}
		}
		//总步数
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				os.write(steps1[i][j]);
			}
		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				os.write(steps1_2[i][j]);
			}
		}
		//全局设置
		for (int i = 0; i < 2; i++) {
			os.write(set1[i]);
		}
		for (int i = 0; i < 2; i++) {
			os.write(set1_2[i]);
		}
	}

	private void writeShengKon(OutputStream os,int sc) throws Exception{
		
		ShengKonData(sc,os);
		
		//shengKonMoreLigthData(os);
	}
	/*
	 * 声控时间块数据
	 */
	private void ShengKonData(int scN,OutputStream os) throws Exception{
		byte[] b1 = new byte[1374];
		b1[0] = 0x55;b1[1] = (byte)0xAA;b1[2] = 0x00;
		JPanel[] timeBlockPanels  = (JPanel[])MainUi.map.get("timeBlockPanels"+scN);
		int j = 0,yu = 0;
		short[][] a = new short[10][4];//场景启用
		for (int i = 0; i < 10; i++) {//块
			for (int k = 1; k < 31; k++) {//组别
				j = (k-1) / 8;
				yu = 7 - ((k-1) % 8);
				if (timeBlockPanels[k].isVisible()) {
					if (timeBlockPanels[k].getComponentCount() > i) {
						DefineJLable_shengKon2 label = (DefineJLable_shengKon2)timeBlockPanels[k].getComponent(i);
						if (label.getBackground().getGreen() == 255) {
							a[i][j] = (short) (a[i][j] + (1 << yu));
						}
					}
				}
			}
			for (int k = 0; k < 4; k++) {
				b1[3+i*4+k] = (byte)a[i][k];
			}
		}
		//全局设置
		List list = (List)Data.ShengKonModelSet.get(""+scN);
		if (list != null) {
			int time = Integer.valueOf((String)(list.get(0))).intValue();
			b1[83] = (byte)(time % 256);
			b1[84] = (byte)(time / 256);
			time = Integer.valueOf((String)(list.get(2))).intValue();
			b1[85] = (byte)time;
			time = Integer.valueOf((String)(list.get(1))).intValue();
			b1[88] = (byte)(time % 256);
			b1[89] = (byte)(time / 256);
		}
		
		//低频持续组  [2][67 + 32];
		int[][] DataSetInts = null;
		for (int i = 0; i < 10; i++) {
			DataSetInts = (int[][])Data.ShengKonShiXuSetObjects[scN-1][i];
			if (DataSetInts!=null) {
				////////////低频持续组
				if (DataSetInts[0][64] == 1) {
					b1[90+7*i] = (byte)128;
				}
				if (DataSetInts[0][2] == 0) {
					b1[90+7*i] = (byte)(16+Byte.toUnsignedInt(b1[90+7*i]));
				} else if (DataSetInts[0][2] == 1) {
					b1[90+7*i] = (byte)(32+DataSetInts[0][3]+1+Byte.toUnsignedInt(b1[90+7*i]));
				} else if (DataSetInts[0][2] == 2) {
					b1[90+7*i] = (byte)(64+DataSetInts[0][3]+1+Byte.toUnsignedInt(b1[90+7*i]));
				}
				//b1[90+7*i] = (byte)(DataSetInts[0][3]+1+Byte.toUnsignedInt(b1[90+7*i]));
				int[] tp1 = new int[4];
				for (int k = 0; k < 32; k++) {
					j = k / 8;
					yu = 7 - (k % 8);
					if (DataSetInts[0][k + 67] == 1) {
						tp1[j] = tp1[j] + (1 << yu);
					}
				}
				for (int k = 0; k < 4; k++) {
					b1[91+7*i+k] = (byte)tp1[k];
				}
				b1[95+7*i] = (byte)(DataSetInts[0][65]+1);
				b1[96+7*i] = (byte)(DataSetInts[0][66]+1);
				//////////////////
				///////低频灭灯模式
				if (DataSetInts[0][0] == 0) {
					b1[204+i] = (byte)(16);
				} else if (DataSetInts[0][0] == 1) {
					b1[204+i] = (byte)(32+DataSetInts[0][1]+1);
				}
				//////低频声控顺序设置
				NewJTable table = (NewJTable)MainUi.map.get("GroupTable");
				int sl = 0;
				for (int n = 0; n < table.getRowCount(); n++) {
					boolean b = (boolean)table.getValueAt(n, 0);
					if (b) {
						sl++;
					}
				}
				for (int k = 0; k < 10; k++) {
					b1[254+k*6+60*i] = (byte)DataSetInts[0][4+k*6];
					for (int k2 = 0; k2 < 5; k2++) {
 						if (sl+2 == DataSetInts[0][5+k2+k*6]) {
							b1[255+60*i+k*6+k2] = (byte)31;
						} else if (sl+3 == DataSetInts[0][5+k2+k*6]) {
							b1[255+60*i+k*6+k2] = (byte)32;
						} else if (sl >= DataSetInts[0][5+k2+k*6]) {
							b1[255+60*i+k*6+k2] = (byte)DataSetInts[0][5+k2+k*6];
						}
					}
				}
				///////////////////高频持续组
				if (DataSetInts[1][64] == 1) {
					b1[864+7*i] = (byte)128;
				}
				if (DataSetInts[1][2] == 0) {
					b1[864+7*i] = (byte)(16+Byte.toUnsignedInt(b1[864+7*i]));
				} else if (DataSetInts[1][2] == 1) {
					b1[864+7*i] = (byte)(32+Byte.toUnsignedInt(b1[864+7*i]));
				} else if (DataSetInts[1][2] == 2) {
					b1[864+7*i] = (byte)(64+Byte.toUnsignedInt(b1[864+7*i]));
				}
				b1[864+7*i] = (byte)(DataSetInts[1][3]+1+Byte.toUnsignedInt(b1[864+7*i]));
				tp1 = new int[4];
				for (int k = 0; k < 32; k++) {
					j = k / 8;
					yu = 7 - (k % 8);
					if (DataSetInts[1][k + 67] == 1) {
						tp1[j] = tp1[j] + (1 << yu);
					}
				}
				for (int k = 0; k < 4; k++) {
					b1[865+7*i+k] = (byte)tp1[k];
				}
				b1[869+7*i] = (byte)(DataSetInts[1][65]+1);
				b1[869+7*i] = (byte)(DataSetInts[1][66]+1);
				//////////////////
				///////////高频灭灯模式
				if (DataSetInts[1][0] == 0) {
					b1[854+i] = (byte)(16);
				} else if (DataSetInts[1][0] == 1) {
					b1[854+i] = (byte)(32+DataSetInts[1][1]+1);
				}
				//////////////////
				////////////////高频声控顺序设置
				sl = 0;
				for (int n = 0; n < table.getRowCount(); n++) {
					boolean b = (boolean)table.getValueAt(n, 0);
					if (b) {
						sl++;
					}
				}
				for (int k = 0; k < 10; k++) {
					b1[974+k*4+40*i] = (byte)DataSetInts[1][4+k*6];
					for (int k2 = 0; k2 < 3; k2++) {
 						if (sl+1 == DataSetInts[1][5+k2+k*6]) {
							b1[975+40*i+k*4+k2] = (byte)31;
						} else if (sl+2 == DataSetInts[1][5+k2+k*6]) {
							b1[975+40*i+k*4+k2] = (byte)32;
						} else if (sl >= DataSetInts[1][5+k2+k*6]) {
							b1[975+40*i+k*4+k2] = (byte)DataSetInts[1][5+k2+k*6];
						}
					}
				}
				/////////////////
			}
		}
		//时间片设置
		int timeCount = timeBlockPanels[0].getComponentCount();
		int timeWidth = 0;
		b1[160] = (byte)timeCount;
		b1[161] = 0;b1[162] = 0;b1[163] = 0;
		for (int i = 0; i < timeCount; i++) {
			DefineJLable_shengKon label = (DefineJLable_shengKon)timeBlockPanels[0].getComponent(i);
			timeWidth = label.getWidth()/5;
			b1[164+i*2] = (byte)(timeWidth%256);
			b1[165+i*2] = (byte)(timeWidth/256);
		}
		os.write(b1);
		
		//块数据
		byte[][][] b2 = new byte[30][10][61];
		Map map = null;
		Vector vector88 = null;
		int size = 0;
		boolean[][] gouXuanValus = null;
		int j2,yu2;
		int[] al;
		for (int i = 0; i < 30; i++) {
			for (int k = 0; k < 10; k++) {
				map = (Map)Data.ShengKonEditObjects[scN-1][i][k];
				if (map!=null) {
					vector88 = (Vector)map.get("0");
					if (vector88 != null) {
						size = vector88.size();
						b2[i][k][0] = (byte)(size%256);
						b2[i][k][1] = (byte)(size/256);
					}
					gouXuanValus = (boolean[][])map.get("1");
					if (gouXuanValus != null) {
						for (int k2 = 0; k2 < gouXuanValus.length; k2++) {
							for (int l = 0; l < gouXuanValus[0].length; l++) {
								j2 = l / 8;
								yu2 = 7 - (l % 8);
								if (gouXuanValus[k2][l]) {
									b2[i][k][2+k2*4+j2] = (byte)(Byte.toUnsignedInt(b2[i][k][2+k2*4+j2])+ (1 << yu2));
								}
							}
						}
					}
					al = (int[])map.get("2");
					if (al!=null) {
						if (al[0] == 0) {//0表示独立
							b2[i][k][38] = 2;
						} else {
							b2[i][k][38] = 1;
						}
						//多灯控制
						b2[i][k][44] = (byte)al[10];
						//加速度
						if (al[1] == 0) {
							b2[i][k][46] = 1;
						}
						if (al[2] == 1) {
							b2[i][k][48] = 1;
						}
						b2[i][k][49] = (byte)al[3];
						
						b2[i][k][51] = (byte)al[4];
						b2[i][k][52] = (byte)al[6];
						b2[i][k][53] = (byte)al[8];
						
						b2[i][k][54] = (byte)al[5];
						b2[i][k][55] = (byte)al[7];
						b2[i][k][56] = (byte)al[9];
					}
				}
				
				//步数据
				int cnt = 0;//灯具数量
				int channelCount = 0;//通道数
				String typeString = "";
				//int number = Integer.valueOf(""+(i+1)).intValue();
				if (Data.GroupOfLightList.size() > i) {
					TreeSet treeSet = (TreeSet)Data.GroupOfLightList.get(i);
					cnt = treeSet.size();
					if (cnt > 0) {
						NewJTable table3 = (NewJTable)MainUi.map.get("table_dengJu");//所有灯具
						int v = (int) treeSet.first();
						typeString = table3.getValueAt(v, 3).toString();//灯具型号
					}
				}
				if (!typeString.equals("")) {
					int dengKuNumber = Integer.valueOf(typeString.split("#")[0].substring(2)).intValue()-1;
					channelCount = Integer.valueOf((String)Data.DengKuChannelCountList.get(dengKuNumber)).intValue();
				}
				int stepWidth = channelCount*cnt;
				int runTime = 0,lgth = 0;
				byte[][] b3 = new byte[32][stepWidth+2];
				if (vector88 != null) {
					Vector tp = null;
					for (int r = 0; r < vector88.size(); r++) {
						tp = (Vector)vector88.get(r);
						runTime = Integer.valueOf(tp.get(1).toString());
						b3[r][0] = (byte)(runTime%256);
						b3[r][1] = (byte)(runTime/256);
						if (tp.size()-2 >= stepWidth) {
							lgth = stepWidth+2;
						} else {
							lgth = tp.size();
						}
						for (int l = 2; l < lgth; l++) {
							b3[r][l] = Integer.valueOf(tp.get(l).toString()).byteValue();
						}
					}
					vector88 = null;
				}
				os.write(b2[i][k]);
				for (int l = 0; l < 32; l++) {
					os.write(b3[l]);
				}
			}
		}
		
	}

	/*
	 * 声控多灯模式数据
	 */
	private void shengKonMoreLigthData(OutputStream os) throws Exception{
		int size = 0,a=0;
		byte[][][] steps = new byte[16][2][1];//总步数
		Vector tp = null,vector99 = null;
		byte[][][][] b1 = new byte[16][2][32][512];//步编辑数据
		String[] sets = null;
		boolean[] tbs = null;
		byte[][][] channalGouXuan = new byte[16][2][64];//通道勾选
		byte[][][] globalSet = new byte[16][2][7];//全局设置
		int r=0 , yu=0;
		for (int scN = 1; scN < 17; scN++) {
			for (int setNo = 1; setNo < 3; setNo++) {
				vector99 = (Vector)Data.ShengKonModelDmxMap.get("TableData"+scN+""+setNo);
				sets = (String[])Data.ShengKonModelDmxMap.get("YaoMaiSet"+scN+""+setNo);
				tbs = (boolean[])Data.ShengKonModelDmxMap.get("GouXuanValue"+scN+""+setNo);//勾选
				if (vector99 != null) {
					size = vector99.size();
					steps[scN-1][setNo-1][0] = (byte)size;
					for (int i = 0; i < size; i++) {
						tp = (Vector)vector99.get(i);
						a = Integer.valueOf((String)(tp.get(1)));
						b1[scN-1][setNo-1][i][0] = (byte)(a%256);
						b1[scN-1][setNo-1][i][1] = (byte)(a/256);
						for (int j = 2; j < 512; j++) {
							a = Integer.valueOf((String)(tp.get(j)));
							b1[scN-1][setNo-1][i][j] = (byte)a;
						}
					}
				}
				if (sets != null) {
					globalSet[scN-1][setNo-1][2] = Integer.valueOf(sets[0]).byteValue();
				}
				if (tbs != null) {
					for (int k = 0; k < tbs.length; k++) {
						r = k / 8;
						yu = 7 - (k % 8);
						if (tbs[k]) {
							channalGouXuan[scN-1][setNo-1][r] = (byte)(Byte.toUnsignedInt(channalGouXuan[scN-1][setNo-1][r]) + (1 << yu));
						}
					}
				}
			}
		}
		//步编辑数据
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 2; j++) {
				for (int j2 = 0; j2 < 32; j2++) {
					os.write(b1[i][j][j2]);
				}
			}
		}
		//勾选数据
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 2; j++) {
				os.write(channalGouXuan[i][j]);
			}
		}
		//总步数
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 2; j++) {
				os.write(steps[i][j]);
			}
		}
		//全局设置
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 2; j++) {
				os.write(globalSet[i][j]);
			}
		}
	}

	/*
	 * 声控环境灯模式数据
	 */
	private void shengKonEnvironmentData(OutputStream os) throws Exception{
		byte[][][] b1 = new byte[18][20][41];
		Map map;
		List list;
		int[] tp;
		int time = 0;
		String s = null;
		for (int i = 1; i < 19; i++) {
			list = null;
			s = null;
			map = (Map)Data.ShengKonHuanJingModelMap.get(""+i);
			if (map!=null) {
				list = (List)map.get("0");
				s = (String)map.get("3");
			}
			for (int j = 0; j < 20; j++) {
				tp = null;
				if (list != null && list.size() > j) {
					tp = (int[])list.get(j);
				}
				if (tp != null) {
					time = tp[0];
					b1[i-1][j][33] = (byte)(time/256);
					b1[i-1][j][34] = (byte)(time%256);
					for (int k = 0; k < 8; k++) {
						if (tp[k+1] == 0) {
							b1[i-1][j][k] = (byte)tp[k+9];
						} else {
							b1[i-1][j][k] = (byte)0xAC;
						}
					}
				}
				if (s!=null) {
					if ("0".equals(s)) {
						b1[i-1][j][32] = (byte)1;
					} else if ("1".equals(s)) {
						b1[i-1][j][32] = (byte)2;
					} else if ("2".equals(s)) {
						b1[i-1][j][32] = (byte)0;
					}
				}
				os.write(b1[i-1][j]);
			}
		}
	}
	
	/*
	 * 12点坐标
	 */
	private void actionTuXing(OutputStream os) throws Exception{
		//颜色图形
		byte[][] t1 = new byte[255][42];
		String[] s = null;
		String[] tps = (String[])bezier.Data.itemMap.get("1");
		String itemName = "";
		for (int i = 61; i < t1.length; i++) {
			s = (String[])bezier.Data.map.get("color"+(i));
			if (s != null) {
				t1[i-1][0] = (byte)i;
				for (int j = 0; j < s.length; j++) {
					t1[i-1][j+1] = (byte)Double.valueOf(s[j]).intValue();
				}
				if (tps != null) {
					itemName = tps[i];
					if (itemName.length() <= 16) {
						t1[i-1][25] = (byte)itemName.length();
						for (int j = 0; j < itemName.length(); j++) {
							t1[i-1][26+j] = itemName.getBytes()[j];
						}
					}
				}
			}
		}
		//动作图形
		byte[][] t2 = new byte[255][42];
		String[] s2 = null;
		String[] tps2 = (String[])bezier.Data.itemMap.get("0");
		String itemName2 = "";
		for (int i = 51; i < t2.length; i++) {
			s2 = (String[])bezier.Data.map.get(""+(i+1));
			if (s2 != null) {
				t2[i-1][0] = (byte)i;
				for (int j = 0; j < s2.length; j++) {
					t2[i-1][j+1] = (byte)Double.valueOf(s2[j]).intValue();
				}
				if (tps2 != null) {
					itemName2 = tps2[i+1];
					if (itemName2.length() <= 16) {
						t2[i-1][25] = (byte)itemName2.length();
						for (int j = 0; j < itemName2.length(); j++) {
							t2[i-1][26+j] = itemName2.getBytes()[j];
						}
					}
				}
			}
		}
		os.write(new byte[10]);
		for (int i = 0; i < 255; i++) {
			os.write(t1[i]);
		}
		for (int i = 0; i < 255; i++) {
			os.write(t2[i]);
		}
	}
	
	/*
	 * 系统配置数据
	 */
	private void systemSet(OutputStream os) throws Exception{
		new DataActionListener().actionPerformed(os);
	}
}
