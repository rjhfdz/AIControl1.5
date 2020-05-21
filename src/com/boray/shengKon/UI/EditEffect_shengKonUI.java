package com.boray.shengKon.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStream;
import java.util.*;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.usb.UsbPipe;

import com.boray.Data.ChannelName;
import com.boray.Data.Data;
import com.boray.Data.MyData;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.IconJDialog;
import com.boray.Utils.Socket;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.suCai.reviewBlock.TimeBlockReviewData;
import com.boray.usb.LastPacketData;
import com.boray.usb.UsbUtil;
import com.boray.xiaoGuoDeng.Listener.CopyToTimeBlockEdit;

public class EditEffect_shengKonUI {
    private NewJTable runTable;
    private JCheckBox[] checkBoxs;
    private JTextField[] textFields;
    private JSlider[] sliders;
    private JLabel[] names;
    //private JLabel[] DmxValues;
    private JSlider slider;
    private JTextField field;
    public boolean[] bs;
    //public static int selected = -1;
    private String groupNum;
    private String number;
    private JToggleButton[] buttons;
    Vector vector88 = null;
    String typeString = "";
    String preSelect = "0";
    private List<Integer> selectPre = new ArrayList<>();
    ;
    int channelCount = 0;
    Map map88;
    boolean[][] gouXuanValus;
    int[] al;
    private int preBtnLength, preChannelCnt;

    private int[] startAddress;//每个灯的起始地址

    private JComboBox duoDengCtrlBox;//多灯控制
    private JPanel pane;
    private JLabel stepLabel;//步数

    private ButtonGroup group;
    private int denKuNum,suCaiNum;

