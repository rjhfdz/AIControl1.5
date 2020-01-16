package com.boray.xiaoGuoDeng.Listener;

import com.boray.Data.Data;
import com.boray.Data.XiaoGuoDengModel;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.Socket;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;

public class TranscribeListener implements ActionListener {

    private String model;
    private boolean first = true;

    public TranscribeListener(String model) {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String code = ((JButton) e.getSource()).getText();
        boolean flag = false;
        if (Data.serialPort != null || Data.socket != null) {
            flag = true;
        }
        if (code.equals("播放")) {
            play(flag);
        } else if (code.equals("停止")) {
            stop(flag);
        } else if (code.equals("删除")) {
            delete(flag);
        } else if (code.equals("开始录制")) {
            if (first) {
                Object[] options = {"否", "是"};
                int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "进入学习将覆盖原有录制数据，是否继续？", "警告",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, options, options[1]);
                if (yes == 1) {
                    transcribeStart(flag);
                    first = false;
                }
            } else {
                transcribeStart(flag);
            }
        } else if (code.equals("停止录制")) {
            transcribeStop(flag);
        }
    }

    /**
     * 发送播放数据
     *
     * @param flag
     */
    private void play(boolean flag) {
        if (flag) {
            byte[] buff = new byte[20];
            buff[0] = (byte) 0xFA;
            buff[1] = (byte) 0x14;
            buff[2] = (byte) 0x61;
            buff[3] = ZhiLingJi.TYPE;
            buff[5] = (byte) 0x02;
            buff[7] = (byte) Integer.parseInt(model);
            buff[10] = (byte) 0x04;
            buff[19] = ZhiLingJi.getJiaoYan(buff);
            if (Data.serialPort != null) {
                try {
                    OutputStream os = Data.serialPort.getOutputStream();
                    os.write(buff);
                    os.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Data.socket != null) {
                Socket.UDPSendData(buff);
            }
        }
    }

    /**
     * 发送停止数据
     *
     * @param flag
     */
    private void stop(boolean flag) {
        if (flag) {
            byte[] buff = new byte[20];
            buff[0] = (byte) 0xFA;
            buff[1] = (byte) 0x14;
            buff[2] = (byte) 0x61;
            buff[3] = ZhiLingJi.TYPE;
            buff[5] = (byte) 0x03;
            buff[7] = (byte) Integer.parseInt(model);
            buff[10] = (byte) 0x04;
            buff[19] = ZhiLingJi.getJiaoYan(buff);
            if (Data.serialPort != null) {
                try {
                    OutputStream os = Data.serialPort.getOutputStream();
                    os.write(buff);
                    os.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Data.socket != null) {
                Socket.UDPSendData(buff);
            }
        }
    }

    /**
     * 发送删除数据
     *
     * @param flag
     */
    private void delete(boolean flag) {
        if (flag) {
            byte[] buff = new byte[20];
            buff[0] = (byte) 0xFA;
            buff[1] = (byte) 0x14;
            buff[2] = (byte) 0x61;
            buff[3] = ZhiLingJi.TYPE;
            buff[5] = (byte) 0x04;
            buff[7] = (byte) Integer.parseInt(model);
            buff[10] = (byte) 0x04;
            buff[19] = ZhiLingJi.getJiaoYan(buff);
            if (Data.serialPort != null) {
                try {
                    OutputStream os = Data.serialPort.getOutputStream();
                    os.write(buff);
                    os.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Data.socket != null) {
                Socket.UDPSendData(buff);
            }
            //清除录制后的数据
            JPanel timeBlockPanels = ((JPanel[]) MainUi.map.get("timeBlockPanels_group" + model))[0];
            timeBlockPanels.removeAll();
        }
    }

    /**
     * 发送开始录制数据
     *
     * @param flag
     */
    private void transcribeStart(boolean flag) {
        if (flag) {
            byte[] buff = new byte[20];
            buff[0] = (byte) 0xFA;
            buff[1] = (byte) 0x14;
            buff[2] = (byte) 0x61;
            buff[3] = ZhiLingJi.TYPE;
            buff[5] = (byte) 0x9E;
            buff[7] = (byte) Integer.parseInt(model);
            buff[10] = (byte) 0x04;
            buff[19] = ZhiLingJi.getJiaoYan(buff);
            if (Data.serialPort != null) {
                try {
                    OutputStream os = Data.serialPort.getOutputStream();
                    os.write(buff);
                    os.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Data.socket != null) {
                Socket.UDPSendData(buff);
            }
        }
    }

    /**
     * 发送停止录制数据
     *
     * @param flag
     */
    private void transcribeStop(boolean flag) {
        if (flag) {
            byte[] buff = new byte[20];
            buff[0] = (byte) 0xFA;
            buff[1] = (byte) 0x14;
            buff[2] = (byte) 0x61;
            buff[3] = ZhiLingJi.TYPE;
            buff[5] = (byte) 0x9F;
            buff[7] = (byte) Integer.parseInt(model);
            buff[10] = (byte) 0x04;
            buff[19] = ZhiLingJi.getJiaoYan(buff);
            if (Data.serialPort != null) {
                try {
                    OutputStream os = Data.serialPort.getOutputStream();
                    os.write(buff);
                    os.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Data.socket != null) {
                Socket.UDPSendData(buff);
            }
        }
    }
}