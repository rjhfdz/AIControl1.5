package com.boray.quanJu.Listener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.IconJDialog;
import com.boray.Utils.Socket;
import com.boray.mainUi.MainUi;

public class TiaoGuangDialog implements ActionListener{
	private List list;
	private TiaoGuangSendCode tiaoGuangSendCode;
	public void actionPerformed(ActionEvent e) {
		show();
	}
	private void show(){
		tiaoGuangSendCode = new TiaoGuangSendCode();
		JFrame f = (JFrame)MainUi.map.get("frame");
		IconJDialog dialog = new IconJDialog(f,true);
		dialog.setResizable(false);
		dialog.setTitle("调光设置");
		dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
		//dialog.setLayout(new BorderLayout());
		dialog.setLocation(f.getLocation().x+f.getSize().width/2-300,f.getLocation().y+f.getSize().height/2-165);
		dialog.setSize(600, 330);
		init(dialog);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new ShowAndSaveTiaoGuang().save();
				list.clear();
				MainUi.map.put("setTiaoGuangCpns", null);
			}
		});
		dialog.setVisible(true);
	}
	private void init(JDialog dialog) {
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		JPanel p4 = new JPanel();
		TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "灯1", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p1.setBorder(tb1);
		TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "灯2", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p2.setBorder(tb2);
		TitledBorder tb3 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "灯3", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p3.setBorder(tb3);
		TitledBorder tb4 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "灯4", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p4.setBorder(tb4);
		p1.setPreferredSize(new Dimension(140,280));
		p2.setPreferredSize(new Dimension(140,280));
		p3.setPreferredSize(new Dimension(140,280));
		p4.setPreferredSize(new Dimension(140,280));
		list = new ArrayList();
		MainUi.map.put("setTiaoGuangCpns", list);
		setP1(p1);
		setP2(p2);
		setP3(p3);
		setP4(p4);
		new ShowAndSaveTiaoGuang().show();
		new Thread(new Runnable() {
			public void run() {
//				if (Data.serialPort != null) {
//					try {
//						OutputStream os = Data.serialPort.getOutputStream();
//						os.write(ZhiLingJi.changJingChaXun(25));
//						os.flush();
//						os.close();
//					} catch (Exception e2) {
//						e2.printStackTrace();
//					}
//				}
				Socket.SendData(ZhiLingJi.changJingChaXun(25));
			}
		}).start();
		dialog.add(p1);dialog.add(p2);
		dialog.add(p3);dialog.add(p4);
	}
	private void setP4(JPanel pane){
		JComboBox box = new JComboBox(new String[]{"可控","不可控"});
		box.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					tiaoGuangSendCode.sendCode(25);
				}
			}
		});
		box.setPreferredSize(new Dimension(120,30));
		pane.add(box);pane.add(new JLabel("灯光上限"));
		pane.add(new JLabel("灯光下限"));
		final JSlider slider = new JSlider(0,100);
		final JSlider slider2 = new JSlider(0,100);
		slider.setValue(0);slider2.setValue(0);
		slider.setOrientation(SwingConstants.VERTICAL);
		slider2.setOrientation(SwingConstants.VERTICAL);
		slider.setPreferredSize(new Dimension(60,140));
		slider2.setPreferredSize(new Dimension(60,140));
		pane.add(slider);pane.add(slider2);
		final JComboBox box2 = new JComboBox();
		final JComboBox box3 = new JComboBox();
		box2.setPreferredSize(new Dimension(60,30));
		box3.setPreferredSize(new Dimension(60,30));
		for (int i = 0; i <= 100; i++) {
			if (i%2 == 0) {
				box2.addItem(String.valueOf(i));
				box3.addItem(String.valueOf(i));
			}
		}
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ItemListener listener = box2.getItemListeners()[0];
				box2.removeItemListener(listener);
				int tp = slider.getValue();
				if (tp%2 != 0) {
					slider.setValue(tp+1);
				}
				box2.setSelectedItem(String.valueOf(slider.getValue()));
				box2.addItemListener(listener);
			}
		});
		slider.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				tiaoGuangSendCode.sendCode(26);
			}
		});
		slider2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ItemListener listener = box3.getItemListeners()[0];
				box3.removeItemListener(listener);
				int tp = slider2.getValue();
				if (tp%2 != 0) {
					slider2.setValue(tp+1);
				}
				box3.setSelectedItem(String.valueOf(slider2.getValue()));
				box3.addItemListener(listener);
			}
		});
		slider2.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				tiaoGuangSendCode.sendCode(25);
			}
		});
		box2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					slider.setValue(Integer.valueOf(box2.getSelectedItem().toString()));
					tiaoGuangSendCode.sendCode(26);
				}
			}
		});
		box3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					slider2.setValue(Integer.valueOf(box3.getSelectedItem().toString()));
					tiaoGuangSendCode.sendCode(25);
				}
			}
		});
		list.add(box);
		list.add(slider);list.add(slider2);
		//list.add(box2);list.add(box3);
		pane.add(box2);pane.add(box3);
	}
	private void setP3(JPanel pane){
		JComboBox box = new JComboBox(new String[]{"可控","不可控"});
		box.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					tiaoGuangSendCode.sendCode(25);
				}
			}
		});
		box.setPreferredSize(new Dimension(120,30));
		pane.add(box);pane.add(new JLabel("灯光上限"));
		pane.add(new JLabel("灯光下限"));
		final JSlider slider = new JSlider(0,100);
		final JSlider slider2 = new JSlider(0,100);
		slider.setValue(0);slider2.setValue(0);
		slider.setOrientation(SwingConstants.VERTICAL);
		slider2.setOrientation(SwingConstants.VERTICAL);
		slider.setPreferredSize(new Dimension(60,140));
		slider2.setPreferredSize(new Dimension(60,140));
		pane.add(slider);pane.add(slider2);
		final JComboBox box2 = new JComboBox();
		final JComboBox box3 = new JComboBox();
		box2.setPreferredSize(new Dimension(60,30));
		box3.setPreferredSize(new Dimension(60,30));
		for (int i = 0; i <= 100; i++) {
			if (i%2 == 0) {
				box2.addItem(String.valueOf(i));
				box3.addItem(String.valueOf(i));
			}
		}
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ItemListener listener = box2.getItemListeners()[0];
				box2.removeItemListener(listener);
				int tp = slider.getValue();
				if (tp%2 != 0) {
					slider.setValue(tp+1);
				}
				box2.setSelectedItem(String.valueOf(slider.getValue()));
				box2.addItemListener(listener);
			}
		});
		slider.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				tiaoGuangSendCode.sendCode(26);
			}
		});
		slider2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ItemListener listener = box3.getItemListeners()[0];
				box3.removeItemListener(listener);
				int tp = slider2.getValue();
				if (tp%2 != 0) {
					slider2.setValue(tp+1);
				}
				box3.setSelectedItem(String.valueOf(slider2.getValue()));
				box3.addItemListener(listener);
			}
		});
		slider2.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				tiaoGuangSendCode.sendCode(25);
			}
		});
		box2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					slider.setValue(Integer.valueOf(box2.getSelectedItem().toString()));
					tiaoGuangSendCode.sendCode(26);
				}
			}
		});
		box3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					slider2.setValue(Integer.valueOf(box3.getSelectedItem().toString()));
					tiaoGuangSendCode.sendCode(25);
				}
			}
		});
		
		list.add(box);
		list.add(slider);list.add(slider2);
		//list.add(box2);list.add(box3);
		pane.add(box2);pane.add(box3);
	}
	private void setP2(JPanel pane){
		JComboBox box = new JComboBox(new String[]{"可控","不可控"});
		box.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					tiaoGuangSendCode.sendCode(25);
				}
			}
		});
		box.setPreferredSize(new Dimension(120,30));
		pane.add(box);pane.add(new JLabel("灯光上限"));
		pane.add(new JLabel("灯光下限"));
		final JSlider slider = new JSlider(0,100);
		final JSlider slider2 = new JSlider(0,100);
		slider.setValue(0);slider2.setValue(0);
		slider.setOrientation(SwingConstants.VERTICAL);
		slider2.setOrientation(SwingConstants.VERTICAL);
		slider.setPreferredSize(new Dimension(60,140));
		slider2.setPreferredSize(new Dimension(60,140));
		pane.add(slider);pane.add(slider2);
		final JComboBox box2 = new JComboBox();
		final JComboBox box3 = new JComboBox();
		box2.setPreferredSize(new Dimension(60,30));
		box3.setPreferredSize(new Dimension(60,30));
		for (int i = 0; i <= 100; i++) {
			if (i%2 == 0) {
				box2.addItem(String.valueOf(i));
				box3.addItem(String.valueOf(i));
			}
		}
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ItemListener listener = box2.getItemListeners()[0];
				box2.removeItemListener(listener);
				int tp = slider.getValue();
				if (tp%2 != 0) {
					slider.setValue(tp+1);
				}
				box2.setSelectedItem(String.valueOf(slider.getValue()));
				box2.addItemListener(listener);
			}
		});
		slider.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				tiaoGuangSendCode.sendCode(26);
			}
		});
		slider2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ItemListener listener = box3.getItemListeners()[0];
				box3.removeItemListener(listener);
				int tp = slider2.getValue();
				if (tp%2 != 0) {
					slider2.setValue(tp+1);
				}
				box3.setSelectedItem(String.valueOf(slider2.getValue()));
				box3.addItemListener(listener);
			}
		});
		slider2.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				tiaoGuangSendCode.sendCode(25);
			}
		});
		box2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					slider.setValue(Integer.valueOf(box2.getSelectedItem().toString()));
					tiaoGuangSendCode.sendCode(26);
				}
			}
		});
		box3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					slider2.setValue(Integer.valueOf(box3.getSelectedItem().toString()));
					tiaoGuangSendCode.sendCode(25);
				}
			}
		});
		
		list.add(box);
		list.add(slider);list.add(slider2);
		//list.add(box2);list.add(box3);
		pane.add(box2);pane.add(box3);
	}
	private void setP1(JPanel pane){
		JComboBox box = new JComboBox(new String[]{"可控","不可控"});
		box.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					tiaoGuangSendCode.sendCode(25);
				}
			}
		});
		box.setPreferredSize(new Dimension(120,30));
		pane.add(box);pane.add(new JLabel("灯光上限"));
		pane.add(new JLabel("灯光下限"));
		final JSlider slider = new JSlider(0,100);
		final JSlider slider2 = new JSlider(0,100);
		slider.setValue(0);slider2.setValue(0);
		slider.setOrientation(SwingConstants.VERTICAL);
		slider2.setOrientation(SwingConstants.VERTICAL);
		slider.setPreferredSize(new Dimension(60,140));
		slider2.setPreferredSize(new Dimension(60,140));
		pane.add(slider);pane.add(slider2);
		final JComboBox box2 = new JComboBox();
		final JComboBox box3 = new JComboBox();
		box2.setPreferredSize(new Dimension(60,30));
		box3.setPreferredSize(new Dimension(60,30));
		for (int i = 0; i <= 100; i++) {
			if (i%2 == 0) {
				box2.addItem(String.valueOf(i));
				box3.addItem(String.valueOf(i));
			}
		}
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ItemListener listener = box2.getItemListeners()[0];
				box2.removeItemListener(listener);
				int tp = slider.getValue();
				if (tp%2 != 0) {
					slider.setValue(tp+1);
				}
				box2.setSelectedItem(String.valueOf(slider.getValue()));
				box2.addItemListener(listener);
			}
		});
		slider.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				tiaoGuangSendCode.sendCode(26);
			}
		});
		slider2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				ItemListener listener = box3.getItemListeners()[0];
				box3.removeItemListener(listener);
				int tp = slider2.getValue();
				if (tp%2 != 0) {
					slider2.setValue(tp+1);
				}
				box3.setSelectedItem(String.valueOf(slider2.getValue()));
				box3.addItemListener(listener);
			}
		});
		slider2.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				tiaoGuangSendCode.sendCode(25);
			}
		});
		box2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					slider.setValue(Integer.valueOf(box2.getSelectedItem().toString()));
					tiaoGuangSendCode.sendCode(26);
				}
			}
		});
		box3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (ItemEvent.SELECTED == e.getStateChange()) {
					slider2.setValue(Integer.valueOf(box3.getSelectedItem().toString()));
					tiaoGuangSendCode.sendCode(25);
				}
			}
		});
		list.add(box);
		list.add(slider);list.add(slider2);
		//list.add(box2);list.add(box3);
		pane.add(box2);pane.add(box3);
		pane.updateUI();
	}
}
