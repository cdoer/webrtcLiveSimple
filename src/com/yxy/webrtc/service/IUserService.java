package com.yxy.webrtc.service;

import com.yxy.webrtc.model.User;

/**
 * Created by yang on 2019-05-01.
 * 1249492252@qq.com
 */
public interface IUserService {
    User login(String username ,String pwd) throws Exception;
    void register(User user) throws Exception;
}
