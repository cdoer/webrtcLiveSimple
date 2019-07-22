package com.yxy.webrtc.dao.impl;

import com.yxy.base.dao.BaseDao;
import com.yxy.base.utils.CollectionUtils;
import com.yxy.webrtc.dao.IUserDao;
import com.yxy.webrtc.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yang on 2019-05-01.
 * 1249492252@qq.com
 * 功能比较简单 不依赖过多的东西 用原生sql查询
 */
public class UserDaoImpl implements IUserDao{

    @Override
    public User getByName(String username) {
        String sql = "select *from T_USER where user_name=?";
        return getSingle(sql,username);
    }

    @Override
    public User getById(String id) {
        String sql = "select *from T_USER where id=?";
        return getSingle(sql,id);
    }

    @Override
    public int add(User user) {
        String sql = "insert T_USER (id,name,user_name,pwd) VALUES(?,?,?,?)";
        return BaseDao.execute(sql,user.getId(),user.getName(),user.getUserName(),user.getPwd());
    }

    private User getSingle(String sql,Object... objs){
        List<Map> query = BaseDao.query(sql,objs);
        List<User> convert = convert(query);
        return CollectionUtils.isEmpty(convert)?null:convert.get(0);
    }

    private List<User> convert( List<Map> query){
        List<User> userList = new ArrayList<User>();
        if(!CollectionUtils.isEmpty(query)){
            for (Map map : query) {
                User user = new User();
                user.setId((String) map.get("id"));
                user.setName((String) map.get("name"));
                user.setUserName((String) map.get("user_name"));
                user.setPwd((String) map.get("pwd"));
                userList.add(user);
            }
        }
        return userList;
    }
}
