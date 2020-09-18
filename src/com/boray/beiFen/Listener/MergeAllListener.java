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
import com.boray.shengKonSuCai.UI.ShengKonSuCaiUI;
import com.boray.xiaoGuoDeng.UI.DefineJLable;
import com.boray.xiaoGuoDeng.reviewCode.ReviewUtils;

public class MergeAllListener implements ActionListener {

    //地址空间-数据区（素材版本）
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
            File file1 = new File(Data.saveCtrlFilePath + "\\K0.DAT");
            DataWrite(file, file1);//写入数据
            JFrame frame = (JFrame) MainUi.map.get("frame");
            JOptionPane.showMessageDialog(frame, "生成灯控文件成功!", "提示", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void DataWrite(File file, File file1) {
        try {
            file1.createNewFile();
            OutputStream stream = new FileOutputStream(file1);
            OutputStream os10 = new FileOutputStream(file);

            //////////////////////
            //系统设置(00-13SEC)14
            systemSet(os10);
            os10.flush();
            System.out.println("系统设置：" + file.length());

            //灯库(14-15SEC)2
            writeFile2(os10);
            repairData(62464, os10, file);
            P1(file1, stream, os10);//写入动态空间索引表
            repairData(65536, os10, file);
            os10.flush();
            System.out.println("灯库：" + file.length());

            //按步编程（倒彩&喝彩&摇麦-16-33SEC）18
            writeHeCaiDaoCaiYaoMai(os10);
//                repairData(139264, os10, file);
            os10.flush();
            System.out.println("按步编程：" + file.length());

            //雾机编程(34SEC)
            writeWuJiModelData(os10);
//                repairData(143360, os10, file);
            for (int i = 0; i < 3548; i++) {
                os10.write(new byte[1]);
            }
            os10.flush();
            System.out.println("雾机编程：" + file.length());


            ////效果灯素材数据（35-228SEC）194
            writeFile4(os10);
//                repairData(937984, os10, file);
            os10.flush();
            System.out.println("效果灯素材：" + file.length());

            ////场景效果灯数据（229-258SEC）30
            for (int i = 1; i < 25; i++) {
                writeXiaoGuoDengData(os10, i);
            }
//                repairData(1060864, os10, file);
            os10.flush();
            System.out.println("场景效果灯：" + file.length());

            ////多灯数据区-16 个声控模式(259-387SEC)129
            shengKonMoreLigthDatatest(os10);
//                repairData(1589248, os10, file);
            os10.flush();
            System.out.println("多灯数据区-16：" + file.length());

            //声控素材数据
            writeShengKonSucai(os10);
            os10.flush();
            System.out.println("声控素材数据：" + file.length());

            ////声控效果灯数据（动态空间）
            for (int i = 1; i < 17; i++) {
                writeShengKon(os10, i);
            }

            os10.flush();
            System.out.println("声控效果灯数据：" + file.length());

            //场景多灯
            os10.write(ReviewUtils.changJingDuoDengReview(0, true));
            os10.flush();
            System.out.println("场景多灯数据：" + file.length());

            //动作素材
            os10.write(dongZuoSuCai());
            os10.flush();
            System.out.println("动作素材数据：" + file.length());

            os10.close();

            file1.delete();//删除计算用的K0文件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void P1(File file1, OutputStream stream, OutputStream os10) {
        try {
            repairData(65536, stream, file1);//补足前面的数据
            List<Long> longs = new ArrayList<>();
            //按步编程（倒彩&喝彩&摇麦-16-33SEC）18
            writeHeCaiDaoCaiYaoMai(stream);
            longs.add(file1.length());
            stream.flush();

            //雾机编程(34SEC)
            writeWuJiModelData(stream);
            for (int i = 0; i < 3548; i++) {
                stream.write(new byte[1]);
            }
            longs.add(file1.length());
            stream.flush();

            ////效果灯素材数据（35-228SEC）194
            writeFile4(stream);
            longs.add(file1.length());
            stream.flush();

            ////场景效果灯数据（229-258SEC）30
            for (int i = 1; i < 25; i++) {
                writeXiaoGuoDengData(stream, i);
            }
            longs.add(file1.length());
            stream.flush();

            ////多灯数据区-16 个声控模式(259-387SEC)129
            shengKonMoreLigthDatatest(stream);
            longs.add(file1.length());
            stream.flush();

            //声控素材数据
            writeShengKonSucai(stream);
            longs.add(file1.length());
            stream.flush();

            ////声控效果灯数据（动态空间）
            for (int i = 1; i < 17; i++) {
                writeShengKon(stream, i);
            }
            longs.add(file1.length());
            stream.flush();

            //场景多灯
            stream.write(ReviewUtils.changJingDuoDengReview(0, true));
            longs.add(file1.length());
            stream.flush();

            //动作素材
            stream.write(dongZuoSuCai());
            longs.add(file1.length());
            stream.close();

            dongTaiKonJianTable(longs, os10);//写入动态空间索引表

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 动态空间索引表
     *
     * @param longs
     * @param os
     */
    private void dongTaiKonJianTable(List<Long> longs, OutputStream os) throws IOException {
        byte[][] bytes = new byte[10][8];
        //总容量. 索引表
        bytes[0][0] = (byte) 0xA1;//固定码
        bytes[0][1] = (byte) 0x1A;
        bytes[0][2] = (byte) 0xA3;
        bytes[0][3] = (byte) 0x3A;
        String count = addZeroForNum(Long.toHexString(longs.get(longs.size() - 1) - 65536));
        bytes[0][4] = (byte) Integer.parseInt(count.substring(0, 2), 16);
        bytes[0][5] = (byte) Integer.parseInt(count.substring(2, 4), 16);
        bytes[0][6] = (byte) Integer.parseInt(count.substring(4, 6), 16);
        bytes[0][7] = (byte) Integer.parseInt(count.substring(6), 16);
        //按步编程. 索引表
        //起始地址
        String str = addZeroForNum(Long.toHexString(65536));
        bytes[1][0] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[1][1] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[1][2] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[1][3] = (byte) Integer.parseInt(str.substring(6), 16);
        str = addZeroForNum(Long.toHexString(longs.get(0) - 65536));//数据容量
        bytes[1][4] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[1][5] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[1][6] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[1][7] = (byte) Integer.parseInt(str.substring(6), 16);
        //雾机数据 . 索引表
        //起始地址
        str = addZeroForNum(Long.toHexString(longs.get(0)));
        bytes[2][0] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[2][1] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[2][2] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[2][3] = (byte) Integer.parseInt(str.substring(6), 16);
        str = addZeroForNum(Long.toHexString(longs.get(1) - longs.get(0)));//数据容量
        bytes[2][4] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[2][5] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[2][6] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[2][7] = (byte) Integer.parseInt(str.substring(6), 16);
        //效果灯素材空间数据材 . 索引表
        //起始地址
        str = addZeroForNum(Long.toHexString(longs.get(1)));
        bytes[3][0] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[3][1] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[3][2] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[3][3] = (byte) Integer.parseInt(str.substring(6), 16);
        str = addZeroForNum(Long.toHexString(longs.get(2) - longs.get(1)));//数据容量
        bytes[3][4] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[3][5] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[3][6] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[3][7] = (byte) Integer.parseInt(str.substring(6), 16);
        //场景素材调用数据 . 索引表
        //起始地址
        str = addZeroForNum(Long.toHexString(longs.get(2)));
        bytes[4][0] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[4][1] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[4][2] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[4][3] = (byte) Integer.parseInt(str.substring(6), 16);
        str = addZeroForNum(Long.toHexString(longs.get(3) - longs.get(2)));//数据容量
        bytes[4][4] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[4][5] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[4][6] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[4][7] = (byte) Integer.parseInt(str.substring(6), 16);
        //多灯数据区声控模式 . 索引表
        //起始地址
        str = addZeroForNum(Long.toHexString(longs.get(3)));
        bytes[5][0] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[5][1] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[5][2] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[5][3] = (byte) Integer.parseInt(str.substring(6), 16);
        str = addZeroForNum(Long.toHexString(longs.get(4) - longs.get(3)));//数据容量
        bytes[5][4] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[5][5] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[5][6] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[5][7] = (byte) Integer.parseInt(str.substring(6), 16);
        //声控素材区 . 索引表
        //起始地址
        str = addZeroForNum(Long.toHexString(longs.get(4)));
        bytes[6][0] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[6][1] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[6][2] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[6][3] = (byte) Integer.parseInt(str.substring(6), 16);
        str = addZeroForNum(Long.toHexString(longs.get(5) - longs.get(4)));//数据容量
        bytes[6][4] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[6][5] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[6][6] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[6][7] = (byte) Integer.parseInt(str.substring(6), 16);
        //声控素材调用数据. 索引表
        //起始地址
        str = addZeroForNum(Long.toHexString(longs.get(5)));
        bytes[7][0] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[7][1] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[7][2] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[7][3] = (byte) Integer.parseInt(str.substring(6), 16);
        str = addZeroForNum(Long.toHexString(longs.get(6) - longs.get(5)));//数据容量
        bytes[7][4] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[7][5] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[7][6] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[7][7] = (byte) Integer.parseInt(str.substring(6), 16);
        //场景多灯
        str = addZeroForNum(Long.toHexString(longs.get(6)));
        bytes[8][0] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[8][1] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[8][2] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[8][3] = (byte) Integer.parseInt(str.substring(6), 16);
        str = addZeroForNum(Long.toHexString(longs.get(7) - longs.get(6)));//数据容量
        bytes[8][4] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[8][5] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[8][6] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[8][7] = (byte) Integer.parseInt(str.substring(6), 16);
        //动作素材
        str = addZeroForNum(Long.toHexString(longs.get(7)));
        bytes[9][0] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[9][1] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[9][2] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[9][3] = (byte) Integer.parseInt(str.substring(6), 16);
        str = addZeroForNum(Long.toHexString(longs.get(8) - longs.get(7)));//数据容量
        bytes[9][4] = (byte) Integer.parseInt(str.substring(0, 2), 16);
        bytes[9][5] = (byte) Integer.parseInt(str.substring(2, 4), 16);
        bytes[9][6] = (byte) Integer.parseInt(str.substring(4, 6), 16);
        bytes[9][7] = (byte) Integer.parseInt(str.substring(6), 16);
        for (int i = 0; i < 10; i++) {
            os.write(bytes[i]);
        }
    }


    /**
     * 动作素材
     *
     * @return
     */
    private byte[] dongZuoSuCai() {
        int count = 0;
        for (Object key : Data.SuCaiDongZuoName.keySet()) {
            List<String> suCaiNameList = (List<String>) Data.SuCaiDongZuoName.get(key);
            if (suCaiNameList != null)
                count += suCaiNameList.size();
        }
        byte[] yinDaoQu = new byte[6];//引导区
        yinDaoQu[0] = (byte) 0xAC;//固定字节
        yinDaoQu[1] = (byte) 0xAB;
        yinDaoQu[2] = (byte) count;//素材总数
        yinDaoQu[3] = 41;
        yinDaoQu[4] = (byte) 0xA0;
        yinDaoQu[5] = (byte) 0xA1;
        List<Byte> list = new ArrayList<>();
        for (int i = 0; i < yinDaoQu.length; i++) {
            list.add(yinDaoQu[i]);
        }
        for (int i = 0; i < count; i++) {
            Map hashMap = (Map) Data.SuCaiDongZuoObject[i];
            if (hashMap != null) {
                Map map = (Map) hashMap.get("actionXiaoGuoData");
                //动作图形
                int selected = Integer.valueOf((String) map.get("2"));
                if (selected == 1) {
                    selected = 255;
                } else if (selected > 1) {
                    selected = selected - 1;
                }
                list.add((byte) selected);
                //运行速度
                int yunXinSpeed = Integer.valueOf((String) map.get("3"));
                list.add((byte) yunXinSpeed);
                //使用开关 1启用/0关
                String ss = (String) map.get("0");
                if (ss != null && "true".equals(ss)) {
                    list.add((byte) 1);
                } else {
                    list.add((byte) 0);
                }
                //拆分    不拆分01/中间拆分02/两端拆分03
                String[] tp1 = (String[]) map.get("4");
                int cc = Integer.valueOf(tp1[0]) + 1;
                list.add((byte) cc);

                //拆分反向
                boolean b = map.containsKey("5") ? (boolean) map.get("5") : false;
                if (b) {
                    list.add((byte) 1);
                } else {
                    list.add((byte) 0);
                }
                int a = 0;
                //X轴反向    是1/否0
                if ("true".equals(tp1[1])) {
                    a = 1;
                } else {
                    a = 0;
                }
                if (b) {
                    if (a == 1) {
                        list.add((byte) 0x81);
                    } else {
                        list.add((byte) 0x80);
                    }
                } else {
                    list.add((byte) a);
                }
                //X半
                if ("true".equals(tp1[2])) {
                    a = 1;
                } else {
                    a = 0;
                }
                list.add((byte) a);
                //Y轴反向
                if ("true".equals(tp1[3])) {
                    a = 1;
                } else {
                    a = 0;
                }
                list.add((byte) a);
                //Y半
                if ("true".equals(tp1[4])) {
                    a = 1;
                } else {
                    a = 0;
                }
                list.add((byte) a);


                //时间A_L	时间B_H
                a = Integer.valueOf(tp1[5]).intValue();
                list.add((byte) (a % 256));
                list.add((byte) (a / 256));

                //自定义动作数据
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
                for (int k = 0; k < bt1.length; k++) {
                    list.add((byte) bt1[k]);
                }
                //贝塞尔曲线
                int[] p1 = null;
                int action = selected;//图型号
                if (action == 0) {
                    p1 = bezier.Data.ZB[1];
                } else if (action == 1) {
                    p1 = bezier.Data.ZB[0];
                } else if (action > 1 && action < 48) {
                    p1 = bezier.Data.ZB[action];
                } else if (action >= 48) {
                    String[] s = (String[]) bezier.Data.map.get("" + action);
                    if (s != null) {
                        p1 = new int[24];
                        for (int k = 0; k < s.length; k++) {
                            p1[k] = Integer.valueOf(s[k]);
                        }
                    } else {
                        p1 = bezier.Data.ZB[0];
                    }
                }
                list.add((byte) action);
                for (int k = 0; k < p1.length; k++) {
                    list.add((byte) p1[k]);
                }
            } else {
                for (int k = 0; k < 41; k++) {
                    list.add((byte) 0);
                }
            }
        }
        byte[] bytes = new byte[list.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = list.get(i);
        }
        return bytes;
    }

    /*
     *数字不足位数左补0
     *
     * @param str
     * @param strLength
     */
    public String addZeroForNum(String str) {
        int strLen = str.length();
        if (strLen < 8) {
            while (strLen < 8) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);//左补0
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }

    /**
     * 对数据空间不足的进行补完
     *
     * @param length
     * @param outputStream
     * @param file
     */
    private void repairData(long length, OutputStream outputStream, File file) throws IOException {
        outputStream.flush();
        int sy = (int) (length - file.length());
        byte[] cc = new byte[sy];
        for (int i = 0; i < sy; i++) {
            cc[i] = (byte) 0XFF;
        }
        outputStream.write(cc);
    }

    /*
     * 灯库模块
     */
    private void writeFile2(OutputStream os) throws Exception {
        //灯库
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
            //熄灯速度通道
            System.arraycopy(getCC(selected), 7, t2[selected], 0, 37);
        }

        //灯具
        byte[][] t3 = new byte[50][23];
        table = (NewJTable) MainUi.map.get("table_dengJu");
        //{"启用","ID","灯具名称","型号","库版本","DMX起始地址","占用通道数"};
        int add = 0;
        for (int i = 0; i < 50; i++) {
            if (i < table.getRowCount()) {
                t3[i][0] = (byte) (i + 1);
                boolean b = (boolean) table.getValueAt(i, 0);
                if (b) {
                    t3[i][1] = 1;
                }
                //地址
                add = Integer.valueOf(table.getValueAt(i, 5).toString()).intValue();
                t3[i][2] = (byte) (add / 256);
                t3[i][3] = (byte) (add % 256);
                //占用通道数
                add = Integer.valueOf(table.getValueAt(i, 6).toString()).intValue();
                t3[i][4] = (byte) add;
                //灯具名称
                String s = (String) (table.getValueAt(i, 2));
                t3[i][5] = (byte) s.getBytes().length;
//                for (int j = 0; j < s.getBytes().length; j++) {
//                    t3[i][6 + j] = s.getBytes()[j];
//                }
                //关联灯库编号
                s = table.getValueAt(i, 3).toString();
                s = s.split("#")[0].substring(2);
                t3[i][22] = (byte) Integer.valueOf(s).intValue();
            }
        }
        //灯具分组
        byte[][] t4 = new byte[30][28];
        table = (NewJTable) MainUi.map.get("GroupTable");
        int q, p;
        //{"启用","ID","组别名称"};
        for (int i = 0; i < 30; i++) {
            if (i < table.getRowCount()) {
                t4[i][1] = (byte) (i + 1);
                boolean b = (boolean) table.getValueAt(i, 0);
                if (b) {
                    t4[i][2] = 1;
                }
                //关联灯具编号
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
                //分组名称
//                String s = (String) table.getValueAt(i, 2);
//                t4[i][10] = (byte) s.getBytes().length;
//                for (int j = 0; j < s.getBytes().length; j++) {
//                    t4[i][11 + j] = s.getBytes()[j];
//                }
            }
        }

        //写入文件
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

    /**
     * 效果灯素材打包
     *
     * @throws Exception
     */
    private void writeFile4(OutputStream os) throws Exception {

        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        NewJTable dengZuTable = (NewJTable) MainUi.map.get("GroupTable");
        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");

        byte[] dengKuTonDao = new byte[23];//灯库通道定义
        byte[] dengKuMeiZuShuLiang = new byte[30];//灯库每组数量
        //固定头
        dengKuTonDao[0] = 0x55;
        dengKuTonDao[1] = (byte) 0xAA;
        dengKuTonDao[2] = (byte) ((byte) table.getRowCount() == 0 ? 0x00 : table.getRowCount());//灯库总数
        int count = 0;
        for (int i = 0; i < table.getRowCount(); i++) {
            String dengKuId = (String) table.getValueAt(i, 0);
            //素材通道数
            int suCaiTonDaoShu = Integer.parseInt(Data.DengKuChannelCountList.get(Integer.parseInt(dengKuId) - 1).toString());
            dengKuTonDao[3 + i] = (byte) suCaiTonDaoShu;

        }
        //素材个数
        for (int i = 0; i < dengZuTable.getRowCount(); i++) {
            int num = 0;
            Map map = (Map) Data.suCaiNameMap.get(i);
            for (int k = 0; k < btns.length; k++) {
                String selectedName = btns[k].getText().substring(0, 2);
                if (map != null) {
                    List<String> suCaiNameList = (List<String>) map.get(selectedName);
                    if (suCaiNameList != null)
                        num += suCaiNameList.size();
                }
            }
            dengKuMeiZuShuLiang[i] = (byte) num;
            count += num;
        }
        List<Byte> xiaoGuoDengGuanLian = new ArrayList<>();//效果灯关联区
        xiaoGuoDengGuanLian.add((byte) 0x55);//引导区
        xiaoGuoDengGuanLian.add((byte) 0xAA);
        xiaoGuoDengGuanLian.add((byte) 0x00);
        xiaoGuoDengGuanLian.add((byte) 0x00);//素材起始地址
        xiaoGuoDengGuanLian.add((byte) 0x00);
        xiaoGuoDengGuanLian.add((byte) 0x00);
        xiaoGuoDengGuanLian.add((byte) (count % 256));//素材总数
        xiaoGuoDengGuanLian.add((byte) (count / 256));
        List<String> str = new ArrayList<>();//记录素材关联
        for (int i = 0; i < dengZuTable.getRowCount(); i++) {
            //素材个数
            int cnt = 0;
            Map map = (Map) Data.suCaiNameMap.get(i);
            for (int k = 0; k < btns.length; k++) {
                String selectedName = btns[k].getText().substring(0, 2);
                if (map != null) {
                    List<String> suCaiNameList = (List<String>) map.get(selectedName);
                    if (suCaiNameList != null)
                        cnt += suCaiNameList.size();
                }
            }
            for (int j = 0; j < cnt; j++) {
                str.add(i + "#" + j);
                xiaoGuoDengGuanLian.add((byte) (i + 1));//素材关联
                xiaoGuoDengGuanLian.add((byte) (j + 1));
            }
        }
        for (int i = 0; i < str.size(); i++) {
            int denZuNum = Integer.parseInt(str.get(i).split("#")[0]);
            int suCaiNum = Integer.parseInt(str.get(i).split("#")[1]);
            HashMap hashMap = (HashMap) Data.SuCaiObjects[denZuNum][suCaiNum];
            if (hashMap != null) {
                List list66 = (List) hashMap.get("channelData");
                Vector vector88 = null;
                if (list66 != null) {
                    vector88 = (Vector) list66.get(0);
                    if (vector88 != null)
                        xiaoGuoDengGuanLian.add((byte) vector88.size());
                    else
                        xiaoGuoDengGuanLian.add((byte) 1);//默认一步
                } else {
                    xiaoGuoDengGuanLian.add((byte) 1);//默认一步
                }
            } else {
                xiaoGuoDengGuanLian.add((byte) 1);//默认一步
            }
        }
        byte[] suCaiData = suCaiQuData(str);//素材区数据

        os.write(dengKuTonDao);
        os.write(dengKuMeiZuShuLiang);
        for (int i = 0; i < xiaoGuoDengGuanLian.size(); i++) {
            os.write(xiaoGuoDengGuanLian.get(i));
        }
        os.write(suCaiData);
    }

    //素材区数据
    public byte[] suCaiQuData(List<String> str) {
        List<Byte> list = new ArrayList<>();
        for (int i = 0; i < str.size(); i++) {
            int dengZuNum = Integer.parseInt(str.get(i).split("#")[0]);
            int suCaiNum = Integer.parseInt(str.get(i).split("#")[1]);
            HashMap hashMap = (HashMap) Data.SuCaiObjects[dengZuNum][suCaiNum];
            TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(dengZuNum);
            //素材通道数
            NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//所有灯具
            int j = (int) treeSet.first();
            String typeString = table3.getValueAt(j, 3).toString();//灯具型号
            int dengKuNumber = Integer.valueOf(typeString.split("#")[0].substring(2)).intValue() - 1;
            int suCaiTonDaoShu = Integer.valueOf((String) Data.DengKuChannelCountList.get(dengKuNumber)).intValue();
            //灯具数量
            int cnt = treeSet.size();
            if (hashMap != null) {
                //通道
                int a = 0;
                List list66 = (List) hashMap.get("channelData");
                Vector vector88 = null;
                //通道控制配置数据
                byte[] tonDaoKonZhi = new byte[6];
                byte[] gouXuan = new byte[cnt * 4];
                byte[] gouXuan2 = new byte[cnt * 4];
                boolean[][] gouXuanValues = null;
                List<Byte> tonDaoBu = new ArrayList<>();
                for (int k = 0; k < (suCaiTonDaoShu * cnt) + 2; k++) {
                    tonDaoBu.add((byte) 00);
                }
                if (list66 != null) {
                    //////////场景勾选
                    int r = 0, yu = 0;
                    gouXuanValues = (boolean[][]) list66.get(1);
                    if (gouXuanValues != null) {
                        for (int k2 = 0; k2 < gouXuanValues.length; k2++) {
                            for (int l = 0; l < gouXuanValues[0].length; l++) {
                                r = l / 8;
                                yu = 7 - (l % 8);
                                if (gouXuanValues[k2][l]) {
                                    gouXuan[k2 * 4 + r] = (byte) (Byte.toUnsignedInt(gouXuan[k2 * 4 + r]) + (1 << yu));
                                }
                            }
                        }
                    }
                    //第二排勾选
                    gouXuanValues = (boolean[][]) list66.get(2);
                    if (gouXuanValues != null) {
                        for (int k2 = 0; k2 < gouXuanValues.length; k2++) {
                            for (int l = 0; l < gouXuanValues[0].length; l++) {
                                r = l / 8;
                                yu = 7 - (l % 8);
                                if (gouXuanValues[k2][l]) {
                                    gouXuan2[k2 * 4 + r] = (byte) (Byte.toUnsignedInt(gouXuan2[k2 * 4 + r]) + (1 << yu));
                                }
                            }
                        }
                    }

                    a = 0;
                    vector88 = (Vector) list66.get(0);
                    if (vector88 != null) {
                        a = vector88.size();
                    }
                    //总帧数
                    tonDaoKonZhi[0] = (byte) a;
                    //手动编程启用
                    tonDaoKonZhi[1] = (byte) 1;

                    //素材灯具数量
                    tonDaoKonZhi[2] = (byte) cnt;
                    tonDaoKonZhi[3] = (byte) suCaiTonDaoShu;
                    //拆分  拆分反向  (备用)
                    tonDaoKonZhi[4] = (byte) 0;
                    tonDaoKonZhi[5] = (byte) 0;
                    //通道步XX编程
                    Vector tpe = null;
                    int timeTp = 0, lgth = 0;
                    int lenght = suCaiTonDaoShu * cnt;
                    if (vector88 != null) {
                        tonDaoBu.clear();
                        for (int n = 0; n < vector88.size(); n++) {
                            tpe = (Vector) vector88.get(n);
                            timeTp = Integer.valueOf(tpe.get(1).toString()).intValue();
                            tonDaoBu.add((byte) (timeTp % 256));
                            tonDaoBu.add((byte) (timeTp / 256));
                            if (tpe.size() - 2 >= lenght) {
                                lgth = lenght + 2;
                            } else {
                                lgth = tpe.size();
                            }
                            for (int l = 2; l < lgth; l++) {
                                tonDaoBu.add(Integer.valueOf(tpe.get(l).toString()).byteValue());
                            }
                            if (tpe.size() - 2 < lenght) {
                                byte[] bytes = new byte[lenght - (tpe.size() - 2)];
                                for (int k = 0; k < bytes.length; k++) {
                                    tonDaoBu.add(bytes[k]);
                                }
                            }
                        }
                    }
                }
                integrate(list, tonDaoKonZhi);//手动配置 6
                integrate(list, gouXuan);//场景勾选 4*灯具数量
                integrate(list, gouXuan2);//渐变勾选 4*灯具数量
                integrate2(list, tonDaoBu);//通道步数据
            } else {
                int length = 0;
                length += 6;//手动配置
                length += 4 * cnt;//场景勾选
                length += 4 * cnt;//渐变勾选
                length += (suCaiTonDaoShu * cnt) + 2;
                for (int k = 0; k < length; k++) {//没有数据默认补零，一个通道
                    list.add((byte) 00);
                }
            }
        }
        byte[] buff = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            buff[i] = list.get(i);
        }
        return buff;
    }

    //数据整合
    public static void integrate(List<Byte> list, byte[] buff) {
        for (int i = 0; i < buff.length; i++) {
            list.add(buff[i]);
        }
    }

    public static void integrate2(List<Byte> list, List<Byte> tonDaoBu) {
        for (int i = 0; i < tonDaoBu.size(); i++) {
            list.add(tonDaoBu.get(i));
        }
    }

    /**
     * 效果灯素材打包格式，弃用——2019年12月7日08:27:10
     *
     * @param os
     * @throws Exception
     */
    private void writeFile3(OutputStream os) throws Exception {
        //素材引导区
        byte[] bytes = new byte[120];
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        for (int i = 0; i < table.getRowCount(); i++) {
            //固定头
            bytes[0 + (i * 6)] = 0x55;
            bytes[1 + (i * 6)] = (byte) 0xAA;
            //素材个数
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
            //素材通道数
            int suCaiTonDaoShu = Integer.parseInt(Data.DengKuChannelCountList.get(Integer.parseInt(dengKuId) - 1).toString());
            bytes[3 + (i * 6)] = (byte) suCaiTonDaoShu;
            //素材启用(默认启用)
            bytes[4 + (i * 6)] = 01;
            //素材属性（暂未定义）
        }
        os.write(bytes);
        //素材区数据
        HashMap hashMap = null;
        byte[][][] T1 = new byte[20][30][53];
        byte[][][][] objects = new byte[20][30][32][34];//32通道数据
        Object[][] zdyObjects = new Object[20][30];//自定义动作数据
        Object[][] gxObjects = new Object[20][30];//场景勾选数据
        byte[][][][] actAndRGB = new byte[20][30][4][24];//贝塞尔曲线12点数据区

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
                    //动作图形
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
                        //动作图形 12点数据
                        actAndRGB[i][j][0] = actAndRGB(0, selected);
                        //运行速度
                        int yunXinSpeed = Integer.valueOf((String) map.get("3"));
                        T1[i][j][1] = (byte) yunXinSpeed;
                        //使用开关 1启用/0关
                        String ss = (String) map.get("0");
                        if (ss != null && "true".equals(ss)) {
                            T1[i][j][2] = (byte) 1;
                        }
                        //拆分    不拆分01/中间拆分02/两端拆分03
                        String[] tp1 = (String[]) map.get("4");
                        int cc = Integer.valueOf(tp1[0]) + 1;
                        T1[i][j][3] = (byte) cc;

                        //拆分反向
                        boolean b = map.containsKey("5") ? (boolean) map.get("5") : false;
                        //X轴反向    是1/否0
                        if ("true".equals(tp1[1])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][5] = (byte) a;
                        if (b) {
                            if (a == 1) {
                                T1[i][j][5] = (byte) 0x81;
                            } else {
                                T1[i][j][5] = (byte) 0x80;
                            }
                        }
                        //X半
                        if ("true".equals(tp1[2])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][6] = (byte) a;
                        //Y轴反向
                        if ("true".equals(tp1[3])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][7] = (byte) a;
                        //Y半
                        if ("true".equals(tp1[4])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][8] = (byte) a;


                        //时间A_L	时间B_H
                        a = Integer.valueOf(tp1[5]).intValue();
                        T1[i][j][9] = (byte) (a % 256);
                        T1[i][j][10] = (byte) (a / 256);

                        ///////////////自定义动作数据
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
                        //红色
                        tp2 = (String[]) list.get(1);
                        a = Integer.valueOf(tp2[0]).intValue();
                        T1[i][j][11] = (byte) a;
                        //绿色
                        a = Integer.valueOf(tp2[1]).intValue();
                        T1[i][j][12] = (byte) a;
                        //蓝色
                        a = Integer.valueOf(tp2[2]).intValue();
                        T1[i][j][13] = (byte) a;
                        //渐变类型
                        b = (String) list.get(4);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][14] = (byte) a;
                        //RGB1 12点数据
                        actAndRGB[i][j][1] = actAndRGB(1, a);
                        //渐变
                        bb = (boolean) list.get(5);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][15] = (byte) a;
                        //参与渐变勾选
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
                        //渐变速度
                        b = (String) list.get(6);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][17] = (byte) a;
                        //使用开关
                        bb = (boolean) list.get(0);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][18] = (byte) a;
                        //拆分
                        b = (String) list.get(7);
                        a = Integer.valueOf(b).intValue() + 1;
                        T1[i][j][19] = (byte) a;

