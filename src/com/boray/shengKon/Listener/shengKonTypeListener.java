package com.boray.shengKon.Listener;

import com.boray.Data.Data;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class shengKonTypeListener implements ActionListener {

    private String dengZu;

    public shengKonTypeListener(String dengZu) {
        this.dengZu = dengZu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JToggleButton btn = (JToggleButton) e.getSource();
        Map map = (Map) Data.shengKonSuCaiNameMap.get(dengZu);
        if (map != null) {
            List tmp = (List) map.get(btn.getName());
            JList list = (JList) MainUi.map.get("shengKon_list");
            DefaultListModel model = (DefaultListModel) list.getModel();
            model.removeAllElements();
            if (tmp != null) {
                for (int i = 0; i < tmp.size(); i++) {
                    model.addElement(tmp.get(i).toString());
                }
                if (tmp.size() > 0) {
                    list.setSelectedIndex(0);
                }
            }
        } else {
            JList list = (JList) MainUi.map.get("shengKon_list");
            list.removeAll();
        }
    }
}
