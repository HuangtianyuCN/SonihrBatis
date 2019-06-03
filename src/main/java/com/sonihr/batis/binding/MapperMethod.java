package com.sonihr.batis.binding;/*
@author 黄大宁Rhinos
@date 2019/6/3 - 14:35
**/


import com.sonihr.batis.session.SqlSession;

import java.lang.reflect.Method;
import java.sql.SQLException;

public class MapperMethod {
    private Method method;

    public MapperMethod(Method method) {
        this.method = method;
    }

    /**
     * 仅执行查询语句,在Mybatis中对这里的method进行了进一步包装
     * 比如说返回值数量，是否为游标，是否为空，然后调用不同的方法
     * 为了简单起见，本文仅仅用于查询单一结果。
     */
    public Object execute(SqlSession sqlSession,Object[] args) throws Exception {
        Object result;
        result = sqlSession.selectOne(args);
        return result;
    }
}
