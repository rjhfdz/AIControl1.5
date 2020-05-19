package com.boray.main.UI;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.addJCheckBox.CWCheckBoxRenderer;
import com.boray.addJCheckBox.CheckBoxCellEditor;
import com.boray.dengKu.UI.NewJTable;
import com.boray.entity.FileOrFolder;
import com.boray.entity.ProjectFile;
import com.boray.entity.ProjectFileInfo;
import com.boray.entity.Users;
import com.boray.main.Listener.CompanyListener;
import com.boray.main.Listener.CompanyMenuListener;
import com.boray.main.Listener.LoginListener;
import com.boray.main.Listener.RegisterListener;
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
    private JMenuItem addFolder;
    private JMenuItem updateFolder;
    private JMenuItem deleteFolder;
    private JMenuItem addFile;
    private JMenuItem updateFile;
    private JMenuItem deleteFile;
    private JMenuItem downloadFile;
    private JMenuItem audit;
    private JMenuItem copy;
    private JMenuItem paste;

    public void show(JPanel panel) {
        if (MainUi.map.get("Users") == null) {
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
            JButton enableEdit = new JButton("�����༭");
            JButton cancelEdit = new JButton("ȡ���༭");

            popupMenu = new JPopupMenu();
            addFolder = new JMenuItem("�½���Ŀ");
            updateFolder = new JMenuItem("��Ŀ������");
            deleteFolder = new JMenuItem("ɾ����Ŀ");
            addFile = new JMenuItem("�ϴ�����");
            updateFile = new JMenuItem("������");
            deleteFile = new JMenuItem("ɾ������");
            downloadFile = new JMenuItem("���ع���");
            audit = new JMenuItem("�ύ���");
            copy = new JMenuItem("����");
            paste = new JMenuItem("ճ��");
            ButtonGroup group = new ButtonGroup();
            group.add(addFolder);group.add(updateFolder);
            group.add(deleteFolder);group.add(addFile);
            group.add(updateFile);group.add(deleteFile);
            group.add(downloadFile);group.add(audit);
            group.add(copy);group.add(paste);
            CompanyMenuListener MenuListener = new CompanyMenuListener();
            addFolder.addActionListener(MenuListener);updateFolder.addActionListener(MenuListener);
            deleteFolder.addActionListener(MenuListener);addFile.addActionListener(MenuListener);
            updateFile.addActionListener(MenuListener);deleteFile.addActionListener(MenuListener);
            downloadFile.addActionListener(MenuListener);audit.addActionListener(MenuListener);
            copy.addActionListener(MenuListener);paste.addActionListener(MenuListener);
            popupMenu.add(addFolder);popupMenu.add(updateFolder);
            popupMenu.add(deleteFolder);popupMenu.add(addFile);
            popupMenu.add(updateFile);popupMenu.add(deleteFile);
            popupMenu.add(downloadFile);popupMenu.add(audit);
            popupMenu.add(copy);popupMenu.add(paste);

            CompanyListener listener = new CompanyListener();
            refresh.addActionListener(listener);
            enableEdit.addActionListener(listener);
            cancelEdit.addActionListener(listener);

            buttonPanel.add(refresh);
            buttonPanel.add(enableEdit);
            buttonPanel.add(cancelEdit);
            JLabel editLabel = new JLabel();
            MainUi.map.put("CompanyEditLabel", editLabel);
            buttonPanel.add(editLabel);
            panel.add(buttonPanel);

            init(panel);
        }
    }

    /**
     * �����ļ��б� ��ʱ��̬
     *
     * @param pane
     */
    public void init(JPanel pane) {
        Users users = (Users) MainUi.map.get("Users");
        Map<String, String> param = new HashMap<>();
        param.put("createby", users.getUsername());
        String request = HttpClientUtil.doGet(Data.ipPort + "findallxminfogs", param);
        List<FileOrFolder> list = JSON.parseArray(request, FileOrFolder.class);
        System.out.println(request);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(300, 550));

        // �������ڵ�
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Boray");

        // ʹ�ø��ڵ㴴�������
        final JTree tree = new JTree(rootNode);

        //����ͼ����ʽ
        tree.setCellRenderer(new CustomTreeCellRenderer());

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
            rootNode.add(node);
        }

        // ��������ʾ���ڵ���
        tree.setShowsRootHandles(true);

        // �������ڵ�ɱ༭
