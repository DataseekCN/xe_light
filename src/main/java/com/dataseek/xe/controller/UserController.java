package com.dataseek.xe.controller;


import com.alibaba.fastjson.JSONObject;
import com.dataseek.xe.entity.UserInfo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {


    @RequestMapping("/signup")
    public void signup(@RequestBody JSONObject json) {
        UserInfo userDto = new UserInfo();
        userDto.setEmail(json.getString("email"));
        userDto.setFirstName(json.getString("first_name"));
        userDto.setLastName(json.getString("last_name"));
        userDto.setPassword(json.getString("password");














    }





}
