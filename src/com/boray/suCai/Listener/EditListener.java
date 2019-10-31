package com.boray.suCai.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JList;

import com.boray.mainUi.MainUi;
import com.boray.suCai.UI.SuCaiEditUI;

public class EditListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        //String suCaiName,int suCaiNum,int denKuNum
        JList suCai_list = (JList) MainUi.map.get("suCai_list");
        JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");
        if (suCai_list.getSelectedIndex() != -1) {
            String suCaiName = suCai_list.getSelectedValue().toString();


            int denKuNum = suCaiLightType.getSelectedIndex();
            int suCaiNum = Integer.valueOf(suCaiName.split("--->")[1]).intValue();

            System.out.println(suCaiName + "" + suCaiNum + "" + denKuNum);
            new SuCaiEditUI().show(suCaiName, suCaiNum, denKuNum);
        }
    }
}
