package com.boray.shengKonSuCai.Listener;

import com.boray.Data.Data;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class ShengKonSuCaiTypeListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JToggleButton btn = (JToggleButton) e.getSource();
        JList suCaiLightType = (JList) MainUi.map.get("shengKonSuCaiLightType");
        if (suCaiLightType.getSelectedValue() != null) {
            Map map = (Map) Data.shengKonSuCaiNameMap.get(suCaiLightType.getSelectedValue().toString());
            if (map != null) {
                List tmp = (List) map.get(btn.getName());
                JList list = (JList) MainUi.map.get("shengKonSuCai_list");
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
                JList list = (JList) MainUi.map.get("shengKonSuCai_list");
                list.removeAll();
            }
        }
    }
}
