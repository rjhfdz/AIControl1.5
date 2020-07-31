package com.boray.main.UI;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.FileOrFolder;
import com.boray.entity.ProjectFile;
import com.boray.entity.Users;
import com.boray.main.Listener.LoginListener;
import com.boray.main.Listener.MineButtonListener;
import com.boray.main.Listener.RegisterListener;
import com.boray.main.Listener.TdButtonListener;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TdUi {

	private LoginListener listener;
    private JPopupMenu popupMenu;
    private JMenuItem addFolder;
    private JMenuItem updateFolder;
    private JMenuItem deleteFolder;
    private JMenuItem addFile;
    private JMenuItem updateFile;
    private JMenuItem deleteFile;
    private JMenuItem downloadFile;

    public void show(JPanel panel) {
        if (MainUi.map.get("Users") == null) {
            JPanel jPanel2 = new JPanel();
            jPanel2.setPreferredSize(new Dimension(900, 588));
            JPanel jPanel = new JPanel();
            jPanel.setPreferredSize(new Dimension(800, 150));
            panel.add(jPanel);
            JPanel pane = new JPanel();
            pane.setPreferredSize(new Dimension(350, 200));
            pane.add(new JLabel("�û�����"));
            JTextField username = new JTextField(20);
            MainUi.map.put("TdUiUsername", username);
            pane.add(username);
            JPasswordField password = new JPasswordField(20);
            MainUi.map.put("TdUiPassword", password);
            pane.add(new JLabel("���룺   "));
            pane.add(password);
            JButton clear = new JButton("���");
            JButton login = new JButton("��¼");
            login.setName("TdUi");
            clear.setName("TdUi");

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
            panel.removeAll();//������пؼ������²���
            panel.updateUI();
            FlowLayout flowLayout6 = new FlowLayout(FlowLayout.LEFT);
            flowLayout6.setVgap(0);
            panel.setLayout(flowLayout6);
            panel.setBorder(new LineBorder(Color.gray));
            panel.setPreferredSize(new Dimension(900, 588));

            JPanel buttonPanel = new JPanel();//�����İ�ť
            buttonPanel.setLayout(flowLayout6);
            buttonPanel.setBorder(new LineBorder(Color.gray));
            buttonPanel.setPreferredSize(new Dimension(900, 35));
//            JButton addFolder = new JButton("�½���Ŀ");
//            JButton updateFolder = new JButton("��Ŀ������");
//            JButton deleteFolder = new JButton("ɾ����Ŀ");
//            JButton addFile = new JButton("�ϴ�����");
//            JButton updateFile = new JButton("����������");
//            JButton deleteFile = new JButton("ɾ������");
//            JButton downloadFile = new JButton("���ع���");
            JButton refresh = new JButton("��Ŀˢ��");

            addFolder = new JMenuItem("�½���Ŀ");
            updateFolder = new JMenuItem("��Ŀ������");
            deleteFolder = new JMenuItem("ɾ����Ŀ");
            addFile = new JMenuItem("�ϴ�����");
            updateFile = new JMenuItem("����������");
            deleteFile = new JMenuItem("ɾ������");
            downloadFile = new JMenuItem("���ع���");

            ButtonGroup group = new ButtonGroup();
            group.add(addFolder);
            group.add(updateFolder);
            group.add(deleteFolder);
            group.add(addFile);
            group.add(updateFile);
            group.add(deleteFile);
            group.add(downloadFile);

            TdButtonListener listener = new TdButtonListener();
            //addFolder.addActionListener(listener);
            //updateFolder.addActionListener(listener);
           // deleteFolder.addActionListener(listener);
            //addFile.addActionListener(listener);
           // updateFile.addActionListener(listener);
            //deleteFile.addActionListener(listener);
            downloadFile.addActionListener(listener);
            refresh.addActionListener(listener);

            buttonPanel.add(refresh);
            popupMenu = new JPopupMenu();
           // popupMenu.add(addFolder);
            //popupMenu.add(updateFolder);
            //popupMenu.add(deleteFolder);
            //popupMenu.add(addFile);
            //popupMenu.add(updateFile);
           // popupMenu.add(deleteFile);
            popupMenu.add(downloadFile);

            panel.add(buttonPanel);

            init(panel);
        }
    }

    /**
     * �����ļ��б� ��ʱ��̬
     *
     * @param pane
     */
    public void init(JPanel pane) {
        Users users = (Users) MainUi.map.get("Users");
        Map<String, String> param = new HashMap<>();
        param.put("usercode", users.getUsercode());
        String request = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/gettdxminfo", param);
        List<FileOrFolder> list = JSON.parseArray(request, FileOrFolder.class);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(430, 550));

        // �������ڵ�
        CustomTreeNode rootNode = new CustomTreeNode("�Ŷ���Ŀ");
        rootNode.setLevel(0);
        DefaultTreeModel model = new DefaultTreeModel(rootNode);

        // ʹ�ø��ڵ㴴�������
        final JTree tree = new JTree(model);

        //����ͼ����ʽ
        tree.setCellRenderer(new CustomTreeCellRenderer());

        for (FileOrFolder folder : list) {
            CustomTreeNode node = new CustomTreeNode(folder);
            node.setLevel(1);
            Map<String, String> map = new HashMap<>();
            map.put("id", folder.getId() + "");
            String str = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/gettdgcinfo", map);
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
        //�Ҽ��˵�
        tree.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 3) {
                    popupMenu.show(tree, e.getX(), e.getY());
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

        MainUi.map.put("tdTree", tree);

        pane.add(panel);
    }
}
