package com.dataseek.xe.dao;

import com.dataseek.xe.entity.EtsyAccountBind;
import com.dataseek.xe.entity.EtsyDeveloperDetail;

public interface IOauthDao {
    //根据app开发者帐号查询相关开发配置信息
    public EtsyDeveloperDetail queryEtsyDeveloperDetail();

    //根据app和etsy账户名查询帐号绑定记录
    public EtsyAccountBind queryBindRecordByAppEtsy(String app_account, String etsy_account);

    //新增etsy账户与应用账户的绑定记录
    public Integer insertAppEtsyBind(String app_account, String etsy_account);
}
