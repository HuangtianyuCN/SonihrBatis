package com.sonihr.batis.session;/*
@author 黄大宁Rhinos
@date 2019/5/31 - 14:14
**/


import com.sonihr.batis.binding.MapperRegistry;
import lombok.Data;


@Data
public class Configuration {
    private MapperRegistry mapperRegistry;
    private String mapperInterfaceName;
    private String resultType;
    private String sql;
    private Class mapperInterface;
    private Class resultClass;
    private String dataSourceName;

    public Configuration() throws ClassNotFoundException {
        mapperInterfaceName = "batisDemo.dao.DepartmentMapper";
        sql = "select id,dept_name departmentName from t_dept where id = ?";
        resultType = "batisDemo.bean.Department";
        mapperInterface = Class.forName(mapperInterfaceName);
        resultClass = Class.forName(resultType);
        mapperRegistry = new MapperRegistry(this);
        dataSourceName = "org.apache.commons.dbcp2.BasicDataSource";
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return this.mapperRegistry.getMapper(type, sqlSession);
    }


}
