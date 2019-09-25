package com.boray.xiaoGuoDeng.Listener;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.boray.Data.Data;
import com.boray.Data.MyColor;
import com.boray.Data.XiaoGuoDengModel;
import com.boray.Utils.CloneUtils;
import com.boray.mainUi.MainUi;
import com.boray.xiaoGuoDeng.UI.DefineJLable;

public class ModelCopyActionListener implements ActionListener {
    private JComboBox box;

    public ModelCopyActionListener(JComboBox box) {
        this.box = box;
    }

    public void actionPerformed(ActionEvent e) {
        int scenN = box.getSelectedIndex() + 1;//源场景编号

        if (scenN == XiaoGuoDengModel.model) {
            return;
        }
        Object[][] objects = new Object[30][20];
        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + scenN);
        for (int k = 1; k < 31; k++) {
            for (int i = 0; i < timeBlockPanels[k].getComponentCount(); i++) {
                DefineJLable lable = (DefineJLable) timeBlockPanels[k].getComponent(i);
                String[] strings = new String[5];
                strings[0] = lable.getLocation().x + "";
                strings[1] = lable.getLocation().y + "";
                strings[2] = lable.getWidth() + "";
                strings[3] = lable.getHeight() + "";
                strings[4] = lable.getText();
                objects[k - 1][i] = strings;
            }
        }

        //添加到当前场景
        Data.XiaoGuoDengObjects[XiaoGuoDengModel.model - 1] = CloneUtils.clone(Data.XiaoGuoDengObjects[scenN - 1]);

        timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + XiaoGuoDengModel.model);
        for (int k = 1; k < 31; k++) {
            timeBlockPanels[k].removeAll();
            for (int i = 0; i < 20; i++) {
                if (objects[k - 1][i] != null) {
                    String[] strings = (String[]) objects[k - 1][i];
                    DefineJLable lable = new DefineJLable(strings[4].substring(0, strings[4].length() - 1), timeBlockPanels[k]);
                    lable.setText(strings[4]);
                    lable.setLocation(new Point(Integer.valueOf(strings[0]).intValue(), Integer.valueOf(strings[1]).intValue()));
                    lable.setSize(Integer.valueOf(strings[2]).intValue(), Integer.valueOf(strings[3]).intValue());
//					int c = Integer.valueOf(strings[4].substring(0,strings[4].length()-1))-1;
                    int c = Integer.valueOf(strings[4].substring(0, strings[4].indexOf("("))) - 1;
                    if (c >= 10) {
                        c = c - 10;
                    }
                    lable.setBackground(MyColor.colors[c]);
                    timeBlockPanels[k].add(lable);
                }
            }
            timeBlockPanels[k].repaint();
        }
    }
}
