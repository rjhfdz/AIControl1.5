package com.boray.xiaoGuoDeng.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JPanel;

import com.boray.Data.Data;
import com.boray.Data.XiaoGuoDengModel;
import com.boray.Utils.CloneUtils;
import com.boray.mainUi.MainUi;
import com.boray.suCai.UI.DongZuoSuCaiEditUI;
import com.boray.xiaoGuoDeng.UI.DefineJLable;
import com.boray.xiaoGuoDeng.UI.SelectSuCaiUI;
import com.boray.xiaoGuoDeng.UI.TimeBlockEditUI;

public class PopMenuListener implements ActionListener {
    private JPanel pane;
    private int blockNum;
    private String ss;

    public PopMenuListener(int blockNum, JPanel pane, String ss) {
        this.pane = pane;
        this.blockNum = blockNum;
        this.ss = ss;
    }

    public void actionPerformed(ActionEvent e) {
        if ("¡¡±à¼­".equals(e.getActionCommand())) {
            DefineJLable lable = (DefineJLable) pane.getComponent(blockNum - 1);
            ss = lable.getText();
            if(lable.getName().equals("TonDao")){
                int dengZuNum = Integer.valueOf(pane.getName());
                if (dengZuNum % 2 == 0) {
                    dengZuNum = dengZuNum / 2;
                } else {
                    dengZuNum = (dengZuNum + 1) / 2;
                }
                int suCaiNum = Integer.valueOf(ss.substring(ss.indexOf("(")+1, ss.indexOf(")")));
                new TimeBlockEditUI().show(ss, dengZuNum, suCaiNum);
            }else{
                int suCaiNum = Integer.valueOf(ss.substring(ss.indexOf("(")+1, ss.indexOf(")")));
                new DongZuoSuCaiEditUI().show(ss, suCaiNum);
            }

        } else if ("¡¡É¾³ý".equals(e.getActionCommand())) {
            if (blockNum == pane.getComponentCount()) {
                pane.remove(blockNum - 1);
                pane.repaint();
            } else {

            }
        } else if ("¡¡É¾³ýÈ«²¿".equals(e.getActionCommand())) {
            pane.removeAll();
            pane.repaint();
        } else if ("¡¡¸´ÖÆ".equals(e.getActionCommand())) {
            int grpN = Integer.valueOf(pane.getName()).intValue();
            int blkN = Integer.valueOf(blockNum).intValue() - 1;
            JPanel[] timeBlockPanels = (JPanel[]) MainUi.map.get("timeBlockPanels_group" + XiaoGuoDengModel.model);
            DefineJLable lable = (DefineJLable) timeBlockPanels[grpN].getComponent(blkN);
            String[] strings = new String[2];
            strings[0] = lable.getWidth() + "";
            strings[1] = lable.getHeight() + "";

            HashMap map = (HashMap) Data.XiaoGuoDengObjects[XiaoGuoDengModel.model - 1][grpN - 1][blkN];
            HashMap copyMap = CloneUtils.clone(map);
            Object[] obj = new Object[]{strings, copyMap};
            Data.CopyObj = obj;
        } else if ("¡¡Ñ¡ÔñËØ²Ä".equals(e.getActionCommand())) {
            DefineJLable defineJLable = (DefineJLable) pane.getComponent(Integer.valueOf(ss.substring(0, ss.indexOf("("))) - 1);
            new SelectSuCaiUI().show(pane, false, defineJLable);
        }
    }
}
