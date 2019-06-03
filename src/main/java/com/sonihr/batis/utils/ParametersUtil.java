package com.sonihr.batis.utils;/*
@author 黄大宁Rhinos
@date 2019/5/30 - 21:43
**/

import java.util.HashMap;
import java.util.Map;

/**
 * 需求：
 * 将字符串 ：
 * insert into t_employee(last_name,gender,email) values(#{lastName},#{gender},#{email})
 * 转换成
 * insert into t_employee(last_name,gender,email) values(?,?,?)
 * 并且保存#{}内序号和字符串
 * <1,lastName><2,gender><3,email>
 * */

public class ParametersUtil {
    static String sql = "insert into t_employee(last_name,gender,email) values(#{lastName},#{gender},#{email})";
    static String otherSql = "insert into t_employee(last_name,gender,email) values(#{lastName},${gender},#{email},${age})";
    private static String convertSql(String sql,Map<String,Integer> parameterMap,Object[] args,boolean isPreparedStatement){
        String res = "";
        StringBuilder sb = new StringBuilder();
        int left = 0;
        int right = 0;
        int len = sql.length();
        int index = 1;
        for(int i=0;i<len;i++){
            if(sql.charAt(i)=='#'||sql.charAt(i)=='$'){
                left = i+2;
                i++;
                continue;
            }
            if(sql.charAt(i)=='}'){
                right = i;
                if(right > left){
                    String tmp = sql.substring(left,right);
                    if(!isPreparedStatement)
                        sb.append(args[index-1]);
                    else
                        sb.append('?');
                    parameterMap.put(tmp,index++);
                }
                continue;
            }
            if(left <= right)
                sb.append(sql.charAt(i));
        }
        return sb.toString();
    }

    public static String statementSql(String sql,Map<String,Integer> parameterMap,Object[] args){
        return convertSql(sql,parameterMap,args,false);
    }

    public static String preparedStatementSql(String sql,Map<String,Integer> parameterMap){
        return convertSql(sql,parameterMap,null,true);
    }

}
