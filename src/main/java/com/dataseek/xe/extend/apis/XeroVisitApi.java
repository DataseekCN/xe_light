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
 * DateTime: 2019-09-12 09
 */
package com.dataseek.xe.extend.apis;

import com.dataseek.xe.dao.impl.OauthDao;
import com.dataseek.xe.entity.XeroDeveloperDetail;
import com.dataseek.xe.util.BASE64Encoder;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.codec.binary.Base64;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class XeroVisitApi {
    private final static Logger logger = LoggerFactory.getLogger(XeroVisitApi.class);

    public static OAuth20Service createXeroService(XeroDeveloperDetail xeroDeveloperDetail){
        OAuth20Service service = null;
        if(xeroDeveloperDetail!=null) {
            String consumer_key = xeroDeveloperDetail.getConsumer_key();
            String consumer_secret = xeroDeveloperDetail.getConsumer_secret();
            String callback_url = xeroDeveloperDetail.getCallback_url();
            String scope = "openid profile email accounting.transactions";
            service = new ServiceBuilder(consumer_key)
                    .apiSecret(consumer_secret)
                    .callback(callback_url)
                    .defaultScope(scope)
                    .build(XeroApi20.instance());
        }
        return service;
    }

    //验证access_token是否过期(true:未过期 false:已过期)
    public static boolean verifyXeroTokenExpireStatus(String access_token,XeroDeveloperDetail xeroDeveloperDetail) throws Exception {
        boolean status = false;
        OAuth20Service service = XeroVisitApi.createXeroService(xeroDeveloperDetail);
        if(service!=null){
            String test_url = "https://api.xero.com/connections";
            OAuthRequest request = new OAuthRequest(Verb.GET, test_url);
            service.signRequest(access_token,request);
            Response response = service.execute(request);
            System.out.println(response.getCode());
            if(response.getCode()==200){
                status=true;
            }
        }
        return status;
    }

    public static OAuth2AccessToken applyXeroToken(String code,XeroDeveloperDetail xeroDeveloperDetail) throws Exception {
        OAuth2AccessToken access_token = null;
        OAuth20Service service = XeroVisitApi.createXeroService(xeroDeveloperDetail);
        if(service!=null){
            access_token = service.getAccessToken(code);
        }
        return access_token;
    }

    //根据refresh token刷新access token
    public static OAuth2AccessToken refreshXeroToken(String refresh_token, XeroDeveloperDetail xeroDeveloperDetail){
        OAuth2AccessToken access_token = null;
        OAuth20Service service = XeroVisitApi.createXeroService(xeroDeveloperDetail);
        if(service!=null){
            try {
//                String refresh_url = "https://identity.xero.com/connect/token";
//                OAuthRequest request = new OAuthRequest(Verb.POST, refresh_url);
//                String authorizationStr = xeroDeveloperDetail.getConsumer_key()+":"+xeroDeveloperDetail.getConsumer_secret();
//                String authorization = "Basic "+ Base64.encodeBase64String(authorizationStr.getBytes("UTF-8"));
//                request.addHeader("Authorization",authorization);
//                request.addHeader("Content-Type","application/x-www-form-urlencoded");
//                request.addBodyParameter("grant_type","refresh_token");
//                request.addBodyParameter("refresh_token",refresh_token);
//                Response response = service.execute(request);
                access_token = service.refreshAccessToken(refresh_token);
                System.out.println();
            } catch (IOException e) {
                logger.error(e.getMessage());
            } catch (InterruptedException e) {
                logger.error(e.getMessage());
            } catch (ExecutionException e) {
                logger.error(e.getMessage());
            }
        }
        return access_token;
    }


}
