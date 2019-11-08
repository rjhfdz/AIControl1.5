package com.boray.main.Listener;

import com.boray.entity.Users;
import com.boray.main.Util.CustomTreeCellRenderer;
import com.boray.main.Util.CustomTreeNode;
import com.boray.main.Util.TreeUtil;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class LocalListenter implements ActionListener {

    private JFrame frame;
    private JTree tree;

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        frame = (JFrame) MainUi.map.get("frame");
        tree = (JTree) MainUi.map.get("LocalTree");
//        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        if (button.getText().equals("�½���Ŀ")) {
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
            File file = (File) root.getUserObject();
            JDialog dialog = new JDialog(frame, true);
            dialog.setResizable(false);
            dialog.setTitle("�½���Ŀ");
            int w = 380, h = 180;
            dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
            dialog.setSize(w, h);
            dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            addItem(dialog, file.getAbsolutePath());
            dialog.setVisible(true);
        } else if (button.getText().equals("��Ŀ������")) {

        } else if (button.getText().equals("ɾ����Ŀ")) {

        } else if (button.getText().equals("��ӹ���")) {

        } else if (button.getText().equals("����������")) {

        } else if (button.getText().equals("ɾ������")) {

        } else if (button.getText().equals("ˢ��")) {

        }
    }

    /**
     * �����Ŀ
     *
     * @param dialog
     */
    private void addItem(final JDialog dialog, final String path) {
        JPanel p1 = new JPanel();
        p1.add(new JLabel("��Ŀ���ƣ�"));
        final JTextField field = new JTextField(15);
        p1.add(field);
        JPanel p2 = new JPanel();
        JButton btn1 = new JButton("ȷ��");
        JButton btn2 = new JButton("ȡ��");
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("ȡ��".equals(e.getActionCommand())) {
                    dialog.dispose();
                } else {
                    File file = new File(path + "//" + field.getText());
                    if (!file.exists()) {//�жϸ��ļ����Ƿ���� ������
                        file.mkdirs();
                        dialog.dispose();
                        JOptionPane.showMessageDialog(frame, "�½��ɹ���", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "����Ŀ�Ѵ��ڣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    }
                    refresh();
                }
            }
        };
        btn1.addActionListener(listener);
        btn2.addActionListener(listener);
        p2.add(btn1);
        p2.add(new JLabel("     "));
        p2.add(btn2);

        JPanel n1 = new JPanel();
        n1.setPreferredSize(new Dimension(350, 20));
        dialog.add(n1);
        dialog.add(p1);
        dialog.add(p2);
    }

    /**
     * ˢ��
     */
    private void refresh() {
        tree.removeAll();
        File file = new File("C://Boray");
        if (!file.exists()) {//�жϸ��ļ����Ƿ���� ������
            file.mkdirs();
        }
        CustomTreeNode root = new CustomTreeNode(file);
        root.setLevel(0);
        DefaultTreeModel model = new DefaultTreeModel(root);
        tree.setModel(model);
        tree.setCellRenderer(new CustomTreeCellRenderer());
        for (File list : file.listFiles()) {
            CustomTreeNode node = new CustomTreeNode(list);
            node.setLevel(1);
            for (File file1 : list.listFiles()) {
                CustomTreeNode fileNode = new CustomTreeNode(file1);
                fileNode.setLevel(2);
                node.add(fileNode);
            }
            root.add(node);
        }
        //Ĭ��չ��ȫ���ڵ�
        TreeUtil util = new TreeUtil();
        util.expandAll(tree, new TreePath(root), true);
    }
}
