package com.boray.suCai.Listener;

import com.boray.Data.Data;
import com.boray.mainUi.MainUi;

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

public class UpLoadOrLoadSuCaiListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("　导出".equals(e.getActionCommand())) {
            JFileChooser fileChooser = new JFileChooser();
            if (!"".equals(Data.projectFilePath)) {
                fileChooser.setCurrentDirectory(new File(Data.projectFilePath));
            }
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setSelectedFile(new File(getFileName()));//设置文件名
            int returnVal = fileChooser.showSaveDialog((JFrame) MainUi.map.get("frame"));
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                Data.projectFilePath = file.getParent();
                saveData(file);
            }
        } else if ("　导入".equals(e.getActionCommand())) {
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
                loadData(file);
            }
        }
    }

    public void saveData(File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            XMLEncoder xmlEncoder = new XMLEncoder(os);
            JList list = (JList) MainUi.map.get("suCaiLightType");//灯库列表
            JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");//类型列表
            JList suCai_list = (JList) MainUi.map.get("suCai_list");//素材列表
            String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
            String suCaiSelect = suCai_list.getSelectedValue().toString();
            String suCaiName = suCaiSelect.substring(0, suCaiSelect.indexOf("-"));
            int suCaiIndex = Integer.parseInt(suCaiSelect.split(">")[1]) - 1;//素材下标
            int dengKuIndex = list.getSelectedIndex();//灯库下标
            String type = "";//素材类型
            for (int i = 0; i < btns.length; i++) {
                if (btns[i].isSelected()) {
                    type = name[i];
                    break;
                }
            }
            xmlEncoder.writeObject(type);//写入素材类型
            xmlEncoder.writeObject(suCaiName);//写入素材名称
            xmlEncoder.writeObject(Data.SuCaiObjects[dengKuIndex][suCaiIndex]);//写入素材数据

            xmlEncoder.flush();
            xmlEncoder.close();
            JFrame frame = (JFrame) MainUi.map.get("frame");
            JOptionPane.showMessageDialog(frame, "素材导出成功！", "提示", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void loadData(File file) {
        try {
            JList list = (JList) MainUi.map.get("suCaiLightType");//灯库列表
            JList suCai_list = (JList) MainUi.map.get("suCai_list");//素材列表
            int dengKuSelect = list.getSelectedIndex();

            InputStream is = new FileInputStream(file);
            XMLDecoder xmlDecoder = new XMLDecoder(is);
            String type = (String) xmlDecoder.readObject();//读取素材类型
            String suCaiName = (String) xmlDecoder.readObject();//读取素材名称
            Object o = xmlDecoder.readObject();//读取素材数据

            //弹出界面
            JFrame f = (JFrame) MainUi.map.get("frame");
            JDialog dialog = new JDialog(f, true);
            dialog.setResizable(false);
            dialog.setTitle("导入素材");
            int w = 380, h = 180;
            dialog.setLocation(f.getLocation().x + f.getSize().width / 2 - w / 2, f.getLocation().y + f.getSize().height / 2 - h / 2);
            dialog.setSize(w, h);
            dialog.setLayout(new FlowLayout(FlowLayout.CENTER));
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            init(dialog, type, suCaiName, o);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //初始化界面
    public void init(final JDialog dialog, final String type, final String suCaiName, final Object o) {
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
                        JList list = (JList) MainUi.map.get("suCai_list");
                        DefaultListModel model = (DefaultListModel) list.getModel();
                        //////////////////获取素材数量
                        JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");
                        Map map2 = (Map) Data.suCaiMap.get(suCaiLightType.getSelectedValue().toString());
                        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
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
                            JOptionPane.showMessageDialog(frame, "最多只能创建30个素材！", "提示", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        String suCaiNameAndNumber = field.getText() + "--->" + (cnt + 1);

                        Data.SuCaiObjects[suCaiLightType.getSelectedIndex()][cnt] = o;

                        if (model == null) {
                            model = new DefaultListModel();
                            model.addElement(suCaiNameAndNumber);
                            list.setModel(model);
                        } else {
                            model.addElement(suCaiNameAndNumber);
                        }
                        list.setSelectedIndex(model.getSize() - 1);
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
     * 根据灯库、类型、素材拼接对应的文件名
     *
     * @return
     */
    public String getFileName() {
        String fileName = "";
        JList list = (JList) MainUi.map.get("suCaiLightType");//灯库列表
        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");//类型列表
        JList suCai_list = (JList) MainUi.map.get("suCai_list");//素材列表
        String type = "";
        String[] name = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};
        for (int i = 0; i < btns.length; i++) {
            if (btns[i].isSelected()) {
                type = name[i];
                break;
            }
        }
        String suCaiSelect = suCai_list.getSelectedValue().toString();
        fileName = list.getSelectedValue().toString() + "----" + type + "----" + suCaiSelect.substring(0, suCaiSelect.indexOf("-")) + ".xml";
        return fileName;
    }
}
