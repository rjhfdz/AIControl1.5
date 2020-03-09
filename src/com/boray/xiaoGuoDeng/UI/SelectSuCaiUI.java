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
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.boray.Data.Data;
import com.boray.Data.MyColor;
import com.boray.Data.XiaoGuoDengModel;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.suCai.UI.SuCaiEditUI;
import com.boray.xiaoGuoDeng.Listener.xiaoGuoDengTypeListener;

public class SelectSuCaiUI {
    private JDialog dialog;
    private Map map;
    private Map nameMap;
    private int dengKuId;

    public void show(JPanel pane) {
        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new JDialog(f, true);
        dialog.setResizable(false);
        dialog.setTitle("选择素材");
        int w = 450, h = 620;
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
        dialog.setSize(w, h);
        dialog.setLayout(new FlowLayout(FlowLayout.LEFT));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        init(pane);
        dialog.setVisible(true);
    }

    private void init(final JPanel pane) {
        String name = pane.getName();
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
                JList list = (JList) MainUi.map.get("xiaoGuoDeng_list");
                int c = pane.getComponentCount();
                DefineJLable label = new DefineJLable((c + 1) + "(" + list.getSelectedValue().toString().split(">")[1] + ")", pane);
                ////////////////////////////////////////////////////////×
                if (c > 0) {
                    DefineJLable defineJLable = (DefineJLable) pane.getComponent(c - 1);
                    int x = defineJLable.getLocation().x + defineJLable.getWidth();
                    int y = defineJLable.getLocation().y;
                    label.setLocation(new Point(x, y));
                }

                ////////////////////////////////////////////////////////
                HashMap hashMap = null;
                Integer denKuNum = Integer.parseInt(getxiaoGuoDengDengKuName()) - 1;
                Integer suCaiNum = Integer.parseInt(list.getSelectedValue().toString().split(">")[1]);
                hashMap = (HashMap) Data.SuCaiObjects[denKuNum][suCaiNum - 1];
                if (hashMap == null) {
                    hashMap = new HashMap<>();
                    Data.SuCaiObjects[denKuNum][suCaiNum - 1] = hashMap;
                }
//                Data.XiaoGuoDengObjects[model][grpN][blkN] = hashMap;
                ////////////////

                if (c >= 10) {
                    c = c - 10;
                }
                label.setBackground(MyColor.colors[c]);
                pane.add(label);
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
                JDialog dialog = new JDialog(f, true);
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
                    if (!"".equals(field.getText().trim())) {
                        JList list = (JList) MainUi.map.get("xiaoGuoDeng_list");
                        DefaultListModel model = (DefaultListModel) list.getModel();

                        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("xiaoGuoDengTypeBtns");
                        String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
                        int cnt = 0;
                        if (map != null) {
                            for (int i = 0; i < btns.length; i++) {
                                List abc = (List) map.get("" + i);
                                if (abc != null) {
                                    cnt = cnt + abc.size();
                                }
                            }
                        }
                        if (cnt == 30) {
                            JFrame frame = (JFrame) MainUi.map.get("frame");
                            JOptionPane.showMessageDialog(frame, "最多只能创建30个素材！", "提示", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        String suCaiNameAndNumber = field.getText() + "--->" + (cnt + 1);
                        if (model == null) {
                            model = new DefaultListModel();
                            model.addElement(suCaiNameAndNumber);
                            list.setModel(model);
                        } else {
                            model.addElement(suCaiNameAndNumber);
                        }
                        list.setSelectedIndex(model.getSize() - 1);
                        for (int i = 0; i < btns.length; i++) {
                            if (btns[i].isSelected()) {
                                List tmp = (List) map.get("" + i);
                                List nameList = (List) nameMap.get("" + i);
                                if (nameList != null) {
                                    nameList.add(suCaiNameAndNumber);
                                } else {
                                    nameList = new ArrayList<>();
                                    nameList.add(suCaiNameAndNumber);
                                    nameMap.put("" + i, nameList);
                                }
                                if (tmp == null) {
                                    tmp = new ArrayList<>();
                                }
                                tmp.add(new HashMap<>());
                                btns[i].setText(name[i] + "(" + tmp.size() + ")");
                                map.put("" + i, tmp);
                            }
                        }
                        dialog.dispose();
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
        NewJTable table3 = (NewJTable) MainUi.map.get("allLightTable");//所有灯具
        TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(Integer.parseInt(name) - 1);
        Iterator iterator = treeSet.iterator();
        MainUi.map.put("xiaoGuoDengIndex", name);
        String s = "";
        map = new HashMap();
        while (iterator.hasNext()) {
            int a = (int) iterator.next();
            if (table3.getRowCount() > 0) {
                s = table3.getValueAt(a, 0).toString();
                Integer s1 = Integer.parseInt(s.split("#")[0].substring(2));//组内灯具的灯具id
                NewJTable table_dengJu = (NewJTable) MainUi.map.get("table_dengJu");//灯具配置
                String s2 = ((String) table_dengJu.getValueAt(s1 - 1, 3)).split("#")[1];//灯库名称
                String str = ((String) table_dengJu.getValueAt(s1 - 1, 3)).split("#")[0];
                dengKuId = Integer.parseInt(str.substring(2));
                map = (Map) Data.suCaiMap.get(s2);
            }
        }

        String[] names = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
        JToggleButton[] btns = new JToggleButton[names.length];
        xiaoGuoDengTypeListener xiaoGuoDengTypeListener = new xiaoGuoDengTypeListener();
        MainUi.map.put("xiaoGuoDengTypeBtns", btns);
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < btns.length; i++) {
            btns[i] = new JToggleButton(names[i]);
            btns[i].setName("" + i);
            btns[i].setPreferredSize(new Dimension(130, 32));
            btns[i].addActionListener(xiaoGuoDengTypeListener);
            group.add(btns[i]);
            pane.add(btns[i]);
        }
        btns[0].setSelected(true);
        if (map != null) {
            for (int i = 0; i < btns.length; i++) {
                List abc = (List) map.get("" + i);
                int size = 0;
                if (abc != null) {
                    size = abc.size();
                }
                btns[i].setText(names[i] + "(" + size + ")");
            }
        } else {
            for (int i = 0; i < btns.length; i++) {
                btns[i].setText(names[i] + "(0)");
            }
        }
        btns[0].setSelected(true);
    }

    public void setP2(JPanel p2) {
        //素材列表
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(270, 530));
        final JList list = new JList();
        MainUi.map.put("xiaoGuoDeng_list", list);
        list.setFixedCellHeight(32);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        list.setCellRenderer(renderer);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (list.getSelectedIndex() != -1) {
                    if (e.getClickCount() == 2) {
                        String suCaiName = list.getSelectedValue().toString();
                        int suCaiNum = Integer.valueOf(suCaiName.split("--->")[1]).intValue();
                        new SuCaiEditUI().show(suCaiName, suCaiNum, dengKuId - 1);
                    }
                }
            }
        });
        DefaultListModel model = new DefaultListModel();

        NewJTable table3 = (NewJTable) MainUi.map.get("allLightTable");//所有灯具
        String name = (String) MainUi.map.get("xiaoGuoDengIndex");
        TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(Integer.parseInt(name) - 1);
        Iterator iterator = treeSet.iterator();
        String s = "";
        nameMap = new HashMap();
        while (iterator.hasNext()) {
            int a = (int) iterator.next();
            if (table3.getRowCount() > 0) {
                s = table3.getValueAt(a, 0).toString();
                Integer s1 = Integer.parseInt(s.split("#")[0].substring(2));//组内灯具的灯具id
                NewJTable table_dengJu = (NewJTable) MainUi.map.get("table_dengJu");//灯具配置
                String s2 = ((String) table_dengJu.getValueAt(s1 - 1, 3)).split("#")[1];//灯库名称
                nameMap = (Map) Data.suCaiNameMap.get(s2);
            }
        }
        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("xiaoGuoDengTypeBtns");
        int selected = 0;
        for (int i = 0; i < btns.length; i++) {
            if (btns[i].isSelected()) {
                selected = i;
                break;
            }
        }
        model.removeAllElements();
        if (nameMap != null) {
            List tmp = (List) nameMap.get("" + selected);
            if (tmp != null) {
                for (int i = 0; i < tmp.size(); i++) {
                    model.addElement(tmp.get(i).toString());
                }
                if (tmp.size() > 0) {
                    list.setSelectedIndex(0);
                }
            }
        }

        list.setModel(model);
        list.setSelectionBackground(new Color(85, 160, 255));
        list.setSelectionForeground(Color.WHITE);
        scrollPane.setViewportView(list);

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
}
