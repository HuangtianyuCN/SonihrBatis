package com.sonihr.batis.datasource;/*
@author 黄大宁Rhinos
@date 2019/5/31 - 21:04
**/

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

@Data
public class DBCPDataSourceFactory implements DataSourceFactory {
    private BasicDataSource basicDataSource;
    public DBCPDataSourceFactory(){
        basicDataSource=new BasicDataSource();
        //2.为数据源实例指定必须的属性
        basicDataSource.setUsername("root");
        basicDataSource.setPassword("08173237eerrtt");
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/testmybatis?useSSL=false&serverTimezone=UTC");
        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        //3。指定数据源的一些可选的属性
        //1)指定数据库连接池中初始化连接数的个数
        basicDataSource.setInitialSize(20);
        //2)指定最大的连接数:同一时刻同时向数据库申请的连接数
        //最大空闲数，放洪峰过后，连接池中的连接过多，
        basicDataSource.setMaxIdle(20);
        //3)指定最小连接数:数据库空闲状态下所需要保留的最小连接数
        //防止当洪峰到来时，再次申请连接引起的性能开销；
        basicDataSource.setMinIdle(10);
        //4)最长等待时间:等待数据库连接的最长时间，单位为毫秒，超出将抛出异常
        basicDataSource.setMaxWaitMillis(10*1000);
    }


    @Override
    public DataSource getDataSource() {
        return basicDataSource;
    }
}
