package com.boray.main.UI;

import com.alibaba.fastjson.JSON;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.FileOrFolder;
import com.boray.entity.Users;
import com.boray.main.TreeUtil.CustomTreeCellRenderer;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyUI {
    public void show(JPanel panel) {
        FlowLayout flowLayout6 = new FlowLayout(FlowLayout.LEFT);
        flowLayout6.setVgap(0);
        panel.setLayout(flowLayout6);
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(900,588));

        JPanel buttonPanel = new JPanel();//顶部的按钮
        buttonPanel.setLayout(flowLayout6);
        buttonPanel.setBorder(new LineBorder(Color.gray));
        buttonPanel.setPreferredSize(new Dimension(900,35));
        JButton addFolder = new JButton("新建文件夹");
        JButton updateFolder = new JButton("文件夹重命名");
        JButton deleteFolder = new JButton("删除文件夹");
        JButton addFile = new JButton("新建文件");
        JButton updateFile = new JButton("文件重命名");
        JButton deleteFile = new JButton("删除文件");
        buttonPanel.add(addFolder);
        buttonPanel.add(updateFolder);
        buttonPanel.add(deleteFolder);
        buttonPanel.add(addFile);
        buttonPanel.add(updateFile);
        buttonPanel.add(deleteFile);

        panel.add(buttonPanel);

        init(panel);
    }

    /**
     * 加载文件列表 暂时静态
     * @param pane
     */
    public void init(JPanel pane){
        Users users = (Users) MainUi.map.get("Users");
        Map<String, String> param = new HashMap<>();
        param.put("createby", users.getUsername());
        String request = HttpClientUtil.doGet("http://128.8.3.48:8778/findallxminfogs",param);
        List<FileOrFolder> list = JSON.parseArray(request,FileOrFolder.class);
        System.out.println(request);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(250,550));

        // 创建根节点
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Boray");

        // 使用根节点创建树组件
        JTree tree = new JTree(rootNode);

        //设置图标样式
        tree.setCellRenderer(new CustomTreeCellRenderer());

        for (FileOrFolder folder:list){
            rootNode.add(new DefaultMutableTreeNode(folder));
        }

        // 设置树显示根节点句柄
        tree.setShowsRootHandles(true);

        // 设置树节点可编辑
        tree.setEditable(true);

        // 设置节点选中监听器
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                System.out.println("当前被选中的节点: " + e.getPath());
            }
        });

        // 创建滚动面板，包裹树（因为树节点展开后可能需要很大的空间来显示，所以需要用一个滚动面板来包裹）
        JScrollPane scrollPane = new JScrollPane(tree);

        // 添加滚动面板到那内容面板
        panel.add(scrollPane, BorderLayout.CENTER);

        pane.add(panel);
    }
}
