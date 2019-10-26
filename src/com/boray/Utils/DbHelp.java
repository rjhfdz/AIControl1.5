package com.boray.Utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbHelp {
	

	
	 public  Connection getConnection2() throws Exception{
	        //1.创建数据库的4个字符串
	        
	        //2.创建Properties对象
	    
	        //3.获取jdbc.pro对应的输入流
	        InputStream in=
	                this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
	        //4.加载输入流
	       
	        //5.具体决定4个字符串的值
	        String driver="com.mysql.jdbc.Driver";
	        String jdbcUrl="jdbc:mysql://localhost:3306/aicontrol";
	        String user="root";
	        String password="root";
	        //6.加载数据库驱动程序
	        Class.forName(driver);
	        //7.通过DriverManager的getConnection()方法获取数据库连接
	        Connection conn=DriverManager.getConnection(jdbcUrl, user, password);
	        return conn;
	    }
	 public int insert(String sql) throws Exception{
	        //1.调用getConnection2数据库连接方法
	        Connection conn=null;
	        Statement statement=null;
	        int i =0;
	        try {
	            conn=getConnection2();
	            //3.要执行的字符串
	          
	            //4.执行sql
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
	                //2.关闭数据库
	                if(conn!=null)
	                    conn.close();
	            }
	           
	        }
	        
	        return i;
	    }
}
