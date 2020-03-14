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
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.boray.Data.Data;
import com.boray.addJCheckBox.CWCheckBoxRenderer;
import com.boray.addJCheckBox.CheckBoxCellEditor;
import com.boray.mainUi.MainUi;

public class PeiZhiUI implements ActionListener{
	private int x;
	public void show(JPanel pane){
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
		JButton btn1 = new JButton("���RDM�ƾ���Ϣ");
		JButton btn2 = new JButton("�½�");
		JButton btn3 = new JButton("ɾ��");
		btn1.setPreferredSize(new Dimension(158,34));
		btn2.setPreferredSize(new Dimension(88,34));
		btn3.setPreferredSize(new Dimension(88,34));
		btn1.setFocusable(false);btn2.setFocusable(false);btn3.setFocusable(false);
		btn1.addActionListener(this);
		btn2.addActionListener(this);
		btn3.addActionListener(this);
		//headPane.add(new JLabel("     "));
		headPane.add(btn1);headPane.add(btn2);headPane.add(btn3);
		
		JScrollPane bodyPane = new JScrollPane();
		setBodyPane(bodyPane);
		
		pane.add(headPane);
		pane.add(bodyPane);
	}
	private void setBodyPane(JScrollPane pane){
		MainUi.map.put("DengJuScrollPane", pane);
		//pane.setBorder(new LineBorder(Color.gray));
		pane.setPreferredSize(new Dimension(890,552));
		
		Object[][] data = {};
		String[] title = {"����","ID","�ƾ�����","�ͺ�","��汾","DMX��ʼ��ַ","ռ��ͨ����"};
		DefaultTableModel model = new DefaultTableModel(data,title);
		NewJTable table = new NewJTable(model,9);
		/////////////////////////
		DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer(){
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
						cell.setBackground(new Color(237,243,254));
						cell.setForeground(Color.black);
		            } else {  
		            	cell.setBackground(Color.white); //����ż���е�ɫ  
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
		table.getColumnModel().getColumn(1).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setPreferredWidth(320);
		table.getColumnModel().getColumn(3).setPreferredWidth(194);
		table.getColumnModel().getColumn(4).setPreferredWidth(88);
        table.setRowHeight(28);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); 
		TableColumn Column0 = table.getColumnModel().getColumn(0);
		Column0.setCellEditor(new CheckBoxCellEditor());
		Column0.setCellRenderer(new CWCheckBoxRenderer());
		
		
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
				if (mouseEvent.getX() == x) {
					if (mouseEvent.getButton() == 1 && mouseEvent.getClickCount() == 2) {
						new DengJuChangeDialog().show();
					}
				}
				x = mouseEvent.getX();
			}
		});
		MainUi.map.put("table_dengJu", table);
		pane.setViewportView(table);
	}
	public void actionPerformed(ActionEvent e) {
		if ("�½�".equals(e.getActionCommand())) {
			new DengJuSetDialog().show();
		} else if("ɾ��".equals(e.getActionCommand())) {
			NewJTable table = (NewJTable)MainUi.map.get("table_dengJu");
			int s = table.getSelectedRow();
			if (s > -1) {
				//Data.dengJu_change = true;
				DefaultTableModel model = (DefaultTableModel)table.getModel();
				int[] selecteds = table.getSelectedRows();
				
				for (int i = 0; i < Data.GroupOfLightList.size(); i++) {
					TreeSet treeSet = (TreeSet)Data.GroupOfLightList.get(i);
					Iterator iterator = treeSet.iterator();
					boolean b = true;
					while (iterator.hasNext() && b) {
						int a = (int) iterator.next();
						for (int j = 0; j < selecteds.length; j++) {
							if (a == selecteds[j]) {
								b = false;
								break;
							} else if (a > selecteds[j]) {
								
							}
						}
					}
					if (!b) {
						JOptionPane.showMessageDialog((JFrame)MainUi.map.get("frame"), "ɾ���ĵƾ��ѱ�������ã�����ɾ����", "��ʾ", JOptionPane.ERROR_MESSAGE);
						return;
					} else {
						//TreeSet treeSet2 = new TreeSet<>();
						
					}
				}
				for (int i = 0; i < Data.GroupOfLightList.size(); i++) {
					TreeSet treeSet = (TreeSet)Data.GroupOfLightList.get(i);
					TreeSet treeSet2 = new TreeSet<>();
					Iterator iterator = treeSet.iterator();
					while (iterator.hasNext()) {
						int a = (int) iterator.next();
						int bef = a;
						for (int j = 0; j < selecteds.length; j++) {
							if (bef > selecteds[j]) {
								a = a - 1;
							}
						}
						treeSet2.add(a);
					}
					Data.GroupOfLightList.set(i, treeSet2);
				}
				
				
				
				for (int i = selecteds.length-1; i >= 0; i--) {
					model.removeRow(selecteds[i]);
				}
				s = selecteds[selecteds.length-1];
				if (table.getRowCount() > s) {
					table.setRowSelectionInterval(s, s);
				} else if (table.getRowCount() > 0) {
					table.setRowSelectionInterval(table.getRowCount()-1, table.getRowCount()-1);
				}
				for (int i = 0; i < table.getRowCount(); i++) {
					table.setValueAt(""+(i+1), i, 1);
				}
			}
		}else if("���RDM�ƾ���Ϣ".equals(e.getActionCommand())){
			new AddRDMDialog().show();
		}
	}
}
