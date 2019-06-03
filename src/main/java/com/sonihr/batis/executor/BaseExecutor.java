package com.sonihr.batis.executor;/*
@author 黄大宁Rhinos
@date 2019/6/3 - 11:08
**/

import com.sonihr.batis.executor.resultset.ResultSetHandler;
import com.sonihr.batis.session.Configuration;
import lombok.Data;


import java.sql.Connection;
import java.util.List;

@Data
public abstract class BaseExecutor implements Executor {
    private Configuration configuration;

    @Override
    public <E> List<E> query(Connection connection,Configuration conf, Object[] args, ResultSetHandler resultSetHandler) throws Exception {
        List<E> list = queryFromDatabase(connection,conf,args, resultSetHandler);
        return list;
    }

    public <E> List<E> queryFromDatabase(Connection connection,Configuration conf, Object[] args, ResultSetHandler resultSetHandler) throws Exception {
        return doQuery(connection,conf,args, resultSetHandler);
    }

    protected abstract <E> List<E> doQuery(Connection connection,Configuration conf, Object[] args, ResultSetHandler resultSetHandler) throws Exception;

}
