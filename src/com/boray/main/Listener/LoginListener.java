package com.boray.main.Listener;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.Utils.Util;
import com.boray.entity.Admin;
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
            param.put("logincode", username);
            param.put("password", password);
            System.out.println(Data.ipPort);
            String request = HttpClientUtil.doGet(Data.ipPort + "/js/a/jk/login", param);
           
            Users users = JSON.parseObject(request, Users.class);
            if(users.getCode()==0) {
            	users.setLoginstatus(1);
            }else {
            	users.setLoginstatus(1);
            }
            if (users != null && !users.getLoginstatus().equals(0)) {
                if (Data.RememberPassword) {
                    Data.userLogin.put(Util.encode(username), Util.encode(password));
                }
                MainUi.map.put("Users", users);
                JPanel p=(JPanel)MainUi.map.get("dapane");
             	
             
                p.removeAll();
                p.updateUI();
                init(p);
//                JPanel minePanel = (JPanel) MainUi.map.get("MinePanel");
//                new MineUI().show(minePanel);
              
               /* JPanel CompanyPanel = (JPanel) MainUi.map.get("CompanyPanel");
                new CompanyUI().show(CompanyPanel);
                JPanel uccnPanel = (JPanel) MainUi.map.get("UccnPanel");
                new UccnUI().show(uccnPanel);
                JPanel sharePanel = (JPanel) MainUi.map.get("SharePanel");
                new ShareUI().show(sharePanel);*/
               
            } else {
                JOptionPane.showMessageDialog(frame, "登录失败，用户名或密码不正确！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "登录失败，网络错误！", "提示", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void init(JPanel pane) {//界面调整
//      pane.setPreferredSize(new Dimension(350, 400));
//      JPanel jPanel = new JPanel();
//      jPanel.setPreferredSize(new Dimension(350, 200));
//      pane.add(jPanel);
//      pane.add(new JLabel("用户名："));
//      JTextField username = new JTextField(20);
//      MainUi.map.put("username", username);
//      pane.add(username);
//      JPasswordField password = new JPasswordField(20);
//      MainUi.map.put("password", password);
//      pane.add(new JLabel("密码：   "));
//      pane.add(password);
//      JButton clear = new JButton("清除");
//      JButton login = new JButton("登录");
//      clear.addActionListener(listener);
//      login.addActionListener(listener);
//
//      pane.add(clear);
//      pane.add(login);
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
      JToggleButton btn7 = new JToggleButton("切换账号");
      btn.setPreferredSize(new Dimension(98, 58));
      btn2.setPreferredSize(new Dimension(98, 58));
      btn3.setPreferredSize(new Dimension(98, 58));
      btn4.setPreferredSize(new Dimension(98, 58));
      btn5.setPreferredSize(new Dimension(98, 58));
      btn6.setPreferredSize(new Dimension(98, 58));
      btn7.setPreferredSize(new Dimension(98, 58));
      ButtonGroup group = new ButtonGroup();

      group.add(btn);
      group.add(btn2);
      group.add(btn3);
      group.add(btn4);
      group.add(btn5);
      group.add(btn6);
      group.add(btn7);
      btn.setSelected(true);
      btn2.setFocusable(false);
      btn3.setFocusable(false);
      btn4.setFocusable(false);
      btn5.setFocusable(false);
      btn6.setFocusable(false);
      btn7.setFocusable(false);

      leftPane.add(btn);
      leftPane.add(btn2);
      if (CheckAdmin()) {//查询是否是管理员
          leftPane.add(btn3);
      }
      leftPane.add(btn4);
      leftPane.add(btn5);
      leftPane.add(btn6);
      leftPane.add(btn7);
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
      btn7.addItemListener(listener);
      
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
      
      JPanel denglu = new JPanel();//登录
      new LoginUi2().show(denglu);

      rightPane.add(uccnPanel, "1");
      rightPane.add(minePanel, "2");
      rightPane.add(sharePanel, "3");
      rightPane.add(CompanyPanel, "4");
      rightPane.add(LocalPanel, "5");
      rightPane.add(TdUi, "6");


      pane.add(leftPane);
      pane.add(rightPane);
  }
    
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
            }
        }
        return flag;
    }
    
}
