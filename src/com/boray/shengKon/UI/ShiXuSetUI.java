package com.boray.shengKon.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.boray.Data.Data;
import com.boray.Data.MyData;
import com.boray.Utils.IconJDialog;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

public class ShiXuSetUI {
    private int[][] DataSetInts;

    public void show(int blockNum) {
        IconJDialog dialog = new IconJDialog();
        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new IconJDialog(f, true);
        dialog.setResizable(false);
        dialog.setTitle("声控时序设置-模式" + MyData.ShengKonModel + "时间片" + blockNum);
        int modelInt = Integer.valueOf(MyData.ShengKonModel).intValue() - 1;
        int TimeBlockInt = blockNum - 1;
        DataSetInts = (int[][]) Data.ShengKonShiXuSetObjects[modelInt][TimeBlockInt];
        if (DataSetInts == null) {
            DataSetInts = new int[2][67 + 32];
            Data.ShengKonShiXuSetObjects[modelInt][TimeBlockInt] = DataSetInts;
        }
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(0);
        dialog.setLayout(flowLayout);
        int width = 1040, height = 696;
        dialog.setSize(width, height);
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - width / 2, f.getLocation().y + f.getSize().height / 2 - height / 2);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();
        JPanel p5 = new JPanel();
        JPanel p6 = new JPanel();
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "低频运行", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "高频运行", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "低频持续运行", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        TitledBorder tb3 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "高频持续运行", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p1.setBorder(tb);
        p2.setBorder(tb1);
        p3.setBorder(tb);
        p4.setBorder(tb1);
        p5.setBorder(tb2);
        p6.setBorder(tb3);
        p1.setPreferredSize(new Dimension(610, 84));
        p2.setPreferredSize(new Dimension(400, 84));
        p3.setPreferredSize(new Dimension(610, 322));
        p4.setPreferredSize(new Dimension(400, 322));
        p5.setPreferredSize(new Dimension(610, 258));
        p6.setPreferredSize(new Dimension(400, 258));
        setP1(p1);
        setP2(p2);
        setP3(p3);
        setP4(p4);
        setP5(p5);
        setP6(p6);

