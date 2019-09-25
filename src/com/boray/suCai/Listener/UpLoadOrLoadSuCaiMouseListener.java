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
        if (e.getButton() == 3) {//����Ҽ�
            JList list = (JList) MainUi.map.get("suCai_list");//�ز��б�
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem export = new JMenuItem("������");
            JMenuItem lead = new JMenuItem("������");
//            if(list.getSelectedValue()!=null){//ѡ��
//                lead.setEnabled(false);
//            }
            UpLoadOrLoadSuCaiListener listener = new UpLoadOrLoadSuCaiListener();
            export.addActionListener(listener);
            lead.addActionListener(listener);
            popupMenu.add(export);
            popupMenu.add(lead);
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
