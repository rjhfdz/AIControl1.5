package com.boray.Listener;

import com.boray.Data.Data;
import com.boray.Utils.JIpAddressField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ArtNet2ConnectListener implements ActionListener {

    private JTextField port2;
    private JIpAddressField artNet2;
    private JButton Connect;
    private JButton Disconnect;

    public ArtNet2ConnectListener(JIpAddressField artNet2, JTextField port2, JButton Connect, JButton Disconnect) {
        this.port2 = port2;
        this.artNet2 = artNet2;
        this.Connect = Connect;
        this.Disconnect = Disconnect;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Á¬½Ó")) {
            try {
                if (artNet2.getIpAddress() != null && !artNet2.getIpAddress().equals("0.0.0.0")) {
                    artNet2.setEnabled(false);
                    port2.setEnabled(false);
                    Data.artNetAddress2 = InetAddress.getByName(artNet2.getIpAddress());
                    Data.port2 = Integer.valueOf(port2.getText());
                    Data.artNetSocket2 = new DatagramSocket();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Connect.setSelected(true);
            Disconnect.setSelected(false);
        } else if (e.getActionCommand().equals("¶Ï¿ª")) {
            if (Data.artNetSocket2 != null) {
                Data.artNetSocket2.close();
            }
            Data.artNetSocket2 = null;
            Data.artNetAddress2 = null;
            artNet2.setEnabled(true);
            port2.setEnabled(true);
            Connect.setSelected(false);
            Disconnect.setSelected(true);
        }
    }
}
