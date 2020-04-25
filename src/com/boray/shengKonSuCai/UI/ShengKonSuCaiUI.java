package com.boray.shengKonSuCai.UI;

import com.boray.Data.Data;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.shengKonSuCai.Listener.CreateOrDelSuCaiListener;
import com.boray.shengKonSuCai.Listener.ShengKonSuCaiTypeListener;
import com.boray.shengKonSuCai.Listener.UpLoadOrLoadShengKonSuCaiMouseListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ShengKonSuCaiUI {
    public void show(JPanel pane) {
        pane.setBorder(new LineBorder(Color.black));
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));

        JScrollPane p1 = new JScrollPane();
        TitledBorder titledBorder2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "灯组名称", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p1.setBorder(titledBorder2);
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
        p3.setPreferredSize(new Dimension(280, 594));
        setP3(p3);

        pane.add(p1);
        pane.add(p2);
        pane.add(p3);
    }

    private void setP3(JPanel p3) {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(270, 520));

        //素材列表
        final JList list = new JList();
        MainUi.map.put("shengKonSuCai_list", list);
        list.setFixedCellHeight(32);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        list.setCellRenderer(renderer);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //String[] s = {};
        //list.setListData(s);
        DefaultListModel model = new DefaultListModel();
        list.setModel(model);
        list.setSelectionBackground(new Color(85, 160, 255));
        list.setSelectionForeground(Color.WHITE);
        //list.setOpaque(false);
        scrollPane.setViewportView(list);

        p3.add(scrollPane);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(230, 40));
        //bottomPanel.setBorder(new LineBorder(Color.black));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(-2);
        flowLayout.setHgap(-2);
        bottomPanel.setLayout(flowLayout);
        JButton newBtn = new JButton("新建");
        JButton editBtn = new JButton("编辑");
        JButton reNameBtn = new JButton("重命名");
        JButton yunBtn = new JButton("云端");
        CreateOrDelSuCaiListener listener = new CreateOrDelSuCaiListener();
        newBtn.addActionListener(listener);
        editBtn.addActionListener(listener);
        reNameBtn.addActionListener(listener);
        yunBtn.addActionListener(listener);
        Dimension dimension = new Dimension(55, 34);
        newBtn.setPreferredSize(dimension);
        editBtn.setPreferredSize(dimension);
        reNameBtn.setPreferredSize(new Dimension(68, 34));
        yunBtn.setPreferredSize(dimension);
        bottomPanel.add(newBtn);
        bottomPanel.add(editBtn);
        bottomPanel.add(reNameBtn);
        bottomPanel.add(yunBtn);
        p3.add(bottomPanel);

        //添加右键菜单
        UpLoadOrLoadShengKonSuCaiMouseListener mouseListener = new UpLoadOrLoadShengKonSuCaiMouseListener();
        list.addMouseListener(mouseListener);
    }

    private void setP2(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(-2);
        pane.setLayout(flowLayout);
        String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
        JToggleButton[] btns = new JToggleButton[name.length];
        ShengKonSuCaiTypeListener listener = new ShengKonSuCaiTypeListener();
        MainUi.map.put("shengKonSuCaiTypeBtns", btns);
        ButtonGroup group = new ButtonGroup();
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
        MainUi.map.put("shengKonAlone", alone);
        JPanel jPanel = new JPanel();
        jPanel.add(label);
        jPanel.add(alone);
        JLabel label2 = new JLabel("所有灯组素材:");
        JLabel count = new JLabel(getCount());
        MainUi.map.put("shengKonCount", count);
        JPanel jPanel2 = new JPanel();
        jPanel2.add(label2);
        jPanel2.add(count);

        pane.add(jPanel);
        pane.add(jPanel2);
    }

    private void setP1(JScrollPane scrollPane) {
        final JList list = new JList();
        MainUi.map.put("shengKonSuCaiLightType", list);
        list.setFixedCellHeight(32);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        list.setCellRenderer(renderer);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (list.getSelectedValue() != null) {

                    String aloneCount = getAlone(list.getSelectedValue().toString());//当前灯库的素材数量
                    JLabel alone = (JLabel) MainUi.map.get("shengKonAlone");
                    alone.setText(aloneCount);

                    String count = getCount();//所有灯库的素材数量
                    JLabel countLabel = (JLabel) MainUi.map.get("shengKonCount");
                    countLabel.setText(count);

                    Map map = (Map) Data.shengKonSuCaiMap.get(list.getSelectedValue().toString());
                    JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiTypeBtns");
                    String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
                    if (map != null) {
                        for (int i = 0; i < btns.length; i++) {
                            java.util.List abc = (java.util.List) map.get("" + i);
                            int size = 0;
                            if (abc != null) {
                                size = abc.size();
                            }
                            btns[i].setText(name[i] + "(" + size + ")");
                        }
                    } else {
                        for (int i = 0; i < btns.length; i++) {
                            btns[i].setText(name[i] + "(0)");
                        }
                    }
                    //
                    int selected = 0;
                    for (int i = 0; i < btns.length; i++) {
                        if (btns[i].isSelected()) {
                            selected = i;
                            break;
                        }
                    }

                    Map nameMap = (Map) Data.shengKonSuCaiNameMap.get(list.getSelectedValue().toString());
                    JList list = (JList) MainUi.map.get("shengKonSuCai_list");
                    DefaultListModel model = (DefaultListModel) list.getModel();
                    model.removeAllElements();
                    if (nameMap != null) {
                        java.util.List tmp = (List) nameMap.get("" + selected);
                        if (tmp != null) {
                            for (int i = 0; i < tmp.size(); i++) {
                                model.addElement(tmp.get(i).toString());
                            }
                            if (tmp.size() > 0) {
                                list.setSelectedIndex(0);
                            }
                        }
                    }
                }
            }
        });
        String[] s = {};
        list.setListData(s);
        list.setSelectionBackground(new Color(85, 160, 255));
        list.setSelectionForeground(Color.WHITE);
        scrollPane.setViewportView(list);
    }


    public String getCount() {
        NewJTable table = (NewJTable) MainUi.map.get("GroupTable");//灯库
        String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
        int count = 0;
        for (int i = 0; i < table.getRowCount(); i++) {
            Map map = (Map) Data.shengKonSuCaiMap.get(table.getValueAt(i, 2).toString());
            if (map != null) {
                for (int j = 0; j < name.length; j++) {
                    List abc = (List) map.get("" + j);
                    int size = 0;
                    if (abc != null) {
                        size = abc.size();
                    }
                    count += size;
                }
            }
        }
        return count + "";
    }

    public String getAlone(String selectVlaue) {
        String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
        int count = 0;
        Map map = (Map) Data.shengKonSuCaiMap.get(selectVlaue);
        if (map != null) {
            for (int j = 0; j < name.length; j++) {
                List abc = (List) map.get("" + j);
                int size = 0;
                if (abc != null) {
                    size = abc.size();
                }
                count += size;
            }
        }
        return count + "";
    }
}
