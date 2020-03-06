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
        JButton copy = new JButton("����");
        JButton paste = new JButton("ճ��");

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
        menuItem = new JMenuItem("���ع���");
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
     * �����ļ��б�
     *
     * @param pane
     */
    public void init(JPanel pane) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(300, 550));

        File file = new File("C://Boray");
        if (!file.exists()) {//�жϸ��ļ����Ƿ���� ������
            file.mkdirs();
        }

        // �������ڵ�
        DefaultMutableTreeNode rootNode = Util.traverseFolder(file);

        // ʹ�ø��ڵ㴴�������
        final JTree tree = new JTree(rootNode);

        //����ͼ����ʽ
        tree.setCellRenderer(new CustomTreeCellRenderer());

        // ��������ʾ���ڵ���
        tree.setShowsRootHandles(true);

        // ���ýڵ�ѡ�м�����
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                System.out.println("��ǰ��ѡ�еĽڵ�: " + e.getPath());
            }
        });
        tree.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                if (path == null) {  //JTree��û���κ��ѡ��
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

        // ����������壬����������Ϊ���ڵ�չ���������Ҫ�ܴ�Ŀռ�����ʾ��������Ҫ��һ�����������������
        JScrollPane scrollPane = new JScrollPane(tree);

        // ��ӹ�����嵽���������
        panel.add(scrollPane, BorderLayout.CENTER);

        //Ĭ��չ��ȫ���ڵ�
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
