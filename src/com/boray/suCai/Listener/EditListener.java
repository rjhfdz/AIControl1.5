package com.boray.suCai.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.boray.mainUi.MainUi;
import com.boray.suCai.UI.DongZuoSuCaiEditUI;
import com.boray.suCai.UI.SuCaiEditUI;

public class EditListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        //String suCaiName,int suCaiNum,int denKuNum
        JRadioButton radioButton = (JRadioButton) MainUi.map.get("xiaoGuoDengSuCaiTypeButton");
        JList suCai_list = (JList) MainUi.map.get("suCai_list");
        JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");
        if (suCai_list.getSelectedIndex() != -1) {
            String suCaiName = suCai_list.getSelectedValue().toString();
            if (!radioButton.isSelected()) {
                int suCaiNum = Integer.valueOf(suCaiName.split("--->")[1]).intValue();
                new DongZuoSuCaiEditUI().show(suCaiName, suCaiNum);
            } else {
                int denKuNum = suCaiLightType.getSelectedIndex();
                int suCaiNum = Integer.valueOf(suCaiName.split("--->")[1]).intValue();

                new SuCaiEditUI().show(suCaiName, suCaiNum, denKuNum);
            }
        }
    }
}
