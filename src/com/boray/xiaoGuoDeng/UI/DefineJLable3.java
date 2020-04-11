package com.boray.xiaoGuoDeng.UI;

import com.boray.Data.MyColor;
import com.boray.Data.XiaoGuoDengModel;
import com.boray.mainUi.MainUi;
import com.boray.xiaoGuoDeng.Listener.PopMenuListener;
import com.boray.xiaoGuoDeng.Listener.SuoYouDengZuLabelMouseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class DefineJLable3 extends JLabel {
    private int xx, yy;
    private Point p;
    private int w = 200, h = 29;
    //private int w_1;
    static boolean b = false;
    private JPanel pane;
    private int blockNum = 1;
    static boolean syn = true;
    private boolean showPopMenu = false;
    private JPopupMenu popupMenu;
    private JMenuItem menuItem, menuItem1, menuItem2;

    public DefineJLable3(final String s, JPanel pane) {
        super(s + "¡Á", SwingConstants.CENTER);
        p = new Point(0, 0);
        this.pane = pane;
//        blockNum = Integer.valueOf(s.substring(0, s.indexOf("("))).intValue();
        this.setEnabled(false);
        this.setBackground(new Color(101, 198, 233));
        popupMenu = new JPopupMenu();
        menuItem = new JCheckBoxMenuItem("¡¡ÆôÓÃ");
        menuItem1 = new JCheckBoxMenuItem("¡¡½ûÓÃ", true);
        ButtonGroup group = new ButtonGroup();
        group.add(menuItem);
        group.add(menuItem1);
//        ActionListener listener = new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                JMenuItem item = (JMenuItem) e.getSource();
//                if ("¡¡½ûÓÃ".equals(e.getActionCommand())) {
//                    setText(s + "¡Á");
//                } else {
//                    setText(s + "¡Ì");
//                }
//                JPanel xiaoGuoParentPane = (JPanel) MainUi.map.get("xiaoGuoParentPane" + XiaoGuoDengModel.model);
//                xiaoGuoParentPane.repaint();
//            }
//        };
        SuoYouDengZuLabelMouseListener listener = new SuoYouDengZuLabelMouseListener();
        menuItem.addActionListener(listener);
        menuItem1.addActionListener(listener);
        menuItem2 = new JMenuItem("¡¡±à¼­");
//        PopMenuListener listener2 = new PopMenuListener(blockNum, pane, s);
        menuItem2.addActionListener(listener);
        popupMenu.add(menuItem);
        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);
        init();
    }

    private void init() {
        setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.black));
        this.setOpaque(true);
        setSize(w, h);
        this.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent mouseEvent) {
                b = false;
                showPopMenu = false;
                try {
                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            JPanel ruleLabel = (JPanel) MainUi.map.get("ruleLabe" + XiaoGuoDengModel.model);
                            ruleLabel.repaint();
                            JPanel xiaoGuoParentPane = (JPanel) MainUi.map.get("xiaoGuoParentPane" + XiaoGuoDengModel.model);
                            xiaoGuoParentPane.repaint();
                        }
                    }, 10);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void mousePressed(MouseEvent mouseEvent) {
                JLabel label = (JLabel) mouseEvent.getSource();
                showPopMenu = false;
                if (mouseEvent.getButton() == 3) {
                    if (label.getText().contains("¡Á")) {
                        menuItem1.setSelected(true);
                    } else {
                        menuItem.setSelected(true);
                    }
                    showPopMenu = true;
//                    if (isEnabled())
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
                JPanel ruleLabel = (JPanel) MainUi.map.get("ruleLabe" + XiaoGuoDengModel.model);
                Graphics graphics = ruleLabel.getGraphics();
                graphics.setColor(Color.red);
                graphics.drawLine(x1, 0, x1, 22);//graphics.drawLine(x1+1, 0, x1+1, 22);
                graphics.drawLine(x2, 0, x2, 22);//graphics.drawLine(x2+1, 0, x2+1, 22);

                JPanel xiaoGuoParentPane = (JPanel) MainUi.map.get("xiaoGuoParentPane" + XiaoGuoDengModel.model);
                Graphics graphics2 = xiaoGuoParentPane.getGraphics();
                graphics2.setColor(Color.gray);
                graphics2.drawLine(x1, 5, x1, 1096);
                graphics2.drawLine(x1 - 1, 5, x1 - 1, 1096);
                graphics2.drawLine(x2 - 1, 5, x2 - 1, 1096);
                graphics2.drawLine(x2, 5, x2, 1096);
            }

            public void mouseExited(MouseEvent arg0) {
            }

            public void mouseEntered(MouseEvent e) {
                if (e.getButton() == 3) {
                    return;
                }
                if (!b) {
                    JLabel label = (JLabel) e.getSource();
                    if (label.isEnabled()) {
                        if (label.getSize().width - 8 <= e.getX()) {
                            label.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));//ÓÒ
                        } else if (8 >= e.getX()) {
                            label.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));//×ó
                        } else {
                            label.setCursor(Cursor.getDefaultCursor());
                        }
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
                                            JLabel label = (JLabel) e.getSource();
                                            Cursor cursor = label.getCursor();

                                            if (label.isEnabled()) {
                                                if (label.getSize().width - 4 <= e.getX()) {
                                                    label.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));//ÓÒ
                                                } else if (4 >= e.getX()) {
                                                    label.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));//×ó
                                                } else {
                                                    label.setCursor(Cursor.getDefaultCursor());
                                                }
                                                if (b) {
                                                    label.setCursor(cursor);
                                                }
                                            }
                                        }

                                        ;

                                        public void mouseDragged(MouseEvent e) {
                                            if (showPopMenu) {
                                                return;
                                            }
                                            JLabel label = (JLabel) e.getSource();
                                            if (isEnabled()) {
                                                try {
                                                    JPanel ruleLabel = (JPanel) MainUi.map.get("ruleLabe" + XiaoGuoDengModel.model);
                                                    JPanel xiaoGuoParentPane = (JPanel) MainUi.map.get("xiaoGuoParentPane" + XiaoGuoDengModel.model);

                                                    ruleLabel.updateUI();
                                                    xiaoGuoParentPane.updateUI();
                                                } catch (Exception e2) {
                                                    e2.printStackTrace();
                                                }
                                                p = label.getLocation();
                                                if (label.getCursor().getType() == Cursor.W_RESIZE_CURSOR) {
                                                    int preX = 0;
                                                    if (blockNum >= 2) {
                                                        DefineJLable3 preDefineJLable = (DefineJLable3) pane.getComponent(blockNum - 2);
                                                        preX = preDefineJLable.getLocation().x + 5;
                                                    }

                                                    int old_width = label.getSize().width;
                                                    Point point = label.getLocation();
                                                    if (old_width == 5 && e.getPoint().x >= 0) {

                                                    } else {
                                                        p.x = e.getPoint().x + point.x;
                                                        if (p.x <= preX) {
                                                            p.x = preX;
                                                        }
                                                        int tx = p.x;
                                                        if (tx % 5 != 0) {
                                                            tx = tx + (5 - tx % 5);
                                                        }
                                                        p.x = tx;
                                                        p.y = point.y;

                                                        w = old_width - (p.x - point.x);
                                                        if (w <= 5) {
                                                            w = 5;
                                                        }
                                                        setLocation(p);
                                                        h = label.getSize().height;
                                                        label.setSize(w, h);
                                                    }
                                                } else if (label.getCursor().getType() == Cursor.E_RESIZE_CURSOR) {
                                                    Point point = label.getLocation();
                                                    int nextX = 10000;
                                                    if (pane.getComponentCount() > blockNum) {
                                                        DefineJLable3 nextDefineJLable = (DefineJLable3) pane.getComponent(blockNum);
                                                        nextX = nextDefineJLable.getLocation().x + nextDefineJLable.getWidth() - 5;
                                                    }
                                                    int preX = 0;
                                                    if (blockNum >= 2) {
                                                        DefineJLable3 preDefineJLable = (DefineJLable3) pane.getComponent(blockNum - 2);
                                                        preX = preDefineJLable.getLocation().x + preDefineJLable.getWidth() + 5;
                                                    }
                                                    if (e.getPoint().x + point.x <= nextX && e.getPoint().x + point.x >= preX) {
                                                        w = e.getPoint().x;
                                                        if (e.getPoint().x <= 5) {
                                                            w = 5;
                                                        }
                                                        if (w % 5 != 0) {
                                                            w = w + (5 - w % 5);
                                                        }
                                                        h = label.getSize().height;
                                                        label.setSize(w, h);
                                                        label.setLocation(p);
                                                    }
                                                } else {
                                                    int preX = 0;
                                                    if (blockNum >= 2) {
                                                        DefineJLable3 preDefineJLable = (DefineJLable3) pane.getComponent(blockNum - 2);
                                                        preX = preDefineJLable.getLocation().x + 5;
                                                        if (preDefineJLable.getWidth() >= label.getWidth()) {
                                                            preX = preDefineJLable.getLocation().x + (preDefineJLable.getWidth() - (label.getWidth() - 5));
                                                        }
                                                    }

                                                    Point point = label.getLocation();
                                                    int firstX = point.x;
                                                    int lastX = e.getPoint().x + point.x - xx;

                                                    p.x = lastX;
                                                    if (p.x <= preX) {
                                                        p.x = preX;
                                                    }
                                                    int tx = p.x;
                                                    if (tx % 5 != 0) {
                                                        tx = tx + (5 - tx % 5);
                                                    }
                                                    lastX = tx;
                                                    p.x = tx;
                                                    p.y = point.y;
                                                    label.setLocation(p);

                                                    if (pane.getComponentCount() > blockNum) {
                                                        //DefineJLable lastDefineJLable;
                                                        for (int i = blockNum; i < pane.getComponentCount(); i++) {
                                                            JLabel lastDefineJLable = (JLabel) pane.getComponent(i);
                                                            Point point2 = lastDefineJLable.getLocation();
                                                            int startX = point2.x;
                                                            point2.x = startX + lastX - firstX;
                                                            lastDefineJLable.setLocation(point2);
                                                        }
                                                    }

                                                }
                                                final int x1 = label.getLocation().x;
                                                final int x2 = x1 + w;


                                                new Timer().schedule(new TimerTask() {
                                                    public void run() {
                                                        JPanel ruleLabel = (JPanel) MainUi.map.get("ruleLabe" + XiaoGuoDengModel.model);
                                                        JPanel xiaoGuoParentPane = (JPanel) MainUi.map.get("xiaoGuoParentPane" + XiaoGuoDengModel.model);
                                                        try {
                                                            //Thread.sleep(2);
                                                        } catch (Exception e2) {
                                                            e2.printStackTrace();
                                                        }

                                                        Graphics graphics = ruleLabel.getGraphics();
                                                        graphics.setColor(Color.red);
                                                        graphics.drawLine(x1, 0, x1, 22);
                                                        graphics.drawLine(x2, 0, x2, 22);

                                                        Graphics graphics2 = xiaoGuoParentPane.getGraphics();
                                                        graphics2.setColor(Color.gray);
                                                        graphics2.drawLine(x1, 5, x1, 1096);
                                                        graphics2.drawLine(x1 - 1, 5, x1 - 1, 1096);
                                                        graphics2.drawLine(x2 - 1, 5, x2 - 1, 1096);
                                                        graphics2.drawLine(x2, 5, x2, 1096);
                                                    }
                                                }, 5);
                                                //label.repaint();
                                            }
                                        }
                                    }
        );
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AlphaComposite cmp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
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
