package com.sonihr.batis.executor.statement;/*
@author 黄大宁Rhinos
@date 2019/6/3 - 15:39
**/

import com.sonihr.batis.executor.parameter.ParameterHandler;
import com.sonihr.batis.executor.resultset.ResultSetHandler;
import com.sonihr.batis.session.Configuration;
import lombok.Data;

@Data
public abstract class BaseStatementHandler implements StatementHandler {
    protected Configuration configuration;
    protected ResultSetHandler resultSetHandler;
    protected ParameterHandler parameterHandler;
    protected Object[] args;

    public BaseStatementHandler(Configuration conf,Object[] args) {
        this.configuration = conf;
        this.args = args;
        this.resultSetHandler = conf.newResultHandler();
        this.parameterHandler = conf.newParameterHandler(args);
    }

}
