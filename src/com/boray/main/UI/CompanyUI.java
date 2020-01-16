package com.boray.main.UI;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.FileOrFolder;
import com.boray.entity.ProjectFile;
import com.boray.entity.Users;
import com.boray.main.Listener.CompanyListener;
import com.boray.main.Listener.LoginListener;
import com.boray.main.Util.CustomTreeCellRenderer;
import com.boray.main.Util.CustomTreeNode;
import com.boray.main.Util.TreeUtil;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyUI {

    private LoginListener listener;

    public void show(JPanel panel) {
        if (MainUi.map.get("Users") == null) {
            JPanel jPanel2 = new JPanel();
            jPanel2.setPreferredSize(new Dimension(900, 588));
            JPanel jPanel = new JPanel();
            jPanel.setPreferredSize(new Dimension(800, 150));
            panel.add(jPanel);
            JPanel pane = new JPanel();
            pane.setPreferredSize(new Dimension(350, 200));
            pane.add(new JLabel("用户名："));
            JTextField username = new JTextField(20);
            MainUi.map.put("CompanyUsername", username);
            pane.add(username);
            JPasswordField password = new JPasswordField(20);
            MainUi.map.put("CompanyPassword", password);
            pane.add(new JLabel("密码：   "));
            pane.add(password);
            JButton clear = new JButton("清除");
            JButton login = new JButton("登录");
            login.setName("Company");
            clear.setName("Company");

            listener = new LoginListener(pane);
            login.addActionListener(listener);
            clear.addActionListener(listener);

            pane.add(clear);
            pane.add(login);
            jPanel2.add(jPanel);
            jPanel2.add(pane);
            panel.add(jPanel2, BorderLayout.CENTER);
        } else {
            panel.removeAll();//清除所有控件，重新布局
            panel.updateUI();
            FlowLayout flowLayout6 = new FlowLayout(FlowLayout.LEFT);
            flowLayout6.setVgap(0);
            panel.setLayout(flowLayout6);
            panel.setBorder(new LineBorder(Color.gray));
            panel.setPreferredSize(new Dimension(900, 588));

            JPanel buttonPanel = new JPanel();//顶部的按钮
            buttonPanel.setLayout(flowLayout6);
            buttonPanel.setBorder(new LineBorder(Color.gray));
            buttonPanel.setPreferredSize(new Dimension(900, 35));
            JButton addFolder = new JButton("新建项目");
            JButton updateFolder = new JButton("项目重命名");
            JButton deleteFolder = new JButton("删除项目");
            JButton addFile = new JButton("上传工程");
            JButton updateFile = new JButton("工程重命名");
            JButton deleteFile = new JButton("删除工程");
            JButton downloadFile = new JButton("下载工程");
            JButton refresh = new JButton("刷新");
            JButton audit = new JButton("提交审核");

            CompanyListener listener = new CompanyListener();
            addFolder.addActionListener(listener);
            updateFolder.addActionListener(listener);
            deleteFolder.addActionListener(listener);
            addFile.addActionListener(listener);
            updateFile.addActionListener(listener);
            deleteFile.addActionListener(listener);
            downloadFile.addActionListener(listener);
            refresh.addActionListener(listener);
            audit.addActionListener(listener);

            buttonPanel.add(addFolder);
            buttonPanel.add(updateFolder);
            buttonPanel.add(deleteFolder);
            buttonPanel.add(addFile);
            buttonPanel.add(updateFile);
            buttonPanel.add(deleteFile);
            buttonPanel.add(downloadFile);
            buttonPanel.add(refresh);
            buttonPanel.add(audit);

            panel.add(buttonPanel);

            init(panel);
        }
    }

    /**
     * 加载文件列表 暂时静态
     *
     * @param pane
     */
    public void init(JPanel pane) {
        Users users = (Users) MainUi.map.get("Users");
        Map<String, String> param = new HashMap<>();
        param.put("createby", users.getUsername());
        String request = HttpClientUtil.doGet(Data.ipPort + "findallxminfogs", param);
        List<FileOrFolder> list = JSON.parseArray(request, FileOrFolder.class);
        System.out.println(request);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(300, 550));

        // 创建根节点
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Boray");

        // 使用根节点创建树组件
        JTree tree = new JTree(rootNode);

        //设置图标样式
        tree.setCellRenderer(new CustomTreeCellRenderer());

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
            rootNode.add(node);
        }

        // 设置树显示根节点句柄
        tree.setShowsRootHandles(true);

        // 设置树节点可编辑
//        tree.setEditable(true);

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

        //默认展开全部节点
        TreeUtil util = new TreeUtil();
        util.expandAll(tree, new TreePath(rootNode), true);

        MainUi.map.put("CompanyTree", tree);

        pane.add(panel);
    }
}
