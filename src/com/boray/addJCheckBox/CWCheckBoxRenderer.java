package com.boray.addJCheckBox;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;

public class CWCheckBoxRenderer extends JCheckBox implements TableCellRenderer {
	  //~ Static fields/initializers -------------------------------------------------------------------------------------

	  private static final long serialVersionUID = 1L;

	  //~ Instance fields ------------------------------------------------------------------------------------------------

	  Border border = new EmptyBorder(1, 2, 1, 2);

	  //~ Constructors ---------------------------------------------------------------------------------------------------

	  public CWCheckBoxRenderer() {
	    super();
	    setOpaque(true);
	    setHorizontalAlignment(SwingConstants.CENTER);
	  }

	  //~ Methods --------------------------------------------------------------------------------------------------------

	   public Component getTableCellRendererComponent(
	    JTable  table,
	    Object  value,
	    boolean isSelected,
	    boolean hasFocus,
	    int     row,
	    int     column) {
	    if (value instanceof Boolean) {
	      setSelected(((Boolean) value).booleanValue());
	      //setEnabled(table.isCellEditable(row, column));
	      //setForeground(table.getForeground());
	      //setBackground(table.getBackground());
	    }
	    if (row % 2 == 0) {  
           	//cell.setBackground(new Color(238,238,238)); //设置奇数行底色  
				//this.setBackground(new Color(225,225,225));
	    	this.setBackground(new Color(237,243,254));
           } else {  
        	   this.setBackground(Color.white); //设置偶数行底色  
           }  
	    return this;
	  }
	}