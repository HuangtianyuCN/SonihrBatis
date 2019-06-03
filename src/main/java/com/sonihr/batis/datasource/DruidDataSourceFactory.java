package com.sonihr.batis.datasource;/*
@author 黄大宁Rhinos
@date 2019/5/31 - 21:04
**/

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;

import javax.sql.DataSource;


@Data
public class DruidDataSourceFactory implements DataSourceFactory{
    private DruidDataSource druidDataSource;
    private String propertiesName;

    public DruidDataSourceFactory(){
        druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/testmybatis?useSSL=false&serverTimezone=UTC");// jdbc:******
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver"); // com.***.***.**.driver
        druidDataSource.setUsername("root"); // root
        druidDataSource.setPassword("08173237eerrtt"); // *****
        druidDataSource.setMaxActive(20);
        druidDataSource.setInitialSize(5);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setMinIdle(1);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        druidDataSource.setValidationQuery("select 1");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(true);
        druidDataSource.setTestOnReturn(true);
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxOpenPreparedStatements(20);
    }

    @Override
    public DataSource getDataSource() {
        return this.druidDataSource;
    }
}
