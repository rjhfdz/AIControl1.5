package com.boray.Utils;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;

import java.io.OutputStream;
import java.net.DatagramPacket;

public class Socket {

    public static void UDPSendData(byte[] buff) {
        try {
            if (Data.socket != null) {
                DatagramPacket packet = new DatagramPacket(buff, buff.length);
                packet.setSocketAddress(Data.address);
                Data.socket.send(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void SerialPortSendData(byte[] buff) {
        try {
            if (Data.serialPort != null) {
                OutputStream os = Data.serialPort.getOutputStream();
                os.write(buff);
                os.flush();
                os.close();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static void SendData(byte[] buff) {
        if (Data.serialPort != null) {
            SerialPortSendData(buff);
        } else if (Data.socket != null) {
            UDPSendData(buff);
        }
    }

    public static void ArtNetSendData(byte[] buff) {
        byte[] bytes = new byte[buff.length + 18];
        for (int i = 0; i < bytes.length; i++) {
            if (i < 18)
                bytes[i] = Data.dmx_def[i];
            else
                bytes[i] = buff[i - 18];
        }
        try {
            if (Data.artNetSocket1 != null) {
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length, Data.artNetAddress1, 6454);
                Data.artNetSocket1.send(packet);
            }
            if (Data.artNetSocket2 != null) {
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length, Data.artNetAddress2, 6454);
                Data.artNetSocket2.send(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void TempArtNet(int[] data ,int[] startAddress){
        byte[] buff = new byte[512 + 8];
        buff[0] = (byte) 0xBB;
        buff[1] = (byte) 0x55;
        buff[2] = (byte) (520 / 256);
        buff[3] = (byte) (520 % 256);
        buff[4] = (byte) 0x80;
        buff[5] = (byte) 0x01;
        buff[6] = (byte) 0xFF;
        for (int j = 0; j < data.length; j++) {
            for (int i = 0; i < startAddress.length; i++) {
                buff[j - 1 + startAddress[i] + 7] = (byte) data[j];
            }
        }
        buff[519] = ZhiLingJi.getJiaoYan(buff);
        SerialPortSendData(buff);
    }

}