    public void show(int block, String groupNum, String number, JPanel pane) {
        this.groupNum = groupNum;
        this.number = number;
        this.pane = pane;
        IconJDialog dialog = new IconJDialog();
        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new IconJDialog(f, true);
        dialog.setResizable(false);
        dialog.setTitle("声控编程-模式" + MyData.ShengKonModel + "-时间块" + block);
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        //flowLayout.setVgap(2);
        dialog.getContentPane().setLayout(flowLayout);
        int width = 900, height = 630;
        dialog.setSize(width, height);
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - width / 2, f.getLocation().y + f.getSize().height / 2 - height / 2);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                save();
            }
        });

        JPanel p1 = new JPanel();
        setP1(p1);
        if (!typeString.equals("")) {

            int i = 0;

            i = number.contains("(") ? Integer.parseInt(number.substring(0, number.indexOf("("))) : Integer.parseInt(number);
            DefineJLable_shengKon2 label = (DefineJLable_shengKon2) pane.getComponent(i - 1);
            i = Integer.parseInt(label.getText().substring(label.getText().indexOf("(") + 1, label.getText().indexOf(")")));


            map88 = (Map) Data.ShengKonSuCai[Integer.valueOf(groupNum).intValue() - 1][i - 1];
//			map88 = (Map)Data.ShengKonEditObjects[Integer.valueOf(MyData.ShengKonModel).intValue()-1][Integer.valueOf(groupNum).intValue()-1][block-1];
            this.denKuNum = Integer.valueOf(groupNum).intValue() - 1;
            this.suCaiNum = i - 1;
            if (map88 == null) {
                map88 = new HashMap<>();
//				Data.ShengKonEditObjects[Integer.valueOf(MyData.ShengKonModel).intValue()-1][Integer.valueOf(groupNum).intValue()-1][block-1] = map88;
                Data.ShengKonSuCai[Integer.valueOf(groupNum).intValue() - 1][i - 1] = map88;
            }
            vector88 = (Vector) map88.get("0");
            gouXuanValus = (boolean[][]) map88.get("1");
            al = (int[]) map88.get("2");
            if (al == null) {
                al = new int[11];//a[10]表示多灯控制
                al[5] = 1;
                al[7] = 1;
                al[9] = 1;
            }

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            tabbedPane.setFocusable(false);
            tabbedPane.setPreferredSize(new Dimension(880, 550));

            JPanel channelPane = new JPanel();
            setChannelPane(channelPane);
            tabbedPane.add("通道控制", channelPane);


            JPanel jiaSuDuPane = new JPanel();
            setJiaSuDuPane(jiaSuDuPane);
            tabbedPane.add(" 加速度  ", jiaSuDuPane);

            dialog.getContentPane().add(p1);
            dialog.getContentPane().add(tabbedPane);
            dialog.setVisible(true);
        } else {
            JFrame frame = (JFrame) MainUi.map.get("frame");
            JOptionPane.showMessageDialog(frame, "该组别还没有灯具，请添加灯具！", "提示", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    void setChannelTop(JScrollPane scrollPane) {
        int dengKuNumber = Integer.valueOf(typeString.split("#")[0].substring(2)).intValue() - 1;
        HashMap map = (HashMap) Data.DengKuList.get(dengKuNumber);
        int tt = Integer.valueOf((String) Data.DengKuChannelCountList.get(dengKuNumber)).intValue();
        scrollPane.setPreferredSize(new Dimension(858, 200));
        scrollPane.setBorder(new LineBorder(Color.gray));

        JPanel pane = new JPanel();
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
        flowLayout2.setHgap(0);
        pane.setLayout(flowLayout2);
        pane.setPreferredSize(new Dimension(1510, 180));
        //TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "调光通道", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
        //tgPane.setBorder(tb);
        JPanel lefPane = new JPanel();
        //lefPane.setBorder(new LineBorder(Color.black));
        //lefPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,-4));
        lefPane.setPreferredSize(new Dimension(26, 180));
        lefPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        JPanel nullPane = new JPanel();
        nullPane.setPreferredSize(new Dimension(20, 144));
        lefPane.add(nullPane);
        JLabel huaBuJLabel = new JLabel("<html>全选</html>");
        huaBuJLabel.setPreferredSize(new Dimension(32, 24));
        huaBuJLabel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseReleased(MouseEvent e) {
                boolean b = checkBoxs[0].isSelected();
                for (int i = 0; i < checkBoxs.length; i++) {
                    if (checkBoxs[i].isEnabled()) {
                        checkBoxs[i].setSelected(!b);
                    }
                }
            }
        });
        lefPane.add(huaBuJLabel);
        //lefPane.add(new JLabel("DMX"));
        pane.add(lefPane);

        int count = 32;
        JPanel[] itemPanes = new JPanel[count];
        JLabel[] labels = new JLabel[count];
        textFields = new JTextField[count];
        sliders = new JSlider[count];
        checkBoxs = new JCheckBox[count];
        names = new JLabel[count];
        //DmxValues = new JLabel[count];
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(1);
        for (int i = 0; i < count; i++) {
            final int a = i;
            itemPanes[i] = new JPanel();
            itemPanes[i].setLayout(flowLayout);
            //itemPanes[i].setBorder(new LineBorder(Color.black));
            itemPanes[i].setPreferredSize(new Dimension(46, 180));
            if (i > 8) {
                labels[i] = new JLabel((i + 1) + "");
            } else {
                labels[i] = new JLabel("0" + (i + 1));
            }
            textFields[i] = new JTextField();
            textFields[i].setText(0 + "");
            textFields[i].setPreferredSize(new Dimension(36, 27));
            sliders[i] = new JSlider(JSlider.VERTICAL, 0, 255, 0);
            sliders[i].setPreferredSize(new Dimension(18, 76));
            checkBoxs[i] = new JCheckBox();
            checkBoxs[i].setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            checkBoxs[i].addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    JCheckBox checkBox = (JCheckBox) e.getSource();
                    gouXuanValus[Integer.valueOf(preSelect).intValue()][a] = checkBox.isSelected();
                }
            });
            checkBoxs[i].setName("" + i);
            //names[i] = new JLabel("<html>未知<br><br></html>",JLabel.CENTER);
            names[i] = new JLabel(ChannelName.getChangeName("未知"), JLabel.CENTER);
            Font f1 = new Font("宋体", Font.PLAIN, 12);
            sliders[i].addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    textFields[a].setText(String.valueOf(sliders[a].getValue()));
                    int[] slt = runTable.getSelectedRows();
                    if (slt.length > 0) {
                        if (group.getButtonCount() <= 0) {
                            for (int k = 0; k < slt.length; k++) {
                                for (int i = 0; i < selectPre.size(); i++) {
                                    runTable.setValueAt(String.valueOf(sliders[a].getValue()), slt[k], a + (channelCount * Integer.valueOf(selectPre.get(i)).intValue()) + 2);
                                }
                            }
                        } else {
                            for (int k = 0; k < slt.length; k++) {
                                runTable.setValueAt(String.valueOf(sliders[a].getValue()), slt[k], a + (channelCount * Integer.valueOf(preSelect).intValue()) + 2);
                            }
                        }
                    }
                }
            });
            sliders[i].addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    outDevice();
                }
            });
            sliders[i].addMouseWheelListener(new MouseWheelListener() {
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
            if (i > tt - 1) {
                checkBoxs[i].setEnabled(false);
                checkBoxs[i].setSelected(false);
                sliders[i].setEnabled(false);
                textFields[i].setEnabled(false);
                names[i].setEnabled(false);
            }
            names[i].setFont(f1);
            names[i].setPreferredSize(new Dimension(42, 30));
            //DmxValues[i] = new JLabel(""+(i+1));
            names[i].setBorder(BorderFactory.createEmptyBorder(-10, 0, -10, 0));
            //names[i].setBorder(new LineBorder(Color.black));
            itemPanes[i].add(labels[i]);
            itemPanes[i].add(textFields[i]);
            itemPanes[i].add(sliders[i]);
            itemPanes[i].add(names[i]);
            itemPanes[i].add(checkBoxs[i]);
            //itemPanes[i].add(DmxValues[i]);
            pane.add(itemPanes[i]);
        }
        for (int j = 0; j < tt; j++) {
            if (j < 16) {
                names[j].setText(ChannelName.getChangeName(map.get("L" + j).toString()));
            } else {
                names[j].setText(ChannelName.getChangeName(map.get("R" + (j - 16)).toString()));
            }
        }
        scrollPane.getHorizontalScrollBar().setUnitIncrement(30);
        scrollPane.setViewportView(pane);
    }

    void setChannelTop222(JScrollPane scrollPane) {

        scrollPane.setPreferredSize(new Dimension(860, 220));
        scrollPane.setBorder(new LineBorder(Color.gray));

        JPanel pane = new JPanel();
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
        flowLayout2.setHgap(0);
        pane.setLayout(flowLayout2);
        pane.setPreferredSize(new Dimension(1180, 196));
        //TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "调光通道", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
        //tgPane.setBorder(tb);
        JPanel lefPane = new JPanel();
        //lefPane.setBorder(new LineBorder(Color.black));
        //lefPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,-4));
        lefPane.setPreferredSize(new Dimension(26, 196));
        lefPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        JPanel nullPane = new JPanel();
        nullPane.setPreferredSize(new Dimension(20, 162));
        lefPane.add(nullPane);
        JLabel huaBuJLabel = new JLabel("<html>全选</html>");
        huaBuJLabel.setPreferredSize(new Dimension(32, 24));
        huaBuJLabel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseReleased(MouseEvent e) {
                boolean b = checkBoxs[0].isSelected();
                for (int i = 0; i < checkBoxs.length; i++) {
                    checkBoxs[i].setSelected(!b);
                }
            }
        });
        lefPane.add(huaBuJLabel);
        //lefPane.add(new JLabel("DMX"));
        pane.add(lefPane);

        int count = 32;
        JPanel[] itemPanes = new JPanel[count];
        JLabel[] labels = new JLabel[count];
        final JTextField[] textFields = new JTextField[count];
        final JSlider[] sliders = new JSlider[count];
        checkBoxs = new JCheckBox[count];
        JLabel[] names = new JLabel[count];
        final JLabel[] DmxValues = new JLabel[count];

        for (int i = 0; i < count; i++) {
            final int a = i;
            itemPanes[i] = new JPanel();
            FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
            flowLayout.setVgap(0);
            itemPanes[i].setLayout(flowLayout);
            //itemPanes[i].setBorder(new LineBorder(Color.black));
            itemPanes[i].setPreferredSize(new Dimension(36, 196));
            if (i > 8) {
                labels[i] = new JLabel((i + 1) + "");
            } else {
                labels[i] = new JLabel("0" + (i + 1));
            }
            textFields[i] = new JTextField();
            textFields[i].setText(0 + "");
            textFields[i].setPreferredSize(new Dimension(36, 27));
            sliders[i] = new JSlider(JSlider.VERTICAL, 0, 255, 0);
            sliders[i].setPreferredSize(new Dimension(18, 88));
            checkBoxs[i] = new JCheckBox();
            checkBoxs[i].setName("" + i);
            names[i] = new JLabel("<html>未知<br><br></html>", JLabel.CENTER);
            Font f1 = new Font("宋体", Font.PLAIN, 12);
            sliders[i].addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {

                    textFields[a].setText(String.valueOf(sliders[a].getValue()));

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

            names[i].setFont(f1);
            //names[i].setPreferredSize(new Dimension(42,30));
            DmxValues[i] = new JLabel("" + (i + 1));
            //names[i].setBorder(new LineBorder(Color.black));
            itemPanes[i].add(labels[i]);
            itemPanes[i].add(textFields[i]);
            itemPanes[i].add(sliders[i]);
            itemPanes[i].add(names[i]);
            itemPanes[i].add(checkBoxs[i]);
            //itemPanes[i].add(DmxValues[i]);
            pane.add(itemPanes[i]);
        }

        scrollPane.getHorizontalScrollBar().setUnitIncrement(30);
        scrollPane.setViewportView(pane);
    }

    private void setChannelPane(JPanel pane) {
        JPanel sp = new JPanel();
        FlowLayout flowLayout_t = new FlowLayout(FlowLayout.CENTER);
        flowLayout_t.setVgap(0);
        sp.setLayout(flowLayout_t);
        //sp.setBorder(new LineBorder(Color.gray));
        sp.setPreferredSize(new Dimension(860, 32));
        sp.add(new JLabel("多灯控制"));
        JComboBox duoDengKonZhiBox = new JComboBox(new String[]{"独立控制", "全部控制"});
        sp.add(duoDengKonZhiBox);
        JPanel N8 = new JPanel();
        N8.setPreferredSize(new Dimension(480, 30));
        sp.add(N8);
        JComboBox dengGuTypeBox = new JComboBox();
        for (int i = 1; i < 9; i++) {
            dengGuTypeBox.addItem("灯具型号" + i);
        }
        sp.add(dengGuTypeBox);
        JButton copyBtn = new JButton("灯具复制");
        sp.add(copyBtn);
        //pane.add(sp);

        JPanel sp2 = new JPanel();
        FlowLayout flowLayout_tt = new FlowLayout(FlowLayout.LEFT);
        flowLayout_tt.setVgap(0);
        sp2.setLayout(flowLayout_tt);
        sp2.setBorder(new LineBorder(Color.gray));
        sp2.setPreferredSize(new Dimension(860, 80));

        int cnt = 0;//灯具数量
        int number = Integer.valueOf(groupNum).intValue();
        TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(number - 1);
        cnt = treeSet.size();

        buttons = new JToggleButton[cnt];
        group = new ButtonGroup();
        NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//所有灯具
        Iterator iterator = treeSet.iterator();
        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (group.getButtonCount() <= 0) {
                    selectPre.clear();
                    for (int i = 0; i < buttons.length; i++) {
                        if (buttons[i].isSelected()) {
                            selectPre.add(i);
                        }
                    }
                    return;
                }

                JToggleButton btn = (JToggleButton) e.getSource();
                int start = Integer.valueOf(btn.getName()).intValue() * channelCount;

                //勾选
                for (int i = 0; i < channelCount; i++) {
                    ItemListener listener = checkBoxs[i].getItemListeners()[0];
                    checkBoxs[i].removeItemListener(listener);
                    checkBoxs[i].setSelected(gouXuanValus[Integer.valueOf(btn.getName()).intValue()][i]);
                    checkBoxs[i].addItemListener(listener);
                }

                //////////////////////////////
                int[] tp = runTable.getSelectedRows();
                int select1 = -1;
                if (tp.length > 0) {
                    select1 = tp[tp.length - 1];
                }
                if (select1 > -1) {
                    ChangeListener changeListener = null;
                    String ssString;
                    for (int i = 0; i < channelCount; i++) {
                        changeListener = sliders[i].getChangeListeners()[0];
                        sliders[i].removeChangeListener(changeListener);
                        ssString = runTable.getValueAt(select1, i + start + 2).toString();
                        textFields[i].setText(ssString);
                        sliders[i].setValue(Integer.valueOf(ssString).intValue());
                        sliders[i].addChangeListener(changeListener);
                    }
                }

                preSelect = btn.getName();
				/*if (!preSelect.equals(btn.getName())) {
					DefaultTableModel model = (DefaultTableModel)runTable.getModel();
					if (runTable.getRowCount() > 0) {
						Vector temp = (Vector)model.getDataVector().clone();
						allMap.put(preSelect, temp);
					}
					for (int i = runTable.getRowCount()-1; i >= 0; i--) {
						model.removeRow(i);
					}
					preSelect = btn.getName();
					Vector vector = (Vector)allMap.get(preSelect);
					if (vector != null) {
						Vector tp = null;
						for (int i = 0; i < vector.size(); i++) {
							tp = (Vector)vector.get(i);
							model.addRow(tp);
						}
						runTable.setRowSelectionInterval(0, 0);
					}
				}*/
            }
        };
        for (int i = 0; i < buttons.length; i++) {
            int cc = (int) iterator.next();
            buttons[i] = new JToggleButton(table3.getValueAt(cc, 2).toString());
            buttons[i].setMargin(new Insets(0, -10, 0, -10));
            buttons[i].setPreferredSize(new Dimension(102, 36));
            buttons[i].setName("" + i);
            buttons[i].addActionListener(listener);
            group.add(buttons[i]);
            sp2.add(buttons[i]);
        }
        if (buttons.length > 0) {
            buttons[0].setSelected(true);
            if (al[10] == 1) {
                for (int i = 1; i < buttons.length; i++) {
                    buttons[i].setEnabled(false);
                }
            }
        }
        pane.add(sp2);

        JScrollPane scrollPane = new JScrollPane();
        setChannelTop(scrollPane);

        JPanel p1 = new JPanel();
        p1.setBorder(new LineBorder(Color.gray));
        p1.setPreferredSize(new Dimension(860, 34));
        JButton button = new JButton("清零");
        JButton button2 = new JButton("满值");
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("清零".equals(e.getActionCommand())) {
                    for (int i = 0; i < sliders.length; i++) {
                        if (sliders[i].isEnabled()) {
                            sliders[i].setValue(1);
                            sliders[i].setValue(0);
                        }
                    }
                } else {
                    for (int i = 0; i < sliders.length; i++) {
                        if (sliders[i].isEnabled()) {
                            sliders[i].setValue(1);
                            sliders[i].setValue(255);
                        }
                    }
                }
                outDevice();
            }
        };
        button.addActionListener(actionListener);
        button2.addActionListener(actionListener);
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(0);
        p1.setLayout(flowLayout);
        p1.add(button);
        p1.add(button2);

        //JPanel p3 = new JPanel();
        //p3.setLayout(flowLayout);
        //p3.setBorder(new LineBorder(Color.gray));
        //p3.setPreferredSize(new Dimension(860,40));
        p1.add(new JLabel("亮灯时长"));
        slider = new JSlider(0, 10000);
        slider.setPreferredSize(new Dimension(140, 30));
        p1.add(slider);
        field = new JTextField(4);
        p1.add(field);
        p1.add(new JLabel("毫秒    "));
        p1.add(new JLabel("多灯控制:"));
        duoDengCtrlBox = new JComboBox(new String[]{"独立控制", "全部控制"});
        duoDengCtrlBox.setSelectedIndex(al[10]);
        duoDengCtrlBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent ee) {
                if (ee.getStateChange() == ItemEvent.SELECTED) {
                    al[10] = duoDengCtrlBox.getSelectedIndex();
                    if (duoDengCtrlBox.getSelectedIndex() == 0) {//独立
                        for (int i = 0; i < buttons.length; i++) {
                            buttons[i].setEnabled(true);
                        }
                    } else {//全部
                        for (int i = 1; i < buttons.length; i++) {
                            buttons[i].setEnabled(false);
                            buttons[i].setSelected(false);
                        }
                        if (!buttons[0].isSelected()) {
                            buttons[0].setSelected(true);
                            preSelect = buttons[0].getName();

                            JToggleButton btn = buttons[0];
                            int start = Integer.valueOf(btn.getName()).intValue() * channelCount;

                            //勾选
                            for (int i = 0; i < channelCount; i++) {
                                ItemListener listener = checkBoxs[i].getItemListeners()[0];
                                checkBoxs[i].removeItemListener(listener);
                                checkBoxs[i].setSelected(gouXuanValus[Integer.valueOf(btn.getName()).intValue()][i]);
                                checkBoxs[i].addItemListener(listener);
                            }

                            //////////////////////////////
                            int[] tp = runTable.getSelectedRows();
                            int select1 = -1;
                            if (tp.length > 0) {
                                select1 = tp[tp.length - 1];
                            }
                            if (select1 > -1) {
                                ChangeListener changeListener = null;
                                String ssString;
                                for (int i = 0; i < channelCount; i++) {
                                    changeListener = sliders[i].getChangeListeners()[0];
                                    sliders[i].removeChangeListener(changeListener);
                                    ssString = runTable.getValueAt(select1, i + start + 2).toString();
                                    textFields[i].setText(ssString);
                                    sliders[i].setValue(Integer.valueOf(ssString).intValue());
                                    sliders[i].addChangeListener(changeListener);
                                }
                            }
                        }
                    }
                }
            }
        });
        p1.add(duoDengCtrlBox);
        slider.setValue(0);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                field.setText(String.valueOf(slider.getValue()));
                int[] slt = runTable.getSelectedRows();
                if (slt.length > 0) {
                    for (int k = 0; k < slt.length; k++) {
                        runTable.setValueAt(String.valueOf(slider.getValue()), slt[k], 1);
                    }
                }
            }
        });
        field.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field.getText()).intValue();
                    if (tb == slider.getValue()) {
                        slider.setValue(tb - 1);
                    }
                    slider.setValue(tb);
                }
            }
        });

        JCheckBox checkBox = new JCheckBox("独立时长");
        JCheckBox checkBox2 = new JCheckBox("统一时长");
        if (al[0] == 0) {
            checkBox.setSelected(true);
        } else if (al[0] == 1) {
            checkBox2.setSelected(true);
        }
        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                al[0] = 0;
            }
        });
        checkBox2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                al[0] = 1;
            }
        });
        ButtonGroup group2 = new ButtonGroup();
        group2.add(checkBox);
        group2.add(checkBox2);
        p1.add(checkBox);
        p1.add(checkBox2);
		
		/*JPanel p4 = new JPanel();
		p4.setLayout(new FlowLayout(FlowLayout.LEFT));
		p4.setBorder(new LineBorder(Color.gray));
		p4.setPreferredSize(new Dimension(860,40));
		p4.add(new JLabel("熄灯通道"));
		JComboBox b1 = new JComboBox();
		JComboBox b2 = new JComboBox();
		JComboBox b3 = new JComboBox();
		JComboBox b4 = new JComboBox();
		b1.setPreferredSize(new Dimension(88,30));
		b2.setPreferredSize(new Dimension(88,30));
		b3.setPreferredSize(new Dimension(88,30));
		b4.setPreferredSize(new Dimension(88,30));
		b1.setEnabled(false);b2.setEnabled(false);b3.setEnabled(false);b4.setEnabled(false);
		p4.add(b1);p4.add(b2);p4.add(b3);p4.add(b4);*/


        JPanel p2 = new JPanel();
        setTable(p2);

        pane.add(scrollPane);
        pane.add(p1);
        pane.add(p2);
    }

    void setTable(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setHgap(0);
        pane.setLayout(flowLayout);
        //pane.setBorder(new LineBorder(Color.gray));
        //pane.setBorder(new LineBorder(Color.red));
        pane.setPreferredSize(new Dimension(858, 192));

        ///////////////////////////////////////////////////////////
        final JScrollPane p4 = new JScrollPane();
        p4.setBorder(new LineBorder(Color.gray));
        //p4.setPreferredSize(new Dimension(856,110));
        p4.setPreferredSize(new Dimension(856, 142));
        ////////////////////////////////////////////////////////////
        int dengKuNumber = Integer.valueOf(typeString.split("#")[0].substring(2)).intValue() - 1;
        channelCount = Integer.valueOf((String) Data.DengKuChannelCountList.get(dengKuNumber)).intValue();

        int tt = channelCount * buttons.length;
        preBtnLength = 0;
        preChannelCnt = 0;
        if (gouXuanValus == null) {
            gouXuanValus = new boolean[buttons.length][channelCount];
        } else if (gouXuanValus.length != buttons.length || gouXuanValus[0].length != channelCount) {
            boolean[][] tpt = gouXuanValus.clone();
            gouXuanValus = new boolean[buttons.length][channelCount];
            preBtnLength = tpt.length;
            preChannelCnt = tpt[0].length;
            int a = preBtnLength, b = preChannelCnt;
            if (tpt.length > buttons.length) {
                a = buttons.length;
            }
            if (tpt[0].length > channelCount) {
                b = channelCount;
            }
            for (int i = 0; i < a; i++) {
                for (int j = 0; j < b; j++) {
                    gouXuanValus[i][j] = tpt[i][j];
                }
            }
        }
        for (int i = 0; i < channelCount; i++) {
            ItemListener listener = checkBoxs[i].getItemListeners()[0];
            checkBoxs[i].removeItemListener(listener);
            checkBoxs[i].setSelected(gouXuanValus[Integer.valueOf(preSelect).intValue()][i]);
            checkBoxs[i].addItemListener(listener);
        }

        Object[] s = new String[tt + 2];
        final String[] temp = new String[tt + 2];
        temp[0] = "1";
        temp[1] = "0";
        s[0] = "步骤";
        s[1] = "亮灯时长";
        for (int i = 2; i < s.length; i++) {
            s[i] = "" + ((i - 1) % channelCount);
            if (((i - 1) % channelCount) == 0) {
                s[i] = "" + channelCount;
            }
            temp[i] = "0";
        }
        Object[][] data88 = {};
        DefaultTableModel model = new DefaultTableModel(data88, s);
        if (vector88 != null) {
            Vector tp = null;
            //int a = 0;
            for (int i = 0; i < vector88.size(); i++) {
                tp = (Vector) vector88.get(i);
                // a = tp.size();
                //if (a < tt+2) {
                if (preChannelCnt < channelCount) {
                    for (int j = 0; j < preBtnLength; j++) {
                        for (int j2 = preChannelCnt; j2 < channelCount; j2++) {
                            if (j == preBtnLength - 1) {
                                tp.add("0");
                            } else {
                                tp.insertElementAt("0", j * preChannelCnt + 2 + j2 + (j * (channelCount - preChannelCnt)));
                            }
                        }
                    }
                }

                //} else {
                if (preChannelCnt > channelCount) {
                    for (int j = preBtnLength - 1; j >= 0; j--) {
                        for (int j2 = preChannelCnt - 1; j2 >= channelCount; j2--) {
                            if (j == preBtnLength - 1) {
                                tp.removeElementAt(j * preChannelCnt + 2 + j2);
                            } else {
                                tp.removeElementAt(j * preChannelCnt + 2 + j2);
                            }
                        }
                    }
                }
                int a = tp.size();
                for (int j = 0; j < tt + 2 - a; j++) {
                    tp.add("0");
                }
                //}
                model.addRow(tp);
            }
        } else {
            model.addRow(temp);
        }
        runTable = new NewJTable(model, 0);
        ///////////////////////////////
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        cell.setBackground(new Color(237, 243, 254));
                        cell.setForeground(Color.black);
                    } else {
                        cell.setBackground(Color.white); //设置偶数行底色
                        cell.setForeground(Color.black);
                    }
                } else {
                    cell.setBackground(new Color(56, 117, 215));
                    cell.setForeground(Color.white);
                }
                return cell;
            }
        };
        for (int i = 0; i < s.length; i++) {
            //runTable.getColumn(runTable.getColumnName(i)).setCellRenderer(cellRenderer);
            runTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        runTable.setSelectionBackground(new Color(56, 117, 215));
        runTable.getTableHeader().setUI(new BasicTableHeaderUI());
        runTable.getTableHeader().setReorderingAllowed(false);
        runTable.setOpaque(false);
        //runTable.setFocusable(false);
        runTable.setFont(new Font(Font.SERIF, Font.BOLD, 14));
        //runTable.getColumnModel().getColumn(1).setPreferredWidth(102);
        runTable.setRowHeight(15);
        runTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int[] tp = runTable.getSelectedRows();
                int select1 = -1;
                if (tp.length > 0) {
                    select1 = tp[tp.length - 1];
                }
                if (select1 > -1 && !e.getValueIsAdjusting()) {
                    ChangeListener listener = null;
                    NewJTable table_DkGl = (NewJTable) MainUi.map.get("table_DkGl");

                    listener = slider.getChangeListeners()[0];
                    slider.removeChangeListener(listener);
                    field.setText(runTable.getValueAt(select1, 1).toString());
                    slider.setValue(Integer.valueOf(runTable.getValueAt(select1, 1).toString()).intValue());
                    slider.addChangeListener(listener);


                    //int toggleButtonSelect = selected;
					
					/*if (selected != -1) {
						//JSlider[] ChannelValueSliders = (JSlider[])MainUi.map.get("ChannelValueSliders");
						//JTextField[] textFields = (JTextField[])MainUi.map.get("ChannelValueFields");
						NewJTable table_dengJu = (NewJTable)MainUi.map.get("table_dengJu");
						
						int channelCount = Integer.valueOf(table_dengJu.getValueAt(selected, 6).toString()).intValue();
						int start = Integer.valueOf(table_dengJu.getValueAt(selected, 5).toString()).intValue();
						for (int i = 0; i < channelCount; i++) {
							listener = sliders[i].getChangeListeners()[0];
							sliders[i].removeChangeListener(listener);
							textFields[i].setText(runTable.getValueAt(select1, start+i+1).toString());
							sliders[i].setValue(Integer.valueOf(runTable.getValueAt(select1, start+i+1).toString()).intValue());
							sliders[i].addChangeListener(listener);
						}
					}*/
                    ChangeListener changeListener = null;
                    String ssString;
                    for (int i = 0; i < channelCount; i++) {
                        changeListener = sliders[i].getChangeListeners()[0];
                        sliders[i].removeChangeListener(changeListener);
                        ssString = runTable.getValueAt(select1, i + (Integer.valueOf(preSelect).intValue() * channelCount) + 2).toString();
                        textFields[i].setText(ssString);
                        sliders[i].setValue(Integer.valueOf(ssString).intValue());
                        sliders[i].addChangeListener(changeListener);
                    }
                    outDevice();
                }
            }
        });
        if (runTable.getRowCount() > 0) {
            runTable.setRowSelectionInterval(0, 0);
        }
        runTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel tableColumnModel = runTable.getColumnModel();
        tableColumnModel.getColumn(0).setPreferredWidth(50);
        tableColumnModel.getColumn(1).setPreferredWidth(50);
        for (int i = 2; i < runTable.getColumnCount(); i++) {
            tableColumnModel.getColumn(i).setPreferredWidth(30);
        }

        p4.getHorizontalScrollBar().setUnitIncrement(30);
        p4.setViewportView(runTable);

        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("　复制　　　　　");
        JMenuItem menuItem1 = new JMenuItem("　粘贴　　　　　");
        int size = runTable.getRowCount();
        stepLabel = new JLabel("总步数:" + size);
        CopyToTimeBlockEdit copyListener = new CopyToTimeBlockEdit(runTable,stepLabel);
        menuItem.addActionListener(copyListener);
        menuItem1.addActionListener(copyListener);
        popupMenu.add(menuItem);
        popupMenu.add(menuItem1);
        runTable.setName("声控");
        runTable.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int s = runTable.rowAtPoint(new Point(e.getX(), e.getY()));
                    int[] a = runTable.getSelectedRows();
                    boolean select = false;
                    for (int i = 0; i < a.length; i++) {
                        if (s == a[i]) {
                            select = true;
                            break;
                        }
                    }
                    if (select) {
                        popupMenu.show(runTable, e.getX(), e.getY());
                    } else {
                        runTable.setRowSelectionInterval(s, s);
                        popupMenu.show(runTable, e.getX(), e.getY());
                    }
                }
            }
        });
        p4.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.show(p4, e.getX(), e.getY());
                }
            }
        });

        JPanel p5 = new JPanel();
        p5.setPreferredSize(new Dimension(840, 40));
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
        flowLayout2.setVgap(-2);
        flowLayout2.setHgap(0);
        p5.setLayout(flowLayout2);
        JButton btn = new JButton("添加");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (buttons.length == 0) {
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "添加失败，请先添加灯具！", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (runTable.getRowCount() < 32) {
                    DefaultTableModel model = (DefaultTableModel) runTable.getModel();
                    String[] s = temp;
                    s[0] = "" + (runTable.getRowCount() + 1);
                    model.addRow(temp);
                    runTable.setRowSelectionInterval(runTable.getRowCount() - 1, runTable.getRowCount() - 1);
                    int size = runTable.getRowCount();
                    stepLabel.setText("总步数:" + size);
                } else {
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "添加失败，总步骤数不能超过32步！", "提示", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });
        JButton btn2 = new JButton("删除");
        btn2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] s = runTable.getSelectedRows();
                DefaultTableModel model = (DefaultTableModel) runTable.getModel();
                for (int i = s.length - 1; i >= 0; i--) {
                    model.removeRow(s[i]);
                }
                for (int i = 0; i < runTable.getRowCount(); i++) {
                    runTable.setValueAt("" + (i + 1), i, 0);
                }
                stepLabel.setText("总步数:" + size);
            }
        });

        final JButton button1 = new JButton("预览");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            button1.setEnabled(false);
                            save();
                            TimeBlockReviewData.sendShengKonData(denKuNum,suCaiNum,startAddress,channelCount,duoDengCtrlBox);
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        } finally {
                            button1.setEnabled(true);
                        }
                    }
                }).start();
            }
        });
        JButton button3 = new JButton("停止预览");
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TimeBlockReviewData.stopShengKonData();
            }
        });

        p5.add(btn);
        p5.add(btn2);
        p5.add(new JLabel("                                      "));
        p5.add(button1);
        p5.add(button3);
        p5.add(new JLabel("                                                                         "));
        p5.add(stepLabel);
        pane.add(p4);
        pane.add(p5);
    }

    private void setJiaSuDuPane(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(0);
        pane.setLayout(flowLayout);

        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);

        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(600, 30));
        p1.setLayout(flowLayout2);
        JRadioButton radioButton = new JRadioButton("启用");
        JRadioButton radioButton2 = new JRadioButton("不启用");
        if (al[1] == 0) {
            radioButton.setSelected(true);
        } else if (al[1] == 1) {
            radioButton2.setSelected(true);
        }
        radioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                al[1] = 0;
            }
        });
        radioButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                al[1] = 1;
            }
        });

        ButtonGroup group = new ButtonGroup();
        group.add(radioButton);
        group.add(radioButton2);
        p1.add(radioButton);
        p1.add(new JLabel("          "));
        p1.add(radioButton2);


        JPanel p2 = new JPanel();
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "运动加速", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p2.setBorder(tb);
        p2.setPreferredSize(new Dimension(620, 80));
        final JCheckBox box = new JCheckBox("图形加速");
        if (al[2] == 0) {
            box.setSelected(false);
        } else {
            box.setSelected(true);
        }
        box.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (box.isSelected()) {
                    al[2] = 1;
                } else {
                    al[2] = 0;
                }
            }
        });
        p2.add(box);
        final JSlider slider = new JSlider(0, 100);
        slider.setPreferredSize(new Dimension(450, 30));
        p2.add(slider);
        final JTextField field = new JTextField(4);
        p2.add(field);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                al[3] = slider.getValue();
                field.setText(String.valueOf(slider.getValue()));
            }
        });
        field.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field.getText()).intValue();
                    if (tb == slider.getValue()) {
                        slider.setValue(tb - 1);
                    }
                    slider.setValue(tb);
                }
            }
        });
        slider.setValue(al[3]);
        field.setText(String.valueOf(al[3]));

        JPanel p3 = new JPanel();
        FlowLayout flowLayout3 = new FlowLayout(FlowLayout.CENTER);
        flowLayout3.setVgap(8);
        p3.setLayout(flowLayout3);
        TitledBorder tb1 = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "通道加速", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p3.setBorder(tb1);
        p3.setPreferredSize(new Dimension(620, 180));

        final JCheckBox box1 = new JCheckBox("通道速度1");
        if (al[4] == 0) {
            box1.setSelected(false);
        } else {
            box1.setSelected(true);
        }
        box1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (box1.isSelected()) {
                    al[4] = 1;
                } else {
                    al[4] = 0;
                }
            }
        });
        final JSlider slider1 = new JSlider(1, 100);
        slider1.setPreferredSize(new Dimension(450, 30));
        final JTextField field1 = new JTextField(4);
        slider1.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                al[5] = slider1.getValue();
                field1.setText(String.valueOf(slider1.getValue()));
            }
        });
        field1.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field1.getText()).intValue();
                    if (tb == slider1.getValue()) {
                        slider1.setValue(tb - 1);
                    }
                    slider1.setValue(tb);
                }
            }
        });
        slider1.setValue(al[5]);
        field1.setText(String.valueOf(al[5]));

        final JCheckBox box2 = new JCheckBox("通道速度2");
        if (al[6] == 0) {
            box2.setSelected(false);
        } else {
            box2.setSelected(true);
        }
        box2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (box2.isSelected()) {
                    al[6] = 1;
                } else {
                    al[6] = 0;
                }
            }
        });
        final JSlider slider2 = new JSlider(1, 100);
        slider2.setPreferredSize(new Dimension(450, 30));
        final JTextField field2 = new JTextField(4);
        slider2.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                al[7] = slider2.getValue();
                field2.setText(String.valueOf(slider2.getValue()));
            }
        });
        field2.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field2.getText()).intValue();
                    if (tb == slider2.getValue()) {
                        slider2.setValue(tb - 1);
                    }
                    slider2.setValue(tb);
                }
            }
        });
        slider2.setValue(al[7]);
        field2.setText(String.valueOf(al[7]));

        final JCheckBox box3 = new JCheckBox("通道速度3");
        if (al[8] == 0) {
            box3.setSelected(false);
        } else {
            box3.setSelected(true);
        }
        box3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (box3.isSelected()) {
                    al[8] = 1;
                } else {
                    al[8] = 0;
                }
            }
        });
        final JSlider slider3 = new JSlider(1, 100);
        slider3.setPreferredSize(new Dimension(450, 30));
        final JTextField field3 = new JTextField(4);
        slider3.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                al[9] = slider3.getValue();
                field3.setText(String.valueOf(slider3.getValue()));
            }
        });
        field3.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field3.getText()).intValue();
                    if (tb == slider3.getValue()) {
                        slider3.setValue(tb - 1);
                    }
                    slider3.setValue(tb);
                }
            }
        });
        slider3.setValue(al[9]);
        field3.setText(String.valueOf(al[9]));

        p3.add(box1);
        p3.add(slider1);
        p3.add(field1);

        p3.add(box2);
        p3.add(slider2);
        p3.add(field2);

        p3.add(box3);
        p3.add(slider3);
        p3.add(field3);

        pane.add(p1);
        pane.add(p2);
        pane.add(p3);
    }

    void setP1(JPanel pane) {
        pane.setPreferredSize(new Dimension(860, 34));
        //pane.setBorder(new LineBorder(Color.gray));
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(1);
        pane.setLayout(flowLayout);


        NewJTable GroupTable = (NewJTable) MainUi.map.get("GroupTable");
        int number = Integer.valueOf(groupNum).intValue();

        String zhuBieName = GroupTable.getValueAt(number - 1, 2).toString();//组别名称
        int cnt = 0;//灯具数量
        TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(number - 1);
        cnt = treeSet.size();
        if (cnt > 0) {
            NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//所有灯具
            int i = (int) treeSet.first();
            typeString = table3.getValueAt(i, 3).toString();//灯具型号

        }

        NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//所有灯具
        //每个灯的起始地址
        startAddress = new int[cnt];
        Iterator iterator = treeSet.iterator();
        int aa = 0;
        while (iterator.hasNext()) {
            int cc = (int) iterator.next();
            startAddress[aa] = Integer.valueOf(table3.getValueAt(cc, 5).toString()).intValue();
            aa++;
        }

        pane.add(new JLabel("组别"));
        JTextField field = new JTextField(12);
        field.setEnabled(false);
        field.setText(zhuBieName);
        pane.add(field);
        pane.add(new JLabel("灯具型号"));
        JTextField field2 = new JTextField(12);
        field2.setEnabled(false);
        field2.setText(typeString);
        pane.add(field2);
        pane.add(new JLabel("灯具数量"));
        JTextField field3 = new JTextField(12);
        field3.setEnabled(false);
        field3.setText(cnt + "");
        JButton button = new JButton("单选");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("单选")) {
                    for (int i = 0; i < buttons.length; i++) {
                        group.remove(buttons[i]);
                    }
                    button.setText("多选");
                } else {
                    for (int i = 0; i < buttons.length; i++) {
                        group.add(buttons[i]);
                    }
                    button.setText("单选");
                }
            }
        });
        pane.add(field3);
        pane.add(button);
    }

    void save(){
        if (runTable.getRowCount() > 0) {
            DefaultTableModel modelTemp = (DefaultTableModel) runTable.getModel();
            Vector temp = (Vector) modelTemp.getDataVector().clone();
            map88.put("0", temp);
        } else {
            map88.put("0", null);
        }
        map88.put("1", gouXuanValus);
        map88.put("2", al);
    }
    private void outDevice() {
        int[] slt = runTable.getSelectedRows();
        int value = 0;
        int i = 0, ii = 0;
        if (slt.length != 0) {
            byte[] buff = new byte[512 + 8];
            byte[] bytes = new byte[512];
            buff[0] = (byte) 0xBB;
            buff[1] = (byte) 0x55;
            buff[2] = (byte) (520 / 256);
            buff[3] = (byte) (520 % 256);
            buff[4] = (byte) 0x80;
            buff[5] = (byte) 0x01;
            buff[6] = (byte) 0xFF;
            if ("独立控制".equals(duoDengCtrlBox.getSelectedItem().toString())) {
                for (int j = 2; j < runTable.getColumnCount(); j++) {
                    value = Integer.valueOf(runTable.getValueAt(slt[0], j).toString()).intValue();
                    i = (j - 2) / channelCount;
                    ii = (j - 2) % channelCount;
                    buff[ii + startAddress[i] - 1 + 7] = (byte) value;
                }
            } else if ("全部控制".equals(duoDengCtrlBox.getSelectedItem().toString())) {
                for (int j = 2; j < channelCount + 2; j++) {
                    value = Integer.valueOf(runTable.getValueAt(slt[0], j).toString()).intValue();
                    ii = (j - 2) % channelCount;
                    for (int j2 = 0; j2 < startAddress.length; j2++) {
                        buff[ii + startAddress[j2] - 1 + 7] = (byte) value;
                    }
                }
            }
            buff[519] = ZhiLingJi.getJiaoYan(buff);
            if (Data.serialPort != null) {
                Socket.SerialPortSendData(buff);
            } else if (Data.socket != null) {
                Socket.UDPSendData(buff);
            }
            System.arraycopy(buff, 8, bytes, 0, 512);
            Socket.ArtNetSendData(bytes);//添加artNet数据协议发送
        }
    }

    private void outDevice_usb() {
        int[] slt = runTable.getSelectedRows();
        int value = 0;
        int i = 0, ii = 0;
        UsbPipe sendUsbPipe = (UsbPipe) MainUi.map.get("sendUsbPipe");
        if (sendUsbPipe != null && slt.length != 0) {
            byte[] buff = new byte[512];
            byte[] temp = new byte[64];
            int[] tl = new int[3];
            for (int j = 0; j < 2; j++) {
                tl[j] = Integer.valueOf(runTable.getValueAt(slt[0], j).toString()).intValue();
            }
            for (int j = 2; j < runTable.getColumnCount(); j++) {
                value = Integer.valueOf(runTable.getValueAt(slt[0], j).toString()).intValue();
                i = (j - 2) / channelCount;
                ii = (j - 2) % channelCount;
                buff[ii + startAddress[i] - 1] = (byte) value;
            }
            try {
                for (int k = 0; k < 8; k++) {
                    System.arraycopy(buff, k * 64, temp, 0, 64);
                    UsbUtil.sendMassge(sendUsbPipe, temp);
                }
                UsbUtil.sendMassge(sendUsbPipe, LastPacketData.getL(buff, tl));
            } catch (javax.usb.UsbPlatformException e2) {
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "数据写出发生错误！", "提示", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
