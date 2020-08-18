package com.boray.main.UI;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.Admin;
import com.boray.entity.Users;
import com.boray.main.Listener.LoginListener;
import com.boray.main.Listener.SelectListener;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class BorayMainUI {

    private LoginListener listener;
    private CardLayout card;
    private JPanel rightPane;

    public void show(JPanel pane) {
        pane.setBorder(new LineBorder(Color.black));

        listener = new LoginListener(pane);

        JPanel p1 = new JPanel();
        init(p1);

        pane.add(p1, BorderLayout.CENTER);
    }

    private void init(JPanel pane) {//界面调整
//        pane.setPreferredSize(new Dimension(350, 400));
//        JPanel jPanel = new JPanel();
//        jPanel.setPreferredSize(new Dimension(350, 200));
//        pane.add(jPanel);
//        pane.add(new JLabel("用户名："));
//        JTextField username = new JTextField(20);
//        MainUi.map.put("username", username);
//        pane.add(username);
//        JPasswordField password = new JPasswordField(20);
//        MainUi.map.put("password", password);
//        pane.add(new JLabel("密码：   "));
//        pane.add(password);
//        JButton clear = new JButton("清除");
//        JButton login = new JButton("登录");
//        clear.addActionListener(listener);
//        login.addActionListener(listener);
//
//        pane.add(clear);
//        pane.add(login);
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel leftPane = new JPanel();
        leftPane.setBorder(new LineBorder(Color.gray));
        leftPane.setPreferredSize(new Dimension(108, 588));
        JToggleButton btn = new JToggleButton("官方发布");
        JToggleButton btn2 = new JToggleButton("个人管理");
        JToggleButton btn3 = new JToggleButton("成员管理");
        JToggleButton btn4 = new JToggleButton("设置团队");
        JToggleButton btn5 = new JToggleButton("本地项目");
        JToggleButton btn6 = new JToggleButton("团队资料");
//        JToggleButton btn7 = new JToggleButton("切换用户");
        btn.setPreferredSize(new Dimension(98, 58));
        btn2.setPreferredSize(new Dimension(98, 58));
        btn3.setPreferredSize(new Dimension(98, 58));
        btn4.setPreferredSize(new Dimension(98, 58));
        btn5.setPreferredSize(new Dimension(98, 58));
        btn6.setPreferredSize(new Dimension(98, 58));
//        btn7.setPreferredSize(new Dimension(98, 58));
        ButtonGroup group = new ButtonGroup();
        group.add(btn);
        group.add(btn2);
        group.add(btn3);
        group.add(btn4);
        group.add(btn5);
        group.add(btn6);
//        group.add(btn7);
        btn.setSelected(true);
        btn2.setFocusable(false);
        btn3.setFocusable(false);
        btn4.setFocusable(false);

        leftPane.add(btn2);
        leftPane.add(btn4);
        leftPane.add(btn);
        leftPane.add(btn2);
        
        MainUi.map.put("ShareBtn",btn3);
        if (CheckAdmin()) {//查询是否是管理员
        leftPane.add(btn3);
        btn3.setVisible(false);
   
        }
        leftPane.add(btn4);
        leftPane.add(btn5);
        leftPane.add(btn6);
//        leftPane.add(btn7);

        JLabel LogoutLabel = new JLabel("<html>注销</html>");
        LogoutLabel.setVisible(true);
        MainUi.map.put("LogoutLabel", LogoutLabel);
        LogoutLabel.setForeground(Color.BLUE);
        LogoutLabel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseReleased(MouseEvent e) {
                Object[] options = {"否", "是"};
                int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "是否注销？", "提示",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, options, options[1]);
                if (yes == 1) {
                    MainUi.map.remove("Users");
                    JLabel LogoutLabel = (JLabel) MainUi.map.get("LogoutLabel");
                    LogoutLabel.setVisible(false);
                    new UccnUI().show((JPanel) MainUi.map.get("UccnPanel"));
                    new MineUI().show((JPanel) MainUi.map.get("MinePanel"));
                    new ShareUI().show((JPanel) MainUi.map.get("SharePanel"));
                    new CompanyUI().show((JPanel) MainUi.map.get("CompanyPanel"));
                    new TdUi().show((JPanel) MainUi.map.get("TdUiPanel"));
                }
            }
        });

        leftPane.add(LogoutLabel);
        rightPane = new JPanel();
        card = new CardLayout();
        rightPane.setLayout(card);

        SelectListener listener = new SelectListener(card, rightPane);
        btn.addItemListener(listener);
        btn2.addItemListener(listener);
        btn3.addItemListener(listener);
        btn4.addItemListener(listener);
        btn5.addItemListener(listener);
        btn6.addItemListener(listener);
//        btn7.addItemListener(listener);

        JPanel uccnPanel = new JPanel();//官方发布
        new UccnUI().show(uccnPanel);
        MainUi.map.put("UccnPanel", uccnPanel);

        JPanel minePanel = new JPanel();//个人管理
        new MineUI().show(minePanel);
        MainUi.map.put("MinePanel", minePanel);

        JPanel sharePanel = new JPanel();//成员管理
        if (CheckAdmin()) {//查询是否是管理员
            new ShareUI().show(sharePanel);
        }

        MainUi.map.put("SharePanel", sharePanel);


        JPanel CompanyPanel = new JPanel();//设置团队
        new CompanyUI().show(CompanyPanel);
        MainUi.map.put("CompanyPanel", CompanyPanel);

        JPanel LocalPanel = new JPanel();//本地项目
        new LocalUI().show(LocalPanel);

        JPanel TdUi = new JPanel();//团队资料
        new TdUi().show(TdUi);
        MainUi.map.put("TdUiPanel", TdUi);

//        JPanel denglu = new JPanel();//登录
//        new LoginUi().show(denglu);

        rightPane.add(uccnPanel, "1");
        rightPane.add(minePanel, "2");
        rightPane.add(sharePanel, "3");
        rightPane.add(CompanyPanel, "4");
        rightPane.add(LocalPanel, "5");
        rightPane.add(TdUi, "6");
//        rightPane.add(denglu, "7");

        pane.add(leftPane);
        pane.add(rightPane);
    }

    /**
     * 检查当前登录的用户是否是管理员
     *
     * @return
     */
    private boolean CheckAdmin() {
        boolean flag = false;
        Users user = (Users) MainUi.map.get("Users");
        if (user != null) {
            Map<String, String> param = new HashMap<>();
            param.put("usercode", user.getUsercode());
            String request = HttpClientUtil.doGet(Data.ipPort + "js/a/jk/getguanliyuan", param);
            Admin admin = JSON.parseObject(request, Admin.class);
            if (admin.getCode() == 0) {
                flag = true;
                MainUi.map.put("admin", admin);
                JToggleButton btn = (JToggleButton) MainUi.map.get("ShareBtn");
                btn.setVisible(true);
            }
        }
        return flag;
    }
}
