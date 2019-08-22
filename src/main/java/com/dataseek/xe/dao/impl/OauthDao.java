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
    //根据app开发者帐号查询相关开发配置信息
    public EtsyDeveloperDetail queryEtsyDeveloperDetail(){
        EtsyDeveloperDetail etsyDeveloperDetail = null;
        String query_sql = " select admin_id,developer_account,consumer_key,consumer_secret," +
                "request_token_url,authorize_url,access_token_url,callback_url " +
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

    //根据app和etsy账户名查询帐号绑定记录
    public EtsyAccountBind queryBindRecordByAppEtsy(String app_account, String etsy_account){
        EtsyAccountBind etsyAccountBind = null;
        String bind_query_sql = " select bind_id,app_account,etsy_account,update_time " +
                " from xero.etsy_account_bind " +
                " where app_account=? and etsy_account=? ";
        Object[] params = new Object[]{app_account,etsy_account};
        List<JSONObject> bindJsons = xeJdbcTemplate.query(bind_query_sql,params,new JSONObjectMapper());
        if(bindJsons!=null&&bindJsons.size()>0) {
            JSONObject bindJson = bindJsons.get(0);
            etsyAccountBind = JSON.parseObject(bindJson.toJSONString(), EtsyAccountBind.class);
        }
        return etsyAccountBind;
    }

    //新增etsy账户与应用账户的绑定记录
    public Integer insertAppEtsyBind(String app_account, String etsy_account){
        Integer affect_count=0;
        String bind_insert_sql = " insert into xero.etsy_account_bind(app_account,etsy_account,update_time)" +
                " values(?,?,?) ";
        Object[] params = new Object[]{app_account,etsy_account};
        affect_count=xeJdbcTemplate.update(bind_insert_sql,params);
        return affect_count;
    }


}
