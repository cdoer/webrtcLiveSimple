package com.yxy.base.dao;


import com.yxy.base.utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yang on 2018-05-05.
 * 1249492252@qq.com
 */
public class BaseDao {
    public static List<Map> query(String sql, Object... obj){
        List<Map> dataList = new ArrayList<Map>();
        Connection conn=null;
        Map item = null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet = null;
        try {
            conn = DBUtils.open();
            preparedStatement = conn.prepareStatement(sql);
            if(obj!=null){
                for (int i = 0; i < obj.length; i++) {
                    Object o = obj[i];
                    preparedStatement.setObject(i+1,obj[i]);
                }
            }
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData rsmd=resultSet.getMetaData();
            int count=rsmd.getColumnCount();
            List<String> cloumnNames = new ArrayList<String>();
            for (int i = 1; i <= count; i++) {
                cloumnNames.add(rsmd.getColumnName(i));
            }
            while(resultSet.next()){
                item = new HashMap();
                for (String cloumnName : cloumnNames) {
                    item.put(cloumnName,resultSet.getObject(cloumnName));
                }
                dataList.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(conn);
            DBUtils.close(preparedStatement);
            DBUtils.close(resultSet);
        }
        return dataList;
    }

    public static int execute(String sql, Object... obj){
        int count = 0;
        Connection conn=null;
        PreparedStatement preparedStatement=null;
        try {
            conn = DBUtils.open();
            preparedStatement = conn.prepareStatement(sql);
            if(obj!=null){
                for (int i = 0; i < obj.length; i++) {
                    Object o = obj[i];
                    preparedStatement.setObject(i+1,obj[i]);
                }
            }
            count = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DBUtils.close(conn);
            DBUtils.close(preparedStatement);
        }
        return count;
    }
}
