package com.sonihr.batis.executor.resultset;/*
@author 黄大宁Rhinos
@date 2019/6/3 - 15:35
**/

import com.sonihr.batis.session.Configuration;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DefaultResultHandler implements ResultSetHandler{

    private Configuration configuration;

    public DefaultResultHandler(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> handleResultSets(Statement statement) throws Exception {
        List<E> list = new ArrayList<E>();
        ResultSet rs = null;
        if(statement instanceof PreparedStatement){
            PreparedStatement ps = (PreparedStatement)statement;
            rs = ps.executeQuery();
            System.out.println("preparedStatement");
        }
        else{
            String sql = this.getConfiguration().getSql();
            rs = statement.executeQuery(sql);
            System.out.println("statement");
        }
        ResultSetMetaData rsmd = rs.getMetaData();
        Class resultClazz = configuration.getResultClass();
        Object resBean = resultClazz.newInstance();
        Field[] fields = resultClazz.getDeclaredFields();
        Map<String,Object> map = new HashMap<>();
        while (rs.next()){
            for(int i=0;i<rsmd.getColumnCount();i++){
                String name = rsmd.getColumnLabel(i+1);
                for(Field field:fields){
                    if(field.getName().equals(name)){
                        Object object = rs.getObject(i+1);
                        map.put(name,object);
                        break;
                    }
                }
            }
            try {
                BeanUtils.populate(resBean,map);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            list.add((E)resBean);
        }
        return list;
    }
}
