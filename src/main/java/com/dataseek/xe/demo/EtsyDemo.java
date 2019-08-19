package com.dataseek.xe.demo;


import net.oauth.*;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient4.HttpClient4;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class EtsyDemo {
    private static final String AUTHORIZE_URL = "https://www.etsy.com/oauth/signin";
    private static final String ACCESS_TOKEN_URL = "https://openapi.etsy.com/v2/oauth/access_token";
    private static final String REQUEST_TOKEN_URL = "https://openapi.etsy.com/v2/oauth/request_token";

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException, OAuthException, URISyntaxException {
        String consumerKey="78qwl864ty5269f469svn6md";
        String consumerSecret="xcalhsuznj";
        OAuthServiceProvider provider = new OAuthServiceProvider(REQUEST_TOKEN_URL,AUTHORIZE_URL,ACCESS_TOKEN_URL);
        OAuthConsumer consumer = new OAuthConsumer("http://www.163.com",consumerKey,consumerSecret,provider);
        OAuthAccessor accessor = new OAuthAccessor(consumer);
        OAuthClient client = new OAuthClient(new HttpClient4());
        List<OAuth.Parameter> parameters = new ArrayList<OAuth.Parameter>();
        parameters.add(new OAuth.Parameter("oauth_callback", "http://www.163.com"));
        OAuthMessage msg = client.getRequestTokenResponse(accessor,
                "POST", parameters);
        String login_url = msg.getParameter("login_url");
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("oauth_callback",msg.getParameter(OAuth.OAUTH_CALLBACK)));
        String param_url = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
        System.out.println(login_url+"&"+param_url);
    }
}
