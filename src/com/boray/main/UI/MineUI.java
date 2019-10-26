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


        JPanel buttonPanel = new JPanel();//�����İ�ť
        buttonPanel.setLayout(flowLayout6);
        buttonPanel.setBorder(new LineBorder(Color.gray));
        buttonPanel.setPreferredSize(new Dimension(900,35));
        JButton addFolder = new JButton("�½��ļ���");
        JButton updateFolder = new JButton("�ļ���������");
        JButton deleteFolder = new JButton("ɾ���ļ���");
        JButton addFile = new JButton("�½��ļ�");
        JButton updateFile = new JButton("�ļ�������");
        JButton deleteFile = new JButton("ɾ���ļ�");
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
     * �����ļ��б� ��ʱ��̬
     * @param pane
     */
    public void init(JPanel pane){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(250,550));

        // �������ڵ�
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("�й�");

        // ���������ڵ�
        DefaultMutableTreeNode gdNode = new DefaultMutableTreeNode("�㶫");
        DefaultMutableTreeNode fjNode = new DefaultMutableTreeNode("����");
        DefaultMutableTreeNode shNode = new DefaultMutableTreeNode("�Ϻ�");
        DefaultMutableTreeNode twNode = new DefaultMutableTreeNode("̨��");

        // �Ѷ����ڵ���Ϊ�ӽڵ���ӵ����ڵ�
        rootNode.add(gdNode);
        rootNode.add(fjNode);
        rootNode.add(shNode);
        rootNode.add(twNode);

        // ���������ڵ�
        DefaultMutableTreeNode gzNode = new DefaultMutableTreeNode("����");
        DefaultMutableTreeNode szNode = new DefaultMutableTreeNode("����");

        DefaultMutableTreeNode fzNode = new DefaultMutableTreeNode("����");
        DefaultMutableTreeNode xmNode = new DefaultMutableTreeNode("����");

        DefaultMutableTreeNode tbNode = new DefaultMutableTreeNode("̨��");
        DefaultMutableTreeNode gxNode = new DefaultMutableTreeNode("����");
        DefaultMutableTreeNode jlNode = new DefaultMutableTreeNode("��¡");

        // �������ڵ���Ϊ�ӽڵ���ӵ���Ӧ�Ķ����ڵ�
        gdNode.add(gzNode);
        gdNode.add(szNode);

        fjNode.add(fzNode);
        fjNode.add(xmNode);

        twNode.add(tbNode);
        twNode.add(gxNode);
        twNode.add(jlNode);

        // ʹ�ø��ڵ㴴�������
        JTree tree = new JTree(rootNode);

        // ��������ʾ���ڵ���
        tree.setShowsRootHandles(true);

        // �������ڵ�ɱ༭
        tree.setEditable(true);

        // ���ýڵ�ѡ�м�����
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                System.out.println("��ǰ��ѡ�еĽڵ�: " + e.getPath());
            }
        });

        // ����������壬����������Ϊ���ڵ�չ���������Ҫ�ܴ�Ŀռ�����ʾ��������Ҫ��һ�����������������
        JScrollPane scrollPane = new JScrollPane(tree);

        // ��ӹ�����嵽���������
        panel.add(scrollPane, BorderLayout.CENTER);

        pane.add(panel);
    }
}
