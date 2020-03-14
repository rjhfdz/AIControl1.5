package com.boray.dengKu.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.OutputStream;
import java.util.*;
import java.util.Timer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.usb.UsbPipe;

import com.boray.Data.Data;
import com.boray.Data.RdmData;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.DragDropRowTableUI;
import com.boray.dengKu.Listener.RDMTableMoveListener;
import com.boray.entity.RDM;
import com.boray.mainUi.MainUi;
import com.boray.usb.UsbUtil;

public class RdmPaneUI implements ActionListener {
    public static List uidList = new ArrayList(15);//Uid
    public static List uid_Byte = new ArrayList(15);//Uid byte格式
    public static boolean openSet = false;
    public static byte[] currentByte = null;
    //public static Map typeMap = new HashMap();//型号
    //public static Map addAndChannelMap = new HashMap<>();//起始地址、通道
    public static int deviceCount = 0;
    private JPopupMenu popupMenu;//菜单
    private JMenuItem moveUpward;//上移
    private JMenuItem moveDown;//下移

    public void show(JPanel pane) {
        pane.setBorder(new LineBorder(Color.gray));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(0);
        pane.setLayout(flowLayout);
        pane.setPreferredSize(new Dimension(902, 588));

        popupMenu = new JPopupMenu();
        moveUpward = new JMenuItem("上移");
        moveDown = new JMenuItem("下移");

        ButtonGroup group = new ButtonGroup();
        group.add(moveUpward);
        group.add(moveDown);
        RDMTableMoveListener listener = new RDMTableMoveListener();
        moveUpward.addActionListener(listener);
        moveDown.addActionListener(listener);

        popupMenu.add(moveUpward);
        popupMenu.add(moveDown);

        JPanel headPane = new JPanel();
        FlowLayout flowLayout2 = new FlowLayout(flowLayout.LEFT);
        flowLayout2.setVgap(0);
        headPane.setLayout(flowLayout2);
        headPane.setPreferredSize(new Dimension(890, 32));
        //headPane.setBorder(new LineBorder(Color.gray));
        //JButton btn1 = new JButton("添加RDM灯具信息");
        JButton btn2 = new JButton("搜索");
        MainUi.map.put("RDMSearch", btn2);
        JButton btn3 = new JButton("退出RDM");
        JButton btn4 = new JButton("DMX排序");
        //btn1.setPreferredSize(new Dimension(158,34));
        btn2.setPreferredSize(new Dimension(88, 34));
        btn3.setPreferredSize(new Dimension(88, 34));
        btn4.setPreferredSize(new Dimension(88, 34));
        //btn1.setFocusable(false);btn2.setFocusable(false);btn3.setFocusable(false);
        btn2.addActionListener(this);
        btn3.addActionListener(this);
        btn4.addActionListener(this);
        //headPane.add(new JLabel("     "));
        //headPane.add(btn1);
        headPane.add(btn2);
        headPane.add(btn3);
        headPane.add(btn4);

        JScrollPane bodyPane = new JScrollPane();
        setBodyPane(bodyPane);

        pane.add(headPane);
        pane.add(bodyPane);
    }

