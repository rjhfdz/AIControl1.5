package com.boray.mainUi;

import com.boray.Listener.SwitchListener;

import javax.swing.*;
import java.awt.*;

public class LeftBtnPane {

    public void show(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(0);
        flowLayout.setHgap(-2);
        pane.setLayout(flowLayout);
        JToggleButton btn10 = new JToggleButton("主窗口");
        JToggleButton btn1 = new JToggleButton("场景配置");
        JToggleButton btn2 = new JToggleButton("灯库配置");
        JToggleButton btn2_2 = new JToggleButton("效果素材");
        JToggleButton btn3 = new JToggleButton("效果编程");
        JToggleButton btn11 = new JToggleButton("声控素材");
        JToggleButton btn4 = new JToggleButton("声控编程");
        MainUi.map.put("xiaoGuoBtn", btn3);
        MainUi.map.put("shengKonBtn", btn4);
        JToggleButton btn5 = new JToggleButton("空调设置");
        JToggleButton btn6 = new JToggleButton("中控设置");
        JToggleButton btn7 = new JToggleButton("全局设置");
        JToggleButton btn8 = new JToggleButton("备份还原");
        MainUi.map.put("QuanJuToggleBtn", btn7);
        btn1.setName("1");btn2.setName("2");btn2_2.setName("9");
        btn3.setName("3");btn4.setName("4");
        btn5.setName("5");btn6.setName("6");
        btn7.setName("7");btn8.setName("8");
        btn10.setName("10");
        btn11.setName("11");
        btn1.setSelected(true);
        SwitchListener listener = new SwitchListener();
        btn1.addActionListener(listener);btn2.addActionListener(listener);btn2_2.addActionListener(listener);
        btn3.addActionListener(listener);btn4.addActionListener(listener);
        btn5.addActionListener(listener);btn6.addActionListener(listener);
        btn7.addActionListener(listener);btn8.addActionListener(listener);
        btn10.addActionListener(listener);
        btn11.addActionListener(listener);
        ImageIcon icon = new ImageIcon(getClass().getResource("/icon/1.png"));
        ImageIcon icon2 = new ImageIcon(getClass().getResource("/icon/2.png"));
        ImageIcon icon3 = new ImageIcon(getClass().getResource("/icon/3.png"));

        ImageIcon icon4 = new ImageIcon(getClass().getResource("/icon/4.png"));
        ImageIcon icon5 = new ImageIcon(getClass().getResource("/icon/5.png"));
        ImageIcon icon6 = new ImageIcon(getClass().getResource("/icon/6.png"));
        ImageIcon icon7 = new ImageIcon(getClass().getResource("/icon/7.png"));
        ImageIcon icon8 = new ImageIcon(getClass().getResource("/icon/8.png"));


        btn1.setIcon(icon);btn2.setIcon(icon2);btn2_2.setIcon(icon3);
        btn3.setIcon(icon3);
        btn4.setIcon(icon4);btn5.setIcon(icon5);btn6.setIcon(icon6);
        btn7.setIcon(icon7);btn8.setIcon(icon8);
        btn10.setIcon(icon8);
        btn11.setIcon(icon3);
        int width = 60,height = 60;
        btn1.setPreferredSize(new Dimension(width,height));
        btn2.setPreferredSize(new Dimension(width,height));
        btn3.setPreferredSize(new Dimension(width,height));
        btn4.setPreferredSize(new Dimension(width,height));
        btn5.setPreferredSize(new Dimension(width,height));
        btn6.setPreferredSize(new Dimension(width,height));
        btn7.setPreferredSize(new Dimension(width,height));
        btn8.setPreferredSize(new Dimension(width,height));
        btn2_2.setPreferredSize(new Dimension(width,height));
        btn10.setPreferredSize(new Dimension(width,height));
        btn11.setPreferredSize(new Dimension(width,height));
        btn1.setMargin(new Insets(0,-10,0,-10));btn2.setMargin(new Insets(-10,-10,-10,-10));
        btn2_2.setMargin(new Insets(-10,-10,-10,-10));
        btn3.setMargin(new Insets(0,-10,0,-10));btn4.setMargin(new Insets(0,-10,0,-10));
        btn5.setMargin(new Insets(0,-10,0,-10));btn6.setMargin(new Insets(0,-10,0,-10));
        btn7.setMargin(new Insets(0,-10,0,-10));btn8.setMargin(new Insets(0,-10,0,-10));
        btn10.setMargin(new Insets(0,-10,0,-10));btn11.setMargin(new Insets(0,-10,0,-10));
        btn1.setFocusable(false);btn2.setFocusable(false);
        btn3.setFocusable(false);btn4.setFocusable(false);
        btn5.setFocusable(false);btn6.setFocusable(false);
        btn7.setFocusable(false);btn8.setFocusable(false);
        btn2_2.setFocusable(false);
        btn10.setFocusPainted(false);btn11.setFocusPainted(false);

        btn1.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn1.setHorizontalTextPosition(SwingConstants.CENTER);
        btn2.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn2.setHorizontalTextPosition(SwingConstants.CENTER);
        btn3.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn3.setHorizontalTextPosition(SwingConstants.CENTER);
        btn4.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn4.setHorizontalTextPosition(SwingConstants.CENTER);
        btn5.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn5.setHorizontalTextPosition(SwingConstants.CENTER);
        btn6.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn6.setHorizontalTextPosition(SwingConstants.CENTER);
        btn7.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn7.setHorizontalTextPosition(SwingConstants.CENTER);
        btn8.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn8.setHorizontalTextPosition(SwingConstants.CENTER);

        btn10.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn10.setHorizontalTextPosition(SwingConstants.CENTER);

        btn11.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn11.setHorizontalTextPosition(SwingConstants.CENTER);

        btn2_2.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn2_2.setHorizontalTextPosition(SwingConstants.CENTER);

        ButtonGroup group = new ButtonGroup();
        group.add(btn10);
        group.add(btn1);
        group.add(btn2);group.add(btn2_2);
        group.add(btn3);
        group.add(btn11);
        group.add(btn4);
        group.add(btn5);
        group.add(btn6);
        group.add(btn7);
        group.add(btn8);

        JPanel jPanel = new JPanel();
        jPanel.setPreferredSize(new Dimension(80,750));

        jPanel.add(btn10);
        jPanel.add(btn1);
        jPanel.add(btn2);jPanel.add(btn2_2);
        jPanel.add(btn3);
        jPanel.add(btn11);
        jPanel.add(btn4);
        jPanel.add(btn5);
        jPanel.add(btn6);
        jPanel.add(btn7);
        jPanel.add(btn8);

//        JScrollPane scrollPane = new JScrollPane();
//        scrollPane.setPreferredSize(new Dimension(95,550));
//        scrollPane.setBorder(BorderFactory.createMatteBorder(0,0,0,0,Color.gray));
//        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
//        scrollPane.setViewportView(jPanel);

        pane.add(jPanel);
    }
}
