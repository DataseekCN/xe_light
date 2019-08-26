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
package com.dataseek.xe.extend.apis;

import com.dataseek.xe.entity.EtsyDeveloperDetail;
import com.dataseek.xe.entity.OauthInfo;
import com.github.scribejava.apis.EtsyApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EtsyVisitApi {
    private final static Logger logger = LoggerFactory.getLogger(EtsyVisitApi.class);

    //返回request token和secret相关实体信息
    public static OauthInfo fetchRequestInfo(EtsyDeveloperDetail etsyDeveloperDetail){
        OauthInfo oauthInfo = null;
        if(etsyDeveloperDetail!=null){
            String consumer_key = etsyDeveloperDetail.getConsumer_key();
            String consumer_secret= etsyDeveloperDetail.getConsumer_secret();
            //callback_url
            String callback_url = etsyDeveloperDetail.getCallback_url();
            final OAuth10aService service = new ServiceBuilder(consumer_key)
                    .apiSecret(consumer_secret)
                    .callback(callback_url)
                    .build(
                            EtsyApi.instance("transactions_r",
                                    "listings_r",
                                    "shops_rw",
                                    "profile_r",
                                    "billing_r")
                    );
            try {
                final OAuth1RequestToken requestToken = service.getRequestToken();
                oauthInfo=new OauthInfo();
                String request_token = requestToken.getToken();
                String request_secret = requestToken.getTokenSecret();
                String grant_url=service.getAuthorizationUrl(requestToken);
                oauthInfo.setGrant_url(grant_url);
                oauthInfo.setRequest_token(request_token);
                oauthInfo.setRequest_secret(request_secret);
            } catch (IOException e) {
                logger.error(e.getMessage());
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            } catch (ExecutionException e) {
                logger.error(e.getMessage());
            }
        }
        else{
            logger.error("etsy developer config info does not exist! ");
        }
        return oauthInfo;
    }

    //返回access token和access secret相关实体信息
    public static OauthInfo fetchAccessInfo(EtsyDeveloperDetail etsyDeveloperDetail,
                                            OauthInfo paramOauthInfo){
        OauthInfo oauthInfo = null;
        if(etsyDeveloperDetail!=null){
            String consumer_key = etsyDeveloperDetail.getConsumer_key();
            String consumer_secret= etsyDeveloperDetail.getConsumer_secret();
            //callback_url
            String callback_url = etsyDeveloperDetail.getCallback_url();
            final OAuth10aService service = new ServiceBuilder(consumer_key)
                    .apiSecret(consumer_secret)
                    .callback(callback_url)
                    .build(
                            EtsyApi.instance("transactions_r",
                                    "listings_r",
                                    "shops_rw",
                                    "profile_r",
                                    "billing_r")
                    );
            try {
                String request_secret = paramOauthInfo.getRequest_secret();
                String request_token = paramOauthInfo.getRequest_token();
                String oauth_verifier = paramOauthInfo.getOauth_verifier();
                final OAuth1RequestToken requestToken = new OAuth1RequestToken(request_token,request_secret);
                final OAuth1AccessToken accessToken = service.getAccessToken(requestToken, oauth_verifier);
                String access_token = accessToken.getToken();
                String access_secret = accessToken.getTokenSecret();
                oauthInfo=new OauthInfo();
                oauthInfo.setAccess_token(access_token);
                oauthInfo.setAccess_secret(access_secret);
            } catch (IOException e) {
                logger.error(e.getMessage());
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            } catch (ExecutionException e) {
                logger.error(e.getMessage());
            }
        }
        else{
            logger.error("etsy developer config info does not exist! ");
        }
        return oauthInfo;
    }
}
