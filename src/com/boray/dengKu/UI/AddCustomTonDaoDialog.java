package com.boray.dengKu.UI;

import com.boray.Data.ChannelName;
import com.boray.Data.Data;
import com.boray.Utils.IconJDialog;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class AddCustomTonDaoDialog implements ActionListener, WindowListener {

    private IconJDialog dialog;
    private JFrame frame;
    private NewJTable table;
    private int x = 0;

    /**
     * 展示界面
     */
    public void show() {
        frame = (JFrame) MainUi.map.get("frame");
        dialog = new IconJDialog(frame, true);
        dialog.setResizable(false);
        dialog.setTitle("自定义通道");
        int w = 800, h = 600;
        dialog.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
        dialog.setSize(w, h);
        dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        init();
        dialog.setVisible(true);
    }

    /**
     * 加载界面ui
     */
    private void init() {
        JPanel panel = new JPanel();
        JButton btn = new JButton("新建");
        JButton btn1 = new JButton("删除");
        btn.addActionListener(this);
        btn1.addActionListener(this);
        panel.add(btn);
        panel.add(btn1);

        JScrollPane bodyPane = new JScrollPane();
        bodyPane.setPreferredSize(new Dimension(780, 520));
        Object[][] data = {};
        String[] title = {"ID", "通道名称"};
        DefaultTableModel model = new DefaultTableModel(data, title);
        table = new NewJTable(model, 0);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        cell.setBackground(Color.white);
                        cell.setForeground(Color.black);
                    } else {
                        cell.setBackground(Color.white); //设置偶数行底色
                        cell.setForeground(Color.black);
                    }
                } else {
                    cell.setBackground(new Color(85, 160, 255));
                    cell.setForeground(Color.white);
                }
                return cell;
            }
        };
//        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < title.length; i++) {
            table.getColumn(table.getColumnName(i)).setCellRenderer(cellRenderer);
        }
        table.setSelectionBackground(new Color(56, 117, 215));
        table.getTableHeader().setUI(new BasicTableHeaderUI());
        table.getTableHeader().setReorderingAllowed(false);
        table.setOpaque(false);
        table.setFont(new Font(Font.SERIF, Font.BOLD, 14));
//        table.getColumnModel().getColumn(0).setPreferredWidth(30);
//        table.getColumnModel().getColumn(1).setPreferredWidth(102);
        table.setRowHeight(30);
        bodyPane.setViewportView(table);

        initData();
