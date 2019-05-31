package com.sonihr.batis.session.defaults;/*
@author 黄大宁Rhinos
@date 2019/5/31 - 14:33
**/

import com.sonihr.batis.quickStart.DataBase;
import com.sonihr.batis.session.Configuration;
import com.sonihr.batis.session.SqlSession;
import lombok.Data;

import java.sql.Connection;
import java.sql.SQLException;

@Data
public class DefaultSqlSession implements SqlSession {
    Configuration configuration;
    Connection connection;

    public DefaultSqlSession(Configuration configuration) throws SQLException {
        this.configuration = configuration;
        this.connection = DataBase.GetInstance().getConnection();
    }

    public DefaultSqlSession() throws SQLException {
    }

    @Override
    public <T> T getMapper(Class<T>  interfaceType) {
        return configuration.getMapper(interfaceType,this);
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() {

    }
}
