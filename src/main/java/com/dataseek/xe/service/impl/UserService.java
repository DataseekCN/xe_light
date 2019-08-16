package com.dataseek.xe.service.impl;

import com.dataseek.xe.dao.IUserDao;
import com.dataseek.xe.entity.UserInfo;
import com.dataseek.xe.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserDao userDao;

    public void insertUser(UserInfo userInfo) {
        userDao.insertUser(userInfo);
    }
}
