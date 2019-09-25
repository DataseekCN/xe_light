package com.dataseek.xe.service.base.impl;

import com.dataseek.xe.dao.IUserDao;
import com.dataseek.xe.entity.UserInfo;
import com.dataseek.xe.service.base.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserDao userDao;

    public void insertUser(UserInfo userInfo) {
        userDao.insertUser(userInfo);
    }

    public List<UserInfo> qryUser(UserInfo userInfo) {
        return userDao.qryUser(userInfo);
    }

    public void updUser(UserInfo userInfo) {
        userDao.updUser(userInfo);
    }
}
