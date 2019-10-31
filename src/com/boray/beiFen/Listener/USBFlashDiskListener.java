package com.boray.beiFen.Listener;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;

public class USBFlashDiskListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        if (Data.serialPort != null) {
            JLabel stateLabel = (JLabel) MainUi.map.get("state");
            try {
                if (stateLabel.getText().equals("�Ѳ���")) {
                    if ("��U�̵���".equals(e.getActionCommand())) {
                        OutputStream os = Data.serialPort.getOutputStream();
                        os.write(ZhiLingJi.USBFlashDiskImportAndExport("����"));
                        os.flush();
                    } else {
                        OutputStream os = Data.serialPort.getOutputStream();
                        os.write(ZhiLingJi.USBFlashDiskImportAndExport("����"));
                        os.flush();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
