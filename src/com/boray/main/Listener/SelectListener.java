package com.boray.main.Listener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SelectListener implements ItemListener {

    private CardLayout card;
    private JPanel rightPane;

    public SelectListener(CardLayout card,JPanel rightPane) {
        this.card = card;
        this.rightPane = rightPane;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        JToggleButton btn = (JToggleButton) e.getSource();
        if (e.getStateChange() == ItemEvent.SELECTED) {
            if ("�ҵ���Ŀ".equals(btn.getText())) {
                card.show(rightPane,"1");
            }else if("��˾��Ŀ".equals(btn.getText())){
                card.show(rightPane,"2");
            }else if("������Ŀ".equals(btn.getText())){
                card.show(rightPane,"3");
            }
        }
    }
}