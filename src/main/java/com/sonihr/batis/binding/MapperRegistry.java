package com.sonihr.batis.binding;/*
@author 黄大宁Rhinos
@date 2019/5/31 - 14:41
**/

import com.sonihr.batis.session.Configuration;
import com.sonihr.batis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;

public class MapperRegistry {
    private Configuration configuration;

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    public <T> T getMapper(Class<T> interfaceType, SqlSession sqlSession){
        MapperProxyFactory<T> mapperProxyFactory = new MapperProxyFactory<T>(configuration);
        return mapperProxyFactory.newInstance(sqlSession);
    }
}
