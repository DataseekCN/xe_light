/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　 ┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * Module Desc:
 * User: taosm
 * DateTime: 2019-08-21 16
 */
package com.dataseek.xe.dao.impl;

import com.dataseek.xe.dao.IOauthDao;
import com.dataseek.xe.entity.OauthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OauthDao implements IOauthDao {
    @Autowired
    private JdbcTemplate xeJdbcTemplate;

    //根据app账户名和etsy账户名查询etsy token管理记录
    public OauthInfo queryTokenRecordByAppEtsyAccount(String app_account,String etsy_account){
        OauthInfo oauthInfo = new OauthInfo();
        String query_sql = "select admin_id,app_account,etsy_account,request_token," +
                "request_secret,access_token,access_secret,update_time " +
                "from xero.etsy_token_admin where app_account=? and etsy_account=? ";
        Object[] params = new Object[]{app_account,etsy_account};
        return oauthInfo;
    }
}
