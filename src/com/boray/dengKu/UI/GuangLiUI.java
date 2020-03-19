package com.boray.dengKu.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.boray.Data.ChannelName;
import com.boray.Data.Data;
import com.boray.Listener.DengKuDataListener;
import com.boray.dengKu.Entity.BlackOutEntity;
import com.boray.dengKu.Entity.SpeedEntity;
import com.boray.dengKu.Listener.ChannelAndSpeedItemListener;
import com.boray.dengKu.Listener.ChannelItemListener;
import com.boray.mainUi.MainUi;

public class GuangLiUI implements ActionListener {
    private NewJTable table;
    private JComboBox[] channelBoxs_L;
    private JComboBox[] channelBoxs_R;
    private JLabel[] channeLabels_L;
    private JLabel[] channeLabels_R;
    private JTextField versionField;
    private int x = 0;

    public void show(JPanel pane) {
        FlowLayout flowLayout6 = new FlowLayout(FlowLayout.LEFT);
        flowLayout6.setVgap(0);
        pane.setLayout(flowLayout6);
        pane.setBorder(new LineBorder(Color.gray));
        pane.setPreferredSize(new Dimension(902, 588));

        JScrollPane leftPane = new JScrollPane();
        //leftPane.setBorder(new LineBorder(Color.black));
        leftPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        leftPane.setPreferredSize(new Dimension(132, 550));

        Object[][] data = {};
        String[] s = {"序号", "灯库名称"};
        DefaultTableModel model = new DefaultTableModel(data, s);
        table = new NewJTable(model, 0);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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
                        //cell.setBackground(new Color(237,243,254));
                        //cell.setBackground(new Color(225,225,225));
                        //cell.setBackground(new Color(243,243,243));
                        cell.setBackground(Color.white);
                        cell.setForeground(Color.black);
                    } else {
                        cell.setBackground(Color.white); //设置偶数行底色
                        cell.setForeground(Color.black);
                    }
                } else {
                    //cell.setBackground(new Color(56,117,215));
                    cell.setBackground(new Color(85, 160, 255));
                    cell.setForeground(Color.white);
                }
                return cell;
            }
        };
        for (int i = 0; i < s.length; i++) {
            table.getColumn(table.getColumnName(i)).setCellRenderer(cellRenderer);
        }
        table.setSelectionBackground(new Color(56, 117, 215));
        /////////////////////////
        table.getTableHeader().setUI(new BasicTableHeaderUI());
        table.getTableHeader().setReorderingAllowed(false);
        table.setOpaque(false);
        //table.setFocusable(false);
        table.setFont(new Font(Font.SERIF, Font.BOLD, 14));
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(102);
        table.setRowHeight(30);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int select = table.getSelectedRow();
                if (select > -1 && !e.getValueIsAdjusting()) {
                    versionField.setText(Data.DengKuVersionList.get(select).toString());
                    int c = Integer.valueOf(Data.DengKuChannelCountList.get(select).toString()).intValue();
                    HashMap map = (HashMap) Data.DengKuList.get(select);
                    ItemListener listener = (ItemListener) MainUi.map.get("ChannelItemListener");
                    for (int j = 0; j < 16; j++) {
                        channelBoxs_L[j].removeItemListener(listener);
                        channelBoxs_R[j].removeItemListener(listener);
                    }
                    if (c > 16) {
                        for (int i = 0; i < 16; i++) {
                            channelBoxs_L[i].setEnabled(true);
							/*channelBoxs_L[i].setVisible(true);
							channeLabels_L[i].setVisible(true);*/
                            channelBoxs_L[i].setSelectedItem(map.get(channelBoxs_L[i].getName()).toString());
                        }
                        for (int i = 0; i < c - 16; i++) {
                            channelBoxs_R[i].setEnabled(true);
							/*channelBoxs_R[i].setVisible(true);
							channeLabels_R[i].setVisible(true);*/
                            channelBoxs_R[i].setSelectedItem(map.get(channelBoxs_R[i].getName()).toString());
                        }
                        for (int i = c - 16; i < channelBoxs_R.length; i++) {
                            channelBoxs_R[i].setEnabled(false);
							/*channelBoxs_R[i].setVisible(false);
							channeLabels_R[i].setVisible(false);*/
                        }
                    } else {
                        for (int i = 0; i < channelBoxs_R.length; i++) {
                            channelBoxs_R[i].setEnabled(false);
							/*channelBoxs_R[i].setVisible(false);
							channeLabels_R[i].setVisible(false);*/
                        }
                        for (int i = 0; i < c; i++) {
                            channelBoxs_L[i].setEnabled(true);
							/*channelBoxs_L[i].setVisible(true);
							channeLabels_L[i].setVisible(true);*/
                            channelBoxs_L[i].setSelectedItem(map.get(channelBoxs_L[i].getName()).toString());
                        }
                        for (int i = c; i < channelBoxs_L.length; i++) {
                            channelBoxs_L[i].setEnabled(false);
							/*channelBoxs_L[i].setVisible(false);
							channeLabels_L[i].setVisible(false);*/
                        }
                    }
                    for (int j = 0; j < 16; j++) {
                        channelBoxs_L[j].addItemListener(listener);
                        channelBoxs_R[j].addItemListener(listener);
                    }

                    Map map2 = (Map) Data.dengKuBlackOutAndSpeedList.get(select);
                    JComboBox[] box1 = (JComboBox[]) MainUi.map.get("channelBox1");
                    JComboBox[] box2 = (JComboBox[]) MainUi.map.get("channelBox2");
                    JComboBox[] box3 = (JComboBox[]) MainUi.map.get("channelBox3");
                    JComboBox[] box4 = (JComboBox[]) MainUi.map.get("speedBox1");
                    JComboBox[] box5 = (JComboBox[]) MainUi.map.get("speedBox2");
                    JComboBox[] box6 = (JComboBox[]) MainUi.map.get("speedBox3");
                    JComboBox[] box7 = (JComboBox[]) MainUi.map.get("speedBox4");
                    if (map2 == null) {
                        for (int i = 0; i < 4; i++) {
                            box1[i].setSelectedIndex(0);
                            box2[i].setSelectedIndex(0);
                            box3[i].setSelectedIndex(0);
                        }

                        for (int i = 0; i < 3; i++) {
                            box4[i].setSelectedIndex(0);
                            box5[i].setSelectedIndex(0);
                            box6[i].setSelectedIndex(0);
                            box7[i].setSelectedIndex(0);
                        }
                    } else {
                        BlackOutEntity blackOutEntity = (BlackOutEntity) map2.get("blackOutEntity");
                        SpeedEntity speedEntity = (SpeedEntity) map2.get("speedEntity");
                        for (int i = 0; i < 4; i++) {
                            box1[i].setSelectedItem(blackOutEntity.getC(i)[0]);
                            box2[i].setSelectedItem(blackOutEntity.getC(i)[1]);
                            box3[i].setSelectedItem(blackOutEntity.getC(i)[2]);
                        }
                        for (int i = 0; i < 3; i++) {
                            box4[i].setSelectedItem(speedEntity.getS(i)[0]);
                            box5[i].setSelectedItem(speedEntity.getS(i)[1]);
                            box6[i].setSelectedItem(speedEntity.getS(i)[2]);
                            box7[i].setSelectedItem(speedEntity.getS(i)[3]);
                        }
                    }
                }
            }
        });
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getX() == x) {
                    if (mouseEvent.getButton() == 1 && mouseEvent.getClickCount() == 2) {
                        new dkChangeDialog().show(table);
                    }
                }
                x = mouseEvent.getX();
            }
        });
        MainUi.map.put("table_DkGl", table);
        leftPane.setViewportView(table);


        JPanel rightPane = new JPanel();
        rightPane.setPreferredSize(new Dimension(380, 554));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(-4);
        rightPane.setLayout(flowLayout);
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "通道定义", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        rightPane.setBorder(tb);
        JPanel Lpane = new JPanel();
        JPanel Rpane = new JPanel();
        //Lpane.setBorder(BorderFactory.createEmptyBorder(-8,0,0,0));
        //Rpane.setBorder(BorderFactory.createEmptyBorder(-8,0,0,0));
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
        flowLayout2.setVgap(2);
        FlowLayout flowLayout3 = new FlowLayout(FlowLayout.LEFT);
        flowLayout3.setVgap(2);
        Lpane.setLayout(flowLayout2);
        Rpane.setLayout(flowLayout3);
		/*Lpane.setBorder(new LineBorder(Color.black));
		Rpane.setBorder(new LineBorder(Color.black));*/
        Lpane.setPreferredSize(new Dimension(180, 590));
        Rpane.setPreferredSize(new Dimension(180, 590));
        channelBoxs_L = new JComboBox[16];
        channelBoxs_R = new JComboBox[16];
        channeLabels_L = new JLabel[16];
        channeLabels_R = new JLabel[16];
        MainUi.map.put("lamp_1_To_16", channelBoxs_L);
        MainUi.map.put("lamp_17_To_32", channelBoxs_R);
		/*mainUI.map.put("lamp_1_To_16_label", channeLabels_L);
		mainUI.map.put("lamp_17_To_32_label", channeLabels_R);*/
        ChannelItemListener listener = new ChannelItemListener();
        MainUi.map.put("ChannelItemListener", listener);
        for (int i = 0; i < channelBoxs_L.length; i++) {
            channeLabels_L[i] = new JLabel("" + (1 + i), JLabel.CENTER);
            channeLabels_L[i].setPreferredSize(new Dimension(58, 22));
            channeLabels_L[i].setBackground(new Color(243, 243, 243));
            channeLabels_L[i].setOpaque(true);
            channeLabels_L[i].setBorder(new LineBorder(new Color(192, 192, 192)));
            channelBoxs_L[i] = new JComboBox();
            channelBoxs_L[i].setPreferredSize(new Dimension(104, 31));
            for (int j = 0; j < ChannelName.names.length; j++) {
                channelBoxs_L[i].addItem(ChannelName.names[j]);
            }
            channeLabels_L[i].setFocusable(false);
            //channeLabels_L[i].setVisible(false);
            channelBoxs_L[i].setFocusable(false);
            channelBoxs_L[i].setEditable(true);
            channelBoxs_L[i].setEnabled(false);
            //channelBoxs_L[i].setVisible(false);
            channelBoxs_L[i].addItemListener(listener);
            channelBoxs_L[i].setName("L" + i);
            Lpane.add(channeLabels_L[i]);
            Lpane.add(channelBoxs_L[i]);
        }
        for (int i = 0; i < channelBoxs_R.length; i++) {
            channeLabels_R[i] = new JLabel("" + (17 + i), JLabel.CENTER);
            channeLabels_R[i].setPreferredSize(new Dimension(58, 22));
            channeLabels_R[i].setBackground(new Color(243, 243, 243));
            channeLabels_R[i].setOpaque(true);
            channeLabels_R[i].setBorder(new LineBorder(new Color(192, 192, 192)));
            channelBoxs_R[i] = new JComboBox();
            channelBoxs_R[i].setPreferredSize(new Dimension(104, 31));
            for (int j = 0; j < ChannelName.names.length; j++) {
                channelBoxs_R[i].addItem(ChannelName.names[j]);
            }
            channeLabels_R[i].setFocusable(false);
            //channeLabels_R[i].setVisible(false);
            channelBoxs_R[i].setFocusable(false);
            channelBoxs_R[i].setEditable(true);
            channelBoxs_R[i].setEnabled(false);
            //channelBoxs_R[i].setVisible(false);
            channelBoxs_R[i].addItemListener(listener);
            channelBoxs_R[i].setName("R" + i);
            Rpane.add(channeLabels_R[i]);
            Rpane.add(channelBoxs_R[i]);
        }
        rightPane.add(Lpane);
        //rightPane.add(new JLabel("      "));
        rightPane.add(Rpane);

        JPanel headPane = new JPanel();
        headPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        headPane.setPreferredSize(new Dimension(620, 30));
        //headPane.setBorder(new LineBorder(Color.gray));
        JButton newBtn = new JButton("新建");
        JButton delBtn = new JButton("删除");
        JButton exportBtn = new JButton("导出");
        JButton importBtn = new JButton("导入");
        JButton customBtn = new JButton("自定义通道");
        newBtn.setPreferredSize(new Dimension(68, 32));
        delBtn.setPreferredSize(new Dimension(68, 32));
        exportBtn.setPreferredSize(new Dimension(68, 32));
        importBtn.setPreferredSize(new Dimension(68, 32));
        customBtn.setPreferredSize(new Dimension(100,32));
        DengKuDataListener listener2 = new DengKuDataListener();
        exportBtn.addActionListener(listener2);
        importBtn.addActionListener(listener2);
        customBtn.addActionListener(listener2);
        newBtn.addActionListener(this);
        delBtn.addActionListener(this);
        newBtn.setFocusable(false);
        delBtn.setFocusable(false);
        exportBtn.setFocusable(false);
        importBtn.setFocusable(false);
        headPane.add(newBtn);
        headPane.add(delBtn);
        versionField = new JTextField(6);
        MainUi.map.put("versionField", versionField);
        versionField.setEnabled(false);
        headPane.add(new JLabel("              库版本"));
        headPane.add(versionField);
        JPanel NULLPane = new JPanel();
        NULLPane.setPreferredSize(new Dimension(242, 30));
        //headPane.add(NULLPane);
        headPane.setBorder(BorderFactory.createEmptyBorder(-5, 0, 0, 0));

        headPane.add(exportBtn);
        headPane.add(importBtn);
