package com.boray.suCai.Listener;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.boray.Data.Data;
import com.boray.mainUi.MainUi;

public class CreateOrDelSuCaiListener implements ActionListener{
	public void actionPerformed(ActionEvent e) {
		if ("新建".equals(e.getActionCommand())) {
			JList suCaiLightType = (JList)MainUi.map.get("suCaiLightType");
			if (suCaiLightType.getSelectedValue()!=null) {
				JFrame f = (JFrame)MainUi.map.get("frame");
				JDialog dialog = new JDialog(f,true);
				dialog.setResizable(false);
				dialog.setTitle("新建素材");
				int w = 380,h = 180;
				dialog.setLocation(f.getLocation().x+f.getSize().width/2-w/2,f.getLocation().y+f.getSize().height/2-h/2);
				dialog.setSize(w,h);
				dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				init(dialog);
				dialog.setVisible(true);
			}
		} else {
			System.out.println("删除");
		}
		
	}

	private void init(final JDialog dialog) {
		JPanel p1 = new JPanel();
		p1.add(new JLabel("素材名称："));
		final JTextField field = new JTextField(15);
		p1.add(field);
		JPanel p2 = new JPanel();
		JButton btn1 = new JButton("确定");
		JButton btn2 = new JButton("取消");
		 
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ("取消".equals(e.getActionCommand())) {
					dialog.dispose();
				} else {
					if (!"".equals(field.getText().trim())) {
						JList list = (JList)MainUi.map.get("suCai_list");
						DefaultListModel  model = (DefaultListModel)list.getModel();
						
						//////////////////获取素材数量
						JList suCaiLightType = (JList)MainUi.map.get("suCaiLightType");
						Map map2 = (Map)Data.suCaiMap.get(suCaiLightType.getSelectedValue().toString());
						JToggleButton[] btns = (JToggleButton[])MainUi.map.get("suCaiTypeBtns");
						String[] name = {"动感","慢摇","抒情","柔和","浪漫","温馨","炫丽","梦幻","其他"};
						int cnt = 0;
						if (map2!=null) {
							for (int i = 0; i < btns.length; i++) {
								List abc = (List)map2.get(""+i);
								if (abc != null) {
									cnt = cnt + abc.size();
								}
							}
						}
						String suCaiNameAndNumber = field.getText()+"--->"+(cnt+1);
						//////////////////
						
						if (model == null) {
							model = new DefaultListModel();
							model.addElement(suCaiNameAndNumber);
							list.setModel(model);
						} else {
							model.addElement(suCaiNameAndNumber);
						}
						list.setSelectedIndex(model.getSize()-1);
						//JList suCaiLightType = (JList)MainUi.map.get("suCaiLightType");
						Map map = (Map)Data.suCaiMap.get(suCaiLightType.getSelectedValue().toString());
						Map nameMap = (Map)Data.suCaiNameMap.get(suCaiLightType.getSelectedValue().toString());
						if (map==null) {
							map = new HashMap<>();
							Data.suCaiMap.put(suCaiLightType.getSelectedValue().toString(), map);
						}
						if (nameMap==null) {
							nameMap = new HashMap<>();
							Data.suCaiNameMap.put(suCaiLightType.getSelectedValue().toString(), nameMap);
						}
						//JToggleButton[] btns = (JToggleButton[])MainUi.map.get("suCaiTypeBtns");
						//String[] name = {"默认","动感","抒情","柔和","浪漫"};
						for (int i = 0; i < btns.length; i++) {
							if (btns[i].isSelected()) {
								List tmp = (List)map.get(""+i);
								List nameList = (List)nameMap.get(""+i);
								if (nameList!=null) {
									nameList.add(suCaiNameAndNumber);
								} else { 
									nameList = new ArrayList<>();
									nameList.add(suCaiNameAndNumber);
									nameMap.put(""+i,nameList);
								}
								if (tmp==null) {
									tmp = new ArrayList<>();
								}
								tmp.add(new HashMap<>());
								btns[i].setText(name[i]+"("+tmp.size()+")");
								map.put(""+i, tmp);
							}
						}
						dialog.dispose();
					}
				}
			}
		};
		btn1.addActionListener(listener);btn2.addActionListener(listener);
		p2.add(btn1);p2.add(new JLabel("     "));p2.add(btn2);
		
		JPanel n1 = new JPanel();
		n1.setPreferredSize(new Dimension(350,20));
		dialog.add(n1);
		dialog.add(p1);dialog.add(p2);
	}
}
