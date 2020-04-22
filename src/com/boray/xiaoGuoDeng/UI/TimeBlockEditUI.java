package com.boray.xiaoGuoDeng.UI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

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
    private JDialog dialog;
    private JCheckBox[] checkBoxs;
    private JCheckBox[] checkBoxs2;//备用
    private int dengKuNumber;//灯库位置
    private NewJTable table = null;
    private JSlider[] sliders;
    private JTextField[] textFields;
    private JLabel[] names;
    private JComboBox box2;//拆分
    private JCheckBox checkBox;//拆分反向
    private JSlider slider2;//时差
    private JLabel stepLabel;//步数
    boolean rgb1 = false, rgb2 = false, rgb3 = false;
    boolean xy = false;
    Vector vector88 = null;
    boolean[] bn = null;
    boolean[] bn2 = null;
    String[] ddTemp = null;
    private HashMap hashMap = null;
    private List actionCompontList = null;
    private List rgb1CompontList = null;
    private List rgb1CompontList2 = null;
    private List rgb1CompontList3 = null;
    private int tt;//通道数
    private boolean flag = false;//判断素材是否新建
    //////预览使用的参数
    private int channelCount = 0;//通道数量
    private int[] startAddress;//每个灯的起始地址

    private int blockNum, group_N, index_N;
    private int denKuNum, suCaiNum;

    private JComboBox box81, box82, box83;
    //预览监听
    private TimeBlockReviewActionListener timeBlockReviewActionListener;
    //停止预览
    private TimeBlockStopReviewActionListener timeBlockStopReviewActionListener;

    public void show(int block, String groupNum, String index) {
        dialog = new JDialog();
        JFrame f = (JFrame) MainUi.map.get("frame");
        dialog = new JDialog(f, true);
        dialog.setResizable(false);
        actionCompontList = new ArrayList<>();
        rgb1CompontList = new ArrayList<>();
        rgb1CompontList2 = new ArrayList<>();
        rgb1CompontList3 = new ArrayList<>();
        blockNum = block;
        group_N = Integer.valueOf(groupNum).intValue();
        index_N = Integer.valueOf(index).intValue();
        dialog.setTitle("场景编程-模式" + XiaoGuoDengModel.model + "-时间块" + block);
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        //flowLayout.setVgap(2);
        dialog.getContentPane().setLayout(flowLayout);
        int width = 740, height = 590;
        dialog.setSize(width, height);
        dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - width / 2, f.getLocation().y + f.getSize().height / 2 - height / 2);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        timeBlockReviewActionListener = new TimeBlockReviewActionListener(XiaoGuoDengModel.model, group_N, blockNum, index_N);
        timeBlockStopReviewActionListener = new TimeBlockStopReviewActionListener(XiaoGuoDengModel.model, group_N, index_N);
        ///////////////
        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(700, 38));
        FlowLayout flowLayout3 = new FlowLayout(FlowLayout.LEFT);
        flowLayout3.setVgap(1);
        p1.setLayout(flowLayout3);
        p1.add(new JLabel("组别"));
        JTextField field = new JTextField(12);
        p1.add(field);
        p1.add(new JLabel("灯具型号"));
        JTextField field2 = new JTextField(12);
        p1.add(field2);
        p1.add(new JLabel("灯具数量"));
        JTextField field3 = new JTextField(12);
        p1.add(field3);
        field.setEnabled(false);
        field2.setEnabled(false);
        field3.setEnabled(false);
        ///////////////
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        tabbedPane.setFocusable(false);
        tabbedPane.setPreferredSize(new Dimension(700, 500));

        //////获取当前时间块参数
        int model = Integer.valueOf(XiaoGuoDengModel.model) - 1;
        int grpN = Integer.valueOf(groupNum).intValue() - 1;
        denKuNum = getxiaoGuoDengDengKuName();
        suCaiNum = block;
        hashMap = (HashMap) Data.SuCaiObjects[denKuNum - 1][suCaiNum - 1];
        if (hashMap == null) {
            flag = true;
            hashMap = new HashMap<>();
            Data.SuCaiObjects[denKuNum - 1][suCaiNum - 1] = hashMap;
        }
