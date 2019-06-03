package com.sonihr.batis.executor.statement;/*
@author 黄大宁Rhinos
@date 2019/6/3 - 15:40
**/

import com.sonihr.batis.executor.resultset.ResultSetHandler;
import com.sonihr.batis.session.Configuration;
import com.sonihr.batis.utils.ParametersUtil;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreparedStatementHandler extends BaseStatementHandler {
    public PreparedStatementHandler(Configuration conf, Object[] args) {
        super(conf, args);
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        this.getParameterHandler().setParameters((PreparedStatement)statement);
    }

    @Override
    public <E> List<E> query(Statement statement, ResultSetHandler resultSetHandler)throws Exception{
        return resultSetHandler.handleResultSets(statement);
    }

    @Override
    public Statement prepare(Connection connection) throws SQLException {
        String sql = configuration.getSql();
        Map<String,Integer> parameterMap = new HashMap<>();
        sql = ParametersUtil.preparedStatementSql(sql,parameterMap);
        configuration.setSql(sql);
        return connection.prepareStatement(sql);
    }

}
