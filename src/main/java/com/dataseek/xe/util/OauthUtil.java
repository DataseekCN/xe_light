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
 * DateTime: 2019-08-23 11
 */
package com.dataseek.xe.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;
//Oath工具类
public class OauthUtil {
    private static final String BASE="abcdefghijklmnopqrstuvwxyz0123456789";

    private static final String OAUTH_SIGNATURE_METHOD = "HMAC-SHA1";

    private static final String OAUTH_VERSION = "1.0";

    //生成oauth_nounce
    public static String generateOauthNounce(){
        String oauth_nounce = null;
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<18;i++){
            int number = random.nextInt(BASE.length());
            sb.append(BASE.charAt(number));
        }
        oauth_nounce=sb.toString();
        return oauth_nounce;
    }

    //生成oauth timestamp
    public static String generateTimestamp(){
        String oauth_timestamp = null;
        Date date = new Date();
        long timemills = date.getTime();
        oauth_timestamp = (timemills+"").substring(0,10);
        return oauth_timestamp;
    }

    public static String encoderStr(String str) {
        String result = "";
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /*
   * 依据传递的参数拼接baseString
   */
    public static String initBaseString(String requestUrl,String requestMethod,String consumerKey, String nonce, String signatureMethord, String version, String time) {
        StringBuffer baseString = new StringBuffer(requestMethod+"&" + encoderStr(requestUrl) + "&");
        StringBuffer paramsStr = new StringBuffer();
        paramsStr.append("oauth_consumer_key=" + consumerKey);
        paramsStr.append("&oauth_nonce=" + nonce);
        paramsStr.append("&oauth_signature_method=" + signatureMethord);
        paramsStr.append("&oauth_timestamp=" + time);
        paramsStr.append("&oauth_version=" + version);

        baseString.append(encoderStr(paramsStr.toString()));
        return baseString.toString();
    }

    /** 
      * 使用HmacSHA算法计算 
      * 
      * @param data 字符基串 
      * @param key 密钥 
      * @return 加密后的签名（长度为16的字节数组） 
      */
    public static byte[] encodeHmacSHA(byte[] data, byte[] key) {
        String method = "HmacSHA1";
        Key k = new SecretKeySpec(key, method);
        Mac mac = null;
        try {
            mac = Mac.getInstance(method);
            mac.init(k);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return mac.doFinal(data);
    }

    //生成密钥
    public static String getSignature(String requestUrl,String nonce,String consumerKey,String consumer_secret,String time,String signatureMethord,String version) {
        String signature = "";
        String requestMethod = "POST";
        String base_string = initBaseString(requestUrl,requestMethod,consumerKey, nonce, signatureMethord, version, time);
        String secret = consumer_secret + "&";
        signature = new BASE64Encoder().encode(encodeHmacSHA(base_string.getBytes(), secret.getBytes()));
        return signature;
    }


    public static void main(String[] args) {
        System.out.println("oauth_nounce:"+OauthUtil.generateOauthNounce());
        System.out.println("oauth_timestamp:"+OauthUtil.generateTimestamp());
        String request_url = "https://openapi.etsy.com/v2/oauth/request_token?scope=transactions_r%20listings_r%20shops_rw%20profile_r%20billing_r";
        String consumer_key = "78qwl864ty5269f469svn6md";
        String consumer_secret = "xcalhsuznj";
        String oauth_nonce = OauthUtil.generateOauthNounce();
        String oauth_timestamp = OauthUtil.generateTimestamp();
        String oauth_version = "1.0";
        String oauth_signature_method = "HMAC-SHA1";
        System.out.print("oauth_signature:"+OauthUtil.getSignature(request_url,oauth_nonce,consumer_key,consumer_secret,oauth_timestamp,oauth_signature_method,oauth_version));
    }
}
