package com.boray.main.UI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class MineUI {

    public void show(JPanel panel){
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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(250,550));

        // 创建根节点
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("中国");

        // 创建二级节点
        DefaultMutableTreeNode gdNode = new DefaultMutableTreeNode("广东");
        DefaultMutableTreeNode fjNode = new DefaultMutableTreeNode("福建");
        DefaultMutableTreeNode shNode = new DefaultMutableTreeNode("上海");
        DefaultMutableTreeNode twNode = new DefaultMutableTreeNode("台湾");

        // 把二级节点作为子节点添加到根节点
        rootNode.add(gdNode);
        rootNode.add(fjNode);
        rootNode.add(shNode);
        rootNode.add(twNode);

        // 创建三级节点
        DefaultMutableTreeNode gzNode = new DefaultMutableTreeNode("广州");
        DefaultMutableTreeNode szNode = new DefaultMutableTreeNode("深圳");

        DefaultMutableTreeNode fzNode = new DefaultMutableTreeNode("福州");
        DefaultMutableTreeNode xmNode = new DefaultMutableTreeNode("厦门");

        DefaultMutableTreeNode tbNode = new DefaultMutableTreeNode("台北");
        DefaultMutableTreeNode gxNode = new DefaultMutableTreeNode("高雄");
        DefaultMutableTreeNode jlNode = new DefaultMutableTreeNode("基隆");

        // 把三级节点作为子节点添加到相应的二级节点
        gdNode.add(gzNode);
        gdNode.add(szNode);

        fjNode.add(fzNode);
        fjNode.add(xmNode);

        twNode.add(tbNode);
        twNode.add(gxNode);
        twNode.add(jlNode);

        // 使用根节点创建树组件
        JTree tree = new JTree(rootNode);

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
