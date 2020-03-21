package com.boray.Utils;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;

import java.io.OutputStream;
import java.net.DatagramPacket;

public class Socket {

    public static void UDPSendData(byte[] buff){
        try {
            if(Data.socket!=null) {
                DatagramPacket packet = new DatagramPacket(buff, buff.length);
                packet.setSocketAddress(Data.address);
                Data.socket.send(packet);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void SerialPortSendData(byte[] buff){
        try {
            if(Data.serialPort!=null) {
                OutputStream os = Data.serialPort.getOutputStream();
                os.write(buff);
                os.flush();
                os.close();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void SendData(byte[] buff){
        if(Data.serialPort!=null){
            SerialPortSendData(buff);
        }else if(Data.socket!=null){
            UDPSendData(buff);
        }
    }
}