//		hashMap = (HashMap)Data.XiaoGuoDengObjects[model][grpN][blkN];
//		if (hashMap == null) {
//			hashMap = new HashMap<>();
//			Data.XiaoGuoDengObjects[model][grpN][blkN] = hashMap;
//		}
        List list66 = (List) hashMap.get("channelData");
        if (list66 != null) {
            vector88 = (Vector) list66.get(0);
            bn = (boolean[]) list66.get(1);
            ddTemp = (String[]) list66.get(2);
            bn2 = (boolean[]) list66.get(3);
        }
        //获取动作效果数据
        //Map map77 = (Map)hashMap.get("actionXiaoGuoData");
        /////////////////////////////
        dengKuNumber = -1;

        NewJTable GroupTable = (NewJTable) MainUi.map.get("GroupTable");
        int number = Integer.valueOf(groupNum).intValue();
        String typeString = "";//灯具型号
        String zhuBieName = GroupTable.getValueAt(number - 1, 2).toString();//组别名称
        int cnt = 0;//灯具数量
        TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(number - 1);
        cnt = treeSet.size();
        if (cnt != 0) {
            rgb1 = false;
            rgb2 = false;
            rgb3 = false;
            xy = false;

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

            int i = (int) treeSet.first();
            typeString = table3.getValueAt(i, 3).toString();
            dengKuNumber = Integer.valueOf(typeString.split("#")[0].substring(2)).intValue() - 1;
            HashMap map = (HashMap) Data.DengKuList.get(dengKuNumber);
            String tempString = "";
            int count = Integer.valueOf((String) Data.DengKuChannelCountList.get(dengKuNumber)).intValue();
            channelCount = count;
            for (int j = 0; j < count; j++) {
                tempString = "";
                if (j < 16) {
                    tempString = map.get("L" + j).toString();
                } else {
                    tempString = map.get("R" + (j - 16)).toString();
                }
                if (tempString.contains("X轴") || tempString.contains("Y轴")) {
                    xy = true;
                }
                if (tempString.contains("RGB")) {
                    if (tempString.contains("-1")) {
                        rgb1 = true;
                    } else if (tempString.contains("-2")) {
                        rgb2 = true;
                    } else if (tempString.contains("-3")) {
                        rgb3 = true;
                    }

                }
                if (xy && rgb1 && rgb2 && rgb3) {
                    break;
                }
            }
			/*Set<Entry<String, String>> set = map.entrySet();
			Iterator it = set.iterator();
			while (it.hasNext()) {
	            Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
	            if (entry.getValue().contains("X轴") || entry.getValue().contains("Y轴")) {
	            	System.out.println("aa:"+entry.getValue());
					xy = true;
				}
	            if (entry.getValue().contains("RGB")) {
	            	if (entry.getValue().contains("-1")) {
	            		rgb1 = true;
					} else if (entry.getValue().contains("-2")) {
						rgb2 = true;
					} else if (entry.getValue().contains("-3")) {
						rgb3 = true;
					}
	            	
				}
	            if (xy && rgb1 && rgb2 && rgb3) {
					break;
				}
	        }*/
            JPanel channelPane = new JPanel();
            setChannelPane(channelPane);
            tabbedPane.add("通道编程", channelPane);
            if (xy) {
                JPanel donZuoPane = new JPanel();
                setDonZuoPane(donZuoPane);
                tabbedPane.add("动作效果", donZuoPane);
            }
            if (rgb1) {
                JPanel RGBcolor1 = new JPanel();
                setRgbColor1(RGBcolor1);
                tabbedPane.add("RGB色彩1", RGBcolor1);
            }
            if (rgb2) {
                JPanel RGBcolor2 = new JPanel();
                setRgbColor2(RGBcolor2);
                tabbedPane.add("RGB色彩2", RGBcolor2);
            }
            if (rgb3) {
                JPanel RGBcolor3 = new JPanel();
                setRgbColor3(RGBcolor3);
                tabbedPane.add("RGB色彩3", RGBcolor3);
            }
        }
        field.setText(zhuBieName);
        field2.setText(typeString);
        field3.setText(cnt + "");
        dialog.getContentPane().add(p1);
        dialog.getContentPane().add(tabbedPane);

        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                save();
            }
        });
        dialog.setVisible(true);
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
        int a = table.getColumnCount() - 2;
        boolean[] b = new boolean[a];
        boolean[] b2 = new boolean[a];
        for (int i = 0; i < a; i++) {
            b[i] = checkBoxs[i].isSelected();
            b2[i] = checkBoxs2[i].isSelected();
        }
        allList.add(b);

        /////////多灯设置///////////
        String[] s = new String[3];
        s[0] = box2.getSelectedIndex() + "";//拆分
        if (checkBox.isSelected()) {
            s[1] = "1";//拆分反向
        } else {
            s[1] = "0";//拆分反向
        }
        s[2] = slider2.getValue() + "";//时差
        allList.add(s);
        allList.add(b2);
        hashMap.put("channelData", allList);

        if (xy) {
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

            hashMap.put("actionXiaoGuoData", map);
        } else {
            //hashMap.put("actionXiaoGuoData", null);
        }

        if (rgb1) {
            List list = (List) hashMap.get("rgb1Data");
            if (list == null) {
                list = new ArrayList<>();
            }
            list.clear();

            JRadioButton radioButton = (JRadioButton) rgb1CompontList.get(0);
            list.add(radioButton.isSelected());

            String[] tp = new String[3];
            JSlider slider = (JSlider) rgb1CompontList.get(1);
            tp[0] = slider.getValue() + "";
            slider = (JSlider) rgb1CompontList.get(2);
            tp[1] = slider.getValue() + "";
            slider = (JSlider) rgb1CompontList.get(3);
            tp[2] = slider.getValue() + "";
            list.add(tp);

            boolean[] bs = new boolean[3];
            JCheckBox box = (JCheckBox) rgb1CompontList.get(4);
            bs[0] = box.isSelected();
            box = (JCheckBox) rgb1CompontList.get(5);
            bs[1] = box.isSelected();
            box = (JCheckBox) rgb1CompontList.get(6);
            bs[2] = box.isSelected();
            list.add(bs);

            box = (JCheckBox) rgb1CompontList.get(7);
            list.add(box.isSelected());

            JComboBox box2 = (JComboBox) rgb1CompontList.get(8);
            list.add(String.valueOf(box2.getSelectedIndex()));

            box = (JCheckBox) rgb1CompontList.get(9);
            list.add(box.isSelected());

            slider = (JSlider) rgb1CompontList.get(10);
            list.add(String.valueOf(slider.getValue()));

            box2 = (JComboBox) rgb1CompontList.get(11);
            list.add(String.valueOf(box2.getSelectedIndex()));

            box = (JCheckBox) rgb1CompontList.get(12);
            list.add(box.isSelected());

            slider = (JSlider) rgb1CompontList.get(13);
            list.add(String.valueOf(slider.getValue()));

            hashMap.put("rgb1Data", list);

        } else {
            //hashMap.put("rgb1Data", null);
        }

        if (rgb2) {
            List list = (List) hashMap.get("rgb2Data");
            if (list == null) {
                list = new ArrayList<>();
            }
            list.clear();

            JRadioButton radioButton = (JRadioButton) rgb1CompontList2.get(0);
            list.add(radioButton.isSelected());

            String[] tp = new String[3];
            JSlider slider = (JSlider) rgb1CompontList2.get(1);
            tp[0] = slider.getValue() + "";
            slider = (JSlider) rgb1CompontList2.get(2);
            tp[1] = slider.getValue() + "";
            slider = (JSlider) rgb1CompontList2.get(3);
            tp[2] = slider.getValue() + "";
            list.add(tp);

            boolean[] bs = new boolean[3];
            JCheckBox box = (JCheckBox) rgb1CompontList2.get(4);
            bs[0] = box.isSelected();
            box = (JCheckBox) rgb1CompontList2.get(5);
            bs[1] = box.isSelected();
            box = (JCheckBox) rgb1CompontList2.get(6);
            bs[2] = box.isSelected();
            list.add(bs);

            box = (JCheckBox) rgb1CompontList2.get(7);
            list.add(box.isSelected());

            JComboBox box2 = (JComboBox) rgb1CompontList2.get(8);
            list.add(String.valueOf(box2.getSelectedIndex()));

            box = (JCheckBox) rgb1CompontList2.get(9);
            list.add(box.isSelected());

            slider = (JSlider) rgb1CompontList2.get(10);
            list.add(String.valueOf(slider.getValue()));

            box2 = (JComboBox) rgb1CompontList2.get(11);
            list.add(String.valueOf(box2.getSelectedIndex()));

            box = (JCheckBox) rgb1CompontList2.get(12);
            list.add(box.isSelected());

            slider = (JSlider) rgb1CompontList2.get(13);
            list.add(String.valueOf(slider.getValue()));

            hashMap.put("rgb2Data", list);

        } else {
            //hashMap.put("rgb2Data", null);
        }

        if (rgb3) {
            List list = (List) hashMap.get("rgb3Data");
            if (list == null) {
                list = new ArrayList<>();
            }
            list.clear();

            JRadioButton radioButton = (JRadioButton) rgb1CompontList3.get(0);
            list.add(radioButton.isSelected());

            String[] tp = new String[3];
            JSlider slider = (JSlider) rgb1CompontList3.get(1);
            tp[0] = slider.getValue() + "";
            slider = (JSlider) rgb1CompontList3.get(2);
            tp[1] = slider.getValue() + "";
            slider = (JSlider) rgb1CompontList3.get(3);
            tp[2] = slider.getValue() + "";
            list.add(tp);

            boolean[] bs = new boolean[3];
            JCheckBox box = (JCheckBox) rgb1CompontList3.get(4);
            bs[0] = box.isSelected();
            box = (JCheckBox) rgb1CompontList3.get(5);
            bs[1] = box.isSelected();
            box = (JCheckBox) rgb1CompontList3.get(6);
            bs[2] = box.isSelected();
            list.add(bs);

            box = (JCheckBox) rgb1CompontList3.get(7);
            list.add(box.isSelected());

            JComboBox box2 = (JComboBox) rgb1CompontList3.get(8);
            list.add(String.valueOf(box2.getSelectedIndex()));

            box = (JCheckBox) rgb1CompontList3.get(9);
            list.add(box.isSelected());

            slider = (JSlider) rgb1CompontList3.get(10);
            list.add(String.valueOf(slider.getValue()));

            box2 = (JComboBox) rgb1CompontList3.get(11);
            list.add(String.valueOf(box2.getSelectedIndex()));

            box = (JCheckBox) rgb1CompontList3.get(12);
            list.add(box.isSelected());

            slider = (JSlider) rgb1CompontList3.get(13);
            list.add(String.valueOf(slider.getValue()));

            hashMap.put("rgb3Data", list);

        } else {
            //hashMap.put("rgb3Data", null);
        }
    }

    void setChannelTop(JScrollPane scrollPane) {
        HashMap map = (HashMap) Data.DengKuList.get(dengKuNumber);
        tt = Integer.valueOf((String) Data.DengKuChannelCountList.get(dengKuNumber)).intValue();

        scrollPane.setPreferredSize(new Dimension(680, 250));
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
        if(flag){
            if(bn==null)
                bn = new boolean[tt];
            for (int i = 0;i<bn.length;i++){
                bn[i] = true;
            }
        }
        final JLabel[] DmxValues = new JLabel[count];

        for (int i = 0; i < count; i++) {
            final int a = i;
            itemPanes[i] = new JPanel();
            FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
            flowLayout.setVgap(0);
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
            if (bn != null) {
                if (i < bn.length) {
                    checkBoxs[i].setSelected(bn[i]);
                }
            }
            if (bn2 != null) {
                if (i < bn2.length) {
                    checkBoxs2[i].setSelected(bn2[i]);
                }
            }
            names[i] = new JLabel("<html>未知<br><br></html>", JLabel.CENTER);
            Font f1 = new Font("宋体", Font.PLAIN, 12);
            sliders[i].addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    textFields[a].setText(String.valueOf(sliders[a].getValue()));
                    int[] slt = table.getSelectedRows();
                    if (slt.length > 0) {
                        for (int k = 0; k < slt.length; k++) {
                            table.setValueAt(String.valueOf(sliders[a].getValue()), slt[k], a + 2);
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
            names[i].setBorder(BorderFactory.createEmptyBorder(-10, 0, -10, 0));
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

        ////灰掉“动作效果”相关
        //aa
        Map map22 = (Map) hashMap.get("actionXiaoGuoData");
        if (map22 != null) {
            String b = (String) map22.get("0");
            if (b.equals("true")) {
                for (int i = 0; i < tt; i++) {
                    if (names[i].getText().contains("X轴") ||
                            names[i].getText().contains("Y轴")) {
                        checkBoxs[i].setEnabled(false);
                        checkBoxs[i].setSelected(true);
                        sliders[i].setEnabled(false);
                        textFields[i].setEnabled(false);
                        //names[i].setEnabled(false);
                    }
                }
            }
        }
        ////灰掉RGB1相关的
        List list = (List) hashMap.get("rgb1Data");
        if (list != null) {
            boolean b = (boolean) list.get(0);
            if (b) {
                for (int i = 0; i < tt; i++) {
                    if (names[i].getText().contains("RGBR-1") ||
                            names[i].getText().contains("RGBG-1") ||
                            names[i].getText().contains("RGBB-1")) {
                        checkBoxs[i].setEnabled(false);
                        checkBoxs[i].setSelected(true);
                        sliders[i].setEnabled(false);
                        textFields[i].setEnabled(false);
                        //names[i].setEnabled(false);
                    }
                }
            }
        }
        ////灰掉RGB2相关的
        list = (List) hashMap.get("rgb2Data");
        if (list != null) {
            boolean b = (boolean) list.get(0);
            if (b) {
                for (int i = 0; i < tt; i++) {
                    if (names[i].getText().contains("RGBR-2") ||
                            names[i].getText().contains("RGBG-2") ||
                            names[i].getText().contains("RGBB-2")) {
                        checkBoxs[i].setEnabled(false);
                        checkBoxs[i].setSelected(true);
                        sliders[i].setEnabled(false);
                        textFields[i].setEnabled(false);
                        //names[i].setEnabled(false);
                    }
                }
            }
        }
        ////灰掉RGB3相关的
        list = (List) hashMap.get("rgb3Data");
        if (list != null) {
            boolean b = (boolean) list.get(0);
            if (b) {
                for (int i = 0; i < tt; i++) {
                    if (names[i].getText().contains("RGBR-3") ||
                            names[i].getText().contains("RGBG-3") ||
                            names[i].getText().contains("RGBB-3")) {
                        checkBoxs[i].setEnabled(false);
                        checkBoxs[i].setSelected(true);
                        sliders[i].setEnabled(false);
                        textFields[i].setEnabled(false);
                        //names[i].setEnabled(false);
                    }
                }
            }
        }


        scrollPane.getHorizontalScrollBar().setUnitIncrement(30);
        scrollPane.setViewportView(pane);
    }

    private void setChannelPane(JPanel pane) {
        //pane.setBorder(new LineBorder(Color.red));
        JScrollPane scrollPane = new JScrollPane();
        setChannelTop(scrollPane);

        JPanel p1 = new JPanel();
        p1.setBorder(new LineBorder(Color.gray));
        p1.setPreferredSize(new Dimension(680, 30));
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
        if (flag)
            slider.setValue(2000);
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

        JPanel p3 = new JPanel();
        p3.setLayout(flowLayout);
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "多灯设置", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p3.setBorder(tb);
        p3.setPreferredSize(new Dimension(684, 64));
        p3.add(new JLabel("拆分"));
        box2 = new JComboBox();
        box2.addItem("不拆分");
        box2.addItem("中间拆分");
        box2.addItem("两端拆分");
        p3.add(box2);
        checkBox = new JCheckBox("拆分反向");
        p3.add(checkBox);
        p3.add(new JLabel("                      "));
        p3.add(new JLabel("时差"));
        slider2 = new JSlider(0, 10000);
        slider2.setValue(0);
        p3.add(slider2);
        final JTextField field2 = new JTextField(4);
        field2.setText("0");

        slider2.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                field2.setText(String.valueOf(slider2.getValue()));
            }
        });
        field2.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field2.getText()).intValue();
                    slider2.setValue(tb);
                }
            }
        });

        if (ddTemp != null) {
            box2.setSelectedIndex(Integer.valueOf(ddTemp[0]).intValue());
            if (ddTemp[1].equals("0")) {
                checkBox.setSelected(false);
            } else {
                checkBox.setSelected(true);
            }
            slider2.setValue(Integer.valueOf(ddTemp[2]).intValue());
        }
        p3.add(field2);
        p3.add(new JLabel("毫秒"));

        final JScrollPane p4 = new JScrollPane();
        p4.setBorder(new LineBorder(Color.gray));
        p4.setPreferredSize(new Dimension(680, 140));
        ////////////////////////////////////////////////////////////
        int tt = Integer.valueOf((String) Data.DengKuChannelCountList.get(dengKuNumber)).intValue();
        Object[] s = new String[tt + 2];
        final String[] temp = new String[tt + 2];
        temp[0] = "1";
        if (flag)
            temp[1] = "2000";
        else
            temp[1] = "0";
        s[0] = "步骤";
        s[1] = "执行时长";
        for (int i = 2; i < s.length; i++) {
            s[i] = "" + (i - 1);
            temp[i] = "0";
        }
        Object[][] data88 = {};
        DefaultTableModel model = new DefaultTableModel(data88, s);
        if (vector88 != null) {
            Vector tp = null;
            int a = 0;
            for (int i = 0; i < vector88.size(); i++) {
                tp = (Vector) vector88.get(i);
                a = tp.size();
                if (a < tt + 2) {
                    for (int j = 0; j < tt + 2 - a; j++) {
                        tp.add("0");
                    }
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
                int[] selects = table.getSelectedRows();
                if (selects.length > 0 && !e.getValueIsAdjusting()) {
                    ChangeListener listener = null;
                    listener = slider.getChangeListeners()[0];
                    slider.removeChangeListener(listener);
                    String temp = table.getValueAt(selects[selects.length - 1], 1).toString();
                    field.setText(temp);
                    slider.setValue(Integer.valueOf(temp).intValue());
                    slider.addChangeListener(listener);
                    for (int i = 2; i < table.getColumnCount(); i++) {
                        listener = sliders[i - 2].getChangeListeners()[0];
                        sliders[i - 2].removeChangeListener(listener);
                        temp = table.getValueAt(selects[selects.length - 1], i).toString();
                        textFields[i - 2].setText(temp);
                        sliders[i - 2].setValue(Integer.valueOf(temp).intValue());
                        sliders[i - 2].addChangeListener(listener);
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
        p5.setPreferredSize(new Dimension(680, 30));
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
        flowLayout2.setVgap(-2);
        flowLayout2.setHgap(0);
        p5.setLayout(flowLayout2);
        JButton btn = new JButton("添加");
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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
        final JButton button1 = new JButton("预览");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            button1.setEnabled(false);
                            save();
                            timeBlockReviewActionListener.actionPerformed();
                            ArtNetReview artNetReview = new ArtNetReview(denKuNum-1,suCaiNum-1,startAddress);
//                            artNetReview.serialPortTest(new int[tt],500);//调用预览数据之前，先归零
                            artNetReview.getData();//发送数据
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
        button3.addActionListener(timeBlockStopReviewActionListener);
        p5.add(btn);
        p5.add(btn2);
        p5.add(new JLabel("                              "));
        p5.add(button1);
        p5.add(button3);
        p5.add(new JLabel("                                     "));
        p5.add(stepLabel);
        pane.add(p5);
    }

    private void setRgbColor3(JPanel pane) {
        List list = (List) hashMap.get("rgb3Data");
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.CENTER);
        JPanel p1 = new JPanel();
        //p1.setBorder(new LineBorder(Color.black));
        p1.setPreferredSize(new Dimension(480, 36));
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
                for (int i = 0; i < tt; i++) {
                    if (names[i].getText().contains("RGBR-3") ||
                            names[i].getText().contains("RGBG-3") ||
                            names[i].getText().contains("RGBB-3")) {
                        checkBoxs[i].setEnabled(a);
                        if (!a) {
                            checkBoxs[i].setSelected(true);
                        }
                        sliders[i].setEnabled(a);
                        textFields[i].setEnabled(a);
                        //names[i].setEnabled(false);
                    }
                }
            }
        };
        radioButton.addActionListener(listener);
        radioButton2.addActionListener(listener);
        rgb1CompontList3.add(radioButton);
        ButtonGroup group2 = new ButtonGroup();
        group2.add(radioButton);
        group2.add(radioButton2);
        radioButton2.setSelected(true);
        if (list != null) {
            boolean b = (boolean) list.get(0);
            if (b) {
                radioButton.setSelected(true);
            } else {
                radioButton2.setSelected(true);
            }
        }
        final JButton button = new JButton("预览");
        //button.addActionListener(timeBlockReviewActionListener);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            button.setEnabled(false);
                            save();
//							if (Data.serialPort != null) {
//								if (Data.file!=null) {
//									new Compare().saveTemp();
//									Compare.compareFile();
//									ReviewUtils.sendReviewCode();
//									//模式  组号  时间块号
//									int[] tt = {XiaoGuoDengModel.model,group_N,blockNum};
//									ReviewUtils.reviewOrder(3, tt);
//									///
//								} else {
//									JOptionPane.showMessageDialog(dialog, "请先生成初始版本的控制器文件导入到控制器，再进行预览！", "提示", JOptionPane.ERROR_MESSAGE);
//								}
//							}
                            timeBlockReviewActionListener.actionPerformed();
//                            ArtNetReview artNetReview = new ArtNetReview(denKuNum-1,suCaiNum-1,startAddress);
//                            artNetReview.getData();//发送数据
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        } finally {
                            button.setEnabled(true);
                        }
                    }
                }).start();
            }
        });
        JButton button2 = new JButton("停止预览");
        button2.addActionListener(timeBlockStopReviewActionListener);
        p1.add(radioButton);
        p1.add(new JLabel("   "));
        p1.add(radioButton2);
        p1.add(new JLabel("           "));
        p1.add(button);
        p1.add(new JLabel("   "));
        p1.add(button2);

        JPanel p7 = new JPanel();
        p7.setPreferredSize(new Dimension(480, 45));
        p7.setLayout(flowLayout2);
        p7.add(button);
        p7.add(new JLabel("   "));
        p7.add(button2);

        JPanel p2 = new JPanel();
        p2.setLayout(flowLayout);
        //p2.setBorder(new LineBorder(Color.black));
        p2.setPreferredSize(new Dimension(378, 114));
        final JSlider slider = new JSlider(0, 255);
        final JSlider slider2 = new JSlider(0, 255);
        final JSlider slider3 = new JSlider(0, 255);
        rgb1CompontList3.add(slider);
        rgb1CompontList3.add(slider2);
        rgb1CompontList3.add(slider3);
        final JTextField field = new JTextField(4);
        final JTextField field2 = new JTextField(4);
        final JTextField field3 = new JTextField(4);
        slider.setPreferredSize(new Dimension(280, 32));
        slider2.setPreferredSize(new Dimension(280, 32));
        slider3.setPreferredSize(new Dimension(280, 32));
        p2.add(new JLabel("R "));
        p2.add(slider);
        p2.add(field);
        p2.add(new JLabel("G "));
        p2.add(slider2);
        p2.add(field2);
        p2.add(new JLabel("B "));
        p2.add(slider3);
        p2.add(field3);

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
        slider2.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                field2.setText(String.valueOf(slider2.getValue()));
            }
        });
        field2.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field2.getText()).intValue();
                    slider2.setValue(tb);
                }
            }
        });
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
        slider.setValue(0);
        slider2.setValue(0);
        slider3.setValue(0);
        if (list != null) {
            String[] tp = (String[]) list.get(1);
            slider.setValue(Integer.valueOf(tp[0]).intValue());
            slider2.setValue(Integer.valueOf(tp[1]).intValue());
            slider3.setValue(Integer.valueOf(tp[2]).intValue());
        }

        JPanel p3 = new JPanel();
        p3.setLayout(flowLayout);
        //p3.setBorder(new LineBorder(Color.black));
        p3.setPreferredSize(new Dimension(530, 34));
        p3.add(new JLabel("参与自动   "));
        JCheckBox checkBox = new JCheckBox("R");
        JCheckBox checkBox2 = new JCheckBox("G");
        JCheckBox checkBox3 = new JCheckBox("B");
        rgb1CompontList3.add(checkBox);
        rgb1CompontList3.add(checkBox2);
        rgb1CompontList3.add(checkBox3);
        p3.add(checkBox);
        p3.add(new JLabel("  "));
        p3.add(checkBox2);
        p3.add(new JLabel("  "));
        p3.add(checkBox3);

        JPanel p4 = new JPanel();
        p4.setLayout(flowLayout);
        //p4.setBorder(new LineBorder(Color.black));
        p4.setPreferredSize(new Dimension(570, 40));
        final JCheckBox checkBox4 = new JCheckBox("颜色检测器");
        rgb1CompontList3.add(checkBox4);
        String[] tps = (String[]) bezier.Data.itemMap.get("1");
        if (tps == null) {
            tps = TuXingAction.getValus2();
        } else {
            String[] temp = TuXingAction.getValus2();
            for (int i = 0; i < 61; i++) {
                tps[i] = temp[i];
            }
        }
        box83 = new JComboBox(tps);

        checkBox4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkBox4.isSelected()) {
                    box83.setSelectedIndex(box83.getItemCount() - 1);
                    box83.setEnabled(false);
                } else {
                    box83.setSelectedIndex(0);
                    box83.setEnabled(true);
                }

            }
        });
        if (list != null) {
            boolean[] bs = (boolean[]) list.get(2);
            checkBox.setSelected(bs[0]);
            checkBox2.setSelected(bs[1]);
            checkBox3.setSelected(bs[2]);

            boolean b = (boolean) list.get(3);
            checkBox4.setSelected(b);
            if (b) {
                box83.setEnabled(false);
            }
        }
