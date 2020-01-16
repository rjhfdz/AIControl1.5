package com.boray.Utils;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.IOException;

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
}
