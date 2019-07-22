package com.yxy.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Created by yang on 2018-05-05.
 * 1249492252@qq.com
 */
public class DBUtils {
    private static String CONNECT_URL = null;
    private static String USERNAME=null;
    private static String PWD=null;
    private static final String MYSQL="mysql";
    private static final String MYSQL_DRIVER="com.mysql.jdbc.Driver";
    private static final String SQLSERVER="sqlserver";
    private static final String SQLSERVER_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String DB_TYPE=MYSQL;
    private static boolean FLAG=false;

    static {
        Properties properties = new Properties();
        InputStream is = null;
        try {
            is =DBUtils.class.getResourceAsStream("db.properties");
            properties.load(is);
            if(properties.getProperty("db_type")!=null) {
                DB_TYPE = properties.getProperty("db_type");
            }
            if(MYSQL.equals(DB_TYPE)) {
                CONNECT_URL = "jdbc:mysql://" + properties.getProperty("ip") + ":"
                        + properties.getProperty("port") + "/" + properties.getProperty("dbname") + "?" +
                        "useUnicode=true&characterEncoding=UTF8";
            }else if(SQLSERVER.equals(DB_TYPE)){
                CONNECT_URL = "jdbc:sqlserver://" + properties.getProperty("ip") + ":"
                        + properties.getProperty("port") + ";DatabaseName=" + properties.getProperty("dbname");
            }else{
                throw new IllegalArgumentException("数据库配置不正确!");
            }
            USERNAME = properties.getProperty("username");
            PWD = properties.getProperty("pwd");
            FLAG = true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is!=null) try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection open(){
        Connection conn=null;
        try {
            if(FLAG) {
                Class.forName(MYSQL.equals(DB_TYPE)?MYSQL_DRIVER:SQLSERVER_DRIVER);
                conn = DriverManager.getConnection(CONNECT_URL, USERNAME, PWD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void close(AutoCloseable closeable){
        try {
             if(closeable!=null){
                 closeable.close();
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
