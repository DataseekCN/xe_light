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
import com.dataseek.xe.entity.EtsyDeveloperDetail;
import com.dataseek.xe.entity.EtsyTokenAdmin;
import com.dataseek.xe.mapper.JSONObjectMapper;
import com.dataseek.xe.util.XeConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OauthDao implements IOauthDao {
    @Autowired
    private JdbcTemplate xeJdbcTemplate;
    //查询相关开发配置信息
    public EtsyDeveloperDetail queryEtsyDeveloperDetail(){
        EtsyDeveloperDetail etsyDeveloperDetail = null;
        String query_sql = " select admin_id,developer_account,consumer_key,consumer_secret," +
                " request_token_url,authorize_url,access_token_url,callback_url " +
                " from xero.etsy_developer_detail " +
                " where developer_account=? ";
        Object[] params = new Object[]{XeConsts.APP_DEVELOPER_ACCOUNT};
        List<JSONObject> detailJsons = xeJdbcTemplate.query(query_sql,params,new JSONObjectMapper());
        if(detailJsons!=null&&detailJsons.size()>0) {
            JSONObject detailJson = detailJsons.get(0);
            etsyDeveloperDetail = JSON.parseObject(detailJson.toJSONString(), EtsyDeveloperDetail.class);
        }
        return etsyDeveloperDetail;
    }

    //根据APP账户查询etsy用户token管理信息
    public EtsyTokenAdmin queryEtsyTokenAdminByAppAccount(String app_account){
        EtsyTokenAdmin etsyTokenAdmin = null;
        String query_sql = " select admin_id,app_account,request_token,request_secret, " +
                " access_token,access_secret,update_time " +
                " from xero.etsy_token_admin " +
                " where app_account=? ";
        Object[] params = new Object[]{app_account};
        List<JSONObject> detailJsons = xeJdbcTemplate.query(query_sql,params,new JSONObjectMapper());
        if(detailJsons!=null&&detailJsons.size()>0) {
            JSONObject detailJson = detailJsons.get(0);
            etsyTokenAdmin = JSON.parseObject(detailJson.toJSONString(), EtsyTokenAdmin.class);
        }
        return etsyTokenAdmin;
    }

    //根据request_token和request_secret查询etsy用户token管理信息
    public EtsyTokenAdmin queryEtsyTokenAdminByTokenAndSecret(String request_token,String request_secret){
        EtsyTokenAdmin etsyTokenAdmin = null;
        String query_sql = " select admin_id,app_account,request_token,request_secret, " +
                " access_token,access_secret,update_time " +
                " from xero.etsy_token_admin " +
                " where request_token=? and request_secret=? ";
        Object[] params = new Object[]{request_token,request_secret};
        List<JSONObject> detailJsons = xeJdbcTemplate.query(query_sql,params,new JSONObjectMapper());
        if(detailJsons!=null&&detailJsons.size()>0) {
            JSONObject detailJson = detailJsons.get(0);
            etsyTokenAdmin = JSON.parseObject(detailJson.toJSONString(), EtsyTokenAdmin.class);
        }
        return etsyTokenAdmin;
    }

    //根据request token和request secret更新access token和access_secret


}