//        tree.setEditable(true);

        // ���ýڵ�ѡ�м�����
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                System.out.println("��ǰ��ѡ�еĽڵ�: " + e.getPath());
                if (tree.getSelectionPath() != null) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
                    if (node.getUserObject() instanceof ProjectFile) {
                        ProjectFile file = (ProjectFile) node.getUserObject();
                        Map<String, String> map = new HashMap<>();
                        map.put("gcinfoid", file.getId() + "");
                        String str = HttpClientUtil.doGet(Data.ipPort + "findalldj", map);
                        List<ProjectFileInfo> infos = JSON.parseArray(str, ProjectFileInfo.class);
                        NewJTable table = (NewJTable) MainUi.map.get("CompanyTable");
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        for (int i = table.getRowCount() - 1; i >= 0; i--) {
                            model.removeRow(i);
                        }
                        String[][] data = new String[infos.size()][4];
                        for (int i = 0; i < infos.size(); i++) {
                            data[i][0] = infos.get(i).getDjname();
                            data[i][1] = infos.get(i).getDjtype();
                            data[i][2] = infos.get(i).getDmxstr();
                            data[i][3] = infos.get(i).getZytd();
                            model.addRow(data[i]);
                        }
                    }
                }
            }
        });
        tree.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                if (path == null) {  //JTree��û���κ��ѡ��
                    return;
                } else {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getSelectionPath().getLastPathComponent();
                    if(node.getUserObject() instanceof FileOrFolder){
                        tree.setSelectionPath(path);
                        if (e.getButton() == 3) {
                            addFolder.setEnabled(true);
                            updateFolder.setEnabled(true);
                            deleteFolder.setEnabled(true);
                            addFile.setEnabled(true);
                            updateFile.setEnabled(false);
                            deleteFile.setEnabled(false);
                            downloadFile.setEnabled(false);
                            audit.setEnabled(false);
                            copy.setEnabled(false);
                            paste.setEnabled(true);
                            popupMenu.show(tree, e.getX(), e.getY());
                        }
                    }else if(node.getUserObject() instanceof ProjectFile){
                        tree.setSelectionPath(path);
                        if (e.getButton() == 3) {
                            addFolder.setEnabled(false);
                            updateFolder.setEnabled(false);
                            deleteFolder.setEnabled(false);
                            addFile.setEnabled(false);
                            updateFile.setEnabled(true);
                            deleteFile.setEnabled(true);
                            downloadFile.setEnabled(true);
                            audit.setEnabled(true);
                            copy.setEnabled(true);
                            paste.setEnabled(false);
                            popupMenu.show(tree, e.getX(), e.getY());
                        }
                    }
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

        MainUi.map.put("CompanyTree", tree);

        pane.add(panel);
        JScrollPane bodyPane = new JScrollPane();
        setTable(bodyPane);
        pane.add(bodyPane);
    }

    private void setTable(JScrollPane pane) {
        Object[][] data = {};
        String[] title = {"�ƾ�����", "�ͺ�", "DMX��ʼ��ַ", "ռ��ͨ����"};
        DefaultTableModel model = new DefaultTableModel(data, title);
        NewJTable table = new NewJTable(model, 0);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        cell.setBackground(Color.white);
                        cell.setForeground(Color.black);
                    } else {
                        cell.setBackground(Color.white); //����ż���е�ɫ
                        cell.setForeground(Color.black);
                    }
                } else {
                    cell.setBackground(new Color(85, 160, 255));
                    cell.setForeground(Color.white);
                }
                return cell;
            }
        };
        for (int i = 0; i < title.length; i++) {
            table.getColumn(table.getColumnName(i)).setCellRenderer(cellRenderer);
        }
        table.setSelectionBackground(new Color(56, 117, 215));
        table.getTableHeader().setUI(new BasicTableHeaderUI());
        table.getTableHeader().setReorderingAllowed(false);
        table.setOpaque(false);
        table.setFont(new Font(Font.SERIF, Font.BOLD, 14));
        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.setRowHeight(28);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        MainUi.map.put("CompanyTable", table);
        pane.setViewportView(table);
    }
}
