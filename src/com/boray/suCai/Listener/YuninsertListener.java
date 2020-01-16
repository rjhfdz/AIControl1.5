package com.boray.suCai.Listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JToggleButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.entity.Users;
import com.boray.mainUi.MainUi;

public class YuninsertListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
//		
//		try {
//			show();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		try {
			show();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	public  void show() throws FileNotFoundException {
		
		JToggleButton [] btn = (JToggleButton [])MainUi.map.get("yunsuCaiTypeBtns");
		int j = 0;
		for (int i = 0; i < btn.length; i++) {
			if(btn[i].getSelectedObjects()!=null) {
				j=i;
				break;
			}
		}
		JList yunsc = (JList) MainUi.map.get("yunsc");

		JList suCaiLightType = (JList)MainUi.map.get("yunsuCaiLightType");
		
		String field=yunsc.getSelectedValue().toString();
		  Users users = (Users) MainUi.map.get("Users");
		String jsonstr =HttpClientUtil.doGet(Data.ipPort+"/getscxx?username="+users.getUsername()+"&kuname="+suCaiLightType.getSelectedValue().toString()+"&sctype="+j+"&filename="+field+"");
		
		
	
		try {
			if(jsonstr!=null&& !jsonstr.equals("")) {
				JSONObject json =JSON.parseObject(jsonstr);
				
				StringBufferDemo(json.getString("filejson"));
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		XMLDecoder d=new XMLDecoder(new BufferedInputStream(new FileInputStream("D:\\a.xml")));
	
		if (!"".equals(field)) {
			JList list = (JList)MainUi.map.get("suCai_list");
			DefaultListModel  model = (DefaultListModel)list.getModel();
			
			//////////////////获取素材数量
		
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
			Data.SuCaiObjects[suCaiLightType.getSelectedIndex()][cnt]=(Map)d.readObject();
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
		}
	}
	
	public void StringBufferDemo(String str) throws IOException {
        File file=new File("D:\\a.xml");
        if(file.exists())
           file.delete();
        	file.createNewFile();
        FileOutputStream out=new FileOutputStream(file,true);        
      
            StringBuffer sb=new StringBuffer();
            sb.append(str);
            out.write(sb.toString().getBytes("utf-8"));
                
        out.close();
}

}
