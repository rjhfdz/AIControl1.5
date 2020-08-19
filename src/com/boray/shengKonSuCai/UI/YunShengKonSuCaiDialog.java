package com.boray.shengKonSuCai.UI;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.Message;
import com.boray.entity.SuCaiFile;
import com.boray.entity.Users;
import com.boray.mainUi.MainUi;
import com.boray.shengKonSuCai.Listener.ShengKonSuCaiYunTypeListener;
import com.boray.shengKonSuCai.Listener.UpLoadOrLoadShengKonSuCaiListener;
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

public class YunShengKonSuCaiDialog implements ActionListener {

    private Users users;
    private JFrame frame;
    private File file;
    private boolean flag;

    public void show(JPanel pane, boolean flag) {
        pane.setBorder(new LineBorder(Color.black));
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));

        this.flag = flag;
        users = (Users) MainUi.map.get("Users");
        frame = (JFrame) MainUi.map.get("frame");

        JScrollPane p1 = new JScrollPane();
        p1.setPreferredSize(new Dimension(200, 594));
        setP1(p1);

        JPanel p2 = new JPanel();
        TitledBorder titledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "素材类型", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p2.setBorder(titledBorder);
        p2.setPreferredSize(new Dimension(150, 586));
        setP2(p2);

        JPanel p3 = new JPanel();
        TitledBorder titledBorder1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "素材列表", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p3.setBorder(titledBorder1);
        p3.setPreferredSize(new Dimension(350, 594));
        setP3(p3);

        JList listFile = (JList) MainUi.map.get("shengKonSuCaiDengZuYun");
        listFile.setSelectedIndex(0);

        pane.add(p1);
        pane.add(p2);
        pane.add(p3);
    }

    private void setP3(JPanel p3) {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(350, 520));

        //素材列表
        final JList list = new JList();
        MainUi.map.put("shengKonSuCaiYun_list", list);
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
        bottomPanel.setPreferredSize(new Dimension(340, 40));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(-2);
        flowLayout.setHgap(-2);
        bottomPanel.setLayout(flowLayout);
        JButton newBtn = new JButton("新增");
        JButton editBtn = new JButton("重命名");
        JButton delBtn = new JButton("删除");
        JButton leadBtn = new JButton("导入");
        JButton shanchuang = new JButton("上传到团队");
        newBtn.addActionListener(this);
        editBtn.addActionListener(this);
        delBtn.addActionListener(this);
        leadBtn.addActionListener(this);
        shanchuang.addActionListener(this);
        Dimension dimension = new Dimension(55, 34);
        newBtn.setPreferredSize(dimension);
        editBtn.setPreferredSize(new Dimension(68, 34));
        delBtn.setPreferredSize(dimension);
        leadBtn.setPreferredSize(dimension);
        shanchuang.setPreferredSize(new Dimension(108, 34));
        if (flag) {
            bottomPanel.add(newBtn);
            bottomPanel.add(editBtn);
            bottomPanel.add(delBtn);
            bottomPanel.add(shanchuang);
        }
        bottomPanel.add(leadBtn);
        p3.add(bottomPanel);
    }

    private void setP2(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(-2);
        pane.setLayout(flowLayout);
        String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
        JToggleButton[] btns = new JToggleButton[name.length];
        MainUi.map.put("shengKonSuCaiYunTypeBtns", btns);
        ButtonGroup group = new ButtonGroup();
        ShengKonSuCaiYunTypeListener listener = new ShengKonSuCaiYunTypeListener(flag);
        for (int i = 0; i < btns.length; i++) {
            btns[i] = new JToggleButton(name[i]);
            btns[i].setName("" + i);
            btns[i].setPreferredSize(new Dimension(130, 32));
            btns[i].addActionListener(listener);
            group.add(btns[i]);
            pane.add(btns[i]);
        }
        btns[0].setSelected(true);
        JLabel label = new JLabel("当前灯组素材:");
        JLabel alone = new JLabel();
        MainUi.map.put("shengKonYunAlone", alone);
        JPanel jPanel = new JPanel();
        jPanel.add(label);
        jPanel.add(alone);
        JLabel label2 = new JLabel("所有灯组素材:");
        JLabel count = new JLabel(getCount());
        MainUi.map.put("shengKonYunCount", count);
        JPanel jPanel2 = new JPanel();
        jPanel2.add(label2);
        jPanel2.add(count);

        pane.add(jPanel);
        pane.add(jPanel2);
    }

    private String getAlone(int index) {
        List<SuCaiFile> listFile = (List<SuCaiFile>) MainUi.map.get("shengKonDengZuYun");
        SuCaiFile suCaiFile = listFile.get(index);
        int count = 0;
        Map<String, String> param = new HashMap<>();
        param.put("usercode", users.getUsercode());
        param.put("kuname", suCaiFile.getKuname());
        String request = "";
        if (flag) {
            request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/getgrskshucaisctypecount", param);
        } else {
            request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/gettdskshucaisctypecount", param);
        }
        List<SuCaiFile> files = JSON.parseArray(request, SuCaiFile.class);
        for (int j = 0; j < files.size(); j++) {
            count = Integer.parseInt(files.get(j).getCountsctype()) + count;
        }
        return count + "";
    }

    private String getCount() {
        List<SuCaiFile> listFile = (List<SuCaiFile>) MainUi.map.get("shengKonDengZuYun");
        Map<String, String> param = new HashMap<>();
        int count = 0;
        for (int i = 0; i < listFile.size(); i++) {
            param.put("usercode", users.getUsercode());
            param.put("kuname", listFile.get(i).getKuname());
            String request = "";
            if (flag) {
                request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/getgrskshucaisctypecount", param);
            } else {
                request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/gettdskshucaisctypecount", param);
            }
            List<SuCaiFile> suCaiFile = JSON.parseArray(request, SuCaiFile.class);
            for (int j = 0; j < suCaiFile.size(); j++) {
                count = Integer.parseInt(suCaiFile.get(j).getCountsctype()) + count;
            }
        }
        return count + "";
    }

    private void setP1(JScrollPane scrollPane) {
        final JList list = new JList();
        MainUi.map.put("shengKonSuCaiDengZuYun", list);
        list.setFixedCellHeight(32);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        list.setCellRenderer(renderer);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (list.getSelectedValue() != null) {
                    List<SuCaiFile> listFile = (List<SuCaiFile>) MainUi.map.get("shengKonDengZuYun");
                    SuCaiFile selectDengzu = listFile.get(list.getSelectedIndex());

                    Map<String, String> param = new HashMap<>();
                    param.put("usercode", users.getUsercode());
                    param.put("kuname", selectDengzu.getKuname());
                    //当前灯组素材
                    JLabel alone = (JLabel) MainUi.map.get("shengKonYunAlone");
                    alone.setText(getAlone(list.getSelectedIndex()));

                    //所有灯组素材
                    JLabel allCount = (JLabel) MainUi.map.get("shengKonYunCount");
                    allCount.setText(getCount());

                    String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
                    JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiYunTypeBtns");

                    JList list = (JList) MainUi.map.get("shengKonSuCaiYun_list");
                    DefaultListModel model = (DefaultListModel) list.getModel();
                    model.removeAllElements();
                    for (int i = 0; i < btns.length; i++) {
                        param.put("sctype", btns[i].getName());
                        String request = "";
                        if (flag) {
                            request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/getgrskshucai", param);
                        } else {
                            request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/gettdskscsctype", param);
                        }
                        List<SuCaiFile> suCaiList = JSON.parseArray(request, SuCaiFile.class);
                        if (btns[i].isSelected()) {
                            for (int j = 0; j < suCaiList.size(); j++) {
                                if (flag) {
                                    model.addElement(suCaiList.get(j).getFilename());
                                } else {
                                    model.addElement(suCaiList.get(j).getShucainame());
                                }
                            }
                        }
                        btns[i].setText(name[i] + "(" + suCaiList.size() + ")");
                        MainUi.map.put("shengKonSuCaiFileYun_list", suCaiList);
                    }
                }
            }
        });
        Map<String, String> param = new HashMap<>();
        param.put("usercode", users.getUsercode());
        String request = "";
        if (flag) {
            request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/getgrskkuname", param);
        } else {
            request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/gettdsksckuname", param);
        }
        List<SuCaiFile> listFile = JSON.parseArray(request, SuCaiFile.class);
        String[] s = new String[listFile.size()];
        for (int i = 0; i < listFile.size(); i++) {
            s[i] = listFile.get(i).getKuname();
        }
        list.setListData(s);
        MainUi.map.put("shengKonDengZuYun", listFile);
        list.setSelectionBackground(new Color(85, 160, 255));
        list.setSelectionForeground(Color.WHITE);
        scrollPane.setViewportView(list);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("新增".equals(e.getActionCommand())) {
            JFrame f = (JFrame) MainUi.map.get("frame");
            JDialog dialog = new JDialog(f, true);
            dialog.setTitle("新增");
            dialog.setResizable(false);
            int width = 280, height = 250;
            dialog.setSize(width, height);
            dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - width / 2, f.getLocation().y + f.getSize().height / 2 - height / 2);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            JPanel p = new JPanel();
            addSuCaiUI(p, dialog);
            dialog.add(p);
            dialog.setVisible(true);
        } else if ("重命名".equals(e.getActionCommand())) {
            JFrame f = (JFrame) MainUi.map.get("frame");
            JDialog dialog = new JDialog(f, true);
            dialog.setTitle("重命名");
            dialog.setResizable(false);
            int width = 280, height = 130;
            dialog.setSize(width, height);
            dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - width / 2, f.getLocation().y + f.getSize().height / 2 - height / 2);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            JPanel p = new JPanel();
            editSuCaiUI(p, dialog);
            dialog.add(p);
            dialog.setVisible(true);
        } else if ("删除".equals(e.getActionCommand())) {
            Object[] options = {"否", "是"};
            int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "是否删除素材？", "警告",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[1]);
            if (yes == 1) {
                final SuCaiFile caiFile = getSelectSuCaiFile();
                if (caiFile == null) {
                    return;
                }
                JList list1 = (JList) MainUi.map.get("shengKonSuCaiYun_list");
                DefaultListModel model = (DefaultListModel) list1.getModel();
                model.remove(list1.getSelectedIndex());
                Map<String, String> param = new HashMap<>();
                param.put("id", caiFile.getId() + "");
                String request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/deletegrskshucai", param);
                Message message = JSON.parseObject(request, Message.class);
                JOptionPane.showMessageDialog(frame, "成功", "提示", JOptionPane.PLAIN_MESSAGE);
                refresh();
            }
        } else if ("下载".equals(e.getActionCommand())) {
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
                    URL url = new URL(HttpClientUtil.URLEncode(Data.downloadIp + caiFile.getShucaifile().replace('\\', '/')));
                    HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
                    urlCon.setConnectTimeout(6000);
                    urlCon.setReadTimeout(6000);
                    int code = urlCon.getResponseCode();
                    if (code != HttpURLConnection.HTTP_OK) {
                        JOptionPane.showMessageDialog(frame, "文件下载失败！", "提示", JOptionPane.PLAIN_MESSAGE);
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
                    JOptionPane.showMessageDialog(frame, "文件下载完成！", "提示", JOptionPane.PLAIN_MESSAGE);
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(frame, "文件下载失败！", "提示", JOptionPane.PLAIN_MESSAGE);
                    e1.printStackTrace();
                }
            }
        }else if("上传到团队".equals(e.getActionCommand())) {
      	  Object[] options = {"否", "是"};
          int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "是否上传素材？", "警告",
                  JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                  null, options, options[1]);
          if (yes == 1) {
              final SuCaiFile caiFile = getSelectSuCaiFile();
              if (caiFile == null) {
                  return;
              }
              JList list1 = (JList) MainUi.map.get("shengKonSuCaiYun_list");
              DefaultListModel model = (DefaultListModel) list1.getModel();
              model.remove(list1.getSelectedIndex());
              Map<String, String> param = new HashMap<>();
              param.put("id", caiFile.getId() + "");
              param.put("usercode", users.getUsercode());
              String request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/tdsksucaiinsert", param);

              JOptionPane.showMessageDialog(frame, "上传成功", "提示", JOptionPane.PLAIN_MESSAGE);
              refresh();
          }
    }
        else if ("导入".equals(e.getActionCommand())) {
            final SuCaiFile caiFile = getSelectSuCaiFile();
            if (caiFile == null) {
                return;
            }
            String folder = System.getProperty("java.io.tmpdir");
            File file = new File(folder + caiFile.getFilename() + ".xml");
            try {
                System.out.println(Data.downloadIp + caiFile.getShucaifile().replace('\\', '/'));
                URL url = new URL(HttpClientUtil.URLEncode(Data.downloadIp + caiFile.getShucaifile().replace('\\', '/')));
                HttpURLConnection urlCon = (HttpURLConnection) url.openConnection();
                urlCon.setConnectTimeout(6000);
                urlCon.setReadTimeout(6000);
                int code = urlCon.getResponseCode();

                if (code != HttpURLConnection.HTTP_OK) {
                    JOptionPane.showMessageDialog(frame, "文件下载失败！", "提示", JOptionPane.PLAIN_MESSAGE);
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
                UpLoadOrLoadShengKonSuCaiListener listener = new UpLoadOrLoadShengKonSuCaiListener();
                listener.loadData(file);
                file.delete();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(frame, "文件导入失败！", "提示", JOptionPane.PLAIN_MESSAGE);
                e1.printStackTrace();
            }
        }
    }

    private void editSuCaiUI(JPanel pane, final JDialog dialog) {
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));
        final SuCaiFile caiFile = getSelectSuCaiFile();
        if (caiFile == null) {
            return;
        }

        JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel5.setPreferredSize(new Dimension(270, 40));
        panel5.add(new JLabel("素材名称"));
        final JTextField field3 = new JTextField(14);
        panel5.add(field3);
        field3.setText(caiFile.getFilename());

        JPanel panel4 = new JPanel();
        JButton btn = new JButton("取消");
        JButton btn1 = new JButton("确定");
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("确定".equals(e.getActionCommand())) {
                    if (field3.getText() == null || field3.getText().equals("")) {
                        JOptionPane.showMessageDialog(frame, "素材名称未填写！", "提示", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    Map<String, String> param = new HashMap<>();
                    param.put("kuname", caiFile.getKuname());
                    param.put("sctype", caiFile.getSctype());
                    param.put("suchainame", field3.getText());
                    param.put("username", users.getUsername());
                    param.put("i", "1");
                    param.put("id", caiFile.getId() + "");
                    String request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/updategrskshucainame", param);
                    Message message = JSON.parseObject(request, Message.class);
                    JOptionPane.showMessageDialog(frame, "成功", "提示", JOptionPane.PLAIN_MESSAGE);
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
        panel.add(new JLabel("灯组名称"));
        List<SuCaiFile> listFile = (List<SuCaiFile>) MainUi.map.get("shengKonDengZuYun");
        final JComboBox field = new JComboBox();
        field.setEditable(true);
        for (int i = 0; i < listFile.size(); i++) {
            field.addItem(listFile.get(i).getKuname());
        }
        panel.add(field);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel2.setPreferredSize(new Dimension(270, 40));
        panel2.add(new JLabel("素材类型"));
        final JComboBox field2 = new JComboBox();
        String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
        for (int i = 0; i < name.length; i++) {
            field2.addItem(name[i]);
        }
        panel2.add(field2);

        JPanel panel5 = new JPanel();
        panel5.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel5.setPreferredSize(new Dimension(270, 40));
        panel5.add(new JLabel("素材名称"));
        final JTextField field3 = new JTextField(14);
        panel5.add(field3);

        JPanel panel3 = new JPanel();
        panel3.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel3.setPreferredSize(new Dimension(270, 40));
        JButton button = new JButton("选择文件");
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
//                    if (file.getName().contains("(") || file.getName().contains("（") || file.getName().contains(")") || file.getName().contains("）")) {
//                        JOptionPane.showMessageDialog(frame, "文件名中不能带括号！", "提示", JOptionPane.PLAIN_MESSAGE);
//                        return;
//                    }
                    String[] s = file.toString().split("\\\\");
                    field3.setText(s[s.length - 1].split("[.]")[0]);
                    Data.yunProjectFilePath = file.getParent();
                }
            }
        });

        JPanel panel4 = new JPanel();
        JButton btn = new JButton("取消");
        JButton btn1 = new JButton("确定");
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("确定".equals(e.getActionCommand())) {
                    String str = field.getSelectedItem().toString();
                    if (str == null || str.equals("")) {
                        JOptionPane.showMessageDialog(frame, "灯组名称未填写！", "提示", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    if (field3.getText() == null || field3.getText().equals("")) {
                        JOptionPane.showMessageDialog(frame, "素材名称未填写！", "提示", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    if (file == null) {
                        JOptionPane.showMessageDialog(frame, "未选择文件！", "提示", JOptionPane.PLAIN_MESSAGE);
                        return;
                    }
                    HttpClientUtil httpsUtils = new HttpClientUtil();
                    Map<String, String> param = new HashMap<>();
                    param.put("kuname", str);
                    param.put("sctype", field2.getSelectedIndex() + "");
                    param.put("suchainame", field3.getText());
                    param.put("usercode", users.getUsercode());
                    param.put("i", "0");
                    Map<String, Object> resultMap = httpsUtils.uploadFileByHTTP(file, Data.ipPort + "/js/a/jk/insertgrskshucai", param);
                    // Message message = JSON.parseObject(resultMap.get("data").toString(), Message.class);
                    JOptionPane.showMessageDialog(frame, "成功", "提示", JOptionPane.PLAIN_MESSAGE);
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
        JList list = (JList) MainUi.map.get("shengKonSuCaiDengZuYun");
        List<SuCaiFile> listFile = (List<SuCaiFile>) MainUi.map.get("shengKonDengZuYun");
        SuCaiFile selectDengzu = listFile.get(list.getSelectedIndex());

        Map<String, String> param = new HashMap<>();
        param.put("usercode", users.getUsercode());
        param.put("kuname", selectDengzu.getKuname());

        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiYunTypeBtns");

        JList list1 = (JList) MainUi.map.get("shengKonSuCaiYun_list");
        for (int i = 0; i < btns.length; i++) {
            if (btns[i].isSelected()) {
                param.put("sctype", btns[i].getName());
                String request = "";
                if (flag) {
                    request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/getgrskshucai", param);
                } else {
                    request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/gettdskscsctype", param);
                }
                List<SuCaiFile> suCaiList = JSON.parseArray(request, SuCaiFile.class);
                suCaiFile = suCaiList.get(list1.getSelectedIndex());
            }
        }
        return suCaiFile;
    }

    public void refresh() {
        JList list = (JList) MainUi.map.get("shengKonSuCaiDengZuYun");
        list.removeAll();
        Map<String, String> param = new HashMap<>();
        param.put("usercode", users.getUsercode());
        String request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/getgrskkuname", param);
        List<SuCaiFile> listFile2 = JSON.parseArray(request, SuCaiFile.class);
        MainUi.map.put("shengKonDengZuYun", listFile2);
        String[] s = new String[listFile2.size()];
        for (int i = 0; i < listFile2.size(); i++) {
            s[i] = listFile2.get(i).getKuname();
        }
        list.setListData(s);
        if (listFile2.size() > 0) {
            list.setSelectedIndex(0);
        }
        List<SuCaiFile> listFile = (List<SuCaiFile>) MainUi.map.get("shengKonDengZuYun");
        if (list.getSelectedIndex() == -1) {
            return;
        }
        SuCaiFile selectDengzu = listFile.get(list.getSelectedIndex());


        param.put("kuname", selectDengzu.getKuname());
        //当前灯组素材
        JLabel alone = (JLabel) MainUi.map.get("shengKonYunAlone");
        alone.setText(getAlone(list.getSelectedIndex()));

        //所有灯组素材
        JLabel allCount = (JLabel) MainUi.map.get("shengKonYunCount");
        allCount.setText(getCount());

        String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiYunTypeBtns");

        JList list1 = (JList) MainUi.map.get("shengKonSuCaiYun_list");
        DefaultListModel model = (DefaultListModel) list1.getModel();
        model.removeAllElements();
        for (int i = 0; i < btns.length; i++) {
            param.put("sctype", btns[i].getName());
            request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/getgrskshucai", param);
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
        JList list = (JList) MainUi.map.get("shengKonSuCaiDengZuYun");
        List<SuCaiFile> listFile = (List<SuCaiFile>) MainUi.map.get("shengKonDengZuYun");
        SuCaiFile file = listFile.get(list.getSelectedIndex());
        Map<String, String> param = new HashMap<>();
        param.put("usercode", users.getUsercode());
        param.put("kuname", file.getKuname());
        param.put("sctype", type);
        String request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/getgrskshucai", param);
        List<SuCaiFile> suCaiList = JSON.parseArray(request, SuCaiFile.class);
        JList jList = (JList) MainUi.map.get("shengKonSuCaiYun_list");
        DefaultListModel model = (DefaultListModel) jList.getModel();
        model.removeAllElements();
        for (int j = 0; j < suCaiList.size(); j++) {
            model.addElement(suCaiList.get(j).getFilename());
        }
    }
}
