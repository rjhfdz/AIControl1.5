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
        Users users = (Users) MainUi.map.get("Users");
        Map<String, String> param = new HashMap<>();
        param.put("createby", users.getUsername());
        String request = HttpClientUtil.doGet("http://128.8.3.48:8778/findallxminfogs",param);
        List<FileOrFolder> list = JSON.parseArray(request,FileOrFolder.class);
        System.out.println(request);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(250,550));

        // �������ڵ�
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Boray");

        // ʹ�ø��ڵ㴴�������
        JTree tree = new JTree(rootNode);

        //����ͼ����ʽ
        tree.setCellRenderer(new CustomTreeCellRenderer());

        for (FileOrFolder folder:list){
            rootNode.add(new DefaultMutableTreeNode(folder));
        }

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
