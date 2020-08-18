package com.boray.main.Listener;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.Utils.IconJDialog;
import com.boray.entity.FileOrFolder;
import com.boray.entity.Message;
import com.boray.entity.ProjectFile;
import com.boray.entity.SuCaiFile;
import com.boray.entity.Users;
import com.boray.main.Util.CustomTreeCellRenderer;
import com.boray.main.Util.CustomTreeNode;
import com.boray.main.Util.TreeUtil;
import com.boray.main.Util.Util;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.LineBorder;
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
        if (str.equals("新建项目")) {
            IconJDialog dialog = new IconJDialog(frame, true);
            dialog.setResizable(false);
            dialog.setTitle("新建项目");
            int w = 380, h = 180;
            dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
            dialog.setSize(w, h);
            dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            addItem(dialog);
            dialog.setVisible(true);
        } else if (str.equals("项目重命名")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "请选择项目！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof FileOrFolder) {
                IconJDialog dialog = new IconJDialog(frame, true);
                dialog.setResizable(false);
                dialog.setTitle("项目重命名");
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
        } else if (str.equals("删除项目")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "请选择项目！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            Object[] options = {"否", "是"};
            int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "是否删除？", "警告",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[1]);
            if (yes == 0) {
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof FileOrFolder) {
                FileOrFolder folder = (FileOrFolder) node.getUserObject();
                Users users = (Users) MainUi.map.get("Users");
                Map<String, String> param = new HashMap<>();
                param.put("createby", users.getUsername());
                param.put("xmid", folder.getId() + "");
                String request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/deletegrxm", param);
                Message message = JSON.parseObject(request, Message.class);
                JOptionPane.showMessageDialog(frame, "删除项目成功", "提示", JOptionPane.PLAIN_MESSAGE);
                refresh();
            } else {
                JOptionPane.showMessageDialog(frame, "请选择项目！", "提示", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (str.equals("上传工程")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "请选择项目！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            UIManager.put("FileChooser.saveButtonText", "上传");
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
                    if (file.getName().contains("(") || file.getName().contains("（") || file.getName().contains(")") || file.getName().contains("）")) {
                        JOptionPane.showMessageDialog(frame, "文件名中不能带括号！", "提示", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    Data.yunProjectFilePath = file.getParent();
                    try {
                        addProject(node, file);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                UIManager.put("FileChooser.saveButtonText", "保存");
            } else {
                JOptionPane.showMessageDialog(frame, "请选择项目！", "提示", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (str.equals("工程重命名")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "请选择工程！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof ProjectFile) {
                IconJDialog dialog = new IconJDialog(frame, true);
                dialog.setResizable(false);
                dialog.setTitle("工程重命名");
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
        } else if (str.equals("删除工程")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "请选择工程！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            Object[] options = {"否", "是"};
            int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "是否删除？", "警告",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[1]);
            if (yes == 0) {
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof ProjectFile) {
                ProjectFile file = (ProjectFile) node.getUserObject();
                Map<String, String> map = new HashMap<>();
                map.put("id", file.getId() + "");
                String request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/deletegrgcname", map);
                Message message = JSON.parseObject(request, Message.class);
                JOptionPane.showMessageDialog(frame, "删除工程成功", "提示", JOptionPane.PLAIN_MESSAGE);
                refresh();
            } else {
                JOptionPane.showMessageDialog(frame, "请选择工程！", "提示", JOptionPane.PLAIN_MESSAGE);
            }
        } else if (str.equals("下载工程")) {

            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "请选择工程！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            String Str = "C://Boray/";
            if (tree.getSelectionPath().toString().contains("工程")) {
                Str = Str + "个人工程/";
            } else if (tree.getSelectionPath().toString().contains("素材")) {
                if (tree.getSelectionPath().toString().contains("场景素材")) {
                    Str = Str + "个人素材/" + "场景素材/";
                } else {
                    Str = Str + "个人素材/" + "声控素材/";
                }
            }

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            DefaultMutableTreeNode node2 = (DefaultMutableTreeNode) node.getParent();
            if (node.getUserObject() instanceof ProjectFile && node2.getUserObject() instanceof FileOrFolder) {
                ProjectFile file = (ProjectFile) node.getUserObject();
                FileOrFolder fileOrFolder = (FileOrFolder) node2.getUserObject();
                Str = Str + fileOrFolder.getXmname() + "/";
                Util.downloadFile(Str,file.getGcname(),false,".xml",Data.ip + file.getGcurl().replace('\\', '/'));
                JOptionPane.showMessageDialog(frame, "文件下载完成！", "提示", JOptionPane.PLAIN_MESSAGE);
//                System.out.println(Str);
//                JFileChooser fileChooser = new JFileChooser();
//                if (!"".equals(Data.projectFilePath)) {
//                    fileChooser.setCurrentDirectory(new File(Data.projectFilePath));
//                }
//                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                fileChooser.setSelectedFile(new File(file.getGcname() + ".xml"));
//                int returnVal = fileChooser.showSaveDialog((JFrame) MainUi.map.get("frame"));
//                if (returnVal == JFileChooser.APPROVE_OPTION) {
//                    File selectedFile = fileChooser.getSelectedFile();
//                    Data.projectFilePath = selectedFile.getParent();
//                    try {
//                        URL url = new URL(HttpClientUtil.URLEncode(Data.ip + file.getGcurl().replace('\\', '/')));
//                        HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
//                        urlCon.setConnectTimeout(6000);
//                        urlCon.setReadTimeout(6000);
//                        // int code = urlCon.getResponseCode();
//                     /*   if (code != HttpURLConnection.HTTP_OK) {
//                            JOptionPane.showMessageDialog(frame, "文件下载失败！", "提示", JOptionPane.PLAIN_MESSAGE);
//                            return;
//                        }*/
//                        DataInputStream in = new DataInputStream(urlCon.getInputStream());
//                        DataOutputStream out = new DataOutputStream(new FileOutputStream(selectedFile.getAbsoluteFile()));
//                        byte[] buffer = new byte[2048];
//                        int count = 0;
//                        while ((count = in.read(buffer)) > 0) {
//                            out.write(buffer, 0, count);
//                        }
//                        if (out != null) {
//                            out.close();
//                        }
//                        if (in != null) {
//                            in.close();
//                        }
//                        JOptionPane.showMessageDialog(frame, "文件下载完成！", "提示", JOptionPane.PLAIN_MESSAGE);
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                }
            } else if (node.getUserObject() instanceof SuCaiFile) {
                SuCaiFile file = (SuCaiFile) node.getUserObject();
                Util.downloadFile(Str,file.getFilename(),false,".xml",Data.ip + file.getShucaifile().replace('\\', '/'));
                JOptionPane.showMessageDialog(frame, "文件下载完成！", "提示", JOptionPane.PLAIN_MESSAGE);
            }
        }else if(str.equals("上传资源到团队")) {
        	 if (null == tree.getSelectionPath().getLastPathComponent()) {
                 JOptionPane.showMessageDialog(frame, "请选择工程！", "提示", JOptionPane.PLAIN_MESSAGE);
                 return;
             }
        	   Users users = (Users) MainUi.map.get("Users");
             Object[] options = {"否", "是"};
             int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "是否上传？", "警告",
                     JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                     null, options, options[1]);
             if(yes==0) {
             	return;
             }
             DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
             if (node.getUserObject() instanceof ProjectFile) {
                 ProjectFile file = (ProjectFile) node.getUserObject();
                 Map<String, String> map = new HashMap<>();
                 map.put("id", file.getId() + "");
                 map.put("usercode", users.getUsercode());
                 String request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/inserttdgcinfo", map);
                System.out.println(request);
                 JOptionPane.showMessageDialog(frame, "上传工程成功", "提示", JOptionPane.PLAIN_MESSAGE);
                 refresh();
             } else {
                 JOptionPane.showMessageDialog(frame, "请选择工程！", "提示", JOptionPane.PLAIN_MESSAGE);
             }
        } 
        
        else if (str.equals("刷新")) {
            refresh();
        } else if (str.equals("项目刷新")) {
            refresh2();
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
        param.put("gcname", file.getName().substring(0, file.getName().indexOf(".")));
        param.put("usercode", users.getUsercode());
        param.put("xmid", folder.getId() + "");
        //  param.put("i", "0");
        Map<String, Object> resultMap = httpsUtils.uploadFileByHTTP(file, Data.ipPort + "/js/a/jk/insertgrgc", param);
        Message message = JSON.parseObject(resultMap.get("data").toString(), Message.class);
        JOptionPane.showMessageDialog(frame, "添加工程成功", "提示", JOptionPane.PLAIN_MESSAGE);
        refresh();

    }

    /**
     * 工程重命名
     *
     * @param dialog
     * @param node
     */
    private void renamePrject(final JDialog dialog, final DefaultMutableTreeNode node) {
        ProjectFile projectFile = (ProjectFile) node.getUserObject();
        JPanel p1 = new JPanel();
        p1.add(new JLabel("工程名称："));
        final JTextField field = new JTextField(15);
        p1.add(field);
        field.setText(projectFile.getGcname());
        JPanel p2 = new JPanel();
        JButton btn1 = new JButton("确定");
        JButton btn2 = new JButton("取消");
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("取消".equals(e.getActionCommand())) {
                    dialog.dispose();
                } else {
                    if (field.getText().equals("")) {
                        JOptionPane.showMessageDialog(frame, "工程名不能为空！", "提示", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    if (field.getText().contains("(") || field.getText().contains("（") || field.getText().contains("）") || field.getText().contains(")")) {
                        JOptionPane.showMessageDialog(frame, "文件名中不能带括号！", "提示", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    ProjectFile file = (ProjectFile) node.getUserObject();
                    Map<String, String> param = new HashMap<>();
                    param.put("id", file.getId() + "");
                    param.put("gcname", field.getText());
                    Message3(dialog, "/js/a/jk/updategrgcname", param);
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
                    if (field.getText().equals("")) {
                        JOptionPane.showMessageDialog(frame, "项目名不能为空", "提示", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    Users users = (Users) MainUi.map.get("Users");
                    Map<String, String> param = new HashMap<>();
                    param.put("xmname", field.getText());
                    param.put("usercode", users.getUsercode());
                    // param.put("xmtype", "0");
                    Message(dialog, "/js/a/jk/insertgrxm", param);
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
        FileOrFolder f = (FileOrFolder) node.getUserObject();
        field.setText(f.getXmname());
        JPanel p2 = new JPanel();
        JButton btn1 = new JButton("确定");
        JButton btn2 = new JButton("取消");
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("取消".equals(e.getActionCommand())) {
                    dialog.dispose();
                } else {
                    if (field.getText().equals("")) {
                        JOptionPane.showMessageDialog(frame, "项目名不能为空", "提示", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    FileOrFolder folder = (FileOrFolder) node.getUserObject();
                    Users users = (Users) MainUi.map.get("Users");
                    Map<String, String> param = new HashMap<>();
                    param.put("createby", users.getUsername());
                    param.put("id", folder.getId() + "");
                    param.put("xmname", field.getText());
                    Message2(dialog, "/js/a/jk/updategrxm", param);
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

        // 创建根节点
        CustomTreeNode rootNode = new CustomTreeNode("个人管理");
        rootNode.setLevel(0);

        DefaultTreeModel model = new DefaultTreeModel(rootNode);

        tree.setModel(model);
        tree.setCellRenderer(new CustomTreeCellRenderer());


        CustomTreeNode gonCheng = new CustomTreeNode("个人工程");
        gonCheng.setLevel(1);
        CustomTreeNode suCai = new CustomTreeNode("个人素材");
        suCai.setLevel(1);
        CustomTreeNode changJingSuCai = new CustomTreeNode("场景素材");
        CustomTreeNode ShengKonSuCai = new CustomTreeNode("声控素材");
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

        //默认展开全部节点
        TreeUtil util = new TreeUtil();
        util.expandAll(tree, new TreePath(rootNode), true);
    }

    private void refresh2() {
        tree.removeAll();
        CustomTreeNode root = new CustomTreeNode("团队项目");
        root.setLevel(0);
        DefaultTreeModel model = new DefaultTreeModel(root);
        tree.setModel(model);
        tree.setCellRenderer(new CustomTreeCellRenderer());
        Users users = (Users) MainUi.map.get("Users");
        Map<String, String> param = new HashMap<>();
        param.put("usercode", users.getUsercode());
        String request = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/gettdxminfo", param);
        List<FileOrFolder> list = JSON.parseArray(request, FileOrFolder.class);
        for (FileOrFolder folder : list) {
            CustomTreeNode node = new CustomTreeNode(folder);
            node.setLevel(1);
            Map<String, String> map = new HashMap<>();
            map.put("id", folder.getId() + "");
            String str = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/gettdgcinfo", map);
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
        //Message message = JSON.parseObject(request, Message.class);
        dialog.dispose();
        JOptionPane.showMessageDialog(frame, "新建项目成功", "提示", JOptionPane.PLAIN_MESSAGE);
    }

    private void Message2(JDialog dialog, String code, Map<String, String> param) {
        String request = HttpClientUtil.doGet(Data.ipPort + code, param);
        //Message message = JSON.parseObject(request, Message.class);
        dialog.dispose();
        JOptionPane.showMessageDialog(frame, "重命名成功", "提示", JOptionPane.PLAIN_MESSAGE);
    }

    private void Message3(JDialog dialog, String code, Map<String, String> param) {
        String request = HttpClientUtil.doGet(Data.ipPort + code, param);
        //Message message = JSON.parseObject(request, Message.class);
        dialog.dispose();
        JOptionPane.showMessageDialog(frame, "重命名成功", "提示", JOptionPane.PLAIN_MESSAGE);
    }
}
