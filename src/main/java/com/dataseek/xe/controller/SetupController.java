package com.dataseek.xe.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dataseek.xe.dao.IOauthDao;
import com.dataseek.xe.dao.IUserDao;
import com.dataseek.xe.entity.InfoDetail;
import com.dataseek.xe.entity.UserInfo;
import com.dataseek.xe.entity.XeroTokenAdmin;
import com.dataseek.xe.service.base.IUserService;
import com.dataseek.xe.util.DataUtil;
import com.dataseek.xe.util.ResponseDto;
import com.dataseek.xe.util.XeConsts;
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

    private String urlHead = "https://openapi.etsy.com/v2";
    private String apiKey = "78qwl864ty5269f469svn6md";

    @ApiOperation(value = "verify shopname")
    @RequestMapping("/etzy/verifyshopname")
    public ResponseDto verifyshopname(@RequestBody JSONObject json) {
        ResponseDto responseDto = new ResponseDto();
        String url = urlHead + "/shops/" + json.getString("shopname");
        try {
            //添加get方式的参数
            URIBuilder uriBuilder = new URIBuilder(url);
            List<NameValuePair> list = new LinkedList<>();
            BasicNameValuePair param1 = new BasicNameValuePair("api_key", apiKey);
            list.add(param1);
            uriBuilder.setParameters(list);
            //调用及返回处理
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity);
            JSONObject jsonResult = JSONObject.parseObject(body);
            JSONArray resultAy = jsonResult.getJSONArray("results");
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
    public void accounts(@RequestBody JSONObject json) {
        String appAccount = json.getString("app_account");
        String tenantId = json.getString("tenantId");

        XeroTokenAdmin xeroTokenAdmin = oauthDao.queryXeroTokenAdminByAppAccount(appAccount);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer "+xeroTokenAdmin.getAccess_token());
        headers.put("Content-Type", "application/json");
        headers.put("Xero-tenant-id", tenantId);



    }


}
