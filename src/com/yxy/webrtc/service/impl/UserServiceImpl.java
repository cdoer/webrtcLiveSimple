package com.yxy.webrtc.service.impl;

import com.yxy.base.exception.BizException;
import com.yxy.base.utils.CommonUtils;
import com.yxy.webrtc.dao.IUserDao;
import com.yxy.webrtc.dao.impl.UserDaoImpl;
import com.yxy.webrtc.model.User;
import com.yxy.webrtc.service.IUserService;

/**
 * Created by yang on 2019-05-01.
 * 1249492252@qq.com
 */
public class UserServiceImpl implements IUserService {
    private IUserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    @Override
    public User login(String username, String pwd) throws Exception {
        if(CommonUtils.isEmpty(username)) throw new BizException("请输入用户名");
        if(CommonUtils.isEmpty(pwd)) throw new BizException("请输入密码");
        User user = userDao.getByName(username);
        if(user==null||!user.getPwd().equals(pwd)){
            throw new BizException("用户名或者密码不正确！");
        }
        return user;
    }

    @Override
    public void register(User user) throws Exception {
        if(user==null) throw new BizException("参数错误");
        CommonUtils.checkEmpty(user.getName(),"昵称");
        CommonUtils.checkEmpty(user.getUserName(),"用户名");
        CommonUtils.checkEmpty(user.getPwd(),"密码");
        if(CommonUtils.isEmpty(user.getId())){
            user.setId(CommonUtils.uuid());
        }
        User byName = userDao.getByName(user.getUserName());
        if(byName!=null) throw new BizException("用户已存在");
        int add = userDao.add(user);
        if(add==0) throw new BizException("注册失败！");
    }
}
