package com.boray.main.Util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.beiFen.Listener.ProjectCreateFileOfCloseFrame;
import com.boray.entity.Message;
import com.boray.entity.ProjectFileInfo;
import com.boray.entity.Users;
import com.boray.mainUi.MainUi;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class selfMotionSave {

    public void autoSave() {
        Data.tempFileAutoSaveTimer = new Timer();
        Data.tempFileAutoSaveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (Data.tempWebFolder != null && Data.tempWebFile != null) {
                    ProjectCreateFileOfCloseFrame fileOfCloseFrame = new ProjectCreateFileOfCloseFrame();
                    fileOfCloseFrame.tt(Data.tempEditWebFile);
                    autoSaveFile();
                }
            }
        }, 1000, 60000);
    }

    public void autoSaveFile() {
        MainUtil util = new MainUtil();
        List<ProjectFileInfo> infos = util.tt(Data.tempEditWebFile, 1);
        String str = JSONArray.toJSONString(infos);
        HttpClientUtil httpsUtils = new HttpClientUtil();
        Users users = (Users) MainUi.map.get("Users");
        Map<String, String> param = new HashMap<>();
        param.put("gcname", Data.tempEditWebFile.getName().substring(0, Data.tempEditWebFile.getName().indexOf(".")));
        param.put("username", users.getUsername());
        param.put("xmid", Data.tempWebFolder.getId() + "");
        param.put("gcinfoid", Data.tempWebFile.getId() + "");
        param.put("i", "1");
        param.put("str", str);
        Map<String, Object> resultMap = httpsUtils.uploadFileByHTTP(Data.tempEditWebFile, Data.ipPort + "fileUploadServletgc", param);
        Message message = JSON.parseObject(resultMap.get("data").toString(), Message.class);
        System.out.println(message.getCode());
    }

}
