package com.boray.xiaoGuoDeng.Listener;

import com.boray.Data.Data;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class xiaoGuoDengTypeListener implements ActionListener {

    private int dengZuId;
    private JRadioButton radioButton;

    public xiaoGuoDengTypeListener(JRadioButton radioButton, int dengZuId) {
        this.radioButton = radioButton;
        this.dengZuId = dengZuId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JToggleButton btn = (JToggleButton) e.getSource();
        String selectedName = btn.getText().substring(0, 2);
        JList suCaiList = (JList) MainUi.map.get("xiaoGuoDeng_list");
        DefaultListModel model = (DefaultListModel) suCaiList.getModel();
        model.removeAllElements();
        if (radioButton.isSelected()) {//通道
            Map map = (Map) Data.suCaiNameMap.get(dengZuId - 1);
            List<String> suCaiNameList = null;
            if (map == null) {
                map = new HashMap();
                suCaiNameList = new ArrayList<>();
                map.put(selectedName, suCaiNameList);
            } else {
                suCaiNameList = (List<String>) map.get(selectedName);
            }
            if (suCaiNameList != null) {
                for (int k = 0; k < suCaiNameList.size(); k++) {
                    model.addElement(suCaiNameList.get(k));
                }
                if (suCaiNameList.size() > 0) {
                    suCaiList.setSelectedIndex(0);
                }
            }
        } else {//动作
            List<String> suCaiNameList = (List<String>) Data.SuCaiDongZuoName.get(selectedName);
            if (suCaiNameList != null) {
                for (int k = 0; k < suCaiNameList.size(); k++) {
                    model.addElement(suCaiNameList.get(k));
                }
                if (suCaiNameList.size() > 0) {
                    suCaiList.setSelectedIndex(0);
                }
            }
        }

    }
}
