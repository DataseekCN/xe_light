package com.dataseek.xe.vo;

import com.alibaba.fastjson.JSONObject;

public class OauthVo{
    //授权地址
    private String grant_url;
    //授权状态流程
    private String auth_status;
    //access_token
    private String access_token;
    //access_secret
    private String access_secret;
    //status
    private String status;

    private String error_msg;

    public String getGrant_url() {
        return grant_url;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public void setGrant_url(String grant_url) {
        this.grant_url = grant_url;
    }

    public String getAuth_status() {
        return auth_status;
    }

    public void setAuth_status(String auth_status) {
        this.auth_status = auth_status;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getAccess_secret() {
        return access_secret;
    }

    public void setAccess_secret(String access_secret) {
        this.access_secret = access_secret;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
