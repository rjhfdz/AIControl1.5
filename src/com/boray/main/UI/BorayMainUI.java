package com.boray.main.UI;

import com.boray.main.Listener.LoginListener;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class BorayMainUI {

    private LoginListener listener;

    public void show(JPanel pane) {
        pane.setBorder(new LineBorder(Color.black));

        MainUi.map.put("BorayMainPanel",pane);

        listener = new LoginListener(pane);

        JPanel p1 = new JPanel();
        init(p1);

        pane.add(p1, BorderLayout.CENTER);
    }

    private void init(JPanel pane) {
        pane.setPreferredSize(new Dimension(350, 400));
        JPanel jPanel = new JPanel();
        jPanel.setPreferredSize(new Dimension(350, 200));
        pane.add(jPanel);
        pane.add(new JLabel("用户名："));
        JTextField username = new JTextField(20);
        MainUi.map.put("username", username);
        pane.add(username);
        JPasswordField password = new JPasswordField(20);
        MainUi.map.put("password", password);
        pane.add(new JLabel("密码：   "));
        pane.add(password);
        JButton clear = new JButton("清除");
        JButton login = new JButton("登录");
        clear.addActionListener(listener);
        login.addActionListener(listener);

        pane.add(clear);
        pane.add(login);
    }
}
