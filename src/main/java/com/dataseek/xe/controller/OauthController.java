package com.dataseek.xe.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dataseek.xe.dao.IOauthDao;
import com.dataseek.xe.entity.OauthInfo;
import com.dataseek.xe.entity.XeroDeveloperDetail;
import com.dataseek.xe.entity.XeroTokenAdmin;
import com.dataseek.xe.extend.apis.XeroVisitApi;
import com.dataseek.xe.service.base.IOauthService;
import com.dataseek.xe.util.AppConfig;
import com.dataseek.xe.util.DateUtils;
import com.dataseek.xe.util.XeConsts;
import com.dataseek.xe.vo.OauthVo;
import com.github.scribejava.core.model.OAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/connections")
public class OauthController {
    private final static Logger logger = LoggerFactory.getLogger(OauthController.class);

    @Autowired
    private IOauthService oauthService;

    @Autowired
    private IOauthDao oauthDao;

	//验证用户etsy的token状态
    @RequestMapping(value="/etsy/token_verify",method = RequestMethod.GET)
    public JSONObject etsyTokenVerify(@RequestParam String app_account){
        OauthVo oauthVo = new OauthVo();
        JSONObject jsonObject = (JSONObject)JSON.toJSON(oauthVo);
        //验证授权状态
        OauthInfo oauthInfo = oauthService.verifyEtsyAuthStatus(app_account);
        //如果等待用户授权
        if(XeConsts.AUTH_STATUS_WAIT_AUTHORIZE.equals(oauthInfo.getAuth_status())){
            jsonObject.put("status","success");
            jsonObject.put("auth_status",XeConsts.AUTH_STATUS_WAIT_AUTHORIZE);
            jsonObject.put("grant_url",oauthInfo.getGrant_url());
        }
        //如果已授权
        else{
            jsonObject.put("status","success");
            jsonObject.put("auth_status",XeConsts.AUTH_STATUS_AUTHORIZED);
            jsonObject.put("access_token",oauthInfo.getAccess_token());
            jsonObject.put("access_secret",oauthInfo.getAccess_secret());
        }
        return jsonObject;
    }
	
	//用户申请etsy的access token
    @RequestMapping(value="/etsy/token_apply",method = RequestMethod.GET)
    public JSONObject etsyTokenApply(@RequestParam String oauth_token,@RequestParam String oauth_verifier){
        OauthVo oauthVo = new OauthVo();
        JSONObject jsonObject = (JSONObject)JSON.toJSON(oauthVo);
        OauthInfo oauthInfo = oauthService.applyEtsyAccessToken(oauth_token,oauth_verifier);
        jsonObject.put("status","success");
        jsonObject.put("auth_status",XeConsts.AUTH_STATUS_AUTHORIZED);
        jsonObject.put("access_token",oauthInfo.getAccess_token());
        jsonObject.put("access_secret",oauthInfo.getAccess_secret());
        return jsonObject;
    }

