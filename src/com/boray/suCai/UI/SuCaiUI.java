package com.boray.suCai.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import com.boray.suCai.Listener.CreateOrDelSuCaiListener;
import com.boray.suCai.Listener.EditListener;
import com.boray.suCai.Listener.ShangchuanListener;
import com.boray.suCai.Listener.SuCaiTypeListener;
import com.boray.suCai.Listener.UpLoadOrLoadSuCaiMouseListener;
import com.boray.suCai.Listener.YundelListener;

public class SuCaiUI {
    public void show(JPanel pane) {
        pane.setBorder(new LineBorder(Color.black));
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));

        JScrollPane p1 = new JScrollPane();
        TitledBorder titledBorder2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "µÆ¿âÃû³Æ", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p1.setBorder(titledBorder2);
        p1.setPreferredSize(new Dimension(200, 594));
        setP1(p1);

        JPanel p2 = new JPanel();
        TitledBorder titledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "ËØ²ÄÀàÐÍ", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p2.setBorder(titledBorder);
        p2.setPreferredSize(new Dimension(150, 586));
        setP2(p2);

        JPanel p3 = new JPanel();
        TitledBorder titledBorder1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "ËØ²ÄÁÐ±í", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p3.setBorder(titledBorder1);
        p3.setPreferredSize(new Dimension(280, 594));
        setP3(p3);

//        JPanel p4 = new JPanel();
//        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "ÔÆ¶ËËØ²Ä", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
//        p4.setBorder(tb);
//        p4.setPreferredSize(new Dimension(280, 592));
//        setP4(p4);

        pane.add(p1);
        pane.add(p2);
        pane.add(p3);
//        pane.add(p4);
    }

    private void setP4(JPanel pane) {
    	 JButton newBtn = new JButton("ÔÆ¶ËËØ²Ä");
    	 
    	 newBtn.addActionListener(new YundelListener());
    	 
    	 pane.add(newBtn);
    }

    private void setP3(JPanel p3) {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(270, 520));

        //ËØ²ÄÁÐ±í
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
        bottomPanel.setPreferredSize(new Dimension(280, 40));
        //bottomPanel.setBorder(new LineBorder(Color.black));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(-2);
        flowLayout.setHgap(-2);
        bottomPanel.setLayout(flowLayout);
        JButton newBtn = new JButton("ÐÂ½¨");
        JButton editBtn = new JButton("±à¼­");
        JButton reNameBtn = new JButton("ÖØÃüÃû");
        JButton upLoadBtn = new JButton("ÔÆ¶Ë");
        JButton reviewBtn = new JButton("Ô¤ÀÀ");
        JButton removeBtn = new JButton("É¾³ý");
        CreateOrDelSuCaiListener listener = new CreateOrDelSuCaiListener();
        EditListener listener2 = new EditListener();
        editBtn.addActionListener(listener2);
        newBtn.addActionListener(listener);
        reNameBtn.addActionListener(listener);
        reviewBtn.addActionListener(listener);
        removeBtn.addActionListener(listener);
//        ShangchuanListener shangchuang = new ShangchuanListener();
        upLoadBtn.addActionListener(listener);
        Dimension dimension = new Dimension(55, 34);
        newBtn.setPreferredSize(dimension);
        editBtn.setPreferredSize(dimension);
        reNameBtn.setPreferredSize(new Dimension(68, 34));
        upLoadBtn.setPreferredSize(dimension);
        reviewBtn.setPreferredSize(dimension);
        removeBtn.setPreferredSize(dimension);
        bottomPanel.add(newBtn);
        bottomPanel.add(editBtn);
        bottomPanel.add(reNameBtn);
        bottomPanel.add(upLoadBtn);
        bottomPanel.add(reviewBtn);
//        bottomPanel.add(removeBtn);
        p3.add(bottomPanel);

        //Ìí¼ÓÓÒ¼ü²Ëµ¥
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

					String aloneCount = getAlone(list.getSelectedValue().toString());//µ±Ç°µÆ¿âµÄËØ²ÄÊýÁ¿
					JLabel alone = (JLabel) MainUi.map.get("alone");
					alone.setText(aloneCount);

					String count = getCount();//ËùÓÐµÆ¿âµÄËØ²ÄÊýÁ¿
					JLabel countLabel = (JLabel) MainUi.map.get("count");
					countLabel.setText(count);

                    Map map = (Map) Data.suCaiMap.get(list.getSelectedValue().toString());
                    JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
                    String[] name = {"¶¯¸Ð", "ÂýÒ¡", "ÊãÇé", "ÈáºÍ", "ÀËÂþ", "ÎÂÜ°", "ìÅÀö", "ÃÎ»Ã", "ÆäËû"};
                    if (map != null) {
                        for (int i = 0; i < btns.length; i++) {
                            List abc = (List) map.get("" + i);
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

                    Map nameMap = (Map) Data.suCaiNameMap.get(list.getSelectedValue().toString());
                    JList list = (JList) MainUi.map.get("suCai_list");
                    DefaultListModel model = (DefaultListModel) list.getModel();
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
        String[] name = {"¶¯¸Ð", "ÂýÒ¡", "ÊãÇé", "ÈáºÍ", "ÀËÂþ", "ÎÂÜ°", "ìÅÀö", "ÃÎ»Ã", "ÆäËû"};
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
        JLabel label = new JLabel("µ±Ç°µÆ¿âËØ²Ä:");
        JLabel alone = new JLabel();
        MainUi.map.put("alone", alone);
        JPanel jPanel = new JPanel();
        jPanel.add(label);
        jPanel.add(alone);
        JLabel label2 = new JLabel("ËùÓÐµÆ¿âËØ²Ä:");
        JLabel count = new JLabel(getCount());
        MainUi.map.put("count", count);
        JPanel jPanel2 = new JPanel();
        jPanel2.add(label2);
        jPanel2.add(count);

        pane.add(jPanel);
        pane.add(jPanel2);

    }

    public String getCount() {
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");//µÆ¿â
        String[] name = {"¶¯¸Ð", "ÂýÒ¡", "ÊãÇé", "ÈáºÍ", "ÀËÂþ", "ÎÂÜ°", "ìÅÀö", "ÃÎ»Ã", "ÆäËû"};
        int count = 0;
        for (int i = 0; i < table.getRowCount(); i++) {
            Map map = (Map) Data.suCaiMap.get(table.getValueAt(i, 1).toString());
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
        String[] name = {"¶¯¸Ð", "ÂýÒ¡", "ÊãÇé", "ÈáºÍ", "ÀËÂþ", "ÎÂÜ°", "ìÅÀö", "ÃÎ»Ã", "ÆäËû"};
        int count = 0;
        Map map = (Map) Data.suCaiMap.get(selectVlaue);
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
