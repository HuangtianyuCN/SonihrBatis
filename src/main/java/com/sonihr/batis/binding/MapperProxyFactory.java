package com.sonihr.batis.binding;/*
@author 黄大宁Rhinos
@date 2019/5/31 - 15:00
**/

import com.sonihr.batis.session.Configuration;
import com.sonihr.batis.session.SqlSession;

import java.lang.reflect.Proxy;

public class MapperProxyFactory<T> {
    private Configuration configuration;

    public MapperProxyFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    private T newInstance(MapperProxy<T> mapperProxy) {
        return (T) Proxy.newProxyInstance(configuration.getMapperInterface().getClassLoader(), new Class[]{this.configuration.getMapperInterface()}, mapperProxy);
    }

    public T newInstance(SqlSession sqlSession){
        MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession,configuration.getSql());
        return newInstance(mapperProxy);
    }
}
