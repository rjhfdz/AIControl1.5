package com.boray.main.Listener;

import com.boray.main.UI.CompanyUI;
import com.boray.main.UI.LocalUI;
import com.boray.main.UI.MineUI;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginListener implements ActionListener {

    private JPanel panel;

    private CardLayout card;
    private JPanel rightPane;

    public LoginListener(JPanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        JFrame frame = (JFrame) MainUi.map.get("frame");
        if ("��¼".equals(button.getText())) {
            String username = ((JTextField) MainUi.map.get("username")).getText();
            String password = ((JPasswordField) MainUi.map.get("password")).getPassword().toString();
            if (username == null || username == "") {
                JOptionPane.showMessageDialog(frame, "��¼ʧ�ܣ��û�������Ϊ�գ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            if (password == null || password == "") {
                JOptionPane.showMessageDialog(frame, "��¼ʧ�ܣ����벻��Ϊ�գ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            init();
        } else {
            JTextField username = (JTextField) MainUi.map.get("username");
            JPasswordField password = (JPasswordField) MainUi.map.get("password");
            username.setText("");
            password.setText("");
        }
    }

    private void init() {
        panel.removeAll();//������пؼ������²���
        panel.updateUI();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel leftPane = new JPanel();
        leftPane.setBorder(new LineBorder(Color.gray));
        leftPane.setPreferredSize(new Dimension(108, 588));
        JToggleButton btn = new JToggleButton("�ҵ���Ŀ");
        JToggleButton btn2 = new JToggleButton("��˾��Ŀ");
        JToggleButton btn3 = new JToggleButton("������Ŀ");
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

        JPanel minePanel = new JPanel();//�ҵ���Ŀ
        new MineUI().show(minePanel);

        JPanel CompanyPanel = new JPanel();//��˾��Ŀ
        new CompanyUI().show(CompanyPanel);

        JPanel LocalPanel = new JPanel();//������Ŀ
        new LocalUI().show(LocalPanel);

        rightPane.add(minePanel, "1");
        rightPane.add(CompanyPanel, "2");
        rightPane.add(LocalPanel, "3");

        panel.add(leftPane);
        panel.add(rightPane);
    }
}