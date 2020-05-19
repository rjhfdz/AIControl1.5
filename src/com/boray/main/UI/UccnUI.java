package com.boray.main.UI;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.ProjectFile;
import com.boray.main.Listener.LoginListener;
import com.boray.main.Listener.RegisterListener;
import com.boray.main.Listener.UccnListener;
import com.boray.main.Util.CustomTreeCellRenderer;
import com.boray.main.Util.CustomTreeNode;
import com.boray.main.Util.TreeUtil;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class UccnUI {

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
            MainUi.map.put("UccnUsername", username);
            pane.add(username);
            JPasswordField password = new JPasswordField(20);
            MainUi.map.put("UccnPassword", password);
            pane.add(new JLabel("密码：   "));
            pane.add(password);
            JButton clear = new JButton("清除");
            JButton login = new JButton("登录");
            JButton register = new JButton("注册");
            login.setName("Uccn");
            clear.setName("Uccn");

            listener = new LoginListener(pane);
            login.addActionListener(listener);
            clear.addActionListener(listener);
            register.addActionListener(new RegisterListener());

            pane.add(clear);
            pane.add(login);
            pane.add(register);
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
            JButton downloadFile = new JButton("下载工程");
            JButton refresh = new JButton("刷新");
            JButton copy = new JButton("复制");

            UccnListener listener = new UccnListener();
            downloadFile.addActionListener(listener);
            refresh.addActionListener(listener);
            copy.addActionListener(listener);

            buttonPanel.add(downloadFile);
            buttonPanel.add(refresh);
            buttonPanel.add(copy);

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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(300, 550));

        // 创建根节点
        CustomTreeNode rootNode = new CustomTreeNode("官方共享");
        rootNode.setLevel(0);
        DefaultTreeModel model = new DefaultTreeModel(rootNode);

        // 使用根节点创建树组件
        JTree tree = new JTree(model);

        //设置图标样式
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

        //工程
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

        // 设置树显示根节点句柄
        tree.setShowsRootHandles(true);

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

        MainUi.map.put("uccnTree", tree);

        pane.add(panel);
    }
}
