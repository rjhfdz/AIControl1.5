package com.boray.xiaoGuoDeng.Listener;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import com.boray.Data.XiaoGuoDengModel;
import com.boray.mainUi.MainUi;

public class DMXModelListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		XiaoGuoDengModel.model = Integer.valueOf(e.getActionCommand().substring(3)).intValue();
		JPanel p30 = (JPanel)MainUi.map.get("XiaoGuoRightPane_8");
		CardLayout cardLayout = (CardLayout)MainUi.map.get("XiaoGuoCardLayout_8");
		cardLayout.show(p30, XiaoGuoDengModel.model+"");
	}
}
