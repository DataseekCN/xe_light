package com.dataseek.xe.controller;

import com.alibaba.fastjson.JSONObject;
import com.dataseek.xe.entity.TestUser;
import com.dataseek.xe.service.ITestService;
import com.dataseek.xe.util.XeConsts;
import com.dataseek.xe.vo.TestUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/test")
public class TestController {
    @Autowired
    private ITestService testService;

    @RequestMapping(value="/verify_user_exist",method = RequestMethod.GET)
    private TestUserVo verifyUserExist(@RequestParam String user_name){
        TestUserVo testUserVo = new TestUserVo();
        TestUser testUser = testService.queryExistTestUser(user_name);
        if(testUser!=null){
            testUserVo.setName(testUser.getName());
            testUserVo.setId(testUser.getId());
            testUserVo.setStatus(XeConsts.RESPONSE_STATUS_FAILURE);
            testUserVo.setError_message("用户名已经存在!");
        }
        return testUserVo;
    }

    @RequestMapping(value="/etsy_authorize",method = RequestMethod.GET)
    private JSONObject etsyAuthorize(@RequestParam String account){
        JSONObject jsonObject = new JSONObject();
        //判定etsy账户是否存在绑定关系
        //判定是否已申请oauthToken

        return jsonObject;
    }
}
