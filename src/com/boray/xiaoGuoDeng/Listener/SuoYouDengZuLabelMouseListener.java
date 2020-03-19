package com.boray.xiaoGuoDeng.Listener;

import com.boray.Data.XiaoGuoDengModel;
import com.boray.mainUi.MainUi;
import com.boray.xiaoGuoDeng.UI.DefineJLable3;
import com.boray.xiaoGuoDeng.UI.OverDmxUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SuoYouDengZuLabelMouseListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + XiaoGuoDengModel.model);
        JPanel panel = (JPanel) MainUi.map.get("SuoYouDengZuPanel" + XiaoGuoDengModel.model);
        DefineJLable3 lable3 = (DefineJLable3) MainUi.map.get("SuoYouDengZuLable" + XiaoGuoDengModel.model);
        if ("　启用".equals(e.getActionCommand())) {
            lable3.setText("所有灯组√");
            for (int i = 0; i < timeBlockPanels.length; i++) {
                if (i != 0) {
                    timeBlockPanels[i].setEnabled(false);
                    for (int j = 0; j < timeBlockPanels[i].getComponentCount(); j++) {
                        timeBlockPanels[i].getComponent(j).setEnabled(false);
                    }
                    timeBlockPanels[i].updateUI();
                }
            }
            panel.setEnabled(true);
            for (int i = 0; i < panel.getComponentCount(); i++) {
                panel.getComponent(i).setEnabled(true);
            }
        } else if ("　禁用".equals(e.getActionCommand())) {
            lable3.setText("所有灯组×");
            for (int i = 0; i < timeBlockPanels.length; i++) {
                if (i != 0) {
                    timeBlockPanels[i].setEnabled(true);
                    for (int j = 0; j < timeBlockPanels[i].getComponentCount(); j++) {
                        timeBlockPanels[i].getComponent(j).setEnabled(true);
                    }
                    timeBlockPanels[i].updateUI();
                }
            }
            panel.setEnabled(false);
            for (int i = 0; i < panel.getComponentCount(); i++) {
                panel.getComponent(i).setEnabled(false);
            }
        } else {
            new OverDmxUI().show(XiaoGuoDengModel.model);
        }
    }
}
