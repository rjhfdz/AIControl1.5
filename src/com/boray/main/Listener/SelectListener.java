package com.boray.main.Listener;

import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SelectListener implements ItemListener {

    private CardLayout card;
    private JPanel rightPane;

    public SelectListener(CardLayout card, JPanel rightPane) {
        this.card = card;
        this.rightPane = rightPane;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        JToggleButton btn = (JToggleButton) e.getSource();
        if (e.getStateChange() == ItemEvent.SELECTED) {


            if (MainUi.map.get("Users") == null) {
                card.show(rightPane, "7");

            } else {
                if ("官方发布".equals(btn.getText())) {
                    card.show(rightPane, "1");
                } else if ("个人管理".equals(btn.getText())) {
                    card.show(rightPane, "2");
                } else if ("成员管理".equals(btn.getText())) {
                    card.show(rightPane, "3");
                } else if ("设置团队".equals(btn.getText())) {
                    card.show(rightPane, "4");
                } else if ("本地项目".equals(btn.getText())) {
                    card.show(rightPane, "5");
                } else if ("团队资料".equals(btn.getText())) {
                    card.show(rightPane, "6");
                } else if ("切换用户".equals(btn.getText())) {
                    System.out.println(1);
                    MainUi.map.remove("Users");
                    card.show(rightPane, "7");
                }
            }
        }
    }
}
