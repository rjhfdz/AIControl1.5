package com.boray.beiFen.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.boray.beiFen.Listener.*;
import com.boray.main.Listener.DataWriteListener;
import com.boray.mainUi.MainUi;

public class BeiFenUI {
    public void show(JPanel pane) {
        pane.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel head = new JPanel();
        head.setLayout(new FlowLayout(FlowLayout.LEFT));
        head.setPreferredSize(new Dimension(430, 80));
        TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "���̵����������", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        head.setBorder(tb1);

        JPanel head2 = new JPanel();
        head2.setLayout(new FlowLayout(FlowLayout.LEFT));
        head2.setPreferredSize(new Dimension(484, 80));
        TitledBorder tb8 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "�������ļ�����", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        head2.setBorder(tb8);

        JButton button = new JButton("������������");
        //button.addActionListener(new DataActionListener());
        JButton button2 = new JButton("���ɵƿ�����");
        //button2.addActionListener(new DataMergeListener());
        JButton button3 = new JButton("���ɿ������ļ�");
        button3.addActionListener(new MergeAllListener());
        JButton button4 = new JButton("���ع���");
        button4.addActionListener(new LoadProjectFileActionListener());
        //button4.addActionListener(new LoadActionListener());
        JButton button8 = new JButton("���湤��");
        button8.addActionListener(new ProjectCreateFileActionListener());
        //button8.addActionListener(new DataAnalyseOfBeiFenListener());
        button.setPreferredSize(new Dimension(120, 34));
        button2.setPreferredSize(new Dimension(120, 34));
        button3.setPreferredSize(new Dimension(120, 34));
        button4.setPreferredSize(new Dimension(120, 34));
        button8.setPreferredSize(new Dimension(120, 34));
        //head.add(button);
        //head.add(button2);
        head.add(button4);
        head.add(button8);
        JPanel n1 = new JPanel();
        n1.setPreferredSize(new Dimension(200, 40));
        head.add(n1);
        head2.add(button3);
        pane.add(head);
        pane.add(head2);

        JPanel p1 = new JPanel();
        TitledBorder tb2 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "���ݼ��ص�������", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p1.setBorder(tb2);
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(4);
        p1.setLayout(flowLayout);
        //p1.setPreferredSize(new Dimension(920,180));
        p1.setPreferredSize(new Dimension(920, 150));

        JPanel p1_1 = new JPanel();
        p1_1.setPreferredSize(new Dimension(420, 100));
        p1_1.setLayout(flowLayout);
        TitledBorder tb5 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "����", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p1_1.setBorder(tb5);
        //p1.setBorder(new LineBorder(Color.gray));
        JButton btn = new JButton("��������");
        btn.addActionListener(new LoadToDeviceActionListener());
        btn.setPreferredSize(new Dimension(120, 34));
        JRadioButton radioButton = new JRadioButton("�ƹ�");
        JRadioButton radioButton2 = new JRadioButton("�յ�");
        JRadioButton radioButton3 = new JRadioButton("�п�");
        JRadioButton radioButton4 = new JRadioButton("ȫ��");
        List list = new ArrayList();
        list.add(radioButton);
        list.add(radioButton2);
        list.add(radioButton3);
        list.add(radioButton4);
        MainUi.map.put("LoadSetListComponent", list);
        radioButton.setSelected(true);
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton);
        group.add(radioButton2);
        group.add(radioButton3);
        group.add(radioButton4);
        p1_1.add(btn);
        p1_1.add(radioButton);
        p1_1.add(radioButton2);
        p1_1.add(radioButton3);
        p1_1.add(radioButton4);
        JPanel np = new JPanel();
        np.setPreferredSize(new Dimension(470, 100));
        TitledBorder tb6 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "д��������ļ�", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        np.setBorder(tb6);
        np.setLayout(flowLayout);
        BackupActionListener backupActionListener = new BackupActionListener();
//        JButton button5 = new JButton("��������");
//        button5.addActionListener(backupActionListener);
//        button5.setPreferredSize(new Dimension(120, 34));
        //p1.add(button5);
        //p1.add(new JLabel("ȫ��"));

        JButton button1 = new JButton("ѡ��������ļ�");
        button1.addActionListener(new SelectDATFileActionListener());
        np.add(button1);
        JButton DataWriteBtn = new JButton("д�������");
        MainUi.map.put("BeiFenComAndWifiDataWrite", DataWriteBtn);
        DataWriteBtn.setName("BeiFenComAndWifiDataWrite");
        DataWriteListener dataWriteListener = new DataWriteListener(DataWriteBtn);
        DataWriteBtn.addActionListener(dataWriteListener);
        np.add(DataWriteBtn);
        JPanel np2 = new JPanel();
        np2.setPreferredSize(new Dimension(400, 30));
        np2.setLayout(flowLayout);
        JLabel label1 = new JLabel("δѡ��:");
        MainUi.map.put("KonZhiQiLabel", label1);
        np2.add(label1);
        np.add(np2);

        p1.add(p1_1);
        p1.add(np);


        JPanel p2 = new JPanel();
        TitledBorder tb3 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "Ч����", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p2.setBorder(tb3);
        p2.setLayout(flowLayout);
        p2.setPreferredSize(new Dimension(420, 80));
        JButton button6 = new JButton("����DMX");
        JButton button7 = new JButton("����DMX");
        button6.addActionListener(new LoadDMXactionListener());
        button7.addActionListener(backupActionListener);
        button6.setPreferredSize(new Dimension(120, 34));
        button7.setPreferredSize(new Dimension(120, 34));
        p2.add(button6);//p2.add(button7);
        //p1.add(p2);

        //pane.add(p2);
        pane.add(p1);

        JPanel p3 = new JPanel();
        TitledBorder tb4 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "U��", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p3.setBorder(tb4);
        p3.setLayout(flowLayout);
        p3.setPreferredSize(new Dimension(920, 70));
        JLabel label = new JLabel("�豸U��״̬��");
        JLabel state = new JLabel("δ����");
        MainUi.map.put("state", state);
        JButton importJButton = new JButton("��U�̵���");
        JButton exportJButton = new JButton("������U��");
        USBFlashDiskListener listener = new USBFlashDiskListener();
        importJButton.addActionListener(listener);
        exportJButton.addActionListener(listener);
        p3.add(label);
        p3.add(state);
        p3.add(importJButton);
        p3.add(exportJButton);
        //���ý�����
        JProgressBar progressBar = new JProgressBar();
        progressBar.setMinimum(0);
        progressBar.setMaximum(255);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        p3.add(progressBar);
        MainUi.map.put("USBProgressBar", progressBar);

        pane.add(p3);
    }
}