//        JButton button3 = new JButton("渐变类型");
//        button3.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                new BezierDialog().show(1, box83.getSelectedIndex(), new JComboBox[]{box83, box81, box82});
//            }
//        });
        p4.add(checkBox4);
//        p4.add(button3);
        rgb1CompontList3.add(box83);
        p4.add(box83);
        JCheckBox box2 = new JCheckBox("渐变");
        JCheckBox box3 = new JCheckBox("硬切");
        box3.setSelected(true);
        rgb1CompontList3.add(box2);
        ButtonGroup group = new ButtonGroup();
        group.add(box2);
        group.add(box3);
        p4.add(box2);
        p4.add(box3);

        if (list != null) {
            String b = (String) list.get(4);
            box83.setSelectedIndex(Integer.valueOf(b).intValue());
            boolean bb = (boolean) list.get(5);
            if (bb) {
                box2.setSelected(true);
            } else {
                box3.setSelected(true);
            }
        }

        JPanel p5 = new JPanel();
        p5.setLayout(flowLayout);
        //p5.setBorder(new LineBorder(Color.black));
        p5.setPreferredSize(new Dimension(460, 38));
        final JSlider slider4 = new JSlider(0, 100);
        final JTextField field4 = new JTextField(4);
        slider4.setPreferredSize(new Dimension(280, 32));
        rgb1CompontList3.add(slider4);
        p5.add(new JLabel("渐变速度"));
        p5.add(slider4);
        p5.add(field4);
        slider4.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                field4.setText(String.valueOf(slider4.getValue()));
            }
        });
        field4.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field4.getText()).intValue();
                    slider4.setValue(tb);
                }
            }
        });
        slider4.setValue(0);
        if (list != null) {
            String tp = (String) list.get(6);
            slider4.setValue(Integer.valueOf(tp).intValue());
        }

        JPanel p6 = new JPanel();
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "多灯设置", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p6.setBorder(tb);
        p6.setPreferredSize(new Dimension(500, 120));
        JPanel panel = new JPanel();
        flowLayout.setVgap(10);
        panel.setLayout(flowLayout);
        //panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(410, 90));
        panel.add(new JLabel("拆分"));
        JComboBox box4 = new JComboBox(TuXingAction.getValues3());
        rgb1CompontList3.add(box4);
        box4.setPreferredSize(new Dimension(280, 32));
