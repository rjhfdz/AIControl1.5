package com.boray.xiaoGuoDeng.UI;

import com.boray.Data.Data;
import com.boray.Data.XiaoGuoDengModel;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.Socket;
import com.boray.mainUi.MainUi;
import com.boray.xiaoGuoDeng.Listener.PopMenuListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

public class DefineJLable2 extends JLabel {
    private int xx, yy;
    private Point p;
    private int w = 50, h = 29;
    //private int w_1;
    static boolean b = false;
    private JPanel pane;
    private int blockNum;
    static boolean syn = true;
    private boolean showPopMenu = false;
    private JPopupMenu popupMenu;
    private JMenuItem menuItem, menuItem1, menuItem2;

    public DefineJLable2(final String s, final String s2, int blockNum, int SumTime, JPanel pane) {
//        super("(" + blockNum + ")" + "Start:" + s + " End:" + s2, SwingConstants.CENTER);
        super(s + "--" + s2, SwingConstants.CENTER);
        p = new Point(0, 0);
        this.pane = pane;
        this.blockNum = blockNum;
        setName(SumTime + "");

        popupMenu = new JPopupMenu();
        if (getBackground().equals(Color.green)) {
            menuItem = new JMenuItem("启用");
            menuItem1 = new JMenuItem("禁用");
        } else {
            menuItem = new JMenuItem("启用");
            menuItem1 = new JMenuItem("禁用");
        }
        ButtonGroup group = new ButtonGroup();
        group.add(menuItem);
        group.add(menuItem1);
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JMenuItem item = (JMenuItem) e.getSource();
                if ("禁用".equals(e.getActionCommand())) {
                    setBackground(Color.red);
                    openAndClose();
                } else {
                    setBackground(Color.green);
                    openAndClose();
                }
                JPanel xiaoGuoParentPane = (JPanel) MainUi.map.get("xiaoGuoParentPane" + XiaoGuoDengModel.model);
                xiaoGuoParentPane.repaint();
            }
        };
        menuItem.addActionListener(listener);
        menuItem1.addActionListener(listener);
        menuItem2 = new JMenuItem("编辑");
        ActionListener listener1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Data.serialPort != null || Data.socket != null) {
                    JPanel timeBlockPanels = ((JPanel[]) MainUi.map.get("timeBlockPanels_group" + XiaoGuoDengModel.model))[0];
                    final DefineJLable2 lable2 = (DefineJLable2) timeBlockPanels.getComponent(0);
                    String[] str = lable2.getText().split("--");
                    final JFrame f = (JFrame) MainUi.map.get("frame");
                    final JDialog dialog = new JDialog(f, true);
                    dialog.setResizable(false);
                    dialog.setTitle("编辑时间块");
                    int w = 300, h = 150;
                    dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
                    dialog.setSize(w, h);
                    dialog.setLayout(new FlowLayout(FlowLayout.LEFT));
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    JPanel panel = new JPanel();
                    panel.setPreferredSize(new Dimension(250, 50));
                    panel.add(new JLabel("播放区间:"));
                    final JTextField start = new JTextField(2);
                    start.setText(str[0]);
                    panel.add(start);
                    panel.add(new JLabel("秒——"));
                    final JTextField end = new JTextField(2);
                    end.setText(str[1]);
                    panel.add(end);
                    panel.add(new JLabel("秒"));
                    JPanel buttom = new JPanel();
                    buttom.setPreferredSize(new Dimension(250, 35));
                    JButton sureBtn = new JButton("确定");
                    JButton canceBtn = new JButton("取消");
                    ActionListener actionListener = new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if ("确定".equals(e.getActionCommand())) {
                                if (Integer.parseInt(start.getText()) > Integer.parseInt(end.getText())) {
                                    JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "开始时间不能比结束时间大!", "提示", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                if (Integer.parseInt(end.getText()) > Integer.parseInt(lable2.getName())) {
                                    JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "结束时间不能比总时间大!", "提示", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                lable2.setText(start.getText() + "--" + end.getText());
                                lable2.repaint();
                                if (lable2.getBackground().equals(Color.green)) {
                                    UpdateData("80");
                                } else {
                                    UpdateData("00");
                                }
                                dialog.dispose();
                            } else {
                                dialog.dispose();
                            }
                        }
                    };
                    sureBtn.addActionListener(actionListener);
                    canceBtn.addActionListener(actionListener);
                    buttom.add(sureBtn);
                    buttom.add(canceBtn);
                    dialog.add(panel);
                    dialog.add(buttom);
                    dialog.setVisible(true);
                }
            }
        };
        menuItem2.addActionListener(listener1);
        popupMenu.add(menuItem);
        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);
        init();
    }

    private void UpdateData(String s) {
        JPanel timeBlockPanels = ((JPanel[]) MainUi.map.get("timeBlockPanels_group" + XiaoGuoDengModel.model))[0];
        DefineJLable2 lable2 = (DefineJLable2) timeBlockPanels.getComponent(0);
        String[] str = lable2.getText().split("--");
        byte[] bytes = new byte[80];
        bytes[0] = (byte) 0xFA;
        bytes[1] = (byte) 0x50;
        bytes[2] = (byte) 0x61;
        bytes[3] = ZhiLingJi.TYPE;
        bytes[4] = (byte) 0x91;
        bytes[5] = (byte) 0x01;
        bytes[7] = (byte) XiaoGuoDengModel.model;
        bytes[8] = (byte) 0xFF;
        bytes[9] = (byte) 0x01;
        int start = Integer.parseInt(str[0]);
        int end = Integer.parseInt(str[1]);
        bytes[10] = (byte) (start % 256);
        bytes[11] = (byte) (start / 256);
        bytes[12] = (byte) (end % 256);
        bytes[13] = (byte) (end / 256);
        bytes[50] = (byte) (Integer.parseInt(lable2.getName()) % 256);
        bytes[51] = (byte) (Integer.parseInt(lable2.getName()) / 256);
        if (s.equals("80"))
            bytes[52] = (byte) 0x80;
//        bytes[54] = (byte) (start / 256);
//        bytes[55] = (byte) (start % 256);
        bytes[79] = ZhiLingJi.getJiaoYan(bytes);
        if (Data.serialPort != null) {
            Socket.SerialPortSendData(bytes);
        } else if (Data.socket != null) {
            Socket.UDPSendData(bytes);
        }
    }

    public void openAndClose(){
        JPanel timeBlockPanels = ((JPanel[]) MainUi.map.get("timeBlockPanels_group" + XiaoGuoDengModel.model))[0];
        DefineJLable2 lable2 = (DefineJLable2) timeBlockPanels.getComponent(0);
        byte[] bytes = new byte[20];
        bytes[0] = (byte) 0xFA;
        bytes[1] = (byte) 0x14;
        bytes[2] = (byte) 0x61;
        bytes[3] = ZhiLingJi.TYPE;
        bytes[4] = (byte) 0xA6;
        bytes[5] = (byte) 0x01;
        bytes[6] = (byte) XiaoGuoDengModel.model;
        if(lable2.getBackground().equals(Color.green)){
            bytes[7] = (byte) 0x80;
        }
        bytes[19] = ZhiLingJi.getJiaoYan(bytes);
        if (Data.serialPort != null) {
            Socket.SerialPortSendData(bytes);
        } else if (Data.socket != null) {
            Socket.UDPSendData(bytes);
        }
    }

    private void init() {
        setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.black));
        this.setOpaque(true);
