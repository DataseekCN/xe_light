package com.dataseek.xe.mapper;

import com.alibaba.fastjson.JSONObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

//通用型JSON Mapper类
public class JSONObjectMapper implements RowMapper<JSONObject> {
    public JSONObject mapRow(ResultSet rs, int index) throws SQLException {
        ResultSetMetaData metaData=rs.getMetaData();
        JSONObject jsonObject = new JSONObject();
        for(int i=1;i<=metaData.getColumnCount();i++){
            String columnName = metaData.getColumnName(i);
            Object columnValue = rs.getObject(columnName);
            jsonObject.put(columnName,columnValue);
        }
        return jsonObject;
    }
}
