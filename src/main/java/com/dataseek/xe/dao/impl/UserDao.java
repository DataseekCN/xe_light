package com.dataseek.xe.dao.impl;

import com.dataseek.xe.config.JdbcSupport;
import com.dataseek.xe.dao.IUserDao;
import com.dataseek.xe.entity.UserInfo;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class UserDao implements IUserDao {

    @Resource(name="jdbcSupportBase")
    JdbcSupport jdbcSupport;

    public void insertUser(UserInfo userInfo) {
        StringBuilder sqlBd = new StringBuilder();
        sqlBd.append("insert into xe_user(first_name,last_name,email,password,active,user_id,create_date) ");
        sqlBd.append("values(:first_name,:last_name,:email,:password,:active,:user_id,sysdate())");

        MapSqlParameterSource mapParam = new MapSqlParameterSource();
        mapParam.addValue("first_name", userInfo.getFirstName());
        mapParam.addValue("last_name", userInfo.getLastName());
        mapParam.addValue("email", userInfo.getEmail());
        mapParam.addValue("password", userInfo.getPassword());
        mapParam.addValue("active", userInfo.getActive());
        mapParam.addValue("user_id", userInfo.getUserId());
        jdbcSupport.update(sqlBd.toString(), mapParam);
    }
}
