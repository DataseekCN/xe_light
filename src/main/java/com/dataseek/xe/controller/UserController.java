package com.dataseek.xe.controller;


import com.alibaba.fastjson.JSONObject;
import com.dataseek.xe.entity.UserInfo;
import com.dataseek.xe.service.IUserService;
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
    public void signup(@RequestBody JSONObject json) {
        UserInfo userDto = new UserInfo();
        userDto.setEmail(json.getString("email"));
        userDto.setFirstName(json.getString("firstName"));
        userDto.setLastName(json.getString("lastName"));
        userDto.setPassword(json.getString("password"));
        userDto.setActive("N");
        userService.insertUser(userDto);
    }
}
