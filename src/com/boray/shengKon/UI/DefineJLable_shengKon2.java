package com.boray.shengKon.UI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

import com.boray.Data.MyData;
import com.boray.mainUi.MainUi;
import com.boray.shengKon.Listener.EditActionListener;

public class DefineJLable_shengKon2 extends JLabel {
    private int xx, yy;
    private Point p;
    private int w = 150, h = 29;
    //private int w_1;
    static boolean b = false;
    private JPanel pane;
    private int blockNum;
    static boolean syn = true;
    private boolean showPopMenu = false;
    private JPopupMenu popupMenu;
    private JMenuItem menuItem, menuItem1, menuItem2, menuItem3;
    private Color oldColor;

    public DefineJLable_shengKon2(final String s, final JPanel pane) {
        super(s, SwingConstants.CENTER);
        p = new Point(0, 0);
        this.pane = pane;
        if (s.contains("("))
            blockNum = Integer.valueOf(s.substring(0, s.indexOf("("))).intValue();
        else
            blockNum = Integer.valueOf(s).intValue();

        popupMenu = new JPopupMenu();
        menuItem = new JMenuItem("启用编辑效果");
        menuItem1 = new JMenuItem("启用场景效果");
        menuItem3 = new JMenuItem("选择素材");
        menuItem2 = new JMenuItem("编辑");
        ButtonGroup group = new ButtonGroup();
        group.add(menuItem);
        group.add(menuItem1);
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //JMenuItem item = (JMenuItem)e.getSource();

                if ("启用编辑效果".equals(e.getActionCommand())) {
                    String str = getText();//获得时间块内的内容
                    if (!(str.contains("(") && str.contains(")"))) {
                        JFrame frame = (JFrame) MainUi.map.get("frame");
                        JOptionPane.showMessageDialog(frame, "未选择对应的素材，不能进行编辑！", "提示", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    menuItem2.setEnabled(true);
                    setBackground(new Color(0, 255, 0));
                } else {
                    String str = getText();//获得时间块内的内容
                    if (!(str.contains("(") && str.contains(")"))) {
                        JFrame frame = (JFrame) MainUi.map.get("frame");
                        JOptionPane.showMessageDialog(frame, "未选择对应的素材，不能启用场景！", "提示", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    menuItem2.setEnabled(false);
                    setBackground(Color.red);
                }
                JPanel xiaoGuoParentPane = (JPanel) MainUi.map.get("shengKonParentPane" + MyData.ShengKonModel);
                xiaoGuoParentPane.updateUI();
            }
        };
        menuItem.addActionListener(listener);
        menuItem1.addActionListener(listener);

        menuItem2.addActionListener(new EditActionListener(blockNum, pane, getText()));
        ;
        menuItem2.setEnabled(false);
        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SelectShengKonSuCaiUI().show(pane, getText());
            }
        });
        popupMenu.add(menuItem2);
        popupMenu.add(menuItem);
        popupMenu.add(menuItem1);
        popupMenu.add(menuItem3);
        init();
    }

    private void init() {
        //this.setBorder(new LineBorder(Color.black));
        setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.black));
        this.setOpaque(true);
        //this.setBackground(Color.pink);
        //this.setPreferredSize(new Dimension(50,29));
        setSize(w, h);
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.white));
                JPanel xiaoGuoParentPane = (JPanel) MainUi.map.get("shengKonParentPane" + MyData.ShengKonModel);
                xiaoGuoParentPane.updateUI();
            }

            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.black));
                JPanel xiaoGuoParentPane = (JPanel) MainUi.map.get("shengKonParentPane" + MyData.ShengKonModel);
                xiaoGuoParentPane.updateUI();
            }

            public void mousePressed(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                showPopMenu = false;
                if (e.getButton() == 3) {
                    if (!label.getBackground().equals(Color.red)) {
                        menuItem2.setEnabled(true);
                        menuItem.setSelected(false);
                    } else {
                        menuItem2.setEnabled(false);
                        menuItem1.setSelected(false);
                    }
                    popupMenu.show(label, e.getX(), e.getY());
                    return;
                }
            }
        });
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
