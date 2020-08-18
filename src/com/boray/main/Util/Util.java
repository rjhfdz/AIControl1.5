package com.boray.main.Util;

import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Util {

    /**
     * 主页通用下载方法
     * @param path  文件存放地址
     * @param fileName  文件名称
     * @param flag  是否弹出选择框
     * @param fileSuffix    文件后缀
     * @param downloadAddress   文件下载地址
     */
    public static void downloadFile(String path, String fileName, Boolean flag, String fileSuffix, String downloadAddress) {
        File file = null;
        if (flag) {//是否弹出选择文件存放位置
            JFileChooser fileChooser = new JFileChooser();
            if (!"".equals(Data.projectFilePath)) {
                fileChooser.setCurrentDirectory(new File(Data.projectFilePath));
            }
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setSelectedFile(new File(fileName + fileSuffix));
            int returnVal = fileChooser.showSaveDialog((JFrame) MainUi.map.get("frame"));
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fileChooser.getSelectedFile();
                Data.projectFilePath = file.getParent();
            }
        } else {//固定位置存放文件
            file = new File(path + fileName + fileSuffix);
            if (!new File(path).exists()) {//判断文件夹是否存在
                new File(path).mkdirs();
            }
        }
        if (file != null) {
            try {
                URL url = new URL(HttpClientUtil.URLEncode(downloadAddress));
                HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
                urlCon.setConnectTimeout(6000);
                urlCon.setReadTimeout(6000);
                DataInputStream in = new DataInputStream(urlCon.getInputStream());
                DataOutputStream out = new DataOutputStream(new FileOutputStream(file.getAbsoluteFile()));
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