//        box4.addItem("不拆分");
//        box4.addItem("中间拆分");
//        box4.addItem("两端拆分");
        JCheckBox checkBox5 = new JCheckBox("拆分反向");
        rgb1CompontList3.add(checkBox5);
        panel.add(box4);
        panel.add(checkBox5);
        panel.add(new JLabel("时差"));
        final JSlider slider5 = new JSlider(0, 100);
        rgb1CompontList3.add(slider5);
        final JTextField field5 = new JTextField(4);
        slider5.setPreferredSize(new Dimension(280, 32));
        panel.add(slider5);
        panel.add(field5);
        p6.add(panel);
        slider5.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                field5.setText(String.valueOf(slider5.getValue()));
            }
        });
        field5.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field5.getText()).intValue();
                    slider5.setValue(tb);
                }
            }
        });
        slider5.setValue(0);
        if (list != null) {
            String tp = (String) list.get(7);
            box4.setSelectedIndex(Integer.valueOf(tp).intValue());
            boolean b = (boolean) list.get(8);
            checkBox5.setSelected(b);
            slider5.setValue(Integer.valueOf((String) list.get(9)).intValue());
        }

        pane.add(p1);
        pane.add(p2);
        pane.add(p3);
        pane.add(p4);
        pane.add(p5);
        pane.add(p6);
        pane.add(p7);
    }

    private void setRgbColor2(JPanel pane) {
        List list = (List) hashMap.get("rgb2Data");
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.CENTER);
        JPanel p1 = new JPanel();
        //p1.setBorder(new LineBorder(Color.black));
        p1.setPreferredSize(new Dimension(480, 36));
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
                for (int i = 0; i < tt; i++) {
                    if (names[i].getText().contains("RGBR-2") ||
                            names[i].getText().contains("RGBG-2") ||
                            names[i].getText().contains("RGBB-2")) {
                        checkBoxs[i].setEnabled(a);
                        if (!a) {
                            checkBoxs[i].setSelected(true);
                        }
                        sliders[i].setEnabled(a);
                        textFields[i].setEnabled(a);
                        //names[i].setEnabled(false);
                    }
                }
            }
        };
        radioButton.addActionListener(listener);
        radioButton2.addActionListener(listener);
        rgb1CompontList2.add(radioButton);
        ButtonGroup group2 = new ButtonGroup();
        group2.add(radioButton);
        group2.add(radioButton2);
        radioButton2.setSelected(true);
        if (list != null) {
            boolean b = (boolean) list.get(0);
            if (b) {
                radioButton.setSelected(true);
            } else {
                radioButton2.setSelected(true);
            }
        }
        final JButton button = new JButton("预览");
        //button.addActionListener(timeBlockReviewActionListener);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            button.setEnabled(false);
                            save();
