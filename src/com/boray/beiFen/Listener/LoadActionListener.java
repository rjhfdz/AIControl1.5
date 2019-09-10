package com.boray.beiFen.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.boray.changJing.Data.DataOfChangJing;
import com.boray.mainUi.MainUi;
import com.boray.zhongKon.Data.DataOfZhongKon;

public class LoadActionListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		Object[] options = { "否", "是" };
		int yes = JOptionPane.showOptionDialog((JFrame)MainUi.map.get("frame"), "将覆盖之前的数据，是否要继续？", "警告",
		JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
		null, options, options[1]);
		if (yes == 1) {
			JFileChooser fileChooser = new JFileChooser();
			try {
				String path = getClass().getResource("/SD卡文件/").getPath().substring(1);
				path = URLDecoder.decode(path,"utf-8"); 
				fileChooser.setCurrentDirectory(new File(path));
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			String[] houZhui = {"dat","bry"};
			FileNameExtensionFilter filter = new FileNameExtensionFilter("*.dat,*.bry", houZhui);
			fileChooser.setFileFilter(filter);
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = fileChooser.showOpenDialog((JFrame)MainUi.map.get("frame"));
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				try {
					analyse_systemSet(file);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	private void analyse_systemSet(File file) throws Exception{
		InputStream is = new FileInputStream(file);
		
		List list = null;
		byte[][] b = new byte[41][95];
		
		for (int i = 0; i <= 40; i++) {
			list = (List)DataOfChangJing.map.get(""+i);
			is.read(b[i]);
			
			//b[i][0] = (byte)0xBB;b[i][1] = (byte)0xBB;
			//b[i][2] = (byte)i;
			//开关
			/*b[i][3] = (byte)(Integer.valueOf(list.get(8).toString()).intValue()+10);
			b[i][4] = (byte)(Integer.valueOf(list.get(10).toString()).intValue()+10);
			b[i][5] = (byte)(Integer.valueOf(list.get(12).toString()).intValue()+10);
			b[i][6] = (byte)(Integer.valueOf(list.get(14).toString()).intValue()+10);*/
			list.set(8,String.valueOf((Byte.toUnsignedInt(b[i][3])-10)));
			list.set(10,String.valueOf((Byte.toUnsignedInt(b[i][4])-10)));
			list.set(12,String.valueOf((Byte.toUnsignedInt(b[i][5])-10)));
			list.set(14,String.valueOf((Byte.toUnsignedInt(b[i][6])-10)));
			
			for (int j = 0; j < 8; j++) {
				//b[i][7+j] = (byte)(Integer.valueOf(list.get(j).toString()).intValue()+10);
				list.set(j,String.valueOf((Byte.toUnsignedInt(b[i][7+j])-10)));
			}
			
			//亮度
			/*b[i][15] = (byte)(Integer.valueOf(list.get(9).toString()).intValue());
			b[i][16] = (byte)(Integer.valueOf(list.get(11).toString()).intValue());
			b[i][17] = (byte)(Integer.valueOf(list.get(13).toString()).intValue());
			b[i][18] = (byte)(Integer.valueOf(list.get(15).toString()).intValue());*/
			list.set(9,String.valueOf((Byte.toUnsignedInt(b[i][15]))));
			list.set(11,String.valueOf((Byte.toUnsignedInt(b[i][16]))));
			list.set(13,String.valueOf((Byte.toUnsignedInt(b[i][17]))));
			list.set(15,String.valueOf((Byte.toUnsignedInt(b[i][18]))));
			
			//开关模式
			//b[i][21] = (byte)(Integer.valueOf(list.get(17).toString()).intValue());
			list.set(17,String.valueOf((Byte.toUnsignedInt(b[i][21]))));
			//亮度继承
			//b[i][22] = (byte)(Integer.valueOf(list.get(18).toString()).intValue());
			list.set(18,String.valueOf((Byte.toUnsignedInt(b[i][22]))));
			//全局亮度
			//b[i][23] = (byte)(Integer.valueOf(list.get(16).toString()).intValue());
			list.set(16,String.valueOf((Byte.toUnsignedInt(b[i][23]))));
			//摇麦模式
			/*if (list.get(27).toString().equals("3")) {
				b[i][87] = (byte)172;
			} else {
				b[i][87] = (byte)(Integer.valueOf(list.get(27).toString()).intValue());
			}*/
			if (Byte.toUnsignedInt(b[i][87])==172) {
				list.set(27,"3");
			} else {
				list.set(27,String.valueOf((Byte.toUnsignedInt(b[i][87]))));
			}
			DataOfChangJing.map.put(""+i,list);
		}
		//空着
		byte[] tps = new byte[6560];
		int cc = is.read(tps);
		int tt = 0;
		while (cc != 6560) {
			tt = is.read(tps,0,6560-cc);
			cc = cc + tt;
		}
		//中控学习区
		//串口墙板
		byte[][] b1 = new byte[111][24];
		List list2,list3,list4,list5;
		int len2,len3,len4,len5;
		for (int i = 0; i < 111; i++) {
			is.read(b1[i]);
			
			len2 = Byte.toUnsignedInt(b1[i][0]);
			len3 = Byte.toUnsignedInt(b1[i][6]);
			len4 = Byte.toUnsignedInt(b1[i][12]);
			len5 = Byte.toUnsignedInt(b1[i][18]);
			if (len2 != 0) {
				list2 = new ArrayList<>();
				for (int k = 0; k < len2; k++) {
					list2.add(Integer.toHexString(Byte.toUnsignedInt(b1[i][1+k])));
				}
				DataOfZhongKon.map2.put("1墙板"+i,list2);
			}
			if (len3 != 0) {
				list3 = new ArrayList<>();
				for (int k = 0; k < len3; k++) {
					list3.add(Integer.toHexString(Byte.toUnsignedInt(b1[i][7+k])));
				}
				DataOfZhongKon.map2.put("2墙板"+i,list3);
			}
			if (len4 != 0) {
				list4 = new ArrayList<>();
				for (int k = 0; k < len4; k++) {
					list4.add(Integer.toHexString(Byte.toUnsignedInt(b1[i][13+k])));
				}
				DataOfZhongKon.map2.put("3墙板"+i,list4);
			}
			if (len5 != 0) {
				list5 = new ArrayList<>();
				for (int k = 0; k < len5; k++) {
					list5.add(Integer.toHexString(Byte.toUnsignedInt(b1[i][19+k])));
				}
				DataOfZhongKon.map2.put("4墙板"+i,list5);
			}
		}
		//空着
		byte[] ttp = new byte[2220];
		cc = is.read(ttp);
		tt = 0;
		while (cc != 2220) {
			tt = is.read(ttp,0,2220-cc);
			cc = cc + tt;
		}
		//串口上行
		byte[][] b2 = new byte[111][13];
		int len = 0;
		List list6;
		for (int i = 0; i < 111; i++) {
			is.read(b2[i]);
			len = Byte.toUnsignedInt(b2[i][0]);
			if (len != 0) {
				list6 = new ArrayList<>();
				for (int j = 0; j < len; j++) {
					list6.add(Integer.toHexString(Byte.toUnsignedInt(b2[i][1+j])));
				}
				for (int j = len; j < 12; j++) {
					list6.add("");
				}
				DataOfZhongKon.map3.put(""+i, list6);
			}
		}
		//串口下行
		byte[][] b3 = new byte[111][13];
		List list7;
		for (int i = 0; i < 111; i++) {
			is.read(b3[i]);
			len = Byte.toUnsignedInt(b3[i][0]);
			if (len != 0) {
				list7 = new ArrayList<>();
				for (int j = 0; j < len; j++) {
					list7.add(Integer.toHexString(Byte.toUnsignedInt(b3[i][1+j])));
				}
				for (int j = len; j < 12; j++) {
					list7.add("");
				}
				DataOfZhongKon.map4.put(""+i, list7);
			}
		}
		//红外码输出
		byte[][] b4 = new byte[111][4];
		List list8;
		String s1,s2,s3,s4;
		for (int i = 0; i < 111; i++) {
			is.read(b4[i]);
			s1 = Integer.toHexString(Byte.toUnsignedInt(b4[i][0]));
			s2 = Integer.toHexString(Byte.toUnsignedInt(b4[i][1]));
			s3 = Integer.toHexString(Byte.toUnsignedInt(b4[i][2]));
			s4 = Integer.toHexString(Byte.toUnsignedInt(b4[i][3]));
			if (!(s1.equals("ff") && s2.equals("ff") && s3.equals("ff") && s4.equals("ff"))) {
				list8 = new ArrayList<>();
				list8.add(s1);list8.add(s2);
				list8.add(s3);list8.add(s4);
				
				DataOfZhongKon.map1.put(""+i, list8);
			}
		}
		
		//全局数据区
		byte[] b5 = new byte[890];
		is.read(b5);
		
		List list9 = (List)MainUi.map.get("quanJuComponeList");
		//波特率
		JComboBox box = (JComboBox)list9.get(0);
		box.setSelectedIndex(Byte.toUnsignedInt(b5[0])-1);
		//b5[0] = (byte)(box.getSelectedIndex()+1);
		
		//灯光协议
		box = (JComboBox)list9.get(1);
		box.setSelectedIndex(Byte.toUnsignedInt(b5[2])-1);
		//b5[2] = (byte)(box.getSelectedIndex()+1);
		//倒喝彩
		List list88 = null;
		int a1 , a2;
		for (int i = 0; i < 202; i++) {
			list88 = (List)DataOfZhongKon.map5.get(""+i);
			a1 = Byte.toUnsignedInt(b5[3+i]);
			a2 = Byte.toUnsignedInt(b5[205+i]);
			if (!(list88 == null && a1 == 0 && a2 == 0)) {
				if (list88 == null) {
					list88 = new ArrayList<>();
				}
				list88.clear();
				list88.add(String.valueOf(a1));
				list88.add(String.valueOf(a2));
				DataOfZhongKon.map5.put(""+i, list88);
			}
		}
		//摇麦间隔
		JComboBox box2 = (JComboBox)MainUi.map.get("yaoMaiJianGeBox");
		box2.setSelectedIndex(Byte.toUnsignedInt(b5[423+12]));
		//b5[423+12] = (byte)(box2.getSelectedIndex());
		//摇麦开关
		JRadioButton radioButton = (JRadioButton)MainUi.map.get("yaoMaiKaiGuangBtn1");
		JRadioButton radioButton22 = (JRadioButton)MainUi.map.get("yaoMaiKaiGuangBtn2");
		if (Byte.toUnsignedInt(b5[425+12]) == 1) {
			radioButton.setSelected(true);
		} else {
			radioButton22.setSelected(true);
		}
		//DMX字节间隔
		box = (JComboBox)list9.get(2);
		box.setSelectedIndex(Byte.toUnsignedInt(b5[427+12]));
		//b5[427+12] = (byte)box.getSelectedIndex();
		//DMX帧间隔
		box = (JComboBox)list9.get(3);
		box.setSelectedIndex(Byte.toUnsignedInt(b5[428+12]));
		//b5[428+12] = (byte)box.getSelectedIndex();
		//效果灯数据源地址
		//b5[437+12] = 0;
		//摇麦1状态地址
		JRadioButton radioButton2 = (JRadioButton)list9.get(6);
		JRadioButton radioButton33 = (JRadioButton)list9.get(7);
		if (Byte.toUnsignedInt(b5[438+12]) == 0) {
			radioButton2.setSelected(true);
		} else {
			radioButton33.setSelected(true);
		}
		//摇麦2状态地址
		radioButton2 = (JRadioButton)list9.get(8);
		radioButton33 = (JRadioButton)list9.get(9);
		if (Byte.toUnsignedInt(b5[439+12]) == 0) {
			radioButton2.setSelected(true);
		} else {
			radioButton33.setSelected(true);
		}
		
		//多模式关联
		int tp = 0;
		for (int i = 0; i < 37; i++) {
			list = (List)DataOfChangJing.map.get(""+i);
			tp = Byte.toUnsignedInt(b5[454+i*10]);
			if (tp == 0) {
				list.set(19, "1");
			} else if (tp == 1) {
				list.set(19, "0");
			} else if (tp == 172) {
				list.set(19, "2");
			}
			for (int j = 0; j < 6; j++) {
				if (j > 2) {
					list.set(20+j, String.valueOf(Byte.toUnsignedInt(b5[456+j+i*10])));
				} else {
					list.set(20+j, String.valueOf(Byte.toUnsignedInt(b5[455+j+i*10])));
				}
			}
			if (Byte.toUnsignedInt(b5[462+i*10]) == 2) {
				list.set(26, "0");
			} else {
				list.set(26, "1");
			}
			DataOfChangJing.map.put(""+i, list);
		}
		//空着
		byte[] bpb = new byte[921];
		cc = is.read(bpb);
		tt = 0;
		while (cc != 921) {
			tt = is.read(bpb,0,921-cc);
			cc = cc + tt;
		}
		//中控配置区 111组号后面的数据
		//串口墙板
		byte[][] b6 = new byte[91][24];
		for (int i = 111; i < 202; i++) {
			is.read(b6[i-111]);
			
			len2 = Byte.toUnsignedInt(b6[i-111][0]);
			len3 = Byte.toUnsignedInt(b6[i-111][6]);
			len4 = Byte.toUnsignedInt(b6[i-111][12]);
			len5 = Byte.toUnsignedInt(b6[i-111][18]);
			if (len2 != 0) {
				list2 = new ArrayList<>();
				for (int k = 0; k < len2; k++) {
					list2.add(Integer.toHexString(Byte.toUnsignedInt(b6[i-111][1+k])));
				}
				DataOfZhongKon.map2.put("1墙板"+i,list2);
			}
			if (len3 != 0) {
				list3 = new ArrayList<>();
				for (int k = 0; k < len3; k++) {
					list3.add(Integer.toHexString(Byte.toUnsignedInt(b6[i-111][7+k])));
				}
				DataOfZhongKon.map2.put("2墙板"+i,list3);
			}
			if (len4 != 0) {
				list4 = new ArrayList<>();
				for (int k = 0; k < len4; k++) {
					list4.add(Integer.toHexString(Byte.toUnsignedInt(b6[i-111][13+k])));
				}
				DataOfZhongKon.map2.put("3墙板"+i,list4);
			}
			if (len5 != 0) {
				list5 = new ArrayList<>();
				for (int k = 0; k < len5; k++) {
					list5.add(Integer.toHexString(Byte.toUnsignedInt(b6[i-111][19+k])));
				}
				DataOfZhongKon.map2.put("4墙板"+i,list5);
			}
		}
		//空着
		byte[] pp = new byte[1820];
		cc = is.read(pp);
		tt = 0;
		while (cc != 1820) {
			tt = is.read(pp,0,1820-cc);
			cc = cc + tt;
		}
		//串口上行
		byte[][] b7 = new byte[91][13];
		for (int i = 111; i < 202; i++) {
			is.read(b7[i-111]);
			
			len = Byte.toUnsignedInt(b7[i-111][0]);
			if (len != 0) {
				list6 = new ArrayList<>();
				for (int j = 0; j < len; j++) {
					list6.add(Integer.toHexString(Byte.toUnsignedInt(b7[i-111][1+j])));
				}
				for (int j = len; j < 12; j++) {
					list6.add("");
				}
				DataOfZhongKon.map3.put(""+i, list6);
			}
		}
		//串口下行
		byte[][] b8 = new byte[91][13];
		for (int i = 111; i < 202; i++) {
			is.read(b8[i-111]);
			
			len = Byte.toUnsignedInt(b8[i-111][0]);
			if (len != 0) {
				list7 = new ArrayList<>();
				for (int j = 0; j < len; j++) {
					list7.add(Integer.toHexString(Byte.toUnsignedInt(b8[i-111][1+j])));
				}
				for (int j = len; j < 12; j++) {
					list7.add("");
				}
				DataOfZhongKon.map4.put(""+i, list7);
			}
		}
		//红外码输出
		byte[][] b9 = new byte[91][4];
		for (int i = 111; i < 202; i++) {
			is.read(b9[i-111]);
			s1 = Integer.toHexString(Byte.toUnsignedInt(b9[i-111][0]));
			s2 = Integer.toHexString(Byte.toUnsignedInt(b9[i-111][1]));
			s3 = Integer.toHexString(Byte.toUnsignedInt(b9[i-111][2]));
			s4 = Integer.toHexString(Byte.toUnsignedInt(b9[i-111][3]));
			if (!(s1.equals("ff") && s2.equals("ff") && s3.equals("ff") && s4.equals("ff"))) {
				list8 = new ArrayList<>();
				list8.add(s1);list8.add(s2);
				list8.add(s3);list8.add(s4);
				DataOfZhongKon.map1.put(""+i, list8);
			}
		}
		//中控离散数据区
		byte[] b10 = new byte[868];
		is.read(b10);
		list = (ArrayList)MainUi.map.get("settingZhongKongListCompone");
		//服务灯
		radioButton = (JRadioButton)list.get(0);
		radioButton2 = (JRadioButton)list.get(1);
		if (Byte.toUnsignedInt(b10[819]) == 0) {
			radioButton.setSelected(true);
		} else {
			radioButton2.setSelected(true);
		}
		
		//红外码频率
		JSlider slider = (JSlider)list.get(8);
		slider.setValue(Byte.toUnsignedInt(b10[820]));
		//b10[820] = (byte)slider.getValue();
		//灯光上行
		radioButton = (JRadioButton)list.get(2);
		radioButton2 = (JRadioButton)list.get(3);
		if (Byte.toUnsignedInt(b10[821]) == 1) {
			radioButton.setSelected(true);
		} else {
			radioButton2.setSelected(true);
		}
		//空调上行
		radioButton = (JRadioButton)list.get(4);
		radioButton2 = (JRadioButton)list.get(5);
		if (Byte.toUnsignedInt(b10[822]) == 1) {
			radioButton.setSelected(true);
		} else {
			radioButton2.setSelected(true);
		}
		//红外单多码
		radioButton = (JRadioButton)list.get(6);
		radioButton2 = (JRadioButton)list.get(7);
		if (Byte.toUnsignedInt(b10[823]) == 0) {
			radioButton.setSelected(true);
		} else {
			radioButton2.setSelected(true);
		}
		
		//空着21069
		byte[] br = new byte[21070];
		cc = is.read(br);
		tt = 0;
		//System.out.println(cc);
		while (cc != 21070) {
			tt = is.read(br,0,21070-cc);
			cc = cc + tt;
		}
		//空着4096
		byte[] brt = new byte[4096];
		cc = is.read(brt);
		tt = 0;
		//System.out.println(cc);
		while (cc != 4096) {
			tt = is.read(brt,0,4096-cc);
			cc = cc + tt;
		}
		
		/////空调数据区
		byte[] b11 = new byte[38];
		is.read(b11);
		
		//开关机
		//b11[2] = (byte)0x9A;
		//b11[10] = (byte)0x9A;
		//冷热模式
		JComboBox box1 = (JComboBox)MainUi.map.get("kongTiaoModelBox");
		box1.setSelectedIndex(Byte.toUnsignedInt(b11[3])-106);
		//b11[3] = (byte)(box1.getSelectedIndex()+106);
		//b11[11] = (byte)(box1.getSelectedIndex()+106);
		//风速档位
		box1 = (JComboBox)MainUi.map.get("kongTiaoDangWeiBox");
		box1.setSelectedIndex(Byte.toUnsignedInt(b11[4])-138);
		//b11[4] = (byte)(box1.getSelectedIndex()+138);
		//b11[12] = (byte)(box1.getSelectedIndex()+138);
		
		//当前温度
		box1 = (JComboBox)MainUi.map.get("kongTiaoNowWenDuBox");
		box1.setSelectedIndex(Byte.toUnsignedInt(b11[5])-18);
		//b11[5] = (byte)0;
		//b11[13] = (byte)0;
		//设定温度
		box1 = (JComboBox)MainUi.map.get("kongTiaoSetWenDuBox");
		box1.setSelectedIndex(Byte.toUnsignedInt(b11[6])-18);
		//b11[6] = (byte)(box1.getSelectedIndex()+18);
		//b11[14] = (byte)(box1.getSelectedIndex()+18);
		//排风开关
		box1 = (JComboBox)MainUi.map.get("kongTiaoPaiFengBox");
		box1.setSelectedIndex(Byte.toUnsignedInt(b11[7])-80);
		//b11[7] = (byte)(box1.getSelectedIndex()+80);
		//b11[15] = (byte)(box1.getSelectedIndex()+80);
		
		//空调默认、预设选择
		JRadioButton radioButton1 = (JRadioButton)MainUi.map.get("kongTiaoKaiJiBtn1");
		JRadioButton radioButton2_2 = (JRadioButton)MainUi.map.get("kongTiaoKaiJiBtn2");
		if (Byte.toUnsignedInt(b11[33]) == 1) {
			radioButton1.setSelected(true);
		} else {
			radioButton2_2.setSelected(true);
		}
		
		//空调阀模式
		box1 = (JComboBox)MainUi.map.get("kongTiaoFengJiTypeBox");
		box1.setSelectedIndex(Byte.toUnsignedInt(b11[32])-1);
		//b11[32] = (byte)(box1.getSelectedIndex()+1);
		//b11[34] = 0;
		//显温度
		JRadioButton radioButton3 = (JRadioButton)MainUi.map.get("kongTiaoShowWenDuBtn1");
		JRadioButton radioButton4 = (JRadioButton)MainUi.map.get("kongTiaoShowWenDuBtn2");
		if (Byte.toUnsignedInt(b11[35]) == 1) {
			radioButton3.setSelected(true);
		} else {
			radioButton4.setSelected(true);
		}
		//中央空调模式
		JComboBox box7 = (JComboBox)MainUi.map.get("kongTiaoCenterModelBox");
		box7.setSelectedIndex(Byte.toUnsignedInt(b11[36])-1);
		//b11[36] = (byte)(box7.getSelectedIndex()+1);
		//485空调地址
		box7 = (JComboBox)MainUi.map.get("kongTiaoRS485AddBox");
		box7.setSelectedIndex(Byte.toUnsignedInt(b11[37])-1);
		//b11[37] = (byte)(box7.getSelectedIndex()+1);
	}
}
