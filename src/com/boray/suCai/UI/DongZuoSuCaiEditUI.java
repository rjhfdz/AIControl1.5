package com.boray.suCai.UI;

import bezier.BezierDialog;
import com.boray.Data.Data;
import com.boray.Data.TuXingAction;
import com.boray.Utils.IconJDialog;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DongZuoSuCaiEditUI {
    private IconJDialog dialog;
    private HashMap hashMap = null;
    private List actionCompontList = null;

    public void show(String suCaiName, int suCaiNum) {
        dialog = new IconJDialog();
        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new IconJDialog(f, true);
        dialog.setResizable(false);
        actionCompontList = new ArrayList<>();
        dialog.setTitle("编辑素材：" + suCaiName);
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);

        dialog.getContentPane().setLayout(flowLayout);
        int width = 740, height = 570;
        dialog.setSize(width, height);
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - width / 2, f.getLocation().y + f.getSize().height / 2 - height / 2);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        hashMap = (HashMap) Data.SuCaiDongZuoObject[suCaiNum - 1];
        if (hashMap == null) {
            hashMap = new HashMap<>();
            Data.SuCaiDongZuoObject[suCaiNum - 1] = hashMap;
        }

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tabbedPane.setFocusable(false);
        tabbedPane.setPreferredSize(new Dimension(700, 500));

        JPanel donZuoPane = new JPanel();
        setDonZuoPane(donZuoPane);
        tabbedPane.add("动作效果", donZuoPane);

        dialog.getContentPane().add(tabbedPane);

        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                save();
            }
        });
        dialog.setVisible(true);
    }

    private void save() {
        Map map = (Map) hashMap.get("actionXiaoGuoData");
        if (map == null) {
            map = new HashMap<>();
        }
        JRadioButton radioButton = (JRadioButton) actionCompontList.get(0);
        if (radioButton.isSelected()) {
            map.put("0", "true");
        } else {
            map.put("0", "false");
        }
        JComboBox box = (JComboBox) actionCompontList.get(1);
        map.put("2", box.getSelectedIndex() + "");
        if (box.getSelectedIndex() >= 48) {
            String str = box.getSelectedItem().toString();
            map.put("6", str.substring(str.indexOf("(") + 1, str.indexOf(")")));
            map.put("7", bezier.Data.map.get("" + box.getSelectedIndex()));
        }

        JSlider slider = (JSlider) actionCompontList.get(2);
        map.put("3", slider.getValue() + "");

        String[] tp = new String[6];
        JComboBox box3 = (JComboBox) actionCompontList.get(3);
        tp[0] = box3.getSelectedIndex() + "";
        for (int i = 0; i < 4; i++) {
            JCheckBox box2 = (JCheckBox) actionCompontList.get(4 + i);
            tp[1 + i] = String.valueOf(box2.isSelected());
        }
        JSlider slider2 = (JSlider) actionCompontList.get(8);
        tp[5] = slider2.getValue() + "";
        map.put("4", tp);
        JCheckBox box4 = (JCheckBox) actionCompontList.get(9);
        map.put("5", box4.isSelected());

        hashMap.put("actionXiaoGuoData", map);
    }

    private void setDonZuoPane(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(0);
        pane.setLayout(flowLayout);

        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);

        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(500, 30));
        p1.setLayout(flowLayout);
        final JRadioButton radioButton = new JRadioButton("启用");
        JRadioButton radioButton2 = new JRadioButton("不启用");
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean a = false;
                if (radioButton.isSelected()) {
                    a = false;
                } else {
                    a = true;
                }
            }
        };
        radioButton.addActionListener(listener);
        radioButton2.addActionListener(listener);
        actionCompontList.add(radioButton);
        radioButton2.setSelected(true);
        ButtonGroup group = new ButtonGroup();
        group.add(radioButton);
        group.add(radioButton2);
        Map map = (Map) hashMap.get("actionXiaoGuoData");
        if (map != null) {
            String b = (String) map.get("0");
            if (b.equals("true")) {
                radioButton.setSelected(true);
            } else {
                radioButton2.setSelected(true);
            }
        }
        p1.add(radioButton);
        p1.add(new JLabel("          "));
        p1.add(radioButton2);

        JPanel p2 = new JPanel();
        //p2.setBorder(new LineBorder(Color.gray));
        p2.setPreferredSize(new Dimension(680, 50));
        p2.setLayout(flowLayout);
        JButton button = new JButton("自定义");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JFrame f = (JFrame) MainUi.map.get("frame");
                final JDialog dialog2 = new JDialog(f, true);
                dialog2.setResizable(false);
                dialog2.setTitle("自定义");
                dialog2.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
                int width = 280, height = 210;
                dialog2.setSize(width, height);
                dialog2.setLocation(f.getLocation().x + f.getSize().width / 2 - width / 2, f.getLocation().y + f.getSize().height / 2 - height / 2);
                dialog2.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                dialog2.add(new JLabel("运行状态:"));
                String[] values = null;
                Map map = (Map) hashMap.get("actionXiaoGuoData");
                if (map != null) {
                    values = (String[]) map.get("1");
                }
                final JRadioButton radioButton = new JRadioButton("滑步");
                final JRadioButton radioButton2 = new JRadioButton("跳帧");
                radioButton.setSelected(true);
                ButtonGroup group = new ButtonGroup();
                group.add(radioButton);
                group.add(radioButton2);
                dialog2.add(radioButton);
                dialog2.add(radioButton2);
                JPanel p1 = new JPanel();
                FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
                flowLayout2.setVgap(0);
                p1.setLayout(flowLayout2);
                p1.setBorder(new LineBorder(Color.gray));
                p1.setPreferredSize(new Dimension(220, 100));
                dialog2.add(p1);
                p1.add(new JLabel("                X"));
                p1.add(new JLabel("                  Y"));
                p1.add(new JLabel("      "));
                p1.add(new JLabel("     1:"));
                final JTextField field = new JTextField("0");
                field.setPreferredSize(new Dimension(80, 32));
                final JTextField field2 = new JTextField("0");
                field2.setPreferredSize(new Dimension(80, 32));
                field2.setPreferredSize(new Dimension(80, 32));
                p1.add(field);
                p1.add(field2);
                p1.add(new JLabel("     2:"));
                final JTextField field3 = new JTextField("0");
                final JTextField field4 = new JTextField("0");
                field3.setPreferredSize(new Dimension(80, 32));
                field4.setPreferredSize(new Dimension(80, 32));
                p1.add(field3);
                p1.add(field4);
                if (values != null) {
                    if (values[0].equals("true")) {
                        radioButton.setSelected(true);
                    } else {
                        radioButton2.setSelected(true);
                    }
                    field.setText(values[1]);
                    field2.setText(values[2]);
                    field3.setText(values[3]);
                    field4.setText(values[4]);
                }
                JButton button = new JButton("确定");
                JButton button2 = new JButton("取消");
                button2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        dialog2.dispose();
                    }
                });
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //获取动作效果数据
                        String[] temp = new String[5];
                        Map map = (Map) hashMap.get("actionXiaoGuoData");
                        if (map == null) {
                            map = new HashMap();
                        }
                        if (radioButton.isSelected()) {
                            temp[0] = "true";
                        } else {
                            temp[0] = "false";
                        }
                        temp[1] = field.getText();
                        temp[2] = field2.getText();
                        temp[3] = field3.getText();
                        temp[4] = field4.getText();
                        map.put("1", temp);
                        hashMap.put("actionXiaoGuoData", map);
                        dialog2.dispose();
                    }
                });
                dialog2.add(button);
                dialog2.add(new JLabel("   "));
                dialog2.add(button2);
                dialog2.setVisible(true);
            }
        });
        JButton button2 = new JButton("动作图形");
        p2.add(button);
        p2.add(button2);
        String[] tps = (String[]) bezier.Data.itemMap.get("0");
        if (tps == null) {
            tps = TuXingAction.getValus();
        }
        final JComboBox box = new JComboBox(tps);
        actionCompontList.add(box);
        if (map != null) {
            int selected = Integer.valueOf((String) map.get("2"));
            box.setSelectedIndex(selected);
        }
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new BezierDialog().show(0, box.getSelectedIndex(), new JComboBox[]{box});
            }
        });
        p2.add(box);
        p2.add(new JLabel("   "));

        JPanel p3 = new JPanel();
        //p3.setBorder(new LineBorder(Color.gray));
        p3.setPreferredSize(new Dimension(480, 50));
        p3.add(new JLabel("运行速度"));
        final JSlider slider = new JSlider(0, 100);
        actionCompontList.add(slider);
        slider.setValue(0);
        slider.setPreferredSize(new Dimension(340, 30));
        p3.add(slider);
        final JTextField field = new JTextField(4);
        field.setText("0");
        p3.add(field);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                field.setText(String.valueOf(slider.getValue()));
            }
        });
        field.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field.getText()).intValue();
                    slider.setValue(tb);
                }
            }
        });
        if (map != null) {
            int yunXinSpeed = Integer.valueOf((String) map.get("3"));
            slider.setValue(yunXinSpeed);
        }

        JPanel p4 = new JPanel();
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "多灯设置", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p4.setBorder(tb);
        p4.setPreferredSize(new Dimension(480, 280));
        p4.setLayout(flowLayout2);
        JPanel p4_to_p1 = new JPanel();
        p4_to_p1.setLayout(flowLayout2);
        p4_to_p1.setPreferredSize(new Dimension(380, 40));
        p4_to_p1.add(new JLabel("拆分"));
        JComboBox box2 = new JComboBox(TuXingAction.getValues4());
        actionCompontList.add(box2);
        JCheckBox box7 = new JCheckBox("拆分反向");
        p4_to_p1.add(box2);
        p4_to_p1.add(box7);

        JPanel p4_to_p2 = new JPanel();
        p4_to_p2.setLayout(flowLayout2);
        p4_to_p2.setPreferredSize(new Dimension(200, 70));
        JCheckBox box3 = new JCheckBox("X轴反向");
        JCheckBox box4 = new JCheckBox("半边反向");
        JCheckBox box5 = new JCheckBox("Y轴反向");
        JCheckBox box6 = new JCheckBox("半边反向");
        actionCompontList.add(box3);
        actionCompontList.add(box4);
        actionCompontList.add(box5);
        actionCompontList.add(box6);
        p4_to_p2.add(new JLabel("       "));
        p4_to_p2.add(box3);
        p4_to_p2.add(box4);
        p4_to_p2.add(new JLabel("       "));
        p4_to_p2.add(box5);
        p4_to_p2.add(box6);

        JPanel p4_to_p3 = new JPanel();
        p4_to_p3.add(new JLabel("    时差"));
        final JSlider slider3 = new JSlider(0, 5000);
        actionCompontList.add(slider3);
        actionCompontList.add(box7);
        slider3.setValue(0);
        slider3.setPreferredSize(new Dimension(310, 30));
        p4_to_p3.add(slider3);
        final JTextField field3 = new JTextField(4);
        field3.setText("0");
        p4_to_p3.add(field3);
        p4_to_p3.add(new JLabel("毫秒"));
        slider3.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                field3.setText(String.valueOf(slider3.getValue()));
            }
        });
        field3.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field3.getText()).intValue();
                    slider3.setValue(tb);
                }
            }
        });
        if (map != null) {
            String[] tp = (String[]) map.get("4");
            box2.setSelectedIndex(Integer.valueOf(tp[0]));
            boolean a, b, c, d;
            if ("true".equals(tp[1])) {
                a = true;
            } else {
                a = false;
            }
            if ("true".equals(tp[2])) {
                b = true;
            } else {
                b = false;
            }
            if ("true".equals(tp[3])) {
                c = true;
            } else {
                c = false;
            }
            if ("true".equals(tp[4])) {
                d = true;
            } else {
                d = false;
            }
            box3.setSelected(a);
            box4.setSelected(b);
            box5.setSelected(c);
            box6.setSelected(d);

            slider3.setValue(Integer.valueOf(tp[5]).intValue());
            boolean b1 = map.containsKey("5") ? (boolean) map.get("5") : false;
            box7.setSelected(b1);
        }

        p4.add(p4_to_p1);
        p4.add(p4_to_p2);
        p4.add(p4_to_p3);
        pane.add(p1);
        pane.add(p2);
        pane.add(p3);
        pane.add(p4);
    }
}
