package com.boray.xiaoGuoDeng.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.usb.UsbPipe;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.Socket;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.usb.LastPacketData;
import com.boray.usb.UsbUtil;
import com.boray.xiaoGuoDeng.Listener.CopyToTimeBlockEdit;

public class WuJiModelUI {
    private NewJTable starTable;
    private NewJTable runTable;
    //private Map wuJiDataMap;
    private List wuJiComponList;
    private Vector vector88, vector99;
    private String[] tkp;
    private String[] tkp2;
    private JComboBox box88;

    public void show(final int no) {
        wuJiComponList = new ArrayList();
        JDialog dialog = new JDialog();
        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new JDialog(f, true);
        dialog.setResizable(false);
        dialog.setTitle("雾机控制" + no);
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(0);
        dialog.setLayout(flowLayout);
        int width = 580, height = 450;
        dialog.setSize(width, height);
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - width / 2, f.getLocation().y + f.getSize().height / 2 - height / 2);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                List allList = (List) Data.wuJiMap.get("" + no);
                if (allList == null) {
                    allList = new ArrayList();
                }
                allList.clear();
                if (starTable.getRowCount() > 0) {
                    DefaultTableModel modelTemp = (DefaultTableModel) starTable.getModel();
                    Vector temp = (Vector) modelTemp.getDataVector().clone();
                    allList.add(temp);
                } else {
                    allList.add(null);
                }

