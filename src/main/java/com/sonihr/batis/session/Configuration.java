package com.sonihr.batis.session;/*
@author 黄大宁Rhinos
@date 2019/5/31 - 14:14
**/

import com.sonihr.batis.binding.MapperProxy;
import com.sonihr.batis.binding.MapperRegistry;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Configuration {
    private MapperRegistry mapperRegistry;
    private String mapperInterfaceName;
    private String resultType;
    private String sql;
    private Class mapperInterface;
    private Class resultClass;

    public Configuration() throws ClassNotFoundException {
        mapperInterfaceName = "batisDemo.dao.DepartmentMapper";
        sql = "select id,dept_name departmentName from t_dept where id = #{id}";
        resultType = "batisDemo.bean.Department";
        mapperInterface = Class.forName(mapperInterfaceName);
        resultClass = Class.forName(resultType);
        mapperRegistry = new MapperRegistry(this);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return this.mapperRegistry.getMapper(type, sqlSession);
    }


}
