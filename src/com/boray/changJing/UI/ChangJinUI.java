package com.boray.changJing.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import com.boray.changJing.Listener.ProgramToTestListener;
import com.boray.mainUi.MainUi;

public class ChangJinUI implements ActionListener{
	private boolean lgClick;
	public void show(JPanel pane){
		//1020,600
		pane.setLayout(new FlowLayout(FlowLayout.LEFT));
		//pane.setBorder(new LineBorder(Color.gray));
		//pane.add(new JLabel("����"));
		JPanel topPane = new JPanel();
		topPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		topPane.setBorder(new LineBorder(Color.gray));
		topPane.setPreferredSize(new Dimension(1010,40));
		JRadioButton radioButton = new JRadioButton("�������");
		JRadioButton radioButton2 = new JRadioButton("��������");
		ButtonGroup group = new ButtonGroup();
		group.add(radioButton);group.add(radioButton2);
		ProgramToTestListener listener = new ProgramToTestListener();
		radioButton.addActionListener(listener);radioButton2.addActionListener(listener);
		radioButton.setSelected(true);
		JButton btn = new JButton("ȫ��");
		JButton btn2 = new JButton("ȫ��");
		btn.addActionListener(this);btn2.addActionListener(this);
		topPane.add(radioButton);
		topPane.add(radioButton2);
		JPanel centerPane = new JPanel();
		//centerPane.setBorder(new LineBorder(Color.green));
		centerPane.setPreferredSize(new Dimension(660,34));
		JPanel p1 = new JPanel();
		FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
		flowLayout.setVgap(-4);
		p1.setLayout(flowLayout);
		p1.setPreferredSize(new Dimension(660,32));
		JButton btn4 = new JButton("����+");
		JButton btn5 = new JButton("����-");
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
		btn4.addActionListener(this);btn5.addActionListener(this);
		JLabel label = new JLabel("",JLabel.CENTER);
		label.setPreferredSize(new Dimension(48, 32));
		p1.add(btn4);p1.add(label);p1.add(btn5);
		p1.setVisible(false);
		MainUi.map.put("LiangDu", label);
		MainUi.map.put("changJingLiangDuPane", p1);
		centerPane.add(p1);
		topPane.add(centerPane);
		topPane.add(btn);topPane.add(btn2);
		
		JPanel leftPane = new JPanel();
		JPanel rightPane = new JPanel();
		new LeftPaneUI().show(leftPane);
		new RightPaneUI().show(rightPane);
		
		pane.add(topPane);
		pane.add(leftPane);
		pane.add(rightPane);
	}
	public void actionPerformed(ActionEvent e) {
		byte[] b = new byte[3];
		b[0] = (byte)0xFB;b[2] = (byte)0xC0;
		/*ȫ�أ�FB 0B C0
		ȫ����FB 0C C0
		���ȼӣ�FB 19 B1
		���ȼ���FB 19 B0
		*/
		if ("ȫ��".equals(e.getActionCommand())) {
			b[1] =  0x0C;
		} else if ("ȫ��".equals(e.getActionCommand())) {
			b[1] =  0x0B;
		} else if ("����+".equals(e.getActionCommand())) {
			b[0] = (byte)0xFB;
			b[1] =  0x19;
			b[2] = (byte)0xB1;
		} else if ("����-".equals(e.getActionCommand())) {
			b[0] = (byte)0xFB;
			b[1] =  0x19;
			b[2] = (byte)0xB0;
		}
		if (Data.serialPort != null) {
			try {
				OutputStream os = Data.serialPort.getOutputStream();
				os.write(b);
				os.flush();
				os.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
