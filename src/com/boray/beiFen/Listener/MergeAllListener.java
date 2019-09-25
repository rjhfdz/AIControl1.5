package com.boray.beiFen.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import javax.swing.*;

import com.boray.Data.Data;
import com.boray.Data.DengKuData;
import com.boray.Data.GetChannelNumber;
import com.boray.dengKu.Entity.BlackOutEntity;
import com.boray.dengKu.Entity.SpeedEntity;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.shengKon.UI.DefineJLable_shengKon;
import com.boray.shengKon.UI.DefineJLable_shengKon2;
import com.boray.xiaoGuoDeng.UI.DefineJLable;

public class MergeAllListener implements ActionListener {

    //��ַ�ռ�-��������9ϵ��-���߰汾���ݿռ䣬���ο�-20190904��
    public void actionPerformed2(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        if (!Data.saveCtrlFilePath.equals("")) {
            fileChooser.setCurrentDirectory(new File(Data.saveCtrlFilePath));
        }
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setSelectedFile(new File("F0.DAT"));
        int returnVal = fileChooser.showSaveDialog((JFrame) MainUi.map.get("frame"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            Data.file = file;
            Data.saveCtrlFilePath = file.getParent();
			/*File file2 = new File(Data.saveCtrlFilePath+"/�ƿ�.dat");
			File file3 = new File(Data.saveCtrlFilePath+"/����.dat");
			File file4 = new File(Data.saveCtrlFilePath+"/���ģʽ.dat");
			File file5 = new File(Data.saveCtrlFilePath+"/ҡ�󵹺Ȳ�ģʽ.dat");
			File file6 = new File(Data.saveCtrlFilePath+"/����ģʽ.dat");
			File file7 = new File(Data.saveCtrlFilePath+"/���ض��ģʽ.dat");
			File file8 = new File(Data.saveCtrlFilePath+"/ϵͳ��������.dat");
			File file9 = new File(Data.saveCtrlFilePath+"/���ػ�����ģʽ.dat");
			File file88 = new File(Data.saveCtrlFilePath+"/12���ַ.dat");*/
            //File file10 = new File(Data.saveCtrlFilePath+"/F0.dat");
            try {
                //OutputStream os = new FileOutputStream(file);
				/*OutputStream os2 = new FileOutputStream(file2);
				OutputStream os3 = new FileOutputStream(file3);
				OutputStream os4 = new FileOutputStream(file4);
				OutputStream os5 = new FileOutputStream(file5);
				OutputStream os6 = new FileOutputStream(file6);
				OutputStream os7 = new FileOutputStream(file7);
				OutputStream os8 = new FileOutputStream(file8);
				OutputStream os9 = new FileOutputStream(file9);
				OutputStream os88 = new FileOutputStream(file88);*/
                OutputStream os10 = new FileOutputStream(file);
				/*writeFile2(os2);
				for (int i = 1; i < 25; i++) {
					writeFile(os3,i);
				}
				writeWuJiModelData(os4);
				writeHeCaiYaoMai(os5);
				for (int i = 1; i < 17; i++) {
					writeShengKon(os6,i);
				}
				shengKonMoreLigthData(os7);
				systemSet(os8);
				shengKonEnvironmentData(os9);
				actionTuXing(os88);
				os2.close();os3.close();os4.close();
				os5.close();os6.close();os7.close();
				os8.close();os9.close();os88.close();*/
                //////////////////////
                //ϵͳ����
                systemSet(os10);
                //����յ�
                byte[] b1 = new byte[4096];
                for (int i = 0; i < 64; i++) {
                    os10.write(b1);
                }
                //�ƿ�
                writeFile2(os10);
                byte[] b2 = new byte[4092];
                os10.write(b2);
                //12 �����ݵ�ַ�������ݣ�80-85SEC��6
                actionTuXing(os10);
                os10.write(new byte[3146]);
                //¼�Ƴ������ݣ�86-109SEC��24
                for (int i = 0; i < 24; i++) {
                    os10.write(b1);
                }
                //������̣�����&�Ȳ�&ҡ��-110-127SEC��
                writeHeCaiYaoMai(os10);
                writeWuJiModelData(os10);
                //4060-548
                byte[] b3 = new byte[3512];
                os10.write(b3);
                ////¼�����ݣ�128-4127SE��
                byte[] b_FF = new byte[4096];
                for (int i = 0; i < b_FF.length; i++) {
                    b_FF[i] = (byte) 0xFF;
                }
                for (int i = 0; i < 4000; i++) {
                    os10.write(b_FF);
                }
                ////Ч�����ز����ݣ���̬�ռ�,��ʼ 4128SEC��
                writeFile3(os10);
                ////����Ч�������ݣ���̬�ռ䣩
                for (int i = 1; i < 25; i++) {
                    writeFile(os10, i);
                }
                ////����Ч�������ݣ���̬�ռ䣩
                for (int i = 1; i < 17; i++) {
                    writeShengKon(os10, i);
                }
                ////���������-16 ������ģʽ
                shengKonMoreLigthData(os10);
                //////////////////////
                os10.flush();
                long length = file.length();
                int sy = (int) (33554432 - length);
                byte[] cc = new byte[sy];
                for (int i = 0; i < sy; i++) {
                    cc[i] = (byte) 0XFF;
                }
                os10.write(cc);
                os10.flush();
                os10.close();

                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "���ɵƿ��ļ��ɹ�!", "��ʾ", JOptionPane.PLAIN_MESSAGE);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    //��ַ�ռ�-���������زİ汾��
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        if (!Data.saveCtrlFilePath.equals("")) {
            fileChooser.setCurrentDirectory(new File(Data.saveCtrlFilePath));
        }
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setSelectedFile(new File("F0.DAT"));
        int returnVal = fileChooser.showSaveDialog((JFrame) MainUi.map.get("frame"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            Data.file = file;
            Data.saveCtrlFilePath = file.getParent();
            try {
                OutputStream os10 = new FileOutputStream(file);
                //////////////////////
                //ϵͳ����(00-13SEC)14
                systemSet(os10);

                os10.flush();
                System.out.println("ϵͳ���ã�"+file.length());

                //�ƿ�(14-15SEC)2
                writeFile2(os10);
                byte[] b2 = new byte[4092];
                os10.write(b2);

                os10.flush();
                System.out.println("�ƿ⣺"+file.length());

                //������̣�����&�Ȳ�&ҡ��-16-33SEC��18
                writeHeCaiYaoMai(os10);

                repairData(139264,os10,file);

                os10.flush();
                System.out.println("������̣�"+file.length());


                writeWuJiModelData(os10);//������(34SEC)

                repairData(143360,os10,file);

                os10.flush();
                System.out.println("�����̣�"+file.length());


                ////Ч�����ز����ݣ�35-228SEC��192
                writeFile3(os10);
                //�����ݽ��в���
                repairData(937984,os10,file);

                os10.flush();
                System.out.println("Ч�����زģ�"+file.length());

                ////����Ч�������ݣ�229-258SEC��30
                for (int i = 1; i < 25; i++) {
                    writeFile(os10, i);
                }

                os10.flush();
                System.out.println("����Ч���ƣ�"+file.length());

                ////���������-16 ������ģʽ(259-387SEC)129
                shengKonMoreLigthData(os10);

                os10.flush();
                System.out.println("���������-16��"+file.length());

                ////����Ч�������ݣ���̬�ռ䣩
                for (int i = 1; i < 17; i++) {
                    writeShengKon(os10, i);
                }

                os10.flush();
                System.out.println("����Ч�������ݣ�"+file.length());

                //���м䲿�ֽ��в������
                repairData(16379904,os10,file);
                //¼�Ƴ������ݣ�4000-4023SEC��24
                byte[] b5 = new byte[4096];
                for (int i = 0; i < 24; i++) {
                    os10.write(b5);
                }

                os10.flush();
                System.out.println("¼�Ƴ������ݣ�"+file.length());

                ////¼�����ݣ�4024-8023SEC��
                byte[] b_FF = new byte[4096];
                for (int i = 0; i < b_FF.length; i++) {
                    b_FF[i] = (byte) 0xFF;
                }
                for (int i = 0; i < 4000; i++) {
                    os10.write(b_FF);
                }

                os10.flush();
                System.out.println("¼�����ݣ�"+file.length());

                //����յ�(8024-8087SEC)
                byte[] b1 = new byte[4096];
                for (int i = 0; i < 64; i++) {
                    os10.write(b1);
                }

                os10.flush();
                System.out.println("����յ���"+file.length());

                //////////////////////
                repairData(33554432,os10,file);
                os10.flush();
                os10.close();

                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "���ɵƿ��ļ��ɹ�!", "��ʾ", JOptionPane.PLAIN_MESSAGE);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * �����ݿռ䲻��Ľ��в���
     * @param length
     * @param outputStream
     * @param file
     */
    private void repairData(long length,OutputStream outputStream,File file) throws IOException {
        outputStream.flush();
        int sy = (int) (length - file.length());
        byte[] cc = new byte[sy];
        for (int i = 0; i < sy; i++) {
            cc[i] = (byte) 0XFF;
        }
        outputStream.write(cc);
    }

    /*
     * �ƿ�ģ��
     */
    private void writeFile2(OutputStream os) throws Exception {
        //�ƿ�
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        byte[][] t1 = new byte[20][68];
        byte[][] t2 = new byte[20][37];
        for (int selected = 0; selected < table.getRowCount(); selected++) {
            String dkName = table.getValueAt(selected, 1).toString();
            int channelCount = Integer.valueOf(Data.DengKuChannelCountList.get(selected).toString());
            HashMap map = (HashMap) Data.DengKuList.get(selected);

            DengKuData dengKuData = new DengKuData();
            JComboBox[] channelBoxs_L = (JComboBox[]) MainUi.map.get("lamp_1_To_16");
            JComboBox[] channelBoxs_R = (JComboBox[]) MainUi.map.get("lamp_17_To_32");
            int[] c = new int[channelCount];

            if (channelCount > 16) {
                for (int i = 0; i < 16; i++) {
                    c[i] = GetChannelNumber.get(map.get(channelBoxs_L[i].getName()).toString());
                }
                for (int i = 16; i < channelCount; i++) {
                    c[i] = GetChannelNumber.get(map.get(channelBoxs_R[i - 16].getName()).toString());
                }
            } else {
                for (int i = 0; i < channelCount; i++) {
                    c[i] = GetChannelNumber.get(map.get(channelBoxs_L[i].getName()).toString());
                }
            }
            dengKuData.setChannel(c);
            dengKuData.setName(dkName);
            dengKuData.setNo(selected);
            dengKuData.setVersion((int) (Double.valueOf(Data.DengKuVersionList.get(selected).toString()) * 100));
            System.arraycopy(dengKuData.getbytes(), 7, t1[selected], 0, 68);
            //Ϩ���ٶ�ͨ��
            System.arraycopy(getCC(selected), 7, t2[selected], 0, 37);
        }

        //�ƾ�
        byte[][] t3 = new byte[50][23];
        table = (NewJTable) MainUi.map.get("table_dengJu");
        //{"����","ID","�ƾ�����","�ͺ�","��汾","DMX��ʼ��ַ","ռ��ͨ����"};
        int add = 0;
        for (int i = 0; i < 50; i++) {
            if (i < table.getRowCount()) {
                t3[i][0] = (byte) (i + 1);
                boolean b = (boolean) table.getValueAt(i, 0);
                if (b) {
                    t3[i][1] = 1;
                }
                //��ַ
                add = Integer.valueOf((String) (table.getValueAt(i, 5))).intValue();
                t3[i][2] = (byte) (add / 256);
                t3[i][3] = (byte) (add % 256);
                //ռ��ͨ����
                add = Integer.valueOf((String) (table.getValueAt(i, 6))).intValue();
                t3[i][4] = (byte) add;
                //�ƾ�����
                String s = (String) (table.getValueAt(i, 2));
                t3[i][5] = (byte) s.getBytes().length;
                for (int j = 0; j < s.getBytes().length; j++) {
                    t3[i][6 + j] = s.getBytes()[j];
                }
                //�����ƿ���
                s = (String) (table.getValueAt(i, 3));
                s = s.split("#")[0].substring(2);
                t3[i][22] = (byte) Integer.valueOf(s).intValue();
            }
        }
        //�ƾ߷���
        byte[][] t4 = new byte[30][28];
        table = (NewJTable) MainUi.map.get("GroupTable");
        int q, p;
        //{"����","ID","�������"};
        for (int i = 0; i < 30; i++) {
            if (i < table.getRowCount()) {
                t4[i][1] = (byte) (i + 1);
                boolean b = (boolean) table.getValueAt(i, 0);
                if (b) {
                    t4[i][2] = 1;
                }
                //�����ƾ߱��
                TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(i);
                if (treeSet.size() > 0) {
                    Iterator iterator = treeSet.iterator();
                    while (iterator.hasNext()) {
                        q = (int) iterator.next();
                        p = q / 8;
                        q = 7 - (q % 8);
                        t4[i][3 + p] = (byte) (Byte.toUnsignedInt(t4[i][3 + p]) + (1 << q));
                    }
                }
                //��������
                String s = (String) table.getValueAt(i, 2);
                t4[i][10] = (byte) s.getBytes().length;
                for (int j = 0; j < s.getBytes().length; j++) {
                    t4[i][11 + j] = s.getBytes()[j];
                }
            }
        }

        //д���ļ�
        for (int i = 0; i < 20; i++) {
            os.write(t1[i]);
        }
        for (int i = 0; i < 20; i++) {
            os.write(t2[i]);
        }
        os.write(new byte[10]);
        for (int i = 0; i < 50; i++) {
            os.write(t3[i]);
        }
        for (int i = 0; i < 30; i++) {
            os.write(t4[i]);
        }
    }

    private byte[] getCC(int select) {
        byte[] cc = new byte[48];
        cc[0] = (byte) 0xFD;
        cc[1] = (byte) 0x30;
        cc[2] = (byte) 0x60;
        cc[3] = (byte) 0xB9;
        cc[4] = (byte) 0x21;
        cc[5] = (byte) 0x00;
        cc[6] = (byte) 0x01;
        cc[7] = (byte) 0x04;

        Map map2 = (Map) Data.dengKuBlackOutAndSpeedList.get(select);
        if (map2 == null) {
            for (int i = 8; i < cc.length; i++) {
                cc[8] = 0;
            }
        } else {
            BlackOutEntity blackOutEntity = (BlackOutEntity) map2.get("blackOutEntity");
            SpeedEntity speedEntity = (SpeedEntity) map2.get("speedEntity");
            for (int i = 0; i < 4; i++) {
                if (blackOutEntity.getC(i)[0].equals("����")) {
                    cc[8 + i * 4] = (byte) 0xFF;
                } else if (blackOutEntity.getC(i)[0].equals("��")) {
                    cc[8 + i * 4] = 0;
                } else {
                    cc[8 + i * 4] = (byte) Integer.valueOf(blackOutEntity.getC(i)[0]).intValue();
                }
                cc[9 + i * 4] = (byte) Integer.valueOf(blackOutEntity.getC(i)[1]).intValue();
                cc[10 + i * 4] = (byte) Integer.valueOf(blackOutEntity.getC(i)[2]).intValue();
            }
            for (int i = 0; i < 3; i++) {
                if (speedEntity.getS(i)[0].equals("��")) {
                    cc[28 + i * 4] = (byte) 0;
                } else {
                    cc[28 + i * 4] = (byte) Integer.valueOf(speedEntity.getS(i)[0]).intValue();
                }
                cc[29 + i * 4] = (byte) Integer.valueOf(speedEntity.getS(i)[1]).intValue();
                cc[30 + i * 4] = (byte) Integer.valueOf(speedEntity.getS(i)[2]).intValue();
                if (speedEntity.getS(i)[3].equals("����")) {
                    cc[31 + i * 4] = (byte) 0;
                } else {
                    cc[31 + i * 4] = (byte) 1;
                }
            }
        }
        return cc;
    }

    private void writeFile3(OutputStream os) throws Exception {
        //�ز�������
        byte[] bytes = new byte[120];
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        for (int i = 0; i < table.getRowCount(); i++) {
            //�̶�ͷ
            bytes[0 + (i * 6)] = 0x55;
            bytes[1 + (i * 6)] = (byte) 0xAA;
            //�زĸ���
            String dengKuId = (String) table.getValueAt(i, 0);
            String dengKuName = (String) table.getValueAt(i, 1);
            Map map2 = (Map) Data.suCaiMap.get(dengKuName);
            JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
            int cnt = 0;
            if (map2 != null) {
                for (int j = 0; j < btns.length; j++) {
                    List abc = (List) map2.get("" + j);
                    if (abc != null) {
                        cnt = cnt + abc.size();
                    }
                }
            }
            bytes[2 + (i * 6)] = (byte) cnt;
            //�ز�ͨ����
            int suCaiTonDaoShu = Integer.parseInt(Data.DengKuChannelCountList.get(Integer.parseInt(dengKuId) - 1).toString());
            bytes[3 + (i * 6)] = (byte) suCaiTonDaoShu;
            //�ز�����(Ĭ������)
            bytes[4 + (i * 6)] = 01;
            //�ز����ԣ���δ���壩
        }
        os.write(bytes);
        //�ز�������
        HashMap hashMap = null;
        byte[][][] T1 = new byte[20][30][53];
        byte[][][][] objects = new byte[20][30][32][34];//32ͨ������
        Object[][] zdyObjects = new Object[20][30];//�Զ��嶯������
        Object[][] gxObjects = new Object[20][30];//������ѡ����
        byte[][][][] actAndRGB = new byte[20][30][4][24];//����������12��������

        for (int i = 0; i < table.getRowCount(); i++) {
            int dengKuId = Integer.parseInt(table.getValueAt(i, 0).toString());
            String dengKuName = (String) table.getValueAt(i, 1);
            int tt = Integer.valueOf((String) Data.DengKuChannelCountList.get(dengKuId - 1)).intValue();
            Map nameMap = (Map) Data.suCaiNameMap.get(dengKuName);
            JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
            int cnt = 0;
            if (null != nameMap)
                for (int k = 0; k < btns.length; k++) {
                    if (null != nameMap.get("" + k)) {
                        cnt = cnt + ((List) nameMap.get("" + k)).size();
                    }
                }
            for (int j = 0; j < cnt; j++) {
//                    int suCaiObjectId = Integer.parseInt(((String) nameList.get(j)).split(">")[1]);
                hashMap = (HashMap) Data.SuCaiObjects[dengKuId - 1][j];
//                byte[] bTp = new byte[(tt + 2) * 32];
                if (hashMap != null) {
                    //����ͼ��
                    Map map = (Map) hashMap.get("actionXiaoGuoData");
                    List list = null;
                    boolean bb = false;
                    int a = 0;
                    if (map != null) {
                        int selected = Integer.valueOf((String) map.get("2"));
                        if (selected == 1) {
                            selected = 255;
                        } else if (selected > 1) {
                            selected = selected - 1;
                        }
                        T1[i][j][0] = (byte) selected;
                        //����ͼ�� 12������
                        actAndRGB[i][j][0] = actAndRGB(0, selected);
                        //�����ٶ�
                        int yunXinSpeed = Integer.valueOf((String) map.get("3"));
                        T1[i][j][1] = (byte) yunXinSpeed;
                        //ʹ�ÿ��� 1����/0��
                        String ss = (String) map.get("0");
                        if (ss != null && "true".equals(ss)) {
                            T1[i][j][2] = (byte) 1;
                        }
                        //���    �����01/�м���02/���˲��03
                        String[] tp1 = (String[]) map.get("4");
                        int cc = Integer.valueOf(tp1[0]) + 1;
                        T1[i][j][3] = (byte) cc;

                        //��ַ���

                        //X�ᷴ��    ��1/��0
                        if ("true".equals(tp1[1])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][5] = (byte) a;
                        //X��
                        if ("true".equals(tp1[2])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][6] = (byte) a;
                        //Y�ᷴ��
                        if ("true".equals(tp1[3])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][7] = (byte) a;
                        //Y��
                        if ("true".equals(tp1[4])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][8] = (byte) a;


                        //ʱ��A_L	ʱ��B_H
                        a = Integer.valueOf(tp1[5]).intValue() * 10;
                        T1[i][j][9] = (byte) (a % 256);
                        T1[i][j][10] = (byte) (a / 256);

                        ///////////////�Զ��嶯������
                        String[] values = (String[]) map.get("1");
                        byte[] bt1 = new byte[5];
                        if (values != null) {
                            if (values[0].equals("true")) {
                                bt1[0] = 1;
                            }
                            for (int l = 1; l < bt1.length; l++) {
                                bt1[l] = (byte) Integer.valueOf(values[l]).intValue();
                            }
                        }
                        zdyObjects[i][j] = bt1;
                        /////////////////////////
                    }

                    //RGB1
                    list = (List) hashMap.get("rgb1Data");
                    String[] tp2 = null;
                    boolean[] bs = null;
                    String b = "";
                    if (list != null) {
                        //��ɫ
                        tp2 = (String[]) list.get(1);
                        a = Integer.valueOf(tp2[0]).intValue();
                        T1[i][j][11] = (byte) a;
                        //��ɫ
                        a = Integer.valueOf(tp2[1]).intValue();
                        T1[i][j][12] = (byte) a;
                        //��ɫ
                        a = Integer.valueOf(tp2[2]).intValue();
                        T1[i][j][13] = (byte) a;
                        //��������
                        b = (String) list.get(4);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][14] = (byte) a;
                        //RGB1 12������
                        actAndRGB[i][j][1] = actAndRGB(1, a);
                        //����
                        bb = (boolean) list.get(5);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][15] = (byte) a;
                        //���뽥�乴ѡ
                        bs = (boolean[]) list.get(2);
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
                        T1[i][j][16] = (byte) a;
                        //�����ٶ�
                        b = (String) list.get(6);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][17] = (byte) a;
                        //ʹ�ÿ���
                        bb = (boolean) list.get(0);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][18] = (byte) a;
                        //���
                        b = (String) list.get(7);
                        a = Integer.valueOf(b).intValue() + 1;
                        T1[i][j][19] = (byte) a;

                        //��ַ���
                        bb = (boolean) list.get(8);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][20] = (byte) a;
                        //ʱ��A_L	ʱ��B_H
                        a = Integer.valueOf((String) list.get(9)).intValue() * 10;
                        T1[i][j][21] = (byte) (a % 256);
                        T1[i][j][22] = (byte) (a / 256);
                    }

                    //RGB2
                    list = (List) hashMap.get("rgb2Data");
                    if (list != null) {
                        //��ɫ
                        tp2 = (String[]) list.get(1);
                        a = Integer.valueOf(tp2[0]).intValue();
                        T1[i][j][23] = (byte) a;
                        //��ɫ
                        a = Integer.valueOf(tp2[1]).intValue();
                        T1[i][j][24] = (byte) a;
                        //��ɫ
                        a = Integer.valueOf(tp2[2]).intValue();
                        T1[i][j][25] = (byte) a;
                        //��������
                        b = (String) list.get(4);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][26] = (byte) a;
                        //RGB1 12������
                        actAndRGB[i][j][2] = actAndRGB(1, a);
                        //����
                        bb = (boolean) list.get(5);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][27] = (byte) a;
                        //���뽥�乴ѡ
                        bs = (boolean[]) list.get(2);
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
                        T1[i][j][28] = (byte) a;
                        //�����ٶ�
                        b = (String) list.get(6);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][29] = (byte) a;
                        //ʹ�ÿ���
                        bb = (boolean) list.get(0);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][30] = (byte) a;
                        //���
                        b = (String) list.get(7);
                        a = Integer.valueOf(b).intValue() + 1;
                        T1[i][j][31] = (byte) a;

                        //��ַ���
                        bb = (boolean) list.get(8);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][32] = (byte) a;
                        //ʱ��A_L	ʱ��B_H
                        a = Integer.valueOf((String) list.get(9)).intValue() * 10;
                        T1[i][j][33] = (byte) (a % 256);
                        T1[i][j][34] = (byte) (a / 256);
                    }

                    //RGB3
                    list = (List) hashMap.get("rgb3Data");
                    if (list != null) {
                        //��ɫ
                        tp2 = (String[]) list.get(1);
                        a = Integer.valueOf(tp2[0]).intValue();
                        T1[i][j][35] = (byte) a;
                        //��ɫ
                        a = Integer.valueOf(tp2[1]).intValue();
                        T1[i][j][36] = (byte) a;
                        //��ɫ
                        a = Integer.valueOf(tp2[2]).intValue();
                        T1[i][j][37] = (byte) a;
                        //��������
                        b = (String) list.get(4);
                        a = Integer.valueOf(b).intValue();
                        //RGB3 12������
                        actAndRGB[i][j][1] = actAndRGB(1, a);
                        T1[i][j][38] = (byte) a;
                        //����
                        bb = (boolean) list.get(5);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][39] = (byte) a;
                        //���뽥�乴ѡ
                        bs = (boolean[]) list.get(2);
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
                        T1[i][j][40] = (byte) a;
                        //�����ٶ�
                        b = (String) list.get(6);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][41] = (byte) a;
                        //ʹ�ÿ���
                        bb = (boolean) list.get(0);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][42] = (byte) a;
                        //���
                        b = (String) list.get(7);
                        a = Integer.valueOf(b).intValue() + 1;
                        T1[i][j][43] = (byte) a;

                        //��ַ���
                        bb = (boolean) list.get(8);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][44] = (byte) a;
                        //ʱ��A_L	ʱ��B_H
                        a = Integer.valueOf((String) list.get(9)).intValue() * 10;
                        T1[i][j][45] = (byte) (a % 256);
                        T1[i][j][46] = (byte) (a / 256);
                    }

                    /////�ֶ��������
                    List list66 = (List) hashMap.get("channelData");
                    Vector vector88 = null;
                    if (list66 != null) {
                        //////////��ѡ
                        int r = 0, yu = 0;
                        int[] bp1 = new int[4];
                        boolean[] bn = (boolean[]) list66.get(1);
                        for (int l = 0; l < bn.length; l++) {
                            r = l / 8;
                            yu = l % 8;
                            if (bn[l]) {
                                bp1[r] = bp1[r] + (1 << yu);
                            }
                        }
                        for (int l = bn.length; l < 32; l++) {
                            r = l / 8;
                            yu = l % 8;
                            bp1[r] = bp1[r] + (1 << yu);
                        }
                        byte[] bp2 = new byte[4];
                        for (int l = 0; l < bp1.length; l++) {
                            bp2[l] = (byte) bp1[l];
                        }
                        gxObjects[i][j] = bp2;
                        //////////////

                        String[] ddTemp = (String[]) list66.get(2);
                        a = 0;
                        vector88 = (Vector) list66.get(0);
                        if (vector88 != null) {
                            a = vector88.size();
                        }
                        //��֡��
                        T1[i][j][47] = (byte) a;
                        //�ֶ��������
                        T1[i][j][48] = (byte) 1;

                        //ʱ��A_L	ʱ��B_H
                        a = Integer.valueOf(ddTemp[2]).intValue();
                        T1[i][j][49] = (byte) (a % 256);
                        T1[i][j][50] = (byte) (a / 256);
                        //���
                        a = Integer.valueOf(ddTemp[0]).intValue() + 1;
                        T1[i][j][51] = (byte) a;
                        //��ַ���
                        a = 0;
                        if (!ddTemp[1].equals("0")) {
                            a = 1;
                        }
                        T1[i][j][52] = (byte) a;

                        Vector tpe = null;
                        int lenght = tt + 2;
                        if (vector88 != null) {
                            for (int n = 0; n < vector88.size(); n++) {
                                tpe = (Vector) vector88.get(n);
                                int timeTp = Integer.valueOf(tpe.get(1).toString()).intValue();
//                                bTp[(tt + 2) * n] = (byte) (timeTp % 256);
//                                bTp[(tt + 2) * n + 1] = (byte) (timeTp / 256);
                                if (lenght > tpe.size()) {
                                    lenght = tpe.size();
                                }
                                objects[i][j][n][0] = (byte) (timeTp % 256);
                                objects[i][j][n][1] = (byte) (timeTp / 256);
                                for (int l = 2; l < lenght; l++) {
//                                    bTp[(tt + 2) * n + l] = Integer.valueOf(tpe.get(l).toString()).byteValue();
                                    objects[i][j][n][l] = Integer.valueOf(tpe.get(l).toString()).byteValue();
                                }
                            }
                        }

                    }

                }
