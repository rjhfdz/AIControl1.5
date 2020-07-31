package com.boray.main.UI;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.addJCheckBox.CWCheckBoxRenderer;
import com.boray.addJCheckBox.CheckBoxCellEditor;
import com.boray.dengKu.UI.NewJTable;
import com.boray.entity.*;
import com.boray.main.Listener.*;
import com.boray.main.Util.CustomTreeCellRenderer;
import com.boray.main.Util.CustomTreeNode;
import com.boray.main.Util.TreeUtil;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyUI {

    private LoginListener listener;
    private JPopupMenu popupMenu;
    private JMenuItem addMember;
    private JMenuItem delMember;

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
            MainUi.map.put("CompanyUsername", username);
            pane.add(username);
            JPasswordField password = new JPasswordField(20);
            MainUi.map.put("CompanyPassword", password);
            pane.add(new JLabel("���룺   "));
            pane.add(password);
            JButton clear = new JButton("���");
            JButton login = new JButton("��¼");
            JButton register = new JButton("ע��");
            login.setName("Company");
            clear.setName("Company");

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
            JButton refresh = new JButton("ˢ��");
//            JButton copy = new JButton("����");

            ShareListener listener = new ShareListener();
//            downloadFile.addActionListener(listener);
            refresh.addActionListener(listener);
//            copy.addActionListener(listener);

            buttonPanel.add(refresh);
//            buttonPanel.add(copy);

            popupMenu = new JPopupMenu();
            addMember = new JMenuItem("�����Ŷ�");
            delMember = new JMenuItem("�߳���Ա");

            ButtonGroup group = new ButtonGroup();
            group.add(addMember);
            group.add(delMember);

            popupMenu.add(addMember);
           // popupMenu.add(delMember);

            addMember.addActionListener(listener);
           // delMember.addActionListener(listener);

            panel.add(buttonPanel);

            init(panel);
        }
    }

    /**
     * �����Ŷӳ�Ա�б�
     *
     * @param pane
     */
    public void init(JPanel pane) {
        Admin admin = (Admin) MainUi.map.get("admin");
        Users users = (Users) MainUi.map.get("Users");
        Map<String, String> param = new HashMap<>();
        param.put("usercode", users.getUsercode());
        String request = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/gettdinfo", param);
        java.util.List<Offerentity> list = JSON.parseArray(request, Offerentity.class);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(300, 550));

        // �������ڵ�
        CustomTreeNode rootNode = new CustomTreeNode("�Ŷ�");
        rootNode.setLevel(0);
        DefaultTreeModel model = new DefaultTreeModel(rootNode);

        // ʹ�ø��ڵ㴴�������
        final JTree tree = new JTree(model);

        //����ͼ����ʽ
        tree.setCellRenderer(new CustomTreeCellRenderer());

        for (Offerentity member : list) {
            CustomTreeNode node = new CustomTreeNode(member);
            node.setLevel(1);
            rootNode.add(node);
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

        MainUi.map.put("shareTree", tree);

        pane.add(panel);
    }
}
