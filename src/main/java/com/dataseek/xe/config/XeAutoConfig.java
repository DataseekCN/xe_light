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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(XeProperties.class)
public class XeAutoConfig {
    @Autowired
    private XeProperties xeProperties;

    public final static String DEFAULT_TX = "defaultTx";

    @Bean(name = "dataSource")
    @Qualifier("dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name="xeDataSource",initMethod = "init",destroyMethod = "close")
    public DruidDataSource initBasicDataSource(){
        DruidDataSource basicDataSource = new DruidDataSource();
        basicDataSource.setDriverClassName(xeProperties.getDriver());
        basicDataSource.setUrl(xeProperties.getJdbc_url());
        basicDataSource.setUsername(xeProperties.getDb_username());
        basicDataSource.setPassword(xeProperties.getDb_password());
        basicDataSource.setInitialSize(xeProperties.getInitialSize());
        basicDataSource.setMaxActive(xeProperties.getMaxActive());
        basicDataSource.setMinIdle(xeProperties.getMinIdle());
        basicDataSource.setValidationQuery("select now() ");
        basicDataSource.setTestOnBorrow(true);
        basicDataSource.setDefaultAutoCommit(true);
        return basicDataSource;
    }

    @Bean(name="xeJdbcTemplate")
    public JdbcTemplate initJdbcTemplate(@Qualifier("xeDataSource")DruidDataSource xeDataSource){
        JdbcTemplate xeJdbcTemplate = new JdbcTemplate();
        xeJdbcTemplate.setDataSource(xeDataSource);
        return xeJdbcTemplate;
    }

    @Bean(name = "jdbcSupportBase")
    @Qualifier("jdbcSupportBase")
    public JdbcSupport jdbcSupport(@Qualifier("dataSource") DataSource dataSource) {
        return new JdbcSupport(dataSource);
    }

    @Bean(name=XeAutoConfig.DEFAULT_TX)
    public DataSourceTransactionManager transactionManager(@Qualifier("xeDataSource")DruidDataSource xeDataSource) {
        final DataSourceTransactionManager txManager = new DataSourceTransactionManager();
        txManager.setDataSource(xeDataSource);
        return txManager;
    }
}
