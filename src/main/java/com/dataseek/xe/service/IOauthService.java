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
package com.dataseek.xe.service;

import com.dataseek.xe.config.XeAutoConfig;
import com.dataseek.xe.entity.OauthInfo;
import com.dataseek.xe.entity.XeroDeveloperDetail;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface IOauthService {
    //验证Etsy的oauth授权情况
    public OauthInfo verifyEtsyAuthStatus(String app_account);

    //申请access token并返回授权链接等信息
    public OauthInfo applyEtsyAccessToken(String oauth_token, String oauth_verifier);

    public boolean checkXeroTokenExist(String app_account);

    //申请xero的授权链接
    @Transactional(value= XeAutoConfig.DEFAULT_TX, rollbackFor=Exception.class)
    String requestXeroAuthUrl(String app_account, XeroDeveloperDetail xeroDeveloperDetail);
}
