package com.boray.suCai.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.boray.Data.Data;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.suCai.Listener.*;
import com.boray.Utils.SuCaiUtil;

public class SuCaiUI {
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
        p3.setPreferredSize(new Dimension(350, 594));
        setP3(p3);


        pane.add(p1);
        pane.add(p2);
        pane.add(p3);
    }


    private void setP3(JPanel p3) {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(350, 520));

        //素材列表
        final JList list = new JList();
        MainUi.map.put("suCai_list", list);
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
        bottomPanel.setPreferredSize(new Dimension(340, 40));
        //bottomPanel.setBorder(new LineBorder(Color.black));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(-2);
        flowLayout.setHgap(-2);
        bottomPanel.setLayout(flowLayout);
        JButton newBtn = new JButton("新建");
        JButton editBtn = new JButton("编辑");
        JButton reNameBtn = new JButton("重命名");
        JButton upLoadBtn = new JButton("云端");
        JButton removeBtn = new JButton("删除");
        MainUi.map.put("XiaoGuoDengSuCaiUpLoadBtn", upLoadBtn);
        CreateOrDelSuCaiListener listener = new CreateOrDelSuCaiListener();
        EditListener listener2 = new EditListener();
        editBtn.addActionListener(listener2);
        newBtn.addActionListener(listener);
        reNameBtn.addActionListener(listener);
        removeBtn.addActionListener(listener);
//        ShangchuanListener shangchuang = new ShangchuanListener();
        upLoadBtn.addActionListener(listener);
        Dimension dimension = new Dimension(55, 34);
        newBtn.setPreferredSize(dimension);
        editBtn.setPreferredSize(dimension);
        reNameBtn.setPreferredSize(new Dimension(68, 34));
        upLoadBtn.setPreferredSize(dimension);
        removeBtn.setPreferredSize(dimension);
        bottomPanel.add(newBtn);
        bottomPanel.add(editBtn);
        bottomPanel.add(reNameBtn);
        bottomPanel.add(upLoadBtn);
        bottomPanel.add(removeBtn);
        p3.add(bottomPanel);

        //添加右键菜单
        UpLoadOrLoadSuCaiMouseListener mouseListener = new UpLoadOrLoadSuCaiMouseListener();
        list.addMouseListener(mouseListener);
    }

    private void setP1(JScrollPane scrollPane) {
        final JList list = new JList();
        MainUi.map.put("suCaiLightType", list);
        list.setFixedCellHeight(32);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        list.setCellRenderer(renderer);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (list.getSelectedValue() != null) {
                    JRadioButton radioButton = (JRadioButton) MainUi.map.get("xiaoGuoDengSuCaiTypeButton");
                    if (!radioButton.isSelected()) {
                        return;
                    }

                    Map map = (Map) Data.suCaiNameMap.get(list.getSelectedIndex());
                    String selectedName = SuCaiUtil.getXiaoGuoDengType();
                    List<String> suCaiNameList = null;
                    if (map == null) {
                        map = new HashMap();
                        suCaiNameList = new ArrayList<>();
                        map.put(selectedName, suCaiNameList);
                    } else {
                        suCaiNameList = (List<String>) map.get(selectedName);
                    }
                    if (suCaiNameList != null) {
                        suCaiNameList = (List<String>) map.get(selectedName);
                        JList list = (JList) MainUi.map.get("suCai_list");
                        DefaultListModel model = (DefaultListModel) list.getModel();
                        model.removeAllElements();
                        for (int i = 0; i < suCaiNameList.size(); i++) {
                            model.addElement(suCaiNameList.get(i));
                        }
                        if (suCaiNameList.size() > 0) {
                            list.setSelectedIndex(0);
                        }
                    }
                    SuCaiUtil.neatenSuCaiTypeBtns();
                }
            }
        });
        String[] s = {};
        list.setListData(s);
        list.setSelectionBackground(new Color(85, 160, 255));
        list.setSelectionForeground(Color.WHITE);
        //list.setOpaque(false);
        scrollPane.setViewportView(list);
    }

    private void setP2(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(-2);
        pane.setLayout(flowLayout);

        JRadioButton radioButton = new JRadioButton("通道");
        JRadioButton radioButton2 = new JRadioButton("动作");
        ButtonGroup groups = new ButtonGroup();
        groups.add(radioButton);
        groups.add(radioButton2);
        radioButton.setSelected(true);
        TonDaoOrDongZuoSuCaiTypeListener typeListener = new TonDaoOrDongZuoSuCaiTypeListener();
        radioButton.addActionListener(typeListener);
        radioButton2.addActionListener(typeListener);
        pane.add(radioButton);
        pane.add(radioButton2);
        MainUi.map.put("xiaoGuoDengSuCaiTypeButton", radioButton);

        String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
        JToggleButton[] btns = new JToggleButton[name.length];
        SuCaiTypeListener listener = new SuCaiTypeListener();
        MainUi.map.put("suCaiTypeBtns", btns);
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
        JLabel label = new JLabel("当前灯库素材:");
        JLabel alone = new JLabel();
        MainUi.map.put("alone", alone);
        JPanel jPanel = new JPanel();
        jPanel.add(label);
        jPanel.add(alone);
        JLabel label2 = new JLabel("所有灯库素材:");
        JLabel count = new JLabel(getCount());
        MainUi.map.put("count", count);
        JPanel jPanel2 = new JPanel();
        jPanel2.add(label2);
        jPanel2.add(count);

        pane.add(jPanel);
        pane.add(jPanel2);
    }

    public String getCount() {
        int count = 0;
        NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
        for (int i = 0; i < table.getRowCount(); i++) {
            Map map = (Map) Data.suCaiNameMap.get(i);
            JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
            String selectedName = "";
            for (int j = 0; j < btns.length; j++) {
                selectedName = btns[j].getText().substring(0, 2);
                if (map != null) {
                    List<String> suCaiNameList = (List<String>) map.get(selectedName);
                    if (suCaiNameList != null)
                        count += suCaiNameList.size();
                }
            }
        }
        return count + "";
    }

    public String getAlone(int selectIndex) {
        int count = 0;
        Map map = (Map) Data.suCaiNameMap.get(selectIndex);
        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
        String selectedName = "";
        for (int i = 0; i < btns.length; i++) {
            selectedName = btns[i].getText().substring(0, 2);
            if (map != null) {
                List<String> suCaiNameList = (List<String>) map.get(selectedName);
                if (suCaiNameList != null)
                    count += suCaiNameList.size();
            }
        }
        return count + "";
    }
}
