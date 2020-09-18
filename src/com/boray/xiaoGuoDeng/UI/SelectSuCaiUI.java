package com.boray.xiaoGuoDeng.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;

import com.boray.Data.Data;
import com.boray.Data.MyColor;
import com.boray.Utils.IconJDialog;
import com.boray.Utils.SuCaiUtil;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.suCai.UI.DongZuoSuCaiEditUI;
import com.boray.suCai.UI.SuCaiEditUI;
import com.boray.suCai.UI.SuCaiUI;
import com.boray.xiaoGuoDeng.Listener.SelectTonDaoOrDongZuoListener;
import com.boray.xiaoGuoDeng.Listener.xiaoGuoDengTypeListener;

public class SelectSuCaiUI {
    private IconJDialog dialog;
    private int dengZuId;//灯组id
    private String name;//面板序号
    private JRadioButton radioButton;//通道
    private JRadioButton radioButton2;//动作
    private JToggleButton[] btns;//素材类型
    private JList suCaiList;//素材列表

    public void show(JPanel pane, Boolean flag, DefineJLable defineJLable) {
        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new IconJDialog(f, true);
        dialog.setResizable(false);
        dialog.setTitle("选择素材");
        int w = 450, h = 620;
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
        dialog.setSize(w, h);
        dialog.setLayout(new FlowLayout(FlowLayout.LEFT));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        init(pane, flag, defineJLable);
        dialog.setVisible(true);
    }

    private void init(final JPanel pane, final Boolean flag, final DefineJLable defineJLable) {
        name = pane.getName();
        //左边效果按钮
        JPanel pane2 = new JPanel();
        setP1(pane2, name);

        //右边素材按钮
        JPanel p3 = new JPanel();
        setP2(p3);

        JButton sureBtn = new JButton("确定");
        sureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = Integer.valueOf(name);
                if (index % 2 != 0 && radioButton2.isSelected()) {//只放通道素材
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "该列只允许存放通道素材！", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (flag) {//新建素材
                    int c = pane.getComponentCount();
                    if (c == 20) {
                        JFrame frame = (JFrame) MainUi.map.get("frame");
                        JOptionPane.showMessageDialog(frame, "最多存放20个素材！", "提示", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if (index % 2 != 0) {//新建通道素材
                        AddTonDaoSuCai(pane);
                    } else {//新建通道素材或者动作素材
                        if (radioButton2.isSelected()) {//选中动作
                            boolean exist = DongZuoSuCaiExist(pane);//动作素材是否存在
                            if (exist) {
                                JFrame frame = (JFrame) MainUi.map.get("frame");
                                JOptionPane.showMessageDialog(frame, "该列只允许存放一个动作素材！", "提示", JOptionPane.ERROR_MESSAGE);
                                return;
                            } else {
                                AddDongZuoSuCai(pane);
                            }
                        } else {//选中通道
                            AddTonDaoSuCai(pane);
                        }
                    }
                } else {//已有素材替换
                    if (index % 2 != 0) {//修改通道素材
                        defineJLable.setName("TonDao");
                        defineJLable.setBackground(Color.green);
                    } else {//修改通道素材或者动作素材
                        boolean exist = DongZuoSuCaiExist(pane);//动作素材是否存在
                        if (exist && radioButton2.isSelected() && !defineJLable.getName().equals("DongZuo")) {
                            JFrame frame = (JFrame) MainUi.map.get("frame");
                            JOptionPane.showMessageDialog(frame, "该列只允许存放一个动作素材！", "提示", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (radioButton2.isSelected()) {
                            defineJLable.setName("DongZuo");
                            defineJLable.setBackground(Color.red);
                        } else {
                            defineJLable.setName("TonDao");
                            defineJLable.setBackground(Color.green);
                        }

                    }
                    UpdateSuCai(defineJLable);
                }
                pane.updateUI();
                dialog.dispose();
            }
        });
        JButton canceBtn = new JButton("取消");
        canceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        JButton createBtn = new JButton("新建素材");
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = (JFrame) MainUi.map.get("frame");
                IconJDialog dialog = new IconJDialog(f, true);
                dialog.setResizable(false);
                dialog.setTitle("新建素材");
                int w = 380, h = 180;
                dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
                dialog.setSize(w, h);
                dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                init(dialog);
                dialog.setVisible(true);
            }
        });