                        //拆分反向
                        bb = (boolean) list.get(8);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][20] = (byte) a;
                        //时间A_L	时间B_H
                        a = Integer.valueOf((String) list.get(9)).intValue();
                        T1[i][j][21] = (byte) (a % 256);
                        T1[i][j][22] = (byte) (a / 256);
                    }

                    //RGB2
                    list = (List) hashMap.get("rgb2Data");
                    if (list != null) {
                        //红色
                        tp2 = (String[]) list.get(1);
                        a = Integer.valueOf(tp2[0]).intValue();
                        T1[i][j][23] = (byte) a;
                        //绿色
                        a = Integer.valueOf(tp2[1]).intValue();
                        T1[i][j][24] = (byte) a;
                        //蓝色
                        a = Integer.valueOf(tp2[2]).intValue();
                        T1[i][j][25] = (byte) a;
                        //渐变类型
                        b = (String) list.get(4);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][26] = (byte) a;
                        //RGB1 12点数据
                        actAndRGB[i][j][2] = actAndRGB(1, a);
                        //渐变
                        bb = (boolean) list.get(5);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][27] = (byte) a;
                        //参与渐变勾选
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
                        //渐变速度
                        b = (String) list.get(6);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][29] = (byte) a;
                        //使用开关
                        bb = (boolean) list.get(0);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][30] = (byte) a;
                        //拆分
                        b = (String) list.get(7);
                        a = Integer.valueOf(b).intValue() + 1;
                        T1[i][j][31] = (byte) a;

                        //拆分反向
                        bb = (boolean) list.get(8);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][32] = (byte) a;
                        //时间A_L	时间B_H
                        a = Integer.valueOf((String) list.get(9)).intValue();
                        T1[i][j][33] = (byte) (a % 256);
                        T1[i][j][34] = (byte) (a / 256);
                    }

                    //RGB3
                    list = (List) hashMap.get("rgb3Data");
                    if (list != null) {
                        //红色
                        tp2 = (String[]) list.get(1);
                        a = Integer.valueOf(tp2[0]).intValue();
                        T1[i][j][35] = (byte) a;
                        //绿色
                        a = Integer.valueOf(tp2[1]).intValue();
                        T1[i][j][36] = (byte) a;
                        //蓝色
                        a = Integer.valueOf(tp2[2]).intValue();
                        T1[i][j][37] = (byte) a;
                        //渐变类型
                        b = (String) list.get(4);
                        a = Integer.valueOf(b).intValue();
                        //RGB3 12点数据
                        actAndRGB[i][j][1] = actAndRGB(1, a);
                        T1[i][j][38] = (byte) a;
                        //渐变
                        bb = (boolean) list.get(5);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][39] = (byte) a;
                        //参与渐变勾选
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
                        //渐变速度
                        b = (String) list.get(6);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][41] = (byte) a;
                        //使用开关
                        bb = (boolean) list.get(0);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][42] = (byte) a;
                        //拆分
                        b = (String) list.get(7);
                        a = Integer.valueOf(b).intValue() + 1;
                        T1[i][j][43] = (byte) a;

                        //拆分反向
                        bb = (boolean) list.get(8);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][44] = (byte) a;
                        //时间A_L	时间B_H
                        a = Integer.valueOf((String) list.get(9)).intValue();
                        T1[i][j][45] = (byte) (a % 256);
                        T1[i][j][46] = (byte) (a / 256);
                    }

                    /////手动编程配置
                    List list66 = (List) hashMap.get("channelData");
                    Vector vector88 = null;
                    if (list66 != null) {
                        //////////勾选
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
                        //总帧数
                        T1[i][j][47] = (byte) a;
                        //手动编程启用
                        T1[i][j][48] = (byte) 1;

                        //时差A_L	时差B_H
                        a = Integer.valueOf(ddTemp[2]).intValue();
                        T1[i][j][49] = (byte) (a % 256);
                        T1[i][j][50] = (byte) (a / 256);
                        //拆分
                        a = Integer.valueOf(ddTemp[0]).intValue() + 1;
                        T1[i][j][51] = (byte) a;
                        //拆分反向
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

        //数据写入  动作模块、RGB模块、手动配置、32通道数据、自定义动作数据、勾选数据、贝塞尔曲线
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 30; j++) {
                os.write(T1[i][j]);
                for (int k = 0; k < 32; k++) {//32数据通道
                    os.write(objects[i][j][k]);
                }
                if (null == zdyObjects[i][j]) {//自定义动作数据
                    os.write(new byte[5]);
                } else {
                    os.write((byte[]) zdyObjects[i][j]);
                }
                if (null == gxObjects[i][j]) {//勾选数据
                    os.write(new byte[4]);
                } else {
                    os.write((byte[]) gxObjects[i][j]);
                }
                for (int k = 0; k < 4; k++) {//贝塞尔曲线
                    os.write(new byte[1]);
                    os.write(actAndRGB[i][j][k]);
                    os.write(new byte[1]);
                    os.write(new byte[16]);
                }
            }
        }
    }

    /**
     * 场景效果灯关联数据写入
     * @param os
     * @param sc
     */
    private void writeXiaoGuoDengData(OutputStream os, int sc) {
        byte[] guDingTou = new byte[2];//固定头
        guDingTou[0] = 0x55;
        guDingTou[1] = (byte) 0xAA;
        byte[] quanJu = new byte[5];//效果灯全局设置
        byte[] luZhi = new byte[2];//录制启用/禁用
        byte[] beiYong = new byte[11];//备用
        byte[][] changJingQiYong = new byte[20][8];//场景启用
        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + sc);
        int j = 0, yu = 0;
        int maxTime = 0, tp6 = 0;
        for (int i = 0; i < 20; i++) {
            short[] qiYong = new short[4];//前30组
            short[] qiYong2 = new short[4];//后30组
            for (int k = 0; k < 30; k++) {
                j = k / 8;
                yu = 7 - (k % 8);
                if (timeBlockPanels[k].isVisible()) {
                    if (timeBlockPanels[k].getComponentCount() > i) {
                        DefineJLable lable = (DefineJLable) timeBlockPanels[k].getComponent(i);
                        if (lable.getText().contains("√")) {
                            tp6 = (lable.getX() + lable.getWidth()) / 5;
                            if (tp6 > maxTime) {
                                maxTime = tp6;
                            }
                            qiYong[j] = (short) (qiYong[j] + (1 << yu));
                        }
                    }
                }
                if (timeBlockPanels[k + 30].isVisible()) {
                    if (timeBlockPanels[k + 30].getComponentCount() > i) {
                        DefineJLable lable = (DefineJLable) timeBlockPanels[k + 30].getComponent(i);
                        if (lable.getText().contains("√")) {
                            tp6 = (lable.getX() + lable.getWidth()) / 5;
                            if (tp6 > maxTime) {
                                maxTime = tp6;
                            }
                            qiYong2[j] = (short) (qiYong2[j] + (1 << yu));
                        }
                    }
                }
            }
            for (int k = 0; k < 4; k++) {
                changJingQiYong[i][k] = (byte) qiYong[k];
                changJingQiYong[i][k + 4] = (byte) qiYong2[k];
            }
        }
        quanJu[1] = (byte) (maxTime % 256);
        quanJu[2] = (byte) (maxTime / 256);
        NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
        int count = 0;
        for (int i = 0; i < table.getRowCount(); i++) {
            if ((boolean) table.getValueAt(i, 0)) {
                count++;
            }
        }
        byte[][] shiJianPian = new byte[count * 2][81];//时间片设置
        byte[][] suCaiGuanLian = new byte[count * 2][40];//素材关联
        for (int i = 0; i < shiJianPian.length; i++) {
            shiJianPian[i][0] = (byte) timeBlockPanels[i].getComponentCount();
            for (int k = 0; k < timeBlockPanels[i].getComponentCount(); k++) {
                DefineJLable lable = (DefineJLable) timeBlockPanels[i].getComponent(k);
                int start = lable.getX() / 5;
                int end = (lable.getX() + lable.getWidth()) / 5;
                shiJianPian[i][(k * 4) + 1] = (byte) (start % 256);
                shiJianPian[i][(k * 4) + 2] = (byte) (start / 256);

                shiJianPian[i][(k * 4) + 3] = (byte) (end % 256);
                shiJianPian[i][(k * 4) + 4] = (byte) (end / 256);
            }
            //计算灯组id
            int dengZuId = Integer.valueOf(timeBlockPanels[i].getName());
            if (dengZuId % 2 == 0) {
                dengZuId = dengZuId / 2;
            } else {
                dengZuId = (dengZuId + 1) / 2;
            }
            for (int k = 0; k < timeBlockPanels[i].getComponentCount(); k++) {
                DefineJLable lable = (DefineJLable) timeBlockPanels[i].getComponent(k);
                String s = lable.getText().substring(lable.getText().indexOf("(") + 1, lable.getText().indexOf(")"));
                int integer = Integer.parseInt(s);
                if (lable.getName().equals("TonDao")) {
                    suCaiGuanLian[i][(k * 2)] = (byte) dengZuId;
                    suCaiGuanLian[i][(k * 2) + 1] = (byte) integer;
                } else {
                    suCaiGuanLian[i][(k * 2)] = (byte) 0xAC;
                    suCaiGuanLian[i][(k * 2) + 1] = (byte) integer;
                }
            }
        }

        try {
            os.write(guDingTou);//固定头
            os.write(quanJu);//效果灯全局设置
            os.write(luZhi);//录制启用/禁用
            os.write(beiYong);//备用
            for (int i = 0; i < changJingQiYong.length; i++) {//场景启用
                for (int k = 0; k < 4; k++) {
                    os.write(changJingQiYong[i][k]);
                }
                for (int k = 0; k < 4; k++) {
                    os.write(changJingQiYong[i][k + 4]);
                }
            }
            for (int i = 0; i < shiJianPian.length; i++) {//时间片设置
                os.write(shiJianPian[i]);
            }
            for (int i = 0; i < suCaiGuanLian.length; i++) {//素材关联
                os.write(suCaiGuanLian[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 时间块数据
     * sc场景号
     * objects 编辑数据
     * zdyObjects  自定义动作数据
     * gxObjects 场景勾选数据
     */
    private byte[][][] timeBlockData(int sc, Object[][] objects, Object[][] zdyObjects, Object[][] gxObjects) {
        HashMap hashMap = null;
        byte[][][] T1 = new byte[30][20][53];
        //Object[][] objects = new Object[30][20];
        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + sc);
        NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//所有灯具

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
                    //动作图形
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
                        //运行速度
                        int yunXinSpeed = Integer.valueOf((String) map.get("3"));
                        T1[i][j][1] = (byte) yunXinSpeed;
                        //使用开关 1启用/0关
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
                        //拆分    不拆分01/中间拆分02/两端拆分03
                        String[] tp1 = (String[]) map.get("4");
                        int cc = Integer.valueOf(tp1[0]) + 1;
                        T1[i][j][3] = (byte) cc;

                        //拆分反向
                        boolean b = map.containsKey("5") ? (boolean) map.get("5") : false;
                        //X轴反向    是1/否0
                        if ("true".equals(tp1[1])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][5] = (byte) a;
                        if (b) {
                            if (a == 1) {
                                T1[i][j][5] = (byte) 0x81;
                            } else {
                                T1[i][j][5] = (byte) 0x80;
                            }
                        }
                        //X半
                        if ("true".equals(tp1[2])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][6] = (byte) a;
                        //Y轴反向
                        if ("true".equals(tp1[3])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][7] = (byte) a;
                        //Y半
                        if ("true".equals(tp1[4])) {
                            a = 1;
                        } else {
                            a = 0;
                        }
                        T1[i][j][8] = (byte) a;


                        //时间A_L	时间B_H
                        a = Integer.valueOf(tp1[5]).intValue();
                        T1[i][j][9] = (byte) (a % 256);
                        T1[i][j][10] = (byte) (a / 256);

                        ///////////////自定义动作数据
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
                        //红色
                        tp2 = (String[]) list.get(1);
                        a = Integer.valueOf(tp2[0]).intValue();
                        T1[i][j][11] = (byte) a;
                        //绿色
                        a = Integer.valueOf(tp2[1]).intValue();
                        T1[i][j][12] = (byte) a;
                        //蓝色
                        a = Integer.valueOf(tp2[2]).intValue();
                        T1[i][j][13] = (byte) a;
                        //渐变类型
                        b = (String) list.get(4);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][14] = (byte) a;
                        //渐变
                        bb = (boolean) list.get(5);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][15] = (byte) a;
                        //参与渐变勾选
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
                        //渐变速度
                        b = (String) list.get(6);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][17] = (byte) a;
                        //使用开关
                        bb = (boolean) list.get(0);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][18] = (byte) a;
                        //拆分
                        b = (String) list.get(7);
                        a = Integer.valueOf(b).intValue() + 1;
                        T1[i][j][19] = (byte) a;

                        //拆分反向
                        bb = (boolean) list.get(8);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][20] = (byte) a;
                        //时间A_L	时间B_H
                        a = Integer.valueOf((String) list.get(9)).intValue();
                        T1[i][j][21] = (byte) (a % 256);
                        T1[i][j][22] = (byte) (a / 256);
                    }

                    //RGB2
                    list = (List) hashMap.get("rgb2Data");
                    if (list != null) {
                        //红色
                        tp2 = (String[]) list.get(1);
                        a = Integer.valueOf(tp2[0]).intValue();
                        T1[i][j][23] = (byte) a;
                        //绿色
                        a = Integer.valueOf(tp2[1]).intValue();
                        T1[i][j][24] = (byte) a;
                        //蓝色
                        a = Integer.valueOf(tp2[2]).intValue();
                        T1[i][j][25] = (byte) a;
                        //渐变类型
                        b = (String) list.get(4);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][26] = (byte) a;
                        //渐变
                        bb = (boolean) list.get(5);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][27] = (byte) a;
                        //参与渐变勾选
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
                        //渐变速度
                        b = (String) list.get(6);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][29] = (byte) a;
                        //使用开关
                        bb = (boolean) list.get(0);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][30] = (byte) a;
                        //拆分
                        b = (String) list.get(7);
                        a = Integer.valueOf(b).intValue() + 1;
                        T1[i][j][31] = (byte) a;

                        //拆分反向
                        bb = (boolean) list.get(8);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][32] = (byte) a;
                        //时间A_L	时间B_H
                        a = Integer.valueOf((String) list.get(9)).intValue();
                        T1[i][j][33] = (byte) (a % 256);
                        T1[i][j][34] = (byte) (a / 256);
                    }

                    //RGB3
                    list = (List) hashMap.get("rgb3Data");
                    if (list != null) {
                        //红色
                        tp2 = (String[]) list.get(1);
                        a = Integer.valueOf(tp2[0]).intValue();
                        T1[i][j][35] = (byte) a;
                        //绿色
                        a = Integer.valueOf(tp2[1]).intValue();
                        T1[i][j][36] = (byte) a;
                        //蓝色
                        a = Integer.valueOf(tp2[2]).intValue();
                        T1[i][j][37] = (byte) a;
                        //渐变类型
                        b = (String) list.get(4);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][38] = (byte) a;
                        //渐变
                        bb = (boolean) list.get(5);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][39] = (byte) a;
                        //参与渐变勾选
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
                        //渐变速度
                        b = (String) list.get(6);
                        a = Integer.valueOf(b).intValue();
                        T1[i][j][41] = (byte) a;
                        //使用开关
                        bb = (boolean) list.get(0);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][42] = (byte) a;
                        //拆分
                        b = (String) list.get(7);
                        a = Integer.valueOf(b).intValue() + 1;
                        T1[i][j][43] = (byte) a;

                        //拆分反向
                        bb = (boolean) list.get(8);
                        a = 0;
                        if (bb) {
                            a = 1;
                        }
                        T1[i][j][44] = (byte) a;
                        //时间A_L	时间B_H
                        a = Integer.valueOf((String) list.get(9)).intValue();
                        T1[i][j][45] = (byte) (a % 256);
                        T1[i][j][46] = (byte) (a / 256);
                    }


                    /////手动编程配置
                    List list66 = (List) hashMap.get("channelData");
                    Vector vector88 = null;
                    if (list66 != null) {
                        //////////勾选
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
                        //总帧数
                        T1[i][j][47] = (byte) a;
                        //手动编程启用
                        T1[i][j][48] = (byte) 1;

                        //时差A_L	时差B_H
                        a = Integer.valueOf(ddTemp[2]).intValue();
                        T1[i][j][49] = (byte) (a % 256);
                        T1[i][j][50] = (byte) (a / 256);
                        //拆分
                        a = Integer.valueOf(ddTemp[0]).intValue() + 1;
                        T1[i][j][51] = (byte) a;
                        //拆分反向
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
     * 场景模块 --雾机模式数据
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
        byte[][][] b1 = new byte[4][5][13];//启动
        byte[][][] b2 = new byte[4][5][13];//运行
        byte[][] b3 = new byte[4][7];//配置
        for (int i = 1; i < 5; i++) {
            allList = (List) Data.wuJiMap.get("" + i);
            if (allList != null) {
                Vector vector88 = (Vector) allList.get(0);//启动
                Vector vector99 = (Vector) allList.get(1);//运行
                String[] tkp = (String[]) allList.get(2);
                if (tkp[0].equals("true")) {//雾机状态
                    b3[i - 1][0] = 1;
                }
                //地址
                b3[i - 1][1] = (byte) (x / 256);
                b3[i - 1][2] = (byte) (x % 256);
                //通道数
                b3[i - 1][3] = (byte) y;

                if (tkp[2].equals("true")) {//运行模式
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

    /**
     * 文件写入倒彩喝彩摇麦数据
     *
     * @param os
     */
    private void writeHeCaiDaoCaiYaoMai(OutputStream os) throws IOException {

        ///摇麦
        Vector[] vectors = null;
        boolean[][] tbs = null;
        String[] setValue = null;
        List<Byte> bytes = new ArrayList<>();//摇麦 启动阶段、运行阶段 步编辑数据
        byte[][][] b3 = new byte[2][2][512];//通道勾选
        byte[][][] steps1 = new byte[2][2][1];//启用、运行 步数
        byte[][] set1 = new byte[2][7];//全局设置
        int a = 0, size8 = 0;
        Vector vector99 = null, tp = null;
        for (int n = 1; n < 3; n++) {
            size8 = 0;
            vectors = (Vector[]) Data.YaoMaiMap.get("TableData" + n);
            tbs = (boolean[][]) Data.YaoMaiMap.get("GouXuanValue" + n);
            if (vectors != null) {
                vector99 = vectors[0];//启动阶段
                if (vector99 != null) {
                    size8 = vector99.size();
                    steps1[n - 1][0][0] = (byte) size8;
                    for (int i = 0; i < size8; i++) {
                        tp = (Vector) vector99.get(i);
                        a = Integer.valueOf((String) (tp.get(1)));
                        bytes.add((byte) (a % 256));
                        bytes.add((byte) (a / 256));
                        for (int j = 2; j < 512; j++) {
                            a = Integer.valueOf((String) (tp.get(j)));
                            bytes.add((byte) a);
                        }
                    }
                }

                vector99 = vectors[1];//运行阶段
                if (vector99 != null) {
                    size8 = vector99.size();
                    steps1[n - 1][1][0] = (byte) size8;
                    for (int i = 0; i < size8; i++) {
                        tp = (Vector) vector99.get(i);
                        a = Integer.valueOf((String) (tp.get(1)));
                        bytes.add((byte) (a % 256));
                        bytes.add((byte) (a / 256));
                        for (int j = 2; j < 512; j++) {
                            a = Integer.valueOf((String) (tp.get(j)));
                            bytes.add((byte) a);
                        }
                    }
                }
            }
            //勾选
            if (tbs != null) {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 510; j++) {
                        if (tbs[i][j]) {
                            b3[n - 1][i][j + 2] = 1;
                        }
                    }
                }
            }
            //全局设置
            setValue = (String[]) Data.YaoMaiMap.get("YaoMaiSet" + n);
            set1[n - 1][0] = 1;//启用
            if (setValue != null) {
                if (Boolean.valueOf(setValue[1])) {
                    set1[n - 1][1] = 1;
                } else {
                    set1[n - 1][1] = 2;
                }
                set1[n - 1][2] = (byte) Integer.valueOf(setValue[2]).intValue();
            }
        }

        ///////////倒喝彩
        byte[][][] b3_2 = new byte[2][2][512];
        byte[][][] steps1_2 = new byte[2][2][1];//启用、运行 步数
        byte[][] set1_2 = new byte[2][7];//全局设置
        String[] s = {"效果灯倒彩模式", "效果灯喝彩模式"};
        for (int n = 1; n < 3; n++) {
            size8 = 0;
            vectors = (Vector[]) Data.DaoHeCaiMap.get("TableData" + s[n - 1]);
            tbs = (boolean[][]) Data.DaoHeCaiMap.get("GouXuanValue" + s[n - 1]);
            if (vectors != null) {
                vector99 = vectors[0];//启动阶段
                if (vector99 != null) {
                    size8 = vector99.size();
                    steps1_2[n - 1][0][0] = (byte) size8;
                    for (int i = 0; i < size8; i++) {
                        tp = (Vector) vector99.get(i);
                        a = Integer.valueOf((String) (tp.get(1)));
                        bytes.add((byte) (a % 256));
                        bytes.add((byte) (a / 256));
                        for (int j = 2; j < 512; j++) {
                            a = Integer.valueOf((String) (tp.get(j)));
                            bytes.add((byte) a);
                        }
                    }
                }

                vector99 = vectors[1];//运行阶段
                if (vector99 != null) {
                    size8 = vector99.size();
                    steps1_2[n - 1][1][0] = (byte) size8;
                    for (int i = 0; i < size8; i++) {
                        tp = (Vector) vector99.get(i);
                        a = Integer.valueOf((String) (tp.get(1)));
                        bytes.add((byte) (a % 256));
                        bytes.add((byte) (a / 256));
                        for (int j = 2; j < 512; j++) {
                            a = Integer.valueOf((String) (tp.get(j)));
                            bytes.add((byte) a);
                        }
                    }
                }
                //勾选
                if (tbs != null) {
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 510; j++) {
                            if (tbs[i][j]) {
                                b3_2[n - 1][i][j + 2] = 1;
                            }
                        }
                    }
                }
                //全局设置
                setValue = (String[]) Data.DaoHeCaiMap.get("DaoHeCaiSet" + s[n - 1]);
                set1_2[n - 1][0] = 1;//启用
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
        byte[] b = new byte[1];
        List<Byte> list = new ArrayList<>();
        list.add((byte) 0xFE);//引导码
        list.add((byte) 0xA0);
        for (int i = 0; i < 4; i++) {//添加备用地址
            list.add(b[0]);
        }
        //总步数
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                list.add(steps1[i][j][0]);
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                list.add(steps1_2[i][j][0]);
            }
        }
        //全局设置
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 7; j++) {
                list.add(set1[i][j]);
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 7; j++) {
                list.add(set1_2[i][j]);
            }
        }
        //通道勾选
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 512; k++) {
                    list.add(b3[i][j][k]);
                }
            }
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 512; k++) {
                    list.add(b3_2[i][j][k]);
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            os.write(list.get(i));
        }
        for (int i = 0; i < bytes.size(); i++) {
            os.write(bytes.get(i));
        }
    }

    /*
     * 场景模块--摇麦与倒喝彩模式数据   旧方法弃置2019-11-9 10:25:38
     */
    private void writeHeCaiYaoMai(OutputStream os) throws Exception {
        ///摇麦
        Vector[] vectors = null;
        boolean[][] tbs = null;
        String[] setValue = null;
        byte[][][] b1 = new byte[2][16][512];
        byte[][][] b2 = new byte[2][16][512];
        byte[][][] b3 = new byte[2][2][512];
        byte[][][] steps1 = new byte[2][2][1];//启用、运行 步数
        byte[][] set1 = new byte[2][7];//全局设置
        int a = 0, size8 = 0;
        Vector vector99 = null, tp = null;
        for (int n = 1; n < 3; n++) {
            size8 = 0;
            vectors = (Vector[]) Data.YaoMaiMap.get("TableData" + n);
            tbs = (boolean[][]) Data.YaoMaiMap.get("GouXuanValue" + n);
            if (vectors != null) {
                vector99 = vectors[0];//启动阶段
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

                vector99 = vectors[1];//运行阶段
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
            //勾选
            if (tbs != null) {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 510; j++) {
                        if (tbs[i][j]) {
                            b3[n - 1][i][j + 2] = 1;
                        }
                    }
                }
            }
            //全局设置
            setValue = (String[]) Data.YaoMaiMap.get("YaoMaiSet" + n);
            set1[n - 1][0] = 1;//启用
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
        ///////////倒喝彩
        byte[][][] b1_2 = new byte[2][16][512];
        byte[][][] b2_2 = new byte[2][16][512];
        byte[][][] b3_2 = new byte[2][2][512];
        byte[][][] steps1_2 = new byte[2][2][1];//启用、运行 步数
        byte[][] set1_2 = new byte[2][7];//全局设置
        String[] s = {"效果灯倒彩模式", "效果灯喝彩模式"};
        for (int n = 1; n < 3; n++) {
            size8 = 0;
            vectors = (Vector[]) Data.DaoHeCaiMap.get("TableData" + s[n - 1]);
            tbs = (boolean[][]) Data.DaoHeCaiMap.get("GouXuanValue" + s[n - 1]);
            if (vectors != null) {
                vector99 = vectors[0];//启动阶段
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

                vector99 = vectors[1];//运行阶段
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
                //勾选
                if (tbs != null) {
                    for (int i = 0; i < 2; i++) {
                        for (int j = 0; j < 510; j++) {
                            if (tbs[i][j]) {
                                b3_2[n - 1][i][j + 2] = 1;
                            }
                        }
                    }
                }
                //全局设置
                setValue = (String[]) Data.DaoHeCaiMap.get("DaoHeCaiSet" + s[n - 1]);
                set1_2[n - 1][0] = 1;//启用
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

    /**
     * 声控素材数据写入
     *
     * @param os
     * @throws Exception
     */
    private void writeShengKonSucai(OutputStream os) throws Exception {
        byte[][] yinDaoA = new byte[30][6];//引导区A
        NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
        for (int i = 0; i < 30; i++) {
            yinDaoA[i][0] = (byte) 0xFE;
            yinDaoA[i][1] = (byte) 0xB0;
            yinDaoA[i][4] = (byte) 0x01;
        }
        ShengKonSuCaiUI suCaiUI = new ShengKonSuCaiUI();
        for (int i = 0; i < table.getRowCount(); i++) {
            String dengZuName = table.getValueAt(i, 2).toString();//灯组名称
            int dengZuNumber = Integer.parseInt(table.getValueAt(i, 1).toString());//灯组序号
            int number = Integer.parseInt(suCaiUI.getAlone(dengZuName));//当前灯组素材总数
            int tonDaoNumber = 0;
            TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(dengZuNumber - 1);
            if (treeSet.size() > 0) {
                NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//所有灯具
                String typeString = table3.getValueAt((int) treeSet.first(), 3).toString();//灯具型号
                tonDaoNumber = Integer.valueOf((String) Data.DengKuChannelCountList.get(Integer.valueOf(typeString.split("#")[0].substring(2)).intValue() - 1)).intValue();//通道数
            }
            yinDaoA[i][2] = (byte) number;
            yinDaoA[i][3] = (byte) tonDaoNumber;
        }
        List<Byte> yinDaoB = new ArrayList<>();//引导区B
        yinDaoB.add((byte) 0XFE);
        yinDaoB.add((byte) 0XB1);
        yinDaoB.add((byte) table.getRowCount());//编程组数
        yinDaoB.add((byte) (Integer.parseInt(suCaiUI.getCount()) % 256));//素材总数
        yinDaoB.add((byte) (Integer.parseInt(suCaiUI.getCount()) / 256));//素材总数
        for (int i = 0; i < table.getRowCount(); i++) {//各素材总步数
            String dengZuName = table.getValueAt(i, 2).toString();//灯组名称
            int number = Integer.parseInt(suCaiUI.getAlone(dengZuName));//当前灯组素材总数
            for (int j = 0; j < number; j++) {
                if (Data.ShengKonSuCai[i][j] != null) {
                    Map map = (Map) Data.ShengKonSuCai[i][j];
                    Vector vector88 = (Vector) map.get("0");
                    if (vector88 != null)
                        yinDaoB.add((byte) vector88.size());
                    else
                        yinDaoB.add((byte) 0X01);//默认一步
                } else {
                    yinDaoB.add((byte) 0X01);//默认一步
                }
            }
        }
        List<Byte> shengKonSuCaiBanBen = new ArrayList<>();//声音素材版本格式
        for (int i = 0; i < table.getRowCount(); i++) {
            String dengZuName = table.getValueAt(i, 2).toString();//灯组名称
            int number = Integer.parseInt(suCaiUI.getAlone(dengZuName));//当前灯组素材总数
            for (int j = 0; j < number; j++) {
                byte[] b2 = new byte[2];
                TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(i);
                int cnt = treeSet.size();//灯具数量
                byte[] b4 = new byte[cnt * 4];//通道勾选
                byte[] b5 = new byte[27];
                if (Data.ShengKonSuCai[i][j] != null) {
                    Map map = (Map) Data.ShengKonSuCai[i][j];
                    int size = 0;
                    boolean[][] gouXuanValus = null;
                    int j2, yu2;
                    int[] al;
                    Vector vector88 = null;
                    if (map != null) {
                        vector88 = (Vector) map.get("0");
                        if (vector88 != null) {
                            size = vector88.size();
                            b2[0] = (byte) (size % 256);
                            b2[1] = (byte) (size / 256);
                        }
                        gouXuanValus = (boolean[][]) map.get("1");
                        if (gouXuanValus != null) {
                            for (int k2 = 0; k2 < gouXuanValus.length; k2++) {
                                for (int l = 0; l < gouXuanValus[0].length; l++) {
                                    j2 = l / 8;
                                    yu2 = 7 - (l % 8);
                                    if (gouXuanValus[k2][l]) {
//                                        b2[2 + k2 * 4 + j2] = (byte) (Byte.toUnsignedInt(b2[2 + k2 * 4 + j2]) + (1 << yu2));
                                        b4[k2 * 4 + j2] = (byte) (Byte.toUnsignedInt(b4[k2 * 4 + j2]) + (1 << yu2));
                                    }
                                }
                            }
                        }
                        al = (int[]) map.get("2");
                        if (al != null) {
                            if (al[0] == 0) {//0表示独立
//                                b2[38 + 32] = 2;
                                b5[4] = 2;
                            } else {
//                                b2[38 + 32] = 1;
                                b5[4] = 1;
                            }
                            //多灯控制
//                            b2[44 + 32] = (byte) al[10];
                            b5[10] = (byte) al[10];
                            //加速度
                            if (al[1] == 0) {
//                                b2[46 + 32] = 1;
                                b5[12] = 1;
                            }
                            if (al[2] == 1) {
//                                b2[48 + 32] = 1;
                                b5[14] = 1;
                            }
//                            b2[49 + 32] = (byte) al[3];
                            b5[15] = (byte) al[3];

//                            b2[51 + 32] = (byte) al[4];
//                            b2[52 + 32] = (byte) al[6];
//                            b2[53 + 32] = (byte) al[8];
//
//                            b2[54 + 32] = (byte) al[5];
//                            b2[55 + 32] = (byte) al[7];
//                            b2[56 + 32] = (byte) al[9];
                            b5[17] = (byte) al[4];
                            b5[18] = (byte) al[6];
                            b5[19] = (byte) al[8];
                            b5[20] = (byte) al[5];
                            b5[21] = (byte) al[7];
                            b5[22] = (byte) al[9];
                        }
                    }
                    for (int k = 0; k < b2.length; k++) {//素材版本
                        shengKonSuCaiBanBen.add(b2[k]);
                    }
                    for (int k = 0; k < b4.length; k++) {
                        shengKonSuCaiBanBen.add(b4[k]);
                    }
                    for (int k = 0; k < b5.length; k++) {
                        shengKonSuCaiBanBen.add(b5[k]);
                    }
                    //步数据
                    int channelCount = 0;//通道数
                    String typeString = "";
                    if (Data.GroupOfLightList.size() > i) {
                        if (cnt > 0) {
                            NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//所有灯具
                            int v = (int) treeSet.first();
                            typeString = table3.getValueAt(v, 3).toString();//灯具型号
                        }
                    }
                    if (!typeString.equals("")) {
                        int dengKuNumber = Integer.valueOf(typeString.split("#")[0].substring(2)).intValue() - 1;
                        channelCount = Integer.valueOf((String) Data.DengKuChannelCountList.get(dengKuNumber)).intValue();
                    }
                    int stepWidth = channelCount * cnt;
                    int runTime = 0, lgth = 0;
                    if (vector88 != null) {
                        Vector tp = null;
                        for (int r = 0; r < vector88.size(); r++) {
                            tp = (Vector) vector88.get(r);
                            runTime = Integer.valueOf(tp.get(1).toString());
                            shengKonSuCaiBanBen.add((byte) (runTime % 256));
                            shengKonSuCaiBanBen.add((byte) (runTime / 256));
                            if (tp.size() - 2 >= stepWidth) {
                                lgth = stepWidth + 2;
                            } else {
                                lgth = tp.size();
                            }
                            for (int l = 2; l < lgth; l++) {
                                shengKonSuCaiBanBen.add(Integer.valueOf(tp.get(l).toString()).byteValue());
                            }
                            if (tp.size() - 2 < stepWidth) {
                                byte[] bytes = new byte[stepWidth - (tp.size() - 2)];
                                for (int k = 0; k < bytes.length; k++) {
                                    shengKonSuCaiBanBen.add(bytes[k]);
                                }
                            }
                        }
                        vector88 = null;
                    }
                } else {
                    for (int k = 0; k < b2.length; k++) {//素材版本
                        shengKonSuCaiBanBen.add(b2[k]);
                    }
                    for (int k = 0; k < b4.length; k++) {
                        shengKonSuCaiBanBen.add(b4[k]);
                    }
                    for (int k = 0; k < b5.length; k++) {
                        shengKonSuCaiBanBen.add(b5[k]);
                    }
                    //步数据
                    int channelCount = 0;//通道数
                    String typeString = "";
                    if (Data.GroupOfLightList.size() > i) {
                        if (cnt > 0) {
                            NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//所有灯具
                            int v = (int) treeSet.first();
                            typeString = table3.getValueAt(v, 3).toString();//灯具型号
                        }
                    }
                    if (!typeString.equals("")) {
                        int dengKuNumber = Integer.valueOf(typeString.split("#")[0].substring(2)).intValue() - 1;
                        channelCount = Integer.valueOf((String) Data.DengKuChannelCountList.get(dengKuNumber)).intValue();
                    }
                    int stepWidth = channelCount * cnt;
                    byte[] b3 = new byte[stepWidth + 2];
                    for (int k = 0; k < b3.length; k++) {
                        shengKonSuCaiBanBen.add(b3[k]);
                    }
                }
            }
        }
        for (int i = 0; i < yinDaoA.length; i++) {//引导区A写入
            os.write(yinDaoA[i]);
        }
        for (int i = 0; i < yinDaoB.size(); i++) {//引导区B写入
            os.write(yinDaoB.get(i));
        }
        for (int i = 0; i < shengKonSuCaiBanBen.size(); i++) {//声控素材版本以及步数写入
            os.write(shengKonSuCaiBanBen.get(i));
        }
    }

    private void writeShengKon(OutputStream os, int sc) throws Exception {

        ShengKonData(sc, os);

        //shengKonMoreLigthData(os);
    }

    /*
     * 声控时间块数据
     */
    private void ShengKonData(int scN, OutputStream os) throws Exception {
        byte[] b1 = new byte[1374];
        b1[0] = 0x55;
        b1[1] = (byte) 0xAA;
        b1[2] = 0x00;
        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels" + scN);
        int j = 0, yu = 0;
        short[][] a = new short[10][4];//场景启用
        for (int i = 0; i < 10; i++) {//块
            for (int k = 1; k < 31; k++) {//组别
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
        //全局设置
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

        //低频持续组  [2][67 + 32];
        int[][] DataSetInts = null;
        for (int i = 0; i < 10; i++) {
            DataSetInts = (int[][]) Data.ShengKonShiXuSetObjects[scN - 1][i];
            if (DataSetInts != null) {
                ////////////低频持续组
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
                ///////低频灭灯模式
                if (DataSetInts[0][0] == 0) {
                    b1[204 + i] = (byte) (16);
                } else if (DataSetInts[0][0] == 1) {
                    b1[204 + i] = (byte) (32 + DataSetInts[0][1] + 1);
                }
                //////低频声控顺序设置
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
                        if (sl + 1 == DataSetInts[0][5 + k2 + k * 6]) {
                            b1[255 + 60 * i + k * 6 + k2] = (byte) 31;
                        } else if (sl + 2 == DataSetInts[0][5 + k2 + k * 6]) {
                            b1[255 + 60 * i + k * 6 + k2] = (byte) 32;
                        } else if (sl >= DataSetInts[0][5 + k2 + k * 6]) {
                            b1[255 + 60 * i + k * 6 + k2] = (byte) DataSetInts[0][5 + k2 + k * 6];
                        }
                    }
                }
                ///////////////////高频持续组
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
                ///////////高频灭灯模式
                if (DataSetInts[1][0] == 0) {
                    b1[854 + i] = (byte) (16);
                } else if (DataSetInts[1][0] == 1) {
                    b1[854 + i] = (byte) (32 + DataSetInts[1][1] + 1);
                }
                //////////////////
                ////////////////高频声控顺序设置
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
        //时间片设置
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

        //块数据  改素材调用，暂时注释
//        byte[][][] b2 = new byte[30][10][61];
//        Map map = null;
//        Vector vector88 = null;
//        int size = 0;
//        boolean[][] gouXuanValus = null;
//        int j2, yu2;
//        int[] al;
//        for (int i = 0; i < 30; i++) {
//            for (int k = 0; k < 10; k++) {
//                map = (Map) Data.ShengKonEditObjects[scN - 1][i][k];
//                if (map != null) {
//                    vector88 = (Vector) map.get("0");
//                    if (vector88 != null) {
//                        size = vector88.size();
//                        b2[i][k][0] = (byte) (size % 256);
//                        b2[i][k][1] = (byte) (size / 256);
//                    }
//                    gouXuanValus = (boolean[][]) map.get("1");
//                    if (gouXuanValus != null) {
//                        for (int k2 = 0; k2 < gouXuanValus.length; k2++) {
//                            for (int l = 0; l < gouXuanValus[0].length; l++) {
//                                j2 = l / 8;
//                                yu2 = 7 - (l % 8);
//                                if (gouXuanValus[k2][l]) {
//                                    b2[i][k][2 + k2 * 4 + j2] = (byte) (Byte.toUnsignedInt(b2[i][k][2 + k2 * 4 + j2]) + (1 << yu2));
//                                }
//                            }
//                        }
//                    }
//                    al = (int[]) map.get("2");
//                    if (al != null) {
//                        if (al[0] == 0) {//0表示独立
//                            b2[i][k][38] = 2;
//                        } else {
//                            b2[i][k][38] = 1;
//                        }
//                        //多灯控制
//                        b2[i][k][44] = (byte) al[10];
//                        //加速度
//                        if (al[1] == 0) {
//                            b2[i][k][46] = 1;
//                        }
//                        if (al[2] == 1) {
//                            b2[i][k][48] = 1;
//                        }
//                        b2[i][k][49] = (byte) al[3];
//
//                        b2[i][k][51] = (byte) al[4];
//                        b2[i][k][52] = (byte) al[6];
//                        b2[i][k][53] = (byte) al[8];
//
//                        b2[i][k][54] = (byte) al[5];
//                        b2[i][k][55] = (byte) al[7];
//                        b2[i][k][56] = (byte) al[9];
//                    }
//                }
//
//                //步数据
//                int cnt = 0;//灯具数量
//                int channelCount = 0;//通道数
//                String typeString = "";
//                //int number = Integer.valueOf(""+(i+1)).intValue();
//                if (Data.GroupOfLightList.size() > i) {
//                    TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(i);
//                    cnt = treeSet.size();
//                    if (cnt > 0) {
//                        NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//所有灯具
//                        int v = (int) treeSet.first();
//                        typeString = table3.getValueAt(v, 3).toString();//灯具型号
//                    }
//                }
//                if (!typeString.equals("")) {
//                    int dengKuNumber = Integer.valueOf(typeString.split("#")[0].substring(2)).intValue() - 1;
//                    channelCount = Integer.valueOf((String) Data.DengKuChannelCountList.get(dengKuNumber)).intValue();
//                }
//                int stepWidth = channelCount * cnt;
//                int runTime = 0, lgth = 0;
//                byte[][] b3 = new byte[32][stepWidth + 2];
//                if (vector88 != null) {
//                    Vector tp = null;
//                    for (int r = 0; r < vector88.size(); r++) {
//                        tp = (Vector) vector88.get(r);
//                        runTime = Integer.valueOf(tp.get(1).toString());
//                        b3[r][0] = (byte) (runTime % 256);
//                        b3[r][1] = (byte) (runTime / 256);
//                        if (tp.size() - 2 >= stepWidth) {
//                            lgth = stepWidth + 2;
//                        } else {
//                            lgth = tp.size();
//                        }
//                        for (int l = 2; l < lgth; l++) {
//                            b3[r][l] = Integer.valueOf(tp.get(l).toString()).byteValue();
//                        }
//                    }
//                    vector88 = null;
//                }
//                os.write(b2[i][k]);
//                for (int l = 0; l < 32; l++) {
//                    os.write(b3[l]);
//                }
//            }
//        }

        byte[][][] buff = new byte[30][10][2];//场景声控块区数据
        for (int i = 0; i < 30; i++) {
            if (timeBlockPanels[i + 1].isVisible()) {
                for (int k = 0; k < timeBlockPanels[i + 1].getComponentCount(); k++) {
                    DefineJLable_shengKon2 lable = (DefineJLable_shengKon2) timeBlockPanels[i + 1].getComponent(k);
                    if (lable.getBackground().getGreen() == 255) {
                        buff[i][k][0] = (byte) (i + 1);
                        String str = lable.getText().substring(lable.getText().indexOf("(") + 1, lable.getText().indexOf(")"));
                        buff[i][k][1] = (byte) Integer.parseInt(str);
                    }
                }
            }
        }
        for (int i = 0; i < 30; i++) {
            for (int k = 0; k < 10; k++) {
                os.write(buff[i][k]);
            }
        }
    }

    private void shengKonMoreLigthDatatest(OutputStream os) throws Exception {

        int size = 0, a = 0;
        byte[] steps = new byte[38];//总步数
        steps[0] = (byte) 0XFE;
        steps[1] = (byte) 0XA2;

        Vector tp = null, vector99 = null;
        byte[][][][] b1 = new byte[16][2][32][512];//步编辑数据 new byte[16][2][32][512]

        //List<List<List<List<Byte>>>> b1 = new ArrayList<>();
        String[] sets = null;
        boolean[] tbs = null;
        byte[][][] channalGouXuan = new byte[16][2][64];//通道勾选
        byte[][][] globalSet = new byte[16][2][7];//全局设置
        int r = 0, yu = 0;
        for (int scN = 1; scN < 17; scN++) {
            for (int setNo = 1; setNo < 3; setNo++) {
                vector99 = (Vector) Data.ShengKonModelDmxMap.get("TableData" + scN + "" + setNo);
                sets = (String[]) Data.ShengKonModelDmxMap.get("YaoMaiSet" + scN + "" + setNo);
                tbs = (boolean[]) Data.ShengKonModelDmxMap.get("GouXuanValue" + scN + "" + setNo);//勾选
                if (vector99 != null) {
                    size = vector99.size();
                    steps[(scN - 1) * 2 + setNo + 5] = (byte) size;//个数
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

        //总步数
        for (int i = 0; i < 38; i++) {

            os.write(steps[i]);

        }

        //全局设置
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 2; j++) {
                os.write(globalSet[i][j]);
            }
        }

        //勾选数据
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 2; j++) {
                os.write(channalGouXuan[i][j]);
            }
        }


        //步编辑数据
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 2; j++) {
                for (int j2 = 0; j2 < (int) steps[i * 2 + j + 6]; j2++) {
                    os.write(b1[i][j][j2]);
                }
            }
        }
    }

    /*
     * 声控多灯模式数据
     */
    private void shengKonMoreLigthData(OutputStream os) throws Exception {
        int size = 0, a = 0;
        byte[][][] steps = new byte[16][2][1];//总步数
        Vector tp = null, vector99 = null;
        byte[][][][] b1 = new byte[16][2][32][512];//步编辑数据
        String[] sets = null;
        boolean[] tbs = null;
        byte[][][] channalGouXuan = new byte[16][2][64];//通道勾选
        byte[][][] globalSet = new byte[16][2][7];//全局设置
        int r = 0, yu = 0;
        for (int scN = 1; scN < 17; scN++) {
            for (int setNo = 1; setNo < 3; setNo++) {
                vector99 = (Vector) Data.ShengKonModelDmxMap.get("TableData" + scN + "" + setNo);
                sets = (String[]) Data.ShengKonModelDmxMap.get("YaoMaiSet" + scN + "" + setNo);
                tbs = (boolean[]) Data.ShengKonModelDmxMap.get("GouXuanValue" + scN + "" + setNo);//勾选
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
    private void shengKonEnvironmentData(OutputStream os) throws Exception {
        byte[][][] b1 = new byte[18][20][41];
        Map map;
        List list;
        int[] tp;
        int time = 0;
        String s = null;
        boolean flag = false;
        for (int i = 1; i < 19; i++) {
            list = null;
            s = null;
            map = (Map) Data.ShengKonHuanJingModelMap.get("" + i);
            if (map != null) {
                list = (List) map.get("0");
                s = (String) map.get("3");
                flag = (boolean) map.get("4");
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
                if (!flag) {
                    b1[i - 1][j][32] = (byte) 0X80;
                } else {
                    if (s != null) {
                        if ("0".equals(s)) {
                            b1[i - 1][j][32] = (byte) 1;
                        } else if ("1".equals(s)) {
                            b1[i - 1][j][32] = (byte) 2;
                        } else if ("2".equals(s)) {
                            b1[i - 1][j][32] = (byte) 0;
                        }
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
     * 12点坐标
     */
    private void actionTuXing(OutputStream os) throws Exception {
        //颜色图形
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
        //动作图形
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
     * 系统配置数据
     */
    private void systemSet(OutputStream os) throws Exception {
        new DataActionListener().actionPerformed(os);
    }
}
