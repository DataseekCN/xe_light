package com.dataseek.xe.dao;

import com.dataseek.xe.entity.InfoDetail;
import com.dataseek.xe.entity.PricePlanInfo;
import com.dataseek.xe.entity.UserInfo;

import java.util.List;

public interface IUserDao {

    public void insertUser(UserInfo userInfo);

    public List<UserInfo> qryUser(UserInfo userInfo);

    public void updUser(UserInfo userInfo);

    public void insertInfoDetail(InfoDetail userInfo);

    public void insertPricePlan(PricePlanInfo pricePlanInfo);

    public PricePlanInfo qryConnection(String appAccount);

}
