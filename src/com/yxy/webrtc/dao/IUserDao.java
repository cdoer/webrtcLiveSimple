package com.yxy.webrtc.dao;

import com.yxy.webrtc.model.User;

/**
 * Created by yang on 2019-05-01.
 * 1249492252@qq.com
 */
public interface IUserDao  {
    User getByName(String username);
    User getById(String id);
    int add(User user);
}
