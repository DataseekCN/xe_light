package com.dataseek.xe.dao;

import com.dataseek.xe.entity.EtsyAccountBind;

public interface IOauthDao {
    //根据app和etsy账户名查询帐号绑定记录
    public EtsyAccountBind queryBindRecordByAppEtsy(String app_account, String etsy_account);
}
