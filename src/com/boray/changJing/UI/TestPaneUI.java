package com.boray.changJing.UI;

import com.boray.Data.ChangJinData;
import com.boray.changJing.Listener.ChangJingSendCodeListener;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TestPaneUI {

    /**
     * 加载场景测试界面
     *
     * @param pane
     */
    public void show(JPanel pane) {
        pane.setPreferredSize(new Dimension(1000, 350));
        //pane.setBorder(new LineBorder(Color.gray));
        pane.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "灯光场景", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        panel.setBorder(tb1);
        panel.setPreferredSize(new Dimension(410, 250));
        JToggleButton[] btns = new JToggleButton[32];
        MainUi.map.put("ToggleBtns_changJing", btns);
        ButtonGroup group = new ButtonGroup();
        ChangJingSendCodeListener listener = new ChangJingSendCodeListener();
        for (int i = 0; i < btns.length; i++) {
            btns[i] = new JToggleButton(ChangJinData.T1()[i]);
            if (i < 22) {
                btns[i].setName(String.valueOf(i));
            } else {
                if (i == 22) {
                    btns[i].setName("23");
                }
                if (i == 23) {
                    btns[i].setName("24");
                }
                if (i >= 24) {
                    btns[i].setName(String.valueOf(i + 5));
                }
            }
            btns[i].setPreferredSize(new Dimension(94, 48));
            btns[i].setMargin(new Insets(0, -10, 0, -10));
            btns[i].addActionListener(listener);
            group.add(btns[i]);
            panel.add(btns[i]);
        }


        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "功能场景", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        panel2.setBorder(tb2);
        panel2.setPreferredSize(new Dimension(1000, 40));
        JToggleButton[] btns2 = new JToggleButton[4];
        for (int i = 0; i < btns2.length; i++) {
            btns2[i] = new JToggleButton(ChangJinData.T1()[i + 32]);
            btns2[i].setPreferredSize(new Dimension(94, 48));
            btns2[i].setMargin(new Insets(0, -10, 0, -10));
            if (i == 0) {
                btns2[i].setName("22");
            } else if (i == 1) {
                btns2[i].setName("26");
            } else if (i == 2) {
                btns2[i].setName("27");
            } else if (i == 3) {
                btns2[i].setName("28");
            }
            btns2[i].addActionListener(listener);
            group.add(btns2[i]);
            panel2.add(btns2[i]);
        }

        pane.add(panel, BorderLayout.NORTH);
        pane.add(panel2);
    }
}
