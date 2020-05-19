package com.boray.main.UI;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.ProjectFile;
import com.boray.main.Listener.LoginListener;
import com.boray.main.Listener.RegisterListener;
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
import java.util.HashMap;
import java.util.Map;

public class UccnUI {

    private LoginListener listener;

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
            MainUi.map.put("UccnUsername", username);
            pane.add(username);
            JPasswordField password = new JPasswordField(20);
            MainUi.map.put("UccnPassword", password);
            pane.add(new JLabel("���룺   "));
            pane.add(password);
            JButton clear = new JButton("���");
            JButton login = new JButton("��¼");
            JButton register = new JButton("ע��");
            login.setName("Uccn");
            clear.setName("Uccn");

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
            JButton downloadFile = new JButton("���ع���");
            JButton refresh = new JButton("ˢ��");
            JButton copy = new JButton("����");

            UccnListener listener = new UccnListener();
            downloadFile.addActionListener(listener);
            refresh.addActionListener(listener);
            copy.addActionListener(listener);

            buttonPanel.add(downloadFile);
            buttonPanel.add(refresh);
            buttonPanel.add(copy);

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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(300, 550));

        // �������ڵ�
        CustomTreeNode rootNode = new CustomTreeNode("�ٷ�����");
        rootNode.setLevel(0);
        DefaultTreeModel model = new DefaultTreeModel(rootNode);

        // ʹ�ø��ڵ㴴�������
        JTree tree = new JTree(model);

        //����ͼ����ʽ
        tree.setCellRenderer(new CustomTreeCellRenderer());

        CustomTreeNode gonCheng = new CustomTreeNode("���̹���");
        gonCheng.setLevel(1);
        CustomTreeNode suCai = new CustomTreeNode("�زĹ���");
        suCai.setLevel(1);
        CustomTreeNode changJingSuCai = new CustomTreeNode("�����ز�");
        CustomTreeNode ShengKonSuCai = new CustomTreeNode("�����ز�");
        changJingSuCai.setLevel(3);
        ShengKonSuCai.setLevel(3);
        suCai.add(changJingSuCai);
        suCai.add(ShengKonSuCai);

        rootNode.add(gonCheng);
        rootNode.add(suCai);

        Map<String, String> param = new HashMap<>();
        String request = HttpClientUtil.doGet(Data.ipPort + "findallgsgcinfo", param);
        java.util.List<ProjectFile> list = JSON.parseArray(request, ProjectFile.class);

        //����
        for (ProjectFile file : list) {
            CustomTreeNode node = new CustomTreeNode(file);
            node.setLevel(2);
            gonCheng.add(node);
        }
        //�ز�
        param.clear();
        param.put("gctype", 0 + "");
        request = HttpClientUtil.doGet(Data.ipPort + "findallgssuchai", param);
        list = JSON.parseArray(request, ProjectFile.class);
        for (ProjectFile file : list) {
            CustomTreeNode node = new CustomTreeNode(file);
            node.setLevel(4);
            changJingSuCai.add(node);
        }
        param.put("gctype", 1 + "");
        request = HttpClientUtil.doGet(Data.ipPort + "findallgssuchai", param);
        list = JSON.parseArray(request, ProjectFile.class);
        for (ProjectFile file : list) {
            CustomTreeNode node = new CustomTreeNode(file);
            node.setLevel(4);
            ShengKonSuCai.add(node);
        }

        // ��������ʾ���ڵ���
        tree.setShowsRootHandles(true);

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

        MainUi.map.put("uccnTree", tree);

        pane.add(panel);
    }
}
