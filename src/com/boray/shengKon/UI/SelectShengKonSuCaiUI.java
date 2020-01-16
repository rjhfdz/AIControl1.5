package com.boray.shengKon.UI;

import com.boray.Data.Data;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.shengKon.Listener.shengKonTypeListener;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class SelectShengKonSuCaiUI {
    private JDialog dialog;

    public void show(JPanel pane, String text) {
        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new JDialog(f, true);
        dialog.setResizable(false);
        dialog.setTitle("选择素材");
        int w = 450, h = 620;
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
        dialog.setSize(w, h);
        dialog.setLayout(new FlowLayout(FlowLayout.LEFT));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        init(text, pane);
        dialog.setVisible(true);
    }

    private void init(final String text, final JPanel pane) {
        String str = pane.getName();
        int groupNum = Integer.parseInt(str);
        NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
        String dengZu = (String) table.getValueAt(groupNum - 1, 2);

        //左边效果按钮
        JPanel pane2 = new JPanel();
        setP1(pane2, dengZu);

        //右边素材按钮
        JPanel p3 = new JPanel();
        setP2(p3, dengZu);

        JButton sureBtn = new JButton("确定");
        sureBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = 0;
                if (text.contains("(")) {
                    String str = text.substring(0, text.indexOf("("));
                    i = Integer.parseInt(str);
                } else {
                    i = Integer.parseInt(text);
                }
                DefineJLable_shengKon2 label = (DefineJLable_shengKon2) pane.getComponent(i - 1);
                JList list = (JList) MainUi.map.get("shengKon_list");
                label.setText(i+"(" + list.getSelectedValue().toString().split(">")[1] + ")");
                label.updateUI();
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

        dialog.add(pane2);
        dialog.add(p3);
        dialog.add(sureBtn);
        dialog.add(canceBtn);
    }

    public void setP1(JPanel pane, String dengZu) {
        pane.setBorder(new LineBorder(Color.black));
        pane.setPreferredSize(new Dimension(150, 540));
        String[] names = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
        JToggleButton[] btns = new JToggleButton[names.length];
        shengKonTypeListener xiaoGuoDengTypeListener = new shengKonTypeListener(dengZu);
        MainUi.map.put("shengKonTypeBtns", btns);
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
        Map map = (Map) Data.shengKonSuCaiMap.get(dengZu);
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

    public void setP2(JPanel p2, String dengZu) {
        //素材列表
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(270, 530));
        final JList list = new JList();
        MainUi.map.put("shengKon_list", list);
        list.setFixedCellHeight(32);
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        list.setCellRenderer(renderer);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultListModel model = new DefaultListModel();

        Map nameMap = (Map) Data.shengKonSuCaiNameMap.get(dengZu);

        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonTypeBtns");
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

}
