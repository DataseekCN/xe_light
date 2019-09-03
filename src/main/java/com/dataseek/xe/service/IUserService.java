package com.dataseek.xe.service;

import com.dataseek.xe.entity.UserInfo;

import java.util.List;

public interface IUserService {

    public void insertUser(UserInfo userInfo);

    public List<String> qryUser(String email, String psw);

}
