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
import net.oauth.*;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient4.HttpClient4;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

            OAuthServiceProvider provider = new OAuthServiceProvider(request_token_url,authorize_url,access_token_url);
            OAuthConsumer consumer = new OAuthConsumer(callback_url,consumer_key,consumer_secret,provider);
            OAuthAccessor accessor = new OAuthAccessor(consumer);
            OAuthClient client = new OAuthClient(new HttpClient4());
            List<OAuth.Parameter> parameters = new ArrayList<OAuth.Parameter>();
            parameters.add(new OAuth.Parameter("oauth_callback", consumer.callbackURL));
            try {
                OAuthMessage msg = client.getRequestTokenResponse(accessor,
                        "POST", parameters);
                final_authorize_url = msg.getParameter("login_url");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OAuthException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return final_authorize_url;
    }
}
