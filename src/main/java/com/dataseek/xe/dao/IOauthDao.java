package com.dataseek.xe.dao;

import com.dataseek.xe.entity.EtsyAccountBind;
import com.dataseek.xe.entity.EtsyDeveloperDetail;

public interface IOauthDao {
    //根据app开发者帐号查询相关开发配置信息
    public EtsyDeveloperDetail queryEtsyDeveloperDetail();


}
