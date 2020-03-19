package com.boray.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.boray.Data.Data;
import com.boray.Data.DengKuData;
import com.boray.Data.GetChannelNumber;
import com.boray.dengKu.Entity.BlackOutEntity;
import com.boray.dengKu.Entity.SpeedEntity;
import com.boray.dengKu.UI.AddCustomTonDaoDialog;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

public class DengKuDataListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        if ("导入".equals(e.getActionCommand())) {
            importData();
        } else if ("导出".equals(e.getActionCommand())) {
            exportData();
        } else if ("自定义通道".equals(e.getActionCommand())) {
            new AddCustomTonDaoDialog().show();//展示自定义通道界面
        }
    }

    private void importData() {
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        if (table.getRowCount() >= 20) {
            JFrame frame = (JFrame) MainUi.map.get("frame");
            JOptionPane.showMessageDialog(frame, "最多只能创建20个灯库！", "提示", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        try {
            String path = getClass().getResource("/lib/").getPath().substring(1);
            path = URLDecoder.decode(path, "utf-8");
            fileChooser.setCurrentDirectory(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!"".equals(Data.importPath)) {
            fileChooser.setCurrentDirectory(new File(Data.importPath));
        } /*else if (!"".equals(Data.exportPath)) {
			fileChooser.setCurrentDirectory(new File(Data.exportPath));
		}*/
        String[] houZhui = {"hxl"};
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.hxl", houZhui);
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog((JFrame) MainUi.map.get("frame"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                Data.importPath = file.getParent();
                InputStream is = new FileInputStream(file);
                int size = (int) file.length();
                byte[] temp = new byte[size];
                is.read(temp);
                is.close();
                if (size % 128 == 0 && Byte.toUnsignedInt(temp[0]) == 253 && Byte.toUnsignedInt(temp[1]) == 80) {
                    handle(temp, 128);
                } else if (size % 80 == 0 && Byte.toUnsignedInt(temp[0]) == 253 && Byte.toUnsignedInt(temp[1]) == 80) {
                    handle(temp, 80);
                }
                Data.dengKu_change = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(byte[] file, int len) {
        int count = file.length / len;
        if (count > 20) {
            JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "灯库数量不能超过20个！", "提示", JOptionPane.ERROR_MESSAGE);
            return;
        }
        for (int i = 0; i < count; i++) {
            int channelCount = Byte.toUnsignedInt(file[42 + i * len]);
            String version = String.valueOf(Byte.toUnsignedInt(file[7 + i * len])) + "." + String.valueOf(Byte.toUnsignedInt(file[8 + i * len]));
            Data.DengKuVersionList.add(version);
            /////////////////熄灯、速度通道
            if (len >= 128) {
                Map map = new HashMap<>();
                //BlackOutEntity blackOutEntity = (BlackOutEntity)map2.get("blackOutEntity");
                //SpeedEntity speedEntity = (SpeedEntity)map2.get("speedEntity");
                BlackOutEntity blackOutEntity = new BlackOutEntity();
                SpeedEntity speedEntity = new SpeedEntity();
                byte[] cc = new byte[48];
                System.arraycopy(file, 128 * i + 80, cc, 0, 48);
                setEnty(blackOutEntity, speedEntity, cc);

                map.put("blackOutEntity", blackOutEntity);
                map.put("speedEntity", speedEntity);
                Data.dengKuBlackOutAndSpeedList.add(map);
            } else {
                Data.dengKuBlackOutAndSpeedList.add(null);
            }

            /////////////////////////////////////////////
            if (channelCount != 0) {
                int nameLenght = Byte.toUnsignedInt(file[9 + i * len]);
                byte[] b = new byte[nameLenght];
                for (int j = 0; j < nameLenght; j++) {
                    b[j] = file[10 + i * len + j];
                }
                String name = new String(b, 0, nameLenght);//灯库名称

                String[] channels = new String[channelCount];//通道定义
                for (int j = 0; j < channelCount; j++) {
                    channels[j] = GetChannelNumber.getChannelName(Byte.toUnsignedInt(file[43 + i * len + j]));
                }

                //////////////////////////////////////////////////////
                NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");

                Data.DengKuChannelCountList.add(String.valueOf(channelCount));
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                int index = table.getRowCount() + 1;
                String[] s = {index + "", name};
                model.addRow(s);
                JComboBox[] boxs = (JComboBox[]) MainUi.map.get("lamp_1_To_16");
                JComboBox[] boxs2 = (JComboBox[]) MainUi.map.get("lamp_17_To_32");
                //JLabel[] labels = (JLabel[])MainUi.map.get("lamp_1_To_16_label");
                //JLabel[] labels2 = (JLabel[])MainUi.map.get("lamp_17_To_32_label");
                HashMap hashMap = new HashMap();
                ItemListener listener = (ItemListener) MainUi.map.get("ChannelItemListener");
                for (int j = 0; j < 16; j++) {
                    boxs[j].removeItemListener(listener);
                    boxs2[j].removeItemListener(listener);
                }
                if (channelCount > 16) {
                    for (int n = 0; n < 16; n++) {
                        boxs[i].setEnabled(true);
                        //boxs[n].setVisible(true);
                        //labels[n].setVisible(true);
                        boxs[n].setSelectedItem(channels[n]);
                        hashMap.put(boxs[n].getName(), channels[n]);
                    }
                    for (int n = 0; n < channelCount - 16; n++) {
                        hashMap.put(boxs2[n].getName(), channels[n + 16]);
                        boxs2[n].setEnabled(true);
                        //boxs2[n].setVisible(true);
                        //labels2[n].setVisible(true);
                        boxs2[n].setSelectedItem(channels[n + 16]);
                    }
                    for (int n = channelCount - 16; n < boxs2.length; n++) {
                        boxs2[n].setEnabled(false);
                        //boxs2[n].setVisible(false);
                        //labels2[n].setVisible(false);
                    }
                } else {
                    for (int n = 0; n < boxs2.length; n++) {
                        boxs2[n].setEnabled(false);
                        //boxs2[n].setVisible(false);
                        //labels2[n].setVisible(false);
                    }
                    for (int n = 0; n < channelCount; n++) {
                        boxs[n].setEnabled(true);
                        //boxs[n].setVisible(true);
                        //labels[n].setVisible(true);
                        boxs[n].setSelectedItem(channels[n]);
                        hashMap.put(boxs[n].getName(), channels[n]);
                    }
                    for (int n = channelCount; n < boxs.length; n++) {
                        boxs[n].setEnabled(false);
                        //boxs[n].setVisible(false);
                        //labels[n].setVisible(false);
                    }
                }
                for (int j = 0; j < 16; j++) {
                    boxs[j].addItemListener(listener);
                    boxs2[j].addItemListener(listener);
                }
                Data.DengKuList.add(hashMap);
                table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
            }
        }
    }

    private void setEnty(BlackOutEntity blackOutEntity, SpeedEntity speedEntity, byte[] b) {
        //熄灯通道   通道号
        if (Byte.toUnsignedInt(b[8]) == 0) {
            blackOutEntity.setC1("无");
        } else if (Byte.toUnsignedInt(b[8]) == 255) {
            blackOutEntity.setC1("所有");
        } else {
            blackOutEntity.setC1(Byte.toUnsignedInt(b[8]) + "");
        }

        if (Byte.toUnsignedInt(b[12]) == 0) {
            blackOutEntity.setC2("无");
        } else if (Byte.toUnsignedInt(b[12]) == 255) {
            blackOutEntity.setC2("所有");
        } else {
            blackOutEntity.setC2(Byte.toUnsignedInt(b[12]) + "");
        }

        if (Byte.toUnsignedInt(b[16]) == 0) {
            blackOutEntity.setC3("无");
        } else if (Byte.toUnsignedInt(b[16]) == 255) {
            blackOutEntity.setC3("所有");
        } else {
            blackOutEntity.setC3(Byte.toUnsignedInt(b[16]) + "");
        }
        if (Byte.toUnsignedInt(b[20]) == 0) {
            blackOutEntity.setC4("无");
        } else if (Byte.toUnsignedInt(b[20]) == 255) {
            blackOutEntity.setC4("所有");
        } else {
            blackOutEntity.setC4(Byte.toUnsignedInt(b[20]) + "");
        }
        //熄灯通道   灭灯值
        blackOutEntity.setMin1(Byte.toUnsignedInt(b[9]) + "");
        blackOutEntity.setMin2(Byte.toUnsignedInt(b[13]) + "");
        blackOutEntity.setMin3(Byte.toUnsignedInt(b[17]) + "");
        blackOutEntity.setMin4(Byte.toUnsignedInt(b[21]) + "");
        //熄灯通道   最大值
        blackOutEntity.setMax1(Byte.toUnsignedInt(b[10]) + "");
        blackOutEntity.setMax2(Byte.toUnsignedInt(b[14]) + "");
        blackOutEntity.setMax3(Byte.toUnsignedInt(b[18]) + "");
        blackOutEntity.setMax4(Byte.toUnsignedInt(b[22]) + "");

        //速度通道   通道号
        if (Byte.toUnsignedInt(b[28]) != 0) {
            speedEntity.setS1(Byte.toUnsignedInt(b[28]) + "");
        }
        if (Byte.toUnsignedInt(b[32]) != 0) {
            speedEntity.setS2(Byte.toUnsignedInt(b[32]) + "");
        }
        if (Byte.toUnsignedInt(b[36]) != 0) {
            speedEntity.setS3(Byte.toUnsignedInt(b[36]) + "");
        }
        //速度通道   最小值
        speedEntity.setMin1(Byte.toUnsignedInt(b[29]) + "");
        speedEntity.setMin2(Byte.toUnsignedInt(b[33]) + "");
        speedEntity.setMin3(Byte.toUnsignedInt(b[37]) + "");
        //速度通道   最大值
        speedEntity.setMax1(Byte.toUnsignedInt(b[30]) + "");
        speedEntity.setMax2(Byte.toUnsignedInt(b[34]) + "");
        speedEntity.setMax3(Byte.toUnsignedInt(b[38]) + "");
        //速度通道   方向
        String s = "";
        if (Byte.toUnsignedInt(b[31]) == 0) {
            s = "正向";
        } else {
            s = "反向";
        }
        speedEntity.setDirect1(s);

        if (Byte.toUnsignedInt(b[35]) == 0) {
            s = "正向";
        } else {
            s = "反向";
        }
        speedEntity.setDirect2(s);

        if (Byte.toUnsignedInt(b[39]) == 0) {
            s = "正向";
        } else {
            s = "反向";
        }
        speedEntity.setDirect3(s);
    }

    //所有灯库导出
    private void exportAllData() {
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        String dkName = "灯库";
        JFileChooser fileChooser = new JFileChooser();
        try {
            String path = getClass().getResource("/lib/").getPath().substring(1);
            path = URLDecoder.decode(path, "utf-8");
            fileChooser.setCurrentDirectory(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
		/*if (!"".equals(Data.exportPath)) {
			fileChooser.setCurrentDirectory(new File(Data.exportPath));
		} else if (!"".equals(Data.importPath)) {
			fileChooser.setCurrentDirectory(new File(Data.importPath));
		}*/
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setSelectedFile(new File(dkName + ".hxl"));
        int returnVal = fileChooser.showSaveDialog((JFrame) MainUi.map.get("frame"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                Data.exportPath = file.getParent();
                OutputStream os = new FileOutputStream(file);
                DengKuData dengKuData = new DengKuData();
                for (int n = 0; n < table.getRowCount(); n++) {
                    int channelCount = Integer.valueOf(Data.DengKuChannelCountList.get(n).toString());
                    JComboBox[] channelBoxs_L = (JComboBox[]) MainUi.map.get("lamp_1_To_16");
                    JComboBox[] channelBoxs_R = (JComboBox[]) MainUi.map.get("lamp_17_To_32");
                    int[] c = new int[channelCount];
                    if (channelCount > 16) {
                        for (int i = 0; i < 16; i++) {
                            c[i] = GetChannelNumber.get(channelBoxs_L[i].getSelectedItem().toString());
                        }
                        for (int i = 16; i < channelCount; i++) {
                            c[i] = GetChannelNumber.get(channelBoxs_R[i - 16].getSelectedItem().toString());
                        }
                    } else {
                        for (int i = 0; i < channelCount; i++) {
                            c[i] = GetChannelNumber.get(channelBoxs_L[i].getSelectedItem().toString());
                        }
                    }
                    dengKuData.setChannel(c);
                    dengKuData.setName(table.getValueAt(n, 1).toString());
                    dengKuData.setNo(n);
                    dengKuData.setVersion((int) (Double.valueOf(Data.DengKuVersionList.get(n).toString()) * 100));
                    byte[] temp = dengKuData.getbytes();
                    os.write(temp);
                    os.flush();
                }
                os.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }

        }
    }

    //单个灯库导出
    private void exportData() {
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        int selected = table.getSelectedRow();
        if (selected > -1) {
            String dkName = table.getValueAt(table.getSelectedRow(), 1).toString();
            JFileChooser fileChooser = new JFileChooser();
            try {
                String path = getClass().getResource("/lib/").getPath().substring(1);
                path = URLDecoder.decode(path, "utf-8");
                fileChooser.setCurrentDirectory(new File(path));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!"".equals(Data.exportPath)) {
                fileChooser.setCurrentDirectory(new File(Data.exportPath));
            } /*else if (!"".equals(Data.importPath)) {
				fileChooser.setCurrentDirectory(new File(Data.importPath));
			}*/

            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setSelectedFile(new File(dkName + ".hxl"));
            int returnVal = fileChooser.showSaveDialog((JFrame) MainUi.map.get("frame"));
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    Data.exportPath = file.getParent();
                    int channelCount = Integer.valueOf(Data.DengKuChannelCountList.get(selected).toString());
                    //HashMap map = (HashMap)Data.dengKuMapList.get(selected);
                    DengKuData dengKuData = new DengKuData();
                    JComboBox[] channelBoxs_L = (JComboBox[]) MainUi.map.get("lamp_1_To_16");
                    JComboBox[] channelBoxs_R = (JComboBox[]) MainUi.map.get("lamp_17_To_32");
                    int[] c = new int[channelCount];
                    OutputStream os = new FileOutputStream(file);
                    if (channelCount > 16) {
                        for (int i = 0; i < 16; i++) {
                            c[i] = GetChannelNumber.get(channelBoxs_L[i].getSelectedItem().toString());
                        }
                        for (int i = 16; i < channelCount; i++) {
                            c[i] = GetChannelNumber.get(channelBoxs_R[i - 16].getSelectedItem().toString());
                        }
                    } else {
                        for (int i = 0; i < channelCount; i++) {
                            c[i] = GetChannelNumber.get(channelBoxs_L[i].getSelectedItem().toString());
                        }
                    }
                    dengKuData.setChannel(c);
                    dengKuData.setName(dkName);
                    dengKuData.setNo(selected);
                    dengKuData.setVersion((int) (Double.valueOf(Data.DengKuVersionList.get(selected).toString()) * 100));
                    byte[] temp = dengKuData.getbytes();
                    os.write(temp);

                    //熄灯速度通道
                    byte[] cc = getCC(selected);

                    os.write(cc);
                    os.flush();
                    os.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }

            }
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
                if (blackOutEntity.getC(i)[0].equals("所有")) {
                    cc[8 + i * 4] = (byte) 0xFF;
                } else if (blackOutEntity.getC(i)[0].equals("无")) {
                    cc[8 + i * 4] = 0;
                } else {
                    cc[8 + i * 4] = (byte) Integer.valueOf(blackOutEntity.getC(i)[0]).intValue();
                }
                cc[9 + i * 4] = (byte) Integer.valueOf(blackOutEntity.getC(i)[1]).intValue();
                cc[10 + i * 4] = (byte) Integer.valueOf(blackOutEntity.getC(i)[2]).intValue();
            }
            for (int i = 0; i < 3; i++) {
                if (speedEntity.getS(i)[0].equals("无")) {
                    cc[28 + i * 4] = (byte) 0;
                } else {
                    cc[28 + i * 4] = (byte) Integer.valueOf(speedEntity.getS(i)[0]).intValue();
                }
                cc[29 + i * 4] = (byte) Integer.valueOf(speedEntity.getS(i)[1]).intValue();
                cc[30 + i * 4] = (byte) Integer.valueOf(speedEntity.getS(i)[2]).intValue();
                if (speedEntity.getS(i)[3].equals("正向")) {
                    cc[31 + i * 4] = (byte) 0;
                } else {
                    cc[31 + i * 4] = (byte) 1;
                }
            }
        }
        return cc;
    }
}
