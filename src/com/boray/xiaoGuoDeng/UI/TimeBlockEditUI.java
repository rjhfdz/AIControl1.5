package com.boray.xiaoGuoDeng.UI;

import java.awt.*;
import java.awt.event.*;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.*;
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

import bezier.BezierDialog;

import com.boray.Data.ChannelName;
import com.boray.Data.Data;
import com.boray.Data.TuXingAction;
import com.boray.Data.XiaoGuoDengModel;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.IconJDialog;
import com.boray.Utils.Socket;
import com.boray.dengKu.UI.NewJTable;
import com.boray.fileCompare.Compare;
import com.boray.mainUi.MainUi;
import com.boray.usb.LastPacketData;
import com.boray.usb.UsbUtil;
import com.boray.xiaoGuoDeng.ArtNetReview;
import com.boray.xiaoGuoDeng.Listener.CopyToTimeBlockEdit;
import com.boray.xiaoGuoDeng.reviewBlock.TimeBlockReviewActionListener;
import com.boray.xiaoGuoDeng.reviewBlock.TimeBlockStopReviewActionListener;
import com.boray.xiaoGuoDeng.reviewByPc.TimeBlockReviewByPc;
import com.boray.xiaoGuoDeng.reviewCode.ReviewUtils;

public class TimeBlockEditUI {
    private IconJDialog dialog;
    private JCheckBox[] checkBoxs;
    private JCheckBox[] checkBoxs2;//备用
    private int dengKuNumber;//灯库位置
    private NewJTable table = null;
    private JSlider[] sliders;
    private JTextField[] textFields;
    private JLabel[] names;
    private JLabel stepLabel;//步数
    Vector vector88 = null;
    boolean[][] bn = null;
    boolean[][] bn2 = null;
    private HashMap hashMap = null;
    private int tt;//通道数
    private String preSelect = "0";
    //////预览使用的参数
    private int channelCount = 0;//通道数量
    private int[] startAddress;//每个灯的起始地址

    private int preBtnLength, preChannelCnt;
    private List<Integer> selectPre = new ArrayList<>();

    private JToggleButton[] buttons;
    private ButtonGroup group;

    private int dengZuNum, suCaiNum;
    //预览监听
    private TimeBlockReviewActionListener timeBlockReviewActionListener;
    //停止预览
    private TimeBlockStopReviewActionListener timeBlockStopReviewActionListener;

    private String suCaiName;

    private String typeString = "";

