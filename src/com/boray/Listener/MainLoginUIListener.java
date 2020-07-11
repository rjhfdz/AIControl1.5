package com.boray.Listener;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.*;
import com.boray.entity.Users;
import com.boray.main.Util.IpConfig;
import com.boray.mainUi.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class MainLoginUIListener implements ActionListener {

    private JFrame frame;

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        frame = (JFrame) MainUi.map.get("frame");
//        JCheckBox box = (JCheckBox) MainUi.map.get("MainRememberPassword");
//        if (box.isSelected())
//            Data.RememberPassword = true;
        if ("登录".equals(button.getText())) {
            String username = ((JTextField) MainUi.map.get("MainUsername")).getText();
            String password = new String(((JPasswordField) MainUi.map.get("MainPassword")).getPassword());
            if (username == null || username == "") {
                JOptionPane.showMessageDialog(frame, "登录失败，用户名不能为空！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            if (password == null || password == "") {
                JOptionPane.showMessageDialog(frame, "登录失败，密码不能为空！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            login(username, password);
        } else if ("清除".equals(button.getText())) {
            JTextField username = (JTextField) MainUi.map.get("MainUsername");
            JPasswordField password = (JPasswordField) MainUi.map.get("MainPassword");
            username.setText("");
            password.setText("");
        } else if ("离线".equals(button.getText())) {
            frame.getContentPane().removeAll();
            //去掉背景图
            frame.getLayeredPane().remove(1);
            frame.getContentPane().repaint();
//            frame.setVisible(false);
            init();
        }
    }

    private void login(String username, String password) {
        InfiniteProgressPanel glasspane = new InfiniteProgressPanel();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        glasspane.setBounds(100, 100, (dimension.width) / 2, (dimension.height) / 2);
        frame.setGlassPane(glasspane);
        glasspane.start();//开始动画加载效果
        frame.setVisible(true);
        try {
            IpConfig config = new IpConfig();
            config.getIpConfig();
            Map<String, String> param = new HashMap<>();
            param.put("logincode", username);
            param.put("password", password);
            String request = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/login", param);
            Users users = JSON.parseObject(request, Users.class);
            if (users != null && !users.getCode().equals(-1)) {
//                if (Data.RememberPassword) {
//                    Data.userLogin.put(Util.encode(username), Util.encode(password));
//                }
                MainUi.map.put("Users", users);
                frame.getContentPane().removeAll();
                //去掉背景图
                frame.getLayeredPane().remove(1);
                frame.getContentPane().repaint();
//                frame.setVisible(false);
                init();
                glasspane.stop();
            } else {
                glasspane.stop();
                JOptionPane.showMessageDialog(frame, "登录失败，用户名或密码不正确！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
        } catch (Exception e) {
            glasspane.stop();
            e.printStackTrace();
//            JOptionPane.showMessageDialog(frame, "登录失败，网络错误！", "提示", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void init() {
        frame.setLayout(new FlowLayout(FlowLayout.LEFT));
//        JTabbedPane tabbedPane = new JTabbedPane();
//        tabbedPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
//        tabbedPane.setPreferredSize(new Dimension(1020,100));
//        tabbedPane.setFocusable(false);
//        JPanel lightPane = new JPanel();
        MenuJPanel lightPane = new MenuJPanel("/icon/界面-5-背景.png");
        lightPane.setPreferredSize(new Dimension(1240, 110));
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "-", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
//        lightPane.setBorder(tb);
        new LightBtnPane().show(lightPane);
        JPanel aboutPane = new JPanel();
        new AboutPane().show(aboutPane);
//        tabbedPane.add("      灯光编程       ", lightPane);
//        tabbedPane.add("       关于          ", aboutPane);

        JPanel bodyPane = new JPanel();
        bodyPane.setBorder(new LineBorder(Color.gray));
        bodyPane.setPreferredSize(new Dimension(1030, 620));
        new BodyPane().show(bodyPane);
        JPanel main = new JPanel();
        main.setBorder(new LineBorder(Color.gray));
        main.setPreferredSize(new Dimension(1230, 720));

        JPanel rjghtPane = new JPanel();
        rjghtPane.setBorder(new LineBorder(Color.gray));
        rjghtPane.setPreferredSize(new Dimension(185, 610));
        new RightPane().show(rjghtPane);

        main.add(lightPane);
        main.add(bodyPane);
        main.add(rjghtPane);
        frame.add(main);
//        frame.setVisible(true);
        frame.validate();
    }

}
