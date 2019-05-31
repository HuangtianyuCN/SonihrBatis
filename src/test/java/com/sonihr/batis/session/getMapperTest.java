package com.sonihr.batis.session;/*
@author 黄大宁Rhinos
@date 2019/5/31 - 16:22
**/

import batisDemo.bean.Department;
import batisDemo.dao.DepartmentMapper;
import org.junit.Test;

import java.sql.SQLException;

public class getMapperTest {
    @Test
    public void getMapper() throws Exception {
        SqlSession sqlSession = new SqlSessionFactoryBuilder().build().openSession();
        DepartmentMapper departmentMapper = sqlSession.getMapper(DepartmentMapper.class);
        Department department = departmentMapper.getDeptById(1);
        System.out.println(department);
    }
}