                if (runTable.getRowCount() > 0) {
                    DefaultTableModel modelTemp = (DefaultTableModel) runTable.getModel();
                    Vector temp = (Vector) modelTemp.getDataVector().clone();
                    allList.add(temp);
                } else {
                    allList.add(null);
                }
                String[] tp = new String[4];
                //雾机状态
                JRadioButton radioButton = (JRadioButton) wuJiComponList.get(0);
                tp[0] = String.valueOf(radioButton.isSelected());
                JComboBox box = (JComboBox) wuJiComponList.get(1);
                tp[1] = box.getSelectedItem().toString();
                radioButton = (JRadioButton) wuJiComponList.get(2);
                tp[2] = String.valueOf(radioButton.isSelected());
                box = (JComboBox) wuJiComponList.get(3);
                tp[3] = box.getSelectedItem().toString();
                allList.add(tp);
                //////////
                String[] tj = new String[]{tp[1], tp[3]};
                //////////
                Data.wuJiMap.put("" + no, allList);
                Data.wuJiMap.put("QJ_set", tj);
            }
        });
        List allList = (List) Data.wuJiMap.get("" + no);
        tkp2 = (String[]) Data.wuJiMap.get("QJ_set");
        if (allList != null) {
            vector88 = (Vector) allList.get(0);
            vector99 = (Vector) allList.get(1);
            tkp = (String[]) allList.get(2);
        }

        JPanel headPane = new JPanel();
        JPanel bodyPane = new JPanel();
        setHeadPane(headPane);
        setBodyPane(bodyPane);
        dialog.add(headPane);
        dialog.add(bodyPane);
        dialog.setVisible(true);
    }

    private void setBodyPane(JPanel pane) {
        pane.setPreferredSize(new Dimension(564, 358));
        //pane.setBorder(new LineBorder(Color.gray));
        pane.setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        //tabbedPane.setPreferredSize(new Dimension(1020,100));
        tabbedPane.setFocusable(false);
        JPanel startPane = new JPanel();
        JPanel runPane = new JPanel();
        setStartPane(startPane);
        setRunPane(runPane);
        tabbedPane.add("启动阶段", startPane);
        tabbedPane.add("运行阶段", runPane);
        pane.add(tabbedPane);
    }

    private void setStartPane(JPanel pane) {
        pane.setLayout(new FlowLayout(FlowLayout.CENTER));
        JPanel p1 = new JPanel();
        p1.setBorder(new LineBorder(Color.gray));
        p1.setPreferredSize(new Dimension(544, 150));
        JPanel[] panels = new JPanel[10];
        JLabel[] labels = new JLabel[10];
        final JTextField[] fields = new JTextField[10];
        final JSlider[] sliders = new JSlider[10];
        for (int i = 0; i < panels.length; i++) {
            final int a = i;
            panels[i] = new JPanel();
            FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
            flowLayout.setVgap(-4);
            panels[i].setLayout(flowLayout);
            panels[i].setPreferredSize(new Dimension(48, 138));
            if (i == 9) {
                labels[i] = new JLabel("" + (i + 1));
            } else {
                labels[i] = new JLabel("0" + (i + 1));
            }
            fields[i] = new JTextField(2);
            fields[i].setPreferredSize(new Dimension(48, 28));
            sliders[i] = new JSlider(SwingConstants.VERTICAL);
            sliders[i].setMaximum(255);
            sliders[i].setValue(0);
            sliders[i].setPreferredSize(new Dimension(20, 100));
            panels[i].add(labels[i]);
            panels[i].add(fields[i]);
            panels[i].add(sliders[i]);

            sliders[i].addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    fields[a].setText(String.valueOf(sliders[a].getValue()));
                    int[] slt = starTable.getSelectedRows();
                    if (slt.length > 0) {
                        for (int k = 0; k < slt.length; k++) {
                            starTable.setValueAt(String.valueOf(sliders[a].getValue()), slt[k], a + 2);
                        }
                    }
                }
            });
            sliders[i].addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    outDevice(starTable);
                }
            });

            fields[i].addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == 10) {
                        int tb = Integer.valueOf(fields[a].getText()).intValue();
                        if (tb == sliders[a].getValue()) {
                            sliders[a].setValue(tb - 1);
                        }
                        sliders[a].setValue(tb);
                        outDevice(starTable);
                    }
                }
            });

            p1.add(panels[i]);
        }


        JPanel p2 = new JPanel();
        p2.setBorder(new LineBorder(Color.gray));
        p2.setPreferredSize(new Dimension(544, 40));
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton button = new JButton("清零");
        JButton button2 = new JButton("满值");
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("清零".equals(e.getActionCommand())) {
                    for (int i = 0; i < sliders.length; i++) {
                        if (sliders[i].isEnabled()) {
                            sliders[i].setValue(1);
                            sliders[i].setValue(0);
                        }
                    }
                } else {
                    for (int i = 0; i < sliders.length; i++) {
                        if (sliders[i].isEnabled()) {
                            sliders[i].setValue(1);
                            sliders[i].setValue(255);
                        }
                    }
                }
                outDevice(starTable);
            }
        };
        button.addActionListener(actionListener);
        button2.addActionListener(actionListener);
        p2.add(button);
        p2.add(button2);

        //JPanel p4 = new JPanel();
        //p4.setBorder(new LineBorder(Color.gray));
        //p4.setPreferredSize(new Dimension(544,50));
        p2.add(new JLabel("执行时长"));
        final JSlider slider = new JSlider(0);
        slider.setPreferredSize(new Dimension(200, 30));
        slider.setMaximum(60000);
        final JTextField field = new JTextField(4);
        p2.add(slider);
        p2.add(field);
        p2.add(new JLabel("毫秒"));
        ////////////////////////执行时长
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                field.setText(String.valueOf(slider.getValue()));
                int[] slt = starTable.getSelectedRows();
                if (slt.length > 0) {
                    for (int k = 0; k < slt.length; k++) {
                        starTable.setValueAt(String.valueOf(slider.getValue()), slt[k], 1);
                    }
                }
            }
        });
        field.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field.getText()).intValue();
                    if (tb == slider.getValue()) {
                        slider.setValue(tb - 1);
                    }
                    slider.setValue(tb);
                }
            }
        });


        JPanel p3 = new JPanel();
        //p3.setBorder(new LineBorder(Color.gray));
        p3.setPreferredSize(new Dimension(544, 120));

        final JScrollPane p4 = new JScrollPane();
        p4.setBorder(new LineBorder(Color.gray));
        p4.setPreferredSize(new Dimension(544, 80));
        ////////////////////////////////////////////////////////////
        Object[] s = new String[12];
        final String[] temp = new String[12];
        temp[0] = "1";
        temp[1] = "0";
        s[0] = "步骤";
        s[1] = "执行时长";
        for (int i = 2; i < s.length; i++) {
            s[i] = "" + (i - 1);
            temp[i] = "0";
        }
        Object[][] data88 = {};
        DefaultTableModel model = new DefaultTableModel(data88, s);

        if (vector88 != null) {
            Vector tp = null;
            int a = 0;
            for (int i = 0; i < vector88.size(); i++) {
                tp = (Vector) vector88.get(i);
                a = tp.size();
                if (a < 12) {
                    for (int j = 0; j < 12 - a; j++) {
                        tp.add("0");
                    }
                }
                model.addRow(tp);
            }
        } else {
            model.addRow(temp);
        }
        starTable = new NewJTable(model, 0);
        ///////////////////////////////
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        cell.setBackground(new Color(237, 243, 254));
                        cell.setForeground(Color.black);
                    } else {
                        cell.setBackground(Color.white); //设置偶数行底色
                        cell.setForeground(Color.black);
                    }
                } else {
                    cell.setBackground(new Color(56, 117, 215));
                    cell.setForeground(Color.white);
                }
                return cell;
            }
        };
        for (int i = 0; i < s.length; i++) {
            starTable.getColumn(starTable.getColumnName(i)).setCellRenderer(cellRenderer);
        }
        starTable.setSelectionBackground(new Color(56, 117, 215));
        starTable.getTableHeader().setUI(new BasicTableHeaderUI());
        starTable.getTableHeader().setReorderingAllowed(false);
        starTable.setOpaque(false);
        //starTable.setFocusable(false);
        starTable.setFont(new Font(Font.SERIF, Font.BOLD, 14));
        //starTable.getColumnModel().getColumn(1).setPreferredWidth(102);
        starTable.setRowHeight(15);
        starTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int[] selects = starTable.getSelectedRows();
                if (selects.length > 0 && !e.getValueIsAdjusting()) {
                    ChangeListener listener = null;
                    listener = slider.getChangeListeners()[0];
                    slider.removeChangeListener(listener);
                    String temp = starTable.getValueAt(selects[selects.length - 1], 1).toString();
                    field.setText(temp);
                    slider.setValue(Integer.valueOf(temp).intValue());
                    slider.addChangeListener(listener);
                    for (int i = 2; i < starTable.getColumnCount(); i++) {
                        listener = sliders[i - 2].getChangeListeners()[0];
                        sliders[i - 2].removeChangeListener(listener);
                        temp = starTable.getValueAt(selects[selects.length - 1], i).toString();
                        fields[i - 2].setText(temp);
                        sliders[i - 2].setValue(Integer.valueOf(temp).intValue());
                        sliders[i - 2].addChangeListener(listener);
                    }
                    outDevice(starTable);
                }
            }
        });
        starTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel tableColumnModel = starTable.getColumnModel();
        tableColumnModel.getColumn(0).setPreferredWidth(50);
        tableColumnModel.getColumn(1).setPreferredWidth(50);
        for (int i = 2; i < starTable.getColumnCount(); i++) {
            tableColumnModel.getColumn(i).setPreferredWidth(30);
        }

        p4.getHorizontalScrollBar().setUnitIncrement(30);
        p4.setViewportView(starTable);

        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("　复制　　　　　");
        JMenuItem menuItem1 = new JMenuItem("　粘贴　　　　　");
        CopyToTimeBlockEdit copyListener = new CopyToTimeBlockEdit(starTable);
        menuItem.addActionListener(copyListener);
        menuItem1.addActionListener(copyListener);
        popupMenu.add(menuItem);
        popupMenu.add(menuItem1);
        starTable.setName("雾机");
        starTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int s = starTable.rowAtPoint(new Point(e.getX(), e.getY()));
                    int[] a = starTable.getSelectedRows();
                    boolean select = false;
                    for (int i = 0; i < a.length; i++) {
                        if (s == a[i]) {
                            select = true;
                            break;
                        }
                    }
                    if (select) {
                        popupMenu.show(starTable, e.getX(), e.getY());
                    } else {
                        starTable.setRowSelectionInterval(s, s);
                        popupMenu.show(starTable, e.getX(), e.getY());
                    }
                }
            }
        });
        p4.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.show(p4, e.getX(), e.getY());
                }
            }
        });

        JPanel p5 = new JPanel();
        p5.setPreferredSize(new Dimension(540, 40));
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
        flowLayout2.setVgap(-2);
        flowLayout2.setHgap(0);
        p5.setLayout(flowLayout2);
        JButton btn = new JButton("添加");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (starTable.getRowCount() < 5) {
                    DefaultTableModel model = (DefaultTableModel) starTable.getModel();
                    String[] s = temp;
                    s[0] = "" + (starTable.getRowCount() + 1);
                    model.addRow(temp);
                    starTable.setRowSelectionInterval(starTable.getRowCount() - 1, starTable.getRowCount() - 1);
                } else {
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "添加失败，总步骤数不能超过5步！", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });
        JButton btn2 = new JButton("删除");
        btn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] s = starTable.getSelectedRows();
                DefaultTableModel model = (DefaultTableModel) starTable.getModel();
                for (int i = s.length - 1; i >= 0; i--) {
                    model.removeRow(s[i]);
                }
                for (int i = 0; i < starTable.getRowCount(); i++) {
                    starTable.setValueAt("" + (i + 1), i, 0);
                }
                if (starTable.getRowCount() > 0) {
                    starTable.setRowSelectionInterval(0, 0);
                }
            }
        });
        p5.add(btn);
        p5.add(btn2);

        p3.add(p4);
        p3.add(p5);


        pane.add(p1);
        pane.add(p2);
        pane.add(p3);
        if (starTable.getRowCount() > 0) {
            starTable.setRowSelectionInterval(0, 0);
        }
        //pane.add(p4);
    }

    private void setRunPane(JPanel pane) {
        pane.setLayout(new FlowLayout(FlowLayout.CENTER));
        JPanel p1 = new JPanel();
        p1.setBorder(new LineBorder(Color.gray));
        p1.setPreferredSize(new Dimension(544, 150));
        JPanel[] panels = new JPanel[10];
        JLabel[] labels = new JLabel[10];
        final JTextField[] fields = new JTextField[10];
        final JSlider[] sliders = new JSlider[10];
        for (int i = 0; i < panels.length; i++) {
            final int a = i;
            panels[i] = new JPanel();
            FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
            flowLayout.setVgap(-4);
            panels[i].setLayout(flowLayout);
            panels[i].setPreferredSize(new Dimension(48, 138));
            if (i == 9) {
                labels[i] = new JLabel("" + (i + 1));
            } else {
                labels[i] = new JLabel("0" + (i + 1));
            }
            fields[i] = new JTextField(2);
            fields[i].setPreferredSize(new Dimension(48, 28));
            sliders[i] = new JSlider(SwingConstants.VERTICAL);
            sliders[i].setMaximum(255);
            sliders[i].setValue(0);
            sliders[i].setPreferredSize(new Dimension(20, 100));
            panels[i].add(labels[i]);
            panels[i].add(fields[i]);
            panels[i].add(sliders[i]);

            sliders[i].addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    fields[a].setText(String.valueOf(sliders[a].getValue()));
                    int[] slt = runTable.getSelectedRows();
                    if (slt.length > 0) {
                        for (int k = 0; k < slt.length; k++) {
                            runTable.setValueAt(String.valueOf(sliders[a].getValue()), slt[k], a + 2);
                        }
                    }
                }
            });

            sliders[i].addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    outDevice(runTable);
                }
            });

            fields[i].addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == 10) {
                        int tb = Integer.valueOf(fields[a].getText()).intValue();
                        if (tb == sliders[a].getValue()) {
                            sliders[a].setValue(tb - 1);
                        }
                        sliders[a].setValue(tb);
                        outDevice(runTable);
                    }
                }
            });

            p1.add(panels[i]);
        }


        JPanel p2 = new JPanel();
        p2.setBorder(new LineBorder(Color.gray));
        p2.setPreferredSize(new Dimension(544, 40));
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        JButton button = new JButton("清零");
        JButton button2 = new JButton("满值");
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("清零".equals(e.getActionCommand())) {
                    for (int i = 0; i < sliders.length; i++) {
                        if (sliders[i].isEnabled()) {
                            sliders[i].setValue(1);
                            sliders[i].setValue(0);
                        }
                    }
                } else {
                    for (int i = 0; i < sliders.length; i++) {
                        if (sliders[i].isEnabled()) {
                            sliders[i].setValue(1);
                            sliders[i].setValue(255);
                        }
                    }
                }
                outDevice(runTable);
            }
        };
        button.addActionListener(actionListener);
        button2.addActionListener(actionListener);
        p2.add(button);
        p2.add(button2);

        //JPanel p4 = new JPanel();
        //p4.setBorder(new LineBorder(Color.gray));
        //p4.setPreferredSize(new Dimension(544,50));
        p2.add(new JLabel("执行时长"));
        final JSlider slider = new JSlider(0);
        slider.setPreferredSize(new Dimension(200, 30));
        slider.setMaximum(60000);
        final JTextField field = new JTextField(4);
        p2.add(slider);
        p2.add(field);
        p2.add(new JLabel("毫秒"));
        ////////////////////////执行时长
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                field.setText(String.valueOf(slider.getValue()));
                int[] slt = runTable.getSelectedRows();
                if (slt.length > 0) {
                    for (int k = 0; k < slt.length; k++) {
                        runTable.setValueAt(String.valueOf(slider.getValue()), slt[k], 1);
                    }
                }
            }
        });
        field.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field.getText()).intValue();
                    if (tb == slider.getValue()) {
                        slider.setValue(tb - 1);
                    }
                    slider.setValue(tb);
                }
            }
        });


        JPanel p3 = new JPanel();
        //p3.setBorder(new LineBorder(Color.gray));
        p3.setPreferredSize(new Dimension(544, 120));

        final JScrollPane p4 = new JScrollPane();
        p4.setBorder(new LineBorder(Color.gray));
        p4.setPreferredSize(new Dimension(544, 80));
        ////////////////////////////////////////////////////////////
        Object[] s = new String[12];
        final String[] temp = new String[12];
        temp[0] = "1";
        temp[1] = "0";
        s[0] = "步骤";
        s[1] = "执行时长";
        for (int i = 2; i < s.length; i++) {
            s[i] = "" + (i - 1);
            temp[i] = "0";
        }
        Object[][] data88 = {};
        DefaultTableModel model = new DefaultTableModel(data88, s);
        if (vector99 != null) {
            Vector tp = null;
            int a = 0;
            for (int i = 0; i < vector99.size(); i++) {
                tp = (Vector) vector99.get(i);
                a = tp.size();
                if (a < 12) {
                    for (int j = 0; j < 12 - a; j++) {
                        tp.add("0");
                    }
                }
                model.addRow(tp);
            }
        } else {
            model.addRow(temp);
        }
        runTable = new NewJTable(model, 0);
        ///////////////////////////////
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        cell.setBackground(new Color(237, 243, 254));
                        cell.setForeground(Color.black);
                    } else {
                        cell.setBackground(Color.white); //设置偶数行底色
                        cell.setForeground(Color.black);
                    }
                } else {
                    cell.setBackground(new Color(56, 117, 215));
                    cell.setForeground(Color.white);
                }
                return cell;
            }
        };
        for (int i = 0; i < s.length; i++) {
            runTable.getColumn(runTable.getColumnName(i)).setCellRenderer(cellRenderer);
        }
        runTable.setSelectionBackground(new Color(56, 117, 215));
        runTable.getTableHeader().setUI(new BasicTableHeaderUI());
        runTable.getTableHeader().setReorderingAllowed(false);
        runTable.setOpaque(false);
        //runTable.setFocusable(false);
        runTable.setFont(new Font(Font.SERIF, Font.BOLD, 14));
        //runTable.getColumnModel().getColumn(1).setPreferredWidth(102);
        runTable.setRowHeight(15);
        runTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int[] selects = runTable.getSelectedRows();
                if (selects.length > 0 && !e.getValueIsAdjusting()) {
                    ChangeListener listener = null;
                    listener = slider.getChangeListeners()[0];
                    slider.removeChangeListener(listener);
                    String temp = runTable.getValueAt(selects[selects.length - 1], 1).toString();
                    field.setText(temp);
                    slider.setValue(Integer.valueOf(temp).intValue());
                    slider.addChangeListener(listener);
                    for (int i = 2; i < runTable.getColumnCount(); i++) {
                        listener = sliders[i - 2].getChangeListeners()[0];
                        sliders[i - 2].removeChangeListener(listener);
                        temp = runTable.getValueAt(selects[selects.length - 1], i).toString();
                        fields[i - 2].setText(temp);
                        sliders[i - 2].setValue(Integer.valueOf(temp).intValue());
                        sliders[i - 2].addChangeListener(listener);
                    }
                    outDevice(runTable);
                }
            }
        });
        runTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel tableColumnModel = runTable.getColumnModel();
        tableColumnModel.getColumn(0).setPreferredWidth(50);
        tableColumnModel.getColumn(1).setPreferredWidth(50);
        for (int i = 2; i < runTable.getColumnCount(); i++) {
            tableColumnModel.getColumn(i).setPreferredWidth(30);
        }

        p4.getHorizontalScrollBar().setUnitIncrement(30);
        p4.setViewportView(runTable);

        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("　复制　　　　　");
        JMenuItem menuItem1 = new JMenuItem("　粘贴　　　　　");
        CopyToTimeBlockEdit copyListener = new CopyToTimeBlockEdit(runTable);
        menuItem.addActionListener(copyListener);
        menuItem1.addActionListener(copyListener);
        popupMenu.add(menuItem);
        popupMenu.add(menuItem1);
        runTable.setName("雾机");
        runTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int s = runTable.rowAtPoint(new Point(e.getX(), e.getY()));
                    int[] a = runTable.getSelectedRows();
                    boolean select = false;
                    for (int i = 0; i < a.length; i++) {
                        if (s == a[i]) {
                            select = true;
                            break;
                        }
                    }
                    if (select) {
                        popupMenu.show(runTable, e.getX(), e.getY());
                    } else {
                        runTable.setRowSelectionInterval(s, s);
                        popupMenu.show(runTable, e.getX(), e.getY());
                    }
                }
            }
        });
        p4.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.show(p4, e.getX(), e.getY());
                }
            }
        });

        JPanel p5 = new JPanel();
        p5.setPreferredSize(new Dimension(540, 40));
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
        flowLayout2.setVgap(-2);
        flowLayout2.setHgap(0);
        p5.setLayout(flowLayout2);
        JButton btn = new JButton("添加");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (runTable.getRowCount() < 5) {
                    DefaultTableModel model = (DefaultTableModel) runTable.getModel();
                    String[] s = temp;
                    s[0] = "" + (runTable.getRowCount() + 1);
                    model.addRow(temp);
                    runTable.setRowSelectionInterval(runTable.getRowCount() - 1, runTable.getRowCount() - 1);
                } else {
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "添加失败，总步骤数不能超过5步！", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });
        JButton btn2 = new JButton("删除");
        btn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] s = runTable.getSelectedRows();
                DefaultTableModel model = (DefaultTableModel) runTable.getModel();
                for (int i = s.length - 1; i >= 0; i--) {
                    model.removeRow(s[i]);
                }
                for (int i = 0; i < runTable.getRowCount(); i++) {
                    runTable.setValueAt("" + (i + 1), i, 0);
                }
                if (runTable.getRowCount() > 0) {
                    runTable.setRowSelectionInterval(0, 0);
                }
            }
        });
        p5.add(btn);
        p5.add(btn2);

        p3.add(p4);
        p3.add(p5);


        pane.add(p1);
        pane.add(p2);
        pane.add(p3);
        if (runTable.getRowCount() > 0) {
            runTable.setRowSelectionInterval(0, 0);
        }
        //pane.add(p4);
    }

    private void setHeadPane(JPanel pane) {
        pane.setPreferredSize(new Dimension(564, 60));
        //pane.setBorder(new LineBorder(Color.gray));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(0);
        pane.setLayout(flowLayout);
        pane.add(new JLabel("            雾机状态:"));
        JRadioButton radioButton = new JRadioButton("启用");
        JRadioButton radioButton2 = new JRadioButton("禁用");
        wuJiComponList.add(radioButton);
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton);
        group.add(radioButton2);
        radioButton2.setSelected(true);
        box88 = new JComboBox();
        wuJiComponList.add(box88);
        for (int i = 1; i < 513; i++) {
            box88.addItem(String.valueOf(i));
        }
        box88.setSelectedIndex(499);
        pane.add(radioButton);
        pane.add(radioButton2);
        pane.add(new JLabel("            DMX512地址:"));
        pane.add(box88);
        pane.add(new JLabel("                       "));
        pane.add(new JLabel("            运行模式:"));
        JRadioButton radioButton3 = new JRadioButton("单次");
        JRadioButton radioButton4 = new JRadioButton("循环");
        wuJiComponList.add(radioButton3);
        ButtonGroup group2 = new ButtonGroup();
        group2.add(radioButton3);
        group2.add(radioButton4);
        radioButton3.setSelected(true);
        JComboBox box2 = new JComboBox();
        wuJiComponList.add(box2);
        box2.setPreferredSize(new Dimension(70, 28));
        for (int i = 1; i < 11; i++) {
            box2.addItem(String.valueOf(i));
        }
        pane.add(radioButton3);
        pane.add(radioButton4);
        pane.add(new JLabel("                 通道数量:"));
        pane.add(box2);
        if (tkp != null) {
            if (tkp[0].equals("true")) {
                radioButton.setSelected(true);
            } else {
                radioButton2.setSelected(true);
            }
            //box.setSelectedItem(tkp[1]);
            if (tkp[2].equals("true")) {
                radioButton3.setSelected(true);
            } else {
                radioButton4.setSelected(true);
            }
            //box2.setSelectedItem(tkp[3]);
        }
        if (tkp2 != null) {
            box88.setSelectedItem(tkp2[0]);
            box2.setSelectedItem(tkp2[1]);
        }
    }

    private void outDevice(NewJTable table) {
        int[] slt = table.getSelectedRows();
        int value = 0;
        int startAddress = box88.getSelectedIndex() + 1;
        if (slt.length != 0) {
            byte[] buff = new byte[512 + 8];
            byte[] bytes = new byte[512];
            buff[0] = (byte) 0xBB;
            buff[1] = (byte) 0x55;
            buff[2] = (byte) (520 / 256);
            buff[3] = (byte) (520 % 256);
            buff[4] = (byte) 0x80;
            buff[5] = (byte) 0x01;
            buff[6] = (byte) 0xFF;
            for (int j = 2; j < table.getColumnCount(); j++) {
                value = Integer.valueOf(table.getValueAt(slt[0], j).toString()).intValue();
                buff[j - 3 + startAddress + 7] = (byte) value;
            }
            buff[519] = ZhiLingJi.getJiaoYan(buff);
            if (Data.serialPort != null) {
                Socket.SerialPortSendData(buff);
            } else if (Data.socket != null) {
                Socket.UDPSendData(buff);
            }
            System.arraycopy(buff, 8, bytes, 0, 512);
            Socket.ArtNetSendData(bytes);//添加artNet数据协议发送
        }
    }

    private void outDevice_usb(NewJTable table) {
        int[] slt = table.getSelectedRows();
        int value = 0;
        int startAddress = box88.getSelectedIndex() + 1;
        UsbPipe sendUsbPipe = (UsbPipe) MainUi.map.get("sendUsbPipe");
        if (sendUsbPipe != null && slt.length != 0) {
            byte[] buff = new byte[512];
            byte[] temp = new byte[64];
            int[] tl = new int[3];
            for (int j = 0; j < 2; j++) {
                tl[j] = Integer.valueOf(table.getValueAt(slt[0], j).toString()).intValue();
            }
            for (int j = 2; j < table.getColumnCount(); j++) {
                value = Integer.valueOf(table.getValueAt(slt[0], j).toString()).intValue();
                buff[j - 3 + startAddress] = (byte) value;
            }
            try {
                for (int k = 0; k < 8; k++) {
                    System.arraycopy(buff, k * 64, temp, 0, 64);
                    UsbUtil.sendMassge(sendUsbPipe, temp);
                }
                UsbUtil.sendMassge(sendUsbPipe, LastPacketData.getL(buff, tl));
            } catch (javax.usb.UsbPlatformException e2) {
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "数据写出发生错误！", "提示", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
