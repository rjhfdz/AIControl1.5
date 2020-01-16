package com.boray.shengKon.Listener;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import com.boray.Data.Data;
import com.boray.Data.MyColor;
import com.boray.Data.MyData;
import com.boray.Utils.CloneUtils;
import com.boray.mainUi.MainUi;
import com.boray.shengKon.UI.DefineJLable_shengKon;
import com.boray.shengKon.UI.DefineJLable_shengKon2;

public class TimeBlockAction_shengKon_Listener implements ActionListener {
    private JPanel pane;

    public TimeBlockAction_shengKon_Listener(JPanel pane) {
        this.pane = pane;
    }

    public void actionPerformed(ActionEvent e) {
        if ("　新建".equals(e.getActionCommand())) {
            int c = pane.getComponentCount();
            if (c == 10) {
                return;
            }
            DefineJLable_shengKon label = new DefineJLable_shengKon((c + 1) + "", pane);
            ////////////////////////////////////////////////////////×
            if (c > 0) {
                DefineJLable_shengKon defineJLable = (DefineJLable_shengKon) pane.getComponent(c - 1);
                int x = defineJLable.getLocation().x + defineJLable.getWidth();
                int y = defineJLable.getLocation().y;
                label.setLocation(new Point(x, y));
            }
            if (c >= 10) {
                c = c - 10;
            }
            label.setBackground(MyColor.colors[c]);
            ////////////////////////////////////////////////////////
            pane.add(label);
            pane.updateUI();

            JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels" + MyData.ShengKonModel);
            for (int i = 1; i < timeBlockPanels.length; i++) {
                c = timeBlockPanels[i].getComponentCount();
//                if (timeBlockPanels[i].isVisible()) {
                    DefineJLable_shengKon2 label2 = new DefineJLable_shengKon2((c + 1) + "", timeBlockPanels[i]);
                    ////////////////////////////////////////////////////////×
                    if (c > 0) {
                        DefineJLable_shengKon2 defineJLable = (DefineJLable_shengKon2) timeBlockPanels[i].getComponent(c - 1);
                        int x = defineJLable.getLocation().x + defineJLable.getWidth();
                        int y = defineJLable.getLocation().y;
                        label2.setLocation(new Point(x, y));
                    }
				/*if (c >= 10) {
					c = c - 10;
				}*/
                    label2.setBackground(Color.red);
                    timeBlockPanels[i].add(label2);
                    timeBlockPanels[i].updateUI();
//                }
            }

        } else if ("　删除所有".equals(e.getActionCommand())) {
            JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels" + MyData.ShengKonModel);
            for (int i = 0; i < timeBlockPanels.length; i++) {
                timeBlockPanels[i].removeAll();
                timeBlockPanels[i].repaint();
            }
        } else if ("　粘贴".equals(e.getActionCommand())) {
            Map shengkongCopyMap = (Map) MainUi.map.get("shengkongCopyMap");
            if (shengkongCopyMap != null) {
                String[] timeWidth = (String[]) shengkongCopyMap.get("0");//时间宽度
                int abc[] = (int[]) shengkongCopyMap.get("1");//记录场景编辑
                int[][] tempDataSet = (int[][]) shengkongCopyMap.get("2");//声控顺序设置参数
                Object[] objects = (Object[]) shengkongCopyMap.get("3");//编辑效果数据

                int c = pane.getComponentCount();
                if (c == 10) {
                    return;
                }
                DefineJLable_shengKon label = new DefineJLable_shengKon((c + 1) + "", pane);
                ////////////////////////////////////////////////////////×
                if (c > 0) {
                    DefineJLable_shengKon defineJLable = (DefineJLable_shengKon) pane.getComponent(c - 1);
                    int x = defineJLable.getLocation().x + defineJLable.getWidth();
                    int y = defineJLable.getLocation().y;
                    label.setLocation(new Point(x, y));
                }
                if (c >= 10) {
                    c = c - 10;
                }
                label.setSize(Integer.valueOf(timeWidth[0]), label.getHeight());
                label.setBackground(MyColor.colors[c]);
                ////////////////////////////////////////////////////////
                pane.add(label);
                pane.updateUI();

                int modelInt = Integer.valueOf(MyData.ShengKonModel).intValue() - 1;
                int TimeBlockInt = c;
                int[][] tempDataSet_clone = CloneUtils.clone(tempDataSet);
                Data.ShengKonShiXuSetObjects[modelInt][TimeBlockInt] = tempDataSet_clone;

                for (int i = 0; i < 30; i++) {
                    HashMap tp = (HashMap) objects[i];
                    HashMap map2 = CloneUtils.clone(tp);
                    Data.ShengKonEditObjects[modelInt][i][TimeBlockInt] = map2;
                }

                JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels" + MyData.ShengKonModel);
                for (int i = 1; i < timeBlockPanels.length; i++) {
                    c = timeBlockPanels[i].getComponentCount();
                    DefineJLable_shengKon2 label2 = new DefineJLable_shengKon2((c + 1) + "", timeBlockPanels[i]);
                    ////////////////////////////////////////////////////////×
                    if (c > 0) {
                        DefineJLable_shengKon2 defineJLable = (DefineJLable_shengKon2) timeBlockPanels[i].getComponent(c - 1);
                        int x = defineJLable.getLocation().x + defineJLable.getWidth();
                        int y = defineJLable.getLocation().y;
                        label2.setLocation(new Point(x, y));
                    }
					/*if (c >= 10) {
						c = c - 10;
					}*/
                    label2.setSize(Integer.valueOf(timeWidth[0]), label2.getHeight());
                    if (abc[i - 1] == 1) {
                        label2.setBackground(new Color(0, 255, 0));
                    } else {
                        label2.setBackground(Color.red);
                    }
                    timeBlockPanels[i].add(label2);
                    timeBlockPanels[i].updateUI();
                }
            }
        }
    }
}
