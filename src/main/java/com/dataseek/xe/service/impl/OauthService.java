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

import com.dataseek.xe.config.XeAutoConfig;
import com.dataseek.xe.dao.IOauthDao;
import com.dataseek.xe.entity.*;
import com.dataseek.xe.extend.apis.EtsyVisitApi;
import com.dataseek.xe.extend.apis.XeroVisitApi;
import com.dataseek.xe.service.IOauthService;
import com.dataseek.xe.util.XeConsts;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OauthService implements IOauthService {
    @Autowired
    private IOauthDao oauthDao;

    //验证Etsy的oauth授权情况
    @Transactional(value= XeAutoConfig.DEFAULT_TX, rollbackFor=Exception.class)
    public OauthInfo verifyEtsyAuthStatus(String app_account){
        OauthInfo oauthInfo = null;
        //查询App开发者相关配置信息
        EtsyDeveloperDetail etsyDeveloperDetail = oauthDao.queryEtsyDeveloperDetail();
        //验证app账户下是否存在access_token
        EtsyTokenAdmin etsyTokenAdmin = oauthDao.queryEtsyTokenAdminByAppAccount(app_account);
        if(etsyTokenAdmin!=null){
            //查询该账户下的access token
            String access_token = etsyTokenAdmin.getAccess_token();
            //查询该账户下的access secret
            String access_secret = etsyTokenAdmin.getAccess_secret();
            //access_token是否存在?
            //未申请access_token
            if(StringUtils.isEmpty(access_token)){
                //申请request token
                oauthInfo= applyEtsyRequestToken(app_account,etsyDeveloperDetail);
            }
            //已申请access_token
            else{
                //验证token有效性
                //设置已授权状态
                oauthInfo = new OauthInfo();
                oauthInfo.setAuth_status(XeConsts.AUTH_STATUS_AUTHORIZED);
                oauthInfo.setAccess_token(access_token);
                oauthInfo.setAccess_secret(access_secret);
            }
        }
        //未申请access_token
        else{
            //申请request_token
            oauthInfo= applyEtsyRequestToken(app_account,etsyDeveloperDetail);
            //验证token有效性
        }
        return oauthInfo;
    }


    //申请request token并返回授权链接等信息
    @Transactional(value= XeAutoConfig.DEFAULT_TX, rollbackFor=Exception.class)
    public OauthInfo applyEtsyRequestToken(String app_account, EtsyDeveloperDetail etsyDeveloperDetail){
        OauthInfo oauthInfo = null;
        //申请request token
        oauthInfo = EtsyVisitApi.fetchRequestInfo(etsyDeveloperDetail);
       if(oauthInfo!=null) {
           //设置授权状态为待授权
           oauthInfo.setAuth_status(XeConsts.AUTH_STATUS_WAIT_AUTHORIZE);
           String request_token = oauthInfo.getRequest_token();
           String request_secret = oauthInfo.getRequest_secret();
           //根据开发者帐号,保存request_token和request_secret
           oauthDao.deleteEtsyTokenAdminByAppAccount(app_account);
           oauthDao.insertEtsyReqTokenAndSecretWithAppAccount(app_account, request_token, request_secret);
       }
        return oauthInfo;
    }

    //申请access token并返回授权链接等信息
    @Transactional(value= XeAutoConfig.DEFAULT_TX, rollbackFor=Exception.class)
    public OauthInfo applyEtsyAccessToken(String oauth_token, String oauth_verifier){
        OauthInfo oauthInfo = null;
        //查询App开发者相关配置信息
        EtsyDeveloperDetail etsyDeveloperDetail = oauthDao.queryEtsyDeveloperDetail();
        EtsyTokenAdmin etsyTokenAdmin = oauthDao.queryEtsyTokenAdminByReqToken(oauth_token);
        OauthInfo paramOauthInfo = new OauthInfo();
        paramOauthInfo.setRequest_token(etsyTokenAdmin.getRequest_token());
        paramOauthInfo.setRequest_secret(etsyTokenAdmin.getRequest_secret());
        paramOauthInfo.setOauth_verifier(oauth_verifier);
        oauthInfo = EtsyVisitApi.fetchAccessInfo(etsyDeveloperDetail,paramOauthInfo);
        oauthDao.updateEtsyAccessTokenAndSecretByRequestToken(oauthInfo);
        return oauthInfo;
    }

    //验证Xero的oauth授权情况
    @Transactional(value= XeAutoConfig.DEFAULT_TX, rollbackFor=Exception.class)
    public OauthInfo verifyXeroAuthStatus(String app_account){
        OauthInfo oauthInfo = null;
        //查询App开发者相关配置信息
        XeroDeveloperDetail xeroDeveloperDetail = oauthDao.queryXeroDeveloperDetail();

        return oauthInfo;
    }

    //验证APP账户下是否存在access token
    @Override
    public boolean checkXeroTokenExist(String app_account) {
        boolean isExist =false;
        XeroTokenAdmin xeroTokenAdmin = oauthDao.queryXeroTokenAdminByAppAccount(app_account);
        if(xeroTokenAdmin!=null){
            String access_token = xeroTokenAdmin.getAccess_token();
            String refresh_token = xeroTokenAdmin.getRefresh_token();
            if(!StringUtils.isEmpty(access_token)
                &&!StringUtils.isEmpty(refresh_token)){
                isExist=true;
            }
        }
        return isExist;
    }

    //申请xero的授权链接
    @Override
    @Transactional(value= XeAutoConfig.DEFAULT_TX, rollbackFor=Exception.class)
    public String requestXeroAuthUrl(String app_account,XeroDeveloperDetail xeroDeveloperDetail){
        String auth_url=null;
        OAuth20Service service = XeroVisitApi.createXeroService(xeroDeveloperDetail);
        final String secretState = UUID.randomUUID().toString().replaceAll("-","");
        final Map<String, String> additionalParams = new HashMap<>();
        additionalParams.put("access_type", "offline");
        auth_url = service.createAuthorizationUrlBuilder()
                .state(secretState)
                .additionalParams(additionalParams)
                .build();
        //保存相关state状态
        XeroTokenAdmin xeroTokenAdmin = new XeroTokenAdmin();
        xeroTokenAdmin.setApp_account(app_account);
        xeroTokenAdmin.setState(secretState);
        oauthDao.insertXeroTokenAdmin(xeroTokenAdmin);
        return auth_url;
    }

    @Override
    @Transactional(value= XeAutoConfig.DEFAULT_TX, rollbackFor=Exception.class)
    public void updateXeroAccessToken(XeroTokenAdmin xeroTokenAdmin) {
        oauthDao.insertXeroTokenAdmin(xeroTokenAdmin);
    }
}
