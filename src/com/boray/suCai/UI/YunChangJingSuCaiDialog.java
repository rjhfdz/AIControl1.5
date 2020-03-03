package com.boray.suCai.UI;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.Message;
import com.boray.entity.SuCaiFile;
import com.boray.entity.Users;
import com.boray.mainUi.MainUi;
import com.boray.suCai.Listener.ChangJingSuCaiYunTypeListener;
import com.boray.suCai.Listener.UpLoadOrLoadSuCaiListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
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

public class YunChangJingSuCaiDialog implements ActionListener {

    private Users users;
    private JFrame frame;
    private File file;

    public void show(JPanel pane) {
        pane.setBorder(new LineBorder(Color.black));
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));

        users = (Users) MainUi.map.get("Users");
        frame = (JFrame) MainUi.map.get("frame");

        JScrollPane p1 = new JScrollPane();
        p1.setPreferredSize(new Dimension(200, 594));
        setP1(p1);

        JPanel p2 = new JPanel();
        TitledBorder titledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "�ز�����", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p2.setBorder(titledBorder);
        p2.setPreferredSize(new Dimension(150, 586));
        setP2(p2);

        JPanel p3 = new JPanel();
        TitledBorder titledBorder1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "�ز��б�", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p3.setBorder(titledBorder1);
        p3.setPreferredSize(new Dimension(280, 594));
        setP3(p3);

        JList listFile = (JList) MainUi.map.get("changJingSuCaiDengKuYun");
        listFile.setSelectedIndex(0);

        pane.add(p1);
        pane.add(p2);
        pane.add(p3);
    }

    private void setP3(JPanel p3) {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(270, 520));

        //�ز��б�
        final JList list = new JList();
        MainUi.map.put("changJingSuCaiYun_list", list);
        list.setFixedCellHeight(32);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        list.setCellRenderer(renderer);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultListModel model = new DefaultListModel();
        list.setModel(model);
        list.setSelectionBackground(new Color(85, 160, 255));
        list.setSelectionForeground(Color.WHITE);
        scrollPane.setViewportView(list);

        p3.add(scrollPane);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(280, 40));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(-2);
        flowLayout.setHgap(-2);
        bottomPanel.setLayout(flowLayout);
        JButton newBtn = new JButton("����");
        JButton editBtn = new JButton("������");
        JButton delBtn = new JButton("ɾ��");
        JButton dowBtn = new JButton("����");
        JButton leadBtn = new JButton("����");
        newBtn.addActionListener(this);
        editBtn.addActionListener(this);
        delBtn.addActionListener(this);
        dowBtn.addActionListener(this);
        leadBtn.addActionListener(this);
        Dimension dimension = new Dimension(55, 34);
        newBtn.setPreferredSize(dimension);
        editBtn.setPreferredSize(new Dimension(68, 34));
        delBtn.setPreferredSize(dimension);
        dowBtn.setPreferredSize(dimension);
        leadBtn.setPreferredSize(dimension);
        bottomPanel.add(newBtn);
        bottomPanel.add(editBtn);
        bottomPanel.add(delBtn);
        bottomPanel.add(dowBtn);
        bottomPanel.add(leadBtn);
        p3.add(bottomPanel);
    }

    private void setP2(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(-2);
        pane.setLayout(flowLayout);
        String[] name = {"����", "��ҡ", "����", "���", "����", "��ܰ", "����", "�λ�", "����"};
        JToggleButton[] btns = new JToggleButton[name.length];
        MainUi.map.put("changJingSuCaiYunTypeBtns", btns);
        ButtonGroup group = new ButtonGroup();
        ChangJingSuCaiYunTypeListener listener = new ChangJingSuCaiYunTypeListener();
        for (int i = 0; i < btns.length; i++) {
            btns[i] = new JToggleButton(name[i]);
            btns[i].setName("" + i);
            btns[i].setPreferredSize(new Dimension(130, 32));
            btns[i].addActionListener(listener);
            group.add(btns[i]);
            pane.add(btns[i]);
        }
        btns[0].setSelected(true);
        JLabel label = new JLabel("��ǰ�����ز�:");
        JLabel alone = new JLabel();
        MainUi.map.put("changJingYunAlone", alone);
        JPanel jPanel = new JPanel();
        jPanel.add(label);
        jPanel.add(alone);
        JLabel label2 = new JLabel("���е����ز�:");
        JLabel count = new JLabel(getCount());
        MainUi.map.put("changJingYunCount", count);
        JPanel jPanel2 = new JPanel();
        jPanel2.add(label2);
        jPanel2.add(count);

        pane.add(jPanel);
        pane.add(jPanel2);
    }

    private void setP1(JScrollPane scrollPane) {
        final JList list = new JList();
        MainUi.map.put("changJingSuCaiDengKuYun", list);
        list.setFixedCellHeight(32);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        list.setCellRenderer(renderer);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (list.getSelectedValue() != null) {
                    java.util.List<SuCaiFile> listFile = (java.util.List<SuCaiFile>) MainUi.map.get("changJingDengKuYun");
                    SuCaiFile selectDengzu = listFile.get(list.getSelectedIndex());

                    Map<String, String> param = new HashMap<>();
                    param.put("username", users.getUsername());
                    param.put("kuname", selectDengzu.getKuname());
                    //��ǰ�����ز�
                    JLabel alone = (JLabel) MainUi.map.get("changJingYunAlone");
                    alone.setText(getAlone(list.getSelectedIndex()));

                    //���е����ز�
                    JLabel allCount = (JLabel) MainUi.map.get("changJingYunCount");
                    allCount.setText(getCount());

                    String[] name = {"����", "��ҡ", "����", "���", "����", "��ܰ", "����", "�λ�", "����"};
                    JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("changJingSuCaiYunTypeBtns");

                    JList list = (JList) MainUi.map.get("changJingSuCaiYun_list");
                    DefaultListModel model = (DefaultListModel) list.getModel();
                    model.removeAllElements();
                    for (int i = 0; i < btns.length; i++) {
                        param.put("sctype", btns[i].getName());
                        String request = HttpClientUtil.doGet(Data.ipPort + "getsc", param);
                        java.util.List<SuCaiFile> suCaiList = JSON.parseArray(request, SuCaiFile.class);
                        if (btns[i].isSelected()) {
                            for (int j = 0; j < suCaiList.size(); j++) {
                                model.addElement(suCaiList.get(j).getFilename());
                            }
                        }
                        btns[i].setText(name[i] + "(" + suCaiList.size() + ")");
                        MainUi.map.put("changJingSuCaiFileYun_list", suCaiList);
                    }
                }
            }
        });
        Map<String, String> param = new HashMap<>();
        param.put("username", users.getUsername());
        String request = HttpClientUtil.doGet(Data.ipPort + "getku", param);
        List<SuCaiFile> listFile = JSON.parseArray(request, SuCaiFile.class);
        String[] s = new String[listFile.size()];
        for (int i = 0; i < listFile.size(); i++) {
            s[i] = listFile.get(i).getKuname();
        }
        list.setListData(s);
        MainUi.map.put("changJingDengKuYun", listFile);
        list.setSelectionBackground(new Color(85, 160, 255));
        list.setSelectionForeground(Color.WHITE);
        scrollPane.setViewportView(list);
    }

    private String getAlone(int index) {
        List<SuCaiFile> listFile = (List<SuCaiFile>) MainUi.map.get("changJingDengKuYun");
        SuCaiFile suCaiFile = listFile.get(index);
        int count = 0;
        Map<String, String> param = new HashMap<>();
        param.put("username", users.getUsername());
        param.put("kuname", suCaiFile.getKuname());
        String request = HttpClientUtil.doGet(Data.ipPort + "getsctype", param);
        List<SuCaiFile> files = JSON.parseArray(request, SuCaiFile.class);
        for (int j = 0; j < files.size(); j++) {
            count = Integer.parseInt(files.get(j).getCountsctype()) + count;
        }
        return count + "";
    }

    private String getCount() {
        List<SuCaiFile> listFile = (List<SuCaiFile>) MainUi.map.get("changJingDengKuYun");
        Map<String, String> param = new HashMap<>();
        int count = 0;
        for (int i = 0; i < listFile.size(); i++) {
            param.put("username", users.getUsername());
            param.put("kuname", listFile.get(i).getKuname());
            String request = HttpClientUtil.doGet(Data.ipPort + "getsctype", param);
            List<SuCaiFile> suCaiFile = JSON.parseArray(request, SuCaiFile.class);
            for (int j = 0; j < suCaiFile.size(); j++) {
                count = Integer.parseInt(suCaiFile.get(j).getCountsctype()) + count;
            }
        }
        return count + "";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("����".equals(e.getActionCommand())) {
            JFrame f = (JFrame) MainUi.map.get("frame");
            JDialog dialog = new JDialog(f, true);
            dialog.setTitle("����");
            dialog.setResizable(false);
            int width = 280, height = 250;
            dialog.setSize(width, height);
            dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - width / 2, f.getLocation().y + f.getSize().height / 2 - height / 2);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            JPanel p = new JPanel();
            addSuCaiUI(p, dialog);
            dialog.add(p);
            dialog.setVisible(true);
        } else if ("������".equals(e.getActionCommand())) {
            JFrame f = (JFrame) MainUi.map.get("frame");
            JDialog dialog = new JDialog(f, true);
            dialog.setTitle("������");
            dialog.setResizable(false);
            int width = 280, height = 130;
            dialog.setSize(width, height);
            dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - width / 2, f.getLocation().y + f.getSize().height / 2 - height / 2);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            JPanel p = new JPanel();
            editSuCaiUI(p, dialog);
            dialog.add(p);
            dialog.setVisible(true);
        } else if ("ɾ��".equals(e.getActionCommand())) {
            Object[] options = {"��", "��"};
            int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "�Ƿ�ɾ���زģ�", "����",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[1]);
            if (yes == 1) {
                final SuCaiFile caiFile = getSelectSuCaiFile();
                if (caiFile == null) {
                    return;
                }
                JList list1 = (JList) MainUi.map.get("changJingSuCaiYun_list");
                DefaultListModel model = (DefaultListModel) list1.getModel();
                model.remove(list1.getSelectedIndex());
                Map<String, String> param = new HashMap<>();
                param.put("id", caiFile.getId() + "");
                String request = HttpClientUtil.doGet(Data.ipPort + "deletesc", param);
                Message message = JSON.parseObject(request, Message.class);
                JOptionPane.showMessageDialog(frame, message.getCode(), "��ʾ", JOptionPane.PLAIN_MESSAGE);
                refresh();
            }
        } else if ("����".equals(e.getActionCommand())) {
            final SuCaiFile caiFile = getSelectSuCaiFile();
            if (caiFile == null) {
                return;
            }
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setSelectedFile(new File(caiFile.getFilename() + ".xml"));
            int returnVal = fileChooser.showSaveDialog((JFrame) MainUi.map.get("frame"));
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try {
                    URL url = new URL(HttpClientUtil.URLEncode(Data.downloadIp + caiFile.getShucaifile()));
                    HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
                    urlCon.setConnectTimeout(6000);
                    urlCon.setReadTimeout(6000);
                    int code = urlCon.getResponseCode();
                    if (code != HttpURLConnection.HTTP_OK) {
                        JOptionPane.showMessageDialog(frame, "�ļ�����ʧ�ܣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    DataInputStream in = new DataInputStream(urlCon.getInputStream());
                    DataOutputStream out = new DataOutputStream(new FileOutputStream(file.getAbsoluteFile()));
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
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(frame, "�ļ�����ʧ�ܣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    e1.printStackTrace();
                }
            }
        } else if ("����".equals(e.getActionCommand())) {
            final SuCaiFile caiFile = getSelectSuCaiFile();
            if (caiFile == null) {
                return;
            }
            String folder = System.getProperty("java.io.tmpdir");
            File file = new File(folder+caiFile.getFilename() + ".xml");
            try {
                URL url = new URL(HttpClientUtil.URLEncode(Data.downloadIp + caiFile.getShucaifile()));
                HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
                urlCon.setConnectTimeout(6000);
                urlCon.setReadTimeout(6000);
                int code = urlCon.getResponseCode();
                if (code != HttpURLConnection.HTTP_OK) {
                    JOptionPane.showMessageDialog(frame, "�ļ�����ʧ�ܣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                DataInputStream in = new DataInputStream(urlCon.getInputStream());
                DataOutputStream out = new DataOutputStream(new FileOutputStream(file.getAbsoluteFile()));
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
                UpLoadOrLoadSuCaiListener listener = new UpLoadOrLoadSuCaiListener();
                listener.loadData(file);
                file.delete();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(frame, "�ļ�����ʧ�ܣ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                e1.printStackTrace();
            }
        }
    }

    private void editSuCaiUI(JPanel pane, JDialog dialog) {
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));
        final SuCaiFile caiFile = getSelectSuCaiFile();
        if (caiFile == null) {
            return;
        }

        JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel5.setPreferredSize(new Dimension(270, 40));
        panel5.add(new JLabel("�ز�����"));
        final JTextField field3 = new JTextField(14);
        panel5.add(field3);
        field3.setText(caiFile.getFilename());

        JPanel panel4 = new JPanel();
        JButton btn = new JButton("ȡ��");
        JButton btn1 = new JButton("ȷ��");
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("ȷ��".equals(e.getActionCommand())) {
                    if (field3.getText() == null || field3.getText().equals("")) {
                        JOptionPane.showMessageDialog(frame, "�ز�����δ��д��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    Map<String, String> param = new HashMap<>();
                    param.put("kuname", caiFile.getKuname());
                    param.put("sctype", caiFile.getSctype());
                    param.put("name", field3.getText());
                    param.put("username", users.getUsername());
                    param.put("i", "1");
                    param.put("id", caiFile.getId() + "");
                    String request = HttpClientUtil.doGet(Data.ipPort + "updatesc", param);
                    Message message = JSON.parseObject(request, Message.class);
                    JOptionPane.showMessageDialog(frame, message.getCode(), "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    refresh();
                    dialog.dispose();
                } else {
                    dialog.dispose();
                }
            }
        };
        btn.addActionListener(actionListener);
        btn1.addActionListener(actionListener);
        panel4.add(btn);
        panel4.add(new JLabel("     "));
        panel4.add(btn1);

        pane.add(panel5);
        pane.add(panel4);
    }

    private void addSuCaiUI(JPanel dialog, final JDialog jDialog) {

        dialog.setLayout(new FlowLayout(FlowLayout.LEFT));

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setPreferredSize(new Dimension(270, 40));
        panel.add(new JLabel("�ƿ�����"));
        final JTextField field = new JTextField(14);
        panel.add(field);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel2.setPreferredSize(new Dimension(270, 40));
        panel2.add(new JLabel("�ز�����"));
        final JComboBox field2 = new JComboBox();
        String[] name = {"����", "��ҡ", "����", "���", "����", "��ܰ", "����", "�λ�", "����"};
        for (int i = 0; i < name.length; i++) {
            field2.addItem(name[i]);
        }
        panel2.add(field2);

        JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel5.setPreferredSize(new Dimension(270, 40));
        panel5.add(new JLabel("�ز�����"));
        final JTextField field3 = new JTextField(14);
        panel5.add(field3);

        JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel3.setPreferredSize(new Dimension(270, 40));
        JButton button = new JButton("ѡ���ļ�");
        panel3.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    file = fileChooser.getSelectedFile();
//                    if (file.getName().contains("(") || file.getName().contains("��") || file.getName().contains(")") || file.getName().contains("��")) {
//                        JOptionPane.showMessageDialog(frame, "�ļ����в��ܴ����ţ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
//                        return;
//                    }
                    Data.yunProjectFilePath = file.getParent();
                }
            }
        });

        JPanel panel4 = new JPanel();
        JButton btn = new JButton("ȡ��");
        JButton btn1 = new JButton("ȷ��");
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("ȷ��".equals(e.getActionCommand())) {
                    if (field.getText() == null || field.getText().equals("")) {
                        JOptionPane.showMessageDialog(frame, "�ƿ�����δ��д��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    if (field3.getText() == null || field3.getText().equals("")) {
                        JOptionPane.showMessageDialog(frame, "�ز�����δ��д��", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    if (file == null) {
                        JOptionPane.showMessageDialog(frame, "δѡ���ļ���", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    HttpClientUtil httpsUtils = new HttpClientUtil();
                    Map<String, String> param = new HashMap<>();
                    param.put("kuname", field.getText());
                    param.put("sctype", field2.getSelectedIndex() + "");
                    param.put("suchainame", field3.getText());
                    param.put("username", users.getUsername());
                    param.put("i", "0");
                    Map<String, Object> resultMap = httpsUtils.uploadFileByHTTP(file, Data.ipPort + "insertsc", param);
                    Message message = JSON.parseObject(resultMap.get("data").toString(), Message.class);
                    JOptionPane.showMessageDialog(frame, message.getCode(), "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    refresh();
                    jDialog.dispose();
                } else {
                    jDialog.dispose();
                }
            }
        };
        btn.addActionListener(actionListener);
        btn1.addActionListener(actionListener);
        panel4.add(btn);
        panel4.add(new JLabel("     "));
        panel4.add(btn1);

        dialog.add(panel);
        dialog.add(panel2);
        dialog.add(panel5);
        dialog.add(panel3);
        dialog.add(panel4);
    }

    public SuCaiFile getSelectSuCaiFile() {
        SuCaiFile suCaiFile = null;
        JList list = (JList) MainUi.map.get("changJingSuCaiDengKuYun");
        List<SuCaiFile> listFile = (List<SuCaiFile>) MainUi.map.get("changJingDengKuYun");
        SuCaiFile selectDengzu = listFile.get(list.getSelectedIndex());

        Map<String, String> param = new HashMap<>();
        param.put("username", users.getUsername());
        param.put("kuname", selectDengzu.getKuname());

        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("changJingSuCaiYunTypeBtns");

        JList list1 = (JList) MainUi.map.get("changJingSuCaiYun_list");
        for (int i = 0; i < btns.length; i++) {
            if (btns[i].isSelected()) {
                param.put("sctype", btns[i].getName());
                String request = HttpClientUtil.doGet(Data.ipPort + "getsc", param);
                List<SuCaiFile> suCaiList = JSON.parseArray(request, SuCaiFile.class);
                suCaiFile = suCaiList.get(list1.getSelectedIndex());
            }
        }
        return suCaiFile;
    }

    public void refresh() {
        Map<String, String> param = new HashMap<>();
        param.put("username", users.getUsername());
        JList list = (JList) MainUi.map.get("changJingSuCaiDengKuYun");
        list.removeAll();
        String request = HttpClientUtil.doGet(Data.ipPort + "getku", param);
        List<SuCaiFile> listFile2 = JSON.parseArray(request, SuCaiFile.class);
        MainUi.map.put("changJingDengKuYun", listFile2);
        String[] s = new String[listFile2.size()];
        for (int i = 0; i < listFile2.size(); i++) {
            s[i] = listFile2.get(i).getKuname();
        }
        list.setListData(s);
        if (listFile2.size() > 0) {
            list.setSelectedIndex(0);
        }
        List<SuCaiFile> listFile = (List<SuCaiFile>) MainUi.map.get("changJingDengKuYun");
        if (list.getSelectedIndex() == -1) {
            return;
        }
        SuCaiFile selectDengzu = listFile.get(list.getSelectedIndex());

        param.put("kuname", selectDengzu.getKuname());
        //��ǰ�����ز�
        JLabel alone = (JLabel) MainUi.map.get("changJingYunAlone");
        alone.setText(getAlone(list.getSelectedIndex()));

        //���е����ز�
        JLabel allCount = (JLabel) MainUi.map.get("changJingYunCount");
        allCount.setText(getCount());

        String[] name = {"����", "��ҡ", "����", "���", "����", "��ܰ", "����", "�λ�", "����"};
        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("changJingSuCaiYunTypeBtns");

        JList list1 = (JList) MainUi.map.get("changJingSuCaiYun_list");
        DefaultListModel model = (DefaultListModel) list1.getModel();
        model.removeAllElements();
        for (int i = 0; i < btns.length; i++) {
            param.put("sctype", btns[i].getName());
            request = HttpClientUtil.doGet(Data.ipPort + "getsc", param);
            List<SuCaiFile> suCaiList = JSON.parseArray(request, SuCaiFile.class);
            if (btns[i].isSelected()) {
                for (int j = 0; j < suCaiList.size(); j++) {
                    model.addElement(suCaiList.get(j).getFilename());
                }
                if (suCaiList.size() > 0) {
                    list1.setSelectedIndex(0);
                }
                suCaiListRefresh(btns[i].getName());
            }
            btns[i].setText(name[i] + "(" + suCaiList.size() + ")");
        }
    }

    private void suCaiListRefresh(String type) {
        JList list = (JList) MainUi.map.get("changJingSuCaiDengKuYun");
        List<SuCaiFile> listFile = (List<SuCaiFile>) MainUi.map.get("changJingDengKuYun");
        SuCaiFile file = listFile.get(list.getSelectedIndex());
        Map<String, String> param = new HashMap<>();
        param.put("username", users.getUsername());
        param.put("kuname", file.getKuname());
        param.put("sctype", type);
        String request = HttpClientUtil.doGet(Data.ipPort + "getsc", param);
        List<SuCaiFile> suCaiList = JSON.parseArray(request, SuCaiFile.class);
        JList jList = (JList) MainUi.map.get("changJingSuCaiYun_list");
        DefaultListModel model = (DefaultListModel) jList.getModel();
        model.removeAllElements();
        for (int j = 0; j < suCaiList.size(); j++) {
            model.addElement(suCaiList.get(j).getFilename());
        }
    }
}