package com.boray.xiaoGuoDeng.UI;

import com.boray.Data.*;
import com.boray.Listener.ZiroToFullActionListener;
import com.boray.Utils.IconJDialog;
import com.boray.Utils.Socket;
import com.boray.Utils.Util;
import com.boray.Utils.WaitProgressBar;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.usb.LastPacketData;
import com.boray.usb.UsbUtil;
import com.boray.xiaoGuoDeng.Listener.CopyToTimeBlockEdit;
import com.boray.xiaoGuoDeng.reviewBlock.ReviewBlock;
import com.boray.xiaoGuoDeng.reviewBlock.TimeBlockReviewData;

import javax.swing.*;
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
import java.awt.*;
import java.awt.event.*;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class OverDmxUI implements ActionListener {
    private NewJTable runTable;
    private JCheckBox[] checkBoxs;
    private JCheckBox[] checkBoxes;
    private JTextField[] textFields;
    private JSlider[] sliders;
    private JLabel[] names;
    private JLabel[] DmxValues;
    private JToggleButton[] btns;
    private JSlider slider;
    private JTextField field;
    private List<Integer> selectPre = new ArrayList<>();
    private ButtonGroup group;
    public static int selected = -1;
    public boolean[] bs;
    public boolean[] bs2;
    //private JRadioButton radioButton,radioButton2,radioButton3,radioButton4;
    private JComboBox box;
    private JComboBox box2;
    private int setNo;
    private JLabel stepLabel;

    public void show(int no) {
        setNo = no;
        selected = -1;
        bs = new boolean[512];
        bs2 = new boolean[512];
        IconJDialog dialog = new IconJDialog();
        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new IconJDialog(f, true);
        dialog.setResizable(false);
        dialog.setTitle("场景多灯" + no);
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        //flowLayout.setVgap(2);
        dialog.getContentPane().setLayout(flowLayout);
        int width = 930, height = 680;
        dialog.setSize(width, height);
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - width / 2, f.getLocation().y + f.getSize().height / 2 - height / 2);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JScrollPane p1 = new JScrollPane();
        JPanel p2 = new JPanel();
        JScrollPane p3 = new JScrollPane();
        JPanel p4 = new JPanel();
        JPanel p5 = new JPanel();
        JPanel p6 = new JPanel();
        setP2(p2);
        setP3(p3);
        setP5(p5);
        setP4(p4);//setP6(p6);
        setP1(p1);
        dialog.getContentPane().add(p1);
        dialog.getContentPane().add(p2);
        dialog.getContentPane().add(p3);
        dialog.getContentPane().add(p4);
        dialog.getContentPane().add(p5);
        dialog.getContentPane().add(p6);
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                if (runTable.getRowCount() > 0) {
                    DefaultTableModel modelTemp = (DefaultTableModel) runTable.getModel();
                    Vector temp = (Vector) modelTemp.getDataVector().clone();
                    Data.XiaoGuoDengModelDmxMap.put("TableData" + setNo, temp);
                } else {
                    Data.XiaoGuoDengModelDmxMap.put("TableData" + setNo, null);
                }
                boolean[] tbs = bs.clone();
                Data.XiaoGuoDengModelDmxMap.put("GouXuanValue" + setNo, tbs);
                tbs = bs2.clone();
                Data.XiaoGuoDengModelDmxMap.put("JianBianGouXuanValue" + setNo, tbs);
                String[] s = new String[1];
                //s[0] = String.valueOf(radioButton.isSelected());
                //s[1] = String.valueOf(radioButton3.isSelected());
//                s[0] = String.valueOf(box.getSelectedItem());
//                Data.XiaoGuoDengModelDmxMap.put("YaoMaiSet" + setNo, s);
            }
        });
        dialog.setVisible(true);
    }

    void setP1(JScrollPane scrollPane) {
        scrollPane.setPreferredSize(new Dimension(910, 140));
        scrollPane.setBorder(new LineBorder(Color.gray));

        JPanel pane = new JPanel();
        pane.setBorder(new LineBorder(Color.gray));
        pane.setPreferredSize(new Dimension(880, 250));

        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        scrollPane.setViewportView(pane);

        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(-2);
        flowLayout.setHgap(0);
        pane.setLayout(flowLayout);
        NewJTable table_dengJu = (NewJTable) MainUi.map.get("table_dengJu");
        int count = table_dengJu.getRowCount();
        if (count > 0) {
            selected = 0;
            btns = new JToggleButton[count];
            group = new ButtonGroup();
            for (int i = 0; i < btns.length; i++) {
                btns[i] = new JToggleButton();
                btns[i].addActionListener(this);
                btns[i].setName("" + i);
                btns[i].setFocusable(false);
                group.add(btns[i]);
                btns[i].setPreferredSize(new Dimension(120, 30));
                btns[i].setMargin(new Insets(0, -10, 0, -10));
                btns[i].setText(table_dengJu.getValueAt(i, 2).toString());
                pane.add(btns[i]);
                box2.addItem(table_dengJu.getValueAt(i, 2).toString());
            }
            btns[0].setSelected(true);
            setHeader();
        }
        if (runTable.getRowCount() > 0) {
            runTable.setRowSelectionInterval(0, 0);
        }
    }

    void setP2(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(-2);
        pane.setLayout(flowLayout);
        //pane.setBorder(new LineBorder(Color.gray));
        pane.setPreferredSize(new Dimension(910, 40));
		
		/*JPanel tempPane = new JPanel();
		tempPane.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
		tempPane.setLayout(new BorderLayout());
		tempPane.setPreferredSize(new Dimension(208,34));
		JPanel p1 = new JPanel();
		FlowLayout flowLayout2 = new FlowLayout(FlowLayout.CENTER);
		flowLayout2.setVgap(0);
		p1.setLayout(flowLayout2);
		p1.setBorder(new LineBorder(Color.gray));
		p1.setPreferredSize(new Dimension(208,30));
		radioButton = new JRadioButton("启动阶段");
		radioButton2 = new JRadioButton("运行阶段");
		ButtonGroup group = new ButtonGroup();
		group.add(radioButton);group.add(radioButton2);
		radioButton.setSelected(true);
		p1.add(radioButton);p1.add(radioButton2);
		tempPane.add(p1);*/
		
		/*JPanel p2 = new JPanel();
		FlowLayout flowLayout3 = new FlowLayout(FlowLayout.CENTER);
		flowLayout3.setVgap(-12);
		p2.setLayout(flowLayout3);
		p2.setPreferredSize(new Dimension(208,38));
		TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "运行模式", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
		p2.setBorder(tb1);
		radioButton3 = new JRadioButton("单次");
		radioButton4 = new JRadioButton("循环");
		ButtonGroup group2 = new ButtonGroup();
		group2.add(radioButton3);group2.add(radioButton4);
		radioButton3.setSelected(true);
		p2.add(radioButton3);p2.add(radioButton4);*/

        JPanel p3 = new JPanel();
        FlowLayout flowLayout4 = new FlowLayout(FlowLayout.LEFT);
        flowLayout4.setVgap(10);
        p3.setLayout(flowLayout4);
        p3.setPreferredSize(new Dimension(248, 40));
        p3.add(new JLabel("场景："));
        box = new JComboBox();
        for (int i = 1; i <= 24; i++) {
            box.addItem("" + i);
        }
        box.setPreferredSize(new Dimension(70, 26));
        //box.addItem("");
        //box.setEditable(true);
        final JButton changJingCopy = new JButton("复制");
        changJingCopy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = Integer.valueOf(box.getSelectedItem().toString());
                Data.XiaoGuoDengModelDmxMap.put("TableData" + setNo, Util.Clone(Data.XiaoGuoDengModelDmxMap.get("TableData" + index)));
                Data.XiaoGuoDengModelDmxMap.put("GouXuanValue" + setNo, Util.Clone(Data.XiaoGuoDengModelDmxMap.get("GouXuanValue" + index)));
                Data.XiaoGuoDengModelDmxMap.put("JianBianGouXuanValue" + setNo, Util.Clone(Data.XiaoGuoDengModelDmxMap.get("JianBianGouXuanValue" + index)));
                Object[] s = new String[514];
                String[] temp = new String[514];
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
                Vector vector99 = (Vector) Data.XiaoGuoDengModelDmxMap.get("TableData" + setNo);
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
                runTable.setModel(model);
                runTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                TableColumnModel tableColumnModel = runTable.getColumnModel();
                tableColumnModel.getColumn(0).setPreferredWidth(50);
                tableColumnModel.getColumn(1).setPreferredWidth(50);
                for (int i = 2; i < runTable.getColumnCount(); i++) {
                    tableColumnModel.getColumn(i).setPreferredWidth(30);
                }
                runTable.setRowSelectionInterval(runTable.getRowCount() - 1, runTable.getRowCount() - 1);
                setHeader();
            }
        });

        p3.add(box);
        p3.add(changJingCopy);

        String[] setValue = (String[]) Data.XiaoGuoDengModelDmxMap.get("YaoMaiSet" + setNo);
        if (setValue != null) {
            box.setSelectedItem(setValue[0]);
        }
        FlowLayout flowLayout1 = new FlowLayout(FlowLayout.CENTER);
        flowLayout1.setVgap(10);
        JPanel p5 = new JPanel();
        p5.setPreferredSize(new Dimension(300, 40));
        p5.setLayout(flowLayout1);
        box2 = new JComboBox();
        box2.setPreferredSize(new Dimension(160, 26));
        final JButton copy = new JButton("复制到当前");
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int select = box2.getSelectedIndex();
                NewJTable table2 = (NewJTable) MainUi.map.get("table_dengJu");
                int channelCount = Integer.valueOf(table2.getValueAt(selected, 6).toString()).intValue();
                int start = Integer.valueOf(table2.getValueAt(selected, 5).toString()).intValue();
                int channelCount2 = Integer.valueOf(table2.getValueAt(select, 6).toString()).intValue();
                int start2 = Integer.valueOf(table2.getValueAt(select, 5).toString()).intValue();
                if (channelCount != channelCount2) {
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "复制失败，灯库占用通道不一致！", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int tableSelect = runTable.getSelectedRow();
                for (int i = 0; i < channelCount; i++) {
                    bs[start + i - 1] = bs[start2 + i - 1];
                    bs2[start + i - 1] = bs2[start2 + i - 1];
                    checkBoxs[i].setSelected(bs[start2 + i - 1]);
                    checkBoxes[i].setSelected(bs2[start2 + i - 1]);
                    Integer value = Integer.valueOf(runTable.getValueAt(tableSelect, start2 + i + 1).toString());
                    runTable.setValueAt(value, tableSelect, start + i + 1);
                    sliders[i].setValue(value);
//                    DmxValues[i].setText(value + "");
                }
            }
        });
        p5.add(box2);
        p5.add(copy);

        JPanel p4 = new JPanel();

        p4.setLayout(flowLayout1);
        p4.setPreferredSize(new Dimension(280, 40));
        final JButton review = new JButton("预览");
        review.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //保存一下数据
                if (runTable.getRowCount() > 0) {
                    DefaultTableModel modelTemp = (DefaultTableModel) runTable.getModel();
                    Vector temp = (Vector) modelTemp.getDataVector().clone();
                    Data.XiaoGuoDengModelDmxMap.put("TableData" + setNo, temp);
                } else {
                    Data.XiaoGuoDengModelDmxMap.put("TableData" + setNo, null);
                }
                boolean[] tbs = bs.clone();
                Data.XiaoGuoDengModelDmxMap.put("GouXuanValue" + setNo, tbs);
                tbs = bs2.clone();
                Data.XiaoGuoDengModelDmxMap.put("JianBianGouXuanValue" + setNo, tbs);
