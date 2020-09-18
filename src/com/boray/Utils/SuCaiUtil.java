package com.boray.Utils;

import com.boray.Data.Data;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuCaiUtil {

    public static String[] names = {"����", "��ҡ", "����", "���", "����", "��ܰ", "����", "�λ�", "����"};

    /**
     * ����ͨ�������ز�����
     *
     * @return ������
     */
    public static String getCount() {
        int count = 0;
        JRadioButton radioButton = (JRadioButton) MainUi.map.get("xiaoGuoDengSuCaiTypeButton");
        if (radioButton.isSelected()) {//ͨ��
            NewJTable table = (NewJTable) MainUi.map.get("GroupTable");
            for (int i = 0; i < table.getRowCount(); i++) {
                Map map = (Map) Data.suCaiNameMap.get(i);
                if (map != null)
                    for (Object key : map.keySet()) {
                        List<String> suCaiNameList = (List<String>) map.get(key);
                        if (suCaiNameList != null)
                            count += suCaiNameList.size();
                    }
            }
        } else {//����
            for (Object key : Data.SuCaiDongZuoName.keySet()) {
                List<String> suCaiNameList = (List<String>) Data.SuCaiDongZuoName.get(key);
                if (suCaiNameList != null)
                    count += suCaiNameList.size();
            }
        }
        return count + "";
    }

    /**
     * ���㵱ǰѡ�еĵ����زĻ��߶����ز�����
     *
     * @param selectIndex ͨ���ز���Ҫ�������ز�����
     * @return ������
     */
    public static String getAlone(int selectIndex) {
        int count = 0;
        JRadioButton radioButton = (JRadioButton) MainUi.map.get("xiaoGuoDengSuCaiTypeButton");
        if (radioButton.isSelected()) {//ͨ��
            Map map = (Map) Data.suCaiNameMap.get(selectIndex);
            if (map != null)
                for (Object key : map.keySet()) {
                    List<String> suCaiNameList = (List<String>) map.get(key);
                    if (suCaiNameList != null)
                        count += suCaiNameList.size();
                }
            return count + "";
        }
        return "";
    }

    /**
     * ��õ�ǰѡ�е��ز�����
     *
     * @return
     */
    public static String getXiaoGuoDengType() {
        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
        String selectedName = "";
        for (int i = 0; i < btns.length; i++) {
            if (btns[i].isSelected()) {
                selectedName = btns[i].getText().substring(0, 2);
                break;
            }
        }
        return selectedName;
    }

    /**
     * ����ǰ����������ز�����
     */
    public static void neatenSuCaiTypeBtns() {
        JRadioButton radioButton = (JRadioButton) MainUi.map.get("xiaoGuoDengSuCaiTypeButton");
        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
        JLabel alone = (JLabel) MainUi.map.get("alone");//��ǰ�����ز�
        JLabel countLabel = (JLabel) MainUi.map.get("count");//���е����ز�
        JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");//�������б�
        if (radioButton.isSelected()) {//ͨ��
            Map map = (Map) Data.suCaiNameMap.get(suCaiLightType.getSelectedIndex());
            List<String> suCaiNameList = null;
            for (int i = 0; i < btns.length; i++) {
                String name = btns[i].getText().substring(0, 2);
                if (map == null)
                    map = new HashMap();
                suCaiNameList = (List<String>) map.get(name);
                if (suCaiNameList != null) {
                    btns[i].setText(name + "(" + suCaiNameList.size() + ")");
                } else {
                    btns[i].setText(name + "(0)");
                }
            }
        } else {
            List<String> suCaiNameList = null;
            for (int i = 0; i < btns.length; i++) {
                String name = btns[i].getText().substring(0, 2);
                suCaiNameList = (List<String>) Data.SuCaiDongZuoName.get(name);
                if (suCaiNameList != null) {
                    btns[i].setText(name + "(" + suCaiNameList.size() + ")");
                } else {
                    btns[i].setText(name + "(0)");
                }
            }
        }
        alone.setText(getAlone(suCaiLightType.getSelectedIndex()));
        countLabel.setText(getCount());
    }

    /**
     * �ز���������ͬ���޸�Ч���ƽ�����ز�����
     *
     * @param str �ز�����
     * @param num �زı��
     */
    public static void neatenXiaoGuoDeng(String str, int num) {

    }

    /**
     * ���ͨ���ز�����
     */
    public static int getTonDaoSuCaiCount(int DengZuId){
        int count = 0;
        Map map = (Map) Data.suCaiNameMap.get(DengZuId);
        if (map != null)
            for (Object key : map.keySet()) {
                List<String> suCaiNameList = (List<String>) map.get(key);
                if (suCaiNameList != null)
                    count += suCaiNameList.size();
            }
        return count;
    }

    /**
     * ��ö����ز�����
     */
    public static int getDongZuoSuCaiCount(){
        int count = 0;
        for (Object key : Data.SuCaiDongZuoName.keySet()) {
            List<String> suCaiNameList = (List<String>) Data.SuCaiDongZuoName.get(key);
            if (suCaiNameList != null)
                count += suCaiNameList.size();
        }
        return count;
    }

}
