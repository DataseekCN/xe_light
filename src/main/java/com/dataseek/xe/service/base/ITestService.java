package com.dataseek.xe.service.base;

import com.dataseek.xe.entity.TestUser;

public interface ITestService {
    //取得已存在用户信息
    public TestUser queryExistTestUser(String userName);
}
