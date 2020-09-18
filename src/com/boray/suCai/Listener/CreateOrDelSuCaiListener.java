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
import com.boray.Utils.SuCaiUtil;
import com.boray.xiaoGuoDeng.reviewBlock.TimeBlockReviewActionListener;
import com.boray.xiaoGuoDeng.reviewBlock.TimeBlockStopReviewActionListener;

public class CreateOrDelSuCaiListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        JRadioButton radioButton = (JRadioButton) MainUi.map.get("xiaoGuoDengSuCaiTypeButton");
        if ("新建".equals(e.getActionCommand())) {
            JFrame f = (JFrame) MainUi.map.get("frame");
            IconJDialog dialog = new IconJDialog(f, true);
            dialog.setResizable(false);
            int w = 380, h = 180;
            dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
            dialog.setSize(w, h);
            dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            if (!radioButton.isSelected()) {//判断是否选中动作按钮，选中新建动作素材，未选中新建效果灯素材
                dialog.setTitle("新建动作素材");
                init2(dialog);
                dialog.setVisible(true);
            } else {
                JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");
                if (suCaiLightType.getSelectedValue() != null) {
                    dialog.setTitle("新建素材");
                    init(dialog);
                    dialog.setVisible(true);
                }
            }
        } else if ("删除".equals(e.getActionCommand())) {
            Object[] options = {"否", "是"};
            int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "删除素材会清空对应效果灯界面对应灯组的素材，是否继续删除？", "警告",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[1]);
            if (yes == 1) {

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
            if (!radioButton.isSelected()) {
                reName2(dialog, name);
            } else {
                reName(dialog, name);
            }
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

    private void reName2(IconJDialog dialog, String name) {
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
                    if(field.getText().trim()!=null&&field.getText().trim()!="") {
                        JList suCaiList = (JList) MainUi.map.get("suCai_list");
                        int number = Integer.parseInt(suCaiList.getSelectedValue().toString().split("--->")[1]);//获得对应素材的编号
                        String name = SuCaiUtil.getXiaoGuoDengType();
                        List<String> suCaiNameList = (List<String>) Data.SuCaiDongZuoName.get(name);
                        suCaiNameList.set(suCaiList.getSelectedIndex(),field.getText()+"--->"+number);
                        Data.SuCaiDongZuoName.put(name,suCaiNameList);
                        DefaultListModel model = new DefaultListModel();
                        for (int i = 0;i<suCaiNameList.size();i++){
                            model.addElement(suCaiNameList.get(i));
                        }
                        suCaiList.setModel(model);
                        suCaiList.setSelectedIndex(0);
                        SuCaiUtil.neatenXiaoGuoDeng(field.getText(), number);
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
                    JList dengkuList = (JList) MainUi.map.get("suCaiLightType");//灯组列表
                    JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
                    JList suCaiList = (JList) MainUi.map.get("suCai_list");//素材列表
                    Map map = (Map) Data.suCaiNameMap.get(dengkuList.getSelectedIndex());
                    int number = Integer.parseInt(suCaiList.getSelectedValue().toString().split("--->")[1]);//获得对应素材的编号
                    int index = suCaiList.getSelectedIndex();//获得对应下标
                    String selectedName = SuCaiUtil.getXiaoGuoDengType();
                    List<String> suCaiNameList = (List<String>) map.get(selectedName);
                    suCaiNameList.set(index, field.getText() + "--->" + number);
                    suCaiList.removeAll();
                    DefaultListModel model = new DefaultListModel();
                    for (int i = 0; i < suCaiNameList.size(); i++) {
                        model.addElement(suCaiNameList.get(i));
                    }
                    suCaiList.setModel(model);
                    suCaiList.setSelectedIndex(0);
                    SuCaiUtil.neatenXiaoGuoDeng(field.getText(), number);
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
     * 显示效果灯新建素材界面
     *
     * @param dialog
     */
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
                        JList list = (JList) MainUi.map.get("suCai_list");//素材列表
                        DefaultListModel model = (DefaultListModel) list.getModel();

                        //////////////////获取素材数量
                        JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");//左侧灯组列表
                        Map map = (Map) Data.suCaiNameMap.get(suCaiLightType.getSelectedIndex());
                        String selectedName = SuCaiUtil.getXiaoGuoDengType();
                        SuCaiUI suCaiUI = new SuCaiUI();
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
                        int cnt = Integer.parseInt(suCaiUI.getAlone(suCaiLightType.getSelectedIndex()));
                        if (suCaiNameList != null) {
                            if (cnt == 50) {
                                JFrame frame = (JFrame) MainUi.map.get("frame");
                                JOptionPane.showMessageDialog(frame, "最多只能创建50个素材！", "提示", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            String suCaiNameAndNumber = field.getText() + "--->" + (cnt + 1);
                            suCaiNameList.add(suCaiNameAndNumber);
                            map.put(selectedName, suCaiNameList);
                            Data.suCaiNameMap.put(suCaiLightType.getSelectedIndex(), map);
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
                        SuCaiUtil.neatenSuCaiTypeBtns();

                        String aloneCount = suCaiUI.getAlone(suCaiLightType.getSelectedIndex());//当前灯库的素材数量
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
     * 显示效果灯新建动作素材界面
     *
     * @param dialog
     */
    private void init2(final JDialog dialog) {
        JPanel p1 = new JPanel();
        p1.add(new JLabel("动作名称："));
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
                        JList list = (JList) MainUi.map.get("suCai_list");
                        DefaultListModel model = (DefaultListModel) list.getModel();
                        int cnt = Integer.valueOf(SuCaiUtil.getCount());
                        if (cnt == 255) {
                            JFrame frame = (JFrame) MainUi.map.get("frame");
                            JOptionPane.showMessageDialog(frame, "最多只能创建255个素材！", "提示", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        String suCaiNameAndNumber = field.getText() + "--->" + (cnt + 1);
                        String name = SuCaiUtil.getXiaoGuoDengType();
                        List<String> suCaiNameList = (List<String>) Data.SuCaiDongZuoName.get(name);
                        if(suCaiNameList!=null){
                            suCaiNameList.add(suCaiNameAndNumber);
                        }else{
                            suCaiNameList = new ArrayList<>();
                            suCaiNameList.add(suCaiNameAndNumber);
                            Data.SuCaiDongZuoName.put(name,suCaiNameList);
                        }
                        if (model == null) {
                            model = new DefaultListModel();
                            model.addElement(suCaiNameAndNumber);
                            list.setModel(model);
                        } else {
                            model.addElement(suCaiNameAndNumber);
                        }
                        list.setSelectedIndex(model.getSize() - 1);
                        SuCaiUtil.neatenSuCaiTypeBtns();
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

}
