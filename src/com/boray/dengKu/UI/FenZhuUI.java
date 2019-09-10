package com.boray.dengKu.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.boray.Data.Data;
import com.boray.addJCheckBox.CWCheckBoxRenderer;
import com.boray.addJCheckBox.CheckBoxCellEditor;
import com.boray.dengKu.Listener.LightGroupsActionListener;
import com.boray.mainUi.MainUi;

public class FenZhuUI implements ActionListener {
    public void show(JPanel pane) {
        pane.setBorder(new LineBorder(Color.gray));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(0);
        pane.setLayout(flowLayout);
        pane.setPreferredSize(new Dimension(902, 588));

        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();
        JPanel p4 = new JPanel();
        setP1(p1);
        setP2(p2);
        setP3(p3);
        setP4(p4);
        pane.add(p1);
        pane.add(p2);
        pane.add(p3);
        pane.add(p4);
    }

    private void setP4(JPanel pane) {
        //pane.setBorder(new LineBorder(Color.gray));
        pane.setPreferredSize(new Dimension(190, 584));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(26);
        flowLayout.setHgap(-2);
        pane.setLayout(flowLayout);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(190, 558));
        Object[][] data = {};
        String[] title = {"所有灯具"};
        DefaultTableModel model = new DefaultTableModel(data, title);
        NewJTable table = new NewJTable(model, 3);
        MainUi.map.put("allLightTable", table);
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
        table.getColumnModel().getColumn(0).setPreferredWidth(182);
        table.setRowHeight(28);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
				/*if (mouseEvent.getX() == x) {
					if (mouseEvent.getButton() == 1 && mouseEvent.getClickCount() == 2) {
						new DengJuChangeDialog().show();
					}
				}
				x = mouseEvent.getX();*/
            }
        });
        scrollPane.setViewportView(table);
        pane.add(scrollPane);
    }

    private void setP3(JPanel pane) {
        //pane.setBorder(new LineBorder(Color.gray));
        pane.setPreferredSize(new Dimension(80, 584));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(26);
        flowLayout.setHgap(-2);
        pane.setLayout(flowLayout);

        JButton btn1 = new JButton(">>");
        JButton btn2 = new JButton("<<");
        JButton btn3 = new JButton("all>>");
        JButton btn4 = new JButton("all<<");
        LightGroupsActionListener listener = new LightGroupsActionListener();
        btn1.addActionListener(listener);
        btn2.addActionListener(listener);
        btn3.addActionListener(listener);
        btn4.addActionListener(listener);
        JPanel NULLPane = new JPanel();
        NULLPane.setPreferredSize(new Dimension(70, 140));
        pane.add(NULLPane);
        pane.add(btn1);
        pane.add(btn2);
        pane.add(btn3);
        pane.add(btn4);
    }

    private void setP2(JPanel pane) {
        //pane.setBorder(new LineBorder(Color.gray));
        pane.setPreferredSize(new Dimension(190, 584));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(26);
        flowLayout.setHgap(-2);
        pane.setLayout(flowLayout);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(190, 558));
        Object[][] data = {};
        String[] title = {"组内灯具"};
        DefaultTableModel model = new DefaultTableModel(data, title);
        NewJTable table = new NewJTable(model, 4);
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
        table.getColumnModel().getColumn(0).setPreferredWidth(182);
        table.setRowHeight(28);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);


        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
				/*if (mouseEvent.getX() == x) {
					if (mouseEvent.getButton() == 1 && mouseEvent.getClickCount() == 2) {
						new DengJuChangeDialog().show();
					}
				}
				x = mouseEvent.getX();*/
            }
        });
        MainUi.map.put("InGroupsTable", table);
        scrollPane.setViewportView(table);
        pane.add(scrollPane);
    }

    private void setP1(JPanel pane) {
        //p1.setBorder(new LineBorder(Color.gray));
        pane.setPreferredSize(new Dimension(280, 584));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(-2);
        flowLayout.setHgap(-2);
        pane.setLayout(flowLayout);
        pane.add(new JLabel("灯具分组"));
        JButton btn1 = new JButton("添加");
        JButton btn2 = new JButton("删除");
        btn1.addActionListener(this);
        btn2.addActionListener(this);
        btn1.setFocusable(false);
        btn2.setFocusable(false);
        pane.add(new JLabel("               "));
        pane.add(btn1);
        pane.add(btn2);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(282, 558));
        Object[][] data = {};
        String[] title = {"启用", "ID", "组别名称"};
        DefaultTableModel model = new DefaultTableModel(data, title);
        final NewJTable table = new NewJTable(model, 9) {
            public boolean isCellEditable(int row, int col) {
                if (col == 0 || col == 2) {
                    return true;
                }
                return false;
            }
        };
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
        for (int i = 0; i < title.length; i++) {
            table.getColumn(table.getColumnName(i)).setCellRenderer(cellRenderer);
        }
        table.setSelectionBackground(new Color(56, 117, 215));
        /////////////////////////
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setUI(new BasicTableHeaderUI());
        table.getTableHeader().setReorderingAllowed(false);
        table.setOpaque(false);
        //table.setFocusable(false);
        table.setFont(new Font(Font.SERIF, Font.BOLD, 14));
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(50);
        table.getColumnModel().getColumn(2).setPreferredWidth(174);
        table.setRowHeight(28);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumn Column0 = table.getColumnModel().getColumn(0);
        Column0.setCellEditor(new CheckBoxCellEditor());
        Column0.setCellRenderer(new CWCheckBoxRenderer());


        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
				/*if (mouseEvent.getX() == x) {
					if (mouseEvent.getButton() == 1 && mouseEvent.getClickCount() == 2) {
						new DengJuChangeDialog().show();
					}
				}
				x = mouseEvent.getX();*/
            }
        });
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int select = table.getSelectedRow();
                if (select > -1 && !e.getValueIsAdjusting()) {
                    TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(select);
                    NewJTable table2 = (NewJTable) MainUi.map.get("InGroupsTable");//组内灯具
                    NewJTable table3 = (NewJTable) MainUi.map.get("allLightTable");//所有灯具
                    DefaultTableModel model2 = (DefaultTableModel) table2.getModel();
                    if (treeSet.size() > 0) {
                        for (int i = table2.getRowCount() - 1; i >= 0; i--) {
                            model2.removeRow(i);
                        }
                        Iterator iterator = treeSet.iterator();
                        while (iterator.hasNext()) {
                            int i = (int) iterator.next();
                            if (i < table3.getRowCount()) {
                                String[] s = {table3.getValueAt(i, 0).toString()};
                                model2.addRow(s);
                            } else {
                                //treeSet.remove(i);
                            }
                        }
                    } else {
                        for (int i = table2.getRowCount() - 1; i >= 0; i--) {
                            model2.removeRow(i);
                        }
                    }
                }
            }
        });
        MainUi.map.put("GroupTable", table);
        scrollPane.setViewportView(table);
        pane.add(scrollPane);
    }

    public void actionPerformed(ActionEvent e) {
        if ("添加".equals(e.getActionCommand())) {
            new AddGroupUI().show();
        } else {
            NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
            int select = table.getSelectedRow();
            if (select != -1) {
                if (select != table.getRowCount() - 1) {
                    JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "组别删除只能从最后一项开始！", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.removeRow(select);
                Data.GroupOfLightList.remove(select);
                for (int i = 0; i < table.getRowCount(); i++) {
                    table.setValueAt(String.valueOf(i + 1), i, 1);
                }
            }
            if (table.getRowCount() > 0) {
                table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
            }
        }
    }
}
