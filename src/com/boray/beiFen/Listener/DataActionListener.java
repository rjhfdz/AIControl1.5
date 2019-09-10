package com.boray.beiFen.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;

import com.boray.Data.Data;
import com.boray.changJing.Data.DataOfChangJing;
import com.boray.mainUi.MainUi;
import com.boray.zhongKon.Data.DataOfZhongKon;

public class DataActionListener {
	public void actionPerformed(OutputStream os) {
		
		saveChangJingData();
		saveZhongKongData();
		
		
		//����������
		List list = null;
		byte[][] b = new byte[41][95];
		
		for (int i = 0; i <= 40; i++) {
			list = (List)DataOfChangJing.map.get(""+i);
			b[i][0] = (byte)0xBB;b[i][1] = (byte)0xBB;
			b[i][2] = (byte)i;
			//����
			b[i][3] = (byte)(Integer.valueOf(list.get(8).toString()).intValue()+10);
			b[i][4] = (byte)(Integer.valueOf(list.get(10).toString()).intValue()+10);
			b[i][5] = (byte)(Integer.valueOf(list.get(12).toString()).intValue()+10);
			b[i][6] = (byte)(Integer.valueOf(list.get(14).toString()).intValue()+10);
			
			for (int j = 0; j < 8; j++) {
				b[i][7+j] = (byte)(Integer.valueOf(list.get(j).toString()).intValue()+10);
			}
			
			//����
			b[i][15] = (byte)(Integer.valueOf(list.get(9).toString()).intValue());
			b[i][16] = (byte)(Integer.valueOf(list.get(11).toString()).intValue());
			b[i][17] = (byte)(Integer.valueOf(list.get(13).toString()).intValue());
			b[i][18] = (byte)(Integer.valueOf(list.get(15).toString()).intValue());
			
			//����ģʽ
			b[i][21] = (byte)(Integer.valueOf(list.get(17).toString()).intValue());
			//���ȼ̳�
			b[i][22] = (byte)(Integer.valueOf(list.get(18).toString()).intValue());
			//ȫ������
			b[i][23] = (byte)(Integer.valueOf(list.get(16).toString()).intValue());
			//ҡ��ģʽ
			if (list.get(27).toString().equals("3")) {
				b[i][87] = (byte)172;
			} else {
				b[i][87] = (byte)(Integer.valueOf(list.get(27).toString()).intValue());
			}
			//20180907��ӹ̶��ֽ�
			if (i == 0) {
				b[0][32] = 0x05;
				/*b[0][33] = "B".getBytes()[0];
				b[0][34] = "o".getBytes()[0];
				b[0][35] = "r".getBytes()[0];
				b[0][36] = "a".getBytes()[0];
				b[0][37] = "y".getBytes()[0];*/
				b[0][33] = "O".getBytes()[0];
				b[0][34] = "t".getBytes()[0];
				b[0][35] = "h".getBytes()[0];
				b[0][36] = "e".getBytes()[0];
				b[0][37] = "r".getBytes()[0];
			}
		}
		
		//�п�ѧϰ��
		//����ǽ��
		byte[][] b1 = new byte[111][24];
		List list2,list3,list4,list5;
		int len2,len3,len4,len5;
		for (int i = 0; i < 111; i++) {
			len2 = 0;len3 = 0;len4 = 0;len5 = 0;
			list2 = (List)DataOfZhongKon.map2.get("1ǽ��"+i);
			list3 = (List)DataOfZhongKon.map2.get("2ǽ��"+i);
			list4 = (List)DataOfZhongKon.map2.get("3ǽ��"+i);
			list5 = (List)DataOfZhongKon.map2.get("4ǽ��"+i);
			if (list2 != null) {
				for (int j = 0; j < list2.size(); j++) {
					if (!"".equals(list2.get(j).toString().trim())) {
						b1[i][1+j] = (byte)Integer.valueOf(list2.get(j).toString().trim(), 16).intValue();
						//len2++;
						len2 = j+1;
					}
				}
				b1[i][0] = (byte)len2;
			}
			if (list3 != null) {
				for (int j = 0; j < list3.size(); j++) {
					if (!"".equals(list3.get(j).toString().trim())) {
						b1[i][7+j] = (byte)Integer.valueOf(list3.get(j).toString().trim(), 16).intValue();
						//len3++;
						len3 = j+1;
					}
				}
				b1[i][6] = (byte)len3;
			}
			if (list4 != null) {
				for (int j = 0; j < list4.size(); j++) {
					if (!"".equals(list4.get(j).toString().trim())) {
						b1[i][13+j] = (byte)Integer.valueOf(list4.get(j).toString().trim(), 16).intValue();
						//len4++;
						len4 = j+1;
					}
				}
				b1[i][12] = (byte)len4;
			}
			if (list5 != null) {
				for (int j = 0; j < list5.size(); j++) {
					if (!"".equals(list5.get(j).toString().trim())) {
						b1[i][19+j] = (byte)Integer.valueOf(list5.get(j).toString().trim(), 16).intValue();
						//len5++;
						len5 = j+1;
					}
				}
				b1[i][18] = (byte)len5;
			}
		}
		//��������
		byte[][] b2 = new byte[111][13];
		int len = 0;
		List list6;
		for (int i = 0; i < 111; i++) {
			len = 0;
			list6 = (List)DataOfZhongKon.map3.get(""+i);
			if (list6 != null) {
				for (int k = 0; k < list6.size(); k++) {
					if (!"".equals(list6.get(k).toString().trim())) {
						b2[i][1+k] = (byte)Integer.valueOf(list6.get(k).toString().trim(), 16).intValue();
						//len++;
						len = k + 1;
					}
				}
				b2[i][0] = (byte)len;
			}
		}
		//��������
		byte[][] b3 = new byte[111][13];
		List list7;
		for (int i = 0; i < 111; i++) {
			len = 0;
			list7 = (List)DataOfZhongKon.map4.get(""+i);
			if (list7 != null) {
				for (int k = 0; k < list7.size(); k++) {
					if (!"".equals(list7.get(k).toString().trim())) {
						b3[i][1+k] = (byte)Integer.valueOf(list7.get(k).toString().trim(), 16).intValue();
						//len++;
						len = k + 1;
					}
				}
				b3[i][0] = (byte)len;
			}
		}
		//���������
		byte[][] b4 = new byte[111][4];
		for (int i = 0; i < 111; i++) {
			for (int k = 0; k < 4; k++) {
				b4[i][k] = (byte)0xFF;
			}
		}
		List list8;
		for (int i = 0; i < 111; i++) {
			//len = 0;
			list8 = (List)DataOfZhongKon.map1.get(""+i);
			if (list8 != null) {
				for (int k = 0; k < 4; k++) {
					if (!"".equals(list8.get(k).toString().trim())) {
						b4[i][k] = (byte)Integer.valueOf(list8.get(k).toString().trim(), 16).intValue();
						//len++;
					}
				}
				//b4[i][0] = (byte)len;
			}
		}
		
		//ȫ��������
		byte[] b5 = new byte[890];
		List list9 = (List)MainUi.map.get("quanJuComponeList");
		//������
		JComboBox box = (JComboBox)list9.get(0);
		b5[0] = (byte)(box.getSelectedIndex()+1);
		/*if (box.getSelectedIndex() == 0) {
			b5[0] = (byte)72;
		} else {
			b5[0] = (byte)150;
		}*/
		//�ƹ�Э��
		box = (JComboBox)list9.get(1);
		b5[2] = (byte)(box.getSelectedIndex()+1);
		//���Ȳ�
		List list88 = null;
		for (int i = 0; i < 202; i++) {
			list88 = (List)DataOfZhongKon.map5.get(""+i);
			if (list88 == null) {
				b5[3+i] = (byte)0;
				b5[205+i] = (byte)0;
			} else {
				b5[3+i] = (byte)Integer.valueOf(list88.get(0).toString()).intValue();
				b5[205+i] = (byte)Integer.valueOf(list88.get(1).toString()).intValue();
			}
		}
		/////////////////���ز���20181126//////////////////////
		Map map;
		List list68;
		int[] tp;
		int time = 0;
		String s = "";
		for (int i = 1; i < 19; i++) {
			map = (Map)Data.ShengKonHuanJingModelMap.get(""+i);
			if (map!=null) {
				list68 = (List)map.get("0");
				if (list68 != null) {
					b5[406+i] = (byte)list68.size();
				}
				if (i == 17 || i == 18) {
					s = (String)map.get("4");
					if (s!=null) {
						b5[427+(i-17)] = (byte)(Integer.valueOf(s).intValue()+1);
					}
				}
			}
		}
		///////////////////////////////////////
		//ҡ����
		JComboBox box2 = (JComboBox)MainUi.map.get("yaoMaiJianGeBox");
		b5[423+12] = (byte)(box2.getSelectedIndex());
		//ҡ�󿪹�
		JRadioButton radioButton = (JRadioButton)MainUi.map.get("yaoMaiKaiGuangBtn1");
		if (radioButton.isSelected()) {
			b5[425+12] = 1;
		} else {
			b5[425+12] = 0;
		}
		//DMX�ֽڼ��
		box = (JComboBox)list9.get(2);
		b5[427+12] = (byte)box.getSelectedIndex();
		//DMX֡���
		box = (JComboBox)list9.get(3);
		b5[428+12] = (byte)box.getSelectedIndex();
		//Ч��������Դ��ַ   20181207��0����Ϊ1
		b5[437+12] = 1;
		//ҡ��1״̬��ַ
		JRadioButton radioButton2 = (JRadioButton)list9.get(6);
		if (radioButton2.isSelected()) {
			b5[438+12] = 0;
		} else {
			b5[438+12] = 1;
		}
		//ҡ��2״̬��ַ
		radioButton2 = (JRadioButton)list9.get(8);
		if (radioButton2.isSelected()) {
			b5[439+12] = 0;
		} else {
			b5[439+12] = 1;
		}
		//��ģʽ����
		for (int i = 0; i < 37; i++) {
			list = (List)DataOfChangJing.map.get(""+i);
			if (list.get(19).toString().equals("0")) {
				b5[454+i*10] = (byte)1;
			} else if (list.get(19).toString().equals("1")) {
				b5[454+i*10] = (byte)0;
			} else if (list.get(19).toString().equals("2")) {
				b5[454+i*10] = (byte)172;
			}
			b5[455+i*10] = (byte)Integer.valueOf(list.get(20).toString()).intValue();
			b5[456+i*10] = (byte)Integer.valueOf(list.get(21).toString()).intValue();
			b5[457+i*10] = (byte)Integer.valueOf(list.get(22).toString()).intValue();
			b5[458+i*10] = 0;
			b5[459+i*10] = (byte)Integer.valueOf(list.get(23).toString()).intValue();
			b5[460+i*10] = (byte)Integer.valueOf(list.get(24).toString()).intValue();
			b5[461+i*10] = (byte)Integer.valueOf(list.get(25).toString()).intValue();
			if (list.get(26).toString().equals("0")) {
				b5[462+i*10] = 2;
			} else {
				b5[462+i*10] = 1;
			}
		}
		//////////////���ذ�20181126/////
		List list81 = null;
		for (int i = 1; i < 17; i++) {
			list81 = (List)MainUi.map.get("callLightScen"+i);
			JRadioButton radioButton3 = (JRadioButton)list81.get(0);
			if (radioButton3.isSelected()) {
				b5[824+(i-1)*2] = 1;
			}
			JComboBox box3 = (JComboBox)list81.get(2);
			b5[825+(i-1)*2] = Integer.valueOf(box3.getSelectedItem().toString()).byteValue();
		}
		//////////////////////////////////
		
		//�п������� 111��ź��������
		//����ǽ��
		byte[][] b6 = new byte[91][24];
		for (int i = 111; i < 202; i++) {
			len2 = 0;len3 = 0;len4 = 0;len5 = 0;
			list2 = (List)DataOfZhongKon.map2.get("1ǽ��"+i);
			list3 = (List)DataOfZhongKon.map2.get("2ǽ��"+i);
			list4 = (List)DataOfZhongKon.map2.get("3ǽ��"+i);
			list5 = (List)DataOfZhongKon.map2.get("4ǽ��"+i);
			if (list2 != null) {
				for (int j = 0; j < list2.size(); j++) {
					if (!"".equals(list2.get(j).toString().trim())) {
						b6[i-111][1+j] = (byte)Integer.valueOf(list2.get(j).toString().trim(), 16).intValue();
						len2++;
					}
				}
				b6[i-111][0] = (byte)len2;
			}
			if (list3 != null) {
				for (int j = 0; j < list3.size(); j++) {
					if (!"".equals(list3.get(j).toString().trim())) {
						b6[i-111][7+j] = (byte)Integer.valueOf(list3.get(j).toString().trim(), 16).intValue();
						len3++;
					}
				}
				b6[i-111][6] = (byte)len3;
			}
			if (list4 != null) {
				for (int j = 0; j < list4.size(); j++) {
					if (!"".equals(list4.get(j).toString().trim())) {
						b6[i-111][13+j] = (byte)Integer.valueOf(list4.get(j).toString().trim(), 16).intValue();
						len4++;
					}
				}
				b6[i-111][12] = (byte)len4;
			}
			if (list5 != null) {
				for (int j = 0; j < list5.size(); j++) {
					if (!"".equals(list5.get(j).toString().trim())) {
						b6[i-111][19+j] = (byte)Integer.valueOf(list5.get(j).toString().trim(), 16).intValue();
						len5++;
					}
				}
				b6[i-111][18] = (byte)len5;
			}
		}
		//��������
		byte[][] b7 = new byte[91][13];
		for (int i = 111; i < 202; i++) {
			len = 0;
			list6 = (List)DataOfZhongKon.map3.get(""+i);
			if (list6 != null) {
				for (int k = 0; k < list6.size(); k++) {
					if (!"".equals(list6.get(k).toString().trim())) {
						b7[i-111][1+k] = (byte)Integer.valueOf(list6.get(k).toString().trim(), 16).intValue();
						len++;
					}
				}
				b7[i-111][0] = (byte)len;
			}
		}
		//��������
		byte[][] b8 = new byte[91][13];
		for (int i = 111; i < 202; i++) {
			len = 0;
			list7 = (List)DataOfZhongKon.map4.get(""+i);
			if (list7 != null) {
				for (int k = 0; k < list7.size(); k++) {
					if (!"".equals(list7.get(k).toString().trim())) {
						b8[i-111][1+k] = (byte)Integer.valueOf(list7.get(k).toString().trim(), 16).intValue();
						len++;
					}
				}
				b8[i-111][0] = (byte)len;
			}
		}
		//���������
		byte[][] b9 = new byte[91][4];
		for (int i = 111; i < 202; i++) {
			for (int k = 0; k < 4; k++) {
				b9[i-111][k] = (byte)0xFF;
			}
		}
		for (int i = 111; i < 202; i++) {
			len = 0;
			list8 = (List)DataOfZhongKon.map1.get(""+i);
			if (list8 != null) {
				for (int k = 0; k < 4; k++) {
					if (!"".equals(list8.get(k).toString().trim())) {
						b9[i-111][k] = (byte)Integer.valueOf(list8.get(k).toString().trim(), 16).intValue();
						//len++;
					}
				}
				//b9[i-111][0] = (byte)len;
			}
		}
		//�п���ɢ������
		byte[] b10 = new byte[868];
		b10[0] = 1;
		list = (ArrayList)MainUi.map.get("settingZhongKongListCompone");
		//�����
		radioButton = (JRadioButton)list.get(0);
		if (radioButton.isSelected()) {
			b10[819] = 0;
		} else {
			b10[819] = 1;
		}
		//������Ƶ��
		JSlider slider = (JSlider)list.get(8);
		b10[820] = (byte)slider.getValue();
		//�ƹ�����
		radioButton = (JRadioButton)list.get(2);
		if (radioButton.isSelected()) {
			b10[821] = 1;
		} else {
			b10[821] = 0;
		}
		//�յ�����
		radioButton = (JRadioButton)list.get(4);
		if (radioButton.isSelected()) {
			b10[822] = 1;
		} else {
			b10[822] = 0;
		}
		//���ⵥ����
		radioButton = (JRadioButton)list.get(6);
		if (radioButton.isSelected()) {
			b10[823] = 0;
		} else {
			b10[823] = 1;
		}
		
		/////�յ�������
		byte[] b11 = new byte[38];
		b11[0] = (byte)0xCF;
		b11[1] = (byte)1;
		b11[8] = (byte)0xCF;
		b11[9] = (byte)2;
		//���ػ�
		b11[2] = (byte)0x9A;
		b11[10] = (byte)0x9A;
		//����ģʽ
		JComboBox box1 = (JComboBox)MainUi.map.get("kongTiaoModelBox");
		b11[3] = (byte)(box1.getSelectedIndex()+106);
		b11[11] = (byte)(box1.getSelectedIndex()+106);
		//���ٵ�λ
		box1 = (JComboBox)MainUi.map.get("kongTiaoDangWeiBox");
		b11[4] = (byte)(box1.getSelectedIndex()+138);
		b11[12] = (byte)(box1.getSelectedIndex()+138);
		
		//��ǰ�¶�
		box1 = (JComboBox)MainUi.map.get("kongTiaoNowWenDuBox");
		b11[5] = (byte)(box1.getSelectedIndex()+18);
		b11[13] = (byte)(box1.getSelectedIndex()+18);
		//�趨�¶�
		box1 = (JComboBox)MainUi.map.get("kongTiaoSetWenDuBox");
		b11[6] = (byte)(box1.getSelectedIndex()+18);
		b11[14] = (byte)(box1.getSelectedIndex()+18);
		
		//�ŷ翪��
		box1 = (JComboBox)MainUi.map.get("kongTiaoPaiFengBox");
		b11[7] = (byte)(box1.getSelectedIndex()+80);
		b11[15] = (byte)(box1.getSelectedIndex()+80);
		
		//�յ�Ĭ�ϡ�Ԥ��ѡ��
		JRadioButton radioButton1 = (JRadioButton)MainUi.map.get("kongTiaoKaiJiBtn1");
		if (!radioButton1.isSelected()) {
			b11[33] = 4;
			for (int i = 0; i < 8; i++) {
				b11[16+i] = b11[0+i];
				b11[24+i] = b11[8+i];
			}
		} else {
			b11[33] = 3;
			/////////////////
			for (int i = 0; i < 8; i++) {
				b11[16+i] = b11[0+i];
				b11[24+i] = b11[8+i];
			}
		}
		//�յ���ģʽ
		box1 = (JComboBox)MainUi.map.get("kongTiaoFengJiTypeBox");
		b11[32] = (byte)(box1.getSelectedIndex()+1);
		b11[34] = 6;
		//���¶�
		JRadioButton radioButton3 = (JRadioButton)MainUi.map.get("kongTiaoShowWenDuBtn1");
		if (radioButton3.isSelected()) {
			b11[35] = 1;
		} else {
			b11[35] = 2;
		}
		//����յ�ģʽ
		JComboBox box7 = (JComboBox)MainUi.map.get("kongTiaoCenterModelBox");
		b11[36] = (byte)(box7.getSelectedIndex()+1);
		//485�յ���ַ
		box7 = (JComboBox)MainUi.map.get("kongTiaoRS485AddBox");
		b11[37] = (byte)(box7.getSelectedIndex()+1);
		
		///////////////////////////////////////////////
		try {
			/*JFileChooser fileChooser = new JFileChooser();
			try {
				String path = getClass().getResource("/SD���ļ�/").getPath().substring(1);
				path = URLDecoder.decode(path,"utf-8"); 
				fileChooser.setCurrentDirectory(new File(path));
			} catch (Exception e2) {
				//e2.printStackTrace();
			}
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setSelectedFile(new File("config.bry"));
			int returnVal = fileChooser.showSaveDialog((JFrame)MainUi.map.get("frame"));
			File file = null;
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				file = fileChooser.getSelectedFile();
			}
			if (file == null) {
				return;
			}*/
			/*String path = "";
			try {
				path = getClass().getResource("/SD���ļ�/").getPath().substring(1);
				path = URLDecoder.decode(path,"utf-8"); 
				path = path + "config_temp.bry";
			} catch (Exception e2) {
				//e2.printStackTrace();
			}
			File file = new File(path);
			//[41][95]
			FileOutputStream os = new FileOutputStream(file);*/
			for (int i = 0; i < 41; i++) {
				os.write(b[i],0,95);
			}
			//����
			byte[] tps = new byte[6560];
			os.write(tps);
			//�п�ǰ111��
			for (int i = 0; i < 111; i++) {
				os.write(b1[i]);
			}
			//�������
			byte[] ttp = new byte[2220];
			os.write(ttp);
			//��������
			for (int i = 0; i < 111; i++) {
				os.write(b2[i]);
			}
			//��������
			for (int i = 0; i < 111; i++) {
				os.write(b3[i]);
			}
			//�������
			for (int i = 0; i < 111; i++) {
				os.write(b4[i]);
			}
			///////ȫ��������
			os.write(b5);
			byte[] bpb = new byte[921];
			os.write(bpb);
			//�п���������91��
			for (int i = 0; i < 91; i++) {
				os.write(b6[i]);
			}
			//�������
			byte[] cc = new byte[1820];
			os.write(cc);
			//��������
			for (int i = 0; i < 91; i++) {
				os.write(b7[i]);
			}
			//��������
			for (int i = 0; i < 91; i++) {
				os.write(b8[i]);
			}
			//�������
			for (int i = 0; i < 91; i++) {
				os.write(b9[i]);
			}
			//�п���ɢȫ��������
			os.write(b10);
			
			//��ʼ��ַ28082            ����21069
			byte[] br1 = new byte[1752];
			os.write(br1);
			///���ػ�����ģʽ���� [18][20][41]   14760
			byte[][][] tp88 = shengKonEnvironmentData();
			for (int i = 0; i < 18; i++) {
				for (int j = 0; j < 20; j++) {
					os.write(tp88[i][j]);
				}
			}
			////////21070 - 16512 = 4558
			byte[] br2 = new byte[4558];
			
			os.write(br2);
			//����4096
			byte[] brt = new byte[4096];
			os.write(brt);
			//�յ�������
			os.write(b11);
			
			/////////////57344 - 53286 = 4058///////////////////////////
			byte[] tpNull = new byte[4058];
			os.write(tpNull);
			os.flush();
			
			//JFrame frame = (JFrame)MainUi.map.get("frame");
			//JOptionPane.showMessageDialog(frame, "�������óɹ�!", "��ʾ", JOptionPane.PLAIN_MESSAGE);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	public DataActionListener saveChangJingData(){
		if (Data.changJingModel != -1 && !Data.isTest) {
			List list = (List)DataOfChangJing.map.get(""+Data.changJingModel);
			if (list == null) {
				list = new ArrayList();
			}
			saveData(list);
			DataOfChangJing.map.put(""+Data.changJingModel, list);
		}
		return this;
	}
	private void saveData(List list){
		list.clear();
		//8�����ɵ�
		JComboBox[] boxs8 = (JComboBox[])MainUi.map.get("kaiGuangBox_BuKeTiao");
		for (int i = 0; i < boxs8.length; i++) {
			list.add(String.valueOf(boxs8[i].getSelectedIndex()));
		}
		
		//4���ƵĿ��غ�����
		JComboBox[] boxs = (JComboBox[])MainUi.map.get("kaiGuangBox");
		JSlider[] sliders = (JSlider[])MainUi.map.get("liangDuSliders");
		for (int i = 0; i < sliders.length; i++) {
			list.add(String.valueOf(boxs[i].getSelectedIndex()));
			list.add(String.valueOf(sliders[i].getValue()));
		}
		//ȫ������
		JSlider slider = (JSlider)MainUi.map.get("quanJuLiangDuSlider");
		list.add(String.valueOf(slider.getValue()));
		//����ģʽ
		JRadioButton radioButton = (JRadioButton)MainUi.map.get("kaiGuangModelBtn1");
		if (radioButton.isSelected()) {
			list.add(String.valueOf(1));
		} else {
			list.add(String.valueOf(0));
		}
		
		//����ģʽ
		JRadioButton radioButton3 = (JRadioButton)MainUi.map.get("liangDuModelBtn1");
		if (radioButton3.isSelected()) {
			list.add(String.valueOf(1));
		} else {
			list.add(String.valueOf(0));
		}
		
		//Ч���ƿ���
		JComboBox box = (JComboBox)MainUi.map.get("xiaoGuoDengKaiGuangBox");
		list.add(String.valueOf(box.getSelectedIndex()));
		
		//����ģʽ
		JComboBox[] boxs2 = (JComboBox[])MainUi.map.get("changJingModelBoxs");
		for (int i = 0; i < boxs2.length; i++) {
			list.add(String.valueOf(boxs2[i].getSelectedIndex()));
		}
		
		//����ģʽ
		JComboBox[] boxs1 = (JComboBox[])MainUi.map.get("shengKonModelBoxs");
		for (int i = 0; i < boxs1.length; i++) {
			list.add(String.valueOf(boxs1[i].getSelectedIndex()));
		}
		
		//����ģʽ
		JRadioButton radioButton1 = (JRadioButton)MainUi.map.get("yunXingModelBtn1");
		if (radioButton1.isSelected()) {
			list.add(String.valueOf(1));
		} else {
			list.add(String.valueOf(0));
		}
		
		//ҡ��ģʽ
		JComboBox box3 = (JComboBox)MainUi.map.get("yaoMaiModelBox");
		list.add(String.valueOf(box3.getSelectedIndex()));
		
		//ҡ�󴥷����
		/*JComboBox box2 = (JComboBox)MainUi.map.get("yaoMaiJianGeBox");
		list.add(String.valueOf(box2.getSelectedIndex()));
		
		//ҡ����������
		JRadioButton radioButton4 = (JRadioButton)MainUi.map.get("yaoMaiKaiGuangBtn1");
		if (radioButton4.isSelected()) {
			list.add(String.valueOf(1));
		} else {
			list.add(String.valueOf(0));
		}*/
	}

	public void saveZhongKongData(){
		JComboBox groupBox = (JComboBox)MainUi.map.get("zhongKonGroupBox");
		int zh = groupBox.getSelectedIndex();
		//������
		JTextField[] fields = (JTextField[])MainUi.map.get("redCodeFields");
		List list = (List)DataOfZhongKon.map1.get(""+zh);
		if (list == null) {
			list = new ArrayList();
		}
		list.clear();
		for (int i = 0; i < 4; i++) {
			list.add(fields[i].getText());
		}
		DataOfZhongKon.map1.put(""+zh, list);
		
		//����ǽ��
		JTextField[] fields2 = (JTextField[])MainUi.map.get("comQianBanFields1");
		JTextField[] fields3 = (JTextField[])MainUi.map.get("comQianBanFields2");
		JTextField[] fields4 = (JTextField[])MainUi.map.get("comQianBanFields3");
		JTextField[] fields5 = (JTextField[])MainUi.map.get("comQianBanFields4");
		List list2 = (List)DataOfZhongKon.map2.get("1ǽ��"+zh);
		List list3 = (List)DataOfZhongKon.map2.get("2ǽ��"+zh);
		List list4 = (List)DataOfZhongKon.map2.get("3ǽ��"+zh);
		List list5 = (List)DataOfZhongKon.map2.get("4ǽ��"+zh);
		if (list2 == null) {
			list2 = new ArrayList<>();
		}
		if (list3 == null) {
			list3 = new ArrayList<>();
		}
		if (list4 == null) {
			list4 = new ArrayList<>();
		}
		if (list5 == null) {
			list5 = new ArrayList<>();
		}
		list2.clear();list3.clear();
		list4.clear();list5.clear();
		for (int i = 0; i < 5; i++) {
			list2.add(fields2[i].getText());
			list3.add(fields3[i].getText());
			list4.add(fields4[i].getText());
			list5.add(fields5[i].getText());
		}
		DataOfZhongKon.map2.put("1ǽ��"+zh, list2);
		DataOfZhongKon.map2.put("2ǽ��"+zh, list3);
		DataOfZhongKon.map2.put("3ǽ��"+zh, list4);
		DataOfZhongKon.map2.put("4ǽ��"+zh, list5);
		
		//��������
		JTextField[] fields6 = (JTextField[])MainUi.map.get("comShangXingFields");
		List list6 = (List)DataOfZhongKon.map3.get(""+zh);
		if (list6 == null) {
			list6 = new ArrayList<>();
		}
		list6.clear();
		for (int i = 0; i < fields6.length; i++) {
			list6.add(fields6[i].getText());
		}
		DataOfZhongKon.map3.put(""+zh, list6);
		
		//��������
		JTextField[] fields7 = (JTextField[])MainUi.map.get("comXiaXingFields");
		List list7 = (List)DataOfZhongKon.map4.get(""+zh);
		if (list7 == null) {
			list7 = new ArrayList<>();
		}
		list7.clear();
		for (int i = 0; i < fields7.length; i++) {
			list7.add(fields7[i].getText());
		}
		DataOfZhongKon.map4.put(""+zh, list7);
		
		//�Ȳ�
		JComboBox box2 = (JComboBox)MainUi.map.get("heCaiBox");
		JComboBox box3 = (JComboBox)MainUi.map.get("daoCaiBox");
		List list8 = (List)DataOfZhongKon.map5.get(""+zh);
		if (list8 == null) {
			list8 = new ArrayList<>();
		}
		list8.clear();
		list8.add(String.valueOf(box2.getSelectedIndex()));
		list8.add(String.valueOf(box3.getSelectedIndex()));
		DataOfZhongKon.map5.put(""+zh, list8);
	}
	
	private byte[][][] shengKonEnvironmentData(){
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
			}
		}
		return b1;
	}
}
