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
import com.boray.Utils.Socket;
import com.boray.entity.RDM;
import com.boray.mainUi.MainUi;
import com.boray.usb.UsbUtil;

public class RdmPaneUI implements ActionListener {
    public static List uidList = new ArrayList(15);//Uid
    public static List uid_Byte = new ArrayList(15);//Uid byte��ʽ
    public static List tempUidList = new ArrayList(15);//Uid
    public static List tempUid_Byte = new ArrayList(15);//Uid byte��ʽ
    public static boolean openSet = false;
    public static byte[] currentByte = null;
    //public static Map typeMap = new HashMap();//�ͺ�
    //public static Map addAndChannelMap = new HashMap<>();//��ʼ��ַ��ͨ��
    public static int deviceCount = 0;
    private JPopupMenu popupMenu;
    private JMenuItem autoCreateDMX;

    private JButton btn2;
    private JButton btn5;


    public void show(JPanel pane) {
        pane.setBorder(new LineBorder(Color.gray));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(0);
        pane.setLayout(flowLayout);
        pane.setPreferredSize(new Dimension(902, 588));


        JPanel headPane = new JPanel();
        FlowLayout flowLayout2 = new FlowLayout(flowLayout.LEFT);
        flowLayout2.setVgap(0);
        headPane.setLayout(flowLayout2);
        headPane.setPreferredSize(new Dimension(890, 32));
        //headPane.setBorder(new LineBorder(Color.gray));
        //JButton btn1 = new JButton("���RDM�ƾ���Ϣ");
        btn2 = new JButton("����");
        btn5 = new JButton("ˢ��");
        MainUi.map.put("RDMRefresh", btn5);
        MainUi.map.put("RDMSearch", btn2);
        JButton btn3 = new JButton("�˳�RDM");
        JButton btn4 = new JButton("DMX����");
        //btn1.setPreferredSize(new Dimension(158,34));
        btn2.setPreferredSize(new Dimension(88, 34));
        btn3.setPreferredSize(new Dimension(88, 34));
        btn4.setPreferredSize(new Dimension(88, 34));
        btn5.setPreferredSize(new Dimension(88, 34));
        //btn1.setFocusable(false);btn2.setFocusable(false);btn3.setFocusable(false);
        btn2.addActionListener(this);
        btn3.addActionListener(this);
        btn4.addActionListener(this);
        btn5.addActionListener(this);
        //headPane.add(new JLabel("     "));
        //headPane.add(btn1);
        headPane.add(btn2);
        headPane.add(btn5);
        headPane.add(btn3);
        headPane.add(btn4);

        popupMenu = new JPopupMenu();
        autoCreateDMX = new JMenuItem("�Զ�����DMX");
        ButtonGroup group = new ButtonGroup();
        group.add(autoCreateDMX);
        popupMenu.add(autoCreateDMX);

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewJTable table = (NewJTable) MainUi.map.get("RDM_table");
                int[] a = table.getSelectedRows();
                int count = 0;
                count = (Integer.parseInt(table.getValueAt(a[0], 4).toString()) + Integer.parseInt(table.getValueAt(a[0], 5).toString()));
                for (int i = 0; i < a.length - 1; i++) {
                    count += Integer.parseInt(table.getValueAt(a[i], 5).toString());
                }
                if (count > 512) {
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "��ʼ��ַ��Χ1 - 512��", "��ʾ", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    count = (Integer.parseInt(table.getValueAt(a[0], 4).toString()) + Integer.parseInt(table.getValueAt(a[0], 5).toString()));
                    for (int i = 0; i < a.length - 1; i++) {
                        byte[] b = new byte[3];
                        b[0] = 2;
                        b[1] = (byte) (count / 256);
                        b[2] = (byte) (count % 256);
                        int s = Integer.parseInt(table.getValueAt(a[i + 1], 2).toString()) - 1;
                        Socket.SendData(RdmData.setType((byte[]) uid_Byte.get(s), 3, b));
                        table.setValueAt(count, a[i + 1], 4);
                        Thread.sleep(300);
                        count += Integer.parseInt(table.getValueAt(a[i + 1], 5).toString());
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        };
        autoCreateDMX.addActionListener(listener);

        JScrollPane bodyPane = new JScrollPane();
        setBodyPane(bodyPane);

        pane.add(headPane);
        pane.add(bodyPane);
    }

    private void setBodyPane(JScrollPane pane) {
        //pane.setBorder(new LineBorder(Color.gray));
        pane.setPreferredSize(new Dimension(890, 552));

        //Object[][] data = {{"ID","UID","�ƾ�����","�ͺ�","5","18","��ע","�߼�����"},{"ID","UID","�ƾ�����","�ͺ�","15","28","��ע","�߼�����"}};
        Object[][] data = {};
        String[] title = {"ID", "UID", "�ƾ�����", "�ͺ�", "DMX��ʼ��ַ", "ռ��ͨ����", "��ע", "�߼�����"};
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
		            	cell.setBackground(new Color(218,218,218)); //���������е�ɫ  
		            } else {  
		            	cell.setBackground(Color.white); //����ż���е�ɫ  
		            }  
				}*/
                if (!isSelected) {
                    if (row % 2 == 0) {
                        cell.setBackground(new Color(237, 243, 254));
                        cell.setForeground(Color.black);
                    } else {
                        cell.setBackground(Color.white); //����ż���е�ɫ
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
                super.mouseReleased(e);
                if (e.getButton() == 3 && table.getSelectedRows().length > 1) {
                    popupMenu.show(table, e.getX(), e.getY());
                }
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
//                            OutputStream os = Data.serialPort.getOutputStream();

                            byte[] b = new byte[2];
                            b[0] = 1;
                            b[1] = 1;
                            //UsbUtil.sendMassge(sendUsbPipe, RdmData.setType(RdmPaneUI.currentByte, 6,b));
//                            os.write(RdmData.setType(RdmPaneUI.currentByte, 6, b));
//                            os.flush();
                            Socket.SendData(RdmData.setType(RdmPaneUI.currentByte, 6, b));
                            Thread.sleep(200);

                            //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType((byte[])RdmPaneUI.uid_Byte.get(rowIndex), 1));
//                            os.write(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(rowIndex), 1));
//                            os.flush();
                            Socket.SendData(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(rowIndex), 1));
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
        if ("����".equals(e.getActionCommand())) {
            if (Data.serialPort != null||Data.socket!=null) {
                try {
//                    final JButton btn = (JButton) e.getSource();
                    btn2.setEnabled(false);
                    btn5.setEnabled(false);
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
//                    OutputStream os = Data.serialPort.getOutputStream();
//                    os.write(RdmData.serch());
//                    os.flush();
                    Socket.SendData(RdmData.serch());
                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            if (RdmPaneUI.uidList.size() != 0 /*&& RdmPaneUI.uidList.size() == RdmPaneUI.deviceCount*/) {
                                try {
									/*if (RdmPaneUI.uidList.size() > 5) {
										Thread.sleep(RdmPaneUI.uidList.size()*200);
									}*/
                                    //UsbPipe sendUsbPipe = (UsbPipe)MainUi.map.get("sendUsbPipe");
//                                    OutputStream os = Data.serialPort.getOutputStream();
                                    //���ͺ�
                                    for (int j = 0; j < RdmPaneUI.uid_Byte.size(); j++) {
                                        //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType((byte[])RdmPaneUI.uid_Byte.get(j), 5));
//                                        os.write(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(j), 5));
//                                        os.flush();
                                        Socket.SendData(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(j), 5));
                                        Thread.sleep(300);
                                    }
                                    //����ʼ��ַ��ͨ����
                                    for (int j = 0; j < RdmPaneUI.uid_Byte.size(); j++) {
                                        //UsbUtil.sendMassge(sendUsbPipe, RdmData.serchType((byte[])RdmPaneUI.uid_Byte.get(j), 1));
//                                        os.write(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(j), 1));
//                                        os.flush();
                                        Socket.SendData(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(j), 1));
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
//                                                        os.write(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(i), 5));
//                                                        os.flush();
                                                        Socket.SendData(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(i), 5));
                                                        Thread.sleep(500);
                                                    } else {
//                                                        os.write(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(i), 1));
//                                                        os.flush();
                                                        Socket.SendData(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(i), 1));
                                                        Thread.sleep(500);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    btn2.setEnabled(true);
                                    btn5.setEnabled(true);
                                } catch (Exception e) {
                                    btn2.setEnabled(true);
                                    btn5.setEnabled(true);
                                    e.printStackTrace();
                                }
                            } else {
                                btn2.setEnabled(true);
                                btn5.setEnabled(true);
                            }
                        }
                    }, 8000);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else {
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "��δ�����豸��", "��ʾ", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if ("�˳�RDM".equals(e.getActionCommand())) {
            if (Data.serialPort != null||Data.socket!=null) {
                Socket.SendData(RdmData.quit());
            } else {
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "��δ�����豸��", "��ʾ", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else if ("DMX����".equals(e.getActionCommand())) {
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
                DefaultTableModel model = (DefaultTableModel) table.getModel();//�������
                for (int i = table.getRowCount() - 1; i >= 0; i--) {
                    model.removeRow(i);
                    uid_Byte.remove(i);
                    uidList.remove(i);
                }
                for (int i = 0; i < rdms.size(); i++) {
                    String[] s = {String.valueOf(table.getRowCount() + 1), rdms.get(i).getUID(),
                            String.valueOf(table.getRowCount() + 1), rdms.get(i).getModel(), rdms.get(i).getDMXStart() + "", rdms.get(i).getAisle() + "", "", "����߼�����"};
                    uid_Byte.add(rdms.get(i).getUidByte());
                    uidList.add(rdms.get(i).getUidTemp());
                    model.addRow(s);
                }
            }
        } else if ("ˢ��".equals(e.getActionCommand())) {
            try {
//                final JButton btn = (JButton) e.getSource();
                btn2.setEnabled(false);
                btn5.setEnabled(false);
                openSet = false;
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        //ˢ����������
                        if (RdmPaneUI.uidList.size() != 0) {
                            try {
                                //���ͺ�
                                for (int j = 0; j < RdmPaneUI.uid_Byte.size(); j++) {
                                    Socket.SendData(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(j), 5));
                                    Thread.sleep(300);
                                }
                                //����ʼ��ַ��ͨ����
                                for (int j = 0; j < RdmPaneUI.uid_Byte.size(); j++) {
                                    Socket.SendData(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(j), 1));
                                    Thread.sleep(300);
                                }
                                NewJTable table = (NewJTable) MainUi.map.get("RDM_table");
                                for (int n = 0; n < 5; n++) {
                                    Thread.sleep(100);
                                    for (int i = 0; i < table.getRowCount(); i++) {
                                        for (int j = 3; j < 6; j++) {
                                            if ("".equals(table.getValueAt(i, j).toString())) {
                                                if (j == 3) {
                                                    Socket.SendData(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(i), 5));
                                                    Thread.sleep(500);
                                                } else {
                                                    Socket.SendData(RdmData.serchType((byte[]) RdmPaneUI.uid_Byte.get(i), 1));
                                                    Thread.sleep(500);
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, 8000);
                Socket.SendData(RdmData.serch());//���·��Ͳ�ѯUID  ˢ��
                new Timer().schedule(new TimerTask() {
                    public void run() {
                        if (RdmPaneUI.tempUidList.size() != 0) {
                            try {
                                //���ͺ�
                                for (int j = 0; j < RdmPaneUI.tempUid_Byte.size(); j++) {
                                    Socket.SendData(RdmData.serchType((byte[]) RdmPaneUI.tempUid_Byte.get(j), 5));
                                    Thread.sleep(300);
                                }
                                //����ʼ��ַ��ͨ����
                                for (int j = 0; j < RdmPaneUI.tempUid_Byte.size(); j++) {
                                    Socket.SendData(RdmData.serchType((byte[]) RdmPaneUI.tempUid_Byte.get(j), 1));
                                    Thread.sleep(300);
                                }
                                NewJTable table = (NewJTable) MainUi.map.get("RDM_table");
                                for (int n = 0; n < 5; n++) {
                                    Thread.sleep(100);
                                    for (int i = 0; i < table.getRowCount(); i++) {
                                        for (int j = 3; j < 6; j++) {
                                            if ("".equals(table.getValueAt(i, j).toString())) {
                                                if (j == 3) {
                                                    Socket.SendData(RdmData.serchType((byte[]) RdmPaneUI.tempUid_Byte.get(i), 5));
                                                    Thread.sleep(500);
                                                } else {
                                                    Socket.SendData(RdmData.serchType((byte[]) RdmPaneUI.tempUid_Byte.get(i), 1));
                                                    Thread.sleep(500);
                                                }
                                            }
                                        }
                                    }
                                }
                                for (int i = 0; i < RdmPaneUI.tempUidList.size(); i++) {
                                    RdmPaneUI.uidList.add(RdmPaneUI.tempUidList.get(i));
                                    RdmPaneUI.uid_Byte.add(RdmPaneUI.tempUid_Byte.get(i));
                                }
                                //�����ʱ����
                                RdmPaneUI.tempUidList.clear();
                                RdmPaneUI.tempUid_Byte.clear();
                                btn2.setEnabled(true);
                                btn5.setEnabled(true);
                            } catch (Exception e) {
                                btn2.setEnabled(true);
                                btn5.setEnabled(true);
                                e.printStackTrace();
                            }
                        } else {
                            btn2.setEnabled(true);
                            btn5.setEnabled(true);
                        }
                    }
                }, 8000);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public void actionPerformed2(ActionEvent e) {
        if ("����".equals(e.getActionCommand())) {
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
                JOptionPane.showMessageDialog(frame, "��δ�����豸��", "��ʾ", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(frame, "��δ�����豸��", "��ʾ", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }

    /*
     * ������
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
