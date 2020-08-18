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
     * ��ҳͨ�����ط���
     * @param path  �ļ���ŵ�ַ
     * @param fileName  �ļ�����
     * @param flag  �Ƿ񵯳�ѡ���
     * @param fileSuffix    �ļ���׺
     * @param downloadAddress   �ļ����ص�ַ
     */
    public static void downloadFile(String path, String fileName, Boolean flag, String fileSuffix, String downloadAddress) {
        File file = null;
        if (flag) {//�Ƿ񵯳�ѡ���ļ����λ��
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
        } else {//�̶�λ�ô���ļ�
            file = new File(path + fileName + fileSuffix);
            if (!new File(path).exists()) {//�ж��ļ����Ƿ����
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
