package com.boray.Data;

public class RdmData {
    /*
     * 搜索
     * FA 3C CC AA 00 10 FF FF FF FF FF FF 00 B8
     *
     * FA 14 60 B9 11 00 F1 00 00 00 00 00 00 00 00 00 00 00 00 29
     */
    public static byte[] serch() {
        byte[] b = new byte[20];
        for (int i = 0; i < b.length; i++) {
            b[i] = 0;
        }
        b[0] = (byte) 0xFA;
        b[1] = (byte) 0x14;
        b[2] = (byte) 0x60;
        b[3] = (byte) ZhiLingJi.TYPE;
        b[4] = (byte) 0x11;
        b[5] = (byte) 0x00;
        b[6] = (byte) 0xF1;
		/*for (int i = 0; i < 6; i++) {
			b[6+i] = (byte)0xFF;
		}*/
        b[19] = getJiaoYan(b);
        return b;
    }

    /**
     * 退出rdm模式
     *
     * @return
     */
    public static byte[] quit() {
        byte[] b = new byte[20];
        b[0] = (byte) 0xFA;
        b[1] = (byte) 0x14;
        b[2] = (byte) 0x60;
        b[3] = (byte) ZhiLingJi.TYPE;
        b[4] = (byte) 0x11;
        b[5] = (byte) 0x00;
        b[6] = (byte) 0xF0;
        b[19] = (byte) ZhiLingJi.getJiaoYan(b);
        return b;
    }

    /**
     * 进入RDM模式
     *
     * @return
     */
    public static byte[] access() {
        byte[] b = new byte[20];
        for (int i = 0; i < b.length; i++) {
            b[i] = 0;
        }
        b[0] = (byte) 0xFA;
        b[1] = (byte) 0x14;
        b[2] = (byte) 0x60;
        b[3] = (byte) ZhiLingJi.TYPE;
        b[4] = (byte) 0x11;
        b[5] = (byte) 0x00;
        b[6] = (byte) 0xF2;
        b[19] = getJiaoYan(b);
        return b;
    }

    /*
     * 数量查询
     * FA 3C CC AA 00 40 FF FF FF FF FF FF 00 B8
     */
    public static byte[] serchCount() {
        byte[] b = new byte[14];
        for (int i = 0; i < b.length; i++) {
            b[i] = 0;
        }
        b[0] = (byte) 0xFA;
        b[1] = (byte) 0x0E;
        b[2] = (byte) 0xCC;
        b[3] = (byte) ZhiLingJi.TYPE;
        b[4] = (byte) 0x00;
        b[5] = (byte) 0x40;
        for (int i = 0; i < 6; i++) {
            b[6 + i] = (byte) 0xFF;
        }
        b[13] = getJiaoYan(b);
        return b;
    }

    /*
     * 查询UID
     * FA 3C CC AA 00 20 （00 00 00 00 00 00） 00 9E
     */
    public static byte[] getUIDById(int num) {
        byte[] b = new byte[14];
        b[0] = (byte) 0xFA;
        b[1] = (byte) 0x0E;
        b[2] = (byte) 0xCC;
        b[3] = (byte) ZhiLingJi.TYPE;
        b[4] = (byte) 0x00;
        b[5] = (byte) 0x20;
        for (int i = 0; i < 6; i++) {
            b[6 + i] = (byte) num;
        }

        b[13] = getJiaoYan(b);
        return b;
    }

    /*
     * 查询设备型号 05 / 起始地址查询 03 / 设备信息 01
     * FA 0E CC B9 05 -20 《42 52 00 62 00 4C》 00 F4
     */
    public static byte[] serchType(byte[] tp, int serchType) {
        byte[] b = new byte[14];

        b[0] = (byte) 0xFA;
        b[1] = (byte) 0x0E;
        b[2] = (byte) 0xCC;
        b[3] = (byte) ZhiLingJi.TYPE;
        b[4] = (byte) serchType;
        b[5] = (byte) 0x20;
        for (int i = 0; i < tp.length; i++) {
            b[6 + i] = tp[i];
        }
        b[13] = getJiaoYan(b);
        return b;
    }

    /*
     * 设置设备型号 05 / 起始地址查03 / 设备信息 01
     */
    public static byte[] setType(byte[] tp, int setType, byte[] value) {
        byte[] b = new byte[16];

        b[0] = (byte) 0xFA;
        b[1] = (byte) 0x10;
        b[2] = (byte) 0xCC;
        b[3] = (byte) ZhiLingJi.TYPE;
        b[4] = (byte) setType;
        b[5] = (byte) 0x30;
        for (int i = 0; i < tp.length; i++) {
            b[6 + i] = tp[i];
        }
        for (int i = 0; i < value.length; i++) {
            b[12 + i] = value[i];
        }
        b[15] = getJiaoYan(b);
        return b;
    }

    /*
     * 校验和
     */
    public static byte getJiaoYan(byte[] b) {
        int all = 0;
        for (int i = 0; i < b.length; i++) {
            all = all + Byte.toUnsignedInt(b[i]);
        }
        return (byte) all;
    }
}
