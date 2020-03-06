package com.boray.main.UI;

import com.boray.Utils.Util;
import com.boray.beiFen.Listener.LoadProjectFileActionListener;
import com.boray.main.Listener.LocalListenter;
import com.boray.main.Util.CustomTreeCellRenderer;
import com.boray.main.Util.CustomTreeNode;
import com.boray.main.Util.TreeUtil;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class LocalUI implements ActionListener {

    private JPopupMenu popupMenu;
    private JMenuItem menuItem;

    public void show(JPanel panel) {
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
        JButton addFile = new JButton("添加工程");
        JButton updateFile = new JButton("工程重命名");
        JButton deleteFile = new JButton("删除工程");
        JButton refresh = new JButton("刷新");
        JButton copy = new JButton("复制");
        JButton paste = new JButton("粘贴");

        LocalListenter listenter = new LocalListenter();
        addFolder.addActionListener(listenter);
        updateFolder.addActionListener(listenter);
        deleteFolder.addActionListener(listenter);
        addFile.addActionListener(listenter);
        updateFile.addActionListener(listenter);
        deleteFile.addActionListener(listenter);
        refresh.addActionListener(listenter);
        copy.addActionListener(listenter);
        paste.addActionListener(listenter);

        popupMenu = new JPopupMenu();
        menuItem = new JMenuItem("加载工程");
        menuItem.addActionListener(this);
        ButtonGroup group = new ButtonGroup();
        group.add(menuItem);
        popupMenu.add(menuItem);

        buttonPanel.add(addFolder);
        buttonPanel.add(updateFolder);
        buttonPanel.add(deleteFolder);
        buttonPanel.add(addFile);
        buttonPanel.add(updateFile);
        buttonPanel.add(deleteFile);
        buttonPanel.add(refresh);
        buttonPanel.add(copy);
        buttonPanel.add(paste);

        panel.add(buttonPanel);

        init(panel);
    }

    /**
     * 加载文件列表
     *
     * @param pane
     */
    public void init(JPanel pane) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(300, 550));

        File file = new File("C://Boray");
        if (!file.exists()) {//判断该文件夹是否存在 并创建
            file.mkdirs();
        }

        // 创建根节点
        DefaultMutableTreeNode rootNode = Util.traverseFolder(file);

        // 使用根节点创建树组件
        final JTree tree = new JTree(rootNode);

        //设置图标样式
        tree.setCellRenderer(new CustomTreeCellRenderer());

        // 设置树显示根节点句柄
        tree.setShowsRootHandles(true);

        // 设置节点选中监听器
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                System.out.println("当前被选中的节点: " + e.getPath());
            }
        });
        tree.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                if (path == null) {  //JTree上没有任何项被选中
                    return;
                } else {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
                    File file1 = (File) node.getUserObject();
                    if (file1.isFile()) {
                        tree.setSelectionPath(path);
                        if (e.getButton() == 3) {
                            popupMenu.show(tree, e.getX(), e.getY());
                        }
                    }
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

        MainUi.map.put("LocalTree", tree);

        pane.add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTree tree = (JTree) MainUi.map.get("LocalTree");
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        File file1 = (File) node.getUserObject();
        if (file1.exists()) {
            LoadProjectFileActionListener listener = new LoadProjectFileActionListener();
            listener.tt(file1, 1);
        }
    }
}
