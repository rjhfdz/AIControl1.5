package com.boray.changJing.UI;

import com.boray.Data.ChangJinData;
import com.boray.Utils.NumberTextField;
import com.boray.Utils.Socket;
import com.boray.changJing.Listener.ChangJingSendCodeListener;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestPaneUI {

    /**
     * 加载场景测试界面
     *
     * @param pane
     */
    public void show(JPanel pane) {
        pane.setPreferredSize(new Dimension(1000, 350));
        //pane.setBorder(new LineBorder(Color.gray));
        pane.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "灯光场景", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        panel.setBorder(tb1);
        panel.setPreferredSize(new Dimension(410, 250));
        JToggleButton[] btns = new JToggleButton[32];
        MainUi.map.put("ToggleBtns_changJing", btns);
        ButtonGroup group = new ButtonGroup();
        ChangJingSendCodeListener listener = new ChangJingSendCodeListener();
        for (int i = 0; i < btns.length; i++) {
            btns[i] = new JToggleButton(ChangJinData.T1()[i]);
            if (i < 22) {
                btns[i].setName(String.valueOf(i));
            } else {
                if (i == 22) {
                    btns[i].setName("23");
                }
                if (i == 23) {
                    btns[i].setName("24");
                }
                if (i >= 24) {
                    btns[i].setName(String.valueOf(i + 5));
                }
            }
            btns[i].setPreferredSize(new Dimension(94, 48));
            btns[i].setMargin(new Insets(0, -10, 0, -10));
            btns[i].addActionListener(listener);
            group.add(btns[i]);
            panel.add(btns[i]);
        }
//        addXieMaUI(panel);


        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
        TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "功能场景", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        panel2.setBorder(tb2);
        panel2.setPreferredSize(new Dimension(1000, 40));
        JToggleButton[] btns2 = new JToggleButton[4];
        for (int i = 0; i < btns2.length; i++) {
            btns2[i] = new JToggleButton(ChangJinData.T1()[i + 32]);
            btns2[i].setPreferredSize(new Dimension(94, 48));
            btns2[i].setMargin(new Insets(0, -10, 0, -10));
            if (i == 0) {
                btns2[i].setName("22");
            } else if (i == 1) {
                btns2[i].setName("26");
            } else if (i == 2) {
                btns2[i].setName("27");
            } else if (i == 3) {
                btns2[i].setName("28");
            }
            btns2[i].addActionListener(listener);
            group.add(btns2[i]);
            panel2.add(btns2[i]);
        }

        pane.add(panel, BorderLayout.NORTH);
        pane.add(panel2);
    }

    public void addXieMaUI(JPanel panel) {
        JLabel label1 = new JLabel("起始地址:");
        final JTextField startAddress = new JTextField(10);
        startAddress.setDocument(new NumberTextField());
        startAddress.setText("1");
        JLabel label2 = new JLabel("芯片数量:");
        final JTextField xinPianCount = new JTextField(10);
        xinPianCount.setDocument(new NumberTextField());
        xinPianCount.setText("1");
        JButton button = new JButton("写入");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int address = Integer.parseInt(startAddress.getText().equals("") ? "1" : startAddress.getText());
                if (startAddress.getText().equals("")||startAddress.getText().equals("0")) {
                    startAddress.setText("1");
                }
                if (address > 1024 || address < 0) {
                    JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "起始地址范围不得超过1-1024！", "提示", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                int count = Integer.parseInt(xinPianCount.getText().equals("") ? "1" : xinPianCount.getText());
                if (xinPianCount.getText().equals("")||xinPianCount.getText().equals("0")) {
                    xinPianCount.setText("1");
                }
                if (count > 512 || count < 0) {
                    JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "芯片数量范围不得超过1-512！", "提示", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                byte[] bytes = new byte[6];
                bytes[0] = (byte) 0XBB;
                bytes[1] = (byte) 0X06;
                bytes[2] = (byte) (address / 256);
                bytes[3] = (byte) (address % 256);
                bytes[4] = (byte) (count / 256);
                bytes[5] = (byte) (count % 256);
                Socket.SendData(bytes);
            }
        });
        panel.add(label1);
        panel.add(startAddress);
        panel.add(label2);
        panel.add(xinPianCount);
        panel.add(button);
    }
}
