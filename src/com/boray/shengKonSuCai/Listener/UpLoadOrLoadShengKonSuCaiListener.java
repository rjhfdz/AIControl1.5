package com.boray.shengKonSuCai.Listener;

import com.boray.Data.Data;
import com.boray.Utils.IconJDialog;
import com.boray.Utils.Util;
import com.boray.mainUi.MainUi;
import com.boray.shengKonSuCai.UI.ShengKonSuCaiUI;
import com.boray.suCai.UI.SuCaiUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;
import java.util.List;

public class UpLoadOrLoadShengKonSuCaiListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("　导出".equals(e.getActionCommand())) {
            JFileChooser fileChooser = new JFileChooser();
            if (!"".equals(Data.projectFilePath))
                fileChooser.setCurrentDirectory(new File(Data.projectFilePath));
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setSelectedFile(new File(getFileName()));//设置文件名
            int returnVal = fileChooser.showSaveDialog((Component) MainUi.map.get("frame"));
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                Data.projectFilePath = file.getParent();
                saveData(file);
            }
        } else if ("　导入".equals(e.getActionCommand())) {
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
        }else if ("　导入覆盖".equals(e.getActionCommand())) {
            Object[] options = {"否", "是"};
            int yes = JOptionPane.showOptionDialog((JFrame) MainUi.map.get("frame"), "是否覆盖选中数据？", "警告",
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
        }else if("    复制".equals(e.getActionCommand())){
            JList list = (JList) MainUi.map.get("shengKonSuCaiLightType");//灯组列表
            JList suCai_list = (JList) MainUi.map.get("shengKonSuCai_list");//素材列表
            String suCaiSelect = suCai_list.getSelectedValue().toString();
            int suCaiIndex = Integer.parseInt(suCaiSelect.split(">")[1]) - 1;//素材下标
            int dengKuIndex = list.getSelectedIndex();//灯库下标
            Data.tempShengKonSuCai = dengKuIndex + "#" + suCaiIndex;
        }else if("    粘贴".equals(e.getActionCommand())){
            if (Data.tempShengKonSuCai == null) {
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "素材粘贴失败，未复制素材！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }else{
                JList list = (JList) MainUi.map.get("shengKonSuCaiLightType");//灯库列表
                String[] str = Data.tempShengKonSuCai.split("#");
                if (!str[0].equals(list.getSelectedIndex() + "")) {
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "素材粘贴失败，所选灯库不一致！", "提示", JOptionPane.PLAIN_MESSAGE);
                    return;
                } else {
                    JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiTypeBtns");
                    String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
                    String s = null;
                    for (int i = 0; i < btns.length; i++) {
                        if (btns[i].isSelected()) {
                            s = name[i];
                        }
                    }
                    //弹出界面
                    JFrame f = (JFrame) MainUi.map.get("frame");
                    IconJDialog dialog = new IconJDialog(f, true);
                    dialog.setResizable(false);
                    dialog.setTitle("粘贴素材");
                    int w = 380, h = 180;
                    dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
                    dialog.setSize(w, h);
                    dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
                    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    init(dialog, s, "", Data.ShengKonSuCai[Integer.valueOf(str[0])][Integer.valueOf(str[1])], "粘贴成功！");
                    dialog.setVisible(true);
                }
            }
        }
    }

    //初始化界面
    public void init(final JDialog dialog, final String type, final String suCaiName, final Object o, final String message) {
        JPanel p1 = new JPanel();
        p1.add(new JLabel("素材名称："));
        final JTextField field = new JTextField(15);
        field.setText(suCaiName);
        p1.add(field);
        JPanel p2 = new JPanel();
        JButton btn1 = new JButton("确定");
        JButton btn2 = new JButton("取消");
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("取消".equals(e.getActionCommand())) {
                    dialog.dispose();
                } else {
                    if (!"".equals(field.getText().trim())) {
                        JList list = (JList) MainUi.map.get("shengKonSuCai_list");
                        DefaultListModel model = (DefaultListModel) list.getModel();
                        //////////////////获取素材数量
                        JList suCaiLightType = (JList) MainUi.map.get("shengKonSuCaiLightType");
                        Map map2 = (Map) Data.shengKonSuCaiMap.get(suCaiLightType.getSelectedValue().toString());
                        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiTypeBtns");
                        String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
                        int cnt = 0;
                        if (map2 != null) {
                            for (int i = 0; i < btns.length; i++) {
                                java.util.List abc = (List) map2.get("" + i);
                                if (abc != null) {
                                    cnt = cnt + abc.size();
                                }
                            }
                        }
                        if (cnt == 30) {
                            JFrame frame = (JFrame) MainUi.map.get("frame");
                            JOptionPane.showMessageDialog(frame, "最多只能创建50个素材！", "提示", JOptionPane.PLAIN_MESSAGE);
                            return;
                        }
                        String suCaiNameAndNumber = field.getText() + "--->" + (cnt + 1);

                        Object newObj = Util.Clone(o);
                        Data.ShengKonSuCai[suCaiLightType.getSelectedIndex()][cnt] = newObj;

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
                            if (btns[i].getText().contains(type)) {
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
                        String aloneCount = suCaiUI.getAlone(suCaiLightType.getSelectedValue().toString());//当前灯库的素材数量
                        JLabel alone = (JLabel) MainUi.map.get("shengKonAlone");
                        alone.setText(aloneCount);

                        String count = suCaiUI.getCount();//所有灯库的素材数量
                        JLabel countLabel = (JLabel) MainUi.map.get("shengKonCount");
                        countLabel.setText(count);
                        JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), message, "提示", JOptionPane.PLAIN_MESSAGE);
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

    private void loadDataCoverage(File file) {
        try {
            JList list = (JList) MainUi.map.get("shengKonSuCaiLightType");
            InputStream is = new FileInputStream(file);
            XMLDecoder xmlDecoder = new XMLDecoder(is);
            String type = null;
            String suCaiName = null;
            Object o = null;
            try {
                String logo = (String) xmlDecoder.readObject();//标识
                if(!"Boray".equals(decode(logo))){
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "素材导入失败，该文件不是素材！", "提示", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                String suCaiType = (String) xmlDecoder.readObject();//声控素材
                if(!"shengKon".equals(decode(suCaiType))){
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "素材导入失败，该文件不是声控素材！", "提示", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                type = (String) xmlDecoder.readObject();//读取素材类型
                suCaiName = (String) xmlDecoder.readObject();//读取素材名称
                o = xmlDecoder.readObject();//读取素材数据
            }catch (Exception e){
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "素材导入失败，该文件不是素材！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiTypeBtns");
            String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
            String str = null;
            for (int i = 0;i<btns.length;i++){
                if(btns[i].isSelected()){
                    str = name[i];
                }
            }
            if(!str.equals(type)){
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "素材导入失败，导入类型与选中的类型不一致！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            //弹出界面
            JFrame f = (JFrame) MainUi.map.get("frame");
            IconJDialog dialog = new IconJDialog(f, true);
            dialog.setResizable(false);
            dialog.setTitle("导入素材");
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
        p1.add(new JLabel("素材名称："));
        final JTextField field = new JTextField(15);
        field.setText(suCaiName);
        p1.add(field);
        JPanel p2 = new JPanel();
        JButton btn1 = new JButton("确定");
        JButton btn2 = new JButton("取消");

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("取消".equals(e.getActionCommand())) {
                    dialog.dispose();
                } else {
                    JList suCaiList = (JList) MainUi.map.get("shengKonSuCai_list");
                    JList dengkuList = (JList) MainUi.map.get("shengKonSuCaiLightType");
                    JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiTypeBtns");
                    Map nameMap = (Map) Data.shengKonSuCaiNameMap.get(dengkuList.getSelectedValue().toString());
                    int number = Integer.parseInt(suCaiList.getSelectedValue().toString().split("--->")[1]);//获得对应素材的编号
                    int index = suCaiList.getSelectedIndex();//获得对应下标
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
                    JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "导入成功！", "提示", JOptionPane.ERROR_MESSAGE);
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
     * 根据灯组、类型、素材拼接对应的文件名
     *
     * @return
     */
    private String getFileName() {
        String fileName = "";
        JList list = (JList) MainUi.map.get("shengKonSuCaiLightType");
        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiTypeBtns");
        JList suCai_list = (JList) MainUi.map.get("shengKonSuCai_list");
        String type = "";
        String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
        for (int i = 0; i < btns.length; i++) {
            if (btns[i].isSelected()) {
                type = name[i];
                break;
            }
        }
        String suCaiSelect = suCai_list.getSelectedValue().toString();
        fileName = "声控-" + list.getSelectedValue().toString() + "-" + type + "-" + suCaiSelect.split("--->")[0] + ".xml";
        return fileName;
    }

    /**
     * 保存数据文件
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
            String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
            String suCaiSelect = suCai_list.getSelectedValue().toString();
            String suCaiName = suCaiSelect.substring(0, suCaiSelect.indexOf("-"));
            int suCaiIndex = Integer.parseInt(suCaiSelect.split(">")[1]) - 1;//素材下标
            int dengZu = list.getSelectedIndex();//灯组下标
            String type = "";//素材类型
            for (int i = 0; i < btns.length; i++) {
                if (btns[i].isSelected()) {
                    type = name[i];
                    break;
                }
            }
            xmlEncoder.writeObject(encode("Boray"));//加入标识
            xmlEncoder.writeObject(encode("shengKon"));//声控素材
            xmlEncoder.writeObject(type);//写入素材类型
            xmlEncoder.writeObject(suCaiName);//写入素材名称
            Map map = (Map) Data.ShengKonSuCai[dengZu][suCaiIndex];
            TreeSet treeSet = (TreeSet) Data.GroupOfLightList.get(dengZu);
            map.put("dengZuCount",treeSet.size());
            Data.ShengKonSuCai[dengZu][suCaiIndex] = map;
            xmlEncoder.writeObject(Data.ShengKonSuCai[dengZu][suCaiIndex]);//写入素材数据

            xmlEncoder.flush();
            xmlEncoder.close();
            JFrame frame = (JFrame) MainUi.map.get("frame");
            JOptionPane.showMessageDialog(frame, "素材导出成功！", "提示", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载数据
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
                String logo = (String) xmlDecoder.readObject();//标识
                if(!"Boray".equals(decode(logo))){
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "素材导入失败，该文件不是素材！", "提示", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                String suCaiType = (String) xmlDecoder.readObject();//声控素材
                if(!"shengKon".equals(decode(suCaiType))){
                    JFrame frame = (JFrame) MainUi.map.get("frame");
                    JOptionPane.showMessageDialog(frame, "素材导入失败，该文件不是声控素材！", "提示", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                type = (String) xmlDecoder.readObject();//读取素材类型
                suCaiName = (String) xmlDecoder.readObject();//读取素材名称
                o = xmlDecoder.readObject();//读取素材数据
            }catch (Exception e){
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "素材导入失败，该文件不是素材！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiTypeBtns");
            String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
            String str = null;
            for (int i = 0;i<btns.length;i++){
                if(btns[i].isSelected()){
                    str = name[i];
                }
            }
            if(!str.equals(type)){
                JFrame frame = (JFrame) MainUi.map.get("frame");
                JOptionPane.showMessageDialog(frame, "素材导入失败，导入类型与选中的类型不一致！", "提示", JOptionPane.PLAIN_MESSAGE);
                return;
            }
            //弹出界面
            JFrame f = (JFrame) MainUi.map.get("frame");
            IconJDialog dialog = new IconJDialog(f, true);
            dialog.setResizable(false);
            dialog.setTitle("导入素材");
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
        p1.add(new JLabel("素材名称："));
        final JTextField field = new JTextField(15);
        field.setText(suCaiName);
        p1.add(field);
        JPanel p2 = new JPanel();
        JButton btn1 = new JButton("确定");
        JButton btn2 = new JButton("取消");
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ("取消".equals(e.getActionCommand())) {
                    dialog.dispose();
                }else{
                    if (!"".equals(field.getText().trim())) {
                        JList list = (JList) MainUi.map.get("shengKonSuCai_list");
                        DefaultListModel model = (DefaultListModel) list.getModel();
                        ///获取素材数量
                        JList suCaiLightType = (JList) MainUi.map.get("shengKonSuCaiLightType");
                        if(suCaiLightType.getSelectedValue()==null){
                            JFrame frame = (JFrame) MainUi.map.get("frame");
                            JOptionPane.showMessageDialog(frame, "请新建灯组后再进行操作！", "提示", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        Map map2 = (Map) Data.shengKonSuCaiMap.get(suCaiLightType.getSelectedValue().toString());
                        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("shengKonSuCaiTypeBtns");
                        String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
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
                            JOptionPane.showMessageDialog(frame, "最多只能创建50个素材！", "提示", JOptionPane.ERROR_MESSAGE);
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
                        String aloneCount = suCaiUI.getAlone(suCaiLightType.getSelectedValue().toString());//当前灯库的素材数量
                        JLabel alone = (JLabel) MainUi.map.get("shengKonAlone");
                        alone.setText(aloneCount);

                        String count = suCaiUI.getCount();//所有灯库的素材数量
                        JLabel countLabel = (JLabel) MainUi.map.get("shengKonCount");
                        countLabel.setText(count);
                        JOptionPane.showMessageDialog((JFrame) MainUi.map.get("frame"), "导入成功！", "提示", JOptionPane.ERROR_MESSAGE);
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

    //加密
    public String encode(String str) {
        return new sun.misc.BASE64Encoder().encode(str.getBytes());
    }

    //解密
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
