package com.sonihr.batis.executor;/*
@author 黄大宁Rhinos
@date 2019/6/3 - 14:50
**/

import com.sonihr.batis.executor.resultset.ResultSetHandler;
import com.sonihr.batis.executor.statement.StatementHandler;
import com.sonihr.batis.session.Configuration;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SimpleExecutor extends BaseExecutor{
    @Override
    public <E> List<E> doQuery(Connection connection,Configuration conf, Object[] args, ResultSetHandler resultSetHandler) throws Exception {
        /**
         * 1. 在Configuration中创建StatementHandler实例handler，StatementHandler中有ParameterHandler和ResultHandler组件实例
         * 2. Executor实现类调用preparedStatement方法，这个方法中的prepare方法返回Statement或者PreparedStatement
         * 3. statementHandler调用parameterize方法，调用parameterHandler组件实例处理参数
         * */
        StatementHandler handler = conf.newStatementHandler(args);
        Statement statement = preparedStatement(connection,handler);
        return handler.query(statement, resultSetHandler);
    }

    private Statement preparedStatement(Connection connection,StatementHandler handler) throws SQLException {
        Statement statement = handler.prepare(connection);
        handler.parameterize(statement);//对参数进行封装
        return statement;
    }
}
