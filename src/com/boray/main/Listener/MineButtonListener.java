package com.boray.main.Listener;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.FileOrFolder;
import com.boray.entity.Message;
import com.boray.entity.ProjectFile;
import com.boray.entity.Users;
import com.boray.main.Util.CustomTreeCellRenderer;
import com.boray.main.Util.CustomTreeNode;
import com.boray.main.Util.TreeUtil;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineButtonListener implements ActionListener {

    private JFrame frame;
    private JTree tree;

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        frame = (JFrame) MainUi.map.get("frame");
        tree = (JTree) MainUi.map.get("mineTree");
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
        if (button.getText().equals("新建项目")) {
            JDialog dialog = new JDialog(frame, true);
            dialog.setResizable(false);
            dialog.setTitle("新建项目");
            int w = 380, h = 180;
            dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
            dialog.setSize(w, h);
            dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            addItem(dialog);
            dialog.setVisible(true);
        } else if (button.getText().equals("项目重命名")) {
            if (node.getUserObject() instanceof FileOrFolder) {
                JDialog dialog = new JDialog(frame, true);
                dialog.setResizable(false);
                dialog.setTitle("重命名");
                int w = 380, h = 180;
                dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
                dialog.setSize(w, h);
                dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                renameItem(dialog, node);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame, "请选择项目！", "提示", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (button.getText().equals("删除项目")) {
            if (node.getUserObject() instanceof FileOrFolder) {
                FileOrFolder folder = (FileOrFolder) node.getUserObject();
                Users users = (Users) MainUi.map.get("Users");
                Map<String, String> param = new HashMap<>();
                param.put("createby", users.getUsername());
                param.put("xmid", folder.getId() + "");
                String request = HttpClientUtil.doGet(Data.ipPort + "deletexminfo", param);
                Message message = JSON.parseObject(request, Message.class);
                JOptionPane.showMessageDialog(frame, message.getCode(), "提示", JOptionPane.PLAIN_MESSAGE);
                refresh();
            } else {
                JOptionPane.showMessageDialog(frame, "请选择项目！", "提示", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (button.getText().equals("添加工程")) {
            if (node.getUserObject() instanceof FileOrFolder) {
                JFileChooser fileChooser = new JFileChooser();
                try {
                    if (!"".equals(Data.yunProjectFilePath)) {
                        fileChooser.setCurrentDirectory(new File(Data.yunProjectFilePath));
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                String[] houZhui = {"xml"};
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.xml", houZhui);
                fileChooser.setFileFilter(filter);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnVal = fileChooser.showSaveDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    Data.yunProjectFilePath = file.getParent();
                    try {
                        addProject(node, file);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "请选择项目！", "提示", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (button.getText().equals("工程重命名")) {
            if (node.getUserObject() instanceof ProjectFile) {
                JDialog dialog = new JDialog(frame, true);
                dialog.setResizable(false);
                dialog.setTitle("重命名");
                int w = 380, h = 180;
                dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
                dialog.setSize(w, h);
                dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                renamePrject(dialog, node);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame, "请选择工程！", "提示", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (button.getText().equals("删除工程")) {
            if (node.getUserObject() instanceof ProjectFile) {
                ProjectFile file = (ProjectFile) node.getUserObject();
                Map<String, String> map = new HashMap<>();
                map.put("id", file.getXmid() + "");
                String request = HttpClientUtil.doGet(Data.ipPort + "deletegc", map);
                Message message = JSON.parseObject(request, Message.class);
                JOptionPane.showMessageDialog(frame, message.getCode(), "提示", JOptionPane.PLAIN_MESSAGE);
                refresh();
            } else {
                JOptionPane.showMessageDialog(frame, "请选择工程！", "提示", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (button.getText().equals("刷新")) {
            refresh();
        }
    }

    /**
     * 添加工程
     *
     * @param node
     * @param file
     */
    private void addProject(DefaultMutableTreeNode node, File file) {
        HttpClientUtil httpsUtils = new HttpClientUtil();
        FileOrFolder folder = (FileOrFolder) node.getUserObject();
        Users users = (Users) MainUi.map.get("Users");
        Map<String, String> param = new HashMap<>();
        param.put("gcname", file.getName());
        param.put("username", users.getUsername());
        param.put("xmid", folder.getId() + "");
        param.put("i", "0");
        Map<String, Object> resultMap = httpsUtils.uploadFileByHTTP(file, Data.ipPort + "fileUploadServletgc", param);
        Message message = JSON.parseObject(resultMap.get("data").toString(), Message.class);
        JOptionPane.showMessageDialog(frame, message.getCode(), "提示", JOptionPane.PLAIN_MESSAGE);
        refresh();

    }

    /**
     * 工程重命名
     *
     * @param dialog
     * @param node
     */
    private void renamePrject(final JDialog dialog, final DefaultMutableTreeNode node) {
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
                    ProjectFile file = (ProjectFile) node.getUserObject();
                    Map<String, String> param = new HashMap<>();
                    param.put("id", file.getId() + "");
                    param.put("gcname", field.getText());
                    Message(dialog, "updategc", param);
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
     * 新建项目
     *
     * @param dialog
     */
    private void addItem(final JDialog dialog) {
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
                    Users users = (Users) MainUi.map.get("Users");
                    Map<String, String> param = new HashMap<>();
                    param.put("xmname", field.getText());
                    param.put("createby", users.getUsername());
                    param.put("xmtype", "0");
                    Message(dialog, "xmcreategr", param);
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
     * 项目重命名
     *
     * @param dialog
     */
    private void renameItem(final JDialog dialog, final DefaultMutableTreeNode node) {
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
                    FileOrFolder folder = (FileOrFolder) node.getUserObject();
                    Users users = (Users) MainUi.map.get("Users");
                    Map<String, String> param = new HashMap<>();
                    param.put("createby", users.getUsername());
                    param.put("xmid", folder.getId() + "");
                    param.put("xmname", field.getText());
                    Message(dialog, "updatexminfo", param);
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
     * 刷新列表
     */
    private void refresh() {
        tree.removeAll();
        CustomTreeNode root = new CustomTreeNode("我的项目");
        root.setLevel(0);
        DefaultTreeModel model = new DefaultTreeModel(root);
        tree.setModel(model);
        tree.setCellRenderer(new CustomTreeCellRenderer());
        Users users = (Users) MainUi.map.get("Users");
        Map<String, String> param = new HashMap<>();
        param.put("createby", users.getUsername());
        String request = HttpClientUtil.doGet(Data.ipPort + "findallxminfogs", param);
        List<FileOrFolder> list = JSON.parseArray(request, FileOrFolder.class);
        for (FileOrFolder folder : list) {
            CustomTreeNode node = new CustomTreeNode(folder);
            node.setLevel(1);
            Map<String, String> map = new HashMap<>();
            map.put("xmid", folder.getId() + "");
            String str = HttpClientUtil.doGet(Data.ipPort + "findbyxmid", map);
            List<ProjectFile> files = JSON.parseArray(str, ProjectFile.class);
            for (ProjectFile file : files) {
                CustomTreeNode fileNode = new CustomTreeNode(file);
                fileNode.setLevel(2);
                node.add(fileNode);
            }
            root.add(node);
        }
        //默认展开全部节点
        TreeUtil util = new TreeUtil();
        util.expandAll(tree, new TreePath(root), true);
    }

    /**
     * 提示框
     *
     * @param dialog
     * @param code
     * @param param
     */
    private void Message(JDialog dialog, String code, Map<String, String> param) {
        String request = HttpClientUtil.doGet(Data.ipPort + code, param);
        Message message = JSON.parseObject(request, Message.class);
        dialog.dispose();
        JOptionPane.showMessageDialog(frame, message.getCode(), "提示", JOptionPane.PLAIN_MESSAGE);
    }
}
