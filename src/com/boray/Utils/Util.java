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
}
