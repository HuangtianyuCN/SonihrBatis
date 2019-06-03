package com.sonihr.batis.session;/*
@author 黄大宁Rhinos
@date 2019/5/31 - 14:13
**/

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Closeable接口和try-with-resource结构相关。
 * 当采用try-with-resource时，try块执行完毕后会调用close方法。
 *  try-with-resource块如果有多个资源，释放顺序与创建相反
 *  如果try块发生异常，会先依次colse，然后再catch，最后finally
 *  try-with-resource无需显式调用资源释放
 * */
public interface SqlSession extends Closeable {
    <T> T getMapper(Class<T> interfaceClass);
    Connection getConnection() throws SQLException;
    <T> T selectOne(Object[] args) throws Exception;
    void close();
}