//                String[] s = new String[1];
//                s[0] = String.valueOf(box.getSelectedItem());
//                Data.XiaoGuoDengModelDmxMap.put("YaoMaiSet" + setNo, s);
                //调用命令
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        review.setEnabled(false);
                        ReviewBlock.xiaoGuoDuoDengReview2(XiaoGuoDengModel.model);
                        review.setEnabled(true);
                    }
                });
                WaitProgressBar.show((Frame) MainUi.map.get("frame"), thread, "预览数据发送中。。。", "数据发送完成", "");
            }
        });
        final JButton stop = new JButton("停止");
        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Socket.SendData(TimeBlockReviewData.getStopReview3(XiaoGuoDengModel.model));
            }
        });
        final JButton button = new JButton("单选");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("单选")) {
                    for (int i = 0; i < btns.length; i++) {
                        group.remove(btns[i]);
                    }
                    button.setText("多选");
                } else {
                    for (int i = 0; i < btns.length; i++) {
                        group.add(btns[i]);
                    }
                    button.setText("单选");
                }
            }
        });
        p4.add(review);
        p4.add(stop);
        p4.add(new JLabel("  "));
        p4.add(button);
        //pane.add(tempPane);
        //pane.add(p2);
        pane.add(p3);
        pane.add(p5);
        pane.add(p4);
