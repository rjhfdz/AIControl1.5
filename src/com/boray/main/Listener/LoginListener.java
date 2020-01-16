package com.boray.main.Listener;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.Utils.Util;
import com.boray.entity.Users;
import com.boray.main.UI.*;
import com.boray.main.Util.IpConfig;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class LoginListener implements ActionListener {

    private JPanel panel;
    private CardLayout card;
    private JPanel rightPane;
    private JFrame frame;

    public LoginListener(JPanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        frame = (JFrame) MainUi.map.get("frame");
        String str = button.getName();
        if ("登录".equals(button.getText())) {
            String username = "";
            String password = "";
            if (str.equals("Company")) {
                username = ((JTextField) MainUi.map.get("CompanyUsername")).getText();
                password = new String(((JPasswordField) MainUi.map.get("CompanyPassword")).getPassword());
            } else if (str.equals("Share")) {
                username = ((JTextField) MainUi.map.get("ShareUsername")).getText();
                password = new String(((JPasswordField) MainUi.map.get("SharePassword")).getPassword());
            } else if (str.equals("Uccn")) {
                username = ((JTextField) MainUi.map.get("UccnUsername")).getText();
                password = new String(((JPasswordField) MainUi.map.get("UccnPassword")).getPassword());
            }

            if (username == null || username == "") {
                JOptionPane.showMessageDialog(frame, "登录失败，用户名不能为空！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            if (password == null || password == "") {
                JOptionPane.showMessageDialog(frame, "登录失败，密码不能为空！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            login(username, password);
        } else {
            if (str.equals("Company")) {
                ((JTextField) MainUi.map.get("CompanyUsername")).setText("");
                ((JPasswordField) MainUi.map.get("CompanyPassword")).setText("");
            } else if (str.equals("Share")) {
                ((JTextField) MainUi.map.get("ShareUsername")).setText("");
                ((JPasswordField) MainUi.map.get("SharePassword")).setText("");
            } else if (str.equals("Uccn")) {
                ((JTextField) MainUi.map.get("UccnUsername")).setText("");
                ((JPasswordField) MainUi.map.get("UccnPassword")).setText("");
            }
        }
    }

    private void login(String username, String password) {
        try {
            IpConfig config = new IpConfig();
            config.getIpConfig();
            Map<String, String> param = new HashMap<>();
            param.put("username", username);
            param.put("userpassword", password);
            String request = HttpClientUtil.doGet(Data.ipPort + "logindl", param);
            Users users = JSON.parseObject(request, Users.class);
            if (users != null && !users.getLoginstatus().equals(0)) {
                if (Data.RememberPassword) {
                    Data.userLogin.put(Util.encode(username), Util.encode(password));
                }
                MainUi.map.put("Users", users);
//                init();
//                JPanel minePanel = (JPanel) MainUi.map.get("MinePanel");
//                new MineUI().show(minePanel);
                JPanel sharePanel = (JPanel) MainUi.map.get("SharePanel");
                new ShareUI().show(sharePanel);
                JPanel CompanyPanel = (JPanel) MainUi.map.get("CompanyPanel");
                new CompanyUI().show(CompanyPanel);
                JPanel uccnPanel = (JPanel) MainUi.map.get("UccnPanel");
                new UccnUI().show(uccnPanel);
            } else {
                JOptionPane.showMessageDialog(frame, "登录失败，用户名或密码不正确！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "登录失败，网络错误！", "提示", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void init() {
        panel.removeAll();//清除所有控件，重新布局
        panel.updateUI();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel leftPane = new JPanel();
        leftPane.setBorder(new LineBorder(Color.gray));
        leftPane.setPreferredSize(new Dimension(108, 588));
        JToggleButton btn = new JToggleButton("我的项目");
        JToggleButton btn2 = new JToggleButton("公司项目");
        JToggleButton btn3 = new JToggleButton("本地项目");
        btn.setPreferredSize(new Dimension(98, 58));
        btn2.setPreferredSize(new Dimension(98, 58));
        btn3.setPreferredSize(new Dimension(98, 58));
        ButtonGroup group = new ButtonGroup();
        group.add(btn);
        group.add(btn2);
        group.add(btn3);
        btn.setSelected(true);
        btn.setFocusable(false);
        btn2.setFocusable(false);
        btn3.setFocusable(false);
        leftPane.add(btn);
        leftPane.add(btn2);
        leftPane.add(btn3);

        rightPane = new JPanel();
        card = new CardLayout();
        rightPane.setLayout(card);

        SelectListener listener = new SelectListener(card, rightPane);
        btn.addItemListener(listener);
        btn2.addItemListener(listener);
        btn3.addItemListener(listener);

//        JPanel minePanel = new JPanel();//我的项目
//        new MineUI().show(minePanel);

        JPanel CompanyPanel = new JPanel();//公司项目
        new CompanyUI().show(CompanyPanel);

        JPanel LocalPanel = new JPanel();//本地项目
        new LocalUI().show(LocalPanel);

//        rightPane.add(minePanel, "1");
        rightPane.add(CompanyPanel, "2");
        rightPane.add(LocalPanel, "3");

        panel.add(leftPane);
        panel.add(rightPane);
    }
}
