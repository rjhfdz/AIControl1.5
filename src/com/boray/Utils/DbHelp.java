package com.boray.Utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbHelp {
	

	
	 public  Connection getConnection2() throws Exception{
	        //1.�������ݿ��4���ַ���
	        
	        //2.����Properties����
	    
	        //3.��ȡjdbc.pro��Ӧ��������
	        InputStream in=
	                this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
	        //4.����������
	       
	        //5.�������4���ַ�����ֵ
	        String driver="com.mysql.jdbc.Driver";
	        String jdbcUrl="jdbc:mysql://localhost:3306/aicontrol";
	        String user="root";
	        String password="root";
	        //6.�������ݿ���������
	        Class.forName(driver);
	        //7.ͨ��DriverManager��getConnection()������ȡ���ݿ�����
	        Connection conn=DriverManager.getConnection(jdbcUrl, user, password);
	        return conn;
	    }
	 public int insert(String sql) throws Exception{
	        //1.����getConnection2���ݿ����ӷ���
	        Connection conn=null;
	        Statement statement=null;
	        int i =0;
	        try {
	            conn=getConnection2();
	            //3.Ҫִ�е��ַ���
	          
	            //4.ִ��sql
	            statement=conn.createStatement();
	           i= statement.executeUpdate(sql);
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }finally{
	            try {
	                if(statement!=null)
	                    statement.close();
	            } catch (Exception e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }finally{
	                //2.�ر����ݿ�
	                if(conn!=null)
	                    conn.close();
	            }
	           
	        }
	        
	        return i;
	    }
}
