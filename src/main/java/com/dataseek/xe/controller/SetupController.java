package com.dataseek.xe.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dataseek.xe.dao.IOauthDao;
import com.dataseek.xe.dao.IUserDao;
import com.dataseek.xe.entity.*;
import com.dataseek.xe.extend.apis.XeroVisitApi;
import com.dataseek.xe.service.base.IOauthService;
import com.dataseek.xe.service.base.IUserService;
import com.dataseek.xe.util.*;
import com.github.scribejava.core.model.OAuth2AccessToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api(value ="SetupController")
@RestController
@RequestMapping("/setup")
@CrossOrigin(origins = "*", maxAge = 3600)
public class SetupController {
    protected static Logger logger = LoggerFactory.getLogger(SetupController.class);

    @Autowired
    private IOauthDao oauthDao;

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IOauthService oauthService;

    private String urlHead = "https://openapi.etsy.com/v2";
    //private String apiKey = "78qwl864ty5269f469svn6md";

    @ApiOperation(value = "verify shopname")
    @RequestMapping("/etzy/verifyshopname")
    public ResponseDto verifyshopname(@RequestBody JSONObject json) {
        ResponseDto responseDto = new ResponseDto();
        String url = urlHead + "/shops/" + json.getString("shopname");
        try {
//            //添加get方式的参数
//            URIBuilder uriBuilder = new URIBuilder(url);
//            List<NameValuePair> list = new LinkedList<>();
//            BasicNameValuePair param1 = new BasicNameValuePair("api_key", apiKey);
//            list.add(param1);
//            uriBuilder.setParameters(list);
//            //调用及返回处理
//            HttpGet httpGet = new HttpGet(uriBuilder.build());
//            CloseableHttpClient httpclient = HttpClients.createDefault();
//            HttpResponse response = httpclient.execute(httpGet);
//            HttpEntity entity = response.getEntity();
//            String body = EntityUtils.toString(entity);
//            JSONObject jsonResult = JSONObject.parseObject(body);
//            JSONArray resultAy = jsonResult.getJSONArray("results");
//            if (resultAy != null && !resultAy.isEmpty()) {
//                responseDto.setVerified(true);
//            }
//            else {
//                responseDto.setVerified(false);
//            }

            EtsyDeveloperDetail etsyDeveloperDetail = oauthDao.queryEtsyDeveloperDetail();
            Map<String, String> qryParam = new HashMap<>();
            qryParam.put("api_key", etsyDeveloperDetail.getConsumer_key());
            HttpResponse response = HttpUtils.doGet(url, null, qryParam);
            JSON respJson = HttpUtils.getJson(response);
            JSONArray resultAy = ((JSONObject)respJson).getJSONArray("results");
            if (resultAy != null && !resultAy.isEmpty()) {
                responseDto.setVerified(true);
            }
            else {
                responseDto.setVerified(false);
            }

            responseDto.setStatus(XeConsts.RESPONSE_STATUS_SUCCESS);
            return  responseDto;
        }
        catch (Exception ex) {
            logger.error(ex.toString(), ex);
            responseDto.setStatus(XeConsts.RESPONSE_STATUS_FAILURE);
            responseDto.setError_message("verify shopname error.");
            return  responseDto;
        }
    }

