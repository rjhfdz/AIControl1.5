package com.boray.shengKonSuCai.Listener;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.SuCaiFile;
import com.boray.entity.Users;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShengKonSuCaiYunTypeListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiYunTypeBtns");
        Users users = (Users) MainUi.map.get("Users");
        for (int i = 0;i<btns.length;i++){
            if(btns[i].isSelected()){
                JList list = (JList) MainUi.map.get("shengKonSuCaiDengZuYun");
                List<SuCaiFile> listFile = (List<SuCaiFile>) MainUi.map.get("shengKonDengZuYun");
                SuCaiFile file = listFile.get(list.getSelectedIndex());
                Map<String, String> param = new HashMap<>();
                param.put("username", users.getUsername());
                param.put("kuname", file.getKuname());
                param.put("sctype", btns[i].getName());
                String request = HttpClientUtil.doGet(Data.ipPort + "getsksc", param);
                List<SuCaiFile> suCaiList = JSON.parseArray(request, SuCaiFile.class);
                JList jList = (JList) MainUi.map.get("shengKonSuCaiYun_list");
                DefaultListModel model = (DefaultListModel) jList.getModel();
                model.removeAllElements();
                for (int j = 0;j<suCaiList.size();j++){
                    model.addElement(suCaiList.get(j).getFilename());
                }
            }
        }
    }
}