//                objects[i][j] = bTp;
            }
        }

        //����д��  ����ģ�顢RGBģ�顢�ֶ����á�32ͨ�����ݡ��Զ��嶯�����ݡ���ѡ���ݡ�����������
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 30; j++) {
                os.write(T1[i][j]);
                for (int k = 0; k < 32; k++) {//32����ͨ��
                    os.write(objects[i][j][k]);
                }
                if (null == zdyObjects[i][j]) {//�Զ��嶯������
                    os.write(new byte[5]);
                }else {
                    os.write((byte[]) zdyObjects[i][j]);
                }
                if (null == gxObjects[i][j]) {//��ѡ����
                    os.write(new byte[4]);
                }else {
                    os.write((byte[]) gxObjects[i][j]);
                }
                for (int k = 0; k < 4; k++) {//����������
                    os.write(new byte[1]);
                    os.write(actAndRGB[i][j][k]);
                    os.write(new byte[1]);
                    os.write(new byte[16]);
                }
            }
        }
    }

    /*
     * ����ģ��
     * sc ������
     */
    private void writeFile(OutputStream os, int sc) throws Exception {
        byte[] t1 = new byte[2560];
        setT1(t1, sc);//��������
        os.write(t1);

        byte[][][] t2 = timeBlockData2(sc);
        for (int i = 0; i < 30; i++) {
            for (int j = 0; j < 20; j++) {
                os.write(t2[i][j]);
            }
        }
//		Object[][] objects = new Object[30][20];
//		Object[][] zdyObjects = new Object[30][20];
//		Object[][] gxObjects = new Object[30][20];
//		byte[][][] t2 = timeBlockData(sc,objects,zdyObjects,gxObjects);
//		byte[] t3 = null;
//		for (int i = 0; i < 30; i++) {
//			for (int j = 0; j < 20; j++) {
//				os.write(t2[i][j]);
//				if (objects[i][j]!=null) {
//					t3 = (byte[])objects[i][j];
//				} else {
//					t3 = new byte[64];
//				}
//				//System.out.println("�飺"+i+"�飺"+j+"###"+t2[i][j].length+"//"+t3.length);
//				os.write(t3);
//			}
//		}
//		byte[] b1 = null;
//		for (int i = 0; i < 30; i++) {
//			for (int j = 0; j < 20; j++) {
//				b1 = (byte[])zdyObjects[i][j];
//				if (b1 == null) {
//					b1 = new byte[5];
//				}
//				os.write(b1);
//			}
//		}
//		byte[] b2 = null;
//		for (int i = 0; i < 30; i++) {
//			for (int j = 0; j < 20; j++) {
//				b2 = (byte[])gxObjects[i][j];
//				if (b2 == null) {
//					b2 = new byte[4];
//					for (int j2 = 0; j2 < b2.length; j2++) {
//						b2[j2] = (byte)0xFF;
//					}
//				}
//				os.write(b2);
//			}
//		}
    }

    /* ��������
     * sc������
     */
    private void setT1(byte[] temp, int sc) {
        temp[0] = 0x55;
        temp[1] = (byte) 0xAA;
        short[][] a = new short[20][4];//��������

        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + sc);
        int j = 0, yu = 0;
        int maxTime = 0, tp6 = 0;
        for (int i = 0; i < 20; i++) {
            for (int k = 1; k < 31; k++) {
                j = (k - 1) / 8;
                yu = 7 - ((k - 1) % 8);
                if (timeBlockPanels[k].isVisible()) {
                    if (timeBlockPanels[k].getComponentCount() > i) {
                        DefineJLable lable = (DefineJLable) timeBlockPanels[k].getComponent(i);
                        if (lable.getText().contains("��")) {
                            tp6 = (lable.getX() + lable.getWidth()) / 5;
                            if (tp6 > maxTime) {
                                maxTime = tp6;
                            }
                            a[i][j] = (short) (a[i][j] + (1 << yu));
                        }
                    }
                }
            }
            for (int k = 0; k < 4; k++) {
                temp[20 + (i * 4) + k] = (byte) a[i][k];
            }
        }
        temp[3] = (byte) (maxTime % 256);
        temp[4] = (byte) (maxTime / 256);
        for (int k = 1; k < 31; k++) {
            temp[100 + (k - 1) * 81] = (byte) timeBlockPanels[k].getComponentCount();
            for (int n = 0; n < timeBlockPanels[k].getComponentCount(); n++) {
                DefineJLable lable = (DefineJLable) timeBlockPanels[k].getComponent(n);
                int start = lable.getX() / 5;
                int end = (lable.getX() + lable.getWidth()) / 5;
                temp[100 + (k - 1) * 81 + 1 + n * 4] = (byte) (start % 256);
                temp[100 + (k - 1) * 81 + 2 + n * 4] = (byte) (start / 256);

                temp[100 + (k - 1) * 81 + 3 + n * 4] = (byte) (end % 256);
                temp[100 + (k - 1) * 81 + 4 + n * 4] = (byte) (end / 256);
            }
        }

    }

    /**
     * ʱ�������
     *
     * @param sc ������
     * @return
     */
    private byte[][][] timeBlockData2(int sc) {
        byte[][][] t2 = new byte[30][20][4];
        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + sc);//ʱ����
        NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//���еƾ�
        for (int i = 0; i < 30; i++) {
            //��õƿ�id
            int number = Integer.valueOf(timeBlockPanels[i + 1].getName()).intValue();
            int tt = 0, a = 0;
            if (number - 1 < Data.GroupOfLightList.size()) {
                TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(number - 1);
                if (!treeSet.isEmpty()) {
                    a = (int) treeSet.first();
                    String typeString = table3.getValueAt(a, 3).toString();
                    tt = Integer.valueOf(typeString.split("#")[0].substring(2)).intValue();
//                    tt = Integer.valueOf((String) Data.DengKuChannelCountList.get(dengKuNumber)).intValue();
                }
            }
            for (int j = 0; j < 20; j++) {
                if (timeBlockPanels[i + 1].isVisible()) {
                    if (timeBlockPanels[i + 1].getComponentCount() > j) {
                        DefineJLable lable = (DefineJLable) timeBlockPanels[i + 1].getComponent(j);
                        String s = lable.getText().substring(lable.getText().indexOf("(") + 1, lable.getText().indexOf(")"));
                        int integer = Integer.parseInt(s);
                        t2[i][j][0] = (byte) tt;
                        t2[i][j][1] = (byte) integer;
                    }
                }

            }
        }
        return t2;
    }

    /* ʱ�������
     * sc������
     * objects �༭����
     * zdyObjects  �Զ��嶯������
     * gxObjects ������ѡ����
     */
    private byte[][][] timeBlockData(int sc, Object[][] objects, Object[][] zdyObjects, Object[][] gxObjects) {
        HashMap hashMap = null;
        byte[][][] T1 = new byte[30][20][53];
        //Object[][] objects = new Object[30][20];
        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + sc);
        NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//���еƾ�

        for (int i = 0; i < 30; i++) {
            int number = Integer.valueOf(timeBlockPanels[i + 1].getName()).intValue();
            int tt = 0, a = 0;
            if (number - 1 < Data.GroupOfLightList.size()) {
                TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(number - 1);
                if (!treeSet.isEmpty()) {
                    a = (int) treeSet.first();
                    String typeString = table3.getValueAt(a, 3).toString();
                    int dengKuNumber = Integer.valueOf(typeString.split("#")[0].substring(2)).intValue() - 1;
                    tt = Integer.valueOf((String) Data.DengKuChannelCountList.get(dengKuNumber)).intValue();
                }
            }

            //byte[] bTp = new byte[(tt+2)*32];
            for (int j = 0; j < 20; j++) {
                hashMap = (HashMap) Data.XiaoGuoDengObjects[sc - 1][i][j];
                byte[] bTp = new byte[(tt + 2) * 32];
                if (hashMap != null) {
                    //����ͼ��
                    Map map = (Map) hashMap.get("actionXiaoGuoData");
                    List list = null;
                    boolean bb = false;
                    a = 0;
                    if (map != null) {
                        int selected = Integer.valueOf((String) map.get("2"));
                        if (selected == 1) {
                            selected = 255;
                        } else if (selected > 1) {
                            selected = selected - 1;
                        }
                        T1[i][j][0] = (byte) selected;
                        //�����ٶ�
                        int yunXinSpeed = Integer.valueOf((String) map.get("3"));
                        T1[i][j][1] = (byte) yunXinSpeed;
                        //ʹ�ÿ��� 1����/0��
                        String ss = (String) map.get("0");
                        if (ss != null && "true".equals(ss)) {
                            T1[i][j][2] = (byte) 1;
                        }
                        //list = (List)hashMap.get("rgb3Data");
						/*if (list!=null) {
							bb = (boolean)list.get(0);
							if (bb) {
								T1[i][j][2] = (byte)1;
							}
						}*/
                        //���    �����01/�м���02/���˲��03
                        String[] tp1 = (String[]) map.get("4");
                        int cc = Integer.valueOf(tp1[0]) + 1;
                        T1[i][j][3] = (byte) cc;

                        //��ַ���

                        //X�ᷴ��    ��1/��0
                        if ("true".equals(tp1[1])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][5] = (byte) a;
                        //X��
                        if ("true".equals(tp1[2])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][6] = (byte) a;
                        //Y�ᷴ��
                        if ("true".equals(tp1[3])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][7] = (byte) a;
                        //Y��
                        if ("true".equals(tp1[4])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][8] = (byte) a;


                        //ʱ��A_L	ʱ��B_H
                        a = Integer.valueOf(tp1[5]).intValue() * 10;
                        T1[i][j][9] = (byte) (a % 256);
                        T1[i][j][10] = (byte) (a / 256);

                        ///////////////�Զ��嶯������
                        String[] values = (String[]) map.get("1");
                        byte[] bt1 = new byte[5];
                        if (values != null) {
                            if (values[0].equals("true")) {
                                bt1[0] = 1;
                            }
                            for (int k = 1; k < bt1.length; k++) {
                                bt1[k] = (byte) Integer.valueOf(values[k]).intValue();
                            }
                        }
                        zdyObjects[i][j] = bt1;
                        /////////////////////////
                    } else {

                    }

                    //RGB1
                    list = (List) hashMap.get("rgb1Data");
                    String[] tp2 = null;
                    boolean[] bs = null;
                    String b = "";
                    if (list != null) {
                        //��ɫ
                        tp2 = (String[]) list.get(1);
                        a = Integer.valueOf(tp2[0]).intValue();
                        T1[i][j][11] = (byte) a;
                        //��ɫ
                        a = Integer.valueOf(tp2[1]).intValue();
                        T1[i][j][12] = (byte) a;
                        //��ɫ
                        a = Integer.valueOf(tp2[2]).intValue();
                        T1[i][j][13] = (byte) a;
                        //��������
                        b = (String) list.get(4);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][14] = (byte) a;
                        //����
                        bb = (boolean) list.get(5);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][15] = (byte) a;
                        //���뽥�乴ѡ
                        bs = (boolean[]) list.get(2);
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
                        T1[i][j][16] = (byte) a;
                        //�����ٶ�
                        b = (String) list.get(6);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][17] = (byte) a;
                        //ʹ�ÿ���
                        bb = (boolean) list.get(0);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][18] = (byte) a;
                        //���
                        b = (String) list.get(7);
                        a = Integer.valueOf(b).intValue() + 1;
                        T1[i][j][19] = (byte) a;

                        //��ַ���
                        bb = (boolean) list.get(8);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][20] = (byte) a;
                        //ʱ��A_L	ʱ��B_H
                        a = Integer.valueOf((String) list.get(9)).intValue() * 10;
                        T1[i][j][21] = (byte) (a % 256);
                        T1[i][j][22] = (byte) (a / 256);
                    }

                    //RGB2
                    list = (List) hashMap.get("rgb2Data");
                    if (list != null) {
                        //��ɫ
                        tp2 = (String[]) list.get(1);
                        a = Integer.valueOf(tp2[0]).intValue();
                        T1[i][j][23] = (byte) a;
                        //��ɫ
                        a = Integer.valueOf(tp2[1]).intValue();
                        T1[i][j][24] = (byte) a;
                        //��ɫ
                        a = Integer.valueOf(tp2[2]).intValue();
                        T1[i][j][25] = (byte) a;
                        //��������
                        b = (String) list.get(4);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][26] = (byte) a;
                        //����
                        bb = (boolean) list.get(5);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][27] = (byte) a;
                        //���뽥�乴ѡ
                        bs = (boolean[]) list.get(2);
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
                        T1[i][j][28] = (byte) a;
                        //�����ٶ�
                        b = (String) list.get(6);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][29] = (byte) a;
                        //ʹ�ÿ���
                        bb = (boolean) list.get(0);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][30] = (byte) a;
                        //���
                        b = (String) list.get(7);
                        a = Integer.valueOf(b).intValue() + 1;
                        T1[i][j][31] = (byte) a;

                        //��ַ���
                        bb = (boolean) list.get(8);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][32] = (byte) a;
                        //ʱ��A_L	ʱ��B_H
                        a = Integer.valueOf((String) list.get(9)).intValue() * 10;
                        T1[i][j][33] = (byte) (a % 256);
                        T1[i][j][34] = (byte) (a / 256);
                    }

                    //RGB3
                    list = (List) hashMap.get("rgb3Data");
                    if (list != null) {
                        //��ɫ
                        tp2 = (String[]) list.get(1);
                        a = Integer.valueOf(tp2[0]).intValue();
                        T1[i][j][35] = (byte) a;
                        //��ɫ
                        a = Integer.valueOf(tp2[1]).intValue();
                        T1[i][j][36] = (byte) a;
                        //��ɫ
                        a = Integer.valueOf(tp2[2]).intValue();
                        T1[i][j][37] = (byte) a;
                        //��������
                        b = (String) list.get(4);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][38] = (byte) a;
                        //����
                        bb = (boolean) list.get(5);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][39] = (byte) a;
                        //���뽥�乴ѡ
                        bs = (boolean[]) list.get(2);
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
                        T1[i][j][40] = (byte) a;
                        //�����ٶ�
                        b = (String) list.get(6);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][41] = (byte) a;
                        //ʹ�ÿ���
                        bb = (boolean) list.get(0);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][42] = (byte) a;
                        //���
                        b = (String) list.get(7);
                        a = Integer.valueOf(b).intValue() + 1;
                        T1[i][j][43] = (byte) a;

                        //��ַ���
                        bb = (boolean) list.get(8);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][44] = (byte) a;
                        //ʱ��A_L	ʱ��B_H
                        a = Integer.valueOf((String) list.get(9)).intValue() * 10;
                        T1[i][j][45] = (byte) (a % 256);
                        T1[i][j][46] = (byte) (a / 256);
                    }


                    /////�ֶ��������
                    List list66 = (List) hashMap.get("channelData");
                    Vector vector88 = null;
                    if (list66 != null) {
                        //////////��ѡ
                        int r = 0, yu = 0;
                        int[] bp1 = new int[4];
                        boolean[] bn = (boolean[]) list66.get(1);
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
                            bp2[k] = (byte) bp1[k];
                        }
                        gxObjects[i][j] = bp2;
                        //////////////

                        String[] ddTemp = (String[]) list66.get(2);
                        a = 0;
                        vector88 = (Vector) list66.get(0);
                        if (vector88 != null) {
                            a = vector88.size();
                        }
                        //��֡��
                        T1[i][j][47] = (byte) a;
                        //�ֶ��������
                        T1[i][j][48] = (byte) 1;

                        //ʱ��A_L	ʱ��B_H
                        a = Integer.valueOf(ddTemp[2]).intValue();
                        T1[i][j][49] = (byte) (a % 256);
                        T1[i][j][50] = (byte) (a / 256);
                        //���
                        a = Integer.valueOf(ddTemp[0]).intValue() + 1;
                        T1[i][j][51] = (byte) a;
                        //��ַ���
                        a = 0;
                        if (!ddTemp[1].equals("0")) {
                            a = 1;
                        }
                        T1[i][j][52] = (byte) a;

                        Vector tpe = null;
                        int lenght = tt + 2;
                        if (vector88 != null) {
                            for (int n = 0; n < vector88.size(); n++) {
                                tpe = (Vector) vector88.get(n);
                                int timeTp = Integer.valueOf(tpe.get(1).toString()).intValue();
                                bTp[(tt + 2) * n] = (byte) (timeTp % 256);
                                bTp[(tt + 2) * n + 1] = (byte) (timeTp / 256);
                                if (lenght > tpe.size()) {
                                    lenght = tpe.size();
                                }
                                for (int k = 2; k < lenght; k++) {
                                    bTp[(tt + 2) * n + k] = Integer.valueOf(tpe.get(k).toString()).byteValue();
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
     * ����ģ�� --���ģʽ����
     */
    private void writeWuJiModelData(OutputStream os) throws Exception {
        List allList = null;
        Vector tp = null;
        int a = 0;
        String[] tkp2 = (String[]) Data.wuJiMap.get("QJ_set");
        int x = 0, y = 0;
        if (tkp2 != null) {
            x = Integer.valueOf(tkp2[0]);
            y = Integer.valueOf(tkp2[1]);
        }
        byte[][][] b1 = new byte[4][5][13];//����
        byte[][][] b2 = new byte[4][5][13];//����
        byte[][] b3 = new byte[4][7];//����
        for (int i = 1; i < 5; i++) {
            allList = (List) Data.wuJiMap.get("" + i);
            if (allList != null) {
                Vector vector88 = (Vector) allList.get(0);//����
                Vector vector99 = (Vector) allList.get(1);//����
                String[] tkp = (String[]) allList.get(2);
                if (tkp[0].equals("true")) {//���״̬
                    b3[i - 1][0] = 1;
                }
                //��ַ
                b3[i - 1][1] = (byte) (x / 256);
                b3[i - 1][2] = (byte) (x % 256);
                //ͨ����
                b3[i - 1][3] = (byte) y;

                if (tkp[2].equals("true")) {//����ģʽ
                    b3[i - 1][4] = 1;
                } else {
                    b3[i - 1][4] = 2;
                }
                if (vector88 != null) {
                    for (int n = 0; n < vector88.size(); n++) {
                        b1[i - 1][n][0] = (byte) vector88.size();
                        tp = (Vector) vector88.get(n);
                        a = Integer.valueOf((String) (tp.get(1)));
                        b1[i - 1][n][1] = (byte) (a % 256);
                        b1[i - 1][n][2] = (byte) (a / 256);
                        for (int j = 2; j < 12; j++) {
                            a = Integer.valueOf((String) (tp.get(j)));
                            b1[i - 1][n][1 + j] = (byte) a;
                        }
                    }
                }
                if (vector99 != null) {
                    for (int n = 0; n < vector99.size(); n++) {
                        b2[i - 1][n][0] = (byte) vector99.size();
                        tp = (Vector) vector99.get(n);
                        a = Integer.valueOf((String) (tp.get(1)));
                        b2[i - 1][n][1] = (byte) (a % 256);
                        b2[i - 1][n][2] = (byte) (a / 256);
                        for (int j = 2; j < 12; j++) {
                            a = Integer.valueOf((String) (tp.get(j)));
                            b2[i - 1][n][1 + j] = (byte) a;
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
     * ����ģ��--ҡ���뵹�Ȳ�ģʽ����
     */
    private void writeHeCaiYaoMai(OutputStream os) throws Exception {
        ///ҡ��
        Vector[] vectors = null;
        boolean[][] tbs = null;
        String[] setValue = null;
        byte[][][] b1 = new byte[2][16][512];
        byte[][][] b2 = new byte[2][16][512];
        byte[][][] b3 = new byte[2][2][512];
        byte[][][] steps1 = new byte[2][2][1];//���á����� ����
        byte[][] set1 = new byte[2][7];//ȫ������
        int a = 0, size8 = 0;
        Vector vector99 = null, tp = null;
        for (int n = 1; n < 3; n++) {
            size8 = 0;
            vectors = (Vector[]) Data.YaoMaiMap.get("TableData" + n);
            tbs = (boolean[][]) Data.YaoMaiMap.get("GouXuanValue" + n);
            if (vectors != null) {
                vector99 = vectors[0];//�����׶�
                if (vector99 != null) {
                    size8 = vector99.size();
                    steps1[n - 1][0][0] = (byte) size8;
                    for (int i = 0; i < size8; i++) {
                        tp = (Vector) vector99.get(i);
                        a = Integer.valueOf((String) (tp.get(1)));
                        b1[n - 1][i][0] = (byte) (a % 256);
                        b1[n - 1][i][1] = (byte) (a / 256);
                        for (int j = 2; j < 512; j++) {
                            a = Integer.valueOf((String) (tp.get(j)));
                            b1[n - 1][i][j] = (byte) a;
                        }
                    }
                }

                vector99 = vectors[1];//���н׶�
                if (vector99 != null) {
                    size8 = vector99.size();
                    steps1[n - 1][1][0] = (byte) size8;
                    for (int i = 0; i < size8; i++) {
                        tp = (Vector) vector99.get(i);
                        a = Integer.valueOf((String) (tp.get(1)));
                        b2[n - 1][i][0] = (byte) (a % 256);
                        b2[n - 1][i][1] = (byte) (a / 256);
                        for (int j = 2; j < 512; j++) {
                            a = Integer.valueOf((String) (tp.get(j)));
                            b2[n - 1][i][j] = (byte) a;
                        }
                    }
                }
            }
            //��ѡ
            if (tbs != null) {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 510; j++) {
                        if (tbs[i][j]) {
                            b3[n - 1][i][j + 2] = 1;
                        }
                    }
                }
            }
            //ȫ������
            setValue = (String[]) Data.YaoMaiMap.get("YaoMaiSet" + n);
            set1[n - 1][0] = 1;//����
            if (setValue != null) {
                if (Boolean.valueOf(setValue[1])) {
                    set1[n - 1][1] = 1;
                } else {
                    set1[n - 1][1] = 2;
                }
                set1[n - 1][2] = (byte) Integer.valueOf(setValue[2]).intValue();
            }
        }
        ///////////////////////////
        ///////////���Ȳ�
        byte[][][] b1_2 = new byte[2][16][512];
        byte[][][] b2_2 = new byte[2][16][512];
        byte[][][] b3_2 = new byte[2][2][512];
        byte[][][] steps1_2 = new byte[2][2][1];//���á����� ����
        byte[][] set1_2 = new byte[2][7];//ȫ������
        String[] s = {"Ч���Ƶ���ģʽ", "Ч���ƺȲ�ģʽ"};
        for (int n = 1; n < 3; n++) {
            size8 = 0;
            vectors = (Vector[]) Data.DaoHeCaiMap.get("TableData" + s[n - 1]);
            tbs = (boolean[][]) Data.DaoHeCaiMap.get("GouXuanValue" + s[n - 1]);
            if (vectors != null) {
                vector99 = vectors[0];//�����׶�
                if (vector99 != null) {
                    size8 = vector99.size();
                    steps1_2[n - 1][0][0] = (byte) size8;
                    for (int i = 0; i < size8; i++) {
                        tp = (Vector) vector99.get(i);
                        a = Integer.valueOf((String) (tp.get(1)));
                        b1_2[n - 1][i][0] = (byte) (a % 256);
                        b1_2[n - 1][i][1] = (byte) (a / 256);
                        for (int j = 2; j < 512; j++) {
                            a = Integer.valueOf((String) (tp.get(j)));
                            b1_2[n - 1][i][j] = (byte) a;
                        }
                    }
                }

                vector99 = vectors[1];//���н׶�
                if (vector99 != null) {
                    size8 = vector99.size();
                    steps1_2[n - 1][1][0] = (byte) size8;
                    for (int i = 0; i < size8; i++) {
                        tp = (Vector) vector99.get(i);
                        a = Integer.valueOf((String) (tp.get(1)));
                        b2_2[n - 1][i][0] = (byte) (a % 256);
                        b2_2[n - 1][i][1] = (byte) (a / 256);
                        for (int j = 2; j < 512; j++) {
                            a = Integer.valueOf((String) (tp.get(j)));
                            b2_2[n - 1][i][j] = (byte) a;
                        }
                    }
                }
                //��ѡ
                if (tbs != null) {
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 510; j++) {
                            if (tbs[i][j]) {
                                b3_2[n - 1][i][j + 2] = 1;
                            }
                        }
                    }
                }
                //ȫ������
                setValue = (String[]) Data.DaoHeCaiMap.get("DaoHeCaiSet" + s[n - 1]);
                set1_2[n - 1][0] = 1;//����
                if (setValue != null) {
                    if (Boolean.valueOf(setValue[1])) {
                        set1_2[n - 1][1] = 1;
                    } else {
                        set1_2[n - 1][1] = 2;
                    }
                    set1_2[n - 1][2] = (byte) Integer.valueOf(setValue[2]).intValue();
                }
            }
        }
        ////////////////////////////
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 16; j++) {
                os.write(b1[i][j]);//ҡ������
            }
            for (int j = 0; j < 16; j++) {
                os.write(b2[i][j]);//ҡ������
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 16; j++) {
                os.write(b1_2[i][j]);//���Ȳ�����
            }
            for (int j = 0; j < 16; j++) {
                os.write(b2_2[i][j]);//���Ȳ�����
            }
        }
        //��ѡ
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
        //�ܲ���
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
        //ȫ������
        for (int i = 0; i < 2; i++) {
            os.write(set1[i]);
        }
        for (int i = 0; i < 2; i++) {
            os.write(set1_2[i]);
        }
    }

    private void writeShengKon(OutputStream os, int sc) throws Exception {

        ShengKonData(sc, os);

        //shengKonMoreLigthData(os);
    }

    /*
     * ����ʱ�������
     */
    private void ShengKonData(int scN, OutputStream os) throws Exception {
        byte[] b1 = new byte[1374];
        b1[0] = 0x55;
        b1[1] = (byte) 0xAA;
        b1[2] = 0x00;
        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels" + scN);
        int j = 0, yu = 0;
        short[][] a = new short[10][4];//��������
        for (int i = 0; i < 10; i++) {//��
            for (int k = 1; k < 31; k++) {//���
                j = (k - 1) / 8;
                yu = 7 - ((k - 1) % 8);
                if (timeBlockPanels[k].isVisible()) {
                    if (timeBlockPanels[k].getComponentCount() > i) {
                        DefineJLable_shengKon2 label = (DefineJLable_shengKon2) timeBlockPanels[k].getComponent(i);
                        if (label.getBackground().getGreen() == 255) {
                            a[i][j] = (short) (a[i][j] + (1 << yu));
                        }
                    }
                }
            }
            for (int k = 0; k < 4; k++) {
                b1[3 + i * 4 + k] = (byte) a[i][k];
            }
        }
        //ȫ������
        List list = (List) Data.ShengKonModelSet.get("" + scN);
        if (list != null) {
            int time = Integer.valueOf((String) (list.get(0))).intValue();
            b1[83] = (byte) (time % 256);
            b1[84] = (byte) (time / 256);
            time = Integer.valueOf((String) (list.get(2))).intValue();
            b1[85] = (byte) time;
            time = Integer.valueOf((String) (list.get(1))).intValue();
            b1[88] = (byte) (time % 256);
            b1[89] = (byte) (time / 256);
        }

        //��Ƶ������  [2][67 + 32];
        int[][] DataSetInts = null;
        for (int i = 0; i < 10; i++) {
            DataSetInts = (int[][]) Data.ShengKonShiXuSetObjects[scN - 1][i];
            if (DataSetInts != null) {
                ////////////��Ƶ������
                if (DataSetInts[0][64] == 1) {
                    b1[90 + 7 * i] = (byte) 128;
                }
                if (DataSetInts[0][2] == 0) {
                    b1[90 + 7 * i] = (byte) (16 + Byte.toUnsignedInt(b1[90 + 7 * i]));
                } else if (DataSetInts[0][2] == 1) {
                    b1[90 + 7 * i] = (byte) (32 + DataSetInts[0][3] + 1 + Byte.toUnsignedInt(b1[90 + 7 * i]));
                } else if (DataSetInts[0][2] == 2) {
                    b1[90 + 7 * i] = (byte) (64 + DataSetInts[0][3] + 1 + Byte.toUnsignedInt(b1[90 + 7 * i]));
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
                    b1[91 + 7 * i + k] = (byte) tp1[k];
                }
                b1[95 + 7 * i] = (byte) (DataSetInts[0][65] + 1);
                b1[96 + 7 * i] = (byte) (DataSetInts[0][66] + 1);
                //////////////////
                ///////��Ƶ���ģʽ
                if (DataSetInts[0][0] == 0) {
                    b1[204 + i] = (byte) (16);
                } else if (DataSetInts[0][0] == 1) {
                    b1[204 + i] = (byte) (32 + DataSetInts[0][1] + 1);
                }
                //////��Ƶ����˳������
                NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
                int sl = 0;
                for (int n = 0; n < table.getRowCount(); n++) {
                    boolean b = (boolean) table.getValueAt(n, 0);
                    if (b) {
                        sl++;
                    }
                }
                for (int k = 0; k < 10; k++) {
                    b1[254 + k * 6 + 60 * i] = (byte) DataSetInts[0][4 + k * 6];
                    for (int k2 = 0; k2 < 5; k2++) {
                        if (sl + 2 == DataSetInts[0][5 + k2 + k * 6]) {
                            b1[255 + 60 * i + k * 6 + k2] = (byte) 31;
                        } else if (sl + 3 == DataSetInts[0][5 + k2 + k * 6]) {
                            b1[255 + 60 * i + k * 6 + k2] = (byte) 32;
                        } else if (sl >= DataSetInts[0][5 + k2 + k * 6]) {
                            b1[255 + 60 * i + k * 6 + k2] = (byte) DataSetInts[0][5 + k2 + k * 6];
                        }
                    }
                }
                ///////////////////��Ƶ������
                if (DataSetInts[1][64] == 1) {
                    b1[864 + 7 * i] = (byte) 128;
                }
                if (DataSetInts[1][2] == 0) {
                    b1[864 + 7 * i] = (byte) (16 + Byte.toUnsignedInt(b1[864 + 7 * i]));
                } else if (DataSetInts[1][2] == 1) {
                    b1[864 + 7 * i] = (byte) (32 + Byte.toUnsignedInt(b1[864 + 7 * i]));
                } else if (DataSetInts[1][2] == 2) {
                    b1[864 + 7 * i] = (byte) (64 + Byte.toUnsignedInt(b1[864 + 7 * i]));
                }
                b1[864 + 7 * i] = (byte) (DataSetInts[1][3] + 1 + Byte.toUnsignedInt(b1[864 + 7 * i]));
                tp1 = new int[4];
                for (int k = 0; k < 32; k++) {
                    j = k / 8;
                    yu = 7 - (k % 8);
                    if (DataSetInts[1][k + 67] == 1) {
                        tp1[j] = tp1[j] + (1 << yu);
                    }
                }
                for (int k = 0; k < 4; k++) {
                    b1[865 + 7 * i + k] = (byte) tp1[k];
                }
                b1[869 + 7 * i] = (byte) (DataSetInts[1][65] + 1);
                b1[869 + 7 * i] = (byte) (DataSetInts[1][66] + 1);
                //////////////////
                ///////////��Ƶ���ģʽ
                if (DataSetInts[1][0] == 0) {
                    b1[854 + i] = (byte) (16);
                } else if (DataSetInts[1][0] == 1) {
                    b1[854 + i] = (byte) (32 + DataSetInts[1][1] + 1);
                }
                //////////////////
                ////////////////��Ƶ����˳������
                sl = 0;
                for (int n = 0; n < table.getRowCount(); n++) {
                    boolean b = (boolean) table.getValueAt(n, 0);
                    if (b) {
                        sl++;
                    }
                }
                for (int k = 0; k < 10; k++) {
                    b1[974 + k * 4 + 40 * i] = (byte) DataSetInts[1][4 + k * 6];
                    for (int k2 = 0; k2 < 3; k2++) {
                        if (sl + 1 == DataSetInts[1][5 + k2 + k * 6]) {
                            b1[975 + 40 * i + k * 4 + k2] = (byte) 31;
                        } else if (sl + 2 == DataSetInts[1][5 + k2 + k * 6]) {
                            b1[975 + 40 * i + k * 4 + k2] = (byte) 32;
                        } else if (sl >= DataSetInts[1][5 + k2 + k * 6]) {
                            b1[975 + 40 * i + k * 4 + k2] = (byte) DataSetInts[1][5 + k2 + k * 6];
                        }
                    }
                }
                /////////////////
            }
        }
        //ʱ��Ƭ����
        int timeCount = timeBlockPanels[0].getComponentCount();
        int timeWidth = 0;
        b1[160] = (byte) timeCount;
        b1[161] = 0;
        b1[162] = 0;
        b1[163] = 0;
        for (int i = 0; i < timeCount; i++) {
            DefineJLable_shengKon label = (DefineJLable_shengKon) timeBlockPanels[0].getComponent(i);
            timeWidth = label.getWidth() / 5;
            b1[164 + i * 2] = (byte) (timeWidth % 256);
            b1[165 + i * 2] = (byte) (timeWidth / 256);
        }
        os.write(b1);

        //������
        byte[][][] b2 = new byte[30][10][61];
        Map map = null;
        Vector vector88 = null;
        int size = 0;
        boolean[][] gouXuanValus = null;
        int j2, yu2;
        int[] al;
        for (int i = 0; i < 30; i++) {
            for (int k = 0; k < 10; k++) {
                map = (Map) Data.ShengKonEditObjects[scN - 1][i][k];
                if (map != null) {
                    vector88 = (Vector) map.get("0");
                    if (vector88 != null) {
                        size = vector88.size();
                        b2[i][k][0] = (byte) (size % 256);
                        b2[i][k][1] = (byte) (size / 256);
                    }
                    gouXuanValus = (boolean[][]) map.get("1");
                    if (gouXuanValus != null) {
                        for (int k2 = 0; k2 < gouXuanValus.length; k2++) {
                            for (int l = 0; l < gouXuanValus[0].length; l++) {
                                j2 = l / 8;
                                yu2 = 7 - (l % 8);
                                if (gouXuanValus[k2][l]) {
                                    b2[i][k][2 + k2 * 4 + j2] = (byte) (Byte.toUnsignedInt(b2[i][k][2 + k2 * 4 + j2]) + (1 << yu2));
                                }
                            }
                        }
                    }
                    al = (int[]) map.get("2");
                    if (al != null) {
                        if (al[0] == 0) {//0��ʾ����
                            b2[i][k][38] = 2;
                        } else {
                            b2[i][k][38] = 1;
                        }
                        //��ƿ���
                        b2[i][k][44] = (byte) al[10];
                        //���ٶ�
                        if (al[1] == 0) {
                            b2[i][k][46] = 1;
                        }
                        if (al[2] == 1) {
                            b2[i][k][48] = 1;
                        }
                        b2[i][k][49] = (byte) al[3];

                        b2[i][k][51] = (byte) al[4];
                        b2[i][k][52] = (byte) al[6];
                        b2[i][k][53] = (byte) al[8];

                        b2[i][k][54] = (byte) al[5];
                        b2[i][k][55] = (byte) al[7];
                        b2[i][k][56] = (byte) al[9];
                    }
                }

                //������
                int cnt = 0;//�ƾ�����
                int channelCount = 0;//ͨ����
                String typeString = "";
                //int number = Integer.valueOf(""+(i+1)).intValue();
                if (Data.GroupOfLightList.size() > i) {
                    TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(i);
                    cnt = treeSet.size();
                    if (cnt > 0) {
                        NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//���еƾ�
                        int v = (int) treeSet.first();
                        typeString = table3.getValueAt(v, 3).toString();//�ƾ��ͺ�
                    }
                }
                if (!typeString.equals("")) {
                    int dengKuNumber = Integer.valueOf(typeString.split("#")[0].substring(2)).intValue() - 1;
                    channelCount = Integer.valueOf((String) Data.DengKuChannelCountList.get(dengKuNumber)).intValue();
                }
                int stepWidth = channelCount * cnt;
                int runTime = 0, lgth = 0;
                byte[][] b3 = new byte[32][stepWidth + 2];
                if (vector88 != null) {
                    Vector tp = null;
                    for (int r = 0; r < vector88.size(); r++) {
                        tp = (Vector) vector88.get(r);
                        runTime = Integer.valueOf(tp.get(1).toString());
                        b3[r][0] = (byte) (runTime % 256);
                        b3[r][1] = (byte) (runTime / 256);
                        if (tp.size() - 2 >= stepWidth) {
                            lgth = stepWidth + 2;
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
     * ���ض��ģʽ����
     */
    private void shengKonMoreLigthData(OutputStream os) throws Exception {
        int size = 0, a = 0;
        byte[][][] steps = new byte[16][2][1];//�ܲ���
        Vector tp = null, vector99 = null;
        byte[][][][] b1 = new byte[16][2][32][512];//���༭����
        String[] sets = null;
        boolean[] tbs = null;
        byte[][][] channalGouXuan = new byte[16][2][64];//ͨ����ѡ
        byte[][][] globalSet = new byte[16][2][7];//ȫ������
        int r = 0, yu = 0;
        for (int scN = 1; scN < 17; scN++) {
            for (int setNo = 1; setNo < 3; setNo++) {
                vector99 = (Vector) Data.ShengKonModelDmxMap.get("TableData" + scN + "" + setNo);
                sets = (String[]) Data.ShengKonModelDmxMap.get("YaoMaiSet" + scN + "" + setNo);
                tbs = (boolean[]) Data.ShengKonModelDmxMap.get("GouXuanValue" + scN + "" + setNo);//��ѡ
                if (vector99 != null) {
                    size = vector99.size();
                    steps[scN - 1][setNo - 1][0] = (byte) size;
                    for (int i = 0; i < size; i++) {
                        tp = (Vector) vector99.get(i);
                        a = Integer.valueOf((String) (tp.get(1)));
                        b1[scN - 1][setNo - 1][i][0] = (byte) (a % 256);
                        b1[scN - 1][setNo - 1][i][1] = (byte) (a / 256);
                        for (int j = 2; j < 512; j++) {
                            a = Integer.valueOf((String) (tp.get(j)));
                            b1[scN - 1][setNo - 1][i][j] = (byte) a;
                        }
                    }
                }
                if (sets != null) {
                    globalSet[scN - 1][setNo - 1][2] = Integer.valueOf(sets[0]).byteValue();
                }
                if (tbs != null) {
                    for (int k = 0; k < tbs.length; k++) {
                        r = k / 8;
                        yu = 7 - (k % 8);
                        if (tbs[k]) {
                            channalGouXuan[scN - 1][setNo - 1][r] = (byte) (Byte.toUnsignedInt(channalGouXuan[scN - 1][setNo - 1][r]) + (1 << yu));
                        }
                    }
                }
            }
        }
        //���༭����
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 2; j++) {
                for (int j2 = 0; j2 < 32; j2++) {
                    os.write(b1[i][j][j2]);
                }
            }
        }
        //��ѡ����
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 2; j++) {
                os.write(channalGouXuan[i][j]);
            }
        }
        //�ܲ���
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 2; j++) {
                os.write(steps[i][j]);
            }
        }
        //ȫ������
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 2; j++) {
                os.write(globalSet[i][j]);
            }
        }
    }

    /*
     * ���ػ�����ģʽ����
     */
    private void shengKonEnvironmentData(OutputStream os) throws Exception {
        byte[][][] b1 = new byte[18][20][41];
        Map map;
        List list;
        int[] tp;
        int time = 0;
        String s = null;
        for (int i = 1; i < 19; i++) {
            list = null;
            s = null;
            map = (Map) Data.ShengKonHuanJingModelMap.get("" + i);
            if (map != null) {
                list = (List) map.get("0");
                s = (String) map.get("3");
            }
            for (int j = 0; j < 20; j++) {
                tp = null;
                if (list != null && list.size() > j) {
                    tp = (int[]) list.get(j);
                }
                if (tp != null) {
                    time = tp[0];
                    b1[i - 1][j][33] = (byte) (time / 256);
                    b1[i - 1][j][34] = (byte) (time % 256);
                    for (int k = 0; k < 8; k++) {
                        if (tp[k + 1] == 0) {
                            b1[i - 1][j][k] = (byte) tp[k + 9];
                        } else {
                            b1[i - 1][j][k] = (byte) 0xAC;
                        }
                    }
                }
                if (s != null) {
                    if ("0".equals(s)) {
                        b1[i - 1][j][32] = (byte) 1;
                    } else if ("1".equals(s)) {
                        b1[i - 1][j][32] = (byte) 2;
                    } else if ("2".equals(s)) {
                        b1[i - 1][j][32] = (byte) 0;
                    }
                }
                os.write(b1[i - 1][j]);
            }
        }
    }

    private byte[] actAndRGB(int type, int select) throws Exception {
        byte[] bytes = new byte[24];
        if (type == 0) {
            if (select == 0) {
                getTwelveCoordinates(1, bytes);
            } else if (select == 1) {
                getTwelveCoordinates(0, bytes);
            } else if (select > 1 && select < 48) {
                getTwelveCoordinates(select, bytes);
            } else if (select >= 48) {
                String[] s = (String[]) bezier.Data.map.get("" + select);
                if (s != null) {
                    getTwelveCoordinates(s, bytes);
                } else {
                    getTwelveCoordinates(0, bytes);
                }
            }
        } else {
            if (select <= 10) {
            } else if (select > 10 && select < 52) {
                getTwelveCoordinatesColor(select - 11, bytes);
            } else if (select > 51) {
                String[] s = (String[]) bezier.Data.map.get("color" + select);
                if (s != null) {
                    getTwelveCoordinates(s, bytes);
                } else {
                    getTwelveCoordinates(0, bytes);
                }
            }
        }
        return bytes;
    }

    private void getTwelveCoordinatesColor(int type, byte[] bytes) {
        for (int i = 0; i < 12; i++) {
            bytes[i] = (byte) Double.valueOf(bezier.Data.ZBcolor[type][i]).intValue();
        }
    }

    private void getTwelveCoordinates(int type, byte[] bytes) {
        for (int i = 0; i < 24; i++) {
            bytes[i] = (byte) Double.valueOf(bezier.Data.ZB[type][i]).intValue();
        }
    }

    private void getTwelveCoordinates(String[] ps, byte[] bytes) {
        for (int i = 0; i < 24; i++) {
            bytes[i] = (byte) Double.valueOf(ps[i]).intValue();
        }
    }

    /*
     * 12������
     */
    private void actionTuXing(OutputStream os) throws Exception {
        //��ɫͼ��
        byte[][] t1 = new byte[255][42];
        String[] s = null;
        String[] tps = (String[]) bezier.Data.itemMap.get("1");
        String itemName = "";
        for (int i = 61; i < t1.length; i++) {
            s = (String[]) bezier.Data.map.get("color" + (i));
            if (s != null) {
                t1[i - 1][0] = (byte) i;
                for (int j = 0; j < s.length; j++) {
                    t1[i - 1][j + 1] = (byte) Double.valueOf(s[j]).intValue();
                }
                if (tps != null) {
                    itemName = tps[i];
                    if (itemName.length() <= 16) {
                        t1[i - 1][25] = (byte) itemName.length();
                        for (int j = 0; j < itemName.length(); j++) {
                            t1[i - 1][26 + j] = itemName.getBytes()[j];
                        }
                    }
                }
            }
        }
        //����ͼ��
        byte[][] t2 = new byte[255][42];
        String[] s2 = null;
        String[] tps2 = (String[]) bezier.Data.itemMap.get("0");
        String itemName2 = "";
        for (int i = 51; i < t2.length; i++) {
            s2 = (String[]) bezier.Data.map.get("" + (i + 1));
            if (s2 != null) {
                t2[i - 1][0] = (byte) i;
                for (int j = 0; j < s2.length; j++) {
                    t2[i - 1][j + 1] = (byte) Double.valueOf(s2[j]).intValue();
                }
                if (tps2 != null) {
                    itemName2 = tps2[i + 1];
                    if (itemName2.length() <= 16) {
                        t2[i - 1][25] = (byte) itemName2.length();
                        for (int j = 0; j < itemName2.length(); j++) {
                            t2[i - 1][26 + j] = itemName2.getBytes()[j];
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
     * ϵͳ��������
     */
    private void systemSet(OutputStream os) throws Exception {
        new DataActionListener().actionPerformed(os);
    }
}