//							if (Data.serialPort != null) {
//								if (Data.file!=null) {
//									new Compare().saveTemp();
//									Compare.compareFile();
//									ReviewUtils.sendReviewCode();
//									//模式  组号  时间块号
//									int[] tt = {XiaoGuoDengModel.model,group_N,blockNum};
//									ReviewUtils.reviewOrder(2, tt);
//									///
//								} else {
//									JOptionPane.showMessageDialog(dialog, "请先生成初始版本的控制器文件导入到控制器，再进行预览！", "提示", JOptionPane.ERROR_MESSAGE);
//								}
//							}
                            timeBlockReviewActionListener.actionPerformed();
//                            ArtNetReview artNetReview = new ArtNetReview(denKuNum-1,suCaiNum-1,startAddress);
//                            artNetReview.getData();//发送数据
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        } finally {
                            button.setEnabled(true);
                        }
                    }
                }).start();
            }
        });
        JButton button2 = new JButton("停止预览");
        button2.addActionListener(timeBlockStopReviewActionListener);
        p1.add(radioButton);
        p1.add(new JLabel("   "));
        p1.add(radioButton2);
        p1.add(new JLabel("           "));
//        p1.add(button);
//        p1.add(new JLabel("   "));
//        p1.add(button2);

        JPanel p7 = new JPanel();
        p7.setPreferredSize(new Dimension(480, 45));
        p7.setLayout(flowLayout2);
        p7.add(button);
        p7.add(new JLabel("   "));
        p7.add(button2);

        JPanel p2 = new JPanel();
        p2.setLayout(flowLayout);
        //p2.setBorder(new LineBorder(Color.black));
        p2.setPreferredSize(new Dimension(378, 114));
        final JSlider slider = new JSlider(0, 255);
        final JSlider slider2 = new JSlider(0, 255);
        final JSlider slider3 = new JSlider(0, 255);
        rgb1CompontList2.add(slider);
        rgb1CompontList2.add(slider2);
        rgb1CompontList2.add(slider3);
        final JTextField field = new JTextField(4);
        final JTextField field2 = new JTextField(4);
        final JTextField field3 = new JTextField(4);
        slider.setPreferredSize(new Dimension(280, 32));
        slider2.setPreferredSize(new Dimension(280, 32));
        slider3.setPreferredSize(new Dimension(280, 32));
        p2.add(new JLabel("R "));
        p2.add(slider);
        p2.add(field);
        p2.add(new JLabel("G "));
        p2.add(slider2);
        p2.add(field2);
        p2.add(new JLabel("B "));
        p2.add(slider3);
        p2.add(field3);

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
        slider2.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                field2.setText(String.valueOf(slider2.getValue()));
            }
        });
        field2.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field2.getText()).intValue();
                    slider2.setValue(tb);
                }
            }
        });
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
        slider.setValue(0);
        slider2.setValue(0);
        slider3.setValue(0);
        if (list != null) {
            String[] tp = (String[]) list.get(1);
            slider.setValue(Integer.valueOf(tp[0]).intValue());
            slider2.setValue(Integer.valueOf(tp[1]).intValue());
            slider3.setValue(Integer.valueOf(tp[2]).intValue());
        }

        JPanel p3 = new JPanel();
        p3.setLayout(flowLayout);
        //p3.setBorder(new LineBorder(Color.black));
        p3.setPreferredSize(new Dimension(530, 34));
        p3.add(new JLabel("参与自动   "));
        JCheckBox checkBox = new JCheckBox("R");
        JCheckBox checkBox2 = new JCheckBox("G");
        JCheckBox checkBox3 = new JCheckBox("B");
        rgb1CompontList2.add(checkBox);
        rgb1CompontList2.add(checkBox2);
        rgb1CompontList2.add(checkBox3);
        p3.add(checkBox);
        p3.add(new JLabel("  "));
        p3.add(checkBox2);
        p3.add(new JLabel("  "));
        p3.add(checkBox3);

        JPanel p4 = new JPanel();
        p4.setLayout(flowLayout);
        //p4.setBorder(new LineBorder(Color.black));
        p4.setPreferredSize(new Dimension(570, 40));
        final JCheckBox checkBox4 = new JCheckBox("颜色检测器");
        rgb1CompontList2.add(checkBox4);
        String[] tps = (String[]) bezier.Data.itemMap.get("1");
        if (tps == null) {
            tps = TuXingAction.getValus2();
        } else {
            String[] temp = TuXingAction.getValus2();
            for (int i = 0; i < 61; i++) {
                tps[i] = temp[i];
            }
        }
        box82 = new JComboBox(tps);
        checkBox4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (checkBox4.isSelected()) {
                    box82.setSelectedIndex(box82.getItemCount() - 1);
                    box82.setEnabled(false);
                } else {
                    box82.setSelectedIndex(0);
                    box82.setEnabled(true);
                }

            }
        });
        if (list != null) {
            boolean[] bs = (boolean[]) list.get(2);
            checkBox.setSelected(bs[0]);
            checkBox2.setSelected(bs[1]);
            checkBox3.setSelected(bs[2]);

            boolean b = (boolean) list.get(3);
            checkBox4.setSelected(b);
            if (b) {
                box82.setEnabled(false);
            }
        }
