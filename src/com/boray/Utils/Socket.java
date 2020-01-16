package com.boray.Utils;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;

import java.io.OutputStream;
import java.net.DatagramPacket;

public class Socket {

    public static void UDPSendData(byte[] buff){
        try {
            DatagramPacket packet = new DatagramPacket(buff, buff.length);
            packet.setSocketAddress(Data.address);
            Data.socket.send(packet);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void SerialPortSendData(byte[] buff){
        try {
            OutputStream os = Data.serialPort.getOutputStream();
            os.write(buff);
            os.flush();
            os.close();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
