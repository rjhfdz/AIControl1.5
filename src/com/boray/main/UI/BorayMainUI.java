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
import javax.swing.text.StyledEditorKit;
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

    private void init(JPanel pane) {//�������
//        pane.setPreferredSize(new Dimension(350, 400));
//        JPanel jPanel = new JPanel();
//        jPanel.setPreferredSize(new Dimension(350, 200));
//        pane.add(jPanel);
//        pane.add(new JLabel("�û�����"));
//        JTextField username = new JTextField(20);
//        MainUi.map.put("username", username);
//        pane.add(username);
//        JPasswordField password = new JPasswordField(20);
//        MainUi.map.put("password", password);
//        pane.add(new JLabel("���룺   "));
//        pane.add(password);
//        JButton clear = new JButton("���");
//        JButton login = new JButton("��¼");
//        clear.addActionListener(listener);
//        login.addActionListener(listener);
//
//        pane.add(clear);
//        pane.add(login);
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel leftPane = new JPanel();
        leftPane.setBorder(new LineBorder(Color.gray));
        leftPane.setPreferredSize(new Dimension(130, 588));
        JToggleButton btn = new JToggleButton("�ٷ�����");
        JToggleButton btn2 = new JToggleButton("���˹���");
        JToggleButton btn3 = new JToggleButton("��Ա����");
        JToggleButton btn4 = new JToggleButton("�����Ŷ�");
        JToggleButton btn5 = new JToggleButton("������Ŀ");
        btn.setPreferredSize(new Dimension(98, 58));
        btn2.setPreferredSize(new Dimension(98, 58));
        btn3.setPreferredSize(new Dimension(98, 58));
        btn4.setPreferredSize(new Dimension(98, 58));
        btn5.setPreferredSize(new Dimension(98, 58));
        ButtonGroup group = new ButtonGroup();

        group.add(btn);
        group.add(btn2);
        group.add(btn3);
        group.add(btn4);
        group.add(btn5);
        btn.setSelected(true);
        btn2.setFocusable(false);
        btn3.setFocusable(false);
        btn4.setFocusable(false);
        btn5.setFocusable(false);


        leftPane.add(btn);
        leftPane.add(btn2);
        if (CheckAdmin()) {//��ѯ�Ƿ��ǹ���Ա
            leftPane.add(btn3);
        }
        leftPane.add(btn4);
        leftPane.add(btn5);
        if (CheckAdmin()) {//��ѯ�Ƿ��ǹ���Ա
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(120, 200));
            leftPane.add(panel);
        } else {
            JPanel panel = new JPanel();
            panel.setPreferredSize(new Dimension(120, 260));
            leftPane.add(panel);
        }
        JLabel UserLabel = new JLabel("�û�:SYSTEM");
        UserLabel.setVisible(false);
        UserLabel.setPreferredSize(new Dimension(120,30));
        MainUi.map.put("UserLabel", UserLabel);
        JLabel LogoutLabel = new JLabel("<html>ע��</html>");
        LogoutLabel.setVisible(false);
        MainUi.map.put("LogoutLabel", LogoutLabel);
        LogoutLabel.setForeground(Color.BLUE);
        LogoutLabel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseReleased(MouseEvent e) {
                Object[] options = {"��", "��"};
                int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "�Ƿ�ע����", "��ʾ",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, options, options[1]);
                if(yes == 1){
                    MainUi.map.remove("Users");
                    JLabel UserLabel = (JLabel) MainUi.map.get("UserLabel");
                    JLabel LogoutLabel = (JLabel) MainUi.map.get("LogoutLabel");
                    UserLabel.setVisible(false);
                    LogoutLabel.setVisible(false);
                    new UccnUI().show((JPanel) MainUi.map.get("UccnPanel"));
                    new MineUI().show((JPanel) MainUi.map.get("MinePanel"));
                    new ShareUI().show((JPanel) MainUi.map.get("SharePanel"));
                    new CompanyUI().show((JPanel) MainUi.map.get("CompanyPanel"));
                }
            }
        });

        leftPane.add(UserLabel);
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

        JPanel uccnPanel = new JPanel();//�ٷ�����
        new UccnUI().show(uccnPanel);
        MainUi.map.put("UccnPanel", uccnPanel);

        JPanel minePanel = new JPanel();//���˹���
        new MineUI().show(minePanel);
        MainUi.map.put("MinePanel", minePanel);

        JPanel sharePanel = new JPanel();//��Ա����
        new ShareUI().show(sharePanel);
        MainUi.map.put("SharePanel", sharePanel);


        JPanel CompanyPanel = new JPanel();//�����Ŷ�
//        new CompanyUI().show(CompanyPanel);
        MainUi.map.put("CompanyPanel", CompanyPanel);

        JPanel LocalPanel = new JPanel();//������Ŀ
        new LocalUI().show(LocalPanel);

        rightPane.add(uccnPanel, "1");
        rightPane.add(minePanel, "2");
        rightPane.add(sharePanel, "3");
        rightPane.add(CompanyPanel, "4");
        rightPane.add(LocalPanel, "5");

//        VisibleUser();
        pane.add(leftPane);
        pane.add(rightPane);
    }

    /**
     * ��鵱ǰ��¼���û��Ƿ��ǹ���Ա
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
            }
        }
        return flag;
    }

    /**
     * ��ʾ��ǰ��¼�û�
     */
    private void VisibleUser() {
        Users users = (Users) MainUi.map.get("Users");
        JLabel UserLabel = (JLabel) MainUi.map.get("UserLabel");
        JLabel LogoutLabel = (JLabel) MainUi.map.get("LogoutLabel");
        UserLabel.setText("�û�:" + users.getUsercode());
        UserLabel.setVisible(true);
        LogoutLabel.setVisible(true);
    }
}
