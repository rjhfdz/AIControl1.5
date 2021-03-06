package com.boray.main.UI;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.*;
import com.boray.main.Listener.LoginListener;
import com.boray.main.Listener.MineButtonListener;
import com.boray.main.Listener.RegisterListener;
import com.boray.main.Listener.ShareListener;
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

public class ShareUI {

    private LoginListener listener;
    private JPopupMenu popupMenu;
    private JMenuItem addMember;
    private JMenuItem delMember;

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
            MainUi.map.put("ShareUsername", username);
            pane.add(username);
            JPasswordField password = new JPasswordField(20);
            MainUi.map.put("SharePassword", password);
            pane.add(new JLabel("密码：   "));
            pane.add(password);
            JButton clear = new JButton("清除");
            JButton login = new JButton("登录");
            JButton register = new JButton("注册");
            login.setName("Share");
            clear.setName("Share");

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
//            JButton downloadFile = new JButton("下载工程");
            JButton refresh = new JButton("刷新");
//            JButton copy = new JButton("复制");

            ShareListener listener = new ShareListener();
//            downloadFile.addActionListener(listener);
            refresh.addActionListener(listener);
//            copy.addActionListener(listener);

//            buttonPanel.add(downloadFile);
            buttonPanel.add(refresh);
//            buttonPanel.add(copy);

            popupMenu = new JPopupMenu();
            addMember = new JMenuItem("增加成员");
            delMember = new JMenuItem("踢出成员");

            ButtonGroup group = new ButtonGroup();
            group.add(addMember);
            group.add(delMember);

            popupMenu.add(addMember);
            popupMenu.add(delMember);

            addMember.addActionListener(listener);
            delMember.addActionListener(listener);

            panel.add(buttonPanel);

            init(panel);
        }
    }

    /**
     * 加载团队成员列表
     *
     * @param pane
     */
    public void init(JPanel pane) {
        Admin admin = (Admin) MainUi.map.get("admin");
        Map<String, String> param = new HashMap<>();
        param.put("officecode", admin.getOfficecode());
        String request = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/gettuandui", param);
        java.util.List<Member> list = JSON.parseArray(request, Member.class);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(300, 550));

        // 创建根节点
        CustomTreeNode rootNode = new CustomTreeNode("成员");
        rootNode.setLevel(0);
        DefaultTreeModel model = new DefaultTreeModel(rootNode);

        // 使用根节点创建树组件
        final JTree tree = new JTree(model);

        //设置图标样式
        tree.setCellRenderer(new CustomTreeCellRenderer());

        for (Member member : list) {
            CustomTreeNode node = new CustomTreeNode(member);
            node.setLevel(1);
            rootNode.add(node);
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

        MainUi.map.put("shareTree1", tree);

        pane.add(panel);
    }
}
