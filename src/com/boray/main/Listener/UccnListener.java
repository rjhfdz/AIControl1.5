package com.boray.main.Listener;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.ProjectFile;
import com.boray.main.Util.CustomTreeCellRenderer;
import com.boray.main.Util.CustomTreeNode;
import com.boray.main.Util.TreeUtil;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class UccnListener implements ActionListener {

    private JFrame frame;
    private JTree tree;

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        frame = (JFrame) MainUi.map.get("frame");
        tree = (JTree) MainUi.map.get("uccnTree");
        if (button.getText().equals("下载工程")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "请选择工程！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof ProjectFile) {
                ProjectFile file = (ProjectFile) node.getUserObject();
                CustomTreeNode folder = (CustomTreeNode) node.getParent();
                String str = (String) folder.getUserObject();
                String path = getPath(str);
                File file1 = new File(path);
                if (!file1.exists()) {//判断文件夹是否存在
                    file1.mkdirs();
                }
                try {
                    File selectedFile = new File(path + "//" + file.getGcname() + ".xml");
                    URL url = new URL(HttpClientUtil.URLEncode(Data.downloadIp + file.getGcurl()));
                    HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
                    urlCon.setConnectTimeout(6000);
                    urlCon.setReadTimeout(6000);
                    int code = urlCon.getResponseCode();
                    if (code != HttpURLConnection.HTTP_OK) {
                        JOptionPane.showMessageDialog(frame, "文件下载失败！", "提示", JOptionPane.PLAIN_MESSAGE);
                        return;
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
                    JOptionPane.showMessageDialog(frame, "文件下载完成！", "提示", JOptionPane.PLAIN_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "文件下载失败！", "提示", JOptionPane.PLAIN_MESSAGE);
                    ex.printStackTrace();
                }
            }
        } else if (button.getText().equals("刷新")) {
            refresh();
        } else if (button.getText().equals("复制")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "请选择工程！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof ProjectFile) {
                Data.tempProjectFile = (ProjectFile) node.getUserObject();
            }
        }
    }

    /**
     * 根据父级工程获得对应的下载路径
     *
     * @param str
     * @return
     */
    private String getPath(String str) {
        String path = "C://Boray" + "//官方共享//";
        if (str.contains("工程")) {
            path = path + "工程共享//";
        } else if (str.contains("场景")) {
            path = path + "素材共享//场景共享//";
        } else if (str.contains("声控")) {
            path = path + "素材共享//声控共享//";
        }
        return path;
    }

    /**
     * 刷新列表
     */
    private void refresh() {
        tree.removeAll();
        CustomTreeNode rootNode = new CustomTreeNode("官方共享");
        rootNode.setLevel(0);
        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        tree.setModel(model);
        tree.setCellRenderer(new CustomTreeCellRenderer());

        CustomTreeNode gonCheng = new CustomTreeNode("工程共享");
        gonCheng.setLevel(1);
        CustomTreeNode suCai = new CustomTreeNode("素材共享");
        suCai.setLevel(1);
        CustomTreeNode changJingSuCai = new CustomTreeNode("场景素材");
        CustomTreeNode ShengKonSuCai = new CustomTreeNode("声控素材");
        changJingSuCai.setLevel(3);
        ShengKonSuCai.setLevel(3);
        suCai.add(changJingSuCai);
        suCai.add(ShengKonSuCai);

        rootNode.add(gonCheng);
        rootNode.add(suCai);
        Map<String, String> param = new HashMap<>();
        String request = HttpClientUtil.doGet(Data.ipPort + "findallgsgcinfo", param);
        java.util.List<ProjectFile> list = JSON.parseArray(request, ProjectFile.class);
        for (ProjectFile file : list) {
            CustomTreeNode node = new CustomTreeNode(file);
            node.setLevel(2);
            gonCheng.add(node);
        }
        //素材
        param.clear();
        param.put("gctype", 0 + "");
        request = HttpClientUtil.doGet(Data.ipPort + "findallgssuchai", param);
        list = JSON.parseArray(request, ProjectFile.class);
        for (ProjectFile file : list) {
            CustomTreeNode node = new CustomTreeNode(file);
            node.setLevel(4);
            changJingSuCai.add(node);
        }
        param.put("gctype", 1 + "");
        request = HttpClientUtil.doGet(Data.ipPort + "findallgssuchai", param);
        list = JSON.parseArray(request, ProjectFile.class);
        for (ProjectFile file : list) {
            CustomTreeNode node = new CustomTreeNode(file);
            node.setLevel(4);
            ShengKonSuCai.add(node);
        }
        TreeUtil util = new TreeUtil();
        util.expandAll(tree, new TreePath(rootNode), true);
    }
}
