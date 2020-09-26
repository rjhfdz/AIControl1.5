package com.boray.suCai.UI;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.boray.Data.ChannelName;
import com.boray.Data.Data;
import com.boray.Data.ZhiLingJi;
import com.boray.Utils.IconJDialog;
import com.boray.Utils.Socket;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;
import com.boray.Utils.SuCaiUtil;
import com.boray.xiaoGuoDeng.Listener.CopyToTimeBlockEdit;
import com.boray.xiaoGuoDeng.reviewBlock.TimeBlockReviewActionListener;
import com.boray.xiaoGuoDeng.reviewBlock.TimeBlockStopReviewActionListener;

public class SuCaiEditUI {
    private IconJDialog dialog;
    private JCheckBox[] checkBoxs;//勾选
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
    //预览监听
    private TimeBlockReviewActionListener timeBlockReviewActionListener;
    //停止预览
    private TimeBlockStopReviewActionListener timeBlockStopReviewActionListener;

    private int dengZuNum, suCaiNum;

    private String typeString = "";

    /**
     * 素材编辑界面展示
     * @param suCaiName 素材名称
     * @param suCaiNum  素材下标
     * @param dengZuNum 灯组下标
     */
    public void show(String suCaiName, int suCaiNum, int dengZuNum) {
        dialog = new IconJDialog();
        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new IconJDialog(f, true);
        dialog.setResizable(false);
        this.dengZuNum = dengZuNum;
        this.suCaiNum = suCaiNum;
        //blockNum = block;
        //group_N = Integer.valueOf(groupNum).intValue();
        dialog.setTitle("编辑素材：" + suCaiName);
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        //flowLayout.setVgap(2);
        dialog.getContentPane().setLayout(flowLayout);
        int width = 900, height = 670;
        dialog.setSize(width, height);
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - width / 2, f.getLocation().y + f.getSize().height / 2 - height / 2);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        ///////////////
        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(860, 34));
        FlowLayout flowLayout3 = new FlowLayout(FlowLayout.LEFT);
        flowLayout3.setVgap(1);
        p1.setLayout(flowLayout3);
        p1.add(new Label("素材类型"));
        JTextField field4 = new JTextField(8);
        p1.add(field4);
        p1.add(new JLabel("灯具型号"));
        JTextField field2 = new JTextField(8);
        p1.add(field2);
        p1.add(new JLabel("灯具数量"));
        JTextField field5 = new JTextField(8);
        p1.add(field5);
        field2.setEnabled(false);
        field4.setEnabled(false);
        field5.setEnabled(false);

        int cnt = 1;//灯具数量
        TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(dengZuNum);
        cnt = treeSet.size();
        field5.setText(cnt + "");
        field4.setText(SuCaiUtil.getXiaoGuoDengType());
        JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");
        field2.setText(suCaiLightType.getSelectedValue().toString());
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

            hashMap = (HashMap) Data.SuCaiObjects[dengZuNum][suCaiNum - 1];
            if (hashMap == null) {
                hashMap = new HashMap<>();
                Data.SuCaiObjects[dengZuNum][suCaiNum - 1] = hashMap;
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
        //勾选
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
        p1.add(new JLabel("                                                     执行时长"));
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
        p4.setPreferredSize(new Dimension(860, 140));
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
            for (int j = 2; j < table.getColumnCount(); j++) {
                value = Integer.valueOf(table.getValueAt(slt[0], j).toString()).intValue();
                i = (j - 2) / channelCount;
                ii = (j - 2) % channelCount;
                buff[ii + startAddress[i] - 1 + 7] = (byte) value;
            }
            buff[519] = ZhiLingJi.getJiaoYan(buff);
            Socket.SendData(buff);
            System.arraycopy(buff, 8, bytes, 0, 512);
            Socket.ArtNetSendData(bytes);//添加artNet数据协议发送
        }
    }

    //预览
    private void outPreview() {
        if (Data.serialPort != null && hashMap != null) {
            List<Byte> buff = new ArrayList<Byte>();
            Map map = (Map) hashMap.get("actionXiaoGuoData");//动作图形
            if (map != null) {
                String[] values = (String[]) map.get("1");
                byte[] bt1 = new byte[5];
                if (values != null) {
                    if (values[0].equals("true")) {
                        bt1[0] = 1;
                    }
                    for (int l = 1; l < bt1.length; l++) {
                        bt1[l] = (byte) Integer.valueOf(values[l]).intValue();
                    }
                }
                addData(buff, bt1);
            } else {

            }
            List list66 = (List) hashMap.get("channelData");//手动编程配置
            if (list66 != null) {
                //////////勾选
                int r = 0, yu = 0;
                int[] bp1 = new int[4];
                boolean[] bn = (boolean[]) list66.get(1);
                for (int l = 0; l < bn.length; l++) {
                    r = l / 8;
                    yu = l % 8;
                    if (bn[l]) {
                        bp1[r] = bp1[r] + (1 << yu);
                    }
                }
                for (int l = bn.length; l < 32; l++) {
                    r = l / 8;
                    yu = l % 8;
                    bp1[r] = bp1[r] + (1 << yu);
                }
                byte[] bp2 = new byte[4];
                for (int l = 0; l < bp1.length; l++) {
                    bp2[l] = (byte) bp1[l];
                }
                addData(buff, bp2);
            }
        }
    }

    public List<Byte> addData(List<Byte> buff, byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            buff.add(bytes[i]);
        }
        return buff;
    }

}
