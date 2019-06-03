package com.sonihr.batis.binding;/*
@author 黄大宁Rhinos
@date 2019/5/31 - 14:38
**/

import com.sonihr.batis.session.SqlSession;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

public class MapperProxy<T>  implements InvocationHandler {
    private SqlSession sqlSession;
    private String sql;

    public MapperProxy(SqlSession sqlSession, String sql) {
        this.sqlSession = sqlSession;
        this.sql = sql;
    }



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MapperMethod mapperMethod = new MapperMethod(method);
        return mapperMethod.execute(sqlSession,args);
    }
}
