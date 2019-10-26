package com.boray.mainUi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.beiFen.Listener.ProjectCreateFileActionListener;
import com.boray.beiFen.Listener.ProjectCreateFileOfCloseFrame;

public class MainUi {
	public static Map map;
	private JFrame frame;
	public static void main(String[] args) {
		new MainUi().show();
	}
	public void show() {
		try {
	         //JDialog.setDefaultLookAndFeelDecorated(true); 
	         UIManager.setLookAndFeel(ch.randelshofer.quaqua.QuaquaManager.getLookAndFeel());
	         UIManager.put("FileChooser.cancelButtonText", "ȡ��");
	         UIManager.put("FileChooser.saveButtonText", "����");
	         UIManager.put("FileChooser.openButtonText", "��");
	         UIManager.put("FileChooser.newFolderButtonText", "�½��ļ���");
	         UIManager.put("OptionPane.yesButtonText", "��");
	         UIManager.put("OptionPane.noButtonText", "��"); 
			} 
		catch (Exception e) {
			e.printStackTrace();
		}
		map = new HashMap();
		frame = new JFrame("9ϵ�����ܿ�����-���߱��ϵͳV1.0");
		map.put("frame", frame);
		
		int screenWidth=((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().width);
		int screenHeight = ((int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().height);
		int frameWidth = 1040;
		int frameHeight = 744;
		frame.setSize(frameWidth, frameHeight);
		frame.setLocation(screenWidth/2-frameWidth/2, screenHeight/2-frameHeight/2);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowFocusListener(new WindowFocusListener() {
			public void windowLostFocus(WindowEvent e) {
			}
			public void windowGainedFocus(WindowEvent e) {
				frame.validate();
			}
		});
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (Data.serialPort != null) {
					try {
						OutputStream os = Data.serialPort.getOutputStream();
						os.write(ZhiLingJi.setBackBaut());
						os.flush();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
				Object[] options = { "��", "��" };
				int yes = JOptionPane.showOptionDialog((JFrame)MainUi.map.get("frame"), "�Ƿ�Ҫ���湤�̣�", "����",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
				null, options, options[1]);
				if (yes == 1) {
					new ProjectCreateFileOfCloseFrame().save();
				}
				Runtime.getRuntime().halt(1);
			}
			public void windowOpened(WindowEvent e) {
				
			}
		});
		frame.setResizable(false);
		init();
		frame.setIconImage(new ImageIcon(getClass().getResource("/icon/boray.png")).getImage());
		frame.setVisible(true);
	}
	private void init(){
		frame.setLayout(new FlowLayout(FlowLayout.LEFT));
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		tabbedPane.setPreferredSize(new Dimension(1020,100));
		tabbedPane.setFocusable(false);
		//tabbedPane.setUI((TabbedPaneUI)BasicTabbedPaneUI.createUI(tabbedPane));
		JPanel lightPane = new JPanel();
		lightPane.setPreferredSize(new Dimension(1020,110));
		TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "-", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		lightPane.setBorder(tb);
		//lightPane.setBorder(new LineBorder(Color.gray));
		new LightBtnPane().show(lightPane);
		JPanel aboutPane = new JPanel();
		new AboutPane().show(aboutPane);
		tabbedPane.add("      �ƹ���       ", lightPane);
		tabbedPane.add("       ����          ", aboutPane);
		
		JPanel bodyPane = new JPanel();
		//bodyPane.setBorder(new LineBorder(Color.gray));
		bodyPane.setPreferredSize(new Dimension(1020,600));
		new BodyPane().show(bodyPane);
		frame.add(lightPane);
		frame.add(bodyPane);
	}
}
