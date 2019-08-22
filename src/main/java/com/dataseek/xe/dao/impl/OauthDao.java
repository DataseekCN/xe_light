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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dataseek.xe.dao.IOauthDao;
import com.dataseek.xe.entity.EtsyAccountBind;
import com.dataseek.xe.entity.OauthInfo;
import com.dataseek.xe.entity.TestUser;
import com.dataseek.xe.mapper.JSONObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OauthDao implements IOauthDao {
    @Autowired
    private JdbcTemplate xeJdbcTemplate;

    //根据app和etsy账户绑定id查询etsy token管理记录
    public OauthInfo queryEtsyTokenRecordByBindId(Integer bind_id){
        OauthInfo oauthInfo = null;
//        String bind_query_sql=" select bind_id,app_account,etsy_account " +
//                " from xero.etsy_account_bind" +
//                " where app_account=? and etsy_account=?";
//        Object[] params = new Object[]{app_account,etsy_account};
//        JSONObject bindInfoJson = xeJdbcTemplate.queryForObject(bind_query_sql,params,new JSONObjectMapper());
//        Integer bind_id = bindInfoJson.getInteger("bind_id");
        //如果存在绑定关系
        if(bind_id!=null){
            String token_query_sql = " select admin_id,bind_id,request_token,request_token," +
                    " request_secret,access_token,access_secret,update_time " +
                    " from xero.etsy_token_admin where bind_id=?";
            Object[] params = new Object[]{bind_id};
            JSONObject oauthInfoJson = xeJdbcTemplate.queryForObject(token_query_sql,params,new JSONObjectMapper());
            oauthInfo = JSON.parseObject(oauthInfoJson.toJSONString(),OauthInfo.class);
        }
        return oauthInfo;
    }

    //根据app和etsy账户名查询帐号绑定记录
    public EtsyAccountBind queryBindRecordByAppEtsy(String app_account, String etsy_account){
        EtsyAccountBind etsyAccountBind = null;
        String bind_query_sql = " select bind_id,app_account,etsy_account,update_time " +
                " from xero.etsy_account_bind " +
                " where app_account=? and etsy_account=? ";
        Object[] params = new Object[]{app_account,etsy_account};
        JSONObject bindJson = xeJdbcTemplate.queryForObject(bind_query_sql,params,new JSONObjectMapper());
        etsyAccountBind = JSON.parseObject(bindJson.toJSONString(),EtsyAccountBind.class);
        return etsyAccountBind;
    }

    //新增etsy账户与应用账户的绑定记录
    public void insertAppEtsyBind(String app_account, String etsy_account){
        String bind_insert_sql = " insert into xero.etsy_account_bind(app_account,etsy_account)" +
                " values() ";
    }


}
