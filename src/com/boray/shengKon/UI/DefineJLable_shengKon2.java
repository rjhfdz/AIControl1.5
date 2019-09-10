package com.boray.shengKon.UI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.boray.Data.MyData;
import com.boray.mainUi.MainUi;
import com.boray.shengKon.Listener.EditActionListener;
import com.boray.xiaoGuoDeng.Listener.PopMenuListener;
import com.sun.istack.internal.FinalArrayList;

public class DefineJLable_shengKon2 extends JLabel{
	private int xx , yy;
	private Point p;
	private int w = 50,h = 29;
	//private int w_1;
	static boolean b = false;
	private JPanel pane;
	private int blockNum;
	static boolean syn = true;
	private boolean showPopMenu = false;
	private JPopupMenu popupMenu;
	private JMenuItem menuItem,menuItem1,menuItem2;
	private Color oldColor;
	
	public DefineJLable_shengKon2(final String s,JPanel pane){
		super(s,SwingConstants.CENTER);
		p = new Point(0,0);
		this.pane = pane;
		blockNum = Integer.valueOf(s).intValue();
		
		popupMenu = new JPopupMenu();
		menuItem = new JCheckBoxMenuItem("启用编辑效果");
		menuItem1 = new JCheckBoxMenuItem("启用场景效果",true);
		ButtonGroup group = new ButtonGroup();
		group.add(menuItem);group.add(menuItem1);
		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//JMenuItem item = (JMenuItem)e.getSource();
				
				if ("启用编辑效果".equals(e.getActionCommand())) {
					menuItem2.setEnabled(true);
					setBackground(new Color(0, 255, 0));
				} else {
					menuItem2.setEnabled(false);
					setBackground(Color.red);
				}
				JPanel xiaoGuoParentPane = (JPanel)MainUi.map.get("shengKonParentPane"+MyData.ShengKonModel);
				xiaoGuoParentPane.updateUI();
			}
		};
		menuItem.addActionListener(listener);menuItem1.addActionListener(listener);
		menuItem2 = new JMenuItem("编辑");
		menuItem2.addActionListener(new EditActionListener(blockNum,pane.getName()));
		menuItem2.setEnabled(false);
		popupMenu.add(menuItem2);
		popupMenu.add(menuItem);
		popupMenu.add(menuItem1);
		
		init();
	}
	private void init(){
		//this.setBorder(new LineBorder(Color.black));
		setBorder(BorderFactory.createMatteBorder(1,1,0,1, Color.black));
		this.setOpaque(true);
		//this.setBackground(Color.pink);
		//this.setPreferredSize(new Dimension(50,29));
		setSize(w, h);
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				setBorder(BorderFactory.createMatteBorder(1,1,0,1,Color.white));
				JPanel xiaoGuoParentPane = (JPanel)MainUi.map.get("shengKonParentPane"+MyData.ShengKonModel);
				xiaoGuoParentPane.updateUI();
			}
			public void mouseExited(MouseEvent e) {
				setBorder(BorderFactory.createMatteBorder(1,1,0,1, Color.black));
				JPanel xiaoGuoParentPane = (JPanel)MainUi.map.get("shengKonParentPane"+MyData.ShengKonModel);
				xiaoGuoParentPane.updateUI();
			}
			public void mousePressed(MouseEvent e) {
				JLabel label = (JLabel)e.getSource();
            	showPopMenu = false;
            	if (e.getButton() == 3) {
            		if (!label.getBackground().equals(Color.red)) {
            			menuItem2.setEnabled(true);
            			menuItem.setSelected(true);
					} else {
						menuItem2.setEnabled(false);
						menuItem1.setSelected(true);
					}
            		popupMenu.show(label, e.getX(), e.getY());
        			return;
				}
			}
		});
	}
	protected void paintComponent(Graphics g) {
		 Graphics2D g2d = (Graphics2D)g;
		 AlphaComposite cmp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f);
	     //g2d.setComposite(cmp.derive(1.0f));
		 g2d.setComposite(cmp);
		super.paintComponent(g2d);
	}
	public void repaint() {
		/*if (p == null) {
			setLocation(new Point(0,0));
		} else {
			setLocation(p);
		}*/
		//setSize(w,h);
        //super.repaint();
    }
}
