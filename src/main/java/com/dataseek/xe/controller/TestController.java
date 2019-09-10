package com.dataseek.xe.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dataseek.xe.entity.OauthInfo;
import com.dataseek.xe.entity.TestUser;
import com.dataseek.xe.service.IOauthService;
import com.dataseek.xe.service.ITestService;
import com.dataseek.xe.util.XeConsts;
import com.dataseek.xe.vo.OauthVo;
import com.dataseek.xe.vo.TestUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/test")
public class TestController {
    @Autowired
    private ITestService testService;

    @Autowired
    private IOauthService oauthService;

    @RequestMapping(value="/verify_user_exist",method = RequestMethod.GET)
    private TestUserVo verifyUserExist(@RequestParam String user_name){
        TestUserVo testUserVo = new TestUserVo();
        TestUser testUser = testService.queryExistTestUser(user_name);
        if(testUser!=null){
            testUserVo.setName(testUser.getName());
            testUserVo.setId(testUser.getId());
            testUserVo.setStatus(XeConsts.RESPONSE_STATUS_FAILURE);
            testUserVo.setError_message("用户名已经存在!");
        }
        return testUserVo;
    }

    @RequestMapping(value="/etsy/token_verify",method = RequestMethod.GET)
    private JSONObject etsyTokenVerify(@RequestParam String app_account){
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

    @RequestMapping(value="/etsy/token_apply",method = RequestMethod.GET)
    private JSONObject etsyTokenApply(@RequestParam String oauth_token,@RequestParam String oauth_verifier){
        OauthVo oauthVo = new OauthVo();
        JSONObject jsonObject = (JSONObject)JSON.toJSON(oauthVo);
        OauthInfo oauthInfo = oauthService.applyEtsyAccessToken(oauth_token,oauth_verifier);
        jsonObject.put("status","success");
        jsonObject.put("auth_status",XeConsts.AUTH_STATUS_AUTHORIZED);
        jsonObject.put("access_token",oauthInfo.getAccess_token());
        jsonObject.put("access_secret",oauthInfo.getAccess_secret());
        return jsonObject;
    }
}