    //验证用户xero的token状态
    @RequestMapping(value="/xero/token_verify",method = RequestMethod.GET)
    public OauthVo xeroTokenVerify(@RequestParam String app_account){
        OauthVo oauthVo = new OauthVo();
        //查询Xero开发者配置信息
        XeroDeveloperDetail xeroDeveloperDetail = AppConfig.xeroDeveloperDetail;
        //判断APP账户下是否存在access token
        boolean tokenIsExist = oauthService.checkXeroTokenExist(app_account);
        //不存在token(否)
        if(!tokenIsExist){
            //申请授权链接
            String auth_url = oauthService.requestXeroAuthUrl(app_account,xeroDeveloperDetail);
            oauthVo.setStatus("success");
            oauthVo.setAuth_status(XeConsts.AUTH_STATUS_WAIT_AUTHORIZE);
            oauthVo.setGrant_url(auth_url);
            return oauthVo;
        }
        //存在token(是)
        else{
            //判断access token是否过期
            //判断access token是否过期--根据APP账户名查询access token
            XeroTokenAdmin xeroTokenAdmin = oauthDao.queryXeroTokenAdminByAppAccount(app_account);
            String access_token = xeroTokenAdmin.getAccess_token();
            String refresh_token = xeroTokenAdmin.getRefresh_token();
            boolean token_status = false;
            try{
                token_status = XeroVisitApi.verifyXeroTokenExpireStatus(access_token,xeroDeveloperDetail);
            }
            catch (Exception e){
                oauthVo.setStatus("failure");
                oauthVo.setError_msg(e.getMessage());
                return oauthVo;
            }
            //access token已过期
            if(!token_status) {
                //刷新access token
                OAuth2AccessToken accessToken = null;
                try {
                    accessToken = XeroVisitApi.refreshXeroToken(refresh_token, xeroDeveloperDetail);
                    //如果刷新成功
                    if(accessToken!=null) {
                        access_token = accessToken.getAccessToken();
                        refresh_token = accessToken.getRefreshToken();
                        Integer expiresIn = accessToken.getExpiresIn();
                        Long currentMills = System.currentTimeMillis();
                        Long expireMills = currentMills + expiresIn;
                        String expire_time = DateUtils.formatDateTimeStrFromMills(expireMills);
                        String update_time = DateUtils.getDateTimeNowStr();
                        xeroTokenAdmin.setAccess_token(access_token);
                        xeroTokenAdmin.setRefresh_token(refresh_token);
                        xeroTokenAdmin.setExpire_time(expire_time);
                        xeroTokenAdmin.setUpdate_time(update_time);
                        //更新最新access_token至数据库表
                        oauthService.updateXeroAccessToken(xeroTokenAdmin);
                    }
                    else{
                        //刷新失败则重新申请access token
                        //申请授权链接
                        String auth_url = oauthService.requestXeroAuthUrl(app_account,xeroDeveloperDetail);
                        oauthVo.setStatus("success");
                        oauthVo.setAuth_status(XeConsts.AUTH_STATUS_WAIT_AUTHORIZE);
                        oauthVo.setGrant_url(auth_url);
                        return oauthVo;
                    }
                }
                catch (Exception e){
                    oauthVo.setStatus("failure");
                    oauthVo.setError_msg(e.getMessage());
                    return oauthVo;
                }

            }
            oauthVo.setStatus("success");
            oauthVo.setAuth_status(XeConsts.AUTH_STATUS_AUTHORIZED);
            //oauthVo.setAccess_token(access_token);
        }
        return oauthVo;
    }

    //申请xero的access token
    @RequestMapping(value="/xero/token_apply",method = RequestMethod.GET)
    public OauthVo xeroTokenApply(@RequestParam String code,@RequestParam String state){
        OauthVo oauthVo = new OauthVo();
        //查询Xero开发者配置信息
        XeroDeveloperDetail xeroDeveloperDetail = AppConfig.xeroDeveloperDetail;
        //根据state查询账户token绑定信息记录
        XeroTokenAdmin xeroTokenAdmin = oauthDao.queryXeroTokenAdminByState(state);
        //申请access token
        OAuth2AccessToken accessToken = null;
        try {
            accessToken = XeroVisitApi.applyXeroToken(code,xeroDeveloperDetail);
            String access_token = accessToken.getAccessToken();
            Integer expiresIn = accessToken.getExpiresIn()*1000;
            String refresh_token = accessToken.getRefreshToken();
            Long currentMills = System.currentTimeMillis();
            Long expireMills = currentMills+expiresIn;
            String expire_time = DateUtils.formatDateTimeStrFromMills(expireMills);
            String update_time = DateUtils.getDateTimeNowStr();
            xeroTokenAdmin.setAccess_token(access_token);
            xeroTokenAdmin.setRefresh_token(refresh_token);
            xeroTokenAdmin.setExpire_time(expire_time);
            xeroTokenAdmin.setUpdate_time(update_time);
            xeroTokenAdmin.setState(null);
            //保存access token和相关信息
            oauthService.updateXeroAccessToken(xeroTokenAdmin);
            oauthVo.setStatus("success");
            oauthVo.setAuth_status(XeConsts.AUTH_STATUS_AUTHORIZED);
        } catch (Exception e) {
            logger.error(e.getMessage());
            oauthVo.setStatus("fail");
        }
        return oauthVo;
    }
}
