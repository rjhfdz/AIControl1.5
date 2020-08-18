package com.boray.main.UI;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.FileOrFolder;
import com.boray.entity.ProjectFile;
import com.boray.entity.SuCaiFile;
import com.boray.entity.Users;
import com.boray.main.Listener.LoginListener;
import com.boray.main.Listener.MineButtonListener;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineUI {

    private LoginListener listener;
    private JPopupMenu popupMenu;
    private JMenuItem addFolder;
    private JMenuItem updateFolder;
    private JMenuItem deleteFolder;
    private JMenuItem addFile;
    private JMenuItem updateFile;
    private JMenuItem deleteFile;
    private JMenuItem downloadFile;
    private JMenuItem addtd;

    public void show(JPanel panel) {
        if (MainUi.map.get("Users") == null) {
            panel.removeAll();//清除所有控件，重新布局
            JPanel jPanel2 = new JPanel();
            jPanel2.setPreferredSize(new Dimension(900, 588));
            JPanel jPanel = new JPanel();
            jPanel.setPreferredSize(new Dimension(800, 150));
            panel.add(jPanel);
            JPanel pane = new JPanel();
            pane.setPreferredSize(new Dimension(350, 200));
            pane.add(new JLabel("用户名："));
            JTextField username = new JTextField(20);
            MainUi.map.put("MineUsername", username);
            pane.add(username);
            JPasswordField password = new JPasswordField(20);
            MainUi.map.put("MinePassword", password);
            pane.add(new JLabel("密码：   "));
            pane.add(password);
            JButton clear = new JButton("清除");
            JButton login = new JButton("登录");
            login.setName("Mine");
            clear.setName("Mine");

            listener = new LoginListener(pane);
            login.addActionListener(listener);
            clear.addActionListener(listener);

            pane.add(clear);
            pane.add(login);
            jPanel2.add(jPanel);
            jPanel2.add(pane);
            panel.add(jPanel2, BorderLayout.CENTER);
            panel.updateUI();
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
//            JButton addFolder = new JButton("新建项目");
//            JButton updateFolder = new JButton("项目重命名");
//            JButton deleteFolder = new JButton("删除项目");
//            JButton addFile = new JButton("上传工程");
//            JButton updateFile = new JButton("工程重命名");
//            JButton deleteFile = new JButton("删除工程");
//            JButton downloadFile = new JButton("下载工程");
            JButton refresh = new JButton("刷新");

            addFolder = new JMenuItem("新建项目");
            updateFolder = new JMenuItem("项目重命名");
            deleteFolder = new JMenuItem("删除项目");
            addFile = new JMenuItem("上传工程");
            updateFile = new JMenuItem("工程重命名");
            deleteFile = new JMenuItem("删除工程");
            downloadFile = new JMenuItem("下载工程");
            addtd = new JMenuItem("上传资源到团队");
            ButtonGroup group = new ButtonGroup();
            group.add(addFolder);
            group.add(updateFolder);
            group.add(deleteFolder);
            group.add(addFile);
            group.add(updateFile);
            group.add(deleteFile);
            group.add(downloadFile);
            group.add(addtd);

            MineButtonListener listener = new MineButtonListener();
            addFolder.addActionListener(listener);
            updateFolder.addActionListener(listener);
            deleteFolder.addActionListener(listener);
            addFile.addActionListener(listener);
            updateFile.addActionListener(listener);
            deleteFile.addActionListener(listener);
            downloadFile.addActionListener(listener);
            refresh.addActionListener(listener);
            addtd.addActionListener(listener);

            buttonPanel.add(refresh);
            popupMenu = new JPopupMenu();
            popupMenu.add(addFolder);
            popupMenu.add(updateFolder);
            popupMenu.add(deleteFolder);
            popupMenu.add(addFile);
            popupMenu.add(updateFile);
            popupMenu.add(deleteFile);
            popupMenu.add(downloadFile);
            popupMenu.add(addtd);
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
        param.put("usercode", users.getUsercode());
        String request = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/getgrxm", param);
        List<FileOrFolder> list = JSON.parseArray(request, FileOrFolder.class);

        String requestsucai = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/getgrshucai", param);
        List<SuCaiFile> suCaiFilelist = JSON.parseArray(requestsucai, SuCaiFile.class);

        String requestsksucai = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/getgrskshucai", param);
        List<SuCaiFile> sksuCaiFilelist = JSON.parseArray(requestsksucai, SuCaiFile.class);


        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(430, 550));

        // 创建根节点
        CustomTreeNode rootNode = new CustomTreeNode("个人管理");
        rootNode.setLevel(0);

        DefaultTreeModel model = new DefaultTreeModel(rootNode);

        // 使用根节点创建树组件
        final JTree tree = new JTree(model);

        //设置图标样式
        tree.setCellRenderer(new CustomTreeCellRenderer());


        CustomTreeNode gonCheng = new CustomTreeNode("个人工程");
        gonCheng.setLevel(1);
        CustomTreeNode suCai = new CustomTreeNode("个人素材");
        suCai.setLevel(1);
        CustomTreeNode changJingSuCai = new CustomTreeNode("场景素材");
        CustomTreeNode ShengKonSuCai = new CustomTreeNode("声控素材");
        changJingSuCai.setLevel(3);
        ShengKonSuCai.setLevel(3);
        suCai.add(changJingSuCai);
        suCai.add(ShengKonSuCai);
        rootNode.add(gonCheng);
        rootNode.add(suCai);

        for (FileOrFolder folder : list) {
            CustomTreeNode node = new CustomTreeNode(folder);
            node.setLevel(1);
            Map<String, String> map = new HashMap<>();
            map.put("xmid", folder.getId() + "");
            String str = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/getgrgc", map);
            List<ProjectFile> files = JSON.parseArray(str, ProjectFile.class);
            for (ProjectFile file : files) {
                CustomTreeNode fileNode = new CustomTreeNode(file);
                fileNode.setLevel(2);
                node.add(fileNode);
            }
            gonCheng.add(node);
        }

        for (SuCaiFile folder : suCaiFilelist) {
            CustomTreeNode node = new CustomTreeNode(folder);
            node.setLevel(4);

            changJingSuCai.add(node);
        }

        for (SuCaiFile folder : sksuCaiFilelist) {
            CustomTreeNode node = new CustomTreeNode(folder);
            node.setLevel(4);

            ShengKonSuCai.add(node);
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
        //右键菜单
        tree.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 3) {
                    popupMenu.show(tree, e.getX(), e.getY());
                }
            }
        });

        // 创建滚动面板，包裹树（因为树节点展开后可能需要很大的空间来显示，所以需要用一个滚动面板来包裹）
        JScrollPane scrollPane = new JScrollPane(tree);

        // 添加滚动面板到那内容面板
        panel.add(scrollPane, BorderLayout.CENTER);

        //默认展开全部节点
        TreeUtil util = new TreeUtil();
        util.expandAll(tree, new TreePath(rootNode), true);

        MainUi.map.put("mineTree", tree);

        pane.add(panel);
    }
}
