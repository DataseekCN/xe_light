package com.dataseek.xe.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dataseek.xe.dao.ITestDao;
import com.dataseek.xe.entity.TestUser;
import com.dataseek.xe.mapper.JSONObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TestDao implements ITestDao {
    @Autowired
    private JdbcTemplate xeJdbcTemplate;

    public TestUser queryTestUserInfoByName(String userName) {
        String query_sql = "select id,name from xero.test_table where name=?";
        Object[] params = new Object[]{userName};
        JSONObject testUserJson = xeJdbcTemplate.queryForObject(query_sql,params,new JSONObjectMapper());
        TestUser testUser = JSON.parseObject(testUserJson.toJSONString(),TestUser.class);
        return testUser;
    }
}
