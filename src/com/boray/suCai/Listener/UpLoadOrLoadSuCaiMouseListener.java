package com.boray.suCai.Listener;

import com.boray.mainUi.MainUi;
import com.boray.suCai.UI.SuCaiEditUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UpLoadOrLoadSuCaiMouseListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {
        JList list = (JList) e.getSource();
        if (list.getSelectedIndex() != -1) {
            if (e.getClickCount() == 2) {
                JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");
                String suCaiName = list.getSelectedValue().toString();
                int suCaiNum = Integer.valueOf(suCaiName.split("--->")[1]).intValue();
                new SuCaiEditUI().show(suCaiName, suCaiNum, suCaiLightType.getSelectedIndex());
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 3) {//鼠标右键
            JList list = (JList) MainUi.map.get("suCai_list");//素材列表
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem export = new JMenuItem("　导出");
            JMenuItem lead = new JMenuItem("　导入");
            JMenuItem coverage = new JMenuItem("　导入覆盖");
            JMenuItem copy = new JMenuItem("    复制");
            JMenuItem paste = new JMenuItem("    粘贴");
            coverage.setEnabled(false);
            if (list.getSelectedValue() != null) {//选中
                coverage.setEnabled(true);
            }
            UpLoadOrLoadSuCaiListener listener = new UpLoadOrLoadSuCaiListener();
            export.addActionListener(listener);
            lead.addActionListener(listener);
            coverage.addActionListener(listener);
            copy.addActionListener(listener);
            paste.addActionListener(listener);
            popupMenu.add(export);
            popupMenu.add(lead);
            popupMenu.add(coverage);
            popupMenu.add(copy);
            popupMenu.add(paste);
            popupMenu.show(list, e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
