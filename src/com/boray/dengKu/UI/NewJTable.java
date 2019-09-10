package com.boray.dengKu.UI;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class NewJTable extends JTable implements Cloneable{
	private int r=0,c=0;
	private int oldR=0,oldC=0;
	private boolean yes = false;
	private int type;
	public NewJTable(Object[][] data, String[] name) {
		super(data,name);
	}
	/*public JTable clone(){
		JTable table = null;
		try {
			table = (JTable) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return table;
	}*/
	public NewJTable(DefaultTableModel model,int type){
		super(model);
		this.type = type;
		setUI(BasicTableUI.createUI(this));
		setShowGrid(true);
		((DefaultTableCellRenderer)getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
		
		
		/*setColumnSelectionAllowed (true);  
        setRowSelectionAllowed (true);  */
        //final JTableHeader header = getTableHeader();  
        /*final JTable table = this;
        //表头增加监听
        addMouseListener(new MouseAdapter() {  

                public void mouseReleased (MouseEvent e) {  
                    if (! e.isShiftDown())  
                    	clearSelection();  
                    
                    //获取点击的列索引  
                    int pick = table.columnAtPoint(e.getPoint());  
                    int row = table.rowAtPoint(e.getPoint());  
                    //设置选择模型  
                    addRowSelectionInterval(row, row);
                    addColumnSelectionInterval (pick, pick);  
                }  
            }); */
	}
	public final void setR_C(int r,int c,boolean b){
		this.r = r;
		this.c = c;
		yes = b;
	}
	public boolean isCellEditable(int row, int col) {
		if (col==0 && type == 9) {
			return true;
		}
		return false;
	}
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {

		  Component component = super.prepareRenderer(renderer, row, column);
		 // component.setForeground(Color.black);
		 /* if (column==1) {
			  component.setForeground(Color.gray);
		  }
		  if (row % 2 == 0) {  
          	//cell.setBackground(new Color(238,238,238)); //设置奇数行底色  
			  //component.setBackground(new Color(225,225,225));
			  component.setBackground(new Color(218,218,218));
          } else {  
          	component.setBackground(Color.white); //设置偶数行底色  
          }*/
		  return component;
	}
	
	/*public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		  Component component = super.prepareRenderer(renderer, row, column);
		  if (row == r && yes) {
			  //component.setBackground(new Color(182,202,234));
			  component.setBackground(new Color(196,196,196));
			  component.setFocusable(false);
			  //yes = false;
			  } else {
				  component.setBackground(Color.white);
			}
			return component;
	}*/
}
