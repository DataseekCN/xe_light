package com.dataseek.xe.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dataseek.xe.dao.IOauthDao;
import com.dataseek.xe.entity.OauthInfo;
import com.dataseek.xe.entity.XeroDeveloperDetail;
import com.dataseek.xe.service.IOauthService;
import com.dataseek.xe.util.XeConsts;
import com.dataseek.xe.vo.OauthVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/connections")
public class OauthController {

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
    public JSONObject xeroTokenVerify(@RequestParam String app_account){
        JSONObject jsonObject = new JSONObject();
        //查询Xero开发者配置信息
        XeroDeveloperDetail xeroDeveloperDetail = oauthDao.queryXeroDeveloperDetail();
        //判断APP账户下是否存在access token
        boolean tokenIsExist = oauthService.checkXeroTokenExist(app_account);
        //不存在token(否)
        if(!tokenIsExist){
            //申请授权链接

        }
        //存在token(是)
        else{
            //判断access token是否过期

            //刷新access token

        }

        return jsonObject;
    }
}
