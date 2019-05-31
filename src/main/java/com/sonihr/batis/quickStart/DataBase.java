package com.sonihr.batis.quickStart;/*
@author 黄大宁Rhinos
@date 2019/5/28 - 22:58
**/

import com.alibaba.druid.pool.DruidDataSource;
import sun.security.jca.GetInstance;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DataBase {
    private DataBase() {
    }

    private static final DruidDataSource druidDataSource;

    static{
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

    public static DruidDataSource GetInstance(){
        return druidDataSource;
    }


}
