package com.dataseek.xe.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dataseek.xe.entity.UserInfo;
import com.dataseek.xe.service.IUserService;
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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Api(value ="UserController")
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    protected static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    private String urlHead = "https://openapi.etsy.com/v2";
    private String apiKey = "78qwl864ty5269f469svn6md";

    @ApiOperation(value = "sign up")
    @RequestMapping("/signup")
    public ResponseDto signup(@RequestBody JSONObject json) {
        ResponseDto responseDto = new ResponseDto();
        try {
            UserInfo userDto = new UserInfo();
            userDto.setEmail(json.getString("email"));
            userDto.setFirstName(json.getString("firstName"));
            userDto.setLastName(json.getString("lastName"));
            //MD5加密
            String mdStr = DataUtil.EncoderByMd5(json.getString("password"));
            userDto.setPassword(mdStr);
            userDto.setActive("N");
            String userId = DataUtil.getUserIdByAtomic();
            userDto.setUserId(userId);

            //校验
            //email合法性校验
            if (!DataUtil.checkEmail(userDto.getEmail())) {
                responseDto.setStatus(XeConsts.RESPONSE_STATUS_FAILURE);
                responseDto.setError_message("email format error.");
                return  responseDto;
            }

            //email和password唯一性校验
            UserInfo qryDto = new UserInfo();
            qryDto.setEmail(userDto.getEmail());
            qryDto.setPassword(userDto.getPassword());
            List<UserInfo> tmpList = userService.qryUser(qryDto);
            if (tmpList != null && !tmpList.isEmpty()) {
                responseDto.setStatus(XeConsts.RESPONSE_STATUS_FAILURE);
                responseDto.setError_message("email and password already exists.");
                return  responseDto;
            }

            //发送激活邮件
            String emailMsg = "Welcome to register as a member, " +
                    "<a href='http://:12006/user/emailverification?userId=" +
                    userDto.getUserId() + "'>click here to activate.</a>";
            DataUtil.sendEmail(userDto.getEmail(), emailMsg);
            //入库
            userService.insertUser(userDto);

            responseDto.setStatus(XeConsts.RESPONSE_STATUS_SUCCESS);
            responseDto.setUser_id(userId);
            return responseDto;
        }
        catch (Exception ex) {
            logger.error(ex.toString(), ex);
            responseDto.setStatus(XeConsts.RESPONSE_STATUS_FAILURE);
            responseDto.setError_message("signup error.");
            return  responseDto;
        }
    }

    @ApiOperation(value = "sign in")
    @RequestMapping("/signin")
    public ResponseDto signin(@RequestBody JSONObject json) {
        ResponseDto responseDto = new ResponseDto();
        try {
            //user是否存在校验
            String mdStr = DataUtil.EncoderByMd5(json.getString("password"));
            UserInfo userDto = new UserInfo();
            userDto.setEmail(json.getString("email"));
            userDto.setPassword(mdStr);

            List<UserInfo> tmpList = userService.qryUser(userDto);
            if (tmpList == null || tmpList.isEmpty()) {
                responseDto.setStatus(XeConsts.RESPONSE_STATUS_FAILURE);
                responseDto.setError_message("user does not exist.");
                return  responseDto;
            }
            UserInfo userInfo = tmpList.get(0);

            //激活校验
            if ("N".equals(userInfo.getActive())) {
                responseDto.setStatus(XeConsts.RESPONSE_STATUS_FAILURE);
                responseDto.setError_message("user hasn't been activated.");
                return  responseDto;
            }
            //构造sessionId
            String sessionId = UUID.randomUUID().toString().replaceAll("_","");
            //更新user的sessionid
            userInfo.setSessionId(sessionId);
            //设置session的timeout
            userInfo.setExpDate(DataUtil.transDateToStr(new Date().getTime()+24*60*60*1000));
            userService.updUser(userInfo);

            responseDto.setUser_session_id(sessionId);
            responseDto.setStatus(XeConsts.RESPONSE_STATUS_SUCCESS);
            return  responseDto;
        }
        catch (Exception ex) {
            logger.error(ex.toString(), ex);
            responseDto.setStatus(XeConsts.RESPONSE_STATUS_FAILURE);
            responseDto.setError_message("signin error.");
            return  responseDto;
        }
    }

    @ApiOperation(value = "email verification")
    @RequestMapping("/emailverification")
    public ResponseDto emailverification(HttpServletRequest request) {
        ResponseDto responseDto = new ResponseDto();
        UserInfo userDto = new UserInfo();
        userDto.setUserId(request.getParameter("userId"));

        List<UserInfo> tmpList = userService.qryUser(userDto);
        if (tmpList == null || tmpList.isEmpty()) {
            responseDto.setStatus(XeConsts.RESPONSE_STATUS_FAILURE);
            responseDto.setError_message("user does not exist.");
            return  responseDto;
        }
        UserInfo userInfo = tmpList.get(0);
        userInfo.setActive("Y");
        userService.updUser(userInfo);

        responseDto.setStatus(XeConsts.RESPONSE_STATUS_SUCCESS);
        return  responseDto;
    }

    @ApiOperation(value = "verify shopname")
    @RequestMapping("/verifyshopname")
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
}
