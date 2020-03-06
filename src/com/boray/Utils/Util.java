package com.boray.Utils;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class Util {
    static DefaultMutableTreeNode temp;

    public static DefaultMutableTreeNode traverseFolder(File file) {
        DefaultMutableTreeNode fujiedian = new DefaultMutableTreeNode(file);
//        File file = new File(path);

        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                if (file.isDirectory()) {//����ǿ��ļ���
                    DefaultMutableTreeNode dn = new DefaultMutableTreeNode(file, false);
                    return dn;
                }
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        //��Ŀ¼�Ļ������ɽڵ㣬���������Ľڵ�
                        fujiedian.add(traverseFolder(file2));
                    } else {
                        //���ļ��Ļ�ֱ�����ɽڵ㣬���Ѹýڵ�ӵ���Ӧ���ڵ���
                        temp = new DefaultMutableTreeNode(file2);
                        fujiedian.add(temp);
                    }
                }
            }
        } else {//�ļ�������
            return null;
        }
        return fujiedian;

    }

    //����
    public static String encode(String str) {
        return new sun.misc.BASE64Encoder().encode(str.getBytes());
    }

    //����
    public static String decode(String s) {
        String str = null;
        try {
            sun.misc.BASE64Decoder decode = new sun.misc.BASE64Decoder();
            str = new String(decode.decodeBuffer(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }


    //web ������ʱ�ļ� ���� �Զ�����
    public static boolean downloadTemp() {
        boolean flag = true;
        try {
            String path = System.getProperty("java.io.tmpdir");
            File selectedFile = new File(path + "//" + Data.tempWebFile.getGcname() + ".xml");
            URL url = new URL(HttpClientUtil.URLEncode(Data.downloadIp + Data.tempWebFile.getGcurl()));
            HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
            urlCon.setConnectTimeout(6000);
            urlCon.setReadTimeout(6000);
            int code = urlCon.getResponseCode();
            if (code != HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "�����༭ʧ�ܣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return false;
            }
            DataInputStream in = new DataInputStream(urlCon.getInputStream());
            DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile.getAbsoluteFile()));
            byte[] buffer = new byte[2048];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            Data.tempEditWebFile = selectedFile;
        } catch (Exception e) {
            JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "�����༭ʧ�ܣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
            flag = false;
        }
        return flag;
    }

    public static void againSendData() {
        Data.againSendDataTimer = new Timer();
        Data.againSendDataTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                JButton dataWrite = (JButton) MainUi.map.get("comAndWifiDataWrite");
                if (Data.sendDataCount > 3) {
                    Data.sendDataCount = 0;
                    dataWrite.setText("д�������");
                    Data.againSendDataTimer.cancel();
                } else {
                    if (Data.serialPort != null) {
                        Socket.SerialPortSendData((byte[]) Data.dataWrite[Data.sendDataSum]);
                        dataWrite.setText("�ط�" + (Data.sendDataCount + 1) + ":" + (Data.sendDataSum + 1) + "/" + Data.dataWrite.length);
                        Data.sendDataCount += 1;
                    } else if (Data.socket != null) {
                        Socket.UDPSendData((byte[]) Data.dataWrite[Data.sendDataSum]);
                        dataWrite.setText("�ط�" + (Data.sendDataCount + 1) + ":" + (Data.sendDataSum + 1) + "/" + Data.dataWrite.length);
                        Data.sendDataCount += 1;
                    }
                }
            }
        }, 3000, 3000);
    }

}
