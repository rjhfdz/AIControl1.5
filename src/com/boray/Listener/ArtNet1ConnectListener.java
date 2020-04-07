package com.boray.Listener;

import com.boray.Data.Data;
import com.boray.Utils.JIpAddressField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ArtNet1ConnectListener implements ActionListener {

    private JIpAddressField artNet1;
    private JTextField port;
    private JButton Connect;
    private JButton Disconnect;

    public ArtNet1ConnectListener(JIpAddressField artNet1, JTextField port, JButton Connect, JButton Disconnect) {
        this.artNet1 = artNet1;
        this.port = port;
        this.Connect = Connect;
        this.Disconnect = Disconnect;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Á¬½Ó")) {
            try {
                if (artNet1.getIpAddress() != null&&!artNet1.getIpAddress().equals("0.0.0.0")) {
                    artNet1.setEnabled(false);
                    port.setEnabled(false);
                    Data.artNetAddress1 = InetAddress.getByName(artNet1.getIpAddress());
                    Data.port1 = Integer.valueOf(port.getText());
                    Data.artNetSocket1 = new DatagramSocket();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            Connect.setSelected(true);
            Disconnect.setSelected(false);
        } else if (e.getActionCommand().equals("¶Ï¿ª")) {
            if (Data.artNetSocket1 != null) {
                Data.artNetSocket1.close();
            }
            Data.artNetSocket1 = null;
            Data.artNetAddress1 = null;
            artNet1.setEnabled(true);
            port.setEnabled(true);
            Connect.setSelected(false);
            Disconnect.setSelected(true);
        }
    }
}
