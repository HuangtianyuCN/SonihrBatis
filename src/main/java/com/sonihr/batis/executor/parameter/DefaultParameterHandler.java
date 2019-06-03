package com.sonihr.batis.executor.parameter;/*
@author 黄大宁Rhinos
@date 2019/6/3 - 15:38
**/

import com.sonihr.batis.session.Configuration;
import com.sonihr.batis.utils.ParametersUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DefaultParameterHandler implements ParameterHandler {
    private Configuration configuration;
    private Object[] args;

    public DefaultParameterHandler(Configuration configuration,Object[] args) {
        this.configuration = configuration;
        this.args = args;
    }

    @Override
    public void setParameters(PreparedStatement ps) throws SQLException {
        String sql = configuration.getSql();
        for(int i=0;i<args.length;i++){
            ps.setObject(i+1,args[i]);
        }
    }
}
