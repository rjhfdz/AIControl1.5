package com.boray.suCai.Listener;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

import com.boray.Data.Data;
import com.boray.Utils.IconJDialog;
import com.boray.dengKu.UI.NewJTable;
import com.boray.entity.Users;
import com.boray.mainUi.MainUi;
import com.boray.suCai.UI.SuCaiUI;
import com.boray.suCai.UI.YunChangJingSuCaiDialog;
import com.boray.xiaoGuoDeng.UI.DefineJLable;
import com.boray.xiaoGuoDeng.reviewBlock.TimeBlockReviewActionListener;
import com.boray.xiaoGuoDeng.reviewBlock.TimeBlockStopReviewActionListener;

public class CreateOrDelSuCaiListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        if ("新建".equals(e.getActionCommand())) {
            JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");
            if (suCaiLightType.getSelectedValue() != null) {
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
        } else if ("删除".equals(e.getActionCommand())) {
            Object[] options = {"否", "是"};
            int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "删除素材会清空对应效果灯界面对应灯组的素材，是否继续删除？", "警告",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[1]);
            if (yes == 1) {
                JList suCaiList = (JList) MainUi.map.get("suCai_list");//素材列表
                int selectIndex = suCaiList.getSelectedIndex();//获得该素材选中的灯库
                NewJTable table3 = (NewJTable) MainUi.map.get("allLightTable");//所有灯具
                NewJTable table = (NewJTable) MainUi.map.get("GroupTable");//灯具分组
                NewJTable table_dengJu = (NewJTable) MainUi.map.get("table_dengJu");//灯具配置

                List<String> list = new ArrayList<>();
                for (int i = 0; i < table.getRowCount(); i++) {
                    TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(i);
                    Iterator iterator = treeSet.iterator();
                    String s = "";
                    while (iterator.hasNext()) {
                        int a = (int) iterator.next();
                        if (table3.getRowCount() > 0) {
                            s = table3.getValueAt(a, 0).toString();
                            Integer s1 = Integer.parseInt(s.split("#")[0].substring(2));//组内灯具的灯具id
                            String s2 = ((String) table_dengJu.getValueAt(s1 - 1, 3)).split("#")[0];//灯库名称
                            int c = Integer.parseInt(s2.substring(2)) - 1;
                            if (selectIndex == c) {
                                String ss = table.getValueAt(i, 2).toString();
                                list.add(ss);
                                break;
                            }
                        }
                    }
                }
                for (int n = 0; n < table.getRowCount(); n++) {
                    for (int i = 1; i <= 24; i++) {
                        JLabel[] labels = (JLabel[]) MainUi.map.get("labels_group" + i);
                        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + i);
                        for (int j = 0; j < list.size(); j++) {
                            if (labels[n + 1].getText().equals(list.get(j))) {
                                timeBlockPanels[n + 1].removeAll();
                                timeBlockPanels[n + 1].updateUI();
                            }
                        }
                    }
                }
            }


        } else if ("重命名".equals(e.getActionCommand())) {
            JList suCaiList = (JList) MainUi.map.get("suCai_list");//素材列表
            String name = suCaiList.getSelectedValue().toString().split("--->")[0];
            JFrame f = (JFrame) MainUi.map.get("frame");
            IconJDialog dialog = new IconJDialog(f, true);
            dialog.setResizable(false);
            dialog.setTitle("重命名");
            int w = 380, h = 180;
            dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
            dialog.setSize(w, h);
            dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            reName(dialog, name);
            dialog.setVisible(true);
        } else if ("云端".equals(e.getActionCommand())) {
            Users users = (Users) MainUi.map.get("Users");
            if (users != null && users.getLoginstatus() != 0) {
                openGeRenOrTuanDui();
            } else {
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "请登录", "警告", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else if ("预览".equals(e.getActionCommand())) {
            JList suCaiList = (JList) MainUi.map.get("suCai_list");//素材列表
//            String name = suCaiList.getSelectedValue().toString().split("--->")[0];
            if (suCaiList.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "未选中素材！", "警告", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JFrame f = (JFrame) MainUi.map.get("frame");
            IconJDialog dialog = new IconJDialog(f, true);
            dialog.setResizable(false);
            dialog.setTitle("预览");
            int w = 380, h = 180;
            dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
            dialog.setSize(w, h);
            dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            reViewsDialog(dialog);
            dialog.setVisible(true);
        }
    }

    /**
     * 判断用户选择打开个人素材还是团队素材
     */
    public void openGeRenOrTuanDui() {
        final JFrame f = (JFrame) MainUi.map.get("frame");
        final IconJDialog dia = new IconJDialog(f, true);
        dia.setResizable(false);
        dia.setTitle("云端素材");
        int w = 380, h = 200;
        dia.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
        dia.setSize(w, h);
        dia.setLayout(new FlowLayout(FlowLayout.CENTER));
        dia.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        final JRadioButton radioButton = new JRadioButton("个人");
        JRadioButton radioButton2 = new JRadioButton("团队");
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton);
        group.add(radioButton2);
        radioButton.setSelected(true);
        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(300, 200));
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setVgap(40);
        flowLayout.setHgap(26);
        p1.setLayout(flowLayout);
        p1.add(radioButton);
        p1.add(new JLabel("     "));
        p1.add(radioButton2);
        JButton sureBtn = new JButton("确定");
        JButton canceBtn = new JButton("取消");
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("确定")) {
                    dia.dispose();
                    IconJDialog dialog = new IconJDialog(f, true);
                    dialog.setTitle("云端素材");
                    dialog.setResizable(false);
                    int width = 750, height = 620;
                    dialog.setSize(width, height);
                    dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - width / 2, f.getLocation().y + f.getSize().height / 2 - height / 2);
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    JPanel p = new JPanel();
                    new YunChangJingSuCaiDialog().show(p, radioButton.isSelected());
                    dialog.getContentPane().add(p);
                    dialog.setVisible(true);
                } else {
                    dia.dispose();
                }
            }
        };
        sureBtn.addActionListener(listener);
        canceBtn.addActionListener(listener);
        p1.add(sureBtn);
        p1.add(new JLabel(" "));
        p1.add(canceBtn);
        dia.add(p1);
        dia.setVisible(true);
    }

    public void getDengZuComBox(JComboBox box) {
        JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");
        int selectIndex = suCaiLightType.getSelectedIndex();//获得该素材选中的灯库
        NewJTable table3 = (NewJTable) MainUi.map.get("allLightTable");//所有灯具
        NewJTable table = (NewJTable) MainUi.map.get("GroupTable");//灯具分组
        NewJTable table_dengJu = (NewJTable) MainUi.map.get("table_dengJu");//灯具配置

        List<String> list = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(i);
            Iterator iterator = treeSet.iterator();
            String s = "";
            while (iterator.hasNext()) {
                int a = (int) iterator.next();
                if (table3.getRowCount() > 0) {
                    s = table3.getValueAt(a, 0).toString();
                    Integer s1 = Integer.parseInt(s.split("#")[0].substring(2));//组内灯具的灯具id
                    String s2 = ((String) table_dengJu.getValueAt(s1 - 1, 3)).split("#")[0];//灯库名称
                    int c = Integer.parseInt(s2.substring(2)) - 1;
                    if (selectIndex == c) {
                        String ss = table.getValueAt(i, 1) + "#" + table.getValueAt(i, 2);
                        list.add(ss);
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < list.size(); i++) {
            box.addItem(list.get(i));
        }
    }

    /**
     * 预览界面弹窗
     *
     * @param dialog
     */
    private void reViewsDialog(JDialog dialog) {
        JPanel p1 = new JPanel();
        p1.add(new JLabel("组别："));
        final JComboBox box = new JComboBox();
        getDengZuComBox(box);
        box.setPreferredSize(new Dimension(120, 35));
        p1.add(box);
        JPanel p2 = new JPanel();
        JButton btn1 = new JButton("启动预览");
        JButton btn2 = new JButton("停止预览");

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");//灯库列表
                JList suCaiList = (JList) MainUi.map.get("suCai_list");//素材列表
                int number = Integer.valueOf(suCaiList.getSelectedValue().toString().split("--->")[1]);
                HashMap hashMap = (HashMap) Data.SuCaiObjects[suCaiLightType.getSelectedIndex()][number - 1];
                if (hashMap == null) {
                    JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "该素材未进行编辑，暂无数据，无法预览！", "警告", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                TimeBlockReviewActionListener timeBlockReviewActionListener = new TimeBlockReviewActionListener(box, suCaiLightType.getSelectedIndex(), number - 1);
                timeBlockReviewActionListener.actionPerformed2();
            }
        };
        btn1.addActionListener(listener);
        TimeBlockStopReviewActionListener timeBlockStopReviewActionListener = new TimeBlockStopReviewActionListener(box, 1, 1);
        btn2.addActionListener(timeBlockStopReviewActionListener);

        p2.add(btn1);
        p2.add(new JLabel("     "));
        p2.add(btn2);

        JPanel n1 = new JPanel();
        n1.setPreferredSize(new Dimension(350, 20));
        dialog.add(n1);
        dialog.add(p1);
        dialog.add(p2);
    }

    private void reName(final JDialog dialog, final String name) {
        JPanel p1 = new JPanel();
        p1.add(new JLabel("素材名称："));
        final JTextField field = new JTextField(15);
        field.setText(name);
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
                    JList dengkuList = (JList) MainUi.map.get("suCaiLightType");//灯库列表
                    JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
                    JList suCaiList = (JList) MainUi.map.get("suCai_list");//素材列表
                    Map nameMap = (Map) Data.suCaiNameMap.get(dengkuList.getSelectedValue().toString());
                    int number = Integer.parseInt(suCaiList.getSelectedValue().toString().split("--->")[1]);//获得对应素材的编号
                    int index = suCaiList.getSelectedIndex();//获得对应下标
                    int btnIndex = 0;
                    for (int i = 0; i < btns.length; i++) {
                        if (btns[i].isSelected()) {
                            btnIndex = i;
                            break;
                        }
                    }
                    List nameList = (List) nameMap.get("" + btnIndex);
                    nameList.set(index, field.getText() + "--->" + number);
                    suCaiList.removeAll();
                    DefaultListModel model = new DefaultListModel();
                    for (int i = 0; i < nameList.size(); i++) {
                        model.addElement(nameList.get(i));
                    }
                    suCaiList.setModel(model);
                    suCaiList.setSelectedIndex(0);
                    neatenXiaoGuoDeng(field.getText(), number);
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
     * 整理效果灯编程界面原有的素材名称
     *
     * @param str
     */
    private void neatenXiaoGuoDeng(String str, int num) {
        JList dengkuList = (JList) MainUi.map.get("suCaiLightType");//灯库列表
        NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
        List<String> list = getDengZuComBox(dengkuList.getSelectedIndex());
        for (int n = 0; n < table.getRowCount(); n++) {
            boolean b = (boolean) table.getValueAt(n, 0);
            for (int i = 1; i <= 24; i++) {
                JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + i);
                JLabel[] labels = (JLabel[]) MainUi.map.get("labels_group" + i);
                if (b) {
                    if (list.contains(labels[n + 1].getText())) {
                        JPanel panel = timeBlockPanels[n + 1];
                        for (int k = 0; k < panel.getComponentCount(); k++) {
                            DefineJLable lable = (DefineJLable) panel.getComponent(k);
                            if (lable.getText().contains("(" + num + ")")) {
                                String s = lable.getText().substring(0, lable.getText().indexOf("("));
                                String s1 = "";
                                if (lable.getText().contains("√")) {
                                    s1 = "√";
                                } else {
                                    s1 = "×";
                                }
                                lable.setText(s + "(" + num + ")  " + str + " " + s1);
                            }
                        }
                        panel.updateUI();
                    }
                }
            }
        }

    }

    /**
     * 获得引用了该灯库的灯组
     *
     * @param dengKuNumber
     */
    public List<String> getDengZuComBox(int dengKuNumber) {
        int selectIndex = dengKuNumber;//获得该素材选中的灯库
        NewJTable table3 = (NewJTable) MainUi.map.get("allLightTable");//所有灯具
        NewJTable table = (NewJTable) MainUi.map.get("GroupTable");//灯具分组
        NewJTable table_dengJu = (NewJTable) MainUi.map.get("table_dengJu");//灯具配置

        List<String> list = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(i);
            Iterator iterator = treeSet.iterator();
            String s = "";
            while (iterator.hasNext()) {
                int a = (int) iterator.next();
                if (table3.getRowCount() > 0) {
                    s = table3.getValueAt(a, 0).toString();
                    Integer s1 = Integer.parseInt(s.split("#")[0].substring(2));//组内灯具的灯具id
                    String s2 = ((String) table_dengJu.getValueAt(s1 - 1, 3)).split("#")[0];//灯库名称
                    int c = Integer.parseInt(s2.substring(2)) - 1;
                    if (selectIndex == c) {
                        String ss = table.getValueAt(i, 2).toString();
                        list.add(ss);
                        break;
                    }
                }
            }
        }
        return list;
    }

    private void init(final JDialog dialog) {
        JPanel p1 = new JPanel();
        p1.add(new JLabel("素材名称："));
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
                    if (!"".equals(field.getText().trim())) {
                        JList list = (JList) MainUi.map.get("suCai_list");
                        DefaultListModel model = (DefaultListModel) list.getModel();

                        //////////////////获取素材数量
                        JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");
                        Map map2 = (Map) Data.suCaiMap.get(suCaiLightType.getSelectedValue().toString());
                        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
                        String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
                        int cnt = 0;
                        if (map2 != null) {
                            for (int i = 0; i < btns.length; i++) {
                                List abc = (List) map2.get("" + i);
                                if (abc != null) {
                                    cnt = cnt + abc.size();
                                }
                            }
                        }
                        if (cnt == 50) {
                            JFrame frame = (JFrame) MainUi.map.get("frame");
                            JOptionPane.showMessageDialog(frame, "最多只能创建50个素材！", "提示", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        String suCaiNameAndNumber = field.getText() + "--->" + (cnt + 1);
//                        Data.AddSuCaiOrder.add((suCaiLightType.getSelectedIndex()) + "#" + cnt);
                        //////////////////

                        if (model == null) {
                            model = new DefaultListModel();
                            model.addElement(suCaiNameAndNumber);
                            list.setModel(model);
                        } else {
                            model.addElement(suCaiNameAndNumber);
                        }
                        list.setSelectedIndex(model.getSize() - 1);
                        //JList suCaiLightType = (JList)MainUi.map.get("suCaiLightType");
                        Map map = (Map) Data.suCaiMap.get(suCaiLightType.getSelectedValue().toString());
                        Map nameMap = (Map) Data.suCaiNameMap.get(suCaiLightType.getSelectedValue().toString());
                        if (map == null) {
                            map = new HashMap<>();
                            Data.suCaiMap.put(suCaiLightType.getSelectedValue().toString(), map);
                        }
                        if (nameMap == null) {
                            nameMap = new HashMap<>();
                            Data.suCaiNameMap.put(suCaiLightType.getSelectedValue().toString(), nameMap);
                        }
                        //JToggleButton[] btns = (JToggleButton[])MainUi.map.get("suCaiTypeBtns");
                        //String[] name = {"默认","动感","抒情","柔和","浪漫"};
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

                        SuCaiUI suCaiUI = new SuCaiUI();

                        String aloneCount = suCaiUI.getAlone(suCaiLightType.getSelectedValue().toString());//当前灯库的素材数量
                        JLabel alone = (JLabel) MainUi.map.get("alone");
                        alone.setText(aloneCount);

                        String count = suCaiUI.getCount();//所有灯库的素材数量
                        JLabel countLabel = (JLabel) MainUi.map.get("count");
                        countLabel.setText(count);

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

    /**
     * 判断该素材是否被引用
     *
     * @return
     */
    public Boolean suCaiIsQuote(String dengkuName) {
        Boolean flag = false;
        NewJTable table = (NewJTable) MainUi.map.get("GroupTable");//获得分组列表
        for (int i = 0; i < 24; i++) {//循环所有效果灯模式
            for (int j = 0; j < table.getRowCount(); j++) {
                MainUi.map.get("timeBlockPanels_group" + j);
            }
        }
        return flag;
    }
}
