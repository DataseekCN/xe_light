package com.dataseek.xe.dao;

import com.dataseek.xe.entity.*;

public interface IOauthDao {
    //根据app开发者帐号查询相关开发配置信息
    public EtsyDeveloperDetail queryEtsyDeveloperDetail();

    //根据APP账户查询etsy用户token管理信息
    public EtsyTokenAdmin queryEtsyTokenAdminByAppAccount(String app_account);

    //根据request_token查询etsy用户token管理信息
    public EtsyTokenAdmin queryEtsyTokenAdminByReqToken(String request_token);

    //根据app帐号删除token管理记录
    public void deleteEtsyTokenAdminByAppAccount(String app_account);

    //根据APP帐号新增token记录,主要包含request token和request secret
    public void insertEtsyReqTokenAndSecretWithAppAccount(String app_account, String request_token, String request_secret);

    //根据request_token更新access_token和access_secret
    public void updateEtsyAccessTokenAndSecretByRequestToken(OauthInfo paramOauthInfo);

    //查询Xero相关开发配置信息
    public XeroDeveloperDetail queryXeroDeveloperDetail();

    //根据APP账户查询xero用户token管理信息
    public XeroTokenAdmin queryXeroTokenAdminByAppAccount(String app_account);
}
