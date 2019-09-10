package com.boray.beiFen.UI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.boray.beiFen.Listener.BackupActionListener;
import com.boray.mainUi.MainUi;

public class JingDuUI {
	public static boolean closeDialog = true;
	public void show(){
		closeDialog = true;
		JFrame f = (JFrame)MainUi.map.get("frame");
		final JDialog dialog = new JDialog(f,true);
		MainUi.map.put("JingDuDialog", dialog);
		dialog.setResizable(false);
		dialog.setTitle("进度");
		dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
		//dialog.setLayout(new BorderLayout());
		dialog.setLocation(f.getLocation().x+f.getSize().width/2-200,f.getLocation().y+f.getSize().height/2-65);
		dialog.setSize(400, 130);
		init(dialog);
		//dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Object[] options = { "否", "是" };
				int yes = JOptionPane.showOptionDialog((JFrame)MainUi.map.get("frame"), "是否退出？", "警告",
				JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
				null, options, options[1]);
				if (yes == 1) {
					closeDialog = false;
					dialog.dispose();
				} else {
					
				}
				
			}
		});
		dialog.setVisible(true);
	}

	private void init(JDialog dialog) {
		FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
		layout.setVgap(34);
		dialog.setLayout(layout);
		JProgressBar progressBar = new JProgressBar();
		MainUi.map.put("progressBar", progressBar);
		
		progressBar.setMinimum(0);
		progressBar.setMaximum(BackupActionListener.packetCount);
		progressBar.setIndeterminate(false);
		//progressBar.setStringPainted(true);  
		progressBar.setPreferredSize(new Dimension(308,18));
		//progressBar.setString("配置加载中...");
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI(progressBar);
			UIManager.setLookAndFeel(ch.randelshofer.quaqua.QuaquaManager.getLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//progressBar.setVisible(true);
		dialog.add(progressBar);
		JLabel label = new JLabel("0/"+BackupActionListener.packetCount,SwingConstants.CENTER);
		label.setPreferredSize(new Dimension(68,32));
		MainUi.map.put("JingDuLabel", label);
		dialog.add(label);
	}
}
