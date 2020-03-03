package com.boray.shengKonSuCai.Listener;

import com.boray.Data.Data;
import com.boray.mainUi.MainUi;
import com.boray.shengKonSuCai.UI.ShengKonSuCaiUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpLoadOrLoadShengKonSuCaiListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("������".equals(e.getActionCommand())) {
            JFileChooser fileChooser = new JFileChooser();
            if (!"".equals(Data.projectFilePath))
                fileChooser.setCurrentDirectory(new File(Data.projectFilePath));
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setSelectedFile(new File(getFileName()));//�����ļ���
            int returnVal = fileChooser.showSaveDialog((Component) MainUi.map.get("frame"));
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                Data.projectFilePath = file.getParent();
                saveData(file);
            }
        } else if ("������".equals(e.getActionCommand())) {
            JFileChooser fileChooser = new JFileChooser();
            if (!"".equals(Data.projectFilePath))
                fileChooser.setCurrentDirectory(new File(Data.projectFilePath));
            String[] houZhui = {"xml"};
            FileNameExtensionFilter filter = new FileNameExtensionFilter("*.xml",houZhui);
            fileChooser.setFileFilter(filter);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = fileChooser.showOpenDialog((JFrame) MainUi.map.get("frame"));
            if(returnVal==JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                Data.projectFilePath = file.getParent();
                loadData(file);
            }
        }else if ("�����븲��".equals(e.getActionCommand())) {
            Object[] options = {"��", "��"};
            int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "�Ƿ񸲸�ѡ�����ݣ�", "����",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                    null, options, options[1]);
            if (yes == 1) {
                JFileChooser fileChooser = new JFileChooser();
                try {
                    if (!"".equals(Data.projectFilePath)) {
                        fileChooser.setCurrentDirectory(new File(Data.projectFilePath));
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                String[] houZhui = {"xml"};
                FileNameExtensionFilter filter = new FileNameExtensionFilter("*.xml", houZhui);
                fileChooser.setFileFilter(filter);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnVal = fileChooser.showOpenDialog((JFrame) MainUi.map.get("frame"));
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    Data.projectFilePath = file.getParent();
                    loadDataCoverage(file);
                }
            }
        }
    }

    private void loadDataCoverage(File file) {
        try {
            JList list = (JList) MainUi.map.get("shengKonSuCaiLightType");
            InputStream is = new FileInputStream(file);
            XMLDecoder xmlDecoder = new XMLDecoder(is);
            String type = null;
            String suCaiName = null;
            Object o = null;
            try {
                String logo = (String) xmlDecoder.readObject();//��ʶ
                if(!"Boray".equals(decode(logo))){
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "�زĵ���ʧ�ܣ����ļ������زģ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                String suCaiType = (String) xmlDecoder.readObject();//�����ز�
                if(!"shengKon".equals(decode(suCaiType))){
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "�زĵ���ʧ�ܣ����ļ����������زģ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                type = (String) xmlDecoder.readObject();//��ȡ�ز�����
                suCaiName = (String) xmlDecoder.readObject();//��ȡ�ز�����
                o = xmlDecoder.readObject();//��ȡ�ز�����
            }catch (Exception e){
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "�زĵ���ʧ�ܣ����ļ������زģ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiTypeBtns");
            String[] name = {"����", "��ҡ", "����", "���", "����", "��ܰ", "����", "�λ�", "����"};
            String str = null;
            for (int i = 0;i<btns.length;i++){
                if(btns[i].isSelected()){
                    str = name[i];
                }
            }
            if(!str.equals(type)){
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "�زĵ���ʧ�ܣ�����������ѡ�е����Ͳ�һ�£�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            //��������
            JFrame f = (JFrame) MainUi.map.get("frame");
            JDialog dialog = new JDialog(f, true);
            dialog.setResizable(false);
            dialog.setTitle("�����ز�");
            int w = 380, h = 180;
            dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
            dialog.setSize(w, h);
            dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            init2(dialog, type, suCaiName, o);
            dialog.setVisible(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init2(final JDialog dialog,final String type,final String suCaiName,final Object o) {
        JPanel p1 = new JPanel();
        p1.add(new JLabel("�ز����ƣ�"));
        final JTextField field = new JTextField(15);
        field.setText(suCaiName);
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
                    JList suCaiList = (JList) MainUi.map.get("shengKonSuCai_list");
                    JList dengkuList = (JList) MainUi.map.get("shengKonSuCaiLightType");
                    JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiTypeBtns");
                    Map nameMap = (Map) Data.shengKonSuCaiNameMap.get(dengkuList.getSelectedValue().toString());
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
                    Data.ShengKonSuCai[dengkuList.getSelectedIndex()][number-1] = o;
                    JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "����ɹ���", "��ʾ", JOptionPane.ERROR_MESSAGE);
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

    /**
     * ���ݵ��顢���͡��ز�ƴ�Ӷ�Ӧ���ļ���
     *
     * @return
     */
    private String getFileName() {
        String fileName = "";
        JList list = (JList) MainUi.map.get("shengKonSuCaiLightType");
        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiTypeBtns");
        JList suCai_list = (JList) MainUi.map.get("shengKonSuCai_list");
        String type = "";
        String[] name = {"����", "��ҡ", "����", "���", "����", "��ܰ", "����", "�λ�", "����"};
        for (int i = 0; i < btns.length; i++) {
            if (btns[i].isSelected()) {
                type = name[i];
                break;
            }
        }
        String suCaiSelect = suCai_list.getSelectedValue().toString();
        fileName = "�����ز�----" + list.getSelectedValue().toString() + "----" + type + "----" + suCaiSelect.split("--->")[0] + ".xml";
        return fileName;
    }

    /**
     * ���������ļ�
     *
     * @param file
     */
    private void saveData(File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            XMLEncoder xmlEncoder = new XMLEncoder(os);
            JList list = (JList) MainUi.map.get("shengKonSuCaiLightType");
            JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiTypeBtns");
            JList suCai_list = (JList) MainUi.map.get("shengKonSuCai_list");
            String[] name = {"����", "��ҡ", "����", "���", "����", "��ܰ", "����", "�λ�", "����"};
            String suCaiSelect = suCai_list.getSelectedValue().toString();
            String suCaiName = suCaiSelect.substring(0, suCaiSelect.indexOf("-"));
            int suCaiIndex = Integer.parseInt(suCaiSelect.split(">")[1]) - 1;//�ز��±�
            int dengZu = list.getSelectedIndex();//�����±�
            String type = "";//�ز�����
            for (int i = 0; i < btns.length; i++) {
                if (btns[i].isSelected()) {
                    type = name[i];
                    break;
                }
            }
            xmlEncoder.writeObject(encode("Boray"));//�����ʶ
            xmlEncoder.writeObject(encode("shengKon"));//�����ز�
            xmlEncoder.writeObject(type);//д���ز�����
            xmlEncoder.writeObject(suCaiName);//д���ز�����
            xmlEncoder.writeObject(Data.ShengKonSuCai[dengZu][suCaiIndex]);//д���ز�����

            xmlEncoder.flush();
            xmlEncoder.close();
            JFrame frame = (JFrame) MainUi.map.get("frame");
            JOptionPane.showMessageDialog(frame, "�زĵ����ɹ���", "��ʾ", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ��������
     * @param file
     */
    public void loadData(File file){
        try {
            JList list = (JList) MainUi.map.get("shengKonSuCaiLightType");
            InputStream is = new FileInputStream(file);
            XMLDecoder xmlDecoder = new XMLDecoder(is);
            String type = null;
            String suCaiName = null;
            Object o = null;
            try {
                String logo = (String) xmlDecoder.readObject();//��ʶ
                if(!"Boray".equals(decode(logo))){
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "�زĵ���ʧ�ܣ����ļ������زģ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                String suCaiType = (String) xmlDecoder.readObject();//�����ز�
                if(!"shengKon".equals(decode(suCaiType))){
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "�زĵ���ʧ�ܣ����ļ����������زģ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                type = (String) xmlDecoder.readObject();//��ȡ�ز�����
                suCaiName = (String) xmlDecoder.readObject();//��ȡ�ز�����
                o = xmlDecoder.readObject();//��ȡ�ز�����
            }catch (Exception e){
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "�زĵ���ʧ�ܣ����ļ������زģ�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiTypeBtns");
            String[] name = {"����", "��ҡ", "����", "���", "����", "��ܰ", "����", "�λ�", "����"};
            String str = null;
            for (int i = 0;i<btns.length;i++){
                if(btns[i].isSelected()){
                    str = name[i];
                }
            }
            if(!str.equals(type)){
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "�زĵ���ʧ�ܣ�����������ѡ�е����Ͳ�һ�£�", "��ʾ", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            //��������
            JFrame f = (JFrame) MainUi.map.get("frame");
            JDialog dialog = new JDialog(f, true);
            dialog.setResizable(false);
            dialog.setTitle("�����ز�");
            int w = 380, h = 180;
            dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
            dialog.setSize(w, h);
            dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            init(dialog, type, suCaiName, o);
            dialog.setVisible(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void init(final JDialog dialog, final String type, final String suCaiName, final Object o){
        JPanel p1 = new JPanel();
        p1.add(new JLabel("�ز����ƣ�"));
        final JTextField field = new JTextField(15);
        field.setText(suCaiName);
        p1.add(field);
        JPanel p2 = new JPanel();
        JButton btn1 = new JButton("ȷ��");
        JButton btn2 = new JButton("ȡ��");
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("ȡ��".equals(e.getActionCommand())) {
                    dialog.dispose();
                }else{
                    if (!"".equals(field.getText().trim())) {
                        JList list = (JList) MainUi.map.get("shengKonSuCai_list");
                        DefaultListModel model = (DefaultListModel) list.getModel();
                        ///��ȡ�ز�����
                        JList suCaiLightType = (JList) MainUi.map.get("shengKonSuCaiLightType");
                        Map map2 = (Map) Data.shengKonSuCaiMap.get(suCaiLightType.getSelectedValue().toString());
                        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiTypeBtns");
                        String[] name = {"����", "��ҡ", "����", "���", "����", "��ܰ", "����", "�λ�", "����"};
                        int cnt = 0;
                        if (map2 != null) {
                            for (int i = 0; i < btns.length; i++) {
                                java.util.List abc = (java.util.List) map2.get("" + i);
                                if (abc != null) {
                                    cnt = cnt + abc.size();
                                }
                            }
                        }
                        if (cnt == 50) {
                            JFrame frame = (JFrame) MainUi.map.get("frame");
                            JOptionPane.showMessageDialog(frame, "���ֻ�ܴ���50���زģ�", "��ʾ", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        String suCaiNameAndNumber = field.getText() + "--->" + (cnt + 1);
                        Data.ShengKonSuCai[suCaiLightType.getSelectedIndex()][cnt] = o;
                        if (model == null) {
                            model = new DefaultListModel();
                            model.addElement(suCaiNameAndNumber);
                            list.setModel(model);
                        } else {
                            model.addElement(suCaiNameAndNumber);
                        }
                        list.setSelectedIndex(model.getSize() - 1);
                        Map map = (Map) Data.shengKonSuCaiMap.get(suCaiLightType.getSelectedValue().toString());
                        Map nameMap = (Map) Data.shengKonSuCaiNameMap.get(suCaiLightType.getSelectedValue().toString());
                        if (map == null) {
                            map = new HashMap<>();
                            Data.shengKonSuCaiMap.put(suCaiLightType.getSelectedValue().toString(), map);
                        }
                        if (nameMap == null) {
                            nameMap = new HashMap<>();
                            Data.shengKonSuCaiNameMap.put(suCaiLightType.getSelectedValue().toString(), nameMap);
                        }
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
                        ShengKonSuCaiUI suCaiUI = new ShengKonSuCaiUI();
                        String aloneCount = suCaiUI.getAlone(suCaiLightType.getSelectedValue().toString());//��ǰ�ƿ���ز�����
                        JLabel alone = (JLabel) MainUi.map.get("shengKonAlone");
                        alone.setText(aloneCount);

                        String count = suCaiUI.getCount();//���еƿ���ز�����
                        JLabel countLabel = (JLabel) MainUi.map.get("shengKonCount");
                        countLabel.setText(count);
                        JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "����ɹ���", "��ʾ", JOptionPane.ERROR_MESSAGE);
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

    //����
    public String encode(String str) {
        return new sun.misc.BASE64Encoder().encode(str.getBytes());
    }

    //����
    public String decode(String s) {
        String str = null;
        try {
            sun.misc.BASE64Decoder decode = new sun.misc.BASE64Decoder();
            str = new String(decode.decodeBuffer(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }
}