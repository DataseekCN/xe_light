package com.dataseek.xe.dao;

import com.dataseek.xe.entity.TestUser;

public interface ITestDao {
    //通过用户名查询用户信息
    public TestUser queryTestUserInfoByName(String userName);
}
