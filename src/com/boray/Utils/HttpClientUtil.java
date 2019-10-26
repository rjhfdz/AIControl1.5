package com.boray.Utils;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {

	public static String doGet(String url, Map<String, String> param) {

		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();

		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();

			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);

			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}

	public static String doGet(String url) {
		return doGet(url, null);
	}

	public static String doPost(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			
			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					System.out.println(param.get(key));
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
				
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}

	public static String doPost(String url) {
		return doPost(url, null);
	}

		// 创建Httpclient对象
	public static String doPostJson(String url, String json) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			if (json != null) {
				StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}
	
	public Map<String,Object> uploadFileByHTTP(File postFile,String postUrl,Map<String,String> postParam){  
        Map<String,Object> resultMap = new HashMap<String,Object>();  
        CloseableHttpClient httpClient = HttpClients.createDefault();    
        try{    
            //把一个普通参数和文件上传给下面这个地址    是一个servlet    
            HttpPost httpPost = new HttpPost(postUrl);    
            //把文件转换成流对象FileBody  
            FileBody fundFileBin = new FileBody(postFile);    
            //设置传输参数  
            MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();    
            multipartEntity.addPart(postFile.getName(), fundFileBin);//相当于<input type="file" name="media"/>    
            //设计文件以外的参数  
            Set<String> keySet = postParam.keySet();  
            for (String key : keySet) {  
               //相当于<input type="text" name="name" value=name>    
               multipartEntity.addPart(key, new StringBody(postParam.get(key), ContentType.create("text/plain", Consts.UTF_8)));    
            }  
              
            HttpEntity reqEntity =  multipartEntity.build();   
            httpPost.setEntity(reqEntity);    
                
        
            //发起请求   并返回请求的响应    
            CloseableHttpResponse response = httpClient.execute(httpPost);    
            try {    
             
                resultMap.put("statusCode", response.getStatusLine().getStatusCode());  
                //获取响应对象    
                HttpEntity resEntity = response.getEntity();    
                if (resEntity != null) {    
                    //打印响应长度    
                 
                    //打印响应内容    
                    resultMap.put("data", EntityUtils.toString(resEntity,Charset.forName("UTF-8")));  
                }    
                //销毁    
                EntityUtils.consume(resEntity);    
            } catch (Exception e) {  
                e.printStackTrace();  
            } finally {    
                response.close();    
            }    
        } catch (ClientProtocolException e1) {  
           e1.printStackTrace();  
        } catch (IOException e1) {  
           e1.printStackTrace();  
        } finally{    
            try {  
               httpClient.close();  
            } catch (IOException e) {  
               e.printStackTrace();  
            }    
        }  
     
       return resultMap;    
    }  
}
