package com.dataseek.xe.service;

import com.dataseek.xe.entity.UserInfo;

import java.util.List;

public interface IUserService {

    public void insertUser(UserInfo userInfo);

    public List<UserInfo> qryUser(UserInfo userInfo);

    public void updUser(UserInfo userInfo);

}
