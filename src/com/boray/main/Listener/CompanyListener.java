package com.boray.main.Listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.Utils.Util;
import com.boray.beiFen.Listener.LoadProjectFileActionListener;
import com.boray.entity.*;
import com.boray.main.Util.*;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyListener implements ActionListener {

    private JFrame frame;
    private JTree tree;

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        frame = (JFrame) MainUi.map.get("frame");
        tree = (JTree) MainUi.map.get("CompanyTree");

        if (tree.getSelectionPath() != null) {
            if (button.getText().equals("刷新")) {
                refresh();
            } else if (button.getText().equals("开启编辑")) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
                if (node.getUserObject() instanceof ProjectFile) {
                    ProjectFile file = (ProjectFile) node.getUserObject();
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node.getParent();
                    FileOrFolder folder = (FileOrFolder) treeNode.getUserObject();
                    Data.tempWebFolder = folder;
                    Data.tempWebFile = file;
                    if (!Util.downloadTemp())
                        return;
                    LoadProjectFileActionListener listener = new LoadProjectFileActionListener();
                    listener.tt(Data.tempEditWebFile, 1);
                    JLabel editLabel = (JLabel) MainUi.map.get("CompanyEditLabel");
                    editLabel.setText("正在编辑：" + folder.getXmname() + " / " + file.getGcname());
                    selfMotionSave save = new selfMotionSave();
                    save.autoSave();
                }
            } else if (button.getText().equals("取消编辑")) {
                Data.tempWebFolder = null;
                Data.tempWebFile = null;
                Data.tempEditWebFile = null;
                JLabel editLabel = (JLabel) MainUi.map.get("CompanyEditLabel");
                editLabel.setText("");
                Data.tempFileAutoSaveTimer = null;
            }
        }

    }

    /**
     * 刷新列表
     */
    private void refresh() {
        tree.removeAll();
        CustomTreeNode root = new CustomTreeNode("Boray");
        root.setLevel(0);
        DefaultTreeModel model = new DefaultTreeModel(root);
        tree.setModel(model);
        tree.setCellRenderer(new CustomTreeCellRenderer());
        Users users = (Users) MainUi.map.get("Users");
        Map<String, String> param = new HashMap<>();
        param.put("createby", users.getUsername());
        String request = HttpClientUtil.doGet(Data.ipPort + "findallxminfogs", param);
        List<FileOrFolder> list = JSON.parseArray(request, FileOrFolder.class);
        for (FileOrFolder folder : list) {
            CustomTreeNode node = new CustomTreeNode(folder);
            node.setLevel(1);
            Map<String, String> map = new HashMap<>();
            map.put("xmid", folder.getId() + "");
            String str = HttpClientUtil.doGet(Data.ipPort + "findbyxmid", map);
            List<ProjectFile> files = JSON.parseArray(str, ProjectFile.class);
            for (ProjectFile file : files) {
                CustomTreeNode fileNode = new CustomTreeNode(file);
                fileNode.setLevel(2);
                node.add(fileNode);
            }
            root.add(node);
        }
        //默认展开全部节点
        TreeUtil util = new TreeUtil();
        util.expandAll(tree, new TreePath(root), true);
    }

}
