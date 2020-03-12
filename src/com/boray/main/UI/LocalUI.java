package com.boray.main.UI;

import com.boray.Utils.Util;
import com.boray.beiFen.Listener.LoadProjectFileActionListener;
import com.boray.main.Listener.LocalListenter;
import com.boray.main.Listener.LocalMenuListenter;
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
    private JMenuItem loadProject;
    private JMenuItem addFolder;
    private JMenuItem reNameFolder;
    private JMenuItem delFolder;
    private JMenuItem addFile;
    private JMenuItem reNameFile;
    private JMenuItem delFile;
    private JMenuItem copy;
    private JMenuItem paste;

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
        JButton refresh = new JButton("刷新");

        LocalListenter listenter = new LocalListenter();
        refresh.addActionListener(listenter);

        popupMenu = new JPopupMenu();
        loadProject = new JMenuItem("加载工程");
        addFolder = new JMenuItem("添加项目");
        reNameFolder = new JMenuItem("项目重命名");
        delFolder = new JMenuItem("删除项目");
        addFile = new JMenuItem("添加工程");
        reNameFile = new JMenuItem("工程重命名");
        delFile = new JMenuItem("删除工程");
        copy = new JMenuItem("复制");
        paste = new JMenuItem("粘贴");
        ButtonGroup group = new ButtonGroup();
        group.add(loadProject);
        group.add(addFolder);
        group.add(reNameFolder);
        group.add(delFolder);
        group.add(addFile);
        group.add(reNameFile);
        group.add(delFile);
        group.add(copy);
        group.add(paste);
        popupMenu.add(addFolder);
        popupMenu.add(reNameFolder);
        popupMenu.add(delFolder);
        popupMenu.add(addFile);
        popupMenu.add(reNameFile);
        popupMenu.add(delFile);
        popupMenu.add(loadProject);
        popupMenu.add(copy);
        popupMenu.add(paste);

        LocalMenuListenter menuListenter = new LocalMenuListenter();
        addFolder.addActionListener(menuListenter);
        reNameFolder.addActionListener(menuListenter);
        delFolder.addActionListener(menuListenter);
        addFile.addActionListener(menuListenter);
        reNameFile.addActionListener(menuListenter);
        delFile.addActionListener(menuListenter);
        loadProject.addActionListener(this);
        copy.addActionListener(menuListenter);
        paste.addActionListener(menuListenter);

        buttonPanel.add(refresh);
        JLabel editLabel = new JLabel();
        MainUi.map.put("LocalEditLabel", editLabel);
        buttonPanel.add(editLabel);
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
                    if (e.getButton() == 3) {
                        if (file1.isFile()) {
                            loadProject.setEnabled(true);
                            addFolder.setEnabled(false);
                            reNameFolder.setEnabled(false);
                            delFolder.setEnabled(false);
                            addFile.setEnabled(false);
                            reNameFile.setEnabled(true);
                            delFile.setEnabled(true);
                            copy.setEnabled(true);
                            paste.setEnabled(false);
                            tree.setSelectionPath(path);
                            popupMenu.show(tree, e.getX(), e.getY());
                        } else if (file1.isDirectory()) {
                            loadProject.setEnabled(false);
                            addFolder.setEnabled(true);
                            reNameFolder.setEnabled(true);
                            delFolder.setEnabled(true);
                            addFile.setEnabled(true);
                            reNameFile.setEnabled(false);
                            delFile.setEnabled(false);
                            copy.setEnabled(false);
                            paste.setEnabled(true);
                            tree.setSelectionPath(path);
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
            Util.stopAutoSaveFile();
            LoadProjectFileActionListener listener = new LoadProjectFileActionListener();
            listener.tt(file1, 1);
            JLabel editLabel = (JLabel) MainUi.map.get("LocalEditLabel");
            editLabel.setText("正在编辑：" + file1.getName());
        }
    }

}
