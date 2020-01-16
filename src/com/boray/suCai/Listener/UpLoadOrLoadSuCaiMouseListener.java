package com.boray.suCai.Listener;

import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UpLoadOrLoadSuCaiMouseListener implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 3) {//鼠标右键
            JList list = (JList) MainUi.map.get("suCai_list");//素材列表
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem export = new JMenuItem("　导出");
            JMenuItem lead = new JMenuItem("　导入");
            JMenuItem coverage = new JMenuItem("　导入覆盖");
            coverage.setEnabled(false);
            if(list.getSelectedValue()!=null){//选中
                coverage.setEnabled(true);
            }
            UpLoadOrLoadSuCaiListener listener = new UpLoadOrLoadSuCaiListener();
            export.addActionListener(listener);
            lead.addActionListener(listener);
            coverage.addActionListener(listener);
            popupMenu.add(export);
            popupMenu.add(lead);
            popupMenu.add(coverage);
            popupMenu.show(list,e.getX(),e.getY());
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