        dialog.add(p1);
        dialog.add(p2);
        dialog.add(p3);
        dialog.add(p4);
        dialog.add(p5);
        dialog.add(p6);
        dialog.setVisible(true);
    }

    void setP1(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(-3);
        pane.setLayout(flowLayout);

        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(580, 30));
        //p1.setBorder(new LineBorder(Color.gray));
        p1.setLayout(flowLayout);
        p1.add(new JLabel("灭灯模式:"));
        final JComboBox box = new JComboBox();
        box.addItem("硬切");
        box.addItem("渐变");
        p1.add(box);
        final JLabel label = new JLabel("渐变速度:");
        final JComboBox box2 = new JComboBox();
        box2.addItem("特慢");
        box2.addItem("慢");
        box2.addItem("普快");
        box2.addItem("快");
        box2.addItem("特快");
        label.setVisible(false);
        box2.setVisible(false);
        p1.add(label);
        p1.add(box2);
        box.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    DataSetInts[0][0] = box.getSelectedIndex();
                    if ("渐变".equals(e.getItem().toString())) {
                        label.setVisible(true);
                        box2.setVisible(true);
                    } else {
                        label.setVisible(false);
                        box2.setVisible(false);
                    }
                }
            }
        });
        box2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    DataSetInts[0][1] = box2.getSelectedIndex();
                }
            }
        });

        pane.add(p1);

        pane.add(new JLabel(" 亮灯模式:"));
        final JComboBox box3 = new JComboBox();
        box3.addItem("硬切");
        box3.addItem("渐变");
        box3.addItem("心跳");
        pane.add(box3);
        final JLabel label2 = new JLabel("渐变速度:");
        final JComboBox box4 = new JComboBox();
        box4.addItem("特慢");
        box4.addItem("慢");
        box4.addItem("普快");
        box4.addItem("快");
        box4.addItem("特快");
        pane.add(label2);
        pane.add(box4);
        label2.setVisible(false);
        box4.setVisible(false);

        box3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    DataSetInts[0][2] = box3.getSelectedIndex();
                    if ("渐变".equals(e.getItem().toString())) {
                        label2.setVisible(true);
                        box4.setVisible(true);
                    } else {
                        label2.setVisible(false);
                        box4.setVisible(false);
                    }
                }
            }
        });
        box4.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    DataSetInts[0][3] = box4.getSelectedIndex();
                }
            }
        });
        box.setSelectedIndex(DataSetInts[0][0]);
        box2.setSelectedIndex(DataSetInts[0][1]);
        box3.setSelectedIndex(DataSetInts[0][2]);
        box4.setSelectedIndex(DataSetInts[0][3]);
    }

    //高频
    void setP2(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(-3);
        pane.setLayout(flowLayout);

        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(480, 30));
        //p1.setBorder(new LineBorder(Color.gray));
        p1.setLayout(flowLayout);
        p1.add(new JLabel("灭灯模式:"));
        final JComboBox box = new JComboBox();
        box.addItem("硬切");
        box.addItem("渐变");
        p1.add(box);
        final JLabel label = new JLabel("渐变速度:");
        final JComboBox box2 = new JComboBox();
        box2.addItem("特慢");
        box2.addItem("慢");
        box2.addItem("普快");
        box2.addItem("快");
        box2.addItem("特快");
        label.setVisible(false);
        box2.setVisible(false);
        p1.add(label);
        p1.add(box2);
        box.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    DataSetInts[1][0] = box.getSelectedIndex();
                    if ("渐变".equals(e.getItem().toString())) {
                        label.setVisible(true);
                        box2.setVisible(true);
                    } else {
                        label.setVisible(false);
                        box2.setVisible(false);
                    }
                }
            }
        });
        box2.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    DataSetInts[1][1] = box2.getSelectedIndex();
                }
            }
        });
        pane.add(p1);

        pane.add(new JLabel(" 亮灯模式:"));
        final JComboBox box3 = new JComboBox();
        box3.addItem("硬切");
        box3.addItem("渐变");
        box3.addItem("心跳");
        pane.add(box3);
        final JLabel label2 = new JLabel("渐变速度:");
        final JComboBox box4 = new JComboBox();
        box4.addItem("特慢");
        box4.addItem("慢");
        box4.addItem("普快");
        box4.addItem("快");
        box4.addItem("特快");
        pane.add(label2);
        pane.add(box4);
        label2.setVisible(false);
        box4.setVisible(false);

        box3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    DataSetInts[1][2] = box3.getSelectedIndex();
                    if ("渐变".equals(e.getItem().toString())) {
                        label2.setVisible(true);
                        box4.setVisible(true);
                    } else {
                        label2.setVisible(false);
                        box4.setVisible(false);
                    }
                }
            }
        });
        box4.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    DataSetInts[1][3] = box4.getSelectedIndex();
                }
            }
        });
        box.setSelectedIndex(DataSetInts[1][0]);
        box2.setSelectedIndex(DataSetInts[1][1]);
        box3.setSelectedIndex(DataSetInts[1][2]);
        box4.setSelectedIndex(DataSetInts[1][3]);
    }

    //低频
    void setP3(JPanel pane) {
        JPanel[] panels = new JPanel[10];
        JLabel[] labels = new JLabel[10];
        JCheckBox[] checkBoxs = new JCheckBox[10];
        JComboBox[][] comboBoxs = new JComboBox[10][5];
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(-2);
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
        flowLayout2.setVgap(3);
        flowLayout2.setHgap(-10);
        pane.setLayout(flowLayout2);
        NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        for (int i = 0; i < 10; i++) {
            final int a = i;
            panels[i] = new JPanel();
            panels[i].setLayout(flowLayout);
            panels[i].setPreferredSize(new Dimension(620, 26));
            //panels[i].setBorder(new LineBorder(Color.gray));
            if (i == 9) {
                labels[i] = new JLabel("步骤10");
            } else {
                labels[i] = new JLabel("步骤0" + (i + 1));
            }
            checkBoxs[i] = new JCheckBox("启用");
            checkBoxs[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JCheckBox box = (JCheckBox) e.getSource();
                    if (box.isSelected()) {
                        DataSetInts[0][4 + a * 6] = 1;
                    } else {
                        DataSetInts[0][4 + a * 6] = 0;
                    }
                }
            });
            if (DataSetInts[0][4 + i * 6] == 1) {
                checkBoxs[i].setSelected(true);
            } else {
                checkBoxs[i].setSelected(false);
            }
            panels[i].add(labels[i]);
            panels[i].add(checkBoxs[i]);
            for (int j = 0; j < 5; j++) {
                final int jj = j;
                comboBoxs[i][j] = new JComboBox();
                comboBoxs[i][j].addItem("空");
                for (int n = 0; n < table.getRowCount(); n++) {
                    boolean b = (boolean) table.getValueAt(n, 0);
                    if (b) {
                        comboBoxs[i][j].addItem(table.getValueAt(n, 2).toString());
                    }
                }
                comboBoxs[i][j].addItem("多灯运行1");
                comboBoxs[i][j].addItem("多灯运行2");
                comboBoxs[i][j].setPreferredSize(new Dimension(96, 30));
                if (DataSetInts[0][5 + j + i * 6] < comboBoxs[i][j].getItemCount()) {
                    comboBoxs[i][j].setSelectedIndex(DataSetInts[0][5 + j + i * 6]);
                } else {
                    DataSetInts[0][5 + j + i * 6] = 0;
                    comboBoxs[i][j].setSelectedIndex(0);
                }
                comboBoxs[i][j].addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        JComboBox box = (JComboBox) e.getSource();
                        if (ItemEvent.SELECTED == e.getStateChange()) {
                            DataSetInts[0][5 + jj + a * 6] = box.getSelectedIndex();
                        }
                    }
                });
                panels[i].add(comboBoxs[i][j]);
            }
            pane.add(panels[i]);
        }
    }

    void setP4(JPanel pane) {
        JPanel[] panels = new JPanel[10];
        JLabel[] labels = new JLabel[10];
        JCheckBox[] checkBoxs = new JCheckBox[10];
        JComboBox[][] comboBoxs = new JComboBox[10][3];
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(-2);
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
        flowLayout2.setVgap(3);
        flowLayout2.setHgap(-10);
        pane.setLayout(flowLayout2);
        NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        for (int i = 0; i < 10; i++) {
            final int a = i;
            panels[i] = new JPanel();
            panels[i].setLayout(flowLayout);
            panels[i].setPreferredSize(new Dimension(600, 26));
            //panels[i].setBorder(new LineBorder(Color.gray));
            if (i == 9) {
                labels[i] = new JLabel("步骤10");
            } else {
                labels[i] = new JLabel("步骤0" + (i + 1));
            }
            checkBoxs[i] = new JCheckBox("启用");
            checkBoxs[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JCheckBox box = (JCheckBox) e.getSource();
                    if (box.isSelected()) {
                        DataSetInts[1][4 + a * 6] = 1;
                    } else {
                        DataSetInts[1][4 + a * 6] = 0;
                    }
                }
            });
            if (DataSetInts[1][4 + i * 6] == 1) {
                checkBoxs[i].setSelected(true);
            } else {
                checkBoxs[i].setSelected(false);
            }
            panels[i].add(labels[i]);
            panels[i].add(checkBoxs[i]);
            for (int j = 0; j < 3; j++) {
                final int jj = j;
                //comboBoxs[i][j] = new JComboBox(new String[]{"空"});
                comboBoxs[i][j] = new JComboBox();
                comboBoxs[i][j].addItem("空");
                for (int n = 0; n < table.getRowCount(); n++) {
                    boolean b = (boolean) table.getValueAt(n, 0);
                    if (b) {
                        comboBoxs[i][j].addItem(table.getValueAt(n, 2).toString());
                    }
                }
                comboBoxs[i][j].addItem("多灯运行1");
                comboBoxs[i][j].addItem("多灯运行2");
                comboBoxs[i][j].setPreferredSize(new Dimension(92, 30));
                if (DataSetInts[1][5 + j + i * 6] < comboBoxs[i][j].getItemCount()) {
                    comboBoxs[i][j].setSelectedIndex(DataSetInts[1][5 + j + i * 6]);
                } else {
                    DataSetInts[1][5 + j + i * 6] = 0;
                    comboBoxs[i][j].setSelectedIndex(0);
                }
                comboBoxs[i][j].addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent e) {
                        JComboBox box = (JComboBox) e.getSource();
                        if (ItemEvent.SELECTED == e.getStateChange()) {
                            DataSetInts[1][5 + jj + a * 6] = box.getSelectedIndex();
                        }
                    }
                });
                panels[i].add(comboBoxs[i][j]);
            }
            pane.add(panels[i]);
        }
    }

    void setP5(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(-1);
        pane.setLayout(flowLayout);

        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(580, 30));
        //p1.setBorder(new LineBorder(Color.gray));
        p1.setLayout(flowLayout);
        final JCheckBox checkBox = new JCheckBox("持续运行总开关");
        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkBox.isSelected()) {
                    DataSetInts[0][64] = 1;
                } else {
                    DataSetInts[0][64] = 0;
                }
            }
        });
        if (DataSetInts[0][64] == 1) {
            checkBox.setSelected(true);
        } else {
            checkBox.setSelected(false);
        }
        p1.add(checkBox);

        pane.add(p1);

        JPanel p2 = new JPanel();
        p2.setLayout(flowLayout);
        p2.setPreferredSize(new Dimension(580, 30));
        //p2.setBorder(new LineBorder(Color.gray));

        p2.add(new JLabel(" 颜色切换模式:"));
        final JComboBox box3 = new JComboBox();
        box3.addItem("硬切");
        box3.addItem("渐变");
        p2.add(box3);
        final JLabel label2 = new JLabel("渐变速度:");
        final JComboBox box4 = new JComboBox();
        box4.addItem("特慢");
        box4.addItem("慢");
        box4.addItem("普快");
        box4.addItem("快");
        box4.addItem("特快");
        p2.add(label2);
        p2.add(box4);
        label2.setVisible(false);
        box4.setVisible(false);

        pane.add(p2);

        box3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    DataSetInts[0][65] = box3.getSelectedIndex();
                    if ("渐变".equals(e.getItem().toString())) {
                        label2.setVisible(true);
                        box4.setVisible(true);
                    } else {
                        label2.setVisible(false);
                        box4.setVisible(false);
                    }
                }
            }
        });
        box4.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    DataSetInts[0][66] = box4.getSelectedIndex();
                }
            }
        });
        box3.setSelectedIndex(DataSetInts[0][65]);
        box4.setSelectedIndex(DataSetInts[0][66]);

        JPanel p3 = new JPanel();
        p3.setLayout(flowLayout);
        p3.setPreferredSize(new Dimension(580, 170));
        p3.setBorder(new LineBorder(Color.gray));
        pane.add(p3);

        NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        String string = "";
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBox box = (JCheckBox) e.getSource();
                if (box.isSelected()) {
                    DataSetInts[0][Integer.valueOf(box.getName()).intValue() + 67] = 1;
                } else {
                    DataSetInts[0][Integer.valueOf(box.getName()).intValue() + 67] = 0;
                }
            }
        };
        JCheckBox box1 = new JCheckBox("多灯运行1");
        JCheckBox box2 = new JCheckBox("多灯运行2");
        box1.setName("30");
        box2.setName("31");
        if (DataSetInts[0][30 + 67] == 0) {
            box1.setSelected(false);
        } else {
            box1.setSelected(true);
        }
        if (DataSetInts[0][31 + 67] == 0) {
            box2.setSelected(false);
        } else {
            box2.setSelected(true);
        }
        box1.addActionListener(listener);
        box2.addActionListener(listener);
        p3.add(box1);
        p3.add(box2);
        for (int n = 0; n < table.getRowCount(); n++) {
            boolean b = (boolean) table.getValueAt(n, 0);
            if (b) {
                string = table.getValueAt(n, 2).toString();
                JCheckBox box = new JCheckBox(string);
                box.setName("" + n);
                if (DataSetInts[0][n + 67] == 0) {
                    box.setSelected(false);
                } else {
                    box.setSelected(true);
                }
                box.addActionListener(listener);
                p3.add(box);
            }
        }
    }

    void setP6(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(-1);
        pane.setLayout(flowLayout);

        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(380, 30));
        //p1.setBorder(new LineBorder(Color.gray));
        p1.setLayout(flowLayout);
        final JCheckBox checkBox = new JCheckBox("持续运行总开关");
        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkBox.isSelected()) {
                    DataSetInts[1][64] = 1;
                } else {
                    DataSetInts[1][64] = 0;
                }
            }
        });
        if (DataSetInts[1][64] == 1) {
            checkBox.setSelected(true);
        } else {
            checkBox.setSelected(false);
        }
        p1.add(checkBox);

        pane.add(p1);

        JPanel p2 = new JPanel();
        p2.setLayout(flowLayout);
        p2.setPreferredSize(new Dimension(380, 30));
        //p2.setBorder(new LineBorder(Color.gray));

        p2.add(new JLabel(" 颜色切换模式:"));
        final JComboBox box3 = new JComboBox();
        box3.addItem("硬切");
        box3.addItem("渐变");
        p2.add(box3);
        final JLabel label2 = new JLabel("渐变速度:");
        final JComboBox box4 = new JComboBox();
        box4.addItem("特慢");
        box4.addItem("慢");
        box4.addItem("普快");
        box4.addItem("快");
        box4.addItem("特快");
        p2.add(label2);
        p2.add(box4);
        label2.setVisible(false);
        box4.setVisible(false);
        pane.add(p2);

        box3.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    DataSetInts[1][65] = box3.getSelectedIndex();
                    if ("渐变".equals(e.getItem().toString())) {
                        label2.setVisible(true);
                        box4.setVisible(true);
                    } else {
                        label2.setVisible(false);
                        box4.setVisible(false);
                    }
                }
            }
        });
        box4.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (ItemEvent.SELECTED == e.getStateChange()) {
                    DataSetInts[1][66] = box4.getSelectedIndex();
                }
            }
        });
        box3.setSelectedIndex(DataSetInts[1][65]);
        box4.setSelectedIndex(DataSetInts[1][66]);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(380, 170));
        JPanel p3 = new JPanel();
        p3.setLayout(flowLayout);
        //p3.setBorder(new LineBorder(Color.gray));
        scrollPane.setViewportView(p3);
        pane.add(scrollPane);

        NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        String string = "";
        int cnt = 0;
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JCheckBox box = (JCheckBox) e.getSource();
                if (box.isSelected()) {
                    DataSetInts[1][Integer.valueOf(box.getName()).intValue() + 67] = 1;
                } else {
                    DataSetInts[1][Integer.valueOf(box.getName()).intValue() + 67] = 0;
                }
            }
        };
        JCheckBox box1 = new JCheckBox("多灯运行1");
        JCheckBox box2 = new JCheckBox("多灯运行2");
        box1.setName("30");
        box2.setName("31");
        if (DataSetInts[1][30 + 67] == 0) {
            box1.setSelected(false);
        } else {
            box1.setSelected(true);
        }
        if (DataSetInts[1][31 + 67] == 0) {
            box2.setSelected(false);
        } else {
            box2.setSelected(true);
        }
        box1.addActionListener(listener);
        box2.addActionListener(listener);
        p3.add(box1);
        p3.add(box2);
        for (int n = 0; n < table.getRowCount(); n++) {
            boolean b = (boolean) table.getValueAt(n, 0);
            if (b) {
                string = table.getValueAt(n, 2).toString();
                JCheckBox box = new JCheckBox(string);
                box.setName("" + n);
                if (DataSetInts[1][n + 67] == 0) {
                    box.setSelected(false);
                } else {
                    box.setSelected(true);
                }
                box.addActionListener(listener);
                p3.add(box);
            }
        }
        if (cnt > 20) {
            int a = cnt - 20;
            a = a / 3;
            if (a % 3 != 0) {
                a = a + 1;
            }
            p3.setPreferredSize(new Dimension(350, 160 + 20 * a));
        } else {
            p3.setPreferredSize(new Dimension(350, 160));
        }

    }
}
