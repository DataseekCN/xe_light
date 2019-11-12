package com.dataseek.xe.dao.impl;

import com.dataseek.xe.config.JdbcSupport;
import com.dataseek.xe.dao.IUserDao;
import com.dataseek.xe.entity.InfoDetail;
import com.dataseek.xe.entity.PricePlanInfo;
import com.dataseek.xe.entity.UserInfo;
import com.dataseek.xe.util.DataUtil;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class UserDao implements IUserDao {

    @Resource(name="jdbcSupportBase")
    JdbcSupport jdbcSupport;

    public void insertUser(UserInfo userInfo) {
        StringBuilder sqlBd = new StringBuilder();
        sqlBd.append("insert into xe_user(first_name,last_name,email,password,active,user_id,create_date) ");
        sqlBd.append("values(:first_name,:last_name,:email,:password,:active,:user_id,sysdate())");

        MapSqlParameterSource mapParam = new MapSqlParameterSource();
        mapParam.addValue("first_name", userInfo.getFirstName());
        mapParam.addValue("last_name", userInfo.getLastName());
        mapParam.addValue("email", userInfo.getEmail());
        mapParam.addValue("password", userInfo.getPassword());
        mapParam.addValue("active", userInfo.getActive());
        mapParam.addValue("user_id", userInfo.getUserId());
        jdbcSupport.update(sqlBd.toString(), mapParam);
    }

    public List<UserInfo> qryUser(UserInfo userInfo) {
        MapSqlParameterSource mapParam = new MapSqlParameterSource();
        StringBuilder sqlBd = new StringBuilder();
        sqlBd.append("select first_name,last_name,email,password,active,user_id,create_date,upd_date,session_id,exp_date from xe_user where 1=1 ");

        if (!DataUtil.isEmpty(userInfo.getEmail())) {
            sqlBd.append("and email=:email ");
            mapParam.addValue("email", userInfo.getEmail());
        }
        if (!DataUtil.isEmpty(userInfo.getPassword())) {
            sqlBd.append("and password=:password ");
            mapParam.addValue("password", userInfo.getPassword());
        }
        if (!DataUtil.isEmpty(userInfo.getUserId())) {
            sqlBd.append("and user_id=:user_id ");
            mapParam.addValue("user_id", userInfo.getUserId());
        }
        if (!DataUtil.isEmpty(userInfo.getSessionId())) {
            sqlBd.append("and session_id=:session_id ");
            mapParam.addValue("session_id", userInfo.getSessionId());
        }

        List<UserInfo> reList = jdbcSupport.query(sqlBd.toString(), UserInfo.class, mapParam);
        return reList;
    }

    public void updUser(UserInfo userInfo) {
        StringBuilder sqlBd = new StringBuilder();
        sqlBd.append("update xe_user set ");
        sqlBd.append("first_name=:first_name,last_name=:last_name,email=:email,password=:password,active=:active, ");
        sqlBd.append("user_id=:user_id,create_date=:create_date,upd_date=sysdate(),session_id=:session_id,exp_date=:exp_date ");
        sqlBd.append("where user_id=:user_id  ");

        MapSqlParameterSource mapParam = new MapSqlParameterSource();
        mapParam.addValue("first_name", userInfo.getFirstName());
        mapParam.addValue("last_name", userInfo.getLastName());
        mapParam.addValue("email", userInfo.getEmail());
        mapParam.addValue("password", userInfo.getPassword());
        mapParam.addValue("active", userInfo.getActive());
        mapParam.addValue("user_id", userInfo.getUserId());
        mapParam.addValue("create_date", userInfo.getCreateDate());
        mapParam.addValue("session_id", userInfo.getSessionId());
        mapParam.addValue("exp_date", userInfo.getExpDate());
        jdbcSupport.update(sqlBd.toString(), mapParam);
    }

    public void insertInfoDetail(InfoDetail userInfo) {
        StringBuilder sqlBd = new StringBuilder();
        sqlBd.append("insert into info_detail(user_id,user_name,email,company_name,country) ");
        sqlBd.append("values(:user_id,:user_name,:email,:company_name,:country)");

        MapSqlParameterSource mapParam = new MapSqlParameterSource();
        mapParam.addValue("user_id", userInfo.getUserId());
        mapParam.addValue("user_name", userInfo.getUserName());
        mapParam.addValue("email", userInfo.getEmail());
        mapParam.addValue("company_name", userInfo.getCompanyName());
        mapParam.addValue("country", userInfo.getCountry());
        jdbcSupport.update(sqlBd.toString(), mapParam);
    }

    public void insertPricePlan(PricePlanInfo pricePlanInfo) {
        StringBuilder sqlBd = new StringBuilder();
        sqlBd.append("insert into price_plan_info(user_id,etsy_shop_name,sync_from_date,cust_info_handle,xero_sale_acct, ");
        sqlBd.append("xero_exps_acct,xero_ship_acct,list_handle,subs_plan,backup_opt,cc_name,cc_email,cc_card,cc_exp_date,cc_csv,connection_id) ");
        sqlBd.append("values(:user_id,:etsy_shop_name,:sync_from_date,:cust_info_handle,:xero_sale_acct,:xero_exps_acct,");
        sqlBd.append(":xero_ship_acct,:list_handle,:subs_plan,:backup_opt,:cc_name,:cc_email,:cc_card,:cc_exp_date,:cc_csv,:connection_id)");

        MapSqlParameterSource mapParam = new MapSqlParameterSource();
        mapParam.addValue("user_id", pricePlanInfo.getUserId());
        mapParam.addValue("etsy_shop_name", pricePlanInfo.getEtsyShopName());
        mapParam.addValue("sync_from_date", pricePlanInfo.getSyncFromDate());
        mapParam.addValue("cust_info_handle", pricePlanInfo.getCustInfoHandle());
        mapParam.addValue("xero_sale_acct", pricePlanInfo.getXeroSaleAcct());
        mapParam.addValue("xero_exps_acct", pricePlanInfo.getXeroExpenseAcct());
        mapParam.addValue("xero_ship_acct", pricePlanInfo.getXeroShipAcct());
        mapParam.addValue("list_handle", pricePlanInfo.getListHandle());
        mapParam.addValue("subs_plan", pricePlanInfo.getSubsPlan());
        mapParam.addValue("backup_opt", pricePlanInfo.getBackupOpt());
        mapParam.addValue("cc_name", pricePlanInfo.getCcName());
        mapParam.addValue("cc_email", pricePlanInfo.getCcEmail());
        mapParam.addValue("cc_card", pricePlanInfo.getCcCard());
        mapParam.addValue("cc_exp_date", pricePlanInfo.getCcExpDate());
        mapParam.addValue("cc_csv", pricePlanInfo.getCcCsv());
        mapParam.addValue("connection_id", pricePlanInfo.getConnectionId());

        jdbcSupport.update(sqlBd.toString(), mapParam);
    }

}