    /**
     * 效果灯块编辑界面
     *
     * @param str   txt字符串
     * @param dengZuNum     灯组下标
     * @param suCaiNum      素材下标
     */
    public void show(String str, int dengZuNum, int suCaiNum) {
        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new IconJDialog(f, true);
        dialog.setResizable(false);
        this.dengZuNum = dengZuNum;
        this.suCaiNum = suCaiNum;
        suCaiName = str;
        int block = Integer.valueOf(str.substring(0, str.indexOf("(")));

        dialog.setTitle("场景编程-模式" + XiaoGuoDengModel.model + "-时间块" + block + "   素材：" + suCaiName);
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        //flowLayout.setVgap(2);
        dialog.getContentPane().setLayout(flowLayout);
        int width = 900, height = 670;
        dialog.setSize(width, height);
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - width / 2, f.getLocation().y + f.getSize().height / 2 - height / 2);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        ///////////////
        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(700, 38));
        FlowLayout flowLayout3 = new FlowLayout(FlowLayout.LEFT);
        flowLayout3.setVgap(1);
        p1.setLayout(flowLayout3);
        p1.add(new JLabel("组别"));
        JTextField field = new JTextField(12);
        p1.add(field);
        p1.add(new JLabel("灯具数量"));
        JTextField field3 = new JTextField(12);
        p1.add(field3);
        field.setEnabled(false);
        field3.setEnabled(false);

        int cnt = 1;//灯具数量
        TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(dengZuNum);
        NewJTable GroupTable = (NewJTable) MainUi.map.get("GroupTable");
        cnt = treeSet.size();
        field3.setText(cnt + "");
        field.setText(GroupTable.getValueAt(dengZuNum, 2).toString());
        if (cnt > 0) {
            NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//所有灯具
            int i = (int) treeSet.first();
            typeString = table3.getValueAt(i, 3).toString();//灯具型号
        }
        if (!typeString.equals("")) {
            dengKuNumber = Integer.valueOf(typeString.split("#")[0].substring(2)).intValue() - 1;

            ///////////////
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            tabbedPane.setFocusable(false);
            tabbedPane.setPreferredSize(new Dimension(880, 590));

            hashMap = (HashMap) Data.SuCaiObjects[dengZuNum][suCaiNum];
            if (hashMap == null) {
                hashMap = new HashMap<>();
                Data.SuCaiObjects[dengZuNum][suCaiNum] = hashMap;
            }
            List list66 = (List) hashMap.get("channelData");
            if (list66 != null) {
                vector88 = (Vector) list66.get(0);
                bn = (boolean[][]) list66.get(1);
                bn2 = (boolean[][]) list66.get(2);
            }

            //获得DMX起始地址
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

            JPanel channelPane = new JPanel();
            setChannelPane(channelPane);
            tabbedPane.add("通道编程", channelPane);

            dialog.getContentPane().add(p1);
            dialog.getContentPane().add(tabbedPane);

            dialog.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    save();
                }
            });
            dialog.setVisible(true);
        } else {
            JFrame frame = (JFrame) MainUi.map.get("frame");
            JOptionPane.showMessageDialog(frame, "该组别还没有灯具，请添加灯具！", "提示", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    void save() {
        if (table == null) {
            return;
        }

        //////////步骤//////////
        List allList = new ArrayList();
        if (table.getRowCount() > 0) {
            DefaultTableModel modelTemp = (DefaultTableModel) table.getModel();
            Vector temp = (Vector) modelTemp.getDataVector().clone();
            allList.add(temp);
        } else {
            allList.add(null);
        }

        /////////勾选/////////
        for (int i = 0; i < channelCount; i++) {//保存当前数据
            bn[Integer.valueOf(preSelect)][i] = checkBoxs[i].isSelected();
            bn2[Integer.valueOf(preSelect)][i] = checkBoxs2[i].isSelected();
        }
        allList.add(bn);
        allList.add(bn2);

        hashMap.put("channelData", allList);
    }

    void setChannelTop(JScrollPane scrollPane) {
        HashMap map = (HashMap) Data.DengKuList.get(dengKuNumber);
        tt = Integer.valueOf((String) Data.DengKuChannelCountList.get(dengKuNumber)).intValue();

        scrollPane.setPreferredSize(new Dimension(860, 250));
        scrollPane.setBorder(new LineBorder(Color.gray));

        JPanel pane = new JPanel();
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
        flowLayout2.setHgap(0);
        pane.setLayout(flowLayout2);
        pane.setPreferredSize(new Dimension(1360, 225));
        //TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "调光通道", TitledBorder.LEFT, TitledBorder.TOP,new Font(Font.SERIF, Font.BOLD, 12));
        //tgPane.setBorder(tb);
        JPanel lefPane = new JPanel();
        //lefPane.setBorder(new LineBorder(Color.black));
        //lefPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,-4));
        lefPane.setPreferredSize(new Dimension(60, 225));
        lefPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        JPanel nullPane = new JPanel();
        nullPane.setPreferredSize(new Dimension(60, 160));
        lefPane.add(nullPane);
        JLabel huaBuJLabel = new JLabel("<html>通道全选</html>");
        huaBuJLabel.setPreferredSize(new Dimension(60, 24));
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
        JLabel huaBuJLabel2 = new JLabel("<html>渐变全选</html>");
        huaBuJLabel2.setPreferredSize(new Dimension(60, 24));
        huaBuJLabel2.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                JLabel label = (JLabel) e.getSource();
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            public void mouseReleased(MouseEvent e) {
                boolean b = checkBoxs2[0].isSelected();
                for (int i = 0; i < checkBoxs2.length; i++) {
                    if (checkBoxs2[i].isEnabled()) {
                        checkBoxs2[i].setSelected(!b);
                    }
                }
            }
        });
        lefPane.add(huaBuJLabel2);
        //lefPane.add(new JLabel("DMX"));
        pane.add(lefPane);

        int count = 32;
        JPanel[] itemPanes = new JPanel[count];
        JLabel[] labels = new JLabel[count];
        textFields = new JTextField[count];
        sliders = new JSlider[count];
        checkBoxs = new JCheckBox[count];
        checkBoxs2 = new JCheckBox[count];
        names = new JLabel[count];
        final JLabel[] DmxValues = new JLabel[count];
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(1);
        for (int i = 0; i < count; i++) {
            final int a = i;
            itemPanes[i] = new JPanel();
            itemPanes[i].setLayout(flowLayout);
            //itemPanes[i].setBorder(new LineBorder(Color.black));
            itemPanes[i].setPreferredSize(new Dimension(41, 210));
            if (i > 8) {
                labels[i] = new JLabel((i + 1) + "");
            } else {
                labels[i] = new JLabel("0" + (i + 1));
            }
            textFields[i] = new JTextField();
            textFields[i].setText(0 + "");
            textFields[i].setPreferredSize(new Dimension(36, 27));
            sliders[i] = new JSlider(JSlider.VERTICAL, 0, 255, 0);
            //sliders[i].setRequestFocusEnabled(true);
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
            sliders[i].setPreferredSize(new Dimension(18, 88));
            checkBoxs[i] = new JCheckBox();
            checkBoxs[i].setName("" + i);
            checkBoxs2[i] = new JCheckBox();
            checkBoxs2[i].setName("" + i);
            names[i] = new JLabel("<html>未知<br><br></html>", JLabel.CENTER);
            Font f1 = new Font("宋体", Font.PLAIN, 12);
            sliders[i].addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    textFields[a].setText(String.valueOf(sliders[a].getValue()));
                    int[] slt = table.getSelectedRows();
                    if (slt.length > 0) {
                        if (group.getButtonCount() <= 0) {
                            for (int k = 0; k < slt.length; k++) {
                                for (int i = 0; i < selectPre.size(); i++) {
                                    table.setValueAt(String.valueOf(sliders[a].getValue()), slt[k], a + (channelCount * Integer.valueOf(selectPre.get(i)).intValue()) + 2);
                                }
                            }
                        } else {
                            for (int k = 0; k < slt.length; k++) {
                                table.setValueAt(String.valueOf(sliders[a].getValue()), slt[k], a + (channelCount * Integer.valueOf(preSelect).intValue()) + 2);
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
                checkBoxs2[i].setEnabled(false);
                checkBoxs2[i].setSelected(false);
                sliders[i].setEnabled(false);
                textFields[i].setEnabled(false);
                names[i].setEnabled(false);
            }

            names[i].setFont(f1);
            names[i].setPreferredSize(new Dimension(42, 30));
            names[i].setBorder(BorderFactory.createEmptyBorder(0, 2, -10, 2));
            //names[i].setPreferredSize(new Dimension(42,30));
            DmxValues[i] = new JLabel("" + (i + 1));
            //names[i].setBorder(new LineBorder(Color.black));
            itemPanes[i].add(labels[i]);
            itemPanes[i].add(textFields[i]);
            itemPanes[i].add(sliders[i]);
            itemPanes[i].add(names[i]);
            itemPanes[i].add(checkBoxs[i]);
            itemPanes[i].add(checkBoxs2[i]);
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

    private void setChannelPane(JPanel pane) {
        JPanel sp2 = new JPanel();
        FlowLayout flowLayout_tt = new FlowLayout(FlowLayout.LEFT);
        flowLayout_tt.setVgap(0);
        sp2.setLayout(flowLayout_tt);
        sp2.setBorder(new LineBorder(Color.gray));
        sp2.setPreferredSize(new Dimension(860, 80));

        int cnt = 0;
        TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(dengZuNum);
        cnt = treeSet.size();

        buttons = new JToggleButton[cnt];
        group = new ButtonGroup();
        NewJTable table3 = (NewJTable) MainUi.map.get("table_dengJu");//所有灯具
        Iterator iterator = treeSet.iterator();
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JToggleButton btn = (JToggleButton) e.getSource();
                int start = Integer.valueOf(btn.getName()).intValue() * channelCount;

                //勾选
                for (int i = 0; i < channelCount; i++) {//保存当前数据
                    bn[Integer.valueOf(preSelect)][i] = checkBoxs[i].isSelected();
                    bn2[Integer.valueOf(preSelect)][i] = checkBoxs2[i].isSelected();
                }

                for (int i = 0; i < channelCount; i++) {
                    checkBoxs[i].setSelected(bn[Integer.valueOf(btn.getName()).intValue()][i]);
                    checkBoxs2[i].setSelected(bn2[Integer.valueOf(btn.getName()).intValue()][i]);
                }

                ///////////
                int[] tp = table.getSelectedRows();
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
                        ssString = table.getValueAt(select1, i + start + 2).toString();
                        textFields[i].setText(ssString);
                        sliders[i].setValue(Integer.valueOf(ssString).intValue());
                        sliders[i].addChangeListener(changeListener);
                    }
                }

                preSelect = btn.getName();
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
        }

        pane.add(sp2);

        //pane.setBorder(new LineBorder(Color.red));
        JScrollPane scrollPane = new JScrollPane();
        setChannelTop(scrollPane);

        JPanel p1 = new JPanel();
        p1.setBorder(new LineBorder(Color.gray));
        p1.setPreferredSize(new Dimension(860, 30));
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
        flowLayout.setVgap(-2);
        p1.setLayout(flowLayout);
        p1.add(button);
        p1.add(button2);
        p1.add(new JLabel("                                执行时长"));
        final JSlider slider = new JSlider(0, 10000);
//        slider.setValue(0);
        final JTextField field = new JTextField(4);
        field.setText("0");
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                field.setText(String.valueOf(slider.getValue()));
                int[] slt = table.getSelectedRows();
                if (slt.length > 0) {
                    for (int k = 0; k < slt.length; k++) {
                        table.setValueAt(String.valueOf(slider.getValue()), slt[k], 1);
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
        p1.add(slider);
        p1.add(field);
        p1.add(new JLabel("毫秒"));

        final JScrollPane p4 = new JScrollPane();
        p4.setBorder(new LineBorder(Color.gray));
        p4.setPreferredSize(new Dimension(680, 140));
        ////////////////////////////////////////////////////////////

        channelCount = Integer.valueOf((String) Data.DengKuChannelCountList.get(dengKuNumber)).intValue();
        int tt = channelCount * buttons.length;
        preBtnLength = 0;
        preChannelCnt = 0;
        if (bn == null || bn2 == null) {
            bn = new boolean[buttons.length][channelCount];
            bn2 = new boolean[buttons.length][channelCount];
        } else if (bn.length != buttons.length || bn[0].length != channelCount || bn2.length != buttons.length || bn2[0].length != channelCount) {
            boolean[][] tpt = bn.clone();
            bn = new boolean[buttons.length][channelCount];
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
                    bn[i][j] = tpt[i][j];
                }
            }
            tpt = bn2.clone();
            bn2 = new boolean[buttons.length][channelCount];
            preBtnLength = tpt.length;
            preChannelCnt = tpt[0].length;
            a = preBtnLength;
            b = preChannelCnt;
            if (tpt.length > buttons.length) {
                a = buttons.length;
            }
            if (tpt[0].length > channelCount) {
                b = channelCount;
            }
            for (int i = 0; i < a; i++) {
                for (int j = 0; j < b; j++) {
                    bn2[i][j] = tpt[i][j];
                }
            }
        }

        for (int i = 0; i < channelCount; i++) {
            checkBoxs[i].setSelected(bn[Integer.valueOf(preSelect).intValue()][i]);

            checkBoxs2[i].setSelected(bn2[Integer.valueOf(preSelect).intValue()][i]);
        }

        Object[] s = new String[tt + 2];
        final String[] temp = new String[tt + 2];
        temp[0] = "1";
        temp[1] = "2000";
        s[0] = "步骤";
        s[1] = "执行时长";
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
            for (int i = 0; i < vector88.size(); i++) {
                tp = (Vector) vector88.get(i);
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
                model.addRow(tp);
            }
        } else {
            model.addRow(temp);
        }
        table = new NewJTable(model, 0);
        /////////////////////////
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
            table.getColumn(table.getColumnName(i)).setCellRenderer(cellRenderer);
        }
        table.setSelectionBackground(new Color(56, 117, 215));
        table.getTableHeader().setUI(new BasicTableHeaderUI());
        table.getTableHeader().setReorderingAllowed(false);
        table.setOpaque(false);
        //table.setFocusable(false);
        table.setFont(new Font(Font.SERIF, Font.BOLD, 14));
        //table.getColumnModel().getColumn(1).setPreferredWidth(102);
        table.setRowHeight(15);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int[] tp = table.getSelectedRows();
                int select1 = -1;
                if (tp.length > 0) {
                    select1 = tp[tp.length - 1];
                }
                if (select1 > -1 && !e.getValueIsAdjusting()) {
                    ChangeListener listener = null;

                    listener = slider.getChangeListeners()[0];
                    slider.removeChangeListener(listener);
                    field.setText(table.getValueAt(select1, 1).toString());
                    slider.setValue(Integer.valueOf(table.getValueAt(select1, 1).toString()).intValue());
                    slider.addChangeListener(listener);
                    ChangeListener changeListener = null;
                    String ssString;
                    for (int i = 0; i < channelCount; i++) {
                        changeListener = sliders[i].getChangeListeners()[0];
                        sliders[i].removeChangeListener(changeListener);
                        ssString = table.getValueAt(select1, i + (Integer.valueOf(preSelect).intValue() * channelCount) + 2).toString();
                        textFields[i].setText(ssString);
                        sliders[i].setValue(Integer.valueOf(ssString).intValue());
                        sliders[i].addChangeListener(changeListener);
                    }
                    outDevice();
                }
            }
        });
        table.setRowSelectionInterval(0, 0);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        TableColumnModel tableColumnModel = table.getColumnModel();
        tableColumnModel.getColumn(0).setPreferredWidth(50);
        tableColumnModel.getColumn(1).setPreferredWidth(50);
        for (int i = 2; i < table.getColumnCount(); i++) {
            tableColumnModel.getColumn(i).setPreferredWidth(30);
        }

        p4.getHorizontalScrollBar().setUnitIncrement(30);
        p4.setViewportView(table);

        final JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("　复制　　　　　");
        JMenuItem menuItem1 = new JMenuItem("　粘贴　　　　　");
        int size = table.getRowCount();
        stepLabel = new JLabel("总步数:" + size);
        CopyToTimeBlockEdit copyListener = new CopyToTimeBlockEdit(table, stepLabel);
        menuItem.addActionListener(copyListener);
        menuItem1.addActionListener(copyListener);
        popupMenu.add(menuItem);
        popupMenu.add(menuItem1);
        table.setName("时间块");
        table.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    int s = table.rowAtPoint(new Point(e.getX(), e.getY()));
                    int[] a = table.getSelectedRows();
                    boolean select = false;
                    for (int i = 0; i < a.length; i++) {
                        if (s == a[i]) {
                            select = true;
                            break;
                        }
                    }
                    if (select) {
                        popupMenu.show(table, e.getX(), e.getY());
                    } else {
                        table.setRowSelectionInterval(s, s);
                        popupMenu.show(table, e.getX(), e.getY());
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
        ////////////////////////////////////////////////////////////
        pane.add(scrollPane);
        pane.add(p1);
//        pane.add(p3);
        pane.add(p4);
        JPanel p5 = new JPanel();
        p5.setPreferredSize(new Dimension(860, 30));
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
                if (table.getRowCount() < 32) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();
                    String[] s = temp;
                    s[0] = "" + (table.getRowCount() + 1);
                    model.addRow(temp);
                    table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
                    int size = table.getRowCount();
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
                int[] s = table.getSelectedRows();
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                for (int i = s.length - 1; i >= 0; i--) {
                    model.removeRow(s[i]);
                }
                for (int i = 0; i < table.getRowCount(); i++) {
                    table.setValueAt("" + (i + 1), i, 0);
                }
                int size = table.getRowCount();
                if (table.getSelectedRow() == -1) {
                    table.setRowSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
                }
                stepLabel.setText("总步数:" + size);
            }
        });
        p5.add(btn);
        p5.add(btn2);
        p5.add(new JLabel("                                                        "));
        p5.add(new JLabel("                                                        "));
        p5.add(stepLabel);
        pane.add(p5);
    }

    private void outDevice() {
        int[] slt = table.getSelectedRows();
        int value = 0;
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
            for (int j = 2; j < table.getColumnCount(); j++) {
                value = Integer.valueOf(table.getValueAt(slt[0], j).toString()).intValue();
                for (int i = 0; i < startAddress.length; i++) {
                    buff[j - 3 + startAddress[i] + 7] = (byte) value;
                }
            }
            buff[519] = ZhiLingJi.getJiaoYan(buff);
            if (Data.serialPort != null) {
                Socket.SerialPortSendData(buff);
            } else if (Data.socket != null) {
                Socket.UDPSendData(buff);
            }
            for (int j = 2; j < table.getColumnCount(); j++) {
                value = Integer.valueOf(table.getValueAt(slt[0], j).toString()).intValue();
                for (int i = 0; i < startAddress.length; i++) {
                    bytes[startAddress[i] + j - 3] = (byte) value;
                }
            }
            Socket.ArtNetSendData(bytes);//添加artNet数据协议发送
//            Socket.SerialPortSendData(bytes);
        }
    }

    private void outDevice_usb() {
        int[] slt = table.getSelectedRows();
        int value = 0;
        UsbPipe sendUsbPipe = (UsbPipe) MainUi.map.get("sendUsbPipe");
        if (sendUsbPipe != null && slt.length != 0) {
            byte[] buff = new byte[512];
            byte[] temp = new byte[64];
            int[] tl = new int[3];
            for (int j = 0; j < 2; j++) {
                tl[j] = Integer.valueOf(table.getValueAt(slt[0], j).toString()).intValue();
            }
            for (int j = 2; j < table.getColumnCount(); j++) {
                value = Integer.valueOf(table.getValueAt(slt[0], j).toString()).intValue();
                for (int i = 0; i < startAddress.length; i++) {
                    buff[j - 3 + startAddress[i]] = (byte) value;
                }
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
