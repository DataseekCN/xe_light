package com.dataseek.xe.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dataseek.xe.dao.IUserDao;
import com.dataseek.xe.entity.InfoDetail;
import com.dataseek.xe.entity.UserInfo;
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

    @Autowired
    private IUserDao userDao;

    @ApiOperation(value = "sign up")
    @RequestMapping("/signup")
    public ResponseDto signup(@RequestBody JSONObject json) {
        ResponseDto responseDto = new ResponseDto();
        try {
            UserInfo userDto = new UserInfo();
            userDto.setEmail(json.getString("email"));
            userDto.setFirstName(json.getString("first_name"));
            userDto.setLastName(json.getString("last_name"));
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
                    "<a href='http://13.236.146.10:12006/user/emailverification?user_id=" +
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
        userDto.setUserId(request.getParameter("user_id"));

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

    @ApiOperation(value = "user info")
    @RequestMapping("/info")
    public ResponseDto info(@RequestBody JSONObject json) {
        ResponseDto responseDto = new ResponseDto();
        //email合法性校验
        if (!DataUtil.checkEmail(json.getString("email"))) {
            responseDto.setStatus(XeConsts.RESPONSE_STATUS_FAILURE);
            responseDto.setError_message("email format error.");
            return  responseDto;
        }

        InfoDetail infoDetail = new InfoDetail();
        infoDetail.setUserId(json.getString("user_id"));
        infoDetail.setUserName(json.getString("user_name"));
        infoDetail.setEmail(json.getString("email"));
        infoDetail.setCompanyName(json.getString("company_name"));
        infoDetail.setCountry(json.getString("country"));
        userDao.insertInfoDetail(infoDetail);

        responseDto.setStatus(XeConsts.RESPONSE_STATUS_SUCCESS);
        return  responseDto;
    }


    @ApiOperation(value = "change pwd")
    @RequestMapping("/changepassword")
    public ResponseDto changepassword(@RequestBody JSONObject json) {
        ResponseDto responseDto = new ResponseDto();
        try {
            String oldPwd = DataUtil.EncoderByMd5(json.getString("old_passwords"));
            String newPwd = DataUtil.EncoderByMd5(json.getString("new_passwords"));

            UserInfo qryDto = new UserInfo();
            qryDto.setPassword(oldPwd);
            List<UserInfo> tmpList = userService.qryUser(qryDto);
            if (tmpList == null || tmpList.isEmpty()) {
                responseDto.setStatus(XeConsts.RESPONSE_STATUS_FAILURE);
                responseDto.setError_message("user does not exist.");
                return  responseDto;
            }
            UserInfo userInfo = tmpList.get(0);
            userInfo.setPassword(newPwd);
            userService.updUser(userInfo);

            responseDto.setStatus(XeConsts.RESPONSE_STATUS_SUCCESS);
            return  responseDto;
        }
        catch (Exception ex) {
            logger.error(ex.toString(), ex);
            responseDto.setStatus(XeConsts.RESPONSE_STATUS_FAILURE);
            responseDto.setError_message("change password error.");
            return  responseDto;
        }
    }
}
