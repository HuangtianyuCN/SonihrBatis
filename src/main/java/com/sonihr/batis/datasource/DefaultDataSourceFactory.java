package com.sonihr.batis.datasource;/*
@author 黄大宁Rhinos
@date 2019/5/31 - 21:41
**/

import javax.sql.DataSource;

public class DefaultDataSourceFactory implements DataSourceFactory {
    public DataSource getDataSource(String name) {
        DataSource dataSource = null;
        if(name.equals("org.apache.commons.dbcp2.BasicDataSource")){
            dataSource = new DBCPDataSourceFactory().getDataSource();
        }
        else
            dataSource = new DruidDataSourceFactory().getDataSource();
        return dataSource;
    }

    @Override
    public DataSource getDataSource() {
        return this.getDataSource("com.alibaba.druid.pool.DruidDataSource");
    }
}