    private void setBodyPane(JScrollPane pane) {
        //pane.setBorder(new LineBorder(Color.gray));
        pane.setPreferredSize(new Dimension(890, 552));

        //Object[][] data = {{"ID","UID","灯具名称","型号","5","18","备注","高级设置"},{"ID","UID","灯具名称","型号","15","28","备注","高级设置"}};
        Object[][] data = {};
        String[] title = {"ID", "UID", "灯具名称", "型号", "DMX起始地址", "占用通道数", "备注", "高级设置"};
        DefaultTableModel model = new DefaultTableModel(data, title);
        final NewJTable table = new NewJTable(model, 3);
        MainUi.map.put("RDM_table", table);
        table.setUI(new DragDropRowTableUI());
        /////////////////////////
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
				/*if (!isSelected) {
					if (row % 2 == 0) {  
		            	cell.setBackground(new Color(218,218,218)); //设置奇数行底色  
		            } else {  
		            	cell.setBackground(Color.white); //设置偶数行底色  
		            }  
				}*/
                if (!isSelected) {
                    if (row % 2 == 0) {
                        cell.setBackground(new Color(237, 243, 254));
                        cell.setForeground(Color.black);
                    } else {
                        cell.setBackground(Color.white); //设置偶数行底色
                        cell.setForeground(Color.black);
                    }
                } else {
                    cell.setBackground(new Color(85, 160, 255));
                    cell.setForeground(Color.white);
                    //cell.setBackground(Color.white);
                    //cell.setForeground(Color.black);
                }
                return cell;
            }
        };
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < title.length; i++) {
            table.getColumn(table.getColumnName(i)).setCellRenderer(cellRenderer);
        }
        table.setSelectionBackground(new Color(56, 117, 215));
        /////////////////////////
        table.getTableHeader().setUI(new BasicTableHeaderUI());
        table.getTableHeader().setReorderingAllowed(false);
        table.setOpaque(false);
        //table.setFocusable(false);
        table.setFont(new Font(Font.SERIF, Font.BOLD, 14));
        hiddenColumn(2, table);
        hiddenColumn(6, table);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        //table.getColumnModel().getColumn(2).setPreferredWidth(0);
        //table.getColumnModel().getColumn(6).setPreferredWidth(0);
        table.getColumnModel().getColumn(3).setPreferredWidth(210);
        table.getColumnModel().getColumn(7).setPreferredWidth(172);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.setRowHeight(28);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		
		/*TableColumn Column3 = table.getColumnModel().getColumn(1);
		JComboBox box = new JComboBox();
		box.setUI((ComboBoxUI)BasicComboBoxUI.createUI(box));
		box.addItem("1");
		box.addItem("2");
		box.addItem("3");
		box.addItem("4");
		//mainUI.map.put("box_lampLib", box);
		DefaultCellEditor defaultCellEditor3 = new DefaultCellEditor(box);
		Column3.setCellEditor(defaultCellEditor3);*/

        table.addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent mouseEvent) {
                NewJTable t = (NewJTable) mouseEvent.getSource();
                int colIndex = t.columnAtPoint(mouseEvent.getPoint());
                if (colIndex == 7) {
                    t.setCursor(new Cursor(Cursor.HAND_CURSOR));
                } else {
                    t.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }

            public void mouseDragged(MouseEvent mouseEvent) {
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                NewJTable t = (NewJTable) e.getSource();
                if (t.getSelectedRows().length > 0) {
                    if (e.getButton() == 3) {
                        popupMenu.show(t, e.getX(), e.getY());
                    }
                }
                super.mouseReleased(e);
            }

            public void mouseClicked(MouseEvent mouseEvent) {
                NewJTable t = (NewJTable) mouseEvent.getSource();
                int colIndex = t.columnAtPoint(mouseEvent.getPoint());
                final int rowIndex = Integer.valueOf(t.getValueAt(t.rowAtPoint(mouseEvent.getPoint()), 2).toString()) - 1;
                if (colIndex == 7) {
                    if (mouseEvent.getButton() == 1 && mouseEvent.getClickCount() == 2) {
                        new Thread(new Runnable() {
                            public void run() {
                                NewJTable table = (NewJTable) MainUi.map.get("RDM_table");
//                                String uid = table.getValueAt(rowIndex, 1).toString();
//                                String devType = table.getValueAt(rowIndex, 3).toString();
                                new RdmSetUI().show(table.getValueAt(table.getSelectedRow(), 1).toString(), table.getValueAt(table.getSelectedRow(), 3).toString());
                            }
                        }).start();
                        try {
                            openSet = true;
                            currentByte = (byte[]) uid_Byte.get(rowIndex);
                            //UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
                            OutputStream os = Data.serialPort.getOutputStream();

                            byte[] b = new byte[2];
                            b[0] = 1;
                            b[1] = 1;
                            //UsbUtil.sendMassge(sendUsbPipe, RdmData.setType(RdmPaneUI.currentByte, 6,b));
                            os.write(RdmData.setType(RdmPaneUI.currentByte, 6, b));
                            os.flush();

                            Thread.sleep(200);

                            //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType((byte[])RdmPaneUI.uid_Byte.get(rowIndex), 1));
                            os.write(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(rowIndex), 1));
                            os.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    t.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
        pane.setViewportView(table);
    }

    public void actionPerformed(ActionEvent e) {
        if ("搜索".equals(e.getActionCommand())) {
            if (Data.serialPort != null) {
                try {
                    final JButton btn = (JButton) e.getSource();
                    btn.setEnabled(false);
                    openSet = false;
                    uidList.clear();
                    uid_Byte.clear();
                    //typeMap.clear();
                    deviceCount = 0;
                    NewJTable table = (NewJTable) MainUi.map.get("RDM_table");
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    for (int i = table.getRowCount() - 1; i >= 0; i--) {
                        model.removeRow(i);
                    }
                    OutputStream os = Data.serialPort.getOutputStream();
                    os.write(RdmData.serch());
                    os.flush();
                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            if (RdmPaneUI.uidList.size() != 0 /*&& RdmPaneUI.uidList.size() == RdmPaneUI.deviceCount*/) {
                                try {
									/*if (RdmPaneUI.uidList.size() > 5) {
										Thread.sleep(RdmPaneUI.uidList.size()*200);
									}*/
                                    //UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
                                    OutputStream os = Data.serialPort.getOutputStream();
                                    //查型号
                                    for (int j = 0; j < RdmPaneUI.uid_Byte.size(); j++) {
                                        //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType((byte[])RdmPaneUI.uid_Byte.get(j), 5));
                                        os.write(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(j), 5));
                                        os.flush();
                                        Thread.sleep(300);
                                    }
                                    //查起始地址、通道数
                                    for (int j = 0; j < RdmPaneUI.uid_Byte.size(); j++) {
                                        //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType((byte[])RdmPaneUI.uid_Byte.get(j), 1));
                                        os.write(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(j), 1));
                                        os.flush();
                                        Thread.sleep(300);
                                    }
                                    NewJTable table = (NewJTable) MainUi.map.get("RDM_table");
                                    for (int n = 0; n < 5; n++) {
                                        Thread.sleep(100);
                                        for (int i = 0; i < table.getRowCount(); i++) {
                                            for (int j = 3; j < 6; j++) {
                                                //System.out.println("//"+table.getValueAt(i, j).toString()+"??");
                                                if ("".equals(table.getValueAt(i, j).toString())) {
                                                    if (j == 3) {
                                                        os.write(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(i), 5));
                                                        os.flush();
                                                        Thread.sleep(500);
                                                    } else {
                                                        os.write(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(i), 1));
                                                        os.flush();
                                                        Thread.sleep(500);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    btn.setEnabled(true);
                                } catch (Exception e) {
                                    btn.setEnabled(true);
                                    e.printStackTrace();
                                }
                            } else {
                                btn.setEnabled(true);
                            }
                        }
                    }, 8000);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else {
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "还未连接设备！", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if ("退出RDM".equals(e.getActionCommand())) {
            if (Data.serialPort != null) {
                try {
                    //FA 14 60 BA 11 00 F0 00 00 00 00 00 00 00 00 00 00 00 00 29
                    byte[] b = new byte[20];
                    //b[0] = (byte)0xCC;b[1] = (byte)0xAA;
                    //b[2] = (byte)0x02;b[3] = (byte)0x3C;

                    b[0] = (byte) 0xFA;
                    b[1] = (byte) 0x14;
                    b[2] = (byte) 0x60;
                    b[3] = (byte) ZhiLingJi.TYPE;
                    b[4] = (byte) 0x11;
                    b[5] = (byte) 0x00;
                    b[6] = (byte) 0xF0;
                    b[19] = (byte) ZhiLingJi.getJiaoYan(b);
                    OutputStream os = Data.serialPort.getOutputStream();
                    os.write(b);
                    os.flush();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else {
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "还未连接设备！", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if ("DMX排序".equals(e.getActionCommand())) {
            JButton btn = (JButton) MainUi.map.get("RDMSearch");
            if (btn.isEnabled()) {
                NewJTable table = (NewJTable) MainUi.map.get("RDM_table");
                List<RDM> rdms = new ArrayList<>();
                for (int i = 0; i < table.getRowCount(); i++) {
                    RDM rdm = new RDM();
                    rdm.setUID(table.getValueAt(i, 1).toString());
                    rdm.setModel(table.getValueAt(i, 3).toString());
                    rdm.setDMXStart(Integer.parseInt(table.getValueAt(i, 4).toString()));
                    rdm.setAisle(Integer.parseInt(table.getValueAt(i, 5).toString()));
                    rdm.setUidByte((byte[]) uid_Byte.get(i));
                    rdm.setUidTemp((String) uidList.get(i));
                    rdms.add(rdm);
                }
                Collections.sort(rdms);
                DefaultTableModel model = (DefaultTableModel) table.getModel();//清空数据
                for (int i = table.getRowCount() - 1; i >= 0; i--) {
                    model.removeRow(i);
                    uid_Byte.remove(i);
                    uidList.remove(i);
                }
                for (int i = 0; i < rdms.size(); i++) {
                    String[] s = {String.valueOf(table.getRowCount() + 1), rdms.get(i).getUID(),
                            String.valueOf(table.getRowCount() + 1), rdms.get(i).getModel(), rdms.get(i).getDMXStart() + "", rdms.get(i).getAisle() + "", "", "进入高级设置"};
                    uid_Byte.add(rdms.get(i).getUidByte());
                    uidList.add(rdms.get(i).getUidTemp());
                    model.addRow(s);
                }
            }
        }
    }

    public void actionPerformed2(ActionEvent e) {
        if ("搜索".equals(e.getActionCommand())) {
            UsbPipe sendUsbPipe = (UsbPipe) MainUi.map.get("sendUsbPipe");
            if (sendUsbPipe != null) {
                try {
                    openSet = false;
                    uidList.clear();
                    uid_Byte.clear();
                    //typeMap.clear();
                    deviceCount = 0;
                    NewJTable table = (NewJTable) MainUi.map.get("RDM_table");
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    for (int i = table.getRowCount() - 1; i >= 0; i--) {
                        model.removeRow(i);
                    }
                    UsbUtil.sendMassge(sendUsbPipe, RdmData.serch());
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else {
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "还未连接设备！", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            UsbPipe sendUsbPipe = (UsbPipe) MainUi.map.get("sendUsbPipe");
            if (sendUsbPipe != null) {
                try {
                    //FA 14 60 BA 11 00 F0 00 00 00 00 00 00 00 00 00 00 00 00 29
                    byte[] b = new byte[64];
                    b[0] = (byte) 0xCC;
                    b[1] = (byte) 0xAA;
                    b[2] = (byte) 0x02;
                    b[3] = (byte) 0x3C;

                    b[4] = (byte) 0xFA;
                    b[5] = (byte) 0x3C;
                    b[6] = (byte) 0x60;
                    b[7] = (byte) 0xBA;
                    b[8] = (byte) 0x11;
                    b[9] = (byte) 0x00;
                    b[10] = (byte) 0xF0;
                    b[63] = (byte) 0x51;
                    UsbUtil.sendMassge(sendUsbPipe, b);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else {
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "还未连接设备！", "提示", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    /*
     * 隐藏列
     */
    public void hiddenColumn(int columnIndex, JTable table) {
        TableColumnModel tcm = table.getColumnModel();
        TableColumn tc = tcm.getColumn(columnIndex);
        tc.setWidth(0);
        tc.setPreferredWidth(0);
        tc.setMaxWidth(0);
        tc.setMinWidth(0);
        table.getTableHeader().getColumnModel().getColumn(columnIndex)
                .setMaxWidth(0);
        table.getTableHeader().getColumnModel().getColumn(columnIndex)
                .setMinWidth(0);
    }
}
