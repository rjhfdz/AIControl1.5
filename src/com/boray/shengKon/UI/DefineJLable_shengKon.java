package com.boray.shengKon.UI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;

import com.boray.Data.MyData;
import com.boray.mainUi.MainUi;
import com.boray.shengKon.Listener.TimeAssignPopMenuListener;

public class DefineJLable_shengKon extends JLabel{
	private int xx , yy;
	private Point p;
	private int w = 50,h = 29;
	//private int w_1;
	static boolean b = false;
	private JPanel pane;
	private int blockNum;
	static boolean syn = true;
	private boolean showPopMenu = false;
	
	public DefineJLable_shengKon(final String s,JPanel pane){
		super(s,SwingConstants.CENTER);
		p = new Point(0,0);
		this.pane = pane;
		blockNum = Integer.valueOf(s).intValue();
		
		init();
	}
	private void init(){
		//this.setBorder(new LineBorder(Color.black));
		setBorder(BorderFactory.createMatteBorder(1,1,0,1, Color.black));
		this.setOpaque(true);
		//this.setBackground(Color.pink);
		//this.setPreferredSize(new Dimension(50,29));
		setSize(w, h);
		this.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent mouseEvent) {
            	//final JLabel label = (JLabel)mouseEvent.getSource();
                b = false;
                showPopMenu = false;
                try {
                	new Timer().schedule(new TimerTask() {
    					public void run() {
    						JPanel ruleLabel = (JPanel)MainUi.map.get("ruleLabe_shengKon"+MyData.ShengKonModel);
    	                	ruleLabel.repaint();
    	                	JPanel xiaoGuoParentPane = (JPanel)MainUi.map.get("shengKonParentPane"+MyData.ShengKonModel);
    	                	xiaoGuoParentPane.repaint();
    					}
    				}, 10);
                } catch (Exception e) {
					e.printStackTrace();
				}
            }
            public void mousePressed(MouseEvent mouseEvent) {
            	JLabel label = (JLabel)mouseEvent.getSource();
            	showPopMenu = false;
            	if (mouseEvent.getButton() == 3) {
            		showPopMenu = true;
            		JPopupMenu popupMenu = new JPopupMenu();
            		JMenuItem menuItem = new JMenuItem("Éù¿ØË³ÐòÉèÖÃ");
            		JMenuItem menuItem2 = new JMenuItem("É¾³ý");
            		JMenuItem menuItem3 = new JMenuItem("¸´ÖÆ");
            		TimeAssignPopMenuListener listener = new TimeAssignPopMenuListener(blockNum,pane);
            		menuItem.addActionListener(listener);
            		menuItem2.addActionListener(listener);
            		menuItem3.addActionListener(listener);
            		if (blockNum != pane.getComponentCount()) {
            			menuItem2.setEnabled(false);
            		} else {
            			menuItem2.setEnabled(true);
					}
            		popupMenu.add(menuItem);popupMenu.add(menuItem2);popupMenu.add(menuItem3);
            		popupMenu.show(label, mouseEvent.getX(), mouseEvent.getY());
        			return;
				}
                xx = mouseEvent.getX();
                yy = mouseEvent.getY();
                w = label.getSize().width;
                h = label.getSize().height;
                b = true;
                int x1 = label.getLocation().x;
                int x2 = x1 + w;
                //System.out.println(w);
                JPanel ruleLabel = (JPanel)MainUi.map.get("ruleLabe_shengKon"+MyData.ShengKonModel);
                Graphics graphics = ruleLabel.getGraphics();
                graphics.setColor(Color.red);
                graphics.drawLine(x1, 0, x1, 22);//graphics.drawLine(x1+1, 0, x1+1, 22);
                graphics.drawLine(x2, 0, x2, 22);//graphics.drawLine(x2+1, 0, x2+1, 22);
                
                /*JPanel xiaoGuoParentPane = (JPanel)MainUi.map.get("shengKonParentPane");
                Graphics graphics2 = xiaoGuoParentPane.getGraphics();
                graphics2.setColor(Color.gray);
                graphics2.drawLine(x1, 5, x1, 1096);graphics2.drawLine(x1-1, 5, x1-1, 1096);
                graphics2.drawLine(x2-1, 5, x2-1, 1096);graphics2.drawLine(x2, 5, x2, 1096);*/
            }
            public void mouseExited(MouseEvent arg0) {
            }
            public void mouseEntered(MouseEvent e) {
            	if (e.getButton() == 3) {return;}
                if (!b) {
                	JLabel label = (JLabel)e.getSource();
                	if (label.getSize().width-8<=e.getX()) {
                        label.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));//ÓÒ
                    } else if (8>=e.getX()) {
                        //label.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));//×ó
                    } else {
                        label.setCursor(Cursor.getDefaultCursor());
                    }
				}
            }
            public void mouseClicked(MouseEvent arg0) {
            }
        });
		this.addMouseMotionListener(new MouseAdapter() {
            
            public void mouseMoved(MouseEvent e) {
            	if (showPopMenu) {
					return;
				}
            	JLabel label = (JLabel)e.getSource();
                Cursor cursor = label.getCursor();
               
                if (label.getSize().width-4<=e.getX()) {
                    label.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));//ÓÒ
                } else if (4>=e.getX()) {
                    //label.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));//×ó
                } else {
                    label.setCursor(Cursor.getDefaultCursor());
                }
                if (b) {
                    label.setCursor(cursor);
                }
            };
            public void mouseDragged(MouseEvent e) {
            	if (showPopMenu) {
					return;
				}
            	JLabel label = (JLabel)e.getSource();
            	if (label.getCursor().getType() != Cursor.E_RESIZE_CURSOR) {
					return;
				}
            	try {
            		JPanel ruleLabel = (JPanel)MainUi.map.get("ruleLabe_shengKon"+MyData.ShengKonModel);
        			JPanel xiaoGuoParentPane = (JPanel)MainUi.map.get("shengKonParentPane"+MyData.ShengKonModel);
        			/*ruleLabel.repaint(1);
                	xiaoGuoParentPane.repaint(1);*/
        			ruleLabel.updateUI();
        			xiaoGuoParentPane.updateUI();
				} catch (Exception e2) {
 					e2.printStackTrace();
 				}
                //p = label.getLocation();
                if (label.getCursor().getType() == Cursor.E_RESIZE_CURSOR) {
                	//Point point = label.getLocation();
                	int startWidth = label.getWidth();
                	int D_value = 0;
                	
                	w = e.getPoint().x;
                    if (e.getPoint().x <= 5) {
                    	w = 5;
					}
                    if (w % 5 != 0) {
						w = w + (5 - w%5);
					}
                    h = label.getSize().height;
                    label.setSize(w,h);
                    //label.setLocation(p);
                    JPanel[] timeBlockPanels = (JPanel[])MainUi.map.get("timeBlockPanels"+MyData.ShengKonModel);
        			for (int i = 1; i < timeBlockPanels.length; i++) {
        				DefineJLable_shengKon2 defineJLable = (DefineJLable_shengKon2)timeBlockPanels[i].getComponent(blockNum-1);
        				defineJLable.setSize(w,h);
        				timeBlockPanels[i].updateUI();
        			}
                    
                    D_value = w - startWidth;
                    if (pane.getComponentCount() > blockNum) {
                    	for (int j = 0; j < timeBlockPanels.length; j++) {
                    		for (int i = blockNum; i < pane.getComponentCount(); i++) {
    							JLabel lastDefineJLable = (JLabel)timeBlockPanels[j].getComponent(i);
    							Point point2 = lastDefineJLable.getLocation();
    							int startX = point2.x;
    							point2.x = startX+D_value;
    							lastDefineJLable.setLocation(point2);
    						}
						}
					}
                }
                final int x1 = label.getLocation().x;
                final int x2 = x1 + w;
                
                
                new Timer().schedule(new TimerTask() {
					public void run() {
						JPanel ruleLabel = (JPanel)MainUi.map.get("ruleLabe_shengKon"+MyData.ShengKonModel);
                    	try {
							//Thread.sleep(2);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
                    	
		                Graphics graphics = ruleLabel.getGraphics();
		                graphics.setColor(Color.red);
		                graphics.drawLine(x1, 0, x1, 22);
		                graphics.drawLine(x2, 0, x2, 22);
		                
					}
				}, 8);
                //label.repaint();
                }
            }
        );
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
