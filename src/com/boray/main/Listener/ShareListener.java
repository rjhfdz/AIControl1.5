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
        if (str.equals("ˢ��")) {
            refresh();
        } else if (str.equals("���ӳ�Ա")) {
            final IconJDialog dialog = new IconJDialog(frame, true);
            final JTextField field = new JTextField(12);
            ActionListener listener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equals("ȷ��")) {
                        String s = field.getText();
                        if (s != null && s != "") {
                            Admin admin = (Admin) MainUi.map.get("admin");
                            Map<String, String> param = new HashMap<>();
                            param.put("usercode", s);
                            param.put("officecode", admin.getOfficecode());
                            String request = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/inserttuandui", param);
                            Message message = JSON.parseObject(request, Message.class);
                            if (Integer.parseInt(message.getCode()) == 0) {
                                JOptionPane.showMessageDialog(frame, "��ӳɹ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(frame, "���ʧ��,û�и��û�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "��Ա���Ʋ���Ϊ��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                            return;
                        }
                        refresh();
                        dialog.dispose();
                    } else {
                        dialog.dispose();
                    }
                }
            };
            UtilJDialog utilJDialog = new UtilJDialog(dialog, "���ӳ�Ա", field, "�û��˺�", listener);
            utilJDialog.isVisible(true);
        } else if (str.equals("�߳���Ա")) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            Object[] options = {"��", "��"};
            int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "�Ƿ��߳��ó�Ա��", "����",
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
                        JOptionPane.showMessageDialog(frame, "�ɹ��߳��ó�Ա", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "�߳��ó�Աʧ��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    }
                    refresh();
                }
            }
        }else if (str.equals("�����Ŷ�")) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree2.getSelectionPath().getLastPathComponent();
            Object[] options = {"��", "��"};
            int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "�Ƿ����ø��Ŷӣ�", "����",
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
                    JOptionPane.showMessageDialog(frame, "���óɹ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                  /*   if (Integer.parseInt(message.getCode()) == 0) {

                     } else {
                         JOptionPane.showMessageDialog(frame, "�߳��ó�Աʧ��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                     }*/
                    //refresh();
                }
            }
        }
    }

    /**
     * ���ݸ������̻�ö�Ӧ������·��
     *
     * @param str
     * @return
     */
    private String getPath(String str) {
        String path = "C://Boray" + "//����ƽ̨//";
        return path;
    }

    /**
     * ˢ���б�
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
