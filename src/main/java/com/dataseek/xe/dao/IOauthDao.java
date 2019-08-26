package com.dataseek.xe.dao;

import com.dataseek.xe.entity.EtsyAccountBind;
import com.dataseek.xe.entity.EtsyDeveloperDetail;
import com.dataseek.xe.entity.EtsyTokenAdmin;

public interface IOauthDao {
    //根据app开发者帐号查询相关开发配置信息
    public EtsyDeveloperDetail queryEtsyDeveloperDetail();

    //根据APP账户查询etsy用户token管理信息
    public EtsyTokenAdmin queryEtsyTokenAdminByAppAccount(String app_account);

    //根据app帐号删除token管理记录
    public void deleteEtsyTokenAdminByAppAccount(String app_account);

    //根据APP帐号新增token记录,主要包含request token和request secret
    public void insertReqTokenAndSecretWithAppAccount(String app_account,String request_token,String request_secret);
}
