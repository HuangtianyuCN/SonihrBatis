package com.sonihr.batis.session;/*
@author 黄大宁Rhinos
@date 2019/5/31 - 14:11
**/

import java.sql.SQLException;

public interface SqlSessionFactory {
    SqlSession openSession() throws SQLException;
}
