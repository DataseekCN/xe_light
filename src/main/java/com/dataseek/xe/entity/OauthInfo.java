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
package com.dataseek.xe.entity;
//认证实体信息
public class OauthInfo {
    //授权地址
    private String authorize_url;
    //授权状态
    private String authorize_status;
    //etsy和app账户绑定关系id
    private Integer bind_id;
    //oauth_consumer_key
    private String oauth_consumer_key;
    //oauth_consumer_secret
    private String oauth_consumer_secret;
    //request_token
    private String request_token;
    //request_secret
    private String request_secret;
    //access_token
    private String access_token;
    //access_secret
    private String access_secret;
    //更新时间
    private String update_time;

    public String getAuthorize_url() {
        return authorize_url;
    }

    public void setAuthorize_url(String authorize_url) {
        this.authorize_url = authorize_url;
    }

    public String getAuthorize_status() {
        return authorize_status;
    }

    public void setAuthorize_status(String authorize_status) {
        this.authorize_status = authorize_status;
    }

    public String getOauth_consumer_key() {
        return oauth_consumer_key;
    }

    public void setOauth_consumer_key(String oauth_consumer_key) {
        this.oauth_consumer_key = oauth_consumer_key;
    }

    public String getOauth_consumer_secret() {
        return oauth_consumer_secret;
    }

    public void setOauth_consumer_secret(String oauth_consumer_secret) {
        this.oauth_consumer_secret = oauth_consumer_secret;
    }

    public String getRequest_token() {
        return request_token;
    }

    public void setRequest_token(String request_token) {
        this.request_token = request_token;
    }

    public String getRequest_secret() {
        return request_secret;
    }

    public void setRequest_secret(String request_secret) {
        this.request_secret = request_secret;
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

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public Integer getBind_id() {
        return bind_id;
    }

    public void setBind_id(Integer bind_id) {
        this.bind_id = bind_id;
    }
}
