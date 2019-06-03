package com.sonihr.batis.executor.statement;/*
@author 黄大宁Rhinos
@date 2019/6/3 - 15:36
**/

import com.sonihr.batis.executor.parameter.ParameterHandler;
import com.sonihr.batis.executor.resultset.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface StatementHandler {
    void parameterize(Statement statement) throws SQLException;
    <E> List<E> query(Statement statement, ResultSetHandler resultSetHandler) throws Exception;
    ParameterHandler getParameterHandler();
    Statement prepare(Connection connection) throws SQLException;
}
