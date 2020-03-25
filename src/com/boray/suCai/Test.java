package com.boray.suCai;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boray.Utils.HttpClientUtil;

public abstract class Test {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
		/*Map<String, Object> map =new HashMap<String, Object>();
		List listchan = new ArrayList<>();
		List listtopDengJuNumber = new ArrayList<>();
    String str = "{\"channelData\":[[[\"1\",\"0\",\"0\",\"123\",\"0\",\"148\",\"132\",\"0\",\"0\",\"0\",\"0\",\"0\"],[\"2\",\"0\",\"0\",\"0\",\"255\",\"115\",\"0\",\"0\",\"0\",\"0\",\"0\",\"0\"]],[false,false,false,false,false,false,false,false,false,false],[\"0\",\"0\",\"0\"]],\"TopDengJuNumber\":[\"0\",\"0\"],\"actionXiaoGuoData\":{\"0\":\"false\",\"2\":\"0\",\"3\":\"0\",\"4\":[\"0\",\"false\",\"false\",\"false\",\"false\",\"0\"]}}";
	
    JSONObject j =  JSON.parseObject(str);
    
    JSONArray chanlist = JSONArray.parseArray(j.getString("channelData"));
  
    
    for (int i = 0; i < chanlist.size(); i++) {
    	 
   
    if(i==0) {
    	JSONArray chanlist1 = (JSONArray) chanlist.get(i);
    	List list = new ArrayList<>(); 
    	for (int k = 0; k < chanlist1.size(); k++) {
			JSONArray chanlist111 = (JSONArray) chanlist1.get(k);
			Object [] num = new Object[chanlist111.size()];
		for (int l = 0; l < chanlist111.size(); l++) {
			
			num[l]= chanlist111.get(l);
			if(l==chanlist1.size()-1) {
			
				list.add(num);
			}
			
		}
		if(k==chanlist1.size()-1) {
			listchan.add(list);
		}
		
    	}
    }else {
    	JSONArray chanlist11 = (JSONArray) chanlist.get(i);
    	Object [] num = new Object[chanlist11.size()];
	for (int l = 0; l < chanlist11.size(); l++) {
			
			num[l]= chanlist11.get(l);
			if(l==chanlist11.size()-1) {
			
				listchan.add(num);
			}
			
		}
    	
    }
    	
    	
	}
    JSONArray topDengJuNumber = JSONArray.parseArray(j.getString("TopDengJuNumber"));
    
    for (int i = 0; i < topDengJuNumber.size(); i++) {
		listtopDengJuNumber.add(topDengJuNumber.get(i));
	}

    JSONObject actionXiaoGuoData = j.getJSONObject("actionXiaoGuoData");
    Map actionXiaoGuoDatamap = new HashMap<>();
    actionXiaoGuoDatamap.put("0", actionXiaoGuoData.get("0"));
    actionXiaoGuoDatamap.put("2", actionXiaoGuoData.get("2"));
    actionXiaoGuoDatamap.put("3", actionXiaoGuoData.get("3"));
    JSONArray a4 = JSONArray.parseArray(actionXiaoGuoData.getString("4"));
   List a4list = new ArrayList<>();
   for (int i = 0; i < a4.size(); i++) {
	a4list.add(a4.get(i));
}
   actionXiaoGuoDatamap.put("4", a4list);
    
    map.put("TopDengJuNumber", listtopDengJuNumber);
    map.put("channelData", listchan);
    map.put("actionXiaoGuoData", actionXiaoGuoDatamap);
    
    List listf = (List) map.get("channelData");
    List v1= (List)listf.get(0);
    
    for (int i = 0; i < v1.size(); i++) {
    	System.out.println(v1.get(i));
	}
    
    System.out.println(JSON.toJSON(map));*/
        test();
    }

//	 public static final String HTTp_URL="https://yqfile.alicdn.com/541d2074f3ee5857196427f0663df967aadf823a.png";
//     public static void main(String[] args) {
//             Dol();
//     }
//         public static void Dol(){
//             BufferedInputStream bis=null;
//             BufferedOutputStream bos=null;
//     try {
//         URL url = new URL(HTTp_URL);
//         HttpURLConnection connection =  (HttpURLConnection) url.openConnection();
//         connection.setRequestMethod("GET");
//         connection.connect();
//         InputStream is = connection.getInputStream();
//         
//         bis = new BufferedInputStream(is);
//         
//         File file = new File("D:/"+HTTp_URL.substring((HTTp_URL.lastIndexOf("/"))));//名字截取 可以省略
//         FileOutputStream fos = new FileOutputStream(file);
//         bos = new BufferedOutputStream(fos);
//         int b = 0;
//         byte[] byArr = new byte[1024*4];
//         while((b=bis.read(byArr))!=-1){
//             bos.write(byArr, 0, b);
//         }
//     } catch (Exception e) {
//         e.printStackTrace();
//     }finally{
//         try {
//             if(bis!=null){
//                 bis.close();
//             }
//             if(bos!=null){
//                 bos.close();
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
// }


    public static void test() {
//        HttpClientUtil httpsUtils = new HttpClientUtil();
//        //要上传的文件的路径
//        String filePath = "E:\\statr.txt";
//        String postUrl = "http://localhost:8778/FileUploadServletgc";
//        Map<String, String> postParam = new HashMap<String, String>();
//        postParam.put("xmid", "11122");
//        postParam.put("gcname", "aa.txt");
//        postParam.put("username", "system");
//        postParam.put("i", "0");
//        File postFile = new File(filePath);
//        Map<String, Object> resultMap = httpsUtils.uploadFileByHTTP(postFile, postUrl, postParam);
//        System.out.println(resultMap);
//        String folder=System.getProperty("java.io.tmpdir");
//        System.out.println(folder);
//        System.out.println(System.getProperty("user.dir"));
//        System.out.println((10-15)/2);
    }

}
