package batisDemo.dao;/*
@author 黄大宁Rhinos
@date 2019/1/31 - 17:10
**/


import batisDemo.bean.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeMapper {
    public Employee getEmpById(Integer id);
    public void addEmp(Employee employee);
    public Boolean updateEmp(Employee employee);
    public void deleteEmpById(Integer id);
    public List<Employee> getEmpsByLastNameLike(String lastName);
}
