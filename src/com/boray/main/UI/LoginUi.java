package com.boray.main.UI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.boray.main.Listener.LoginListener;
import com.boray.main.Listener.RegisterListener;
import com.boray.mainUi.MainUi;

public class LoginUi  {

	private LoginListener listener;

    public void show(JPanel panel) {
    
        	 panel.removeAll();//������пؼ������²���
             panel.updateUI();
            JPanel jPanel2 = new JPanel();
            jPanel2.setPreferredSize(new Dimension(900, 588));
            JPanel jPanel = new JPanel();
            jPanel.setPreferredSize(new Dimension(800, 150));
            panel.add(jPanel);
            JPanel pane = new JPanel();
            pane.setPreferredSize(new Dimension(350, 200));
            pane.add(new JLabel("�û�����"));
            JTextField username = new JTextField(20);
            MainUi.map.put("UccnUsername", username);
            pane.add(username);
            JPasswordField password = new JPasswordField(20);
            MainUi.map.put("UccnPassword", password);
            pane.add(new JLabel("���룺   "));
            pane.add(password);
            JButton clear = new JButton("���");
            JButton login = new JButton("��¼");
            JButton register = new JButton("ע��");
            login.setName("Uccn");
            clear.setName("Uccn");

            listener = new LoginListener(panel);
            login.addActionListener(listener);
            clear.addActionListener(listener);
            register.addActionListener(new RegisterListener());

            pane.add(clear);
            pane.add(login);
            pane.add(register);
            jPanel2.add(jPanel);
            jPanel2.add(pane);
            panel.add(jPanel2, BorderLayout.CENTER);
        } 
        
    

}
