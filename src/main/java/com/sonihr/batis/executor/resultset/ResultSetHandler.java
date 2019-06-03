package com.sonihr.batis.executor.resultset;/*
@author 黄大宁Rhinos
@date 2019/6/3 - 15:35
**/

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface ResultSetHandler {
    <E> List<E> handleResultSets(Statement statement) throws SQLException, IllegalAccessException, InstantiationException, Exception;
}
