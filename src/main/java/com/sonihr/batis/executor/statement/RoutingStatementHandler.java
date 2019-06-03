package com.sonihr.batis.executor.statement;/*
@author 黄大宁Rhinos
@date 2019/6/3 - 21:04
**/

import com.sonihr.batis.executor.parameter.ParameterHandler;
import com.sonihr.batis.executor.resultset.ResultSetHandler;
import com.sonihr.batis.session.Configuration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class RoutingStatementHandler implements StatementHandler {

    private StatementHandler delegate;
    private Configuration configuration;

    public RoutingStatementHandler(Configuration configuration,Object args[]) {
        this.configuration = configuration;
        if(judgeStatementType())
            this.delegate = new PreparedStatementHandler(configuration,args);
        else
            this.delegate = new SimpleStatementHandler(configuration,args);
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        delegate.parameterize(statement);
    }

    @Override
    public <E> List<E> query(Statement statement, ResultSetHandler resultSetHandler) throws Exception {
        return delegate.query(statement, resultSetHandler);
    }

    @Override
    public ParameterHandler getParameterHandler() {
        return delegate.getParameterHandler();
    }

    @Override
    public Statement prepare(Connection connection) throws SQLException {
        return delegate.prepare(connection);
    }

    private boolean judgeStatementType(){
        if(configuration.getSql().contains("$"))
            return false;//statement
        return true;//preparedStatement
    }
}
