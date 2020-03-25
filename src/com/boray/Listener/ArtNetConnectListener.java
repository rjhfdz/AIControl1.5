package com.boray.Listener;

import com.boray.Data.Data;
import com.boray.Utils.JIpAddressField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ArtNetConnectListener implements ActionListener {

    private JIpAddressField artNet1;
    private JIpAddressField artNet2;
    private JButton Connect;
    private JButton Disconnect;

    public ArtNetConnectListener(JIpAddressField artNet1, JIpAddressField artNet2, JButton Connect, JButton Disconnect) {
        this.artNet1 = artNet1;
        this.artNet2 = artNet2;
        this.Connect = Connect;
        this.Disconnect = Disconnect;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Á¬½Ó")) {
            try {
                if (artNet1.getIpAddress() != null&&!artNet1.getIpAddress().equals("0.0.0.0")) {
                    artNet1.setEnabled(false);
                    Data.artNetAddress1 = InetAddress.getByName(artNet1.getIpAddress());
                    Data.artNetSocket1 = new DatagramSocket();
                }
                if (artNet2.getIpAddress() != null&&!artNet2.getIpAddress().equals("0.0.0.0")) {
                    artNet2.setEnabled(false);
                    Data.artNetAddress2 = InetAddress.getByName(artNet2.getIpAddress());
                    Data.artNetSocket2 = new DatagramSocket();
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
            if (Data.artNetSocket2 != null) {
                Data.artNetSocket2.close();
            }
            Data.artNetSocket1 = null;
            Data.artNetSocket2 = null;
            Data.artNetAddress1 = null;
            Data.artNetAddress2 = null;
            artNet1.setEnabled(true);
            artNet2.setEnabled(true);
            Connect.setSelected(false);
            Disconnect.setSelected(true);
        }
    }
}
