package com.boray.xiaoGuoDeng.Listener;

import com.boray.Utils.IconJDialog;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class DMXAndModelListener implements MouseListener {

    private String model;

    public DMXAndModelListener(String model) {
        this.model = model;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        JFrame frame = (JFrame) MainUi.map.get("frame");
        IconJDialog dialog = new IconJDialog(frame, true);
        dialog.setResizable(false);
        dialog.setTitle("DMX录制——模式" + model);
        int w = 600, h = 285;
        dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
        dialog.setSize(w, h);
        dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        BorderLayout layout = (BorderLayout) dialog.getLayout();
        layout.setHgap(20);
        layout.setVgap(20);
        initUI(dialog);
        dialog.setVisible(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * 加载UI界面
     *
     * @param dialog
     */
    public void initUI(final Dialog dialog) {
        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(Color.gray));
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(565, 80));
        JButton play = new JButton("播放");
        JButton stop = new JButton("停止");
        JButton delete = new JButton("删除");

        TranscribeListener listener = new TranscribeListener(model);
        play.addActionListener(listener);
        stop.addActionListener(listener);
        delete.addActionListener(listener);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(play);
        buttonPanel.add(stop);
        buttonPanel.add(delete);

        JPanel panel1 = new JPanel();
        panel1.setPreferredSize(new Dimension(565, 20));

        panel.add(panel1, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        JPanel jPanel = new JPanel();
        jPanel.setBorder(new LineBorder(Color.gray));
        jPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        jPanel.setPreferredSize(new Dimension(565, 150));
        JButton startDMX = new JButton("开始录制");
        JButton stopDMX = new JButton("停止录制");
        JLabel label2 = new JLabel("录制进度：                                                                                                              ");
        JLabel sec2 = new JLabel("0秒");

        startDMX.addActionListener(listener);
        stopDMX.addActionListener(listener);

        JProgressBar bar = new JProgressBar();
        bar.setMinimum(0);
        bar.setMaximum(600);
        bar.setValue(0);
        bar.setPreferredSize(new Dimension(555, 25));
        MainUi.map.put("DMXBarTwo" + model, bar);
        MainUi.map.put("DMXsec" + model, sec2);

        JPanel panel2 = new JPanel();
        panel2.setPreferredSize(new Dimension(565, 20));

        jPanel.add(panel2);
        jPanel.add(startDMX);
        jPanel.add(stopDMX);
        jPanel.add(label2);
        jPanel.add(sec2);
        jPanel.add(bar);
//        JPanel panel1 = new JPanel();
//        panel1.setPreferredSize(new Dimension(420, 45));
//        dialog.add(panel1);
        dialog.add(panel);
        dialog.add(jPanel);
    }
}
