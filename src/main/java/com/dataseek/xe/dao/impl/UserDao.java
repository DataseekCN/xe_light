package com.dataseek.xe.dao.impl;

import com.dataseek.xe.config.JdbcSupport;
import com.dataseek.xe.dao.IUserDao;
import com.dataseek.xe.entity.UserInfo;
import com.dataseek.xe.util.DataUtil;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

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

    public List<UserInfo> qryUser(UserInfo userInfo) {
        MapSqlParameterSource mapParam = new MapSqlParameterSource();
        StringBuilder sqlBd = new StringBuilder();
        sqlBd.append("select first_name,last_name,email,password,active,user_id,create_date,upd_date,session_id from xe_user where 1=1 ");

        if (!DataUtil.isEmpty(userInfo.getEmail())) {
            sqlBd.append("and email=:email ");
            mapParam.addValue("email", userInfo.getEmail());
        }
        if (!DataUtil.isEmpty(userInfo.getPassword())) {
            sqlBd.append("and password=:password ");
            mapParam.addValue("password", userInfo.getPassword());
        }
        if (!DataUtil.isEmpty(userInfo.getUserId())) {
            sqlBd.append("and user_id=:user_id ");
            mapParam.addValue("user_id", userInfo.getUserId());
        }

        List<UserInfo> reList = jdbcSupport.query(sqlBd.toString(), UserInfo.class, mapParam);
        return reList;
    }

    public void updUser(UserInfo userInfo) {
        StringBuilder sqlBd = new StringBuilder();
        sqlBd.append("update xe_user set ");
        sqlBd.append("first_name=:first_name,last_name=:last_name,email=:email,password=:password,active=:active, ");
        sqlBd.append("user_id=:user_id,create_date=:create_date,upd_date=sysdate(),session_id=:session_id ");
        sqlBd.append("where user_id=:user_id  ");

        MapSqlParameterSource mapParam = new MapSqlParameterSource();
        mapParam.addValue("first_name", userInfo.getFirstName());
        mapParam.addValue("last_name", userInfo.getLastName());
        mapParam.addValue("email", userInfo.getEmail());
        mapParam.addValue("password", userInfo.getPassword());
        mapParam.addValue("active", userInfo.getActive());
        mapParam.addValue("user_id", userInfo.getUserId());
        mapParam.addValue("create_date", userInfo.getCreateDate());
        mapParam.addValue("session_id", userInfo.getSessionId());
        jdbcSupport.update(sqlBd.toString(), mapParam);
    }


}
