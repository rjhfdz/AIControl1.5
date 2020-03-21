package com.boray.xiaoGuoDeng.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;
import java.net.URLDecoder;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.boray.Data.Data;
import com.boray.Data.XiaoGuoDengModel;
import com.boray.Utils.WaitProgressBar;
import com.boray.mainUi.MainUi;
import com.boray.xiaoGuoDeng.Listener.*;
import com.boray.xiaoGuoDeng.reviewBlock.ReviewBlock;

public class RightMainUI {
    public static Image image, offScreenImage;
    private JScrollPane topPane, bottom_leftPane;
    private int Number;

    public void show(JPanel pane, int n) {
        Number = n;
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
        pane.setLayout(flowLayout);
        pane.setPreferredSize(new Dimension(822, 574));
        pane.setBorder(new LineBorder(Color.gray));

        JPanel p1 = new JPanel();
        p1.setLayout(flowLayout);
        //p1.setBorder(new LineBorder(Color.gray));
        p1.setPreferredSize(new Dimension(810, 32));
        final JButton button = new JButton("预览");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        button.setEnabled(false);
//						ReviewUtils.sceneReview(XiaoGuoDengModel.model);
                        DefineJLable3 lable3 = (DefineJLable3) MainUi.map.get("SuoYouDengZuLable" + XiaoGuoDengModel.model);
                        if (Data.serialPort != null) {
//							if (Data.file!=null) {
//								new Compare().saveTemp();
//								Compare.compareFile();
//								ReviewUtils.sendReviewCode();
//								ReviewUtils.modeReviewOrder(XiaoGuoDengModel.model);
                            if (lable3.isEnabled())
                                ReviewBlock.xiaoGuoDuoDengReview(XiaoGuoDengModel.model);
                            else
                                ReviewBlock.serialPortReview(XiaoGuoDengModel.model);
//							} else {
//								JFrame frame = (JFrame)MainUi.map.get("frame");
//								JOptionPane.showMessageDialog(frame, "请先生成初始版本的控制器文件导入到控制器，再进行预览！", "提示", JOptionPane.ERROR_MESSAGE);
//							}
                        } else if (Data.socket != null) {
//							ReviewBlock.saveFile(XiaoGuoDengModel.model);
                            if (lable3.isEnabled())
                                ReviewBlock.xiaoGuoDuoDengReview(XiaoGuoDengModel.model);
                            else
                                ReviewBlock.socketReview(XiaoGuoDengModel.model);
                        }
                        button.setEnabled(true);
                    }
                });
                WaitProgressBar.show((Frame) MainUi.map.get("frame"), thread, "预览数据发送中。。。", "数据发送完成", "");
            }
        });
        p1.add(button);
        JPanel N1 = new JPanel();
        //N1.setBorder(new LineBorder(Color.gray));
        N1.setPreferredSize(new Dimension(532, 24));
        p1.add(N1);
        p1.add(new JLabel("源场景"));
        JComboBox box = new JComboBox();
        for (int i = 1; i <= 24; i++) {
            box.addItem(String.valueOf(i));
        }
        JButton button2 = new JButton("复制到当前");
        button2.addActionListener(new ModelCopyActionListener(box));
        p1.add(box);
        p1.add(button2);

        ////////////////////////////////
        JPanel topLeftPane = new JPanel();
        //topLeftPane.setBorder(new LineBorder(Color.gray));
        topLeftPane.setPreferredSize(new Dimension(100, 32));
        topPane = new JScrollPane();
        setTopPane(topPane);
        bottom_leftPane = new JScrollPane();
        setBottom_leftPane(bottom_leftPane);
        JScrollPane bottom_rightPane = new JScrollPane();
		/*JPanel p30 = new JPanel();
		CardLayout cardLayout = new CardLayout();
		MainUi.map.put("xiaoGuoParentPane", p30);
		MainUi.map.put("xiaoGuoParentPane_cardLayout", cardLayout);
		p30.setLayout(cardLayout);
		JScrollPane[] scrollPanes = new JScrollPane[24];
		for (int i = 0; i < scrollPanes.length; i++) {
			scrollPanes[i] = new JScrollPane();
			setBottom_rightPane(scrollPanes[i]);
			p30.add(String.valueOf(i), scrollPanes[i]);
		}*/

        setBottom_rightPane(bottom_rightPane);
        ////////////////////////////////

        pane.add(p1);
        pane.add(topLeftPane);
        pane.add(topPane);
        pane.add(bottom_leftPane);
        pane.add(bottom_rightPane);
        //pane.add(p30);
    }

    private void setTopPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.gray));
        //scrollPane.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.gray));
        scrollPane.setPreferredSize(new Dimension(720, 32));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        JPanel rulePane = new JPanel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
        rulePane.setLayout(flowLayout);

        rulePane.setPreferredSize(new Dimension(12560, 30));
        //rulePane.setBorder(new LineBorder(Color.gray));
		
		/*JLabel label = new JLabel();
		label.setPreferredSize(new Dimension(88,30));
		rulePane.add(label);*/

        try {
            //String path = getClass().getResource("/image/").getPath().substring(1);
            //path = URLDecoder.decode(path,"utf-8") + "rule.png";
            //image = ImageIO.read(new File(path));
            image = ImageIO.read(getClass().getResource("/image/rule.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JPanel ruleLabe = new JPanel(true) {
            protected void paintComponent(Graphics g) {
                try {
                    if (image != null) {
                        g.drawImage(image, 0, 0, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ruleLabe.setPreferredSize(new Dimension(12450, 30));
        MainUi.map.put("ruleLabe" + Number, ruleLabe);
        rulePane.add(ruleLabe);

        scrollPane.setViewportView(rulePane);
    }

    private void setBottom_leftPane(JScrollPane scrollPane) {
        scrollPane.setPreferredSize(new Dimension(100, 508));
        scrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.gray));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        JPanel pane = new JPanel();
        //pane.setPreferredSize(new Dimension(94,1130));
        pane.setPreferredSize(new Dimension(92, 1110));
        JLabel[] labels = new JLabel[31];
        MainUi.map.put("labels_group" + Number, labels);
        JLabel label = new JLabel("全部灯组");
        MainUi.map.put("SuoYouDengZuLabel" + Number, label);
        for (int i = 0; i < labels.length; i++) {
            if (i == 0) {
                labels[i] = new JLabel("DMX录制");
                DMXAndModelListener listener = new DMXAndModelListener(Number + "");
                labels[i].addMouseListener(listener);
            } else {
                labels[i] = new JLabel("组" + i);
            }
            if (i == 1) {
                label.setOpaque(true);
                label.setBackground(new Color(243, 243, 243));
                label.setPreferredSize(new Dimension(88, 30));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(new LineBorder(Color.gray));
                pane.add(label);
            }
            labels[i].setOpaque(true);
            labels[i].setBackground(new Color(243, 243, 243));
            labels[i].setPreferredSize(new Dimension(88, 30));
            labels[i].setHorizontalAlignment(SwingConstants.CENTER);
            labels[i].setBorder(new LineBorder(Color.gray));
            pane.add(labels[i]);
        }
        scrollPane.setViewportView(pane);
    }

    private void setBottom_rightPane(JScrollPane scrollPane) {

        scrollPane.setPreferredSize(new Dimension(720, 508));
        scrollPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.gray));

        scrollPane.getHorizontalScrollBar().setUnitIncrement(30);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        //scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        //scrollPane.setBorder(new LineBorder(Color.black));大家都在发
        JPanel parentPane = new JPanel();
        MainUi.map.put("xiaoGuoParentPane" + Number, parentPane);
        parentPane.setPreferredSize(new Dimension(10000, 1096));
        JPanel[] timeBlockPanels = new JPanel[31];
        MainUi.map.put("timeBlockPanels_group" + Number, timeBlockPanels);
        //CreateTimeBlockListener[] listeners = new CreateTimeBlockListener[31];
        //右键菜单
        CreateTimeBlockListener listener = new CreateTimeBlockListener();
        //FlowLayout flowLayout3 = new FlowLayout(FlowLayout.LEFT);
        //flowLayout3.setHgap(0);flowLayout3.setVgap(0);
        JPanel panel = new JPanel();
        MainUi.map.put("SuoYouDengZuPanel" + Number, panel);
        for (int i = 0; i < timeBlockPanels.length; i++) {
            timeBlockPanels[i] = new JPanel();
            timeBlockPanels[i].setLayout(null);
            timeBlockPanels[i].setBorder(new LineBorder(Color.gray));
            timeBlockPanels[i].setPreferredSize(new Dimension(10000, 30));
            timeBlockPanels[i].setBackground(new Color(192, 192, 192));
            timeBlockPanels[i].setOpaque(true);
            timeBlockPanels[i].setName("" + i);
            //listeners[i] = new CreateTimeBlockListener();
            if (i != 0) {
                timeBlockPanels[i].addMouseListener(listener);
            }/*else{
                DefineJLable2 lable2 = new DefineJLable2("10","50",1,50,timeBlockPanels[i]);
                lable2.setSize(200,29);
                lable2.setBackground(Color.green);
                timeBlockPanels[i].add(lable2);
            }*/
            if (i == 1) {
                panel.setLayout(null);
                panel.setBorder(new LineBorder(Color.gray));
                panel.setPreferredSize(new Dimension(10000, 30));
                panel.setBackground(new Color(192, 192, 192));
                panel.setOpaque(true);
                panel.setEnabled(false);
                DefineJLable3 lable3 = new DefineJLable3("所有灯库", panel);
                MainUi.map.put("SuoYouDengZuLable" + Number, lable3);
                panel.add(lable3);
                SuoYouDengZuPanelMouseListener suoYouDengZuPanelMouseListener = new SuoYouDengZuPanelMouseListener();
                panel.addMouseListener(suoYouDengZuPanelMouseListener);
                parentPane.add(panel);
            }
            parentPane.add(timeBlockPanels[i]);
        }
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                bottom_leftPane.getVerticalScrollBar().setValue(e.getValue());
            }
        });
        scrollPane.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                topPane.getHorizontalScrollBar().setValue(e.getValue());
            }
        });
        scrollPane.setViewportView(parentPane);
    }

    private void setP2(JPanel pane) {
        FlowLayout flowLayout2 = new FlowLayout(FlowLayout.LEFT);
        flowLayout2.setVgap(5);
        pane.setPreferredSize(new Dimension(12560, 1130));
        pane.setLayout(flowLayout2);

        JPanel rulePane = new JPanel();
        FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        flowLayout.setVgap(0);
        rulePane.setLayout(flowLayout);
        //rulePane.setBorder(new LineBorder(Color.gray));
        rulePane.setPreferredSize(new Dimension(12560, 30));

        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(88, 30));
        rulePane.add(label);

        try {
            String path = getClass().getResource("/image/").getPath().substring(1);
            path = URLDecoder.decode(path, "utf-8") + "rule.png";
            image = ImageIO.read(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JPanel ruleLabe = new JPanel(true) {
            protected void paintComponent(Graphics g) {
                try {
                    if (image != null) {
                        g.drawImage(image, 0, 0, null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        ruleLabe.setPreferredSize(new Dimension(12450, 100));
        MainUi.map.put("ruleLabe" + Number, ruleLabe);
        rulePane.add(ruleLabe);

        pane.add(rulePane);

        JPanel[] panels = new JPanel[31];
        JLabel[] labels = new JLabel[31];
        JPanel[] timeBlockPanels = new JPanel[31];
        //右键菜单
        CreateTimeBlockListener[] listeners = new CreateTimeBlockListener[31];

        FlowLayout flowLayout3 = new FlowLayout(FlowLayout.LEFT);
        flowLayout3.setHgap(0);
        flowLayout3.setVgap(0);
        for (int i = 0; i < panels.length; i++) {
            panels[i] = new JPanel();
            panels[i].setLayout(flowLayout);
            panels[i].setPreferredSize(new Dimension(12560, 30));
            timeBlockPanels[i] = new JPanel();
            timeBlockPanels[i].setLayout(null);
            timeBlockPanels[i].setBorder(new LineBorder(Color.gray));
            timeBlockPanels[i].setPreferredSize(new Dimension(12400, 30));
            timeBlockPanels[i].setBackground(new Color(192, 192, 192));
            timeBlockPanels[i].setOpaque(true);
            listeners[i] = new CreateTimeBlockListener();
            timeBlockPanels[i].addMouseListener(listeners[i]);
            if (i == 0) {
                labels[i] = new JLabel("DMX录制");
            } else {
                labels[i] = new JLabel("组" + i);
            }
            labels[i].setOpaque(true);
            labels[i].setBackground(new Color(243, 243, 243));
            labels[i].setPreferredSize(new Dimension(88, 30));
            labels[i].setHorizontalAlignment(SwingConstants.CENTER);
            labels[i].setBorder(new LineBorder(Color.gray));
            panels[i].add(labels[i]);
            panels[i].add(timeBlockPanels[i]);
            pane.add(panels[i]);
        }
    }
}
