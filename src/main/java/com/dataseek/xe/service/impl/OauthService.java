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
import com.dataseek.xe.entity.EtsyAccountBind;
import com.dataseek.xe.entity.OauthInfo;
import com.dataseek.xe.service.IOauthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OauthService implements IOauthService {
    @Autowired
    private IOauthDao oauthDao;

    //验证Etsy的oauth授权情况
    @Transactional(propagation = Propagation.REQUIRED)
    public OauthInfo verifyEtsyAuthStatus(String app_account,String etsy_account){
        OauthInfo oauthInfo = new OauthInfo();
        //验证是否存在etsy账户与app应用账户绑定关系
        EtsyAccountBind accountBind = oauthDao.queryBindRecordByAppEtsy(app_account,etsy_account);
        Integer bind_id = accountBind.getBind_id();
        //etsy帐号与应用帐号未绑定
        if(bind_id==null){
            //将应用帐号与etsy帐号进行绑定

            //返回authorize url

        }
        //etsy帐号与应用帐号已绑定
        else{
            //验证是否已经申请access token和access secret

        }
        return  oauthInfo;
    }

    //申请etsy authorize url

}
