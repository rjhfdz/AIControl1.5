package com.boray.main.Listener;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.Utils.IconJDialog;
import com.boray.Utils.UtilJDialog;
import com.boray.entity.*;
import com.boray.main.Util.CustomTreeCellRenderer;
import com.boray.main.Util.CustomTreeNode;
import com.boray.main.Util.TreeUtil;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ShareListener implements ActionListener {

    private JFrame frame;
    private JTree tree;
    private JTree tree2;

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        frame = (JFrame) MainUi.map.get("frame");
        tree = (JTree) MainUi.map.get("shareTree1");
        tree2 = (JTree) MainUi.map.get("shareTree2");
        String str = button.getActionCommand();
        if (str.equals("刷新")) {
            refresh();
        } else if (str.equals("增加成员")) {
            final IconJDialog dialog = new IconJDialog(frame, true);
            final JTextField field = new JTextField(12);
            ActionListener listener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equals("确定")) {
                        String s = field.getText();
                        if (s != null && s != "") {
                            Admin admin = (Admin) MainUi.map.get("admin");
                            Map<String, String> param = new HashMap<>();
                            param.put("usercode", s);
                            param.put("officecode", admin.getOfficecode());
                            String request = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/inserttuandui", param);
                            Message message = JSON.parseObject(request, Message.class);
                            if (Integer.parseInt(message.getCode()) == 0) {
                                JOptionPane.showMessageDialog(frame, "添加成功", "提示", JOptionPane.PLAIN_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(frame, "添加失败,没有该用户", "提示", JOptionPane.PLAIN_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "成员名称不能为空", "提示", JOptionPane.PLAIN_MESSAGE);
                            return;
                        }
                        refresh();
                        dialog.dispose();
                    } else {
                        dialog.dispose();
                    }
                }
            };
            UtilJDialog utilJDialog = new UtilJDialog(dialog, "增加成员", field, "用户账号", listener);
            utilJDialog.isVisible(true);
        } else if (str.equals("踢出成员")) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            Object[] options = {"否", "是"};
            int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "是否踢出该成员？", "警告",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[1]);
            if (yes == 1) {
                if (node.getUserObject() instanceof Member) {
                    Member member = (Member) node.getUserObject();
                    Map<String, String> param = new HashMap<>();
                    param.put("id", member.getId().toString());
                    String request = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/deletetuandui", param);
                    Message message = JSON.parseObject(request, Message.class);
                    if (Integer.parseInt(message.getCode()) == 0) {
                        JOptionPane.showMessageDialog(frame, "成功踢出该成员", "提示", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "踢出该成员失败", "提示", JOptionPane.PLAIN_MESSAGE);
                    }
                    refresh();
                }
            }
        }else if (str.equals("设置团队")) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree2.getSelectionPath().getLastPathComponent();
            Object[] options = {"否", "是"};
            int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "是否设置改团队？", "警告",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[1]);
            if (yes == 1) {
                if (node.getUserObject() instanceof Offerentity) {
                    Offerentity member = (Offerentity) node.getUserObject();
                    Users users = (Users) MainUi.map.get("Users");
                    Map<String, String> param = new HashMap<>();
                    param.put("officecode", member.getOfficecode().toString());
                    param.put("usercode", users.getUsercode());
                    String request = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/updatetdinfo", param);
                    JOptionPane.showMessageDialog(frame, "设置成功", "提示", JOptionPane.PLAIN_MESSAGE);
                  /*   if (Integer.parseInt(message.getCode()) == 0) {

                     } else {
                         JOptionPane.showMessageDialog(frame, "踢出该成员失败", "提示", JOptionPane.PLAIN_MESSAGE);
                     }*/
                    //refresh();
                }
            }
        }
    }

    /**
     * 根据父级工程获得对应的下载路径
     *
     * @param str
     * @return
     */
    private String getPath(String str) {
        String path = "C://Boray" + "//共享平台//";
        return path;
    }

    /**
     * 刷新列表
     */
    private void refresh() {
        tree.removeAll();
        CustomTreeNode rootNode = new CustomTreeNode("BorayShare");
        rootNode.setLevel(0);
        DefaultTreeModel model = new DefaultTreeModel(rootNode);
        tree.setModel(model);
        tree.setCellRenderer(new CustomTreeCellRenderer());
        Map<String, String> param = new HashMap<>();
        String request = HttpClientUtil.doGet(Data.ipPort + "findbygx", param);
        java.util.List<ProjectFile> list = JSON.parseArray(request, ProjectFile.class);
        for (ProjectFile file : list) {
            CustomTreeNode node = new CustomTreeNode(file);
            node.setLevel(1);
            rootNode.add(node);
        }
        TreeUtil util = new TreeUtil();
        util.expandAll(tree, new TreePath(rootNode), true);
    }
}