//        JButton button3 = new JButton("渐变类型");
//        button3.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                new BezierDialog().show(1, box82.getSelectedIndex(), new JComboBox[]{box82, box81, box83});
//            }
//        });
        p4.add(checkBox4);
//        p4.add(button3);
        rgb1CompontList2.add(box82);
        p4.add(box82);
        JCheckBox box2 = new JCheckBox("渐变");
        JCheckBox box3 = new JCheckBox("硬切");
        box3.setSelected(true);
        rgb1CompontList2.add(box2);
        ButtonGroup group = new ButtonGroup();
        group.add(box2);
        group.add(box3);
        p4.add(box2);
        p4.add(box3);

        if (list != null) {
            String b = (String) list.get(4);
            box82.setSelectedIndex(Integer.valueOf(b).intValue());
            boolean bb = (boolean) list.get(5);
            if (bb) {
                box2.setSelected(true);
            } else {
                box3.setSelected(true);
            }
        }

        JPanel p5 = new JPanel();
        p5.setLayout(flowLayout);
        //p5.setBorder(new LineBorder(Color.black));
        p5.setPreferredSize(new Dimension(460, 38));
        final JSlider slider4 = new JSlider(0, 100);
        final JTextField field4 = new JTextField(4);
        slider4.setPreferredSize(new Dimension(280, 32));
        rgb1CompontList2.add(slider4);
        p5.add(new JLabel("渐变速度"));
        p5.add(slider4);
        p5.add(field4);
        slider4.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                field4.setText(String.valueOf(slider4.getValue()));
            }
        });
        field4.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field4.getText()).intValue();
                    slider4.setValue(tb);
                }
            }
        });
        slider4.setValue(0);
        if (list != null) {
            String tp = (String) list.get(6);
            slider4.setValue(Integer.valueOf(tp).intValue());
        }

        JPanel p6 = new JPanel();
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "多灯设置", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p6.setBorder(tb);
        p6.setPreferredSize(new Dimension(500, 120));
        JPanel panel = new JPanel();
        flowLayout.setVgap(10);
        panel.setLayout(flowLayout);
        //panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(410, 90));
        panel.add(new JLabel("拆分"));
        JComboBox box4 = new JComboBox(TuXingAction.getValues3());
        rgb1CompontList2.add(box4);
        box4.setPreferredSize(new Dimension(280, 32));
