package com.boray.Utils;

import com.boray.Data.Data;
import com.boray.dengKu.UI.NewJTable;
import com.boray.mainUi.MainUi;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SuCaiUtil {

    public static String[] names = {"动感", "慢摇", "抒情", "柔和", "浪漫", "温馨", "炫丽", "梦幻", "其他"};

    /**
     * 计算通道或动作素材数量
     *
     * @return 总数量
     */
    public static String getCount() {
        int count = 0;
        JRadioButton radioButton = (JRadioButton) MainUi.map.get("xiaoGuoDengSuCaiTypeButton");
        if (radioButton.isSelected()) {//通道
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
        } else {//动作
            for (Object key : Data.SuCaiDongZuoName.keySet()) {
                List<String> suCaiNameList = (List<String>) Data.SuCaiDongZuoName.get(key);
                if (suCaiNameList != null)
                    count += suCaiNameList.size();
            }
        }
        return count + "";
    }

    /**
     * 计算当前选中的灯组素材或者动作素材数量
     *
     * @param selectIndex 通道素材需要，动作素材随意
     * @return 总数量
     */
    public static String getAlone(int selectIndex) {
        int count = 0;
        JRadioButton radioButton = (JRadioButton) MainUi.map.get("xiaoGuoDengSuCaiTypeButton");
        if (radioButton.isSelected()) {//通道
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
     * 获得当前选中的素材类型
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
     * 整理当前灯组的所有素材数量
     */
    public static void neatenSuCaiTypeBtns() {
        JRadioButton radioButton = (JRadioButton) MainUi.map.get("xiaoGuoDengSuCaiTypeButton");
        JToggleButton[] btns = (JToggleButton[]) MainUi.map.get("suCaiTypeBtns");
        JLabel alone = (JLabel) MainUi.map.get("alone");//当前灯组素材
        JLabel countLabel = (JLabel) MainUi.map.get("count");//所有灯组素材
        JList suCaiLightType = (JList) MainUi.map.get("suCaiLightType");//左侧灯组列表
        if (radioButton.isSelected()) {//通道
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
     * 素材重命名后，同步修改效果灯界面的素材名称
     *
     * @param str 素材名称
     * @param num 素材编号
     */
    public static void neatenXiaoGuoDeng(String str, int num) {

    }

    /**
     * 获得通道素材数量
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
     * 获得动作素材数量
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
