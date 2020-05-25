package com.boray.quanJu.Listener;

import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.IconJDialog;
import com.boray.Utils.Socket;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class DMX512TestDialog implements ActionListener {

    private JToggleButton[] btns;
    private JTextField[] textFields;
    private JSlider[] sliders;
    private JLabel[] names;
    private ButtonGroup group;
    public static int selected = 0;

    @Override
    public void actionPerformed(ActionEvent e) {
        show();
    }

    /**
     * 展示界面
     */
    private void show() {
        JFrame f = (JFrame) MainUi.map.get("frame");
        IconJDialog dialog = new IconJDialog(f, true);
        dialog.setResizable(false);
        dialog.setTitle("DMX512通道测试");
        dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - 300, f.getLocation().y + f.getSize().height / 2 - 165);
        dialog.setSize(930, 360);
        init(dialog);//加载页面UI
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {//关闭窗口时保存数据
            public void windowClosing(WindowEvent e) {
                saveData();
            }
        });
        dialog.setVisible(true);
    }

    /**
     * 加载页面UI
     *
     * @param dialog
     */
    private void init(JDialog dialog) {
        //加载按钮面板
        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(910, 110));
        p1.setLayout(new FlowLayout(FlowLayout.LEFT));
        Border lb1 = BorderFactory.createLineBorder(Color.gray);
        p1.setBorder(lb1);
        btns = new JToggleButton[26];
        group = new ButtonGroup();
        for (int i = 0; i < btns.length; i++) {
            btns[i] = new JToggleButton();
            btns[i].setName("" + i);
            btns[i].setFocusable(false);
            group.add(btns[i]);
            btns[i].setPreferredSize(new Dimension(75, 30));
            btns[i].setMargin(new Insets(-20, -2, -20, -2));
            btns[i].setText("Page" + (i + 1));
            btns[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    saveData();//保存当前面板数据
                    selected = Integer.valueOf(((JToggleButton) e.getSource()).getName()).intValue();
                    setData();//设置选中按钮的数据
                }
            });
            p1.add(btns[i]);
        }

        //加载滚动条显示
        JPanel p2 = new JPanel();
        p2.setPreferredSize(new Dimension(910, 150));
        p2.setLayout(new FlowLayout(FlowLayout.LEFT));
        Border lb2 = BorderFactory.createLineBorder(Color.gray);
        p2.setBorder(lb2);
        names = new JLabel[20];
        textFields = new JTextField[20];
        sliders = new JSlider[20];
        JPanel[] itemPanes = new JPanel[20];
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(1);
        for (int i = 0; i < names.length; i++) {
            final int a = i;
            itemPanes[i] = new JPanel();
            itemPanes[i].setLayout(flowLayout);
            itemPanes[i].setPreferredSize(new Dimension(40, 130));
            if (i >= 9) {
                names[i] = new JLabel((i + 1) + "");
            } else {
                names[i] = new JLabel("0" + (i + 1));
            }
            textFields[i] = new JTextField();
            textFields[i].setText(0 + "");
            textFields[i].setPreferredSize(new Dimension(36, 27));
            sliders[i] = new JSlider(JSlider.VERTICAL, 0, 255, 0);
            sliders[i].setPreferredSize(new Dimension(18, 76));
            sliders[i].addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    textFields[a].setText(String.valueOf(sliders[a].getValue()));
                }
            });
            sliders[i].addMouseWheelListener(new MouseWheelListener() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    if (sliders[a].isEnabled()) {
                        if (e.getUnitsToScroll() > 0) {
                            sliders[a].setValue(sliders[a].getValue() - 1);
                        } else {
                            sliders[a].setValue(sliders[a].getValue() + 1);
                        }
                        outDevice();
                    }
                }
            });
            sliders[i].addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    outDevice();
                }
            });
            textFields[i].addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == 10) {
                        int tb = Integer.valueOf(textFields[a].getText()).intValue();
                        if (tb == sliders[a].getValue()) {
                            sliders[a].setValue(tb - 1);
                        }
                        sliders[a].setValue(tb);
                        outDevice();
                    }
                }
            });

            itemPanes[i].add(names[i]);
            itemPanes[i].add(textFields[i]);
            itemPanes[i].add(sliders[i]);
            p2.add(itemPanes[i]);
        }

        btns[0].setSelected(true);//默认选中第一个按钮 并加载数据
        setData();

        //加载清零 满值按钮
        JPanel p3 = new JPanel();
        p3.setPreferredSize(new Dimension(910, 40));
        p3.setLayout(new FlowLayout(FlowLayout.CENTER));
        Border lb3 = BorderFactory.createLineBorder(Color.gray);
        p3.setBorder(lb3);
        JButton button1 = new JButton("清零");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 20; i++) {
                    sliders[i].setValue(0);
                }
            }
        });
        JButton button2 = new JButton("全部清零");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 20; i++) {
                    sliders[i].setValue(0);
                }
                for (int i = 0; i < 512; i++) {
                    Data.DMX512Data[i] = 0;
                }
            }
        });
        JButton button3 = new JButton("满值");
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 20; i++) {
                    sliders[i].setValue(255);
                }
            }
        });
        JButton button4 = new JButton("全部满值");
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 20; i++) {
                    sliders[i].setValue(255);
                }
                for (int i = 0; i < 512; i++) {
                    Data.DMX512Data[i] = (byte) 255;
                }
            }
        });
        p3.add(button1);
        p3.add(button2);
        p3.add(button3);
        p3.add(button4);

        dialog.add(p1);
        dialog.add(p2);
        dialog.add(p3);
    }

    /**
     * 发送预览数据
     */
    private void outDevice() {
        byte[] buff = new byte[512 + 8];
        buff[0] = (byte) 0xBB;
        buff[1] = (byte) 0x55;
        buff[2] = (byte) (520 / 256);
        buff[3] = (byte) (520 % 256);
        buff[4] = (byte) 0x80;
        buff[5] = (byte) 0x01;
        buff[6] = (byte) 0xFF;
        for (int j = 0; j < 512; j++) {
            buff[j + 7] = Data.DMX512Data[j];
        }
        buff[519] = ZhiLingJi.getJiaoYan(buff);
        Socket.SendData(buff);
    }

    /**
     * 保存数据
     */
    private void saveData() {
        for (int i = 0; i < 20; i++) {
            if (selected == 25) {
                if (i < 12) {
                    Data.DMX512Data[20 * selected + i] = (byte) sliders[i].getValue();
                }
            } else {
                Data.DMX512Data[20 * selected + i] = (byte) sliders[i].getValue();
            }
        }
    }

    /**
     * 设置数据
     */
    private void setData() {
        if (selected == 25) {
            for (int i = 0; i < 20; i++) {
                if (i >= 12) {
                    names[i].setVisible(false);
                    textFields[i].setVisible(false);
                    sliders[i].setVisible(false);
                } else {
                    names[i].setText(20 * selected + (i + 1) + "");
                    textFields[i].setText(Byte.toUnsignedInt(Data.DMX512Data[20 * selected + i]) + "");
                    sliders[i].setValue(Byte.toUnsignedInt(Data.DMX512Data[20 * selected + i]));
                }
            }
        } else {
            for (int i = 0; i < 20; i++) {
                if (selected == 0) {
                    if (i >= 9) {
                        names[i].setText((i + 1) + "");
                    } else {
                        names[i].setText("0" + (i + 1));
                    }
                } else {
                    names[i].setText(20 * selected + (i + 1) + "");
                }
                textFields[i].setText(Byte.toUnsignedInt(Data.DMX512Data[20 * selected + i]) + "");
                sliders[i].setValue(Byte.toUnsignedInt(Data.DMX512Data[20 * selected + i]));
                names[i].setVisible(true);
                textFields[i].setVisible(true);
                sliders[i].setVisible(true);
            }
        }
    }
}