//        box4.addItem("不拆分");
//        box4.addItem("中间拆分");
//        box4.addItem("两端拆分");
        JCheckBox checkBox5 = new JCheckBox("拆分反向");
        rgb1CompontList2.add(checkBox5);
        panel.add(box4);
        panel.add(checkBox5);
        panel.add(new JLabel("时差"));
        final JSlider slider5 = new JSlider(0, 100);
        rgb1CompontList2.add(slider5);
        final JTextField field5 = new JTextField(4);
        slider5.setPreferredSize(new Dimension(280, 32));
        panel.add(slider5);
        panel.add(field5);
        p6.add(panel);
        slider5.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                field5.setText(String.valueOf(slider5.getValue()));
            }
        });
        field5.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field5.getText()).intValue();
                    slider5.setValue(tb);
                }
            }
        });
        slider5.setValue(0);
        if (list != null) {
            String tp = (String) list.get(7);
            box4.setSelectedIndex(Integer.valueOf(tp).intValue());
            boolean b = (boolean) list.get(8);
            checkBox5.setSelected(b);
            slider5.setValue(Integer.valueOf((String) list.get(9)).intValue());
        }

        pane.add(p1);
        pane.add(p2);
        pane.add(p3);
        pane.add(p4);
        pane.add(p5);
        pane.add(p6);
        pane.add(p7);
    }

    private void setRgbColor1(JPanel pane) {
        List list = (List) hashMap.get("rgb1Data");
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.CENTER);
        JPanel p1 = new JPanel();
        //p1.setBorder(new LineBorder(Color.black));
        p1.setPreferredSize(new Dimension(480, 36));
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
                for (int i = 0; i < tt; i++) {
                    if (names[i].getText().contains("RGBR-1") ||
                            names[i].getText().contains("RGBG-1") ||
                            names[i].getText().contains("RGBB-1")) {
                        checkBoxs[i].setEnabled(a);
                        if (!a) {
                            checkBoxs[i].setSelected(true);
                        }
                        sliders[i].setEnabled(a);
                        textFields[i].setEnabled(a);
                        //names[i].setEnabled(false);
                    }
                }
            }
        };
        radioButton.addActionListener(listener);
        radioButton2.addActionListener(listener);
        rgb1CompontList.add(radioButton);
        ButtonGroup group2 = new ButtonGroup();
        group2.add(radioButton);
        group2.add(radioButton2);
        radioButton2.setSelected(true);
        if (list != null) {
            boolean b = (boolean) list.get(0);
            if (b) {
                radioButton.setSelected(true);
            } else {
                radioButton2.setSelected(true);
            }
        }
        final JButton button = new JButton("预览");
        //button.addActionListener(timeBlockReviewActionListener);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            button.setEnabled(false);
                            save();
//							if (Data.serialPort != null) {
//								if (Data.file!=null) {
//									new Compare().saveTemp();
//									Compare.compareFile();
//									ReviewUtils.sendReviewCode();
//									//模式  组号  时间块号
//									int[] tt = {XiaoGuoDengModel.model,group_N,blockNum};
//									ReviewUtils.reviewOrder(1, tt);
//									///
//								} else {
//									JOptionPane.showMessageDialog(dialog, "请先生成初始版本的控制器文件导入到控制器，再进行预览！", "提示", JOptionPane.ERROR_MESSAGE);
//								}
//							}
                            timeBlockReviewActionListener.actionPerformed();
//                            ArtNetReview artNetReview = new ArtNetReview(denKuNum-1,suCaiNum-1,startAddress);
//                            artNetReview.getData();//发送数据
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        } finally {
                            button.setEnabled(true);
                        }
                    }
                }).start();
            }
        });
        JButton button2 = new JButton("停止预览");
        button2.addActionListener(timeBlockStopReviewActionListener);
        p1.add(radioButton);
        p1.add(new JLabel("   "));
        p1.add(radioButton2);
        p1.add(new JLabel("           "));
//        p1.add(button);
//        p1.add(new JLabel("   "));
//        p1.add(button2);

        JPanel p7 = new JPanel();
        p7.setPreferredSize(new Dimension(480, 45));
        p7.setLayout(flowLayout2);
        p7.add(button);
        p7.add(new JLabel("   "));
        p7.add(button2);

        JPanel p2 = new JPanel();
        p2.setLayout(flowLayout);
        //p2.setBorder(new LineBorder(Color.black));
        p2.setPreferredSize(new Dimension(378, 114));
        final JSlider slider = new JSlider(0, 255);
        final JSlider slider2 = new JSlider(0, 255);
        final JSlider slider3 = new JSlider(0, 255);
        rgb1CompontList.add(slider);
        rgb1CompontList.add(slider2);
        rgb1CompontList.add(slider3);
        final JTextField field = new JTextField(4);
        final JTextField field2 = new JTextField(4);
        final JTextField field3 = new JTextField(4);
        slider.setPreferredSize(new Dimension(280, 32));
        slider2.setPreferredSize(new Dimension(280, 32));
        slider3.setPreferredSize(new Dimension(280, 32));
        p2.add(new JLabel("R "));
        p2.add(slider);
        p2.add(field);
        p2.add(new JLabel("G "));
        p2.add(slider2);
        p2.add(field2);
        p2.add(new JLabel("B "));
        p2.add(slider3);
        p2.add(field3);

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
        slider2.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                field2.setText(String.valueOf(slider2.getValue()));
            }
        });
        field2.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field2.getText()).intValue();
                    slider2.setValue(tb);
                }
            }
        });
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
        slider.setValue(0);
        slider2.setValue(0);
        slider3.setValue(0);
        if (list != null) {
            String[] tp = (String[]) list.get(1);
            slider.setValue(Integer.valueOf(tp[0]).intValue());
            slider2.setValue(Integer.valueOf(tp[1]).intValue());
            slider3.setValue(Integer.valueOf(tp[2]).intValue());
        }

        JPanel p3 = new JPanel();
        p3.setLayout(flowLayout);
        //p3.setBorder(new LineBorder(Color.black));
        p3.setPreferredSize(new Dimension(530, 34));
        p3.add(new JLabel("参与自动   "));
        JCheckBox checkBox = new JCheckBox("R");
        JCheckBox checkBox2 = new JCheckBox("G");
        JCheckBox checkBox3 = new JCheckBox("B");
        rgb1CompontList.add(checkBox);
        rgb1CompontList.add(checkBox2);
        rgb1CompontList.add(checkBox3);
        p3.add(checkBox);
        p3.add(new JLabel("  "));
        p3.add(checkBox2);
        p3.add(new JLabel("  "));
        p3.add(checkBox3);

        JPanel p4 = new JPanel();
        p4.setLayout(flowLayout);
        //p4.setBorder(new LineBorder(Color.black));
        p4.setPreferredSize(new Dimension(570, 40));
        final JCheckBox checkBox4 = new JCheckBox("颜色检测器");
        rgb1CompontList.add(checkBox4);
//        JButton button3 = new JButton("渐变类型");
        String[] tps = (String[]) bezier.Data.itemMap.get("1");

        if (tps == null) {
            tps = TuXingAction.getValus2();
        } else {
            String[] temp = TuXingAction.getValus2();
            for (int i = 0; i < 61; i++) {
                tps[i] = temp[i];
            }
        }
        box81 = new JComboBox(tps);
//        button3.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                new BezierDialog().show(1, box81.getSelectedIndex(), new JComboBox[]{box81, box82, box83});
//            }
//        });
        p4.add(checkBox4);
