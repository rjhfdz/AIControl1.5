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
        if (button.getText().equals("新建项目")) {
            DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
            File file = (File) root.getUserObject();
            JDialog dialog = new JDialog(frame, true);
            dialog.setResizable(false);
            dialog.setTitle("新建项目");
            int w = 380, h = 180;
            dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
            dialog.setSize(w, h);
            dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            addItem(dialog, file.getAbsolutePath());
            dialog.setVisible(true);
        } else if (button.getText().equals("项目重命名")) {

        } else if (button.getText().equals("删除项目")) {

        } else if (button.getText().equals("添加工程")) {

        } else if (button.getText().equals("工程重命名")) {

        } else if (button.getText().equals("删除工程")) {

        } else if (button.getText().equals("刷新")) {

        }
    }

    /**
     * 添加项目
     *
     * @param dialog
     */
    private void addItem(final JDialog dialog, final String path) {
        JPanel p1 = new JPanel();
        p1.add(new JLabel("项目名称："));
        final JTextField field = new JTextField(15);
        p1.add(field);
        JPanel p2 = new JPanel();
        JButton btn1 = new JButton("确定");
        JButton btn2 = new JButton("取消");
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("取消".equals(e.getActionCommand())) {
                    dialog.dispose();
                } else {
                    File file = new File(path + "//" + field.getText());
                    if (!file.exists()) {//判断该文件夹是否存在 并创建
                        file.mkdirs();
                        dialog.dispose();
                        JOptionPane.showMessageDialog(frame, "新建成功！", "提示", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "该项目已存在！", "提示", JOptionPane.PLAIN_MESSAGE);
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
     * 刷新
     */
    private void refresh() {
        tree.removeAll();
        File file = new File("C://Boray");
        if (!file.exists()) {//判断该文件夹是否存在 并创建
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
        //默认展开全部节点
        TreeUtil util = new TreeUtil();
        util.expandAll(tree, new TreePath(root), true);
    }
}
