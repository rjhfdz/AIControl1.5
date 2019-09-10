package com.boray.xiaoGuoDeng.Listener;

import com.boray.Data.Data;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class xiaoGuoDengTypeListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {

        JToggleButton btn = (JToggleButton)e.getSource();
        NewJTable table3 = (NewJTable)MainUi.map.get("allLightTable");//所有灯具
        String name = (String) MainUi.map.get("xiaoGuoDengIndex");
        TreeSet treeSet = (TreeSet)Data.GroupOfLightList.get(Integer.parseInt(name)-1);
        Iterator iterator = treeSet.iterator();
        String s = "";
        Map map = new HashMap();
        while (iterator.hasNext()) {
            int a = (int) iterator.next();
            if(table3.getRowCount()>0) {
                s = table3.getValueAt(a, 0).toString();
                Integer s1 = Integer.parseInt(s.split("#")[0].substring(2));//组内灯具的灯具id
                NewJTable table_dengJu = (NewJTable) MainUi.map.get("table_dengJu");//灯具配置
                String s2 = ((String) table_dengJu.getValueAt(s1 - 1, 3)).split("#")[1];//灯库名称
                map = (Map) Data.suCaiNameMap.get(s2);
            }
        }
        if (map!=null) {
            List tmp = (List)map.get(btn.getName());
            JList list = (JList)MainUi.map.get("xiaoGuoDeng_list");
            DefaultListModel  model = (DefaultListModel)list.getModel();
            model.removeAllElements();
            if (tmp!=null) {
                for (int i = 0; i < tmp.size(); i++) {
                    model.addElement(tmp.get(i).toString());
                }
                if (tmp.size()>0) {
                    list.setSelectedIndex(0);
                }
            }
        } else {
            JList list = (JList)MainUi.map.get("xiaoGuoDeng_list");
            list.removeAll();
        }

    }
}
