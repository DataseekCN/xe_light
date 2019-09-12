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

import com.dataseek.xe.entity.XeroDeveloperDetail;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.oauth.OAuth20Service;

public class XeroVisitApi {
    public static OAuth20Service createXeroService(XeroDeveloperDetail xeroDeveloperDetail){
        OAuth20Service service = null;
        if(xeroDeveloperDetail!=null) {
            String consumer_key = xeroDeveloperDetail.getConsumer_key();
            String consumer_secret = xeroDeveloperDetail.getConsumer_secret();
            String callback_url = xeroDeveloperDetail.getCallback_url();
            String scope = "openid profile email accounting.transactions accounting.reports.read accounting.settings.read accounting.journals.read accounting.contacts accounting.attachments assets projects";
            service = new ServiceBuilder(consumer_key)
                    .apiSecret(consumer_secret)
                    .callback(callback_url)
                    .defaultScope(scope)
                    .build(XeroApi20.instance());
        }
        return service;
    }


}
