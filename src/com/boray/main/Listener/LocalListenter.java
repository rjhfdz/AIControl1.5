package com.boray.main.Listener;

import com.boray.Data.Data;
import com.boray.Utils.Util;
import com.boray.entity.FileOrFolder;
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
import java.io.IOException;
import java.nio.file.Files;
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
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "��ѡ����Ŀ��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            File file = (File) node.getUserObject();
            JDialog dialog = new JDialog(frame, true);
            dialog.setResizable(false);
            dialog.setTitle("�½���Ŀ");
            int w = 380, h = 180;
            dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
            dialog.setSize(w, h);
            dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            System.out.println(file.getAbsolutePath());
            reName(dialog, file.getAbsolutePath());
            dialog.setVisible(true);

        } else if (button.getText().equals("ɾ����Ŀ")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "��ѡ����Ŀ��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            File file = (File) node.getUserObject();
            boolean flag = delFile(file);
            if (flag) {
                JOptionPane.showMessageDialog(frame, "ɾ���ɹ���", "��ʾ", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "ɾ��ʧ�ܣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
            }
            refresh();
        } else if (button.getText().equals("��ӹ���")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "��ѡ����Ŀ��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            UIManager.put("FileChooser.saveButtonText", "���");
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof File) {
                File file = (File) node.getUserObject();
                if (file.isDirectory()) {
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
                        File source = fileChooser.getSelectedFile();
                        File dest = new File(((File) node.getUserObject()).getAbsolutePath() + "//" + source.getName());
                        try {//�����ļ�
                            Files.copy(source.toPath(), dest.toPath());
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        } finally {
                            refresh();
                        }
                        JOptionPane.showMessageDialog(frame, "��ӳɹ���", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
            UIManager.put("FileChooser.saveButtonText", "����");
        } else if (button.getText().equals("����������")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "��ѡ�񹤳̣�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (((File) node.getUserObject()).isFile()) {
                File file = (File) node.getUserObject();//�ļ�
                DefaultMutableTreeNode folder = (DefaultMutableTreeNode) node.getParent();
                File fileOrFolder = (File) folder.getUserObject();//�ļ���
                JDialog dialog = new JDialog(frame, true);
                dialog.setResizable(false);
                dialog.setTitle("������");
                int w = 380, h = 180;
                dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
                dialog.setSize(w, h);
                dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                System.out.println(file.getAbsolutePath());
                rePorjectName(dialog, file.getAbsolutePath(), fileOrFolder.getAbsolutePath());
                dialog.setVisible(true);
            }
        } else if (button.getText().equals("ɾ������")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "��ѡ�񹤳̣�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            File file = (File) node.getUserObject();
            boolean flag = delFile(file);
            if (flag) {
                JOptionPane.showMessageDialog(frame, "ɾ���ɹ���", "��ʾ", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "ɾ��ʧ�ܣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
            }
            refresh();
        } else if (button.getText().equals("ˢ��")) {
            refresh();
        } else if (button.getText().equals("����")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "��ѡ�񹤳̣�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof File && ((File) node.getUserObject()).isFile()) {
                Data.tempLocalFile = (File) node.getUserObject();
            }
        } else if (button.getText().equals("ճ��")) {
            if (null == tree.getSelectionPath().getLastPathComponent()) {
                JOptionPane.showMessageDialog(frame, "��ѡ����Ŀ��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
            if (node.getUserObject() instanceof File && ((File) node.getUserObject()).isDirectory()) {
                try {//�����ļ�
                    File copy = new File(((File) node.getUserObject()).getAbsolutePath() + "//" + Data.tempLocalFile.getName());
                    Files.copy(Data.tempLocalFile.toPath(), copy.toPath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    refresh();
                }
            }
        }
    }

    /**
     * ɾ����Ŀ���߹���
     *
     * @param file
     * @return
     */
    private boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
        }
        return file.delete();
    }

    /**
     * ����������
     */
    private void rePorjectName(final JDialog dialog, final String Name, final String str) {
        JPanel p1 = new JPanel();
        p1.add(new JLabel("��������"));
        final JTextField field = new JTextField(15);
        p1.add(field);
        JPanel p2 = new JPanel();
        JButton btn1 = new JButton("ȷ��");
        JButton btn2 = new JButton("ȡ��");

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("ȡ��".equals(e.getActionCommand())) {
                    dialog.dispose();
                } else {
                    new File(Name).renameTo(new File(str + "//" + field.getText() + "." + Name.split("[.]")[1]));
                    refresh();
                    dialog.dispose();
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
     * @param path
     */
    private void reName(final JDialog dialog, final String path) {
        JPanel p1 = new JPanel();
        p1.add(new JLabel("��Ŀ���ƣ�"));
        final JTextField field = new JTextField(15);
        p1.add(field);
        JPanel p2 = new JPanel();
        JButton btn1 = new JButton("ȷ��");
        JButton btn2 = new JButton("ȡ��");

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("ȡ��".equals(e.getActionCommand())) {
                    dialog.dispose();
                } else {
                    String newName = field.getText();
                    new File(path).renameTo(new File("C://Boray//" + newName));
                    refresh();
                    dialog.dispose();
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
        DefaultMutableTreeNode root = Util.traverseFolder(file);
        DefaultTreeModel model = new DefaultTreeModel(root);
        tree.setModel(model);
        tree.setCellRenderer(new CustomTreeCellRenderer());
        //Ĭ��չ��ȫ���ڵ�
        TreeUtil util = new TreeUtil();
        util.expandAll(tree, new TreePath(root), true);
    }
}
