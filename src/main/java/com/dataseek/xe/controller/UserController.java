package com.dataseek.xe.controller;


import com.alibaba.fastjson.JSONObject;
import com.dataseek.xe.entity.UserInfo;
import com.dataseek.xe.service.IUserService;
import com.dataseek.xe.util.DataUtil;
import com.dataseek.xe.util.ResponseDto;
import com.dataseek.xe.util.XeConsts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value ="UserController")
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {
    protected static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

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
            if (!DataUtil.checkEmail(userDto.getEmail())) {
                responseDto.setStatus(XeConsts.RESPONSE_STATUS_FAILURE);
                responseDto.setError_message("email format error.");
                return  responseDto;
            }
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
}
