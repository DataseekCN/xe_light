package com.dataseek.xe.dao;

import com.dataseek.xe.entity.UserInfo;

import java.util.List;

public interface IUserDao {

    public void insertUser(UserInfo userInfo);

    public List<String> qryUser(String email, String psw);

}
