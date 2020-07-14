package com.boray.beiFen.Listener;

import com.boray.Data.Data;
import com.boray.Utils.Util;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SelectDATFileActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        try {
            if (!"".equals(Data.saveCtrlFilePath)) {
                fileChooser.setCurrentDirectory(new File(Data.saveCtrlFilePath));
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        String[] houZhui = {"DAT", "dat"};
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.DAT", houZhui);
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fileChooser.showOpenDialog((JFrame) MainUi.map.get("frame"));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            Data.saveCtrlFilePath = file.getParent();
            Data.saveCtrlFile = file.getAbsolutePath();
            JLabel label = (JLabel) MainUi.map.get("KonZhiQiLabel");
            label.setText("ÒÑÑ¡Ôñ:" + Data.saveCtrlFile);
            System.out.println(Data.saveCtrlFile);
        }
    }
}
