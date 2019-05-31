package com.sonihr.batis.session.defaults;/*
@author 黄大宁Rhinos
@date 2019/5/31 - 14:25
**/

import com.sonihr.batis.session.Configuration;
import com.sonihr.batis.session.SqlSession;
import com.sonihr.batis.session.SqlSessionFactory;
import lombok.Data;

import java.sql.SQLException;

@Data
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;

    public DefaultSqlSessionFactory() throws ClassNotFoundException {
        this.configuration = new Configuration();
    }

    @Override
    public SqlSession openSession() throws SQLException {
        return new DefaultSqlSession(configuration);
    }
}
