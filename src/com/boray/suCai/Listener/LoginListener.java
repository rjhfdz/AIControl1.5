package com.boray.suCai.Listener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boray.Data.Data;
import com.boray.Utils.HttpClientUtil;
import com.boray.mainUi.MainUi;

public class LoginListener implements ActionListener {
	
	private JPasswordField pass;
	private JTextField username;
	private JPanel pane1;
	
	public LoginListener(JPasswordField p,JTextField u,JPanel pane) {
		pass = p;
		username=u;
		pane1 =pane; 
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
	
		
		if(username.getText()!=null&&pass.getText()!=null&&!username.getText().equals("")&&!pass.getText().equals("")) {
			
			String str =HttpClientUtil.doGet("http://localhost:8778/login?username="+username.getText()+"&userpassword="+pass.getText()+"");
			
			JSONObject json =JSON.parseObject(str);
		
			if(json.get("login").equals("0")) {
				JOptionPane.showMessageDialog(null, "账号或密码错误", "标题",JOptionPane.WARNING_MESSAGE);
				
//			}else {
//				JOptionPane.showMessageDialog(null, "登录成功", "标题",JOptionPane.WARNING_MESSAGE);  
//				pane1.removeAll();
//				pane1.repaint();
//				JScrollPane scrollPane = new JScrollPane();
//				scrollPane.setPreferredSize(new Dimension(270,520));
//				String strjson =HttpClientUtil.doGet("http://localhost:8778/getshucai");
//				Map map = new HashMap<>();
//				
//				JSONArray j = JSONArray.parseArray(strjson);
//				
//				for (int i = 0; i < j.size(); i++) {
//					JSONObject jo = j.getJSONObject(i);
//					Data.yunSuCaiMap.put(jo.get("filename"), jo.get("filejson"));
//				}
//				
//				
//				
//				
//				//素材列表
//				final JList list = new JList();
//				
//				MainUi.map.put("yunsc", list);
//				list.setFixedCellHeight(32);
//				DefaultListCellRenderer renderer = new DefaultListCellRenderer();
//				renderer.setHorizontalAlignment(SwingConstants.CENTER);
//				list.setCellRenderer(renderer);
//				list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//				//String[] s = {};
//				//list.setListData(s);
//				DefaultListModel  model = new DefaultListModel();
//				Map yunsc = Data.yunSuCaiMap;
//				for(Object key : yunsc.keySet()){   //只能遍历key  
//				    model.addElement(key);
//				}
//				
//				
//				
//				list.setModel(model);
//				list.setSelectionBackground(new Color(85,160,255));
//				list.setSelectionForeground(Color.WHITE);
//				//list.setOpaque(false);
//				scrollPane.setViewportView(list);
//				list.setSelectedIndex(model.getSize()-1);
//				pane1.add(scrollPane);
//				Dimension dimension = new Dimension(100,34);
//				JButton delBtn = new JButton("删除");
//				JButton editBtn = new JButton("下载");
//				delBtn.setPreferredSize(dimension);
//				editBtn.setPreferredSize(dimension);
//				editBtn.addActionListener(new YuninsertListener());
//				delBtn.addActionListener(new YundelListener());
//				pane1.add(editBtn);
//				pane1.add(delBtn);
//			
				
			}else {
				JOptionPane.showMessageDialog(null, "登录成功", "标题",JOptionPane.WARNING_MESSAGE); 
				pane1.removeAll();
				pane1.repaint();
				FlowLayout flowLayout2 = new FlowLayout(FlowLayout.CENTER);
				
				pane1.setLayout(flowLayout2);
				Dimension dimension = new Dimension(150,34);
				JButton delBtn = new JButton("云端素材");
				delBtn.addActionListener(new YundelListener());
			pane1.add(delBtn);	
			
			}
			
		}else {
			JOptionPane.showMessageDialog(null, "账号密码不能为空", "标题",JOptionPane.WARNING_MESSAGE);  
		}
	}
	
	public  void show() throws FileNotFoundException {
		String field="123";
		String str =HttpClientUtil.doGet("http://localhost:8778/getshucai");
		JSONObject json =JSON.parseObject(str);
		try {
			StringBufferDemo(json.getString("filejson"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		XMLDecoder d=new XMLDecoder(new BufferedInputStream(new FileInputStream("D:\\a.xml")));
		Data.SuCaiObjects[0][1]=(Map)d.readObject();
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
		}
	}
	
	public void StringBufferDemo(String str) throws IOException {
        File file=new File("D:\\a.xml");
        if(!file.exists())
            file.createNewFile();
        FileOutputStream out=new FileOutputStream(file,true);        
      
            StringBuffer sb=new StringBuffer();
            sb.append(str);
            out.write(sb.toString().getBytes("utf-8"));
                
        out.close();
}

}
