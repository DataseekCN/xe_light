package com.dataseek.xe.vo;

import com.dataseek.xe.util.BaseVo;

public class TestUserVo extends BaseVo {
    //用户id
    private Integer id;
    //用户名
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
