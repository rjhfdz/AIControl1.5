package com.boray.shengKonSuCai.Listener;

import com.boray.Data.Data;
import com.boray.Utils.IconJDialog;
import com.boray.dengKu.UI.NewJTable;
import com.boray.entity.Users;
import com.boray.mainUi.MainUi;
import com.boray.shengKon.UI.DefineJLable_shengKon2;
import com.boray.shengKonSuCai.UI.ShengKonSuCaiEditUI;
import com.boray.shengKonSuCai.UI.ShengKonSuCaiUI;
import com.boray.shengKonSuCai.UI.YunShengKonSuCaiDialog;
import com.boray.suCai.UI.YunChangJingSuCaiDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class CreateOrDelSuCaiListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("新建".equals(e.getActionCommand())) {
            JList suCaiLightType = (JList) MainUi.map.get("shengKonSuCaiLightType");
            if (suCaiLightType.getSelectedValue() != null) {
                JFrame f = (JFrame) MainUi.map.get("frame");
                int cnt = 0;//灯具数量
                TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(suCaiLightType.getSelectedIndex());
                cnt = treeSet.size();
                if (!(cnt > 0)) {
                    JOptionPane.showMessageDialog(f, "该组别还没有灯具，请添加灯具！", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
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
        } else if ("编辑".equals(e.getActionCommand())) {
            JList suCai_list = (JList) MainUi.map.get("shengKonSuCai_list");
            JList suCaiLightType = (JList) MainUi.map.get("shengKonSuCaiLightType");
            if (suCai_list.getSelectedIndex() != -1) {
                String suCaiName = suCai_list.getSelectedValue().toString();
                int denKuNum = suCaiLightType.getSelectedIndex();
                int suCaiNum = Integer.valueOf(suCaiName.split("--->")[1]).intValue();
                System.out.println(suCaiName + "" + suCaiNum + "" + denKuNum);
                new ShengKonSuCaiEditUI().show(suCaiName, suCaiNum, denKuNum);
            }
        } else if ("重命名".equals(e.getActionCommand())) {
            JList suCaiList = (JList) MainUi.map.get("shengKonSuCai_list");
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
                    new YunShengKonSuCaiDialog().show(p,radioButton.isSelected());
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
                if (e.getActionCommand().equals("确定")) {
                    JList suCaiList = (JList) MainUi.map.get("shengKonSuCai_list");
                    JList dengkuList = (JList) MainUi.map.get("shengKonSuCaiLightType");
                    JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiTypeBtns");
                    Map nameMap = (Map) Data.shengKonSuCaiNameMap.get(dengkuList.getSelectedValue().toString());
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
                    neatenShengKon(field.getText(), number);
                    dialog.dispose();
                } else if (e.getActionCommand().equals("取消")) {
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

    private void init(final JDialog dialog) {
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
                        JList list = (JList) MainUi.map.get("shengKonSuCai_list");
                        DefaultListModel model = (DefaultListModel) list.getModel();
                        ///获取素材数量
                        JList suCaiLightType = (JList) MainUi.map.get("shengKonSuCaiLightType");
                        Map map2 = (Map) Data.shengKonSuCaiMap.get(suCaiLightType.getSelectedValue().toString());
                        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiTypeBtns");
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
                        Map map = (Map) Data.shengKonSuCaiMap.get(suCaiLightType.getSelectedValue().toString());
                        Map nameMap = (Map) Data.shengKonSuCaiNameMap.get(suCaiLightType.getSelectedValue().toString());
                        if (map == null) {
                            map = new HashMap<>();
                            Data.shengKonSuCaiMap.put(suCaiLightType.getSelectedValue().toString(), map);
                        }
                        if (nameMap == null) {
                            nameMap = new HashMap<>();
                            Data.shengKonSuCaiNameMap.put(suCaiLightType.getSelectedValue().toString(), nameMap);
                        }
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
                        ShengKonSuCaiUI suCaiUI = new ShengKonSuCaiUI();
                        String aloneCount = suCaiUI.getAlone(suCaiLightType.getSelectedValue().toString());//当前灯库的素材数量
                        JLabel alone = (JLabel) MainUi.map.get("shengKonAlone");
                        alone.setText(aloneCount);

                        String count = suCaiUI.getCount();//所有灯库的素材数量
                        JLabel countLabel = (JLabel) MainUi.map.get("shengKonCount");
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
     * 声控素材重命名时同步修改声控编程界面的引用素材名称
     *
     * @param str
     * @param num
     */
    private void neatenShengKon(String str, int num) {
        NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
        for (int n = 0; n < table.getRowCount(); n++) {
            String name = table.getValueAt(n, 2).toString();//灯组名称
            for (int j = 1; j <= 16; j++) {
                JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels" + j);
                JLabel[] labels = (JLabel[]) MainUi.map.get("labels_shengKong" + j);
                for (int i = 1; i < labels.length; i++) {
                    if (name.equals(labels[i].getText())) {
                        for (int k = 0; k < timeBlockPanels[i].getComponentCount(); k++) {
                            DefineJLable_shengKon2 lable = (DefineJLable_shengKon2) timeBlockPanels[i].getComponent(k);
                            if (lable.getText().contains("(" + num + ")")) {
                                String s = lable.getText().substring(0, lable.getText().indexOf("("));
                                lable.setText(s + "(" + num + ")  " + str);
                            }
                        }
                        timeBlockPanels[i].updateUI();
                    }
                }
            }
        }
    }
}
