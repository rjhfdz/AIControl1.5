package com.boray.suCai.Listener;

import com.boray.Data.Data;
import com.boray.mainUi.MainUi;
import com.boray.Utils.SuCaiUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

public class TonDaoOrDongZuoSuCaiTypeListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton upLoadBtn = (JButton) MainUi.map.get("XiaoGuoDengSuCaiUpLoadBtn");
        String name = SuCaiUtil.getXiaoGuoDengType();//获得选中的素材类型
        //清空素材列表
        JList list = (JList) MainUi.map.get("suCai_list");
        DefaultListModel model = (DefaultListModel) list.getModel();
        model.removeAllElements();
        List<String> suCaiNameList = null;
        if (e.getActionCommand().equals("通道")) {
            upLoadBtn.setVisible(true);
            JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");//左侧灯组列表
            Map map = (Map) Data.suCaiNameMap.get(suCaiLightType.getSelectedIndex());

            if (map != null) {
                suCaiNameList = (List<String>) map.get(name);
                if (suCaiNameList != null) {
                    for (int i = 0; i < suCaiNameList.size(); i++) {
                        model.addElement(suCaiNameList.get(i));
                    }
                    if (suCaiNameList.size() > 0) {
                        list.setSelectedIndex(0);
                    }
                }
            }
        } else {
            upLoadBtn.setVisible(false);
            suCaiNameList = (List<String>) Data.SuCaiDongZuoName.get(name);
            if (suCaiNameList != null) {
                for (int i = 0; i < suCaiNameList.size(); i++) {
                    model.addElement(suCaiNameList.get(i));
                }
                if (suCaiNameList.size() > 0) {
                    list.setSelectedIndex(0);
                }
            }
        }
        SuCaiUtil.neatenSuCaiTypeBtns();
    }
}