//        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getX() == x) {
                    if (mouseEvent.getButton() == 1 && mouseEvent.getClickCount() == 2) {
                        IconJDialog dia = new IconJDialog(frame, true);
                        dia.setResizable(false);
                        dia.setTitle("编辑");
                        int w = 380, h = 180;
                        dia.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
                        dia.setSize(w, h);
                        dia.setLayout(new FlowLayout(FlowLayout.CENTER));
                        dia.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        JPanel p1 = new JPanel();
                        p1.add(new JLabel("通道名称："));
                        final JTextField field = new JTextField(15);
                        p1.add(field);
                        JPanel p2 = new JPanel();
                        JButton btn1 = new JButton("确定");
                        JButton btn2 = new JButton("取消");

                        ActionListener listener = new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if ("取消".equals(e.getActionCommand())) {
                                    dia.dispose();
                                } else {
                                    if (field.getText() != null && field.getText() != "") {
                                        table.setValueAt(field.getText(), table.getSelectedRow(), 1);
                                        Data.dengKuTonDaoList.set(table.getSelectedRow(), field.getText());
                                        table.repaint();
                                        dia.dispose();
                                    }
                                }
                            }
                        };

                        btn1.addActionListener(listener);
                        btn2.addActionListener(listener);
                        p2.add(btn1);
                        p2.add(new JLabel("     "));
                        p2.add(btn2);

                        JPanel n1 = new JPanel();
                        n1.setPreferredSize(new Dimension(350, 20));
                        dia.add(n1);
                        dia.add(p1);
                        dia.add(p2);
                        dia.setVisible(true);
                    }
                }
                x = mouseEvent.getX();
            }
        });

        dialog.add(panel);
        dialog.add(bodyPane);

        dialog.addWindowListener(this);
    }

    /**
     * 加载界面数据
     */
    private void initData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < Data.dengKuTonDaoList.size(); i++) {
            Object[] o = {table.getRowCount() + 1, Data.dengKuTonDaoList.get(i)};
            model.addRow(o);
        }
        table.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("新建".equals(e.getActionCommand())) {
            IconJDialog dia = new IconJDialog(frame, true);
            dia.setResizable(false);
            dia.setTitle("新建");
            int w = 380, h = 180;
            dia.setLocation(frame.getLocation().x + frame.getSize().width / 2 - w / 2, frame.getLocation().y + frame.getSize().height / 2 - h / 2);
            dia.setSize(w, h);
            dia.setLayout(new FlowLayout(FlowLayout.CENTER));
            dia.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            init(dia);
            dia.setVisible(true);
        } else if ("删除".equals(e.getActionCommand())) {
            Data.dengKuTonDaoList.remove(table.getSelectedRow());
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.removeRow(table.getSelectedRow());
            table.repaint();
        }
    }

    private void init(final JDialog dia) {
        JPanel p1 = new JPanel();
        p1.add(new JLabel("通道名称："));
        final JTextField field = new JTextField(15);
        p1.add(field);
        JPanel p2 = new JPanel();
        JButton btn1 = new JButton("确定");
        JButton btn2 = new JButton("取消");

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("取消".equals(e.getActionCommand())) {
                    dia.dispose();
                } else {
                    if (field.getText() != null && !field.getText().equals("")) {
                        if (Data.dengKuTonDaoList.contains(field.getText().trim())) {
                            JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "该通道已存在，请重新输入！", "提示", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        Object[] o = {table.getRowCount() + 1, field.getText().trim()};
                        model.addRow(o);
                        Data.dengKuTonDaoList.add(field.getText().trim());
                        table.repaint();
                        dia.dispose();
                    } else {
                        JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "内容不能为空！", "提示", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
        };

        btn1.addActionListener(listener);
        btn2.addActionListener(listener);
        p2.add(btn1);
        p2.add(new JLabel("     "));
        p2.add(btn2);

        JPanel n1 = new JPanel();
        n1.setPreferredSize(new Dimension(350, 20));
        dia.add(n1);
        dia.add(p1);
        dia.add(p2);
    }

    public void addDengKuTonDaoList() {
        JComboBox[] channelBoxs_L = (JComboBox[]) MainUi.map.get("lamp_1_To_16");
        JComboBox[] channelBoxs_R = (JComboBox[]) MainUi.map.get("lamp_17_To_32");
        List<String> channelStr_L = new ArrayList<>();
        List<String> channelStr_R = new ArrayList<>();
        ItemListener listener = (ItemListener) MainUi.map.get("ChannelItemListener");
        for (int j = 0; j < 16; j++) {
            channelBoxs_L[j].removeItemListener(listener);
            channelBoxs_R[j].removeItemListener(listener);
        }
        for (int i = 0; i < channelBoxs_L.length; i++) {
            if (channelBoxs_L[i].isEnabled()) {
                channelStr_L.add(channelBoxs_L[i].getSelectedItem().toString());
                channelBoxs_L[i].removeAllItems();
            }
            if (channelBoxs_R[i].isEnabled()) {
                channelStr_R.add(channelBoxs_R[i].getSelectedItem().toString());
                channelBoxs_R[i].removeAllItems();
            }
            for (int j = 0; j < ChannelName.names.length; j++) {
                if (channelBoxs_L[i].isEnabled())
                    channelBoxs_L[i].addItem(ChannelName.names[j]);
                if (channelBoxs_R[i].isEnabled())
                    channelBoxs_R[i].addItem(ChannelName.names[j]);
            }
        }
        for (int i = 0; i < channelBoxs_L.length; i++) {
            for (int j = 0; j < Data.dengKuTonDaoList.size(); j++) {
                if (channelBoxs_L[i].isEnabled())
                    channelBoxs_L[i].addItem(Data.dengKuTonDaoList.get(j));
                if (channelBoxs_R[i].isEnabled())
                    channelBoxs_R[i].addItem(Data.dengKuTonDaoList.get(j));
            }
        }
        for (int i = 0; i < channelBoxs_L.length; i++) {
            if (channelBoxs_L[i].isEnabled())
                channelBoxs_L[i].setSelectedItem(channelStr_L.get(i));
            if (channelBoxs_R[i].isEnabled())
                channelBoxs_R[i].setSelectedItem(channelStr_R.get(i));
        }
        for (int j = 0; j < 16; j++) {
            channelBoxs_L[j].addItemListener(listener);
            channelBoxs_R[j].addItemListener(listener);
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        NewJTable table_DkGl = (NewJTable) MainUi.map.get("table_DkGl");
        if (table_DkGl.getRowCount() > 0) {
            addDengKuTonDaoList();
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
