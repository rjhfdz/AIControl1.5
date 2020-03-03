package com.boray.main.Listener;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.ProjectFile;
import com.boray.entity.Users;
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
import java.util.HashMap;
import java.util.Map;

public class ShareListener implements ActionListener {

    private JFrame frame;
    private JTree tree;

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        frame = (JFrame) MainUi.map.get("frame");
        tree = (JTree) MainUi.map.get("shareTree");
        if (button.getText().equals("���ع���")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "��ѡ�񹤳̣�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof ProjectFile) {
                ProjectFile file = (ProjectFile) node.getUserObject();
                CustomTreeNode folder = (CustomTreeNode) node.getParent();
                String str = (String) folder.getUserObject();
                String path = getPath(str);
                File file1 = new File(path);
                if (!file1.exists()) {//�ж��ļ����Ƿ����
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
                        JOptionPane.showMessageDialog(frame, "�ļ�����ʧ�ܣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
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
                    JOptionPane.showMessageDialog(frame, "�ļ�������ɣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "�ļ�����ʧ�ܣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    ex.printStackTrace();
                }
            }
        } else if (button.getText().equals("ˢ��")) {
            refresh();
        } else if (button.getText().equals("����")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "��ѡ�񹤳̣�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof ProjectFile) {
                Data.tempProjectFile = (ProjectFile) node.getUserObject();
            }
        }
    }

    /**
     * ���ݸ������̻�ö�Ӧ������·��
     *
     * @param str
     * @return
     */
    private String getPath(String str) {
        String path = "C://Boray" + "//����ƽ̨//";
        return path;
    }

    /**
     * ˢ���б�
     */
    private void refresh() {
        tree.removeAll();
        CustomTreeNode rootNode = new CustomTreeNode("BorayShare");
        rootNode.setLevel(0);
        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        tree.setModel(model);
        tree.setCellRenderer(new CustomTreeCellRenderer());
        Map<String, String> param = new HashMap<>();
        String request = HttpClientUtil.doGet(Data.ipPort + "findbygx", param);
        java.util.List<ProjectFile> list = JSON.parseArray(request, ProjectFile.class);
        for (ProjectFile file : list) {
            CustomTreeNode node = new CustomTreeNode(file);
            node.setLevel(1);
            rootNode.add(node);
        }
        TreeUtil util = new TreeUtil();
        util.expandAll(tree, new TreePath(rootNode), true);
    }
}
