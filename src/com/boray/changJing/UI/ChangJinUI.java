package com.boray.changJing.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.Socket;
import com.boray.changJing.Listener.ProgramToTestListener;
import com.boray.mainUi.MainUi;

public class ChangJinUI implements ActionListener {
    private boolean lgClick;
    private CardLayout card;
    private JPanel rightPane;

    public void show(JPanel pane) {

        rightPane = new JPanel();
        card = new CardLayout();
        rightPane.setLayout(card);

        //1020,600
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));
        //pane.setBorder(new LineBorder(Color.gray));
        //pane.add(new JLabel("场景"));
        JPanel topPane = new JPanel();
        topPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPane.setBorder(new LineBorder(Color.gray));
        topPane.setPreferredSize(new Dimension(1010, 40));
        JRadioButton radioButton = new JRadioButton("场景编程");
        JRadioButton radioButton2 = new JRadioButton("场景测试");
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton);
        group.add(radioButton2);
        ProgramToTestListener listener = new ProgramToTestListener(card, rightPane);
        radioButton.addActionListener(listener);
        radioButton2.addActionListener(listener);
        radioButton2.setSelected(true);
        JButton btn = new JButton("全开");
        JButton btn2 = new JButton("全关");
        btn.addActionListener(this);
        btn2.addActionListener(this);
        topPane.add(radioButton);
        topPane.add(radioButton2);
        JPanel centerPane = new JPanel();
        //centerPane.setBorder(new LineBorder(Color.green));
        centerPane.setPreferredSize(new Dimension(660, 34));
        JPanel p1 = new JPanel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(-4);
        p1.setLayout(flowLayout);
        p1.setPreferredSize(new Dimension(660, 32));
        JButton btn4 = new JButton("亮度+");
        JButton btn5 = new JButton("亮度-");
		/*MouseListener mouseListener = new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				lgClick = false;
			}
			public void mousePressed(MouseEvent e) {
				lgClick = true;
				int a = 0;
				while (lgClick) {
					try {
						//Thread.sleep(50);
					} catch (Exception e2) {
					}
					System.out.println(a++);
				}
			}
		};
		btn4.addMouseListener(mouseListener);*/
        btn4.addActionListener(this);
        btn5.addActionListener(this);
        JLabel label = new JLabel("", JLabel.CENTER);
        label.setPreferredSize(new Dimension(48, 32));
        p1.add(btn4);
        p1.add(label);
        p1.add(btn5);
//        p1.setVisible(true);
        MainUi.map.put("LiangDu", label);
        MainUi.map.put("changJingLiangDuPane", p1);
        centerPane.add(p1);
        topPane.add(centerPane);
        topPane.add(btn);
        topPane.add(btn2);

        JPanel programme = new JPanel();
        new ProgrammePaneUI().show(programme);
        JPanel test = new JPanel();
        new TestPaneUI().show(test);
//		JPanel leftPane = new JPanel();
//		JPanel rightPane = new JPanel();
//		new LeftPaneUI().show(leftPane);
//		new RightPaneUI().show(rightPane);

        rightPane.add(test, "场景测试");
        rightPane.add(programme, "场景编程");

        pane.add(topPane);
        pane.add(rightPane);
//		pane.add(leftPane);
//		pane.add(rightPane);
    }

    public void actionPerformed(ActionEvent e) {
        byte[] b = new byte[3];
        b[0] = (byte) 0xFB;
        b[2] = (byte) 0xC0;
		/*全关：FB 0B C0
		全开：FB 0C C0
		亮度加：FB 19 B1
		亮度减：FB 19 B0
		*/
        if ("全开".equals(e.getActionCommand())) {
            b[1] = 0x0C;
        } else if ("全关".equals(e.getActionCommand())) {
            b[1] = 0x0B;
        } else if ("亮度+".equals(e.getActionCommand())) {
            b[0] = (byte) 0xFB;
            b[1] = 0x19;
            b[2] = (byte) 0xB1;
        } else if ("亮度-".equals(e.getActionCommand())) {
            b[0] = (byte) 0xFB;
            b[1] = 0x19;
            b[2] = (byte) 0xB0;
        }
        if (Data.serialPort != null) {
            Socket.SerialPortSendData(b);
        } else if (Data.socket != null) {
            Socket.UDPSendData(b);
        }
    }
}
