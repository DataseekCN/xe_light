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
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EtsyApi {
    //返回Etsy用户权限访问确认地址
    public static String requestAuthorizeUrl(EtsyDeveloperDetail etsyDeveloperDetail){
        String final_authorize_url = null;
        if(etsyDeveloperDetail!=null){
            String consumer_key = etsyDeveloperDetail.getConsumer_key();
            String consumer_secret= etsyDeveloperDetail.getConsumer_secret();
            //request token url
            String request_token_url=etsyDeveloperDetail.getRequest_token_url();
            //authorize url
            String authorize_url=etsyDeveloperDetail.getAuthorize_url();
            //access token url
            String access_token_url=etsyDeveloperDetail.getAccess_token_url();
            //callback_url
            String callback_url = etsyDeveloperDetail.getCallback_url();

            final OAuth10aService service = new ServiceBuilder(consumer_key)
                    .apiSecret(consumer_secret)
                    .callback(callback_url)
                    .build(com.github.scribejava.apis.EtsyApi.instance());
            try {
                final OAuth1RequestToken requestToken = service.getRequestToken();
                String request_token = requestToken.getToken();
                String request_secret = requestToken.getTokenSecret();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return final_authorize_url;
    }
}
