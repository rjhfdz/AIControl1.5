package com.boray.suCai.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import com.boray.Data.Data;
import com.boray.mainUi.MainUi;

public class SuCaiTypeListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        JToggleButton btn = (JToggleButton) e.getSource();
        JRadioButton radioButton = (JRadioButton) MainUi.map.get("xiaoGuoDengSuCaiTypeButton");
        JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");
        JList list = (JList) MainUi.map.get("suCai_list");
        DefaultListModel model = (DefaultListModel) list.getModel();
        model.removeAllElements();
        String name = btn.getText().substring(0, 2);
        if (radioButton.isSelected()) {//通道
            if (suCaiLightType.getSelectedValue() != null) {
                Map map = (Map) Data.suCaiNameMap.get(suCaiLightType.getSelectedIndex());
                if (map != null) {
                    List<String> suCaiNameList = (List<String>) map.get(name);
                    if (suCaiNameList != null) {
                        for (int i = 0; i < suCaiNameList.size(); i++) {
                            model.addElement(suCaiNameList.get(i));
                        }
                        if (suCaiNameList.size() > 0) {
                            list.setSelectedIndex(0);
                        }
                    }
                }
            }
        } else {//动作
            List<String> suCaiNameList = (List<String>) Data.SuCaiDongZuoName.get(name);
            if (suCaiNameList != null) {
                for (int i = 0; i < suCaiNameList.size(); i++) {
                    model.addElement(suCaiNameList.get(i));
                }
                if (suCaiNameList.size() > 0) {
                    list.setSelectedIndex(0);
                }
            }
        }
    }
}
