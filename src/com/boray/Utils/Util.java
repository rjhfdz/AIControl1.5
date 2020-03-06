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
                if (file.isDirectory()) {//如果是空文件夹
                    DefaultMutableTreeNode dn = new DefaultMutableTreeNode(file, false);
                    return dn;
                }
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        //是目录的话，生成节点，并添加里面的节点
                        fujiedian.add(traverseFolder(file2));
                    } else {
                        //是文件的话直接生成节点，并把该节点加到对应父节点上
                        temp = new DefaultMutableTreeNode(file2);
                        fujiedian.add(temp);
                    }
                }
            }
        } else {//文件不存在
            return null;
        }
        return fujiedian;

    }

    //加密
    public static String encode(String str) {
        return new sun.misc.BASE64Encoder().encode(str.getBytes());
    }

    //解密
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


    //web 下载临时文件 用作 自动保存
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
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "开启编辑失败！", "提示", JOptionPane.PLAIN_MESSAGE);
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
            JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "开启编辑失败！", "提示", JOptionPane.PLAIN_MESSAGE);
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
                    dataWrite.setText("写入控制器");
                    Data.againSendDataTimer.cancel();
                } else {
                    if (Data.serialPort != null) {
                        Socket.SerialPortSendData((byte[]) Data.dataWrite[Data.sendDataSum]);
                        dataWrite.setText("重发" + (Data.sendDataCount + 1) + ":" + (Data.sendDataSum + 1) + "/" + Data.dataWrite.length);
                        Data.sendDataCount += 1;
                    } else if (Data.socket != null) {
                        Socket.UDPSendData((byte[]) Data.dataWrite[Data.sendDataSum]);
                        dataWrite.setText("重发" + (Data.sendDataCount + 1) + ":" + (Data.sendDataSum + 1) + "/" + Data.dataWrite.length);
                        Data.sendDataCount += 1;
                    }
                }
            }
        }, 3000, 3000);
    }

}
