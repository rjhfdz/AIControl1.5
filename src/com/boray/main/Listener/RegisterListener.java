package com.boray.main.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class RegisterListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec("C:\\Program Files\\Internet Explorer\\iexplore.exe http://47.105.197.68:8980/js/account/forgetPwd");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}