    @ApiOperation(value = "get accounts from xero")
    @RequestMapping("/xero/accounts")
    public ResponseDto accounts(@RequestBody JSONObject json) throws Exception {
        ResponseDto responseDto = new ResponseDto();
        String appAccount = json.getString("app_account");
        XeroTokenAdmin xeroTokenAdmin = oauthDao.queryXeroTokenAdminByAppAccount(appAccount);
        if (xeroTokenAdmin != null) {
            //取tenantId
            Map<String, String> headers = new HashMap<>();
            String accessToken = xeroTokenAdmin.getAccess_token();
            headers.put("Authorization","Bearer " + accessToken);
            headers.put("Content-Type","application/json");
            HttpResponse response = HttpUtils.doGet("https://api.xero.com/connections", headers, null);
            JSON respJson = null;
            //outh未授权
            if(response.getStatusLine().getStatusCode() == 401){
                OAuth2AccessToken token = XeroVisitApi.refreshXeroToken(xeroTokenAdmin.getRefresh_token(), AppConfig.xeroDeveloperDetail);
                //如果刷新成功
                if(token!=null) {
                    accessToken = token.getAccessToken();
                    Integer expiresIn = token.getExpiresIn();
                    Long currentMills = System.currentTimeMillis();
                    Long expireMills = currentMills + expiresIn * 1000;
                    String expire_time = DateUtils.formatDateTimeStrFromMills(expireMills);
                    String update_time = DateUtils.getDateTimeNowStr();
                    xeroTokenAdmin.setAccess_token(accessToken);
                    xeroTokenAdmin.setRefresh_token(token.getRefreshToken());
                    xeroTokenAdmin.setExpire_time(expire_time);
                    xeroTokenAdmin.setUpdate_time(update_time);
                    //更新最新access_token至数据库表
                    oauthService.updateXeroAccessToken(xeroTokenAdmin);

                    headers.put("Authorization","Bearer " + accessToken);
                    response = HttpUtils.doGet("https://api.xero.com/connections", headers, null);
                }
            }
            respJson = HttpUtils.getJson(response);
            String tenantId = ((JSONArray)respJson).getJSONObject(0).getString("tenantId");

            headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + accessToken);
            headers.put("Content-Type", "application/json");
            headers.put("Xero-tenant-id", tenantId);
            response = HttpUtils.
                    doGet("https://api.xero.com/api.xro/2.0/Accounts", headers, null);
            respJson = HttpUtils.getJson(response);
            JSONArray acctArray = ((JSONObject)respJson).getJSONArray("Accounts");
            List<AccountInfo> acctInfos = new ArrayList<>();
            for (int i = 0; i < acctArray.size(); i++) {
                JSONObject jObj = acctArray.getJSONObject(i);
                AccountInfo acctInfo = new AccountInfo();
                acctInfo.setCode(jObj.getString("Code"));
                acctInfo.setName(jObj.getString("Name"));
                acctInfos.add(acctInfo);
            }

            responseDto.setAccounts(acctInfos);

        }
        else {
            logger.info("not find access token in accounts.");
        }

        responseDto.setStatus(XeConsts.RESPONSE_STATUS_SUCCESS);
        return  responseDto;
    }

    @ApiOperation(value = "price plan submit")
    @RequestMapping("/submit")
    public ResponseDto pricePlanSubmit(@RequestBody JSONObject json) throws Exception {
        ResponseDto responseDto = new ResponseDto();
        JSONObject formOb = json.getJSONObject("form");

        PricePlanInfo pricePlan = new PricePlanInfo();
        pricePlan.setUserId(formOb.getString("userId"));
        pricePlan.setEtsyShopName(formOb.getString("etsyShopName"));
        pricePlan.setSyncFromDate(formOb.getString("syncFromDate"));
        pricePlan.setCustInfoHandle(formOb.getString("customerInfoHandle"));
        pricePlan.setXeroSaleAcct(formOb.getString("xeroSalesAccount"));
        pricePlan.setXeroExpenseAcct(formOb.getString("xeroExpenseAccount"));
        pricePlan.setXeroShipAcct(formOb.getString("xeroShippingAccount"));
        pricePlan.setListHandle(formOb.getString("listingInfoHandle"));
        pricePlan.setSubsPlan(formOb.getString("subscriptionPlan"));
        pricePlan.setBackupOpt(formOb.getString("backupOption"));
        pricePlan.setCcName(formOb.getString("ccName"));
        pricePlan.setCcEmail(formOb.getString("ccEmail"));
        pricePlan.setCcCard(formOb.getString("ccCard"));
        pricePlan.setCcExpDate(formOb.getString("ccExpDate"));
        pricePlan.setCcCsv(formOb.getString("ccCSV"));
        userDao.insertPricePlan(pricePlan);

        responseDto.setStatus(XeConsts.RESPONSE_STATUS_SUCCESS);
        return  responseDto;
    }
}
