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

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class RdmGuangLiUI implements ActionListener{
	public void show2(JPanel pane){
		pane.setBorder(new LineBorder(Color.gray));
		FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
		flowLayout.setVgap(0);
		pane.setLayout(flowLayout);
		pane.setPreferredSize(new Dimension(902,588));
		
		JPanel headPane = new JPanel();
		FlowLayout flowLayout2 = new FlowLayout(flowLayout.LEFT);
		flowLayout2.setVgap(0);
		headPane.setLayout(flowLayout2);
		headPane.setPreferredSize(new Dimension(890,32));
		//headPane.setBorder(new LineBorder(Color.gray));
		//JButton btn1 = new JButton("添加RDM灯具信息");
		JButton btn2 = new JButton("搜索");
		JButton btn3 = new JButton("退出RDM");
		//btn1.setPreferredSize(new Dimension(158,34));
		btn2.setPreferredSize(new Dimension(88,34));
		btn3.setPreferredSize(new Dimension(88,34));
		//btn1.setFocusable(false);btn2.setFocusable(false);btn3.setFocusable(false);
		btn2.addActionListener(this);
		btn3.addActionListener(this);
		//headPane.add(new JLabel("     "));
		//headPane.add(btn1);
		headPane.add(btn2);headPane.add(btn3);
		
		JScrollPane bodyPane = new JScrollPane();
		setBodyPane(bodyPane);
		
		pane.add(headPane);
		pane.add(bodyPane);
	}
	private void setBodyPane(JScrollPane pane){
		//pane.setBorder(new LineBorder(Color.gray));
		pane.setPreferredSize(new Dimension(890,552));
		
		Object[][] data = {};
		String[] title = {"ID","UID","灯具名称","型号","DMX起始地址","占用通道数","备注","高级设置"};
		DefaultTableModel model = new DefaultTableModel(data,title);
		NewJTable table = new NewJTable(model,2);
		/////////////////////////
		DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer(){
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
						cell.setBackground(new Color(237,243,254));
						cell.setForeground(Color.black);
		            } else {  
		            	cell.setBackground(Color.white); //设置偶数行底色  
		            	cell.setForeground(Color.black);
		            }  
				} else {
					cell.setBackground(new Color(85,160,255));
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
		table.setSelectionBackground(new Color(56,117,215));
		/////////////////////////
		table.getTableHeader().setUI(new BasicTableHeaderUI());
		table.getTableHeader().setReorderingAllowed(false);
		table.setOpaque(false);
		//table.setFocusable(false);
		table.setFont(new Font(Font.SERIF, Font.BOLD, 14));
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(172);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(3).setPreferredWidth(180);
		//table.getColumnModel().getColumn(4).setPreferredWidth(88);
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
		pane.setViewportView(table);
	}
	public void actionPerformed(ActionEvent e) {
		if ("搜索".equals(e.getActionCommand())) {
			
		} else {
			
		}
	}
}
