package com.boray.main.UI;

import com.boray.main.Listener.LoginListener;
import com.boray.main.Listener.SelectListener;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

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
        leftPane.setPreferredSize(new Dimension(108, 588));
        JToggleButton btn = new JToggleButton("����ƽ̨");
        JToggleButton btn2 = new JToggleButton("��Ŀ����");
        JToggleButton btn3 = new JToggleButton("������Ŀ");
        JToggleButton btn4 = new JToggleButton("�ٷ�����");
        btn.setPreferredSize(new Dimension(98, 58));
        btn2.setPreferredSize(new Dimension(98, 58));
        btn3.setPreferredSize(new Dimension(98, 58));
        btn4.setPreferredSize(new Dimension(98, 58));
        ButtonGroup group = new ButtonGroup();
        group.add(btn);
        group.add(btn2);
        group.add(btn3);
        group.add(btn4);
        btn2.setSelected(true);
        btn.setFocusable(false);
        btn2.setFocusable(false);
        btn3.setFocusable(false);
        btn4.setFocusable(false);

        leftPane.add(btn2);
        leftPane.add(btn4);
        leftPane.add(btn);
        leftPane.add(btn3);

        rightPane = new JPanel();
        card = new CardLayout();
        rightPane.setLayout(card);

        SelectListener listener = new SelectListener(card, rightPane);
        btn.addItemListener(listener);
        btn2.addItemListener(listener);
        btn3.addItemListener(listener);
        btn4.addItemListener(listener);

//        JPanel minePanel = new JPanel();//�ҵ���Ŀ
//        new MineUI().show(minePanel);
//        MainUi.map.put("MinePanel", minePanel);

        JPanel sharePanel = new JPanel();//����ƽ̨
        new ShareUI().show(sharePanel);
        MainUi.map.put("SharePanel", sharePanel);

        JPanel uccnPanel = new JPanel();//�ٷ�����
        new UccnUI().show(uccnPanel);
        MainUi.map.put("UccnPanel", uccnPanel);

        JPanel CompanyPanel = new JPanel();//��Ŀ����
        new CompanyUI().show(CompanyPanel);
        MainUi.map.put("CompanyPanel", CompanyPanel);

        JPanel LocalPanel = new JPanel();//������Ŀ
        new LocalUI().show(LocalPanel);


        rightPane.add(CompanyPanel, "2");
        rightPane.add(sharePanel, "1");
        rightPane.add(LocalPanel, "3");
        rightPane.add(uccnPanel, "4");

        pane.add(leftPane);
        pane.add(rightPane);
    }
}
