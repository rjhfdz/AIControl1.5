package com.boray.main.Listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.*;
import com.boray.main.Util.CustomTreeCellRenderer;
import com.boray.main.Util.CustomTreeNode;
import com.boray.main.Util.MainUtil;
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
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyMenuListener implements ActionListener {

    private JFrame frame;
    private JTree tree;

    @Override
    public void actionPerformed(ActionEvent e) {
        frame = (JFrame) MainUi.map.get("frame");
        tree = (JTree) MainUi.map.get("CompanyTree");
        if (tree.getSelectionPath() != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            JMenuItem menuItem = (JMenuItem) e.getSource();
            if (node.getUserObject() instanceof FileOrFolder) {
                if (menuItem.getText().equals("�½���Ŀ")) {
                    JDialog dialog = new JDialog(frame, true);
                    dialog.setResizable(false);
                    dialog.setTitle("�½���Ŀ");
                    int w = 380, h = 180;
                    dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
                    dialog.setSize(w, h);
                    dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    addItem(dialog);
                    dialog.setVisible(true);
                } else if (menuItem.getText().equals("��Ŀ������")) {
                    JDialog dialog = new JDialog(frame, true);
                    dialog.setResizable(false);
                    dialog.setTitle("������");
                    int w = 380, h = 180;
                    dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
                    dialog.setSize(w, h);
                    dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    renameItem(dialog, node);
                    dialog.setVisible(true);
                } else if (menuItem.getText().equals("ɾ����Ŀ")) {
                    FileOrFolder folder = (FileOrFolder) node.getUserObject();
                    Users users = (Users) MainUi.map.get("Users");
                    Map<String, String> param = new HashMap<>();
                    param.put("createby", users.getUsername());
                    param.put("xmid", folder.getId() + "");
                    String request = HttpClientUtil.doGet(Data.ipPort + "deletexminfo", param);
                    Message message = JSON.parseObject(request, Message.class);
                    JOptionPane.showMessageDialog(frame, message.getCode(), "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    refresh();
                } else if (menuItem.getText().equals("�ϴ�����")) {
                    UIManager.put("FileChooser.saveButtonText", "�ϴ�");
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
                    UIManager.put("FileChooser.saveButtonText", "����");
                }else if (menuItem.getText().equals("ճ��")) {
                    FileOrFolder folder = (FileOrFolder) node.getUserObject();
                    Users users = (Users) MainUi.map.get("Users");
                    Map<String, String> param = new HashMap<>();
                    param.put("gcname", Data.tempProjectFile.getGcname());
                    param.put("gcurl", Data.tempProjectFile.getGcurl());
                    param.put("xmid", folder.getId().toString());
                    param.put("createby", users.getId());
                    String request = HttpClientUtil.doGet(Data.ipPort + "copygc", param);
                    JOptionPane.showMessageDialog(frame, request, "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    refresh();
                }
            } else if (node.getUserObject() instanceof ProjectFile) {
                ProjectFile file = (ProjectFile) node.getUserObject();
                if (menuItem.getText().equals("������")) {
                    JDialog dialog = new JDialog(frame, true);
                    dialog.setResizable(false);
                    dialog.setTitle("������");
                    int w = 380, h = 180;
                    dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
                    dialog.setSize(w, h);
                    dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    renamePrject(dialog, node);
                    dialog.setVisible(true);
                } else if (menuItem.getText().equals("ɾ������")) {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", file.getId() + "");
                    String request = HttpClientUtil.doGet(Data.ipPort + "deletegc", map);
                    Message message = JSON.parseObject(request, Message.class);
                    JOptionPane.showMessageDialog(frame, message.getCode(), "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    refresh();
                } else if (menuItem.getText().equals("���ع���")) {
                    //����ѡ�еĹ����ļ���ø����ļ���

                    CustomTreeNode folder = (CustomTreeNode) node.getParent();
                    FileOrFolder fileOrFolder = (FileOrFolder) folder.getUserObject();
                    String path = "C://Boray" + "//" + fileOrFolder.getXmname();
                    File file1 = new File(path);
                    if (!file1.exists()) {//�ж��ļ����Ƿ����
                        file1.mkdirs();
                    }
                    try {
                        File selectedFile = new File(path + "//" + file.getGcname() + ".xml");
                        URL url = new URL(HttpClientUtil.URLEncode(Data.downloadIp + file.getGcurl()));
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
                        JOptionPane.showMessageDialog(frame, "�ļ�����ʧ�ܣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                        ex.printStackTrace();
                    }
                } else if (menuItem.getText().equals("�ύ���")) {
                    Users users = (Users) MainUi.map.get("Users");
                    Map<String, String> param = new HashMap<>();
                    param.put("id", file.getId() + "");
                    param.put("username", users.getUsername());
                    String request = HttpClientUtil.doGet(Data.ipPort + "insertgx", param);
                    Message message = JSON.parseObject(request, Message.class);
                    JOptionPane.showMessageDialog(frame, message.getCode(), "��ʾ", JOptionPane.PLAIN_MESSAGE);
                }else if(menuItem.getText().equals("����")){
                    Data.tempProjectFile = file;
                }
            }
        }
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
//                    param.put("xmtype", "1");
                    Message(dialog, "xmcreategs", param);
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
     * ��ӹ���
     *
     * @param node
     * @param file
     */
    private void addProject(DefaultMutableTreeNode node, File file) {
        MainUtil util = new MainUtil();
        List<ProjectFileInfo> infos = util.tt(file, 1);
        String str = JSONArray.toJSONString(infos);
        HttpClientUtil httpsUtils = new HttpClientUtil();
        FileOrFolder folder = (FileOrFolder) node.getUserObject();
        Users users = (Users) MainUi.map.get("Users");
        Map<String, String> param = new HashMap<>();
        param.put("gcname", file.getName().substring(0, file.getName().indexOf(".")));
        param.put("username", users.getUsername());
        param.put("xmid", folder.getId() + "");
        param.put("i", "0");
        param.put("str", str);
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
        p1.add(new JLabel("�������ƣ�"));
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
     * ˢ���б�
     */
    private void refresh() {
        tree.removeAll();
        CustomTreeNode root = new CustomTreeNode("Boray");
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
