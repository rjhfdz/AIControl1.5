package com.boray.addJCheckBox;

import java.awt.Color;
import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;

public class CheckBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
	  //~ Static fields/initializers -------------------------------------------------------------------------------------

	  private static final long serialVersionUID = 1L;

	  //~ Instance fields ------------------------------------------------------------------------------------------------

	  protected JCheckBox checkBox;

	  //~ Constructors ---------------------------------------------------------------------------------------------------

	  public CheckBoxCellEditor() {
	    checkBox = new JCheckBox();
	    checkBox.setOpaque(true);
	    checkBox.setHorizontalAlignment(SwingConstants.CENTER);
	  }

	  //~ Methods --------------------------------------------------------------------------------------------------------

	  public Object getCellEditorValue() {
	    return Boolean.valueOf(checkBox.isSelected());
	  }

	  //~ ----------------------------------------------------------------------------------------------------------------

	   public Component getTableCellEditorComponent(
	    JTable  table,
	    Object  value,
	    boolean isSelected,
	    int     row,
	    int     column) {
	    checkBox.setSelected(((Boolean) value).booleanValue());
	    if (row % 2 == 0) {  
           	//cell.setBackground(new Color(238,238,238)); //设置奇数行底色  
	    	//checkBox.setBackground(new Color(225,225,225));
	    	checkBox.setBackground(new Color(237,243,254));
           } else {  
        	checkBox.setBackground(Color.white); //设置偶数行底色  
           }
	    return checkBox;

	  }
	}