package com.boray.suCai.Listener;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;

import com.boray.Data.Data;
import com.boray.dengKu.UI.NewJTable;
import com.boray.entity.Users;
import com.boray.mainUi.MainUi;
import com.boray.suCai.UI.SuCaiUI;
import com.boray.suCai.UI.YunChangJingSuCaiDialog;

public class CreateOrDelSuCaiListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
        if ("�½�".equals(e.getActionCommand())) {
            JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");
            if (suCaiLightType.getSelectedValue() != null) {
                JFrame f = (JFrame) MainUi.map.get("frame");
                JDialog dialog = new JDialog(f, true);
                dialog.setResizable(false);
                dialog.setTitle("�½��ز�");
                int w = 380, h = 180;
                dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
                dialog.setSize(w, h);
                dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                init(dialog);
                dialog.setVisible(true);
            }
        } else if ("ɾ��".equals(e.getActionCommand())) {
            JList dengkuList = (JList) MainUi.map.get("suCaiLightType");//�ƿ��б�
            JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
            JList suCaiList = (JList) MainUi.map.get("suCai_list");//�ز��б�
            boolean flag = false;
            int index = 0;
            for (int i = 0; i < btns.length; i++) {
                if (btns[i].isSelected()) {
                    flag = true;
                    index = i;
                }
            }
            if (dengkuList.getSelectedValue() != null && suCaiList.getSelectedValue() != null && flag) {
                Object[] options = {"��", "��"};
                int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "�Ƿ�ɾ����", "����",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, options, options[1]);
                if (yes == 1) {
                    String dengkuStr = dengkuList.getSelectedValue().toString();
                    String sucaiIndex = suCaiList.getSelectedValue().toString().split(">")[1];
                }
            } else {
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "��δѡ�����ݣ�", "��ʾ", JOptionPane.ERROR_MESSAGE);
            }
            System.out.println("ɾ��");
        } else if ("������".equals(e.getActionCommand())) {
            JList suCaiList = (JList) MainUi.map.get("suCai_list");//�ز��б�
            String name = suCaiList.getSelectedValue().toString().split("--->")[0];
            JFrame f = (JFrame) MainUi.map.get("frame");
            JDialog dialog = new JDialog(f, true);
            dialog.setResizable(false);
            dialog.setTitle("������");
            int w = 380, h = 180;
            dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
            dialog.setSize(w, h);
            dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            reName(dialog, name);
            dialog.setVisible(true);
        }else if("�ƶ�".equals(e.getActionCommand())){
            Users users = (Users) MainUi.map.get("Users");
            if (users != null && users.getLoginstatus() != 0) {
                JFrame f = (JFrame) MainUi.map.get("frame");
                JDialog dialog = new JDialog(f, true);
                dialog.setTitle("�ƶ��ز�");
                dialog.setResizable(false);
                int width = 720, height = 620;
                dialog.setSize(width, height);
                dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - width / 2, f.getLocation().y + f.getSize().height / 2 - height / 2);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                JPanel p = new JPanel();
                new YunChangJingSuCaiDialog().show(p);
                dialog.getContentPane().add(p);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "���¼", "����", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
    }

    private void reName(final JDialog dialog, final String name) {
        JPanel p1 = new JPanel();
        p1.add(new JLabel("�ز����ƣ�"));
        final JTextField field = new JTextField(15);
        field.setText(name);
        p1.add(field);
        JPanel p2 = new JPanel();
        JButton btn1 = new JButton("ȷ��");
        JButton btn2 = new JButton("ȡ��");

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("ȡ��".equals(e.getActionCommand())) {
                    dialog.dispose();
                } else {
                    JList dengkuList = (JList) MainUi.map.get("suCaiLightType");//�ƿ��б�
                    JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
                    JList suCaiList = (JList) MainUi.map.get("suCai_list");//�ز��б�
                    Map nameMap = (Map) Data.suCaiNameMap.get(dengkuList.getSelectedValue().toString());
                    int number = Integer.parseInt(suCaiList.getSelectedValue().toString().split("--->")[1]);//��ö�Ӧ�زĵı��
                    int index = suCaiList.getSelectedIndex();//��ö�Ӧ�±�
                    int btnIndex = 0;
                    for (int i = 0; i < btns.length; i++) {
                        if (btns[i].isSelected()) {
                            btnIndex = i;
                            break;
                        }
                    }
                    List nameList = (List) nameMap.get("" + btnIndex);
                    nameList.set(index, field.getText() + "--->" + number);
                    suCaiList.removeAll();
                    DefaultListModel model = new DefaultListModel();
                    for (int i = 0; i < nameList.size(); i++) {
                        model.addElement(nameList.get(i));
                    }
                    suCaiList.setModel(model);
                    suCaiList.setSelectedIndex(0);
                    dialog.dispose();
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
        dialog.add(n1);
        dialog.add(p1);
        dialog.add(p2);
    }

    private void init(final JDialog dialog) {
        JPanel p1 = new JPanel();
        p1.add(new JLabel("�ز����ƣ�"));
        final JTextField field = new JTextField(15);
        p1.add(field);
        JPanel p2 = new JPanel();
        JButton btn1 = new JButton("ȷ��");
        JButton btn2 = new JButton("ȡ��");

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ("ȡ��".equals(e.getActionCommand())) {
                    dialog.dispose();
                } else {
                    if (!"".equals(field.getText().trim())) {
                        JList list = (JList) MainUi.map.get("suCai_list");
                        DefaultListModel model = (DefaultListModel) list.getModel();

                        //////////////////��ȡ�ز�����
                        JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");
                        Map map2 = (Map) Data.suCaiMap.get(suCaiLightType.getSelectedValue().toString());
                        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
                        String[] name = {"����", "��ҡ", "����", "���", "����", "��ܰ", "����", "�λ�", "����"};
                        int cnt = 0;
                        if (map2 != null) {
                            for (int i = 0; i < btns.length; i++) {
                                List abc = (List) map2.get("" + i);
                                if (abc != null) {
                                    cnt = cnt + abc.size();
                                }
                            }
                        }
                        if (cnt == 30) {
                            JFrame frame = (JFrame) MainUi.map.get("frame");
                            JOptionPane.showMessageDialog(frame, "���ֻ�ܴ���30���زģ�", "��ʾ", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        String suCaiNameAndNumber = field.getText() + "--->" + (cnt + 1);
//                        Data.AddSuCaiOrder.add((suCaiLightType.getSelectedIndex()) + "#" + cnt);
                        //////////////////

                        if (model == null) {
                            model = new DefaultListModel();
                            model.addElement(suCaiNameAndNumber);
                            list.setModel(model);
                        } else {
                            model.addElement(suCaiNameAndNumber);
                        }
                        list.setSelectedIndex(model.getSize() - 1);
                        //JList suCaiLightType = (JList)MainUi.map.get("suCaiLightType");
                        Map map = (Map) Data.suCaiMap.get(suCaiLightType.getSelectedValue().toString());
                        Map nameMap = (Map) Data.suCaiNameMap.get(suCaiLightType.getSelectedValue().toString());
                        if (map == null) {
                            map = new HashMap<>();
                            Data.suCaiMap.put(suCaiLightType.getSelectedValue().toString(), map);
                        }
                        if (nameMap == null) {
                            nameMap = new HashMap<>();
                            Data.suCaiNameMap.put(suCaiLightType.getSelectedValue().toString(), nameMap);
                        }
                        //JToggleButton[] btns = (JToggleButton[])MainUi.map.get("suCaiTypeBtns");
                        //String[] name = {"Ĭ��","����","����","���","����"};
                        for (int i = 0; i < btns.length; i++) {
                            if (btns[i].isSelected()) {
                                List tmp = (List) map.get("" + i);
                                List nameList = (List) nameMap.get("" + i);
                                if (nameList != null) {
                                    nameList.add(suCaiNameAndNumber);
                                } else {
                                    nameList = new ArrayList<>();
                                    nameList.add(suCaiNameAndNumber);
                                    nameMap.put("" + i, nameList);
                                }
                                if (tmp == null) {
                                    tmp = new ArrayList<>();
                                }
                                tmp.add(new HashMap<>());
                                btns[i].setText(name[i] + "(" + tmp.size() + ")");
                                map.put("" + i, tmp);
                            }
                        }

                        SuCaiUI suCaiUI = new SuCaiUI();

                        String aloneCount = suCaiUI.getAlone(suCaiLightType.getSelectedValue().toString());//��ǰ�ƿ���ز�����
                        JLabel alone = (JLabel) MainUi.map.get("alone");
                        alone.setText(aloneCount);

                        String count = suCaiUI.getCount();//���еƿ���ز�����
                        JLabel countLabel = (JLabel) MainUi.map.get("count");
                        countLabel.setText(count);

                        dialog.dispose();
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
        dialog.add(n1);
        dialog.add(p1);
        dialog.add(p2);
    }

    /**
     * �жϸ��ز��Ƿ�����
     *
     * @return
     */
    public Boolean suCaiIsQuote(String dengkuName) {
        Boolean flag = false;
        NewJTable table = (NewJTable) MainUi.map.get("GroupTable");//��÷����б�
        for (int i = 0; i < 24; i++) {//ѭ������Ч����ģʽ
            for (int j = 0; j < table.getRowCount(); j++) {
                MainUi.map.get("timeBlockPanels_group" + j);
            }
        }
        return flag;
    }
}
