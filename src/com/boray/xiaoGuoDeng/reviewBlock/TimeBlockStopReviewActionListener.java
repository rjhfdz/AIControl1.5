package com.boray.xiaoGuoDeng.reviewBlock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;

import com.boray.Data.Data;
import com.boray.Utils.Socket;

import javax.swing.*;

public class TimeBlockStopReviewActionListener implements ActionListener {
    private int sc, group, block;

    private JComboBox comboBox;

    public TimeBlockStopReviewActionListener(int sc, int group, int block) {
        this.sc = sc;
        this.block = block;
        this.group = group;
    }

    public TimeBlockStopReviewActionListener(JComboBox comboBox, int sc, int block) {
        this.comboBox = comboBox;
        this.sc = sc;
        this.block = block;
    }

    public void actionPerformed(ActionEvent e) {
        if (comboBox != null || comboBox.getSelectedItem().toString() != "") {
            this.group = Integer.parseInt(comboBox.getSelectedItem().toString().split("#")[0]);
        }else{
            return;
        }
        if (Data.serialPort != null) {
            try {
                //Õ£÷π‘§¿¿
                byte[] b = TimeBlockReviewData.getStopReview(sc, group, block);
                OutputStream os = Data.serialPort.getOutputStream();
                os.write(b);
                os.flush();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else if (Data.socket != null) {
            try {
                //Õ£÷π‘§¿¿
                byte[] b = TimeBlockReviewData.getStopReview(sc, group, block);
                Socket.UDPSendData(b);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
}
