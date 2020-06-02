package bezier;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;

import javax.swing.*;
import javax.swing.JSpinner.DefaultEditor;

import com.boray.Data.Data;
import com.boray.Utils.IconJDialog;
import com.boray.Utils.UtilJDialog;
import com.boray.mainUi.MainUi;

public class bezierListener implements ActionListener {
    private BezierPanel bp;
    private int selected = 0;
    private JSpinner spinner;
    private JComboBox[] boxs;
    private int sled = 48;
    private int sled2 = 61;
    private String[] temp;

    public bezierListener(BezierPanel bezierPanel2, int sled, JComboBox[] box, JSpinner spinner) {
        bp = bezierPanel2;
        selected = sled;
        this.spinner = spinner;
        this.boxs = box;
    }

    public void actionPerformed(ActionEvent e) {
        String s = e.getActionCommand();
        if ("放大".equals(s)) {
            bp.setZoom(0);
        } else if ("缩小".equals(s)) {
            bp.setZoom(1);
        } else if ("重置".equals(s)) {
			/*if (selected == 0) {
				bp.setShape(1);
			} else if (selected == 1) {
				bp.setShape(0);
			} else if (selected > 1 && selected < 48) {
				bp.setShape(selected);
			} else if (selected >= 48) {
				bp.setShape(0);
			}*/
            bp.setShape(0);
        } else if ("旋转".equals(s)) {
            int degree = 1;
            try {
                spinner.commitEdit();
            } catch (ParseException e1) {
                JComponent editor = spinner.getEditor();
                if (editor instanceof DefaultEditor) {
                    int a = Integer.valueOf(((DefaultEditor) editor).getTextField().getText()).intValue();
                    if (a > 359) {
                        spinner.setValue(359);
                    } else if (a < 1) {
                        spinner.setValue(1);
                    }
                }
            }
            degree = Integer.valueOf(spinner.getValue().toString()).intValue();
            bp.setRotate(degree);
        } else if ("保存".equals(s)) {
            temp = null;
            JFrame frame = (JFrame) MainUi.map.get("frame");
            if (bp.image == null) {
                //if (selected == 0 || (selected > 1 && selected < 48)) {
                if (selected < 48) {
                    JComboBox boxTemp = boxs[0];
//                    int sled = 48;
                    for (int i = 48; i < 256; i++) {
                        if (!boxTemp.getItemAt(i).toString().contains("(")) {
                            sled = i;
                            break;
                        }
                    }
                    int sl = JOptionPane.showConfirmDialog(frame, "图形数据将保存在动作图形" + (sled - 1) + "中，是否继续？", "提示", JOptionPane.YES_NO_OPTION);
                    if (sl == 0) {
                        JFrame f = (JFrame) MainUi.map.get("frame");
                        final IconJDialog dialog = new IconJDialog(f, true);
                        final JTextField field = new JTextField(12);
                        ActionListener listener = new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(e.getActionCommand().equals("确定")){
                                    JComboBox box = boxs[0];
                                    box.removeItemAt(sled);
                                    box.insertItemAt((sled - 1) + "(" + field.getText() + ")", sled);
                                    String[] tps = new String[256];
                                    for (int i = 0; i < 256; i++) {
                                        tps[i] = box.getItemAt(i).toString();
                                    }
                                    bezier.Data.itemMap.put("0", tps);
                                    temp = bp.getPoints();
                                    bezier.Data.map.put("" + sled, temp);
                                    dialog.dispose();
                                }else{
                                    dialog.dispose();
                                }
                            }
                        };
                        UtilJDialog utilJdialog = new UtilJDialog(dialog, "自定义图形", field, "动作名称", listener);
                        utilJdialog.isVisible(true);
//                        JFileChooser fileChooser = new JFileChooser();
//                        if (!Data.saveCtrlFilePath.equals("")) {
//                            fileChooser.setCurrentDirectory(new File(Data.saveCtrlFilePath));
//                        }
//                        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                        fileChooser.setSelectedFile(new File("" + (sled - 1)));
//                        int returnVal = fileChooser.showSaveDialog((JFrame) MainUi.map.get("frame"));
//                        if (returnVal == JFileChooser.APPROVE_OPTION) {
//                            File file = fileChooser.getSelectedFile();
//                            JComboBox box = boxs[0];
//                            box.removeItemAt(sled);
//                            box.insertItemAt((sled - 1) + "(" + file.getName() + ")", sled);
//                            String[] tps = new String[256];
//                            for (int i = 0; i < 256; i++) {
//                                tps[i] = box.getItemAt(i).toString();
//                            }
//                            bezier.Data.itemMap.put("0", tps);
//                            temp = bp.getPoints();
//                            bezier.Data.map.put("" + sled, temp);
//                        }
                    }
                    return;
                } else {
                    temp = bp.getPoints();
                    JFrame f = (JFrame) MainUi.map.get("frame");
                    final IconJDialog dialog = new IconJDialog(f, true);
                    final JTextField field = new JTextField(12);
                    ActionListener listener = new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(e.getActionCommand().equals("确定")){
                                JComboBox box = boxs[0];
                                box.removeItemAt(selected);
                                box.insertItemAt((selected - 1) + "(" + field.getText() + ")", selected);
                                box.setSelectedIndex(selected);
                                String[] tps = new String[256];
                                for (int i = 0; i < 256; i++) {
                                    tps[i] = box.getItemAt(i).toString();
                                }
                                bezier.Data.itemMap.put("0", tps);
                                bezier.Data.map.put("" + selected, temp);
                                dialog.dispose();
                            }else{
                                dialog.dispose();
                            }
                        }
                    };
                    UtilJDialog utilJdialog = new UtilJDialog(dialog, "自定义图形", field, "动作名称", listener);
                    utilJdialog.isVisible(true);
//                    JFileChooser fileChooser = new JFileChooser();
//                    if (!Data.saveCtrlFilePath.equals("")) {
//                        fileChooser.setCurrentDirectory(new File(Data.saveCtrlFilePath));
//                    }
//                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                    fileChooser.setSelectedFile(new File("" + (selected - 1)));
//                    int returnVal = fileChooser.showSaveDialog((JFrame) MainUi.map.get("frame"));
//                    if (returnVal == JFileChooser.APPROVE_OPTION) {
//                        File file = fileChooser.getSelectedFile();
//
//                    }
                }
            } else {
                if (selected < 61) {
                    JComboBox boxTemp = boxs[0];

                    for (int i = 61; i < 256; i++) {
                        if (!boxTemp.getItemAt(i).toString().contains("(")) {
                            sled2 = i;
                            break;
                        }
                    }
                    int sl = JOptionPane.showConfirmDialog(frame, "图形数据将保存在动作图形" + sled2 + "中，是否继续？", "提示", JOptionPane.YES_NO_OPTION);
                    if (sl == 0) {
                        JFrame f = (JFrame) MainUi.map.get("frame");
                        final IconJDialog dialog = new IconJDialog(f, true);
                        final JTextField field = new JTextField(12);
                        ActionListener listener = new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                if(e.getActionCommand().equals("确定")){
                                    JComboBox box = null;
                                    for (int i = 2; i >= 0; i--) {
                                        box = boxs[i];
                                        if (box != null) {
                                            box.removeItemAt(sled2);
                                            box.insertItemAt(sled2 + "(" + field.getText() + ")", sled2);
                                        }
                                    }
                                    //box = boxs[0];
                                    String[] tps = new String[256];
                                    for (int i = 0; i < 256; i++) {
                                        tps[i] = box.getItemAt(i).toString();
                                    }
                                    bezier.Data.itemMap.put("1", tps);
                                    temp = bp.getPoints();
                                    bezier.Data.map.put("color" + sled2, temp);
                                    dialog.dispose();
                                }else{
                                    dialog.dispose();
                                }
                            }
                        };
                        UtilJDialog utilJdialog = new UtilJDialog(dialog, "自定义图形", field, "动作名称", listener);
                        utilJdialog.isVisible(true);
//                        JFileChooser fileChooser = new JFileChooser();
//                        if (!Data.saveCtrlFilePath.equals("")) {
//                            fileChooser.setCurrentDirectory(new File(Data.saveCtrlFilePath));
//                        }
//                        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                        fileChooser.setSelectedFile(new File("" + sled));
//                        int returnVal = fileChooser.showSaveDialog((JFrame) MainUi.map.get("frame"));
//                        if (returnVal == JFileChooser.APPROVE_OPTION) {
//                            File file = fileChooser.getSelectedFile();
//
//                        }
                    }
                    return;
                } else {
                    JFrame f = (JFrame) MainUi.map.get("frame");
                    final IconJDialog dialog = new IconJDialog(f, true);
                    final JTextField field = new JTextField(12);
                    ActionListener listener = new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if(e.getActionCommand().equals("确定")){
                                JComboBox box = null;
                                for (int i = 2; i >= 0; i--) {
                                    box = boxs[i];
                                    if (box != null) {
                                        box.removeItemAt(selected);
                                        box.insertItemAt(selected + "(" + field.getText() + ")", selected);
                                    }
                                }

                                box.setSelectedIndex(selected);
                                String[] tps = new String[256];
                                for (int i = 0; i < 256; i++) {
                                    tps[i] = box.getItemAt(i).toString();
                                }
                                bezier.Data.itemMap.put("1", tps);
                                temp = bp.getPoints();
                                bezier.Data.map.put("color" + selected, temp);
                                dialog.dispose();
                            }else{
                                dialog.dispose();
                            }
                        }
                    };
                    UtilJDialog utilJdialog = new UtilJDialog(dialog, "自定义图形", field, "动作名称", listener);
                    utilJdialog.isVisible(true);
//                    JFileChooser fileChooser = new JFileChooser();
//                    if (!Data.saveCtrlFilePath.equals("")) {
//                        fileChooser.setCurrentDirectory(new File(Data.saveCtrlFilePath));
//                    }
//                    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//                    fileChooser.setSelectedFile(new File("" + selected));
//                    int returnVal = fileChooser.showSaveDialog((JFrame) MainUi.map.get("frame"));
//                    if (returnVal == JFileChooser.APPROVE_OPTION) {
//                        File file = fileChooser.getSelectedFile();
//
//
//                    }
                }
            }
        }
    }

}