//        setSize(w, h);
        this.addMouseListener(new MouseListener() {
            public void mouseReleased(MouseEvent mouseEvent) {
                //final JLabel label = (JLabel)mouseEvent.getSource();
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
                    if (label.getText().contains("×")) {
                        menuItem1.setSelected(true);
                    } else {
                        menuItem.setSelected(true);
                    }
                    showPopMenu = true;
                    popupMenu.show(label, mouseEvent.getX(), mouseEvent.getY());
                    return;
                }
            }

            public void mouseExited(MouseEvent arg0) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseClicked(MouseEvent arg0) {
            }
        });
//        this.addMouseMotionListener(new MouseAdapter() {
//
//                                        public void mouseMoved(MouseEvent e) {
//                                            if (showPopMenu) {
//                                                return;
//                                            }
//                                            JLabel label = (JLabel) e.getSource();
//                                            Cursor cursor = label.getCursor();
//
//                                            if (label.getSize().width - 4 <= e.getX()) {
//                                                label.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));//右
//                                            } else if (4 >= e.getX()) {
//                                                label.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));//左
//                                            } else {
//                                                label.setCursor(Cursor.getDefaultCursor());
//                                            }
//                                            if (b) {
//                                                label.setCursor(cursor);
//                                            }
//                                        }
//
//                                        ;
//
//                                        public void mouseDragged(MouseEvent e) {
//                                            if (showPopMenu) {
//                                                return;
//                                            }
//                                            JLabel label = (JLabel) e.getSource();
//                                            try {
//                                                JPanel ruleLabel = (JPanel) MainUi.map.get("ruleLabe" + XiaoGuoDengModel.model);
//                                                JPanel xiaoGuoParentPane = (JPanel) MainUi.map.get("xiaoGuoParentPane" + XiaoGuoDengModel.model);
//
//                                                ruleLabel.updateUI();
//                                                xiaoGuoParentPane.updateUI();
//                                            } catch (Exception e2) {
//                                                e2.printStackTrace();
//                                            }
//                                            p = label.getLocation();
//                                            if (label.getCursor().getType() == Cursor.W_RESIZE_CURSOR) {
//                                                int preX = 0;
//                                                if (blockNum >= 2) {
//                                                    DefineJLable2 preDefineJLable = (DefineJLable2) pane.getComponent(blockNum - 2);
//                                                    preX = preDefineJLable.getLocation().x + 5;
//                                                }
//
//                                                int old_width = label.getSize().width;
//                                                Point point = label.getLocation();
//                                                if (old_width == 5 && e.getPoint().x >= 0) {
//
//                                                } else {
//                                                    p.x = e.getPoint().x + point.x;
//                                                    if (p.x <= preX) {
//                                                        p.x = preX;
//                                                    }
//                                                    int tx = p.x;
//                                                    if (tx % 5 != 0) {
//                                                        tx = tx + (5 - tx % 5);
//                                                    }
//                                                    p.x = tx;
//                                                    p.y = point.y;
//
//                                                    w = old_width - (p.x - point.x);
//                                                    if (w <= 5) {
//                                                        w = 5;
//                                                    }
//                                                    setLocation(p);
//                                                    h = label.getSize().height;
//                                                    label.setSize(w, h);
//                                                }
//                                            } else if (label.getCursor().getType() == Cursor.E_RESIZE_CURSOR) {
//                                                Point point = label.getLocation();
//                                                int nextX = 10000;
//                                                if (pane.getComponentCount() > blockNum) {
//                                                    DefineJLable2 nextDefineJLable = (DefineJLable2) pane.getComponent(blockNum);
//                                                    nextX = nextDefineJLable.getLocation().x + nextDefineJLable.getWidth() - 5;
//                                                }
//                                                int preX = 0;
//                                                if (blockNum >= 2) {
//                                                    DefineJLable2 preDefineJLable = (DefineJLable2) pane.getComponent(blockNum - 2);
//                                                    preX = preDefineJLable.getLocation().x + preDefineJLable.getWidth() + 5;
//                                                }
//                                                if (e.getPoint().x + point.x <= nextX && e.getPoint().x + point.x >= preX) {
//                                                    w = e.getPoint().x;
//                                                    if (e.getPoint().x <= 5) {
//                                                        w = 5;
//                                                    }
//                                                    if (w % 5 != 0) {
//                                                        w = w + (5 - w % 5);
//                                                    }
//                                                    h = label.getSize().height;
//                                                    label.setSize(w, h);
//                                                    label.setLocation(p);
//                                                }
//                                            } else {
//                                                int preX = 0;
//                                                if (blockNum >= 2) {
//                                                    DefineJLable2 preDefineJLable = (DefineJLable2) pane.getComponent(blockNum - 2);
//                                                    preX = preDefineJLable.getLocation().x + 5;
//                                                    if (preDefineJLable.getWidth() >= label.getWidth()) {
//                                                        preX = preDefineJLable.getLocation().x + (preDefineJLable.getWidth() - (label.getWidth() - 5));
//                                                    }
//                                                }
//
//                                                Point point = label.getLocation();
//                                                int firstX = point.x;
//                                                int lastX = e.getPoint().x + point.x - xx;
//
//                                                p.x = lastX;
//                                                if (p.x <= preX) {
//                                                    p.x = preX;
//                                                }
//                                                int tx = p.x;
//                                                if (tx % 5 != 0) {
//                                                    tx = tx + (5 - tx % 5);
//                                                }
//                                                lastX = tx;
//                                                p.x = tx;
//                                                p.y = point.y;
//                                                label.setLocation(p);
//
//                                                if (pane.getComponentCount() > blockNum) {
//                                                    //DefineJLable lastDefineJLable;
//                                                    for (int i = blockNum; i < pane.getComponentCount(); i++) {
//                                                        JLabel lastDefineJLable = (JLabel) pane.getComponent(i);
//                                                        Point point2 = lastDefineJLable.getLocation();
//                                                        int startX = point2.x;
//                                                        point2.x = startX + lastX - firstX;
//                                                        lastDefineJLable.setLocation(point2);
//                                                    }
//                                                }
//
//                                            }
//                                            final int x1 = label.getLocation().x;
//                                            final int x2 = x1 + w;
//
//
//                                            new Timer().schedule(new TimerTask() {
//                                                public void run() {
//                                                    JPanel ruleLabel = (JPanel) MainUi.map.get("ruleLabe" + XiaoGuoDengModel.model);
//                                                    JPanel xiaoGuoParentPane = (JPanel) MainUi.map.get("xiaoGuoParentPane" + XiaoGuoDengModel.model);
//                                                    try {
//                                                        //Thread.sleep(2);
//                                                    } catch (Exception e2) {
//                                                        e2.printStackTrace();
//                                                    }
//
//                                                    Graphics graphics = ruleLabel.getGraphics();
//                                                    graphics.setColor(Color.red);
//                                                    graphics.drawLine(x1, 0, x1, 22);
//                                                    graphics.drawLine(x2, 0, x2, 22);
//
//                                                    Graphics graphics2 = xiaoGuoParentPane.getGraphics();
//                                                    graphics2.setColor(Color.gray);
//                                                    graphics2.drawLine(x1, 5, x1, 1096);
//                                                    graphics2.drawLine(x1 - 1, 5, x1 - 1, 1096);
//                                                    graphics2.drawLine(x2 - 1, 5, x2 - 1, 1096);
//                                                    graphics2.drawLine(x2, 5, x2, 1096);
//                                                }
//                                            }, 5);
//                                            //label.repaint();
//                                        }
//                                    }
//        );
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