//        p4.add(button3);

        checkBox4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (checkBox4.isSelected()) {
                    box81.setSelectedIndex(box81.getItemCount() - 1);
                    box81.setEnabled(false);
                } else {
                    box81.setSelectedIndex(0);
                    box81.setEnabled(true);
                }
            }
        });
        if (list != null) {
            boolean[] bs = (boolean[]) list.get(2);
            checkBox.setSelected(bs[0]);
            checkBox2.setSelected(bs[1]);
            checkBox3.setSelected(bs[2]);

            boolean b = (boolean) list.get(3);
            checkBox4.setSelected(b);
            if (b) {
                box81.setEnabled(false);
            }
        }
        rgb1CompontList.add(box81);
        p4.add(box81);
        JCheckBox box2 = new JCheckBox("渐变");
        JCheckBox box3 = new JCheckBox("硬切");
        box3.setSelected(true);
        rgb1CompontList.add(box2);
        ButtonGroup group = new ButtonGroup();
        group.add(box2);
        group.add(box3);
        p4.add(box2);
        p4.add(box3);

        if (list != null) {
            String b = (String) list.get(4);
            box81.setSelectedIndex(Integer.valueOf(b).intValue());
            boolean bb = (boolean) list.get(5);
            if (bb) {
                box2.setSelected(true);
            } else {
                box3.setSelected(true);
            }
        }

        JPanel p5 = new JPanel();
        p5.setLayout(flowLayout);
        //p5.setBorder(new LineBorder(Color.black));
        p5.setPreferredSize(new Dimension(460, 38));
        final JSlider slider4 = new JSlider(0, 100);
        final JTextField field4 = new JTextField(4);
        slider4.setPreferredSize(new Dimension(280, 32));
        rgb1CompontList.add(slider4);
        p5.add(new JLabel("渐变速度"));
        p5.add(slider4);
        p5.add(field4);
        slider4.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                field4.setText(String.valueOf(slider4.getValue()));
            }
        });
        field4.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field4.getText()).intValue();
                    slider4.setValue(tb);
                }
            }
        });
        slider4.setValue(0);
        if (list != null) {
            String tp = (String) list.get(6);
            slider4.setValue(Integer.valueOf(tp).intValue());
        }

        JPanel p6 = new JPanel();
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray), "多灯设置", TitledBorder.LEFT, TitledBorder.TOP, new Font(Font.SERIF, Font.BOLD, 12));
        p6.setBorder(tb);
        p6.setPreferredSize(new Dimension(500, 120));
        JPanel panel = new JPanel();
        flowLayout.setVgap(10);
        panel.setLayout(flowLayout);
        //panel.setBorder(new LineBorder(Color.gray));
        panel.setPreferredSize(new Dimension(410, 90));
        panel.add(new JLabel("拆分"));
        JComboBox box4 = new JComboBox(TuXingAction.getValues3());
        rgb1CompontList.add(box4);
        box4.setPreferredSize(new Dimension(280, 32));
//        box4.addItem("不拆分");
//        box4.addItem("中间拆分");
//        box4.addItem("两端拆分");
        JCheckBox checkBox5 = new JCheckBox("拆分反向");
        rgb1CompontList.add(checkBox5);
        panel.add(box4);
        panel.add(checkBox5);
        panel.add(new JLabel("时差"));
        final JSlider slider5 = new JSlider(0, 100);
        rgb1CompontList.add(slider5);
        final JTextField field5 = new JTextField(4);
        slider5.setPreferredSize(new Dimension(280, 32));
        panel.add(slider5);
        panel.add(field5);
        p6.add(panel);
        slider5.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                field5.setText(String.valueOf(slider5.getValue()));
            }
        });
        field5.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    int tb = Integer.valueOf(field5.getText()).intValue();
                    slider5.setValue(tb);
                }
            }
        });
        slider5.setValue(0);
        if (list != null) {
            String tp = (String) list.get(7);
            box4.setSelectedIndex(Integer.valueOf(tp).intValue());
            boolean b = (boolean) list.get(8);
            checkBox5.setSelected(b);
            slider5.setValue(Integer.valueOf((String) list.get(9)).intValue());
        }

        pane.add(p1);
        pane.add(p2);
        pane.add(p3);
        pane.add(p4);
        pane.add(p5);
        pane.add(p6);
        pane.add(p7);
    }

    private void setDonZuoPane(JPanel pane) {
        FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER);
        flowLayout.setVgap(0);
        pane.setLayout(flowLayout);

        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);

        JPanel p1 = new JPanel();
        p1.setPreferredSize(new Dimension(500, 30));
        p1.setLayout(flowLayout2);
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
                for (int i = 0; i < tt; i++) {
                    if (names[i].getText().contains("X轴") ||
                            names[i].getText().contains("Y轴")) {
                        checkBoxs[i].setEnabled(a);
                        if (!a) {
                            checkBoxs[i].setSelected(true);
                        }
                        sliders[i].setEnabled(a);
                        textFields[i].setEnabled(a);
                        //names[i].setEnabled(false);
                    }
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

        JPanel p5 = new JPanel();
        p5.setPreferredSize(new Dimension(680, 50));
        p5.setLayout(flowLayout);
        final JButton button3 = new JButton("预览");
        //button3.addActionListener(timeBlockReviewActionListener);
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            button3.setEnabled(false);
                            save();
                            timeBlockReviewActionListener.actionPerformed();
//							if (Data.serialPort != null) {
//								if (Data.file!=null) {
//									new Compare().saveTemp();
//									Compare.compareFile();
//									ReviewUtils.sendReviewCode();
//									//模式  组号  时间块号
//									int[] tt = {XiaoGuoDengModel.model,group_N,blockNum};
//									ReviewUtils.reviewOrder(4, tt);
//									///
//								} else {
//									JOptionPane.showMessageDialog(dialog, "请先生成初始版本的控制器文件导入到控制器，再进行预览！", "提示", JOptionPane.ERROR_MESSAGE);
//								}
//							}
//                            ArtNetReview artNetReview = new ArtNetReview(denKuNum-1,suCaiNum-1,startAddress);
//                            artNetReview.getData();//发送数据
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        } finally {
                            button3.setEnabled(true);
                        }
                    }
                }).start();
            }
        });
        JButton button4 = new JButton("停止预览");
        button4.addActionListener(timeBlockStopReviewActionListener);
        p5.add(button3);
        p5.add(new JLabel("   "));
        p5.add(button4);

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
        //p4_to_p1.setBorder(new LineBorder(Color.gray));
        p4_to_p1.setPreferredSize(new Dimension(380, 40));
        p4_to_p1.add(new JLabel("拆分"));
        JComboBox box2 = new JComboBox();
        actionCompontList.add(box2);
        box2.addItem("不拆分");
        box2.addItem("中间拆分");
        box2.addItem("两端拆分");
        p4_to_p1.add(box2);

        JPanel p4_to_p2 = new JPanel();
        p4_to_p2.setLayout(flowLayout2);
        //p4_to_p2.setBorder(new LineBorder(Color.gray));
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
        final JSlider slider3 = new JSlider(0, 100);
        actionCompontList.add(slider3);
        slider3.setValue(0);
        slider3.setPreferredSize(new Dimension(340, 30));
        p4_to_p3.add(slider3);
        final JTextField field3 = new JTextField(4);
        field3.setText("0");
        p4_to_p3.add(field3);
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
        }

        p4.add(p4_to_p1);
        p4.add(p4_to_p2);
        p4.add(p4_to_p3);
        pane.add(p1);
        pane.add(p2);
        pane.add(p3);
        pane.add(p4);
        pane.add(p5);
    }

    void setP1(JPanel pane) {

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

    public Integer getxiaoGuoDengDengKuName() {
        NewJTable table3 = (NewJTable) MainUi.map.get("allLightTable");//所有灯具
        Integer name = group_N;
        TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(name - 1);
        Iterator iterator = treeSet.iterator();
        String s2 = "";
        while (iterator.hasNext()) {
            int a = (int) iterator.next();
            if (table3.getRowCount() > 0) {
                String s = table3.getValueAt(a, 0).toString();
                Integer s1 = Integer.parseInt(s.split("#")[0].substring(2));//组内灯具的灯具id
                NewJTable table_dengJu = (NewJTable) MainUi.map.get("table_dengJu");//灯具配置
                s2 = ((String) table_dengJu.getValueAt(s1 - 1, 3)).split("#")[0].substring(2);//灯库名称
            }
        }
        return Integer.parseInt(s2);
    }
}
