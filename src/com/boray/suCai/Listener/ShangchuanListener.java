package com.boray.suCai.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;

import com.alibaba.fastjson.JSON;
import com.boray.Data.Data;
import com.boray.Utils.DbHelp;
import com.boray.Utils.HttpClientUtil;
import com.boray.mainUi.MainUi;

public class ShangchuanListener implements ActionListener {
	
	@Override
	public void actionPerformed(ActionEvent e) {
	
		String filename="";
		System.out.println(filename);
		JList suCai_list = (JList)MainUi.map.get("suCai_list");
		JList suCaiLightType = (JList)MainUi.map.get("suCaiLightType");
		String suCaiName = suCai_list.getSelectedValue().toString();
		String[] array1=suCaiName.split("--->");

		JToggleButton [] btn = (JToggleButton [])MainUi.map.get("suCaiTypeBtns");
		int j = 0;
		for (int i = 0; i < btn.length; i++) {
			if(btn[i].getSelectedObjects()!=null) {
				j=i;
				break;
			}
		}
		
		int denKuNum = suCaiLightType.getSelectedIndex();
		int suCaiNum = Integer.valueOf(suCaiName.split("--->")[1]).intValue();
		Map hashMap = (HashMap)Data.SuCaiObjects[denKuNum][suCaiNum];
		
		XMLEncoder xml;
		try {
			xml = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("D:\\Test.xml")));
			xml.writeObject(hashMap);
			xml.close();
			
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		Map<String, String> param = new HashMap<>();
		param.put("filejson", readToString("D:\\Test.xml"));
		param.put("username", "1");
		param.put("filename", array1[0]);
		DbHelp db = new DbHelp();
		String i="";
		String sql = "insert shucai (filejson,filename,username,kuname,sctype) values('"+readToString("D:\\Test.xml")+"','"+array1[0]+"','1','"+suCaiLightType.getSelectedValue().toString()+"','"+j+"')";
		try {
			i = db.insert(sql)+"";
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		if(i.equals("1")) {
			JOptionPane.showMessageDialog(null, "上传成功", "标题",JOptionPane.WARNING_MESSAGE);  
			
		}else {
			JOptionPane.showMessageDialog(null, "上传失败", "标题",JOptionPane.WARNING_MESSAGE);  
			
		}
		
		
	}
	
	
	public String readToString(String fileName) {  
        String encoding = "UTF-8";  
        File file = new File(fileName);  
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            return new String(filecontent, encoding);  
        } catch (UnsupportedEncodingException e) {  
            System.err.println("The OS does not support " + encoding);  
            e.printStackTrace();  
            return null;  
        }  
    }

	/*// TODO Auto-generated method stub
	String field="123";
	if (!"".equals(field)) {
		JList list = (JList)MainUi.map.get("suCai_list");
		DefaultListModel  model = (DefaultListModel)list.getModel();
		
		//////////////////获取素材数量
		JList suCaiLightType = (JList)MainUi.map.get("suCaiLightType");
		Map map2 = (Map)Data.suCaiMap.get(suCaiLightType.getSelectedValue().toString());
		JToggleButton[] btns = (JToggleButton[])MainUi.map.get("suCaiTypeBtns");
		String[] name = {"动感","慢摇","抒情","柔和","浪漫","温馨","炫丽","梦幻","其他"};
		int cnt = 0;
		if (map2!=null) {
			for (int i = 0; i < btns.length; i++) {
				List abc = (List)map2.get(""+i);
				if (abc != null) {
					cnt = cnt + abc.size();
				}
			}
		}
		String suCaiNameAndNumber = field+"--->"+(cnt+1);
		//////////////////
		
		if (model == null) {
			model = new DefaultListModel();
			model.addElement(suCaiNameAndNumber);
			list.setModel(model);
		} else {
			model.addElement(suCaiNameAndNumber);
		}
		list.setSelectedIndex(model.getSize()-1);
		//JList suCaiLightType = (JList)MainUi.map.get("suCaiLightType");
		Map map = (Map)Data.suCaiMap.get(suCaiLightType.getSelectedValue().toString());
		Map nameMap = (Map)Data.suCaiNameMap.get(suCaiLightType.getSelectedValue().toString());
		if (map==null) {
			map = new HashMap<>();
			Data.suCaiMap.put(suCaiLightType.getSelectedValue().toString(), map);
		}
		if (nameMap==null) {
			nameMap = new HashMap<>();
			Data.suCaiNameMap.put(suCaiLightType.getSelectedValue().toString(), nameMap);
		}
		//JToggleButton[] btns = (JToggleButton[])MainUi.map.get("suCaiTypeBtns");
		//String[] name = {"默认","动感","抒情","柔和","浪漫"};
		for (int i = 0; i < btns.length; i++) {
			if (btns[i].isSelected()) {
				List tmp = (List)map.get(""+i);
				List nameList = (List)nameMap.get(""+i);
				if (nameList!=null) {
					nameList.add(suCaiNameAndNumber);
				} else { 
					nameList = new ArrayList<>();
					nameList.add(suCaiNameAndNumber);
					nameMap.put(""+i,nameList);
				}
				if (tmp==null) {
					tmp = new ArrayList<>();
				}
				tmp.add(new HashMap<>());
				btns[i].setText(name[i]+"("+tmp.size()+")");
				map.put(""+i, tmp);
			}
		}
	}*/
	 
}
