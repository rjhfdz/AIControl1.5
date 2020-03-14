package com.boray.dengKu.Listener;

import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class RDMTableMoveListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem menuItem = (JMenuItem) e.getSource();
        NewJTable table = (NewJTable) MainUi.map.get("RDM_table");
        if (menuItem.getText().equals("иорф")) {
            int select[] = table.getSelectedRows();

        } else {

        }
    }

}
