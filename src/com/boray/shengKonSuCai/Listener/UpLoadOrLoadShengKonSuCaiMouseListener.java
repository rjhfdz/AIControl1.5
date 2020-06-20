package com.boray.shengKonSuCai.Listener;

import com.boray.mainUi.MainUi;
import com.boray.shengKonSuCai.UI.ShengKonSuCaiEditUI;
import com.boray.suCai.UI.SuCaiEditUI;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UpLoadOrLoadShengKonSuCaiMouseListener implements MouseListener {
    @Override
    public void mouseClicked(MouseEvent e) {
        JList list = (JList) e.getSource();
        if (list.getSelectedIndex() != -1) {
            if (e.getClickCount() == 2) {

                JList suCaiLightType = (JList) MainUi.map.get("shengKonSuCaiLightType");
                String suCaiName = list.getSelectedValue().toString();
                int denKuNum = suCaiLightType.getSelectedIndex();
                int suCaiNum = Integer.valueOf(suCaiName.split("--->")[1]).intValue();
                System.out.println(suCaiName + "" + suCaiNum + "" + denKuNum);
                new ShengKonSuCaiEditUI().show(suCaiName, suCaiNum, denKuNum);
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 3) {//����Ҽ�
            JList list = (JList) MainUi.map.get("shengKonSuCai_list");//�ز��б�
            JPopupMenu popupMenu = new JPopupMenu();
            JMenuItem export = new JMenuItem("������");
            JMenuItem lead = new JMenuItem("������");
            JMenuItem coverage = new JMenuItem("�����븲��");
            JMenuItem copy = new JMenuItem("    ����");
            JMenuItem paste = new JMenuItem("    ճ��");
            coverage.setEnabled(false);
            if (list.getSelectedValue() != null) {//ѡ��
                coverage.setEnabled(true);
            }
            UpLoadOrLoadShengKonSuCaiListener listener = new UpLoadOrLoadShengKonSuCaiListener();
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
