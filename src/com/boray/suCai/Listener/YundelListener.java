package com.boray.suCai.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.boray.entity.Users;
import com.boray.mainUi.MainUi;
import com.boray.suCai.UI.DialogSuCaiUI;

public class YundelListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

//		JList suCai_list = (JList)MainUi.map.get("suCai_list");
//		JList suCaiLightType = (JList)MainUi.map.get("suCaiLightType");
//		JList yunsc = (JList) MainUi.map.get("yunsc");
//
//		JList suCaiLightType1 = (JList)MainUi.map.get("suCaiLightType");
//		
//		if (suCai_list.getSelectedIndex()!=-1) {
//			String suCaiName = suCai_list.getSelectedValue().toString();
//		
//			
//			int denKuNum = suCaiLightType.getSelectedIndex();
//			
//			
//	}
//		System.out.println(suCaiLightType.getSelectedIndex());
//		
        Users users = (Users) MainUi.map.get("Users");

        if (users != null && users.getLoginstatus() != 0) {

        } else {
            JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "ÇëµÇÂ¼", "¾¯¸æ", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog();
        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new JDialog(f, true);
        dialog.setResizable(false);
        int width = 720, height = 620;
        dialog.setSize(width, height);
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - width / 2, f.getLocation().y + f.getSize().height / 2 - height / 2);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel p = new JPanel();
        new DialogSuCaiUI().show(p);
        dialog.getContentPane().add(p);
        dialog.setVisible(true);

    }
}
