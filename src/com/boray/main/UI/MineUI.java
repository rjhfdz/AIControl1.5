package com.boray.main.UI;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.FileOrFolder;
import com.boray.entity.ProjectFile;
import com.boray.entity.Users;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineUI {

    public void show(JPanel panel) {
        FlowLayout flowLayout6 = new FlowLayout(FlowLayout.LEFT);
        flowLayout6.setVgap(0);
        panel.setLayout(flowLayout6);
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(900, 588));

        JPanel buttonPanel = new JPanel();//�����İ�ť
        buttonPanel.setLayout(flowLayout6);
        buttonPanel.setBorder(new LineBorder(Color.gray));
        buttonPanel.setPreferredSize(new Dimension(900, 35));
        JButton addFolder = new JButton("�½���Ŀ");
        JButton updateFolder = new JButton("��Ŀ������");
        JButton deleteFolder = new JButton("ɾ����Ŀ");
        JButton addFile = new JButton("��ӹ���");
        JButton updateFile = new JButton("����������");
        JButton deleteFile = new JButton("ɾ������");
        JButton refresh = new JButton("ˢ��");

        MineButtonListener listener = new MineButtonListener();
        addFolder.addActionListener(listener);
        updateFolder.addActionListener(listener);
        deleteFolder.addActionListener(listener);
        addFile.addActionListener(listener);
        updateFile.addActionListener(listener);
        deleteFile.addActionListener(listener);
        refresh.addActionListener(listener);

        buttonPanel.add(addFolder);
        buttonPanel.add(updateFolder);
        buttonPanel.add(deleteFolder);
        buttonPanel.add(addFile);
        buttonPanel.add(updateFile);
        buttonPanel.add(deleteFile);
        buttonPanel.add(refresh);

        panel.add(buttonPanel);

        init(panel);
    }

    /**
     * �����ļ��б� ��ʱ��̬
     *
     * @param pane
     */
    public void init(JPanel pane) {
        Users users = (Users) MainUi.map.get("Users");
        Map<String, String> param = new HashMap<>();
        param.put("createby", users.getUsername());
        String request = HttpClientUtil.doGet(Data.ipPort + "findallxminfogr", param);
        List<FileOrFolder> list = JSON.parseArray(request, FileOrFolder.class);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(250, 550));

        // �������ڵ�
        CustomTreeNode rootNode = new CustomTreeNode("�ҵ���Ŀ");
        rootNode.setLevel(0);
        DefaultTreeModel model = new DefaultTreeModel(rootNode);

        // ʹ�ø��ڵ㴴�������
        JTree tree = new JTree(model);

        //����ͼ����ʽ
        tree.setCellRenderer(new CustomTreeCellRenderer());

        for (FileOrFolder folder : list) {
            CustomTreeNode node = new CustomTreeNode(folder);
            node.setLevel(1);
            Map<String, String> map = new HashMap<>();
            map.put("xmid", folder.getId() + "");
            String str = HttpClientUtil.doGet(Data.ipPort +"findbyxmid", map);
            List<ProjectFile> files = JSON.parseArray(str, ProjectFile.class);
            for (ProjectFile file : files) {
                CustomTreeNode fileNode = new CustomTreeNode(file);
                fileNode.setLevel(2);
                node.add(fileNode);
            }
            rootNode.add(node);
        }

        // ��������ʾ���ڵ���
        tree.setShowsRootHandles(true);

        // �������ڵ�ɱ༭
//        tree.setEditable(true);

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

        //Ĭ��չ��ȫ���ڵ�
        TreeUtil util = new TreeUtil();
        util.expandAll(tree, new TreePath(rootNode), true);

        MainUi.map.put("mineTree", tree);

        pane.add(panel);
    }
}
