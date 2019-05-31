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
    private Class resultClazz;

    public MapperProxy(SqlSession sqlSession, String sql, Class resultClazz) {
        this.sqlSession = sqlSession;
        this.sql = sql;
        this.resultClazz = resultClazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        PreparedStatement ps = sqlSession.getConnection().prepareStatement(sql);
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
}
