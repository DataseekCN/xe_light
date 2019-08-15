package com.dataseek.xe.dao.impl;

import com.dataseek.xe.dao.IUserDao;
import com.dataseek.xe.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

public class UserDao implements IUserDao {


    @Autowired
    private JdbcTemplate xeJdbcTemplate;

    @Override
    public void insertUser(UserInfo userInfo) {

//        PreparedStatementSetter params = new PreparedStatementSetter();
//        params.

        xeJdbcTemplate.update("", userInfo);



    }
}
