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
import com.dataseek.xe.entity.EtsyDeveloperDetail;
import com.dataseek.xe.entity.EtsyTokenAdmin;
import com.dataseek.xe.entity.OauthInfo;
import com.dataseek.xe.mapper.JSONObjectMapper;
import com.dataseek.xe.util.DateUtil;
import com.dataseek.xe.util.XeConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class OauthDao implements IOauthDao {
    private final static Logger logger = LoggerFactory.getLogger(OauthDao.class);
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
        if(!StringUtils.isEmpty(app_account)) {
            String query_sql = " select admin_id,app_account,request_token,request_secret, " +
                    " access_token,access_secret,update_time " +
                    " from xero.etsy_token_admin " +
                    " where app_account=? ";
            Object[] params = new Object[]{app_account};
            List<JSONObject> detailJsons = xeJdbcTemplate.query(query_sql, params, new JSONObjectMapper());
            if (detailJsons != null && detailJsons.size() > 0) {
                JSONObject detailJson = detailJsons.get(0);
                etsyTokenAdmin = JSON.parseObject(detailJson.toJSONString(), EtsyTokenAdmin.class);
            }
        }
        return etsyTokenAdmin;
    }

    //根据request_token查询etsy用户token管理信息
    public EtsyTokenAdmin queryEtsyTokenAdminByReqToken(String request_token){
        EtsyTokenAdmin etsyTokenAdmin = null;
        if(!StringUtils.isEmpty(request_token)) {
            String query_sql = " select admin_id,app_account,request_token,request_secret, " +
                    " access_token,access_secret,update_time " +
                    " from xero.etsy_token_admin " +
                    " where request_token=?  ";
            Object[] params = new Object[]{request_token};
            List<JSONObject> detailJsons = xeJdbcTemplate.query(query_sql, params, new JSONObjectMapper());
            if (detailJsons != null && detailJsons.size() > 0) {
                JSONObject detailJson = detailJsons.get(0);
                etsyTokenAdmin = JSON.parseObject(detailJson.toJSONString(), EtsyTokenAdmin.class);
            }
        }
        return etsyTokenAdmin;
    }

    //根据app帐号删除token管理记录
    public void deleteEtsyTokenAdminByAppAccount(String app_account){
        if(!StringUtils.isEmpty(app_account)) {
            String delete_sql = " delete from xero.etsy_token_admin " +
                    " where app_account=?  ";
            Object[] params = new Object[]{app_account};
            xeJdbcTemplate.update(delete_sql, params);
            logger.info("token record has been deleted!");
        }
    }

    //根据APP帐号新增token记录,主要包含request token和request secret
    public void insertReqTokenAndSecretWithAppAccount(String app_account,String request_token,String request_secret){
        if(!StringUtils.isEmpty(app_account)
                &&!StringUtils.isEmpty(request_token)
                &&!StringUtils.isEmpty(request_secret)
        ) {
            String access_token = "";
            String access_secret = "";
            String update_time = DateUtil.getNowTime_EN();
            String insert_sql = " insert into xero.etsy_token_admin(app_account,request_token,request_secret,access_token,access_secret,update_time) " +
                    " values(?,?,?,?,?,?) ";
            Object[] params = new Object[]{app_account, request_token, request_secret, access_token, access_secret, update_time};
            xeJdbcTemplate.update(insert_sql, params);
            logger.info("token record has been inserted!");
        }
    }

    //根据request_token更新access_token和access_secret
    public void updateAccessTokenAndSecretByRequestToken(OauthInfo paramOauthInfo){
        String request_token = paramOauthInfo.getRequest_token();
        String access_token = paramOauthInfo.getAccess_token();
        String access_secret = paramOauthInfo.getAccess_secret();
        String update_time = DateUtil.getNowTime_EN();
        if(!StringUtils.isEmpty(request_token)
                &&!StringUtils.isEmpty(access_token)
                &&!StringUtils.isEmpty(access_secret)
        ) {
            String update_sql = " update xero.etsy_token_admin " +
                    " set request_token=?,request_secret=?,access_token=?,access_secret=?,update_time=? " +
                    " where request_token=? ";
            Object[] params = new Object[]{"","",access_token,access_secret,update_time,request_token};
            xeJdbcTemplate.update(update_sql, params);
            logger.info("token record has been updated!");
        }
    }

}
