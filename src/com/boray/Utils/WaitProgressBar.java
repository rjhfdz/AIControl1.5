package com.boray.Utils;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;

public class WaitProgressBar implements ActionListener {

    private static final String DEFAULT_STATUS = "���Ժ󡣡���";
    private JDialog dialog;
    private JProgressBar progressBar;
    private JLabel lbStatus;
    private JButton btnCancel;
    private Window parent;
    private Thread thread;//����ҵ����߳�
    private String statusInfo;
    private String resultInfo;
    private String cancelInfo;

    /**
     * ��ʾ�������Ի���
     *
     * @param parent
     * @param thread
     */
    public static void show(Window parent, Thread thread) {
        new WaitProgressBar(parent, thread, DEFAULT_STATUS, null, null);
    }

    /**
     * ��ʾ�������Ի���
     *
     * @param parent
     * @param thread
     * @param statusInfo
     */
    public static void show(Window parent, Thread thread, String statusInfo) {
        new WaitProgressBar(parent, thread, statusInfo, null, null);
    }

    /**
     * ��ʾ�Ի���
     *
     * @param parent
     * @param thread
     * @param statusInfo
     * @param resultInfo
     * @param cancelInfo
     */
    public static void show(Window parent, Thread thread, String statusInfo, String resultInfo, String cancelInfo) {
        new WaitProgressBar(parent, thread, statusInfo, resultInfo, cancelInfo);
    }

    /**
     * �Ի����캯��
     *
     * @param parent
     * @param thread
     * @param statusInfo
     * @param resultInfo
     * @param cancelInfo
     */
    private WaitProgressBar(Window parent, Thread thread, String statusInfo, String resultInfo, String cancelInfo) {
        this.parent = parent;
        this.thread = thread;
        this.statusInfo = statusInfo;
        this.resultInfo = resultInfo;
        this.cancelInfo = cancelInfo;
        initUI();
        startThread();
        dialog.setVisible(true);
    }

    /**
     * ������ʾ�������ĶԻ���
     */
    private void initUI() {
        if (parent instanceof Dialog) {
            dialog = new JDialog((Dialog) parent, true);
        } else if (parent instanceof Frame) {
            dialog = new JDialog((Frame) parent, true);
        } else {
            dialog = new JDialog((Frame) null, true);
        }

        final JPanel mainPane = new JPanel(null);
        progressBar = new JProgressBar();
        lbStatus = new JLabel("" + statusInfo);
        btnCancel = new JButton("�ر�");
        progressBar.setIndeterminate(true);
        btnCancel.addActionListener(this);

        mainPane.add(progressBar);
        mainPane.add(lbStatus);
//        mainPane.add(btnCancel);

        dialog.getContentPane().add(mainPane);
        dialog.setUndecorated(true);//��ȥtitle
        dialog.setResizable(true);
        dialog.setSize(390, 100);
        dialog.setLocationRelativeTo(parent);//���ô˴��������ָ�������λ��

        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);//������ر�

        mainPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                layout(mainPane.getWidth(), mainPane.getHeight());
            }
        });
    }

    /**
     * �����߳�
     */
    private void startThread() {
        new Thread() {
            public void run() {
                try {
                    thread.start();//�����ʱ����
                    //�ȴ����ﴦ���߳̽���
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //�رս�����ʾ��
                    dialog.dispose();
                    if (resultInfo != null && !resultInfo.trim().equals("")) {
                        String title = "��ʾ";
                        JOptionPane.showMessageDialog(parent, resultInfo, title, JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        }.start();
    }

    /**
     * ���ÿռ��λ�ô�С
     *
     * @param width
     * @param height
     */
    private void layout(int width, int height) {
        progressBar.setBounds(20, 20, 350, 15);
        lbStatus.setBounds(20, 50, 350, 25);
        btnCancel.setBounds(width - 85, height - 31, 75, 21);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        resultInfo = cancelInfo;
        thread.stop();
    }

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread() {
            public void run() {
                int index = 0;
                while (index < 5) {
                    try {
                        sleep(1000);
                        System.out.println(++index);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        WaitProgressBar.show(null, thread, "Status", "Result", "Cancel");
    }
}
