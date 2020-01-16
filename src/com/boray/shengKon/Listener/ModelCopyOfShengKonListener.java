package com.boray.shengKon.Listener;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import com.boray.Data.Data;
import com.boray.Data.MyColor;
import com.boray.Data.MyData;
import com.boray.Utils.CloneUtils;
import com.boray.mainUi.MainUi;
import com.boray.shengKon.UI.DefineJLable_shengKon;
import com.boray.shengKon.UI.DefineJLable_shengKon2;
import com.boray.xiaoGuoDeng.UI.DefineJLable;

public class ModelCopyOfShengKonListener implements ActionListener {
    private JComboBox box;

    public ModelCopyOfShengKonListener(JComboBox box) {
        this.box = box;
    }

    public void actionPerformed(ActionEvent e) {
        int scenN = box.getSelectedIndex() + 1;
        if (scenN == Integer.valueOf(MyData.ShengKonModel).intValue()) {
            return;
        }
        Object[][] objects = new Object[31][10];
        JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels" + scenN);
        for (int k = 0; k < 31; k++) {
            for (int i = 0; i < timeBlockPanels[k].getComponentCount(); i++) {
                if (k == 0) {
                    DefineJLable_shengKon lable = (DefineJLable_shengKon) timeBlockPanels[k].getComponent(i);
                    String[] strings = new String[6];
                    strings[0] = lable.getLocation().x + "";
                    strings[1] = lable.getLocation().y + "";
                    strings[2] = lable.getWidth() + "";
                    strings[3] = lable.getHeight() + "";
                    strings[4] = lable.getText();
                    objects[k][i] = strings;
                } else {
                    DefineJLable_shengKon2 lable = (DefineJLable_shengKon2) timeBlockPanels[k].getComponent(i);
                    String[] strings = new String[6];
                    strings[0] = lable.getLocation().x + "";
                    strings[1] = lable.getLocation().y + "";
                    strings[2] = lable.getWidth() + "";
                    strings[3] = lable.getHeight() + "";
                    strings[4] = lable.getText();
                    if (!lable.getBackground().equals(Color.red))
                        strings[5] = "1";
                    else
                        strings[5] = "0";
                    objects[k][i] = strings;
                }
            }
        }


//		Object[] objects2 = new Object[10];//时间长度
//		int[][] abc = new int[30][10];
//
//		for (int i = 0; i < timeBlockPanels[0].getComponentCount(); i++) {
//			DefineJLable_shengKon lable = (DefineJLable_shengKon)timeBlockPanels[0].getComponent(i);
//			String[] strings = new String[5];
//			strings[0] = lable.getLocation().x+"";
//			strings[1] = lable.getLocation().y+"";
//			strings[2] = lable.getWidth()+"";
//			strings[3] = lable.getHeight()+"";
//			strings[4] = lable.getText();
//			objects2[i] = strings;
//		}
//		for (int i = 1; i < timeBlockPanels.length; i++) {
//			for (int k = 0; k < timeBlockPanels[i].getComponentCount(); k++) {
//				DefineJLable_shengKon2 lable2 = (DefineJLable_shengKon2)timeBlockPanels[i].getComponent(k);
//				if (!lable2.getBackground().equals(Color.red)) {
//					abc[i-1][k] = 1;
//				}
//			}
//
//		}
        //////添加到效果灯模式
        /////////声控--效果灯模式叠加DMX
        for (int i = 1; i < 3; i++) {
            Vector temp = (Vector) Data.ShengKonModelDmxMap.get("TableData" + scenN + "" + i);
            if (temp != null) {
                temp = (Vector) temp.clone();
            }
            Data.ShengKonModelDmxMap.put("TableData" + MyData.ShengKonModel + "" + i, temp);

            boolean[] tbs = (boolean[]) Data.ShengKonModelDmxMap.get("GouXuanValue" + scenN + "" + i);
            if (tbs != null) {
                tbs = tbs.clone();
            }
            Data.ShengKonModelDmxMap.put("GouXuanValue" + MyData.ShengKonModel + "" + i, tbs);

            String[] s = (String[]) Data.ShengKonModelDmxMap.get("YaoMaiSet" + scenN + "" + i);
            if (s != null) {
                s = s.clone();
            }
            Data.ShengKonModelDmxMap.put("YaoMaiSet" + MyData.ShengKonModel + "" + i, s);
        }
        ////声控--效果灯模式设置
        ArrayList list = (ArrayList) Data.ShengKonModelSet.get(String.valueOf(scenN));
        if (list != null) {
            list = (ArrayList) list.clone();
        }
        Data.ShengKonModelSet.put(MyData.ShengKonModel, list);

        Data.ShengKonShiXuSetObjects[Integer.valueOf(MyData.ShengKonModel).intValue() - 1] = CloneUtils.clone(Data.ShengKonShiXuSetObjects[scenN - 1]);
        Data.ShengKonEditObjects[Integer.valueOf(MyData.ShengKonModel).intValue() - 1] = CloneUtils.clone(Data.ShengKonEditObjects[scenN - 1]);
        timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels" + MyData.ShengKonModel);
        for (int i = 0; i < timeBlockPanels.length; i++) {
            timeBlockPanels[i].removeAll();
        }

        for (int k = 0; k < 31; k++) {
            for (int i = 0; i < 10; i++) {
                if (objects[k][i] != null) {
                    String[] strings = (String[]) objects[k][i];
                    if (k == 0) {
                        DefineJLable_shengKon lable = new DefineJLable_shengKon(strings[4], timeBlockPanels[0]);
                        lable.setText(strings[4]);
                        lable.setLocation(new Point(Integer.valueOf(strings[0]).intValue(), Integer.valueOf(strings[1]).intValue()));
                        lable.setSize(Integer.valueOf(strings[2]).intValue(), Integer.valueOf(strings[3]).intValue());
                        int c = Integer.valueOf(strings[4]) - 1;
                        if (c >= 10) {
                            c = c - 10;
                        }
                        lable.setBackground(MyColor.colors[c]);
                        timeBlockPanels[k].add(lable);
                        timeBlockPanels[k].repaint();
                    } else {
                        DefineJLable_shengKon2 lable = new DefineJLable_shengKon2(strings[4], timeBlockPanels[k]);
                        lable.setText(strings[4]);
                        lable.setLocation(new Point(Integer.valueOf(strings[0]).intValue(), Integer.valueOf(strings[1]).intValue()));
                        lable.setSize(Integer.valueOf(strings[2]).intValue(), Integer.valueOf(strings[3]).intValue());
                        if (strings[5].equals("1"))
                            lable.setBackground(new Color(0, 255, 0));
                        else
                            lable.setBackground(Color.red);
                        timeBlockPanels[k].add(lable);
                        timeBlockPanels[k].repaint();
                    }
                }
            }
            timeBlockPanels[k].repaint();
        }

//		for (int i = 0; i < 10; i++) {
//			if (objects2[i] != null) {
//				String[] strings = (String[])objects2[i];
//				DefineJLable_shengKon lable = new DefineJLable_shengKon(strings[4], timeBlockPanels[0]);
//				lable.setText(strings[4]);
//				lable.setLocation(new Point(Integer.valueOf(strings[0]).intValue(), Integer.valueOf(strings[1]).intValue()));
//				lable.setSize(Integer.valueOf(strings[2]).intValue(), Integer.valueOf(strings[3]).intValue());
//				int c = Integer.valueOf(strings[4])-1;
//				if (c >= 10) {
//					c = c - 10;
//				}
//				lable.setBackground(MyColor.colors[c]);
//				timeBlockPanels[0].add(lable);
//				timeBlockPanels[0].updateUI();
//				for (int k = 1; k < timeBlockPanels.length; k++) {
//					c = timeBlockPanels[k].getComponentCount();
//					DefineJLable_shengKon2 label2 = new DefineJLable_shengKon2((c+1)+"",timeBlockPanels[k]);
//					////////////////////////////////////////////////////////×
//					if (c > 0) {
//						DefineJLable_shengKon2 defineJLable = (DefineJLable_shengKon2)timeBlockPanels[k].getComponent(c-1);
//						int x = defineJLable.getLocation().x + defineJLable.getWidth();
//						int y = defineJLable.getLocation().y;
//						label2.setLocation(new Point(x, y));
//					}
//					label2.setSize(Integer.valueOf(strings[2]).intValue(), Integer.valueOf(strings[3]).intValue());
//					/*if (c >= 10) {
//						c = c - 10;
//					}*/
//					if (abc[k-1][c] == 1) {
//						label2.setBackground(new Color(0, 255, 0));
//					} else {
//						label2.setBackground(Color.red);
//					}
//					timeBlockPanels[k].add(label2);
//					timeBlockPanels[k].updateUI();
//				}
//			}
//		}
    }
}
