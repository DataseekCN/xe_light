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
 * DateTime: 2019-08-21 14
 */
package com.dataseek.xe.service.impl;

import com.dataseek.xe.dao.IOauthDao;
import com.dataseek.xe.entity.EtsyDeveloperDetail;
import com.dataseek.xe.entity.EtsyTokenAdmin;
import com.dataseek.xe.entity.OauthInfo;
import com.dataseek.xe.extend.apis.EtsyVisitApi;
import com.dataseek.xe.service.IOauthService;
import com.dataseek.xe.util.XeConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class OauthService implements IOauthService {
    @Autowired
    private IOauthDao oauthDao;

    //验证Etsy的oauth授权情况
    @Transactional(propagation = Propagation.REQUIRED)
    public OauthInfo verifyEtsyAuthStatus(String app_account){
        OauthInfo oauthInfo = null;
        //查询App开发者相关配置信息
        EtsyDeveloperDetail etsyDeveloperDetail = oauthDao.queryEtsyDeveloperDetail();
        //验证app账户下是否存在oauth_token
        EtsyTokenAdmin etsyTokenAdmin = oauthDao.queryEtsyTokenAdminByAppAccount(app_account);
        if(etsyTokenAdmin!=null){
            //查询该账户下的access token
            String access_token = etsyTokenAdmin.getAccess_token();
            //access_token是否存在?
            //未申请access_token
            if(StringUtils.isEmpty(access_token)){
                //申请request token
                oauthInfo=applyRequestToken(app_account,etsyDeveloperDetail);
            }
            //已申请access_token
            else{
                //验证token有效性

            }
        }
        //未申请access_token
        else{
            //申请access_token
            oauthInfo=applyRequestToken(app_account,etsyDeveloperDetail);
            //验证token有效性

        }
        return oauthInfo;
    }


    //申请request token并返回授权链接等信息
    @Transactional(propagation = Propagation.REQUIRED)
    public OauthInfo applyRequestToken(String app_account,EtsyDeveloperDetail etsyDeveloperDetail){
        OauthInfo oauthInfo = null;
        //申请request token
        oauthInfo = EtsyVisitApi.fetchRequestInfo(etsyDeveloperDetail);
        //设置授权状态为待授权
        oauthInfo.setAuth_status(XeConsts.AUTH_STATUS_WAIT_AUTHORIZE);
        String request_token = oauthInfo.getRequest_token();
        String request_secret = oauthInfo.getRequest_secret();
        //根据开发者帐号,保存request_token和request_secret
        oauthDao.deleteEtsyTokenAdminByAppAccount(app_account);
        oauthDao.insertReqTokenAndSecretWithAppAccount(app_account,request_token,request_secret);
        return oauthInfo;
    }



}