//        pane.add(button);
    }

    void setP3(JScrollPane scrollPane) {
        scrollPane.setPreferredSize(new Dimension(910, 260));
        scrollPane.setBorder(new LineBorder(Color.gray));

        JPanel pane = new JPanel();
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
        flowLayout2.setHgap(0);
        pane.setLayout(flowLayout2);
        pane.setPreferredSize(new Dimension(1570, 230));
        //TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "调光通道", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
        //tgPane.setBorder(tb);
        JPanel lefPane = new JPanel();
        //lefPane.setBorder(new LineBorder(Color.black));
        //lefPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,-4));
        lefPane.setPreferredSize(new Dimension(90, 230));
        lefPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        JPanel nullPane = new JPanel();
        nullPane.setPreferredSize(new Dimension(90, 125));
        lefPane.add(nullPane);
        JLabel huaBuJLabelAll = new JLabel("   通道全选");
        huaBuJLabelAll.setPreferredSize(new Dimension(90, 20));
        huaBuJLabelAll.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                NewJTable table2 = (NewJTable) MainUi.map.get("table_dengJu");
                boolean b = checkBoxs[0].isSelected();
                for (int i = 0; i < checkBoxs.length; i++) {
                    if (checkBoxs[i].isEnabled()) {
                        checkBoxs[i].setSelected(!b);
                    }
                }
                for (int i = 0; i < btns.length; i++) {
                    int start = Integer.valueOf(table2.getValueAt(i, 5).toString()).intValue();
                    int channelCount = Integer.valueOf(table2.getValueAt(i, 6).toString()).intValue();
                    for (int k = 0; k < channelCount; k++) {
                        bs[start + k - 1] = !b;
                    }
                }

            }

            public void mouseEntered(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        JLabel huaBuJLabel = new JLabel("   通道页选");
        huaBuJLabel.setPreferredSize(new Dimension(90, 20));
        huaBuJLabel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseReleased(MouseEvent e) {
                boolean b = checkBoxs[0].isSelected();
                for (int i = 0; i < checkBoxs.length; i++) {
                    if (checkBoxs[i].isEnabled()) {
                        checkBoxs[i].setSelected(!b);
                    }
                }
            }
        });
        lefPane.add(huaBuJLabelAll);
        lefPane.add(huaBuJLabel);
        JLabel huaBuJLabel2 = new JLabel("   渐变页选");
        JLabel huaBuJLabel2All = new JLabel("   渐变全选");
        huaBuJLabel2.setPreferredSize(new Dimension(90, 20));
        huaBuJLabel2All.setPreferredSize(new Dimension(90, 20));
        huaBuJLabel2.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseReleased(MouseEvent e) {
                boolean b = checkBoxes[0].isSelected();
                for (int i = 0; i < checkBoxes.length; i++) {
                    if (checkBoxes[i].isEnabled()) {
                        checkBoxes[i].setSelected(!b);
                    }
                }
            }
        });
        huaBuJLabel2All.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                NewJTable table2 = (NewJTable) MainUi.map.get("table_dengJu");
                boolean b = checkBoxes[0].isSelected();
                for (int i = 0; i < checkBoxes.length; i++) {
                    if (checkBoxes[i].isEnabled()) {
                        checkBoxes[i].setSelected(!b);
                    }
                }
                for (int i = 0; i < btns.length; i++) {
                    int start = Integer.valueOf(table2.getValueAt(i, 5).toString()).intValue();
                    int channelCount = Integer.valueOf(table2.getValueAt(i, 6).toString()).intValue();
                    for (int k = 0; k < channelCount; k++) {
                        bs2[start + k - 1] = !b;
                    }
                }
            }

            public void mouseEntered(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        });
        lefPane.add(huaBuJLabel2);
        lefPane.add(huaBuJLabel2All);
        //lefPane.add(new JLabel("DMX"));
        pane.add(lefPane);

        int count = 32;
        JPanel[] itemPanes = new JPanel[count];
        JLabel[] labels = new JLabel[count];
        textFields = new JTextField[count];
        sliders = new JSlider[count];
        checkBoxs = new JCheckBox[count];
        checkBoxes = new JCheckBox[count];
        names = new JLabel[count];
        DmxValues = new JLabel[count];
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(1);
        for (int i = 0; i < count; i++) {
            final int a = i;
            itemPanes[i] = new JPanel();
            itemPanes[i].setLayout(flowLayout);
            //itemPanes[i].setBorder(new LineBorder(Color.black));
            itemPanes[i].setPreferredSize(new Dimension(46, 210));
            if (i > 8) {
                labels[i] = new JLabel((i + 1) + "");
            } else {
                labels[i] = new JLabel("0" + (i + 1));
            }
            textFields[i] = new JTextField();
            textFields[i].setText(0 + "");
            textFields[i].setPreferredSize(new Dimension(36, 27));
            sliders[i] = new JSlider(JSlider.VERTICAL, 0, 255, 0);
            sliders[i].setPreferredSize(new Dimension(18, 76));
            checkBoxs[i] = new JCheckBox();
            checkBoxs[i].setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            checkBoxs[i].addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    JCheckBox checkBox = (JCheckBox) e.getSource();
                    bs[Integer.valueOf(DmxValues[a].getText()).intValue() - 1] = checkBox.isSelected();
                }
            });
            checkBoxs[i].setName("" + i);
            checkBoxes[i] = new JCheckBox();
            checkBoxes[i].setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            checkBoxes[i].addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    JCheckBox checkBox = (JCheckBox) e.getSource();
                    bs2[Integer.valueOf(DmxValues[a].getText()).intValue() - 1] = checkBox.isSelected();
                }
            });
            checkBoxes[i].setName("" + i);
            //names[i] = new JLabel("<html>未知<br><br></html>",JLabel.CENTER);
            names[i] = new JLabel(ChannelName.getChangeName("未知"), JLabel.CENTER);
            Font f1 = new Font("宋体", Font.PLAIN, 12);
            sliders[i].addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    textFields[a].setText(String.valueOf(sliders[a].getValue()));
                    int[] slt = runTable.getSelectedRows();
                    if (slt.length > 0) {
//                        for (int k = 0; k < slt.length; k++) {
//                            runTable.setValueAt(String.valueOf(sliders[a].getValue()), slt[k], Integer.valueOf(DmxValues[a].getText()).intValue() + 1);
//                        }

                        if (group.getButtonCount() <= 0) {
                            NewJTable table2 = (NewJTable) MainUi.map.get("table_dengJu");
                            for (int k = 0; k < slt.length; k++) {
                                for (int i = 0; i < selectPre.size(); i++) {
                                    int channelCount = Integer.valueOf(table2.getValueAt(selectPre.get(i), 6).toString()).intValue();
                                    int start = Integer.valueOf(table2.getValueAt(selectPre.get(i), 5).toString()).intValue();
                                    if (channelCount > a) {
                                        runTable.setValueAt(String.valueOf(sliders[a].getValue()), slt[k], start + a + 1);
                                    }
                                }

                            }
                        } else {
                            for (int k = 0; k < slt.length; k++) {
                                runTable.setValueAt(String.valueOf(sliders[a].getValue()), slt[k], Integer.valueOf(DmxValues[a].getText()).intValue() + 1);
                            }
                        }
                    }
                }
            });
            sliders[i].addMouseWheelListener(new MouseWheelListener() {
                public void mouseWheelMoved(MouseWheelEvent e) {
                    if (sliders[a].isEnabled()) {
                        if (e.getUnitsToScroll() > 0) {
                            sliders[a].setValue(sliders[a].getValue() - 1);
                        } else {
                            sliders[a].setValue(sliders[a].getValue() + 1);
                        }
                        outDevice();
                    }
                }
            });
            sliders[i].addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    outDevice();
                }
            });
            textFields[i].addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == 10) {
                        int tb = Integer.valueOf(textFields[a].getText()).intValue();
                        if (tb == sliders[a].getValue()) {
                            sliders[a].setValue(tb - 1);
                        }
                        sliders[a].setValue(tb);
                        outDevice();
                    }
                }
            });

            names[i].setFont(f1);
            names[i].setPreferredSize(new Dimension(42, 40));
            DmxValues[i] = new JLabel("" + (i + 1));
            names[i].setBorder(BorderFactory.createEmptyBorder(-10, 0, -10, 0));
            //names[i].setBorder(new LineBorder(Color.black));
            itemPanes[i].add(labels[i]);
            itemPanes[i].add(textFields[i]);
            itemPanes[i].add(sliders[i]);
            itemPanes[i].add(names[i]);
            itemPanes[i].add(checkBoxs[i]);
            itemPanes[i].add(checkBoxes[i]);
            itemPanes[i].add(DmxValues[i]);
            pane.add(itemPanes[i]);
        }
        scrollPane.getHorizontalScrollBar().setUnitIncrement(30);
        scrollPane.setViewportView(pane);
    }

    void setP4(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        //flowLayout.setVgap(0);
        pane.setLayout(flowLayout);
        pane.setBorder(new LineBorder(Color.gray));
        pane.setPreferredSize(new Dimension(910, 40));

        JButton button1 = new JButton("清零");
        JButton button2 = new JButton("全部清零");
        JButton button3 = new JButton("满值");
        JButton button4 = new JButton("全部满值");
        ZiroToFullActionListener ziroToFullActionListener = new ZiroToFullActionListener(runTable);
        ziroToFullActionListener.setDt(sliders, textFields);
        button1.addActionListener(ziroToFullActionListener);
        button2.addActionListener(ziroToFullActionListener);
        button3.addActionListener(ziroToFullActionListener);
        button4.addActionListener(ziroToFullActionListener);
        pane.add(button1);
        pane.add(button2);
        pane.add(button3);
        pane.add(button4);

        pane.add(new JLabel("                  " +
                "                  执行时长"));
        slider = new JSlider(0);
        slider.setValue(2000);
        slider.setPreferredSize(new Dimension(180, 30));
        slider.setMaximum(10000);
        field = new JTextField(4);
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
        pane.add(slider);
        pane.add(field);
        pane.add(new JLabel("毫秒"));
    }

    void setP5(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setHgap(0);
        pane.setLayout(flowLayout);
        //pane.setBorder(new LineBorder(Color.gray));
        //pane.setBorder(new LineBorder(Color.red));
        pane.setPreferredSize(new Dimension(910, 150));

        ///////////////////////////////////////////////////////////
        final JScrollPane p4 = new JScrollPane();
        p4.setBorder(new LineBorder(Color.gray));
        p4.setPreferredSize(new Dimension(910, 110));
        ////////////////////////////////////////////////////////////
        Object[] s = new String[514];
        final String[] temp = new String[514];
        temp[0] = "1";
        temp[1] = "2000";
        s[0] = "步骤";
        s[1] = "执行时长";
        for (int i = 2; i < s.length; i++) {
            s[i] = "" + (i - 1);
            temp[i] = "0";
        }
        Object[][] data88 = {};
        DefaultTableModel model = new DefaultTableModel(data88, s);
        Vector vector99 = (Vector) Data.XiaoGuoDengModelDmxMap.get("TableData" + setNo);
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
                int[] temp = runTable.getSelectedRows();
                int select1 = -1;
                if (temp.length > 0) {
                    select1 = temp[temp.length - 1];
                }
                if (select1 > -1 && !e.getValueIsAdjusting()) {
                    ChangeListener listener = null;
                    NewJTable table_DkGl = (NewJTable) MainUi.map.get("table_DkGl");

                    listener = slider.getChangeListeners()[0];
                    slider.removeChangeListener(listener);
                    field.setText(runTable.getValueAt(select1, 1).toString());
                    slider.setValue(Integer.valueOf(runTable.getValueAt(select1, 1).toString()).intValue());
                    slider.addChangeListener(listener);


                    //int toggleButtonSelect = selected;

                    if (selected != -1) {
                        //JSlider[] ChannelValueSliders = (JSlider[])MainUi.map.get("ChannelValueSliders");
                        //JTextField[] textFields = (JTextField[])MainUi.map.get("ChannelValueFields");
                        NewJTable table_dengJu = (NewJTable) MainUi.map.get("table_dengJu");

                        int channelCount = Integer.valueOf(table_dengJu.getValueAt(selected, 6).toString()).intValue();
                        int start = Integer.valueOf(table_dengJu.getValueAt(selected, 5).toString()).intValue();
                        for (int i = 0; i < channelCount; i++) {
                            listener = sliders[i].getChangeListeners()[0];
                            sliders[i].removeChangeListener(listener);
                            textFields[i].setText(runTable.getValueAt(select1, start + i + 1).toString());
                            sliders[i].setValue(Integer.valueOf(runTable.getValueAt(select1, start + i + 1).toString()).intValue());
                            sliders[i].addChangeListener(listener);
                        }
                    }
					/*ChangeListener listener = null;
					listener = slider.getChangeListeners()[0];
					slider.removeChangeListener(listener);
					String temp = runTable.getValueAt(selects[selects.length-1], 1).toString();
					field.setText(temp);
					slider.setValue(Integer.valueOf(temp).intValue());
					slider.addChangeListener(listener);
					for (int i = 2; i < runTable.getColumnCount(); i++) {
						listener = sliders[i-2].getChangeListeners()[0];
						sliders[i-2].removeChangeListener(listener);
						temp = runTable.getValueAt(selects[selects.length-1], i).toString();
						textFields[i-2].setText(temp);
						sliders[i-2].setValue(Integer.valueOf(temp).intValue());
						sliders[i-2].addChangeListener(listener);
					}*/
                    outDevice();
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
        int size = runTable.getRowCount();
        stepLabel = new JLabel("总步数:" + size);
        CopyToTimeBlockEdit copyListener = new CopyToTimeBlockEdit(runTable, stepLabel);
        menuItem.addActionListener(copyListener);
        menuItem1.addActionListener(copyListener);
        popupMenu.add(menuItem);
        popupMenu.add(menuItem1);
        runTable.setName("效果多灯");
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
        p5.setPreferredSize(new Dimension(910, 40));
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
        flowLayout2.setVgap(-2);
        flowLayout2.setHgap(0);
        p5.setLayout(flowLayout2);
        JButton btn = new JButton("添加");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (selected == -1) {
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "添加失败，请先添加灯具！", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (runTable.getRowCount() < 128) {
                    DefaultTableModel model = (DefaultTableModel) runTable.getModel();
                    String[] s = temp;
                    s[0] = "" + (runTable.getRowCount() + 1);
                    s[1] = "2000";
                    model.addRow(temp);
                    runTable.setRowSelectionInterval(runTable.getRowCount() - 1, runTable.getRowCount() - 1);
                    int size = runTable.getRowCount();
                    stepLabel.setText("总步数:" + size);
                } else {
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "添加失败，总步骤数不能超过128步！", "提示", JOptionPane.ERROR_MESSAGE);
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
                if (runTable.getSelectedRow() == -1) {
                    runTable.setRowSelectionInterval(runTable.getRowCount() - 1, runTable.getRowCount() - 1);
                }
                int size = runTable.getRowCount();
                stepLabel.setText("总步数:" + size);
            }
        });
        p5.add(btn);
        p5.add(btn2);
        p5.add(new JLabel("                                                                                                                                                                         "));
        p5.add(stepLabel);
        pane.add(p4);
        pane.add(p5);
    }

    public void actionPerformed(ActionEvent e) {
        if (group.getButtonCount() <= 0) {
            selectPre.clear();
            for (int i = 0; i < btns.length; i++) {
                if (btns[i].isSelected()) {
                    selectPre.add(i);
                }
            }
            return;
        }
        selected = Integer.valueOf(((JToggleButton) e.getSource()).getName()).intValue();
        setHeader();
    }

    private void setHeader() {
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        NewJTable table2 = (NewJTable) MainUi.map.get("table_dengJu");
        int index = Integer.valueOf(table2.getValueAt(selected, 3).toString().split("#")[0].substring(2)).intValue() - 1;

        int channelCount = Integer.valueOf(table2.getValueAt(selected, 6).toString()).intValue();
        Map map = (Map) Data.DengKuList.get(index);
        int start = Integer.valueOf(table2.getValueAt(selected, 5).toString()).intValue();

        int tableSelect = runTable.getSelectedRow();

        if (Data.XiaoGuoDengModelDmxMap.get("GouXuanValue" + setNo) != null) {
            bs = (boolean[]) Data.XiaoGuoDengModelDmxMap.get("GouXuanValue" + setNo);
        }
        if (Data.XiaoGuoDengModelDmxMap.get("JianBianGouXuanValue" + setNo) != null) {
            bs2 = (boolean[]) Data.XiaoGuoDengModelDmxMap.get("JianBianGouXuanValue" + setNo);
        }

        if (tableSelect != -1) {
            ChangeListener listener = null;
            ItemListener itemListener = null;
            for (int i = 0; i < channelCount; i++) {
                names[i].setEnabled(true);

                itemListener = checkBoxs[i].getItemListeners()[0];
                checkBoxs[i].removeItemListener(itemListener);
                checkBoxs[i].setEnabled(true);
                checkBoxs[i].setSelected(bs[start + i - 1]);
                checkBoxs[i].addItemListener(itemListener);

                itemListener = checkBoxes[i].getItemListeners()[0];
                checkBoxes[i].removeItemListener(itemListener);
                checkBoxes[i].setEnabled(true);
                checkBoxes[i].setSelected(bs2[start + i - 1]);
                checkBoxes[i].addItemListener(itemListener);

                sliders[i].setEnabled(true);
                textFields[i].setEnabled(true);
                DmxValues[i].setEnabled(true);
                DmxValues[i].setText((start + i) + "");
                sliders[i].setValue(Integer.valueOf(runTable.getValueAt(tableSelect, start + i + 1).toString()).intValue());
            }
            for (int i = channelCount; i < 32; i++) {
                names[i].setEnabled(false);
                checkBoxs[i].setEnabled(false);
                checkBoxes[i].setEnabled(false);
                sliders[i].setEnabled(false);
                textFields[i].setEnabled(false);
                DmxValues[i].setEnabled(false);
                names[i].setText("<html>未知<br><br></html>");
                listener = sliders[i].getChangeListeners()[0];
                sliders[i].removeChangeListener(listener);
                sliders[i].setValue(0);
                textFields[i].setText("0");
                sliders[i].addChangeListener(listener);

                itemListener = checkBoxs[i].getItemListeners()[0];
                checkBoxs[i].removeItemListener(itemListener);
                checkBoxs[i].setSelected(false);
                checkBoxs[i].addItemListener(itemListener);

                itemListener = checkBoxes[i].getItemListeners()[0];
                checkBoxes[i].removeItemListener(itemListener);
                checkBoxes[i].setSelected(false);
                checkBoxes[i].addItemListener(itemListener);
                DmxValues[i].setText("1");
            }
            if (channelCount > 16) {
                for (int i = 0; i < 16; i++) {
                    names[i].setText(ChangeFormatString.change(map.get("L" + i).toString()));
                }
                for (int i = 0; i < channelCount - 16; i++) {
                    names[i + 16].setText(ChangeFormatString.change(map.get("R" + i).toString()));
                }
            } else {
                for (int i = 0; i < channelCount; i++) {
                    names[i].setText(ChangeFormatString.change(map.get("L" + i).toString()));
                }
            }
        } else {
            ChangeListener listener = null;
            ItemListener itemListener = null;
            for (int i = 0; i < channelCount; i++) {
                names[i].setEnabled(true);

                itemListener = checkBoxs[i].getItemListeners()[0];
                checkBoxs[i].removeItemListener(itemListener);
                checkBoxs[i].setEnabled(true);
                checkBoxs[i].setSelected(bs[start + i - 1]);
                checkBoxs[i].addItemListener(itemListener);

                itemListener = checkBoxes[i].getItemListeners()[0];
                checkBoxes[i].removeItemListener(itemListener);
                checkBoxes[i].setEnabled(true);
                checkBoxes[i].setSelected(bs2[start + i - 1]);
                checkBoxes[i].addItemListener(itemListener);

                sliders[i].setEnabled(true);
                textFields[i].setEnabled(true);
                DmxValues[i].setEnabled(true);
                DmxValues[i].setText((start + i) + "");
            }
            for (int i = channelCount; i < 32; i++) {
                names[i].setEnabled(false);
                names[i].setText("<html>未知<br><br></html>");
                checkBoxs[i].setEnabled(false);
                checkBoxes[i].setEnabled(false);
                sliders[i].setEnabled(false);
                textFields[i].setEnabled(false);
                DmxValues[i].setEnabled(false);

                listener = sliders[i].getChangeListeners()[0];
                sliders[i].removeChangeListener(listener);
                sliders[i].setValue(0);
                textFields[i].setText("0");
                sliders[i].addChangeListener(listener);

                itemListener = checkBoxs[i].getItemListeners()[0];
                checkBoxs[i].removeItemListener(itemListener);
                checkBoxs[i].setSelected(false);
                checkBoxs[i].addItemListener(itemListener);

                itemListener = checkBoxes[i].getItemListeners()[0];
                checkBoxes[i].removeItemListener(itemListener);
                checkBoxes[i].setSelected(false);
                checkBoxes[i].addItemListener(itemListener);

                DmxValues[i].setText("1");
            }
            if (channelCount > 16) {
                for (int i = 0; i < 16; i++) {
                    names[i].setText(ChangeFormatString.change(map.get("L" + i).toString()));
                }
                for (int i = 0; i < channelCount - 16; i++) {
                    names[i + 16].setText(ChangeFormatString.change(map.get("R" + i).toString()));
                }
            } else {
                for (int i = 0; i < channelCount; i++) {
                    names[i].setText(ChangeFormatString.change(map.get("L" + i).toString()));
                }
            }
        }
    }

    private void showDtOfTable() {
        NewJTable table = (NewJTable) MainUi.map.get("table_DkGl");
        NewJTable table2 = (NewJTable) MainUi.map.get("table_dengJu");
        int index = Integer.valueOf(table.getValueAt(selected, 3).toString().split("#")[0].substring(2)).intValue();

        int channelCount = Integer.valueOf(Data.dengKuName.get(index).toString()).intValue();
        Map map = (Map) Data.DengKuList.get(index);
        int start = Integer.valueOf(table2.getValueAt(selected, 5).toString()).intValue();


        int tableSelect = runTable.getSelectedRow();

        if (tableSelect != -1) {
            ChangeListener listener = null;
            ItemListener itemListener = null;
            for (int i = 0; i < channelCount; i++) {
                names[i].setEnabled(true);
                checkBoxs[i].setEnabled(true);
                sliders[i].setEnabled(true);
                textFields[i].setEnabled(true);
                checkBoxs[i].setSelected(Data.checkList[start + i - 1]);
            }
            for (int i = channelCount; i < 32; i++) {
                names[i].setEnabled(false);
                checkBoxs[i].setEnabled(false);
                sliders[i].setEnabled(false);
                textFields[i].setEnabled(false);

                listener = sliders[i].getChangeListeners()[0];
                sliders[i].removeChangeListener(listener);
                sliders[i].setValue(0);
                textFields[i].setText("0");
                sliders[i].addChangeListener(listener);
                itemListener = checkBoxs[i].getItemListeners()[0];
                checkBoxs[i].removeItemListener(itemListener);
                checkBoxs[i].setSelected(false);
                checkBoxs[i].addItemListener(itemListener);
            }
            if (channelCount > 16) {
                for (int i = 0; i < 16; i++) {
                    names[i].setText(ChangeFormatString.change(map.get("L" + i).toString()));
                }
                for (int i = 0; i < channelCount - 16; i++) {
                    names[i + 16].setText(ChangeFormatString.change(map.get("R" + i).toString()));
                }
            } else {
                for (int i = 0; i < channelCount; i++) {
                    names[i].setText(ChangeFormatString.change(map.get("L" + i).toString()));
                }
            }
        } else {

        }
    }

    void setP6(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        //flowLayout.setVgap(0);
        pane.setLayout(flowLayout);
        pane.setBorder(new LineBorder(Color.gray));
        pane.setPreferredSize(new Dimension(858, 46));

        JPanel p4 = new JPanel();
        //p4.setBorder(new LineBorder(Color.gray));
        p4.setPreferredSize(new Dimension(544, 44));
        p4.add(new JLabel("执行时长"));
        JSlider slider = new JSlider(0);
        slider.setValue(0);
        slider.setPreferredSize(new Dimension(360, 30));
        slider.setMaximum(5000);
        JTextField field = new JTextField(4);
        p4.add(slider);
        p4.add(field);
        p4.add(new JLabel("毫秒"));

        pane.add(p4);
    }

    private void outDevice() {
        int[] slt = runTable.getSelectedRows();
        int value = 0;
        //int startAddress = box88.getSelectedIndex()+1;
        if (slt.length != 0) {
            byte[] buff = new byte[512 + 8];
            buff[0] = (byte) 0xBB;
            buff[1] = (byte) 0x55;
            buff[2] = (byte) (520 / 256);
            buff[3] = (byte) (520 % 256);
            buff[4] = (byte) 0x80;
            buff[5] = (byte) 0x01;
            buff[6] = (byte) 0xFF;
            for (int j = 2; j < runTable.getColumnCount(); j++) {
                value = Integer.valueOf(runTable.getValueAt(slt[0], j).toString()).intValue();
                buff[j - 2 + 7] = (byte) value;
            }
            buff[519] = ZhiLingJi.getJiaoYan(buff);
            Socket.SendData(buff);
//            try {
//                OutputStream os = Data.serialPort.getOutputStream();
//                os.write(buff);
//                os.flush();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

    private void outDevice_usb() {
        int[] slt = runTable.getSelectedRows();
        int value = 0;
        //int startAddress = box88.getSelectedIndex()+1;
        UsbPipe sendUsbPipe = (UsbPipe) MainUi.map.get("sendUsbPipe");
        if (sendUsbPipe != null && slt.length != 0) {
            byte[] buff = new byte[512];
            byte[] temp = new byte[64];
            int[] tl = new int[3];
            for (int j = 0; j < 2; j++) {
                tl[j] = Integer.valueOf(runTable.getValueAt(slt[0], j).toString()).intValue();
            }
            for (int j = 2; j < runTable.getColumnCount(); j++) {
                value = Integer.valueOf(runTable.getValueAt(slt[0], j).toString()).intValue();
                buff[j - 2] = (byte) value;
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
