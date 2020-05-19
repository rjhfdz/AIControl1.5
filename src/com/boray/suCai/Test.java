package com.boray.suCai;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boray.Utils.HttpClientUtil;

public abstract class Test {

    public static void main(String[] args) throws IOException  {

        //创建发送端Socket对象
//        DatagramSocket ds = new DatagramSocket();
//
//        //创建数据并打包
//        String s = "A001A908020104FE";    //要发送的数据
//
//        byte[] bys = test1();  //将数据放在字节数组中
//        int length = bys.length;    //字节数组的长度
//        InetAddress address = InetAddress.getByName("128.8.3.101"); //目的地址
//        int port = 8089;  //设置端口号
//
//        //打包
//        DatagramPacket dp = new DatagramPacket(bys,length,address,port);
//
//        //发送数据
//        ds.send(dp);
//
//        //释放资源
//        ds.close();
        System.out.println(6%2);
    }

    public static byte[] test1(){
        //A0 01 A9 08 02 02 01 FE
        byte[] b = new byte[8];
        int[] a = new int[8];
        a[0] = 160;
        a[1] = 1;
        a[2] = 169;
        a[3] = 8;
        a[4] = 2;
        a[5] = 4;
        a[6] = 1;
        a[7] = 254;
        for (int i = 0; i < a.length; i++) {
            b[i] = (byte)a[i];
        }
        return b;
    }


    public static void test() {
        List<String> str = new ArrayList<>();
        str.add("123");
        str.add("321");
        str.add("435");
        str.add("derg");
        if (!str.contains("456")) {//判断是否存在
            str.add("456");
        }
        for (int i = 0; i < str.size(); i++) {
            System.out.println(str.get(i));
        }

//        HttpClientUtil httpsUtils = new HttpClientUtil();
//        //要上传的文件的路径
//        String filePath = "E:\\statr.txt";
//        String postUrl = "http://localhost:8778/FileUploadServletgc";
//        Map<String, String> postParam = new HashMap<String, String>();
//        postParam.put("xmid", "11122");
//        postParam.put("gcname", "aa.txt");
//        postParam.put("username", "system");
//        postParam.put("i", "0");
//        File postFile = new File(filePath);
//        Map<String, Object> resultMap = httpsUtils.uploadFileByHTTP(postFile, postUrl, postParam);
//        System.out.println(resultMap);
//        String folder=System.getProperty("java.io.tmpdir");
//        System.out.println(folder);
//        System.out.println(System.getProperty("user.dir"));
//        System.out.println((10-15)/2);
    }

}
