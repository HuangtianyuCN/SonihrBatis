package com.sonihr.batis.executor.parameter;/*
@author 黄大宁Rhinos
@date 2019/6/3 - 15:38
**/

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ParameterHandler {
    void setParameters(PreparedStatement ps) throws SQLException;
}
