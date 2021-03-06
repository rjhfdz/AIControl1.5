package com.boray.Utils;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.entity.Users;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.Timer;

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
                JButton dataWrite = (JButton) MainUi.map.get(Data.DataWriteBtnName);
                if (Data.sendDataCount >= 3) {
                    JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "数据写入失败，请检查设备或接口。", "提示", JOptionPane.ERROR_MESSAGE);
                    Data.sendDataCount = 0;
                    dataWrite.setText("写入控制器");
                    dataWrite.setEnabled(true);
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
        }, 3000, 2000);
    }

    /**
     * 定时任务，每隔十秒执行一次检测当前用户是否在线
     */
    public static void checkUserState() {
        if (Data.checkUserState != null) {
            Data.checkUserState = null;
        }
        Data.checkUserState = new Timer();
        Data.checkUserState.schedule(new TimerTask() {
            @Override
            public void run() {
                JLabel statusbar = (JLabel) MainUi.map.get("statusbar");//状态栏
                Users users = (Users) MainUi.map.get("Users");
                if (users != null) {
                    Map<String, String> param = new HashMap<>();
                    param.put("logincode", users.getUsercode());
                    param.put("password", users.getUserpassword());
                    try {
                        String request = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/login", param);
                        Users user = JSON.parseObject(request, Users.class);
                        if (user != null && !users.getCode().equals(-1)) {
                            user.setUserpassword(users.getUserpassword());
                            MainUi.map.put("Users", user);
                            statusbar.setText(" 当前登录用户：" + user.getUsercode() + " ！");
                        }
                    } catch (Exception e) {
                        statusbar.setText(" 网络错误，用户离线中 ！");
                    }
                } else {
                    statusbar.setText(" 当前暂无用户登录 ！");
                }
            }
        }, 0, 10000);//立即执行，之后该任务每间十秒执行一次
    }

    /**
     * 检查用户登录状态
     */
    public static void checkUserLogin() {

    }

    public static void stopAutoSaveFile() {
        JLabel editLabel = (JLabel) MainUi.map.get("CompanyEditLabel");
        if (editLabel != null) {
            editLabel.setText("");
            Data.tempWebFolder = null;
            Data.tempWebFile = null;
            Data.tempEditWebFile = null;
            Data.tempFileAutoSaveTimer = null;
        }
    }

    public static boolean checkRepetition(int startA, int startB, int endA, int endB) {
        if (Math.max(startA, startB) <= Math.min(endA, endB)) {
            if (startA == endB || startB == endA) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    /**
     * 素材复制
     *
     * @param o
     * @return
     */
    public static Object Clone(Object o) {
        Object newObj = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream obs = new ObjectOutputStream(out);
            obs.writeObject(o);

            ByteArrayInputStream ios = new ByteArrayInputStream(out.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(ios);

            newObj = ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newObj;
    }


    /**
     * 设备RAM重装指令
     */
    public static void RAMReset() {
        byte[] bytes = new byte[20];
        bytes[0] = (byte) 0XFA;
        bytes[1] = (byte) 0X14;
        bytes[2] = (byte) 0X64;
        bytes[3] = ZhiLingJi.TYPE;
        bytes[4] = (byte) 0X10;
        bytes[5] = (byte) 0X00;
        bytes[6] = (byte) 0X55;
        bytes[7] = (byte) 0XAA;
        bytes[19] = ZhiLingJi.getJiaoYan(bytes);
        Socket.SendData(bytes);
    }

}
