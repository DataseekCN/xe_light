package com.dataseek.xe.controller;


import com.alibaba.fastjson.JSONObject;
import com.dataseek.xe.entity.UserInfo;
import com.dataseek.xe.service.IUserService;
import com.dataseek.xe.util.DataUtil;
import com.dataseek.xe.util.ResponseDto;
import com.dataseek.xe.util.XeConsts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private IUserService userService;


    @RequestMapping("/signup")
    public ResponseDto signup(@RequestBody JSONObject json) {
        UserInfo userDto = new UserInfo();
        userDto.setEmail(json.getString("email"));
        userDto.setFirstName(json.getString("firstName"));
        userDto.setLastName(json.getString("lastName"));
        userDto.setPassword(json.getString("password"));
        userDto.setActive("N");
        String userId = DataUtil.getUserIdByAtomic();
        userDto.setUserId(userId);

        ResponseDto responseDto = new ResponseDto();
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
}
