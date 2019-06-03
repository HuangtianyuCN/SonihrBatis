package com.sonihr.batis.session.defaults;/*
@author 黄大宁Rhinos
@date 2019/5/31 - 14:33
**/

import com.sonihr.batis.datasource.DefaultDataSourceFactory;
import com.sonihr.batis.executor.Executor;
import com.sonihr.batis.executor.SimpleExecutor;
import com.sonihr.batis.executor.resultset.DefaultResultHandler;
import com.sonihr.batis.session.Configuration;
import com.sonihr.batis.session.SqlSession;
import lombok.Data;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Data
public class DefaultSqlSession implements SqlSession {
    private Configuration configuration;
    private DataSource dataSource;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration) throws SQLException {
        this.configuration = configuration;
        this.dataSource = new DefaultDataSourceFactory().getDataSource(configuration.getDataSourceName());
        this.executor = new SimpleExecutor();
    }

    public DefaultSqlSession() throws SQLException {
    }

    @Override
    public <T> T getMapper(Class<T>  interfaceType) {
        return configuration.getMapper(interfaceType,this);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }


    @Override
    public <T> T selectOne(Object[] args) throws Exception {
        List<T> list = executor.query(this.getConnection(),configuration,args,new DefaultResultHandler(this.configuration));
        //默认只获得第一条
        return list.get(0);
    }

    @Override
    public void close() {

    }
}