        dialog.add(pane2);
        dialog.add(p3);
        dialog.add(sureBtn);
        dialog.add(canceBtn);
        dialog.add(createBtn);
    }

    public void init(final JDialog dialog) {
        JPanel p1 = new JPanel();
        p1.add(new JLabel("素材名称："));
        final JTextField field = new JTextField(15);
        p1.add(field);
        JPanel p2 = new JPanel();
        JButton btn1 = new JButton("确定");
        JButton btn2 = new JButton("取消");

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("取消".equals(e.getActionCommand())) {
                    dialog.dispose();
                } else {
                    TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(dengZuId - 1);
                    Iterator iterator = treeSet.iterator();
                    if (iterator.hasNext()) {
                        if (!"".equals(field.getText().trim())) {
                            JList list = (JList) MainUi.map.get("xiaoGuoDeng_list");
                            DefaultListModel model = (DefaultListModel) list.getModel();

                            String selectedName = "";
                            for (int i = 0; i < btns.length; i++) {
                                if (btns[i].isSelected()) {
                                    selectedName = btns[i].getText().substring(0, 2);
                                    break;
                                }
                            }

                            if (radioButton.isSelected()) {//通道
                                Map map = (Map) Data.suCaiNameMap.get(dengZuId - 1);
                                List<String> suCaiNameList = null;
                                if (map == null) {
                                    map = new HashMap();
                                    suCaiNameList = new ArrayList<>();
                                    map.put(selectedName, suCaiNameList);
                                } else {
                                    suCaiNameList = (List<String>) map.get(selectedName);
                                    if (suCaiNameList == null)
                                        suCaiNameList = new ArrayList<>();
                                }
                                int cnt = SuCaiUtil.getTonDaoSuCaiCount(dengZuId - 1);
                                if (cnt == 50) {
                                    JFrame frame = (JFrame) MainUi.map.get("frame");
                                    JOptionPane.showMessageDialog(frame, "最多只能创建50个素材！", "提示", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                if (suCaiNameList != null) {
                                    String suCaiNameAndNumber = field.getText() + "--->" + (cnt + 1);
                                    suCaiNameList.add(suCaiNameAndNumber);
                                    map.put(selectedName, suCaiNameList);
                                    Data.suCaiNameMap.put(dengZuId - 1, map);
                                    if (model == null) {
                                        model = new DefaultListModel();
                                        model.addElement(suCaiNameAndNumber);
                                        list.setModel(model);
                                    } else {
                                        model.addElement(suCaiNameAndNumber);
                                    }
                                    if (suCaiNameList.size() > 0) {
                                        list.setSelectedIndex(0);
                                    }
                                }
                            } else {//动作
                                int cnt = SuCaiUtil.getDongZuoSuCaiCount();
                                if (cnt == 255) {
                                    JFrame frame = (JFrame) MainUi.map.get("frame");
                                    JOptionPane.showMessageDialog(frame, "最多只能创建255个素材！", "提示", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                String suCaiNameAndNumber = field.getText() + "--->" + (cnt + 1);
                                List<String> suCaiNameList = (List<String>) Data.SuCaiDongZuoName.get(selectedName);
                                if (suCaiNameList != null) {
                                    suCaiNameList.add(suCaiNameAndNumber);
                                } else {
                                    suCaiNameList = new ArrayList<>();
                                    suCaiNameList.add(suCaiNameAndNumber);
                                    Data.SuCaiDongZuoName.put(selectedName, suCaiNameList);
                                }
                                list.setSelectedIndex(model.getSize() - 1);
                            }
                            neatenSuCaiTypeBtns();
                            getSuCaiList();
                            dialog.dispose();
                        }
                    } else {
                        JFrame frame = (JFrame) MainUi.map.get("frame");
                        JOptionPane.showMessageDialog(frame, "该灯具无组内灯具，无法创建素材！", "提示", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
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

    public void setP1(JPanel pane, String name) {
        pane.setBorder(new LineBorder(Color.black));
        pane.setPreferredSize(new Dimension(150, 540));
        int index = Integer.valueOf(name);
        if (index % 2 == 0) {
            dengZuId = index / 2;
        } else {
            dengZuId = (index + 1) / 2;
        }
        radioButton = new JRadioButton("通道");
        radioButton2 = new JRadioButton("动作");
        ButtonGroup groups = new ButtonGroup();
        groups.add(radioButton);
        groups.add(radioButton2);
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                neatenSuCaiTypeBtns();//展示各类型的素材数量
                getSuCaiList();//展示选中类型的素材
            }
        };
        radioButton.addActionListener(listener);
        radioButton2.addActionListener(listener);
        radioButton.setSelected(true);

        pane.add(radioButton);
        pane.add(radioButton2);

        btns = new JToggleButton[SuCaiUtil.names.length];
        xiaoGuoDengTypeListener xiaoGuoDengTypeListener = new xiaoGuoDengTypeListener(radioButton, dengZuId);
        MainUi.map.put("xiaoGuoDengTypeBtns", btns);
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < btns.length; i++) {
            btns[i] = new JToggleButton(SuCaiUtil.names[i]);
            btns[i].setName("" + i);
            btns[i].setPreferredSize(new Dimension(130, 32));
            btns[i].addActionListener(xiaoGuoDengTypeListener);
            group.add(btns[i]);
            pane.add(btns[i]);
        }
        btns[0].setSelected(true);

        neatenSuCaiTypeBtns();//展示各类型的素材数量
    }

    public void setP2(JPanel p2) {
        //素材列表
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(270, 530));
        suCaiList = new JList();
        DefaultListModel model = new DefaultListModel();
        suCaiList.setModel(model);
        MainUi.map.put("xiaoGuoDeng_list", suCaiList);
        suCaiList.setFixedCellHeight(32);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        suCaiList.setCellRenderer(renderer);
        suCaiList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        suCaiList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (suCaiList.getSelectedIndex() != -1) {
                    if (e.getClickCount() == 2) {
                        String suCaiName = suCaiList.getSelectedValue().toString();
                        int suCaiNum = Integer.valueOf(suCaiName.split("--->")[1]).intValue();
                        if (radioButton.isSelected()) {//通道
                            new SuCaiEditUI().show(suCaiName, suCaiNum, dengZuId - 1);
                        } else {//动作
                            new DongZuoSuCaiEditUI().show(suCaiName, suCaiNum);
                        }
                    }
                }
            }
        });


        suCaiList.setSelectionBackground(new Color(85, 160, 255));
        suCaiList.setSelectionForeground(Color.WHITE);
        scrollPane.setViewportView(suCaiList);

        getSuCaiList();//展示选中类型的素材

        p2.setBorder(new LineBorder(Color.black));
        p2.setPreferredSize(new Dimension(280, 540));
        p2.add(scrollPane);

    }

    public String getxiaoGuoDengDengKuName() {
        NewJTable table3 = (NewJTable) MainUi.map.get("allLightTable");//所有灯具
        String name = (String) MainUi.map.get("xiaoGuoDengIndex");
        TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(Integer.parseInt(name) - 1);
        Iterator iterator = treeSet.iterator();
        String s2 = "";
        while (iterator.hasNext()) {
            int a = (int) iterator.next();
            if (table3.getRowCount() > 0) {
                String s = table3.getValueAt(a, 0).toString();
                Integer s1 = Integer.parseInt(s.split("#")[0].substring(2));//组内灯具的灯具id
                NewJTable table_dengJu = (NewJTable) MainUi.map.get("table_dengJu");//灯具配置
                s2 = ((String) table_dengJu.getValueAt(s1 - 1, 3)).substring(2, 3);//灯库名称
            }
        }
        return s2;
    }


    /**
     * 显示当前选中类型的素材列表
     */
    public void getSuCaiList() {
        DefaultListModel model = (DefaultListModel) suCaiList.getModel();
        model.removeAllElements();
        for (int i = 0; i < btns.length; i++) {
            if (btns[i].isSelected()) {
                String selectedName = btns[i].getText().substring(0, 2);
                if (radioButton.isSelected()) {//显示通道素材
                    Map map = (Map) Data.suCaiNameMap.get(dengZuId - 1);
                    List<String> suCaiNameList = null;
                    if (map == null) {
                        map = new HashMap();
                        suCaiNameList = new ArrayList<>();
                        map.put(selectedName, suCaiNameList);
                    } else {
                        suCaiNameList = (List<String>) map.get(selectedName);
                    }
                    if (suCaiNameList != null) {
                        for (int k = 0; k < suCaiNameList.size(); k++) {
                            model.addElement(suCaiNameList.get(k));
                        }
                        if (suCaiNameList.size() > 0) {
                            suCaiList.setSelectedIndex(0);
                        }
                    }
                    break;
                } else {//显示动作素材
                    List<String> suCaiNameList = (List<String>) Data.SuCaiDongZuoName.get(selectedName);
                    if (suCaiNameList != null) {
                        for (int k = 0; k < suCaiNameList.size(); k++) {
                            model.addElement(suCaiNameList.get(k));
                        }
                        if (suCaiNameList.size() > 0) {
                            suCaiList.setSelectedIndex(0);
                        }
                    }
                }
            }
        }
    }

    /**
     * 整理素材类型数量
     */
    public void neatenSuCaiTypeBtns() {
        for (int i = 0; i < btns.length; i++) {
            String selectedName = btns[i].getText().substring(0, 2);
            List<String> suCaiNameList = null;
            if (radioButton.isSelected()) {//通道
                Map map = (Map) Data.suCaiNameMap.get(dengZuId - 1);
                if (map == null) {
                    map = new HashMap();
                    suCaiNameList = new ArrayList<>();
                    map.put(selectedName, suCaiNameList);
                } else {
                    suCaiNameList = (List<String>) map.get(selectedName);
                }
            } else {//动作
                suCaiNameList = (List<String>) Data.SuCaiDongZuoName.get(selectedName);
            }
            if (suCaiNameList != null)
                btns[i].setText(selectedName + "(" + suCaiNameList.size() + ")");
            else
                btns[i].setText(selectedName + "(0)");
        }
    }

    /**
     * 判断动作素材是否已存在
     *
     * @return
     */
    public boolean DongZuoSuCaiExist(JPanel pane) {
        boolean flag = false;
        int c = pane.getComponentCount();
        for (int i = 0; i < c; i++) {
            DefineJLable defineJLable = (DefineJLable) pane.getComponent(i);
            if (defineJLable.getName().equals("DongZuo")) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 新建通道块
     *
     * @param pane
     */
    public void AddTonDaoSuCai(JPanel pane) {
        JList list = (JList) MainUi.map.get("xiaoGuoDeng_list");
        int c = pane.getComponentCount();
        String[] strings = list.getSelectedValue().toString().split("--->");
        DefineJLable label = new DefineJLable((c + 1) + "(" + strings[1] + ")  " + strings[0] + " ", pane);
        if (c > 0) {
            DefineJLable defineJLable = (DefineJLable) pane.getComponent(c - 1);
            int x = defineJLable.getLocation().x + defineJLable.getWidth();
            int y = defineJLable.getLocation().y;
            label.setLocation(new Point(x, y));
        }
        label.setName("TonDao");
        HashMap hashMap = null;
        Integer suCaiNum = Integer.parseInt(strings[1]) - 1;
        hashMap = (HashMap) Data.SuCaiObjects[dengZuId][suCaiNum];
        if (hashMap == null) {
            hashMap = new HashMap<>();
            Data.SuCaiObjects[dengZuId][suCaiNum] = hashMap;
        }
        label.setBackground(Color.green);
        pane.add(label);
    }

    /**
     * 新建动作块
     *
     * @param pane
     */
    public void AddDongZuoSuCai(JPanel pane) {
        JList list = (JList) MainUi.map.get("xiaoGuoDeng_list");
        int c = pane.getComponentCount();
        String[] strings = list.getSelectedValue().toString().split("--->");
        DefineJLable label = new DefineJLable((c + 1) + "(" + strings[1] + ")  " + strings[0] + " ", pane);
        if (c > 0) {
            DefineJLable defineJLable = (DefineJLable) pane.getComponent(c - 1);
            int x = defineJLable.getLocation().x + defineJLable.getWidth();
            int y = defineJLable.getLocation().y;
            label.setLocation(new Point(x, y));
        }
        label.setName("DongZuo");
        HashMap hashMap = null;
        Integer suCaiNum = Integer.parseInt(strings[1]) - 1;
        hashMap = (HashMap) Data.SuCaiDongZuoObject[suCaiNum];
        if (hashMap == null) {
            hashMap = new HashMap<>();
            Data.SuCaiDongZuoObject[suCaiNum] = hashMap;
        }
        label.setBackground(Color.red);
        pane.add(label);
    }

    /**
     * 修改素材
     *
     * @param defineJLable
     */
    public void UpdateSuCai(DefineJLable defineJLable) {
        JList list = (JList) MainUi.map.get("xiaoGuoDeng_list");
        String s = defineJLable.getText().substring(0, defineJLable.getText().indexOf("("));
        String[] strings = list.getSelectedValue().toString().split("--->");
        String s1 = "";
        if (defineJLable.getText().contains("√")) {
            s1 = "√";
        } else {
            s1 = "×";
        }
        defineJLable.setText(s + "(" + strings[1] + ")  " + strings[0] + " " + s1);
    }

}
