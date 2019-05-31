package com.sonihr.batis.session;/*
@author 黄大宁Rhinos
@date 2019/5/31 - 14:12
**/

import com.sonihr.batis.session.defaults.DefaultSqlSessionFactory;

public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build() throws ClassNotFoundException {
        return new DefaultSqlSessionFactory();
    }
}
