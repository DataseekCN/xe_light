package com.dataseek.xe.dao.impl;

import com.dataseek.xe.config.JdbcSupport;
import com.dataseek.xe.dao.IUserDao;
import com.dataseek.xe.entity.UserInfo;
import com.dataseek.xe.util.SpringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao implements IUserDao {

    private JdbcSupport jdbcSupport = null;

    public UserDao() {
        this.jdbcSupport = (JdbcSupport) SpringUtils.getBean("jdbcSupport1");
    }

    public void insertUser(UserInfo userInfo) {
        StringBuilder sqlBd = new StringBuilder();
        sqlBd.append("insert into xe_user(first_name,last_name,email,password,active) ");
        sqlBd.append("values(:first_name,:last_name,:email,:password,:active)");

        MapSqlParameterSource mapParam = new MapSqlParameterSource();
        mapParam.addValue("first_name", userInfo.getFirstName());
        mapParam.addValue("last_name", userInfo.getLastName());
        mapParam.addValue("email", userInfo.getEmail());
        mapParam.addValue("password", userInfo.getPassword());
        mapParam.addValue("active", userInfo.getActive());
        jdbcSupport.update(sqlBd.toString(), mapParam);
    }
}