//        headPane.add(customBtn);
        pane.add(headPane);
        JLabel labe = new JLabel(" ");
        labe.setPreferredSize(new Dimension(188, 28));
        pane.add(labe);
        pane.add(leftPane);
        pane.add(rightPane);

        JPanel lastPane = new JPanel();
        setLastPane(lastPane);
        pane.add(lastPane);
    }

    private void setLastPane(JPanel pane) {
        pane.setLayout(new BorderLayout());
        //pane.setBorder(new LineBorder(Color.black));
        pane.setPreferredSize(new Dimension(372, 554));

        JPanel topPane = new JPanel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setHgap(-4);
        flowLayout.setVgap(40);
        topPane.setLayout(flowLayout);
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "熄灯通道", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        topPane.setBorder(tb);
        topPane.setPreferredSize(new Dimension(354, 350));
        ChannelAndSpeedItemListener listener = new ChannelAndSpeedItemListener();
        JLabel[] labels = new JLabel[4];
        JComboBox[] boxs = new JComboBox[4];
        MainUi.map.put("channelBox1", boxs);

        JLabel[] labels2 = new JLabel[4];
        JComboBox[] boxs2 = new JComboBox[4];
        MainUi.map.put("channelBox2", boxs2);

        JLabel[] labels3 = new JLabel[4];
        JComboBox[] boxs3 = new JComboBox[4];
        MainUi.map.put("channelBox3", boxs3);

        for (int i = 0; i < 4; i++) {
            labels[i] = new JLabel("通道号");
            labels2[i] = new JLabel("     灭灯值");
            labels3[i] = new JLabel("     最亮值");
            boxs[i] = new JComboBox();
            boxs[i].addItem("无");
            for (int j = 1; j < 33; j++) {
                boxs[i].addItem(String.valueOf(j));
            }
            boxs[i].addItem("所有");
            boxs2[i] = new JComboBox();
            boxs3[i] = new JComboBox();
            for (int j = 0; j < 256; j++) {
                boxs2[i].addItem(String.valueOf(j));
                boxs3[i].addItem(String.valueOf(j));
            }
            boxs3[i].setSelectedIndex(0);
            boxs[i].addItemListener(listener);
            boxs2[i].addItemListener(listener);
            boxs3[i].addItemListener(listener);
            boxs[i].setName("通道" + i);
            boxs2[i].setName("灭灯" + i);
            boxs3[i].setName("最亮" + i);
            topPane.add(labels[i]);
            topPane.add(boxs[i]);
            topPane.add(labels2[i]);
            topPane.add(boxs2[i]);
            topPane.add(labels3[i]);
            topPane.add(boxs3[i]);
        }

        JPanel bottomPane = new JPanel();
        FlowLayout flowLayout2 = new FlowLayout(flowLayout.LEFT);
        flowLayout2.setHgap(-4);
        flowLayout2.setVgap(20);
        bottomPane.setLayout(flowLayout2);
        TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "速度通道", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        bottomPane.setBorder(tb1);
        bottomPane.setPreferredSize(new Dimension(354, 200));
        JLabel[] labels4 = new JLabel[3];
        JComboBox[] boxs4 = new JComboBox[3];
        MainUi.map.put("speedBox1", boxs4);

        JLabel[] labels5 = new JLabel[3];
        JComboBox[] boxs5 = new JComboBox[3];
        MainUi.map.put("speedBox2", boxs5);

        JLabel[] labels6 = new JLabel[3];
        JComboBox[] boxs6 = new JComboBox[3];
        MainUi.map.put("speedBox3", boxs6);

        JLabel[] labels7 = new JLabel[3];
        JComboBox[] boxs7 = new JComboBox[3];
        MainUi.map.put("speedBox4", boxs7);

        for (int i = 0; i < 3; i++) {
            labels4[i] = new JLabel("通道" + (i + 1));
            labels5[i] = new JLabel("最小值");
            labels6[i] = new JLabel("最大值");
            labels7[i] = new JLabel("方向");
            boxs4[i] = new JComboBox();
            boxs4[i].addItem("无");
            for (int j = 1; j < 33; j++) {
                boxs4[i].addItem(String.valueOf(j));
            }
            boxs5[i] = new JComboBox();
            boxs6[i] = new JComboBox();
            for (int j = 0; j < 256; j++) {
                boxs5[i].addItem(String.valueOf(j));
                boxs6[i].addItem(String.valueOf(j));
            }
            boxs6[i].setSelectedIndex(0);
            boxs7[i] = new JComboBox();
            boxs7[i].addItem("正向");
            boxs7[i].addItem("反向");

            boxs4[i].setPreferredSize(new Dimension(52, 30));
            boxs5[i].setPreferredSize(new Dimension(58, 30));
            boxs6[i].setPreferredSize(new Dimension(58, 30));
            boxs7[i].setPreferredSize(new Dimension(64, 30));

            boxs4[i].addItemListener(listener);
            boxs5[i].addItemListener(listener);
            boxs6[i].addItemListener(listener);
            boxs7[i].addItemListener(listener);

            boxs4[i].setName("速度" + i);
            boxs5[i].setName("最小" + i);
            boxs6[i].setName("最大" + i);
            boxs7[i].setName("方向" + i);


            bottomPane.add(labels4[i]);
            bottomPane.add(boxs4[i]);
            bottomPane.add(labels5[i]);
            bottomPane.add(boxs5[i]);
            bottomPane.add(labels6[i]);
            bottomPane.add(boxs6[i]);
            bottomPane.add(labels7[i]);
            bottomPane.add(boxs7[i]);
        }

        pane.add(topPane, BorderLayout.NORTH);
        pane.add(bottomPane);
    }

    public void actionPerformed(ActionEvent e) {
        //Data.dengKu_change = true;
        if ("新建".equals(e.getActionCommand())) {
			/*DefaultTableModel model = (DefaultTableModel)table.getModel();
			String[] s = {(table.getRowCount()+1)+"","CD401"};
			model.addRow(s);*/
            new dkCreateDialog().show(table);
        } else {
            int s = table.getSelectedRow();
            if (s > -1) {
                boolean b = false;
                NewJTable table8 = (NewJTable) MainUi.map.get("table_dengJu");
                for (int i = 0; i < table8.getRowCount(); i++) {
                    int a = Integer.valueOf(table8.getValueAt(i, 3).toString().split("#")[0].substring(2)).intValue() - 1;
                    if (a == s) {
                        b = true;
                        break;
                    }
                }
                if (b) {
                    JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "该灯库已被关联，不能删除！", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                //清空素材，避免灯库通道出现错误
                Data.suCaiMap.remove(table.getValueAt(s, 1));
                Data.suCaiNameMap.remove(table.getValueAt(s, 1));
                for (int i = 0; i < 30; i++) {
                    Data.SuCaiObjects[s][i] = null;
                }
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.removeRow(s);
				/*Data.dengKuName.remove(s);
				Data.dengKuMapList.remove(s);*/
                Data.dengKuBlackOutAndSpeedList.remove(s);
                Data.DengKuChannelCountList.remove(s);
                Data.DengKuList.remove(s);
                Data.DengKuVersionList.remove(s);
                if (table.getRowCount() > s) {
                    table.setRowSelectionInterval(s, s);
                } else if (table.getRowCount() > 0) {
                    table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
                } else if (table.getRowCount() == 0) {
                    for (int i = 0; i < 16; i++) {
						/*channeLabels_L[i].setVisible(false);
						channelBoxs_L[i].setVisible(false);
						channeLabels_R[i].setVisible(false);
						channelBoxs_R[i].setVisible(false);*/
                        channeLabels_L[i].setEnabled(false);
                        channelBoxs_L[i].setEnabled(false);
                        channeLabels_R[i].setEnabled(false);
                        channelBoxs_R[i].setEnabled(false);
                    }
                }
                for (int i = 0; i < table.getRowCount(); i++) {
                    table.setValueAt("" + (i + 1), i, 0);
                    for (int j = 0; j < table8.getRowCount(); j++) {
                        String string = table8.getValueAt(j, 3).toString().split("#")[1];
                        if (table.getValueAt(i, 1).toString().equals(string)) {
                            table8.setValueAt("ID" + (i + 1) + "#" + string, j, 3);
                        }
                    }
                }
            }
        }
    }
}
