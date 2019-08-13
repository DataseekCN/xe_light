/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　 ┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * Module Desc:
 * User: taosm
 * DateTime: 2019-08-13 15
 */
package com.dataseek.xe.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
@EnableConfigurationProperties(XeProperties.class)
public class XeAutoConfig {
    @Autowired
    private XeProperties xeProperties;

    @Bean(name="xeDataSource")
    public DruidDataSource initBasicDataSource(){
        DruidDataSource basicDataSource = new DruidDataSource();
        basicDataSource.setDriverClassName(xeProperties.getDriver());
        basicDataSource.setUrl(xeProperties.getJdbc_url());
        basicDataSource.setUsername(xeProperties.getDb_username());
        basicDataSource.setPassword(xeProperties.getDb_password());
        basicDataSource.setInitialSize(xeProperties.getInitialSize());

        basicDataSource.setDefaultAutoCommit(false);
        return basicDataSource;
    }
}
