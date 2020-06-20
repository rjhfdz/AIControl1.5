package com.boray.main.Listener;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.Utils.IconJDialog;
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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineButtonListener implements ActionListener {

    private JFrame frame;
    private JTree tree;

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();
        frame = (JFrame) MainUi.map.get("frame");
        tree = (JTree) MainUi.map.get("mineTree");
        if (str.equals("�½���Ŀ")) {
            IconJDialog dialog = new IconJDialog(frame, true);
            dialog.setResizable(false);
            dialog.setTitle("�½���Ŀ");
            int w = 380, h = 180;
            dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
            dialog.setSize(w, h);
            dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            addItem(dialog);
            dialog.setVisible(true);
        } else if (str.equals("��Ŀ������")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "��ѡ����Ŀ��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof FileOrFolder) {
                IconJDialog dialog = new IconJDialog(frame, true);
                dialog.setResizable(false);
                dialog.setTitle("������");
                int w = 380, h = 180;
                dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
                dialog.setSize(w, h);
                dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                renameItem(dialog, node);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame, "��ѡ����Ŀ��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (str.equals("ɾ����Ŀ")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "��ѡ����Ŀ��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof FileOrFolder) {
                FileOrFolder folder = (FileOrFolder) node.getUserObject();
                Users users = (Users) MainUi.map.get("Users");
                Map<String, String> param = new HashMap<>();
                param.put("createby", users.getUsername());
                param.put("xmid", folder.getId() + "");
                String request = HttpClientUtil.doGet(Data.ipPort + "deletexminfo", param);
                Message message = JSON.parseObject(request, Message.class);
                JOptionPane.showMessageDialog(frame, message.getCode(), "��ʾ", JOptionPane.PLAIN_MESSAGE);
                refresh();
            } else {
                JOptionPane.showMessageDialog(frame, "��ѡ����Ŀ��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (str.equals("�ϴ�����")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "��ѡ����Ŀ��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            UIManager.put("FileChooser.saveButtonText", "�ϴ�");
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
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
                    if (file.getName().contains("(") || file.getName().contains("��") || file.getName().contains(")") || file.getName().contains("��")) {
                        JOptionPane.showMessageDialog(frame, "�ļ����в��ܴ����ţ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    Data.yunProjectFilePath = file.getParent();
                    try {
                        addProject(node, file);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                UIManager.put("FileChooser.saveButtonText", "����");
            } else {
                JOptionPane.showMessageDialog(frame, "��ѡ����Ŀ��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (str.equals("����������")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "��ѡ�񹤳̣�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof ProjectFile) {
                IconJDialog dialog = new IconJDialog(frame, true);
                dialog.setResizable(false);
                dialog.setTitle("������");
                int w = 380, h = 180;
                dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
                dialog.setSize(w, h);
                dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                renamePrject(dialog, node);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(frame, "��ѡ�񹤳̣�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (str.equals("ɾ������")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "��ѡ�񹤳̣�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof ProjectFile) {
                ProjectFile file = (ProjectFile) node.getUserObject();
                Map<String, String> map = new HashMap<>();
                map.put("id", file.getId() + "");
                String request = HttpClientUtil.doGet(Data.ipPort + "deletegc", map);
                Message message = JSON.parseObject(request, Message.class);
                JOptionPane.showMessageDialog(frame, message.getCode(), "��ʾ", JOptionPane.PLAIN_MESSAGE);
                refresh();
            } else {
                JOptionPane.showMessageDialog(frame, "��ѡ�񹤳̣�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (str.equals("���ع���")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "��ѡ�񹤳̣�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof ProjectFile) {
                ProjectFile file = (ProjectFile) node.getUserObject();
                JFileChooser fileChooser = new JFileChooser();
                if (!"".equals(Data.projectFilePath)) {
                    fileChooser.setCurrentDirectory(new File(Data.projectFilePath));
                }
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setSelectedFile(new File(file.getGcname() + ".xml"));
                int returnVal = fileChooser.showSaveDialog((JFrame) MainUi.map.get("frame"));
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    Data.projectFilePath = selectedFile.getParent();
                    try {
                        URL url = new URL(Data.ip + file.getGcurl());
                        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
                        urlCon.setConnectTimeout(6000);
                        urlCon.setReadTimeout(6000);
                        int code = urlCon.getResponseCode();
                        if (code != HttpURLConnection.HTTP_OK) {
                            JOptionPane.showMessageDialog(frame, "�ļ�����ʧ�ܣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                            return;
                        }
                        DataInputStream in = new DataInputStream(urlCon.getInputStream());
                        DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile.getAbsoluteFile()));
                        byte[] buffer = new byte[2048];
                        int count = 0;
                        while ((count = in.read(buffer)) > 0) {
                            out.write(buffer, 0, count);
                        }
                        if (out != null) {
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                        JOptionPane.showMessageDialog(frame, "�ļ�������ɣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } else if (str.equals("ˢ��")) {
            refresh();
        }
    }

    /**
     * ��ӹ���
     *
     * @param node
     * @param file
     */
    private void addProject(DefaultMutableTreeNode node, File file) {
        HttpClientUtil httpsUtils = new HttpClientUtil();
        FileOrFolder folder = (FileOrFolder) node.getUserObject();
        Users users = (Users) MainUi.map.get("Users");
        Map<String, String> param = new HashMap<>();
        param.put("gcname", file.getName().substring(0, file.getName().indexOf(".")));
        param.put("username", users.getUsername());
        param.put("xmid", folder.getId() + "");
        param.put("i", "0");
        Map<String, Object> resultMap = httpsUtils.uploadFileByHTTP(file, Data.ipPort + "fileUploadServletgc", param);
        Message message = JSON.parseObject(resultMap.get("data").toString(), Message.class);
        JOptionPane.showMessageDialog(frame, message.getCode(), "��ʾ", JOptionPane.PLAIN_MESSAGE);
        refresh();

    }

    /**
     * ����������
     *
     * @param dialog
     * @param node
     */
    private void renamePrject(final JDialog dialog, final DefaultMutableTreeNode node) {
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
                    if (field.getText().contains("(") || field.getText().contains("��") || field.getText().contains("��") || field.getText().contains(")")) {
                        JOptionPane.showMessageDialog(frame, "�ļ����в��ܴ����ţ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
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
     * �½���Ŀ
     *
     * @param dialog
     */
    private void addItem(final JDialog dialog) {
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
     * ��Ŀ������
     *
     * @param dialog
     */
    private void renameItem(final JDialog dialog, final DefaultMutableTreeNode node) {
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
     * ˢ���б�
     */
    private void refresh() {
        tree.removeAll();
        CustomTreeNode root = new CustomTreeNode("�ҵ���Ŀ");
        root.setLevel(0);
        DefaultTreeModel model = new DefaultTreeModel(root);
        tree.setModel(model);
        tree.setCellRenderer(new CustomTreeCellRenderer());
        Users users = (Users) MainUi.map.get("Users");
        Map<String, String> param = new HashMap<>();
        param.put("usercode", users.getUsercode());
        String request = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/getgrxm", param);
        List<FileOrFolder> list = JSON.parseArray(request, FileOrFolder.class);
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
            root.add(node);
        }
        //Ĭ��չ��ȫ���ڵ�
        TreeUtil util = new TreeUtil();
        util.expandAll(tree, new TreePath(root), true);
    }

    /**
     * ��ʾ��
     *
     * @param dialog
     * @param code
     * @param param
     */
    private void Message(JDialog dialog, String code, Map<String, String> param) {
        String request = HttpClientUtil.doGet(Data.ipPort + code, param);
        Message message = JSON.parseObject(request, Message.class);
        dialog.dispose();
        JOptionPane.showMessageDialog(frame, message.getCode(), "��ʾ", JOptionPane.PLAIN_MESSAGE);
    }
}
