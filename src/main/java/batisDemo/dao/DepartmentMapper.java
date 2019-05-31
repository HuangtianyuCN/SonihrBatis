package batisDemo.dao;/*
@author 黄大宁Rhinos
@date 2019/2/25 - 15:32
**/


import batisDemo.bean.Department;

public interface DepartmentMapper {
    public Department getDeptById(Integer id);
    public Department getDeptByIdStep(Integer id);

}
