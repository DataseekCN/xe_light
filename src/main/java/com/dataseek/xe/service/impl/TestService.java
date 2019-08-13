package com.dataseek.xe.service.impl;

import com.dataseek.xe.dao.ITestDao;
import com.dataseek.xe.entity.TestUser;
import com.dataseek.xe.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService implements ITestService {
    @Autowired
    private ITestDao testDao;

    public TestUser queryExistTestUser(String userName) {
        TestUser testUser = testDao.queryTestUserInfoByName(userName);
        return testUser;
    }
}
