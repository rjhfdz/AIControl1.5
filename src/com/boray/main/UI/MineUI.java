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
            panel.removeAll();//������пؼ������²���
            JPanel jPanel2 = new JPanel();
            jPanel2.setPreferredSize(new Dimension(900, 588));
            JPanel jPanel = new JPanel();
            jPanel.setPreferredSize(new Dimension(800, 150));
            panel.add(jPanel);
            JPanel pane = new JPanel();
            pane.setPreferredSize(new Dimension(350, 200));
            pane.add(new JLabel("�û�����"));
            JTextField username = new JTextField(20);
            MainUi.map.put("MineUsername", username);
            pane.add(username);
            JPasswordField password = new JPasswordField(20);
            MainUi.map.put("MinePassword", password);
            pane.add(new JLabel("���룺   "));
            pane.add(password);
            JButton clear = new JButton("���");
            JButton login = new JButton("��¼");
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
            JButton refresh = new JButton("ˢ��");

            addFolder = new JMenuItem("�½���Ŀ");
            updateFolder = new JMenuItem("��Ŀ������");
            deleteFolder = new JMenuItem("ɾ����Ŀ");
            addFile = new JMenuItem("�ϴ�����");
            updateFile = new JMenuItem("����������");
            deleteFile = new JMenuItem("ɾ������");
            downloadFile = new JMenuItem("���ع���");
            addtd = new JMenuItem("�ϴ���Դ���Ŷ�");
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
     * �����ļ��б� ��ʱ��̬
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

        // �������ڵ�
        CustomTreeNode rootNode = new CustomTreeNode("���˹���");
        rootNode.setLevel(0);

        DefaultTreeModel model = new DefaultTreeModel(rootNode);

        // ʹ�ø��ڵ㴴�������
        final JTree tree = new JTree(model);

        //����ͼ����ʽ
        tree.setCellRenderer(new CustomTreeCellRenderer());


        CustomTreeNode gonCheng = new CustomTreeNode("���˹���");
        gonCheng.setLevel(1);
        CustomTreeNode suCai = new CustomTreeNode("�����ز�");
        suCai.setLevel(1);
        CustomTreeNode changJingSuCai = new CustomTreeNode("�����ز�");
        CustomTreeNode ShengKonSuCai = new CustomTreeNode("�����ز�");
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

        MainUi.map.put("mineTree", tree);

        pane.add(panel);
    }
}
