package com.dataseek.xe.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dataseek.xe.dao.IOauthDao;
import com.dataseek.xe.entity.OauthInfo;
import com.dataseek.xe.entity.TestUser;
import com.dataseek.xe.entity.XeroDeveloperDetail;
import com.dataseek.xe.extend.apis.XeroVisitApi;
import com.dataseek.xe.service.IOauthService;
import com.dataseek.xe.service.ITestService;
import com.dataseek.xe.util.XeConsts;
import com.dataseek.xe.vo.OauthVo;
import com.dataseek.xe.vo.TestUserVo;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@CrossOrigin
@RequestMapping("/test")
public class TestController {
    @Autowired
    private ITestService testService;

    @Autowired
    private IOauthService oauthService;

    @Autowired
    private IOauthDao oauthDao;

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

    @RequestMapping(value="/xero/organisation",method = RequestMethod.GET)
    private JSONObject fetchXeroOrganisation(){
        JSONObject jsonObject = new JSONObject();
        //查询Xero开发者配置信息
        XeroDeveloperDetail xeroDeveloperDetail = oauthDao.queryXeroDeveloperDetail();
        OAuth20Service service = XeroVisitApi.createXeroService(xeroDeveloperDetail);
        String test_url = "https://api.xero.com/api.xro/2.0/Organisations";
        OAuthRequest request = new OAuthRequest(Verb.GET, test_url);
        service.signRequest("eyJhbGciOiJSUzI1NiIsImtpZCI6IjFDQUY4RTY2NzcyRDZEQzAyOEQ2NzI2RkQwMjYxNTgxNTcwRUZDMTkiLCJ0eXAiOiJKV1QiLCJ4NXQiOiJISy1PWm5jdGJjQW8xbkp2MENZVmdWY09fQmsifQ.eyJuYmYiOjE1Njg4MDgxMTQsImV4cCI6MTU2ODgwODgzNCwiaXNzIjoiaHR0cHM6Ly9pZGVudGl0eS54ZXJvLmNvbSIsImF1ZCI6Imh0dHBzOi8vaWRlbnRpdHkueGVyby5jb20vcmVzb3VyY2VzIiwiY2xpZW50X2lkIjoiQUM4MTEzRUE1NUEwNEQ3NjgzMTYyRTg0NjFBRjNDODgiLCJzdWIiOiJhNjg5NjIwMTExY2Y1YjE0OWVlM2E2N2NlZTA1ZDk0NyIsImF1dGhfdGltZSI6MTU2ODgwODEwMiwiaWRwIjoibG9jYWwiLCJ4ZXJvX3VzZXJpZCI6ImVlOGQ4YWI0LWUwY2EtNGQ0Ny1hNjA0LTIxMWVjM2RlMmJjMiIsImdsb2JhbF9zZXNzaW9uX2lkIjoiZDlkY2Q4ZTEzNmY2NDc0NDhkMTZkM2MwNzlkNzNkNzEiLCJqdGkiOiI4YTNhMjI0MjkxMzcwMjMzNzcxNmI2YWM3ODU5YTg2NSIsInNjb3BlIjpbImVtYWlsIiwicHJvZmlsZSIsIm9wZW5pZCIsImFjY291bnRpbmcucmVwb3J0cy5yZWFkIiwicHJvamVjdHMiLCJhY2NvdW50aW5nLnNldHRpbmdzIiwiYWNjb3VudGluZy5zZXR0aW5ncy5yZWFkIiwiYWNjb3VudGluZy5hdHRhY2htZW50cyIsImFjY291bnRpbmcudHJhbnNhY3Rpb25zIiwiYWNjb3VudGluZy5qb3VybmFscy5yZWFkIiwiYXNzZXRzIiwiYWNjb3VudGluZy5jb250YWN0cyIsIm9mZmxpbmVfYWNjZXNzIl0sImFtciI6WyJwd2QiXX0.IYwJXY7Y9qM72YaUjCKjZtZzxW2HxGMr-K9ziFDr4nUO1NTj66Xy09zsN6Xg6x5jSl3nm9Evbab-9aHOsfY5uj9-3y3r8EIsMnNHHetqSBVbSPph6zrRY7vzVMxjykz2rusqfGkdsHUh-kZ4PjJlUdhyKgJJG-18gEOQoO2n-kR2-_g0iUULh20A1utHIyo1Tk7FEboYMOyR_DuknmksGzRIKEBYzKM4_i42CqUUI8DZbsknKzGshB3FjibJNqP2_tpSja13hRjBMuUQgVUy0MsSiDQqfS_pPZE8M_jL9A6ISA_xeLq-V6oUKwRrovE6xQwLCjToJexrqhfUVqBAvg",request);
        try {
            Response response = service.execute(request);
            System.out.println(response.getCode());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
