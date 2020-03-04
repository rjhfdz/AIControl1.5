package com.boray.main.Util;

import com.boray.dengKu.UI.NewJTable;
import com.boray.entity.ProjectFileInfo;

import javax.swing.table.DefaultTableModel;
import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MainUtil {

    private Map map;
    private List list;
    private Vector vector;

    public List<ProjectFileInfo> tt(File file, int type) {
        List<ProjectFileInfo> infos = new ArrayList<>();
        try {
            InputStream is = new FileInputStream(file);
            XMLDecoder xmlDecoder = new XMLDecoder(is);
            //场景配置区
            if (type == 2) {
                xmlDecoder.readObject();
            } else {
                map = (Map) xmlDecoder.readObject();
            }
            //中控学习区
            //串口墙板
            map = (Map) xmlDecoder.readObject();

            //串口上行
            map = (Map) xmlDecoder.readObject();

            //串口下行
            map = (Map) xmlDecoder.readObject();

            //红外码输出
            map = (Map) xmlDecoder.readObject();

            //倒喝彩
            map = (Map) xmlDecoder.readObject();
            /////////////////////////全局设置数据区/////////////////////
            list = (ArrayList) xmlDecoder.readObject();
            ////////////////////////////////中控离散数据区/////////
            list = (ArrayList) xmlDecoder.readObject();
            if (type != 2) {
                list = (ArrayList) xmlDecoder.readObject();
                /////////////////灯库///////////////////
                list = (List) xmlDecoder.readObject();
                list = (List) xmlDecoder.readObject();
                list = (List) xmlDecoder.readObject();
                list = (List) xmlDecoder.readObject();
                vector = (Vector) xmlDecoder.readObject();

                vector = (Vector) xmlDecoder.readObject();
                Object[][] data = {};
                String[] title = {"启用", "ID", "灯具名称", "型号", "库版本", "DMX起始地址", "占用通道数"};
                DefaultTableModel model = new DefaultTableModel(data, title);
                NewJTable table = new NewJTable(model, 9);
                if (vector != null) {
                    Vector tp = null;
                    int a = 0;
                    for (int i = 0; i < vector.size(); i++) {
                        tp = (Vector) vector.get(i);
                        model.addRow(tp);
                    }
                    if (table.getRowCount() > 0) {
                        table.setRowSelectionInterval(0, 0);
                    }
                }
                for (int i = 0;i<table.getRowCount();i++){
                    ProjectFileInfo info = new ProjectFileInfo();
                    info.setDjname(table.getValueAt(i,2).toString());
                    info.setDjtype(table.getValueAt(i,3).toString());
                    info.setDmxstr(table.getValueAt(i,5).toString());
                    info.setZytd(table.getValueAt(i,6).toString());
                    infos.add(info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return infos;
    }
}
