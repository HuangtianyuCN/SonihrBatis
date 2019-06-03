package com.sonihr.batis.executor;/*
@author 黄大宁Rhinos
@date 2019/6/3 - 11:08
**/

import com.sonihr.batis.executor.resultset.ResultSetHandler;
import com.sonihr.batis.session.Configuration;

import java.sql.Connection;
import java.util.List;

public interface Executor {
    <E> List<E> query(Connection connection, Configuration conf, Object[] args, ResultSetHandler resultSetHandler) throws Exception;
}
