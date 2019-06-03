package com.sonihr.batis.quickStart;/*
@author 黄大宁Rhinos
@date 2019/5/28 - 21:37
**/

import batisDemo.bean.Department;
import batisDemo.dao.DepartmentMapper;
import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class CoreFunction {

    //测试基本的JDBC功能
    @Test
    public void testJdbc() throws SQLException {
        String sql = "select * from t_employee where id = 2";
        Connection connection = DBUtil.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            System.out.println(id);
            String email = resultSet.getString("email");
            System.out.println(email);
        }
        resultSet.close();
        statement.close();
        connection.close();
    }

    //测试动态代理实现Mapper方法，返回javaBean
    @Test
    public void testDynamicProxy() {
        Object proxy = Proxy.newProxyInstance(CoreFunction.class.getClassLoader(),
                new Class[]{DepartmentMapper.class}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String sql = "select id,dept_name departmentName from t_dept where id = " + args[0];
                        Connection connection = DBUtil.getConnection();
                        Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(sql);
                        if (resultSet.next()) {
                            int id = resultSet.getInt(1);
                            String departmentName = resultSet.getString("departmentName");
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", id);
                            map.put("departmentName", departmentName);
                            Department department = new Department();
                            BeanUtils.populate(department, map);
                            return department;
                        }
                        return null;
                    }
                });
        Department department = ((DepartmentMapper) proxy).getDeptById(1);
        System.out.println(department);
    }

    //测试线程池
    @Test
    public void testDruidPool() throws SQLException {
        String sql = "select * from t_employee where id = 2";
        DruidDataSource dataSource = DataBase.GetInstance();
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            System.out.println(id);
        }
        connection.close();
    }

    //测试已知namespace与sql方法的情况下，返回javaBean
    @Test
    public void testNameSpaceAndSql() throws SQLException, ClassNotFoundException {
        //configuration
        String className = "batisDemo.dao.DepartmentMapper";
        String sql = "select id,dept_name departmentName from t_dept where id = ?";
        String resultType = "batisDemo.bean.Department";
        //sqlSession
        DataSource dataSource = DataBase.GetInstance();
        Connection sqlSession = dataSource.getConnection();
        //executor
        Class interfaceMapper = Class.forName(className);
        Class resultClazz = Class.forName(resultType);
        Object proxy = Proxy.newProxyInstance(CoreFunction.class.getClassLoader(),
                new Class[]{interfaceMapper}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                        //StatementHandler,TODO:把#{}内内容取出来放入map中，然后把所有的#{}替换成？
                        PreparedStatement ps = sqlSession.prepareStatement(sql);
                        ps.setInt(1, (Integer) args[0]);
                        ResultSet rs = ps.executeQuery();
                        ResultSetMetaData rsmd = rs.getMetaData();
                        Object resBean = resultClazz.newInstance();
                        Field[] fields = resultClazz.getDeclaredFields();
                        Map<String,Object> map = new HashMap<>();
                        while (rs.next()){
                            for(int i=0;i<rsmd.getColumnCount();i++){
                                System.out.println(i);
                                String name = rsmd.getColumnLabel(i+1);
                                for(Field field:fields){
                                    if(field.getName().equals(name)){
                                        Object object = rs.getObject(i+1);
                                        map.put(name,object);
                                        break;
                                    }
                                }
                            }
                        }
                        BeanUtils.populate(resBean,map);
                        return resBean;
                    }
                });
        Department department = ((DepartmentMapper) proxy).getDeptById(1);
        System.out.println(department);

    }

}
